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
 * Created on May 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonFileReader;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ApplicationFooterAction extends XSSSupportedAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ApplicationFooterAction.class);

	/**
	 * @param mapping ActionMapping
	 * @param form	ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		LOGGER.info("in execute method");
		ActionForward forward = mapping.findForward(Constants.FAILURE);
		String pageTitle = (String) request.getParameter("PAGE_TITLE_KEY");
		String fileNameKey = (String) request.getParameter("FILE_NAME_KEY");
		if (isUserInputValid(pageTitle, fileNameKey))
		{
			String fileName = XMLPropertyHandler.getValue(fileNameKey);
			CommonFileReader reader = new CommonFileReader();
			StringBuffer filePath = new StringBuffer();
			filePath.append(CommonServiceLocator.getInstance().getPropDirPath())
			.append(File.separator).append(fileName);
			String contents = reader.readData(filePath.toString());
			request.setAttribute("CONTENTS", contents);
			request.setAttribute("PAGE_TITLE", pageTitle);
			forward = mapping.findForward(Constants.SUCCESS);
		}

		return forward;
	}

	/**
	 * Check whether the page title is empty or it contains any vulnerable character.
	 * @param pageTitle page title like app.privacyNotice,app.contactUs...
	 * @param fileNameKey key for file name
	 * @return true if pageTitle and fileNameKey are valid or false
	 */
	private boolean isUserInputValid(String pageTitle, String fileNameKey)
	{
		boolean isValid = true;
		//Added check for xss vulnerable character for bug:8583
		if (Validator.isEmpty(pageTitle) || Validator.isXssVulnerable(pageTitle))
		{
			isValid = false;
		}
		if (Validator.isEmpty(fileNameKey) || Validator.isXssVulnerable(fileNameKey))
		{
			isValid = false;
		}
		return isValid;
	}
}