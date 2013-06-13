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
 * TODO
 */
package edu.wustl.common.exceptionformatter;

import edu.wustl.dao.JDBCDAO;

/**
 * @author kalpana_thakur
 *
 */
public interface IDBExceptionFormatter
{
	/**
	 * @param objExcp :Exception.
	 * @param jdbcDAO : jdbcDAO.
 	 * @return It will return the formatted messages.
	 */
	String getFormatedMessage(Exception objExcp,JDBCDAO jdbcDAO);
}
