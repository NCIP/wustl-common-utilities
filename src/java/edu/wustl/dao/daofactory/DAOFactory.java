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
 * <p>Title: DAOFactory Class>
 * <p>Description:	DAOFactory is a factory class pluggable for different applications used
 * to instantiate different DAO type objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 */

package edu.wustl.dao.daofactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.DOMWriter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.util.XMLHelper;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.DatabaseProperties;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;
import java.sql.Connection;


/**
 * @author kalpana_thakur
 */

public class DAOFactory implements IDAOFactory
{
	/**
	 * This member will store the default Connection Manager name.
	 */
	private String defaultConnMangrName;

	/**
	 * This member will store the JDBC Connection Manager name.
	 */
	private String jdbcConnMangrName;

	/**
	 * This member will store data source for JDBC connection.
	 */
	private String dataSource;

	/**
	 * This member will store the Default DAO class name.
	 * Mostly HibernateDAOImpl will be considered as default DAO
	 */
	private String defaultDAOClassName;
	/**
	 * This member will store the JDBC DAO class name.
	 */
	private String jdbcDAOClassName;
	/**
	 * This member will store the name of the application.
	 */
	private String applicationName;
	/**
	 * This member will store the configuration file name.
	 */
	private String configurationFile;
	/**
	 * This member will store the EntityResolver.
	 */
	private static final EntityResolver entityResolver =
		XMLHelper.DEFAULT_DTD_RESOLVER;

	/**
	 * This member will store the configuration instance.
	 */
	private Configuration configuration;

	/**
	 * This member will store the sessionFactory instance.
	 */
	private SessionFactory sessionFactory;

	/**
	 * This will store the default setting for DAO factory(true / false).
	 */
	private Boolean isDefaultDAOFactory;

	/**
	 * Database properties.
	 */
	private DatabaseProperties databaseProperties;

	/**
	 * hibernateMetaData Hibernate metadata specific to the application.
	 */
	private HibernateMetaData hibernateMetaData;

	/**
	 * @return the hibernateMetaData
	 */
	public HibernateMetaData getHibernateMetaData()
	{
		return hibernateMetaData;
	}

	/**
	 * @param hibernateMetaData the hibernateMetaData to set
	 */
	public void setHibernateMetaData(HibernateMetaData hibernateMetaData)
	{
		this.hibernateMetaData = hibernateMetaData;
	}

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(DAOFactory.class);


	/**
	 * This method will be called to retrieved default DAO instance.
	 * It will read the concrete class for DAO and instantiate it
	 * and also sets the Connection manager object to it.
	 * @return return the DAO instance.
	 * @throws DAOException :Generic DAOException.
	 */
	public DAO getDAO()throws DAOException
	{
		try
		{
		   DAO dao = (DAO)Class.forName(defaultDAOClassName).newInstance();
		   IConnectionManager connManager = getDefaultConnManager();
		   dao.setConnectionManager(connManager);
		   ((HibernateDAO)dao).setHibernateMetaData(HibernateMetaDataFactory.
				   getHibernateMetaData(applicationName));
		   return dao;
		}
		catch (Exception excp )
		{
			logger.error(excp.getMessage(), excp);
			throw DAOUtility.getInstance().getDAOException
			(excp, "db.dao.init.error", "DAOFactory.java ");
		}


	}

	/**
	 * This method will be called to retrieved the JDBC DAO instance.
	 * It will read the concrete class for DAO and instantiate it
	 * and also sets the Connection manager object to it.
	 * @return the JDBCDAO instance
	 * @throws DAOException :Generic DAOException.
	 */
	public JDBCDAO getJDBCDAO()throws DAOException
	{

		try
		{
			   JDBCDAO jdbcDAO = (JDBCDAO) Class.forName(jdbcDAOClassName).newInstance();
			   IConnectionManager connManager = getJDBCConnManager();
			   jdbcDAO.setConnectionManager(connManager);
			   jdbcDAO.setDatabaseProperties(databaseProperties);
			   jdbcDAO.setBatchSize(databaseProperties.getDefaultBatchSize());
			   return jdbcDAO;
		}
		catch (Exception excp )
		{
			logger.error(excp.getMessage(), excp);
			throw DAOUtility.getInstance().getDAOException
			(excp, "db.jdbcdao.init.error", "DAOFactory.java ");
		}

	}


	/**
	 *This method will be called to build the session factory.
	 *It reads the configuration file ,build the sessionFactory and configuration object
	 *and set the connection manager.
	 *@throws DAOException :Generic DAOException.
	 */
	public void buildSessionFactory() throws DAOException
	{
		try
		{
			configuration = setConfiguration(configurationFile);
			sessionFactory = configuration.buildSessionFactory();
			HibernateMetaDataFactory.setHibernateMetaData(applicationName,
					configuration);
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			throw DAOUtility.getInstance().getDAOException
			(exp, "db.sessionfactory.init.error", "DAOFactory.java ");
		}
		catch(Error err)
		{
			logger.error(err.getMessage(), err);
			throw DAOUtility.getInstance().getDAOException
			(null, "db.sessionfactory.init.error", "DAOFactory.java ");
		}
	}

	/**
	 * @return IConnectionManager : connection manager.
	 * @throws ClassNotFoundException  ClassNotFoundException
	 * @throws IllegalAccessException  IllegalAccessException
	 * @throws InstantiationException  InstantiationException
	 */
	private IConnectionManager getDefaultConnManager() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		IConnectionManager connectionManager =
			(IConnectionManager)Class.forName(defaultConnMangrName).newInstance();
		connectionManager.setApplicationName(applicationName);
		connectionManager.setSessionFactory(sessionFactory);
		connectionManager.setConfiguration(configuration);

