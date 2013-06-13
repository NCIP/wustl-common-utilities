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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

import org.apache.struts.action.ActionErrors;
import org.hibernate.Hibernate;


/**
 * @author prashant_bandal
 *
 */
public class BlobDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String,
	 * org.apache.struts.action
	 * .ActionErrors)
	 */
	/**
	 * This method validate blob data type values.
	 * @param enteredValue blob data type Value.
	 * @param errors ActionErrors object.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		return false;
	}

	/**
	 * get blob data type Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		File file = new File(str);
		DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(file)));
		byte[] buff = new byte[(int) file.length()];
		dis.readFully(buff);
		dis.close();
		return Hibernate.createBlob(buff);
	}

}
