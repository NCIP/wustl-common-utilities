/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.report;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.datahandler.AbstractDataHandler;
import edu.wustl.common.datahandler.DataHandlerFactory;
import edu.wustl.common.datahandler.DataHandlerParameter;
import edu.wustl.common.datahandler.HandlerTypeEnum;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.report.bean.ReportBean;
import edu.wustl.common.report.reportBizLogic.ReportBizLogic;
import edu.wustl.dao.exception.DAOException;

/**
 * @author root
 *
 */
/**
 * @author root
 *
 */
/**
 * @author root
 *
 */
public abstract class ReportGenerator
{

	public static final String HEALTH_STATUS_REPORT = "Enrollment Tracking Report";
	public static final String STUDIES_NAVIGATION_ENROLLMENT_REPORT = "Studies Navigation Enrollment Report";
	public static final String SERVICES_REPORT = "Services Report";
	public static final String DEMOGRAPHICS_REPORT = "Demographics Report";
	public static final String FLOWCHART = "Flowchart";

	public static final String STUDY_NAVGATION_ENROLLMENT_REPORT_QUERY = "StudiesNavigationEnrollmentReportQuery";
	public static final String HEALTH_STATUS_CS_REPORT_QUERY = "";
	public static final String HEALTH_STATUS_NAV_ENR_REPORT_QUERY = "";
	public static final String SERVICE_REPORT_QUERY = "ServiceReportQuery";
	public static final String REGGISTERED_COUNT_QUERY = "HSRegisteredParticipantCountQuery";
	public static final String SERVICES_COUNT_QUERY = "ServiceCountQuery";
	public static final String ENROLLMENT_TRACKINE_REPORT_QUERY = "EnrollmentTrackingReportQuery";
	public static final String ENROLLMENT_TRACKINE_REPORT_CS_QUERY = "EnrollmentTrackingReportCSQuery";

	public static ReportGenerator getImplObj(String reportName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, BizLogicException
	{
		ReportBizLogic repLogic = new ReportBizLogic();

		List<Object> reportDetails = repLogic.getReportDetails(reportName);
		if (reportDetails.get(0).toString().equals("custom") || reportDetails.get(0).toString().equals("participant"))
		{
			return new CustomReportGenerator(reportDetails.get(1).toString());
		}
		else
		{
			return (ReportGenerator) Class.forName(reportDetails.get(1).toString()).newInstance();
		}

	}

	public void generateCSV(ReportBean repoBean) throws IOException, IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException, DAOException,
			BizLogicException
	{
		DataHandlerParameter dataParameter = new DataHandlerParameter();
		AbstractDataHandler csvdataHandler = DataHandlerFactory.getDataHandler(HandlerTypeEnum.CSV,
				dataParameter, repoBean.getFileDetails().getFilePath());
		csvdataHandler.openFile();
		try
		{
			getReportData(csvdataHandler, repoBean);
		}
		finally
		{
			csvdataHandler.closeFile();
		}
	}

	protected List<Object> getColumnNames(ReportBean repoBean)
	{
		return null;

	}

	protected void getReportData(AbstractDataHandler handler, ReportBean repoBean)
			throws IOException, DAOException, BizLogicException
	{
		List<Object> paramList = new ArrayList<Object>();
		paramList.add("Report Name:");
		paramList.add(repoBean.getReportName());
		handler.appendData(paramList);

		paramList.clear();
		paramList.add("Exported On:");
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Calendar cal = Calendar.getInstance();
		paramList.add(dateFormat.format(cal.getTime()));
		handler.appendData(paramList);

		paramList.clear();
		if (repoBean.getSessionDataBean() != null)
		{
			String exportedBy = repoBean.getSessionDataBean().getLastName() + ",  "
					+ repoBean.getSessionDataBean().getFirstName();
			paramList.add("Exported By:");
			paramList.add(exportedBy);

		}
		handler.appendData(paramList);

		paramList.clear();
		paramList.add("Duration:");
		String duration = "";
		if ((repoBean.getFromDate() != null && !repoBean.getToDate().equals(""))
				&& (repoBean.getToDate() != null && !repoBean.getToDate().equals("")))
			duration = repoBean.getFromDate() + "to " + repoBean.getToDate();
		else if ((repoBean.getFromDate() != null && !repoBean.getFromDate().equals(""))
				&& (repoBean.getToDate() == null || repoBean.getToDate().equals("")))
			duration = "From: " + repoBean.getFromDate();
		else if ((repoBean.getFromDate() == null || repoBean.getFromDate().equals(""))
				&& (repoBean.getToDate() != null && !repoBean.getToDate().equals("")))
			duration = "Up To : " + repoBean.getToDate();
		else if ((repoBean.getFromDate() == null || repoBean.getFromDate().equals(""))
				&& (repoBean.getToDate() == null || repoBean.getToDate().equals("")))
			duration = "Not Specified";
		paramList.add(duration);
		handler.appendData(paramList);

		addEmptyRow(handler);
		addEmptyRow(handler);
	}

	protected void addEmptyRow(AbstractDataHandler handler) throws IOException
	{
		List<Object> emptyRow = new ArrayList<Object>();
		handler.appendData(emptyRow);

	}

	public static List<NameValueBean> getReportNames(String csID) throws BizLogicException
	{
		ReportBizLogic repoLogic = new ReportBizLogic();
		Long csId = 0l;
		if(csID!=null || csID!="")
		{
		 csId = Long.valueOf(csID);
		}
		List<Object> reportNames = repoLogic.getReportNames(csId);
		return getReportNamesBeans(reportNames);

	}
	
	public static List<NameValueBean> getSystemReportNames() throws BizLogicException
	{
		ReportBizLogic repoLogic = new ReportBizLogic();
		List<Object> reportNames = repoLogic.getSystemReportNames();
		return getReportNamesBeans(reportNames);

	}
	
	
	public static List<NameValueBean> getReportNamesForUSer(Long csId, Long userId) throws BizLogicException
	{
		ReportBizLogic repoLogic = new ReportBizLogic();
		
		List<Object> reportNames = repoLogic.getReportNamesByUserId(userId,csId);
		return getReportNamesBeans(reportNames);

	}

	/**
	 * @param reportNames
	 * @return
	 */
	private static List<NameValueBean> getReportNamesBeans(List<Object> reportNames)
	{
		List<NameValueBean> nameList = new ArrayList<NameValueBean>();

		for (Object reportName : reportNames)
		{
			NameValueBean bean = new NameValueBean();
			bean.setName(((List)reportName).get(0));
			bean.setValue(((List)reportName).get(0));
			nameList.add(bean);

		}
		return nameList;
	}
	
	public static List<NameValueBean> getReportNames(String csID, String participantId) throws BizLogicException
	{
		ReportBizLogic repoLogic = new ReportBizLogic();
		Long csId = 0l;
		if(csID!=null || csID!="")
		{
			csId = Long.valueOf(csID);
		}
		List<Object> reportNames;
		if(participantId != null)
		{
			reportNames = repoLogic.getReportNames(csId, Long.valueOf(participantId));
		}
		else
		{
			reportNames = repoLogic.getReportNames(csId, null);
		}
		return getReportNamesBeans(reportNames);

	}
	
	
	/**
	 * @param ticketId
	 * @param userName TODO
	 * @param password TODO
	 * @throws Exception
	 */
	public abstract void generateReport(Long ticketId, List<String> dbDetails) throws Exception;

}
