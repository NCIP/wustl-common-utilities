/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.codegen.validator;

import java.util.List;

public class PKGeneratorProperty {

	private String generatorName; 
	private List<String> dataTypes;
	private List<String> databaseTypes;
	private List<String> pkGeneratorParams;

	public PKGeneratorProperty(String generatorName,List<String> dataTypes,List<String> databaseTypes,List<String> pkGeneratorParams) {
		this.generatorName=generatorName;
		this.dataTypes = dataTypes;
		this.databaseTypes = databaseTypes;
		this.pkGeneratorParams=pkGeneratorParams;
	}
	
	public List<String> getDatabaseTypes() {
		return databaseTypes;
	}
	
	public String getGeneratorName() {
		return generatorName;
	}
	
	public List<String> getDataTypes() {
		return dataTypes;
	}
	
	public List<String> getPkGeneratorParams() {
		return pkGeneratorParams;
	}

	public String toString() {
		final String TAB = "    ";
		String retValue = "";
		retValue = "PKGeneratorProperty ( " + super.toString() + TAB
				+ "generatorName = " + this.generatorName + TAB
				+ "dataTypes = " + this.dataTypes + TAB + "databaseTypes = "
				+ this.databaseTypes + TAB + "pkGeneratorParams = "
				+ this.pkGeneratorParams + TAB + " )";

		return retValue;
	}
}
