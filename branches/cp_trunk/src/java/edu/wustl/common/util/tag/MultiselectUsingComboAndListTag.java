
package edu.wustl.common.util.tag;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author suhas_khot
 *
 */
public class MultiselectUsingComboAndListTag extends TagSupport
{

	/**
	 *
	 */
	private static final long serialVersionUID = 4L;

	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	private static final Logger LOGGER = Logger.getCommonLogger(MultiselectUsingComboAndListTag.class);

	/**
	 * Id of the text field for the date component.
	 */
	private transient String identifier;

	/**
	 * Style class for the text field for the date component.
	 */
	private transient String styleClass = "";

	/**
	 * Style class for the text field for the date component.
	 */
	private transient String addNewActionStyleClass = "";

	/**
	 * Size of the text field for the date component.
	 */
	private transient Integer size = Integer.valueOf(20);

	/**
	 *  A string variable used to construct HTML for the n-level combo.
	 */
	private transient String addNewActionName = "";

	/**
	 *  A string variable used to construct HTML for the n-level combo.
	 */
	private transient String addButtonOnClick = "";

	/**
	 *  A string variable used to construct HTML for the n-level combo.
	 */
	private transient String removeButtonOnClick = "";

	/**
	 * Id of the text field for the date component.
	 */
	private transient String selectIdentifier;

	/**
	 * Size of the text field for the date component.
	 */
	private transient Integer numRows = Integer.valueOf(4);

	/** The Constant MSU_COMBO_SELECT_TAG. */
	public static final String MSU_COMBO_SELECT_TAG = "MSUComboSelectTag";

	/** The Constant MSU_COMBO_REMOVE_BUTTON_DIV_TAG. */
	public static final String MSU_COMBO_REM_BUT_DIV_TAG = "MSUComboRemoveButtonDivTag";

	/** The Constant MSU_COMBO_ADD_BUTTON_DIV_TAG. */
	public static final String MSU_COMBO_ADD_BUTTON_DIV_TAG = "MSUComboAddButtonDivTag";

	/** The Constant MSU_COMBO_ANCHOR_TAG. */
	public static final String MSU_COMBO_ANCHOR_TAG = "MSUComboAnchorTag";

	/** The Constant MSU_COMBO_INPUT_TAG. */
	public static final String MSU_COMBO_INPUT_TAG = "MSUComboHTMLInputTag";

	private List collection = null;

