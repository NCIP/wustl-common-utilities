
package edu.wustl.security.manager;

import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Utility;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;

/**
 * Factory to create an instance of ISecurityManager.
 * @author deepti_shelar
 *
 */
public class SecurityManagerFactory
{
	/**
	 * Returns the instance of SM.
	 * @return ISecurityManager sm instance
	 * @throws SMException smexc
	 */
	public static ISecurityManager getSecurityManager() throws SMException
	{
		String smClassName = SecurityManagerPropertiesLocator.getInstance()
				.getSecurityMgrClassName();
		ISecurityManager securityManager = null;
		if (smClassName == null)
		{
			Utility.getInstance().throwSMException(null, "Could not get the className ", "sm.operation.error");
		}
		else
		{
			securityManager = getSMInstance(smClassName);
		}
		return securityManager;
	}

	/**
	 * @param smClassName class name for SM.
	 * @throws SMException exc
	 * @return ISecurityManager sm instance
	 */
	private static ISecurityManager getSMInstance(String smClassName) throws SMException
	{
		ISecurityManager securityManager = null;
		try
		{
			Class className = Class.forName(smClassName);
			securityManager = (ISecurityManager) className.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			String message = "Expected SecurityManager class name is not provided in properties file";
			Utility.getInstance().throwSMException(e, message, "sm.operation.error");
		}
		catch (InstantiationException e)
		{
			String message = "Can not instantiate class ";
			Utility.getInstance().throwSMException(e, message, "sm.operation.error");
		}
		catch (IllegalAccessException e)
		{
			String message = "Illegal access to the class ";
			Utility.getInstance().throwSMException(e, message, "sm.operation.error");
		}
		return securityManager;
	}
}
