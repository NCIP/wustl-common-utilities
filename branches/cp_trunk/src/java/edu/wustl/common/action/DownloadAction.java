
package edu.wustl.common.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.scheduler.bizLogic.ReportAuditDataBizLogic;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.exception.FileCleanedException;
import edu.wustl.common.scheduler.exception.NotAuthorizedToDownloadException;
import edu.wustl.common.scheduler.util.ReportSchedulerUtil;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class DownloadAction extends SecureAction
{

	private static final Logger log = Logger.getCommonLogger(DownloadAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws BizLogicException
	{
		Long fileId = Long.valueOf(request.getParameter("file"));
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);
		ReportAuditDataBizLogic fileBiz = new ReportAuditDataBizLogic();
		ReportAuditData fileDetail = null;
		String fileName = "";
		String message = "";
		Boolean isAuthorized = true;
		String isMessage = request.getParameter("message");
		try
		{
			fileDetail = ((ReportAuditData) fileBiz.retrieve(ReportAuditData.class.getName(),
					fileId));
			fileName = fileDetail.getFileName();
			ReportSchedulerUtil.validateFile(fileName, sessionDataBean.getUserId());
		}
		catch (NotAuthorizedToDownloadException e)
		{
			message = "You are not authorized to download this file.";
			log.error(e.getMessage());
			isAuthorized = false;
		}
		catch (FileCleanedException e)
		{
			message = "The file has already been deleted.";
			log.error(e.getMessage());
			isAuthorized = false;
		}
		finally
		{
			if (!message.equals(""))
			{
				try
				{
					response.getWriter().write(message);
				}
				catch (IOException e)
				{
					log.error(e.getMessage());
				}
			}
		}

		if(isAuthorized)
		{
			try
			{
				Utility.sendFile(response, fileName);

				if (isMessage == null || !isMessage.equals("true"))
				{
					if (fileDetail.getDownloadCount() == null)
					{
						fileDetail.setDownloadCount((long) 0);
					}
					fileDetail.setDownloadCount(fileDetail.getDownloadCount() + 1);
					fileBiz.update(fileDetail);
				}
			}
			catch (Exception e)
			{
				log.error(e.getMessage());
			}
		}

		return null;
	}

}
