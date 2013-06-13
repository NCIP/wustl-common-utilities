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
 * <p>Title: AbstractForwardToProcessor Class>
 * <p>Description:	AbstractForwardToProcessor populates data required for ForwardTo activity</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 08, 2006
 */

package edu.wustl.common.util;

import java.util.Map;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This method populates data required for ForwardTo activity.
 * @author Krunal Thakkar
 */
public abstract class AbstractForwardToProcessor
{

	/**
	 * @param actionForm AbstractActionForm current form.
	 * @param domainObject AbstractDomainObject domain object.
	 * @return Map list of forwarded data.
	 */
	public abstract Map populateForwardToData(AbstractActionForm actionForm,
			AbstractDomainObject domainObject);
}
