/**
 * <p>Title: HTTPMessage Class>
 * <p>Description:	This is the wrapper class over error messages from the server.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 15, 2005
 */

package edu.wustl.common.struts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the wrapper class over error messages from the server.
 * @author aniruddha_phadnis
 */
public class HTTPMessage implements Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -3767645078908289018L;

	/**
	 * Specifies message List.
	 */
	private List<Object> messageList;

	/**
	 * Specifies response Status.
	 */
	private String responseStatus;

	/**
	 * Specifies session Id.
	 */
	private String sessionId;

	/**
	 * Specifies domain Object Id.
	 */
	private Long domainObjectId;

	/**
	 * Constructor.
	 */
	public HTTPMessage()
	{
		messageList = new ArrayList<Object>();
	}

	/**
	 * Constructor.
	 * @param messageList message List.
	 */
	public HTTPMessage(List<Object> messageList)
	{
		this.messageList = messageList;
	}

	/**
	 * Returns the list of error messages.
	 * @return the list of error messages.
	 * @see #setMessageList(List)
	 */
	public List<Object> getMessageList()
	{
		return messageList;
	}

	/**
	* Sets the list of error messages.
	* @param message the list of error messages.
	* @see #getMessageList()
	*/
	public void setMessageList(List<Object> message)
	{
		this.messageList = message;
	}

	/**
	 * Adds an error object to the list.
	 * @param object an error object.
	 * @see #getMessageList()
	 * @see #setMessageList(List)
	 */
	public void addMessage(Object object)
	{
		messageList.add(object);
	}

	/**
	 * Returns the Response Status of the operation.
	 * @return the Response Status of the operation
	 */
	public String getResponseStatus()
	{
		return responseStatus;
	}

	/**
	 * Sets Response Status of the operation.
	 * @param responseStatus Response Status of the operation
	 */
	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	/**
	 * Returns the Session ID.
	 * @return the Session ID
	 */
	public String getSessionId()
	{
		return sessionId;
	}

	/**
	 * Sets the Session ID.
	 * @param sessionId the Session ID
	 */
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	/**
	 * Sets the ID of Domain Object.
	 * @param domainObjectId the ID of Domain Object
	 */
	public void setDomainObjectId(Long domainObjectId)
	{
		this.domainObjectId = domainObjectId;
	}

	/**
	 * Returns the ID of Domain Object.
	 * @return the ID of Domain Object
	 */
	public Long getDomainObjectId()
	{
		return this.domainObjectId;
	}
}