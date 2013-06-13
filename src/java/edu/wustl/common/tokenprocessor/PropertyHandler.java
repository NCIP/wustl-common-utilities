/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.tokenprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;

/**
 * This class has functions to read LabelGenerator Properties file.
 * @author abhijit_naik
 */
public final class PropertyHandler
{

	/**
	 * Name generator properties.
	 */

	private static Properties labelTokenProperties = null;
	/**
	 * private constructor.
	 */
	private PropertyHandler()
	{

	}

	/**
	 * Load the Properties file.
	 * @param path Path
	 * @throws Exception Generic exception
	 */
	public static void init(String path) throws Exception
	{
		final String absolutePath = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + path;
		final InputStream inpurStream = new FileInputStream(new File(absolutePath));
		labelTokenProperties = new Properties();
		labelTokenProperties.load(inpurStream);
	}

	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String.
	 * @param propertyName Property Name
	 * @return String
	 * @throws Exception Generic exception
	 */

	public static synchronized String getValue(String propertyName) throws Exception
	{

		if (labelTokenProperties == null)
		{
			init(Constants.LABEL_TOKEN_PROP_FILE_NAME);
		}
		return (String) labelTokenProperties.get(propertyName);

	}
	public static synchronized Enumeration<Object> getAllKeys() throws Exception
	{

		if (labelTokenProperties == null)
		{
			init(Constants.LABEL_TOKEN_PROP_FILE_NAME);
		}
		return labelTokenProperties.keys();

	}

}
