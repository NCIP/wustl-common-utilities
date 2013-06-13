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

package edu.wustl.security.beans;

import java.util.Set;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class SecurityDataBean
{
	/**
	 * user.
	 */
	private String user;
	/**
	 * group.
	 */
	private Set group;
	/**
	 * roleName.
	 */
	private String roleName;
	/**
	 * groupName.
	 */
	private String groupName;
	/**
	 * protGrpName.
	 */
	private String protGrpName;

	/**
	 * @return Returns the protGrpName.
	 */
	public String getProtGrpName()
	{
		return protGrpName;
	}

	/**
	 * @param protGrpName The protGrpName to set.
	 */
	public void setProtGrpName(final String protGrpName)
	{
		this.protGrpName = protGrpName;
	}

	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName()
	{
		return roleName;
	}

	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(final String roleName)
	{
		this.roleName = roleName;
	}

	/**
	 * @return Returns the userGroup.
	 */
	public Set getGroup()
	{
		return group;
	}

	/**
	 * @param group The userGroup to set.
	 */
	public void setGroup(final Set group)
	{
		this.group = group;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(final String user)
	{
		this.user = user;
	}

	/**
	 * @return Returns the groupName.
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * @param groupName The groupName to set.
	 */
	public void setGroupName(final String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * overriding toString().
	 * @return String
	 */
	public String toString()
	{
		return new String(" user:" + user + " groupName:" + groupName + " group:" + group.size()
				+ " role:" + roleName + " protectionGroup:" + protGrpName);
	}

}
