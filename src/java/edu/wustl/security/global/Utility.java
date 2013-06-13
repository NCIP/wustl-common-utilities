/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.global;

import java.util.Map;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeType;

/**
 * @author deepti_shelar
 *
 */
public final class Utility
{

	/**
	 * logger -Generic Logger.
	 */

	private static org.apache.log4j.Logger logger = Logger.getLogger(Utility.class);
	/**
	 * creates a single obj.
	 */
	private static Utility util = new Utility();;
	/**
	 * Private constructor.
	 */
	private Utility()
	{

	}
	/**
	 * returns the single obj.
	 * @return Utility obj
	 */
	public static Utility getInstance()
	{
		return util;
	}

	/**
	 * TO get the PrivilegeType of an Entity.
	 * @param tagKeyValueMap The reference to Entity.
	 * @return appropriate PrivilegeType of the given Entity.
	 */
	public PrivilegeType getPrivilegeType(final Map<String, String> tagKeyValueMap)
	{
		PrivilegeType pType = PrivilegeType.ClassLevel;
		if (tagKeyValueMap.containsKey(Constants.PRIVILEGE_TAG_NAME))
		{
			String tagVal = tagKeyValueMap.get(Constants.PRIVILEGE_TAG_NAME);
			pType = PrivilegeType.getPrivilegeType(Integer.parseInt(tagVal));
		}
		return pType;
	}

	/**
	 *
	 * @param tagKeyValueMap map
	 * @return boolean whether attribute has a taggedvalue as birthdate
	 */
	public boolean getIsBirthDate(final Map<String, String> tagKeyValueMap)
	{
		boolean isBirthDate = false;
		if (tagKeyValueMap.containsKey(edu.wustl.security.global.Constants.BDATE_TAG_NAME))
		{
			String tagValue = tagKeyValueMap
					.get(edu.wustl.security.global.Constants.BDATE_TAG_NAME);
			if (tagValue.equalsIgnoreCase(Boolean.valueOf(true).toString()))
			{
				isBirthDate = true;
			}
		}
		return isBirthDate;
	}

	/**
	 *
	 * Called when we need to throw SMException.
	 * @param exc exception
	 * @param mess message to be shown on error
	 * @param errKey TODO
	 * @throws SMException exception
	 */
	public void throwSMException(Exception exc, String mess, String errKey) throws SMException
	{
		logger.error(mess, exc);
		ErrorKey errorKey = ErrorKey.getErrorKey(errKey);
		if(errorKey == null)
		{
			errorKey = ErrorKey.getErrorKey("sm.operation.error");
			//errorKey.setErrorMessage(mess);
		}
		throw new SMException(errorKey, exc, mess);
	}
}
