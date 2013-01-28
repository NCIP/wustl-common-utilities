/**
 * <p>Title: AbstractJDBCDAOImpl Class>
 * <p>Description:	JDBCDAO is default implementation of DAO and JDBCDAO through JDBC.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.connectionmanager.ConnectionManager;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.StatementData;


/**
 * @author kalpana_thakur
 *
 */
public abstract class AbstractJDBCDAOImpl extends AbstractDAOImpl implements JDBCDAO
{
	/**
  	* LOGGER Logger - class logger.
  	*/
  	private static final Logger logger =
  		Logger.getCommonLogger(AbstractJDBCDAOImpl.class);

	/**
	 * Connection.
	 */
	private Connection connection = null ;

	/**
	 * This will hold all database specific properties.
	 */
	private DatabaseProperties databaseProperties;

	/**
	 * batch statement.
	 */
	private PreparedStatement prepBatchStatement;

	/**
	 * Query preparedStatement.
	 */
	private PreparedStatement preparedStatement;

	/**
	 * This will maintain the list of all statements.
	 */
	private List<Statement> openedStmts = new ArrayList<Statement>();

	/**
	 * This method will be used to establish the session with the database.
	 * Declared in DAO class.
	 * @param sessionDataBean : holds the data associated to the session.
	 * @throws DAOException :It will throw DAOException
	 */
	public void openSession(SessionDataBean sessionDataBean)
	throws DAOException
	{
		connection = connectionManager.getConnection();
	}

	/**
	 * This method will be used to close the session with the database.
	 * Declared in DAO class.
	 * @throws DAOException : It will throw DAOException.
	 */
	public void closeSession() throws DAOException
	{
		connectionManager.closeConnection();
		batchClose();
		closeConnectionParams();
	}

