/**
 * <p>Title: AddNewAction Class>
 * <p>Description:	This Class is used to maintain FormBean for AddNew operation</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on Apr 12, 2006
 */

package edu.wustl.common.action;

import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to maintain FormBean for AddNew operation.
 * @author Krunal Thakkar
 */
public class AddNewAction extends XSSSupportedAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(AddNewAction.class);

	/**
	 * Overrides the execute method of Action class.
	 * Maintains FormBean for AddNew operation.
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward actionForward;
		try
		{
			LOGGER.info("Started Add action");
			AddNewSessionDataBean sessionDataBean = createNewSessionDataBean(form, request);
			Stack<AddNewSessionDataBean> formBeanStack = getStackBeanFromSession(request);
			formBeanStack.push(sessionDataBean);
			request.setAttribute(Constants.SUBMITTED_FOR, "AddNew");
			actionForward = mapping.findForward(request.getParameter(Constants.ADD_NEW_FORWARD_TO));
		}
		catch (Exception e)
		{
			LOGGER.info("Exception: " + e.getMessage(), e);
			actionForward = mapping.findForward(Constants.SUCCESS);
		}
		return actionForward;
	}

	/**
	 * Return stack of session data bean that store current session.
	 * @param request HttpServletRequest to get  current session.
	 * @return Stack of type AddNewSessionDataBean.
	 */
	private Stack<AddNewSessionDataBean> getStackBeanFromSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		Stack<AddNewSessionDataBean> formBeanStack = (Stack<AddNewSessionDataBean>) session
				.getAttribute(Constants.FORM_BEAN_STACK);

		if (formBeanStack == null)
		{
			LOGGER.debug("Creating FormBeanStack in AddNewAction.");
			formBeanStack = new Stack<AddNewSessionDataBean>();
		}
		session.setAttribute(Constants.FORM_BEAN_STACK, formBeanStack);
		return formBeanStack;
	}

	/**
	 * create new session data bean object that contains data for new action.
	 * @param form ActionForm.
	 * @param request HttpServletRequest to get data required for new action.
	 * @return AddNewSessionDataBean object.
	 */
	private AddNewSessionDataBean createNewSessionDataBean(ActionForm form,
			HttpServletRequest request)
	{
		AddNewSessionDataBean sessionDataBean = new AddNewSessionDataBean();
		sessionDataBean.setAbstractActionForm((AbstractActionForm) form);
		sessionDataBean.setForwardTo(request.getParameter(Constants.FORWARD_TO));
		sessionDataBean.setAddNewFor(request.getParameter(Constants.ADD_NEW_FOR));
		return sessionDataBean;
	}
}
