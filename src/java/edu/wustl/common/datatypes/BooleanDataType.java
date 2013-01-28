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
public class BooleanDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String,
	 * org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate boolean data type values.
	 * @param enteredValue boolean data type Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		return false;
	}

	/**
	 * get boolean data type Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		return Boolean.valueOf(str);
	}

}
