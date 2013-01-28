
package edu.wustl.common.audit;

import java.util.Map;

/**
 * The Class AuditableObject.
 */
public class AuditableObject
{

	/**
	 * added default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/** The table name. */
	private String tableName;

	/** The attribute column name map. */
	private Map<String, String> attributeColumnNameMap;

	/**
	 * @return the tableName
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @return the attributeColumnNameMap
	 */
	public Map<String, String> getAttributeColumnNameMap()
	{
		return attributeColumnNameMap;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * @param attributeColumnNameMap the attributeColumnNameMap to set
	 */
	public void setAttributeColumnNameMap(Map<String, String> attributeColumnNameMap)
	{
		this.attributeColumnNameMap = attributeColumnNameMap;
	}

	/**
	 * Gets the column name.
	 *
	 * @param attributeName the attribute name
	 *
	 * @return the column name
	 */
	public String getColumnName(String attributeName)
	{
		return attributeColumnNameMap.get(attributeName);
	}
}
