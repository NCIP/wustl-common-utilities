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
 * <p>Title: CDEImpl Class
 * <p>Description: This class provides the implementation of the CDE interface.</p> 
 *<p>It also provides methods to set the values of the CDE and obtain the inforamtion about the CDE.</p> 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 26, 2005
 */

package edu.wustl.common.cde;

import java.util.Date;
import java.util.Set;

/**
 * @author mandar_deshmukh
 *
 * <p>Description: This class provides the implementation of the CDE interface.</p>
 *<p>It also provides methods to set the values of the CDE and obtain the inforamtion about the CDE.</p>
 * @hibernate.class table="CATISSUE_CDE"
 */
public class CDEImpl implements CDE
{
	/**
	 * publicId is a unique id assigned to each CDE.
	 */
    private String publicId;
    /**
     * preferredName is the name of the CDE that is preferred.
     */
    private String preferredName;
    /**
     * longName is the complete name of the CDE.
     */
    private String longName;
    /**
     * defination is the defination of the CDE.
     */
    private String defination;
    /**
     * version is the current version of the CDE.
     */
    private String version;
    /**
     * permissibleValues is the List of all the Permissible Values for the CDE.
     */
    private Set permissibleValues;
    /**
     * TimeStamp showing the date when the CDE was last updated.
     */
    private Date dateLastModified = new Date();
    /**
     * getPublicId method returns the public id of the CDE.
     * @return PublicID as a String Object of the CDE.
     * @hibernate.id name="publicId" column="PUBLIC_ID" type="string"
     * length="30" unsaved-value="null" generator-class="assigned"
     */
    public String getPublicId()
    {
        return this.publicId;
    }
    /**
     * @param publicid setPublicId method is used for setting the publicId of the CDE.
     */
    public void setPublicId(String publicid)
    {
        this.publicId = publicid;
    }
    /**
     * getLongName method returns the long name of the CDE.
     * @return LongName as a String Object of the CDE.
     * @hibernate.property name="longName" type="string" column="LONG_NAME" length="200"
     */
    public String getLongName()
    {
        return this.longName;
    }
    /**
     * setLongName method is used for setting the longName of the CDE.
     * @param longname accepts a String object.
     */
    public void setLongName(String longname)
    {
        this.longName = longname;
    }
    /**
     * getDefination method returns the Defination of the CDE.
     * @return Defination of the CDE as a String Object.
     * @hibernate.property name="defination" type="string" column="DEFINITION" length="500"
     */
    public String getDefination()
    {
        return this.defination;
    }
    /**
     * setDefination method is used for setting the defination of the CDE.
     * @param defination accepts a String object.
     */
    public void setDefination(String defination)
    {
        this.defination = defination;
    }
    /**
     * getVersion method returns the version of the CDE.
     * @return Version of the CDE as a String Object.
     * @hibernate.property name="version" type="string" column="VERSION" length="50"
     */
    public String getVersion()
    {
        return this.version;
    }
    /**
     * setVersion method is used for setting the version of the CDE.
     * @param version accepts a String object.
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    /**
     * getPermissibleValues method returns the Permissible Values of the CDE.
     * @return List of the PermissibleValues of the CDE.
     * @hibernate.set name="permissibleValues" table="CATISSUE_PERMISSIBLE_VALUE" cascade="all-delete-orphan"
     * @hibernate.collection-key column="PUBLIC_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.common.cde.PermissibleValueImpl"
     */
    public Set getPermissibleValues()
    {
        return this.permissibleValues;
    }
    /**
     * setPermissibleValues method is used for setting the PermissibleValues of the CDE.
     * @param permissiblevalues accepts a List object as a parameter.
     */
    public void setPermissibleValues(Set permissiblevalues)
    {
        this.permissibleValues = permissiblevalues;
    }
    /**
     * addPermisibleValue method allows the user to add a permissible value to the existing
     * list of available values.
     * @param permissibleValue accepts an object of PermissibleValueImpl class.
     * @return boolean value indicating whether the object was successfully added or not.
     * @author Mandar Deshmukh
     */
    public boolean addPermissibleValue(PermissibleValueImpl permissibleValue)
    {
        return this.permissibleValues.add(permissibleValue);
    }
    /**
     *
     * @author Mandar Deshmukh
     *
     * remove PermissibleValue method allows the user to
     * remove a permissible value from the existing
     * list of available values.
     * @param permissibleValue accepts an object of PermissibleValueImpl class.
     * @return boolean value indicating whether the object was successfully removed or not.
     *
     */
    public boolean removePermissibleValue(PermissibleValueImpl permissibleValue)
    {
        return this.permissibleValues.remove(permissibleValue);
    }
    /**
     * @return Returns the preferredName.
     */
    public String getPreferredName()
    {
        return preferredName;
    }
    /**
     * @param preferredname The preferredName to set.
     */
    public void setPreferredName(String preferredname)
    {
        this.preferredName = preferredname;
    }
    //Aniruddha : Attribute type modified to "date" instead of "timestamp"
    /**
     * Returns the date when the CDE was last updated.
     * @return the date when the CDE was last updated.
     * @hibernate.property name="dateLastModified" type="date" column="LAST_UPDATED"
     */
    public Date getDateLastModified()
    {
        return dateLastModified;
    }
    /**
     * Sets the date when the CDE was last updated.
     * @param dateLastModified the date when the CDE was last updated.
     */
    public void setDateLastModified(Date dateLastModified)
    {
        this.dateLastModified = dateLastModified;
    }
    //Mandar 05-apr-06 : bugid: 1622 : Parameterised constructor
    /**
     * @param publicId PublicID of the CDE.
     * @param preferredName PreferredName of the CDE.
     * @param longName Longname of the CDE.
     * @param defination Defination of the CDE.
     * @param version Version of the CDE.
     * @param dateLastModified Last modified date of the CDE.
     */
    public CDEImpl(String publicId, String preferredName, String longName, // NOPMD
    		String defination, String version, Date dateLastModified)
    {
    	this.publicId = publicId;
    	this.preferredName = preferredName;
    	this.longName = longName;
    	this.defination = defination;
    	this.version = version;
    	this.dateLastModified = dateLastModified;
    } // empty constructor
    /**
     * Default constructor.
     */
    public CDEImpl()
    {
    	// empty constructor
    }
}
