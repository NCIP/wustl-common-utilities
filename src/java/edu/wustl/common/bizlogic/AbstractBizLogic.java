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
 * <p>Title: AbstractBizLogic Class>
 * <p>Description:	AbstractBizLogic is the base class of all the Biz Logic classes.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */

package edu.wustl.common.bizlogic;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

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
import edu.wustl.common.transformer.TransformerUtil;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HashedDataHandler;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;

/**
 * AbstractBizLogic is the base class of all the Biz Logic classes.
 * @author gautam_shetty
 */
public abstract class AbstractBizLogic implements IBizLogic // NOPMD
{
	/**
	 * Application name to get DAO.
	 */
	private String appName;
	/**
	 * Constructor with argument as application name.
	 * This application is used to get DAO.
	 * @param appName Application name.
	 */
	public AbstractBizLogic(String appName)
	{
		super();
		this.appName=appName;
	}
	/**
	 * constructor initialized with default application name.
	 */
	public AbstractBizLogic()
	{
		super();
		String appName = CommonServiceLocator.getInstance().getAppName();
		this.appName=appName;
	}
	/**
	 * This method returns application name.
	 * @return appName
	 */
	public String getAppName()
	{
		return this.appName;
	}
	/**
	 * This method returns application name.
	 * @param appName application Name.
	 */
	public void setAppName(String appName)
	{
		this.appName=appName;
	}
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(AbstractBizLogic.class);
	/**
	 * This method gets called before insert method.
	 * Any logic before inserting into database can be included here.
	 * @param obj The object to be inserted.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected abstract void preInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException;
	/**
	 * Inserts an object into the database.
	 * @param obj The object to be inserted.
	 * @param dao the dao object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException;
	/**
	 * Inserts an object into the database.
	 * @param obj The object to be inserted.
	 * @param dao the dao object.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract void insert(Object obj, DAO dao) throws BizLogicException;
	/**
	 * This method gets called after insert method.
	 * Any logic after inserting object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected abstract void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException;
	/**
	 * This method gets called after insert method.
	 * Any logic after inserting object in database can be included here.
	 * @param objCollection Collection of object to be inserted
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected abstract void postInsert(Collection<AbstractDomainObject> objCollection, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException;
	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param dao the dao object
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract void delete(Object obj, DAO dao) throws BizLogicException;
	/**
	 * This method gets called before update method.
	 * Any logic before updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException BizLogic Exception
	 * */
	protected abstract void preUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException;
	/**
	 * Updates an objects into the database.
	 * @param dao the dao object.
	 * @param obj The object to be updated into the database.
	 * @param oldObj The old object.
	 * @param sessionDataBean  session specific Data
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract void update(DAO dao, Object obj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException;
	/**
	 * This method gets called after update method.
	 * Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException BizLogic Exception
	 */
	protected abstract void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException;
	/**
	 * Updates an objects into the database.
	 * @param dao the dao object
	 * @param obj The object to be updated.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract void update(DAO dao, Object obj) throws BizLogicException;
	/**
	 * Validates the domain object for enumerated values.
	 * @param obj The domain object to be validated.
	 * @param dao The DAO object
	 * @param operation The operation(Add/Edit) that is to be performed.
	 * @return True if all the enumerated value attributes contain valid values
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected abstract boolean validate(Object obj, DAO dao, String operation)
			throws BizLogicException;
	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param daoType dao Type
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,
	 * please use method, delete(Object obj)
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public void delete(Object obj, int daoType) throws BizLogicException
	{
		delete(obj);
	}
	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @throws BizLogicException BizLogic Exception
	 **/
	public void delete(Object obj) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			dao = getHibernateDao(getAppName(),null);
			delete(obj, dao);
			dao.commit();
		}
		catch (ApplicationException exception)
		{
			rollback(dao);
			String errMsg = getErrorMessage(exception,obj,"Deleting");
			//exception.getErrorKey().setErrorMessage(errMsg);
			LOGGER.debug(errMsg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMsg);
		}
		finally
		{
			closeSession(dao);
		}
	}
	/**
	 * This method inserts object. If insert only is true then insert of Defaultbiz logic is called.
	 * @param obj The object to be inserted
	 * @param sessionDataBean  session specific data
	 * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
	 * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
	 */
	private void insert(Object obj, SessionDataBean sessionDataBean, boolean isInsertOnly)
			throws BizLogicException
	{
		DAO dao = null;
		try
		{
			dao = getHibernateDao(getAppName(),sessionDataBean);
			// Authorization to ADD object checked here
			if (isAuthorized(dao, obj, sessionDataBean))
			{
				validate(obj, dao, Constants.ADD);
				preInsert(obj, dao, sessionDataBean);
				insert(obj, sessionDataBean, isInsertOnly, dao);
				dao.commit();
				postInsert(obj, dao, sessionDataBean);
			}
		}
		catch (ApplicationException exception)
		{
			rollback(dao);
			String errMssg = getErrorMessage(exception,obj,"Inserting");
			LOGGER.debug(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMssg);
		}
		finally
		{
			closeSession(dao);
		}
	}
	/**
	 * This method insert collection of objects.
	 * @param objCollection Collection of objects to be inserted
	 * @param sessionDataBean  session specific data
	 * @param daoType Type of dao (Hibernate or JDBC)
	 * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
	 * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * insert(Collection objCollection,SessionDataBean sessionDataBean,boolean isInsertOnly)
	 */
	public final void insert(Collection<AbstractDomainObject> objCollection,
			SessionDataBean sessionDataBean, int daoType, boolean isInsertOnly)
			throws BizLogicException
	{
		insert(objCollection, sessionDataBean, isInsertOnly);
	}
	/**
	 * This method insert collection of objects.
	 * @param objCollection Collection of objects to be inserted
	 * @param sessionDataBean  session specific data
	 * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
	 * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
	 */
	public final void insert(Collection<AbstractDomainObject> objCollection,
			SessionDataBean sessionDataBean, boolean isInsertOnly) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			dao = getHibernateDao(getAppName(),sessionDataBean);
			preInsert(objCollection, dao, sessionDataBean);
			insertMultiple(objCollection, dao, sessionDataBean);
			dao.commit();
			postInsert(objCollection, dao, sessionDataBean);
		}
		catch (ApplicationException exception)
		{
			rollback(dao);
			String errMsg = getErrorMessage(exception,objCollection,"Inserting");
			LOGGER.debug(errMsg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMsg);
		}
		finally
		{
			closeSession(dao);
		}
	}
	/**
	 * This method insert collection of objects.
	 * @param objCollection Collection of objects to be inserted
	 * @param sessionDataBean  session specific data
	 * @param dao object
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public final void insertMultiple(Collection<AbstractDomainObject> objCollection, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		//  Authorization to ADD multiple objects (e.g. Aliquots) checked here
		for (AbstractDomainObject obj : objCollection)
		{
			if (isAuthorized(dao, obj, sessionDataBean))
			{
				validate(obj, dao, Constants.ADD);
			}
			else
			{
				ErrorKey errorKey = ErrorKey.getErrorKey("access.execute.action.denied");
				throw new BizLogicException(errorKey, null,	"");
			}
		}
		for (AbstractDomainObject obj : objCollection)
		{
			insert(obj, sessionDataBean, false, dao);
		}
	}
	/**
	 * @param obj object to be insert and validate
	 * @param sessionDataBean session specific Data
	 * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
	 * @param dao The dao object
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	private void insert(Object obj, SessionDataBean sessionDataBean, boolean isInsertOnly, DAO dao)
			throws BizLogicException
	{
		if (isInsertOnly)
		{
			insert(obj, dao);
		}
		else
		{
			insert(obj, dao, sessionDataBean);
		}
	}
	/**
	 * This method insert object.
	 * @param obj object to be insert
	 * @param sessionDataBean session specific Data
	 * @param daoType dao Type
	 * @exception BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * insert(Object obj, SessionDataBean sessionDataBean)
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public final void insert(Object obj, SessionDataBean sessionDataBean, int daoType)
			throws BizLogicException
	{
		insert(obj, sessionDataBean, false);
	}
	/**
	 * This method insert object.
	 * @param obj object to be insert
	 * @param sessionDataBean session specific Data
	 * @exception BizLogicException BizLogic Exception
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public final void insert(Object obj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		insert(obj, sessionDataBean, false);
	}
	/**
	 * This method insert object.
	 * @param obj object to be insert
	 * @param daoType dao Type
	 * @throws BizLogicException Generic BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * insert(Object obj) throws BizLogicException,UserNotAuthorizedException
	 */
	public final void insert(Object obj, int daoType) throws BizLogicException
	{
		insert(obj, null, true);
	}
	/**
	 * This method insert object.
	 * @param obj object to be insert
	 * @throws BizLogicException BizLogic Exception
	 */
	public final void insert(Object obj) throws BizLogicException
	{
		insert(obj, null, true);
	}
	/**
	 * Updates an object into the database.
	 * @param currentObj The object to be updated.
	 * @param oldObj old Object
	 * @param sessionDataBean session specific Data
	 * @param isUpdateOnly isUpdateOnly
	 * @throws BizLogicException BizLogic Exception
	 */
	private void update(Object currentObj, Object oldObj, SessionDataBean sessionDataBean,
			boolean isUpdateOnly) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			dao = getHibernateDao(getAppName(),sessionDataBean);
			// Authorization to UPDATE object checked here
			if (isAuthorized(dao, currentObj, sessionDataBean))
			{
				validate(currentObj, dao, Constants.EDIT);
				preUpdate(dao, currentObj, oldObj, sessionDataBean);
				if (isUpdateOnly)
				{
					update(dao, currentObj);
				}
				else
				{
					update(dao, currentObj, oldObj, sessionDataBean);
				}
				dao.commit();
				postUpdate(dao, currentObj, oldObj, sessionDataBean);
			}
			else
			{
				throw getBizLogicException(null, "access.execute.action.denied","");
			}
		}
		catch (ApplicationException exception)
		{
			rollback(dao);
			String errMsg = getErrorMessage(exception,currentObj,"Updating");
			LOGGER.debug(errMsg, exception);
			throw new BizLogicException(exception.getErrorKey(),
					exception,exception.getMsgValues(),errMsg);
		}
		finally
		{
			closeSession(dao);
		}
	}
	/**
	 * Updates an object into the database.
	 * @param currentObj The object to be updated.
	 * @param oldObj old Object
	 * @param daoType dao Type
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * update(Object currentObj, Object oldObj,SessionDataBean sessionDataBean)
	 * throws BizLogicException, UserNotAuthorizedException
	 */
	public final void update(Object currentObj, Object oldObj, int daoType,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		update(currentObj, oldObj, sessionDataBean, false);
	}
	/**
	 * Updates an object into the database.
	 * @param currentObj The object to be updated.
	 * @param oldObj old Object
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException BizLogic Exception
	 * */
	public final void update(Object currentObj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		update(currentObj, oldObj, sessionDataBean, false);
	}
	/**
	 * Updates an object into the database.
	 * @param currentObj The object to be updated.
	 * @param daoType dao Type
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * update(Object currentObj) throws BizLogicException,UserNotAuthorizedException
	 */
	public final void update(Object currentObj, int daoType) throws BizLogicException
	{
		update(currentObj, null, null, true);
	}
	/**
	 * Updates an object into the database.
	 * @param currentObj The object to be updated.
	 * @throws BizLogicException BizLogic Exception
	 */
	public final void update(Object currentObj) throws BizLogicException
	{
		update(currentObj, null, null, true);
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
				if(obj instanceof LinkedHashSet && !((LinkedHashSet)obj).isEmpty())
				{
					obj = ((LinkedHashSet)obj).iterator().next();
				}

				HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
				.getHibernateMetaData(appName);
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
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param className Contains class Name.
	 * @param identifier Contains the identifier.
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 * @return isSuccess
	 */
	public boolean populateUIBean(String className, Long identifier, IValueObject uiForm)
			throws BizLogicException
	{
		//long startTime = System.currentTimeMillis();
		boolean isSuccess = false;
		DAO dao = null;
		try
		{
			dao = getHibernateDao(getAppName(),null);
			Object object = dao.retrieveById(className, identifier);

			if (object != null)
			{
				/*
				  If the record searched is present in the database,
				  populate the formbean with the information retrieved.
				 */
				AbstractDomainObject abstractDomain = (AbstractDomainObject) object;

				prePopulateUIBean(abstractDomain, uiForm);
				uiForm.setAllValues(abstractDomain);
				postPopulateUIBean(abstractDomain, uiForm);
				isSuccess = true;
			}
		}
		catch (ApplicationException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "biz.popbean.error", "Exception in bean population");
		}
		finally
		{
			closeSession(dao);
		}

		//String simpleClassName = Utility.parseClassName(className);

		//long endTime = System.currentTimeMillis();
		//LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR UI - " + simpleClassName + " : "
		//	+ (endTime - startTime));

		return isSuccess;
	}
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param className Contains the class Name.
	 * @param identifier identifier.
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 * @return AbstractDomainObject
	 */
	public AbstractDomainObject populateDomainObject(String className, Long identifier,
			IValueObject uiForm) throws BizLogicException
	{
		//long startTime = System.currentTimeMillis();
		DAO dao = null;
		AbstractDomainObject abstractDomain = null;
		try
		{
			dao = getHibernateDao(getAppName(),null);
			Object object = dao.retrieveById(className, identifier);
			abstractDomain = populateFormBean(uiForm, object);
		}
		catch (Exception daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "biz.popdomain.error",
					"Exception in domain population");
		}
		finally
		{
			closeSession(dao);
		}

		//String simpleClassName = Utility.parseClassName(className);
		//long endTime = System.currentTimeMillis();
		//LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB - " + simpleClassName + " : "
		//+ (endTime - startTime));

		return abstractDomain;
	}
	/**
	 * @param uiForm Form object.
	 * @param object object to populate.
	 * @return AbstractDomainObject
	 * @throws AssignDataException throws this exception if not able to set all values.
	 */
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
				//abstractDomain.setAllValues(uiForm);
				TransformerUtil.transform(uiForm, abstractDomain);
			}
		}
		return abstractDomain;
	}
	/**
	 * This method gets called before populateUIBean method.
	 * Any logic before updating uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected abstract void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException;
	/**
	 * This method gets called after populateUIBean method.
	 * Any logic after populating  object uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected abstract void postPopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException;
	/**
	 * This method checks Read Denied.
	 * @return Read Denied or not.
	 */
	public abstract boolean isReadDeniedTobeChecked();
	/**
	 * This method gets Read Denied Privilege Name.
	 * @return Denied Privilege Name.
	 */
	public abstract String getReadDeniedPrivilegeName();
	/**
	 * boolean value true if Privilege to view else false.
	 * @param objName object.
	 * @param identifier Long Id
	 * @param sessionDataBean SessionDataBean object.
	 * @return true if Privilege to view else false.
	 * @throws BizLogicException throw BizLogicException
	 */
	public boolean hasPrivilegeToView(String objName, Long identifier, // NOPMD
			SessionDataBean sessionDataBean)throws BizLogicException
	{
		return false;
	}
	/**
	 * @param exception Exception object thrown in a catch block.
	 * @param key error-key in applicationResource file.
	 * @param logMessage message to log inlogger.
	 * @throws BizLogicException
	 * @return BizLogicException
	 */
	protected BizLogicException getBizLogicException(Exception exception, String key,
			String logMessage)
	{
		LOGGER.debug(logMessage);
		ErrorKey errorKey = ErrorKey.getErrorKey(key);
		return new BizLogicException(errorKey, exception, logMessage);
	}
	/**
	 * @param dao DAO object.
	 */
	protected void rollback(DAO dao)
	{
		try
		{
			if(dao != null)
			{
				dao.rollback();
			}
		}
		catch (DAOException daoEx)
		{
			LOGGER.fatal("Rollback unsuccessful in method.", daoEx);
		}
	}
	/**
	 * @param dao DAO object
	 * @throws BizLogicException :Generic BizLogic Exception- session not closed.
	 */
	protected void closeSession(DAO dao) throws BizLogicException
	{
		try
		{
			if(dao != null)
			{
				dao.closeSession();
			}
		}
		catch (DAOException exception)
		{
			LOGGER.error("Not able to close DAO session.", exception);
			throw new BizLogicException(exception);
		}
	}

	/**
	 * This method will be called to insert hashed data values.
	 * @param tableName :Name of the table
	 * @param columnValues :List of column values
	 * @param columnNames  :List of column names.
	 * @throws BizLogicException  :BizLogicException
	 */
	public void insertHashedValues(String tableName, List<Object> columnValues,
			List<String> columnNames) throws BizLogicException
	{
		JDBCDAO jdbcDao=null;
		try
		{
			LOGGER.debug("Insert hashed data to database");
			String appName=CommonServiceLocator.getInstance().getAppName();
			jdbcDao = DAOConfigFactory.getInstance().getDAOFactory(appName).getJDBCDAO();
			jdbcDao.openSession(null);
			HashedDataHandler hashedDataHandler = new HashedDataHandler();
			hashedDataHandler.insertHashedValues(tableName, columnValues, columnNames, jdbcDao);
		}
		catch (DAOException exception)
		{
			LOGGER.debug(exception.getMessage(), exception);
			throw new BizLogicException(exception);
		}
		finally
		{
			try
			{
				jdbcDao.closeSession();
			}
			catch (DAOException exception)
			{
				LOGGER.debug(exception.getMessage(), exception);
				throw new BizLogicException(exception);
			}
		}
	}
	/**
	 * This method returns hibernate DAO according to the application name.
	 * @param appName Application name.
	 * @param sessionDataBean SessionDataBean object.
	 * @return DAO
	 * @throws DAOException Generic DAO exception.
	 */
	protected static DAO getHibernateDao(String appName,SessionDataBean sessionDataBean) throws DAOException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
		DAO dao = daofactory.getDAO();
		dao.openSession(sessionDataBean);
		return dao;
	}
	/**
     * This method gives the error message.
     * This method should be overrided for customizing error message
     * @param exception - Exception
     * @param obj - Object
     * @param operation operation
     * @return - error message string
     */
    public String getErrorMessage(ApplicationException exception, Object obj, String operation) // NOPMD
    {
    	StringBuffer buff = new StringBuffer();

    	if (exception.getWrapException() == null)
    	{

    		if(exception.getErrorKey()== null && exception.toMsgValuesArray()
    				!= null && exception.toMsgValuesArray().length>0)
    		{
    			for(String msg :exception.toMsgValuesArray())
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
    		if(exp != null)
    		{
    			buff.append(formatException(exp,obj,operation));
    		}
    		else
    		{
    			buff.append(exception.getMessage());
    		}
    	}
        return buff.toString();
    }
    /**
     * This method returns root exception used in message formatter.
     * @param exception ApplicationException
     * @return exception used in message formatter.
     */
    private Exception getWrapException(ApplicationException exception)
    {
    	Throwable rootException=null;
    	Throwable wrapException=exception;
		while(true)
		{
    		if((wrapException instanceof ApplicationException))
    		{
    			wrapException= wrapException.getCause();
    			continue;
    		}
    		else
    		{
    			rootException= wrapException;
    			break;
    		}
		}
    	return (Exception)rootException;
    }
}
