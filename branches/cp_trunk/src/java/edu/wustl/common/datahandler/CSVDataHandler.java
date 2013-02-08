
package edu.wustl.common.datahandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.wustl.common.util.global.Constants;

/**
 *
 * @author ravindra_jain
 * @version 1.0
 * @created 21-Apr-2009 6:57:50 PM
 */
public class CSVDataHandler extends AbstractDataHandler
{
	/**
	 * BufferedWriter object.
	 */
	private BufferedWriter bufferedWriter;
	/**
	 * Buffer Size.
	 */
	private Integer bufferSize;
	/**
	 * Count Number of Rows.
	 */
	private static int rowCount;
	/**
	 * Use the separator.
	 */
	private String delimiter;
	/**
	 * PRIVATE CONSTRUCTOR.
	 * @param fileName Name of the File
	 * @param bufferSize Size of the Buffer
	 * @param delimiter delimiter
	 */
	CSVDataHandler(String fileName, Integer bufferSize, String delimiter) // NOPMD
	{
		super() ;
		if(bufferSize == null)
		{
			/**
			 *  Default value.
			 */
			bufferSize = 100;
		}
		if (bufferSize <= 0)
		{
			throw new IllegalArgumentException("BufferSize (Row Count) should be > 0");
		}
		this.fileName = fileName;
		this.bufferSize = bufferSize;
		if(delimiter == null)
		{
			delimiter = Constants.DELIMETER;
		}
		this.delimiter = delimiter;
	}
	/**
	 * @override.
	 * @throws IOException throw IOException
	 */
	public void openFile() throws IOException
	{
		bufferedWriter = new BufferedWriter(new FileWriter(fileName));
	}
	/**
	 *
	 * @param values String data to append
	 */
	public void appendData(String values)
	{
		throw new UnsupportedOperationException();
	}
	/**
	 *
	 * @param values List of object.
	 * @throws IOException throw IOException
	 */
	public void appendData(List<Object> values) throws IOException
	{
		//Writes the list of data into file
		String newLine = System.getProperty(("line.separator"));

		if (values != null)
		{
			for (Object obj : values)
			{
				if(obj == null)
				{
					obj = new String(""); // NOPMD
				}
				String value = obj.toString();
				value = value.replaceAll("\"", "'");
				value = "\"" + value + "\""; // NOPMD
				String data = value + delimiter;
				bufferedWriter.write(data);
			}
			rowCount++;
			bufferedWriter.write(newLine);
		}

		if (rowCount == bufferSize)
		{
			flush();
			rowCount = 0;
		}
	}
	/**
	 *
	 * @throws IOException throw IOException
	 */
	public void flush() throws IOException
	{
		bufferedWriter.flush();
	}
	/**
	 *
	 * @throws IOException throw IOException
	*/
	public void closeFile() throws IOException
	{
		if (bufferedWriter != null)
		{
			bufferedWriter.flush();
			bufferedWriter.close();
		}
	}
}
