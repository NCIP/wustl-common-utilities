
package edu.wustl.common.labelSQLApp.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.labelSQLApp.bizlogic.LabelSQLAssociationBizlogic;
import edu.wustl.common.labelSQLApp.form.CPDashboardForm;
import edu.wustl.common.report.ReportGenerator;
import edu.wustl.common.bizlogic.DefaultBizLogic;

import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.query.generator.ColumnValueBean;

/** 
 * @author Ashraf
 *	This is the entry point for the labelSQLApp. CSId or CPId is required.
 */
public class CPDashboardAction extends Action
{

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				"sessionData");
		String isSystemDashBoard = request.getParameter("isSystemDashboard");
		String forward = "success";
		if (isSystemDashBoard != null && isSystemDashBoard.equals("true"))
		{
			if (!sessionDataBean.isAdmin())
			{
				forward = "redirect";
			}
		}
		if (forward.equals("success"))
		{
			loadDashboard(actionForm, request, sessionDataBean.isAdmin(),
					sessionDataBean.getUserId());
		}

		return actionMapping.findForward(forward);
	}

	/**
	 * @param actionForm
	 * @param request
	 * @throws Exception
	 */
	private void loadDashboard(ActionForm actionForm, HttpServletRequest request, boolean isAdmin,
			Long userId) throws Exception
	{
		CPDashboardForm cpDashboardForm = (CPDashboardForm) actionForm;
		String cpId = request.getParameter("cpSearchCpId");
		String participantId = request.getParameter("participantId");
		Long cp = null;
		if (cpId != null)
		{
			cp = Long.valueOf(cpId);
		}
		LinkedHashMap<String, Long> displayNameMap = new LabelSQLAssociationBizlogic()
				.getAssocAndDisplayNameMapByCPId(cp);
		cpDashboardForm.setDisplayNameAndAssocMap(displayNameMap);
		List<NameValueBean> reportNameList = new ArrayList<NameValueBean>();

		if (isAdmin)
		{
			if (cpId != null)
			{
				if(participantId != null && !participantId.equals(""))
				{
					reportNameList = ReportGenerator.getReportNames(cpId, participantId);
					QueryWhereClause queryWhereClause = new QueryWhereClause("edu.wustl.clinportal.domain.Participant");
					queryWhereClause.addCondition(new EqualClause("id", '?'));
					Object[] valueObjects = {Long.valueOf(participantId)};
					List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
					for (Object valueObject : valueObjects)
					{
						columnValueBeans.add(new ColumnValueBean(valueObject));
					}
					request.setAttribute("participantObject", new DefaultBizLogic().retrieve("edu.wustl.clinportal.domain.Participant", null, queryWhereClause,
							columnValueBeans).get(0));
				}
				else
				{
					reportNameList = ReportGenerator.getReportNames(cpId, null);
				}
			}
			else
			{
				reportNameList = ReportGenerator.getSystemReportNames();
			}
		}
		else
		{
			//reportNameList = ReportGenerator.getReportNamesForUSer(Long.valueOf(cpId), userId);
			if(participantId != null && !participantId.equals(""))
			{
				reportNameList = ReportGenerator.getReportNames(cpId, participantId);
				QueryWhereClause queryWhereClause = new QueryWhereClause("edu.wustl.clinportal.domain.Participant");
				queryWhereClause.addCondition(new EqualClause("id", '?'));
				Object[] valueObjects = {Long.valueOf(participantId)};
				List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
				for (Object valueObject : valueObjects)
				{
					columnValueBeans.add(new ColumnValueBean(valueObject));
				}
				request.setAttribute("participantObject", new DefaultBizLogic().retrieve("edu.wustl.clinportal.domain.Participant", null, queryWhereClause,
						columnValueBeans).get(0));
			}
			else
			{
				reportNameList = ReportGenerator.getReportNames(cpId, null);
			}
		}
		request.setAttribute("cpId", cpId);
		request.setAttribute("participantId", participantId);
		request.setAttribute("reportNameList", reportNameList);
	}
}
