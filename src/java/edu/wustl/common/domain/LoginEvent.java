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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @hibernate.class table="CATISSUE_LOGIN_AUDIT_EVENT_LOG"
 **/
public class LoginEvent implements Serializable
{
	/**
	 * SerialVersionId.
	 */
	private static final long serialVersionUID = -7085963264585176596L;
	/**
     * System generated unique id.
     */
	protected Long id;
	/**
     * Date and time of the event.
     */
	protected Date timestamp = Calendar.getInstance().getTime();
	/**
     * User's login id.
     */
	protected Long userLoginId;
	/**
     * User's source or domain he belongs to.
     */
	protected Long sourceId;
	/**
	 * IP address of the machine.
	 */
	protected String ipAddress;
	/**
	 * boolean value.
	 */
	protected boolean isLoginSuccessful;
	/**
	 * Login name of the user.
	 */
	protected String loginName;
	/**
     * Returns user's login Name.
     * @return user's login Name
     * @hibernate.property name="loginName" type="String"
     * column="LOGIN_NAME"
     */
	public String getLoginName() {
		return loginName;
	}
	/**
	 *
	 * @param loginName sets the loginName.
	 */
	public void setLoginName(final String loginName) {
		this.loginName = loginName;
	}
	/**
     * Returns user's login Status.
     * @return user's login Status
     * @hibernate.property name="isLoginSuccessful" type="boolean"
     * column="IS_LOGIN_SUCCESSFUL"
     */
	public boolean getIsLoginSuccessful()
	{
		return isLoginSuccessful;
	}
	/**
	 *
	 * @param loginSuccessful sets the boolean value.
	 */
	public void setIsLoginSuccessful(boolean loginSuccessful)
	{
		this.isLoginSuccessful = loginSuccessful;
	}
	/**
     * Returns user's login id.
     * @return user's login id
     * @hibernate.property name="userLoginId" type="string"
     * column="USER_LOGIN_ID" length="50"
     */
	public Long getUserLoginId()
	{
		return userLoginId;
	}
	/**
	 *
	 * @param userLoginId sets the userLoginId.
	 */
	public void setUserLoginId(Long userLoginId)
	{
		this.userLoginId = userLoginId;
	}
	/**
     * Returns System generated unique id.
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_PARAM_SEQ"
     */
	public Long getId()
	{
		return id;
	}
	/**
     * Sets unique id.
     * @param id Identifier to be set.
     * @see #getId()
     */
	public void setId(Long id)
	{
		this.id = id;
	}
	/**
     * Returns date and time of the event.
     * @return Date and time of the event.
     * @see #setTimestamp(Date)
     * @hibernate.property name="timestamp" type="timestamp"
     * column="LOGIN_TIMESTAMP"
     */
	public Date getTimestamp()
	{
		return timestamp;
	}
	/**
     * Sets date and time of the event.
     * @param timestamp Date and time of the event.
     * @see #getTimestamp()
     */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	/**
     * @hibernate.property name="sourceId" type="long" column="LOGIN_SOURCE_ID"
     * @return Returns the institutionId.
     */
    public Long getSourceId()
    {
        return sourceId;
    }
    /**
     * @param sourceId The userId to set.
     */
    public void setSourceId(Long sourceId)
    {
    	this.sourceId=sourceId;
    }
    /**
     * @return IPAddress in String format.
	 * @hibernate.property name="ipAddress" type="string"
     * column="LOGIN_IP_ADDRESS" length="20"
	 **/
	public String getIpAddress()
	{
		return ipAddress;
	}
	/**
	 *
	 * @param ipAddress ipAddress to set.
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
}
