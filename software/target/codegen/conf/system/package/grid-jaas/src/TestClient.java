/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

import gov.nih.nci.system.security.authentication.cagrid.GridAuthenticationHelper;
import org.globus.gsi.GlobusCredential;

public class TestClient
{
	
	public static void main(String args[]) throws Exception
	{
		String username = "SDKTestUser";
		String password = "Psat123!@#";
		GridAuthenticationHelper loginHelper = new GridAuthenticationHelper("@CAGRID_LOGIN_MODULE_NAME@");
		GlobusCredential proxy = loginHelper.login(username,password);
		System.out.println(proxy);
		System.out.println("Identity:"+proxy.getIdentity());
	}
	
}