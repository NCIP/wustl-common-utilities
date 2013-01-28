
package edu.wustl.common.scheduler.domain;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7963371667610830688L;

	private Long id;
	protected Date startDate;
	protected Date endDate;
	protected String interval;
	protected String name;
	protected String activityStatus;

	public String getActivityStatus()
	{
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getInterval()
	{
		return interval;
	}

	public void setInterval(String interval)
	{
		this.interval = interval;
	}

}
