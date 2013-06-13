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
 * <p>Title: DAOException Class>
 * <p>Description:DAOException implements the ApplicationException interface
 * It will customizes the database related exception.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.exception;

import org.hibernate.HibernateException;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.util.DAOConstants;


/**
 * @author kalpana_thakur
 *	This will customizes the database related exceptions.
 */
public class DAOException extends ApplicationException
{

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Error log.
	 */
	private String errorlog ;

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * It will called on occurrence of database related exception.
	 * @param errorKey : key assigned to the error
	 * @param exception :exception
	 * @param msgValues : message displayed when error occurred
	 */
	public DAOException(final ErrorKey errorKey, final Exception exception, final String msgValues)
	{
		super(errorKey, exception, msgValues);
	}

	/**
	 * The public constructor will be invoked on occurrence of hibernate exception.
	 * It also restrict creating object without initializing mandatory members.
	 * It will invoked on occurrence of hibernate exception.
	 * @param errorKey : key assigned to the error
	 * @param hibernateException :
	 * @param msgValues : message displayed when error occurred
	 */
	public DAOException(final ErrorKey errorKey,HibernateException hibernateException,
			final String msgValues)
	{
		super(errorKey, hibernateException, msgValues);
		errorlog = generateErrorMessage(hibernateException);
	}

	/**
	 * This function formats a complete message with all details about error which caused the
	 * exception. This function intended to use for logging error message.
	 *
	 * Usage: logger.error(ex.getLogMessage,ex);
	 *
	 * @return formatted detailed error message.
	 */
	public String getLogMessage()
	{
		StringBuffer logMsg = new StringBuffer(super.getLogMessage());
		logMsg.append(errorlog);
		return logMsg.toString();
	}


	/**
	 * This method will be invoked to generate buffer of all the
	 * wrapped messages when hibernate exception has been thrown.
	 * @param hibernateException Exception
	 * @return message.
	 */
	private String generateErrorMessage(HibernateException hibernateException)
	{
		StringBuffer message = new StringBuffer();
		if(hibernateException != null)
		{
			String [] str = hibernateException.getMessages();

			for (int i = 0; i < str.length; i++)
			{
				message.append(str[i]).append(DAOConstants.TRAILING_SPACES);
			}
		}
		return message.toString();
	}
}
