
package edu.wustl.common.actionForm;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This provides the interface / API to set values of domain object to form object.
 * @author sachin_lale
 *
 */
public interface IValueObject
{

	/**
	 * This method sets all Values.
	 * @param abstractDomain AbstractDomainObject object.
	 */
	void setAllValues(AbstractDomainObject abstractDomain);

}
