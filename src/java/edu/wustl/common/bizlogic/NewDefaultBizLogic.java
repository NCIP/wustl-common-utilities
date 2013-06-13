/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.bizlogic;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatterFactory;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDataValidatorFactory;
import edu.wustl.common.transformer.TransformerUtil;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.validator.IDataValidator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;

public abstract class NewDefaultBizLogic implements IBizLogic
{

	private static transient final Logger LOGGER = Logger.getCommonLogger(NewDefaultBizLogic.class);

	
	/**
	 * @param exception Exception object thrown in a catch block.
	 * @param key error-key in applicationResource file.
	 * @param logMessage message to log inlogger.
	 * @throws BizLogicException
	 * @return BizLogicException
	 */
	public static BizLogicException getBizLogicException(Exception exception, String key,
			String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}

	public String getErrorMessage(ApplicationException exception, Object obj, String operation) // NOPMD
	{
		StringBuffer buff = new StringBuffer();

		if (exception.getWrapException() == null)
		{

			if (exception.getErrorKey() == null && exception.toMsgValuesArray() != null
					&& exception.toMsgValuesArray().length > 0)
			{
				for (String msg : exception.toMsgValuesArray())
				{
					buff.append(msg).append("  ");
				}
			}
			else
			{
				buff.append(exception.getMessage());
			}
		}
		else
		{
			Exception exp = getWrapException(exception);
			if (exp != null)
			{
				buff.append(formatException(exp, obj, operation));
			}
			else
			{
				buff.append(exception.getMessage());
			}
		}
		return buff.toString();
	}

	/**
	 * This method provides wrapped exception till last level.
	 *
	 * @param exception the exception
	 * @return the wrapped exception
	 */
	private Exception getWrapException(Exception exception)
	{
		Throwable rootException = null;
		Throwable wrapException = exception;
		while (true)
		{
			if ((wrapException.getCause() == null))
			{
				rootException = wrapException;
				break;
			}
			else
			{
				wrapException = wrapException.getCause();
				continue;
			}
		}
		return (Exception) rootException;
	}

