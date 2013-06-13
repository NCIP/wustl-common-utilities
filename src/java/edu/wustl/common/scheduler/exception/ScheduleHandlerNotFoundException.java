/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.exception;


public class ScheduleHandlerNotFoundException extends Exception
{
private static final long serialVersionUID = -1458325467226137898L;
	
	public ScheduleHandlerNotFoundException(String message)
	{
		super(message);
	}
}
