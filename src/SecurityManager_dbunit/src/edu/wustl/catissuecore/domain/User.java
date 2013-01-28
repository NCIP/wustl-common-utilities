/**
 * <p>Title: User </p>
 * <p>Description: A person who interacts with the caTISSUE Core 
 * data system and/or participates in the process of biospecimen 
 * collection, processing, or utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * A person who interacts with the caTISSUE Core data system
 * and/or participates in the process of biospecimen collection,
 * processing, or utilization.
 * @hibernate.class table="CATISSUE_USER"
 */
public class User extends AbstractDomainObject 
{

    /**
     * System generated unique id.
     */
    protected Long id;

    //Change for API Search   --- Ashwin 04/10/2006
    /**
     * A string containing the Last Name of the user.
     */
    protected String lastName;

    //Change for API Search   --- Ashwin 04/10/2006
    /**
     * A string containing the First Name of the user.
     */   
    protected String firstName;

    //Change for API Search   --- Ashwin 04/10/2006
    /**
     * A string containing the login name of the user.
     */
    protected String loginName;

    //Change for API Search   --- Ashwin 04/10/2006
    /**
     * EmailAddress of the user.
     */
    protected String emailAddress;
    
    /**
     * Old password of this user.
     */
    protected String oldPassword;
    
    /**
     * EmailAddress Address of the user.
     */
    protected String newPassword;

    /**
     * Date of user registration.
     */
    protected Date startDate;

    //Change for API Search   --- Ashwin 04/10/2006
   
       /**
     * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED.
     */
    protected String activityStatus;

    /**
     * Comments given by the approver.
     */
    protected String comments;

    /**
     * Role id of the user.
     */
    protected String roleId= "";
    
    /**
     * Set of collection protocol.
     */
    protected Collection collectionProtocolCollection = new HashSet();
    
    protected Collection clinicalStudyCollection = new HashSet();
    
    protected String pageOf;
    
    /**
     * Identifier of this user in csm user table. 
     */
    protected Long csmUserId;
    
    //Change for API Search   --- Ashwin 04/10/2006
    /**
	 * whether user is logging for the first time
	 */
	protected Boolean firstTimeLogin;
	
	

    /**
     * Initialize a new User instance.
     * Note: Hibernate invokes this constructor through reflection API.  
     */
    
    /**
     * Set of passwod collection for the user.
     */
    protected Collection passwordCollection = new HashSet();
    

   

    /**
     * Returns the id assigned to user.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_USER_SEQ"
     * @return Returns the id.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
	 * Returns the password assigned to user.
	 * @hibernate.property name="emailAddress" type="string" column="EMAIL_ADDRESS" length="255"
	 * @return Returns the password.
	 */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * @return Returns the oldPassword.
     */
    public String getOldPassword()
    {
        return oldPassword;
    }
    
    /**
     * @param oldPassword The oldPassword to set.
     */
    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }
    
    //@hibernate.property name="password" type="string" column="PASSWORD" length="50"
    /**
	 * Returns the newPassword assigned to user.
	 * @return Returns the newPassword.
	 */
    public String getNewPassword()
    {
        return newPassword;
    }
    
    /**
     * @param newPassword The new Password to set.
     */
    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }
    
    /**
	 * Returns the firstname assigned to user.
	 * @hibernate.property name="firstName" type="string" column="FIRST_NAME" length="255"
	 * @return Returns the firstName.
	 */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    /**
	 * Returns the lastname assigned to user.
	 * @hibernate.property name="lastName" type="string" column="LAST_NAME" length="255"
	 * @return Returns the lastName.
	 */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
	 * Returns the loginname assigned to user.
	 * @hibernate.property name="loginName" type="string" column="LOGIN_NAME" length="255" 
	 * not-null="true" unique="true"
	 * @return Returns the loginName.
	 */
    public String getLoginName()
    {
        return loginName;
    }

    /**
     * @param loginName The loginName to set.
     */
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    /**
	 * Returns the date when the user is added to the system.
	 * @hibernate.property name="startDate" type="date" column="START_DATE"
	 * @return Returns the dateAdded.
	 */
    public Date getStartDate()
    {
        return startDate;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Returns the activitystatus of the user.
     * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
     * @return Returns the activityStatus.
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * @param activityStatus The activityStatus to set.
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

   
    /**
     * @return Returns the collectionProtocolCollection.
     * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLL_COORDINATORS" 
     * cascade="save-update" inverse="true" lazy="true"
     * @hibernate.collection-key column="USER_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
     */
    public Collection getCollectionProtocolCollection()
    {
        return collectionProtocolCollection;
    }

    /**
     * @param collectionProtocolCollection The collectionProtocolCollection to set.
     */
    public void setCollectionProtocolCollection(
            Collection collectionProtocolCollection)
    {
        this.collectionProtocolCollection = collectionProtocolCollection;
    }

    /**
     * @return Returns the pageOf.
     */
    public String getPageOf()
    {
        return pageOf;
    }
    
    /**
     * @param pageOf The pageOf to set.
     */
    public void setPageOf(String pageOf)
    {
        this.pageOf = pageOf;
    }
    
    /**
	 * Returns the password assigned to user.
	 * @hibernate.property name="csmUserId" type="long" column="CSM_USER_ID" length="20"
	 * @return Returns the password.
	 */
    public Long getCsmUserId()
    {
        return csmUserId;
    }
    
    /**
     * @param csmUserId The csmUserId to set.
     */
    public void setCsmUserId(Long csmUserId)
    {
        this.csmUserId = csmUserId;
    }
    
    //	/**
    //     * Returns the comments given by the approver. 
    //     * @return the comments given by the approver.
    //     * @see #setCommentClob(String)
    //     */
    //    public Clob getCommentClob()
    //    {
    //        return commentClob;
    //    }
    //    
    //    /**
    //     * Sets the comments given by the approver.
    //     * @param comments the comments given by the approver.
    //     * @see #getCommentClob() 
    //     */
    //    public void setCommentClob(Clob commentClob) throws SQLException
    //    {
    //        if (commentClob == null)
    //        {
    //            comments = "";
    //            commentClob = null;
    //        }
    //        else
    //        {
    //            this.commentClob = commentClob;
    //            this.comments = commentClob.getSubString(1L,(int)commentClob.length());
    //        }
    //    }

    /**
     * Returns the comments given by the approver. 
     * @hibernate.property name="comments" type="string" 
     * column="STATUS_COMMENT" length="2000" 
     * @return the comments given by the approver.
     * @see #setComments(String)
     */
    public String getComments()
    {
        return comments;
    }
    /**
     * Sets the comments given by the approver.
     * @param comments The comments to set.
     * @see #getComments()
     */
    public void setComments(String commentString)
    {
    	this.comments = "";
        if (commentString != null)
        {
        	this.comments = commentString;        
        }
    }
   
    
    /**
     * @hibernate.set name="passwordCollection" table="CATISSUE_PASSWORD"
     * cascade="save-update" inverse="true" lazy="true"
     * @hibernate.collection-key column="USER_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Password"
     */
    public Collection getPasswordCollection()
    {
		return passwordCollection;
	}
    
    /**
     * @return Returns the passwordCollection.
     */
	public void setPasswordCollection(Collection passwordCollection) 
	{
		this.passwordCollection = passwordCollection;
	}
    
    /**
     * This function Copies the data from an UserForm object to a User object.
     * @param user An UserForm object containing the information about the user.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
       
    }
    
    
   
  
}