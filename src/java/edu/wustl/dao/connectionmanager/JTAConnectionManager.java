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
*<p>Title: ConnectionManager class>
 * <p>Description:	It handles all
 * hibernate specific operations like opening and closing of hibernate connection, session etc
 * Connection manager has ThreadLocal instance variable which holds the Map having session object
 * as per the application .It holds Map<ApplicationName, session>
 * thus allow user to use multiple hibernate sessions as per the application.</p>
 * @author kalpana_thakur
 * @version 1.0
 */

package edu.wustl.dao.connectionmanager;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author kalpana_thakur
 *
 */
public class JTAConnectionManager implements IConnectionManager
{

	/**
	 *Class Logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(JTAConnectionManager.class);

	/**
	 * This member will store the name of the application.
	 */
	protected String applicationName;

	/**
	 * This member will store the configuration instance.
	 */
	protected Configuration configuration;

	/**
	 * This member will store the sessionFactory instance.
	 */
	protected SessionFactory sessionFactory;

	/**
	 * This member will store data source for JDBC connection.
	 */
	protected String dataSource;

	/**
	* specify Session instance.
	*/
	private Session session = null;

	//    /**
	//  	* specify Transaction instance.
	//  	*/
	//    private Transaction transaction = null;

	/**
	 * It will instantiate applicationSessionMap.
	 * This map holds the session object associated to the application.
	 * Map will stored in threadLocal,whenever new session will be created ,
	 * threadLocal will be checked first to obtain the session associated to application.
	 * This method will be called to retrieve the current session.
	 * It will check the session for the running application in applicationSessionMap.
	 * If present, retrieved the session from the Map otherwise create the
	 * new session and store it into the Map.
	 * @return : the session.
	 *@throws DAOException :Generic DAOException.
	 */
	public Session getSession() throws DAOException
	{
		newSession();
		//beginTransaction();
		//transaction = session.beginTransaction();
		return session;
	}

	/**
	* This method will be called to begin new transaction.
	* @throws DAOException : It will throw DAOException.
	*/
	public void beginTransaction() throws DAOException
	{

		beginTxn();
		/*if(transaction != null)
		{
			logger.debug("Transaction already opened, only one transaction can be opened at a time.");
			throw DAOUtility.getInstance().getDAOException(null,
					"db.mult.transaction.open.error", "");
		}
		transaction = session.beginTransaction();*/

	}

	private void beginTxn()
	{
		UserTransaction txn = null;
		try
		{
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			if (txn.getStatus() == Status.STATUS_NO_TRANSACTION)
			{
				logger.info("=========== Starting a new Transaction ================");
				txn.begin();
			}

		}
		catch (Exception e)
		{
			throw new RuntimeException("Error beginning txn: ", e);
		}
	}

	/**
	 * This method will be called to close the session.
	 * It will check the session for the running application in applicationSessionMap,
	 * if present it will remove it from the Map.
	 *@throws DAOException :Generic DAOException.
	 */
	public void closeSession() throws DAOException
	{
		//close();
		//transaction = null;

	}

	/**
	 * This method will be called to close the session.
	 * It will check the session for the running application in applicationSessionMap,
	 * if present it will remove it from the Map.
	 *@throws DAOException :Generic DAOException.
	 */
	private void close() throws DAOException
	{
		/*try
		{
			if(session != null)
			{
				session.close();
				session=null;
			}
		}
		catch(HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp,
					"db.close.conn.error", "ConnectionManager.java ");
		}*/
	}

	/**
	* Commit the database level changes.
	* @throws DAOException : It will throw DAOException.
	*/
	public void commit() throws DAOException
	{
		UserTransaction txn = null;
		try 
		{
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		    txn.commit();
		    txn.begin();
		} 
		catch (Exception exp) 
		{
			logger.error(exp.getMessage(), exp);
			if(exp.getCause() instanceof HibernateException)
			{
				exp = (Exception)exp.getCause();
			}
			throw DAOUtility.getInstance().getDAOException(exp,"db.commit.error", "JTAConnectionManager.java ");
		}
		
		/*try
		{
			if (transaction != null)
			{
				transaction.commit();
			}
			transaction = null;
		}
		catch(HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp,
        			"db.commit.error", "ConnectionManager.java ");

		}*/
	}

	/**
	* RollBack all the changes after last commit.
	* Declared in DAO class.
	* @throws DAOException : It will throw DAOException.
	*/
	public void rollback() throws DAOException
	{
		UserTransaction txn = null;
		try
		{
			txn = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			if (txn.getStatus() == Status.STATUS_ACTIVE)
			{
				logger.error("========= Doing rollback =======");
				txn.rollback();
				txn.begin();
//				try
//				{
//					logger.error("========= Doing rollback =======");
//					txn.rollback();
//				}
//				catch(Exception e)
//				{
//					// transaction got rolled back rather being commited
//				}
//				txn.begin();
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error committing txn: ", e);
		}
		/*try
		{
			if (transaction != null)
			{
				transaction.rollback();
			}
		}
		catch(HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp,
					"db.rollback.error", "ConnectionManager.java ");
		}*/
	}

	/**
	 * This will established new session.
	 * @throws DAOException :Generic DAOException.
	 */
	private void newSession() throws DAOException
	{
		try
		{
			//session = sessionFactory.openSession();
			beginTxn();
			session = sessionFactory.getCurrentSession();
			session.setFlushMode(FlushMode.COMMIT);
			//session.connection().setAutoCommit(false);
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage(), excp);
			throw DAOUtility.getInstance().getDAOException(excp, "db.open.session.error",
					"ConnectionManager.java ");
		}
	}

	/**
	 *This method will be called to retrieved the current connection object.
	 *@return Connection object
	 *@throws DAOException :Generic DAOException.
	 */
	public Connection getConnection() throws DAOException
	{
		newSession();
		// Perhaps we need to synchronize session state with
		// underlying database connection so that changes made
		// through session are visible through connection as well
		// in same transaction context
		session.flush();
		return session.connection();
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
	 */
	public Configuration getConfiguration()
	{
		return configuration;
	}

	/**
	 * This will called to set the configuration object.
	 * @param cfg configuration
	 */
	public void setConfiguration(Configuration cfg)
	{
		this.configuration = cfg;
	}

	/**
	 * This will called to retrieve session factory object.
	 * @return sessionFactory
	 */
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	/**
	 * This will called to set session factory object.
	 * @param sessionFactory : session factory.
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
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
}
