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
 *
 */

package edu.wustl.common.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wustl.common.domain.LoginDetails;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author suhas_khot
 *
 */
public class LoginAuditManager
{

	/**
	 * Gets the login audit event details.
	 *
	 * @param userLoginId the user login id
	 * @param isLoginSuccessful is login successful
	 * @param maxResults the max results
	 * @param sinceDate the since date
	 *
	 * @return the login audit event details
	 *
	 * @throws DAOException the DAO exception
	 */
	private List<LoginDetails> getLoginAuditEventDetails(Long userLoginId, int maxResults,
			Date sinceDate, Boolean isLoginSuccessful, String loginName) throws DAOException
	{
		StringBuffer queryName = new StringBuffer(500);
		queryName
				.append("select new edu.wustl.common.domain.LoginDetails(loginAudit.userLoginId, loginAudit.sourceId, loginAudit.ipAddress, loginAudit.isLoginSuccessful, loginAudit.loginName, loginAudit.timestamp) from edu.wustl.common.domain.LoginEvent loginAudit");
		List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
		queryName.append(appendWhereClause(userLoginId, sinceDate, isLoginSuccessful, loginName,
				columnValueBeans));
		queryName.append(" order by loginAudit.timestamp desc");
		DAOUtility daoUtility = DAOUtility.getInstance();
		HibernateDAO hibernateDao = daoUtility.getHibernateDAO(null);
		List<LoginDetails> loginDetails = null;
		if (maxResults > 0)
		{
			loginDetails = hibernateDao.executeQuery(queryName.toString(), columnValueBeans,
					maxResults);
		}
		else
		{

			loginDetails = hibernateDao.executeQuery(queryName.toString(), columnValueBeans);
		}
		daoUtility.closeHibernateDAO(hibernateDao);
		return loginDetails;
	}

	/**
	 * Checks if is where clause present.
	 *
	 * @param userLoginId the user login id
	 * @param sinceDate the since date
	 * @param isLoginSuccessful the is login successful
	 * @param loginName the login name
	 *
	 * @return true, if is where clause present
	 */
	private String appendWhereClause(Long userLoginId, Date sinceDate, Boolean isLoginSuccessful,
			String loginName, List<ColumnValueBean> columnValueBeans)
	{
		StringBuffer appendWhereClause = new StringBuffer();
		if (userLoginId == null && sinceDate == null && isLoginSuccessful == null
				&& loginName == null)
		{
			appendWhereClause.append("");
		}
		else
		{
			appendWhereClause.append(" where");
			appendWhereColumn(isLoginSuccessful, "loginAudit.isLoginSuccessful", columnValueBeans,
					appendWhereClause);
			appendWhereColumn(userLoginId, "loginAudit.userLoginId", columnValueBeans,
					appendWhereClause);
			appendWhereColumn(sinceDate, "loginAudit.timestamp", columnValueBeans,
					appendWhereClause);
			appendWhereColumn(loginName, "loginAudit.loginName", columnValueBeans,
					appendWhereClause);
		}
		return appendWhereClause.toString();
	}

	/**
	 * Append where column.
	 *
	 * @param columnValueBeans the column value beans
	 * @param appendWhereClause the append where clause
	 * @param columnName the column name
	 */
	private void appendWhereColumn(Object columnValue, String columnName,
			List<ColumnValueBean> columnValueBeans, StringBuffer appendWhereClause)
	{
		if (columnValue != null)
		{
			if ("where".equalsIgnoreCase(appendWhereClause.toString().trim()))
			{
				appendWhereClause.append(" ");
				appendWhereClause.append(columnName);
				appendEquals(columnName, appendWhereClause);
				appendWhereClause.append("?");
			}
			else
			{
				appendWhereClause.append(" and ");
				appendWhereClause.append(columnName);
				appendEquals(columnName, appendWhereClause);
				appendWhereClause.append("?");
			}
			columnValueBeans.add(new ColumnValueBean(columnValue));
		}
	}

