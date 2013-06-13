/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.common.datatypes;

import java.io.IOException;
import java.text.ParseException;

import org.apache.struts.action.ActionErrors;


/**
 * @author prashant_bandal
 *
 */
public class LongDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String,
	 * org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate long data type values.
	 * @param enteredValue entered Value as long.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		return false;
	}

	/**
	 * get long data type Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		return Long.valueOf(str);
	}

}
