/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.csm.loginmodule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.sql.DataSource;

import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authentication.principal.EmailIdPrincipal;
import gov.nih.nci.security.authentication.principal.FirstNamePrincipal;
import gov.nih.nci.security.authentication.principal.LastNamePrincipal;
import gov.nih.nci.security.authentication.principal.LoginIdPrincipal;
import gov.nih.nci.security.constants.Constants;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;
import gov.nih.nci.security.util.StringEncrypter;
import gov.nih.nci.security.util.StringUtilities;
import gov.nih.nci.security.util.StringEncrypter.EncryptionException;

public class CSMRDBMSHelper
{

	/**
	 * Accepts the connection properties as well as the user id and password.
	 * It opens a connection to the database and fires a the query to find. If
	 * the query was successful then it returns TRUE else it returns FALSE
	 * @param connectionProperties table containing details for establishing
	 * 			connection like the driver, the url, the user name and the
	 * 			password for the establishing the database connection. It also
	 * 			contains the actual query statement to retrieve the user record
	 * @param userID the user entered user name provided by the calling
	 * 			application
	 * @param password the user entered password provided by the calling
	 * 			application
	 * @param subject
	 * @return TRUE if the authentication was sucessful using the provided user
	 * 		   	credentials and FALSE if the authentication fails
	 * @throws CSInternalConfigurationException
	 * @throws CSInternalInsufficientAttributesException
	 */

	private static org.apache.log4j.Logger logger = Logger.getLogger(CSMRDBMSHelper.class);

	public static boolean authenticate(Hashtable connectionProperties, String userID,
			char[] password, Subject subject) throws CSInternalConfigurationException,
			CSInternalInsufficientAttributesException
	{

		Connection connection = getConnection(connectionProperties);
		if (connection == null)
		{
			return false;
		}

		String encryptedPassword = new String(password);
		String encryptionEnabled = (String) connectionProperties.get(Constants.ENCRYPTION_ENABLED);
		if (!StringUtilities.isBlank(encryptionEnabled)
				&& encryptionEnabled.equalsIgnoreCase(Constants.YES))
		{
			StringEncrypter se;
			try
			{
				se = new StringEncrypter();
				encryptedPassword = se.encrypt(new String(password));
			}
			catch (EncryptionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		encryptedPassword = StringUtilities.initTrimmedString(encryptedPassword);

		String query = (String) connectionProperties.get("query");
		if (StringUtilities.isBlank(query))
		{
			return authenticateAndObtainSubject(connection, connectionProperties, userID,
					encryptedPassword, subject);
		}
		else
		{
			return executeQuery(connection, query, userID, encryptedPassword);
		}

	}

	/**
	 * Accepts the connection properties as well as the user id and password.
	 * It opens a connection to the database and fires a the query to find. If
	 * the query was successful then it returns TRUE else it returns FALSE
	 * @param connection connection of the DB on which to validate the user.
	 * @param connectionProperties table containing details for establishing
	 * 			connection like the driver, the url, the user name and the
	 * 			password for the establishing the database connection. It also
	 * 			contains the actual query statement to retrieve the user record
	 * @param userID the user entered user name provided by the calling
	 * 			application
	 * @param password the user entered password provided by the calling
	 * 			application
	 * @param subject
	 * @return TRUE if the authentication was sucessful using the provided user
	 * 		   	credentials and FALSE if the authentication fails
	 * @throws CSInternalConfigurationException
	 * @throws CSInternalInsufficientAttributesException
	 */
	private static boolean authenticateAndObtainSubject(Connection connection,
			Hashtable connectionProperties, String userID, String password, Subject subject)
			throws CSInternalInsufficientAttributesException, CSInternalConfigurationException
	{

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean loginOK = false;

		String tableName = (String) connectionProperties.get(Constants.TABLE_NAME);

		String userNameColumn = (String) connectionProperties.get(Constants.USER_LOGIN_ID);
		String passwordColumn = (String) connectionProperties.get(Constants.USER_PASSWORD);
		String lastNameColumn = (String) connectionProperties.get(Constants.USER_LAST_NAME);
		String firstNameColumn = (String) connectionProperties.get(Constants.USER_FIRST_NAME);
		String emailIdColumn = (String) connectionProperties.get(Constants.USER_EMAIL_ID);
		String query = "SELECT " + userNameColumn + ", " + firstNameColumn + ", " + lastNameColumn
				+ ", " + emailIdColumn + " FROM " + tableName + " WHERE " + userNameColumn
				+ " = ? " + "AND " + passwordColumn + " = ?";

		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, password);
		}
		catch (SQLException e)
		{
			throw new CSInternalConfigurationException(
					"Unable to generate query statement to validate user credentials");
		}

		try
		{
			resultSet = statement.executeQuery();
		}
		catch (SQLException e)
		{
			throw new CSInternalConfigurationException(
					"Unable to execute the query to validate user credentials");
		}
		if (resultSet != null)
		{
			try
			{
				while (resultSet.next())
				{
					String firstName = resultSet.getString(firstNameColumn);
					if (StringUtilities.isBlank(firstName))
					{
						throw new CSInternalInsufficientAttributesException(
								"User Attribute First Name not found");
					}
					else
					{
						subject.getPrincipals().add(new FirstNamePrincipal(firstName));
					}
					String lastName = resultSet.getString(lastNameColumn);
					if (StringUtilities.isBlank(lastName))
					{
						throw new CSInternalInsufficientAttributesException(
								"User Attribute Last Name not found");
					}
					else
					{
						subject.getPrincipals().add(new LastNamePrincipal(lastName));
					}
					String emailId = resultSet.getString(emailIdColumn);
					if (StringUtilities.isBlank(emailId))
					{
						throw new CSInternalInsufficientAttributesException(
								"User Attribute Email Id not found");
					}
					else
					{
						subject.getPrincipals().add(new EmailIdPrincipal(emailId));
					}

					subject.getPrincipals().add(new LoginIdPrincipal(userID));

					loginOK = true;
					break;
				}
			}
			catch (SQLException e)
			{
				throw new CSInternalConfigurationException(
						"Unable to execute the query to validate user credentials");
			}
		}
		try
		{
			if (resultSet != null)
			{
				resultSet.close();
			}
			if (statement != null)
			{
				statement.close();
			}
			if (connection != null)
			{
				connection.close();
			}
		}
		catch (SQLException sqe)
		{
			logger.debug("Authentication||" + userID
					+ "|executeQuery|Failure| Error in closing connections |" + sqe.getMessage());
		}
		logger.debug("Authentication||" + userID + "|executeQuery|Success| Login is " + loginOK
				+ " for the user " + userID + "and password " + password + "|");
		return loginOK;
	}