	/**
	 * This method formats Exception.
	 * @param exception exception
	 * @param obj object
	 * @param operation operation
	 * @return error message.
	 */
	public String formatException(Exception exception, Object obj, String operation) // NOPMD
	{
		String errMsg;
		String tableName = null;
		try
		{
			if (exception == null)
			{
				ErrorKey errorKey = ErrorKey.getErrorKey("biz.formatex.error");
				throw new ApplicationException(errorKey, null, "exception is null.");
			}
			// Get ExceptionFormatter
			ExceptionFormatter exFormatter = ExceptionFormatterFactory.getFormatter(exception);
			// call for Formating Message
			if (exFormatter == null)
			{
				errMsg = exception.getMessage();
			}
			else
			{
				if (obj instanceof LinkedHashSet && !((LinkedHashSet) obj).isEmpty())
				{
					obj = ((LinkedHashSet) obj).iterator().next();
				}

				HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
						.getHibernateMetaData(CommonServiceLocator.getInstance().getAppName());
				if (hibernateMetaData != null)
				{
					tableName = hibernateMetaData.getTableName(obj.getClass());
				}
				else
				{
					tableName = "";
				}
				errMsg = exFormatter.formatMessage(exception);
			}
		}
		catch (ApplicationException except)
		{
			LOGGER.error(except.getMessage(), except);
			// if Error occured while formating message then get message
			// formatted through Default Formatter
			String[] arg = {operation, tableName};
			errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01", arg);
		}
		return errMsg;
	}

	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		return false;
	}

	@Override
	public boolean isAuthorized(DAO paramDAO, Object paramObject,
			SessionDataBean paramSessionDataBean) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");
	}

	//@see edu.wustl.common.bizlogic.IBizLogic#
	// isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	abstract public boolean isAuthorized(Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException;

	protected void postInsert(Object obj, SessionDataBean sessionDataBean) throws BizLogicException
	{

	}

	protected void preInsert(Object obj, SessionDataBean sessionDataBean) throws BizLogicException
	{

	}

	public void insert(Object obj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			if (isAuthorized(obj, sessionDataBean))
			{
				IDataValidatorFactory dataValidatorFactory = AbstractFactoryConfig.getInstance()
						.getDataValidatorFactory();
				IDataValidator dataValidator = dataValidatorFactory.getDataValidator(obj.getClass()
						.getName());
				dataValidator.validate(obj, Constants.ADD);
				preInsert(obj, sessionDataBean);
				insert(obj, sessionDataBean, 0);
				postInsert(obj, sessionDataBean);
			}
		}
		catch (ApplicationException exception)
		{
			String errMssg = getErrorMessage(exception, obj, "Inserting");
			LOGGER.debug(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(), exception, exception
					.getMsgValues(), errMssg);
		}
	}

	protected void postUpdate(Object currentObj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		// TODO Auto-generated method stub

	}

	protected void preUpdate(Object currentObj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		// TODO Auto-generated method stub

	}

	public void update(Object currentObj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			if (isAuthorized(currentObj, sessionDataBean))
			{
				IDataValidatorFactory dataValidatorFactory = AbstractFactoryConfig.getInstance()
						.getDataValidatorFactory();
				String className = getClassNameIfHibernateProxy(currentObj);
				IDataValidator dataValidator = dataValidatorFactory.getDataValidator(className);
				dataValidator.validate(currentObj, Constants.EDIT);
				preUpdate(currentObj, oldObj, sessionDataBean);
				update(currentObj, oldObj, 0, sessionDataBean);
				postUpdate(currentObj, oldObj, sessionDataBean);
			}
		}
		catch (ApplicationException exception)
		{
			String errMsg = getErrorMessage(exception, currentObj, "Updating");
			LOGGER.debug(errMsg, exception);
			throw new BizLogicException(exception.getErrorKey(), exception, exception
					.getMsgValues(), errMsg);
		}
	}
	
	public static String getClassNameIfHibernateProxy(Object object)
	{
		String className = object.getClass().getName();
		if(object instanceof HibernateProxy)
		{
			Class klass = Hibernate.getClass(object);
			className = klass.getName();
		}
		return className;
	}

	abstract protected Object getObject(Long identifier);

	public boolean populateUIBean(String className, Long identifier, IValueObject uiForm)
			throws BizLogicException
	{
		//long startTime = System.currentTimeMillis();
		boolean isSuccess = false;

		Object object = getObject(identifier);
		if (object != null)
		{
			/*
			  If the record searched is present in the database,
			  populate the formbean with the information retrieved.
			 */
			AbstractDomainObject abstractDomain = (AbstractDomainObject) object;

			prePopulateUIBean(abstractDomain, uiForm);
			uiForm.setAllValues(abstractDomain);
			//postPopulateUIBean(abstractDomain, uiForm);
			isSuccess = true;
		}

		return isSuccess;
	}

	/**
	 * This method gets called before populateUIBean method.
	 * Any logic before updating uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException
	{
		//Empty prePopulateUIBean method.
	}

	/**
	 * This method gets called after populateUIBean method.
	 * Any logic after populating  object uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected void postPopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException
	{
		// Empty postPopulateUIBean method.
	}

	public AbstractDomainObject populateDomainObject(String className, Long identifier,
			IValueObject uiForm) throws BizLogicException
	{
		//long startTime = System.currentTimeMillis();
		//DAO dao = null;
		AbstractDomainObject abstractDomain = null;
		try
		{
			//dao = getHibernateDao(CommonServiceLocator.getInstance().getAppName(),null);
			//Object object = dao.retrieveById(className, identifier);
			Object object = getObject(identifier);
			abstractDomain = populateFormBean(uiForm, object);
		}
		catch (Exception daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "biz.popdomain.error",
					"Exception in domain population");
		}
		//		finally
		//		{
		//			closeSession(dao);
		//		}

		//String simpleClassName = Utility.parseClassName(className);
		//long endTime = System.currentTimeMillis();
		//LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB - " + simpleClassName + " : "
		//+ (endTime - startTime));

		return abstractDomain;
	}

	private AbstractDomainObject populateFormBean(IValueObject uiForm, Object object)
			throws AssignDataException
	{
		AbstractDomainObject abstractDomain = null;
		if (object != null)
		{
			/*
			  If the record searched is present in the database,
			  populate the formbean with the information retrieved.
			 */
			abstractDomain = (AbstractDomainObject) object;
			if (abstractDomain != null)
			{
				TransformerUtil.transform(uiForm, abstractDomain);
			}
		}
		return abstractDomain;
	}

	protected Object getCorrespondingOldObject(Collection objectCollection, Long identifier)
	{
		Iterator iterator = objectCollection.iterator();
		AbstractDomainObject abstractDomainObject = null;
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObj = (AbstractDomainObject) iterator.next();
			if ((identifier != null) && (identifier.equals(abstractDomainObj.getId())))
			{
				abstractDomainObject = abstractDomainObj;
				break;
			}
		}
		return abstractDomainObject;
	}

	//	/**
	//	* This method returns hibernate DAO according to the application name.
	//	* @param appName Application name.
	//	* @param sessionDataBean SessionDataBean object.
	//	* @return DAO
	//	* @throws DAOException Generic DAO exception.
	//	*/
	//	protected static DAO getHibernateDao(String appName,SessionDataBean sessionDataBean) throws DAOException
	//	{
	//		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
	//		DAO dao = daofactory.getDAO();
	//		dao.openSession(sessionDataBean);
	//		return dao;
	//	}
	//
	//	/**
	//	* @param dao DAO object
	//	* @throws BizLogicException :Generic BizLogic Exception- session not closed.
	//	*/
	//	protected void closeSession(DAO dao) throws BizLogicException
	//	{
	//		try
	//		{
	//			if(dao != null)
	//			{
	//				dao.closeSession();
	//			}
	//		}
	//		catch (DAOException exception)
	//		{
	//			LOGGER.error("Not able to close DAO session.", exception);
	//			throw new BizLogicException(exception);
	//		}
	//	}

	public void createProtectionElement(Object arg0) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public void delete(Object arg0) throws BizLogicException
	{
		// TODO Auto-generated method stub

	}

	public void delete(Object arg0, int arg1) throws BizLogicException
	{
		// TODO Auto-generated method stub

	}

	public List getList(String arg0, String[] arg1, String arg2, boolean arg3)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List getList(String arg0, String[] arg1, String arg2, QueryWhereClause arg3,
			List<ColumnValueBean> arg4) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List getList(String arg0, String[] arg1, String arg2, String[] arg3, String[] arg4,
			Object[] arg5, String arg6, String arg7) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List getList(String arg0, String[] arg1, String arg2, String[] arg3, String[] arg4,
			Object[] arg5, String arg6, String arg7, boolean arg8) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public String getObjectId(DAO arg0, Object arg1) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public String getReadDeniedPrivilegeName()
	{
		return null;
	}

	public List getRelatedObjects(DAO arg0, Class arg1, String arg2, Long[] arg3)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List getRelatedObjects(DAO arg0, Class arg1, QueryWhereClause arg2,
			List<ColumnValueBean> arg3) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public boolean hasPrivilegeToView(String arg0, Long arg1, SessionDataBean arg2)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public void insert(Object arg0) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public void insert(Object arg0, int arg1) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public boolean isReadDeniedTobeChecked()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public List retrieve(String arg0) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public Object retrieve(String arg0, Long arg1) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List<Object> retrieve(String arg0, ColumnValueBean arg1) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List retrieve(String arg0, String arg1, Object arg2) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List retrieve(String arg0, String[] arg1, QueryWhereClause arg2)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List<Object> retrieve(String arg0, String[] arg1, QueryWhereClause arg2,
			List<ColumnValueBean> arg3) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List retrieve(String arg0, String[] arg1, String[] arg2, Object[] arg3, String arg4)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public List retrieve(String arg0, String[] arg1, String[] arg2, String[] arg3, Object[] arg4,
			String arg5) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public Object retrieveAttribute(String arg0, Long arg1, String arg2) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public Object retrieveAttribute(Class arg0, Long arg1, String arg2) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public Object retrieveAttribute(Class arg0, ColumnValueBean arg1, String arg2)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public Object retrieveAttribute(DAO arg0, Class arg1, ColumnValueBean arg2, String arg3)
			throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public void update(Object arg0) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}

	public void update(Object arg0, int arg1) throws BizLogicException
	{
		ErrorKey errorKey = ErrorKey.getErrorKey("method.without.implementation");
		throw new BizLogicException(errorKey, new Exception(), "ClinportalBizLogic.java :");

	}
}
