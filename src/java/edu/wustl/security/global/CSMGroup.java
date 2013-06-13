/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.global;

/**
 * This class has represents CSM group.
 * @author ravi_kumar
 *
 */
public class CSMGroup
{
	/**
	 * Class PG Name.
	 */
	private String pgName;
	/**
	 * Class PI Group Name.
	 */
	private String piGroupName;
	/**
	 * Class coordinator-group-name.
	 */
	private String coGroupName;
	/**
	 * Constructor.
	 * @param pgName  PG Name
	 * @param piGroupName PI Group Name.
	 * @param coGroupName coordinator-group-name.
	 */
	public CSMGroup(String pgName,String piGroupName,String coGroupName)
	{
		this.pgName=pgName;
		this.piGroupName=piGroupName;
		this.coGroupName=coGroupName;
	}
	/**
	 * @return the pgName
	 */
	public String getPgName()
	{
		return pgName;
	}

	/**
	 * @param pgName the pgName to set
	 */
	public void setPgName(String pgName)
	{
		this.pgName = pgName;
	}

	/**
	 * @return the piGroupName
	 */
	public String getPiGroupName()
	{
		return piGroupName;
	}

	/**
	 * @param piGroupName the piGroupName to set
	 */
	public void setPiGroupName(String piGroupName)
	{
		this.piGroupName = piGroupName;
	}

	/**
	 * @return the coGroupName
	 */
	public String getCoGroupName()
	{
		return coGroupName;
	}

	/**
	 * @param coGroupName the coGroupName to set
	 */
	public void setCoGroupName(String coGroupName)
	{
		this.coGroupName = coGroupName;
	}
}
