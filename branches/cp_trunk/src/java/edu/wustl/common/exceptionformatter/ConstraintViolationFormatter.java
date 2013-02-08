/**
 * <p>Title: ConstraintViolationFormatter class>
 * <p>Description: ConstraintViolationFormatter is used to construct user readable constraint
 * violation messages</p>
 * @version 1.0
 * @author kalpana_thakur
 */
package edu.wustl.common.exceptionformatter;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * @author kalpana_thakur
 * Used to format the exception thrown to user readable form.
 */
public class ConstraintViolationFormatter implements ExceptionFormatter
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ConstraintViolationFormatter.class);
	/**
	 * This will be called to format constraint violation exception messages.
	 * @param objExcp : Exception thrown.
	 * @return string : It return the formatted error messages.
	 */
	public String formatMessage(Exception objExcp)
	{
		String formatedMessage = null ;
		JDBCDAO jdbcDAO=null;
		try
		{
			String appName = CommonServiceLocator.getInstance().getAppName();
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
			String dbType= daoFactory.getDataBaseType();
			jdbcDAO = daoFactory.getJDBCDAO();
			jdbcDAO.openSession(null);
			IDBExceptionFormatter idbExFormatter= ExceptionFormatterFactory.getIDBExceptionFormatter(dbType);
			formatedMessage =idbExFormatter.getFormatedMessage(objExcp, jdbcDAO);
		}
		catch(Exception exp)
		{
			LOGGER.debug("Not able to format exception.", exp);
		}
		finally
		{
			try
			{
				jdbcDAO.closeSession();
			}
			catch (DAOException exception)
			{
				LOGGER.debug("Exception during closing the JDBC session. ",exception);
			}
		}
		return formatedMessage;
	}
}
