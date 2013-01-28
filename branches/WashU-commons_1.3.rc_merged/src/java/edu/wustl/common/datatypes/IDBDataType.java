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
public interface IDBDataType
{
	/**
	 * This method validate various data types.
	 * @param enteredValue entered Value
	 * @param errors errors
	 * @return conditionError.
	 */
	boolean validate(String enteredValue, ActionErrors errors);

	/**
	 * get Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	Object getObjectValue(String str) throws ParseException, IOException;
}
