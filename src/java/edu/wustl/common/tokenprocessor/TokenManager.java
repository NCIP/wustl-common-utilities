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

package edu.wustl.common.tokenprocessor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import edu.wustl.common.exception.ApplicationException;

/**
 * @author suhas_khot
 *
 */
public class TokenManager
{

	/**
	 * Given a object and format returns value.
	 * @param object
	 * @param format
	 * @throws ApplicationException
	 */
	private static final JEP parser = new JEP();

	public static String getLabelValue(final Object object, final String format)
			throws ApplicationException
	{
		return parseFormat(object, format.trim());
	}

	/**
	 * Parse format and generate value
	 * @param object
	 * @param format
	 * @param tokenValue
	 * @param delimiter
	 * @param fileName
	 * @throws ApplicationException
	 */
	public static String parseFormat(final Object object, String format)
			throws ApplicationException
	{
		StringBuffer tokenValue = new StringBuffer();
		try
		{
			int formateStringEndsAt=format.indexOf("\"",1)+1;
			String formateString = format.substring(0, formateStringEndsAt).trim().replace("\"","");
			String remainingString=format.substring(formateStringEndsAt,format.length()).trim();
			int valueStringStartsAt=remainingString.indexOf(",")+1;
			String valueString = remainingString.substring(valueStringStartsAt,remainingString.length()).trim();
			String[] valueStringArray = valueString.split(",");
			String value = null;
			ArrayList<Object> values = new ArrayList<Object>();
			Enumeration<Object> allKeys = null;
			Node parsedExpressionNode = null;
			Double evaluatedValue = null;
			String keyWord = null;
			for (int i = 0; i < valueStringArray.length; i++)
			{
				allKeys = PropertyHandler.getAllKeys();
				value = valueStringArray[i].toString();
				value=" "+value.replaceAll(" ", "")+" ";
				while (allKeys.hasMoreElements())
				{
					keyWord = (String) allKeys.nextElement();
					value = getValueAfterProcessingKey(object, value, keyWord);
				}
				parsedExpressionNode = parser.parse(value);
				evaluatedValue = Double.parseDouble(String.valueOf(parser.evaluate(parsedExpressionNode)));
				values.add(evaluatedValue.longValue());
			}
			tokenValue.append(String.format(formateString, values.toArray()));
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ApplicationException(null, e, e.getMessage(),
					"Auto Id generation format seems invalid.Contact administrator.");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			throw new ApplicationException(null, e, e.getMessage(),
					"Please enclose tokens between '%' delimiter.");
		}
		catch (ParseException e)
		{
			throw new ApplicationException(null, e, e.getMessage(), "Error while generating PPID.");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			throw new ApplicationException(null, e, e.getMessage(), "Error while generating PPID.");
		}

		return tokenValue.toString();
	}

	private static String getValueAfterProcessingKey(final Object object, String value,
			String keyWord) throws ApplicationException
	{
		String token=null;
		String finalValue=value;// CSID+MICSID
		String pattern="[^\\w]"+keyWord+"[^\\w]";// [^\\w]CSID[^\\w]
		Pattern tokenPattern = Pattern.compile(pattern);
		Matcher tokenMatcher = tokenPattern.matcher(value);
		while (tokenMatcher.find() )
        {
			token = TokenFactory.getInstance(keyWord).getTokenValue(object);//4
			String patternString=value.substring(tokenMatcher.start(),tokenMatcher.end());// 4+
			String patternStringWithTokenValue=patternString.replace(keyWord,token);
			finalValue=finalValue.replace(patternString,patternStringWithTokenValue);
        }

		return finalValue;
	}

}
