/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * PrivilegeCache will cache all privileges for a specific user
 * An instance of PrivilegeCache will be created for every user who logs in.
 */

package edu.wustl.security.privilege;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.locator.PrivilegeLocator;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

/**
 * @author ravindra_jain creation date : 14th April, 2008
 * @version 1.0
 */
public class PrivilegeCache
{

	/** The Constant STAR. */
	private static final String STAR = "*";

	/** The Constant PERCENT_DELIMITER. */
	private static final String PERCENT_DELIMITER = "%";

	/** The Constant COMMA_DELIMITER. */
	private static final String COMMA_DELIMITER = ",";

	/** The Constant SINGLE_QUOTE. */
	private static final String SINGLE_QUOTE = "'";

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(PrivilegeCache.class);
	/**
	 * login name of the user who has logged in.
	 */
	private String loginName;

	/**
	 * the map of object id and corresponding permissions / privileges.
	 */
	private Map<String, BitSet> privilegeMap;

	/**
	 * After initialization of the variables, a call to method 'initialize()'
	 * is made initialize() uses some ProtectionElementSearchCriterias & gets
	 * Protection Elements from the database.
	 * @param loginName login name of the user who has logged in.
	 */
	public PrivilegeCache(final String loginName)
	{
		privilegeMap = new HashMap<String, BitSet>();
		this.loginName = loginName;

		//Initializing privilege cache.
		initialize();
	}

	/**
	 * This method gets Protection Elements we are interested in, from the
	 * database We achieve this by using the ProtectionElementSearchCriteria
	 * provided by CSM Then, a call to getPrivilegeMap is made & we get a
	 * Collection of ObjectPrivilegeMap for each call Here, for every passed
	 * ProtectionElement,the method looks for the privileges that User has on
	 * the ProtectionElement such ObjectPrivilegeMaps are then passed to
	 * 'populatePrivileges' method.
	 *
	 */
	private void initialize()
	{
		try
		{
			/*
			 * Changes done for Bug #19862 -  Slow Login
			 *
			 * Previously privileges were populated for each cacheable and eager objects. The query to fetch privileges
			 * was run in for loop, so for each cacheable and eagar object privileges were fetched from database
			 * and privilege map was populated.
			 *
			 * To avoid this we have generated comma separated list of cacheable and eager objects. This comma separated
			 * list is used to fetch privileges form database. Here instead of fetching privileges for each object,
			 * we have generated single query to fetch all the privileges. This will avoid multiple data base trips.
			 * Now all the privileges are fetch in single query and privilege map is populated.
			 *
			 * Thus code changes made are related to fetching strategy of privileges from database.
			 *
			 */

			//Get comma separated cacheable object list by reading CacheableObjects.xml.
			String commaSeperatedClasses = getCommaSeparatedString(PrivilegeManager.getInstance()
					.getClasses());
			//Fetch all the privileges from the database for these comma separated cacheableObjects in a single query.
			Collection<ObjectPrivilegeMap> objectPrivileges = getObjectPrivilegeMap(commaSeperatedClasses);
			//Populate privilege Map for all the cacheable objects.
			for (ObjectPrivilegeMap objectPrivilegeMap : objectPrivileges)
			{
				populatePrivileges(objectPrivilegeMap);
			}

			//Get comma separated eager object list by reading CacheableObjects.xml.
			String commaSeperatedEagerObjects = getCommaSeparatedString(PrivilegeManager
					.getInstance().getEagerObjects());

			//Fetch all the privileges from the database for these comma separated eagar objects in a single query.
			Collection<ObjectPrivilegeMap> eagerObjectPrivileges = getObjectPrivilegeMap(commaSeperatedEagerObjects);

			//Populate privilege Map for all the eagar objects.
			for (ObjectPrivilegeMap objectPrivilegeMap : eagerObjectPrivileges)
			{
				populatePrivileges(objectPrivilegeMap);
			}

			//TODO Instead of passing comma separated cacheable and eagar objects, passed the protection elements list.
			// At the time of fetching privileges from database, generate query as per the protection element list.
			// So this code of generating comma separated list will be moved in query generation code. (AuthorizationDAOImpl class)
		}
		catch (SMException e)
		{
			String message = "error in initialising cache " + e.getMessage();
			logger.error(message);
		}
	}


	/**
	 * Gets the object privilege map.
	 *
	 * @param protEleObjIds the prot ele obj ids
	 *
	 * @return the object privilege map new
	 *
	 * @throws SMException the SM exception
	 */
	private Collection getObjectPrivilegeMap(final String protEleObjIds) throws SMException
	{
		Collection objPrivMap = new ArrayList();
		try
		{
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();

			//Generate list of Protection Elements
			List pEObjectList = new ArrayList();
			pEObjectList.add(protEleObjIds);

			// Fetch privileges from the database for respective user and PE object.
			objPrivMap = privilegeUtility.getUserProvisioningManager().getPrivilegeMap(loginName,
					pEObjectList);
		}
		catch (CSException excp)
		{
			logger.error(excp.getMessage(), excp);
		}
		return objPrivMap;
	}

