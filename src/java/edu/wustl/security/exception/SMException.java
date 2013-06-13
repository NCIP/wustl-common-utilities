/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.exception;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

/**
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SMException extends ApplicationException
{
	/**
	 *
	 * @param errorKey eror key
	 * @param exception exc
	 * @param msgValues meg
	 */
	public SMException(final ErrorKey errorKey, final Exception exception, final String msgValues)
	{
		super(errorKey, exception, msgValues);
	}

	/**
	 * serial version id.
	 */
	private static final long serialVersionUID = 1998965888442573900L;
}
