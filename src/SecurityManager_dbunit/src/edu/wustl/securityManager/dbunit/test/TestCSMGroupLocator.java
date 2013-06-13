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

import junit.framework.TestCase;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.locator.CSMGroupLocator;


public class TestCSMGroupLocator extends SecurityManagerBaseTestCase
{
	/**
	 * Generic Logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(TestCSMGroupLocator.class);

	public void testGetPGName()
	{
		try
		{
			String className="edu.wustl.common.domain.CollectionProtocol";
			Class clazz= Class.forName(className);
			String pgName=CSMGroupLocator.getInstance().getPGName(Long.valueOf(1),clazz);
			assertEquals("COLLECTION_PROTOCOL_1",pgName);
		}
		catch (Exception exception)
		{
			fail("Not able to get correct pg name.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	public void testGetPIGroupName()
	{
		try
		{
			String className="edu.wustl.common.domain.CollectionProtocol";
			Class clazz= Class.forName(className);
			String pgName=CSMGroupLocator.getInstance().getPIGroupName(Long.valueOf(1),clazz);
			assertEquals("PI_COLLECTION_PROTOCOL_1",pgName);
		}
		catch (Exception exception)
		{
			fail("Not able to get correct PIGroup name.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	public void testGetCoordinatorGroupName()
	{
		try
		{
			String className="edu.wustl.common.domain.CollectionProtocol";
			Class clazz= Class.forName(className);
			String pgName=CSMGroupLocator.getInstance().getCoordinatorGroupName(Long.valueOf(1),clazz);
			assertEquals("COORDINATORS_COLLECTION_PROTOCOL_1",pgName);
		}
		catch (Exception exception)
		{
			fail("Not able to get correct coordinator name.");
			logger.fatal(exception.getMessage(),exception);
		}
	}
}
