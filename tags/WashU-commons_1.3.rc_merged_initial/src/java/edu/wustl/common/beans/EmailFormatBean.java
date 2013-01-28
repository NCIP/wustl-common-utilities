/**
 *
 */
package edu.wustl.common.beans;

/**
 * The Class EmailFormatBean for storing the information needed while sending an
 * email.
 *
 * @author Gaurav Sawant
 */
public class EmailFormatBean
{

    /**
     * to store exception.
     */
    private Exception exception;

    /**
     * to store user Id.
     */
    private Long userId;

    /**
     * The user name.
     */
    private String userName;

//    /**
//     * to store scenario.
//     * TODO - for future upgrades
//     */
//    private String scenario;

    /**
 * Gets the exception.
 *
 * @return the exception
 */
    public final Exception getException()
    {
        return exception;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public final String getUserName()
    {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param name
     *            the new user name
     */
    public final void setUserName(final String name)
    {
        userName = name;
    }

    /**
     * Sets the exception.
     *
     * @param newException
     *            the new exception
     */
    public final void setException(final Exception newException)
    {
        exception = newException;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public final Long getUserId()
    {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userIdentifier
     *            the new user id
     */
    public final void setUserId(final Long userIdentifier)
    {
        userId = userIdentifier;
    }

}
