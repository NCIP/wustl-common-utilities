/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.audit;

/**
 * This class represents the simple attributes for the AuditableClass.
 * @author niharika_sharma
 *
 */
public class Attribute
{
	/**
	 * Name of the attribute.
	 */
	private String name;
	/**
	 * Data type of the attribute.
	 */
	private String dataType;
	/**
	 * Returns the DataType.
	 * @return dataType.
	 */
	public String getDataType()
	{
		return dataType;
	}
	/**
	 * Sets the DataType.
	 * @param dataType dataType to set.
	 */
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	/**
	 * Returns the name.
	 * @return name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Sets the name.
	 * @param name name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
