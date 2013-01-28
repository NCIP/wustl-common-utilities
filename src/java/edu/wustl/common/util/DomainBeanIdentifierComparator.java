/**
 *
 */

package edu.wustl.common.util;

import java.io.Serializable;
import java.util.Comparator;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This class compares the two domain objects  by their identifiers.
 * @author chetan_patil
 *
 */
public class DomainBeanIdentifierComparator implements Comparator<Object>,Serializable
{

	/**
	 * serial Version UID.
	 */
	private static final long serialVersionUID = -3912067793850542651L;

	/**
	 * compare two objects.
	 * @param object1 Object first object.
	 * @param object2 Object second object.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @return int numeric value for comparison.
	 */
	public int compare(Object object1, Object object2)
	{
		AbstractDomainObject domainObject1 = (AbstractDomainObject) object1;
		AbstractDomainObject domainObject2 = (AbstractDomainObject) object2;

		Long value = domainObject1.getId() - domainObject2.getId();
		return value.intValue();
	}
}
