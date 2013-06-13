/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.codegen;

public class GeneratorError
{
	private String message;
	private GenerationException ge;
	
	public GeneratorError(String message, GenerationException ge) {
		this.message = message;
		this.ge = ge;
	}
	
	public GeneratorError(String message)
	{
		this.message = message;
	}
	
	public String toString()
	{
		return message + (ge == null ? "" :" : "+ge.getMessage());
	}
}