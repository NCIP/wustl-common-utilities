package edu.wustl.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author kalpana_thakur
 * This class will handle insert/update of all the hashed values as per the database type.
 */
public class HashedDataHandler
{
	/**
	 * Class Logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(HashedDataHandler.class);

	/**
	 * This method returns the metaData associated to the table specified in tableName.
	 * @param tableName Name of the table whose metaData is requested
	 * @param columnNames Table columns
	 * @param jdbcDAO : Database connections to retrieve meta data.
	 * @return ResultSet It will return the resultset associated to the table.
	 * @throws DAOException : DAOException
	 * @throws SQLException :
	 */
	private ResultSet createQueryAndGetResultSet(String tableName,List<String> columnNames,
			JDBCDAO jdbcDAO)throws DAOException,SQLException
	{

		StringBuffer sqlBuff = new StringBuffer(DAOConstants.TRAILING_SPACES);
		sqlBuff.append("Select").append(DAOConstants.TRAILING_SPACES);

		for (int i = 0; i < columnNames.size(); i++)
		{
			sqlBuff.append(columnNames.get(i));
			if (i != columnNames.size() - 1)
			{
				sqlBuff.append("  ,");
			}
		}
		sqlBuff.append(" from " + tableName + " where 1!=1");
		return jdbcDAO.getQueryResultSet(sqlBuff.toString());
	}

	/**
	 * This method will returns the metaData associated to the table specified in tableName
	 * and update the list columnNames.
	 * @param tableName Name of the table whose metaData is requested
	 * @param jdbcDAO : Database connections to retrieve meta data.
	 * @return It will return the metaData associated to the table.
	 * @throws DAOException : DAOException
	 */
	private ResultSet createQueryAndGetResultSet(String tableName,JDBCDAO jdbcDAO)
	throws DAOException
	{
		ResultSet resultSet=null;
		try
		{

			StringBuffer sqlBuff = new StringBuffer(DAOConstants.TRAILING_SPACES);
			sqlBuff.append("Select * from " ).append(tableName).append(" where 1!=1");
			resultSet=jdbcDAO.getQueryResultSet(sqlBuff.toString());
		}
		catch (DAOException sqlExp)
		{
			logger.info(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"HashedDataHandler.java ");
		}

		return resultSet;
	}

	/**
	 * This method generates the Insert query.
	 * @param tableName : Name of the table given to insert query
	 * @param columnNamesList : List of columns of the table.
	 * @return query String.
	 */
	private String createInsertQuery(String tableName,List<String> columnNamesList)
	{
		StringBuffer query = new StringBuffer("INSERT INTO " + tableName + "(");
		StringBuffer colValues = new StringBuffer();
		Iterator<String> columnIterator = columnNamesList.iterator();
		while (columnIterator.hasNext())
		{
			query.append(columnIterator.next());
			colValues.append(DAOConstants.INDEX_VALUE_OPERATOR).append(DAOConstants.TRAILING_SPACES);
			if (columnIterator.hasNext())
			{
				query.append(DAOConstants.DELIMETER).append(DAOConstants.TRAILING_SPACES);
				colValues.append(DAOConstants.DELIMETER);
			}
			else
			{
				query.append(") values(");
				colValues.append(") ");
				query.append(colValues.toString());
			}
		}

		return query.toString();
	}

	/**
	 * @param columnValues :
	 * @param metaData :
	 * @param stmt :
	 * @throws SQLException :
	 * @throws DAOException :
	 */
	private void setStmtIndexValue(List<Object> columnValues,
			ResultSetMetaData metaData, PreparedStatement stmt)
			throws SQLException, DAOException
	{
		for (int i = 0; i < columnValues.size(); i++)
		{
			Object obj = columnValues.get(i);
			int index = i;index++;
			if(isTimeStampColumn(stmt,index,obj))
			{
				continue;
			}
			if(isDateColumn(metaData,index))
			{
				setDateColumns(stmt, index,obj);
				continue;
			}
			if(isTinyIntColumn(metaData,index))
			{
				setTinyIntColumns(stmt, index, obj);
				continue;
			}
			if(isNumberColumn(metaData,index))
			{
				setNumberColumns(stmt, index, obj);
				continue;
			}
			stmt.setObject(index, obj);
		}
	}

	/**
	 * @param metaData :
	 * @param index :
	 * @return true if column type date.
	 * @throws SQLException :Exception
	 */
	private boolean isDateColumn(ResultSetMetaData metaData,int index) throws SQLException
	{
		boolean isDateType = false;
		String type = metaData.getColumnTypeName(index);
		if ("DATE".equals(type))
		{
			isDateType = true;
		}
		return isDateType;
	}

	/**
	 * @param metaData :
	 * @param index :
	 * @return true if column type TinyInt.
	 * @throws SQLException :Exception
	 */
	private boolean isTinyIntColumn(ResultSetMetaData metaData,int index) throws SQLException
	{
		boolean isTinyIntType = false;
		String type = metaData.getColumnTypeName(index);
		if ("TINYINT".equals(type))
		{
			isTinyIntType = true;
		}
		return isTinyIntType;
	}

