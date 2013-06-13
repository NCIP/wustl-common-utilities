/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

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
