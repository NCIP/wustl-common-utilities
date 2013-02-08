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
