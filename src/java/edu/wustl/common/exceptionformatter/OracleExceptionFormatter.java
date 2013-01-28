/*
 * TODO
 */
package  edu.wustl.common.exceptionformatter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author kalpana_thakur
 *
 */
public class OracleExceptionFormatter implements IDBExceptionFormatter
{

	/**
	 * Class Logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(OracleExceptionFormatter.class);

	/**
	 * This will generate the formatted error messages.
	 * @param excp :Exception.
	 * @param jdbcDAO : jdbcDAO.
	 * @return the formated messages.
	 */
	public String getFormatedMessage(Exception excp,JDBCDAO jdbcDAO )
	{

		Exception objExcp = excp;
        StringBuffer columnNameBuff = new StringBuffer("");
        String formattedErrMsg = ""; // Formatted Error Message return by this method
	    if(objExcp instanceof gov.nih.nci.security.exceptions.CSTransactionException)
        {
            objExcp = (Exception)objExcp.getCause();
        }
        try
        {
        	// Get Constraint Name from messages
        	String sqlMessage = Utility.generateErrorMessage(objExcp);
        	int tempstartIndexofMsg = sqlMessage.indexOf('(');

            String temp = sqlMessage.substring(tempstartIndexofMsg);
            int startIndexofMsg = temp.indexOf(Constants.DELIMETER);
            int endIndexofMsg = temp.indexOf(')');
            String strKey =temp.substring((startIndexofMsg+1),endIndexofMsg);
            startIndexofMsg = strKey.indexOf('.');
            String key =strKey.substring((startIndexofMsg+1));

            formattedErrMsg = getFormatedMessage(
					columnNameBuff, jdbcDAO,
					key);

         }
        catch(Exception e)
        {
            LOGGER.debug(e.getMessage(),e);
        }
        return formattedErrMsg;
    }

/**
 * @param columnNameBuff ;
 * @param jdbcdao :
 * @param key :
 * @return :
 * @throws DAOException :
 * @throws SQLException :
 */
	private String getFormatedMessage(StringBuffer columnNameBuff,
			JDBCDAO jdbcdao, String key)throws DAOException, SQLException

	{
		String formattedErrMsg = "";
		String tableName = "";

		try
		{
			String query = "select COLUMN_NAME,TABLE_NAME from user_cons_columns" +
			" where constraint_name = '"+key+"'";

			ResultSet resultSet = jdbcdao.getQueryResultSet(query);
			while(resultSet.next())
			{
				columnNameBuff.append(resultSet.getString("COLUMN_NAME")).
				append(Constants.DELIMETER);
				tableName = resultSet.getString("TABLE_NAME");
			}
			if(columnNameBuff.length()>0 && tableName.length()>0)
			{
				String columnName = columnNameBuff.toString().
				substring(0,columnNameBuff.toString().length()-1);
				String displayName = Utility.getDisplayName(tableName,jdbcdao);
				Object[] arguments = new Object[]{displayName,columnName};
				formattedErrMsg = MessageFormat.format(Constants.
						CONSTRAINT_VOILATION_ERROR,arguments);
			}
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
		}
		return formattedErrMsg;
	}

}
