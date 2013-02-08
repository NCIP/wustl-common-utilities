
package edu.wustl.common.domain;

import java.sql.Clob;

/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT_DETAILS"
 **/
public class AuditEventDetails implements java.io.Serializable
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
	 * Specifies element Name.
	 */
	private String elementName;

	/**
	 * Specifies previous Value.
	 */
	private Clob previousValue;

	/**
	 * Specifies current Value.
	 */
	private Clob currentValue;

	/**
	 * Specifies AuditEventLog instance.
	 */
	private AuditDataEventLog auditEventLog;

	/**
	 * Returns System generated unique id.
	 * @return Long System generated unique identifier.
	 * @see #setId(Long)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_DET_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets unique identifier.
	 * @param identifier Identifier to be set.
	 * @see #getId()
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns element name.
	 * @return the element name.
	 * @hibernate.property name="elementName" type="string"
	 * column="ELEMENT_NAME" length="150"
	 **/
	public String getElementName()
	{
		return elementName;
	}

	/**
	 * set the element name.
	 * @param elementName the element Name.
	 * @hibernate.property name="elementName" type="string"
	 * column="ELEMENT_NAME" length="150"
	 **/
	public void setElementName(String elementName)
	{
		this.elementName = elementName;
	}

	/**
	 * return previous value.
	 * @return the previous value.
	 * @hibernate.property name="previousValue" type="string"
	 * column="PREVIOUS_VALUE" length="150"
	 **/
	public Clob getPreviousValue()
	{
		return previousValue;
	}

	/**
	 * set the previous value.
	 * @param previousValue the previous value to be set.
	 * @hibernate.property name="previousValue" type="string"
	 * column="PREVIOUS_VALUE" length="150"
	 **/

	public void setPreviousValue(Clob previousValue)
	{
		this.previousValue = previousValue;
	}

	/**
	 * This method return current value.
	 * @return the current value.
	 * @hibernate.property name="currentValue" type="string"
	 * column="CURRENT_VALUE" length="500"
	 **/
	public Clob getCurrentValue()
	{
		return currentValue;
	}

	/**
	 * set the current value.
	 * @param currentValue the current value.
	 * @hibernate.property name="currentValue" type="string"
	 * column="CURRENT_VALUE" length="500"
	 **/

	public void setCurrentValue(Clob currentValue)
	{
		this.currentValue = currentValue;
	}

	/**
	 * This method gets the audit event log object.
	 * @return the audit event log object.
	 * @hibernate.many-to-one column="AUDIT_EVENT_LOG_ID"
	 * class="edu.wustl.common.domain.AuditDataEventLog" constrained="true"
	 * @see #setParticipant(Site)
	 */
	public AuditDataEventLog getAuditEventLog()
	{
		return auditEventLog;
	}

	/**
	 * set the value of audit event in audit event log.
	 * @param auditEventLog The auditEventLog to set.
	 */
	public void setAuditEventLog(AuditDataEventLog auditEventLog)
	{
		this.auditEventLog = auditEventLog;
	}

	/**
	 * This method returns hash Code.
	 * overrides java.lang.Object.hashCode.
	 * @return int.
	 */
	public int hashCode()
	{
		int hashCode = 0;

		if (id != null)
		{
			hashCode += id.intValue();
		}
		if (elementName != null)
		{
			hashCode += elementName.hashCode();
		}
		if (previousValue != null)
		{
			hashCode += previousValue.hashCode();
		}
		if (currentValue != null)
		{
			hashCode += currentValue.hashCode();
		}
		return hashCode;
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
		return "Id " + id + " " + "ElementName " + elementName + " " + "PreviousValue "
				+ previousValue + " " + "CurrentValue " + currentValue + "\n";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals()
	 */
	/**
	 * This method compare Object.
	 * @param object object.
	 * @return true if object is equal.
	 */
	public boolean equals(Object object)
	{
		 return this == object;
	}
}