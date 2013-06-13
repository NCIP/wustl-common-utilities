/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Jul 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.bizlogic;

import java.util.List;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IBizLogic
{
	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param daoType dao Type.
	 * @throws BizLogicException generic Bizlogic exception.
	 * @deprecated : This method uses daoType argument which is not required anymore,please use method
	 * delete(Object obj) throws UserNotAuthorizedException,BizLogicException;
	 */
	void delete(Object obj, int daoType) throws BizLogicException;
	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @throws BizLogicException BizLogic Exception
	 */
	void delete(Object obj) throws BizLogicException;
	/**
	 * Inserts an object from database.
	 * @param obj The object to be Inserted.
	 * @param sessionDataBean session specific Data
	 * @param daoType dao Type
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * insert(Object obj, SessionDataBean sessionDataBean)
	 * throws BizLogicException, UserNotAuthorizedException;
	 */
	void insert(Object obj, SessionDataBean sessionDataBean, int daoType) throws BizLogicException;
	/**
	 * Inserts an object from database.
	 * @param obj The object to be Inserted.
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException BizLogic Exception
	 */
	void insert(Object obj, SessionDataBean sessionDataBean)
			throws BizLogicException;
	/**
	 * Inserts an object from database.
	 * @param obj The object to be Inserted.
	 * @param daoType dao Type
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * insert(Object obj) throws BizLogicException,UserNotAuthorizedException;
	 */
	void insert(Object obj, int daoType) throws BizLogicException;
	/**
	 * Inserts an object from database.
	 * @param obj The object to be Inserted.
	 * @throws BizLogicException BizLogic Exception
	 */
	void insert(Object obj) throws BizLogicException;
	/**
	 * Updates an object.
	 * @param currentObj current Object.
	 * @param oldObj old Object.
	 * @param daoType dao Type
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * update(Object currentObj, Object oldObj,SessionDataBean sessionDataBean)throws BizLogicException,
	 * UserNotAuthorizedException;
	 */
	void update(Object currentObj, Object oldObj, int daoType, SessionDataBean sessionDataBean)
			throws BizLogicException;
	/**
	 * Updates an object.
	 * @param currentObj current Object.
	 * @param oldObj old Object.
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException BizLogic Exception
	 */
	void update(Object currentObj, Object oldObj,SessionDataBean sessionDataBean)
	throws BizLogicException;
	/**
	 * Updates an object.
	 * @param currentObj current Object.
	 * @param daoType dao Type
	 * @throws BizLogicException BizLogic Exception
	 * @deprecated This method uses daoType argument which is not required anymore,please use method
	 * update(Object currentObj) throws BizLogicException,UserNotAuthorizedException;
	 */
	void update(Object currentObj, int daoType) throws BizLogicException;
	/**
	 * Updates an object.
	 * @param currentObj current Object.
	 * @throws BizLogicException BizLogic Exception
	 */
	void update(Object currentObj) throws BizLogicException;
	/**
	 * creates Protection Element.
	 * @param currentObj current Object.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	void createProtectionElement(Object currentObj) throws BizLogicException;
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName source Object Name
	 * @param selectColumnName An array of field names.
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @return List
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated This method has been deprecated with new DAO implementation.
	 *  instead of this method retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause)
			method can be used.
	 */
	List retrieve(String sourceObjectName, String[] selectColumnName, // NOPMD
			String[] whereColumnName, String[] whereColumnCondition, Object[] whereColumnValue,
			String joinCondition) throws BizLogicException;
	/**
	 * This method checks for a particular privilege on a particular Object_Id.
	 * Gets privilege name as well as Object_Id from appropriate BizLogic
	 * depending on the context of the operation.
	 * @param dao The dao object.
	 * @param domainObject domain Object.
	 * @param sessionDataBean session specific Data
	 * @return Authorized or not.
	 * @throws BizLogicException generic BizLogic Exception.
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized
	 * (edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	boolean isAuthorized(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean) throws BizLogicException;
	/**
	 * This method returns the protection element name which should be used to authorize.
	 * Default Implementation
	 * If call is through some bizLogic which does not require authorization,
	 * let that operation be allowed for ALL
	 * @param dao The dao object.
	 * @param domainObject domain Object.
	 * @return Object Id.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	String getObjectId(DAO dao, Object domainObject)throws BizLogicException;
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName source Object Name.
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparision condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @return List.
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated This method has been deprecated with new DAO implementation.
	 *  instead of this method retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause)
			method can be used.
	 */
	List retrieve(String sourceObjectName, String[] whereColumnName, // NOPMD
			String[] whereColumnCondition, Object[] whereColumnValue, String joinCondition)
			throws BizLogicException;
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param className class Name.
	 * @param colName Contains the field name.
	 * @param colValue Contains the field value.
	 * @return List.
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated
	 */
	List retrieve(String className, String colName, Object colValue)
			throws BizLogicException;
	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the classname whose records are to be retrieved.
	 * @return list.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	List retrieve(String sourceObjectName) throws BizLogicException;
	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName source Object Name.
	 * @param identifier identifier.
	 * @return object.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	Object retrieve(String sourceObjectName, Long identifier) throws BizLogicException;
	/**
	 *
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @param separatorBetweenFields separator Between Fields
	 * @param isToExcludeDisabled is To Exclude Disabled
	 * @return Returns collection
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated
	 */
	List getList(String sourceObjectName, String[] displayNameFields, // NOPMD
			String valueField, String[] whereColumnName, String[] whereColumnCondition,
			Object[] whereColumnValue, String joinCondition, String separatorBetweenFields,
			boolean isToExcludeDisabled) throws BizLogicException;
	/**
	 *
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @param separatorBetweenFields separator Between Fields
	 * @return Returns collection
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated
	 */
	List getList(String sourceObjectName, String[] displayNameFields, // NOPMD
			String valueField, String[] whereColumnName, String[] whereColumnCondition,
			Object[] whereColumnValue, String joinCondition,
			String separatorBetweenFields) throws BizLogicException;
	/**
	 *
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param isToExcludeDisabled -is To Exclude Disabled
	 * @return Returns collection.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	List getList(String sourceObjectName, String[] displayNameFields,
			String valueField, boolean isToExcludeDisabled) throws BizLogicException;
	/**
	 *
	 * @param dao The dao object.
	 * @param sourceClass source Class.
	 * @param classIdentifier class Identifier.
	 * @param objIDArr object ID Array.
	 * @return list of related objects.
	 * @throws BizLogicException generic BizLogic Exception.
	 */
	List getRelatedObjects(DAO dao, Class sourceClass, String classIdentifier,
			Long [] objIDArr) throws BizLogicException;
	/**
	 * To retrieve the attribute value for the given source object name & Id.
	 * @param sourceObjectName Source object in the Database.
	 * @param identifier Id of the object.
	 * @param attributeName attribute name to be retrieved.
	 * @return The Attribute value corresponding to the SourceObjectName & id.
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated
	 */
	Object retrieveAttribute(String sourceObjectName, Long identifier, String attributeName)
			throws BizLogicException;
	/**
	 * This is a wrapper function to retrieves attribute  for given class
	 * name and identifier using dao.retrieveAttribute().
	 * @param objClass source Class object
	 * @param identifier identifer of the source object
	 * @param attributeName attribute to be retrieved
	 * @return object.
	 * @throws BizLogicException generic BizLogic Exception.
	 * @deprecated
	 */
	Object retrieveAttribute(Class objClass, Long identifier, String attributeName)
			throws BizLogicException;
	/**
	 * populates UIBean.
	 * @param className class Name
	 * @param identifier identifier
	 * @param uiForm object of the class which implements IValueObject
	 * @return populated or not.
	 * @throws BizLogicException BizLogic Exception
	 */
	boolean populateUIBean(String className, Long identifier, IValueObject uiForm)
			throws BizLogicException;
	/**
	 * populates Domain Object.
	 * @param className class Name
	 * @param identifier identifier
	 * @param uiForm object of the class which implements IValueObject
	 * @return AbstractDomainObject.
	 * @throws BizLogicException BizLogic Exception.
	 */
	AbstractDomainObject populateDomainObject(String className, Long identifier,
			IValueObject uiForm) throws BizLogicException;
	/**
	 * Checkes is ReadDenied.
	 * @return isReadDenied
	 */
	boolean isReadDeniedTobeChecked();
	/**
	 * Check Privilege To View.
	 * @param objName object Name.
	 * @param identifier identifier
	 * @param sessionDataBean session specific Data
	 * @return hasPrivilegeToView.
	 * @throws BizLogicException BizLogic Exception.
	 */
	boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)throws BizLogicException;
	/**
	 * gets ReadDenied Privilege Name.
	 * @return Read Denied Privilege Name.
	 */
	String getReadDeniedPrivilegeName();
	/**
	 * Retrieves the records for class name in sourceObjectName according QueryWhereClause.
	 * @param sourceObjectName :source object name
	 * @param selectColumnName :An array of field names to be selected
	 * @param queryWhereClause :object of QueryWhereClause.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return list :retrieved objects list
	 * @deprecated : Use public List<Object> retrieve(String sourceObjectName,
	 * String[] selectColumnName,QueryWhereClause queryWhereClause,
	 * List<ColumnValueBean> columnValueBeans)
	 */
	List retrieve(String sourceObjectName,
			String[] selectColumnName,QueryWhereClause queryWhereClause) throws BizLogicException;

	/**
	 * Retrieves the records for class name in sourceObjectName according QueryWhereClause.
	 * @param sourceObjectName :source object name
	 * @param selectColumnName :An array of field names to be selected
	 * @param queryWhereClause :object of QueryWhereClause.
	 * @param columnValueBeans : column value beans.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return list :retrieved objects list
	 */
	List<Object> retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans)
			throws BizLogicException;

	/**
	 * Retrieves the records for class name in sourceObjectName according QueryWhereClause.
	 * @param sourceObjectName :source object name
	 * @param columnValueBean : column value bean.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return list :retrieved objects list
	 */
	List<Object> retrieve(String sourceObjectName, ColumnValueBean columnValueBean)
			throws BizLogicException;

	/**
	 * Sorting of ID columns.
	 * @param sourceObjectName source Object Name
	 * @param selectColumnName Select Column Name
	 * @param separatorBetweenFields separator Between Fields
	 * @param queryWhereClause :object of QueryWhereClause.
	 * @param columnValueBeans column value beans.
	 * @return nameValuePairs.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	List getList(String sourceObjectName, String[] selectColumnName,
			String separatorBetweenFields, QueryWhereClause queryWhereClause,
			List<ColumnValueBean> columnValueBeans)
			throws BizLogicException;

	/**
	 * Retrieves attribute value for given class name and identifier.
	 * @param objClass source Class object
	 * @param attributeName attribute to be retrieved
	 * @param columnValueBean columnValueBean
	 * @return List.
	 * @throws BizLogicException generic BizLogicException.
	 */
	Object retrieveAttribute(Class objClass,
			ColumnValueBean columnValueBean, String attributeName)
			throws BizLogicException;

	/**
	 * This method returns related objects.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param queryWhereClause object of QueryWhereClause
	 * @param columnValueBeans column value beans.
	 * @return list of related objects.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	List getRelatedObjects(DAO dao, Class sourceClass,
			QueryWhereClause queryWhereClause,List<ColumnValueBean> columnValueBeans)
			throws BizLogicException;

	Object retrieveAttribute(DAO dao, Class objClass, ColumnValueBean columnValueBean,
			String attributeName) throws BizLogicException;

}
