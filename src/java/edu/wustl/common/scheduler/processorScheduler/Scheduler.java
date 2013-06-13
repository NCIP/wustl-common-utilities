/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.processorScheduler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.wustl.common.scheduler.bizLogic.ScheduleBizLogic;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.exception.AlreadyScheduledException;
import edu.wustl.common.scheduler.exception.ScheduleHandlerNotFoundException;
import edu.wustl.common.scheduler.factory.SchedulerFactory;
import edu.wustl.common.scheduler.scheduleProcessor.AbstractScheduleProcessor;


public class Scheduler
{

	/**
	 * @throws IOException 
	 * 
	 */
	public Scheduler() throws IOException
	{
	}

	/**
	 * @param scheduleId
	 * @return
	 * @throws Exception 
	 */
	public boolean isAlreadyScheduled(Long scheduleId) throws Exception
	{
		return SchedulerFactory.getInstance().getScheduleHandlersMap().containsKey(scheduleId);
	}

	/**
	 * @param scheduler
	 * @throws Exception 
	 */
	public void schedule(AbstractScheduleProcessor scheduleProcessor, boolean forceSchedule)
			throws Exception
	{
		Long scheduleId = ((Schedule) scheduleProcessor.getScheduleObject()).getId();
		String scheduleName = ((Schedule) scheduleProcessor.getScheduleObject()).getName();
		if (forceSchedule)
		{
			if (isAlreadyScheduled(scheduleId))
			{
				unSchedule(scheduleId);
			}
			schedule(scheduleProcessor);
		}
		else
		{
			if (isAlreadyScheduled(scheduleId))
			{
				throw new AlreadyScheduledException("The scedule " + scheduleName
						+ " is already scheduled.");
			}
			else
			{
				schedule(scheduleProcessor);
			}
		}
	}

	/**
	 * @param scheduleProcessor
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private void schedule(AbstractScheduleProcessor scheduleProcessor) throws NumberFormatException, Exception
	{
		String interval = ((Schedule) scheduleProcessor.getScheduleObject()).getInterval();
		Date startDate = ((Schedule) scheduleProcessor.getScheduleObject()).getStartDate();
		ScheduleBizLogic schedBizLogic = new ScheduleBizLogic();
		//getStartDelayFromStartTime
		ScheduledFuture schedHandler = SchedulerFactory
				.getInstance()
				.getSchedulerInstance()
				.scheduleAtFixedRate(scheduleProcessor,
						schedBizLogic.getStartDelayFromStartTime(startDate),
						schedBizLogic.getLiteralInterval(interval), TimeUnit.SECONDS);
		SchedulerFactory.getInstance().addHandler(
				((Schedule) scheduleProcessor.getScheduleObject()).getId(), schedHandler);

	}

	/**
	 * @param scheduleId
	 * @throws Exception 
	 */
	public void unSchedule(Long scheduleId) throws Exception
	{
		if (isAlreadyScheduled(scheduleId))
		{
			SchedulerFactory.getInstance().removeHandler(scheduleId);
		}
		else
		{
			throw new ScheduleHandlerNotFoundException("Schedule with ID " + scheduleId
					+ " has not been scheduled.");
		}
	}

}
