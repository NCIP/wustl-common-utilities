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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This is an interface for QueryBizLogic.
 * @author ravi_kumar
 *
 */
public interface IQueryBizLogic
{
	/**
	 * get Query Object Name Table Name Map.
	 * @return Query Object Name Table Name Map
	 */
	Map getQueryObjectNameTableNameMap();

	/**
	 * get Pivilege Type Map.
	 * @return Pivilege Type Map
	 */
	Map getPivilegeTypeMap();

	/**
	 * get Relation Data.
	 * @return Relation Data map
	 */
	Map getRelationData();

	/**
	 * This method initialize Query Data.
	 */
	void initializeQueryData();

	/**
	 * get Alias Name.
	 * @param columnName column Name
	 * @param columnValue column Value
	 * @return Alias Name
	 * @throws DAOException DAO Exception
	 */
	String getAliasName(String columnName, Object columnValue) throws DAOException;

	/**
	 * get Column Names.
	 * @param value value.
	 * @return Column Names
	 * @throws DAOException DAO Exception
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	List getColumnNames(String value) throws DAOException, ClassNotFoundException;

	/**
	 * get Next Table Names.
	 * @param prevValue previous Value.
	 * @return returns set of Next Table Names.
	 * @throws DAOException DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	Set getNextTableNames(String prevValue) throws DAOException, ClassNotFoundException;

	/**
	 * get Display Name.
	 * @param aliasName alias Name
	 * @return Display Name
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	String getDisplayName(String aliasName) throws DAOException, ClassNotFoundException;

	/**
	 * get Display Name by Table Name.
	 * @param tableName table Name
	 * @return Display Name
	 * @throws DAOException DAO Exception
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	String getDisplayNamebyTableName(String tableName) throws DAOException,ClassNotFoundException;
	
	String getDisplayNamebyTableName(String tableName,SessionDataBean sessionDataBean) throws DAOException,ClassNotFoundException;

	/**
	 * get All Table Names.
	 * @param aliasName alias Name
	 * @param forQI for QI.
	 * @return All Table Names
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	Set getAllTableNames(String aliasName, int forQI) throws DAOException,ClassNotFoundException;

	/**
	 * get Main Objects Of Query.
	 * @return list of Main Objects Of Query.
	 * @throws DAOException DAOException.
	 */
	List getMainObjectsOfQuery() throws DAOException;

	/**
	 * get Related Table Aliases.
	 * @param aliasName alias Name
	 * @return list of Related Table Aliases.
	 * @throws DAOException DAOException
	 */
	List getRelatedTableAliases(String aliasName) throws DAOException;

	/**
	 * set tables in path.
	 * @param parentTableId parent Table Id.
	 * @param childTableId child Table Id.
	 * @return tables in path.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	Set setTablesInPath(Long parentTableId, Long childTableId) throws DAOException,ClassNotFoundException;

	/**
	 * get Attribute Type.
	 * @param columnName column Name
	 * @param aliasName alias Name
	 * @return Attribute Type
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	String getAttributeType(String columnName, String aliasName) throws DAOException,ClassNotFoundException;

	/**
	 * get Table Id From Alias Name.
	 * @param aliasName alias Name.
	 * @return Table Id
	 * @throws DAOException DAOException
	 */
	String getTableIdFromAliasName(String aliasName) throws DAOException;

	/**
	 * set Column Names.
	 * @param aliasName alias Name
	 * @return Column Names.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	List setColumnNames(String aliasName) throws DAOException, ClassNotFoundException;

	/**
	 * get Column Names.
	 * @param aliasName alias Name
	 * @param defaultViewAttributesOnly default View Attributes Only.
	 * @return Column Names.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	List getColumnNames(String aliasName, boolean defaultViewAttributesOnly)
			throws DAOException, ClassNotFoundException;

	/**
	 * get Specimen Type Count.
	 * @param specimanType speciman Type
	 * @param jdbcDAO jdbc DAO
	 * @return Specimen Type Count
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	String getSpecimenTypeCount(String specimanType, JDBCDAO jdbcDAO) throws DAOException,
	ClassNotFoundException;

	/**
	 * get Total Summary Details.
	 * @return Total Summary Details.
	 * @throws DAOException DAOException
	 */
	Map<String, Object> getTotalSummaryDetails() throws DAOException;

	/**
	 * This method inserts Query For MySQL.
	 * @param sqlQuery sql Query
	 * @param sessionData session Data
	 * @param jdbcDAO jdbcDAO object.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	void insertQueryForMySQL(String sqlQuery, SessionDataBean sessionData, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException;

	/**
	 * This method insert Query For Oracle.
	 * @param sqlQuery sql Query
	 * @param sessionData session Data
	 * @param jdbcDAO jdbcDAO object
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	void insertQueryForOracle(String sqlQuery, SessionDataBean sessionData, JDBCDAO jdbcDAO)
	throws DAOException, ClassNotFoundException, SQLException, IOException;

	/**
	 * This method insert Query.
	 * @param sqlQuery sql Query
	 * @param sessionData session Data.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	void insertQuery(String sqlQuery, SessionDataBean sessionData) throws DAOException,
	ClassNotFoundException;

	/**
	 * @param sessionDataBean session Data Bean
	 * @param querySessionData query Session Data
	 * @param startIndex start Index
	 * @return PagenatedResultData
	 * @throws DAOException DAOException
	 */
	PagenatedResultData execute(SessionDataBean sessionDataBean,
			QuerySessionData querySessionData, int startIndex) throws DAOException;

	/**
	 * This method execute SQL.
	 * @param sql sql
	 * @return List
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	List executeSQL(String sql) throws DAOException, ClassNotFoundException;

}
