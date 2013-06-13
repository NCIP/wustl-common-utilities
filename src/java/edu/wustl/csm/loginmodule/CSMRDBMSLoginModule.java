/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.csm.loginmodule;

import gov.nih.nci.security.authentication.loginmodules.RDBMSLoginModule;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;

import java.util.Hashtable;
import java.util.Map;

import javax.security.auth.Subject;

public class CSMRDBMSLoginModule extends RDBMSLoginModule
{

	/**
	 * This is an internal method which invokes the helper method from the
	 * RDBMSHelper class. It returns TRUE is the authentication is sucessful.
	 * @param user the user entered user name provided by the calling application
	 * @param password the user entered password provided by the calling application
	 * @return TRUE if the authentication was successful using the provided user
	 * credentials and FALSE if the authentication fails
	 * @throws CSInternalConfigurationException
	 * @throws CSInternalInsufficientAttributesException
	 */
	protected boolean validate(Map options, String user, char[] password, Subject subject)
			throws CSInternalConfigurationException, CSInternalInsufficientAttributesException
	{
		return CSMRDBMSHelper.authenticate(new Hashtable(options), user, password, subject);
	}
}
