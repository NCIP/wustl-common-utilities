/**
 * PrivilegeCacheManager will manage all the instances of PrivilegeCache.
 * This will be a singleton.
 * Instances of PrivilegeCache can be accessed from the instance of PrivilegeCacheManager
 */

package edu.wustl.security.privilege;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Utility;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * @author ravindra_jain
 * creation date : 14th April, 2008
 * @version 1.0
 */
public final class PrivilegeManager
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(PrivilegeManager.class);

	/**
	 * Singleton instance of PrivilegeCacheManager.
	 */
	private static PrivilegeManager instance = new PrivilegeManager();

	/**
	 * the map of login name and corresponding PrivilegeCache.
	 */
	private Map<String, PrivilegeCache> privilegeCaches;
	/**
	 * privilegeUtility.
	 */
	private PrivilegeUtility privilegeUtility;
	/**
	 * lazyobjs.
	 */
	private List<String> lazyObjects;
	/**
	 * classes.
	 */
	private List<String> classes;
	/**
	 * eager objects.
	 */
	private List<String> eagerObjects;
	/**
	 * isSuccessful false if ant exc occurs at instantiation.
	 */
	private static boolean isSuccessful = true;

	/**
	 * private constructor to make the class singleton.
	 */
	private PrivilegeManager()
	{
		lazyObjects = new ArrayList<String>();
		classes = new ArrayList<String>();
		eagerObjects = new ArrayList<String>();
		privilegeUtility = new PrivilegeUtility();
		privilegeCaches = new HashMap<String, PrivilegeCache>();
		try
		{
			readXmlFile("CacheableObjects.xml");
		}
		catch (SMException e)
		{
			isSuccessful = false;
			logger.error(e.getStackTrace());
		}
	}

	/**
	 * return the Singleton PrivilegeCacheManager instance.
	 * @throws SMException ex
	 * @return PrivilegeManager pManager
	 */
	public static PrivilegeManager getInstance() throws SMException
	{
		if(!isSuccessful)
		{
			String mess = "error occured in instantiation of PrivilegeManager";
			Utility.getInstance().throwSMException(null,mess, "sm.operation.error");
		}
		return instance;
	}

	/**
	 * to return the PrivilegeCache object from the Map of PrivilegeCaches.
	 * @param loginName login name
	 * @return PrivilegeCache cache
	 */
	public PrivilegeCache getPrivilegeCache(String loginName)
	{
		synchronized (privilegeCaches)
		{
			PrivilegeCache privilegeCache = privilegeCaches.get(loginName);
			if (privilegeCache == null)
			{
				privilegeCache = new PrivilegeCache(loginName);
				privilegeCaches.put(loginName, privilegeCache);
			}
			return privilegeCache;
		}
	}

	/**
	 * To get all PrivilegeCache objects.
	 * @return Collection of cache
	 */
	public Collection<PrivilegeCache> getPrivilegeCaches()
	{
		synchronized (privilegeCaches)
		{
			return privilegeCaches.values();
		}		
	}

	/**
	 * This method will generally be called from CatissueCoreSesssionListener.sessionDestroyed.
	 * in order to remove the corresponding PrivilegeCache from the Session.
	 * @param userId user id
	 */
	public void removePrivilegeCache(String userId)
	{
		synchronized (privilegeCaches)
		{
			privilegeCaches.remove(userId);
		}
	}

	/**
	 * This Utility method is called dynamically as soon as a
	 * Site or CollectionProtocol object gets created through the UI
	 * & adds detials regarding that object to the PrivilegeCaches of
	 * appropriate users in Session.
	 *
	 * @param objectId id
	 * @throws SMException e
	 */
	private void addObjectToPrivilegeCaches(String objectId) throws SMException
	{
		try
		{
			ProtectionElement protectionElement = privilegeUtility.getUserProvisioningManager()
					.getProtectionElement(objectId);
			Collection<ProtectionElement> protElements = new ArrayList<ProtectionElement>();
			protElements.add(protectionElement);
			synchronized (privilegeCaches)
			{
				for (PrivilegeCache privilegeCache : privilegeCaches.values())
				{
					Collection<ObjectPrivilegeMap> objPrivMapCol = privilegeUtility
							.getUserProvisioningManager().getPrivilegeMap(
									privilegeCache.getLoginName(), protElements);
					if (!objPrivMapCol.isEmpty())
					{
						privilegeCache.addObject(objectId, objPrivMapCol.iterator().next()
								.getPrivileges());
					}
				}
			}
		}
		catch (CSObjectNotFoundException e)
		{
			Utility.getInstance().throwSMException(e, e.getMessage(), "sm.operation.error");
		}
		catch (CSException e)
		{
			Utility.getInstance().throwSMException(e, e.getMessage(), "sm.operation.error");
		}
	}

	/**
	 * @param authorizationData data
	 * @param protectionObjects protObjs
	 * @param dynamicGroups set
	 * @param objectId id
	 * @throws SMException e
	 */
	public void insertAuthorizationData(List authorizationData, Set protectionObjects,
			String[] dynamicGroups, String objectId) throws SMException
	{
		PrivilegeUtility utility = new PrivilegeUtility();
		try
		{
			utility.insertAuthorizationData(authorizationData, protectionObjects, dynamicGroups);
		}
		catch (SMException exception)
		{
			String mess = "Exception in insertAuthorizationData:" + exception;
			Utility.getInstance().throwSMException(exception, mess, "sm.operation.error");
		}
		addObjectToPrivilegeCaches(objectId);
	}

	/**
	 * @param fileName name of the file
	 * @throws SMException e
	 */
	private void readXmlFile(String fileName) throws SMException
	{
		Document doc = createDoc(fileName);
		if (doc != null)
		{
			Element root = doc.getDocumentElement();
			getClasses(root);
			getObjects(root);
		}
	}

	/**
	 * @param root root
	 */
	private void getObjects(Element root)
	{
		NodeList nodeList1 = root.getElementsByTagName("ObjectType");

		int length1 = nodeList1.getLength();

		for (int counter = 0; counter < length1; counter++)
		{
			Element element = (Element) (nodeList1.item(counter));
			String temp = element.getAttribute("pattern");
			String lazily = element.getAttribute("cacheLazily");

			if (lazily.equalsIgnoreCase("false") || lazily.equalsIgnoreCase(""))
			{
				eagerObjects.add(temp);
			}
			else
			{
				lazyObjects.add(temp.replace('*', '_'));
			}
		}
	}

	/**
	 * @param root root
	 */
	private void getClasses(Element root)
	{
		NodeList nodeList = root.getElementsByTagName("Class");

		int length = nodeList.getLength();

		for (int counter = 0; counter < length; counter++)
		{
			Element element = (Element) (nodeList.item(counter));
			String temp = element.getAttribute("name");
			classes.add(temp);
		}
	}

	/**
	 * @param fileName name of the file
	 * @throws SMException e
	 * @return Doc
	 */
	private Document createDoc(String fileName) throws SMException
	{
		String xmlFileName = fileName;
		Document doc = null;
		InputStream inputXmlFile = this.getClass().getClassLoader()
				.getResourceAsStream(xmlFileName);

		if (inputXmlFile == null)
		{
			logger.debug("FileNotFound with name : " + fileName);
		}
		else
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try
			{
				builder = factory.newDocumentBuilder();
				doc = builder.parse(inputXmlFile);
			}
			catch (ParserConfigurationException excp)
			{
				String mess = "DocumentBuilder cannot be created:";
				Utility.getInstance().throwSMException(excp, mess, "sm.operation.error");
			}
			catch (SAXException excp)
			{
				String mess = "Not able to parse xml file:" + fileName;
				Utility.getInstance().throwSMException(excp, mess, "sm.operation.error");
			}
			catch (IOException excp)
			{
				String mess = "Not able to parse xml file: IOException" + fileName;
				Utility.getInstance().throwSMException(excp, mess, "sm.operation.error");
			}
		}
		return doc;
	}
	/**
	 * @return List of classes
	 */
	public List<String> getClasses()
	{
		return Collections.unmodifiableList(classes);
	}
	/**
	 * @return List of lazy objs
	 */
	public List<String> getLazyObjects()
	{
		return Collections.unmodifiableList(lazyObjects);
	}
	/**
	 * @return List of eager objs
	 */
	public List<String> getEagerObjects()
	{
		return Collections.unmodifiableList(eagerObjects);
	}

	/**
	 * get a set of login names of users having given privilege on given object.
	 *
	 * @param objectId id
	 * @param privilege priv
	 * @return Set of users
	 * @throws SMException e
	 */
	public Set<String> getAccesibleUsers(String objectId, String privilege) throws SMException
	{
		Set<String> result = new HashSet<String>();
		try
		{
			UserProvisioningManager userProvManager = privilegeUtility.getUserProvisioningManager();

			List<Group> list = userProvManager.getAccessibleGroups(objectId, privilege);
			if(list != null)
			{
				for (Group group : list)
				{
					Set<User> users = group.getUsers();
					for (User user : users)
					{
						result.add(user.getLoginName());
					}
				}
			}
		}
		catch (CSException excp)
		{
			String mess = "Not able to get instance of UserProvisioningManager:";
			Utility.getInstance().throwSMException(excp, mess, "sm.operation.error");
		}
		return result;
	}
	/**
	 * Creates a ROle object.
	 * @param roleName rolename
	 * @param privileges set of privileges
	 * @throws SMException 
	 */
	public void createRole(String roleName,Set<String>privileges) throws SMException
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		Role role=null;
		try
		{
		    role = privilegeUtility.getRole(roleName);
		}
		catch(Exception e)
		{
			role = new Role();
			role.setName(roleName);
			role.setDesc("Dynamically created role");
			role.setApplication(privilegeUtility.getApplication(SecurityManagerPropertiesLocator.
					getInstance().getApplicationCtxName()));
			Set<Privilege> privilegeList = new HashSet<Privilege>();
			try
			{
			for (String privilegeId : privileges)
			{
				Privilege privilege = privilegeUtility.getUserProvisioningManager().
				getPrivilegeById(privilegeId);
				privilegeList.add(privilege);
			}
			role.setPrivileges(privilegeList);
			UserProvisioningManager userProvisioningManager = privilegeUtility
			.getUserProvisioningManager();
			
				userProvisioningManager.createRole(role);
			}
			catch (CSObjectNotFoundException e1)
			{
				Utility.getInstance().throwSMException(e1, e1.getMessage(), "sm.operation.error");
			}
			catch (CSTransactionException e2)
			{
				Utility.getInstance().throwSMException(e2, e2.getMessage(), "sm.operation.error");
			}
		}
	}
	/**
	 * This is a temporary method written for StorageContainer - special case
	 * Used for StorageContainerBizLogic.isDeAssignable() method.
	 * @param roleId roleid
	 * @param objectId obj id
	 * @param privilegeName name of the priv
	 * @return boolean whether has privilege
	 * @throws SMException 
	 */
	public boolean hasGroupPrivilege(String roleId, String objectId, String privilegeName) throws SMException
	{
		boolean hasPriv = true;
		PrivilegeUtility utility = new PrivilegeUtility();
		String groupId = utility.getGroupIdForRole(roleId);
		Set<User> users;
		try
		{
			users = utility.getUserProvisioningManager().getUsers(groupId);

			for (User user : users)
			{
				if (!getPrivilegeCache(user.getLoginName()).hasPrivilege(objectId, privilegeName))
				{
					hasPriv = false;
				}
			}
		}
		catch (CSObjectNotFoundException e)
		{
			Utility.getInstance().throwSMException(e, e.getMessage(), "sm.operation.error");
		}
		return hasPriv;
	}
}
