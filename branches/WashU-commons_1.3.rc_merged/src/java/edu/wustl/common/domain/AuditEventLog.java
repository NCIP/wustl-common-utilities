
package edu.wustl.common.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT_LOG"
 **/
public class AuditEventLog implements java.io.Serializable
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
	 * Type of the event.
	 */
	private String eventType;
	/**
	 * object of audit event.
	 */
	private AuditEvent auditEvent;
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
	 * This method get the type of the event.
	 * @return the type of the event
	 * @hibernate.property name="eventType" type="string"
	 * column="EVENT_TYPE" length="50"
	 */
	public String getEventType()
	{
		return eventType;
	}
	/**
	 * set the type of the event.
	 * @param eventType the type of the event.
	 */
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * This method get the audit event object.
	 * @return the audit event object.
	 * @hibernate.many-to-one column="AUDIT_EVENT_ID"
	 * class="edu.wustl.common.domain.AuditEvent" constrained="true"
	 * @see #setParticipant(Site)
	 */
	public AuditEvent getAuditEvent()
	{
		return auditEvent;
	}
	/**
	 * set the audit event object.
	 * @param auditEvent the AuditEvent object.
	 */
	public void setAuditEvent(AuditEvent auditEvent)
	{
		this.auditEvent = auditEvent;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * This method convert to string.
	 * @return String.
	 */
	public String toString()
	{
		return id + " " + objectIdentifier + " " + objectName + " " + eventType + " \n "
				+ auditEventDetailsCollection;
	}
}