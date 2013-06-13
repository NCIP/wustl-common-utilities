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
 *
 */

package edu.wustl.common.datatypes;

/**
 * @author prashant_bandal
 *
 */
public class DataTypeConfigObject
{

	/**
	 * Specifies dataType Name.
	 */
	private String dataTypeName;

	/**
	 * Specifies dataType Class Name.
	 */
	private String className;

	/**
	 * @param dataTypeName the dataTypeName to set
	 */
	public void setDataTypeName(String dataTypeName)
	{
		this.dataTypeName = dataTypeName;
	}

	/**
	 * @return the dataTypeName
	 */
	public String getDataTypeName()
	{
		return dataTypeName;
	}

	/**
	 * @param dataTypeClassName the className to set
	 */
	public void setClassName(String dataTypeClassName)
	{
		this.className = dataTypeClassName;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

}
