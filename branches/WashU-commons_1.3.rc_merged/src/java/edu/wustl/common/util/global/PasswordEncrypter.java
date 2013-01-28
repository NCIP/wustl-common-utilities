
package edu.wustl.common.util.global;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.impexp.DatabaseUtility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.util.StringUtilities;

/**
 * PasswordEncrypter: This class encrypts all data of given field from a given table.
 * The database connection parameters have to be provided.
 *
 * The encryption is done using the class PasswordManager.
 *
 * @author abhishek_mehta
 *
 */

public final class PasswordEncrypter
{
	/**
	 * private constructor.
	 */
	private PasswordEncrypter()
	{

	}
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(PasswordEncrypter.class);

	/**
	 * dbUtility object is used to store the argument values and getting database connection.
	 */
	private static DatabaseUtility dbUtility= new DatabaseUtility();

	/**
	 * Minimum number of arguments required.
	 */
	private static final int MIN_NO_ARGS=9;
	/**
	 * The name of the csm table whose password field is to be encrypted.
	 */
	private static String csmDbTableName="csm_user";

	/**
	 * Index for csm table parameter.
	 */
	private static final int INDEX_CSM_DB_TABLE_NAME = 9;

	/**
	 * The name of the application table whose password field is to be encrypted.
	 */
	private static String dbTableName;

	/**
	 * Index for database table name whose password field is to be encrypted.
	 */
	private static final int INDEX_DB_TABLE_NAME=7;

	/**
	 * The name of the field in table whose row values have to be encrypted.
	 */
	private static String dbTableFieldName;

	/**
	 * Index for field-name in table.
	 */
	private static final int INDEX_DB_TABLE_FIELD_NAME = 8;

	/**
	 * Main method.
	 * @param args arguments to main methods.
	 * @throws Exception -If number of arguments are less than 7
	 */
	public static void main(String[] args) throws Exception
	{

		configureDBConnection(args);
		// Create an updatable result set
		Connection connection = dbUtility.getConnection();

		//Encrypting password for csm_user table
		String sql = "SELECT " + csmDbTableName + ".* FROM " + csmDbTableName;
		updatePasswords(connection, sql);

		//Encrypting password for catissue_password table
		sql = "SELECT " + dbTableName + ".* FROM "
				+ dbTableName;
		updatePasswords(connection, sql);
	}

	/**
	 * This method will update password field with new encrypted password in the database.
	 * @param connection Database connection object
	 * @param sql Query String
	 * @throws SQLException generic SQL Exception
	 */
	private static void updatePasswords(Connection connection, String sql) throws SQLException
	{
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		ResultSet resultSet = stmt.executeQuery(sql);

		resultSet.first();
		try
		{
		while (resultSet.next())
		{
			String userPassword = resultSet.getString(dbTableFieldName);
			if (!StringUtilities.isBlank(userPassword))
			{
					//Encrypting the password and updating the database.
					String encryptedPasswordWithNewEncryption = PasswordManager
							.encrypt(PasswordManager.decode(userPassword));
					resultSet.updateString(dbTableFieldName,
							encryptedPasswordWithNewEncryption);
					resultSet.updateRow();
				}
			}
		}
		catch (PasswordEncryptionException exception)
		{
			LOGGER.fatal("Not able to encrypt the passwords.", exception);
		}
		finally
		{
		resultSet.close();
		stmt.close();
		}
	}

	/**
	 * This method is for configuring database connection.
	 * @param args String[] of configuration info
	 * @throws Exception -If number of arguments are less than 7
	 */
	private static void configureDBConnection(String[] args) throws Exception
	{
		if (args.length >= MIN_NO_ARGS)
		{
			dbUtility.setDbParams(args);
			setOtherParams(args);
		}
		else
		{
			LOGGER.debug("Incorrect number of parameters!!!!");
			throw new Exception("Incorrect number of parameters!!!!");
		}
	}

	/**
	 * This method set parameters related to this class.
	 * @param args String array of configuration info
	 */
	private static void setOtherParams(String[] args)
	{
		dbTableName=args[INDEX_DB_TABLE_NAME];
		dbTableFieldName=args[INDEX_DB_TABLE_FIELD_NAME];
		if(args.length>MIN_NO_ARGS)
		{
			csmDbTableName=args[INDEX_CSM_DB_TABLE_NAME];
		}
	}
}