	/**
	 * @return the collection
	 */
	public List getCollection()
	{
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(List collection)
	{
		this.collection = collection;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass()
	{
		return styleClass;
	}

	/**
	 * @return the addNewActionStyleClass
	 */
	public String getAddNewActionStyleClass()
	{
		return addNewActionStyleClass;
	}

	/**
	 * @return the size
	 */
	public Integer getSize()
	{
		return size;
	}

	/**
	 * @return the addNewActionName
	 */
	public String getAddNewActionName()
	{
		return addNewActionName;
	}

	/**
	 * @return the addButtonOnClick
	 */
	public String getAddButtonOnClick()
	{
		return addButtonOnClick;
	}

	/**
	 * @return the removeButtonOnClick
	 */
	public String getRemoveButtonOnClick()
	{
		return removeButtonOnClick;
	}

	/**
	 * @return the selectIdentifier
	 */
	public String getSelectIdentifier()
	{
		return selectIdentifier;
	}

	/**
	 * @return the numRows
	 */
	public Integer getNumRows()
	{
		return numRows;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}

	/**
	 * @param addNewActionStyleClass the addNewActionStyleClass to set
	 */
	public void setAddNewActionStyleClass(String addNewActionStyleClass)
	{
		this.addNewActionStyleClass = addNewActionStyleClass;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size)
	{
		this.size = size;
	}

	/**
	 * @param addNewActionName the addNewActionName to set
	 */
	public void setAddNewActionName(String addNewActionName)
	{
		this.addNewActionName = addNewActionName;
	}

	/**
	 * @param addButtonOnClick the addButtonOnClick to set
	 */
	public void setAddButtonOnClick(String addButtonOnClick)
	{
		this.addButtonOnClick = addButtonOnClick;
	}

	/**
	 * @param removeButtonOnClick the removeButtonOnClick to set
	 */
	public void setRemoveButtonOnClick(String removeButtonOnClick)
	{
		this.removeButtonOnClick = removeButtonOnClick;
	}

	/**
	 * @param selectIdentifier the selectIdentifier to set
	 */
	public void setSelectIdentifier(String selectIdentifier)
	{
		this.selectIdentifier = selectIdentifier;
	}

	/**
	 * @param numRows the numRows to set
	 */
	public void setNumRows(Integer numRows)
	{
		this.numRows = numRows;
	}

	@Override
	public int doStartTag() throws JspException
	{
		Logger.out.debug("Now rendering multiselect using MultiselectUsingComboAndListTag....");
		try
		{
			InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
			Properties props = new Properties();
			props.load(stream);

			JspWriter out = pageContext.getOut();

			out.print(Constants.EMPTY_STRING);
			if (validate())
			{
				out.print(generateOutput());
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Exception!! No response generated  "+e.getMessage());
			throw new JspTagException(
					"Exception encountered while rendering custom tag MultiselectUsingComboAndListTag");
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

	/**
	 * Method to validate the given values for the attributes.
	 * @return true if all required attributes are in proper valid format. Otherwise returns false.
	 */
	private boolean validate()
	{
		boolean result = true;

		ServletRequest request = pageContext.getRequest();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);

		if (errors == null)
		{
			errors = new ActionErrors();
		}

		//validations for identifier
		result = validateId(result, errors);

		//setting errors in request
		request.setAttribute(Globals.ERROR_KEY, errors);

		return result;
	}

	/**
	 * @param result result
	 * @param errors ActionErrors
	 * @return result
	 */
	private boolean validateId(boolean result, ActionErrors errors)
	{
		boolean reslt = result;
		if (Utility.isNull(identifier))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ID attribute is null"));
			reslt = false;
		}
		else if (Constants.EMPTY_STRING.equals(identifier.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ID attribute is empty"));
			reslt = false;
		}
		return reslt;
	}

	/**
	 * @return String generated output.
	 * @throws IOException I/O exception.
	 */
	private String generateOutput() throws IOException
	{

		InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
		Properties props = new Properties();
		props.load(stream);

		StringBuffer multiSelectUsingCombosHTMLStr = new StringBuffer(500);
		multiSelectUsingCombosHTMLStr
				.append("<table cellpadding='0' cellspacing='0'><tr><td width='35%' valign='top' align='left' style='padding-left:0px'>");

		Object[] inputTagArgs = {identifier, identifier, styleClass, size};
		multiSelectUsingCombosHTMLStr.append(MessageFormat.format(props
				.getProperty(MSU_COMBO_INPUT_TAG), inputTagArgs));
		if (getAddNewActionName() != null && !("".equalsIgnoreCase(getAddNewActionName())))
		{
			String AddActionId = "addAction_" + identifier;
			Object[] MSUCombAnchorTagArgs = {AddActionId, addNewActionStyleClass, addNewActionName};
			multiSelectUsingCombosHTMLStr.append(MessageFormat.format(props
					.getProperty(MSU_COMBO_ANCHOR_TAG), MSUCombAnchorTagArgs));
		}
		multiSelectUsingCombosHTMLStr
				.append("</td><td width=\"20%\" align=\"center\" valign=\"top\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td height=\"22\" align=\"center\" valign=\"top\">");

		String addButtonId = "addButton_" + identifier;
		Object[] MSUComboAddButtonDivTag = {addButtonId, addButtonOnClick};
		multiSelectUsingCombosHTMLStr.append(MessageFormat.format(props
				.getProperty(MSU_COMBO_ADD_BUTTON_DIV_TAG), MSUComboAddButtonDivTag));
		multiSelectUsingCombosHTMLStr.append("</td></tr><tr><td height=\"22\" align=\"center\">");

		String removeButtonId = "removeButton_" + identifier;
		Object[] MSUComboRemoveButtonDivTag = {removeButtonId, removeButtonOnClick};
		multiSelectUsingCombosHTMLStr.append(MessageFormat.format(props
				.getProperty(MSU_COMBO_REM_BUT_DIV_TAG), MSUComboRemoveButtonDivTag));

		multiSelectUsingCombosHTMLStr
				.append("</td></tr></table></td><td width=\"50%\" align=\"center\" class=\"black_ar_new\">");

		Object[] MSUComboSelectTag = {selectIdentifier, selectIdentifier, numRows};

		multiSelectUsingCombosHTMLStr.append(MessageFormat.format(props
				.getProperty(MSU_COMBO_SELECT_TAG), MSUComboSelectTag));
		if (getCollection() != null && !getCollection().isEmpty())
		{
			for (Object object : getCollection())
			{
				NameValueBean nameValue = (NameValueBean) object;
				multiSelectUsingCombosHTMLStr.append("<option id=\"" + nameValue.getValue()
						+ "\" value=\"" + nameValue.getValue() + "\" title=\""+nameValue.getName()+"\" selected=\"selected\">"
						+ nameValue.getName() + "</option>");
			}
		}
		multiSelectUsingCombosHTMLStr.append("</select></td></tr></table>");

		return multiSelectUsingCombosHTMLStr.toString();
	}
}
