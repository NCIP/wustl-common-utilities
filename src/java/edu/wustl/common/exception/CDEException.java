package edu.wustl.common.exception;

/**
 * Exception for CDE.
 */
public class CDEException extends Exception
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 6734202360827158521L;

	/**
	 * No argument constructor.
	 */
	public CDEException()
	{
		super();
	}

	/**
	 * One argument constructor.
	 * @param message Message with exception
	 */
	public CDEException(String message)
	{
		this(message, null);
	}

	/**
	 * One argument constructor.
	 * @param exception Exception
	 */
	public CDEException(Exception exception)
	{
		this("", exception);
	}

	/**
	 * Constructor with message and exception.
	 * @param message Message with exception
	 * @param wrapException The wrapException to set.
	 */
	public CDEException(String message, Exception wrapException)
	{
		super(message, wrapException);
	}
}
