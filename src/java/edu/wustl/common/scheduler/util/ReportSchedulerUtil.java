/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import edu.wustl.common.actionForm.ReportForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.report.ReportGenerator;
import edu.wustl.common.report.bean.FileDetails;
import edu.wustl.common.report.bean.ReportBean;
import edu.wustl.common.report.bean.ReportConditions;
import edu.wustl.common.report.reportBizLogic.ReportBizLogic;
import edu.wustl.common.scheduler.bizLogic.ReportAuditDataBizLogic;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.exception.FileCleanedException;
import edu.wustl.common.scheduler.exception.NotAuthorizedToDownloadException;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.EmailDetails;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOUtility;

public class ReportSchedulerUtil
{

	/**
	 * @param emailAddress
	 * @param reportFileNameLink
	 * @return
	 * @throws MessagingException
	 */

	public static final Map<String, String> durationLiteralMap = new HashMap<String, String>()
	{

		{
			put("daily", "Last day");
			put("weekly", "Last week");
			put("monthly", "Last month");
			put("halfYearly", "Last half year");
			put("quarterly", "Last quarter");
			put("yearly", "Last year");

		}
	};

	public static final Map<String, Map<String, Date>> durationMap = new HashMap<String, Map<String, Date>>()
	{

		private static final long serialVersionUID = 4959050892081797569L;
		{

			put("daily", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = 3617663645137591425L;
				{
					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
			put("weekly", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = -7574700919738494797L;
				{

					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH) - 1);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
			put("monthly", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = -8525829445595800065L;
				{
					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
			put("quarterly", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = -1653746956874412243L;
				{
					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 3);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
			put("halfYearly", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = -2860781773167585687L;
				{
					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 6);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
			put("yearly", new HashMap<String, Date>()
			{

				private static final long serialVersionUID = 4471949936040179278L;
				{
					put("endDate", new Date());
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
					put("startDate", new Date(cal.getTimeInMillis()));
				}
			});
		}
	};

	/**
	 * @param input
	 * @param replacement
	 * @return
	 */
	public static String replaceSpCharForFile(String input, String replacement)
	{
		return input.replaceAll("[?:^/ \\ * < > | \"]", replacement);
	}

	/**
	 * @param ticketId
	 * @return
	 * @throws Exception 
	 */
	public static String getFileDownloadLink(String ticketId) throws Exception
	{
		String url = CommonServiceLocator.getInstance().getAppURL();
		if (url == null)
		{
			url = (String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
					"host.app.url");
		}
		StringBuilder downloadURL = new StringBuilder(url);

		downloadURL.append("/").append("RedirectHome.do?pageOf=pageOfDownload&file=")
				.append(ticketId);

		return downloadURL.toString();
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static String getDownloadFilePath(String fileName) throws BizLogicException
	{
		ReportAuditDataBizLogic fileBiz = new ReportAuditDataBizLogic();
		List list = fileBiz.executeQuery(
				"select fileDetail.fileName from " + ReportAuditData.class.getName()
						+ " fileDetail where fileDetail.fileName like'%" + fileName + "'", null);
		String filePath = null;
		if (list == null || list.isEmpty())
		{
		}
		else
		{
			filePath = (String) list.get(0);
		}
		return filePath;
	}

	/**
	 * @param zipName
	 * @param filePathList
	 * @throws IOException 
	 */
	public static boolean createZipFile(String zipName, List<String> filePathList)
			throws IOException
	{
		boolean success = true;
		byte[] buffer = new byte[1024];
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipName));
		zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);
		FileInputStream fileInputStream = null;

		try
		{
			for (String fileName : filePathList)
			{
				try
				{
					fileInputStream = new FileInputStream(new File(fileName));
					zipOut.putNextEntry(new ZipEntry(getFileNameFromPath(fileName)));
					int len;
					while ((len = fileInputStream.read(buffer)) > 0)
					{
						zipOut.write(buffer, 0, len);
					}
					zipOut.closeEntry();
				}
				finally
				{
					fileInputStream.close();
				}
			}
		}
		catch (IOException e)
		{
			success = false;
			throw e;
		}
		finally
		{
			zipOut.close();
			for (String fileName : filePathList)
			{
				File file = new File(fileName);
				FileUtils.forceDelete(file);
			}
		}

		return success;

	}

	/**
	 * @param path
	 * @return
	 */
	public static String getFileNameFromPath(String path)
	{
		File file = new File(path);
		return file.getName();
	}

	/**
	 * @param fileName
	 * @param userId
	 * @throws BizLogicException
	 * @throws NotAuthorizedToDownloadException
	 * @throws FileCleanedException
	 */
	public static void validateFile(String fileName, Long userId) throws BizLogicException,
			NotAuthorizedToDownloadException, FileCleanedException
	{
		ReportAuditDataBizLogic fileBiz = new ReportAuditDataBizLogic();
		@SuppressWarnings("rawtypes")
		List list = fileBiz.executeQuery(
				"select fileDetail.fileName from " + ReportAuditData.class.getName()
						+ " fileDetail where fileDetail.userId= " + userId
						+ " and fileDetail.fileName like'" + "%" + fileName + "%'", null);

		if (list == null || list.isEmpty())
		{
			throw new NotAuthorizedToDownloadException("Not authorized to download file");
		}
		else
		{
			File file = new File((String) list.get(0));
			if (!file.exists())
			{
				throw new FileCleanedException("The file has been cleaned up");
			}
		}

	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getDurationListJSON(HttpServletRequest request) throws Exception
	{
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		beans.add(new NameValueBean("Last day", "daily"));
		beans.add(new NameValueBean("Last week", "weekly"));
		beans.add(new NameValueBean("Last month", "monthly"));
		beans.add(new NameValueBean("Last quarter", "quarterly"));
		beans.add(new NameValueBean("Last half year", "halfYearly"));
		beans.add(new NameValueBean("Last year", "yearly"));

		return SchedulerDataUtility.createExtJsJson(request, beans);

	}

	/**
	 * @param itemId
	 * @return
	 * @throws DAOException
	 */
	public static Boolean isSysReport(Long itemId) throws DAOException
	{
		JDBCDAO dao = SchedulerDataUtility.getJDBCDAO();
		dao.openSession(null);
		List<Object> list = null;
		try
		{
			list = dao.executeQuery(
					"select CS_ID from REPORT_DETAILS where IDENTIFIER = " + itemId, null);

		}
		finally
		{
			dao.closeSession();
		}

		return ((List) list.get(0)).get(0).equals("");
	}

	/**
	 * @param csId
	 * @param reportId
	 * @return
	 * @throws DAOException
	 */
	public static Boolean isStudyBasedSchedule(Long csId, Long reportId) throws DAOException
	{
		JDBCDAO dao = SchedulerDataUtility.getJDBCDAO();
		dao.openSession(null);
		List<Object> list = null;
		try
		{
			list = dao.executeQuery("select IDENTIFIER from REPORT_DETAILS where IDENTIFIER = "
					+ reportId
					+ " and  IDENTIFIER in ( select IDENTIFIER from REPORT_DETAILS where lower(REPORT_TYPE) != 'participant' and CS_ID ="
					+ csId + ") ", null);
		}
		finally
		{
			dao.closeSession();
		}

		return !(list == null || list.isEmpty());
	}

	/**
	 * @param ticketId
	 * @throws BizLogicException
	 */
	public static boolean generateReport(ReportAuditData reportAuditData) throws BizLogicException
	{

		ReportAuditDataBizLogic fileBiz = new ReportAuditDataBizLogic();
		boolean success = true;
		try
		{
			reportAuditData.setJobStatus("Running");
			reportAuditData.setExecuteionStart(new Timestamp(new Date().getTime()));
			fileBiz.update(reportAuditData);
			ReportBean reportBean = new ReportBean();
			String reportName = new ReportBizLogic().getReportNameById(reportAuditData
					.getReportId());
			reportBean.setReportName(reportName);
			SimpleDateFormat formatter = new SimpleDateFormat(SchedulerConstants.DATE_FORMAT);
			reportBean.setFromDate(formatter.format(reportAuditData.getReportDurationStart()));
			reportBean.setToDate(formatter.format(reportAuditData.getReportDurationEnd()));
			reportBean.setCondition(ReportConditions.BETWEEN.getConditionValue());
			FileDetails fileDetails = Utility.generateFilePath(reportName);
			fileDetails.setFileName(fileDetails.getFileName().replace(".csv",
					"_" + reportAuditData.getId() + ".csv"));
			fileDetails.setFilePath(fileDetails.getFilePath().replace(".csv",
					"_" + reportAuditData.getId() + ".csv"));
			reportBean.setFileDetails(fileDetails);
			ReportGenerator reportGenerator = ReportGenerator.getImplObj(reportName);

			reportGenerator.generateCSV(reportBean);

			String zipFile = fileDetails.getFilePath().replace(".csv",
					SchedulerConstants.ZIP_EXTENSION);
			ReportSchedulerUtil.createZipFile(zipFile,
					new ArrayList<String>(Arrays.asList(fileDetails.getFilePath())));
			reportAuditData.setFileName(zipFile);
			reportAuditData.setJobStatus("Completed");
		}
		catch (Exception e)
		{
			reportAuditData.setErrorDescription(e.getMessage());
			reportAuditData.setJobStatus("Error");
			e.printStackTrace();
			success = false;

		}
		finally
		{
			reportAuditData.setExecuteionEnd(new Timestamp(new Date().getTime()));
			fileBiz.update(reportAuditData);
		}

		return success;
	}

	/**
	 * @param userId
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getEmail(Long userId) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException, Exception
	{
		IHostAppUserDataRetriever iHostUserRet = (IHostAppUserDataRetriever) Class.forName(
				(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
						"host.user.retrieval.implName")).newInstance();
		String email = iHostUserRet.getUserEmail(userId);
		return email;
	}

	/**
	 * @param hsForm
	 * @param sessionDataBean
	 * @return
	 * @throws ParseException
	 * @throws DAOException
	 * @throws SQLException
	 * @throws BizLogicException
	 */
	public static ReportAuditData generateTicket(ReportForm hsForm, SessionDataBean sessionDataBean)
			throws DAOException, SQLException, BizLogicException, ParseException
	{
	ReportAuditData customReportAudit = new ReportAuditData();
		customReportAudit.setUserId(sessionDataBean.getUserId());
		SimpleDateFormat formatter = new SimpleDateFormat(CommonServiceLocator.getInstance()
				.getDatePattern());
		if (hsForm.getFromDate() != null && hsForm.getFromDate() != "")
		{
			customReportAudit.setReportDurationStart(formatter.parse(hsForm.getFromDate()));
		}
		if (hsForm.getToDate() != null && hsForm.getToDate() != "")
		{
			customReportAudit.setReportDurationEnd(formatter.parse(hsForm.getToDate()));
		}
		JDBCDAO dao = SchedulerDataUtility.getJDBCDAO();
		dao.openSession(null);
		ResultSet reportDetailRS = null;
		try
		{
			ReportBizLogic repoBiz = new ReportBizLogic();
			reportDetailRS = repoBiz.getReportDetailsResult(dao, hsForm.getReportName());
			if (reportDetailRS != null && reportDetailRS.next()
					&& reportDetailRS.getObject("CS_ID") != null)
			{
				//populate csid if its a study or cp report
				customReportAudit.setCsId(Long
						.valueOf(reportDetailRS.getObject("CS_ID").toString()));
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
		ReportAuditDataBizLogic repoAuditBiz = new ReportAuditDataBizLogic();
		repoAuditBiz.insert(customReportAudit);
		return customReportAudit;
	}
}
