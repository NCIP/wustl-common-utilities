/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.query.hql;

import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.List;

public class SearchHQLQuery extends HQLCriteria implements HQLSearchQuery,SDKQuery
{
	private static final long serialVersionUID = 1L;

	public SearchHQLQuery(String hql, List<Object> parameters) {
		super(hql, parameters);
	}
}