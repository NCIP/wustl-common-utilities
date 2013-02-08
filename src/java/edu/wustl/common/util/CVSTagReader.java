/*
 * Created on Jun 29, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CVSTagReader
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CVSTagReader.class);
	/**
	 * Reads the file and returns the CVS tag from comment section.
	 * File pattern is
	 *
		 /* $Name: 1.1.4.1.2.7 $
		 * $Log: CVSTagReader.java,v $
		 * Revision 1.1.4.1.2.7  2009/01/19 11:48:24  prashant_bandal
		 * code refactoring.
		 *
		 * Revision 1.1.4.1.2.6  2008/11/25 13:45:34  ravi_kumar
		 * code refactoring.
		 *
		 * Revision 1.1.4.1.2.5  2008/11/24 08:00:12  ravi_kumar
		 * code refactoring.
		 *
		 * Revision 1.1.4.1.2.4  2008/10/01 13:02:39  dharitri_pradhan
		 * java-doc has been edited.
		 *
		 * Revision 1.1.4.1.2.3  2008/09/25 11:11:09  ravi_kumar
		 * code refactoring
		 *
		 * Revision 1.1.4.1.2.2  2008/09/19 14:29:53  ravi_kumar
		 * Code refactoring.
		 *
		 * Revision 1.1.4.1.2.1  2008/09/18 07:31:18  ravi_kumar
		 * run formatter and removed all import related warnings.
		 *
		 * Revision 1.1.4.1  2008/04/24 05:32:05  abhijit_naik
		 * refactoring and formatiing code
		 *
		 * Revision 1.1  2006/07/04 10:50:37  Kapil
		 * Read the tag information from a file.
		 *
		 * Revision 1.1  2006/06/28 14:02:17  Kapil
		 * Test cvs keywords
		 *
	 * @param file String File in which tag information is available.
	 * @return parse and return the CVS tag form which check-out is done.
	 * Returns null if any IOError occurs or if tag can not be parsed.
	 **/
	public String readTag(String file)
	{
		String tag=null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line!= null)
			{
				if (line.indexOf("$Name:") != -1)
				{
					tag = parseTag(line);
				}
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException exp)
		{
			LOGGER.error(exp.getMessage(), exp);
		}
		return tag;
	}

	/**
	 * @param str one line string that contains CVS tag
	 * @return Returns the CVS tag parse from the following pattern.
	 * Line pattern: * $Name: 1.1.4.1.2.7 $
	 * Example1: * $Name: 1.1.4.1.2.7 $
	 * Example2: * $Name: 1.1.4.1.2.7 $
	 * */
	private String parseTag(String str)
	{
		String tag=null;
		try
		{
			StringTokenizer tok = new StringTokenizer(str);

			//Ignore first token: "*:"
			String firstTok = tok.nextToken();
			if ("*".equals(firstTok))
			{
				//Ignore 2nd token: "$Name:"
				String secondTok = tok.nextToken();
				if ("$Name:".equals(secondTok))
				{
					//Third token is the tag
					StringBuffer tagvalue = new StringBuffer();
					while (tok.hasMoreTokens())
					{
						tagvalue.append(tok.nextToken());
						tagvalue.append(' ');
					}
					tag=tagvalue.toString().trim();
				}
			}
		}
		catch (Exception exp)
		{
			LOGGER.error(exp.getMessage(), exp);
		}
		return tag;
	}
}