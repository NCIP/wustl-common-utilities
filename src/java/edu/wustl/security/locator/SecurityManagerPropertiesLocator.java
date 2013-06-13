/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.locator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.global.Constants;

/**
 * Reads the SecurityManager.properties file and loads properties to be referred by SecurityManager.
 * @author deepti_shelar
 */
public final class SecurityManagerPropertiesLocator
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManagerPropertiesLocator.class);

	/**
	 * property names from SecurityManager.properties file .
	 */
	private String appCtxName;
	/**
	 * class name.
	 */
	private String className;

	/**
	 * Instantiating the class whenever loaded for the first time.
	 * The same instance will be returned whenever getInstance is called
	 */
	private static SecurityManagerPropertiesLocator singleObj = new SecurityManagerPropertiesLocator();

	/**
	 * Making the class singleton.
	 */
	private SecurityManagerPropertiesLocator()
	{
		InputStream inputStream = SecurityManagerPropertiesLocator.class.getClassLoader()
				.getResourceAsStream(Constants.SM_PROP_FILE);
		Properties smProp = new Properties();
		try
		{
			smProp.load(inputStream);
			inputStream.close();
			appCtxName = smProp.getProperty(Constants.APP_CTX_NAME);
			className = smProp.getProperty(Constants.SM_CLASSNAME);
		}
		catch (IOException exception)
		{
			logger.fatal("Not able to initialize Security Manager Properties.", exception);
		}
	}

	/**
	 * Singleton class, will return the single object every time.
	 * @return SecurityManagerPropertiesLocator instance
	 */
	public static SecurityManagerPropertiesLocator getInstance()
	{
		return singleObj;
	}

	/**
	 * @return the appCtxName
	 */
	public String getApplicationCtxName()
	{
		return appCtxName;
	}

	/**
	 * @return the className
	 */
	public String getSecurityMgrClassName()
	{
		return className;
	}
}
