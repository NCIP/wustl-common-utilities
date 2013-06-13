/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ReportAuditData implements Serializable
{

	private static final long serialVersionUID = 7990113772791309646L;

	private Long id;
	protected Long scheduleId;
	protected Long reportId;
	protected Long userId;
	protected Long csId;
	protected String fileName;
	protected Timestamp executeionStart;
	protected Timestamp executeionEnd;
	protected String jobStatus;
	protected Boolean isEmailed;
	protected String errorDescription;
	protected Long downloadCount;
	protected Date reportDurationStart;
	protected Date reportDurationEnd;

	
	public String getJobStatus()
	{
		return jobStatus;
	}

	
	public void setJobStatus(String jobStatus)
	{
		this.jobStatus = jobStatus;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getScheduleId()
	{
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId)
	{
		this.scheduleId = scheduleId;
	}

	public Long getReportId()
	{
		return reportId;
	}

	public void setReportId(Long reportId)
	{
		this.reportId = reportId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getCsId()
	{
		return csId;
	}

	public void setCsId(Long csId)
	{
		this.csId = csId;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public Timestamp getExecuteionStart()
	{
		return executeionStart;
	}

	public void setExecuteionStart(Timestamp executeionStart)
	{
		this.executeionStart = executeionStart;
	}

	public Timestamp getExecuteionEnd()
	{
		return executeionEnd;
	}

	public void setExecuteionEnd(Timestamp executeionEnd)
	{
		this.executeionEnd = executeionEnd;
	}

	public Boolean getIsEmailed()
	{
		return isEmailed;
	}

	public void setIsEmailed(Boolean isEmailed)
	{
		this.isEmailed = isEmailed;
	}

	public String getErrorDescription()
	{
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription)
	{
		this.errorDescription = errorDescription;
	}

	public Long getDownloadCount()
	{
		return downloadCount;
	}

	public void setDownloadCount(Long downloadCount)
	{
		this.downloadCount = downloadCount;
	}

	public Date getReportDurationStart()
	{
		return reportDurationStart;
	}

	public void setReportDurationStart(Date reportDurationStart)
	{
		this.reportDurationStart = reportDurationStart;
	}

	public Date getReportDurationEnd()
	{
		return reportDurationEnd;
	}

	public void setReportDurationEnd(Date reportDurationEnd)
	{
		this.reportDurationEnd = reportDurationEnd;
	}

}
