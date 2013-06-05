
package edu.wustl.common.scheduler.scheduleProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.wustl.common.report.bean.FileDetails;
import edu.wustl.common.scheduler.domain.BaseSchedule;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.exception.ScheduleExpiredException;
import edu.wustl.common.scheduler.exception.ScheduleHandlerNotFoundException;
import edu.wustl.common.scheduler.processorScheduler.Scheduler;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.DAOUtility;



/**
 * @author root
 *
 */
/**
 * @author root
 *
 */
public abstract class AbstractScheduleProcessor implements Runnable
{

	private static final Logger log = Logger.getCommonLogger(ReportScheduleProcessor.class);

	protected Schedule scheduleObject;
	protected Map<Long, FileDetails> scheduledItemFileDetailsMap;
	protected Collection<Object[]> emailAndIdList;
	protected List<Long> ticketIdList = new ArrayList<Long>();

	public List<Long> getTicketIdList()
	{
		return ticketIdList;
	}

	public void addTicketId(Long ticketId)
	{
		ticketIdList.add(ticketId);
	}

	public Collection<Object[]> getEmailAndIdList()
	{
		return emailAndIdList;
	}

	public void setEmailAndIdList() throws Exception
	{
		this.emailAndIdList = populateEmailAddress();
	}

	public void setScheduledItemFileDetails(Map<Long, FileDetails> scheduledItemFileCollection)
	{
		this.scheduledItemFileDetailsMap = scheduledItemFileCollection;
	}

	public Schedule getScheduleObject()
	{
		return scheduleObject;
	}

	public void setScheduleObject(Schedule scheduleObject)
	{
		this.scheduleObject = scheduleObject;
	}

	public void run()
	{
		try
		{
			System.out.println("------------------------------------------------------------Begin Report");
			System.out.println("------------------------------------------------------------Schedule "+scheduleObject.getId());

			shutDownIfExpired();
			generateTicket();
			for(Long ticket:ticketIdList)
			{
				System.out.println("------------------------------------------------------------Ticket"+ticket);
			}
			executeSchedule();
			postScheduleExecution();
			System.out.println("------------------------------------------------------------End Report");


		}
		catch (Exception e)
		{
			e.printStackTrace();

		} finally {
			DAOUtility.getInstance().cleanupTxnState();
		}

	}

	/**
	 * @throws Exception
	 * Override this method in the derived class to perform actual execution
	 */
	 public abstract void executeSchedule() throws Exception;

	/**
	 * @throws Exception
	 * Override this method in the derived class to perform post shedule execution tasks
	 */
	protected abstract void postScheduleExecution() throws Exception;
	
	/**
	 * @throws Exception
	 * Populates report audit table and generates ticket
	 */
	protected abstract void generateTicket() throws Exception;

	
	/**
	 * @throws Exception
	 * Override this method to perform post execution activities
	 */
	protected void logGeneratedFileDetails(Long ticketId) throws Exception
	{
		/*setEmailAndIdList();
		if (emailAndIdList != null && !emailAndIdList.isEmpty())
		{
			GeneratedFileDetailsBizLogic fileBiz = new GeneratedFileDetailsBizLogic();
			GeneratedFileDetails generatedFileDetail = (GeneratedFileDetails) fileBiz.retrieve(
					GeneratedFileDetails.class.getName(), ticketId);
			for (Object emailAddressAndId : emailAndIdList)
			{
				Object[] objArr = (Object[]) emailAddressAndId;
				StringBuffer body = new StringBuffer("");

				if (generatedFileDetail.getExecutionStatus().equalsIgnoreCase("Completed"))
				{
					GeneratedFileDetails userFileDetail = generatedFileDetail;
					userFileDetail.setId(null);
					userFileDetail.setUserId((Long) objArr[1]);
					fileBiz.insert(userFileDetail);
					//setTicketId(userFileDetail.getId());
					body.append(ReportSchedul;erUtil.getFileDownloadLink(userFileDetail.getId()
							.toString()));
					System.out.println("Link: " + body.toString());
				}
				else if (generatedFileDetail.getExecutionStatus().equalsIgnoreCase("Error"))
				{
					//setTicketId(generatedFileDetail.getId());
					body.append("Error while executing report ").append(
							generatedFileDetail.getErrorDescription());
				}

				SchedulerDataUtility.sendScheduleMail((String) objArr[0], "Subject",
						body.toString());
			}
			if (generatedFileDetail.getExecutionStatus().equalsIgnoreCase("Completed"))
			{
				fileBiz.delete(generatedFileDetail);
			}
		}*/
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	protected Collection<Object[]> populateEmailAddress() throws Exception
	{
		Collection<Long> userIdCollection = ((BaseSchedule) scheduleObject)
				.getRecepientUserIdCollection();
		Collection<Object[]> emailAddressCollection = null;
		if (userIdCollection != null)
		{
			if (((BaseSchedule) scheduleObject).getIncludeMe())
			{
				userIdCollection.add(((BaseSchedule) scheduleObject).getOwnerId());
			}

		}
		if (!userIdCollection.isEmpty())
		{
			emailAddressCollection = SchedulerDataUtility
					.getUsersIdAndEmailAddressList(userIdCollection);
		}

		return emailAddressCollection;
	}

	public abstract boolean mail() throws Exception;
	

	/**
	 * @throws Exception 
	 */
	protected void shutDownIfExpired() throws Exception
	{
		if (scheduleObject.getEndDate() != null)
		{
			if (new Date().getTime() >= scheduleObject.getEndDate().getTime())
			{
				Scheduler scheduler = new Scheduler();
				scheduler.unSchedule(scheduleObject.getId());
				log.info("Schedule: " + scheduleObject.getName()
						+ " has reached its end date hence has been stopped.");
				System.out.println("Schedule: " + scheduleObject.getName()
						+ " has reached its end date hence has been stopped.");
				throw new ScheduleExpiredException("Schedule: " + scheduleObject.getName()
						+ " has reached its end date");
			}
		}
	}

}
