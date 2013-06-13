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


import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.logger.Logger;


public class FilesCleanupProcessor extends AbstractScheduleProcessor
{

	private static final Logger log = Logger.getCommonLogger(FilesCleanupProcessor.class);

	/**
	 * 
	 */
	public FilesCleanupProcessor()
	{
		super();
		Schedule cleanUpSchedule = new Schedule();
		cleanUpSchedule.setId(Long.MAX_VALUE);
		cleanUpSchedule.setName("CleanUpSchedule");
		cleanUpSchedule.setInterval(SchedulerConstants.DAILY_INTERVAL);
		this.scheduleObject = cleanUpSchedule;
	}

	/* (non-Javadoc)
	 * @see main.java.scheduler.scheduleProcessors.AbstractScheduleProcessor#executeSchedule()
	 */
	public void executeSchedule() throws Exception
	{
		Long threshold = Long.valueOf((String) SchedulerConfigurationPropertiesHandler
				.getInstance().getProperty(SchedulerConstants.CLEANUP_THRESHOLD));

		SchedulerDataUtility.cleanOldFilesFromDir(threshold);
	}

	@Override
	protected void postScheduleExecution() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean mail() throws Exception
	{
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateTicket() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}