	/**
	 * populatePrivileges does the Mapping (inserts into Map) of the Object Id's
	 * and corresponding BitSets.
	 *
	 * BitSet is used for the Mapping. There are total 10 possible Privileges /
	 * Permissions. So, we use 10 bits of bitset for storing these Permissions
	 * For every objectId in PrivilegeMap, a bit '1' in BitSet indicates user
	 * has permission on that Privilege & a bit '0' indicates otherwise So, in
	 * this method, we examine each Privilege returned by PrivilegeMap & set the
	 * BitSet corresponding to the objectId accordingly.
	 * @param objPrivMapCol objPrivMapCol.
	 */
	private void populatePrivileges(final Collection<ObjectPrivilegeMap> objPrivMapCol)
	{
		// To populate the permissionMap
		for (ObjectPrivilegeMap objectPrivilegeMap : objPrivMapCol)
		{
			//Set Bitset corresponding to the objectId and populate privilege Map.
			populatePrivileges(objectPrivilegeMap);
		}
	}

	/**
	 * Simply checks if the user has any privilage on given object id.
	 * @param objectId object Id
	 * @return return true if user has privilege, false otherwise.
	 */
	public boolean hasPrivilege(String objectId)
	{
		boolean hasPrivilege = false;
		BitSet bitSet = privilegeMap.get(objectId);
		if (bitSet != null && bitSet.cardinality() > 0)
		{
			hasPrivilege = true;
		}

		return hasPrivilege;
	}

	/**
	 * To check for 'Class' & 'Action' level Privilege Here, we take className
	 * as the objectId & retrieve its associated BitSet from the privilegeMap
	 * Then, we check whether User has Permission over the passed Privilege or
	 * no & return true if user has privilege, false otherwise.
	 * @param classObj classObj
	 * @param privilegeName privilege Name
	 * @return return true if user has privilege, false otherwise.
	 * @throws SMException e
	 */
	public boolean hasPrivilege(Class classObj, String privilegeName) throws SMException
	{
		return hasPrivilege(classObj.getName(), privilegeName);
	}

	/**
	 * To check for 'Object' level Privilege Here, we take objectId from Object &
	 * retrieve its associated BitSet from the privilegeMap Then, we check
	 * whether User has Permission over the passed Privilege or no & return true
	 * if user has privilege, false otherwise.
	 *
	 * @param aDObject aDObject is AbstractDomainObject.
	 * @param privilegeName privilege Name.
	 * @return return true if user has privilege, false otherwise.
	 * @throws SMException e
	 */
	public boolean hasPrivilege(AbstractDomainObject aDObject, String privilegeName)
			throws SMException
	{
		return hasPrivilege(aDObject.getObjectId(), privilegeName);
	}

	/**
	 * To check for Privilege given the ObjectId Here, we take objectId &
	 * retrieve its associated BitSet from the privilegeMap Then, we check
	 * whether User has Permission over the passed Privilege or no & return true
	 * if user has privilege, false otherwise.
	 * @param objectId object Id
	 * @param privilegeName privilege Name.
	 * @return return true
	 * if user has privilege, false otherwise.
	 * @throws SMException e
	 */
	public boolean hasPrivilege(String objectId, String privilegeName) throws SMException
	{
		boolean isAuthorized = false;
		BitSet bitSet = privilegeMap.get(objectId);
		if (bitSet == null)
		{
			for (String objectIdPart : PrivilegeManager.getInstance().getLazyObjects())
			{
				if (objectId.startsWith(objectIdPart))
				{
					bitSet = getPrivilegesFromDatabase(objectId);
					break;
				}
			}
		}

		if (bitSet != null)
		{
			isAuthorized = bitSet.get(getBitNumber(privilegeName));
		}
		return isAuthorized;
	}

	/**
	 * This method gets Privileges From Database.
	 * @param objectId object Id
	 * @return BitSet return bitSet of Privileges from Database.
	 * @throws SMException e
	 */
	private BitSet getPrivilegesFromDatabase(String objectId) throws SMException
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();

		BitSet bitSet = privilegeMap.get(objectId);

		try
		{
			ProtectionElement protectionElement = new ProtectionElement();
			protectionElement.setObjectId(objectId);
			ProtectionElementSearchCriteria protEleSearchCrit = new ProtectionElementSearchCriteria(
					protectionElement);
			List list = privilegeUtility.getUserProvisioningManager().getObjects(protEleSearchCrit);
			Collection objPrivMap = privilegeUtility.getUserProvisioningManager().getPrivilegeMap(
					loginName, list);
			populatePrivileges(objPrivMap);
			bitSet = privilegeMap.get(objectId);
		}
		catch (CSException excp)
		{
			logger.error(excp.getMessage(), excp);
		}

