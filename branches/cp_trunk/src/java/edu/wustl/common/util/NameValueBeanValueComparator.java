
package edu.wustl.common.util;


import java.io.Serializable;

import edu.wustl.common.beans.NameValueBean;
/**
 * This comparator is used where sorting is to be done on value of NameValueBean.
 */
public class NameValueBeanValueComparator implements java.util.Comparator<Object>,Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 630481702617730538L;

	/**
	 * @param arg0 Object object to be compared.
	 * @param arg1 Object object to be compared.
	 * @see ava.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @return integer output of comparison.
	 */
	public int compare(Object arg0, Object arg1)
	{
		int diff=0;
		boolean isNameValueBean = arg0 instanceof NameValueBean && arg1 instanceof NameValueBean;
		if (isNameValueBean)
		{
			NameValueBean nvb1 = (NameValueBean) arg0;
			NameValueBean nvb2 = (NameValueBean) arg1;
			//Compare according to relevance counter
			if (nvb1.getValue() != null && nvb2.getValue() != null)
			{
				diff=(Long.valueOf(nvb1.getValue())).compareTo(Long.valueOf(nvb2.getValue()));
			}
		}
		return diff;
	}
}
