
package edu.wustl.security.global;

import java.util.List;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

/**
 * Class to provide required objects from csm apis.
 * @author deepti_shelar
 *
 */
public final class ProvisionManager
{

	/**
	 * provManager.
	 */
	private static ProvisionManager provManager = new ProvisionManager();

	/**
	 * pri constructor.
	 */
	private ProvisionManager()
	{

	}

	/**
	 * @return ProvisionManager single instance
	 */
	public static ProvisionManager getInstance()
	{
		return provManager;
	}

	/**
	 * logger Logger - Generic logger.
	 */
	private final org.apache.log4j.Logger logger = Logger.getLogger(ProvisionManager.class);
	/**
	 * authTManager.
	 */
	private AuthenticationManager authTManager = null;
	/**
	 * authRManager.
	 */
	private AuthorizationManager authRManager = null;

	/**
	 * Returns the UserProvisioningManager singleton object.
	 *
	 * @return UserProvisioningManager up
	 * @throws SMException exc
	 */
	public UserProvisioningManager getUserProvisioningManager() throws SMException
	{
		UserProvisioningManager upManager = null;
		upManager = (UserProvisioningManager) getAuthorizationManager();
		return upManager;
	}

	/**
	 * Returns the AuthenticationManager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthenticationManager is
	 * created for the caTISSUE Core.
	 *
	 * @return AuthenticationManager au
	 * @throws CSException exc
	 */
	public AuthenticationManager getAuthenticationManager() throws CSException
	{
		if (authTManager == null)
		{
			authTManager = SecurityServiceProvider
					.getAuthenticationManager(SecurityManagerPropertiesLocator.getInstance()
							.getApplicationCtxName());
		}
		return authTManager;
	}

	/**
	 * Returns the Authorization Manager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthorizationManager is
	 * created.
	 *
	 * @return AuthenticationManager au
	 * @throws	SMException exc
	 */
	public AuthorizationManager getAuthorizationManager() throws SMException
	{

		if (authRManager == null)
		{
			try
			{
				authRManager = SecurityServiceProvider
						.getAuthorizationManager(
								SecurityManagerPropertiesLocator.getInstance()
								.getApplicationCtxName());
			}
			catch (CSConfigurationException e)
			{
				String mess = "error in geting getAuthorizationManager " + e.getMessage();
				Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
			}
			catch (CSException e)
			{
				String mess = "error in geting getAuthorizationManager " + e.getMessage();
				Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
			}
		}

		return authRManager;
	}

	/**
	* Returns group id from Group name.
	* @param groupName name
	* @return String str
	* @throws SMException  exc
	*/
	public String getGroupID(final String groupName) throws SMException
	{
		List<Group> list;
		String groupId = null;
		Group group = new Group();
		group.setGroupName(groupName);
		UserProvisioningManager upManager = getUserProvisioningManager();
		SearchCriteria searchCriteria = new GroupSearchCriteria(group);
		String appCtxName = SecurityManagerPropertiesLocator.getInstance().getApplicationCtxName();
		try
		{
			group.setApplication(upManager.getApplication(appCtxName));
			list = getObjects(searchCriteria);
			if (!list.isEmpty())
			{
				group = (Group) list.get(0);
				groupId = group.getGroupId().toString();
			}
		}
		catch (CSObjectNotFoundException e)
		{
			String mess = "error in geting application according to app context name " + appCtxName
					+ " " + e.getMessage();
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return groupId;
	}

	/**
	 * Returns role id from role name.
	 * @param roleName name
	 * @return String roleid
	 * @throws SMException exc
	 */
	public String getRoleID(final String roleName) throws SMException
	{
		String roleId = null;
		Role role = new Role();
		role.setName(roleName);
		SearchCriteria searchCriteria = new RoleSearchCriteria(role);
		UserProvisioningManager upManager = getUserProvisioningManager();
		String appCtxName = SecurityManagerPropertiesLocator.getInstance().getApplicationCtxName();
		try
		{
			role.setApplication(upManager.getApplication(appCtxName));
			List list = getObjects(searchCriteria);
			if (!list.isEmpty())
			{
				role = (Role) list.get(0);
				roleId = role.getId().toString();
			}
		}
		catch (CSObjectNotFoundException e)
		{
			String mess = "error in geting application according to app context name " + appCtxName
					+ " " + e.getMessage();
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return roleId;
	}

	/**
	 * Returns list of objects corresponding to the searchCriteria passed.
	 * @param searchCriteria cr
	 * @return List of resultant objects
	 * @throws SMException if searchCriteria passed is null or if search results in no results
	 */
	public List getObjects(SearchCriteria searchCriteria) throws SMException
	{
		if (null == searchCriteria)
		{
			logger.debug("searchCriteria is null");
			String mesg = "searchCriteria is null";
			Utility.getInstance().throwSMException(null, mesg, "sm.operation.error");
		}
		UserProvisioningManager upManager = getUserProvisioningManager();
		List list = upManager.getObjects(searchCriteria);
		if (null == list || list.size() <= 0)
		{
			logger.warn("Search resulted in no results");
		}
		return list;
	}
}
