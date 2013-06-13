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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeneratorErrors
{
	private List<GeneratorError> errors;
	
	public GeneratorErrors()
	{
		errors = new ArrayList<GeneratorError>();
	}
	
	public void addError(GeneratorError error)
	{
		if(error!=null)
			this.errors.add(error);
	}
	
	public void addErrors(GeneratorErrors errors)
	{
		if(errors!=null)
			this.errors.addAll(errors.getErrors());
	}
	
	public Collection<GeneratorError> getErrors()
	{
		return this.errors;	
	}

	public boolean isEmpty()
	{
		return this.errors.isEmpty();	
	}	
}