
package edu.wustl.common.actionForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.domain.AbstractDomainObject;

public class ReportForm extends AbstractActionForm
{

	private static final long serialVersionUID = 8115557815741081465L;
	
	private String fromDate;
	private String toDate;
	private String reportName;
	//private String reportConditions;
	private List<String> reportNameList = new ArrayList<String>();

	@Override
	public int getFormId()
	{
		return 77;
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public String getReportName()
	{
		return reportName;
	}

	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	//	public String getReportConditions()
	//	{
	//		return reportConditions;
	//	}
	//
	//
	//
	//	
	//	public void setReportConditions(String reportConditions)
	//	{
	//		this.reportConditions = reportConditions;
	//	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		return null;

	}

}
