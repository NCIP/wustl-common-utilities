/**
 *
 */

package edu.wustl.common.exception;

/**
 * @author prashant_bandal
 *
 */
public class ParseException extends Exception
{

	/**
	 * Specifies serial Version UID.
	 */
	private static final long serialVersionUID = 6683027539524802184L;

	/**
	 * Constructor.
	 * @param exception Exception.
	 */
	public ParseException(Exception exception)
	{
		super(exception);
	}

}
