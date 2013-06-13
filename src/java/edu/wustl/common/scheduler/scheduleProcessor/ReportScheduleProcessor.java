/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.scheduleProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.WordUtils;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.report.CustomReportGenerator;
import edu.wustl.common.report.ReportGenerator;
import edu.wustl.common.report.reportBizLogic.ReportBizLogic;
import edu.wustl.common.scheduler.bizLogic.ReportAuditDataBizLogic;
import edu.wustl.common.scheduler.domain.BaseSchedule;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.scheduler.util.ReportSchedulerUtil;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

public class ReportScheduleProcessor extends AbstractScheduleProcessor
{

	private static final Logger log = Logger.getCommonLogger(ReportScheduleProcessor.class);

	/* (non-Javadoc)
	 * @see main.java.scheduler.scheduleProcessors.AbstractScheduleProcessor#executeSchedule()
	 */
	synchronized public void executeSchedule() throws Exception
	{
		ReportGenerator reportGenerator = null;
		ReportBizLogic repoBiz = new ReportBizLogic();
		ReportAuditDataBizLogic repoAuditBiz = new ReportAuditDataBizLogic();
		for (Long ticketId : getTicketIdList())
		{
			ReportAuditData

			reportAuditData = (ReportAuditData) repoAuditBiz.retrieve(
					ReportAuditData.class.getName(), ticketId);
			String reportName = repoBiz.getReportNameById(reportAuditData.getReportId());
			reportGenerator = ReportGenerator.getImplObj(reportName);

			if (reportGenerator instanceof CustomReportGenerator)
			{
				reportGenerator.generateReport(ticketId,
						SchedulerDataUtility.getCustomReportParamList());
			}
			else
			{
				ReportSchedulerUtil.generateReport(reportAuditData);
			}
		}
	}

	@Override
	synchronized public void postScheduleExecution() throws Exception
	{
		mail();
		
		try
		{
			ticketIdList.clear();
		}
		catch (UnsupportedOperationException e)
		{
			ticketIdList = new ArrayList<Long>();
		}
		
	}

