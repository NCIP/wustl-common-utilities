
package edu.wustl.common.report.reportBizLogic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.report.bean.ReportBean;
import edu.wustl.common.report.bean.ReportConditions;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

public class ReportBizLogic extends DefaultBizLogic
{

	protected static ReportBean repBean;

	public ReportBizLogic()
	{

	}

	public ReportBizLogic(ReportBean repBean)
	{
		this.repBean = repBean;
	}

	protected String processQueryCondition(String query)
	{
		String condition = repBean.getCondition();
		ReportConditions valueOf = ReportConditions.BETWEEN;
		String replacedQuery = "";
		switch (valueOf)
		{
			case BETWEEN :
			{
				if (repBean.getToDate() != null && repBean.getToDate() != ""
						&& (repBean.getFromDate() == null || repBean.getFromDate() == ""))
				{
					String wherePart = "and trunc(to_date(to_char(record.event_time_stamp,'MM-DD-YYYY'),'MM-DD-YYYY')) <= to_date('"
							+ repBean.getToDate() + "','MM-DD-YYYY')";
					replacedQuery = query.replaceAll("@@dateCondition@@", wherePart);

				}
				else if (repBean.getFromDate() != null && repBean.getFromDate() != ""
						&& (repBean.getToDate() == null || repBean.getToDate() == ""))
				{
					String wherePart = "and trunc(to_date(to_char(record.event_time_stamp,'MM-DD-YYYY'),'MM-DD-YYYY')) >= to_date('"
							+ repBean.getFromDate() + "','MM-DD-YYYY')";
					replacedQuery = query.replaceAll("@@dateCondition@@", wherePart);
				}
				else if (repBean.getFromDate() != null && repBean.getFromDate() != ""
						&& (repBean.getToDate() != null || repBean.getToDate() != ""))
				{
					String wherePart = "and trunc(to_date(to_char(record.event_time_stamp,'MM-DD-YYYY'),'MM-DD-YYYY')) between to_date('"
							+ repBean.getFromDate()
							+ "','MM-DD-YYYY') and to_date('"
							+ repBean.getToDate() + "','MM-DD-YYYY')";
					replacedQuery = query.replaceAll("@@dateCondition@@", wherePart);
				}
				else if (repBean.getFromDate() == null || repBean.getFromDate() == ""
						&& (repBean.getToDate() == null || repBean.getToDate() == ""))
				{
					replacedQuery = query.replaceAll("@@dateCondition@@", "");
				}

			}

		}
		return replacedQuery;
	}

	public List<Object> getReportData() throws DAOException
	{
		return null;
	}

	public List<Object> getReportData(String query) throws BizLogicException
	{
		String updatedQuery = "";
		updatedQuery = processQueryCondition(query);
		try
		{
			return getData(updatedQuery);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
	}

	protected List getData(String query) throws DAOException
	{
		JDBCDAO dao = null;
		List<Object> result = new ArrayList<Object>();
		try
		{
			dao = DAOConfigFactory.getInstance()
					.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getJDBCDAO();
			dao.openSession(null);
			result = dao.executeQuery(query, null);
		}
		finally
		{
			dao.closeSession();
		}

		return result;

	}

	/**
	 * @param dao
	 * @param reportName
	 * @return
	 * @throws DAOException
	 */
	public ResultSet getReportDetailsResult(JDBCDAO dao, String reportName) throws DAOException
	{
		ResultSet result = null;
		String query = "SELECT REPORT_TYPE,REPORT_GENERATOR,CS_ID FROM REPORT_DETAILS WHERE REPORT_NAME = '"
				+ reportName + "'";
		result = dao.getResultSet(query, null, 1);
		return result;
	}

	public List<Object> getReportNames(long csId) throws BizLogicException
	{
		List<Object> list = new ArrayList<Object>();
		if (csId != 0l)
		{
			list = getReportNamesByCsId(csId);
		}
		else
		{
			list = getSystemReportNames();
		}
		return list;
	}

	/**
	 * @param userId
	 * @return
	 * @throws BizLogicException
	 */
	public List<Object> getReportNamesByUserId(long userId, long csId) throws BizLogicException
	{
		String query = "SELECT REPORT_NAME, IDENTIFIER FROM REPORT_DETAILS WHERE USER_ID ="
			+ userId + "AND lower(REPORT_TYPE) != 'participant' and CS_ID=" + csId;
		return getReportNamesFromQuery(query);
	}

	/**
	 * @return
	 * @throws BizLogicException
	 */
	public List<Object> getSystemReportNames() throws BizLogicException
	{
		String query = "SELECT REPORT_NAME, IDENTIFIER FROM REPORT_DETAILS WHERE CS_ID IS NULL";
		return getReportNamesFromQuery(query);
	}

	/**
	 * @param csId
	 * @return
	 * @throws BizLogicException
	 */
	private List<Object> getReportNamesByCsId(long csId) throws BizLogicException
	{
		String query = "SELECT REPORT_NAME, IDENTIFIER FROM REPORT_DETAILS WHERE lower(REPORT_TYPE) != 'participant' and CS_ID =" + csId;
		return getReportNamesFromQuery(query);
	}

	/**
	 * @param query
	 * @return
	 * @throws BizLogicException
	 */
	private List<Object> getReportNamesFromQuery(String query) throws BizLogicException
	{
		List<Object> data = new ArrayList<Object>();
		try
		{
			data = getData(query);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		return data;
	}

	public List<Object> getReportDetails(String reportName) throws BizLogicException
	{
		String query = "SELECT REPORT_TYPE,REPORT_GENERATOR,CS_ID FROM REPORT_DETAILS WHERE REPORT_NAME = '"
				+ reportName + "'";
		List<Object> data = new ArrayList<Object>();
		try
		{
			data = getData(query);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		return (List) data.get(0);
	}

	public String getReportNameById(Long reportId) throws BizLogicException
	{
		String query = "SELECT REPORT_NAME FROM REPORT_DETAILS WHERE IDENTIFIER =" + reportId;
		List<Object> data = new ArrayList<Object>();
		try
		{
			data = getData(query);
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		return (String) ((List) data.get(0)).get(0);
	}
	
	public List<Object> getReportNames(long csId, Long participantId) throws BizLogicException
	{
		List<Object> list = new ArrayList<Object>();
		if (csId != 0l)
		{
			if(participantId != null)
			{				
				list = getParticipantReportNamesByCsId(csId);
			}
			else
			{
				list = getReportNamesByCsId(csId);
			}
		}
		else
		{
			list = getSystemReportNames();
		}
		return list;
	}
	
	/**
	 * @param csId
	 * @return
	 * @throws BizLogicException
	 */
	private List<Object> getParticipantReportNamesByCsId(long csId) throws BizLogicException
	{
		String query = "SELECT REPORT_NAME, IDENTIFIER FROM REPORT_DETAILS WHERE lower(REPORT_TYPE) = 'participant' and CS_ID =" + csId;
		return getReportNamesFromQuery(query);
	}


}
