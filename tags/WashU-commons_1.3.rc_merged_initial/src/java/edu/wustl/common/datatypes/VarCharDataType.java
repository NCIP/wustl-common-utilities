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
public class VarCharDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType#getObjectValue(java.lang.String)
	 */
	/**
	 * get varchar data type Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str) throws ParseException, IOException
	{
		// TODO Auto-generated method stub
		return str;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate varchar data type values.
	 * @param enteredValue blob data type Value.
	 * @param errors ActionErrors object.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
