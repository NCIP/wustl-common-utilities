package edu.wustl.common.scheduler.exception;


public class AlreadyScheduledException extends Exception

	
{
	private static final long serialVersionUID = -1458325467226137898L;
	
	public AlreadyScheduledException(String message)
	{
		super(message);
	}
}
