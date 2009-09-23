/**
 * <p>Title: SendEmail Class>
 * <p>Description:	This Class is used to send emails.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
 */

package edu.wustl.common.util.global;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * This Class is used to send E-mails.
 *
 * @author gautam_shetty
 */
public class SendEmail
{
	
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(SendEmail.class);
	/**
	 * From email address.
	 */
	private String from;
	/**
	 * Host address.
	 */
	private String host;
	/**
	 * Password of the User Email Id.
	 */
	private String emailPassword ;
	/**
	 * Constructor with host and from e-mail address.
	 * @param host -host address of man server.
	 * @param from - Sender email address.
	 * @throws MessagingException Throws exception if host is null.
	 */
	public SendEmail(String host,String from) throws MessagingException
	{
		if(null==host)
		{
			LOGGER.fatal("Host can't be null");
			throw new MessagingException("Host can't be null");
		}
		this.host=host;
		this.from=from;
	}
	/**
	 * Constructor with host and from e-mail address and password.
	 * @param emailPassword Sender email address password 
	 * @param from host address of man server.
	 * @param host Sender email address.
	 */
	public SendEmail(String host, String from, String emailPassword) throws MessagingException
	{
		if(null==host)
		{
			LOGGER.fatal("Host can't be null");
			throw new MessagingException("Host can't be null");
		}
		this.host = host;
		this.from = from;
		this.emailPassword = emailPassword;
	}
	/**
	 * Used to send the mail with given parameters.
	 *
	 * @param emailDetails EmailDetails object which contains email subject, body recipient address.
	 * @return true if mail was successfully sent, false if it fails
	 */
	public boolean sendMail(EmailDetails emailDetails)
	{
		System.out.println("Inside SendMail method...");
		boolean sendStatus;
		Session session = getEmailSession();
		try
		{
			MimeMessage msg = setEmailInfo(emailDetails,session);
			/*Transport trans = session.getTransport();
			try
			{
				trans.connect(this.host, this.from, this.emailPassword);
				trans.sendMessage(msg, msg.getAllRecipients());
			}
			finally
			{
				trans.close();
			}*/
			System.out.println(this.host + this.from + this.emailPassword);
			Transport.send(msg);
			sendStatus = true;
		}
		catch (MessagingException mex)
		{
			LOGGER.warn("Unable to send mail to: " + emailDetails.getToAddress(),mex);
			sendStatus = false;
			System.out.println(mex.getMessage());
		}
		return sendStatus;
	}

	/**
	 * @param emailDetails EmailDetails object which contains email subject, body recipient address.
	 * @param session Email Session.
	 * @return message with updated
	 * @throws MessagingException This exception thrown if email parameters are wrongly formatted.
	 */
	private MimeMessage setEmailInfo(EmailDetails emailDetails,Session session)
			throws MessagingException
	{
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.CC, emailDetails.getCcInternetAddrArray());
		msg.setRecipients(Message.RecipientType.BCC, emailDetails.getBccInternetAddrArray());
		msg.setRecipients(Message.RecipientType.TO, emailDetails.getToInternetAddrArray());
		msg.setSubject(emailDetails.getSubject());
		msg.setSentDate(new Date());
		// create and fill the first message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(emailDetails.getBody());
		Multipart multiPart = new MimeMultipart();
		multiPart.addBodyPart(messageBodyPart);
		// add the Multipart to the message
		msg.setContent(multiPart);
		return msg;
	}

	/**
	 * @return Default mail Session.
	 */
	private Session getEmailSession()
	{
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", Boolean.valueOf(true)) ;
		Authenticator auth = new MailAuthenticator(this.from,this.emailPassword) ;
		//Session session = Session.getDefaultInstance(props, null);
		Session session = Session.getInstance(props, auth);
		session.setDebug(false);
		return session;
	}
	/**
	 * @return the from
	 */
	public String getFrom()
	{
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}

	/**
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}
	/**
	 * @return the emailPassword
	 */
	public String getEmailPassword()
	{
		return emailPassword;
	}
	/**
	 * @param emailPassword the emailPassword to set
	 */
	public void setEmailPassword(String emailPassword)
	{
		this.emailPassword = emailPassword;
	}
	/**
	 * This is the private class which performs SMTP Mail Authentication
	 * This mail authentication is Password Authentication to check the sender of 
	 * the mail is valid user of the mail server.
	 * @author shrishail_kalshetty
	 *
	 */
	private class MailAuthenticator extends Authenticator
	{
		/**
		 * Sender Email Address.
		 */
		private String emailSenderId ;
		/**
		 * Password of Sender Email Address.
		 */
		private String emailPassword ;
		/**
		 * @param emailPassword Password of the Senders email Address.
		 * @param emailSenderId Email Address of the Sender.
		 */
		public MailAuthenticator(String emailSenderId, String emailPassword)
		{
			this.emailSenderId = emailSenderId;
			this.emailPassword = emailPassword;
		}
		/**
		 * @return returns the password authentication.
		 */
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(this.emailSenderId,this.emailPassword);
		}
	}
	/**
	 * This is the main method to check whether Email sent successfully or not.
	 * @param args Arguments that are passed to the main method.
	 */
	/*public static void main(String[] args)
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
		System.out.println("Inside SendEmail");
		EmailDetails emailDetails = new EmailDetails() ;
		String[] toAddress = {args[3],args[4]} ;
		emailDetails.setToAddress(toAddress);
		emailDetails.setBody("Body of the Mail");
		emailDetails.setSubject("Test Mail");
		
		try
		{
			String host = args[0]  ;
			String from = args[1] ;
			String passwd = args[2] ;
			SendEmail sendEmail = new SendEmail(host,from,passwd);
			if(sendEmail.sendMail(emailDetails))
			{
				System.out.println("Email Sent Successfully...");
			}
			else
			{
				System.out.println("Email Not Sent Successfully...");
			}
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
