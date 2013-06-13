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

import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author prashant_bandal
 *
 */
public class NumericDataType implements IDBDataType
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(NumericDataType.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.ValidatorDataTypeInterface#
	 * validate(java.lang.String, java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate numeric values.
	 * @param enteredValue numeric Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		Validator validator = new Validator();
		boolean condonError = false;
		LOGGER.debug(" Check for integer");
		if (validator.convertToLong(enteredValue) == null)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.intvalue.required"));
			condonError = true;
			LOGGER.debug(enteredValue + " is not a valid integer");
		}
		else if (!validator.isPositiveNumeric(enteredValue, 0))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"simpleQuery.intvalue.poisitive.required"));
			condonError = true;
			LOGGER.debug(enteredValue + " is not a positive integer");
		}
		return condonError;
	}

	/**
	 * get numeric Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IO Exception
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		return Integer.valueOf(str);
	}

}
