/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.scheduleProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wustl.common.scheduler.bizLogic.ScheduleBizLogic;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.ReportSchedule;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.processorScheduler.Scheduler;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;


public class StartUpScheduleProcessor
{

	/**
	 * @throws Exception 
	 */
	public void scheduleSavedSchedules() throws Exception
	{

		List<Schedule> scheduleList = new ArrayList<Schedule>();
		populateSchedules(scheduleList);
		scheduleSchedulesList(scheduleList);
		//Schedule Cleanup process to delete old files
		schedule(new FilesCleanupProcessor());
	}

	/**
	 * @param scheduleList
	 * @throws Exception 
	 */
	public void scheduleSchedulesList(List<Schedule> scheduleList)
			throws Exception
	{
		if (!scheduleList.isEmpty())
		{
			for (Schedule schedule : scheduleList)
			{
				if (schedule instanceof ReportSchedule)
				{
					ReportScheduleProcessor rSchedProc = new ReportScheduleProcessor();
					rSchedProc.setScheduleObject(schedule);
					schedule(rSchedProc);
				}
			}
		}
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
	 * @param schedulesList
	 * @throws Exception
	 */
	public void populateSchedules(List<Schedule> schedulesList) throws Exception
	{
		List<String> scheduleTypes = Arrays
				.asList(((String) SchedulerConfigurationPropertiesHandler.getInstance()
						.getProperty(SchedulerConstants.SCHEDULE_TYPES)).split(","));
		ScheduleBizLogic schedBizLogic = new ScheduleBizLogic();

		for (String scheduleName : scheduleTypes)
		{
			schedulesList.addAll(schedBizLogic.retrieve(scheduleName));
		}
	}
	
}
