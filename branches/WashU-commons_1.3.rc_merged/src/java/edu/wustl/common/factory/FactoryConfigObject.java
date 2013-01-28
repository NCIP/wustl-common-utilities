/**
 *
 */
package edu.wustl.common.factory;


/**
 * @author prashant_bandal
 *
 */
public class FactoryConfigObject
{
	/**
	 * Specifies dataType Name.
	 */
	private String factoryName;

	/**
	 * Specifies dataType Class Name.
	 */
	private String factoryClassName;

	/**
	 * @return the factoryName
	 */
	public String getFactoryName()
	{
		return factoryName;
	}

	/**
	 * @param factoryName the factoryName to set
	 */
	public void setFactoryName(String factoryName)
	{
		this.factoryName = factoryName;
	}

	/**
	 * @return the factoryClassName
	 */
	public String getFactoryClassName()
	{
		return factoryClassName;
	}

	/**
	 * @param factoryClassName the factoryClassName to set
	 */
	public void setFactoryClassName(String factoryClassName)
	{
		this.factoryClassName = factoryClassName;
	}

}
