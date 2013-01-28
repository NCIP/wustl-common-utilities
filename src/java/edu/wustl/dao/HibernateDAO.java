/**
 * <p>Title: HibernateDAO Interface</p>
 * <p>Description :This interface defines methods which are specific to
 * Hibernate operations</p>
 *  * @author kalpana_thakur
 */
package edu.wustl.dao;

import java.util.List;
import java.util.Map;

import edu.wustl.common.domain.LoginDetails;
import edu.wustl.dao.exception.AuditException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.NamedQueryParam;


/** This interface defines methods which are specific to Hibernate operations .*/

public interface HibernateDAO extends DAO
{

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param queryName : handle for named query.
	 * @param namedQueryParams : Map holding the parameter type and parameter value.
	 * @return the list of data.
	 * @throws DAOException :Generic DAOException.
	 */
	List executeNamedQuery(String queryName,Map<String, NamedQueryParam> namedQueryParams) throws DAOException;

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @param columnValueBeans column data beans.
	 * @return list of data.
	 * @throws DAOException Database exception.
	 */
	List executeParamHQL(String query,List<ColumnValueBean> columnValueBeans)
	throws DAOException;

	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve
	 * @param query  HQL query to execute
	 * @param startIndex Starting index value
	 * @param maxRecords max number of records to fetch
	 * @param paramValues List of parameter values.
	 * @return List of data.
	 * @throws DAOException database exception.
	 */
	List executeQuery(String query,Integer startIndex,
			Integer maxRecords,List paramValues) throws DAOException;

	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "!=", "<", ">", "in", "null" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @param onlyDistinctRows true if only distinct rows should be selected.
	 * @param columnValueBeans columnValueBeans
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 *  */

	List retrieve(String sourceObjectName,String[] selectColumnName,
			QueryWhereClause queryWhereClause,boolean onlyDistinctRows,
			List<ColumnValueBean> columnValueBeans) throws DAOException;


	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "<", ">", "=<", ">=" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 * @param columnValueBeans columnValueBeans
	 */

	List retrieve(String sourceObjectName,
			String[] selectColumnName,QueryWhereClause queryWhereClause,
			List<ColumnValueBean> columnValueBeans) throws DAOException;



	/**
	 * Retrieves attribute value for given class name and identifier.
	 * @param objClass source Class object
	 * @param attributeName attribute to be retrieved
	 * @param columnValueBean columnValueBean
	 * @return List.
	 * @throws DAOException generic DAOException.
	 */
	List retrieveAttribute(Class objClass, ColumnValueBean columnValueBean,
			String attributeName) throws DAOException;


	/**
	 * This method will be called to set the hibernate metadata
	 * for the application.
	 * @param hibernateMetaData the hibernateMetaData to set
	 */
	void setHibernateMetaData(HibernateMetaData hibernateMetaData);

	/**
	 * This method will be called to fetch hibernate metadata
	 * associated to the application.
	 * @return the hibernateMetaData
	 */
	HibernateMetaData getHibernateMetaData();

	/**
	 * Sets the status of LoginAttempt to loginStatus provided as an argument.
	 * @param loginStatus LoginStatus boolean value.
	 * @param loginDetails LoginDetails object.
	 * @throws AuditException AuditException
	 */
	void auditLoginEvents(boolean loginStatus,
			LoginDetails loginDetails)throws AuditException;

	/**
	 * Execute query.
	 *
	 * @param query the query
	 * @param columnValueBeans the column value beans
	 * @param maxResults the max results
	 *
	 * @return the list
	 *
	 * @throws DAOException the DAO exception
	 */
	List executeQuery(String query,List<ColumnValueBean> columnValueBeans, int maxResults)
	throws DAOException;
}
