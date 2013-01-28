
package edu.wustl.common.scheduler.domain;

public class ReportSchedule extends BaseSchedule
{

	private static final long serialVersionUID = -7380819609285659729L;

	private String duration;

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

}
