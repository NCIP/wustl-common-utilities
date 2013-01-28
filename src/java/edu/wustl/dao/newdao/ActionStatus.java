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
