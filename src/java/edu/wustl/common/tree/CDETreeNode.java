/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Aug 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.tree;

import java.io.Serializable;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDETreeNode extends TreeNodeImpl implements Serializable, Comparable<Object>
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Specify cde Name.
	 */
	private String cdeName;

	/**
	 * Constructor.
	 */
	public CDETreeNode()
	{
		super();
	}

	/**
	 * Constructor.
	 * @param identifier identifier.
	 * @param value value.
	 */
	public CDETreeNode(Long identifier, String value)
	{
		super(identifier, value);
	}

	/**
	 * @return Returns the cdeName.
	 */
	public String getCdeName()
	{
		return cdeName;
	}

	/**
	 * @param cdeName The cdeName to set.
	 */
	public void setCdeName(String cdeName)
	{
		this.cdeName = cdeName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.tree.TreeNodeImpl#toString()
	 */
	/**
	 * This method return value as String.
	 * @return value as String.
	 */
	public String toString()
	{
		return this.getValue();
	}

	/**
	 * Compare objects.
	 * @param tmpobj Object.
	 * @return int.
	 */
	public int compareTo(Object tmpobj)
	{
		CDETreeNode treeNode = (CDETreeNode) tmpobj;
		return getValue().compareTo(treeNode.getValue());
	}
	/**
	 * overrides TreeNodeImpl.equals method .
	 * @param obj Object.
	 * @return if equal true else false.
	 */
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
	/**
	 * overrides TreeNodeImpl.hashCode method.
	 * @return hashCode.
	 */
	public int hashCode()
	{
		return super.hashCode();
	}
}