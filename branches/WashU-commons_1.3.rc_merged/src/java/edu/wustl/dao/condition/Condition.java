/**
 * <p>Title: Condition Interface>
 * <p>Description:	Condition Interface will be implemented by all where clause
 * conditions like "in","=","!=" e.t.c.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.condition;

import edu.wustl.dao.exception.DAOException;

/**
 * @author kalpana_thakur
 */
public interface Condition
{



	/**
	 * This method will be called to get the class name and table name.
	 * @return class name or table name.
	 */
	String getSourceObjectName();

	/**
	 * @param sourceObjectName set the class name or table name.
	 */
	void setSourceObjectName(String sourceObjectName);



	/**
	 * This will create the where clause for the condition type.
	 * It will called by QueryWhereClause.java ,it make use of column name,column value,
	 * table or class name to generate the condition.
	 * @return String:
	 * @throws DAOException database exception.
	 */
	String buildSql()throws DAOException;

}
