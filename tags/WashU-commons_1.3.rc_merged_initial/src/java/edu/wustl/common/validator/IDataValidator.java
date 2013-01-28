package edu.wustl.common.validator;

import edu.wustl.common.exception.ApplicationException;


public interface IDataValidator<T>
{

	public void validate(T obj,String operation) throws ApplicationException;
}
