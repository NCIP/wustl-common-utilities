/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.audit;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class contains collection of all the domain object classes to be audited.
 * @author niharika_sharma
 *
 */
public class AuditableMetaData
{

	/**
	 * Collection of all the auditable classes.
	 */
	private Collection<AuditableClass> auditableMetadataClass  =
		new ArrayList<AuditableClass>();

	/**
	 * Default constructor.
	 */
	public AuditableMetaData()
	{
	}

	/**
	 * Returns the collection of auditable classes.
	 * @return AuditableClass collection.
	 */
	public Collection<AuditableClass> getAuditableClass()
	{
		return auditableMetadataClass;
	}
}
