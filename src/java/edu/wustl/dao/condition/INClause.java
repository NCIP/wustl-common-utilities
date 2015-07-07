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
 * <p>Title: INClause Class>
 * <p>Description:	INClause implements the Condition interface,
 * it is used to generate in clause of query.</p>
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
 * This class used to generate in clause of query
 */
public class INClause implements Condition
{

	/**
	 * Name of the where Column.
	 */
	private final String columnName;


	/**
	 * Value of the where column.
	 */
	private final Object[] colValueArray;

	/**
	 * Name of the class or table.
	 */
	private String sourceObjectName;

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName : Name of the Column
	 * @param sourceObjectName :Name of the Table or class name.
	 * @param values : Values to be given to in condition.
	 * It holds comma separated string values for queried column name.
	 */
	public INClause(String columnName, String values ,String sourceObjectName )
	{
		this.columnName = columnName;
		this.sourceObjectName = sourceObjectName;
		this.colValueArray = values.split(DAOConstants.DELIMETER);
	}

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName : Name of the Column
	 * @param values : Values to be given to in condition.
	 * It holds comma separated string values for queried column name.
	 */
	public INClause(String columnName, String values)
	{
		this.columnName = columnName;
		this.colValueArray = values.split(DAOConstants.DELIMETER);


	}

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName :Name of the Column
	 * @param sourceObjectName :Name of the Table or class name.
	 * @param object : Values to be given to in condition.
	 * It holds array of objects having values for queried column name.
	 */
	public INClause(String columnName,
			Object[] object,String sourceObjectName)
	{

		this.columnName = columnName;
		this.sourceObjectName = sourceObjectName;
		this.colValueArray = object;
	}

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param columnName :Name of the Column
	 * @param object : Values to be given to in condition.
	 * It holds array of objects having values for queried column name.
	 */
	public INClause(String columnName,
			Object[] object)
	{

		this.columnName = columnName;
		this.colValueArray = object;
	}



	/**
	 * This method will generate the in clause of Query.
	 * @return String:
	 * @throws DAOException database exception.
	 */
	public String buildSql()throws DAOException
	{
		for(Object obj : colValueArray)
		{
			DAOUtility.checkforInvalidData(obj);
		}
		StringBuffer strBuff = new StringBuffer(DAOConstants.TRAILING_SPACES);

		strBuff = new StringBuffer(DAOConstants.TRAILING_SPACES);

		String sourceObject = DAOUtility.getInstance().parseClassName(sourceObjectName);

		strBuff.append(sourceObject).append(DAOConstants.DOT_OPERATOR).append(columnName).
		append(DAOConstants.TRAILING_SPACES).append(DAOConstants.IN_CONDITION).
		append(DAOConstants.TRAILING_SPACES);

		updateInclause(strBuff);

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

	/**
	 * This is called to append all the column values to in clause.
	 * @param strBuff :
	 */
	private void updateInclause(StringBuffer strBuff)
	{

		strBuff.append("(  ");
		for (int j = 0; j < colValueArray.length; j++)
		{

			if(colValueArray[j] instanceof String)
			{
				strBuff.append("'"+colValueArray[j]+"'");
			}
			else
			{
				strBuff.append(colValueArray[j]);
			}

			if((j+1) < colValueArray.length)
			{
				strBuff.append(", ");
			}
		}
		strBuff.append(") ");
	}
}
