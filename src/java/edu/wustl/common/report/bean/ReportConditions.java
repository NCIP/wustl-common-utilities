package edu.wustl.common.report.bean;



public enum ReportConditions {
	BETWEEN("Between");
	
	private String conditionValue;
	
	ReportConditions(String conditionValue)
	{
		this.conditionValue = conditionValue;
	}
	
	public String getConditionValue()
	{
		return this.conditionValue;
	}
}
