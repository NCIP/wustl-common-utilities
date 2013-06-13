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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.global.Constants;

/**
 * @author prashant_bandal
 *
 */
public class TinyIntDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.ValidatorDataTypeInterface#
	 * validate(java.lang.String, java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate TinyInt values.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{

		boolean condnError = false;
		if (!Constants.BOOLEAN_YES.equals(enteredValue.trim())
				&& !Constants.BOOLEAN_NO.equals(enteredValue.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.tinyint.format"));
			condnError = true;
		}
		return condnError;
	}

	/**
	 * get tinyint Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		return null;
	}

}
