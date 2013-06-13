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
 * Created on Feb 16, 2007
 */

package edu.wustl.common.util.tag;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Santosh Chandak
 * JSP tag for Autocomplete feature. The body of this tag is executed once for every call of the tag
 * when the page is rendered.
 * To use this tag, include AutocompleterCommon.jsp in your page
 */
public class AutoCompleteTag extends TagSupport
{

	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	private static final Logger LOGGER = Logger.getCommonLogger(AutoCompleteTag.class);
	/**
	 * serialVersionUID long - version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * property on which Auto completer is to be applied.
	 */
	private String property;

	/**
	 * Object containing values of the dropdown. Supported data types.
	 * 1. String Array 2. List of Name Value Beans
	 *
	 */
	private Object optionsList;

	/**
	 * Default style.
	 */
	private String styleClass = "formFieldSized15";

	/**
	 * Number of results to be shown, set to 11 as for time drop downs showing 11 values was more logical.
	 */
	private String numberOfResults = "11";

	/**
	 * Trigger matching when user enters these number of characters.
	 */
	private String numberOfCharacters = "1";

	/**
	 * Constant for FALSE.
	 */
	private static final String FALSE = "false";
	/**
	 * Constant for FALSE.
	 */
	private static final String TRUE = "true";
	/**
	 * set to true if the textbox is readOnly.
	 */
	private Object readOnly = FALSE;

	/**
	 * set to true if the textbox is disabled.
	 */
	private Object disabled = FALSE;

	/**
	 * Functions to be called when textbox loses focus.
	 */
	private String onChange = "";

	/**
	 * initial value in the textbox, this is compulsory attribute.
	 */
	private Object initialValue = "";

	/**
	 * if the property is dependent on some other property.
	 * eg. type depends on class
	 *
	 */
	private String dependsOn = "";

	/**
	 * size String.
	 */
	private String size = "300";

	/**
	 *  true - in case of static lists eg. class, type
	 *  false - in case of dynamic lists eg. site, user
	 */
	private String staticField = TRUE;
	
	/**
	 * Functions to be called when combo-box value changes.
	 */
	private String onFocus = "";

	/**
	 * A call back function, which gets executed by JSP runtime when opening tag
	 * for this custom tag is encountered.
	 * @exception JspException jsp exception.
	 * @return integer value to skip body.
	 */
	public int doStartTag() throws JspException
	{

		try
		{
			JspWriter out = pageContext.getOut();
			String autocompleteHTMLStr = null;
			if (staticField.equalsIgnoreCase(TRUE))
			{
				autocompleteHTMLStr = getAutocompleteHTML();
			}
			else
			{
				autocompleteHTMLStr = getAutocompleteHTMLForDynamicProperty();
			}

			clearTagVariables();

			out.print(autocompleteHTMLStr);
		}
		catch (IOException ioe)
		{
			LOGGER.debug(ioe.getMessage(), ioe);
			throw new JspTagException("Error:IOException while writing to the user");
		}

		return SKIP_BODY;
	}

	/**
	  *  Clearing the variables.
	  */
	private void clearTagVariables()
	{
		onChange = "";
		initialValue = "";
		readOnly = FALSE;
		dependsOn = "";
		size = "300";
		disabled = FALSE;
		onFocus= FALSE;
	}

	//	@SuppressWarnings("unchecked")
	/**
	 * @return String auto complete result.
	 * @throws IOException IO Exception.
	 */
	private String getAutocompleteHTML() throws IOException
	{
		StringBuffer autoCompleteResult = new StringBuffer();
		InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
		Properties props = new Properties();
		props.load(stream);
		prepareCommonData();
		/**
		 *  Always pass the function with brackets, appending '()' will not be done
		 */
		setOnchangeValue();

		StringBuffer readOnly = getReadOnlyValue();
		Object[] arguments = {styleClass, initialValue, size, property, readOnly.toString()};
		autoCompleteResult.append(MessageFormat.format(props.getProperty("ACTinputImageTag"),
				arguments));
		StringBuffer valueList = getValueInList();
		String autoC = "";
		if (property.equals(Constants.SPECIMEN_TYPE))
		{
			autoC = "var AutoC = ";
		}
		Object[] args = {property, numberOfResults, numberOfCharacters};
		String autoCompleteStr = MessageFormat.format(props.getProperty("ACTautocompleter"), args);
		String autStr = Utility.replaceAll(autoCompleteStr, "*", "{");
		String autoCompStr = Utility.replaceAll(autStr, "#", "}");
		Object[] arg = {autoC,valueList, autoCompStr};
		autoCompleteResult.append(MessageFormat.format(props.getProperty("ACTscript"), arg));
		return autoCompleteResult.toString();
	}