	/**
	 * @param metaData :
	 * @param index :
	 * @return true if column type is Number.
	 * @throws SQLException :Exception
	 */
	private boolean isNumberColumn(ResultSetMetaData metaData,int index) throws SQLException
	{
		boolean isNumberType = false;
		String type = metaData.getColumnTypeName(index);
		if ("NUMBER".equals(type))
		{
			isNumberType = true;
		}
		return isNumberType;
	}

	/**
	 * This method called to set Number value to PreparedStatement.
	 * @param stmt : TODO
	 * @param index : TODO
	 * @param obj : Object
	 * @throws SQLException : SQLException
	 */
	private void setNumberColumns(PreparedStatement stmt,
			int index, Object obj) throws SQLException
	{
			if (obj != null	&& obj.toString().equals("##"))
			{
				stmt.setObject(index , Integer.valueOf(-1));
			}
			else
			{
				stmt.setObject(index , obj);
			}
	}

	/**
	 * This method called to set TimeStamp value to PreparedStatement.
	 * @param stmt :PreparedStatement
	 * @param index :
	 * @param obj :
	 * @return return true if column type is timeStamp.
	 * @throws SQLException SQLException
	 */
	private boolean isTimeStampColumn(PreparedStatement stmt, int index,Object obj) throws SQLException
	{
		boolean isTimeStampColumn = false;
		if(obj instanceof Timestamp)
		{
			Timestamp date = (Timestamp)obj;
			stmt.setObject(index , date);
			isTimeStampColumn = true;

		}
		return isTimeStampColumn;
	}


	/**
	 * This method is called to set TinyInt value
	 * to prepared statement.
	 * @param stmt : TODO
	 * @param index :
	 * @param obj :
	 * @throws SQLException : SQLException
	 */
	private void setTinyIntColumns(PreparedStatement stmt, int index, Object obj)
			throws SQLException
	{
		if (obj != null && (Boolean.parseBoolean(obj.toString())|| obj.equals("1")))
		{
			stmt.setObject(index , 1);
		}
		else
		{
			stmt.setObject(index, 0);
		}
	}

	/**
	 * This method used to set Date values.
	 * to prepared statement
	 * @param stmt :TODO
	 * @param index :
	 * @param obj :
	 * @throws SQLException : SQLException
	 * @throws DAOException : DAOException
	 */
	private void setDateColumns(PreparedStatement stmt,
			int index,Object obj)
			throws SQLException, DAOException
	{
		if (obj != null && obj.toString().equals("##"))
		{
			java.util.Date date = null;
			try
			{
				date = CommonUtilities.parseDate("1-1-9999", "mm-dd-yyyy");
			}
			catch (ParseException exp)
			{
				throw DAOUtility.getInstance().getDAOException(exp,
						"db.date.parse.error", "HashedDataHandler.java ");
			}
			Date sqlDate = new Date(date.getTime());
			stmt.setDate(index, sqlDate);
		}
	}


	/**
	 * This method checks the TimeStamp value.
	 * @param obj :
	 * @return It returns the TimeStamp value
	 * *//*
	private Timestamp isColumnValueDate(Object obj)
	{
		Timestamp timestamp = null;
		try
		{
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.getDefault());
			formatter.setLenient(false);
			java.util.Date date = formatter.parse(obj.toString());
			Timestamp timestampInner = new Timestamp(date.getTime());
			if (obj != null && !DAOConstants.TRAILING_SPACES.equals(obj.toString()))
			{
				timestamp = timestampInner;
			}
		}
		catch (ParseException parseExp)
		{
			logger.error(parseExp.getMessage(),parseExp);
		}

		return timestamp;
	}
*/

	/**
	 * This method will be called to insert hashed data values.
	 * @param tableName :Name of the table
	 * @param columnValues :List of column values
	 * @param columnNames  :List of column names.
	 * @param jdbcDAO : Database jdbcDAO
	 * @throws DAOException  :DAOException
	 */
	public void insertHashedValues(String tableName, List<Object> columnValues, List<String> columnNames,
			JDBCDAO jdbcDAO)throws DAOException
	{
		List<String>columnNamesList = new ArrayList<String>();
		ResultSetMetaData metaData;
		ResultSet resultSet=null;
		PreparedStatement stmt = null;
		try
		{
			if(columnNames != null && !columnNames.isEmpty())
			{
				resultSet = createQueryAndGetResultSet(tableName, columnNames,jdbcDAO);
				metaData=resultSet.getMetaData();
				columnNamesList = columnNames;
			}
			else
			{
				resultSet = createQueryAndGetResultSet(tableName,jdbcDAO);
				metaData=resultSet.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++)
				{
					columnNamesList.add(metaData.getColumnName(i));
				}
			}

			String insertQuery = createInsertQuery(tableName,columnNamesList);
			stmt = jdbcDAO.getPreparedStatement(insertQuery);
			setStmtIndexValue(columnValues, metaData, stmt);
			stmt.executeUpdate();
		}
		catch (SQLException sqlExp)
		{
			logger.info(sqlExp.getMessage(),sqlExp);
			throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
			"HashedDataHandler.java ");
		}
		finally
		{
			try
			{
				stmt.close();
				jdbcDAO.closeStatement(resultSet);
			}
			catch (SQLException sqlExp)
			{
				logger.info(sqlExp.getMessage(),sqlExp);
				throw DAOUtility.getInstance().getDAOException(sqlExp, "db.update.data.error",
				"HashedDataHandler.java ");
			}
		}

	}


}
