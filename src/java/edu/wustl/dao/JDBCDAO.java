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
 * <p>Title: JDBCDAO Interface</p>
 * <p>Description :This interface defines methods which are specific to JDBC operations</p>
 *  @author kalpana_thakur
 */

package edu.wustl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.StatementData;

/** This interface defines methods which are specific to JDBC operations.*/
public interface JDBCDAO extends DAO
{

	/**
	 * This method will execute the SQL and modifies the database.
	 * @param sql sql statement.
	 * @return StatementData statement specific data like row count,
	 * but not auto-generated key(s)
	 * @throws DAOException generic DAOException.
	 * @deprecated : Avoid use of this, it will be removed in future.
	 * public StatementData executeUpdate(String sql,List<ColumnValueBean> columnValueBeans)
	 * throws DAOException
	 */
	StatementData executeUpdate(String sql) throws DAOException;

	/**
	 * This method will execute the SQL and modifies the database.
	 * @param sql sql statement.
	 * @return StatementData statement specific data like row count,
	 * generatedKeys auto-generated key(s)
	 * @throws DAOException generic DAOException.
	 * @deprecated : Avoid use of Statement.
	 */
	StatementData executeUpdateWithGeneratedKey(String sql) throws DAOException;

	/**
	 * This method will be called to get the result set.
	 * @param sql sql statement.
	 * @throws DAOException generic DAOException.
	 * @return ResultSet : ResultSet
	 * @deprecated : Avoid use of this, it will be removed in future,
	 * use public ResultSet getResultSet(String sql,
	 * List<ColumnValueBean> columnValueBeans, Integer maxRecords)
	 */
	ResultSet getQueryResultSet(String sql)throws DAOException;

	/**
	 * This method will be called to set the size of the batch.
	 * @param batchSize batchSize
	 * @throws DAOException : Generic database exception.
	 */
	void setBatchSize(int batchSize)throws DAOException;

	/**
	 * Deletes the table from the database.
	 * @param tableName The table to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	void deleteTable(String tableName) throws DAOException;

	/**
	 * @return :This will return the Date Pattern.
	 */
	String getDatePattern();

	/**
	 * @return :This will return the Time Pattern.
	 */
	String getTimePattern();

	/**
	 * @return :This will return the Date Format Function.
	 */
	String getDateFormatFunction();
	/**
	 * @return :This will return the Time Format Function.
	 */
	String getTimeFormatFunction();

	/**
	 * @return :This will return the Date to string function
	 */
	String getDateTostrFunction();
	/**
	 * @return :This will return the string to Date function
	 */
	String getStrTodateFunction();

	/**
	 * Retrieves the records for class name in sourceObjectName according to
	 * field values passed in the passed session.
	 * @param sourceObjectName This will holds the object name.
	 * @param selectColumnName An array of field names in select clause.
	 * @param queryWhereClause This will hold the where clause.It holds following:
	 * 1.whereColumnName : An array of field names in where clause.
	 * 2.whereColumnCondition : The comparison condition for the field values.
	 * 3.whereColumnValue : An array of field values.
	 * 4.joinCondition : The join condition.
	 * @param onlyDistinctRows True if only distinct rows should be selected
	 * @return The ResultSet containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 * @deprecated Avoid use of Statement.
	 * public ResultSet retrieveResultSet(String sourceObjectName, String[] selectColumnName,
	 * QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
	 * boolean onlyDistinctRows) throws DAOException
	 */
	ResultSet retrieveResultSet(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,
			 boolean onlyDistinctRows) throws DAOException;


