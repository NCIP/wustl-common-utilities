/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.labelSQLApp.exception;

import java.io.Serializable;

public class LabelSQLAppException extends Exception implements Serializable
{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	public LabelSQLAppException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public LabelSQLAppException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LabelSQLAppException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LabelSQLAppException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
