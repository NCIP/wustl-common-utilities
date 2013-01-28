
package edu.wustl.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.exception.UserNotAuthenticatedException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * Class intercepts the struts action call and performs authorization to ensure
 * the user has an EXECUTE privilege on the subclassing Action. If the User does
 * not have the EXECUTE privilege then access is denied to execute the business
 * logic. No coding is needed to implement this authorization. To implement this
 * solution in the authorization schema: 1. Create a protection element with the
 * subclassing action's fully qualified class name (e.g.,
 * gov.nih.nci.action.MyAction ) 2. Create a protection group(s) and assoicate
 * the element from step one. 3. Create a privilege named 'EXECUTE' and assign
 * it to the target role. 4. Associate the user or user_group with the
 * protection group in the context of the role created in step 3. 5. Subclass
 * the SecureAction and implement executeSecureWorkflow
 *
 * @author Aarti Sharma
 *
 */
public abstract class SecureAction extends BaseAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(SecureAction.class);
	/**
	 * Authorizes the user and executes the secure workflow. If authorization
	 * fails, the user is denied access to the secured action
	 *
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * @exception Exception Generic exception
	 */
	protected final ActionForward  executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		LOGGER.info("In execute method of secure Action");
		//long startTime = System.currentTimeMillis();
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
		checkAddNewOperation(request);
		return executeSecureAction(mapping, form, request, response);

	}


	/**
	 * Return,about next action to be performed.
	 * @param request HttpServletRequest
	 * @param mapping ActionMapping
	 * @return ActionForward
	 */
	protected ActionForward getActionForward(HttpServletRequest request, ActionMapping mapping)
	{
		return mapping.findForward(Constants.ACCESS_DENIED);
	}

	/**
	 * Returns the object id of the protection element that represents
	 * the Action that is being requested for invocation.
	 * @param request HttpServletRequest
	 * @return object Id.
	 */
	protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
	{
		return this.getClass().getName();
	}

	/**
	 *
	 * @param actionClassName String
	 * @return the name of privilege assigned to user.
	 */
	protected String getPrivilegeName(String actionClassName)
	{
		return Variables.privilegeDetailsMap.get(actionClassName);
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
	protected abstract ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

}