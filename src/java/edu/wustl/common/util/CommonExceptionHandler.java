/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import edu.wustl.common.beans.EmailFormatBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 *
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class CommonExceptionHandler extends ExceptionHandler
{
    /**
     * LOGGER Logger - Generic LOGGER.
     */
    private static final Logger LOGGER = Logger
            .getCommonLogger(CommonExceptionHandler.class);

    /**
     * Retrieve the error details from request and set it in session.
     *
     * @param exception
     *            Exception generic exception.
     * @param exConfig
     *            ExceptionConfig configuration exception.
     * @param mapping
     *            ActionMapping information about current page.
     * @param formInstance
     *            ActionForm populated form.
     * @param request
     *            HttpServletRequest information about request.
     * @param response
     *            HttpServletResponse information about response.
     *
     * @return the output of execute method.
     *
     * @throws ServletException
     *             servlet exception.
     */
    public ActionForward execute(Exception exception,
            ExceptionConfig exConfig,
            ActionMapping mapping,
            ActionForm formInstance, HttpServletRequest request,
            HttpServletResponse response) throws ServletException
    {
        LOGGER.error(getErrorMsg(exception), exception);
        StringBuffer mess = new StringBuffer("Unhandled Exception occured in ")
                .append(CommonServiceLocator.getInstance().getAppURL()).append(
                        " : ").append(exception.getMessage());
        request.getSession().setAttribute(Constants.ERROR_DETAIL, mess);

        // send an email.
        SessionDataBean sessionDataBean = (SessionDataBean) request
                .getSession().getAttribute(Constants.SESSION_DATA);
        EmailFormatBean emailFormatBean = new EmailFormatBean();
        emailFormatBean.setException(exception);
        emailFormatBean.setUserId(sessionDataBean.getUserId());
        StringBuilder username = new StringBuilder();
        username.append(sessionDataBean.getLastName());
        username.append(",");
        username.append(sessionDataBean.getFirstName());
        emailFormatBean.setUserName(username.toString());
        SendEmailUtility.sendEmailOnGlobalException(emailFormatBean);
        return super.execute(exception, exConfig, mapping, formInstance,
                request, response);
    }

	/**
     * Gets the error msg.
     *
     * @param exception
     *            the Exception.
     *
     * @return the string of the error message.
     */
	public String getErrorMsg(final Exception exception)
	{
		StringBuffer msg=new StringBuffer();
		if (null==exception)
		{
			msg.append("Exception was NULL");
		}
		else
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter printWriter = new PrintWriter(baos, true);
			exception.printStackTrace(printWriter);
			msg.append("Unhandled Exception occured in application :- \nMessage: ")
			.append(exception.getMessage())
			.append("\nStackTrace: ").append(baos.toString());
		}
		return msg.toString();
	}
}
