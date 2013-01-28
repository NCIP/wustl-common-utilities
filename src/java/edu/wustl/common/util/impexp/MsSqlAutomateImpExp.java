package edu.wustl.common.util.impexp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

/**
 * This class is for import/export meta data for MSSql server database.
 * @author ravi_kumar
 */
public class MsSqlAutomateImpExp extends AbstractAutomateImpExp
{
	/**
	 * Format file name.
	 */
	private static final String FORMAT_FILE_EXTENTION = "_FormatFile.txt";

	/**
	 * Method to export meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	public void executeExport(String[] args) throws ApplicationException
	{
		//Export method for MSSQL is not implemented.
	}

	/**
	 * Method to import meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	public void executeImport(String[] args) throws ApplicationException
	{
		preImpExp(args);
		Connection conn=null;
		try
		{
			conn = getConnection();
			for(int i = 0 ; i < getSize(); i++)
			{
				String dumpFilePath = getFilePath()+getTableNamesList().get(i)+".csv";
				String formatFilePath=getFilePath()+getTableNamesList().get(i)
					+FORMAT_FILE_EXTENTION;
				importDataMsSQLServer(conn,dumpFilePath,getTableNamesList().get(i),formatFilePath);
			}
		}
		catch(Exception exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.mssqlimport.error");
			throw new ApplicationException(errorKey,exception,"MsSqlAutomateImpExp");
		}
		finally
		{
			closeConnection(conn);
		}

	}

	/**
	 * Method to get database connection.
	 * @throws SQLException Generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 * @return MYSQL database connection
	 */
	public Connection getConnection() throws SQLException, ClassNotFoundException
	{
		DatabaseUtility dbUtil=getDbUtility();
		Connection connection = null;
		Class.forName(dbUtil.getDbDriver());
		String url = "";

		url = new StringBuffer("jdbc:sqlserver://").append(dbUtil.getDbServerNname()).append(":").
			append(dbUtil.getDbServerPortNumber()).append(";").append("databaseName=")
				.append(dbUtil.getDbName()).append(";").toString();

		connection = DriverManager.getConnection(url, dbUtil.getDbUserName(),dbUtil.getDbPassword());
		return connection;
	}

	/**
	 *  This method will insert the data to mssqlserver database.
	 * @param conn Database connection
	 * @param dataFileName File Name
	 * @param tableName Table Name.
	 * @param formatFileName Format FIle Name.
	 * @throws SQLException Generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 */
    private void importDataMsSQLServer(Connection conn,String dataFileName, String tableName,
    			String formatFileName)
    	throws SQLException, ClassNotFoundException
    	{
        Statement stmt=null;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query =  new StringBuffer("BULK INSERT ").append(tableName).
            append(" FROM '").append(dataFileName).append("' WITH ( FIELDTERMINATOR  = ',' , FORMATFILE = '").
            append(formatFileName).append("' )").toString();
            stmt.execute(query);
       	}
        finally
        {
            stmt.close();
        }
    }
}
