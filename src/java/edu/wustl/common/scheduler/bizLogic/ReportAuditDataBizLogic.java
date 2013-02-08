
package edu.wustl.common.scheduler.bizLogic;

import java.util.Collection;
import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;

public class ReportAuditDataBizLogic extends DefaultBizLogic
{

	public ReportAuditDataBizLogic()
	{
		super(CommonServiceLocator.getInstance().getAppName());
	}

	/**
	 * @param userId
	 * @return
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public List getReportAuditDataListbyUser(Long userId, Collection<Long> ticketIdCollection)
			throws BizLogicException, DAOException
	{
		List data = null;
		JDBCDAO jdbcDAO = null;
		try
		{
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(getAppName());
			jdbcDAO = daoFactory.getJDBCDAO();
			jdbcDAO.openSession(null);
			data = jdbcDAO
					.executeQuery("select IDENTIFIER, EXEC_STATUS, REPORT_ID from REPORT_JOB_DETAILS where USER_ID = "
							+ userId
							+ " and IDENTIFIER in "
							+ SchedulerDataUtility
									.getQueryInClauseStringFromIdList(ticketIdCollection));
		}
		finally
		{
			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}
		}
		return data;
	}

	/**
	 * @param reportId
	 * @throws DAOException
	 */
	public void setIsEmailed(Long reportId) throws DAOException
	{
		JDBCDAO jdbcDAO = null;
		try
		{
			IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(getAppName());
			jdbcDAO = daoFactory.getJDBCDAO();
			jdbcDAO.openSession(null);
			DAOUtility daoUtil = DAOUtility.getInstance();
			daoUtil.beginTransaction();
			jdbcDAO.executeUpdate("update REPORT_JOB_DETAILS set IS_EMAILED = 1 where IDENTIFIER = "
					+ reportId);
			jdbcDAO.commit();
			daoUtil.commitTransaction();
		}
		finally
		{
			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}
		}
	}

	/**
	 * @return
	 * @throws BizLogicException
	 */
	@SuppressWarnings("unchecked")
	public List<ReportAuditData> getFileDetailsList() throws BizLogicException
	{
		ColumnValueBean colVal = new ColumnValueBean("jobStatus", "Completed");
		return retrieve(ReportAuditData.class.getName(), colVal);
	}

	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		return true;
	}

}
