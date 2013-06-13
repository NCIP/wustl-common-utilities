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
 * <p>Title: DAOUtility Class>
 * <p>Description:	DAOUtility class holds utility methods for DAO .</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;



/**
 * @author kalpana_thakur
 * This class holds utility methods for DAO.
 */
public final class DAOUtility
{
	/**
	 * GET_PARAMETERIZED_QUERIES_DETAILS String details of parameterized queries.
	 */
	 public static final String GET_PARAM_QUERIES_DETAILS = "getParameterizedQueriesDetails";

	/**
	 * creates a singleton object.
	 */
	private static DAOUtility daoUtil = new DAOUtility();;
	/**
	 * Private constructor.
	 */
	private DAOUtility()
	{

	}
	/**
	 * returns the single object.
	 * @return Utility object.
	 */
	public static DAOUtility getInstance()
	{
		return daoUtil;
	}

	/**
     * logger Logger - Generic logger.
     */
      private static Logger logger =
           Logger.getCommonLogger(DAOUtility.class);
	 /**
     * Constants that will appear in HQL for retrieving Attributes of the Collection data type.
     */
    private static final String ELEMENTS = "elements";

    /**
     * To Create the attribute name for HQL select part.
     * If the  selectColumnName is in format "elements((attributeName))" then
     * it will return String as "elements(className.AttributeName)"
     * else it will return String in format "className.AttributeName"
     * @param className The className
     * @param selectColumnName The select column name passed to form HQL.
     * either in format "elements(attributeName)" or "AttributeName"
     * @return The Select column name for the HQL.
     */
    public String createAttributeNameForHQL(String className, String selectColumnName)
    {
		String attribute;
		// Check whether the select Column start with "elements" & ends with ")" or not
		if (isColumnNameContainsElements(selectColumnName))
		{
			int startIndex = selectColumnName.indexOf('(')+1;
			attribute =  selectColumnName.substring(0,startIndex) +
			className + "." + selectColumnName.substring(startIndex);
		}
		else
		{
			attribute =  className + "." + selectColumnName;
		}
		return attribute;
	}

	/**
	 * Check whether the select Column start with "elements" & ends with ")" or not.
	 * @param nameOfColumn The columnName
	 * @return true if the select Column start with "elements" & ends with ")" or not
	 */
	public boolean isColumnNameContainsElements(String nameOfColumn)
	{
		String columnName = nameOfColumn;
		columnName = columnName.toLowerCase().trim();
		return columnName.startsWith(ELEMENTS) && columnName.endsWith(")");
	}