	/**
	 * @return valueList.
	 */
	private StringBuffer getValueInList()
	{
		StringBuffer valueList = new StringBuffer();
		if (optionsList instanceof List)
		{
			List nvbList = (List) optionsList;
			if (nvbList != null && !nvbList.isEmpty())
			{
				for (int i = 0; i < nvbList.size(); i++)
				{
					NameValueBean nvb = (NameValueBean) nvbList.get(i);
					valueList.append("valuesInList[" + i + "] = \"")
					.append(nvb.getName()).append(
							"\";");
				}
			}
		}
		return valueList;
	}

	/**
	 * @return readOnly.
	 */
	private StringBuffer getReadOnlyValue()
	{
		StringBuffer readOnlyStr = new StringBuffer();
		if (readOnly.toString().equalsIgnoreCase(TRUE))
		{
			readOnlyStr.append("readonly");
		}
		else if (onFocus.toString().equalsIgnoreCase(TRUE))
		{
			readOnlyStr.append("onfocus=\"").append(onChange).append('\"');
		}
		else
		{
			readOnlyStr.append("onblur=\"").append(onChange).append('\"');
		}
		return readOnlyStr;
	}

	/**
	 * set Onchange Value.
	 */
	private void setOnchangeValue()
	{
		if (TextConstants.EMPTY_STRING.equals(onChange))
		{
			onChange = "trimByAutoTag(this)";
		}
		else
		{
			onChange = "trimByAutoTag(this);" + onChange;
		}
	}

	//@SuppressWarnings("unchecked")
	/**
	 * This method prepare common data.
	 */
	private void prepareCommonData()
	{
		setInitialValue();
		/**
		 *  As Type depends on class, get the optionsList
		 *  as List from optionsList which was passed as a map
		 */
		if (property.equalsIgnoreCase(Constants.SPECIMEN_TYPE) && dependsOn != null
				&& !dependsOn.equals(TextConstants.EMPTY_STRING))
		{
			String className = dependsOn;
			List specimenTypeList = (List) ((Map) optionsList).get(className);
			optionsList = specimenTypeList;
		}

		/**
		 *  Converting other data types to list of Name Value Beans
		 */
		processStringArrayOptionList();
		processListOptionList();

	}

	/**
	 * set Initial Value.
	 */
	private void setInitialValue()
	{
		if (initialValue == null || initialValue.equals(TextConstants.EMPTY_STRING))
		{
			initialValue = pageContext.getRequest().getParameter(property);

			assignInitialValue();
		}
	}

	/**
	 * assign Initial Value.
	 */
	private void assignInitialValue()
	{
		if (initialValue == null)
		{
			String[] title = (String[]) pageContext.getRequest().getParameterValues(property);

			if (title != null && title.length > 0)
			{
				initialValue = title[0];
			}
			else
			{
				initialValue = "";
			}

		}
	}

	/**
	 * process OptionList.
	 */
	private void processListOptionList()
	{
		if (optionsList instanceof List)
		{

			List nvbList = (List) optionsList;
			if (nvbList != null && !nvbList.isEmpty())
			{

				// TODO other than NVB
				NameValueBean nvb1 = (NameValueBean) nvbList.get(0);
				if (nvb1.getName().equals(Constants.SELECT_OPTION))
				{
					nvbList.remove(0);
				}

			}
			/* if(nvbList == null || nvbList.size() == 0)
			{
				initialValue = "No Records Present"; // needed?
			}  */
		}
	}

	/**
	 * process String Array OptionList.
	 */
	private void processStringArrayOptionList()
	{
		if (optionsList instanceof String[])
		{
			String[] stringArray = (String[]) optionsList;
			List tempNVBList = new ArrayList();
			if (stringArray != null)
			{
				for (int i = 0; i < stringArray.length; i++)
				{
					tempNVBList.add(new NameValueBean(stringArray[i], stringArray[i]));
				}
			}
			optionsList = tempNVBList;
		}
	}

