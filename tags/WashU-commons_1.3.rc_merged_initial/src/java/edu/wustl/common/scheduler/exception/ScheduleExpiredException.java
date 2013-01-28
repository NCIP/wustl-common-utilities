package edu.wustl.common.scheduler.exception;


public class ScheduleExpiredException extends Exception
{
	private static final long serialVersionUID = 976031163699858369L;

	public ScheduleExpiredException(String message)
	{
		super(message);
	}
}
