package edu.wustl.common.domain;
/**
 * This class is the base class for all the Log objects that can be added to
 * an audit event.
 * @author niharika_sharma
 */

/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT_LOG"
 **/
public class AbstractAuditEventLog implements java.io.Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7101283835050458948L;
	/**
	 * System generated unique id.
	 */
	private Long id;
	/**
	 * object of audit event.
	 */
	private AuditEvent auditEvent;
	/**
	 * @see #setParticipant(Site)
	 * This method get the audit event object.
	 * @return the audit event object.
	 * @hibernate.many-to-one column="AUDIT_EVENT_ID"
	 * class="edu.wustl.common.domain.AuditEvent" constrained="true"
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
}
