/*
 * Created on Jul 19, 2004
 *
 */

package edu.wustl.common.util.global;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import edu.wustl.common.util.logger.Logger;

/**
 * This class is used to retrieve values of keys from the ApplicationResources.properties file.
 * @author kapil_kaveeshwar
 */
public final class ApplicationProperties
{
	/**
	 * private constructor.
	 */
	private ApplicationProperties()
	{

	}
	/**
	 * Generic resource bundle.
	 */
	private static ResourceBundle bundle;

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ApplicationProperties.class);

	/**
	 * Load the resource bundle file.
	 * @param baseName Resource Bundle file name.
	 */
	public static void initBundle(String baseName)
	{
		bundle = ResourceBundle.getBundle(baseName);
	}

	/**
	 * @param theKey key in a application property file
	 * @return the value of key.
	 */
	public static String getValue(String theKey)
	{
		String val = CommonConstants.EMPTY_STRING;
		if (bundle == null)
		{
			LOGGER.fatal("resource bundle is null cannot return value for key " + theKey);
		}
		else
		{
			val = bundle.getObject(theKey).toString();
		}
		return val;
	}

	/**
	 * This method should be used when you want to
	 * customize error message with multiple replacement parameters.
	 *
	 * @param theKey - error key
	 * @param placeHolders - replacement Strings
	 * @return - complete error message
	 */
	public static String getValue(String theKey, List<String> placeHolders)
	{
		String msg = CommonConstants.EMPTY_STRING;
		if (bundle == null)
		{
			LOGGER.fatal("resource bundle is null cannot return value for key " + theKey);
		}
		else
		{
			msg = bundle.getObject(theKey).toString();
			StringBuffer message = new StringBuffer(msg);
			for (int i = 0; i < placeHolders.size(); i++)
			{
				message.replace(message.indexOf("{"),
						message.indexOf("}") + 1,placeHolders.get(i));
			}
			msg=message.toString();
		}
		return msg;
	}

	/**
	 * This method should be used when you want to customize error message with single replacement parameter.
	 * @param theKey - error key
	 * @param placeHolder - replacement Strings
	 * @return - complete error message
	 */
	public static String getValue(String theKey, String placeHolder)
	{
		List<String> temp = new ArrayList<String>();
		temp.add(placeHolder);
		return getValue(theKey, temp);
	}

}