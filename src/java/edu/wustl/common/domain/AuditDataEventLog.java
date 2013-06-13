/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * @hibernate.class table="CATISSUE_DATA_AUDIT_EVENT_LOG"
 **/
public class AuditDataEventLog extends AbstractAuditEventLog implements java.io.Serializable
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	private Long id;
	/**
	 * identifier that identify the event object.
	 */
	private Long objectIdentifier;
	/**
	 * Name of the event object.
	 */
	private String objectName;

	/**
	 * AuditDataEventLog.
	 */
	private Collection<AuditDataEventLog> auditDataEventLogs = new HashSet<AuditDataEventLog>();

	/**
	 * collection that contains details of event audit.
	 */
	private Collection<AuditEventDetails> auditEventDetailsCollection = new HashSet<AuditEventDetails>();
	/**
	 * This method gets the id.
	 * @return the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_LOG_SEQ"
	 */
	public Long getId()
	{
		return id;
	}
	/**
	 * set the id.
	 * @param identifier identifier to be set.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * This method gets the identifier of the object.
	 * @return the identifier of the object.
	 * @hibernate.property name="objectIdentifier" type="long"
	 * column="OBJECT_IDENTIFIER" length="50"
	 */
	public Long getObjectIdentifier()
	{
		return objectIdentifier;
	}
	/**
	 * set the object Identifier.
	 * @param objectIdentifier the identifier of the object.
	 */
	public void setObjectIdentifier(Long objectIdentifier)
	{
		this.objectIdentifier = objectIdentifier;
	}

	/**
	 * This method get the name of object.
	 * @return the name of object.
	 * @hibernate.property name="ObjectName" type="string"
	 * column="OBJECT_NAME" length="50"
	 */
	public String getObjectName()
	{
		return objectName;
	}
	/**
	 * set the name of the object.
	 * @param objectName the name of object.
	 */
	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	/**
	 * This method get the Audit Event Details Collcetion.
	 * @return Audit Event Details Collcetion.
	 * @hibernate.set name="auditEventDetailsCollection" table="CATISSUE_AUDIT_EVENT_DETAILS"
	 * @hibernate.collection-key column="AUDIT_EVENT_LOG_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.domain.AuditEventDetails"
	 */
	public Collection<AuditEventDetails> getAuditEventDetailsCollection()
	{
		return auditEventDetailsCollection;
	}
	/**
	 * set the collection with audit event details.
	 * @param auditEventDetailsCollection the audit Event Details Collcetion.
	 */
	public void setAuditEventDetailsCollection(
			Collection<AuditEventDetails> auditEventDetailsCollection)
	{
		this.auditEventDetailsCollection = auditEventDetailsCollection;
	}
	/**
	 * This method get the Audit Event Details Collcetion.
	 * @return AuditDataEventLog object Details.
	 * @hibernate.set name="auditDataEventLogs" table="CATISSUE_DATA_AUDIT_EVENT_LOG"
	 * @hibernate.collection-key column="PARENT_LOG_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.domain.AuditDataEventLog"
	 */
	public Collection<AuditDataEventLog> getAuditDataEventLogs()
	{
		return auditDataEventLogs;
	}
	/**
	 *
	 * @param auditDataEventLogs Set the Collection object.
	 */
	public void setAuditDataEventLogs(
			Collection<AuditDataEventLog> auditDataEventLogs)
	{
		this.auditDataEventLogs = auditDataEventLogs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * This method convert to string.
	 * @return String.
	 */
	public String toString()
	{
		return getId() + " " + objectIdentifier + " " + objectName + " \n "
				+ auditEventDetailsCollection;
	}
}