/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

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
