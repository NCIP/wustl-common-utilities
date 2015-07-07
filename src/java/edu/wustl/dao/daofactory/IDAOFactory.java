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
 * <p>Title: IDAOFactory Interface>
 * <p>Description:	IDAOFactory is a factory class pluggable for different applications used
 * to instantiate different DAO type objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */

package edu.wustl.dao.daofactory;

import java.sql.Connection;

import edu.wustl.dao.DAO;
import edu.wustl.dao.DatabaseProperties;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;



/**
 * @author kalpana_thakur
 */
public interface IDAOFactory
{

	/**
	 *This method will be called to build the session factory.
	 *It reads the configuration file ,build the sessionFactory and configuration object
	 *and set the connection manager.
	 * @throws DAOException  generic exception
	 */
	void buildSessionFactory ()throws DAOException;

	/**
	 * This method will be called to set default DAO class name.
	 * @param defaultDAOClassName default DAO class name
	 */
	void setDefaultDAOClassName(String defaultDAOClassName);

	/**
	 * This method will be called to get default DAO class name.
	 * @return Default DAO class name.
	 */
	String getDefaultDAOClassName();

	/**
	 * This method will be called to set the JDBC DAO class name.
	 * @param jdbcDAOClassName : JDBC DAO class name.
	 */
	void setJdbcDAOClassName(String jdbcDAOClassName);

	/**
	 * This method will be called to get the JDBC DAO class name.
	 * @return jdbcDAOClassName
	 */
	String getJdbcDAOClassName();

	/**
	 * This method will be called to set the Connection Manager name.
	 * @param connectionManagerName : Name of connection manager.
	 */
	void setDefaultConnMangrName(String connectionManagerName);
	/**
	 * This method will be called to get the Connection Manager name.
	 * @return Connection Manager name.
	 */
	String getDefaultConnMangrName();

	/**
	 * This method will be called to set the application name.
	 * @param applicationName : Application Name.
	 */
	void setApplicationName(String applicationName);
	/**
	 * This method will be called to get the applicationName name.
	 * @return Application name.
	 */
	String getApplicationName();

	/**
	 * This method will be called to set the configuration file name.
	 * @param configurationFile : name of configuration file.
	 */
	void setConfigurationFile(String configurationFile);

	/**
	 * This method will be called to set the configuration file name.
	 * @return configurationFile
	 */
	String getConfigurationFile();

	/**
	 * This method will be called to retrieved default DAO instance.
	 * It will read the concrete class for DAO and instantiate it
	 * and also sets the Connection manager object to it.
	 * @return return the DAO instance.
	 * @throws DAOException :Generic DAOException.
	 */
	DAO getDAO()throws DAOException;

	/**
	 * This method will be called to retrieved the JDBC DAO instance.
	 * It will read the concrete class for DAO and instantiate it
	 * and also sets the Connection manager object to it.
	 * @return the JDBCDAO instance
	 * @throws DAOException :Generic DAOException.
	 */
	JDBCDAO getJDBCDAO()throws DAOException;


	/**
	 * DAOFactory will be default if default attribute in ApplicationDAOProperties.xml
	 * is set to true.
	 * @return true if DAO factory is default DAO factory.
	 */
	Boolean getIsDefaultDAOFactory();

	/**
	 * DAOFactory will be default if default attribute in ApplicationDAOProperties.xml
	 * is set to true.
	 * @param isDefaultDAOFactory this will be set to true if the DAOFactory has to be
	 * used as default DAO factory.
	 */
	void setIsDefaultDAOFactory(Boolean isDefaultDAOFactory);


	/**
	 * This will called to retrieve connectionManager object.
	 * @return connectionManager
	 */
	//IConnectionManager getConnectionManager();

	/**
	 * This method will be called to get the JDBC connection manager name.
	 * @return jdbcConnMangrName
	 */
	 String getJdbcConnMangrName();

	/**
	 * This method will be called to set connection manager name.
	 * @param jdbcConnMangrName : JDBC connection manager name.
	 */
	 void setJdbcConnMangrName(String jdbcConnMangrName);

	 /**
	  * This method will be called to set all database properties.
	  * @param databaseProperties :database properties.
	  */
	 void setDatabaseProperties(DatabaseProperties databaseProperties);
	 /**
	 * This method will be called to get the database name.
	 * @return database name.
	 */
	String getDataBaseType();

	/**
	 * @return the hibernateMetaData
	 */
	HibernateMetaData getHibernateMetaData();
	
	/**
	 * Gets the connectiontion.
	 * 
	 * @return the connectiontion
	 * @throws DAOException the dAO exception
	 */
	Connection getConnection() throws DAOException;

	/**
	 * Close connection.
	 * 
	 * @param connObj the conn obj
	 * @throws DAOException the dAO exception
	 */
	void closeConnection(Connection connObj) throws DAOException;

}
