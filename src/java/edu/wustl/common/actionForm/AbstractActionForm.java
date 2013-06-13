/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * <p>Title: AbstractForm Class>
 * <p>Description:  AbstractForm class is the superclass of all the formbean classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;

/**
 * AbstractForm class is the superclass of all the formbean classes.
 * @author gautam_shetty
 */
public abstract class AbstractActionForm extends ActionForm implements IValueObject
{

	/**
	 * Serial Version Unique Identifier.
	 */
	private static final long serialVersionUID = 1815115874068324152L;

	/**
	 * Specifies whether the id is mutable or not.
	 */
	private boolean mutable = true;

	/**
	 * Specifies unique identifier.
	 * */
	private long id;

	/**
	 * Specifies the page associated with this form bean.
	 */
	private String pageOf;

	/**
	 * A String containing the operation(Add/Edit) to be performed.
	 * */
	private String operation;

	/**
	 * Activity Status.
	 */
	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();

	/**
	 * Specifies object Id.
	 */
	private String objectId = "";

	/**
	 * @return Returns the mutable.
	 */
	public boolean isMutable()
	{
		return mutable;
	}

	/**
	 * @param mutable The mutable to set.
	 */
	public void setMutable(boolean mutable)
	{
		this.mutable = mutable;
	}

	/**
	 * Returns unique identifier.
	 * @return unique identifier.
	 * @see #setId(long)
	 * */
	public long getId()
	{
		return id;
	}

	/**
	 * Sets unique identifier.
	 * @param identifier unique identifier.
	 * @see #getId()
	 * */
	public void setId(long identifier)
	{
		if (isMutable())
		{
			this.id = identifier;
		}
	}

	/**
	 * Returns the operation(Add/Edit) to be performed.
	 * @return Returns the operation.
	 * @see #setOperation(String)
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * Sets the operation to be performed.
	 * @param operation The operation to set.
	 * @see #getOperation()
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	/**
	 * Checks the operation to be performed is add operation.
	 * @return Returns true if operation is equal to "add", else it returns false
	 * */
	public boolean isAddOperation()
	{
		return operation.equals(Constants.ADD);
	}

	/**
	 * Returns specified page associated with this form bean.
	 * @return specified page .
	 */
	public String getPageOf()
	{
		return pageOf;
	}

	/**
	 * Sets the page associated with this form bean.
	 * @param pageOf the page name to associate with this form bean.
	 */
	public void setPageOf(String pageOf)
	{
		this.pageOf = pageOf;
	}

	/**
	 * This method gets the activity status.
	 * @return the activity Status.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * This method sets the activity status.
	 * @param activityStatus activity status.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the id assigned to form bean.
	 * @return the id assigned to form bean
	 */
	public abstract int getFormId();

	/**
	 * This method validates component for string value and
	 * updates ActionError object accordingly.
	 * @param componentName Component which is to be checked.
	 * @param labelName Label of the component on the jsp page which is checked.
	 * @param errors ActionErrors Object.
	 * @param validator Validator Object.
	 */
	protected void checkValidString(String componentName, String labelName, ActionErrors errors,
			Validator validator)
	{
		if (Validator.isEmpty(componentName))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(labelName)));
		}
		else
		{
			if (!validator.isAlpha(componentName))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue(labelName)));
			}
		}
	}

	/**
	 * This method validates component for numeric value and
	 * updates ActionError object accordingly.
	 * @param componentName Component which is to be checked.
	 * @param labelName Label of the component on the jsp page which is checked.
	 * @param errors ActionErrors Object.
	 * @param validator Validator Object.
	 */
	protected void checkValidNumber(String componentName, String labelName, ActionErrors errors,
			Validator validator)
	{
		if (Validator.isEmpty(componentName))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(labelName)));
		}
		else
		{
			if (!validator.isNumeric(componentName))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue(labelName)));
			}
		}
	}

	/**
	 * Resets the values of all the fields.
	 * This method defined in ActionForm is overridden in this class.
	 * @param mapping ActionMapping object.
	 * @param request HttpServletRequest object.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		reset();
	}

	/**
	 * Resets the values.
	 */
	protected abstract void reset();

	/**
	 *  Specifies redirectTo.
	 */
	private String redirectTo = null;

	/**
	 * @return Returns the redirectTo.
	 */
	public String getRedirectTo()
	{
		return redirectTo;
	}

	/**
	 * @param redirectTo The redirectTo to set.
	 */
	public void setRedirectTo(String redirectTo)
	{
		this.redirectTo = redirectTo;
	}

	/**
	 * Set Redirect Value.
	 * @param validator Validator object.
	 */
	public void setRedirectValue(Validator validator)
	{
		String redirectValue = getRedirectTo();
		if (Validator.isEmpty(redirectValue))
		{
			redirectValue = "";
		}
		setRedirectTo(redirectValue);
	}

	// -------- For success pages

	/**
	 * Specifies onSubmit.
	 */
	private String onSubmit;

	/**
	 * @return Returns the onSubmit.
	 */
	public String getOnSubmit()
	{
		return onSubmit;
	}

	/**
	 * @param onSubmit The onSubmit to set.
	 */
	public void setOnSubmit(String onSubmit)
	{
		this.onSubmit = onSubmit;
	}

	/**
	 * Specifies forwardTo.
	 */
	private String forwardTo = "success";

	/**
	 * @return Returns the forwardTo.
	 */
	public String getForwardTo()
	{
		return forwardTo;
	}

	/**
	 * @param forwardTo The forwardTo to set.
	 */
	public void setForwardTo(String forwardTo)
	{
		this.forwardTo = forwardTo;
	}

	/**
	 * Specifies submittedFor.
	 */
	private String submittedFor = null;

	/**
	 * @return Returns submittedFor value
	 */
	public String getSubmittedFor()
	{
		return submittedFor;
	}

	/**
	 * @param submittedFor - submittedFor value
	 */
	public void setSubmittedFor(String submittedFor)
	{
		this.submittedFor = submittedFor;
	}

	/**
	 * This method set Identifier of newly added object by AddNew operation into FormBean
	 * which initiliazed AddNew operation.
	 * @param addNewFor - add New For.
	 * @param addObjectIdentifier - Identifier of newly added object by AddNew operation
	 */
	public abstract void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier);

	/**
	 * Gets object id.
	 * @return object Id.
	 */
	public String getObjectId()
	{
		return objectId;
	}

	/**
	 * Sets object Id.
	 * @param objectId object Id.
	 */
	public void setObjectId(String objectId)
	{
		this.objectId = objectId;
	}

}