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
 * <p>Title: ApproveUserShowAction Class>
 * <p>Description:	ApproveUserShowAction is used to show the list of users
 * who have newly registered.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 25, 2005
 */

package edu.wustl.common.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * DomainObjectListAction is used to show the list of all
 * values of the domain to be shown.
 * @author gautam_shetty
 */
public class DomainObjectListAction extends SecureAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(DomainObjectListAction.class);

	/**
	 * set the domain object list,along with other details in session.
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @exception Exception -generic exception
	 * @return ActionForward
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		LOGGER.info("in execute method");
		List list = null;
		List showList = null;
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		//Returns the page number to be shown.
		int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));
		HttpSession session = request.getSession();
		int startIndex = Constants.ZERO;
		int endIndex = Constants.NUMBER_RESULTS_PER_PAGE;
		if (pageNum == Constants.START_PAGE)
		{
			list = getList(abstractForm);
			if (Constants.NUMBER_RESULTS_PER_PAGE > list.size())
			{
				endIndex = list.size();
			}
			session.setAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST, list);
		}
		else
		{
			list = (List) session.getAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST);

			//Set the start index of the users in the list.
			startIndex = (pageNum - Constants.ONE) * Constants.NUMBER_RESULTS_PER_PAGE;

			//Set the end index of the users in the list.
			endIndex = startIndex + Constants.NUMBER_RESULTS_PER_PAGE;
			if (endIndex > list.size())
			{
				endIndex = list.size();
			}
		}
		showList = list.subList(startIndex, endIndex);
		request.setAttribute(Constants.SHOW_DOMAIN_OBJECT_LIST, showList);
		request.setAttribute(Constants.PAGE_NUMBER, Integer.toString(pageNum));
		request.setAttribute(Constants.TOTAL_RESULTS, Integer.toString(list.size()));
		request.setAttribute(Constants.RESULTS_PER_PAGE, Integer
				.toString(Constants.NUMBER_RESULTS_PER_PAGE));
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * add the domain object name having new and pending  activity status,in a list.
	 * @param abstractForm AbstractActionForm
	 * @return List list
	 * @throws ApplicationException Application Exception
	 */
	private List getList(AbstractActionForm abstractForm) throws ApplicationException
	{
		List list;
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(abstractForm.getFormId());

			IDomainObjectFactory iDomainObjFact = AbstractFactoryConfig.getInstance()
			.getDomainObjectFactory();
			//If start page is to be shown retrieve the list from the database.

			if (abstractForm.getFormId() == Constants.APPROVE_USER_FORM_ID)
			{
				String[] whereColumnNames = {"activityStatus", "activityStatus"};
				String[] whereColCond = {"=", "="};
				String[] whereColumnValues = {"New", "Pending"};
				list = bizLogic.retrieve(iDomainObjFact.getDomainObjectName(abstractForm
						.getFormId()), whereColumnNames, whereColCond, whereColumnValues,
						Constants.OR_JOIN_CONDITION);
			}
			else
			{
				list = bizLogic.retrieve(iDomainObjFact.getDomainObjectName(abstractForm
						.getFormId()), "activityStatus", "Pending");
			}
		}
		catch (BizLogicException exception)
		{
			LOGGER.error("Problem in object retrieve."+exception.getMessage());
			throw new ApplicationException(exception.getErrorKey(), exception,exception.getMsgValues());
		}
		return list;
	}

	/**
	 * This method gets Object Id.
	 * @param form AbstractActionForm
	 * @return Object Id.
	 */
	protected String getObjectId(AbstractActionForm form)
	{
		return null;
	}

	/**
	 * This method checks is Authorized To Execute.
	 * @param request HttpServletRequest
	 * @return true.
	 */
	protected boolean isAuthorizedToExecute(HttpServletRequest request)
	{
		return true;
	}
}