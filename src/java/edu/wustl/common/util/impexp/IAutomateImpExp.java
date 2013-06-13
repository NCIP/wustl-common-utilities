/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.impexp;

import java.sql.Connection;
import java.sql.SQLException;

import edu.wustl.common.exception.ApplicationException;

/**
 * This interface is for common Import Export functionality.
 * @author ravi_kumar
 *
 */
public interface IAutomateImpExp
{
	/**
	 * This method set all parameters to passed into main methods.
	 * @param dbUtil :object of DatabaseUtility
	 * @throws ApplicationException Application Exception
	 */
	void init(DatabaseUtility dbUtil) throws ApplicationException;

	/**
	 * Method to import meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	void executeImport(String[] args) throws ApplicationException;

	/**
	 * Method to export meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	void executeExport(String[] args) throws ApplicationException;

	/**
	 * Methods to get database connection.
	 * @throws SQLException Generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 * @return Database connection
	 */
	Connection getConnection()throws SQLException, ClassNotFoundException;
}
