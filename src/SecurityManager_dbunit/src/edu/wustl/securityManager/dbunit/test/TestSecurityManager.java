
package edu.wustl.securityManager.dbunit.test;

import java.util.List;

import org.junit.Test;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Constants;
import edu.wustl.security.manager.SecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * Test cases for SecurityManager class.
 * @author deepti_shelar
 */
public class TestSecurityManager extends SecurityManagerBaseTestCase
{

	
	/**
	 * logger Logger - Generic logger.
	 */
	protected static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);

	public void setUp() throws Exception
	{

		count++;
		securityManager = SecurityManagerFactory.getSecurityManager();
		removeAllUsers();
		insertSampleCSMUser();
		super.setUp();
	}

	

	/**
	 * Inserts a sample User.
	 * @throws SMException e
	 */
	private void insertSampleCSMUser() throws SMException
	{
		User user = new User();
		String newVal = loginName + count;
		user.setDepartment(newVal);
		user.setEmailId(newVal + "@test.com");
		user.setFirstName(newVal);
		user.setLoginName(newVal);
		user.setOrganization(newVal);
		user.setPassword(newVal);
		user.setTitle(newVal);
		user.setLastName(newVal);
		securityManager.createUser(user);
	}

	/**
	 * assigns the given group name to the user with the given login name.
	 * @param loginName login name
	 * @param groupName grp name
	 * @throws SMException e
	 */
	private void assignGroupToUser(String loginName, String groupName) throws SMException
	{
		User user = securityManager.getUser(loginName);
		String userId = user.getUserId().toString();
		securityManager.assignUserToGroup(groupName, userId);
	}

	/**
	 * Removes all users from the system.
	 */
	private void removeAllUsers()
	{
		try
		{
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
	 * Returns the user matching with the login name.
	 * @param loginName
	 *            name
	 * @return User
	 */
	private User getUserByLoginName(String loginName)
	{
		User user = null;
		try
		{
			user = securityManager.getUser(loginName);
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
		return user;
	}

	/**
	 * Test GetProtectionGroupByName.
	 */
	@Test
	public void testGetProtectionGroupByName()
	{
		AbstractDomainObject obj = new edu.wustl.catissuecore.domain.User();
		obj.setId(Long.valueOf(Constants.INDEX_ONE));

		try
		{
			String[] protGrp = securityManager.getProtectionGroupByName(obj);
			assertEquals("ADMINISTRATORS_DATA_GROUP", protGrp[0]);
			assertNotNull(protGrp);
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test Create user method.
	 */
	public void testCreateUser()
	{
		User user = new User();
		String newVal = loginName + ++count;
		user.setDepartment(newVal);
		user.setEmailId(newVal + "@test.com");
		user.setFirstName(newVal);
		user.setLoginName(newVal);
		user.setOrganization(newVal);
		user.setPassword(newVal);
		user.setTitle(newVal);
		try
		{
			securityManager.createUser(user);
			User addedUser = getUserByLoginName(newVal);
			assertEquals(newVal, addedUser.getLoginName());
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test Create user method.
	 */
	public void testCreateUserException()
	{
		User user = new User();
		String newVal = loginName + count;
		user.setDepartment(newVal);
		user.setEmailId(newVal + "@test.com");
		user.setFirstName(newVal);
		user.setLoginName(newVal);
		user.setOrganization(newVal);
		user.setPassword(newVal);
		user.setTitle(newVal);
		try
		{
			securityManager.createUser(user);
			User addedUser = getUserByLoginName(newVal);
			assertEquals(newVal, addedUser.getLoginName());
		}
		catch (SMException e)
		{
			logger.error("User already exists so cant create a new user" + e.getStackTrace());
		}
	}

	/**
	 * Test getUser method.
	 */
	public void testGetUser()
	{
		try
		{
			User user = securityManager.getUser(loginName + count);
			assertEquals(user.getLoginName(), loginName + count);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test getUser method.
	 */
	public void testGetUserException()
	{
		User user = null;
		try
		{
			user = securityManager.getUser(loginName + count);
		}
		catch (Exception e)
		{
			assertNull(user);
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test RemoveUser.
	 */
	public void testRemoveUser()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.removeUser(userId.toString());
			}
			allUsers = securityManager.getUsers();
			assertEquals(allUsers.size(), 0);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test RemoveUser.
	 */
	public void testRemoveUserException()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.removeUser(userId.toString() + "fail");
			}
			allUsers = securityManager.getUsers();
			assertEquals(allUsers.size(), 0);
		}
		catch (Exception e)
		{
			System.out.println("cant remove user");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test RemoveUser.
	 */
	public void testRemoveUserException1()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			System.setProperty("gov.nih.nci.security.configFile", null);
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.removeUser(userId.toString() + "fail");
			}
			allUsers = securityManager.getUsers();
			assertEquals(allUsers.size(), 0);
		}
		catch (Exception e)
		{
			System.out.println("cant remove user");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test getRoles().
	 */
	public void testGetRoles()
	{
		try
		{
			List<Role> roles = securityManager.getRoles();
			assertEquals(roles.size(), 4);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test getRoles().
	 */
	public void testGetRolesException()
	{
		try
		{
			System.setProperty("gov.nih.nci.security.configFile", null);
			List<Role> roles = securityManager.getRoles();
			assertEquals(roles.size(), 4);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test GetGroupIdForRole().
	 */
	public void testGetGroupIdForRole()
	{
		try
		{
			List<Role> roles = securityManager.getRoles();
			for (Role role : roles)
			{
				String groupIdForRole = securityManager.getGroupIdForRole(role.getId().toString());
				assertNotNull(groupIdForRole);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * testGetUserById.
	 */
	public void testGetUserById()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				User userById = securityManager.getUserById(userId.toString());
				assertNotNull(userById);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test Login method.
	 */
	public void testGetUserByIdException()
	{
		User userById = null;
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				userById = securityManager.getUserById(userId.toString() + "1");
				assertNotNull(userById);
			}
		}
		catch (SMException e)
		{
			assertNull(userById);
			System.out.println("****" + e.getMessage());
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test getUsers method.
	 */
	public void testGetUsers()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			assertEquals(allUsers.size(), 1);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test getUsers method.
	 */
	public void testGetUsersException()
	{
		try
		{
			System.setProperty("gov.nih.nci.security.configFile", null);
			List<User> allUsers = securityManager.getUsers();
			assertEquals(allUsers.size(), 1);
		}
		catch (Exception e)
		{
			System.out.println("error in get Users");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test assign user to group.
	 */
	public void testAssignUserToGroup()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.assignUserToGroup("TECHNICIAN_GROUP", userId.toString());
				String userGroup = securityManager.getRoleName(userId);
				assertEquals(userGroup, "Technician");
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test assign user to group.
	 */
	public void testAssignUserToGroupException()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				securityManager.assignUserToGroup("TECHNICIAN_GROUP", userId.toString() + "fail");
				String userGroup = securityManager.getRoleName(userId);
				assertEquals(userGroup, "Technician");
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test remove user from group.
	 */
	public void testRemoveUserFromGroup()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				assignGroupToUser(loginName + count, adminGroup);
				securityManager.removeUserFromGroup(adminGroup, userId.toString());
				String userGroup = securityManager.getRoleName(userId);
				assertEquals(userGroup, "");
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test modifyUser.
	 */
	public void testModifyUser()
	{
		try
		{
			User userByLoginName = getUserByLoginName(loginName + count);

			assertEquals(loginName + count, userByLoginName.getLastName());
			userByLoginName.setLoginName("modifiedLastName");
			securityManager.modifyUser(userByLoginName);
			User modifiedUser = getUserByLoginName("modifiedLastName");
			assertNotNull(modifiedUser);
			assertEquals("modifiedLastName", modifiedUser.getLoginName());
			assertEquals(loginName + count, modifiedUser.getLastName());
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test modifyUser.
	 */
	public void testModifyUserException()
	{
		try
		{
			User userByLoginName = getUserByLoginName(loginName + count);
			userByLoginName.setUserId(userByLoginName.getUserId() + 1);
			assertEquals(loginName + count, userByLoginName.getLastName());
			userByLoginName.setLoginName("modifiedLastName");
			securityManager.modifyUser(userByLoginName);
			User modifiedUser = getUserByLoginName("modifiedLastName");
			assertNotNull(modifiedUser);
			assertEquals("modifiedLastName", modifiedUser.getLoginName());
			assertEquals(loginName + count, modifiedUser.getLastName());
		}
		catch (SMException e)
		{
			System.out.println("exception in modifying");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test assignAdditionalGroupsToUser.
	 */
	public void testAssignAdditionalGroupsToUser()
	{
		User userByLoginName = getUserByLoginName(loginName + count);
		String[] groupIds = {"1"};
		try
		{
			assignGroupToUser(loginName + count, "PUBLIC_GROUP");
			securityManager.assignAdditionalGroupsToUser(userByLoginName.getUserId().toString(),
					groupIds);
			//	assertEquals(userByLoginName.getGroups().size(), 1);
			String userGroup = securityManager.getRoleName(userByLoginName.getUserId());
			assertEquals(userGroup, "Administrator");
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test assignAdditionalGroupsToUser.
	 */
	public void testAssignAdditionalGroupsToUserException()
	{
		User userByLoginName = getUserByLoginName(loginName + count);
		String[] groupIds = {"1"};
		try
		{
			//assignGroupToUser(loginName+count, "PUBLIC_GROUP");
			securityManager.assignAdditionalGroupsToUser(userByLoginName.getUserId().toString()
					+ "fail", groupIds);
			//	assertEquals(userByLoginName.getGroups().size(), 1);
			String userGroup = securityManager.getRoleName(userByLoginName.getUserId());
			assertEquals(userGroup, "Administrator");
		}
		catch (SMException e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * test AssignRoleToUser().
	 */

	public void testAssignRoleToUser()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				assignGroupToUser(user.getLoginName(), adminGroup);
				securityManager.assignRoleToUser(userId.toString(), "1");
				Role userRole = securityManager.getUserRole(userId);
				assertEquals("Administrator", userRole.getName());
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test testGetGroupIdForRoleFromExistingUser.
	 */

	public void testGetGroupIdForRoleFromExistingUser()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				assignGroupToUser(loginName + count, adminGroup);
				String roleID = securityManager.getUserRole(userId).getId().toString();
				String userGrp = securityManager.getGroupIdForRole(roleID);
				assertEquals(userGrp, "1");

			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test Login method.
	 */

	public void testLogin()
	{
		try
		{
			String val = loginName + count;
			boolean loginSuccess = securityManager.login(val, val);
			assertTrue(loginSuccess);
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test Login method fail.
	 */
	public void testLoginFail()
	{
		try
		{
			String val = loginName + count;
			boolean loginSuccess = securityManager.login(val + "fail", val);
			assertFalse(loginSuccess);
		}
		catch (Exception e)
		{
			System.out.println("exception in login");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test testGetRoleName1.
	 */

	public void testGetRoleName1()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				String userGrp = securityManager.getRoleName(userId + 1);
				assertNotNull(userGrp);
			}
		}
		catch (Exception e)
		{
			System.out.println("exception in getting role name");
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * Test testGetUserRole.
	 */

	public void testGetUserRole()
	{
		try
		{
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				assignGroupToUser(loginName + count, "PUBLIC_GROUP");
				Role userRole = securityManager.getUserRole(userId);
				assertNotNull(userRole);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * 	testGetUserRoleException.
	 */

	public void testGetUserRoleException()
	{
		Role userRole = null;
		
		try
		{
			System.setProperty("gov.nih.nci.security.configFile", null);
			List<User> allUsers = securityManager.getUsers();
			for (User user : allUsers)
			{
				Long userId = user.getUserId();
				//assignGroupToUser(loginName+count, "PUBLIC_GROUP");
				userRole = securityManager.getUserRole(userId);
			}
		}
		catch (Exception e)
		{
			assertNull(userRole);
			logger.error(e.getStackTrace());
		}
	}
	/**
	 * test getuserGroup()
	 */
	/*
		public void testGetRoleNameForAdmin() {
			try {
				User user = getUserByLoginName(loginName + count);
				assignGroupToUser(loginName + count, adminGroup);
				String userGroup = securityManager.getRoleName(user.getUserId());
				assertEquals("Administrator", userGroup);
			} catch (Exception e) {
				logger.error(e.getStackTrace());
			}
		}
		*//**
			 * test getuserGroup()
			 */
	/*
		public void testGetRoleNameForScientist() {
			try {
				User user = getUserByLoginName(loginName + count);
				String userGroup = "";
				assignGroupToUser(loginName + count, "PUBLIC_GROUP");
				userGroup = securityManager.getRoleName(user.getUserId());
				assertEquals("Scientist", userGroup);

			} catch (Exception e) {
				logger.error(e.getStackTrace());
			}
		}
		*//**
			 * test getuserGroup()
			 */
	/*
		public void testGetRoleNameForSupervisor() {
			try {
				User user = getUserByLoginName(loginName + count);
				assignGroupToUser(loginName + count, "SUPERVISOR_GROUP");
				String userGroup = securityManager.getRoleName(user.getUserId());
				assertEquals("Supervisor", userGroup);
			} catch (Exception e) {
				logger.error(e.getStackTrace());
			}
		}
		*//**
			 * test getuserGroup()
			 */
	/*
		public void testGetRoleNameForTech() {
			try {
				User user = getUserByLoginName(loginName + count);
				assignGroupToUser(loginName + count, "TECHNICIAN_GROUP");
				String userGroup = securityManager.getRoleName(user.getUserId());
				assertEquals("Technician", userGroup);
			} catch (Exception e) {
				logger.error(e.getStackTrace());
			}
		}*/
}
