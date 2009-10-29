/**
 * <p>Title: AppLogger Class>
 * <p>Description:  Application Logger class</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 *
 * FIXME: Java doc.
 */

package edu.wustl.common.util.logger;


import org.apache.log4j.Level;


/**
 * This is an utility class which provides functions to get logger objects.
 * This class get neither instantiated nor extended.
 *
 */
public final class Logger
{

	/**
	 * @deprecated.
	 */
	@Deprecated
	public static org.apache.log4j.Logger out = org.apache.log4j.Logger.getLogger("");
	/**
	 * org.apache.log4j.Logger instance.
	 */
	private org.apache.log4j.Logger logger;
	/**
	 * String constant.
	 */
	private final static String FQCN = Logger.class.getName() ;
	/**
	 * Logger class should not get instantiated from outside. Hence the constructor is private.
	 * @param apacheLogger org.apache.log4j.Logger instance.
	 */
	private Logger(org.apache.log4j.Logger apacheLogger)
	{
		this.logger = apacheLogger;
	}

	/**
	 * Creates new logger object for a given class.
	 * @param className java class for which logger need to be created.
	 * @return new commonLogger object.
	 */
	public static Logger getCommonLogger(Class className)
	{
		return new Logger(LoggerConfig.getConfiguredLogger(className));
	}

	/**
	 * Log a message object with the DEBUG level.
	 * This method first checks if this category is DEBUG enabled by comparing the level of this category
	 * with the DEBUG level. If this category is DEBUG enabled, then it converts the message object
	 * (passed as parameter) to a string by invoking the appropriate ObjectRenderer.
	 * It then proceeds to call all the registered appenders in this category and
	 * also higher in the hierarchy depending on the value of the additivity flag.
	 * @param message the message object to log.
	 */
	public void debug(Object message)
	{
		if (logger.isEnabledFor(Level.DEBUG))
		{
			//logger.debug(message);
			logger.log(FQCN, Level.DEBUG, message, null) ;
		}
	}

	/**
	 * Log a message object with the DEBUG level including the stack trace of
	 * the Throwable t passed as parameter.
	 * @param message the message object to log.
	 * @param throwable the exception to log, including its stack trace.
	 */
	public void debug(Object message, Throwable throwable)
	{
		if (logger.isEnabledFor(Level.DEBUG))
		{
			//logger.debug(message, throwable);
			logger.log(FQCN, Level.DEBUG, message, throwable) ;
		}
	}

	/**
	 * Log a message object with the ERROR Level.
	 * This method first checks if this category is ERROR enabled
	 * by comparing the level of this category with ERROR Level.
	 * If this category is ERROR enabled, then it converts the
	 * message object passed as parameter to a string by invoking the appropriate ObjectRenderer.
	 * It proceeds to call all the registered appenders in this category
	 * and also higher in the hierarchy depending on the value of the additivity flag.
	 * @param message the message object to log.
	 */
	public void error(Object message)
	{
		if (logger.isEnabledFor(Level.ERROR))
		{
			//logger.error(message);
			logger.log(FQCN, Level.ERROR, message, null) ;
		}
	}

	/**
	 * Log a message object with the ERROR level including the stack trace of
	 * the Throwable t passed as parameter.
	 * @param message the message object to log.
	 * @param throwable the exception to log, including its stack trace.
	 */
	public void error(Object message, Throwable throwable)
	{
		if (logger.isEnabledFor(Level.ERROR))
		{
			//logger.error(message, throwable);
			logger.log(FQCN, Level.ERROR, message, throwable) ;
		}
	}

	/**
	 * Log a message object with the FATAL Level.
	 * This method first checks if this category is FATAL enabled by comparing
	 * the level of this category with FATAL Level. If the category is FATAL enabled,
	 * then it converts the message object passed as parameter to a string by invoking
	 * the appropriate ObjectRenderer. It proceeds to call all the registered appenders
	 * in this category and also higher in the hierarchy depending on the value of the additivity flag.
	 * @param message the message object to log.
	 */
	public void fatal(Object message)
	{
		if (logger.isEnabledFor(Level.FATAL))
		{
			//logger.fatal(message);
			logger.log(FQCN, Level.FATAL, message, null) ;
		}
	}

	/**
	 * Log a message object with the FATAL level including the stack trace of
	 * the Throwable t passed as parameter.
	 * @param message the message object to log.
	 * @param throwable the exception to log, including its stack trace.
	 */
	public void fatal(Object message, Throwable throwable)
	{
		if (logger.isEnabledFor(Level.FATAL))
		{
			//logger.fatal(message, throwable);
			logger.log(FQCN, Level.FATAL, message, throwable) ;
		}
	}

	/**
	 * Log a message object with the INFO Level.
	 * This method first checks if this category is INFO enabled by comparing the level
	 * of this category with INFO Level. If the category is INFO enabled, then it converts
	 * the message object passed as parameter to a string by invoking the appropriate ObjectRenderer.
	 * It proceeds to call all the registered appenders
	 * in this category and also higher in the hierarchy depending on the value of the additivity flag.
	 * @param message the message object to log.
	 */
	public void info(Object message)
	{
		if (logger.isEnabledFor(Level.INFO))
		{
			//logger.info(message);
			logger.log(FQCN, Level.INFO, message, null) ;
		}
	}

	/**
	 * Log a message object with the INFO level including the stack trace of
	 * the Throwable t passed as parameter.
	 * @param message the message object to log.
	 * @param throwable the exception to log, including its stack trace.
	 */
	public void info(Object message, Throwable throwable)
	{
		if (logger.isEnabledFor(Level.INFO))
		{
			//logger.info(message, throwable);
			logger.log(FQCN, Level.INFO, message, throwable) ;
		}
	}

	/**
	 * Log a message object with the WARN Level.
	 * @param message the message object to log.
	 */
	public void warn(Object message)
	{
		if (logger.isEnabledFor(Level.WARN))
		{
			//logger.warn(message);
			logger.log(FQCN, Level.WARN, message, null) ;
		}
	}

	/**
	 * Log a message with the WARN level including the stack trace of the Throwable t passed as parameter.
	 * @param message the message object to log.
	 * @param throwable the exception to log, including its stack trace.
	 */
	public void warn(Object message, Throwable throwable)
	{
		if (logger.isEnabledFor(Level.WARN))
		{
			//logger.warn(message, throwable);
			logger.log(FQCN, Level.WARN, message, throwable) ;
		}
	}

	/**
	 * @deprecated Please now use getCommonLogger(Class className).
	 * @see getCommonLogger(Class className)
	 * @param className java class for which logger need to be created.
	 * @return new logger object.
	 */
	public static org.apache.log4j.Logger getLogger(Class className)
	{
		return LoggerConfig.getConfiguredLogger(className);
	}

}