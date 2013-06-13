/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.logger;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;


/**
 * @author prashant_bandal
 * This is an utility class which provides functions to get logger objects.
 * This class get neither instantiated nor extended.
 */
public final class LoggerConfig
{

	/**
	 * default logger object, to be used if logger configuration is not available.
	 */
	private static org.apache.log4j.Logger out = org.apache.log4j.Logger
			.getLogger(LoggerConfig.class);
	/**
	 * Indicates whether Logger is configured or not.
	 */
	private static boolean isConfigured = false;

	/**
	 * LoggerConfig class should not get instantiated from outside. Hence the constructor is private.
	 */
	private LoggerConfig()
	{

	}

	/**
	 * This method gets Configured Logger for a given class.
	 * @param className java class for which logger need to be created.
	 * @return org.apache.log4j.Logger.
	 */
	public static org.apache.log4j.Logger getConfiguredLogger(Class className)
	{
		org.apache.log4j.Logger logger;
		if (isConfigured)
		{
			logger = org.apache.log4j.Logger.getLogger(className);
		}
		else
		{
			out
			.warn("Application specific logger configuration is not done." +
			" Please use Logger.configureLogger(path) before using getLogger()");
			logger = out;
		}
		return logger;
	}

	/**
	 * This method configure Logger.
	 * @param propDirPath Path of directory containing properties file.
	 */
	public static void configureLogger(String propDirPath)
	{
		if (!isConfigured)
		{
			isConfigured = true;
			StringBuffer log4jFilePath = new StringBuffer();

			try
			{
				if(null!=propDirPath && !"".equals(propDirPath))
				{
					log4jFilePath.append(propDirPath).append(File.separator);
				}
				log4jFilePath.append("log4j.properties");
				out.info("Path of log4j properties file:"+log4jFilePath);
				PropertyConfigurator.configure(log4jFilePath.toString());

			}
			catch (Exception malformedURLEx)
			{
				isConfigured = false;
				out.fatal("Logger not configured. Invalid config file "
						+ log4jFilePath.toString());
			}
		}
	}

}