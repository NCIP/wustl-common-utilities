/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.security.acegi.authentication;

import gov.nih.nci.system.security.acegi.providers.GroupNameAuthenticationToken;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.AuthenticationProvider;

public class NullGroupNameAuthenticationProvider implements AuthenticationProvider
{

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		return authentication;
	}

	public boolean supports(Class authentication) {
		
		return GroupNameAuthenticationToken.class.isAssignableFrom(authentication);
	}

}