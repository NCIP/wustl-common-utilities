
package edu.wustl.common.action;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.ReportForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.report.CustomReportGenerator;
import edu.wustl.common.report.ReportGenerator;
import edu.wustl.common.report.bean.FileDetails;
import edu.wustl.common.report.bean.ReportBean;
import edu.wustl.common.scheduler.bizLogic.ReportAuditDataBizLogic;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.scheduler.util.ReportSchedulerUtil;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.Utility;

public class ReportAction extends SecureAction
{

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ReportForm hsForm = (ReportForm) form;
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				"sessionData");

		String reportName = hsForm.getReportName();
		if (reportName != null)
		{
			ReportGenerator reportGenerator = ReportGenerator.getImplObj(reportName);
			String fileName = "";
			String filePath = "";
			if (reportGenerator instanceof CustomReportGenerator)
			{
				ReportAuditData customReportAudit = ReportSchedulerUtil.generateTicket(hsForm,
						sessionDataBean);

				List<String> customParams = SchedulerDataUtility.getCustomReportParamList();
				if(request.getParameter("participantId")!=null && !"".equalsIgnoreCase(request.getParameter("participantId")))
				{
					customParams.add(request.getParameter("participantId"));
				}
				reportGenerator.generateReport(customReportAudit.getId(),
						customParams);
				customReportAudit = (ReportAuditData) new ReportAuditDataBizLogic().retrieve(
						ReportAuditData.class.getName(), customReportAudit.getId());
				if (customReportAudit.getJobStatus() != null
						&& customReportAudit.getJobStatus().equalsIgnoreCase("Completed"))
				{
					if (customReportAudit.getFileName() != null)
					{
						File file = new File(customReportAudit.getFileName());
						fileName = file.getName();
						filePath = file.getPath();
					}
				}
			}
			else
			{
				FileDetails fileDetails = Utility.generateFilePath(reportName);
				fileDetails.setFileName(fileDetails.getFileName()
						+ edu.wustl.common.util.global.Constants.CSV_FILE_EXTENTION);
				fileDetails.setFilePath(fileDetails.getFilePath()
						+ edu.wustl.common.util.global.Constants.CSV_FILE_EXTENTION);
				ReportBean reportBean = new ReportBean();
				reportBean.setSessionDataBean(sessionDataBean);
				reportBean.setAllValues(hsForm.getReportName(), hsForm.getFromDate(),
						hsForm.getToDate());
				reportBean.setFileDetails(fileDetails);
				reportGenerator.generateCSV(reportBean);
				fileName = fileDetails.getFileName();
				filePath = fileDetails.getFilePath();
			}
			SendFile.sendFileToClient(response, filePath, fileName, "application/download");
		}
		return null;
	}

}
