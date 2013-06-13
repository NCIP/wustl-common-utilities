/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>Title: KeyComparator Class</p>
 * <p>Description:  This class is base class for comparing two keys.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author abhishek_mehta
 * @version 1.00
 * Created on July 26, 2007
 */
public class KeyComparator implements Comparator<Object> , Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -5979206033335764493L;

	/**
	 * This method compare between two objects passed as parameters.
	 * @param object1 Object object to be compared.
	 * @param object2 Object object to be compared.
	 * @return integer value based on the output of comparison.
	 */
	public int compare(Object object1, Object object2)
	{
		String key1 = (String) object1;
		String key2 = (String) object2;
		int outer1 = getIntValue( key1);
		int outer2 = getIntValue(key2);
		int retValue=outer1 - outer2;
		if (retValue == 0)
		{
			String innerKey1 = key1.substring(key1.indexOf('_') + 1);

			int inner1 = getIntValue(innerKey1);
			String innerKey2 = key2.substring(key2.indexOf('_') + 1);
			int inner2 = getIntValue(innerKey2);
			retValue = inner1 - inner2;
		}
		return retValue;
	}

	/**
	 * @param key String value to compare.
	 * @return int integer value after ':'
	 */
	private int getIntValue(String key)
	{
		int val;
		int index = key.indexOf('_');
		if (index == -1)
		{
			val=0;
		}
		else
		{
			val = getIntValueInKey(key, index);
		}
		return val;
	}

	/**
	 * This method return the integer for a substring.
	 * @param key String key.
	 * @param index int index.
	 * @return int integer value after ':' chanracter.
	 */
	private int getIntValueInKey(String key, int index)
	{
		String outerKey1 = key.substring(0, index);
		return Integer.parseInt(outerKey1.substring(outerKey1.indexOf(':') + 1));
	}
}
