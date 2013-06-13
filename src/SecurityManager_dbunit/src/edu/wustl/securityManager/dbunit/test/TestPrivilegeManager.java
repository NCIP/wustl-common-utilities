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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import gov.nih.nci.security.authorization.domainobjects.User;
/**
 * test case for PrivilegeManager.
 * @author deepti_shelar
 *
 */
public class TestPrivilegeManager extends SecurityManagerBaseTestCase
{

	/**
	 * logger Logger - Generic logger.
	 */
	protected static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);
	
	PrivilegeManager privManager;
	static String configFile = "";
	private final String adminGroup = "ADMINISTRATOR_GROUP";

	public void setUp() throws Exception
	{

		privManager = PrivilegeManager.getInstance();
		removeAllUsers();
		insertSampleCSMUser();

		System.setProperty("javax.net.ssl.trustStore",
				"E://jboss-4.2.2.GA//server//default//conf//chap8.keystore");
		/*appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{
			//	cs.startSession("test", "test");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail();
			System.exit(1);
		}*/
		super.setUp();
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
	 * Inserts a sample User.
	 * @throws SMException e
	 */
	private void insertSampleCSMUser() throws SMException
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
		ISecurityManager securityManager;
		try
		{
			securityManager = SecurityManagerFactory.getSecurityManager();
			securityManager.createUser(user);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a sample User.
	 * @param name name
	 * @throws SMException e
	 */
	private void insertSampleCSMUser(String name) throws SMException
	{
		User user = new User();
		String newVal = name;
		user.setDepartment(newVal);
		user.setEmailId(newVal + "@test.com");
		user.setFirstName(newVal);
		user.setLoginName(newVal);
		user.setOrganization(newVal);
		user.setPassword(newVal);
		user.setTitle(newVal);
		user.setLastName(newVal);
		ISecurityManager securityManager;
		try
		{
			securityManager = SecurityManagerFactory.getSecurityManager();
			securityManager.createUser(user);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * testGetClasses.
	 */
	public void testGetClasses()
	{
		List<String> classes = privManager.getClasses();
		assertNotNull(classes);
	}

	/**
	 * testGetLazyObjects.
	 */
	public void testGetLazyObjects()
	{
		List<String> classes = privManager.getLazyObjects();
		assertEquals(0, classes.size());
	}

	/**
	 * testEagerObjects.
	 */
	public void testEagerObjects()
	{
		List<String> classes = privManager.getEagerObjects();
		assertNotNull(classes);
	}

	/**
	 * testGetAccesibleUsers.
	 */
	public void testGetAccesibleUsers()
	{
		String objectId = "edu.wustl.catissuecore.domain.Participant";
		String privilege = "QUERY";
		Set<String> classes;
		try
		{
			assignGroupToUser("test", adminGroup);

			classes = privManager.getAccesibleUsers(objectId, privilege);
			assertNotNull(classes);
			assertEquals(1, classes.size());
			insertSampleCSMUser("test1");
			assignGroupToUser("test1", "SUPERVISOR_GROUP");
			classes = privManager.getAccesibleUsers(objectId, privilege);
			assertNotNull(classes);
			assertEquals(2, classes.size());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

	/**
	 * getPrivilegeCaches.
	 */
	public void testGetPrivilegeCaches()
	{
		Collection<PrivilegeCache> classes = privManager.getPrivilegeCaches();
		assertNotNull(classes);
		assertEquals(0, classes.size());
	}

	/**
	 * testGetPrivilegeCacheLoginName.
	 */
	public void testGetPrivilegeCacheLoginName()
	{
		PrivilegeCache privilegeCache = privManager.getPrivilegeCache("test");
		assertNotNull(privilegeCache);
		assertEquals("test", privilegeCache.getLoginName());
	}

	/**
	 * testGetPrivilegeCacheLoginName..
	 */
	public void testRemovePrivilegeCache()
	{
		Collection<PrivilegeCache> classes1 = privManager.getPrivilegeCaches();
		assertNotNull(classes1);
		assertEquals(1, classes1.size());
		privManager.removePrivilegeCache("test");
		Collection<PrivilegeCache> classes = privManager.getPrivilegeCaches();
		assertNotNull(classes);
		assertEquals(0, classes.size());
	}

	/**
	 * getPrivilegeCaches.
	 */
	/*
		public void testHasGroupPrivilege()
		{
			String roleId = "1";
			String objectId = "edu.wustl.catissuecore.domain.Participant";
			String privilegeName = "QUERY";
			boolean hasPriv;
			try
			{
				hasPriv = privManager.hasGroupPrivilege(roleId, objectId, privilegeName);
				System.out.println("hasPriv  "+hasPriv);
				assertNotNull(hasPriv);
			}
			catch (CSObjectNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (CSException e)
			{
				e.printStackTrace();
			}
		}*/
	/**
	 * assigns the given group name to the user with the given login name.
	 * @param loginName login name
	 * @param groupName grp name
	 * @throws SMException e
	 */
	private void assignGroupToUser(String loginName, String groupName) throws SMException
	{
		ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
		User user = securityManager.getUser(loginName);
		String userId = user.getUserId().toString();
		securityManager.assignUserToGroup(groupName, userId);
	}

	/**
	 * testInsertAuthData.
	 */
	public void testInsertAuthData()
	{
		removeAllUsers();
		List authorizationData = new ArrayList();
		Set group = new HashSet();
		String userId = "";
		ISecurityManager securityManager;
		try
		{
			securityManager = SecurityManagerFactory.getSecurityManager();

			gov.nih.nci.security.authorization.domainobjects.User csmUser =
				new gov.nih.nci.security.authorization.domainobjects.User();
			SecurityDataBean userGroupRoleProtectionGroupBean;
			userGroupRoleProtectionGroupBean = new SecurityDataBean();
			userGroupRoleProtectionGroupBean.setUser(userId);
			userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
			userGroupRoleProtectionGroupBean.setGroupName(adminGroup);
			userGroupRoleProtectionGroupBean.setGroup(group);
			authorizationData.add(userGroupRoleProtectionGroupBean);
			Set protectionObjects = new HashSet();
			edu.wustl.catissuecore.domain.User usr = new edu.wustl.catissuecore.domain.User();
			usr.setLastName("dee12");
			usr.setId(new Long(5412));
			usr.setLoginName("dee21");
			usr.setEmailAddress("dee1@dee.com");
			csmUser.setLoginName(usr.getLoginName());
			csmUser.setLastName(usr.getLastName());
			csmUser.setFirstName(usr.getFirstName());
			csmUser.setEmailId(usr.getEmailAddress());
			csmUser.setStartDate(Calendar.getInstance().getTime());
			securityManager.createUser(csmUser);
			//assignGroupToUser(usr.getLoginName(), "PUBLIC_GROUP");
			protectionObjects.add(usr);
			final Map<String, String[]> protectionGroupsForObjectTypes =
				new HashMap<String, String[]>();
			protectionGroupsForObjectTypes.put(User.class.getName(),
					new String[]{"PUBLIC_DATA_GROUP"});
			edu.wustl.security.global.Constants.STATIC_PG_FOR_OBJ_TYPES
					.putAll(protectionGroupsForObjectTypes);
			//	String[] protectionGroups = securityManager.getProtectionGroupByName(usr);
			String[] protectionGroups = {"PUBLIC_DATA_GROUP"};
			privManager.insertAuthorizationData(authorizationData, protectionObjects,
					protectionGroups, usr.getObjectId());
		}
		catch (SMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