	/**
	 * This method will be called to execute query.
	 * @param sql : sql.
	 * @param columnValueBeans :set of column value beans.
	 * @return (1) the row count for INSERT,UPDATE or DELETE statements
	 * or (2) 0 for SQL statements that return nothing
	 * @throws DAOException :Generic Exception
	 * @deprecated : Avoid use of this it will be removed in future,
	 * use public void executeUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans)
	 * throws DAOException
	 */
	StatementData executeUpdate(String sql,List<ColumnValueBean> columnValueBeans)
	throws DAOException;


	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve
	 * @param query  HQL query to execute
	 * @param startIndex Starting index value
	 * @param maxRecords max number of records to fetch
	 * @param paramValues List of parameter values.
	 * @return List of data.
	 * @throws DAOException database exception.
	 * @deprecated : Avoid use of this it will be removed in future,
	 * Use public List executeQuery(String query,
	 * Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException
	 */
	List executeQuery(String query,Integer startIndex,
			Integer maxRecords,List paramValues) throws DAOException;

	/**
	 * This method will be called to execute query.
	 * @param query :query string.
	 * @return prepared statement
	 * @throws DAOException :Generic Exception
	 * @deprecated Do not use this method.
	 */
	PreparedStatement getPreparedStatement(String query) throws DAOException;

	/**
     * Retrieves a DatabaseMetaData object that contains
     * metadata about the database to which this
     * Connection  object represents a connection.
     * The metadata includes information about the database's
     * tables, its supported SQL grammar, its stored
     * procedures, the capabilities of this connection, and so on.
     *@param tableName : table name must match the table name as it is stored
     *in this database
     * @return a  ResultSet  object for this
     *          Connection  object
     * @exception DAOException if a database access error occurs
     */
    ResultSet getDBMetaDataResultSet(String tableName) throws DAOException;

    /**
	* Returns the ResultSet containing all the rows according to the columns specified
	* from the table represented in sourceObjectName.
	* @param sourceObjectName The table name.
	* @param selectColumnName The column names in select clause.
	* @param onlyDistinctRows true if only distinct rows should be selected
	* @return The ResultSet containing all the rows according to the columns specified
	* from the table represented in sourceObjectName.
	* @throws DAOException generic DAOException.
	*/
	List retrieve(String sourceObjectName, String[] selectColumnName, boolean onlyDistinctRows)
			throws DAOException;


	/**
	 * This method has been added to close statement for which resultset is returned.
	 * @param resultSet ResultSet
	 * @throws DAOException : database exception
	 */
	void closeStatement(ResultSet resultSet) throws DAOException;


	/**
	 * @param batchSize :batchSize
	 * @param tableName : name of the table
	 * @param columnSet : columns
	 * @throws DAOException : database exception
	 */
	void batchInitialize(int batchSize,String tableName,SortedSet<String> columnSet)
	throws DAOException;

	/**
	 * @param dataMap Map holding the column value data.
	 * @throws DAOException : database exception.
	 */
	void batchInsert(SortedMap<String,ColumnValueBean> dataMap)throws DAOException;

	/**
	 * This method will be called to commit batch updates.
	 * @throws DAOException : Database exception
	 */
	void batchCommit()throws DAOException;

	/**
	 * Close the batch statement.
	 * @throws DAOException Database exception
	 */
	void batchClose()throws DAOException;

	/**
	 * This method will be called to get the result set.
	 * @param sql SQL statement.
	 * @param columnValueBeans columnValueBeans
	 * @param maxRecords maxRecords
	 * @throws DAOException generic DAOException.
	 * @return ResultSet : ResultSet
	 */
	 ResultSet getResultSet(String sql,
			List<ColumnValueBean> columnValueBeans, Integer maxRecords) throws DAOException;

	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve.
	 * @param query  HQL query to execute
	 * @param maxRecords max number of records to fetch
	 * @param columnValueBeans columnValueBeans
	 * @return List of data.
	 * @throws DAOException database exception.
	 */
	 List executeQuery(String query,
		Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException;

	 /**
	  * This method will be called to execute query.
	  * @param sql query string.
	  * @param columnValueBeans :Bean having column name ,
	  * column value,and column type.
	  * @throws DAOException :Generic Exception
	  */
	  void executeUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans)
	  throws DAOException;

	  /**
		* Retrieves the records for class name in sourceObjectName according to
		* field values passed in the passed session.
		* @param sourceObjectName This will holds the object name.
		* @param selectColumnName An array of field names in select clause.
		* @param queryWhereClause This will hold the where clause.It holds following:
		* 1.whereColumnName : An array of field names in where clause.
		* 2.whereColumnCondition : The comparison condition for the field values.
		* 3.whereColumnValue : An array of field values.
		* 4.joinCondition : The join condition.
		* @param onlyDistinctRows True if only distinct rows should be selected
		* @param columnValueBeans : This will hold column name, value and column type.
		* @return The ResultSet containing all the rows from the table represented
		* in sourceObjectName which satisfies the where condition
		* @throws DAOException : DAOException
		*/
	  ResultSet retrieveResultSet(String sourceObjectName, String[] selectColumnName,
				QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
				 boolean onlyDistinctRows) throws DAOException;

	/**
	 * Retrieves the records for class name in sourceObjectName according to
	 * field values passed in the passed session.
	 * @param sourceObjectName This will holds the object name.
	 * @param selectColumnName An array of field names in select clause.
	 * @param queryWhereClause This will hold the where clause.
	 * @param onlyDistinctRows True if only distinct rows should be selected
	 * @param columnValueBeans This will hold the column name value and it's type.
	 * @return The list containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 */
	 List retrieve(String sourceObjectName, String[] selectColumnName,
				QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
				 boolean onlyDistinctRows) throws DAOException;

	/**
	 * Returns the ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName as per the where clause.
	 * @param sourceObjectName The table name.
	 * @param selectColumnName The column names in select clause.
	 * @param queryWhereClause The where condition clause which holds the where column name,
	 * value and conditions applied
	 * @param columnValueBeans This will hold colum name value and type.
	 * @return The ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 */
	 List retrieve(String sourceObjectName,
			String[] selectColumnName, QueryWhereClause queryWhereClause,
			List<ColumnValueBean> columnValueBeans)throws DAOException;

	 /**
		 *This method will be called to get all database properties.
		 * @return database properties.
		 */
	 DatabaseProperties getDatabaseProperties();

	 /**
	  * This method will be called to set all the database specific properties.
	  * @param databaseProperties : database properties.
	  */
	  void setDatabaseProperties(DatabaseProperties databaseProperties);

}