		return bitSet;
	}

	/**
	 * This method is used to refresh the Privilege Cache for the user A call to
	 * this method forces CSM to go to the database & get the ProtectionElements
	 * For more, please refer to the 'initialize' method above.
	 *
	 */
	public void refresh()
	{
		initialize();
	}

	/**
	 * This method gets Bit Number of privilege Name.
	 * @param privilegeName privilegeName privilege Name.
	 * @return bit number of given privilege.
	 */
	private int getBitNumber(String privilegeName)
	{
		PrivilegeLocator instance = PrivilegeLocator.getInstance();
		edu.wustl.security.privilege.Privilege privilege = instance
				.getPrivilegeByName(privilegeName);
		return privilege.getBitNumber();
	}

	/**
	 * This method gets Login name.
	 * @return login name.
	 */
	public String getLoginName()
	{
		return loginName;
	}

	/**
	 * This method set bitset and populate privilege map.
	 * BitSet is used for the Mapping. There are total 10 possible Privileges /
	 * Permissions. So, we use 10 bits of bitset for storing these Permissions
	 * For every objectId in PrivilegeMap, a bit '1' in BitSet indicates user
	 * has permission on that Privilege & a bit '0' indicates otherwise So, in
	 * this method, we examine each Privilege returned by PrivilegeMap & set the
	 * BitSet corresponding to the objectId accordingly.
	 * @param objectId object Id
	 * @param privileges collection of privileges.
	 */
	public void addObject(String objectId, Collection<Privilege> privileges)
	{
		BitSet bitSet = new BitSet();

		for (Privilege privilege : privileges)
		{
			bitSet.set(getBitNumber(privilege.getName()));
		}
		privilegeMap.put(objectId, bitSet);
	}

	/**
	 * get the ids and privileges where ids start with the given prefix.
	 *
	 * @param prefix prefix.
	 * @return map of Privileges for Prefix
	 */
	public Map<String, List<NameValueBean>> getPrivilegesforPrefix(String prefix)
	{
		Map<String, List<NameValueBean>> map = new HashMap<String, List<NameValueBean>>();

		for (Entry<String, BitSet> entry : privilegeMap.entrySet())
		{
			if (entry.getKey().startsWith(prefix))
			{
				List<NameValueBean> privileges = getPrivilegeNames(entry.getValue());
				if (!privileges.isEmpty())
				{
					map.put(entry.getKey(), privileges);
				}
			}
		}

		return map;
	}
	
	/**
	 * returns list of privileges list with privilege name and id
	 * @param objectId
	 * @return
	 */
	public List<NameValueBean> getPrivilegesforObjectId(String objectId)
	{
		List<NameValueBean> privList = new ArrayList<NameValueBean>();
		if(privilegeMap.containsKey(objectId))
		{
			privList = getPrivilegeNames(privilegeMap.get(objectId));
		}

		return privList;
	}


	/**
	 * convert the given bitset into a set of privilege names.
	 * @param value value
	 * @return return list of privilegeNames.
	 */
	private List<NameValueBean> getPrivilegeNames(BitSet value)
	{
		List<NameValueBean> privilegeNames = new ArrayList<NameValueBean>();
		for (int i = 0; i < value.size(); i++)
		{
			if (value.get(i))
			{
				NameValueBean nmv = new NameValueBean();
				nmv.setName(PrivilegeLocator.getInstance().getPrivilegeByBit(i).getPrivilegeName());
				for (Object o : CommonUtilities.getAllPrivileges())
				{
					NameValueBean privilege = (NameValueBean) o;
					if (privilege.getName().equals(nmv.getName()))
					{
						nmv.setValue(privilege.getValue());
						privilegeNames.add(nmv);
						break;
					}
				}
			}
		}
		return privilegeNames;
	}

	/**
	 * Gets the comma separated cacheable and eagar objects.
	 *
	 * @param objectList the object list
	 *
	 * @return the comma separated string
	 *
	 * @throws SMException the SM exception
	 */
	private String getCommaSeparatedString(List<String> objectList) throws SMException
	{
		StringBuffer stringBuffer = new StringBuffer();

		for (String objectName : objectList)
		{
			//Few eagar objects have pattern like 'edu.wustl.clinportal.domain.XYZ*'
			// So to query for that pattern replace star with percent delimiter.
			if (objectName.contains(STAR))
			{
				objectName = objectName.replace(STAR, PERCENT_DELIMITER);
				stringBuffer.append(objectName);
			}
			else
			{
				stringBuffer.append(SINGLE_QUOTE + objectName + SINGLE_QUOTE);
			}
			stringBuffer.append(COMMA_DELIMITER);
		}
		return stringBuffer.substring(0, stringBuffer.length() - 1);
	}

	/**
	 * Populate privileges.
	 *
	 * @param objectPrivilegeMap the object privilege map
	 */
	private void populatePrivileges(ObjectPrivilegeMap objectPrivilegeMap)
	{
		//get PE object Id
		String objectId = objectPrivilegeMap.getProtectionElement().getObjectId();

		//Generate Bitset with respect to PE objects
		BitSet bitSet = new BitSet();
		for (Object privilege : objectPrivilegeMap.getPrivileges())
		{
			bitSet.set(getBitNumber(((Privilege) privilege).getName()));
		}
		//Populate privilege Map
		privilegeMap.put(objectId, bitSet);
	}

}
