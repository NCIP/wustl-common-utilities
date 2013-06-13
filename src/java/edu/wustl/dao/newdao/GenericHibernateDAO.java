/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao.newdao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AuditEvent;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.AuditException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;

public class GenericHibernateDAO<T, ID extends Serializable> implements DAO<T, ID>
{

	/**
	* LOGGER Logger - class logger.
	*/
	private static final Logger logger = Logger.getCommonLogger(GenericHibernateDAO.class);
	
	private static final String ACTIVITY_STATUS_HQL = "select activityStatus from %s where id =:id";
	
	protected String applicationName;

	protected SessionDataBean sessionDataBean;

	protected Class<T> persistentClass;

	/**
	* AuditManager for auditing.
	*/
	private AuditManager auditManager;

	public GenericHibernateDAO(String applicationName, SessionDataBean sessionDataBean)
	{
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.applicationName = applicationName;
		this.sessionDataBean = sessionDataBean;
		auditManager = new AuditManager(sessionDataBean,applicationName);
	}

	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	protected Session getSession()
	{
		//return SessionFactoryHolder.getInstance().getSessionFactory(applicationName).getCurrentSession();//getSessionFromConnectionManager();
		return getSessionFromConnectionManager();
	}
	
	private Session getSessionFromConnectionManager()
	{
		Session session = null;
		try
		{
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(applicationName);
			IConnectionManager conMgnr = daoFactory.getDAO().getConnectionManager();
			session = conMgnr.getSession();
		}
		catch(DAOException daoExp)
		{
			logger.error("Error inititializing hibernate session: ", daoExp);
			throw new RuntimeException("Error inititializing hibernate session: ", daoExp);
		}
		return session;
	}

	private JDBCDAO getJDBCDAO()
	{
		JDBCDAO jdbcDao = null;
		try
		{
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(applicationName);
			jdbcDao = daoFactory.getJDBCDAO();
			jdbcDao.openSession(sessionDataBean);
		}
		catch(DAOException daoExp)
		{
			logger.error("Error inititializing hibernate session: ", daoExp);
			throw new RuntimeException("Error inititializing hibernate session: ", daoExp);
		}
		return jdbcDao;
	}

