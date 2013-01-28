
package edu.wustl.common.exceptionformatter;

import java.text.MessageFormat;

import edu.wustl.common.util.global.ApplicationProperties;

/**
 * Default Exception Formatter class.
 */
public class DefaultExceptionFormatter implements ExceptionFormatter
{

	/**
	 * format Message.
	 * @param objExcp Exception
	 * @param args arguments.
	 * @return Message.
	 */
	public String formatMessage(Exception objExcp, Object[] args)
	{
		return null;
	}

	/**
	 * get Error Message.
	 * @param key key
	 * @param args arguments
	 * @return Error Message.
	 */
	public String getErrorMessage(String key, Object[] args)
	{
		StringBuffer message = new StringBuffer();
		message.append(ApplicationProperties.getValue(key));
		if (message != null && args != null)
		{
			message.replace(0, message.length(), MessageFormat.format(message.toString(), args));
		}
		return message.toString();
	}

	public String formatMessage(Exception objExcp)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
