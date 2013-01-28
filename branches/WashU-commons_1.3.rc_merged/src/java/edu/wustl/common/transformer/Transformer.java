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
