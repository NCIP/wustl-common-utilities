/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.common.exception;

/**
 * @author prafull_kadam
 * Class to wrap Password Encryption/Decryption related exception.
 */
public class PasswordEncryptionException extends Exception
{

	/**
	 * serial version id.
	 */
	private static final long serialVersionUID = 7522016908298800388L;

	/**
	 * Default constructor.
	 */
	public PasswordEncryptionException()
	{
		super();
	}

	/**
	 * @param message the detail message. The detail message is saved for later retrieval by the
	 *  getMessage() method.
	 */
	public PasswordEncryptionException(String message)
	{
		super(message);
	}

	/**
	 * @param cause the cause (which is saved for later retrieval by the getCause() method).
	 */
	public PasswordEncryptionException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message the detail message.
	 * @param cause the cause (which is saved for later retrieval by the getCause() method).
	 */
	public PasswordEncryptionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