	/**
	 * Append equals.
	 *
	 * @param columnName the column name
	 * @param appendWhereClause the append where clause
	 */
	private void appendEquals(String columnName, StringBuffer appendWhereClause)
	{
		if ("loginAudit.timestamp".equalsIgnoreCase(columnName))
		{
			appendWhereClause.append(">=");
		}
		else
		{
			appendWhereClause.append("=");
		}
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param userLoginName the user login name
	 *
	 * @return the last successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public LoginDetails getLastSuccessfulLoginDetails(String userLoginName) throws DAOException
	{
		LoginDetails loginDetails = null;
		List<LoginDetails> loginAuditDetails = getLoginAuditEventDetails(null, 0, null, true,
				userLoginName);
		if (loginAuditDetails != null && !loginAuditDetails.isEmpty())
		{
			loginDetails = loginAuditDetails.iterator().next();
		}
		return loginDetails;
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param userLoginName the user login name
	 *
	 * @return the last un successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public LoginDetails getLastUnSuccessfulLoginDetails(String userLoginName) throws DAOException
	{
		LoginDetails loginDetails = null;
		List<LoginDetails> loginAuditDetails = getLoginAuditEventDetails(null, 0, null, false,
				userLoginName);
		if (loginAuditDetails != null && !loginAuditDetails.isEmpty())
		{
			loginDetails = loginAuditDetails.iterator().next();
		}
		return loginDetails;
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param count the count
	 * @param userLoginName the user login name
	 *
	 * @return the last successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getLastSuccessfulLoginDetails(String userLoginName, int count)
			throws DAOException
	{
		return getLoginAuditEventDetails(null, count, null, true, userLoginName);
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param count the count
	 * @param userLoginName the user login name
	 *
	 * @return the last un successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getLastUnSuccessfulLoginDetails(String userLoginName, int count)
			throws DAOException
	{
		return getLoginAuditEventDetails(null, count, null, false, userLoginName);
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param since the since
	 * @param userLoginName the user login name
	 *
	 * @return the last successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getLastSuccessfulLoginDetails(String userLoginName, Date since)
			throws DAOException
	{
		return getLoginAuditEventDetails(null, 0, since, true, userLoginName);
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param since the since
	 * @param userLoginName the user login name
	 *
	 * @return the last un successful login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getLastUnSuccessfulLoginDetails(String userLoginName, Date since)
			throws DAOException
	{
		return getLoginAuditEventDetails(null, 0, since, false, userLoginName);
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param userLoginId the user login id
	 *
	 * @return the last successful login details
	 * @throws DAOException
	 */
	public LoginDetails getLastSuccessfulLoginDetails(Long userLoginId) throws DAOException
	{
		LoginDetails loginDetails = null;
		List<LoginDetails> loginAuditDetails = getLoginAuditEventDetails(userLoginId, 0, null,
				true, null);
		if (loginAuditDetails != null && !loginAuditDetails.isEmpty())
		{
			loginDetails = loginAuditDetails.iterator().next();
		}
		return loginDetails;
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param userLoginId the user login id
	 *
	 * @return the last un successful login details
	 * @throws DAOException
	 */
	public LoginDetails getLastUnSuccessfulLoginDetails(Long userLoginId) throws DAOException
	{
		LoginDetails loginDetails = null;
		List<LoginDetails> loginAuditDetails = getLoginAuditEventDetails(userLoginId, 0, null,
				false, null);
		if (loginAuditDetails != null && !loginAuditDetails.isEmpty())
		{
			loginDetails = loginAuditDetails.iterator().next();
		}
		return loginDetails;
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param userLoginId the user login id
	 * @param count the count
	 *
	 * @return the last successful login details
	 * @throws DAOException
	 */
	public List<LoginDetails> getLastSuccessfulLoginDetails(Long userLoginId, int count)
			throws DAOException
	{
		return getLoginAuditEventDetails(userLoginId, count, null, true, null);
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param userLoginId the user login id
	 * @param count the count
	 *
	 * @return the last un successful login details
	 * @throws DAOException
	 */
	public List<LoginDetails> getLastUnSuccessfulLoginDetails(Long userLoginId, int count)
			throws DAOException
	{
		return getLoginAuditEventDetails(userLoginId, count, null, false, null);
	}

	/**
	 * Gets the last successful login details.
	 *
	 * @param userLoginId the user login id
	 * @param since the since
	 *
	 * @return the last successful login details
	 * @throws DAOException
	 */
	public List<LoginDetails> getLastSuccessfulLoginDetails(Long userLoginId, Date since)
			throws DAOException
	{
		return getLoginAuditEventDetails(userLoginId, 0, since, true, null);
	}

	/**
	 * Gets the last un successful login details.
	 *
	 * @param userLoginId the user login id
	 * @param since the since
	 *
	 * @return the last un successful login details
	 * @throws DAOException
	 */
	public List<LoginDetails> getLastUnSuccessfulLoginDetails(Long userLoginId, Date since)
			throws DAOException
	{
		return getLoginAuditEventDetails(userLoginId, 0, since, false, null);
	}

	/**
	 * Gets the all login details.
	 *
	 * @return the all login details
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getAllLoginDetails() throws DAOException
	{
		return getLoginAuditEventDetails(null, 0, null, null, null);
	}

	/**
	 * Gets the all login details for user.
	 *
	 * @param userLoginId the user login id
	 * @param maxResults the max results
	 * @param sinceDate the since date
	 *
	 * @return the all login details for user
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getAllLoginDetailsForUser(Long userLoginId, int maxResults,
			Date sinceDate) throws DAOException
	{
		return getLoginAuditEventDetails(userLoginId, maxResults, sinceDate, null, null);
	}

	/**
	 * Gets the all login details for user.
	 *
	 * @param maxResults the max results, set to '0' to get all results
	 * @param sinceDate the since date, set to null to get all results
	 * @param loginName the login name
	 *
	 * @return the all login details for user
	 *
	 * @throws DAOException the DAO exception
	 */
	public List<LoginDetails> getAllLoginDetailsForUser(String loginName, int maxResults,
			Date sinceDate) throws DAOException
	{
		return getLoginAuditEventDetails(null, maxResults, sinceDate, null, loginName);
	}
}
