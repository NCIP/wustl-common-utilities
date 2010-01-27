
package edu.wustl.common.util.global;

import java.io.BufferedReader;
import java.io.FileReader;

import edu.wustl.common.util.logger.Logger;
/**
 * This class is used to read a file.
 */
public class CommonFileReader
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CommonFileReader.class);

	/**
	 * @param fileName file name.
	 * @return return the content of file.
	 */
	public String readData(String fileName)
	{
		StringBuffer buffer = new StringBuffer();
		try
		{
			BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
			String line = bufferReader.readLine();
			while ((line = bufferReader.readLine()) != null)
			{
				buffer.append(line).append("<br>");
			}
			bufferReader.close();
		}
		catch (Exception exception)
		{
			LOGGER.debug("Not able to read the file:"+fileName,exception);
		}

		return buffer.toString();
	}
}
