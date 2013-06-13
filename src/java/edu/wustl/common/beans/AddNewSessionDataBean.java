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
 * <p>Title: AddNewAction Class>
 * <p>Description:	This Class is used to maintain FormBean for AddNew operation</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on Apr 12, 2006
 */

package edu.wustl.common.beans;

import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * This Class is used to maintain FormBean for AddNew operation.
 * @author Krunal Thakkar
 */
public class AddNewSessionDataBean
{

	/**
	 * Specifies AbstractActionForm object.
	 */
	private AbstractActionForm abstractActionForm;

	/**
	 * Specifies forward To.
	 */
	private String forwardTo;

	/**
	 * Specifies add New For.
	 */
	private String addNewFor;

	/**
	 * @return Returns abstractActionForm
	 */
	public AbstractActionForm getAbstractActionForm()
	{
		return this.abstractActionForm;
	}

	/**
	 * @param abstractActionForm The abstractActionForm to set
	 */
	public void setAbstractActionForm(AbstractActionForm abstractActionForm)
	{
		this.abstractActionForm = abstractActionForm;
	}

	/**
	 * @return Returns redirectToPath
	 */
	public String getForwardTo()
	{
		return this.forwardTo;
	}

	/**
	 * @param forwardTo The forwardTo to set.
	 */
	public void setForwardTo(String forwardTo)
	{
		this.forwardTo = forwardTo;
	}

	/**
	 * @return Returns addNewFor
	 */
	public String getAddNewFor()
	{
		return this.addNewFor;
	}

	/**
	 * @param addNewFor The addNewFor to set
	 */
	public void setAddNewFor(String addNewFor)
	{
		this.addNewFor = addNewFor;
	}
}
