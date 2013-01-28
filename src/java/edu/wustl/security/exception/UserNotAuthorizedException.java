
package edu.wustl.security.exception;

import edu.wustl.common.exception.ErrorKey;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class UserNotAuthorizedException extends SMException
{
	/**
	 * Privilege name.
	 */
	private String privilegeName;
	/**
	 * Base object.
	 */
	private String baseObject;
	/**
	 * baseObjectIdentifier.
	 */
	private String baseObjectIdentifier;

	 /**
	  * domainObjectName
	  */
    private String domainObjectName;   
  
    /**
     * 
     * @return domain Object Name.
     */
	public String getDomainObjectName() 
	{
		return domainObjectName;
	}

	/**
	 * The domainObjectName to set.
	 * @param domainObjectName domain Object Name.
	 */
	public void setDomainObjectName(String domainObjectName) 
	{
		this.domainObjectName = domainObjectName;
	}
	/**
	 * @return Returns the baseObject.
	 */
	public String getBaseObject()
	{
		return baseObject;
	}

	/**
	 * @param baseObject The baseObject to set.
	 */
	public void setBaseObject(String baseObject)
	{
		this.baseObject = baseObject;
	}

	/**
	 * @return Returns the baseObjectIdentifier.
	 */
	public String getBaseObjectIdentifier()
	{
		return baseObjectIdentifier;
	}

	/**
	 * @param baseObjectIdentifier The baseObjectIdentifier to set.
	 */
	public void setBaseObjectIdentifier(String baseObjectIdentifier)
	{
		this.baseObjectIdentifier = baseObjectIdentifier;
	}

	/**
	 * @return Returns the privilegeName.
	 */
	public String getPrivilegeName()
	{
		return privilegeName;
	}

	/**
	 *
	 * @param errorKey eror key
	 * @param exception exc
	 * @param msgValues meg
	 */
	public UserNotAuthorizedException(final ErrorKey errorKey, final Exception exception,
			final String msgValues)
	{
		super(errorKey, exception, msgValues);
	}

	/**
	 * @param privilegeName The privilegeName to set.
	 */
	public void setPrivilegeName(String privilegeName)
	{
		this.privilegeName = privilegeName;
	}
	/* public UserNotAuthorizedException() {
	//	super();
	}
	*//**
		 * @param message
		 */
	/*
		public UserNotAuthorizedException(String message) {
			super(message);
		}
		*//**
		 * @param message
		 * @param cause
		 */
	/*
		public UserNotAuthorizedException(String message, Throwable cause) {
		//	super(message, cause);
		}
		*//**
		 * @param cause
		 */
	/*
		public UserNotAuthorizedException(Throwable cause) {
		//	super(cause);
		}*/
}
