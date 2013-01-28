package edu.wustl.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import org.apache.log4j.Logger;

import edu.wustl.common.beans.EmailFormatBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.LoggerConfig;


/**
 * The Class SendEmailUtility.
 *
 * @author Gaurav Sawant
 */
public final class SendEmailUtility
{

    /**
     * The Constant LOGGER.
     */
    private final static Logger LOGGER = LoggerConfig.getConfiguredLogger(SendEmailUtility.class);

    /**
     * Instantiates a new send email utility.
     */
    private SendEmailUtility()
    {
        // private constructor.
    }

    /**
     * This method returns the email property object which is to be used for
     * sending mails.
     *
     * @param scenarioName
     *            the scenario name. [For future use].
     *
     * @return the EmailPropertyObject instance
     */
    public static EmailPropertyObject getEmailPropertyObject(
            final String scenarioName)
    {
        LOGGER.debug(scenarioName);
        EmailPropertyObject emailObject = new EmailPropertyObject();
        String contents = getEmailContents(scenarioName);
        String subject = getEmailSubject(scenarioName);
        emailObject.setSender(XMLPropertyHandler
                .getValue(CommonEmailHandler.EMAIL_FROM_ADDR));
        emailObject.setContent(contents);
        emailObject.setSubject(subject);
        return emailObject;
    }

    /**
     * Gets the email subject.
     *
     * @param scenarioName
     *            the scenario name [For future use].
     *
     * @return the email subject
     */
    private static String getEmailSubject(final String scenarioName)
    {
        LOGGER.debug(scenarioName);
        String subject = XMLPropertyHandler
                .getValue(CommonEmailHandler.EMAIL_SUBJECT);
        if (subject == null || "".equals(subject))
        {
            subject = CommonEmailHandler.DEFAULT_EMAIL_SUBJECT;
        }
        return subject;
    }

    /**
     * Gets the email contents.
     *
     * @param scenarioName
     *            the scenario name [For future use].
     *
     * @return the email contents
     */
    private static String getEmailContents(final String scenarioName)
    {
        LOGGER.debug(scenarioName);
        StringBuilder contents = new StringBuilder();
        contents.append("\nHi");
        contents
                .append("\nSystem error has occured. Following are the details :- \n");
        contents.append("\nUser Name:@userName@");
        contents.append("\nTimestamp:@timestamp@");
        contents.append("\nError stacktrace:@stacktrace@");
        contents.append("\n");
        contents.append("\nRegards");
        String fromUser = XMLPropertyHandler
                .getValue(CommonEmailHandler.EMAIL_FROM_NAME);
        if (fromUser == null || "".equals(fromUser.trim()))
        {
            fromUser = CommonEmailHandler.DEFAULT_EMAIL_FROM_NAME;
        }
        contents.append("\n");
        contents.append(fromUser);
        return contents.toString();
    }

