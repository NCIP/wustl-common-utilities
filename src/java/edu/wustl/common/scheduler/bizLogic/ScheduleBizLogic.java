/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.bizLogic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.BaseSchedule;
import edu.wustl.common.scheduler.domain.ReportSchedule;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.processorScheduler.Scheduler;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.scheduler.scheduleProcessor.AbstractScheduleProcessor;
import edu.wustl.common.scheduler.scheduleProcessor.FilesCleanupProcessor;
import edu.wustl.common.scheduler.scheduleProcessor.ReportScheduleProcessor;
import edu.wustl.common.scheduler.util.ReportSchedulerUtil;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class ScheduleBizLogic extends DefaultBizLogic
{

	/**
	 * @param appName
	 */
	public ScheduleBizLogic()
	{
		super(CommonServiceLocator.getInstance().getAppName());
	}

	/**
	 * @param scheduleType
	 * @param ownerId 
	 * @param isSysDashboard TODO
	 * @param id TODO
	 * @return
	 * @throws DAOException
	 * @throws BizLogicException
	 */
	public List<Schedule> getSchedulesByOwner(String scheduleType, Long ownerId)
			throws DAOException, BizLogicException
	{
		/*return list;*/

		@SuppressWarnings("unchecked")
		List<Schedule> scheduleList = retrieve(scheduleType);
		List<Schedule> ownerBasedScheduleList = new ArrayList<Schedule>();
		for (Schedule schedule : scheduleList)
		{
			if (((BaseSchedule) schedule).getOwnerId().equals(ownerId)
					|| ((BaseSchedule) schedule).getRecepientUserIdCollection().contains(ownerId))
			{
				if (schedule.getActivityStatus().equals("Active"))
				{
					ownerBasedScheduleList.add(schedule);
				}
			}
		}

		return ownerBasedScheduleList;
	}

	/**
	 * @param scheduleType
	 * @param id
	 * @return
	 * @throws DAOException
	 * @throws BizLogicException
	 */
	public Object getSchedulesByTypeAndId(String scheduleType, long id) throws DAOException,
			BizLogicException
	{
		return retrieve(scheduleType, id);
	}

	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		return true;
	}

	/**
	 * @param scheduleDomainInterval
	 * @return interval in seconds
	 */
	public static long getLiteralInterval(String scheduleDomainInterval)
	{
		Long literalInterval = (long) 0;

		if (scheduleDomainInterval.equalsIgnoreCase(SchedulerConstants.DAILY_INTERVAL))
		{
			literalInterval = (long) 24 * 60 * 60;
		}
		else if (scheduleDomainInterval.equalsIgnoreCase(SchedulerConstants.WEEKLY_INTERVAL))
		{
			literalInterval = (long) 24 * 60 * 60 * 7;
		}
		else if (scheduleDomainInterval.equalsIgnoreCase(SchedulerConstants.MONTHLY_INTERVAL))
		{
			literalInterval = (long) 24 * 60 * 60 * 30;
		}
		else if (scheduleDomainInterval.equalsIgnoreCase(SchedulerConstants.QUARTERLY_INTERVAL))
		{
			literalInterval = (long) 24 * 60 * 60 * 90;
		}
		else if (scheduleDomainInterval.equalsIgnoreCase(SchedulerConstants.YEARLY_INTERVAL))
		{
			literalInterval = (long) 24 * 60 * 60 * 365;
		}

		return literalInterval;
	}

	/**
	 * @param startDate
	 * @return
	 * @throws Exception 
	 */
	public long getStartDelayFromStartTime(Date startDate) throws Exception
	{

		int hrs = Integer.parseInt(((String) SchedulerConfigurationPropertiesHandler.getInstance()
				.getProperty(SchedulerConstants.EXECUTION_TIME_HRS)));
		int min = Integer.parseInt(((String) SchedulerConfigurationPropertiesHandler.getInstance()
				.getProperty(SchedulerConstants.EXECUTION_TIME_MIN)));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hrs);
		cal.set(Calendar.MINUTE, min);
		Date date = new Date();
		Long delay = (long) 0;
		if (cal.getTimeInMillis() > date.getTime())
		{
			delay = (cal.getTimeInMillis() - date.getTime()) / 1000;
		}
		else
		{
			cal.add(Calendar.DAY_OF_MONTH,1);
			delay = (cal.getTimeInMillis() - date.getTime()) / 1000;
		}

		if (startDate != null)
		{
			if (!(startDate.getTime() < (new Date().getTime())))
			{
				delay = (startDate.getTime() - new Date().getTime()) / 1000;
			}
		}
		return delay;
	}

	/**
	 * @param schedule
	 * @throws Exception 
	 */
	public void insertAndSchedule(Schedule schedule) throws Exception
	{
		
		if (schedule.getId() == null)
		{
			insert(schedule);
		}
		else
		{
			update(schedule);
		}
		schedule(getProcessorForSchedule(schedule));
	}

	/**
	 * @param processor
	 * @throws Exception 
	 */
	public void schedule(AbstractScheduleProcessor processor) throws Exception
	{
		Scheduler scheduler = new Scheduler();
		scheduler.schedule(processor, true);
	}

	/**
	 * @param id
	 * @throws BizLogicException
	 */
	public void markDeleted(Long id) throws BizLogicException
	{
		Schedule schedule = (Schedule) retrieve(Schedule.class.getName(), id);
		schedule.setActivityStatus("Deleted");
		update(schedule);
		
	}

	/**
	 * @param scheduleList
	 * @return
	 * @throws DAOException 
	 */
	public List<Schedule> filterSchedules(List<Schedule> scheduleList, Boolean isSystemDashboard,
			Long csId) throws DAOException
	{
		List<Schedule> ownerBasedScheduleList = new ArrayList<Schedule>();
		for (Schedule sched : scheduleList)
		{
			if (sched instanceof ReportSchedule)
			{
				if (isSystemDashboard)
				{
					boolean isSysReport = false;
					for (Long itemId : ((BaseSchedule) sched).getScheduleItemIdCollection())
					{
						if (ReportSchedulerUtil.isSysReport(itemId))
						{
							isSysReport = true;	
						}
					}
					if (isSysReport)
					{
						ownerBasedScheduleList.add(sched);
					}
				}
				else
				{
					boolean isStudyReport = false;
					for (Long itemId : ((BaseSchedule) sched).getScheduleItemIdCollection())
					{
						if (ReportSchedulerUtil.isStudyBasedSchedule(csId, itemId))
						{
							isStudyReport = true;
						}
					}
					if (isStudyReport)
					{
						ownerBasedScheduleList.add(sched);
					}
				}
			}
		}

		return ownerBasedScheduleList;
	}

	/**
	 * @throws Exception 
	 */
	public void scheduleOnStartUp() throws Exception
	{
		String[] scheduleTypes = ((String) SchedulerConfigurationPropertiesHandler.getInstance()
				.getProperty(SchedulerConstants.SCHEDULE_TYPES)).split(",");
		for (String scheduleType : scheduleTypes)
		{
			List<Schedule> list = executeQuery("from " + scheduleType
					+ " sched where sched.activityStatus='Active'", null);
			for (Schedule scheduleObject : list)
			{
				schedule(getProcessorForSchedule(scheduleObject));
			}
		}
		schedule(new FilesCleanupProcessor());
	}

	/**
	 * @param schedule
	 * @return
	 */
	public AbstractScheduleProcessor getProcessorForSchedule(Schedule schedule)
	{
		AbstractScheduleProcessor schedProc = null;
		if (schedule instanceof ReportSchedule)
		{
			schedProc = new ReportScheduleProcessor();
			schedProc.setScheduleObject(schedule);
		}
		return schedProc;
	}
}
