/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.action;

import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.newdao.ActionStatus;

/**
 *  This Class is used to Add data in the database.
 */
public class CommonAddAction extends BaseAddEditAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CommonAddAction.class);
	/**
	 * This method get data from form of current session and add it for further operation.
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param form Action Form.
	 * @return ActionForward
	 * @throws ApplicationException Application Exceptio.
	 */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws ApplicationException
	{
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		ActionForward forward;
		ActionMessages messages = new ActionMessages();

		String target;
		String objectName = getObjectName(abstractForm);
		AbstractDomainObject abstractDomain = insertDomainObject(request, abstractForm);
		SessionDataBean sessionDataBean=(SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		setSuccessMsg(request, messages, objectName, abstractDomain,sessionDataBean);
		abstractForm.setId(abstractDomain.getId().longValue());
		request.setAttribute(Constants.SYSTEM_IDENTIFIER, abstractDomain.getId());
		abstractForm.setMutable(false);

		String submittedFor = (String) request.getParameter(Constants.SUBMITTED_FOR);

		if ("AddNew".equals(submittedFor))
		{
			//Retrieving AddNewSessionDataBean from Stack
			forward = getForwardToFromStack(mapping, request, abstractDomain, objectName);

		}
		else
		{
			target = getForwardTo(request, abstractDomain, abstractForm);
			forward = mapping.findForward(target);
		}

		request.setAttribute("forwardToPrintMap", generateForwardToPrintMap(abstractForm,
				abstractDomain));
		//Status message key.
		StringBuffer statusMessageKey = new StringBuffer();
		statusMessageKey.append(abstractForm.getFormId()).append('.').append(abstractForm.isAddOperation());
		request.setAttribute(Constants.STATUS_MESSAGE_KEY, statusMessageKey.toString());
		return forward;
	}

	/**
	 * Set Success Message.
	 * @param request HttpServletRequest
	 * @param messages ActionMessages
	 * @param objectName object Name
	 * @param abstractDomain AbstractDomainObject
	 * @throws ApplicationException Application Exception.
	 */
	private void setSuccessMsg(HttpServletRequest request, ActionMessages messages,
			String objectName, AbstractDomainObject abstractDomain,SessionDataBean sessionDataBean) throws ApplicationException
	{
		String[] displayNameParams = addMessage(abstractDomain, objectName,sessionDataBean);
		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("object.add" + ".successOnly",
				displayNameParams));
		request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
		saveMessages(request, messages);
	}

	/**
	 * Retrieving AddNewSessionDataBean from Stack.
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @param abstractDomain AbstractDomainObject
	 * @param objectName object Name
	 * @return ActionForward
	 * @throws ApplicationException Application Exception.
	 */
	private ActionForward getForwardToFromStack(ActionMapping mapping, HttpServletRequest request,
			AbstractDomainObject abstractDomain, String objectName) throws ApplicationException
	{

		Stack formBeanStack = (Stack) request.getSession().getAttribute(Constants.FORM_BEAN_STACK);
		ActionForward forward;

		if (formBeanStack == null)
		{
			forward = mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			AddNewSessionDataBean addNewSessionDataBean = (AddNewSessionDataBean) formBeanStack
					.pop();

			if (addNewSessionDataBean == null)
			{
				throw new ApplicationException(ErrorKey.getErrorKey("errors.item.unknown"), null,
						AbstractDomainObject.parseClassName(objectName));

			}
			String forwardTo = addNewSessionDataBean.getForwardTo();
			AbstractActionForm sessionFormBean = updateSessionForm(abstractDomain,
					addNewSessionDataBean, formBeanStack, request);

			if ((sessionFormBean.getOperation().equals("edit")))
			{
				forward = getEditForward(mapping, forwardTo);
			}
			else
			{
				forward = (mapping.findForward(forwardTo));
			}
		}
		return forward;
	}

	/**
	 * inserts Domain Object.
	 * @param request HttpServletRequest
	 * @param abstractForm AbstractActionForm
	 * @return AbstractDomainObject
	 * @throws ApplicationException Application Exception
	 */
	private AbstractDomainObject insertDomainObject(HttpServletRequest request,
			AbstractActionForm abstractForm) throws ApplicationException
	{
		try
		{
			AbstractDomainObject abstractDomain;
			IDomainObjectFactory abstractDomainObjectFactory = getIDomainObjectFactory();
			abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(),
					abstractForm);
			IBizLogic bizLogic = getIBizLogic(abstractForm);
			bizLogic.insert(abstractDomain, getSessionData(request));

			return abstractDomain;
		}
		catch (AssignDataException e)
		{
			LOGGER.debug(e.getMessage(), e);
			throw new ApplicationException(e.getErrorKey(), e,e.getMsgValues());
		}
	}

	/**
	 * This method gets target.
	 * @param request HttpServletRequest
	 * @param abstractDomain AbstractDomainObject
	 * @param abstractForm AbstractActionForm
	 * @return target
	 * @throws ApplicationException Application Exception
	 */
	private String getForwardTo(HttpServletRequest request, AbstractDomainObject abstractDomain,
			AbstractActionForm abstractForm) throws ApplicationException
	{
		String target = Constants.SUCCESS;
		String submittedFor = (String) request.getParameter(Constants.SUBMITTED_FOR);
		if ("ForwardTo".equals(submittedFor))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, "Default");
			request.setAttribute("forwardToHashMap", generateForwardToHashMap(abstractForm,
					abstractDomain));
		}
		if (abstractForm.getForwardTo() != null && abstractForm.getForwardTo().trim().length() > 0)
		{
			String forwardTo = abstractForm.getForwardTo();
			target = forwardTo;
		}
		return target;
	}

	/**
	 * This method gets Edit Forward.
	 * @param mapping ActionMapping
	 * @param forwardTo forward To.
	 * @return ActionForward.
	 */
	private ActionForward getEditForward(ActionMapping mapping, String forwardTo)
	{

		ActionForward editForward = new ActionForward();
		String addPath = (mapping.findForward(forwardTo)).getPath();
		String editPath = addPath.replaceFirst("operation=add", "operation=edit");
		editForward.setPath(editPath);
		return editForward;
	}

	/**
	 * This method updates Session Form.
	 * @param abstractDomain AbstractDomainObject
	 * @param addNewSessionDataBean AddNewSessionDataBean
	 * @param formBeanStack form Bean Stack
	 * @param request HttpServletRequest
	 * @return AbstractActionForm.
	 */
	private AbstractActionForm updateSessionForm(AbstractDomainObject abstractDomain,
			AddNewSessionDataBean addNewSessionDataBean, Stack formBeanStack,
			HttpServletRequest request)
	{
		AbstractActionForm sessionFormBean = addNewSessionDataBean.getAbstractActionForm();
		sessionFormBean.setAddNewObjectIdentifier(addNewSessionDataBean.getAddNewFor(),
				abstractDomain.getId());
		sessionFormBean.setMutable(false);

		if (formBeanStack.isEmpty())
		{
			HttpSession session = request.getSession();
			session.removeAttribute(Constants.FORM_BEAN_STACK);
			request.setAttribute(Constants.SUBMITTED_FOR, "Default");
		}
		else
		{
			request.setAttribute(Constants.SUBMITTED_FOR, "AddNew");
		}
		String formBeanName = Utility.getFormBeanName(sessionFormBean);
		request.setAttribute(formBeanName, sessionFormBean);

		return sessionFormBean;
	}

}
