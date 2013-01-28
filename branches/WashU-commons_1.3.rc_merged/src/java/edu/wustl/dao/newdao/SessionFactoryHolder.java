
package edu.wustl.dao.newdao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.ApplicationDAOPropertiesParser;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.util.HibernateMetaDataFactory;
/**
 * 
 * @author sachin
 *
 */
public class SessionFactoryHolder
{

//	/**
//	* LOGGER Logger - class logger.
//	*/
//	private static final Logger logger = Logger.getCommonLogger(SessionFactoryHolder.class);
//	
//	private Map<String, SessionFactory> sessionFactoryMap = new HashMap<String, SessionFactory>();
//
//	private static SessionFactoryHolder sessionFactoryHolderInstance = new SessionFactoryHolder();
//	
//	private SessionFactoryHolder()
//	{
//	
//	}
//
//	public static SessionFactoryHolder getInstance()
//	{
//		if(sessionFactoryHolderInstance==null)
//		{
//			sessionFactoryHolderInstance = new SessionFactoryHolder();
//		}
//		return sessionFactoryHolderInstance;
//	}
//	
//	public void load(String daoConfigurationFileName)
//	{
//		try
//		{
//			load(initializeConfigurationMap(daoConfigurationFileName));
//		}
//		catch(Exception exp)
//		{
//			logger.error("Exception encountered in loading configuration. "+exp.getMessage());
//			throw new RuntimeException("Exception encountered in loading configuration file "+daoConfigurationFileName,exp);
//		}
//	}
//	
//	public void load(Map<String, String> hbmCfgsMap)
//	{
//		try
//		{
//			for (Map.Entry<String, String> entry : hbmCfgsMap.entrySet())
//			{
//				createSessionFactory(entry.getKey(), entry.getValue());
//			}
//		}
//		catch(Exception exp)
//		{
//			logger.error("Exception encountered in loading configuration. "+exp.getMessage());
//			throw new RuntimeException("Exception encountered in loading configuration",exp);
//		}
//		
//	}
//	
////	public SessionFactoryHolder(Map<String, String> hbmCfgsMap) throws DAOException
////	{
////		initialize(hbmCfgsMap);
////	}
//
//	public SessionFactory getSessionFactory(String name)
//	{
//		return sessionFactoryMap.get(name);
//	}
//
//
//	private void createSessionFactory(String applicationName, String hbmCfg) throws Exception
//	{
//		Configuration configuration = createConfiguration(hbmCfg);
//		SessionFactory sessionFactory = configuration.buildSessionFactory();
//		HibernateMetaDataFactory.setHibernateMetaData(applicationName,
//				configuration);
//		sessionFactoryMap.put(applicationName, sessionFactory);
//	}
//	
//	private Configuration createConfiguration(String configurationfile) throws Exception
//	{
//
//		Configuration configuration = new Configuration();
//	    configuration.configure(configurationfile);
//	    return configuration;
//  
//	}
//	
//	private Map<String, String> initializeConfigurationMap(String daoConfigurationFileName) throws Exception
//	{
//		Map<String, String> configurationMap = new HashMap<String, String>();
//		ApplicationDAOPropertiesParser applicationPropertiesParser =
//			new ApplicationDAOPropertiesParser(daoConfigurationFileName);
//		Map<String, IDAOFactory> daoFactoryMap = applicationPropertiesParser.getDaoFactoryMap();
//		
//		Iterator<String> mapKeySetIterator = daoFactoryMap.keySet().iterator();
//		while(mapKeySetIterator.hasNext())
//		{
//			String appName = mapKeySetIterator.next();
//			IDAOFactory daoFactory = (IDAOFactory)daoFactoryMap.get(appName);
//			configurationMap.put(appName, daoFactory.getConfigurationFile());
//		}
//		return configurationMap;
//	}
}
