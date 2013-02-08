
package edu.wustl.common.exceptionformatter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;

/**
 * @author sachin_lale
 * Description: The Factory class to instatiate ExceptionFormatter object of given Exception.
 */
public class ExceptionFormatterFactory
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ExceptionFormatterFactory.class);

	/**
	 * Specify ResourceBundle property.
	 */
	private static ResourceBundle prop;

	/**
	 * DB_FORMATTER_MAP map.
	 */
	private static final Map<String, IDBExceptionFormatter> DB_FORMATTER_MAP =
		new HashMap<String, IDBExceptionFormatter>();
	static
	{
		try
		{
			/* Load ExceptionFormatter.properties file
			 * property file format is as follows:
			 * Exception_Class_Name = Exception_Formatter_Class_Name
			 */
			prop = ResourceBundle.getBundle("ExceptionFormatter");
			LOGGER.debug("File Loaded");
			initDBFormatterMap();
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage() + " " + e);
		}
	}

	/**
	 *  @param excp Exception excp : The fully qualified class name of excp
	 *  and the Exception_Formatter class name should be in ExceptionFormatter.properties file
	 *  @return Formatter.
	 */
	public static ExceptionFormatter getFormatter(Exception excp)
	{
		ExceptionFormatter expFormatter = null;
		try
		{
			//Get Excxeption Class name from given Object
			String excpClassName = excp.getClass().getName();

			//Get Exception Formatter Class name from Properties file
			String formatterClassName = prop.getString(excpClassName);
			if (formatterClassName == null)
			{
				LOGGER.debug("ExceptionFormatter Class not found for " + excpClassName);
			}
			else
			{
				//	Instantiate a Exception Formatter
				LOGGER.debug("exceptionClass: " + excpClassName);
				LOGGER.debug("formatterClass: " + formatterClassName);
				expFormatter = (ExceptionFormatter) Class.forName(formatterClassName).newInstance();
			}
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage() + " " + e);
		}
		return expFormatter;
	}

	/**
	 * This method gets Display Name.
	 * @param tableName table Name
	 * @param jdbcDao JDBCDAO object.
	 * @return Display Name.
	 */
	public static String getDisplayName(String tableName, JDBCDAO jdbcDao)
	{
		String displayName = "";
		try
		{
			PreparedStatement pstmt = jdbcDao
					.getPreparedStatement("select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA "
							+ "where TABLE_NAME= ? ");
			pstmt.setString(1, tableName);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next())
			{
				displayName = resultSet.getString("DISPLAY_NAME");
				break;
			}
			resultSet.close();
			pstmt.close();
		}
		catch (Exception exception)
		{
			LOGGER.debug(exception.getMessage(), exception);
		}
		return displayName;
	}

	/**
	 * Gets IDB Exception Formatter.
	 * @param dbType db Type
	 * @return IDBExceptionFormatter
	 */
	public static IDBExceptionFormatter getIDBExceptionFormatter(String dbType)
	{
		return DB_FORMATTER_MAP.get(dbType.toLowerCase(CommonServiceLocator.getInstance()
				.getDefaultLocale()));
	}

	/**
	 * init DBFormatter Map.
	 */
	public static void initDBFormatterMap()
	{
		DB_FORMATTER_MAP.put("mysql", (IDBExceptionFormatter) new MysqlExceptionFormatter());
		DB_FORMATTER_MAP.put("oracle", (IDBExceptionFormatter) new OracleExceptionFormatter());
	}
}
