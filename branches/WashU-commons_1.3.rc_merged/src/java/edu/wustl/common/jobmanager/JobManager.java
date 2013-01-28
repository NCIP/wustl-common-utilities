
package edu.wustl.common.jobmanager;

import java.util.LinkedList;
import java.util.Queue;

// TODO: Auto-generated Javadoc
/**
 * The JobManager class will manages all the job/threads.
 * All the jobs will get initiated by the job manager this is a singleton class
 *
 * @author nitesh_marwaha
 */
final public class JobManager extends Thread
{

	/** The job queue. */
	private Queue<Job> jobQueue;

	/** The is job mgr running. */
	private boolean isJobMgrRunning = true;

	/** The job manager instance. */
	private static JobManager jobMgrInstance;

	/**
	 * Instantiates a new job manager.
	 */
	private JobManager()
	{
		super();
		jobQueue = new LinkedList<Job>();

	}

	/**
	 * Gets the single instance of JobManager.
	 *
	 * @return single instance of JobManager
	 */
	public static synchronized JobManager getInstance()
	{
		if (jobMgrInstance == null)
		{
			jobMgrInstance = new JobManager();
		}
		return jobMgrInstance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		while (isJobMgrRunning)
		{
			final Job job = jobQueue.poll();
			if (job != null)
			{
				final Thread jobThread = new Thread(job);
				jobThread.start();
			}

		}
	}

	/**
	 * Adds the job.
	 *
	 * @param job the job
	 */
	public void addJob(final Job job)
	{
		jobQueue.add(job);
	}

	/**
	 * Shutdown job manager.
	 */
	public void shutdownJobManager()
	{
		isJobMgrRunning = false;
	}

}
