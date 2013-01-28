/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.cde;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.cde.xml.XMLCDE;
import edu.wustl.common.cde.xml.XMLPermissibleValueType;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.CDEException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDECacheManager // NOPMD by shrishail_kalshetty on 8/31/09 2:42 PM
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(CDECacheManager.class);
	/**
	 * @param cdeXMLMAP Map of xmlCDEs configured by the user in the CDEConfig.xml.
	 * The refresh() method accepts the XMLCDEs map and iterates through the map.
	 *  A CDE is downloaded from the server and depending on the XMLCDEs object
	 *  configuration, it is stored in the database. An email is sent to the
	 *  administrator regarding the download status
	 * of the CDEs. The errors if generated are reported to the administrator by email.
	 * @throws CDEException throws CDEException
	 */
	public void refresh(Map cdeXMLMAP) throws CDEException // NOPMD
	{
		logger.info("Initializing CDE Cache Manager");
		CDEDownloader cdeDownloader = null;
		List downloadedCDEList = new ArrayList();
		List errorLogs = new ArrayList();
		try
		{
			cdeDownloader = new CDEDownloader();
			cdeDownloader.connect();
			logger.info("Connected to the server successfully...");
		}
		catch (Exception exp)
		{
			errorLogs.add(exp.getMessage()); //Logging the error message.
			logger.error(exp.getMessage(), exp); 	//Send the error logs to administrator.
			throw new CDEException(sendCDEDownloadStatusEmail(errorLogs));
		}
		Set xmlCDEMapKeySet = cdeXMLMAP.keySet();
		Iterator itr = xmlCDEMapKeySet.iterator();
		while (itr.hasNext())
		{
			Object key = itr.next();
			XMLCDE xmlCDE = (XMLCDE) cdeXMLMAP.get(key);
			if (xmlCDE.isCache())
			{
				try
				{
					CDE cde = cdeDownloader.downloadCDE(xmlCDE);
					logger.info(cde.getLongName() + " : CDE download successful ... ");
					configurePermissibleValues(cde, xmlCDE);
					downloadedCDEList.add(cde);
				}
				catch (Exception exp)
				{
					errorLogs.add(exp.getMessage());
					logger.error(exp.getMessage(), exp);
				}
			}
		}
		// Send the errors logs while downloading the CDEs to the administrator.
		//sendCDEDownloadStatusEmail(errorLogs);
		CDEBizLogic cdeBizLogic = new CDEBizLogic(); //Inserting the downloaded CDEs into database.
		Iterator iterator = downloadedCDEList.iterator();
		while (iterator.hasNext())
		{
			CDE cde = (CDE) iterator.next();
			try
			{
				cdeBizLogic.insert(cde, null);
				logger.debug(cde.getLongName() + " : CDE inserted in database ... ");
			}
			catch (BizLogicException bizLogicExp)
			{
				errorLogs.add(bizLogicExp.getMessage());
				logger.error(bizLogicExp.getMessage(), bizLogicExp);
			}
		}
	}
	/**
	 * Sends an email containing the error logs occured while downloading the CDEs to the administrator.
	 * @param errorLogs The list of errors.
	 * @return String value.
	 */
	private String sendCDEDownloadStatusEmail(List errorLogs)
	{
		StringBuffer body = new StringBuffer();
		if (!errorLogs.isEmpty())
		{
			body.append(TextConstants.TWO_LINE_BREAK).append("Dear Administrator,")
			.append(TextConstants.TWO_LINE_BREAK)
			.append(ApplicationProperties.getValue("email.cdeDownload.body.start"))
			.append(TextConstants.TWO_LINE_BREAK);

		Iterator<String> iterator = errorLogs.iterator();
		while (iterator.hasNext())
		{
			body.append(iterator.next())
					.append(TextConstants.TWO_LINE_BREAK);
		}

		body.append(ApplicationProperties.getValue("email.catissuecore.team"));
		}
		return body.toString();
	}
	/**
	 * Sets the parent permissible values for each of the permissible value of the CDE.
	 * depending on the parent-child relationships present in the XMlCDE provided.
	 * @param cde The CDE whose permissible values are to be configured.
	 * @param xmlCDE The XMLCDE object for the cde which contains the parent-child
	 *  relationships between the
	 * 				 permissible values.
	 */
	private void configurePermissibleValues(CDE cde, XMLCDE xmlCDE) // NOPMD by shrishail_kalshetty on 8/31/09 2:42 PM
	{
		Set configuredPermissibleValues = new HashSet();
		Set permissibleValues = cde.getPermissibleValues();
		Iterator iterator = xmlCDE.getXMLPermissibleValues().iterator();
		while (iterator.hasNext())
		{
			XMLPermissibleValueType xmlPermissibleValueType = (XMLPermissibleValueType)iterator.next();
			PermissibleValueImpl permissibleValue = (PermissibleValueImpl) getPermissibleValueObject(
					permissibleValues, xmlPermissibleValueType.getConceptCode());
			if (permissibleValue != null)
			{
				// If the parent permissible value concept code is null
				// set the cde value for the permissible value.
				if (xmlPermissibleValueType.getParentConceptCode() == null
						|| xmlPermissibleValueType.getParentConceptCode().equals(""))
				{
					permissibleValue.setCde(cde);
					permissibleValue.setParentPermissibleValue(null);
				}
				else //If the parent permissible value concept code is not null,
					//set the parent permissible value and set the cde as null.
				{
					PermissibleValue parentPermissibleValue = getPermissibleValueObject(
						permissibleValues, xmlPermissibleValueType.getParentConceptCode());
					//Set the parent permissible value of this permissible value.
					permissibleValue.setParentPermissibleValue(parentPermissibleValue);
					permissibleValue.setCde(null);
				}
				configuredPermissibleValues.add(permissibleValue);
			}
		}
		//Get the permissible values from the set whose relationship is not present in the xml file.
		permissibleValues.removeAll(configuredPermissibleValues);
		//Put these permissible values under the cde in the tree structure.
		Iterator itr = permissibleValues.iterator();
		while (itr.hasNext())
		{
			PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) itr.next();
			permissibleValueImpl.setCde(cde);
			permissibleValueImpl.setParentPermissibleValue(null);
		}
		if (!permissibleValues.isEmpty())
		{
			configuredPermissibleValues.addAll(permissibleValues);
		}
		CDEImpl cdeImpl = (CDEImpl) cde; //Set the configured permissible value set to the cde.
		cdeImpl.setPermissibleValues(configuredPermissibleValues);
	}
	/**
	 * Returns the permissible value object for the concept code from the Set of permissible values.
	 * @param permissibleValues The Set of permissible values.
	 * @param conceptCode The conceptCode whose permissible value object is required.
	 * @return the permissible value object for the concept code from the Set of permissible values.
	 */
	private PermissibleValue getPermissibleValueObject(Set permissibleValues, String conceptCode)
	{
		PermissibleValue permissibleValue = null ;
		Iterator iterator = permissibleValues.iterator();
		while (iterator.hasNext())
		{
			permissibleValue = (PermissibleValue) iterator.next();
			if (permissibleValue.getValue().equals(conceptCode))
			{
				//return permissibleValue;
				break ;
			}
		}
		return permissibleValue;
	}
}