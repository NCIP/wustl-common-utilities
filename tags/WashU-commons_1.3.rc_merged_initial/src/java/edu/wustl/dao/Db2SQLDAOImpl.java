package edu.wustl.dao;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
/**
 * DB2DAO.
 * @author kalpana_thakur
 *
 */
public class Db2SQLDAOImpl  extends AbstractJDBCDAOImpl
{

	/**
	 * Logger.
	 */
	private static final Logger logger =
		Logger.getCommonLogger(Db2SQLDAOImpl.class);


	/**
	 * @param tableName :
	 * @throws DAOException :
	 */
	public void deleteTable(String tableName) throws DAOException
	{
		logger.debug("Drop Table");
		executeUpdate("DROP TABLE " +
				tableName+ " cascade constraints");

	}
}
