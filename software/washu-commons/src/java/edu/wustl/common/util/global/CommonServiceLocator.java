/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import edu.wustl.common.util.logger.LoggerConfig;


/**
 * This class is a common service locator. Different parameter like application url,
 * properties file directory etc. are set when this class loads. are set when this class loads.
 * @author ravi_kumar
 */
public final class CommonServiceLocator
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(CommonServiceLocator.class);

	/**
	 * object of class CommonServiceLocator.
	 */
	private static CommonServiceLocator commonServLocator;
	/**
	 * Application Name.
	 */
	private String appName;

	/**
	 * Application Home directory.
	 */
	private String appHome;
	/**
	 * Directory path of properties file.
	 */
	private String propDirPath;
	/**
	 * Application URL.
	 */
	private String appURL;

	/**
	 * Date separator.
	 */
	private String dateSeparator="-";

	/**
	 * Date separator.
	 */
	private String dateSeparatorSlash="/";

	/**
	 * Minimum year.
	 * e.g 1900
	 */
	private String minYear;
	/**
	 * Maximum year.
	 * e.g 9999
	 */
	private String maxYear;

	/**
	 * Date pattern.
	 * e.g.MM-dd-yyyy
	 */
	private String datePattern;

	/**
	 * Time pattern. e.g. HH:mm:ss
	 */
	private String timePattern;
	/**
	 * Date time pattern.
	 * e.g. yyyy-MM-dd-HH24.mm.ss.SSS
	 */
	private String timeStampPattern;

	/**
	 * Path the request should be redirected to in case of XSS validation failures.
	 */
	private String xssFailurePath;

	/**
	 *No argument constructor.
	 *Here all the properties are set
	 */
	private CommonServiceLocator()
	{
		initProps();
		LoggerConfig.configureLogger(this.propDirPath);
	}

	public CommonServiceLocator(String applicatioResourcePath)
	{
		try
		{
			initProps(new FileInputStream(applicatioResourcePath));
		}
		catch (FileNotFoundException e)
		{
			logger.fatal("Not able to load properties file",e);
		}
		LoggerConfig.configureLogger(this.propDirPath);
	}

	private void initProps(InputStream stream)
	{
		try
		{
			Properties props= new Properties();
			props.load(stream);
			setAppName(props);
			setPropDirPath();
			setDateSeparator(props);
			setDatePattern(props);
			setTimePattern(props);
			setTimeStampPattern(props);
			setMinYear(props);
			setMaxYear(props);
			setXSSFailurePath(props);
			stream.close();
		}
		catch (IOException exception)
		{
			logger.fatal("Not able to load properties file",exception);
		}
		
	}

	/**
	 * This method return object of the class CommonServiceLocator.
	 * @return object of the class CommonServiceLocator.
	 */
	public static CommonServiceLocator getInstance()
	{
		if(commonServLocator == null)
		{
			commonServLocator = new CommonServiceLocator();
		}
		return commonServLocator;
	}

	/**
	 * This method return object of the class CommonServiceLocator.
	 * @return object of the class CommonServiceLocator.
	 */
	public static CommonServiceLocator getInstance(String applicatioResourcePath)
	{
		if(commonServLocator == null)
		{
			commonServLocator = new CommonServiceLocator(applicatioResourcePath);
		}
		return commonServLocator;
	}
	/**
	 * This method loads properties file.
	 */
	private void initProps()
	{
		InputStream stream = CommonServiceLocator.class.getClassLoader()
		.getResourceAsStream("ApplicationResources.properties");
		initProps(stream);
	    
	}

	/**
	 * Set XSS failure path.
	 * @param props Properties.
	 */
	private void setXSSFailurePath(final Properties props)
    {
	    xssFailurePath = props.getProperty("xss.failure.redirect.path");
    }

	/**
	 * @return the XSS failure Path.
	 */
	public String getXSSFailurePath()
	{
	    return xssFailurePath;
	}
	/**
	 * @return the application name.
	 */
	public String getAppName()
	{
		return appName;
	}


	/**
	 * Set the application name.
	 * @param props Object of Properties
	 */
	private void setAppName(Properties props)
	{
		appName=props.getProperty("app.name");
	}

	/**
	 * @return the application home directory where application is running.
	 */
	public String getAppHome()
	{
		return appHome;
	}

	/**
	 * Set application home directory where application is running.
	 * @param appHome Object of Properties
	 */
	public void setAppHome(String appHome)
	{
		this.appHome=appHome;
	}

	/**
	 * @return the path of directory of property files.
	 */
	public String getPropDirPath()
	{
		return propDirPath;
	}


	/**
	 * Set path of directory of property files.
	 */
	private void setPropDirPath()
	{
		String path = System.getProperty("app.propertiesFile");
		if(path==null)
		{
			propDirPath = "";
		}
		else
		{
			File propetiesDirPath = new File(path);
			propDirPath = propetiesDirPath.getParent();
		}
	}


	/**
	 * @return the Application URL.
	 */
	public String getAppURL()
	{
		return appURL;
	}


	/**
	 * This method Set Application URL.
	 * @param requestURL request URL.
	 */
	public void setAppURL(String requestURL)
	{
		if (appURL == null || "".equals(appURL.trim()))
		{
			String tempUrl="";
			try
			{
				URL url = new URL(requestURL);
				tempUrl = url.getProtocol() + "://" + url.getAuthority() + url.getPath();
				appURL = tempUrl.substring(0, tempUrl.lastIndexOf('/'));
				logger.debug("Application URL set: " + appURL);
			}
			catch (MalformedURLException urlExp)
			{
				logger.error(urlExp.getMessage(), urlExp);
			}
		}
	}


	/**
	 * @return the dateSeparator
	 */
	public String getDateSeparator()
	{
		return ApplicationProperties.getValue("date.separator");
	}

	/**
	 * @param props Object of Properties
	 */
	public void setDateSeparator(Properties props)
	{
		this.dateSeparator = props.getProperty("date.separator");
	}

	/**
	 * @return the dateSeparatorSlash
	 */
	public String getDateSeparatorSlash()
	{
		return dateSeparatorSlash;
	}

	/**
	 * @return the minYear
	 */
	public String getMinYear()
	{
		return  ApplicationProperties.getValue("min.year");
	}

	/**
	 * @param props Object of Properties.
	 */
	public void setMinYear(Properties props)
	{
		this.minYear = props.getProperty("min.year");
	}

	/**
	 * @return the maxYear
	 */
	public String getMaxYear()
	{
		return ApplicationProperties.getValue("max.year");
	}

	/**
	 * @param props Object of Properties.
	 */
	public void setMaxYear(Properties props)
	{
		this.maxYear = props.getProperty("max.year");
	}

	/**
	 * @return the datePattern
	 */
	public String getDatePattern()
	{
		return  ApplicationProperties.getValue("date.pattern");
	}

	/**
	 * @param props Object of Properties
	 */
	public void setDatePattern(Properties props)
	{
		this.datePattern = props.getProperty("date.pattern");
	}

	/**
	 * @return the timePattern
	 */
	public String getTimePattern()
	{
		return ApplicationProperties.getValue("time.pattern");
	}

	/**
	 * @param props Object of Properties
	 */
	public void setTimePattern(Properties props)
	{
		this.timePattern = props.getProperty("time.pattern");
	}

	/**
	 * @return the timeStampPattern
	 */
	public String getTimeStampPattern()
	{
		return ApplicationProperties.getValue("timestamp.pattern");
	}

	/**
	 * @param props Object of Properties
	 */
	public void setTimeStampPattern(Properties props)
	{
		this.timeStampPattern = props.getProperty("timestamp.pattern");
	}

	/**
	 * This method gets Default Locale.
	 * @return Default Locale.
	 */
	public Locale getDefaultLocale()
	{
		return Locale.getDefault();
	}
}
