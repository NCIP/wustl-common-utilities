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

import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;

/**
 * @author prashant_bandal
 *
 */
public class DateDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.ValidatorDataTypeInterface#
	 * validate(java.lang.String, java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate Date value.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		Validator validator = new Validator();
		boolean isConError = false;
		if (!(validator.checkDate(enteredValue)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.date.format"));
			isConError = true;
		}
		return isConError;
	}

	/**
	 * get Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str) throws ParseException, IOException
	{
		return Utility.parseDate(str, Utility.datePattern(str));
	}

}
