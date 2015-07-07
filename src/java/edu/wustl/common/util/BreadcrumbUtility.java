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
 * 
 */

package edu.wustl.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 * @author shrishail_kalshetty
 *
 */
public class BreadcrumbUtility
{

	/**
	 * private constructor.
	 */
	private BreadcrumbUtility()
	{
		//private constructor.
	}

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BreadcrumbUtility.class);

	/**
	 * Constant for info image path.
	 */
	private static final String INFO_IMAGE = "<img align=\"top\" alt=\"Info\" src=\"images/info.png\">";

	/**
	 * Constant for arrow image path.
	 */
	private static final String ARROW_IMAGE = "<img alt=\"Arrow\" src=\"images/arrow.png\">";

	/**
	 * Constant for HTML blank space.
	 */
	private static final String HTML_BLANK_SPACE = "&nbsp;&nbsp;&nbsp;";

	/**
	 * Constant for URL.
	 */
	private static final String URL = "URL";

	/**
	 * Constant for javaScript function.
	 */
	private static final String JAVASCRIPT = "JavaScript";

	/**
	 * Constant for Empty String.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * Constant for string width.
	 */
	private static final Integer BREADCRUMB_WIDTH = 12;

	/**
	 * This method creates and returns the breadCrumbString.
	 * @param map Map object.
	 * @param width width for displaying breadCrumb String. 
	 * 
	 * @return breadCrumbString.	 
	 */
	public static String createBreadCrumbString(Map<String, NameValueBean> map, Integer width)
	{
		LOGGER.info("creating breadcrumb String..");
		StringBuffer breadCrumbString = new StringBuffer();
		if (map != null && !map.isEmpty())
		{
			if (width == null)
			{
				width = BREADCRUMB_WIDTH;
			}
			else if (width == 0)
			{
				width = BREADCRUMB_WIDTH;
			}
			breadCrumbString
					.append("<table style=\"background-color: #eef5fd;border:solid;border-width:1px;"
							+ "border-color:#c9ebff;padding-left: 12px;\" width=\"100%\">");

			breadCrumbString
					.append("<tr style=\"width:100%;\"><td width=\"100%\" style=\"font-family:"
							+ " arial;font-size: 12px;font-weight: normal;\">&nbsp;");

			breadCrumbString.append(INFO_IMAGE).append(HTML_BLANK_SPACE);
			final Set<Entry<String, NameValueBean>> entrySet = map.entrySet();
			final Iterator<Entry<String, NameValueBean>> iterator = entrySet.iterator();

			while (iterator.hasNext())
			{
				final Entry<String, NameValueBean> entry = iterator.next();
				final String toolTip = entry.getKey();
				final NameValueBean nameValueBean = entry.getValue();
				final String displayStr = toolTip.length() > width ? toolTip.substring(0, width)
						+ "..." : toolTip;
				if (nameValueBean == null || EMPTY_STRING.equals(nameValueBean.getValue()))
				{
					breadCrumbString
							.append(
									"<label style=\"font-family: arial;font-size: 12px; font-weight: normal;\" title=\"")
							.append(toolTip).append("\">").append(displayStr).append("</label>");
				}
				else
				{
					breadCrumbString
							.append(
									"<a style=\"color: #0000ff;font-family: arial;font-size: 12px;font-weight: normal;cursor:pointer;\"")
							.append(" title=\"").append(toolTip);
					if (nameValueBean.getName().equals(URL))
					{
						breadCrumbString.append("\" href=\"").append(nameValueBean.getValue())
								.append("\">");

					}
					else if (nameValueBean.getName().equals(JAVASCRIPT))
					{
						breadCrumbString.append("\" onClick=\"javascript:").append(
								nameValueBean.getValue()).append(";\">");
					}
					else
					{
						throw new RuntimeException(
								"Exception in Breadcrumb: Name of NameValueBean in Map should be 'URL' or 'JavaScript'.");
					}
					breadCrumbString.append(displayStr).append("</a>");
				}
				if (iterator.hasNext())
				{
					breadCrumbString.append(HTML_BLANK_SPACE).append(ARROW_IMAGE).append(
							HTML_BLANK_SPACE);
				}
			}
			breadCrumbString.append("</td></tr></table>");
		}
		return breadCrumbString.toString();
	}

}
