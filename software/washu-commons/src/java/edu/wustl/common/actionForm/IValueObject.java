/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.actionForm;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.UIRepOfDomain;

/**
 * This provides the interface / API to set values of domain object to form object.
 * @author sachin_lale
 *
 *
 * Modified on 07 Jan,2011
 * Extended IValueObject interface from UIRepOfDomain marker interface.
 * @author atul_kaushal
 *
 */
//#1
public interface IValueObject extends UIRepOfDomain
{

	/**
	 * This method sets all Values.
	 * @param abstractDomain AbstractDomainObject object.
	 */
	void setAllValues(AbstractDomainObject abstractDomain);

}
