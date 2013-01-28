
package edu.wustl.common.labelSQLApp.form;

import java.util.LinkedHashMap;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

public class CPDashboardForm extends AbstractActionForm
{

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Collection of query labels
	 */
	private LinkedHashMap<String, Integer> labelQueryResultMap;

	private String cpId;

	private LinkedHashMap<String, Long> displayNameAndAssocMap;

	private int queryResult;

	public String getCpId()
	{
		return cpId;
	}

	public void setCpId(String cpId)
	{
		this.cpId = cpId;
	}

	public LinkedHashMap<String, Integer> getLabelQueryResultMap()
	{
		return labelQueryResultMap;
	}

	public void setLabelQueryResultMap(LinkedHashMap<String, Integer> labelQueryResultMap)
	{
		this.labelQueryResultMap = labelQueryResultMap;
	}

	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

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

	/**
	 * @return the displayNameAnsAssocMap
	 */
	public LinkedHashMap<String, Long> getDisplayNameAndAssocMap()
	{
		return displayNameAndAssocMap;
	}

	/**
	 * @param displayNameAnsAssocMap the displayNameAnsAssocMap to set
	 */
	public void setDisplayNameAndAssocMap(LinkedHashMap<String, Long> displayNameAndAssocMap)
	{
		this.displayNameAndAssocMap = displayNameAndAssocMap;
	}

	/**
	 * @return the queryResult
	 */
	public int getQueryResult()
	{
		return queryResult;
	}

	/**
	 * @param queryResult the queryResult to set
	 */
	public void setQueryResult(int queryResult)
	{
		this.queryResult = queryResult;
	}

}