    /**
     * Send email on global exception.
     *
     * @param emailFormatBean
     *            the email format bean
     */
    public static void sendEmailOnGlobalException(
            final EmailFormatBean emailFormatBean)
    {
        final Exception exception = emailFormatBean.getException();
        // final Long userId=emailFormatBean.getUserId();
        final String userName = emailFormatBean.getUserName();
        if (isSendEmailOnFailure())
        {
            String mailServer = XMLPropertyHandler
                    .getValue(CommonEmailHandler.EMAIL_SERVER);
            String sendFromEmailId = XMLPropertyHandler
                    .getValue(CommonEmailHandler.EMAIL_FROM_ADDR);
            LOGGER.debug("Mail server - " + mailServer);
            LOGGER.debug("Mail sender - " + sendFromEmailId);
            try
            {
                final EmailPropertyObject emailPropertyObj = getEmailPropertyObject(null);
                emailPropertyObj.setContentAttributes(
                        new String[] {"@userName@"},
                        new String[] {userName});
                final Calendar timestamp = Calendar.getInstance();
                emailPropertyObj.setContentAttributes(
                        new String[] {"@timestamp@"},
                        new String[] {timestamp.getTime().toString()});
                formatException(exception, emailPropertyObj);

                String[] toEmailAddresses = getEmailRecipients();
                emailPropertyObj.setRecipients(toEmailAddresses);
                emailPropertyObj.setSender(XMLPropertyHandler
                        .getValue(CommonEmailHandler.EMAIL_FROM_ADDR));

                CommonEmailHandler.sendEmail(emailPropertyObj);
            }
            catch (final BizLogicException e)
            {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Gets the email recipients.
     *
     * @return the email recipients
     */
    private static String[] getEmailRecipients()
    {
        String[] recipientsList = new String[] {};
        String recipients = XMLPropertyHandler
                .getValue(CommonEmailHandler.EMAIL_RECEPIENTS_ADDR);
        if (recipients != null)
        {
            recipientsList = recipients.split(",");
        }
        return recipientsList;
    }

    /**
     * Checks if is send email on failure.
     *
     * @return true, if is send email on failure
     */
    private static boolean isSendEmailOnFailure()
    {
        boolean isSendEmailOnFailure = false;
        String useEmailConfigFromCommPkgVal = XMLPropertyHandler
                .getValue(CommonEmailHandler.USE_EMAIL_COMM_PFG_CFG);
        if (useEmailConfigFromCommPkgVal != null
                && !"".equals(useEmailConfigFromCommPkgVal.trim()))
        {
            isSendEmailOnFailure = Boolean
                    .valueOf(useEmailConfigFromCommPkgVal);
        }
        return isSendEmailOnFailure;
    }

    /**
     * This method formats the exception stack trace.
     *
     * @param exception
     *            Exception
     * @param emailPropertyObj
     *            EmailPropertyObject
     */
    private static void formatException(final Exception exception,
            final EmailPropertyObject emailPropertyObj)
    {
        final StringBuffer msg = new StringBuffer("");
        if (exception != null)
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final PrintWriter printWriter = new PrintWriter(baos, true);
            exception.printStackTrace(printWriter);
            msg.append("Unhandled Exception occured in system - \nMessage: ")
                    .append(exception.getMessage()).append("\nStackTrace: ")
                    .append(baos.toString());
            if (msg != null && !msg.equals(""))
            {
                emailPropertyObj.setContentAttributes(
                        new String[] {"@stacktrace@"}, new String[] {msg
                                .toString() });
            }
        }
    }


    /*
     * Gets the email contents file path.
     *
     * @return the email contents file path
     */
    /*
    public String getEmailContentsFilePath() {
        return System.getProperty("app.emailContentsFile");
    }
    */

    /*
     * This method process the email contents document and returns the email
     * object.
     *
     * @param scenarioName
     * @param emailObject
     * @param subject
     * @param contents
     * @param root
     * @return email object
     */
/*    private static EmailPropertyObject processEmailContentDoc(
            String scenarioName, EmailPropertyObject emailObject,
            String subject, String contents, Element root) {
        NodeList nodeList = root.getElementsByTagName("scenario");

        int length = nodeList.getLength();
        for (int counter = 0; counter < length; counter++) {
            Element element = (Element) (nodeList.item(counter));
            if (element != null && element.getAttribute("name") != null
                    && scenarioName.equals(element.getAttribute("name"))) {
                NodeList subjectList = element.getElementsByTagName("subject");
                if (subjectList != null) {
                    subject = ((Element) subjectList.item(0)).getTextContent();
                }
                NodeList contentList = element.getElementsByTagName("content");
                if (contentList != null) {
                    contents = ((Element) contentList.item(0)).getTextContent();
                }
                break;
            }
        }
        emailObject.setSender(XMLPropertyHandler
                .getValue("email.sendEmailFrom.emailAddress"));
        emailObject.setContent(contents);
        emailObject.setSubject(subject);
        return emailObject;
    }
*/
}
