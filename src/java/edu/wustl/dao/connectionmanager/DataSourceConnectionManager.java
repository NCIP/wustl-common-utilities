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
*<p>Title: DataSourceConnectionManager class>
 * <p>Description:	It handles only Datasource connection.
 * Connection manager has ThreadLocal instance variable which holds the Map having session object
 * as per the application .It holds Map<ApplicationName, session>
 * thus allow user to use multiple hibernate sessions as per the application.</p>
 * @author virender_mehta
 * @version 1.1.4
 */
package edu.wustl.dao.connectionmanager;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOUtility;

/**
 * This class is used to obtain connection
 * @author virender_mehta
 *
 */
public class DataSourceConnectionManager implements IConnectionManager
{
   /**
    * logger Logger - Generic logger.
    */
	private static final Logger logger =
		Logger.getCommonLogger(DataSourceConnectionManager.class);

	/**
	 * Connection object
	 */
	private Connection conn = null;
	/**
	 * This member will store the name of the application.
	 */
	protected String applicationName;

	/**
	 * This member will store data source for JDBC connection.
	 */
	protected String dataSource;


	/**
	 * This method will be called to close the session.
	 * It will check the session for the running application in applicationSessionMap,
	 * if present it will remove it from the Map.
	 *@throws DAOException :Generic DAOException.
	 */
	public void closeSession() throws DAOException
	{
		close();
	}

	/**
	 * This method will be called to close the session.
	 * It will check the session for the running application in applicationSessionMap,
	 * if present it will remove it from the Map.
	 *@throws DAOException :Generic DAOException.
	 */
	private void close()throws DAOException
	{
		try
		{
			if(conn != null)
			{
				conn.close();
				conn=null;
			}
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			throw DAOUtility.getInstance().getDAOException(e,
					"db.close.conn.error", "DataSourceConnectionManager.java ");
		}
	}


	 /**
	 * Commit the database level changes.
	 * @throws DAOException : It will throw DAOException.
	 */
	public void commit() throws DAOException
	{
		try
		{
			if (conn != null)
			{
				conn.commit();
			}
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			throw DAOUtility.getInstance().getDAOException(e,
					"db.commit.error", "DataSourceConnectionManager.java ");
		}
	}


	 /**
	 * RollBack all the changes after last commit.
	 * Declared in DAO class.
	 * @throws DAOException : It will throw DAOException.
	 */
	public void rollback() throws DAOException
	{
		try
		{
			if (conn != null)
			{
				conn.rollback();
			}
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			throw DAOUtility.getInstance().getDAOException(e,
					"db.rollback.error", "DataSourceConnectionManager.java ");
		}
	}




	/**
	 *This method will be called to retrieved the current connection object.
	 *@return Connection object
	 *@throws DAOException :Generic DAOException.
	 */
	public Connection getConnection() throws DAOException
	{
		try
		{
			InitialContext ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx.lookup(getDataSource());
			conn = dataSource.getConnection();
		}
		catch (NamingException e)
		{
			logger.error(e.getMessage(), e);
			throw DAOUtility.getInstance().getDAOException(e,
					"db.dao.init.error", "DataSourceConnectionManager.java");
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			throw DAOUtility.getInstance().getDAOException(e,
					"db.dao.init.error", "DataSourceConnectionManager.java");
		}
		return conn;
	}


   	/**
	 *This method will be called to close current connection.
	 *@throws DAOException :Generic DAOException.
	 */
	public void closeConnection() throws DAOException
	{
		close();
	}

	/**
	 * This will called to retrieve configuration object.
	 * @return configuration
	 * @throws DAOException
	 */
	public Configuration getConfiguration()
	{
		logger.error("dao.method.without.implementation");
		return null;
	}

	/**
	 * This will called to set the configuration object.
	 * @param cfg configuration
	 */
	public void setConfiguration(Configuration cfg)
	{
		logger.error("dao.method.without.implementation");
	}

	/**
	 * This will called to retrieve session factory object.
	 * @return sessionFactory
	 */
	public SessionFactory getSessionFactory()
	{
		logger.error("dao.method.without.implementation");
		return null;
	}

	/**
	 * This will called to set session factory object.
	 * @param sessionFactory : session factory.
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		logger.error("dao.method.without.implementation");
	}


	/**
	 * This method will be called to set applicationName.
	 * @param applicationName : Name of the application.
	 */
	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}

	/**
	 * This method will be called to retrieved the application Name.
	 * @return application name.
	 */
	public String getApplicationName()
	{
		return applicationName;
	}

	/**
	 * This method will be called to get the data source.
	 * @return dataSource
	 */
	public String getDataSource()
	{
		return dataSource;
	}

	/**
	 * This method will be called to set the data source.
	 * @param dataSource : JDBC connection name.
	 */
	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}
	/**
	 * Begin transaction.
	 * @throws DAOException database exception.
	 */
	public void beginTransaction() throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");

	}

	/**
	 * Get Session.
	 * @return Session
	 * @throws DAOException database exception.
	 */
	public Session getSession() throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}

}
