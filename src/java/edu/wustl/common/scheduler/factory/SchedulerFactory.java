/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;

public class SchedulerFactory
{

	private static SchedulerFactory schedulerFact;
	@SuppressWarnings("rawtypes")
	private static Map<Long, ScheduledFuture> scheduleHandlersMap = new HashMap<Long, ScheduledFuture>();

	/** The scheduler. */
	private static ScheduledExecutorService scheduler;

	/**
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	private SchedulerFactory() throws NumberFormatException, Exception
	{
		scheduler = Executors.newScheduledThreadPool((Integer
				.parseInt((String) SchedulerConfigurationPropertiesHandler.getInstance()
						.getProperty(SchedulerConstants.THREAD_POOL_SIZE))));

	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static synchronized SchedulerFactory getInstance() throws Exception
	{
		if (schedulerFact == null)
		{
			schedulerFact = new SchedulerFactory();
		}
		return schedulerFact;
	}

	/**
	 * @param id
	 * @param scheduleHandler
	 */
	public synchronized void addHandler(Long id, @SuppressWarnings("rawtypes") ScheduledFuture scheduleHandler)
	{
		scheduleHandlersMap.put(id, scheduleHandler);
	}

	/**
	 * @param id
	 */
	public synchronized void removeHandler(Long id)
	{
		scheduleHandlersMap.get(id).cancel(true);
		scheduleHandlersMap.remove(id);
	}

	/**
	 * @return
	 */
	public synchronized ScheduledExecutorService getSchedulerInstance()
	{
		return scheduler;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public synchronized Map<Long, ScheduledFuture> getScheduleHandlersMap()
	{
		return scheduleHandlersMap;
	}

}
