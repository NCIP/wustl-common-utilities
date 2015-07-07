/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao;

import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.exception.DAOException;

/**
 * @author kalpana_thakur
 *
 */
public abstract class AbstractDAOImpl implements DAO
{


	/**
	 * Class Logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(AbstractDAOImpl.class);
	/**
	 * Connection Manager.
	 */
	protected IConnectionManager connectionManager ;

	/**
	 * Batch size.
	 */
	protected int batchSize = 1;

	/**
	 * It holds the batch size.
	 */
	protected transient int  batchCounter = 0;

	/**
	 * This method will be called to set the size of the batch.
	 * @param batchSize batchSize
	 * @throws DAOException : Generic database exception.
	 */
	public void setBatchSize(int batchSize) throws DAOException
	{
		this.batchSize = batchSize;
	}

	/**
	 * This method will be used to establish the session with the database.
	 * Declared in  class.
	 * @param sessionDataBean session Data.
	 * @throws DAOException generic DAOException.
	 */
	public abstract void openSession(SessionDataBean sessionDataBean)
	throws DAOException;


	/**
	 * This method will be used to close the session with the database.
	 * Declared in DAO class.
	 * @throws DAOException : It will throw DAOException.
	 */
	public abstract void closeSession() throws DAOException;

	/**
	 * Commit the database level changes.
	 * Declared in  class.
	 * @throws DAOException generic DAOException.
	 */
	public abstract void commit() throws DAOException;

	/**
	 * RollBack all the changes after last commit.
	 * Declared in  class.
	 * @throws DAOException generic DAOException.
	 */
	public abstract void rollback() throws DAOException;

	/**
	 * This method will be called to set connection Manager object.
	 * @param connectionManager : Connection Manager.
	 */
	public void setConnectionManager(IConnectionManager connectionManager)
	{
		logger.debug("Setting the connection manager");
		this.connectionManager = connectionManager;
	}

	/**(non-Javadoc).
	 * @see edu.wustl.dao.DAO#getConnectionManager()
	 * @return : It returns the Connection Manager
	 */
	public IConnectionManager getConnectionManager()
	{
		logger.debug("Get the connection manager");
		return connectionManager;
	}


	/**
	 * updates the persisted object into the database.
	 * @param obj Object to be updated in database
	 * @param oldObj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	public void update(Object obj,Object oldObj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}

	/**
	 * @param sourceObjectName :
	 * @param identifier :
	 * @return Object :
	 * @throws DAOException :
	 */
	public Object retrieveById(String sourceObjectName, Long identifier)
			throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}
	/**
	 * @param entityName :
	 * @param identifier :
	 * @return Object :
	 * @throws DAOException :
	 */
	public Object retrieveByIdAndEntityName(String entityName, Long identifier) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}
	/**
	 * @param obj : object to be deleted
	 * @throws DAOException : daoExp
	 */
	public void delete(Object obj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}
	/**
	 * @param entityName :entity Name from hbm.
	 * @param obj : object to be deleted
	 * @throws DAOException : daoExp
	 */
	public void delete(String entityName,Object obj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}

	/**
	 * Insert the Object in the database.
	 * @param obj Object to be inserted in database
	 * @throws DAOException generic DAOException
	 */
	public void insert(Object obj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}
	/**
	 * Insert the Object in the database.
	 * @param entityName :entity Name from hbm.
	 * @param obj Object to be inserted in database
	 * @throws DAOException generic DAOException
	 */
	public void insert(String entityName,Object obj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}

	/**
	 * @see edu.wustl.common.dao.DAO#retrieveAttribute(java.lang.Class, java.lang.Long, java.lang.String)
	 * @param objClass : Class name
	 * @param identifier : Identifier of object
	 * @param attributeName : Attribute Name to be fetched
	 * @param columnName : where clause column field.
	 * @return It will return the Attribute of the object having given identifier
	 * @throws DAOException : DAOException
	 */
	public List retrieveAttribute(Class objClass,String columnName, Long identifier,
			String attributeName) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}

	/**
	 * updates the object into the database.
	 * @param obj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	public void update(Object obj) throws DAOException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("dao.method.without.implementation");
		throw new DAOException(errorKey,new Exception(),"AbstractJDBCDAOImpl.java :");
	}
	
		/**
	 * Merge. This method merges the object passed as parameter with the same object present in
	 * database. If no old object is present in db then new object is inserted in database.
	 * @param objectToBeMerged the object to be merged
	 */
	public Object merge(Object objectToBeMerged) {
		throw new RuntimeException("Unsupported Operation");
	}


}