	/**
	 * This function prepares the HTML to be rendered.
	 * @return String - containing HTML to be rendered
	 * @throws IOException IO Exception
	 *
	 */
	//@SuppressWarnings("unchecked")
	private String getAutocompleteHTMLForDynamicProperty() throws IOException
	{

		InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
		Properties props = new Properties();
		props.load(stream);
		String displayProperty = "display" + property;
		prepareCommonData();
		/**
		 *  Always pass the function with brackets, appending '()' will not be done
		 */
		setOnChangeValueForDynamicProperty();
		String name = setInitialValueForDynamicProperty(displayProperty);
		String value = TextConstants.EMPTY_STRING;
		if (optionsList instanceof List)
		{
			List nvbList = (List) optionsList;
			for (int i = 0; i < nvbList.size(); i++)
			{
				NameValueBean nvb1 = (NameValueBean) nvbList.get(i);
				boolean nvbValue = nvb1.getValue().equals(initialValue) || nvb1.getValue() != null
						&& "00".equals(nvb1.getValue()) && "0".equals(initialValue);
				if (nvbValue)
				{
					name = nvb1.getName();
					value = nvb1.getValue();
					break;
				}
			}
		}

		StringBuffer autoCompleteResult = new StringBuffer("<div id=\"");
		String div = "divFor" + displayProperty;
		autoCompleteResult.append(div).append(
				"\" style=\"display: none;\" class=\"autocomplete\"></div>");
		setInputImageTag(autoCompleteResult, name, value, props);
		Object[] scriptArgs = {displayProperty, displayProperty};
		autoCompleteResult.append(MessageFormat.format(props
				.getProperty("ACTscriptForDynamicProperty"), scriptArgs));
		autoCompleteResult = setAutocompleter(autoCompleteResult, div, props);
		autoCompleteResult.append("</script>");
		return autoCompleteResult.toString();
	}

	/**
	 * set Input Image Tag.
	 * @param autoCompleteResult autoCompleteResult
	 * @param name name
	 * @param value value
	 * @param props Properties object.
	 */
	private void setInputImageTag(StringBuffer autoCompleteResult, String name, String value,
			Properties props)
	{
		String readOnlyValue = TextConstants.EMPTY_STRING;
		if (readOnly.toString().equalsIgnoreCase(TRUE))
		{
			readOnlyValue = "readonly";
		}
		String onBlur = " onblur=\"" + onChange + "\"";
		String isDisabled = "";
		if (disabled.toString().equalsIgnoreCase(TRUE))
		{
			isDisabled = "disabled=\"true\"";
		}
		Object[] inputtImageTagArgs = {styleClass, name, size, readOnlyValue, onBlur, isDisabled,
				property, value};
		autoCompleteResult.append(MessageFormat.format(props.getProperty("ACTinImTagForDP"),
				inputtImageTagArgs));
	}

	/**
	 * @param autoCompleteResult autoComplete Result
	 * @param div div
	 * @param props Properties object.
	 * @return autoCompleteResult.
	 */
	private StringBuffer setAutocompleter(StringBuffer autoCompleteResult, String div,
			Properties props)
	{
		StringBuffer valueIds = new StringBuffer();
		if (optionsList instanceof List)
		{
			List nvbList = (List) optionsList;
			if (nvbList != null && !nvbList.isEmpty())
			{

				for (int i = 0; i < nvbList.size(); i++)
				{
					NameValueBean nvb = (NameValueBean) nvbList.get(i);
					valueIds.append("valuesInListOfdisplay")
					.append(property).append('[').append(i)
							.append("] = \"").append(nvb.getName())
							.append("\";idsInListOfdisplay")
							.append(property).append('[')
							.append(i).append("] = \"").append(
									nvb.getValue()).append("\";");
				}
				/**
				 *  Giving call to autocompleter constructor
				 */
				Object[] autocompleterArgs = {valueIds, property, div, numberOfResults,
						numberOfCharacters};
				String autoCompleteStr = MessageFormat.format(props
						.getProperty("ACTautocompleterDP"), autocompleterArgs);
				String autStr = Utility.replaceAll(autoCompleteStr, "*", "{");
				String autoCompStr = Utility.replaceAll(autStr, "#", "}");
				autoCompleteResult.append(autoCompStr);
			}
		}
		return autoCompleteResult;
	}

	/**
	 * set OnChange Value For Dynamic Property.
	 */
	private void setOnChangeValueForDynamicProperty()
	{
		if (TextConstants.EMPTY_STRING.equals(onChange))
		{
			onChange = "trimByAutoTagAndSetIdInForm(this)";
		}
		else
		{
			onChange = "trimByAutoTagAndSetIdInForm(this);" + onChange;
		}
	}

