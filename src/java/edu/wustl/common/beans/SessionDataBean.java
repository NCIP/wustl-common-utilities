/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.beans;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description:  </p>
 * <p>Copyright: (c) Washington University, School of Medicine 2005</p>
 * <p>Company: Washington University, School of Medicine, St. Louis.</p>
 *
 * @author Poornima Govindrao
 * @version 1.0
 */

public class SessionDataBean implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Specifies first Name. */
	private String firstName;

	/** Specifies last Name. */
	private String lastName;

	/** Specifies csm User Id. */
	private String csmUserId;

	/** Specifies user Name. */
	private String userName;

	/** Specifies ip Address. */
	private String ipAddress;

	/** Specifies user Id. */
	private Long userId = null;

	/** Specifies security Required. */
	private boolean securityRequired = Boolean.FALSE;

	/** Specifies is Admin. */
	private boolean isAdminUser = false;

	/**
	 * This method returns isAdminUser.
	 *
	 * @return isAdminUser
	 */
	public boolean isAdmin()
	{
		return isAdminUser;
	}

	/**
	 * This method sets Admin.
	 * @param isAdmin is Admin.
	 */
	public void setAdmin(boolean isAdmin)
	{
		this.isAdminUser = isAdmin;
	}

	/**
	 * Gets the user name.
	 * @return Returns the userName.
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Sets the user name.
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * Gets the csm user id.
	 * @return Returns the csmUserId.
	 */
	public String getCsmUserId()
	{
		return csmUserId;
	}

	/**
	 * Sets the csm user id.
	 * @param csmUserId The csmUserId to set.
	 */
	public void setCsmUserId(String csmUserId)
	{
		this.csmUserId = csmUserId;
	}

	/**
	 * Gets the ip address.
	 * @return Returns the ipAddress.
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 * @param ipAddress The ipAddress to set.
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the user id.
	 * @return Returns the userId.
	 */
	public Long getUserId()
	{
		return userId;
	}

	/**
	 * Sets the user id.
	 * @param userId The userId to set.
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * This method gets First Name.
	 * @return the First Name.
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * This method sets the First Name.
	 * @param firstName the First Name.
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * This method gets Last Name.
	 * @return the last name.
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * This method sets the Last Name.
	 * @param lastName the Last Name.
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * This method returns boolean value for securityRequired.
	 * @return security Required.
	 */
	public boolean isSecurityRequired()
	{
		return securityRequired;
	}

	/**
	 * This method sets the boolen value of securityRequired.
	 * @param securityRequired security Required.
	 */
	public void setSecurityRequired(boolean securityRequired)
	{
		this.securityRequired = securityRequired;
	}
}
