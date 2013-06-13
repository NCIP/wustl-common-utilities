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
 * Created on Aug 10, 2005
 */

package edu.wustl.dao.exception;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

/**
 * @author kapil_kaveeshwar
 */
public class AuditException extends ApplicationException
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 5720766957546246226L;

	/**
	 * The Only public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param exception root exception, if any, which caused this error.
	 * @param msgValues custom message, additional information.
	 */
	public AuditException(Exception exception, String msgValues)
	{
		super(ErrorKey.getErrorKey("error.audit.fail"),exception,msgValues);
	}

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * It will called on occurrence of database related exception.
	 * @param errorKey : key assigned to the error
	 * @param exception :exception
	 * @param msgValues : message displayed when error occurred
	 */
	public AuditException(final ErrorKey errorKey, final Exception exception, final String msgValues)
	{
		super(errorKey, exception, msgValues);
	}
}
