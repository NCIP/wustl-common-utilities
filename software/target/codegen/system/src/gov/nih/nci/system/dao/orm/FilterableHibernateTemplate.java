/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.dao.orm;


import gov.nih.nci.system.security.helper.SecurityInitializationHelper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class FilterableHibernateTemplate extends HibernateTemplate {

	private SecurityInitializationHelper securityHelper;

	public FilterableHibernateTemplate(SessionFactory sessionFactory, SecurityInitializationHelper securityHelper) {
		super(sessionFactory);
		this.securityHelper = securityHelper;
	}

	protected void enableFilters(Session session) {
		if (securityHelper == null) {
			super.enableFilters(session);
			return;
		}
		
		securityHelper.enableAttributeLevelSecurity(getSessionFactory());
		securityHelper.initializeFilters(session);
	}
	
}
