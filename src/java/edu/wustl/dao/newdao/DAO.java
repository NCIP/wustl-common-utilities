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
 * <p>Title: DAO Interface>
 * <p>Description:	DAO provides methods to manipulate the domain objects.
 * It provides methods like insert ,update etc .</p>
 * @version 1.0
 * @author sachin_lale
 */

package edu.wustl.dao.newdao;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author sachin_lale
 * Handles database operations like insertion, updation, deletion and retrieval of data.
 */
public interface DAO<T, ID extends Serializable>
{

	/**
	 * Insert the Object to the database.
	 * @param obj Object to be inserted in database
	 * @throws DAOException generic DAOException
	 */
	void insert(T obj) throws DAOException;

	/**
	 * updates the object into the database.
	 * @param obj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	T update(T obj) throws DAOException;

	/**h 
	 * 
	 * @param currentObj
	 * @param previousObj
	 * @throws DAOException
	 */
	T update(T currentObj, T previousObj) throws DAOException;

	/**
	 * Deletes the persistent object from the database.
	 * @param obj The object to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	void delete(T obj) throws DAOException;

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	List<T> findAll() throws DAOException;

	/**
	 * Retrieve and returns the source object for given id.
	 * @param sourceObjectName Source object in the Database.
	 * @param identifier identifier of source object.
	 * @return object
	 * @throws DAOException generic DAOException.
	 */
	T findById(ID id) throws DAOException;

	List executeQuery(String query, Integer startIndex, Integer maxRecords,
			List<ColumnValueBean> columnValueBeans) throws DAOException;

	List executeNamedQuery(String queryName,Integer startIndex, Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException;
	
	List executeQuery(String query, Integer startIndex, Integer maxRecords,ColumnValueBean columnValueBean) throws DAOException;

	List executeNamedQuery(String queryName,Integer startIndex, Integer maxRecords,ColumnValueBean columnValueBean) throws DAOException;
	
	public List executeSQLQuery(String sql,Integer startIndex, Integer maxRecords,List<ColumnValueBean> columnValueBeans) throws DAOException;
	
	public void executeSQLUpdate(String sql,List<LinkedList<ColumnValueBean>> columnValueBeans) throws DAOException;
}