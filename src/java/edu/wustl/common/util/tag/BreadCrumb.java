/**
 * 
 */

package edu.wustl.common.util.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.BreadcrumbUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is used for displaying the breadCrumb String on UI.
 * 
 * Parameters required for this tag are :
 * 1) Map containing the breadCrumb String and its value as NameValueBean object.
 * 2) Width specifying the breadCrumb string width to display on UI.
 * NameValueBean contains the following values.
 * name will contain either the 'URL' or 'JavaScript' value.
 * When value of name in NamValuBean object is 'URL' 
 * pass the its value as link to call on the breadCrumb String when clicked. 
 * When value of name in NamValuBean object is 'JavaScript' 
 * pass the its value as javaScript function to call on the breadCrumb String when clicked.
 * 
 * @author shrishail_kalshetty
 * 
 */
public class BreadCrumb extends TagSupport
{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(BreadCrumb.class);

	/**
	 * Map for the breadCrumb containing breadCrumbString and its associated URL or javaScript.
	 */
	private transient Map<String, NameValueBean> map;

	/**
	 * Width for displaying the breadCrumb string.
	 */
	private transient Integer width;

	/**
	 * @param map the map to set.
	 */
	public void setMap(Map<String, NameValueBean> map)
	{
		this.map = map;
	}

	/**
	 * @return the map.
	 */
	public Map<String, NameValueBean> getMap()
	{
		return map;
	}

	/**
	 * @return the width.
	 */
	public Integer getWidth()
	{
		return width;
	}

	/**
	 * @param width the width to set.
	 */
	public void setWidth(Integer width)
	{
		this.width = width;
	}

	/**
	 * @return integer constant.
	 * @throws JspException throw JspException.
	 */
	@Override
	public int doStartTag() throws JspException
	{
		LOGGER.info("creating the breadcrumb information.");

		try
		{
			final JspWriter jspWriter = pageContext.getOut();
			jspWriter.print(BreadcrumbUtility.createBreadCrumbString(map, width));
		}
		catch (IOException ioEx)
		{
			LOGGER.error("IOException in Breadcrumb : " + ioEx.getMessage());
			throw new JspTagException("Exception in Breadcrumb : " + ioEx.getMessage());
		}
		catch (NullPointerException nullEx)
		{
			LOGGER.error("NullPointerException in Breadcrumb : " + nullEx.getMessage());
			throw new JspTagException("Exception in Breadcrumb : " + nullEx.getMessage());
		}
		return SKIP_BODY;
	}

	/**
	 * @return integer constant.
	 * @throws JspException throw JspException.
	 */
	@Override
	public int doEndTag() throws JspException
	{
		LOGGER.info("created the Breadcrumb tag.");
		return EVAL_PAGE;
	}
}
