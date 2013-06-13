/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.security.impl;

import org.hibernate.SessionFactory;

import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.system.ApplicationSessionFactory;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserProvisioningManagerImpl
		extends
			gov.nih.nci.security.provisioning.UserProvisioningManagerImpl
{
	/**
	 * @throws CSConfigurationException exc
	 */
	public UserProvisioningManagerImpl() throws CSConfigurationException
	{
		/** Modified by amit_doshi
		 *  code reviewer abhijit_naik
		 */
		super(SecurityManagerPropertiesLocator.getInstance().getApplicationCtxName());
		String ctxName = SecurityManagerPropertiesLocator.getInstance().getApplicationCtxName();
		SessionFactory sFactory = ApplicationSessionFactory
				.getSessionFactory(ctxName);
		super.setAuthorizationDAO(new AuthorizationDAOImpl(sFactory,ctxName));
	}
}
