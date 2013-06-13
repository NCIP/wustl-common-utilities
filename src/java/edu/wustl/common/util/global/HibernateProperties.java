/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Jul 19, 2004
 *
 */

package edu.wustl.common.util.global;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.wustl.common.util.logger.Logger;

/**
 * This class is used to retrieve values of keys from the ApplicationResources.properties file.
 * @author kapil_kaveeshwar
 */
public final class HibernateProperties
{
	/**
	 * private constructor.
	 */
	private HibernateProperties()
	{

	}
	/**
	 * Properties object.
	 */
	private static Properties prop;

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(HibernateProperties.class);
	/**
	 * This method load the properties file.
	 * @param baseName File Name
	 * @throws IOException - Generic IO exception
	 */
	public static void initBundle(String baseName) throws IOException
	{
		try
		{
			File file = new File(baseName);
			BufferedInputStream stram = new BufferedInputStream(new FileInputStream(file));
			prop = new Properties();
			prop.load(stram);
			stram.close();
		}
		catch (FileNotFoundException exe)
		{
			LOGGER.error("Not able to read the file:"+baseName,exe);
			throw exe;
		}
		catch (IOException exe)
		{
			LOGGER.error("Not able to load the properties file:"+baseName,exe);
			throw exe;
		}
	}
	/**
	 * get the value for a key.
	 * @param theKey key in properties file.
	 * @return value for a key.
	 */
	public static String getValue(String theKey)
	{
		return prop.getProperty(theKey);
	}
}