	/**
	 * Commit the database level changes.
	 * Declared in DAO class.
	 * @throws DAOException : It will throw DAOException
	 * @throws SMException
	 */
	public void commit() throws DAOException
	{
		logger.debug("connection commit");
		try
		{
			batchCommit();
			if(connectionManager instanceof ConnectionManager)
			{
				//Not using the getConnection method, since we don't to get a new Connection if its closed. 
				connection.commit();
			}	
			
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.commit.error",
					"AbstractJDBCDAOImpl.java");
		}
	}

	/**
	 * RollBack all the changes after last commit.
	 * Declared in DAO class.
	 * @throws DAOException : It will throw DAOException.
	 */
	public void rollback() throws DAOException
	{
		/*try
		{
			logger.debug("Session rollback");
			connection.rollback();
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.rollback.error",
					"AbstractJDBCDAOImpl.java");
		}*/

	}

	 /**
	 * This method will be called to begin new transaction.
	 * @throws DAOException : It will throw DAOException.
	 */
	public void beginTransaction() throws DAOException
	{
		logger.debug("Begin transaction .");
		connectionManager.beginTransaction();
	}

	/**
	 * @param batchSize :batchSize
	 * @param tableName : name of the table
	 * @param columnSet : columns
	 * @throws DAOException : database exception
	 */
	public void batchInitialize(int batchSize,String tableName,SortedSet<String> columnSet)
	throws DAOException
	{
		logger.debug("Initialize batch");
		try
		{
			validateBatchParams(batchSize,tableName,columnSet);
			batchCounter = 0;
			setBatchSize(batchSize);
			String sql = generateQuery(tableName,columnSet);
			prepBatchStatement = getConnection().prepareStatement(sql);
		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException
			(exp, "db.batch.initialization.error", "AbstractJDBCDAOImpl.java");
		}
	}

	/**
	 * @param batchSize : set the batch size
	 * @param tableName : name of the table
	 * @param columnSet : set of columns
	 * @throws DAOException : database exception.
	 */
	private void validateBatchParams(int batchSize,String tableName,
			SortedSet<String> columnSet) throws DAOException
	{
		Validator validator = new Validator();
		if(batchSize == 0 || !validator.isNumeric(String.valueOf(batchSize)))
		{
			throw DAOUtility.getInstance().getDAOException
			(null, "db.batch.size.issue", "AbstractJDBCDAOImpl.java");
		}

		if(Validator.isEmpty(tableName))
		{
			throw DAOUtility.getInstance().getDAOException
			(null, "db.table.name.empty", "AbstractJDBCDAOImpl.java ");
		}
		if(columnSet == null || columnSet.isEmpty())
		{
			throw DAOUtility.getInstance().getDAOException
			(null, "db.column.set.empty", "AbstractJDBCDAOImpl.java");
		}
	}

	/**
	 * @param dataMap Map holding the column value data.
	 * @throws DAOException : database exception.
	 */
	public void batchInsert(SortedMap<String,ColumnValueBean> dataMap)throws DAOException
	{
		logger.debug("insert batch");
		try
		{
			if(prepBatchStatement == null)
			{
				ErrorKey errorKey = ErrorKey.getErrorKey("db.batch.initialization.error");
				throw new DAOException(errorKey,null,"AbstractJDBCDAOImpl.java :");
			}

			Iterator<String> columns = dataMap.keySet().iterator();
			int columnIndex = 1;
			while(columns.hasNext())
			{
				String column = columns.next();
				ColumnValueBean colValueBean = dataMap.get(column);
				if((colValueBean.getColumnValue() instanceof Date))
				{
					prepBatchStatement.setDate(columnIndex,setDateToPrepStmt(colValueBean));
				}
				else if(colValueBean.getColumnValue() instanceof Timestamp)
				{
					prepBatchStatement.setTimestamp(columnIndex,
							(Timestamp)colValueBean.getColumnValue());
				}
				else
				{
					prepBatchStatement.setObject(columnIndex, colValueBean.getColumnValue());
				}
				columnIndex += 1;
			}
			prepBatchStatement.addBatch();
			batchCounter += 1;
			if(batchCounter >= batchSize)
			{
				prepBatchStatement.executeBatch();
				prepBatchStatement.clearBatch();
				batchCounter = 0;
			}
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException
			(exp, "db.batch.insert.error", "AbstractJDBCDAOImpl.java");
		}

	}

	/**
	 * @param colValueBean : column data bean
	 * @throws SQLException : SQL exception.
	 * @throws DAOException database exception.
	 * @return Date date value
	 */
	private java.sql.Date setDateToPrepStmt(ColumnValueBean colValueBean)
			throws SQLException, DAOException
	{
		Date date = (Date)colValueBean.getColumnValue();
		return new java.sql.Date(date.getTime());
	}
	/**
	 * This method will be called to commit batch updates.
	 * @throws DAOException : Database exception
	 */
	public void batchCommit()throws DAOException
	{
		try
		{
			if(prepBatchStatement != null)
			{
				 if(batchCounter != 0)
				 {
					 prepBatchStatement.executeBatch();
				 }
				batchCounter = 0;
				if(connectionManager instanceof ConnectionManager)
				{
					//Not using the getConnection method, since we don't to get a new Connection if its closed. 
					connection.commit();
				}	
				
			}

		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException
			(exp, "db.batch.commit.error", "AbstractJDBCDAOImpl.java");

		}
	}

	/**
	 * Close the batch statement.
	 * @throws DAOException Database exception
	 */
	public void batchClose()throws DAOException
	{
		try
		{
			if(prepBatchStatement != null)
			{
				prepBatchStatement.close();
				preparedStatement = null;
			}
		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException
			(exp, "db.batch.close.error", "AbstractJDBCDAOImpl.java");

		}

	}

	/**
	 * @param tableName :
	 * @param columnSet set of column names
	 * @return String SQL
	 */
	private String generateQuery(String tableName,SortedSet<String> columnSet)
	{
		logger.debug("Generate String");

		StringBuffer insertSql = new StringBuffer(DAOConstants.TRAILING_SPACES);
		StringBuffer valuePart = new StringBuffer(DAOConstants.TRAILING_SPACES);
		insertSql.append("insert into").append(DAOConstants.TRAILING_SPACES).
		append(tableName).append(" (");
		valuePart.append("values (");
		Iterator<String> columns = columnSet.iterator();

		while(columns.hasNext())
		{
			insertSql.append(columns.next().toString());
			valuePart.append('?');
			if(columns.hasNext())
			{
				insertSql.append(DAOConstants.DELIMETER);
				valuePart.append(DAOConstants.DELIMETER);
			}

		}
		insertSql.append(" )");
		valuePart.append(" )");

		insertSql.append(valuePart.toString());
		logger.debug("Sql String:"+insertSql.toString());
		return insertSql.toString();

	}
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
	public ResultSet retrieveResultSet(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,
			 boolean onlyDistinctRows) throws DAOException
	{
		StringBuffer queryStrBuff = generateSQL(sourceObjectName,
		selectColumnName, queryWhereClause, onlyDistinctRows);
		return getQueryResultSet(queryStrBuff.toString());

	}

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
	public ResultSet retrieveResultSet(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
			 boolean onlyDistinctRows) throws DAOException
	{
		StringBuffer queryStrBuff = generateSQL(sourceObjectName,
		selectColumnName, queryWhereClause, onlyDistinctRows);
		ResultSet resultSet =  getResultSet(queryStrBuff.toString(), columnValueBeans, null);
	//	closeStatement(resultSet);
		return resultSet;

	}

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
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
			 boolean onlyDistinctRows) throws DAOException
	{
		logger.debug("Inside retrieve method");
		StringBuffer queryStrBuff = generateSQL(sourceObjectName,
		selectColumnName, queryWhereClause, onlyDistinctRows);
		return executeQuery(queryStrBuff.toString(), null, columnValueBeans);
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to
	 * field values passed in the passed session.
	 * @param sourceObjectName This will holds the object name.
	 * @param selectColumnName An array of field names in select clause.
	 * @param queryWhereClause This will hold the where clause.
	 * @param onlyDistinctRows True if only distinct rows should be selected
	 * @return The list containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @deprecated : Avoid use of this, it will be removed in future.
	 * public List retrieve(String sourceObjectName, String[] selectColumnName,
	 * QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans,
	 * boolean onlyDistinctRows) throws DAOException
	 * @throws DAOException : DAOException
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,
			 boolean onlyDistinctRows) throws DAOException
	{
		logger.debug("Inside retrieve method");
		StringBuffer queryStrBuff = generateSQL(sourceObjectName,
		selectColumnName, queryWhereClause, onlyDistinctRows);
		List list  = executeQuery(queryStrBuff.toString());
		return list;
	}

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
	 */
	private StringBuffer generateSQL(String sourceObjectName,
			String[] selectColumnName, QueryWhereClause queryWhereClause,
			boolean onlyDistinctRows)
	{
		StringBuffer queryStrBuff = getSelectPartOfQuery(selectColumnName, onlyDistinctRows);
		logger.debug("Prepare from part of the query");
		queryStrBuff.append("FROM ").append(sourceObjectName);

		if(queryWhereClause != null)
		{
			queryStrBuff.append(queryWhereClause.toWhereClause());
		}

		logger.debug("JDBC Query " + queryStrBuff);
		return queryStrBuff;
	}
	/**
	 * This method will return the select clause of Query.
	 * @param selectColumnName An array of field names in select clause.
	 * @param onlyDistinctRows true if only distinct rows should be selected
	 * @return It will return the select clause of Query.
	 */
	private StringBuffer getSelectPartOfQuery(String[] selectColumnName,
			boolean onlyDistinctRows)
	{
		logger.debug("Prepare select part of query");
		StringBuffer query = new StringBuffer("SELECT ");
		if ((selectColumnName != null) && (selectColumnName.length > 0))
		{
			if (onlyDistinctRows)
			{
				query.append(" DISTINCT ");
			}
			int index;
			for (index = 0; index < (selectColumnName.length - 1); index++)
			{
				query.append(selectColumnName[index]).append("  ,");
			}
			query.append(selectColumnName[index]).append("  ");
		}
		else
		{
			query.append("* ");
		}
		return query;
	}

	/**
	 * This method will be called for executing a static SQL statement.
	 * @see edu.wustl.dao.JDBCDAO#executeUpdate(java.lang.String)
	 * @return (1) the row count for INSERT,UPDATE or DELETE statements
	 * or (2) 0 for SQL statements that return nothing
	 * @param query :Holds the query string.
	 * @throws DAOException : DAOException.
	 * @deprecated : Avoid use of this, it will be removed in future.
	 * public StatementData executeUpdate(String sql,List<ColumnValueBean> columnValueBeans)
	 * throws DAOException
	 */
	public StatementData executeUpdate(String query) throws DAOException
	{
		logger.debug("Execute query.");
		Statement statement = null;
		StatementData statementData = new StatementData();
		try
		{
			statement = getConnection().createStatement();
			statementData.setRowCount(statement.executeUpdate(query));
			setStatementData(statement, statementData,query,false);
			return statementData ;
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"AbstractJDBCDAOImpl.java :   "+query);
		}
		finally
		{
			closeStatementInstance(statement);
		}

	}

	/**
	 * This method will be called for executing a static SQL statement.
	 * @see edu.wustl.dao.JDBCDAO#executeUpdate(java.lang.String)
	 * @return (1) the row count for INSERT,UPDATE or DELETE statements
	 * or (2) 0 for SQL statements that return nothing
	 * @param query :Holds the query string.
	 * @throws DAOException : DAOException.
	 * @deprecated : Avoid use of Statement.
	 */
	public StatementData executeUpdateWithGeneratedKey(String query) throws DAOException
	{
		logger.debug("Execute query.");
		Statement statement = null;
		StatementData statementData = new StatementData();
		try
		{
			statement = createStatement();
			statementData.setRowCount(statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS));
			setStatementData(statement, statementData,query,true);
			return statementData ;
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"AbstractJDBCDAOImpl.java :   "+query);
		}
		finally
		{
			closeStatementInstance(statement);
		}

	}
	/**
	 * Remove statement from opened statements list.
	 * @param statement statement instance
	 * @throws DAOException database exception.
	 */
	private void removeStmts(Statement statement)throws DAOException
	{
		try
		{
			statement.close();
			openedStmts.remove(statement);
		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.stmt.close.error",
					"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
	 * This method will be called to get the result set.
	 * @param sql SQL statement.
	 * @throws DAOException generic DAOException.
	 * @return ResultSet : ResultSet
	 * @deprecated : Avoid use of this, it will be removed in future,
	 * use public ResultSet getResultSet(String sql,
	 * List<ColumnValueBean> columnValueBeans, Integer maxRecords)
	 * throws DAOException
	 */
	public ResultSet getQueryResultSet(String sql) throws DAOException
	{
		logger.debug("Get Query RS");
		try
		{
			Statement statement = createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet;
		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java  "+sql);
		}
	}

	/**
	 *This method will be called to close current connection.
	 *@param query :
	 *@throws DAOException :Generic DAOException.
	 *@return list
	 *@deprecated : Avoid use of this, it will be removed in future,
	 *use public List executeQuery(String query,
	 *Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException
	 */
	public List executeQuery(String query) throws DAOException
	{
		logger.debug("get list from RS");
		try
		{
			ResultSet resultSet = getQueryResultSet(query);
			logger.debug("RS"+resultSet);
			List resultData =  DAOUtility.getInstance().getListFromRS(resultSet);
			closeStatement(resultSet);
			return resultData;
		}
		catch(SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java "+query);
		}

	}

	/**
	 * This method will be called to execute query.
	 * @param sql query string.
	 * @param columnValueBeans :Bean having column name ,
	 * column value,and column type.
	 * @throws DAOException :Generic Exception
	 */
	public void executeUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans)
	throws DAOException
	{
		PreparedStatement stmt = null;
		try
		{
			//validate SQL.
			validateSql(sql, columnValueBeans);

			//Initialize Statement.
			 stmt = getPreparedStatement(sql);

			Iterator<LinkedList<ColumnValueBean>> columnValueBeanItr = columnValueBeans.iterator();

			while(columnValueBeanItr.hasNext())
			{
				LinkedList<ColumnValueBean> ColumnValueBeans =
					columnValueBeanItr.next();
				//Populate Statement.
				populateStatement(ColumnValueBeans, stmt);
				stmt.executeUpdate();
			}

		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"AbstractJDBCDAOImpl.java   "+sql);
		}
		catch (FileNotFoundException exp)
		{
			logger.info(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.file.not.found.error",
			sql);
		}
		catch (IOException exp)
		{
			logger.info(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.file.not.found.error",
			sql);
		}
		finally
		{
			closeStatementInstance(stmt);
		}
	}

	/**
	 * This method will be called to execute query.
	 * @param sql query string.
	 * @param columnValueBeans :Bean having column name ,
	 * column value,and column type.
	 * @return (1) the row count for INSERT,UPDATE or DELETE statements
	 * or (2) 0 for SQL statements that return nothing
	 * @throws DAOException :Generic Exception
	 * @deprecated : Avoid use of this it will be removed in future,
	 * use public void executeUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans)
	 * throws DAOException
	 */
	public StatementData executeUpdate(String sql,List<ColumnValueBean> columnValueBeans)
	throws DAOException
	{
		try
		{
			if(!sql.contains("?") || columnValueBeans == null || columnValueBeans.isEmpty())
			{
				throw DAOUtility.getInstance().getDAOException(null, "db.prepstmt.param.error",
				"AbstractJDBCDAOImpl.java  "+sql);
			}

			PreparedStatement stmt = getPreparedStatement(sql);

			Iterator<ColumnValueBean> colValItr =  columnValueBeans.iterator();
			int index = 1;
			while(colValItr.hasNext())
			{
				ColumnValueBean colValueBean = colValItr.next();

				if((colValueBean.getColumnValue() instanceof Date))
				{
					stmt.setDate(index,setDateToPrepStmt(colValueBean));
				}
				else if(colValueBean.getColumnValue() instanceof Timestamp)
				{
					stmt.setTimestamp(index,(Timestamp)colValueBean.getColumnValue());
				}
				else
				{
					stmt.setObject(index, colValueBean.getColumnValue());
				}

				index += 1;
			}
			StatementData statementData = new StatementData();
			statementData.setRowCount(stmt.executeUpdate());
			setStatementData(stmt, statementData,sql,false);
			return statementData;
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"AbstractJDBCDAOImpl.java   "+sql);
		}
		finally
		{
			closePreparedStmt();
		}
	}


	/**
	 * Populate statement with values.
	 * @param columnValueBeans columnValueBeans
	 * @param stmt statement instance.
	 * @throws SQLException SQLException
	 * @throws DAOException DAOException
	 * @throws IOException
	 */
	private void populateStatement(
			List<ColumnValueBean> columnValueBeans,
			PreparedStatement stmt) throws SQLException, DAOException, IOException
	{
		if(columnValueBeans != null)
		{
			Iterator<ColumnValueBean> colValItr =  columnValueBeans.iterator();
			int index = 1;
			while(colValItr.hasNext())
			{
				ColumnValueBean colValueBean = colValItr.next();

				if(colValueBean.getColumnValue() instanceof Timestamp)
				{
					stmt.setTimestamp(index,(Timestamp)colValueBean.getColumnValue());
				}
				else if((colValueBean.getColumnValue() instanceof Date))
				{
					stmt.setDate(index,setDateToPrepStmt(colValueBean));
				}
				else if (colValueBean.getColumnValue() instanceof File)
				{
					File file = (File)colValueBean.getColumnValue();
					FileInputStream fis = new FileInputStream(file);
			        int fileLength = (int) file.length();
			        stmt.setBinaryStream(index,fis, fileLength);
				}
				else if (colValueBean.getColumnValue() instanceof Blob)
				{
					 stmt.setBlob(index,(Blob)colValueBean.getColumnValue());
				}
				else if (colValueBean.getColumnValue() instanceof Clob)
				{
					 stmt.setClob(index,(Clob)colValueBean.getColumnValue());
				}
				else if (colValueBean.getColumnValue() instanceof StringReader)
				{
					StringReader reader = (StringReader) colValueBean.getColumnValue();
					int length = 0;
					do
					{
						length++;
					}while(reader.read()!= -1);
					reader.reset();
					stmt.setCharacterStream(index, reader, length);
				}
				else if(colValueBean.getColumnValue() instanceof InputStream)
				{
					InputStream is = (InputStream)colValueBean.getColumnValue();
					Blob blobFile = Hibernate.createBlob(is);
					int length = (int)blobFile.length();
					stmt.setBinaryStream(index, (InputStream)colValueBean.getColumnValue(), length);
				}
				else
				{
					stmt.setObject(index, colValueBean.getColumnValue());
				}

				index += 1;
			}
		}
	}

	/**
	 * Validate SQL and data for invalid or malicious code before initializing prepared statement.
	 * @param sql Query with '?' to initialize the prepared statement.
	 * @param columnValueBeans Beans having column name, type and data types.
	 * @throws DAOException DAOException
	 */
	private void validateSql(String sql,
			List<LinkedList<ColumnValueBean>> columnValueBeans) throws DAOException
	{

		if(!sql.contains("?") || columnValueBeans == null || columnValueBeans.isEmpty())
		{
			throw DAOUtility.getInstance().getDAOException(null, "db.prepstmt.param.error",
			sql);
		}
		
		//
		// We do not need to perform data validations as the underlying JDBC driver escapes
		// strings when prepared statement along with bind variables are used as in
		// INSERT INTO catissue_participant (first_name, last_name) values (?, ?)
		//
		
	}

	/**
	 * This method will be called to check for invalid/malicious data.
	 * @param sql : Query having '?' as parameters
	 * @param beans : having column name, value and column type.
	 * @throws DAOException : database exception.
	 *//*
	private void checkforInvalidData(String sql,
			List<ColumnValueBean> beans) throws DAOException
	{
		Iterator<ColumnValueBean> beansIter = beans.iterator();
		while(beansIter.hasNext())
		{
			ColumnValueBean bean = beansIter.next();
			for(int counter =0; counter < DAOConstants.INVALID_DATA.length;counter++)
			{
				if(bean.getColumnValue() instanceof String &&
					(bean.getColumnValue().toString()).trim()
					.toLowerCase()
					.contains(DAOConstants.INVALID_DATA[counter].trim().toLowerCase()))
				{

					logger.error("SQl : "+sql+
						"  Invalid data : "+ bean.getColumnValue().toString()+
				    	" Encountered invalid character:" +
				    	DAOConstants.INVALID_DATA[counter]);
					throw DAOUtility.getInstance().getDAOException(null,
					"db.malicious.data.encountered",bean.getColumnValue().toString()+
					":"+DAOConstants.INVALID_DATA[counter]);
				}
			}
		}
	}*/

	/**
	 * @param stmt statement instance.
	 * @param statementData statement data
	 * @param sql query string
	 * @param isGeneratedKey If statement required generated keys
	 * @throws SQLException SQL exception
	 */
	private void setStatementData(Statement stmt,StatementData statementData,String sql,boolean isGeneratedKey)
	throws SQLException
	{
		String token = DAOUtility.getInstance().getToken(sql, "insert".length());
		if(token.compareToIgnoreCase("insert") == 0 && isGeneratedKey)
		{
			statementData.setGeneratedKeys(stmt.getGeneratedKeys());
		}
		statementData.setFetchSize(stmt.getFetchSize());
		statementData.setMaxFieldSize(stmt.getMaxFieldSize());
		statementData.setMaxRows(stmt.getMaxRows());
	}

	/**
	 * This method will return the Query prepared statement.
	 * @return Statement statement.
	 * @throws DAOException :Generic Exception
	 */
	private Statement createStatement()throws DAOException
	{
		try
		{
			Statement statement = getConnection().createStatement();
			openedStmts.add(statement);
			return statement;
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.creation.error",
			"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
	 * This method will return the Query prepared statement.
	 * @param query :Query String
	 * @return PreparedStatement.
	 * @throws DAOException :Generic Exception
	 * @deprecated Do not use this method.
	 */
	public PreparedStatement getPreparedStatement(String query) throws DAOException
	{
		try
		{
			preparedStatement = getConnection().prepareStatement
			(query);//,Statement.RETURN_GENERATED_KEYS);
			openedStmts.add(preparedStatement);
			return preparedStatement;
		}
		catch (SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.creation.error",
			"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
     * Retrieves a DatabaseMetaData object that contains
     * metadata about the database to which this
     * Connection  object represents a connection.
     * The metadata includes information about the database's
     * tables, its supported SQL grammar, its stored
     * procedures, the capabilities of this connection, and so on.
     *@param tableName : table name must match the table name as it is stored
     *in this database
     * @return a  ResultSet  for this
     *          Connection  object
     * @exception DAOException if a database access error occurs
     */
   public ResultSet getDBMetaDataResultSet(String tableName) throws DAOException
   {
	   try
		{
		   ResultSet resultSet = getConnection().getMetaData().
		   getIndexInfo(getConnection().getCatalog(), null,tableName, true, false);
		   return resultSet;
		}
	    catch (SQLException sqlExp)
		{
	    	logger.error(sqlExp.getMessage(),sqlExp);
	    	throw DAOUtility.getInstance().getDAOException(sqlExp, "db.retrieve.data.error",
				"AbstractJDBCDAOImpl.java ");
		}
   }
	/**
	 * This method will be called to close all the Database connections.
	 * @throws DAOException :Generic Exception
	 */
	protected void closeConnectionParams()throws DAOException
	{
			closeStmt();
			closePreparedStmt();
	}

	/**
	 * Closes the prepared statement if open.
	 * @throws DAOException : DAO exception
	 */
	private void closePreparedStmt() throws DAOException
	{

		try
		{
			if (preparedStatement != null)
			{
				preparedStatement.close();
				preparedStatement = null;
			}
		}
		catch(SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.close.error",
					"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
	 * Closes the prepared statement if open.
	 * @param statement statement
	 * @throws DAOException : DAO exception
	 */
	private void closeStatementInstance(Statement statement) throws DAOException
	{

		try
		{
			if(statement != null)
			{
				statement.close();
				statement = null;
			}
		}
		catch(SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.close.error",
					"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
	 * Closes the statement if open.
	 * @throws DAOException : DAO exception
	 */
	private void closeStmt() throws DAOException
	{
		try
		{
			Iterator<Statement> stmtIterator = openedStmts.iterator();
			while(stmtIterator.hasNext())
			{
				Statement stmt = stmtIterator.next();
				/*do
				{
					ResultSet resultSet = stmt.getResultSet();
					if(resultSet != null)
					{
						resultSet.close();
					}
				}while(stmt.getMoreResults());*/
				stmt.close();
			}

		}
		catch(SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.close.error",
					"AbstractJDBCDAOImpl.java ");
		}
	}

	/**
	 * This method will be called to get connection Manager object.
	 * @return IConnectionManager: Connection Manager.
	 * @throws DAOException 
	 */
	protected Connection getConnection() throws DAOException
	{
		logger.debug("Get the connection");
			if(isClosed())
			{
				openedStmts.clear();
				connection = connectionManager.getConnection();
			}
		
		return connection;
	}
	private boolean isClosed() throws DAOException
	{
		boolean isClosed = false;
		try{
			isClosed = connection.isClosed();
		}catch(HibernateException exception)
		{
			isClosed = true;
		}
		catch (SQLException e)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("dao.close.or.not.initialized");
			throw new DAOException(errorKey,null,"AbstractJDBCDAOImpl.java :");
		}
		return isClosed;
	}

	/**
	 *This method will be called to get all database properties.
	 * @return database properties.
	 */
	public DatabaseProperties getDatabaseProperties()
	{
		return databaseProperties;
	}

	/**
	 * This method will be called to set all the database specific properties.
	 * @param databaseProperties : database properties.
	 */
	public void setDatabaseProperties(DatabaseProperties databaseProperties)
	{
		this.databaseProperties = databaseProperties;
	}

	/**
	 * @return :This will return the Date Pattern.
	 */
	public String getDatePattern()
	{
		return databaseProperties.getDatePattern();
	}

	/**
	 * @return :This will return the Time Pattern.
	 */
	public String getTimePattern()
	{
		return databaseProperties.getTimePattern();
	}
	/**
	 * @return :This will return the Date Format Function.
	 */
	public String getDateFormatFunction()
	{
		return databaseProperties.getDateFormatFunction();
	}
	/**
	 * @return :This will return the Time Format Function.
	 */
	public String getTimeFormatFunction()
	{
		return databaseProperties.getTimeFormatFunction();
	}

	/**
	 * @return :This will return the Date to string function
	 */
	public String getDateTostrFunction()
	{
		return databaseProperties.getDateTostrFunction();
	}
	/**
	 * @return :This will return the string to Date function
	 */
	public String getStrTodateFunction()
	{
		return databaseProperties.getStrTodateFunction();
	}

	/**
	 * Returns the ResultSet containing all the rows in the table represented in sourceObjectName.
	 * @param sourceObjectName The table name.
	 * @return The ResultSet containing all the rows in the table represented in sourceObjectName.
	 * @throws ClassNotFoundException
	 * @throws DAOException generic DAOException
	 */
	public List retrieve(String sourceObjectName) throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, null, null,null,false);
	}

	/**
	 * Returns the ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName.
	 * @param sourceObjectName The table name.
	 * @param selectColumnName The column names in select clause.
	 * @return The ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName.
	 * @throws DAOException : DAOException
	*/
	public List retrieve(String sourceObjectName, String[] selectColumnName) throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, selectColumnName,null,null,false);
	}

	/**
	 * Returns the ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName.
	 * @param sourceObjectName The table name.
	 * @param selectColumnName The column names in select clause.
	 * @param onlyDistinctRows true if only distinct rows should be selected.
	 * @return The ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName.
	 * @throws DAOException DAOException.
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			boolean onlyDistinctRows) throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, selectColumnName,null,null,
				onlyDistinctRows);
	}

	/**
	 * Returns the ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName as per the where clause.
	 * @param sourceObjectName The table name.
	 * @param selectColumnName The column names in select clause.
	 * @param queryWhereClause The where condition clause which holds the where column name,
	 * value and conditions applied
	 * @return The ResultSet containing all the rows according to the columns specified
	 * from the table represented in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 * @deprecated : Avoid use of this method this will be removed in future.
	 * Use public List retrieve(String sourceObjectName,
	 * String[] selectColumnName, QueryWhereClause queryWhereClause,
	 * List<ColumnValueBean> columnValueBeans)throws DAOException
	 */
	public List retrieve(String sourceObjectName,
			String[] selectColumnName, QueryWhereClause queryWhereClause)
			throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, selectColumnName,queryWhereClause,false);
	}

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
	public List retrieve(String sourceObjectName,
			String[] selectColumnName, QueryWhereClause queryWhereClause,
			List<ColumnValueBean> columnValueBeans)throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, selectColumnName,queryWhereClause,columnValueBeans,false);
	}

	/**
	 * Returns the ResultSet containing all the rows from the table represented in sourceObjectName
	 * according to the where clause.It will create the where condition clause which holds where column name,
	 * value and conditions applied.
	 * @param sourceObjectName The table name.
	 * @param whereColumnName The column names in where clause.
	 * @param whereColumnValue The column values in where clause.
	 * @return The ResultSet containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 * @deprecated
	 */
	public List retrieve(String sourceObjectName, String whereColumnName, Object whereColumnValue)
			throws DAOException
	{
		logger.debug("Inside retrieve method");
		String[] selectColumnName = null;

		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(whereColumnName,whereColumnValue,sourceObjectName));

		return retrieve(sourceObjectName, selectColumnName,queryWhereClause,null,false);
	}

	public List retrieve(String sourceObjectName,
			ColumnValueBean columnValueBean) throws DAOException
	{
		logger.debug("Inside retrieve method");
		String[] selectColumnName = null;

		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(columnValueBean.getColumnName(),
				"?",sourceObjectName));

		List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
		columnValueBeans.add(columnValueBean);

		return retrieve(sourceObjectName, selectColumnName,
				queryWhereClause, columnValueBeans,
				false);
	}


	/**
	 * This method has been added to close statement for which resultset is returned.
	 * @param resultSet ResultSet
	 * @throws DAOException : database exception
	 */
	public void closeStatement(ResultSet resultSet) throws DAOException
	{
		try
		{
			Statement stmt=resultSet.getStatement();
			removeStmts(stmt);
		}
		catch(SQLException sqlExp)
		{
			logger.error(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.stmt.close.error",
			"AbstractJDBCDAOImpl.java ");
		}
	}

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
	 * Integer maxRecords,List<ColumnValueBean> columnValueBeans)
	 * throws DAOException
	 */
	public List executeQuery(String query,Integer startIndex,
			Integer maxRecords,List paramValues) throws DAOException
	{
		try
		{
			ResultSet resultSet = getQueryResultSet(query,maxRecords);
			List resultData =  DAOUtility.getInstance().getListFromRS(resultSet);
			closeStatement(resultSet);
			return resultData;
		}
		catch(SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java "+query);
		}

	}

	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve.
	 * @param query  HQL query to execute
	 * @param columnValueBeans columnValueBeans
	 * @return List of data.
	 * @throws DAOException database exception.
	  */
	public List executeQuery(String query,
			List<ColumnValueBean> columnValueBeans) throws DAOException
	{
		return executeQuery(query,null,columnValueBeans);
	}

	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve.
	 * @param query  HQL query to execute
	 * @param maxRecords max number of records to fetch
	 * @param columnValueBeans columnValueBeans
	 * @return List of data.
	 * @throws DAOException database exception.
	  */
	public List executeQuery(String query,
			Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException
	{
		try
		{
			ResultSet resultSet = getResultSet(query,columnValueBeans,maxRecords);
			List resultData =  DAOUtility.getInstance().getListFromRS(resultSet);
			closeStatement(resultSet);
			return resultData;
		}
		catch(SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java "+query);
		}

	}

	/**
	 * This method will be called to retrieve result set for specified number of records.
	 * @param sql : SQL
	 * @param maxRecords max number of records.
	 * @return the result set.
	 * @throws DAOException database exception.
	 */
	private ResultSet getQueryResultSet(String sql, int maxRecords)
			throws DAOException
	{

		logger.debug("Get Query RS [" + sql +"] MAX RECORDS =["+maxRecords + "]");
		try
		{
			Statement statement = createStatement();
			statement.setMaxRows(maxRecords);
			return statement.executeQuery(sql);
		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java  "+sql);
		}
	}

	/**
	 * This method will be called to get the result set.
	 * @param sql SQL statement.
	 * @param columnValueBeans columnValueBeans
	 * @param maxRecords maxRecords
	 * @throws DAOException generic DAOException.
	 * @return ResultSet : ResultSet
	 */
	public ResultSet getResultSet(String sql,
			List<ColumnValueBean> columnValueBeans, Integer maxRecords) throws DAOException
	{
		logger.debug("Get Query RS [" + sql +"] MAX RECORDS =["+maxRecords + "]");
		PreparedStatement statement = null;
		try
		{
			//validate SQL.
			if(columnValueBeans != null && !columnValueBeans.isEmpty())
			{
				if(!sql.contains("?"))
				{
					throw DAOUtility.getInstance().getDAOException(null,
							"db.prepstmt.param.error",sql);
				}
				DAOUtility.checkforInvalidData(sql, columnValueBeans);
			}

			//Initialize Statement.
			statement = getPreparedStatement(sql);

			//Populate Statement.
			populateStatement(columnValueBeans, statement);

			//set the max row size.
			if(maxRecords != null)
			{
				statement.setMaxRows(maxRecords.intValue());
			}
			return statement.executeQuery();

		}
		catch (SQLException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"AbstractJDBCDAOImpl.java  "+sql);
		}
		catch (FileNotFoundException exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.file.not.found.error",
			sql);
		}
		catch (IOException exp)
		{
			logger.info(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.file.not.found.error",
			sql);
		}

	}
}