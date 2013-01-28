package edu.wustl.dao.newdao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.HibernateMetaData;


public class CleanDAO
{

	/**
  	* LOGGER Logger - class logger.
  	*/
  	private static final Logger logger =
  		Logger.getCommonLogger(CleanDAO.class);

	
	private String applicationName;
	
	private Session session;
	
	private static ThreadLocal<CleanDAO> cleanDAO = new ThreadLocal<CleanDAO>();
	
	private CleanDAO(String applicationName)
	{
		this.applicationName = applicationName;
		//session = SessionFactoryHolder.getInstance().getSessionFactory(applicationName).openSession();//getNewSessionFromConnectionManager();
		session = getNewSessionFromConnectionManager();
	}
	
	private Session getNewSessionFromConnectionManager()
	{
		Session session = null;
		try
		{
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(applicationName);
			IConnectionManager conMgnr = daoFactory.getDAO().getConnectionManager();
			session = conMgnr.getSessionFactory().openSession();
			session.setFlushMode(FlushMode.NEVER);
		}
		catch(DAOException daoExp)
		{
			logger.error("Error inititializing hibernate session: ", daoExp);
			throw new RuntimeException("Error inititializing hibernate session: ", daoExp);
		}
		return session;
	}

	
	public static CleanDAO getInstance(String applicationName)
	{
		if (cleanDAO.get() == null) {
			cleanDAO.set(new CleanDAO(applicationName));
		}
		
		return cleanDAO.get();
	}
	
	/**
	 * Retrieve Object.
	 * @param sourceObjectName source Object Name.
	 * @param identifier identifier.
	 * @return object.
	 * @throws DAOException generic DAOException.
	 */
	public Object retrieveById(String sourceObjectName, Long identifier)
	 throws DAOException
	 {
		logger.debug("Inside retrieve method");
		try
		{
			Object object = session.load(Class.forName(sourceObjectName), identifier);
            return HibernateMetaData.getProxyObjectImpl(object);

			/*Object object = session.get(Class.forName(sourceObjectName), identifier);
			return object;*/
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
			"HibernateDAOImpl.java ");
		}

	}
	
	public void closeSession()
	{
		try {
			session.close();
		} finally {
			session = null;
			cleanDAO.remove();
		}
	}
	
	public List executeQuery(String query,Integer startIndex,Integer maxRecords,List paramValues) throws DAOException
	{
		logger.debug("Execute query");
		try
		{
			
			Query hibernateQuery = session.createQuery(query);
			if(startIndex != null && maxRecords !=null )
			{
				hibernateQuery.setFirstResult(startIndex.intValue());
				hibernateQuery.setMaxResults(maxRecords.intValue());
			}
			setQueryParametes(hibernateQuery, paramValues);
		    return hibernateQuery.list();
		
		}
		catch(HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(),hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java "+query);
		}
	}
	
	private void setQueryParametes(Query query, List<ColumnValueBean> columnValueBeans)
	{
		if (columnValueBeans != null)
		{
			Iterator<ColumnValueBean> colValItr = columnValueBeans.iterator();
			while (colValItr.hasNext())
			{
				ColumnValueBean colValueBean = colValItr.next();
				query.setParameter(colValueBean.getColumnName(), colValueBean.getColumnValue());
			}
		}
	}
}
