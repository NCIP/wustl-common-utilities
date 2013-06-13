/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.validator;

import edu.wustl.common.exception.ApplicationException;


public interface IDataValidator<T>
{

	public void validate(T obj,String operation) throws ApplicationException;
}
