/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This class contains email details.
 * @author ravi_kumar
 *
 */
public class EmailDetails
{
	/**
	 * Message recipient's e-mail address as to.
	 */
	private List<String> toAddress;

	/**
	 * Message recipient's e-mail address as carbon copy(cc).
	 */
	private List<String> ccAddress;

	/**
	 * Message recipient's e-mail address as blind carbon copy(bcc).
	 */
	private List<String> bccAddress;

	/**
	 * E-mail subject.
	 */
	private String subject;

	/**
	 * E-mail body.
	 */
	private String body;

	/**
	 * No argument constructor.
	 */
	public EmailDetails()
	{
		toAddress = new ArrayList<String>();
	}
	/**
	 * This method adds e-mail address in main recipient's address(to) list.
	 * @param toAddress E-mail address to add in address list.
	 */
	public void addToAddress(String toAddress)
	{
		this.toAddress.add(toAddress);
	}

	/**
	 * Remove an e-mail address from main recipient's address list.
	 * @param toAddress -Address to remove from address list.
	 */
	public void removeToAddress(String toAddress)
	{
		this.toAddress.remove(toAddress);
	}

	/**
	 * Set main recipient(to) address list.
	 * @param toAddress TO e-mail address as String array.
	 */
	public void setToAddress(String[] toAddress)
	{
		this.toAddress=Arrays.asList(toAddress);
	}

	/**
	 * @return main recipient address(to) list.
	 */
	public Collection<String> getToAddress()
	{
	 return this.toAddress;
	}

	/**
	 * @return main recipient(to) address list as array of InternetAddress.
	 * @throws AddressException This exception thrown when a wrongly formatted address is encountered
	 */
	public InternetAddress[] getToInternetAddrArray() throws AddressException
	{
		return convertToInternetAddrArray(toAddress);
	}

	/**
	 * Add to carbon copy(cc) recipient address list.
	 * @param ccAddress Address a address in cc address list..
	 */
	public void addCcAddress(String ccAddress)
	{
		if(null==this.ccAddress)
		{
			this.ccAddress = new ArrayList<String>();
		}
		this.ccAddress.add(ccAddress);
	}

	/**
	 * Remove from carbon copy(cc) recipient address list.
	 * @param ccAddress -Address to remove from recipient address list.
	 */
	public void removeCcAddress(String ccAddress)
	{
		this.ccAddress.remove(ccAddress);
	}

	/**
	 * This method set carbon copy(cc) recipient address list.
	 * @param ccAddress cc e-mail address list as String array.
	 */
	public void setCcAddress(String[] ccAddress)
	{
		this.ccAddress = Arrays.asList(ccAddress);
	}

	/**
	 * @return the carbon copy(cc) recipient address list.
	 */
	public Collection<String> getCcAddress()
	{
		return ccAddress;
	}

	/**
	 * @return return carbon copy(cc) recipient address list as array of InternetAddress.
	 * @throws AddressException This exception thrown when a wrongly formatted address is encountered
	 */
	public InternetAddress[] getCcInternetAddrArray() throws AddressException
	{
		return convertToInternetAddrArray(ccAddress);
	}

	/**
	 * Add an e-mail address to blind carbon copy(bcc) recipient address list.
	 * @param bccAddress Address to add in blind carbon copy(bcc) recipient address list.
	 */
	public void addBccAddress(String bccAddress)
	{
		if(null==this.bccAddress)
		{
			this.bccAddress = new ArrayList<String>();
		}
		this.bccAddress.add(bccAddress);
	}

	/**
	 * Remove a e-mail id from bcc address list.
	 * @param bccAddress -Address to remove from blind carbon copy(bcc) recipient address list.
	 */
	public void removeBccAddress(String bccAddress)
	{
		this.bccAddress.remove(bccAddress);
	}

	/**
	 *  This method set blind carbon copy(cc) recipient address list.
	 * @param bccAddress the e-mail address list as String array
	 */
	public void setBccAddress(String[] bccAddress)
	{
		this.bccAddress = Arrays.asList(bccAddress);
	}

	/**
	 * @return the blind carbon copy(bcc) recipient address list.
	 */
	public Collection<String> getBccAddress()
	{
		return bccAddress;
	}

	/**
	 * @return bcc e-mail address list as array of InternetAddress.
	 * @throws AddressException This exception thrown when a wrongly formatted address is encountered
	 */
	public InternetAddress[] getBccInternetAddrArray() throws AddressException
	{
		return convertToInternetAddrArray(bccAddress);
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody()
	{
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body)
	{
		this.body = body;
	}


	/**
	 * This method convert Array To InternetAddrArray.
	 * Added by kiran_pinnamaneni. code reviewer abhijit_naik
	 * @param emailAddrList convert into internet address array
	 * @return internetAddress
	 * @throws AddressException This exception thrown when a wrongly formatted address is encountered
	 */
	private InternetAddress[] convertToInternetAddrArray(List<String> emailAddrList)
		throws AddressException
	{
		List<InternetAddress> internetAddrList= new ArrayList<InternetAddress>();
		if(emailAddrList!=null)
		{
			for (String emailAddr:emailAddrList)
			{
				internetAddrList.add( new InternetAddress(emailAddr));
			}
		}
		return (InternetAddress[])internetAddrList.toArray(new InternetAddress[internetAddrList.size()]);
	}
}
