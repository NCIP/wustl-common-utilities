/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.privilege;

/**
 * This class is for Privilege.
 * @author ravi_kumar
 *
 */
public class Privilege
{
	/**
	 * privilege Name.
	 */
	private String privilegeName;

	/**
	 * bit Number.
	 */
	private int bitNumber;

	/**
	 * role Name.
	 *//*
	private String roleName;*/
	/**
	 * Constructor.
	 * @param privilegeName Privilege Name
	 * @param bitNumber bit number
	 */
	public Privilege(String privilegeName,int bitNumber)
	{
		this.privilegeName=privilegeName;
		this.bitNumber=bitNumber;
		//this.roleName=roleName;
	}

	/**
	 * @return the privilegeName
	 */
	public String getPrivilegeName()
	{
		return privilegeName;
	}

	/**
	 * @param privilegeName the privilegeName to set
	 */
	public void setPrivilegeName(String privilegeName)
	{
		this.privilegeName = privilegeName;
	}

	/**
	 * @return the bitNumber
	 */
	public int getBitNumber()
	{
		return bitNumber;
	}

	/**
	 * @param bitNumber the bitNumber to set
	 */
	public void setBitNumber(int bitNumber)
	{
		this.bitNumber = bitNumber;
	}
	/**
	 * @return the roleName
	 *//*
	public String getRoleName()
	{
		return roleName;
	}
	*//**
	 * @param roleName the roleName to set
	 *//*
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}*/
}
