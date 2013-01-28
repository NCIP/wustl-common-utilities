
package edu.wustl.common.report.bean;



import edu.wustl.common.beans.SessionDataBean;

public class ReportBean
{

	private String reportName;
	private String toDate;
	private String fromDate;
	private String condition;
	private String reportType;
	private SessionDataBean sessionDataBean;
	private String reportQuery;
	private FileDetails fileDetails;

	public String getReportName()
	{
		return reportName;
	}

	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public SessionDataBean getSessionDataBean()
	{
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean)
	{
		this.sessionDataBean = sessionDataBean;
	}

	public String getReportQuery()
	{
		return reportQuery;
	}

	public void setReportQuery(String reportQuery)
	{
		this.reportQuery = reportQuery;
	}

	public FileDetails getFileDetails()
	{
		return fileDetails;
	}

	public void setFileDetails(FileDetails fileDetails)
	{
		this.fileDetails = fileDetails;
	}

	public void setAllValues(String reportName, String fromDate, String toDate)
	{
		this.reportName = reportName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		//this.condition = hsForm.getReportConditions();
		this.condition = ReportConditions.BETWEEN.getConditionValue();

	}

}
