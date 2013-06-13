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
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@author Mandar Deshmukh
 *@version 1.0
 */

package edu.wustl.common.beans;

import java.io.Serializable;

import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class NameValueBean implements Comparable<NameValueBean>, Serializable
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(NameValueBean.class);
	/**
	 * Specifies serial Version UID.
	 */
	private static final long serialVersionUID = 861314614541823827L;

	/**
	 * Specifies name Object.
	 */
	private Object name;

	/**
	 * Specifies value Object.
	 */
	private Object value;

	/**
	 * Relevence counter field is for sorting according to relevance.
	 */
	private Long relevanceCounter;

	/**
	 * Specifies Constructor.
	 */
	public NameValueBean()
	{

	}

	/**
	 * Specifies parameterised Constructor.
	 * @param name name
	 * @param value value
	 */
	public NameValueBean(Object name, Object value)
	{
		this.name = name;
		this.value = value;
	}

	/**
	 * Specifies parameterised Constructor.
	 * @param name name
	 * @param value value
	 * @param relevanceCounter relevance Counter
	 */
	public NameValueBean(Object name, Object value, Long relevanceCounter)
	{
		this.name = name;
		this.value = value;
		this.relevanceCounter = relevanceCounter;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name.toString();
	}

	/**
	 * This method sets the name.
	 * @param name The name to set.
	 */
	public void setName(Object name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return value.toString();
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * @return Returns the relevanceCounter.
	 */
	public Long getRelevanceCounter()
	{
		return relevanceCounter;
	}

	/**
	 * @param relevanceCounter The relevanceCounter to set.
	 */
	public void setRelevanceCounter(Long relevanceCounter)
	{
		this.relevanceCounter = relevanceCounter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * This method convert to string.
	 * @return String.
	 */
	public String toString()
	{
		return ("name:" + name.toString() + " value:" + value.toString());
	}

	/**
	 * This method compare Object.
	 * @param nameValueBean nameValueBean Object.
	 * @return int.
	 */
	public int compareTo(NameValueBean nameValueBean)
	{
		Comparable comparable;
		comparable = (Comparable) name;
		return compareObject(comparable, nameValueBean);
	}

	/**
	 * This method compare Object.
	 * @param comparable comparable
	 * @param nameValueBean nameValueBean Object
	 * @return int
	 */
	private int compareObject(Comparable comparable, NameValueBean nameValueBean)
	{
		int cmpResult;
		try
		{
			cmpResult = comparable.compareTo(nameValueBean.name);
		}
		catch (ClassCastException exception)
		{
			LOGGER.error("Objects to compared must be of same class.", exception);
			cmpResult = 0;
		}

		return cmpResult;
	}

	/**
	 * This method compare Object.
	 * @param object object.
	 * @return true if object is equal.
	 */
	public boolean equals(Object object)

	{
		boolean equal = false;
		if (object != null)
		{
			if (this.getClass().equals(object.getClass()))
			{
				NameValueBean nvb = (NameValueBean) object;

				if (this.getName().equals(nvb.getName()) && this.getValue().equals(nvb.getValue()))
				{
					equal = true;
				}
			}
		}
		return equal;
	}

	/**
	 * This method returns hash Code.
	 * @return int.
	 */
	public int hashCode()

	{
		int hashCode = super.hashCode();
		if (this.getName() != null && this.getValue() != null)
		{
			hashCode = this.getName().hashCode() * this.getValue().hashCode();
		}
		return hashCode;
	}
}
