
package edu.wustl.securityManager.dbunit.test;

import edu.wustl.security.global.Constants;
import edu.wustl.security.privilege.PrivilegeType;
import junit.framework.TestCase;
/**
 * test case for PrivilegType.
 * @author deepti_shelar
 *
 */
public class TestPrivilegeType extends SecurityManagerBaseTestCase
{
	/**
	 * testGetPrivilegeTypeClass.
	 */
	public void testGetPrivilegeTypeClass()
	{
		int value = Constants.CLASS_LEVEL_SECURE_RETRIEVE;
		PrivilegeType privilegeType = PrivilegeType.getPrivilegeType(value);
		assertEquals(PrivilegeType.ClassLevel, privilegeType);
	}
	/**
	 * testGetPrivilegeTypeObject.
	 */
	public void testGetPrivilegeTypeObject()
	{
		int value = Constants.OBJECT_LEVEL_SECURE_RETRIEVE;
		PrivilegeType privilegeType = PrivilegeType.getPrivilegeType(value);
		assertEquals(PrivilegeType.ObjectLevel, privilegeType);
	}
	/**
	 * testGetPrivilegeTypeInsecure.
	 */
	public void testGetPrivilegeTypeInsecure()
	{
		int value = Constants.INSECURE_RETRIEVE;
		PrivilegeType privilegeType = PrivilegeType.getPrivilegeType(value);
		assertEquals(PrivilegeType.InsecureLevel, privilegeType);
	}
}
