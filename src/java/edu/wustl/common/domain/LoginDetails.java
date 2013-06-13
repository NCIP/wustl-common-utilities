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

import java.util.Date;

/**
 * This class acts as a POJO for all the Login Attempt related information.
 * An instance of this class is passed to the LoginAuditManager's audit() method,
 * to audit the login attempt
 * @author niharika_sharma
 */
public class LoginDetails
{
	/**
	 * User's login id.
	 */
	private Long userLoginId;
	/**
	 * User's source or domain he belongs to.
	 */
	private Long sourceId;
	/**
	 * IP address of the machine.
	 */
	private String ipAddress;
	/**
	 * Status of the login attempt, success or failure.
	 */
	private boolean isLoginSuccessful;
	/**
	 * Login name of the user.
	 */
	protected String loginName;
	/**
	 * Returns the userLoginId.
	 * @return user Login Name.
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * Sets the loginName.
	 * @param loginName to set value of loginName.
	 */
	public void setLoginName(final String loginName) {
		this.loginName = loginName;
	}
	/**
	 * Constructor accepting the details required to create the LoginDetails object.
	 * @param userLoginId User Login Id.
	 * @param sourceId Source Id.
	 * @param ipAddress IP Address.
	 */
	public LoginDetails(Long userLoginId, Long sourceId, String ipAddress)
	{
		this.ipAddress = ipAddress;
		this.userLoginId = userLoginId;
		this.sourceId = sourceId;
	}
	/**
	 * Constructor accepting the details required to create the LoginDetails object.
	 * along with login status.
	 * @param userLoginId User Login Id.
	 * @param sourceId Source Id.
	 * @param ipAddress IP Address.
	 * @param isLoginSuccessful boolean value.
	 */
	public LoginDetails(Long userLoginId, Long sourceId, String ipAddress,
			boolean isLoginSuccessful)
	{
		this(userLoginId, sourceId, ipAddress);
		this.isLoginSuccessful = isLoginSuccessful;
	}
	/**
	 * Constructor accepting the details required to create the LoginDetails object.
	 * along with login status.
	 * @param userLoginId User Login Id.
	 * @param sourceId Source Id.
	 * @param ipAddress IP Address.
	 * @param isLoginSuccessful boolean value.
	 */
	public LoginDetails(Long userLoginId, Long sourceId, String ipAddress,
			String loginName )
	{
		this(userLoginId, sourceId, ipAddress);
		this.loginName = loginName;
	}
	/**
	 * Returns the userLoginId.
	 * @return user Login Id.
	 */
	public Long getUserLoginId()
	{
		return userLoginId;
	}
	/**
	 * Sets the userLoginId.
	 * @param userLoginId userLoginId to set.
	 */
	public void setUserLoginId(Long userLoginId)
	{
		this.userLoginId = userLoginId;
	}
	/**
	 * Returns the sourceId.
	 * @return sourceId.
	 */
	public Long getSourceId()
	{
		return sourceId;
	}
	/**
	 * Sets the sourceId.
	 * @param sourceId sourceId to set.
	 */
	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}
	/**
	 * Returns the ipAddress.
	 * @return ipAddress.
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}
	/**
	 * Sets the ipAddress.
	 * @param ipAddress ipAddress to set.
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	/**
	 * Returns the isLoginSuccessful.
	 * @return boolean result.
	 */
	public boolean isLoginSuccessful()
	{
		return isLoginSuccessful;
	}
	/**
	 * Sets the isLoginSuccessful.
	 * @param isLoginSuccessful Sets the boolean value.
	 */
	public void setLoginSuccessful(boolean isLoginSuccessful)
	{
		this.isLoginSuccessful = isLoginSuccessful;
	}

	/**
     * Date and time of the event.
     */
	protected Date timestamp;

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

		/**
	 * Constructor accepting the details required to create the LoginDetails object.
	 * along with login status.
	 * @param userLoginId User Login Id.
	 * @param sourceId Source Id.
	 * @param ipAddress IP Address.
	 * @param isLoginSuccessful boolean value.
	 */
	public LoginDetails(Long userLoginId, Long sourceId, String ipAddress,
			boolean isLoginSuccessful, String loginName, Date timeStamp)
	{
		this(userLoginId, sourceId, ipAddress, isLoginSuccessful);
		this.loginName = loginName;
		this.timestamp = timeStamp;
	}
}
