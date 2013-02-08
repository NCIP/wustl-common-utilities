
package edu.wustl.common.exceptionformatter;

/**
 * @author sachin_lale
 * Description: Interface defines method for formatting the database specific Exception message
 */
public interface ExceptionFormatter
{

	/**
	 * This method format Message.
	 * @param objExcp Exception.
	 * @return formated Message.
	 */
	String formatMessage(Exception objExcp);
}
