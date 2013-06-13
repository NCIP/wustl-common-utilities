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

/**
 * Main class to run junit test cases.
 */

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author deepti_shelar
 * Test Suite for testing all Security Manager classes
 */
public class TestAll extends TestCase
{

	/**
	 * Main method called by junit target in build.xml.
	 * @param args arguments to main method.
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(TestAll.class);
	}
	/**
	 *
	 * @return Test object.
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for Query Interface Classes");
		suite.addTestSuite(TestSecurityManager.class);
		suite.addTestSuite(TestPrivilegeCache.class);
		suite.addTestSuite(TestPrivilegeManager.class);
		suite.addTestSuite(TestPrivilegeUtility.class);
		suite.addTestSuite(TestAuthorizationDAOImpl.class);
		suite.addTestSuite(TestPrivilegeType.class);
		suite.addTestSuite(TestPrivilege.class);
		suite.addTestSuite(TestUtility.class);
		return suite;
	}
}
