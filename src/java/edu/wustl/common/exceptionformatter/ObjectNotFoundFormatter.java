/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.exceptionformatter;

import java.text.MessageFormat;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;

/**
 * Description: Object Not Found Formatter.
 *
 */
public class ObjectNotFoundFormatter implements ExceptionFormatter
{
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ObjectNotFoundFormatter.class);
	/**
	 * This method format Message.
	 * @param objExcp Exception
	 * @return formatted Error Message.
	 */
	public String formatMessage(Exception objExcp) // NOPMD
	{
		String formattedErrMsg = null;
		String temp1 = "exists: ";
		String temp2 = "class: ";
		String temp3 = "given ";
		int startIndex = -1;
		int endIndex = -1;
		int tempIndex = -1;
		JDBCDAO jdbcDAO=null;
		try
		{
			String appName = CommonServiceLocator.getInstance().getAppName();
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
			jdbcDAO = daoFactory.getJDBCDAO();
			jdbcDAO.openSession(null);
			// get Message from exception object
			String message = objExcp.getMessage();

			// get column name from message
			startIndex = message.indexOf(temp1) - 1;
			tempIndex = message.indexOf(temp3) + temp3.length();
			String columnName = message.substring(tempIndex, startIndex);
			LOGGER.debug(columnName + "--" + columnName.length());

			// get column value on from message for which object was not found.
			startIndex = message.indexOf(temp1) + temp1.length();
			endIndex = message.indexOf(',', startIndex);
			String value = message.substring(startIndex, endIndex);
			LOGGER.debug(value + "  " + value.length());

			// get class name from message
			startIndex = message.indexOf(temp2) + temp2.length();
			String className = message.substring(startIndex);
			LOGGER.debug(className + "--" + className.length());
			Class classObj = Class.forName(className);
			// get table name from class
			String displayName ;
			HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
			.getHibernateMetaData(appName);
			if (hibernateMetaData != null)
			{
				displayName = ExceptionFormatterFactory.getDisplayName(hibernateMetaData
						.getTableName(classObj), jdbcDAO);
			}
			else
			{
				displayName = "";
			}
			
			Object[] arguments = new Object[]{displayName, columnName, value};
			formattedErrMsg = MessageFormat.format(Constants.OBJECT_NOT_FOUND_ERROR, arguments);
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage(), e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;
		}
		return formattedErrMsg;
	}
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args)
	{
		String formattedErrMsg = null;
		String temp1 = "exists: ";
		String temp2 = "class: ";
		String temp3 = "given ";
		int startIndex = -1;
		int endIndex = -1;
		int tempIndex = -1;
		try
		{
			// get Message from exception object
			String message = "No row with the given identifier exists: 123," +
					" of class: edu.wustl.catissuecore.domain.StorageContainer";

			// get column name from message
			startIndex = message.indexOf(temp1) - 1;
			tempIndex = message.indexOf(temp3) + temp3.length();
			String columnName = message.substring(tempIndex, startIndex);

			// get column value on from message for which object was not found.
			startIndex = message.indexOf(temp1) + temp1.length();
			endIndex = message.indexOf(',', startIndex);
			String value = message.substring(startIndex, endIndex);

			// get class name from message
			startIndex = message.indexOf(temp2) + temp2.length();
			// get table name from class
			String displayName = "dispname";

			Object[] arguments = new Object[]{displayName, columnName, value};

			formattedErrMsg = MessageFormat.format(Constants.OBJECT_NOT_FOUND_ERROR, arguments);
			LOGGER.debug(formattedErrMsg);
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage(), e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;
		}
	}
}
