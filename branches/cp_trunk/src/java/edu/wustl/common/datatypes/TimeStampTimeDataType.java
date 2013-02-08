/**
 *
 */
package edu.wustl.common.datatypes;

import java.io.IOException;
import java.text.ParseException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;


/**
 * @author prashant_bandal
 *
 */
public class TimeStampTimeDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate timestamptime value.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		Validator validator = new Validator();
		boolean conditionError = false;
		if (!(validator.isValidTime(enteredValue, CommonServiceLocator.getInstance().getTimePattern())))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.time.format"));
			conditionError = true;
		}
		return conditionError;
	}

	/**
	 * get timestamptime Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
