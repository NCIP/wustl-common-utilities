/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.beans;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Poornima Govindrao
 *@version 1.0
 */

public class SessionDataBean
{

	/**
	 * Specifies first Name.
	 */
	private String firstName;

	/**
	 * Specifies last Name.
	 */
	private String lastName;

	/**
	 * Specifies csm User Id.
	 */
	private String csmUserId;

	/**
	 * Specifies user Name.
	 */
	private String userName;

	/**
	 * Specifies ip Address.
	 */
	private String ipAddress;

	/**
	 * Specifies user Id.
	 */
	private Long userId = null;

	/**
	 * Specifies security Required.
	 */
	private boolean securityRequired = Boolean.FALSE;

	/**
	 * Specifies is Admin.
	 */
	private boolean isAdminUser = false;

	/**
	 * This method returns isAdminUser.
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
	 * @return Returns the userName.
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return Returns the csmUserId.
	 */
	public String getCsmUserId()
	{
		return csmUserId;
	}

	/**
	 * @param csmUserId The csmUserId to set.
	 */
	public void setCsmUserId(String csmUserId)
	{
		this.csmUserId = csmUserId;
	}

	/**
	 * @return Returns the ipAddress.
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress The ipAddress to set.
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return Returns the userId.
	 */
	public Long getUserId()
	{
		return userId;
	}

	/**
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