	/**
	 * This method will get the connection from the Connection properties parameters
	 * If the parameters contains the dataSource parameter then it will return the
	 * connection using that data source else will take the connection by normal userId
	 * & password etc. properties
	 * @param connectionProperties table containing details for establishing
	 * 			connection like the driver, the url, the user name and the
	 * 			password for the establishing the database connection. It also
	 * 			contains the actual query statement to retrieve the user record
	 * @return the connection obtained using the connectionProperties.
	 * @throws CSInternalConfigurationException exception.
	 */
	private static Connection getConnection(Hashtable connectionProperties)
			throws CSInternalConfigurationException
	{
		Connection conn = null;
		if (connectionProperties != null)
		{
			String dataSource = (String) connectionProperties.get("dataSource");
			if (dataSource == null)
			{
				// Data Source not Found Take the connection using normal DB credentials.
				String driver = (String) connectionProperties.get("driver");
				String url = (String) connectionProperties.get("url");
				String user = (String) connectionProperties.get("user");
				String passwd = (String) connectionProperties.get("passwd");

				// load the driver, if it is not loaded
				try
				{
					Class.forName(driver);
				}
				catch (ClassNotFoundException e)
				{
					throw new CSInternalConfigurationException(
							"Unable to Load Driver for Authentication Database");
				}

				// Get the connection to the database
				try
				{
					conn = DriverManager.getConnection(url, user, passwd);
				}
				catch (SQLException e)
				{
					throw new CSInternalConfigurationException(
							"Unable to obtain connection for Authentication Database");
				}
			}
			else
			{
				// Data Source  Found Take the connection using DataSource
				try
				{
					InitialContext ctx = new InitialContext();
					DataSource ds;
					ds = (DataSource) ctx.lookup(dataSource);
					conn = ds.getConnection();
				}
				catch (NamingException e)
				{
					throw new CSInternalConfigurationException("Data source " + dataSource
							+ "not found");
				}
				catch (SQLException e)
				{
					throw new CSInternalConfigurationException(
							"Unable to obtain connection for Authentication Database");
				}
			}
		}
		return conn;
	}

	/**
	 * Accepts the connection object, the query string and the user credentials
	 * and queries the database to retrieve the user record. It first prepares a
	 * statement from the connection and the query passed as parameter. It
	 * replaces the user id and password in the statement and executes the same.
	 * If a record it retrieved from the database it indicates that the user
	 * credentail provided are correct and a TRUE is returned. Finally the
	 * database resources like connection, statement etc. are freed and released
	 * @param connection the connection obtained using the connection properties
	 * 			provided in the configuration
	 * @param query the actual query statement which is used to retrieve the
	 * 			user record from the database
	 * @param userID the user entered user name provided by the calling
	 * 			application
	 * @param password the user entered password provided by the calling
	 * 			application
	 * @return TRUE if the querying is successful and the user record is
	 * 			retrieved using the provided credentials
	 * @throws CSInternalConfigurationException
	 *
	 */
	private static boolean executeQuery(Connection connection, String query, String userID,
			String password) throws CSInternalConfigurationException
	{
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean loginOK = false;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			if (resultSet != null)
			{
				while (resultSet.next())
				{
					loginOK = true;
					break;
				}
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
			throw new CSInternalConfigurationException(
					"Unable to execute the query to validate user credentials");
		}
		finally
		{
			try
			{
				if (resultSet != null)
				{
					resultSet.close();
				}
				if (statement != null)
				{
					statement.close();
				}
				if (connection != null)
				{
					connection.close();
				}
			}
			catch (SQLException sqe)
			{
				logger.debug("Authentication||" + userID
						+ "|executeQuery|Failure| Error in closing connections |"
						+ sqe.getMessage());
			}
		}
		logger.debug("Authentication||" + userID + "|executeQuery|Success| Login is " + loginOK
				+ " for the user " + userID + "and password " + password + "|");
		return loginOK;
	}
}
