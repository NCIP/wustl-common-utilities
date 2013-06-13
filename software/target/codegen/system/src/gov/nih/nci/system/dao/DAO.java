/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.dao;

import java.util.List;

/**
 * @author Satish Patel, Dan Dumitru
 */
public interface DAO {

	/**
	 * Queries the datasource 
	 * 
	 * @param request 
	 *           
	 * @return
	 * @throws DAOException
	 */
	public Response query(Request request) throws DAOException, Exception;
	
	public List<String> getAllClassNames();

}
