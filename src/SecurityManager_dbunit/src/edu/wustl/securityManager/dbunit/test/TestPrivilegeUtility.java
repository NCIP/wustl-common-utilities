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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.RoleSearchCriteria;
/**
 * Test case for PrivilegeUtility.
 * @author deepti_shelar
 *
 */
public class TestPrivilegeUtility extends SecurityManagerBaseTestCase
{
	/**
	 * privilegeUtility.
	 */
	PrivilegeUtility privilegeUtility;
	/**
	 * adminGroup.
	 */
	private final String adminGroup = "ADMINISTRATOR_GROUP";
	/**
	 * configFile.
	 */
	static String configFile = "";
	/**
	 * logger Logger - Generic logger.
	 */
	protected static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);
	/**
	 * set up.
	 * @throws Exception e
	 */
	public void setUp() throws Exception
	{
		privilegeUtility = new PrivilegeUtility();
	}

	/**
	 * testGetRole.
	 */
	public void testGetRole()
	{
		String roleName = "Administrator";
		try
		{
			Role role = privilegeUtility.getRole(roleName);
			assertEquals("Administrator", role.getName());
			assertEquals("1", role.getId().toString());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}


	/**
	 * testGetRolePrivileges.
	 */
	public void testGetRolePrivileges()
	{
		String roleId = "1";
		try
		{
			Set<Privilege> rolePrivileges = privilegeUtility.getRolePrivileges(roleId);
			assertEquals(24, rolePrivileges.size());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * testGetUserProvisioningManager.
	 */
	public void testGetUserProvisioningManager()
	{
		try
		{
			UserProvisioningManager upManager = privilegeUtility.getUserProvisioningManager();
			assertNotNull(upManager);
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * 
	 */
	public void testGetUserById()
	{
		removeAllUsers();
		insertSampleCSMUser();
		try
		{
			ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				User user1 = privilegeUtility.getUserById(userId.toString());
				assertNotNull(user1);
				assertEquals("test", user1.getLastName());
			}
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
		finally
		{
			removeAllUsers();
		}
	}

	/**
	 * 
	 */
	public void testGetUser()
	{
		removeAllUsers();
		insertSampleCSMUser();
		try
		{
			User user1 = privilegeUtility.getUser("test");
			assertNotNull(user1);
			assertEquals("test", user1.getLastName());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
		finally
		{
			removeAllUsers();
		}
	}

	/**
	 * 
	 */
	public void testGetProtectionGroup()
	{
		try
		{
			ProtectionGroup protectionGroup = privilegeUtility
					.getProtectionGroup("ADMINISTRATOR_PROTECTION_GROUP");
			assertNotNull(protectionGroup);
			assertEquals("ADMINISTRATOR_PROTECTION_GROUP", protectionGroup.getProtectionGroupName());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * 
	 */
	public void testGetGroupIdForRole()
	{
		String grpId;
		try
		{
			grpId = privilegeUtility.getGroupIdForRole("1");
			assertNotNull(grpId);
			assertEquals("1", grpId);
		}
		catch (SMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void testGetObjects()
	{
		Role role = new Role();
		role.setName("");
		RoleSearchCriteria criteria = new RoleSearchCriteria(role);
		List<Role> list;
		try
		{
			list = privilegeUtility.getObjects(criteria);
			for (Role role1 : list)
			{
				System.out.println("getName() " + role1.getName());
			}
			assertNotNull(list);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void testGetObjectsNull()
	{
		Role role = new Role();
		role.setName("");
		List<Role> list;
		try
		{
			list = privilegeUtility.getObjects(null);
			for (Role role1 : list)
			{
				System.out.println("getName() " + role1.getName());
			}
			assertNotNull(list);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void testGetPrivilegeById()
	{
		try
		{
			Privilege priv = privilegeUtility.getPrivilegeById("1");
			assertNotNull(priv);
			assertEquals("CREATE", priv.getName());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * 
	 */
	public void testAssignAdditionalGroupsToUser()
	{
		removeAllUsers();
		insertSampleCSMUser();
		try
		{
			String[] groupIds = {"3", "4"};
			ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
			User user = securityManager.getUser("test");
			privilegeUtility.assignAdditionalGroupsToUser(user.getUserId().toString(), groupIds);
			/*Set<Group> groups = user.getGroups();
			for (Group object : groups) {
				System.out.println(object.getGroupName());
			}*/
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * 
	 */
	public void testGetApplication()
	{
		try
		{
			Application application = privilegeUtility.getApplication("catissuecore");
			assertNotNull(application);
			assertEquals("catissuecore", application.getApplicationName());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Inserts a sample User.
	 * @throws SMException 
	 * 
	 * @throws Exception
	 */
	private void insertSampleCSMUser()
	{
		User user = new User();
		String newVal = "test";
		user.setDepartment(newVal);
		user.setEmailId(newVal + "@test.com");
		user.setFirstName(newVal);
		user.setLoginName(newVal);
		user.setOrganization(newVal);
		user.setPassword(newVal);
		user.setTitle(newVal);
		user.setLastName(newVal);
		try
		{
			SecurityManagerFactory.getSecurityManager().createUser(user);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Removes all users from the system.
	 */
	private void removeAllUsers()
	{
		try
		{
			ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.removeUser(userId.toString());
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}
	/**
	 * testInsertAuthData.
	 */
	public void testInsertAuthData()
	{
		removeAllUsers();
		Vector authorizationData = new Vector();
		Set group = new HashSet();
		String userId = "";
		ISecurityManager securityManager;
		try
		{
			securityManager = SecurityManagerFactory.getSecurityManager();
			gov.nih.nci.security.authorization.domainobjects.User csmUser 
			= new gov.nih.nci.security.authorization.domainobjects.User();
			SecurityDataBean userGroupRoleProtectionGroupBean;
			userGroupRoleProtectionGroupBean = new SecurityDataBean();
			userGroupRoleProtectionGroupBean.setUser(userId);
			userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
			userGroupRoleProtectionGroupBean.setGroupName(adminGroup);
			userGroupRoleProtectionGroupBean.setGroup(group);
			authorizationData.add(userGroupRoleProtectionGroupBean);
			PrivilegeUtility util = new PrivilegeUtility();
			Set protectionObjects = new HashSet();
			edu.wustl.catissuecore.domain.User usr = new edu.wustl.catissuecore.domain.User();
			usr.setLastName("dee1");
			usr.setId(new Long(551));
			usr.setLoginName("dee11");
			usr.setEmailAddress("dee1@dee.com");
			csmUser.setLoginName(usr.getLoginName());
			csmUser.setLastName(usr.getLastName());
			csmUser.setFirstName(usr.getFirstName());
			csmUser.setEmailId(usr.getEmailAddress());
			csmUser.setStartDate(Calendar.getInstance().getTime());
			securityManager.createUser(csmUser);
			protectionObjects.add(usr);
			final Map<String, String[]> protectionGroupsForObjectTypes =
				new HashMap<String, String[]>();
			protectionGroupsForObjectTypes.put(User.class.getName(),
					new String[]{"PUBLIC_DATA_GROUP"});
			edu.wustl.security.global.Constants.STATIC_PG_FOR_OBJ_TYPES
					.putAll(protectionGroupsForObjectTypes);
			String[] protectionGroups = {"PUBLIC_DATA_GROUP"};
			util.insertAuthorizationData(authorizationData, protectionObjects, protectionGroups);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}
}
