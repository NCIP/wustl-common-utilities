
package edu.wustl.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;


import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for sending file to client end.
 * @author Poornima Govindrao
 *
 */
public final class SendFile
{

	/**
	 * private constructor.
	 */
	private SendFile()
	{

	}
	/**
	 * LOGGER Logger Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(SendFile.class);

	/**
	 *
	 * @param response HttpServletResponse object.
	 * @param filePath Path of file.
	 * @param fileName name of file.
	 * @param contentType Content type of attachment.
	 */
	public static void sendFileToClient(HttpServletResponse response, String filePath,
			String fileName, String contentType)
	{
		if (filePath != null && (filePath.length()!=0))
		{
			File file = new File(filePath);
			if (file.exists())
			{
				response.setContentType(contentType);
				response.setHeader("Content-Disposition", "attachment;filename=\""
						+ fileName+ "\";");
				response.setContentLength((int) file.length());
				writeToStream(response, file);
			}
			else
			{
				LOGGER.error("Sorry Cannot Download as fileName is null");
			}
		}
	}

	/**
	 * This method write data into OutputStream.
	 * @param response HttpServletResponse
	 * @param file File object to write.
	 */
	private static void writeToStream(HttpServletResponse response, File file)
	{
		BufferedInputStream bis=null;
		try
		{
			OutputStream opstream = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));

			byte[] buf = new byte[Constants.FOUR_KILO_BYTES];
			int count=bis.read(buf);
			while (count > -1)
			{
				opstream.write(buf, 0, count);
				count = bis.read(buf);
			}
			opstream.flush();
			bis.close();
			boolean isDeleted = file.delete();
			if(!isDeleted)
			{
				LOGGER.info("Not able to delete file "+file.getName());
			}
		}
		catch (FileNotFoundException ex)
		{
			LOGGER.error("Exception in method sendFileToClient:"+ex.getMessage(), ex);
		}
		catch (IOException ex)
		{
			LOGGER.error("Exception in method sendFileToClient:"+ex.getMessage(), ex);
		}
	}
}
