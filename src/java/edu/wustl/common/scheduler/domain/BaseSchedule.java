
package edu.wustl.common.scheduler.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

public class BaseSchedule extends Schedule
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3993888125380366698L;

	protected Collection<Long> recepientUserIdCollection = new LinkedHashSet<Long>();
	protected Long ownerId;
	protected String comments;
	protected Collection<Long> scheduleItemIdCollection = new LinkedHashSet<Long>();
	protected Boolean includeMe;

	public Boolean getIncludeMe()
	{
		return includeMe;
	}

	public void setIncludeMe(Boolean includeMe)
	{
		this.includeMe = includeMe;
	}

	public Collection<Long> getScheduleItemIdCollection()
	{
		return scheduleItemIdCollection;
	}

	public void setScheduleItemIdCollection(Collection<Long> scheduleItemIdCollection)
	{
		this.scheduleItemIdCollection = scheduleItemIdCollection;
	}

	public Collection<Long> getRecepientUserIdCollection()
	{
		return recepientUserIdCollection;
	}

	public void setRecepientUserIdCollection(Collection<Long> recepientUserIdCollection)
	{
		this.recepientUserIdCollection = recepientUserIdCollection;
	}

	public Long getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(Long ownerId)
	{
		this.ownerId = ownerId;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

}
