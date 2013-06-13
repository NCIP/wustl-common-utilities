/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao.newdao;


public enum ActionStatus 
{
	SUCCESSFUL(true),FAIL(false);
	
	public static String ACTIONSTAUS = "ACTIONSTAUS";
	private boolean isSuccessAction;
	
	
	private ActionStatus(boolean isSuccessAction)
	{
		this.isSuccessAction = isSuccessAction;
	}
	public boolean isSuccessAction()
	{
		return isSuccessAction;
	}

	public boolean isFailureAction()
	{
		return !isSuccessAction;
	}

}
