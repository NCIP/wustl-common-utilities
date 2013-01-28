/*
 * TODO
 */

package edu.wustl.common.exceptionformatter;


import java.sql.ResultSet;
import java.util.HashMap;

import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;

/**
 * @author kalpana_thakur
 *
 */
public class MysqlExceptionFormatter implements IDBExceptionFormatter // NOPMD
{
	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	private static final Logger logger = Logger.getCommonLogger(MysqlExceptionFormatter.class);
	/**
	 * Index name Constant.
	 */
	private static final String INDEX_NAME = "INDEX_NAME";
	/**
	 * This will generate the formatted error messages.
	 * @param objExp :Exception.
	 * @param jdbcDAO : jdbcDAO.
	 * @return the formated messages.
	 */
	public String getFormatedMessage(Exception objExp, JDBCDAO jdbcDAO) // NOPMD
	{
		String formattedErrMsg = null; // Formatted Error Message return by this method
		//Connection connection = null;
		try
		{
			String tableName = Utility.parseException(objExp); // Get table name from DB exceptn msg.
			// Generate Error Message by appending all messages of previous cause Exceptions
			String sqlMessage = Utility.generateErrorMessage(objExp);
			// From the MySQL error msg and extract the key ID
			// The unique key voilation message is "Duplicate entry %s for key %d"
			int key = -1;
			int indexofMsg = 0;
			indexofMsg = sqlMessage.indexOf(Constants.MYSQL_DUPL_KEY_MSG);
			indexofMsg += Constants.MYSQL_DUPL_KEY_MSG.length();
			// Get the %d part of the string
			String strKey = sqlMessage.substring(indexofMsg, sqlMessage.length() - 1);
			key = Integer.parseInt(strKey);
			logger.debug(String.valueOf(key));
			// For the key extracted frm the string, get the column name on which costraint has failed
			boolean found = false;
			/*// get connection from arguments
			if(args[1]!=null) {	connection =(Connection)args[1];}
			else {	logger.debug("Error Message: Connection object not given");	}
			DatabaseMetaData dbmd = connection.getMetaData(); Get DB metadata object for connection*/
			//  Get a description of the given table's indices and statistics
			ResultSet rs = jdbcDAO.getDBMetaDataResultSet(tableName);
			HashMap indexDetails = new HashMap();
			int indexCount = 1;
			String constraintVoilated = "";
			while (rs.next())	// In this loop, all the indexes are stored as key of the HashMap
			{					// and the column names are stored as value.
				logger.debug("Key: " + indexCount);
				if (key == indexCount)
				{
					constraintVoilated = rs.getString(INDEX_NAME);
					logger.debug("Constraint: " + constraintVoilated);
					found = true; // break;  column name for given key index found
				}
				StringBuffer temp = (StringBuffer) indexDetails.get(rs.getString(INDEX_NAME));
				if (temp != null)
				{
					temp.append(rs.getString("COLUMN_NAME"));
					temp.append(",");
					indexDetails.remove(rs.getString(INDEX_NAME));
					indexDetails.put(rs.getString(INDEX_NAME), temp);
					logger.debug("Column :" + temp.toString());
				}
				else
				{
					temp = new StringBuffer(rs.getString("COLUMN_NAME"));
					temp.append(","); 		//temp.append(rs.getString("COLUMN_NAME"));
					indexDetails.put(rs.getString(INDEX_NAME), temp);
				}
				indexCount++; // increment record count*/
			}
			logger.debug("out of loop");
			rs.close();
			StringBuffer columnNames = new StringBuffer("");
			if (found)
			{
				columnNames = (StringBuffer) indexDetails.get(constraintVoilated);
				logger.debug("Column Name: " + columnNames.toString());
				logger.debug("Constraint: " + constraintVoilated);
			}
			formattedErrMsg = Utility.prepareMessage(columnNames, tableName, jdbcDAO);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;
		}
		return formattedErrMsg;
	}
	/**
	 *For the key extracted from the string, get the column name on which the
	  constraint has failed.It get a description of the given table's indices and statistics.
	  In this loop, all the indexes are stored as key of the HashMap
	  and the column names are stored as value.
	 * @param objExcp :
	 * @param tableName :
	 * @param jdbcDAO :
	 * @return columnNames :
	 * @throws DAOException :
	 * @throws SQLException :
	 */
	/*
		private String getColumnNames(Exception objExcp, String tableName,
				JDBCDAO jdbcDAO) throws DAOException, SQLException
		{
			String columnNames = "";
			ResultSet resultSet = null;
			try
			{
				int key = getErrorKey(objExcp);
				resultSet = jdbcDAO.getDBMetaDataResultSet(tableName);
				columnNames = getColumnNames( resultSet, key);
			}
			catch(SQLException sqlExp)
			{
				LOGGER.debug(sqlExp.getMessage(), sqlExp);
				ErrorKey errorKey = ErrorKey.getErrorKey("db.operation.error");
				throw new DAOException(errorKey,sqlExp,"MysqlFormattedErrorMessages.java :");
			}
			finally
			{
				if(resultSet != null)
				{
					resultSet.close();
				}

			}
			return columnNames;
		}
	 *//**
	 * @param resultSet :
	 * @param key :
	 * @return :
	 * @throws SQLException :
	 */
	/*
		private String getColumnNames(ResultSet resultSet, int key)
		throws SQLException
		{
			StringBuffer columnNames = new StringBuffer("");
			boolean found=false;

			HashMap<String,StringBuffer> indexDetails = new HashMap<String,StringBuffer>();

			int indexCount = 1;
			String constraintVoilated = "";
			while(resultSet.next())
			{

				if(key == indexCount)
				{
					constraintVoilated=resultSet.getString(INDEX_NAME);
					found=true; // column name for given key index found
					//break;
				}
				updateIndexDetailsMap(resultSet, indexDetails);
				indexCount++; // increment record count
			}

			if(found)
			{
				columnNames = (StringBuffer) indexDetails.get(constraintVoilated);
			}
			return columnNames.toString();
		}

	 *//**
	 * @param resultSet :Result set
	 * @param indexDetails : Map holding details of indexes.
	 * @throws SQLException :Exception.
	 */
	/*
		private void updateIndexDetailsMap(ResultSet resultSet, Map<String,StringBuffer>  indexDetails)
				throws SQLException
		{
			StringBuffer temp = (StringBuffer)indexDetails.get(resultSet.getString(INDEX_NAME));
			if(temp == null)
			{
				temp = new StringBuffer(resultSet.getString("COLUMN_NAME"));
				temp.append(DAOConstants.SPLIT_OPERATOR);
				indexDetails.put(resultSet.getString(INDEX_NAME),temp);

			}
			else
			{
				temp.append(resultSet.getString("COLUMN_NAME"));
				temp.append(DAOConstants.SPLIT_OPERATOR);
				indexDetails.remove(resultSet.getString(INDEX_NAME));
				indexDetails.put(resultSet.getString(INDEX_NAME),temp);
			}
		}

	 *//**
	 * @param objExcp :
	 * @return :
	 */
	/*
		private int getErrorKey(Exception objExcp)
		{
			// Generate Error Message by appending all messages of previous cause Exceptions
				String sqlMessage = Utility.generateErrorMessage(objExcp);

				// From the MySQL error message and extract the key ID
				// The unique key Error message is "Duplicate entry %s for key %d"

				int key = -1;
				int indexofMsg = 0;
				indexofMsg = sqlMessage.indexOf(Constants.MYSQL_DUPL_KEY_MSG);
				indexofMsg += Constants.MYSQL_DUPL_KEY_MSG.length();

				// Get the %d part of the string
				String strKey =sqlMessage.substring(indexofMsg,sqlMessage.length()-1);
				key = Integer.parseInt(strKey);
			return key;
		}
	 *//**
	 * @param objExcp :
	 * @return :
	 * @throws ClassNotFoundException :
	 */
	/*
		private String getTableName(Exception objExcp)
				throws ClassNotFoundException
		{
			String tableName;
			//get Class name from message "could not insert [class name]"
			ConstraintViolationException  cEX = (ConstraintViolationException)objExcp;
			String message = objExcp.getMessage();

			int startIndex = message.indexOf('[');
			int endIndex = message.indexOf('#');
			if(endIndex == -1)
			{
				endIndex = message.indexOf(']');
			}
			String className = message.substring((startIndex+1),endIndex);
			Class classObj = Class.forName(className);
			// get table name from class
			tableName = HibernateMetaData.getRootTableName(classObj);
			if(!(cEX.getSQL().contains(tableName)))
			{
				 tableName = HibernateMetaData.getTableName(classObj);
			}
			return tableName;
		}
	 */
}