	/**
	 * @param displayProperty display Property
	 * @return name
	 */
	private String setInitialValueForDynamicProperty(String displayProperty)
	{
		String name = TextConstants.EMPTY_STRING;
		if ("0".equals(initialValue) || initialValue.toString().equalsIgnoreCase("undefined")
				|| TextConstants.EMPTY_STRING.equals(initialValue))
		{
			name = assignInitialValueToName(displayProperty);
		}
		return name;
	}

	/**
	 * @param displayProperty display Property.
	 * @return name
	 */
	private String assignInitialValueToName(String displayProperty)
	{
		String name;
		name = pageContext.getRequest().getParameter(displayProperty);

		if (name == null || name.equals(TextConstants.EMPTY_STRING))
		{
			String[] title = (String[]) pageContext.getRequest()
					.getParameterValues(displayProperty);

			if (title != null && title.length > 0)
			{
				name = title[0];
			}
			else
			{
				name = TextConstants.EMPTY_STRING;
			}
		}
		return name;
	}

	/**
	 * A call back function.
	 * @exception JspException jsp exception.
	 * @return integer value for evaluated page.
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

	/**
	 * @return String number of characters.
	 */
	public String getNumberOfCharacters()
	{
		return numberOfCharacters;
	}

	/**
	 * @param numberOfCharacters String number of characters.
	 */
	public void setNumberOfCharacters(String numberOfCharacters)
	{
		this.numberOfCharacters = numberOfCharacters;
	}

	/**
	 * @return String number of results.
	 */
	public String getNumberOfResults()
	{
		return numberOfResults;
	}

	/**
	 * @param numberOfResults String number of results.
	 */
	public void setNumberOfResults(String numberOfResults)
	{
		this.numberOfResults = numberOfResults;
	}

	/**
	 * @return Object list of options.
	 */
	public Object getOptionsList()
	{
		return optionsList;
	}

	/**
	 * @param optionsList Object set the list of option.
	 */
	public void setOptionsList(Object optionsList)
	{
		this.optionsList = optionsList;
	}

	/**
	 * @return String name of property.
	 */
	public String getProperty()
	{
		return property;
	}

	/**
	 * @param property String set the property.
	 */
	public void setProperty(String property)
	{
		this.property = property;
	}

	/**
	 * @return String size.
	 */
	public String getSize()
	{
		return size;
	}

	/**
	 * @param size String set the size.
	 */
	public void setSize(String size)
	{
		this.size = size;
	}

	/**
	 * @return String style class.
	 */
	public String getStyleClass()
	{
		return styleClass;
	}

	/**
	 * @param styleClass set the style class.
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}

	/**
	 * @return Object initial value.
	 */
	public Object getInitialValue()
	{
		return initialValue;
	}

	/**
	 * @param initialValue Object set initial value.
	 */
	public void setInitialValue(Object initialValue)
	{
		if (initialValue != null)
		{
			this.initialValue = initialValue.toString();
		}
	}

	/**
	 * @return Returns the onChange.
	 */
	public String getOnChange()
	{
		return onChange;
	}

	/**
	 * @param onChange The onChange to set.
	 */
	public void setOnChange(String onChange)
	{
		this.onChange = onChange;
	}

	/**
	 * @return Returns the readOnly.
	 */
	public Object getReadOnly()
	{
		return readOnly;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(Object readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	 * @return Returns the dependsOn.
	 */
	public String getDependsOn()
	{
		return dependsOn;
	}

	/**
	 * @param dependsOn The dependsOn to set.
	 */
	public void setDependsOn(String dependsOn)
	{
		this.dependsOn = dependsOn;
	}

	/**
	 * @return Returns the staticField.
	 */
	public String getStaticField()
	{
		return staticField;
	}

	/**
	 * @param staticField The staticField to set.
	 */
	public void setStaticField(String staticField)
	{
		this.staticField = staticField;
	}

	/**
	 * @return Returns the disabled.
	 */
	public Object getDisabled()
	{
		return disabled;
	}

	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(Object disabled)
	{
		this.disabled = disabled;
	}
	
	/**
	 * @param Returns the onFocus.
	 */
	public String getOnFocus()
	{
		return onFocus;
	}

	/**
	 * @param onFocus The onFocus to set.
	 */
	public void setOnFocus(String onFocus)
	{
		this.onFocus = onFocus;
	}

}