
package edu.wustl.securityManager.dbunit.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.impl.AuthorizationDAOImpl;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.system.ApplicationSessionFactory;
/**
 * Test class for AuthorizationDAOImpl.
 * @author deepti_shelar
 *
 */
public class TestAuthorizationDAOImpl extends SecurityManagerBaseTestCase
{

	/**
	 * logger Logger - Generic logger.
	 */
	protected static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);

	AuthorizationDAOImpl impl;
	private transient ISecurityManager securityManager = null;
	static String configFile = "";
	private final String adminGroup = "ADMINISTRATOR_GROUP";
	private final String publicGroup = "PUBLIC_GROUP";

	public void setUp()
	{
		String ctxName = SecurityManagerPropertiesLocator.getInstance().getApplicationCtxName();
		SessionFactory sFactory;
		try
		{
			sFactory = ApplicationSessionFactory.getSessionFactory(ctxName);
			impl = new AuthorizationDAOImpl(sFactory, ctxName);
			securityManager = SecurityManagerFactory.getSecurityManager();

			removeAllUsers();
			insertSampleCSMUser();

			super.setUp();

		}
		catch (CSConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Returns the user matching with the login name.
	 * @param loginName name
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
		securityManager.createUser(user);
	}

	/**
	 * assigns the given group name to the user with the given login name.
	 * @param loginName login
	 * @param groupName name
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
	 * 
	 */
	public void testGetGroup()
	{
		try
		{
			assignGroupToUser("test", adminGroup);
			assignGroupToUser("test", publicGroup);
			User user = getUserByLoginName("test");
			Set groups = impl.getGroups(user.getUserId().toString());
			assertEquals(2, groups.size());
		}
		catch (CSObjectNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void testGetPrivilegeMapForUser()
	{
		try
		{
			assignGroupToUser("test", adminGroup);
			assignGroupToUser("test", publicGroup);
			User user = getUserByLoginName("test");
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			ProtectionElement protectionElement = new ProtectionElement();
			protectionElement.setObjectId("edu.wustl.catissuecore.domain.User_1");
			ProtectionElementSearchCriteria protEleSearchCrit = new ProtectionElementSearchCriteria(
					protectionElement);
			List<ProtectionElement> list = privilegeUtility.getUserProvisioningManager()
					.getObjects(protEleSearchCrit);

			List<ObjectPrivilegeMap> map = impl.getPrivilegeMap(user.getLoginName(), list);
			assertNotNull(map);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
		catch (CSException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void testGetPrivilegeMapForSite()
	{
		try
		{
			assignGroupToUser("test", adminGroup);
			assignGroupToUser("test", publicGroup);
			User user = getUserByLoginName("test");
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			ProtectionElement protectionElement = new ProtectionElement();
			protectionElement.setObjectId("edu.wustl.catissuecore.domain.Site");
			ProtectionElementSearchCriteria protEleSearchCrit = new ProtectionElementSearchCriteria(
					protectionElement);
			List<ProtectionElement> list = privilegeUtility.getUserProvisioningManager()
					.getObjects(protEleSearchCrit);

			List<ObjectPrivilegeMap> map = impl.getPrivilegeMap(user.getLoginName(), list);
			assertNotNull(map);

		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
		catch (CSException e)
		{
			e.printStackTrace();
		}
	}
}
