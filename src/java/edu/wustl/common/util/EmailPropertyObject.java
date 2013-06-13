/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * Object to contain email related properties like subject of the email, content
 * of the email.
 *
 * @author Gaurav Sawant
 */
public class EmailPropertyObject
{
	/**
     * Subject of the email.
     */
	private String subject;

	/**
     * Content of the mail.
     */
	private String content;

	/**
     * Email address of the sender.
     */
	private String sender;

	/**
     * A list containing email addresses of the recipients.
     */
	private List<String> recipients;

	/**
     * An array containing the email addresses of users to whom the mail is to
     * be ccied.
     */
	private List<String> ccTo;


    /**
     * Gets the cc to.
     *
     * @return the cc to
     */
    public String[] getCcTo()
    {
        String[] ccToArr = null;
        if (ccTo != null)
        {
            ccToArr = ccTo.toArray(new String[] {});
        }
        return ccToArr;
    }


	/**
     * Sets the cc to.
     *
     * @param ccToArray
     *            the array containing the 'cc' list.
     */
	public void setCcTo(String[] ccToArray)
	{
		ccTo = Arrays.asList(ccToArray);
	}


    /**
     * Gets the recipients.
     *
     * @return the recipients
     */
    public String[] getRecipients()
    {
        String[] recipientsArr = null;
        if (recipients != null)
        {
            recipientsArr = recipients.toArray(new String[] {});
        }
        return recipientsArr;
    }

	/**
     * Sets the recipients.
     *
     * @param recipientsArray
     *            the new recipients
     */
	public void setRecipients(String[] recipientsArray)
	{
		recipients = Arrays.asList(recipientsArray);
	}


	/**
     * Gets the sender.
     *
     * @return the sender
     */
	public String getSender()
	{
		return sender;
	}


	/**
     * Sets the sender.
     *
     * @param sender
     *            the new sender
     */
	public void setSender(String sender)
	{
		this.sender = sender;
	}

	/**
     * Gets the content.
     *
     * @return Returns the content.
     */
	public String getContent()
	{
		return content;
	}


	/**
     * Sets the content.
     *
     * @param body
     *            the body
     */
	public void setContent(String body)
	{
		content = body;
	}


	/**
     * Gets the subject.
     *
     * @return Returns the subject.
     */
	public String getSubject()
	{
		return subject;
	}


	/**
     * Sets the subject.
     *
     * @param subject
     *            The subject to set.
     */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

    /**
     * This method replaces the given <code>contentAttributes</code> with the
     * corresponding <code>contentAttributevalues</code> with the email
     * contents.
     *
     * @param contentAttributes
     *            the content attributes to be replaced.
     * @param contentAttributevalues
     *            the actual content attribute values.
     */
    public void setContentAttributes(final String[] contentAttributes,
            final String[] contentAttributevalues)
    {
        for (int counter = 0; counter < contentAttributes.length; counter++)
        {
            // For fixing bug #11776
            content = content.replaceFirst(contentAttributes[counter],
                    java.util.regex.Matcher
                            .quoteReplacement(contentAttributevalues[counter]));
        }
    }

    /**
     * This method set the subject attributes.
     *
     * @param token
     *            the token
     * @param projectName
     *            the project name
     */
    public void setSubjectAttributes(final String token, final String projectName)
    {
        subject = subject.replaceAll(token, java.util.regex.Matcher
                .quoteReplacement(projectName));
    }


}

