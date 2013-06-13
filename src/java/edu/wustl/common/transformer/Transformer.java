/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.transformer;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.dto.AbstractDTOObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * 
 * @author vinod_gaikwad
 *
 */
public interface Transformer
{
	 public AbstractDomainObject transform(AbstractDomainObject abstractDomainObject,IValueObject form) throws AssignDataException;
	 public AbstractDTOObject formToDTOTransform(IValueObject form) throws AssignDataException;
	 public AbstractDomainObject dtoToDomainTransform(AbstractDTOObject abstractDtonObject, AbstractDomainObject abstractDomainObject) throws AssignDataException;
}
