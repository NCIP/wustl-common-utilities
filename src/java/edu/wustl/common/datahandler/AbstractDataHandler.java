
package edu.wustl.common.datahandler;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author ravindra_jain
 * @version 1.0
 * @created 21-Apr-2009 6:57:50 PM
 */
public abstract class AbstractDataHandler
{
	/**
	 * Name of File.
	 */
	protected String fileName;
	/**
	 *
	 * @throws IOException throw IOException
	 */
	public abstract void openFile() throws IOException;
	/**
	 *
	 * @param values List of object.
	 * @throws IOException throw IOException
	 */
	public abstract void appendData(List<Object> values) throws IOException;
	/**
	 *
	 * @param values String values.
	 */
	public abstract void appendData(String values);
	/**
	 *
	 * @throws IOException throw IOException
	 */
	public abstract void closeFile() throws IOException;
	/**
	 *
	 * @throws IOException throw IOException
	 */
	public abstract void flush() throws IOException;
}
