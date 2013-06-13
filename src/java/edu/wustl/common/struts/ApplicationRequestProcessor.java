/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.struts;

import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java -
 * Code Style - Code Templates
 */
public class ApplicationRequestProcessor extends TilesRequestProcessor
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger
			.getCommonLogger(ApplicationRequestProcessor.class);

	/**
	 * Constructor.
	 */
	public ApplicationRequestProcessor()
	{
		super();
	}

	/**
	 * This method process Action Form.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mapping ActionMapping
	 * @return ActionForm
	 */
	protected ActionForm processActionForm(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping)
	{
		LOGGER.debug("contentType " + request.getContentType());
		ActionForm form = null;
		if (request.getContentType() != null && request.getContentType().equals(Constants.HTTP_API))
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
				HTTPWrapperObject wrapperObject = (HTTPWrapperObject) ois.readObject();

				setOperation(request, wrapperObject);

				form = wrapperObject.getForm();

				LOGGER.debug("mapping.getAttribute() " + mapping.getAttribute());

				setMappingAttribute(request, mapping, form);
			}
			catch (Exception e)
			{
				LOGGER.debug(e.getMessage(), e);
			}
		}
		else
		{
			form = super.processActionForm(request, response, mapping);
		}
		return form;
	}

	/**
	 * set Mapping Attribute.
	 * @param request HttpServletRequest
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 */
	private void setMappingAttribute(HttpServletRequest request, ActionMapping mapping,
			ActionForm form)
	{
		if (form != null)
		{
			if ("request".equals(mapping.getScope()))
			{
				request.setAttribute(mapping.getAttribute(), form);
			}
			else
			{
				HttpSession session = request.getSession();
				session.setAttribute(mapping.getAttribute(), form);
			}
		}
	}

	/**
	 * set login/logout Operation.
	 * @param request HttpServletRequest
	 * @param wrapperObject HTTPWrapperObject
	 */
	private void setOperation(HttpServletRequest request, HTTPWrapperObject wrapperObject)
	{
		String operation = wrapperObject.getOperation();
		if (operation.equals(Constants.LOGIN))
		{
			request.setAttribute(Constants.OPERATION, Constants.LOGIN);
		}
		else if (operation.equals(Constants.LOGOUT))
		{
			request.setAttribute(Constants.OPERATION, Constants.LOGOUT);
		}
		else
		{
			request.setAttribute(Constants.OPERATION, operation);
		}
	}

	/**
	 * process Populate.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param form ActionForm
	 * @param mapping ActionMapping
	 * @throws ServletException Servlet Exception
	 */
	protected void processPopulate(HttpServletRequest request, HttpServletResponse response,
			ActionForm form, ActionMapping mapping) throws ServletException
	{
		if (!(request.getContentType() != null && request.getContentType().equals(Constants.HTTP_API)))
		{
			super.processPopulate(request, response, form, mapping);
		}
	}
}