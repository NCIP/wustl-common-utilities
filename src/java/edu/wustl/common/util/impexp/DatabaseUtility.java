package edu.wustl.common.util.impexp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * Database utility class.
 * @author ravi_kumar
 *
 */
public class DatabaseUtility
{

	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static final Logger LOGGER = Logger.getCommonLogger(DatabaseUtility.class);
	/**
	 * This map contains class name for Automate import/export and their database type.
	 */
	private static Map<String,String> dbNameVsClassName;

	static {
		dbNameVsClassName= new HashMap<String,String>();
		dbNameVsClassName.put("oracle", "edu.wustl.common.util.impexp.OracleAutomateImpExp");
		dbNameVsClassName.put("mysql", "edu.wustl.common.util.impexp.MySqlAutomateImpExp");
		dbNameVsClassName.put("mssql", "edu.wustl.common.util.impexp.MsSqlAutomateImpExp");
		}
	/**
	 * The Name of the server for the database. For example : localhost
	 */
	private String dbServerName;

	/**
	 * The Port number of the server for the database.
	 */
	private String dbServerPortNumber;

	/**
	 * The Type of Database. Use one of the two values 'MySQL', 'Oracle'.
	 */
	private String dbType;

	/**
	 * Name of the Database.
	 */
	private String dbName;

	/**
	 * Database User name.
	 */
	private String dbUserName;

	/**
	 * Database Password.
	 */
	private String dbPassword;

	/**
	 * The database Driver.
	 */
	private String dbDriver;

	/**
	 * Index for database Server Name.
	 */
	private static final int  INDX_DB_SERVER_NAME=0;

	/**
	 * Index for database server port number.
	 */
	private static final int  INDX_DB_SERVER_PORT_NUMBER=1;

	/**
	 * Index for database type.
	 */
	private static final int  INDX_DB_TYPE=2;

	/**
	 * Index for database name.
	 */
	private static final int  INDX_DB_NAME=3;

	/**
	 * Index for database user name.
	 */
	private static final int  INDX_DB_USER_NAME=4;

	/**
	 * Index for database password.
	 */
	private static final int  INDX_DB_PASSWORD=5;

	/**
	 * Index for database driver.
	 */
	private static final int  INDX_DB_DRIVER=6;

	/**
	 * For mysql database.
	 */
	public static final String MYSQL_DATABASE="MYSQL";

	/**
	 * For oracle database.
	 */
	public static final String ORACLE_DATABASE="ORACLE";

	/**
	 * @return the dbServerName
	 */
	public String getDbServerNname()
	{
		return dbServerName;
	}

	/**
	 * @param dbServerName the dbServerName to set
	 */
	public void setDbServerName(String dbServerName)
	{
		this.dbServerName = dbServerName;
	}

	/**
	 * @return the dbServerPortNumber
	 */
	public String getDbServerPortNumber()
	{
		return dbServerPortNumber;
	}

	/**
	 * @param dbServerPortNumber the dbServerPortNumber to set
	 */
	public void setDbServerPortNumber(String dbServerPortNumber)
	{
		this.dbServerPortNumber = dbServerPortNumber;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType()
	{
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType)
	{
		this.dbType = dbType;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName()
	{
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}

	/**
	 * @return the dbUserName
	 */
	public String getDbUserName()
	{
		return dbUserName;
	}

	/**
	 * @param dbUserName the dbUserName to set
	 */
	public void setDbUserName(String dbUserName)
	{
		this.dbUserName = dbUserName;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword()
	{
		return dbPassword;
	}

	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}

	/**
	 * @return the dbDriver
	 */
	public String getDbDriver()
	{
		return dbDriver;
	}

	/**
	 * @param dbDriver the dbDriver to set
	 */
	public void setDbDriver(String dbDriver)
	{
		this.dbDriver = dbDriver;
	}

	/**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws SQLException Generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 * @throws ApplicationException Application Exception
	 */
	public Connection getConnection() throws SQLException, ClassNotFoundException,ApplicationException
	{
		IAutomateImpExp automateImpExp=(IAutomateImpExp)getAutomatImpExpObj();
		automateImpExp.init(this);
		return automateImpExp.getConnection();
	}

	/**
	 * This method sets all database parameters.
	 * @param args String array containing database parameters.
	 */
	public void setDbParams(String[] args)
	{
		setDbServerName(args[INDX_DB_SERVER_NAME]);
		setDbServerPortNumber(args[INDX_DB_SERVER_PORT_NUMBER]);
		setDbType(args[INDX_DB_TYPE]);
		setDbName(args[INDX_DB_NAME]);
		setDbUserName(args[INDX_DB_USER_NAME]);
		setDbPassword(args[INDX_DB_PASSWORD]);
		setDbDriver(args[INDX_DB_DRIVER]);
	}

	/**
	 * This methods returns object for import/export meta-data according to database type.
	 * @return object of IAutomateImpExp.
	 * @throws ApplicationException Application Exception
	 */
	public IAutomateImpExp getAutomatImpExpObj() throws ApplicationException
	{
		String className=dbNameVsClassName.get(dbType);

		try
		{
			return (IAutomateImpExp)Class.forName(className).newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.debug(exception.getMessage(), exception);
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.dbtype.error");
			throw new ApplicationException(errorKey,exception,
				"Not able to get import/export class. Please make sure database type is correct.");
		}
	}
}
