/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.privilege;

import edu.wustl.security.global.Constants;

/**
 * Enumeration that will contains different types of class level privileges.
 * @author prafull_kadam
 *
 */
public enum PrivilegeType
{
	/**
	 *
	 */
	ObjectLevel(Constants.OBJECT_LEVEL_SECURE_RETRIEVE),
	/**
	 *
	 */
	InsecureLevel(Constants.INSECURE_RETRIEVE),
	/**
	 *
	 */
	ClassLevel(Constants.CLASS_LEVEL_SECURE_RETRIEVE);

	/**
	 * value.
	 */
	private int value;

	/**
	 * @param val The value.
	 */
	PrivilegeType(int val)
	{
		this.value = val;
	}

	/**
	 * To get the PrivilegeType for the given value.
	 * @param value The value
	 * @return The PrivilegeType for the given value
	 */
	public static PrivilegeType getPrivilegeType(int value)
	{
		PrivilegeType type;
		switch (value)
		{
			case Constants.OBJECT_LEVEL_SECURE_RETRIEVE :
				type = ObjectLevel;
				break;
			case Constants.CLASS_LEVEL_SECURE_RETRIEVE :
				type = ClassLevel;
				break;
			case Constants.INSECURE_RETRIEVE :
			default :
				type = InsecureLevel;
				break;
		}
		return type;
	}

}
