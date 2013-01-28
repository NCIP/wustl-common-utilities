
package edu.wustl.common.util;


import java.io.Serializable;

import edu.wustl.common.beans.NameValueBean;
/**
 * This comparator is used where sorting should be done on relevance counter of NameValueBean.
 */
public class NameValueBeanRelevanceComparator implements java.util.Comparator<Object>,Serializable
{
	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -2391618615413887973L;

	/**
	 * @param arg0 Object object to be compared.
	 * @param arg1 Object object to be compared.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @return int output of comparison.
	 */

	public int compare(Object arg0, Object arg1)
	{
		int retValue=0;
		if (arg0 instanceof NameValueBean && arg1 instanceof NameValueBean)
		{
			NameValueBean nvb1 = (NameValueBean) arg0;
			NameValueBean nvb2 = (NameValueBean) arg1;

			if (nvb1.getRelevanceCounter() != null && nvb2.getRelevanceCounter() != null)
			{
				retValue = getDiffOfNameValueBeans(nvb1, nvb2);
			}
		}
		return retValue;
	}

	/**
	 * This method returns difference of two NameValueBean.
	 * @param retValue
	 * @param nvb1 NameValueBean
	 * @param nvb2 NameValueBean
	 * @return difference of two NameValueBean.
	 */
	private int getDiffOfNameValueBeans(NameValueBean nvb1, NameValueBean nvb2)
	{
		int retValue = nvb1.getRelevanceCounter().compareTo(nvb2.getRelevanceCounter());
		if (retValue == 0 && nvb1.getName() != null && nvb2.getName() != null)
		{
				retValue= nvb1.getName().toLowerCase().compareTo(
						nvb2.getName().toLowerCase());
		}
		return retValue;
	}
}
