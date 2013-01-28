/*
 * This will hold the implementation specific to MySQL.
 */

package edu.wustl.dao;

import edu.wustl.dao.exception.DAOException;


/**
 * @author kalpana_thakur
 *
 */
public class MySQLDAOImpl extends AbstractJDBCDAOImpl
{

	/**
	 * @param tableName :
	 * @throws DAOException :
	 */
	public void deleteTable(String tableName) throws DAOException
	{
		StringBuffer query;
		query = new StringBuffer("DROP TABLE IF EXISTS ").append(tableName);
		executeUpdate(query.toString());

	}
}
