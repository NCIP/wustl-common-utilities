package edu.wustl.common.exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;


/**
 * This class provides the functionality to cache error keys and their messages.
 * It provides functions to format dynamic error messages. Object of this class has
 * been used by ApplicationException class and its children.
 * @author abhijit_naik
 *
 */
public final class ErrorKey implements Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 5958370735621520931L;
	/**
	 * The error number used to uniquely identify exception.
	 */
	private String errorNumber;
	/**
	 * Error key name used to uniquely identify error message and errorKey Object.
	 * This key is the same which is set in properties file.
	 */
	private String errorKey;
	/**
	 * Message to be displayed when the error occurs.
	 * The error message set in properties file
	 */
	private String errorMessage;
	/**
	 * Error message values if any.
	 */
	private Object []messageValues;

	/**
	 * The static map used as cache for the ErrorKey objects.
	 */
	private static Map<String, ErrorKey> errorKeyMap = new HashMap<String, ErrorKey>();
	
	private static Logger logger = Logger.getCommonLogger(ErrorKey.class);

	/**
	 * Private constructor to restrict rest of the world from instantiating object.
	 * @param errNo unique number for the exception.
	 * @param key	error key used in properties file.
	 * @param msg 	User friendly error message.
	 */
	private ErrorKey(String errNo, String key, String msg)
	{
		this.errorNumber = errNo;
		this.errorKey = key;
		setErrorMessage(msg);
	}
	
	/**
	 * Private constructor to restrict rest of the world from instantiating object.
	 * @param errNo unique number for the exception.
	 * @param key	error key used in properties file.
	 * @param msg 	User friendly error message.
	 */
	private ErrorKey(String key, String msg)
	{
		//this.errorNumber = errNo;
		this.errorKey = key;
		setErrorMessage(msg);
	}


	/**
	 * Values to be injected in error message.
	 * @param values values for error message.
	 */
	public void setMessageValues(Object [] values)
	{
		messageValues = Arrays.asList(values).toArray();
	}

	/**
	 * This function used to get the appropriate error key.
	 * object for a give error key string.
	 * @param errorName the error key.
	 * @return the appropriate errorKey object.
	 */
	public static ErrorKey getErrorKey(String errorName)
	{
		//TODO:will remove this sysout : temporary
		//System.out.println("Error key name ::"+errorName);
		logger.info("Error key name ::"+errorName);
		return errorKeyMap.get(errorName);
	}

	/**
	 * Returns current thread's class loader.
	 * @return current thread's class loader.
	 */
	private static ClassLoader getCurrClassLoader()
	{
		return Thread.currentThread().getContextClassLoader();
	}
	/**
	 * Initializes the ErrorKey Map.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @throws IOException throws if properties file not found or unable to read.
	 */
	public static void init(String separator) throws IOException
	{
		InputStream stream =getCurrClassLoader().getResourceAsStream("ApplicationResources.properties");
		//addErrorKeysToMap(stream, separator);
		addErrorKeysToMap(stream);
		stream.close();
	}

	/**
	 * THis functions used to load errorKeys for a given properties file.
	 * @param propFile property file.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @throws IOException if it fails to read given profFile.
	 */
	public static void updateErrorKeys(String propFile, String separator) throws IOException
	{
		InputStream stream =getCurrClassLoader().getResourceAsStream(propFile);
		addErrorKeysToMap(stream,separator);
		stream.close();
	}

	/**
	 * This function updates errorKeyMap with the given properties file.
	 * @param stream inputStream of the properties file.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @throws IOException if function fails to read key/messages from
	 * properties file due to I/O error it throw IOException.
	 */
	public static void addErrorKeysToMap(InputStream stream, String separator) throws IOException
	{
		Properties errorKeyPros = new Properties();
		errorKeyPros.load(stream);
		Enumeration<Object> enumerator = errorKeyPros.keys();
		while(enumerator.hasMoreElements())
		{
			String key =(String) enumerator.nextElement();
			String message = errorKeyPros.getProperty(key);
			if (message.contains(separator))
			{
				ErrorKey errorKey = createErrorKeyObject(key, message, separator);
				errorKeyMap.put(key, errorKey);
			}
		}
	}
	
	/**
	 * This function updates errorKeyMap with the given properties file.
	 * @param stream inputStream of the properties file.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @throws IOException if function fails to read key/messages from
	 * properties file due to I/O error it throw IOException.
	 */
	public static void addErrorKeysToMap(InputStream stream) throws IOException
	{
		Properties errorKeyPros = new Properties();
		errorKeyPros.load(stream);
		Enumeration<Object> enumerator = errorKeyPros.keys();
		while(enumerator.hasMoreElements())
		{
			String key =(String) enumerator.nextElement();
			String message = errorKeyPros.getProperty(key);
			ErrorKey errorKey = createErrorKeyObject(key, message);
			errorKeyMap.put(key, errorKey);
			
		}
	}
	

	/**
	 * THis function constructs new ErrorKey Object.
	 * @param key Error key to uniquely identify ErrorKey object.
	 * @param msg Error Message.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @return newly created ErrorKey Object.
	 */
	private static ErrorKey createErrorKeyObject(String key, String msg, String separator)
	{

		String [] errorInfo = msg.split(separator);

		return new ErrorKey(errorInfo[0],key,errorInfo[1]);

	}
	
	/**
	 * THis function constructs new ErrorKey Object.
	 * @param key Error key to uniquely identify ErrorKey object.
	 * @param msg Error Message.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @return newly created ErrorKey Object.
	 */
	private static ErrorKey createErrorKeyObject(String key, String msg)
	{
			return new ErrorKey(key,msg);
	}
	
	/**
	 * THis function constructs new ErrorKey Object.
	 * @param key Error key to uniquely identify ErrorKey object.
	 * @param msg Error Message.
	 * @param separator A token used in error message to separate out
	 * error number and error message.
	 * @return newly created ErrorKey Object.
	 */
	public static ErrorKey getErrorKeyObject(String key)
	{
		return new ErrorKey(key,ApplicationProperties.getValue(key));

	}

	

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * This function format error message with the available parameter values.
	 * If parameter values are not present then returns the message assuming there
	 * is no need of parameters to format the message.
	 * @return formatted message.
	 */
	public String getMessageWithValues()
	{
		String msg;
		if (messageValues == null || messageValues.length ==0)
		{
			msg=this.errorMessage;
		}
		else
		{
			msg= MessageFormat.format(errorMessage, messageValues);
		}
		return msg;
	}


	/**
	 * Returns error number.
	 * @return error number.
	 */
	protected String getErrorNumber()
	{
		return errorNumber;
	}


	/**
	 * sets the error number.
	 * @param errorNumber error Number.
	 */
	protected void setErrorNumber(String errorNumber)
	{
		this.errorNumber = errorNumber;
	}


	/**
	 * returns error key.
	 * @return key.
	 */
	protected String getErrorKey()
	{
		return errorKey;
	}
}
