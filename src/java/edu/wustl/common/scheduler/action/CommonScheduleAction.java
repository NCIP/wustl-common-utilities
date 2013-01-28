
package edu.wustl.common.scheduler.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.scheduler.bizLogic.ScheduleBizLogic;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.exception.ScheduleValidationException;
import edu.wustl.common.scheduler.util.ReportSchedulerUtil;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.dao.newdao.ActionStatus;

public class CommonScheduleAction extends DispatchAction
{

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward populateScheduleGrid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);
		Boolean isSysDashboard = request.getParameter("isSysDashboard").equals("true")
				? true
				: false;
		ScheduleBizLogic schedBiz = new ScheduleBizLogic();
		response.getOutputStream().print(
				SchedulerDataUtility.getJSONFromScheduleList(
						schedBiz.filterSchedules(schedBiz.getSchedulesByOwner(
								request.getParameter(SchedulerConstants.SCHEDULE_TYPE),
								sessionDataBean.getUserId()), isSysDashboard, Long.valueOf(request
								.getParameter("id")))).toString());

		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id = Long.valueOf(request.getParameter(SchedulerConstants.ID));
		new ScheduleBizLogic().markDeleted(id);
		request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addAndSchedule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonFormData = new JSONObject(request.getParameter(SchedulerConstants.FORM_JSON));
		JSONObject responseJson = new JSONObject();
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);
		try
		{
			ScheduleBizLogic schedBizLogic = new ScheduleBizLogic();
			schedBizLogic.insertAndSchedule(SchedulerDataUtility.getScheduleFromJsonMap(
					jsonFormData, sessionDataBean.getUserId()));
			responseJson.append(SchedulerConstants.TYPE, SchedulerConstants.SUCCESS);
			if (jsonFormData.getString(SchedulerConstants.SCHEDULE_TYPE).equalsIgnoreCase(
					SchedulerConstants.REPORT_SCHEDULE))
			{
				responseJson.append(
						SchedulerConstants.MESSAGE,
						"Report Schedule"
								+ SchedulerConstants.DATA_SAVED);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			responseJson.append(SchedulerConstants.TYPE, SchedulerConstants.ERROR);
			responseJson.append(
					SchedulerConstants.MESSAGE,
					jsonFormData.getString(SchedulerConstants.SCHEDULE_TYPE).replace(
							"edu.wustl.common.scheduler.domain.", "")
							+ SchedulerConstants.DATA_NOT_SAVED);

		}
		response.getOutputStream().print(responseJson.toString());
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws ApplicationException
	 * @throws ScheduleValidationException
	 */
	public ActionForward populateSavedQueriesDropDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws JSONException,
			IOException, ApplicationException, ScheduleValidationException
	{
		/*SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);
		response.getOutputStream().print(
				SchedulerDataUtility.getQueryNameListJSON(request, sessionDataBean).toString());*/
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public ActionForward populateUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		response.getOutputStream().print(
				SchedulerDataUtility.getUserNameListJSON(request).toString());
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public ActionForward getCurrentUserId(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws JSONException,
			IOException, ApplicationException
	{
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);
		response.getOutputStream().print(sessionDataBean.getUserId());
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws ApplicationException
	 * @throws ScheduleValidationException
	 */
	public ActionForward populateSavedReportsDropDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws JSONException,
			IOException, ApplicationException, ScheduleValidationException
	{

		Long csId = Long.parseLong(request.getParameter("csId"));
		response.getOutputStream().print(
				SchedulerDataUtility.getReportNameListJSON(request, csId).toString());
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward populateDuration(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		response.getOutputStream().print(
				ReportSchedulerUtil.getDurationListJSON(request).toString());
		return null;
	}

}
