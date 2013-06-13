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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.SecurityManager;
import edu.wustl.security.privilege.PrivilegeCache;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
/**
 * Test case for PrivilegeCache.
 * @author deepti_shelar
 *
 */
public class TestPrivilegeCache extends SecurityManagerBaseTestCase
{

	/**
	 * logger Logger - Generic logger.
	 */
	protected static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);
	static String configFile = "";
	static PrivilegeCache cache;

	public void setUp()
	{

	}
	static
	{
			cache = new PrivilegeCache("test");
	}

	/**
	 * 
	 */
	public void testInitialise()
	{
		PrivilegeCache cache = new PrivilegeCache("test");
		System.out.println("cache " + cache.getLoginName());
		assertNotNull(cache);
		assertEquals("test", cache.getLoginName());
	}

	/**
	 * 
	 */
	public void testRefresh()
	{
		PrivilegeCache cache = new PrivilegeCache("test");
		cache.refresh();
	}

	
	/**
	 * 
	 */
	public void testHasPrivilege()
	{
		boolean hasPrivilege;
		try
		{
			hasPrivilege = cache.hasPrivilege("edu.wustl.catissuecore.domain.Participant",
					"READ");
			assertFalse(hasPrivilege);
		}
		catch (SMException e)
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 */
	public void testHasPrivilegeObjectId()
	{
		boolean hasPrivilege = cache.hasPrivilege("edu.wustl.catissuecore.domain.Participant");
		System.out.println("hasPrivilege" + hasPrivilege);
		assertFalse(hasPrivilege);
	}

	/**
	 * 
	 */
	public void testGetPrivilegesforPrefix()
	{
		Collection<Privilege> privs = new ArrayList<Privilege>();
		Privilege pr = new Privilege();
		pr.setName("READ");
		privs.add(pr);
		cache.addObject("edu.wustl.catissuecore.domain.User_1", privs);

		Map<String, List<NameValueBean>> privilegeMap = cache
				.getPrivilegesforPrefix("edu.wustl.catissuecore.domain.Address");
		Set<String> keySet = privilegeMap.keySet();
		Iterator<String> iterator = keySet.iterator();

		while (iterator.hasNext())
		{
			String next = iterator.next();
			System.out.println("next " + next);
			List<NameValueBean> list = privilegeMap.get(next);
			System.out.println("---" + list.size());
		}
		assertNotNull(privilegeMap);
	}
	/**
	 * 
	 */
	public void testAddObject()
	{
		Collection<Privilege> privs = new ArrayList<Privilege>();
		Privilege pr = new Privilege();
		pr.setName("READ");
		privs.add(pr);
		cache.addObject("edu.wustl.catissuecore.domain.Participant", privs);
	}

}
