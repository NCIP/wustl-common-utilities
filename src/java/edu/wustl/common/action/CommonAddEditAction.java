/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * <p>Title: CommonAddEditAction Class>
 * <p>Description:	This is common class used to Add/Edit the data in the database.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.newdao.ActionStatus;

/**
 * This Class is used to Add/Edit data in the database.
 * @author gautam_shetty
 */
public class CommonAddEditAction extends XSSSupportedAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CommonAddEditAction.class);

	/**
	 * Overrides the execute method of Action class.
	 * Adds / Updates the data in the database.
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * @throws Exception Exception
	 * */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		LOGGER.info("in execute method");
		BaseAddEditAction addEditAction;
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		ActionForward actionfwd;
		try
		{
			if (abstractForm.isAddOperation())
			{
				addEditAction = new CommonAddAction();
			}
			else
			{
				addEditAction = new CommonEdtAction();
			}
			actionfwd = addEditAction.executeXSS(mapping, abstractForm, request, response);
		}
		catch (ApplicationException applicationException)
		{
			LOGGER.error("Common Add/Edit failed.." + applicationException.getCustomizedMsg(),applicationException);
			request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.FAIL);
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item",
					applicationException.getCustomizedMsg());
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);

			actionfwd = mapping.findForward(Constants.FAILURE);
		}
		return actionfwd;
	}
}