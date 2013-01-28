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
