/*
 * Created on Aug 10, 2005
 */

package edu.wustl.common.exception;

/**
 * @author kapil_kaveeshwar
 */
public class AssignDataException extends ApplicationException
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = -3191673172998892548L;

	/**
	 * The Only public constructor to restrict creating object without
	 * initializing mandatory members.
	 * @param errorKey The object which will represent the root cause of the error.
	 * @param exception root exception, if any, which caused this error.
	 * @param msgValues custom message, additional information.
	 */
	public AssignDataException(ErrorKey errorKey, Exception exception, String msgValues)
	{
		super(errorKey, exception, msgValues);
	}
}
