package edu.wustl.common.util;

import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.EmailDetails;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * The Class CommonEmailHandler.
 */
public final class CommonEmailHandler
{

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = LoggerConfig
            .getConfiguredLogger(CommonEmailHandler.class);

    /**
     * The Constant USE_EMAIL_COMM_PFG_CFG.
     */
    public static final String USE_EMAIL_COMM_PFG_CFG = "use.email.commonpackage.config";

    /**
     * The Constant EMAIL_SERVER.
     */
    public static final String EMAIL_SERVER = "email.mailServer";

    /**
     * The Constant EMAIL_FROM_ADDR.
     */
    public static final String EMAIL_FROM_ADDR = "email.sendEmailFrom.emailAddress";

    /**
     * The Constant EMAIL_RECEPIENTS_ADDR.
     */
    public static final String EMAIL_RECEPIENTS_ADDR = "email.sendEmailTo.emailAddress";

    /**
     * The Constant ADMIN_SUPPORT_EMAIL_ADDR.
     */
    public static final String ADMIN_SUPPORT_EMAIL_ADDR = "email.admin.support.emailAddress";

    /**
     * The Constant EMAIL_FROM_NAME.
     */
    public static final String EMAIL_FROM_NAME = "email.sendEmailFrom.name";

    /**
     * The Constant EMAIL_SUBJECT.
     */
    public static final String EMAIL_SUBJECT = "email.exception.subject";

    /**
     * The Constant DEFAULT_EMAIL_FROM_NAME.
     */
    public static final String DEFAULT_EMAIL_FROM_NAME = "Administrator";

    /**
     * The Constant DEFAULT_EMAIL_SUBJECT.
     */
    public static final String DEFAULT_EMAIL_SUBJECT = "System Exception Occured";

    /**
     * Instantiates a new common email handler.
     */
    private CommonEmailHandler()
    {
        // private constructor
    }

    /**
     * This method sends the email by using the contents of the email property
     * object.
     *
     * @param emailPropertyObject
     *            the email property object.
     *
     * @return <code>true</code> if email is successfully send else
     *         <code>false</code>.
     *
     * @throws BizLogicException
     *             the biz logic exception.
     */
    public static boolean sendEmail(
            final EmailPropertyObject emailPropertyObject)
            throws BizLogicException
    {
        boolean emailStatus = false;
        try
        {
            if (emailPropertyObject != null
                    && emailPropertyObject.getSender() != null
                    && emailPropertyObject.getRecipients() != null)
            {
                String[] toMailAddress = emailPropertyObject.getRecipients();
                EmailDetails emailDetails = new EmailDetails();
                Validator validator = new Validator();
                processToAddress(validator, toMailAddress);
                emailDetails.setToAddress(toMailAddress);
                emailDetails.setSubject(emailPropertyObject.getSubject());
                emailDetails.setBody(emailPropertyObject.getContent());
                processCCAddress(emailPropertyObject, emailDetails);
                LOGGER.debug("Sending email");
                SendEmail email = new SendEmail(XMLPropertyHandler
                        .getValue(EMAIL_SERVER), emailPropertyObject
                        .getSender());
                emailStatus = email.sendMail(emailDetails);
            }
        }
        catch (MessagingException e)
        {
            throw new BizLogicException(ErrorKey
                    .getErrorKey("error.common.emailHandler"), e,
                    "Error in email setup");
        }
        return emailStatus;
    }

    /**
     * Process "cc" address of Email.
     *
     * @param emailPropertyObject
     *            the email property object.
     * @param emailDetails
     *            the email details.
     */
    private static void processCCAddress(
            final EmailPropertyObject emailPropertyObject,
            final EmailDetails emailDetails)
    {
        if (emailPropertyObject.getCcTo() != null)
        {
            emailDetails
                    .setCcAddress(removeInvalidEmailAddress(emailPropertyObject
                            .getCcTo()));
        }
    }

    /**
     * Process "to" address in Email.
     *
     * @param validator
     *            the validator.
     * @param toMailAddress
     *            the to mail address.
     */
    private static void processToAddress(final Validator validator,
            final String[] toMailAddress)
    {
        for (int i = 0; i < toMailAddress.length; i++)
        {
            if (!isValidEmail(toMailAddress[i], validator))
            {
                toMailAddress[i] = XMLPropertyHandler
                        .getValue(ADMIN_SUPPORT_EMAIL_ADDR);
            }
        }
    }

    /**
     * Checks if the given <code>emailAddress</code> is valid.
     *
     * @param emailAddress
     *            the email address.
     * @param validator
     *            the validator to use.
     *
     * @return <code>true</code>, if valid email.
     */
    private static boolean isValidEmail(final String emailAddress,
            final Validator validator)
    {
        boolean isValid = true;
        if (emailAddress == null || "".equals(emailAddress.trim())
                || !validator.isValidEmailAddress(emailAddress))
        {
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method removes any empty email address from the list and adds the
     * support email address if no other email address is present.
     *
     * @param mailCCList
     *            the mail cc list
     *
     * @return the list of valid email address
     */
    private static String[] removeInvalidEmailAddress(final String[] mailCCList)
    {
        Validator validator = new Validator();
        String[] mailCC = new String[] {};
        List<String> toCC = new LinkedList<String>();
        for (String mailToCC : mailCCList)
        {
            if (isValidEmail(mailToCC, validator))
            {
                toCC.add(mailToCC);
            }
            else
            {
                toCC.add(XMLPropertyHandler.getValue(ADMIN_SUPPORT_EMAIL_ADDR));
            }
        }
        return toCC.toArray(mailCC);
    }
}
