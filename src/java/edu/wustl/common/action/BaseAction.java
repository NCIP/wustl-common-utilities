
package edu.wustl.common.action;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.UserNotAuthenticatedException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the base class for all other Actions. The class provides generic
 * methods that are reusable by all subclasses. In addition, this class ensures
 * that the user is authenticated before calling the executeWorkflow of the
 * subclass. If the User is not authenticated then an
 * UserNotAuthenticatedException is thrown.
 * @deprecated use SecureAction instead of BaseAction
 * In this release BaseAction is deprecated, use SecureAction instead
 * of BaseAction for login and authentication validation.
 * In later releases BaseAction.java will have only cross scripting validations,
 * all login and authentication validation will be done through SecureAction
 * @author Aarti Sharma
 *
 */
public abstract class BaseAction extends XSSSupportedAction
{
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BaseAction.class);
	/**
	 * Method ensures that the user is authenticated before calling the
	 * executeAction of the subclass. If the User is not authenticated then an
	 * UserNotAuthenticatedException is thrown.
	 *
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * @exception Exception Generic exception
	 */
	public final ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		LOGGER.info("Inside execute method of BaseAction ");
		preExecute(mapping, form, request, response);
		Object sessionData = request.getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		Object accessObj = request.getParameter(Constants.ACCESS);
		if (!(sessionData != null && accessObj != null) && getSessionData(request) == null)
		{
				//Forward to the Login
				throw new UserNotAuthenticatedException();
		}
		setAttributeFromParameter(request, Constants.OPERATION);
		setAttributeFromParameter(request, Constants.MENU_SELECTED);
		if(null!=form)
		{
			request.setAttribute(Constants.HELP_URL_KEY, form.getClass().getName());
		}
		return executeAction(mapping, form, request, response);
	}

	/**
	 * Subclasses should implement this method to execute the Action logic.
	 *
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception Generic exception
	 */
	protected abstract ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * @param request HttpServletRequest
	 * @param paramName String -parameter name
	 */
	protected void setAttributeFromParameter(HttpServletRequest request, String paramName)
	{
		String paramValue = request.getParameter(paramName);
		if (paramValue != null)
		{
			request.setAttribute(paramName, paramValue);
		}
	}

	/**
	 * sets the URL of the application in proper format.
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 */
	protected void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
	{
		/** Added by amit_doshi
		 *  code reviewer abhijit_naik
		 */
		if(request.getRequestURL()!=null)
		{
			CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());
		}
	}

	/**
	 * Returns the current User authenticated by CSM Authentication from current session.
	 * @param request HttpServletRequest
	 * @return userName if SessionDataBean is not null
	 */
	protected String getUserLoginName(HttpServletRequest request)
	{
		String userName = null;
		SessionDataBean sessionData = getSessionData(request);
		if (sessionData != null)
		{
			userName = sessionData.getUserName();
		}
		return userName;
	}
	/**
	 * get data from the current session.
	 * @param request HttpServletRequest
	 * @return SessionDataBean from session
	 */
	protected SessionDataBean getSessionData(HttpServletRequest request)
	{
		return (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
	}

	/**
	 * This function checks call to the action and sets/removes required attributes
	 *  if AddNew or ForwardTo activity is executing.
	 * @param request - HTTPServletRequest calling the action
	 */
	protected void checkAddNewOperation(HttpServletRequest request)
	{
		String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);

		String submittedForParam = (String) request.getParameter(Constants.SUBMITTED_FOR);

		if ((Constants.SUBMITTED_FOR_ADD_NEW.equals(submittedFor)))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_ADD_NEW);
		}
		else if (Constants.SUBMITTED_FOR_ADD_NEW.equals(submittedForParam))
		{
			addNewOperation(request, submittedFor);
		}
		else if (Constants.SUBMITTED_FOR_FORWARD_TO.equals(submittedFor))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_FORWARD_TO);
			removeFormBeanStack(request);
		}
		//if AddNew loop is over
		else if (Constants.SUBMITTED_FOR_DEFAULT.equals(submittedFor))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_DEFAULT);
			removeFormBeanStack(request);
		}
		//if AddNew or ForwardTo loop is broken...
		else
		{
			removeFormBeanStack(request);
		}
	}
	/**
	 * sets required attributes.
	 * @param request HTTPServletRequest calling the action
	 * @param submittedFor submitted For.
	 */
	private void addNewOperation(HttpServletRequest request, String submittedFor)
	{
		if (Constants.SUBMITTED_FOR_DEFAULT.equals(submittedFor))
		{
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_DEFAULT);
		}
		else
		{
			request.setAttribute(Constants.SUBMITTED_FOR, Constants.SUBMITTED_FOR_ADD_NEW);
		}
	}
	/**
	 * remove data from current session.
	 * @param request HttpServletRequest
	 */
	private void removeFormBeanStack(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if ((session.getAttribute(Constants.FORM_BEAN_STACK)) != null)
		{
			session.removeAttribute(Constants.FORM_BEAN_STACK);
		}
	}
	/**
	 * This method calls the specified method passed as parameter.
	 * This allows us to have different entry points.
	 * to an action class. To use this method,
	 * 1. pass the method name as parameter in some request variable
	 * 2. in your executeAction/executeSecureAction method, get the parameter passed.
	 * and pass it to this method.
	 * 3. the control will directly go to the method name of the class that you specified.
	 * This way you can reuse the same action multiple times.
	 *
	 * @param methodName - name of the method to be called
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return 	ActionForward
	 * @throws Exception generic exception
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, // NOPMD
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionForward = null;
		if (!"".equals(methodName))
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object[] args = {mapping, form, request, response};
				actionForward = (ActionForward) method.invoke(this, args);
			}
		}
		return actionForward;
	}
	/**
	 * This method returns the method with the specified name if the method exists. Return null other wise.
	 * @param name - String name of method
	 * @param className - Class
	 * @return method object
	 */
	protected Method getMethod(String name, Class className)
	{
		Method method = null;
		Class[] types = {ActionMapping.class, ActionForm.class, HttpServletRequest.class,
				HttpServletResponse.class};
		try
		{
			method = className.getDeclaredMethod(name, types);
		}
		catch (NoSuchMethodException excp1)
		{
			LOGGER.error(excp1.getMessage(), excp1);
		}
		catch (SecurityException excp3)
		{
			LOGGER.error(excp3.getMessage(), excp3);
		}
		return method;
	}
}