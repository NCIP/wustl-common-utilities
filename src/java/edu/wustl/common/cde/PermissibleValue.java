/**
 * Created on May 26, 2005
 *
 * Title : PermissibleValue Interface</p>
 * <p> This interface defines methods that can be used to access the information
 * about a PermissibleValue</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * 
 */
package edu.wustl.common.cde;

import java.util.Set;

/**
 * @author mandar_deshmukh
 *
 * <p> This interface defines methods that can be used to access the information
 * about a PermissibleValue</p>
 *
 */
public interface PermissibleValue
{
    /**
     * getIdentifier method returns the unique id associated with the.
     * PermissibleValue
     * @return Returns the identifier.
     */
    Long getIdentifier();
    /**
     * getConceptid method returns the concept id associated with the.
     * PermissibleValue
     * @return Returns the conceptid.
     */
    String getConceptid();
    /**
     * getDefiantion method returns the defination of the PermissibleValue.
     * @return Returns the defination.
     */
    String getDefination();
    /**
     * getParentPermissibleValue method returns the Parent PermissibleValue node.
     * of the PermissibleValue
     * @return Returns the parentPermissibleValue.
     */
    PermissibleValue getParentPermissibleValue();
    /**
     * getSubPermissibleValues method returns a set of all the sub.
     * PermissibleValues of the PermissibleValue
     * @return Returns the subPermissibleValues.
     */
    Set getSubPermissibleValues();
    /**
     * getValue method returns the Value of the PermissibleValue.
     * @return Returns the value.
     */
    String getValue();
    /**
     * The CDE to which this permissible values belongs.
     * @return Returns The CDE to which this permissible values belongs.
     */
    CDE getCde();
}
