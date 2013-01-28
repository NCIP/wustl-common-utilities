package edu.wustl.common.factory;

import edu.wustl.common.validator.IDataValidator;


public interface IDataValidatorFactory {

	public IDataValidator getDataValidator(String domainObjectClassName);
}
