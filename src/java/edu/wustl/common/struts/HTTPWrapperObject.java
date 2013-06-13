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
 * <p>Title: HTTPWrapperObject Class>
 * <p>Description:	This class provides a wrapper object which constitutes
 * an object of AbstractDomainObject & operation that is to be performed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 20, 2005
 */

package edu.wustl.common.struts;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;

/**
 * This class provides a wrapper object which constitutes
 * an object of AbstractDomainObject & operation that is to be performed.
 * @author Aniruddha Phadnis
 */
public class HTTPWrapperObject implements Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -4958330782397508598L;

	/**
	 * Specifies form Bean.
	 */
	private ActionForm formBean;

	/**
	 * Specifies operation.
	 */
	private String operation;

	/**
	 * Specifies constructor.
	 */
	public HTTPWrapperObject()
	{
	}

	/**
	 * constructor.
	 * @param domainObject Object
	 * @param operation operation
	 * @throws ApplicationException Application Exception
	 */
	public HTTPWrapperObject(Object domainObject, String operation) throws ApplicationException
	{
		//Gautam: Changes done for common package.
		IDomainObjectFactory actionFormFactory = AbstractFactoryConfig.getInstance()
		.getDomainObjectFactory();
		AbstractActionForm abstractForm = actionFormFactory.getFormBean(domainObject, operation);
		formBean = abstractForm;

		this.operation = operation;
	}

	/**
	 * Returns FormBean object.
	 * @return form Bean.
	 */
	public ActionForm getForm()
	{
		return formBean;
	}

	/**
	 * Returns Operation value.
	 * @return operation
	 */
	public String getOperation()
	{
		return this.operation;
	}
}