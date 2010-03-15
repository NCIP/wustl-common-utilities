/**
 * <p>Title: AbstractDomain Class>
 * <p>Description:  AbstractDomain class is the superclass of all the domain classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Identifiable;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * AbstractDomain class is the superclass of all the domain classes.
 * @author gautam_shetty
 */
public abstract class AbstractDomainObject implements Serializable,Identifiable
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * This method gets Object Id.
	 * @return Return Object Id.
	 */
	public String getObjectId()
	{
		return this.getClass().getName() + "_" + this.getId();
	}

	/**
	 * Parses the fully qualified class  name and returns only the class name.
	 * @param qualifiedName The fully qualified classname.
	 * @return The qualified class name.
	 */
	public static String parseClassName(String qualifiedName)
	{
		return CommonUtilities.parseClassName(qualifiedName);
	}
	/**
	 * Copies all values from the AbstractForm object
	 * @param abstractForm The AbstractForm object
	 */
	//  public abstract void setAllValues(AbstractActionForm abstractForm) throws AssignDataException;

	/**
	 * Copies all values from the AbstractForm object and set it in relevant variable.
	 * @param valueObject The IValueObject.
	 * @throws AssignDataException Assign Data Exception.
	 */
	public void setAllValues(IValueObject valueObject)throws AssignDataException {
        throw new UnsupportedOperationException();
    }

	/**
	 * Returns the unique system identifier assigned to the domain object.
	 * @return returns a unique system identifier assigned to the domain object.
	 * @see #setId(Long)
	 * */
	public abstract Long getId();

	/**
	 * Sets an system Identifier for the domain object.
	 * @param identifier Identifier for the domain object.
	 * @see #getId()
	 * */
	public abstract void setId(Long identifier);

	/**
	 * Returns message label to display on success of add or edit.
	 * @return message.
	 */
	public String getMessageLabel()
	{
		return getId().toString();
	}
}