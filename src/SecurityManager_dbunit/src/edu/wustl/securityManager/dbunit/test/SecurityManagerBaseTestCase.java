/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.securityManager.dbunit.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.ISecurityManager;

/**
 * @author ravi_kumar
 *
 */
public class SecurityManagerBaseTestCase extends TestCase
{

	protected transient ISecurityManager securityManager = null;
	protected static int count = 0;
	protected static String loginName = "test";
	protected static String configFile = "";
	protected final String adminGroup = "ADMINISTRATOR_GROUP";
	/**
	 * Logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManagerBaseTestCase.class);
	static
	{
		Properties smProp;
		InputStream inputStream = SecurityManagerPropertiesLocator.class.getClassLoader()
				.getResourceAsStream("smDBUnit.properties");
		smProp = new Properties();
		try
		{
			smProp.load(inputStream);
			inputStream.close();
			String configFileName = smProp.getProperty("gov.nih.nci.security.configFile");
			URL url =SecurityManagerBaseTestCase.class.getClassLoader().getResource(configFileName);
			if(url!=null)
			{
				configFile=url.getPath().substring(1);
			}			
			System.setProperty("gov.nih.nci.security.configFile", configFile);
		}
		catch (IOException exception)
		{
			logger.error(exception.getStackTrace());
		}
	}
	

	/**
	 * No argument constructor.
	 */
	public SecurityManagerBaseTestCase()
	{
		super();
	}
	/**
	 * Constructs a test case with the given name.
	 * @param name Name of Test Case.
	 */
	public SecurityManagerBaseTestCase(String name)
	{
		super(name);
	}
	/**
	 * 
	 */
	public void setUp() throws Exception
	{
		super.setUp();
	}
}