	/**
	 * Insert the Object to the database.
	 * @param obj Object to be inserted in database
	 * @throws DAOException generic DAOException
	 */
	public void insert(T obj) throws DAOException
	{
		try
		{
			Session session = getSession();
			session.save(obj);
			auditManager.audit(obj, null, "INSERT");
			insertAudit();
		}
		catch (AuditException exp)
		{
			logger.warn("Error encountered during audit of insert event "+exp.getMessage());
		}
		catch (Exception hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.insert.data.error",
					"GenericHibernateDAO.java ");
		}
		
	}

	/**
	 * updates the object into the database.
	 * @param currentObj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	public T update(T currentObj) throws DAOException
	{
		logger.debug("Update Object");
		try
		{
			Long objectId = auditManager.getObjectId(currentObj);
			getSession().evict(currentObj);
			T previousObj = findById((ID)objectId);
			currentObj =update(currentObj, previousObj);
		}
		catch (AuditException exp)
		{
			logger.warn("Error encountered during audit of update event"+exp.getMessage());
		}
		catch (Exception hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.update.data.error",
					"GenericHibernateDAO.java ");
		}
		return currentObj ;
	}


	/**
	 * This method will be called when user need to audit and update the changes.
	 * @param currentObj object with new changes
	 * * @param previousObj persistent object fetched from database.
	 * @throws DAOException : generic DAOException
	 */
	public T update(T currentObj, T previousObj) throws DAOException
	{
		logger.debug("Update Object");
		try
		{
			auditManager.audit(currentObj, previousObj, "UPDATE");
			insertAudit();
			currentObj = (T)getSession().merge(currentObj);
		}
		catch (AuditException exp)
		{
			logger.warn(exp.getMessage());
		}
		catch (Exception hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.update.data.error",
					"GenericHibernateDAO.java ");
		}
		return currentObj;

	}

	/**
	 * Deletes the persistent object from the database.
	 * @param obj The object to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	public void delete(T obj) throws DAOException
	{
		logger.debug("Delete Object");
		try
		{
			getSession().delete(obj);
		}
		catch (Exception hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.delete.data.error",
					"GenericHibernateDAO.java ");

		}
	}

	public T findById(ID id)
	{
		T entity = (T) getSession().get(getPersistentClass(), id);
		return entity;
	}
	
	public List<T> findAll()
	{
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion)
	{
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion)
		{
			crit.add(c);
		}
		return crit.list();
	}
	
	
	public List executeQuery(String query, Integer startIndex, Integer maxRecords,
			List<ColumnValueBean> columnValueBeans) throws DAOException
	{
		logger.debug("Execute query");
		try
		{
			Session session = getSession();
			Query hibernateQuery = session.createQuery(query);
			if (startIndex != null && maxRecords != null)
			{
				hibernateQuery.setFirstResult(startIndex.intValue());
				hibernateQuery.setMaxResults(maxRecords.intValue());
			}
			setQueryParameters(hibernateQuery, columnValueBeans);
			return hibernateQuery.list();

		}
		catch (Exception hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"GenericHibernateDAO.java " + query);
		}
	}
	
	public List executeQuery(String query, Integer startIndex, Integer maxRecords,
			ColumnValueBean columnValueBean) throws DAOException
	{
		List<ColumnValueBean> valueList = new ArrayList<ColumnValueBean>();
		valueList.add(columnValueBean);
		return executeQuery(query, startIndex, maxRecords, valueList);
	}

	/**
	 * This method returns named query.
	 * @param queryName : handle for named query.
	 * @param namedQueryParams : Map holding the parameter type and parameter value.
	 * @return the list of data.
	 * @throws DAOException : database exception.
	 */
	public List executeNamedQuery(String queryName, Integer startIndex, Integer maxRecords, List<ColumnValueBean> columnValueBeans)
			throws DAOException
	{
		logger.debug("Execute named query");
		try
		{
			Query query = getSession().getNamedQuery(queryName);
			setQueryParameters(query, columnValueBeans);
			return query.list();
		}
		catch (Exception hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"GenericHibernateDAO.java " + queryName);
		}
	}
	
	/**
	 * This method returns named query.
	 * @param queryName : handle for named query.
	 * @param namedQueryParams : Map holding the parameter type and parameter value.
	 * @return the list of data.
	 * @throws DAOException : database exception.
	 */
	public List executeNamedQuery(String queryName, Integer startIndex, Integer maxRecords, ColumnValueBean columnValueBean)
			throws DAOException
	{
		List<ColumnValueBean> valueList = new ArrayList<ColumnValueBean>();
		valueList.add(columnValueBean);
		return executeNamedQuery(queryName, startIndex, maxRecords, valueList);
	}
	

	public List executeSQLQuery(String sql,Integer startIndex, Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException
	{
		logger.debug("Execute executeSQLUpdate");
		try
		{
			SQLQuery sqlquery = getSession().createSQLQuery(sql);
			if (startIndex != null && maxRecords != null)
			{
				sqlquery.setFirstResult(startIndex.intValue());
				sqlquery.setMaxResults(maxRecords.intValue());
			}
			setQueryParameters(sqlquery, columnValueBeans);
			return sqlquery.list();
		}
		catch (Exception hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"GenericHibernateDAO.java ");
		}
	}
	
	public void executeSQLUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans) throws DAOException
	{
		logger.debug("Execute executeSQLUpdate");
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = getJDBCDAO();
			jdbcDao.executeUpdate(sql, columnValueBeans);
		}
		catch (Exception hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"GenericHibernateDAO.java ");
		}
		finally
		{
			jdbcDao.closeSession();
		}
	}
	private void setQueryParameters(Query query, List<ColumnValueBean> columnValueBeans)
	{
		if (columnValueBeans != null)
		{
			Iterator<ColumnValueBean> colValItr = columnValueBeans.iterator();
			while (colValItr.hasNext())
			{
				ColumnValueBean colValueBean = colValItr.next();
				setParameter(query,colValueBean);
			}
		}	
	}

	private void setParameter(Query query,ColumnValueBean colValueBean)
	{
		if(colValueBean.getColumnValue() instanceof Collection)
		{
			query.setParameterList(colValueBean.getColumnName(), (Collection)colValueBean.getColumnValue());
		}
		else if(colValueBean.getColumnValue() instanceof Object[])
		{
			query.setParameterList(colValueBean.getColumnName(), (Object[])colValueBean.getColumnValue());
		}
		else
		{
			query.setParameter(colValueBean.getColumnName(), colValueBean.getColumnValue());
		}	
	}

	/**
	 * This method inserts audit Event details in database.
	 * @throws DAOException generic DAOException.
	 */
	private void insertAudit() throws DAOException
	{
		if (auditManager.getAuditEvent() != null
				&& !auditManager.getAuditEvent().getAuditEventLogCollection().isEmpty())
		{
			getSession().save(auditManager.getAuditEvent());
		}
		auditManager.setAuditEvent(new AuditEvent());
		auditManager.initializeAuditManager(sessionDataBean);
	}
	
	public void setSessionDataBean(SessionDataBean sessionDataBean)
	{
		this.sessionDataBean = sessionDataBean;
	}
	
	public String getActivityStatus(Long id) throws DAOException
	{
		String hql = String.format(ACTIVITY_STATUS_HQL, getPersistentClass().getName());
		List<ColumnValueBean> valueList = new ArrayList<ColumnValueBean>();
		valueList.add(new ColumnValueBean("id", id));
		List<String> list = executeQuery(hql, null, null, valueList);
		String activityStatus = "";
		if (!list.isEmpty())
		{
			activityStatus = list.get(0);
		}
		return activityStatus;
	}
}
