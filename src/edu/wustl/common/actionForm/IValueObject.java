
package edu.wustl.common.actionForm;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.UIRepOfDomain;

/**
 * This provides the interface / API to set values of domain object to form object.
 * @author sachin_lale
 *
 */
public interface IValueObject extends UIRepOfDomain
{

	/**
	 * This method sets all Values.
	 * @param abstractDomain AbstractDomainObject object.
	 */
	void setAllValues(AbstractDomainObject abstractDomain);

}
