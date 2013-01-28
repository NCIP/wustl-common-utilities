/**
 * <p>Title: ResponseServlet Class>
 * <p>Description:	This servlet generates & sends the response to the HTTP API
 * Client in the form of HTTPMessage object.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @author Kapil Kaveeshwar
 * @version 1.00
 * Created on Dec 19, 2005
 */

package edu.wustl.common.struts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This servlet generates & sends the response to the HTTP API Client.
 * in the form of HTTPMessage object.
 * @author aniruddha_phadnis
 * @author kapil_kaveeshwar
 */
public class ResponseServlet extends HttpServlet
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -7659915815532090389L;

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ResponseServlet.class);

	/**
	 * do Get method.
	 * @param req HttpServletRequest.
	 * @param res HttpServletResponse
	 * @throws ServletException Servlet Exception.
	 * @throws IOException IO Exception.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}

	/**
	 * do Post method.
	 * @param req HttpServletRequest.
	 * @param res HttpServletResponse
	 * @throws ServletException Servlet Exception.
	 * @throws IOException IO Exception.
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		HTTPMessage httpMessage = new HTTPMessage();
		ActionMessages messages = (ActionMessages) req.getAttribute(Globals.MESSAGE_KEY);
		ActionErrors errors = (ActionErrors) req.getAttribute(Globals.ERROR_KEY);
		String operation = (String) req.getAttribute(Constants.OPERATION);
		Iterator itert = null;
		itert = setRespStatus(httpMessage, messages, errors, itert);
		if (itert == null)
		{
			setHttpMessage(req, httpMessage, operation);
		}
		else
		{
			Locale local = (Locale) req.getSession().getAttribute(Globals.LOCALE_KEY);
			MessageResources resources = (MessageResources) req.getAttribute(Globals.MESSAGES_KEY);
			while (itert.hasNext())
			{
				Iterator iterator = null;
				iterator = getMessages(messages, errors, itert);
				while (iterator.hasNext())
				{
					ActionMessage actionMessage = (ActionMessage) iterator.next();
					String key = actionMessage.getKey();
					httpMessage.addMessage(resources.getMessage(local, key, actionMessage
							.getValues()));
				}
			}
		}
		setDomainObjId(req, httpMessage, operation);
		res.setContentType(Constants.HTTP_API);
		ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
		oos.writeObject(httpMessage);
		oos.flush();
		oos.close();
	}

	/**
	 * set Http Message.
	 * @param req HttpServletRequest
	 * @param httpMessage HTTPMessage
	 * @param operation operation
	 */
	private void setHttpMessage(HttpServletRequest req, HTTPMessage httpMessage, String operation)
	{
		if (operation.equals(Constants.LOGIN))
		{
			httpMessage.setResponseStatus(Constants.SUCCESS);
			httpMessage.addMessage("Successful Login");
			httpMessage.setSessionId(req.getSession(true).getId());
		}
		else if (operation.equals(Constants.LOGOUT))
		{
			httpMessage.setResponseStatus(Constants.SUCCESS);
			httpMessage.addMessage("Successful Logout");
			httpMessage.setSessionId(null);
		}
	}

	/**
	 * get Messages.
	 * @param messages ActionMessages
	 * @param errors ActionErrors
	 * @param itert Iterator
	 * @return Iterator
	 */
	private Iterator getMessages(ActionMessages messages, ActionErrors errors, Iterator itert)
	{
		Iterator iterator;
		String property = (String) itert.next();
		if (messages == null)
		{
			iterator = errors.get(property);
		}
		else
		{
			iterator = messages.get(property);
		}
		return iterator;
	}

	/**
	 * set Response Status.
	 * @param httpMessage HTTPMessage
	 * @param messages ActionMessages
	 * @param errors ActionErrors
	 * @param itert Iterator
	 * @return Iterator
	 */
	private Iterator setRespStatus(HTTPMessage httpMessage, ActionMessages messages,
			ActionErrors errors, Iterator itert)
	{
		Iterator iterator = itert;
		if (messages != null)
		{
			iterator = messages.properties();
			httpMessage.setResponseStatus(Constants.SUCCESS);
		}
		else if (errors != null)
		{
			iterator = errors.properties();
			httpMessage.setResponseStatus(Constants.FAILURE);
		}
		return iterator;
	}

	/**
	 * set Domain Object Id.
	 * @param req HttpServletRequest
	 * @param httpMessage HTTPMessage
	 * @param operation operation
	 */
	private void setDomainObjId(HttpServletRequest req, HTTPMessage httpMessage, String operation)
	{
		if (!operation.equals(Constants.LOGIN) && !operation.equals(Constants.LOGOUT))
		{
			LOGGER
					.debug("id in ResponseServlet-->"
							+ req.getAttribute(Constants.SYSTEM_IDENTIFIER));

			if (req.getAttribute(Constants.SYSTEM_IDENTIFIER) == null)
			{
				httpMessage.setDomainObjectId(null);
			}
			else
			{
				httpMessage.setDomainObjectId((Long)req.getAttribute(Constants.SYSTEM_IDENTIFIER));
			}
		}
	}
}
