
package edu.wustl.securityManager.dbunit.test;

import junit.framework.TestCase;
import edu.wustl.security.global.Constants;
import edu.wustl.security.privilege.Privilege;
/**
 * test case for Privilege.
 * @author deepti_shelar
 *
 */
public class TestPrivilege extends SecurityManagerBaseTestCase
{
	/**
	 * testGetPrivDetails.
	 */
	public void testGetPrivDetails()
	{
		Privilege priv = new Privilege("READ", 0);
		assertEquals(0, priv.getBitNumber());
		assertEquals("READ", priv.getPrivilegeName());
	}

	/**
	 * testSetPrivDetails2.
	 */
	public void testSetPrivDetails2()
	{
		Privilege priv = new Privilege("READ", 0);
		priv.setPrivilegeName(Constants.READ_DENIED);
		assertEquals(Constants.READ_DENIED, priv.getPrivilegeName());
	}

	/**
	 * testSetPrivDetails.
	 */
	public void testSetPrivDetails()
	{
		Privilege priv = new Privilege("READ", 0);
		priv.setBitNumber(edu.wustl.security.global.Constants.INDEX_ONE);
		assertEquals(edu.wustl.security.global.Constants.INDEX_ONE, priv.getBitNumber());
	}
}
