/**
 * Utility class for methods related to CSM
 */

package edu.wustl.security.privilege;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Identifiable;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Constants;
import edu.wustl.security.global.ProvisionManager;
import edu.wustl.security.global.Utility;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ApplicationSearchCriteria;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * Utility class for methods related to CSM.
 * @author ravindra_jain
 *
 */
public class PrivilegeUtility
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(PrivilegeUtility.class);
	/**
	 * instance of SecurityManager.
	 */
	private static ISecurityManager securityManager = null;

	/**
	 * PrivilegeUtility.
	 */
	public PrivilegeUtility()
	{
		try
		{
			securityManager = SecurityManagerFactory.getSecurityManager();
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * This method creates protection elements corresponding to protection
	 * objects passed and associates them with static as well as dynamic
	 * protection groups that are passed. It also creates user group, role,
	 * protection group mapping for all the elements in authorization data.
	 *
	 * @param authorizationData
	 *            Vector of SecurityDataBean objects
	 * @param protectionObjects
	 *            Set of AbstractDomainObject instances
	 * @param dynamicGroups
	 *            Array of dynamic group names
	 * @throws SMException sme
	 */
	public void insertAuthorizationData(List authorizationData, Set protectionObjects,
			String[] dynamicGroups) throws SMException
			{
		//Create protection elements corresponding to all protection
		Set protElems = createProtectionElementsFromProtectionObjects(protectionObjects);
		//Create user group role protection group and their mappings if
		if (authorizationData != null)
		{
			createUserGroupRoleProtectionGroup(authorizationData, protElems);
		}

		//Assigning protection elements to dynamic groups
		assignProtectionElementsToGroups(protElems, dynamicGroups);
			}

	/**
	 * This method creates protection elements from the protection objects
	 * passed and associate them with respective static groups they should be
	 * added to depending on their class name if the corresponding protection
	 * element does not already exist.
	 *
	 * @param protectionObjects objs
	 * @return set elems
	 * @throws SMException e
	 */
	private Set createProtectionElementsFromProtectionObjects(
			Set<AbstractDomainObject> protectionObjects) throws SMException
			{
		ProtectionElement protElems;
		Set<ProtectionElement> pElements = new HashSet<ProtectionElement>();
		Identifiable protectionObject;
		Iterator<AbstractDomainObject> iterator;
		UserProvisioningManager upManager;
		upManager = getUserProvisioningManager();

		if (protectionObjects != null)
		{
			for (iterator = protectionObjects.iterator(); iterator.hasNext();)
			{
				protElems = new ProtectionElement();
				protectionObject = (Identifiable) iterator.next();
				protElems.setObjectId(protectionObject.getObjectId());
				populateProtectionElement(protElems, protectionObject, upManager);
				pElements.add(protElems);
			}
		}
		return pElements;
			}

	/**
	 * This method creates user group, role, protection group mappings in
	 * database for the passed authorizationData. It also adds protection
	 * elements to the protection groups for which mapping is made. For each
	 * element in authorization Data passed: User group is created and users are
	 * added to user group if one does not exist by the name passed. Similarly
	 * Protection Group is created and protection elements are added to it if
	 * one does not exist. Finally user group and protection group are
	 * associated with each other by the role they need to be associated with.
	 * If no role exists by the name an exception is thrown and the
	 * corresponding mapping is not created
	 *
	 * @param authorizationData list
	 * @param protElems elems
	 * @throws SMException sme
	 */
	private void createUserGroupRoleProtectionGroup(List authorizationData, Set protElems)
			throws SMException
	{
		ProtectionGroup protectionGroup = null;
		SecurityDataBean bean;
		String[] roleIds = null;
		Group group = null;
		UserProvisioningManager upManager = getUserProvisioningManager();
		if (authorizationData != null)
		{
			for (int i = 0; i < authorizationData.size(); i++)
			{

				try
				{
					bean = (SecurityDataBean) authorizationData.get(i);
					group = getNewGroupObject(bean);
					group = getGroupObject(group);
					assignGroupToUsersInUserGroup(bean, group);
					protectionGroup = getNewProtectionGroupObj(bean);
					protectionGroup = addProtElementToGroup(protectionGroup, protElems);
					roleIds = new String[Constants.INDEX_ONE];
					roleIds[0] = getRoleId(bean);
					upManager.assignGroupRoleToProtectionGroup(String.valueOf(protectionGroup
							.getProtectionGroupId()),
							String.valueOf(group.getGroupId()), roleIds);
				}
				catch (CSTransactionException ex)
				{
					StringBuffer mess = new StringBuffer(
							"Error occured Assigned Group Role " +
							"To Protection Group ").append(
							protectionGroup.getProtectionGroupId()).append(' ').append(
							group.getGroupId()).append(' ').append(roleIds);
					Utility.getInstance().throwSMException(ex, mess.toString(), "sm.operation.error");
				}
			}
		}
	}

	/**
	 * @param bean SecurityDataBean
	 * @return String role id
	 * @throws SMException sme
	 */
	private String getRoleId(SecurityDataBean bean) throws SMException
	{
		Role role = new Role();
		role.setName(bean.getRoleName());
		RoleSearchCriteria criteria = new RoleSearchCriteria(role);
		List list = getObjects(criteria);
		return String.valueOf(((Role) list.get(0)).getId());
	}

	/**
	 * If Protection group already exists add protection elements to the group
	 * If the protection group does not already exist create the protection group
	 * and add protection elements to it.
	 * @param protectionGroup pGroups
	 * @param protElems elems
	 * @return ProtectionGroup grp
	 * @throws SMException exc
	 */
	private ProtectionGroup addProtElementToGroup(ProtectionGroup protectionGroup, Set protElems)
			throws SMException
			{
		ProtectionGroup protGroup = protectionGroup;
		ProtectionGroupSearchCriteria searchCriteria = new ProtectionGroupSearchCriteria(protGroup);
		UserProvisioningManager upManager = getUserProvisioningManager();
		List<ProtectionGroup> list = upManager.getObjects(searchCriteria);
		try
		{
			if (null == list || list.size() <= 0)
			{
				protGroup.setProtectionElements(protElems);
				upManager.createProtectionGroup(protGroup);
			}
			else
			{
				protGroup = (ProtectionGroup) list.get(0);
			}
		}
		catch (CSTransactionException e)
		{
			String mess = "Error in creating protection group in addProtElementToGroup"+e.getMessage();
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return protGroup;
	}

	/**
	 * @param bean {@link SecurityDataBean}
	 * @return {@link ProtectionGroup}
	 * @throws SMException e
	 */
	private ProtectionGroup getNewProtectionGroupObj(SecurityDataBean bean) throws SMException
	{
		ProtectionGroup protectionGroup;
		protectionGroup = new ProtectionGroup();
		protectionGroup.setApplication(getApplication(SecurityManagerPropertiesLocator
				.getInstance().getApplicationCtxName()));
		protectionGroup.setProtectionGroupName(bean.getProtGrpName());
		return protectionGroup;
	}

	/**
	 * @param bean bean
	 * @param group Group
	 * @throws SMException ex
	 */
	private void assignGroupToUsersInUserGroup(SecurityDataBean bean,
			Group group) throws SMException
	{
		User user;
		Set userGroup = bean.getGroup();
		
		if(userGroup != null)
		{
			Iterator it = userGroup.iterator();
			while(it.hasNext())
			{
				user = (User) it.next();
				if(user != null)
				{
					assignAdditionalGroupsToUser(String.valueOf(user.getUserId()), new String[]{String
						.valueOf(group.getGroupId())});
				}
			
			}
		}
	}

	/**
	 * @param bean bean
	 * @return Group grp
	 * @throws SMException exc
	 */
	private Group getNewGroupObject(SecurityDataBean bean)
			throws SMException
	{
		Group group = new Group();
		group.setApplication(getApplication(SecurityManagerPropertiesLocator.getInstance()
				.getApplicationCtxName()));
		group.setGroupName(bean.getGroupName());
		return group;
	}

	/**
	 * @param group group
	 * @return Group
	 * @throws SMException exc
	 */
	private Group getGroupObject(Group group) throws SMException
	{
		Group grp = null;
		GroupSearchCriteria grpSrchCri = new GroupSearchCriteria(group);
		UserProvisioningManager upManager = getUserProvisioningManager();
		List<Group> list = upManager.getObjects(grpSrchCri);
		if (null == list || list.size() <= 0)
		{
			try
			{
				upManager.createGroup(group);
				list = getObjects(grpSrchCri);
				//grp = (Group) list.get(0);
			}
			catch (CSTransactionException e)
			{
				String mess = "error in creating group object "+e.getMessage();
				Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
			}
		}
		grp = (Group) list.get(0);
	
		return grp;
	}

	/**
	 * This method assigns Protection Elements passed to the Protection group
	 * names passed.
	 *
	 * @param protElems pElems
	 * @param groups groups
	 * @throws SMException e
	 */
	private void assignProtectionElementsToGroups(Set<ProtectionElement>
	protElems, String[] groups) throws SMException
	{
		ProtectionElement protectionElement;
		Iterator<ProtectionElement> iterator;
		if (groups != null)
		{
			for (int i = 0; i < groups.length; i++)
			{
				for (iterator = protElems.iterator(); iterator.hasNext();)
				{
					protectionElement = (ProtectionElement) iterator.next();
					assignProtectionElementToGroup(protectionElement, groups[i]);
				}
			}
		}
	}

	/**
	 * @param protectionElement eElement
	 * @param protectionObject abstarctDomainObj
	 * @param upManager userProvManager
	 * @throws SMException exc
	 */
	private void populateProtectionElement(ProtectionElement protectionElement,
			Identifiable protectionObject, UserProvisioningManager upManager)
			throws SMException
	{
		try
		{
			protectionElement.setApplication(getApplication(SecurityManagerPropertiesLocator
					.getInstance().getApplicationCtxName()));
			protectionElement.setProtectionElementDescription(protectionObject.getClass().getName()
					+ " object");
			protectionElement.setProtectionElementName(protectionObject.getObjectId());

			String[] staticGroups = (String[]) edu.wustl.security.global.Constants.
			STATIC_PG_FOR_OBJ_TYPES
					.get(protectionObject.getClass().getName());
			setProtectGroups(protectionElement, staticGroups);
			upManager.createProtectionElement(protectionElement);
		}
		catch (CSTransactionException ex)
		{
			String mess = "Error occured while creating Potection Element "
					+ protectionElement.getProtectionElementName();
			logger.warn(mess, ex);
			Utility.getInstance().throwSMException(ex, mess, "sm.operation.error");
			//throw new CSException(mess, ex);
		}
		catch(CSException ex)
		{
			String mess = "Error occured while creating Potection Element "
				+ protectionElement.getProtectionElementName();
			logger.warn(mess, ex);
			Utility.getInstance().throwSMException(ex, mess, "sm.operation.error");
			//throw new CSException(mess, ex);
		}

	}

	/**
	 * @param userId string
	 * @param groupIds array
	 * @throws SMException exc
	 */
	public void assignAdditionalGroupsToUser(String userId, String[] groupIds) throws SMException
	{
		securityManager.assignAdditionalGroupsToUser(userId, groupIds);
	}

	/**
	 * Returns list of objects corresponding to the searchCriteria passed.
	 *
	 * @param searchCriteria criteria for search
	 * @return List of resultant objects
	 * @throws SMException
	 *             if searchCriteria passed is null or if search results in no
	 *             results
	 * @throws SMException exc
	 */
	public List getObjects(SearchCriteria searchCriteria) throws SMException
	{
		return ProvisionManager.getInstance().getObjects(searchCriteria);
	}

	/**
	 * @param protectionElement elemnt
	 * @param groupsName name
	 * @throws SMException e
	 */
	private void assignProtectionElementToGroup(ProtectionElement protectionElement,
			String groupsName) throws SMException
	{
		try
		{
			UserProvisioningManager upManager = getUserProvisioningManager();
			upManager.assignProtectionElement(groupsName, protectionElement.getObjectId());
		}
		catch (CSException e)
		{
			StringBuffer mess = new StringBuffer(
					"The Security Service encountered" +
					" an error while associating protection group:")
					.append(groupsName).append(" to protectionElement").append(
							protectionElement.getProtectionElementName());
		}
	}

	/**
	 * @param protectionElement element
	 * @param staticGroups groups
	 * @throws CSException exc
	 */
	private void setProtectGroups(ProtectionElement protectionElement, String[] staticGroups)
			throws CSException
	{
		ProtectionGroup protectionGroup;
		Set<ProtectionGroup> protectionGroups = null;
		ProtectionGroupSearchCriteria pgSearchCriteria;
		if (staticGroups != null)
		{
			protectionGroups = new HashSet<ProtectionGroup>();
			for (int i = 0; i < staticGroups.length; i++)
			{
				protectionGroup = new ProtectionGroup();
				protectionGroup.setProtectionGroupName(staticGroups[i]);
				pgSearchCriteria = new ProtectionGroupSearchCriteria(protectionGroup);
				try
				{
					List<ProtectionGroup> list = getObjects(pgSearchCriteria);
					protectionGroup = (ProtectionGroup) list.get(0);
					protectionGroups.add(protectionGroup);
				}
				catch (SMException sme)
				{
					logger.warn("Error occured while retrieving " + staticGroups[i]
							+ "  From Database: ", sme);
				}
			}
			protectionElement.setProtectionGroups(protectionGroups);
		}
	}

	/**
	 * @param applicationName app Name
	 * @return Application
	 * @throws SMException exc
	 */
	public Application getApplication(String applicationName) throws SMException
	{
		Application application = new Application();
		application.setApplicationName(applicationName);
		ApplicationSearchCriteria appnSearchCri = new ApplicationSearchCriteria(application);
		application = (Application) getUserProvisioningManager().getObjects(appnSearchCri).get(0);
		return application;
	}

	/**
	 * Returns the UserProvisioningManager singleton object.
	 * @return UserProvisioningManager instance
	 * @throws SMException e
	 */
	public UserProvisioningManager getUserProvisioningManager() throws SMException
	{
		return ProvisionManager.getInstance().getUserProvisioningManager();
	}

	/**
	 * Returns the Authorization Manager for the caTISSUE Core. This method follows
	 * the singleton pattern so that only one AuthorizationManager is created.
	 * @return AuthorizationManager
	 * @throws CSException common security exception
	 */
	protected AuthorizationManager getAuthorizationManager() throws SMException
	{
		return ProvisionManager.getInstance().getAuthorizationManager();
	}

	/**
	 * This method returns the User object from the database for the passed
	 * User's Login Name. If no User is found then null is returned.
	 *
	 * @param loginName Login name of the user
	 * @return User user
	 * @throws SMException exc
	 */
	public User getUser(String loginName) throws SMException
	{
		return securityManager.getUser(loginName);
	}

	/**
	 * Returns the User object for the passed User id.
	 *
	 * @param userId -
	 *            The id of the User object which is to be obtained
	 * @return The User object from the database for the passed User id
	 * @throws SMException
	 *             if the User object is not found for the given id
	 */
	public User getUserById(String userId) throws SMException
	{
		return securityManager.getUserById(userId);
	}

	/**
	 * This method returns role corresponding to the rolename passed.
	 *
	 * @param roleName name of the role
	 * @return Role role
	 * @throws SMException exc
	 */
	public Role getRole(String roleName) throws SMException
	{
		if (roleName == null)
		{
			String mess = "Role name passed is null";
			Utility.getInstance().throwSMException(null, mess, "sm.operation.error");
		}

		//Search for role by the name roleName
		Role role = new Role();
		role.setName(roleName);
		role.setApplication(getApplication(SecurityManagerPropertiesLocator.getInstance()
				.getApplicationCtxName()));
		RoleSearchCriteria roleSearchCriteria = new RoleSearchCriteria(role);
		List<Role> list;
		try
		{
			list = getObjects(roleSearchCriteria);
			role = (Role) list.get(0);
		}
		catch (SMException e)
		{
			String mess = "Role not found by name " + roleName;
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return role;
	}

	/**
	 * given a role id it returns privileges.
	 * @param roleId id
	 * @return set of privs
	 * @throws SMException exc
	 */
	public Set<Privilege> getRolePrivileges(String roleId) throws SMException
	{
		Set privileges = null;
		try
		{
			privileges = getUserProvisioningManager().getPrivileges(roleId);
		}
		catch (CSObjectNotFoundException e)
		{
			String mess = "Error in getting RolePrivileges"+e.getMessage();
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return privileges ;
	}

	/**
	 * This method returns protection group corresponding to the naem passed. In
	 * case it does not exist it creates one and returns that.
	 * @param pgName string
	 * @throws SMException exception
	 * @return ProtectionGroup grp
	 */
	public ProtectionGroup getProtectionGroup(String pgName) throws SMException
	{
		if (pgName == null)
		{
			String mess = "pgName passed is null";
			Utility.getInstance().throwSMException(null, mess, "sm.operation.error");
		}
		ProtectionGroup protGrp = null;
		//Search for Protection Group of the name passed
		ProtectionGroupSearchCriteria pgSearchCriteria;
		ProtectionGroup protectionGroup;
		protectionGroup = new ProtectionGroup();
		protectionGroup.setProtectionGroupName(pgName);
		pgSearchCriteria = new ProtectionGroupSearchCriteria(protectionGroup);
		UserProvisioningManager upManager = null;
		List<ProtectionGroup> list;
		try
		{
			upManager = getUserProvisioningManager();
			list = getObjects(pgSearchCriteria);
			protGrp = (ProtectionGroup) list.get(0);
		}
		catch (SMException e)
		{
			logger.debug("Protection Group not found by name " + pgName);
			try
			{
				upManager.createProtectionGroup(protectionGroup);
				list = getObjects(pgSearchCriteria);
				protGrp = (ProtectionGroup) list.get(0);
			}
			catch (CSTransactionException e1)
			{
				String mess = "error in creating protection grp for name "+pgName;
				Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
			}
		}
		return protGrp;
	}
	/**
	 * @param roleID string
	 * @return groupid string
	 * @throws SMException exc
	 */
	public String getGroupIdForRole(String roleID) throws SMException
	{
		return securityManager.getGroupIdForRole(roleID);
	}
	/**
	 * @param privilegeId priv Id
	 * @return Privilege priv
	 * @throws SMException e
	 */
	public Privilege getPrivilegeById(String privilegeId) throws SMException
	{
		Privilege privilegeById = null;
		try
		{
			privilegeById = getUserProvisioningManager().getPrivilegeById(privilegeId);
		}
		catch (CSObjectNotFoundException e)
		{
			String mess = "Error in getting RolePrivileges"+e.getMessage();
			Utility.getInstance().throwSMException(e, mess, "sm.operation.error");
		}
		return privilegeById;
	}
}
