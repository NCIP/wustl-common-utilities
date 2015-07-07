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
 * <p>Title: DAO Interface>
 * <p>Description:	DAO provides methods to manipulate the domain objects.
 * It provides methods like insert ,update etc .</p>
 * @version 1.0
 * @author kalpana_thakur
 */

package edu.wustl.dao;

import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;


/**
 * @author kalpana_thakur
 * Handles database operations like insertion, updation, deletion and retrieval of data.
 */
public interface DAO
{
	/**
	 * Insert the Object to the database.
	 * @param obj Object to be inserted in database
	 * @throws DAOException generic DAOException
	 */
	void insert(Object obj) throws DAOException;//,boolean isAuditable)
	/**
	 * Insert the object into the database.
	 * @param entityName from hbm file.
	 * @param obj Object to be inserted in database.
	 * @throws DAOException : generic DAOException
	 */
	void insert(String entityName,Object obj) throws DAOException;

	/**
	 * updates the object into the database.
	 * @param obj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	void update(Object obj) throws DAOException;


	/**
	 * Deletes the persistent object from the database.
	 * @param obj The object to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	void delete(Object obj) throws DAOException;
	/**
	 * Deletes the persistent object from the database.
	 * @param entityName from hbm file.
	 * @param obj The object to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	void delete(String entityName,Object obj) throws DAOException;

	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "!=", "<", ">", "in", "null" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @param onlyDistinctRows true if only distinct rows should be selected.
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 * @deprecated : Avoid using these methods in case of JDBC, these are specific to Hibernate.
	 * Will move them in Hibernate DAO in next release.
	 */

	List retrieve(String sourceObjectName,String[] selectColumnName,
			QueryWhereClause queryWhereClause,boolean onlyDistinctRows) throws DAOException;

	/**
	 * Retrieve and returns the source object for given id.
	 * @param sourceObjectName Source object in the Database.
	 * @param identifier identifier of source object.
	 * @return object
	 * @throws DAOException generic DAOException.
	 */
	Object retrieveById(String sourceObjectName, Long identifier) throws DAOException;
	/**
	 * Retrieve and returns the source object for given id.
	 * @param entityName from hbm file.
	 * @param identifier identifier of source object.
	 * @return object
	 * @throws DAOException generic DAOException.
	 */
	 Object retrieveByIdAndEntityName(String entityName, Long identifier) throws DAOException;

	/**
	 * Create a new instance of Query for the given HQL query string.
	 * Execute the Query and returns the list of data.
	 * @param query query
	 * @return List.
	 * @throws DAOException generic DAOException.
	 * @deprecated Deprecated this method use alternate
	 *  API exposed to support this issue.
	 */
	List executeQuery(String query)
		throws DAOException;

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @param columnValueBeans column data beans.
	 * @return list of data.
	 * @throws DAOException Database exception.
	 */
	List executeQuery(String query,List<ColumnValueBean> columnValueBeans)
	throws DAOException;

	/**
	 * Retrieves attribute value for given class name and identifier.
	 * @param objClass source Class object
	 * @param identifier identifier of the source object
	 * @param attributeName attribute to be retrieved
	 * @param columnName : where clause column field.
	 * @return List.
	 * @deprecated user retrieveAttribute in HibernateDAO.java pass column name
	 * and column value column value bean
	 * @throws DAOException generic DAOException.
	 */
	List retrieveAttribute(Class objClass, String columnName,Long identifier,
			String attributeName) throws DAOException;


	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "<", ">", "=<", ">=" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 * @deprecated : Avoid using these methods in case of JDBC, these are specific to Hibernate.
	 * Will move them in Hibernate DAO in next release.
	 */

	List retrieve(String sourceObjectName,
			String[] selectColumnName,QueryWhereClause queryWhereClause) throws DAOException;

	/**
	 * Retrieve and returns the list of all source objects for given
	 * condition on a single column. The condition value
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param whereColumnName Column name to be included in where clause.
	 * @param whereColumnValue Value of the Column name that included in where clause.
	 * @return the list of all source objects for given condition on a single column.
	 * @throws DAOException generic DAOException.
	 * @deprecated : Avoid using these methods. Use List retrieve(String sourceObjectName,
	 * List<ColumnValueBean> columnValueBeans) pass column name
	 * and column value column value bean
	 */
	List retrieve(String sourceObjectName, String whereColumnName,
			Object whereColumnValue) throws DAOException;

	/**
	 * Returns the ResultSet containing all the rows from the table represented in sourceObjectName
	 * according to the where clause.It will create the where condition clause which holds where column name,
	 * value and conditions applied.
	 * @param sourceObjectName The table name.
	 * @param columnValueBean columnValueBean
	 * @return The ResultSet containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 */
	List retrieve(String sourceObjectName,ColumnValueBean columnValueBean)
			throws DAOException;

	/**
	 * Returns the list of all source objects available in database.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @return the list of all source objects available in database.
	 * @throws DAOException generic DAOException.
	 */
	List retrieve(String sourceObjectName) throws DAOException;

	/**
	 * Returns the list of all objects with the select columns specified.
	 * @param sourceObjectName Source object in the Database.
	 * @param selectColumnName column names in the select clause.
	 * @return the list of all objects with the select columns specified.
	 * @throws DAOException generic DAOException.
	 */
	List retrieve(String sourceObjectName, String[] selectColumnName)
			throws DAOException;
	/**
	 * Create database connection having auto-commit mode as disabled.
	 * Open a Session on the given connection.
	 * mandatory to call commit to update the changes.
	 * @param sessionDataBean : This will hold the session related information.
	 * @throws DAOException : generic DAOException
	 */
	void openSession(SessionDataBean sessionDataBean) throws DAOException;

	/**
	 * End the session by releasing the JDBC connection and cleaning up.
	 * @throws DAOException :generic DAOException
	 */
	void closeSession() throws DAOException;

	/**
	 * Makes all changes made since the previous
     * commit/rollback.
     * This method should be used only when auto-commit mode has been disabled.
	 * @throws DAOException : generic DAOException
	 */
	void commit() throws DAOException;

	/**
	 * Undoes all changes made in the current transaction
	 * This method should be used only when auto-commit mode has been disabled.
	 * @throws DAOException : generic DAOException
	 */
	void rollback() throws DAOException;

	/**
	 * This method will be called to set connection Manager Object.
	 * @param connectionManager : Connection Manager.
	 */
	void setConnectionManager(IConnectionManager connectionManager);
	
	 IConnectionManager  getConnectionManager();


	 /**
	 * This method will be called to begin new transaction.
	 * @throws DAOException : It will throw DAOException.
	 */
	void beginTransaction() throws DAOException;

	/**
	 * This method will be called when user need to audit and update the changes.
	 * @param currentObj object with new changes
	 * * @param previousObj persistent object fetched from database.
	 * @throws DAOException : generic DAOException
	 */
	void update(Object currentObj,Object previousObj) throws DAOException;

	/**
	 * Merge. This method merges the object passed as parameter with the same object present in
	 * database. If no old object is present in db then new object is inserted in database.
	 * @param objectToBeMerged the object to be merged
	 */
	Object merge(Object objectToBeMerged);
}