	/**
     * Parses the fully qualified class Name and returns only the class Name.
     * @param fullyQualifiedName The fully qualified class Name.
     * @return The className.
	 * @throws DAOException database exception
     */
    public String parseClassName(String fullyQualifiedName) throws DAOException
    {
    	String qualifiedName = fullyQualifiedName;
        try
        {
        	qualifiedName = fullyQualifiedName.substring(fullyQualifiedName.
        			lastIndexOf(DAOConstants.DOT_OPERATOR) + 1);
        }
        catch (Exception exp)
        {
        	logger.fatal("Problem while retrieving Fully Qualified class name.", exp);
        	throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"DAOUtility.java ");
        }
        return qualifiedName;
    }



	/** This method add details of query in a list.
	 * @param queryName String name of query.
	 * @param values List of type Object object list.
	 * @return Collection containing details of query.
	 * @throws DAOException :
	 * TODO : need to check this method.have to remove this
	 */

	public Collection<Object> executeHQL(String queryName, List<Object> values) throws DAOException
	{
		DAO dao = null;
		try
		{
			String appName = CommonServiceLocator.getInstance().getAppName();
			dao = DAOConfigFactory.getInstance().
			getDAOFactory(appName).getDAO();
			dao.openSession(null);
			return((HibernateDAO)dao).executeNamedQuery(queryName,null);

			/*if (values != null)
			{
				for (int counter = 0; counter < values.size(); counter++)
				{
					Object value = values.get(counter);
					String onlyClassName = value.getClass().getSimpleName();
					if (String.class.getSimpleName().equals(onlyClassName))
					{
						query.setString(counter, (String) value);
					}
					else if (Integer.class.getSimpleName().equals(onlyClassName))
					{
						query.setInteger(counter, Integer.parseInt(value.toString()));
					}
					else if (Long.class.getSimpleName().equals(onlyClassName))
					{
						query.setLong(counter, Long.parseLong(value.toString()));
					}

				}
			}*/
		}
		catch(HibernateException excp)
		{
			throw DAOUtility.getInstance().getDAOException(excp,
					"db.update.data.error","DAOUtility.java :");
		}
		finally
		{
			dao.closeSession();
		}
	}
	/**
	 * Return the output of execution of query.
	 * @param queryName String name of query.
	 * @return Collection containing output of execution of query.
	 * @throws DAOException :
	 */
	public Collection executeHQL(String queryName)
	throws DAOException
	{
		return executeHQL(queryName, null);
	}

	/**
	 * Returns the list from RS.
	 * @param resultSet :RS
	 * @return :List
	 * @throws SQLException :
	 */
	public List getListFromRS(ResultSet resultSet)throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		List list = new ArrayList();
		while (resultSet.next())
		{
			updateList(resultSet,list,columnCount,metaData);
		}

		return list;
	}

	/**
	 * This method will read the resultSet and update the list.
	 * @param list :list of data
	 * @param columnCount : number of columns
	 * @param metaData : meta data
	 * @param resultSet : resultSet
	 * @throws SQLException : exception
	 */
	private void updateList(ResultSet resultSet,List list,
			int columnCount,ResultSetMetaData metaData) throws SQLException
	{
		List rowDataList = new ArrayList();
		for (int i = 1; i <= columnCount; i++)
		{
			Object retObj;
			switch (metaData.getColumnType(i))
			{
			case Types.CLOB :
				retObj = resultSet.getObject(i);
				break;
			case Types.DATE :
			case Types.TIMESTAMP :
				retObj = resultSet.getTimestamp(i);
				if (retObj == null)
				{
					break;
				}
				SimpleDateFormat formatter = new SimpleDateFormat(
						DAOConstants.DATE_PATTERN_MM_DD_YYYY + " "
						+ DAOConstants.TIME_PATTERN_HH_MM_SS);
				retObj = formatter.format((java.util.Date) retObj);
				break;
			default :
				retObj = resultSet.getObject(i);
				if (retObj != null)
				{
					retObj = retObj.toString();
				}
			}
			if (retObj == null)
			{
				rowDataList.add("");
			}
			else
			{
				rowDataList.add(retObj);
			}
		}
		list.add(rowDataList);
	}
	/**
	 * Checks result set.
	 * @return :true if result set exists.
	 * @param resultSet : query resultSet
	 * @throws DAOException : DAOException
	 */
	public boolean isResultSetExists(ResultSet resultSet)throws DAOException
	{
		boolean isResultSetExists = false;
		try
		{

			if(resultSet.next())
			{
				isResultSetExists = true;
			}

		}
		catch(SQLException exp)
		{
			throw DAOUtility.getInstance().getDAOException(exp,
        			"db.retrieve.data.error", "DatabaseConnectionParams.java ");
		}
		return isResultSetExists;
	}

	/**
	 * @param query queryObject
	 * @param namedQueryParams : Query parameters to set
	 */
	public void substitutionParameterForQuery(Query query,
		Map<String, NamedQueryParam> namedQueryParams)
	{
		if(namedQueryParams != null && !namedQueryParams.isEmpty())
		{
			for (int counter = 0; counter < namedQueryParams.size(); counter++)
			{
				NamedQueryParam queryParam = (NamedQueryParam) namedQueryParams.get(counter+"");

				int objectType = queryParam.getType();
				if ( DBTypes.STRING == objectType)
				{
					query.setString(counter, queryParam.getValue().toString());
				}
				else if (DBTypes.INTEGER == objectType)
				{
					query.setInteger(counter, Integer.parseInt
							(queryParam.getValue().toString()));
				}
				else if (DBTypes.LONG == objectType)
				{
					query.setLong(counter, Long.parseLong(queryParam.getValue().toString()));
				}
				else if (DBTypes.BOOLEAN == objectType)
				{
					query.setBoolean(counter,
							Boolean.parseBoolean(queryParam.getValue().toString()));
				}
			}
		}
	}


	/**
	 * @param exception : DAOException thrown
	 * @param errorName : error key
	 * @param msgValues : Error message
	 * @return the DAOException instance.
	 */
	public DAOException getDAOException(Exception exception,String errorName, String msgValues)
	{
		if(exception != null)
		{
			logger.debug(exception.getMessage(),exception);
		}
		return new DAOException(ErrorKey.getErrorKey(errorName),exception,msgValues);

	}

	/**
	 * This method is used to get the token from the string.
	 * @param string string
	 * @param tokenSize size
	 * @return token
	 */
	public String getToken(String string , int tokenSize)
	{
		String tempString = string.trim();
		char[] dst = new char[tokenSize];
		tempString.getChars(0, tokenSize, dst, 0);
		return new String(dst);
	}

	/**
	 * This method will be called to check for invalid/malicious data.
	 * @param sql : Query having '?' as parameters
	 * @param beans : having column name, value and column type.
	 * @throws DAOException : database exception.
	 */
	public static void checkforInvalidData(String sql,
			List<ColumnValueBean> beans) throws DAOException
	{
		Iterator<ColumnValueBean> beansIter = beans.iterator();
		while(beansIter.hasNext())
		{
			ColumnValueBean bean = beansIter.next();
			for (int counter = 0; counter < DAOConstants.INVALID_DATA.length; counter++)
			{
				if (bean.getColumnValue() instanceof String
						&& (bean.getColumnValue().toString()).trim().toLowerCase().contains(
								DAOConstants.INVALID_DATA[counter].trim().toLowerCase()))
				{
					boolean isExtraCheckRequired = isExtraCheckRequired(DAOConstants.INVALID_DATA[counter]);

					if (!isExtraCheckRequired
							|| (isExtraCheckRequired && hasSQLKeyword(bean.getColumnValue()
									.toString().trim().toLowerCase(),
									DAOConstants.INVALID_DATA[counter])))
					{
						logger.error("SQl : " + sql + "  Invalid data : "
								+ bean.getColumnValue().toString()
								+ " Encountered invalid character:"
								+ DAOConstants.INVALID_DATA[counter]);
						throw DAOUtility.getInstance().getDAOException(
								null,
								"db.malicious.data.encountered",
								bean.getColumnValue().toString() + ":"
										+ DAOConstants.INVALID_DATA[counter]);
					}
				}
			}
		}
	}

	/**
	 * This method will check if the given SQL keyword is one of the keywords that
	 * needs extra check before invalidating the input data.
	 * Extra check is required for: execute, exec, sp_executesql, delete, drop
	 * @param sqlKeyword
	 * @return isExtraCheckRequired
	 */
	private static boolean isExtraCheckRequired(String sqlKeyword)
	{
		boolean isExtraCheckRequired = false;
		for (int counter = 0; counter < DAOConstants.EXTRA_CHECK_DATA.length; counter++)
		{
			if (sqlKeyword.equals(DAOConstants.EXTRA_CHECK_DATA[counter]))
			{
				isExtraCheckRequired = true;
				break;
			}
		}
		return isExtraCheckRequired;
	}

	/**
	 * This method splits the value using 'space' delimiter and checks if
	 * the sqlKeyword is present in the data.
	 * @param value
	 * @param sqlKeyword
	 * @return hasSQLKeyword
	 */
	private static boolean hasSQLKeyword(String value, String sqlKeyword)
	{
		boolean hasSQLKeyword = false;
		String[] splitValue = value.split(" ");
		for (int i = 0; i < splitValue.length; i++)
		{
			if(sqlKeyword.equals(splitValue[i]))
			{
				hasSQLKeyword = true;
				break;
			}
		}
		return hasSQLKeyword;
	}

	/**
	 * This method will be called to look for invalid data.
	 * @param dataValue dataValue
	 * @throws DAOException database exception.
	 */
	public static void checkforInvalidData(Object dataValue) throws DAOException
	{
		for(int counter =0; counter < DAOConstants.INVALID_DATA.length;counter++)
		{
			if(dataValue instanceof String &&
				(dataValue.toString()).trim()
				.toLowerCase()
				.contains(DAOConstants.INVALID_DATA[counter].trim().toLowerCase()))
			{

				logger.error("  Invalid data : "+ dataValue.toString()+
			    	" Encountered invalid character:" +
			    	DAOConstants.INVALID_DATA[counter]);
				throw DAOUtility.getInstance().getDAOException(null,
				"db.malicious.data.encountered",dataValue.toString()+
				":"+DAOConstants.INVALID_DATA[counter]);
			}
		}
	}

	/**
	 *
	 * @return
	 * @throws DAOException
	 */
	public HibernateDAO getHibernateDAO(SessionDataBean sessionDataBean) throws DAOException
	{
		String appName = CommonServiceLocator.getInstance().getAppName();
		HibernateDAO hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(
				appName).getDAO();
		hibernateDao.openSession(sessionDataBean);
		return hibernateDao;
	}

	/**
	 * Close hibernate dao.
	 *
	 * @param hibernateDao the hibernate dao
	 *
	 * @throws DAOException the DAO exception
	 */
	public void closeHibernateDAO(HibernateDAO hibernateDao) throws DAOException
	{
		if (hibernateDao != null)
		{
			hibernateDao.closeSession();
		}
	}

	/**
	 * Gets the column value bean list.
	 *
	 * @param valueObjects the value objects
	 *
	 * @return the column value bean list
	 */
	public List<ColumnValueBean> getColumnValueBeanList(Object[] valueObjects)
	{
		List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
		for (Object valueObject : valueObjects)
		{
			columnValueBeans.add(new ColumnValueBean(valueObject));
		}
		return columnValueBeans;
	}
	

	/**
	 * @throws NamingException 
	 * @throws NotSupportedException 
	 * @throws SystemException 
	 * 
	 */
	public void beginTransaction()
	{
		beginTransaction(0);
	}
	
	public void beginTransaction(int seconds) 
	{
		UserTransaction txn = null;
		try {
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			if (txn.getStatus() == Status.STATUS_NO_TRANSACTION)
			{
				if (seconds > 0) {
					txn.setTransactionTimeout(seconds);
				} else {
					txn.setTransactionTimeout(0); // restore default value
				}
				txn.begin();
			}
		}
		catch (Exception e) 
		{
			throw new RuntimeException("Error beginning a user transaction", e);
		}
	}
	 

	/**
	 * @throws DAOException
	 */
	public void commitTransaction() throws DAOException
	{
		UserTransaction txn = null;
		try
		{
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			txn.commit();
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			if (exp.getCause() instanceof HibernateException)
			{
				exp = (Exception) exp.getCause();
			}
			throw DAOUtility.getInstance().getDAOException(exp, "db.commit.error",
					"JTAConnectionManager.java ");
		}
	}

	/**
	 * 
	 */
	public void rollbackTransaction()
	{
		UserTransaction txn = null;
		try
		{
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			if (txn.getStatus() == Status.STATUS_ACTIVE)
			{
				txn.rollback();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error committing txn: ", e);
		}
	}
	
	/**
	 * @return
	 * @throws NamingException
	 * @throws SystemException
	 */
	public Transaction suspendTransaction() throws NamingException, SystemException
	{
		TransactionManager tm = (TransactionManager) new InitialContext()
				.lookup("java:/TransactionManager");
		Transaction txn = tm.suspend();
		return txn;
	}

	/**
	 * @param txn
	 * @throws NamingException
	 * @throws InvalidTransactionException
	 * @throws IllegalStateException
	 * @throws SystemException
	 */
	public void resumeTransaction(Transaction txn) throws NamingException,
			InvalidTransactionException, IllegalStateException, SystemException
	{
		TransactionManager tm = (TransactionManager) new InitialContext()
				.lookup("java:/TransactionManager");
		tm.resume(txn);
	}
	
	public void executeDDL(String appName, String ddl) {
		executeDDL(appName, ddl, true);
	}
	
	public void executeDDL(String appName, String ddl, boolean useNewConnection) {
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			IDAOFactory df = DAOConfigFactory.getInstance().getDAOFactory(appName);
			IConnectionManager connMgr = df.getDAO().getConnectionManager();
			
			if (useNewConnection) {
				session = connMgr.getSessionFactory().openSession();
			} else {
				session = connMgr.getSessionFactory().getCurrentSession();
			}

			conn = session.connection();
			stmt = conn.createStatement();
			stmt.execute(ddl);
		} catch (Exception e) {
			throw new RuntimeException("Error executing DDL " + ddl, e);
		} finally {
			if (stmt != null) {
				try { 
					stmt.close();
				} catch (Exception e) { }
			}
			
			if (useNewConnection && conn != null) {
				try {
					conn.close();
					session.close();
				} catch (Exception e) { }
			}
		}
	}
}
