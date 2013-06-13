/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

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