		return connectionManager;
	}



	/**
	 * @return IConnectionManager : connection manager.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws InstantiationException InstantiationException
	 */
	private IConnectionManager getJDBCConnManager() throws
			InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		IConnectionManager connectionManager =
			(IConnectionManager)Class.forName(jdbcConnMangrName).newInstance();
		connectionManager.setApplicationName(applicationName);
		connectionManager.setDataSource(databaseProperties.getDataSource());
		connectionManager.setSessionFactory(sessionFactory);
		connectionManager.setConfiguration(configuration);
		return connectionManager;
	}

	 /**
     * This method adds configuration file to Hibernate Configuration.
     * It will parse the configuration file and creates the configuration.
     * @param configurationfile name of the file that needs to be added
     * @return Configuration :Configuration object.
	 * @throws DAOException :Generic DAOException.
     */
    private Configuration setConfiguration(String configurationfile) throws DAOException
    {
        try
        {

        	Configuration configuration = new Configuration();
            //InputStream inputStream = DAOFactory.class.getClassLoader().getResourceAsStream(configurationfile);
        	InputStream inputStream = Thread.currentThread().getContextClassLoader().
        	getResourceAsStream(configurationfile);
            List<Object> errors = new ArrayList<Object>();
            // hibernate api to read configuration file and convert it to
            // Document(dom4j) object.
            XMLHelper xmlHelper = new XMLHelper();
            Document document = xmlHelper.createSAXReader(configurationfile, errors, entityResolver).read(
                    new InputSource(inputStream));
            // convert to w3c Document object.
            DOMWriter writer = new DOMWriter();
            org.w3c.dom.Document doc = writer.write(document);
            // configure
            configuration.configure(doc);
            return configuration;
        }
        catch (Exception exp)
        {
        	logger.error(exp.getMessage(), exp);
        	throw DAOUtility.getInstance().getDAOException
			(exp, "db.conf.file.parse.error", "DAOFactory.java ");
        }

    }


	/**
	 * This method will be called to set Connection Manager name.
	 * @param connectionManagerName : Connection Manager.
	 */
	public void setDefaultConnMangrName(String connectionManagerName)
	{
		this.defaultConnMangrName = connectionManagerName;
	}

	/**
	 * This method will be called to set defaultDAOClassName.
	 * @param defaultDAOClassName : defaultDAOClassName.
	 */
	public void setDefaultDAOClassName(String defaultDAOClassName)
	{
		this.defaultDAOClassName = defaultDAOClassName;
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
	 * This method will be called to set jdbcDAOClassName.
	 * @param jdbcDAOClassName : jdbcDAOClassName.
	 */
	public void setJdbcDAOClassName(String jdbcDAOClassName)
	{
		this.jdbcDAOClassName = jdbcDAOClassName;
	}


	/**
	 * This method will be called to retrieved the connectionManagerName.
	 * @return connectionManagerName.
	 */
	public String getDefaultConnMangrName()
	{
		return defaultConnMangrName;
	}

	/**
	 * This method will be called to retrieved the defaultDAOClassName.
	 * @return defaultDAOClassName.
	 */
	public String getDefaultDAOClassName()
	{
		return defaultDAOClassName;
	}

	/**
	 * This method will be called to retrieved the jdbcDAOClassName.
	 * @return jdbcDAOClassName.
	 */
	public String getJdbcDAOClassName()
	{
		return jdbcDAOClassName;
	}

	/**
	 * This method will be called to retrieved the configurationFile name.
	 * @return configurationFile.
	 */
	public String getConfigurationFile()
	{
		return configurationFile;
	}

	/**
	 *This method will be called to set the configuration file name.
	 *@param configurationFile : Name of configuration file.
	 */
	 public void setConfigurationFile(String configurationFile)
	{
		this.configurationFile = configurationFile;
	}



	/**
	 * @return This will return true if DAO factory is default.
	 */
	public Boolean getIsDefaultDAOFactory()
	{

		return isDefaultDAOFactory;
	}

	/**
	 * This will be set to true if DAO factory is default.
	 * @param isDefaultDAOFactory :
	 */
	public void setIsDefaultDAOFactory(Boolean isDefaultDAOFactory)
	{
		this.isDefaultDAOFactory = isDefaultDAOFactory;
	}

	/**
	 * This method will be called to get the JDBC connection manager name.
	 * @return jdbcConnMangrName
	 */
	public String getJdbcConnMangrName()
	{
		return jdbcConnMangrName;
	}

	/**
	 * This method will be called to set connection manager name.
	 * @param jdbcConnMangrName : JDBC connection manager name.
	 */
	public void setJdbcConnMangrName(String jdbcConnMangrName)
	{
		this.jdbcConnMangrName = jdbcConnMangrName;
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
	  * This method will be called to set all database properties.
	  * @param databaseProperties :database properties.
	  */
	public void setDatabaseProperties(DatabaseProperties databaseProperties)
	{
		this.databaseProperties = databaseProperties;
	}

	/**
	 * This method will be called to get the database name.
	 * @return database name.
	 */
	public String getDataBaseType()
	{
		return databaseProperties.getDataBaseType();
	}
	/**
	 * Client's responsible for closing the connection...
	 * 
	 * @return a new connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws DAOException
	{
		try
		{
			return getJDBCConnManager().getConnection();
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage(), excp);
			throw DAOUtility.getInstance().getDAOException(excp, "FOOO...", "DAOFactory.java ");
		}
	}

	/**
	 * Client's responsible for closing the connection...
	 * 
	 * @return a new connection
	 * @throws DAOException
	 */
	public void closeConnection(Connection connObj) throws DAOException
	{
		try
		{
			connObj.close();
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage(), excp);
			throw DAOUtility.getInstance().getDAOException(excp, "FOOO...", "DAOFactory.java ");
		}
	}
	
	

}
