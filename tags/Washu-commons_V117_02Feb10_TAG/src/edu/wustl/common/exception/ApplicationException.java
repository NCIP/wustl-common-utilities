
package edu.wustl.common.exception;

import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Base class for all custom exception classes which defines behavior of
 * exception classes and puts some constraints.
 * @author abhijit_naik
 *
 */
public class ApplicationException extends Exception
{

	/**
	 * Wrapped Exception.
	 */
	private transient Exception wrapException;
	/**
	 * The unique serial version UID.
	 */
	private static final long serialVersionUID = 6277184346384055451L;

	/**
	 * Logger object used to log messages.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ApplicationException.class);

	/**
	 *The errorKey object for the exception occurred.
	 */
	private ErrorKey errorKey;

	/**
	 * Custom message, if any, other than errorKey message.
	 */
	private String errorMsg;

	/**
	 * Parameters for error message.
	 */
	private String msgValues;

	/**
	 * The String of error message values to be send to ApplicationException should
	 * use this constant separator to separate values.
	 */
	public static final String ERR_MSG_VALUES_SEPARATOR = ":";


	/**
	 * complete customized error message.
	 */
	private String customizedMsg;

	public String getCustomizedMsg() {
		return customizedMsg;
	}

	public void setCustomizedMsg(String customizedMsg) {
		this.customizedMsg = customizedMsg;
	}

	/**
	 * The Only public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param errorKey The object which will represent the root cause of the error.
	 * @param exception root exception, if any, which caused this error.
	 * @param msgValues custom message, additional information.
	 */
	public ApplicationException(ErrorKey errorKey, Exception exception, String msgValues)
	{
		super(exception);
		this.wrapException = exception;
		/*if (errorKey == null)
		{
			LOGGER.fatal("While constructing application exception errorKey object must not be null");
			throw new AppRunTimeException();
		}*/

		this.errorKey = errorKey;
		setErrorMsg(msgValues);
	}

	public ApplicationException(ErrorKey errorKey, Exception exception, String msgValues,String customizedMsg)
	{
		this(errorKey, exception, msgValues);
		this.customizedMsg = customizedMsg;

	}


	/**
	 * Protected constructor which only child classes can use to reuse
	 * properties of another exception object.
	 * @param applicationException an exception whose properties will be reused.
	 */
	protected ApplicationException(ApplicationException applicationException)
	{
		this(applicationException.errorKey,applicationException,applicationException.msgValues);
	}
	/**
	 * Formats error message to be send to end user.
	 * @return formatted message.
	 */
	public String getFormattedMessage()
	{
		StringBuffer formattedMsg = new StringBuffer();
		formattedMsg.append(errorKey.getErrorNumber()).append(":-");
		String errMsg = errorKey.getMessageWithValues();
		formattedMsg.append(errMsg);
		return formattedMsg.toString();
	}

	/**
	 * This function formats a complete message with all details about error which caused the
	 * exsception. This function intended to use for logging error message.
	 *
	 * Usage: logger.error(ex.getLogMessage,ex);
	 *
	 * @return formatted detailed error message.
	 */
	public String getLogMessage()
	{
		StringBuffer logMsg = new StringBuffer();
		logMsg.append(getFormattedMessage());
		if (!"".equals(getErrorMsg()))
		{
			logMsg.append(" Error caused at- ").append(getErrorMsg());
		}
		if(getMessage()!=null)
		{
			logMsg.append("\n Root cause: ").append(getMessage());
		}
		if(wrapException!=null)
		{
			logMsg.append("; ").append(wrapException.getMessage());
		}
		return logMsg.toString();
	}

	/**
	 * Returns errorKey object. This function accessible only by child classes
	 * @return errorKey object reference.
	 */
	public ErrorKey getErrorKey()
	{
		return errorKey;
	}

	/**
	 * this function returns the key name of the error used from applicationResource
	 * bundle.
	 * @return error key name.
	 */
	public String getErrorKeyName()
	{
		String keyName;
		if (errorKey == null)
		{
			keyName="";
		}
		else
		{
			keyName=errorKey.getErrorKey();
		}
		return keyName;
	}
	/**
	 * Sets errorKey object. This function accessible only by child classes
	 * @param errorKey erroKey object to be set.
	 */
	protected void setErrorKey(ErrorKey errorKey)
	{
		this.errorKey = errorKey;
	}

	/**
	 * Returns the custom error message set when the error is occurred.
	 * @return The custom error message.
	 */
	protected String getErrorMsg()
	{
		return this.errorMsg;
	}

	/**
	 * This function checks the custom error parameter. If it has values for
	 * errorKey message text then sets custom error blank and sets those values to
	 * errorKey member object.
	 * @param errorValParam custom error message or values for erroKey text message.
	 */
	protected final void setErrorMsg(String errorValParam)
	{
		//this.errorMsg = errorValParam;
		if (errorValParam != null)
		{
			this.errorMsg = "";
			setMsgValues(errorValParam);
			String[] errorValues = errorValParam.split(ERR_MSG_VALUES_SEPARATOR);
			if(errorKey != null)
			{
				errorKey.setMessageValues(errorValues);
			}
		}
	}

	/**
	 * Inner runtime exception class used to throw exception if
	 * ApplicationException object not get created properly.
	 */
	private static class AppRunTimeException extends RuntimeException
	{

		/**
		 * A unique serial versionUID.
		 */
		private static final long serialVersionUID = 9019490724476120927L;

		/**
		 * default constructor.
		 */
		public AppRunTimeException()
		{
			super("Failed to construct ApplicationException object."
					+ " Please see the comple error log above");
		}
	}


	/**
	 * returns error message parameters.
	 * @return message parameters
	 */
	public String getMsgValues()
	{
		return msgValues;
	}
	/**
	 * @return String[].
	 */
	public String[] toMsgValuesArray()
	{
		String [] valueArr;
		if (msgValues == null)
		{
			valueArr =new String [] {errorMsg};
		}
		else
		{
			valueArr =msgValues.split(ERR_MSG_VALUES_SEPARATOR);
		}
		return valueArr;
	}

	/**
	 * sets error message parameters.
	 * @param msgValues parameters.
	 */
	public final void setMsgValues(String msgValues)
	{
		this.msgValues = msgValues;
	}

	/**
	 * This function is used to get key value set for the Error Key.
	 * @return Unique key value of ErrorKey.
	 */
	public String getErrorKeyAsString()
	{
		return errorKey.getErrorKey();
	}

	/**
	 * @return Returns the wrapException.
	 */
	public Exception getWrapException()
	{
		return wrapException;
	}

	/**
	* This will return the complete error message.
	*/
	 public String getMessage()
	 {
		 String message = "";
		 if(customizedMsg != null && !Validator.isEmpty(customizedMsg))
		 {
			 message = customizedMsg;
		 }
		 else if(errorKey != null)
		 {
			 message = errorKey.getMessageWithValues();
		 }
		 return message;
	 }
}