	/* (non-Javadoc)
	 * @see main.java.scheduler.scheduleProcessors.AbstractScheduleProcessor#mail()
	 * Mails download link if report execution is successfull and error details if execution fails.
	 */
	@Override
	public boolean mail() throws Exception
	{
		
		boolean success = true;
		try
		{
			Collection<Long> userCollection = ((BaseSchedule) scheduleObject)
					.getRecepientUserIdCollection();
			if (((BaseSchedule) scheduleObject).getIncludeMe())
			{
				userCollection.add(((BaseSchedule) scheduleObject).getOwnerId());
			}
			ReportAuditDataBizLogic reportAuditBiz = new ReportAuditDataBizLogic();
			for (Long userId : userCollection)
			{
				List<List> dataList = reportAuditBiz.getReportAuditDataListbyUser(userId,
						getTicketIdList());
				String email = ReportSchedulerUtil.getEmail(userId);
				ReportBizLogic repoBiz = new ReportBizLogic();
				StringBuilder body = new StringBuilder("");
				populateMailHeader(userId, body);
				for (List reportAuditData : dataList)
				{
					if (reportAuditData.get(1) != null)
					{
						if (((String) reportAuditData.get(1)).equalsIgnoreCase("Completed"))
						{
							populateDownloadLinkInMail(repoBiz, body, reportAuditData);
						}
						else
						{
							populateErrorMessageInMail(repoBiz, body, reportAuditData);
						}
						reportAuditBiz.setIsEmailed(Long.valueOf((String)reportAuditData.get(0)));
						//reportAuditBiz.update(reportAuditData);
					}
				}
				populateMailBodyEnding(body);
				sendMail(email, body);
			}
		}
		catch (Exception e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/**
	 * @param email
	 * @param body
	 * @throws MessagingException
	 * @throws Exception
	 */
	private boolean sendMail(String email, StringBuilder body) throws Exception
	{
		boolean success = true;
		try
		{
			SchedulerDataUtility.sendScheduleMail(
					email,
					(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
							"scheduler.mail.subject")
							+ scheduleObject.getName(), body.toString());
		}
		catch (Exception e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/**
	 * @param repoBiz
	 * @param body
	 * @param reportAuditData
	 * @throws BizLogicException
	 */
	private boolean populateErrorMessageInMail(ReportBizLogic repoBiz, StringBuilder body,
			List reportAuditData) throws BizLogicException
	{
		boolean success = true;
		try
		{
			body.append("\n").append(
					repoBiz.getReportNameById(Long.valueOf((String)reportAuditData.get(2))) + ": "
							+ "Report could not be generated, please contact the administrator.");
		}
		catch (BizLogicException e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/**
	 * @param repoBiz
	 * @param body
	 * @param reportAuditData
	 * @throws BizLogicException
	 * @throws Exception
	 */
	private boolean populateDownloadLinkInMail(ReportBizLogic repoBiz, StringBuilder body,
			List reportAuditData) throws Exception
	{
		boolean success = true;
		try
		{
			body.append("\n")
					.append(repoBiz.getReportNameById(Long.valueOf((String) reportAuditData.get(2)))
							+ ": "
							+ ReportSchedulerUtil.getFileDownloadLink((String) reportAuditData.get(0)));
		}
		catch (Exception e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/**
	 * @param userId
	 * @param body
	 * @return TODO
	 * @throws Exception
	 */
	public boolean populateMailHeader(Long userId, StringBuilder body) throws Exception
	{
		boolean success = true;
		try
		{
			String[] nameString = SchedulerDataUtility
					.getUserNamesList(new ArrayList<Long>(Arrays.asList(userId))).get(0).split(",");

			body.append("Hello " + WordUtils.capitalize(nameString[1]) + " "
					+ WordUtils.capitalize(nameString[0]) + ",\n");
			body.append((String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
					"scheduler.mail.header"));
		}
		catch (Exception e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/**
	 * @param body
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private boolean populateMailBodyEnding(StringBuilder body) throws Exception
	{
		boolean success = true;
		try
		{
			Calendar deleteDay = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(CommonServiceLocator.getInstance()
					.getDatePattern());
			deleteDay.set(
					Calendar.DATE,
					deleteDay.get(Calendar.DATE)
							+ Integer.valueOf((String) SchedulerConfigurationPropertiesHandler
									.getInstance().getProperty(
											"scheduler.cleanUp.timeInterval.days")));
			body.append(((String) SchedulerConfigurationPropertiesHandler.getInstance()
					.getProperty("scheduler.mail.end")).replace("?",
					formatter.format(new Date(deleteDay.getTimeInMillis()))));
		}
		catch (Exception e)
		{
			success = false;
			throw e;
		}
		return success;
	}

	/* (non-Javadoc)
	 * @see main.java.scheduler.scheduleProcessors.AbstractScheduleProcessor#generateTicket()
	 */
	@Override
	protected void generateTicket() throws Exception
	{
		ReportAuditData reportAuditData = new ReportAuditData();
		populateInitialAuditData(reportAuditData);
		ReportAuditDataBizLogic reportAuditBiz = new ReportAuditDataBizLogic();
		ReportBizLogic repoBiz = new ReportBizLogic();
		//one schedule can have multiple reports so we will have multiple tickets.
		for (Long reportId : ((BaseSchedule) scheduleObject).getScheduleItemIdCollection())
		{
			reportAuditData.setReportId(reportId);
			String reportName = repoBiz.getReportNameById(reportId);
			populateAuditDataWithCsId(reportAuditData, repoBiz, reportName);
			Collection<Long> recepientCollection = ((BaseSchedule) scheduleObject)
					.getRecepientUserIdCollection();
			if (((BaseSchedule) scheduleObject).getIncludeMe())
			{
				recepientCollection.add(((BaseSchedule) scheduleObject).getOwnerId());
			}
			// reports are generated for every recipient separately
			for (Long userId : recepientCollection)
			{
				reportAuditData.setId(null);
				reportAuditData.setUserId(userId);
				reportAuditBiz.insert(reportAuditData);
				addTicketId(reportAuditData.getId());
			}
		}
	}

	/**
	 * @param reportAuditData
	 * @param repoBiz
	 * @param reportName
	 * @throws DAOException
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	private void populateAuditDataWithCsId(ReportAuditData reportAuditData, ReportBizLogic repoBiz,
			String reportName) throws DAOException, SQLException, NumberFormatException
	{
		JDBCDAO dao = SchedulerDataUtility.getJDBCDAO();
		dao.openSession(null);
		ResultSet reportDetailRS = null;
		try
		{
			reportDetailRS = repoBiz.getReportDetailsResult(dao, reportName);
			if (reportDetailRS != null && reportDetailRS.next()
					&& reportDetailRS.getObject("CS_ID") != null)
			{
				//populate csid if its a study or cp report
				reportAuditData.setCsId(Long.valueOf(reportDetailRS.getObject("CS_ID").toString()));
			}
		}
		finally
		{
			if (reportDetailRS != null)
			{
				dao.closeStatement(reportDetailRS);
			}
			dao.closeSession();
		}
	}

	/**
	 * @param reportAuditData
	 */
	private void populateInitialAuditData(ReportAuditData reportAuditData)
	{
		//populate schedule id
		reportAuditData.setScheduleId(scheduleObject.getId());
		//populate is emailed
		reportAuditData.setIsEmailed(false);
		////populate report generation start and end date
		Map<String, Date> reportDurationMap = ReportSchedulerUtil.durationMap.get((scheduleObject
				.getInterval()));
		reportAuditData.setReportDurationStart(reportDurationMap.get("startDate"));
		reportAuditData.setReportDurationEnd(reportDurationMap.get("endDate"));
	}

}
