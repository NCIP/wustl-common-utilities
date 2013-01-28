/**
 * <p>Title: EqualClause Class>
 * <p>Description:	LessThenClause implements the Condition interface,
 * it is used to generate "<" condition clause of query.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.condition;

import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author kalpana_thakur
 * This class is used to generate "<" condition clause of query
 */
public class LessThenClause implements Condition
{


	/**
	 * Name of the where Column.
	 */
	private final String columnName;


	/**
	 * Value of the where column.
	 */
	private final Object colValue;

	/**
	 * Name of the class or table.
	 */
	private String sourceObjectName;


	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName :Name of the Column
	 * @param sourceObjectName :Name of the Table or class name.
	 * @param object :Column value
	 */
	public LessThenClause (String columnName ,Object object,String sourceObjectName)
	{
		this.columnName = columnName;
		this.colValue = object;
		this.sourceObjectName = sourceObjectName;
	}

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName :Name of the Column
	 * @param object :Column value
	 */
	public LessThenClause (String columnName ,Object object)
	{
		this.columnName = columnName;
		this.colValue = object;
	}

	/**
	 * This method will be called to build Greater clause.
	 * @return Query string.
	 * @throws DAOException Database exception
	 */
	public String buildSql()throws DAOException
	{
		DAOUtility.checkforInvalidData(colValue);
		StringBuffer strBuff = new StringBuffer(DAOConstants.TRAILING_SPACES);

		String sourceObject = DAOUtility.getInstance().parseClassName(sourceObjectName);

		strBuff.append(sourceObject).append(DAOConstants.DOT_OPERATOR).
		append(columnName).append(DAOConstants.TRAILING_SPACES).append(DAOConstants.LESSTHEN).
		append(DAOConstants.TRAILING_SPACES);

		if(colValue instanceof String)
		{
			strBuff.append("'"+colValue+"'");
		}
		else
		{
			strBuff.append(colValue);
		}
		strBuff.append(DAOConstants.TRAILING_SPACES);
		return strBuff.toString();
	}


	/**
	 * @return class name or table name.
	 */
	public String getSourceObjectName()
	{
		return sourceObjectName;
	}


	/**
	 * @param sourceObjectName set the class name or table name.
	 */
	public void setSourceObjectName(String sourceObjectName)
	{
		this.sourceObjectName = sourceObjectName;
	}




}
