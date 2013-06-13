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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.security.global.Constants;
import edu.wustl.security.global.Utility;
import edu.wustl.security.privilege.PrivilegeType;
/**
 * Test case for Utility class.
 * @author deepti_shelar
 *
 */
public class TestUtility extends SecurityManagerBaseTestCase
{
	/**
	 * getPrivilegeType for obj.
	 */
	public void testGetPrivilegeTypeObj()
	{
		Map<String, String> tagKeyValueMap = new HashMap<String, String>();
		tagKeyValueMap.put(Constants.PRIVILEGE_TAG_NAME, "2");
		PrivilegeType pType = Utility.getInstance().getPrivilegeType(tagKeyValueMap);
		assertEquals(PrivilegeType.ObjectLevel, pType);
	}
	/**
	 * getPrivilegeType for class.
	 */
	public void testGetPrivilegeTypeClass()
	{
		Map<String, String> tagKeyValueMap = new HashMap<String, String>();
		tagKeyValueMap.put(Constants.PRIVILEGE_TAG_NAME, "1");
		PrivilegeType pType = Utility.getInstance().getPrivilegeType(tagKeyValueMap);
		assertEquals(PrivilegeType.ClassLevel, pType);
	}
	/**
	 * getPrivilegeType for insecure.
	 */
	public void testGetPrivilegeTypeInsecure()
	{
		Map<String, String> tagKeyValueMap = new HashMap<String, String>();
		tagKeyValueMap.put(Constants.PRIVILEGE_TAG_NAME, "0");
		PrivilegeType pType = Utility.getInstance().getPrivilegeType(tagKeyValueMap);
		assertEquals(PrivilegeType.InsecureLevel, pType);
	}
	/**
	 * getIsBirthDate for true.
	 */
	public void testIsBirthDateTrue()
	{
		Map<String, String> tagKeyValueMap = new HashMap<String, String>();
		tagKeyValueMap.put(edu.wustl.security.global.Constants.BDATE_TAG_NAME, Boolean.TRUE.toString());
		boolean isBirthDate = Utility.getInstance().getIsBirthDate(tagKeyValueMap);
		assertTrue(isBirthDate);
	}
	/**
	 * getIsBirthDate for false.
	 */
	public void testIsBirthDateFalse()
	{
		Map<String, String> tagKeyValueMap = new HashMap<String, String>();
		tagKeyValueMap.put(edu.wustl.security.global.Constants.BDATE_TAG_NAME, Boolean.FALSE.toString());
		boolean isBirthDate = Utility.getInstance().getIsBirthDate(tagKeyValueMap);
		assertFalse(isBirthDate);
	}
}
