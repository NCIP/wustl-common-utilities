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
 * Created on Jun 12, 2006
 */

package edu.wustl.common.util.tag;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_bh
 * JSP tag for N-level combo-box. The body of this tag is executed once per
 * every request for one n-level comboboxes.
 */
public class NLevelCustomCombo extends TagSupport
{

	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	private static final Logger LOGGER = Logger.getCommonLogger(NLevelCustomCombo.class);
	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * a datastructure of type Map of Map of ... of List.
	 * This forms a hierarchy of data. The depth of this tree hierarchy decides
	 * the number of combos used to display this complex data structure.
	 * Hence it can be called a recursive Map data structure.
	 */
	private transient Map dataMap;

	/**
	 * List of string values used as labels for n combos.
	 */
	private transient String[] labelNames; // optional

	/**
	 * List of string values used for naming each combos.
	 */
	private transient String[] attributeNames;

	/**
	 * List of string values used for naming each combos.
	 */
	private String[] tdStyleClassArray;

	/**
	 * List of string values used to initailize first n-1 combos.
	 */
	private transient String[] initialValues;

	/**
	 * A string value used to give, a row of combos a unique value for id attribute of each combo.
	 */
	private transient String rowNumber;

	/**
	 * Number of empty combos needed for inital condition.
	 */
	private transient String noOfEmptyCombos = "3";

	/**
	 * A string value representing the style class to use with all combos.
	 */
	private transient String styleClass; // optional

	/**
	 * A string value representing the td style class to use with all table divisions.
	 */
	protected String tdStyleClass; // optional

	/**
	 * A boolean value for getting combos vertically aligned.
	 */
	private transient boolean isVerticalCombos; // optional

	/**
	 * A boolean value to disable all the combos.
	 */
	private transient boolean disabled;

	/**
	 * A string value for onChange event, common to all comboboxes.
	 */
	private transient String onChange = "onCustomListBoxChange(this) ";

	/**
	 *  A string variable used to construct HTML for the n-level combo.
	 */
	private transient String combosHTMLStr = "";

	/**
	 * An integer counter variable for counting combos.
	 */
	private transient int comboCounter = 0;

	/**
	 * A vriable to store the number of combos needed to construct n-combos.
	 */
	private transient int noOfCombosNeeded = 0;

	/**
	 * form Label Style.
	 */
	private transient String formLabelStyle = "";

	/**
	 * A string vriable which stores opening table row tag <tr> for
	 * vertical combos, otherwise empty for horizantal combos.
	 */
	private transient String verticalCombosStart = "";

	/**
	 * A string vriable which stores closing table row tag for
	 * vertical combos, otherwise empty for horizantal combos.
	 */
	private transient String verticalCombosEnd = "";

	/**
	 * A call back function, which gets executed by JSP runtime when opening tag for this
	 * custom tag is encountered. Call a recursive function to get the work done.
	 * @return SKIP_BODY
	 * @throws JspException Jsp Exception.
	 */
	public int doStartTag() throws JspException
	{
		numberOfCombosNeeded();
		initOptionalAttributes();
		if (validate())
		{
			try
			{
				InputStream stream = Utility.getCurrClassLoader().getResourceAsStream(
						"Tag.properties");
				Properties props = new Properties();
				props.load(stream);
				JspWriter out = pageContext.getOut();

				combosHTMLStr = combosHTMLStr + "<table cellpadding='0' cellspacing='0'><tr>";
				getCombos(dataMap, props);

				out.print(combosHTMLStr);

			}
			catch (IOException ioe)
			{
				LOGGER.debug(ioe.getMessage(), ioe);
				throw new JspTagException("Error:IOException while writing to the user");
			}

			// has to be reintialized for next usage of this tag.
			comboCounter = 0;
			noOfCombosNeeded = 0;
			combosHTMLStr = "";
			verticalCombosStart = "";
			verticalCombosEnd = "";
		}
		return SKIP_BODY;
	}

	/**
	 * A validation function.
	 * @return true if tag attributes are valid, false if tag attributes are invalid.
	 */
	private boolean validate()
	{
		ServletRequest request = pageContext.getRequest();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		boolean validateValue = true;
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		boolean attributeNamesValue = attributeNames == null
				|| attributeNames.length != noOfCombosNeeded;
		boolean labelNamesValue = labelNames == null || labelNames.length != noOfCombosNeeded;
		boolean initValues = initialValues == null || initialValues.length > noOfCombosNeeded
				|| initialValues.length < (noOfCombosNeeded - 1);
		if (attributeNamesValue)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Attributes list is either null or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY, errors);
			validateValue = false;
		}
		else if (labelNamesValue)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Label names list is either empty or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY, errors);
			validateValue = false;
		}
		else if (initValues)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"Initail values list is either empty or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY, errors);
			validateValue = false;
		}
		return validateValue;
	}

	/**
	 * A call back function to set a recursive Map data structure 'dataMap'.
	 * @param value value
	 */
	public void setDataMap(Map value)
	{
		dataMap = value;
	}

	/**
	 * A call back function which sets onChange.
	 * @param value value
	 */
	public void setOnChange(String value)
	{
		onChange = value;
	}

	/**
	 * This method will be called to return HTML String for TAG.
	 * @return HTML String for TAG
	 */
	public String getCombosHTMLStr()
	{
		return combosHTMLStr;
	}

	/**
	 *  This method will be called to set HTML String for TAG.
	 * @param combosHTMLStr HTML String for TAG
	 */
	public void setCombosHTMLStr(String combosHTMLStr)
	{
		this.combosHTMLStr = combosHTMLStr;
	}


	/**
	 * A call back function to set a list of label names.
	 * @param value String array.
	 */
	public void setLabelNames(String[] value)
	{
		List<String> labelNamesList = Arrays.asList(value);
		String[] labelNamesArray = new String[labelNamesList.size()];
		this.labelNames = labelNamesList.toArray(labelNamesArray);
	}

	/**
	 * A call back function to set a list of attribute names.
	 * @param value String array.
	 */
	public void setAttributeNames(String[] value)
	{
		List<String> attributeNamesList = Arrays.asList(value);
		String[] attributeNamesArray = new String[attributeNamesList.size()];
		this.attributeNames = attributeNamesList.toArray(attributeNamesArray);
	}

	/**
	 * A call back function to set a list of initialization values.
	 * @param value String array.
	 */
	public void setInitialValues(String[] value)
	{
		List<String> initialValuesList = Arrays.asList(value);
		String[] initialValuesArray = new String[initialValuesList.size()];
		initialValues = initialValuesList.toArray(initialValuesArray);
	}

	/**
	 * get Inital Values.
	 * @return initialValues.
	 */
	public String[] getInitalValues()
	{
		List<String> initialValuesList = Arrays.asList(initialValues);
		String[] initialValuesArray = new String[initialValuesList.size()];
		return initialValuesList.toArray(initialValuesArray);
	}

	/**
	 * A call back function to set row number.
	 * @param value value.
	 */
	public void setRowNumber(String value)
	{
		rowNumber = value;
	}

	/**
	 * A call back function to set  style class.
	 * @param value value
	 */
	public void setStyleClass(String value)
	{
		styleClass = value;
	}

	/**
	 * A call back function to set td style class.
	 * @param value value.
	 */
	public void setTdStyleClass(String value)
	{
		tdStyleClass = value;
	}

	/**
	 * A call back function to set isVerticalCombos.
	 * @param value isVerticalCombos.
	 */
	public void setIsVerticalCombos(boolean value)
	{
		isVerticalCombos = value;
		if (isVerticalCombos)
		{
			verticalCombosStart = "<tr>";
			verticalCombosEnd = "</tr>";
		}
	}

	/**
	 * A call back function to set noOfEmptyCombos.
	 * @param value The noOfEmptyCombos to set.
	 */
	public void setNoOfEmptyCombos(String value)
	{
		this.noOfEmptyCombos = value;
	}

	/**
	 * A initialization function which takes care of
	 * initialization related to optional attributes.
	 */
	private void initOptionalAttributes()
	{
		setInitLabelNames();
		setInitInitialValues();
		if (styleClass == null)
		{
			styleClass = "";
		}
		if (tdStyleClass == null)
		{
			tdStyleClass = "";
		}
		if (rowNumber == null || rowNumber.equals(""))
		{
			rowNumber = "1";
		}
	}

	/**
	 * set Init Initial Values.
	 */
	private void setInitInitialValues()
	{
		boolean initValues = initialValues == null || initialValues.length == 0;
		if (initValues)
		{
			initialValues = new String[noOfCombosNeeded];
			for (int i = 0; i <= noOfCombosNeeded; i++)
			{
				initialValues[i] = "-1";
			}
		}
		else
		{
			for (int i = 0; i < initialValues.length; i++)
			{
				boolean initValue = initialValues[i] == null || initialValues[i].equals("")
						|| initialValues[i].equals("0");
				if (initValue)
				{
					initialValues[i] = "-1";
				}
			}
		}
	}

	/**
	 * set Init Label Names.
	 */
	private void setInitLabelNames()
	{
		if (labelNames == null || labelNames.length == 0)
		{
			labelNames = new String[noOfCombosNeeded];
			for (int i = 0; i <= noOfCombosNeeded; i++)
			{
				labelNames[i] = "";
			}
		}
	}

	/**
	 * A utility function to find nunber of combos needed from a recursive map data structure.
	 */
	private void numberOfCombosNeeded()
	{
		if (dataMap == null || dataMap.isEmpty())
		{
			noOfCombosNeeded = Integer.parseInt(noOfEmptyCombos);
		}
		else
		{
			Map map = dataMap;
			while (map instanceof Map)
			{
				noOfCombosNeeded++;
				Set kSet = map.keySet();
				Object[] kSetArray = kSet.toArray();
				Object firstVal = map.get(kSetArray[0]);
				if (firstVal instanceof Map)
				{
					map = (Map) firstVal;
				}
				else
				{
					noOfCombosNeeded++;
					break;
				}
			}
		}
	}

	/**
	 * A recursive function to construct the html code for n-level combos.
	 * Number of calls to this function is equivalent to number of combos created.
	 * @param dMap - a
	 * @param props Properties object.
	 * @throws IOException IOException
	 */
	private void getCombos(Object dMap, Properties props) throws IOException
	{

		if (tdStyleClassArray != null)
		{
			tdStyleClass = tdStyleClassArray[comboCounter];
		}
		boolean mapList = (dMap instanceof Map && ((Map) dMap).size() > 0)
				|| (dMap instanceof List) && (((List) dMap).size() > 0);
		if (mapList)
		{
			getCombosFromMapOrList(dMap, props);
		}
		else
		// to handle initial value condition were dMap.size() == 0 , NO RECURSSIVE CALL FROM HERE
		{
			// use noOfEmptyCombos here
			setEmptyCombos(props);
		}
	}

	/**
	 * set No Of Empty Combos.
	 * @param props Properties object.
	 */
	private void setEmptyCombos(Properties props)
	{
		for (int i = 0; i < Integer.parseInt(noOfEmptyCombos); i++)
		{
			if (tdStyleClassArray != null)
			{
				tdStyleClass = tdStyleClassArray[comboCounter];
			}

			String isDisabled = "";
			if (disabled)
			{
				isDisabled = " disabled = \"true\"";
			}
			Object[] arguments = {verticalCombosStart, formLabelStyle,
					attributeNames[comboCounter], styleClass, tdStyleClass, rowNumber,
					comboCounter, isDisabled};
			combosHTMLStr = combosHTMLStr
					+ MessageFormat.format(props.getProperty("NLCCemptyComboHTML"), arguments);
			comboCounter++;
		}
	}

	/**
	 * @param dMap dMap
	 * @param props Properties object.
	 * @throws IOException IOException
	 */
	private void getCombosFromMapOrList(Object dMap, Properties props) throws IOException
	{
		if (dMap instanceof Map)
		{
			Set keySet = ((Map) dMap).keySet();

			String isDisabled = "";
			if (disabled)
			{
				isDisabled = " disabled = \"true\"";
			}
			Object[] arguments = {verticalCombosStart, formLabelStyle,
					attributeNames[comboCounter], styleClass, tdStyleClass, rowNumber,
					comboCounter, onChange, isDisabled};

			combosHTMLStr = combosHTMLStr
					+ MessageFormat.format(props.getProperty("NLCCcomboHTMLMap"), arguments);

			String initialValForThisCombo = (String) initialValues[comboCounter];
			// iterate through keys to get options for the current combo.
			getOptionsForCurrentCombo(keySet, initialValForThisCombo);
			combosHTMLStr = combosHTMLStr + "</select> </td> " + verticalCombosEnd;
			Object[] keySetArray = keySet.toArray();
			int indexForNextCombo = getIndexForNextCombo(keySetArray);
			comboCounter++;
			getCombos(((Map) dMap).get(keySetArray[indexForNextCombo]), props);
		}
		else if (dMap instanceof List) // Termination condition for recursion
		{
			List dList = (List) dMap;

			String isDisabled = "";
			if (disabled)
			{
				isDisabled = " disabled = \"true\"";
			}
			Object[] args = {verticalCombosStart, formLabelStyle, attributeNames[comboCounter],
					styleClass, tdStyleClass, rowNumber, comboCounter, isDisabled};

			combosHTMLStr = combosHTMLStr
					+ MessageFormat.format(props.getProperty("NLCCcomboHTMLList"), args);
			getOptionsForList(dList);
			combosHTMLStr = combosHTMLStr + "</select> </td> " + verticalCombosEnd;
			comboCounter++;
		}
	}

	/**
	 * @param keySetArray key Set Array.
	 * @return indexForNextCombo
	 */
	private int getIndexForNextCombo(Object[] keySetArray)
	{
		// logic to use init value starts here
		String initValue = (String) initialValues[comboCounter];
		int indexForNextCombo = 0;
		for (int i = 0; i < keySetArray.length; i++)
		{
			//String key = String.valueOf(keySetArray[i]);
			String key = ((NameValueBean) keySetArray[i]).getValue();
			if (initValue.equals(key))
			{
				indexForNextCombo = i;
			}
		}
		return indexForNextCombo;
	}

	/**
	 * @param dList dList.
	 */
	private void getOptionsForList(List dList)
	{
		if (comboCounter > 0 && !(((String) initialValues[comboCounter - 1]).equals("-1")))
		{
			for (int i = 0; i < dList.size(); i++)
			{
				NameValueBean nvb = (NameValueBean) dList.get(i);
				String optionSelection = getOptionSelection(nvb);
				combosHTMLStr = combosHTMLStr + "<option " + optionSelection + "value=\""
						+ nvb.getValue() + "\">";
				combosHTMLStr = combosHTMLStr + nvb.getName();
				combosHTMLStr = combosHTMLStr + "</option>";
			}
		}
	}

	/**
	 * @param nvb NameValueBean
	 * @return option Selection.
	 */
	private String getOptionSelection(NameValueBean nvb)
	{
		String optionSelection = "";
		if (initialValues.length == noOfCombosNeeded && initialValues[comboCounter] != null
				&& !(initialValues[comboCounter].equals("")))
		{
			String initialValForThisCombo = (String) initialValues[comboCounter];
			if (initialValForThisCombo.equals(nvb.getValue()))
			{
				optionSelection = " selected =\"true\" ";
			}
		}
		return optionSelection;
	}

	/**
	 * @param keySet keySet
	 * @param initialValForThisCombo initial Value For This Combo.
	 */
	private void getOptionsForCurrentCombo(Set keySet, String initialValForThisCombo)
	{
		Iterator keyIter = keySet.iterator();
		boolean isCurrentCombo = (comboCounter == 0)
				|| (comboCounter > 0
						&& !(((String) initialValues[comboCounter - 1]).equals("-1")));
		if (isCurrentCombo)
		{
			setOptionSelectionForCurrentCombo(initialValForThisCombo, keyIter);
		}
	}

	/**
	 * @param initialValForThisCombo initial Value For This Combo.
	 * @param keyIter key Iterator
	 */
	private void setOptionSelectionForCurrentCombo(String initialValForThisCombo, Iterator keyIter)
	{
		while (keyIter.hasNext())
		{
			// original String key = String.valueOf(keyIter.next());
			NameValueBean nvb = (NameValueBean) keyIter.next();
			String optionSelection = "";

			if (initialValForThisCombo.equals(nvb.getValue()))
			{
				optionSelection = " selected =\"true\" ";
			}
			combosHTMLStr = combosHTMLStr + "<option " + optionSelection + " value=\""
					+ nvb.getValue() + "\">";
			combosHTMLStr = combosHTMLStr + nvb.getName();
			combosHTMLStr = combosHTMLStr + "</option>";
		}
	}

	/**
	 * A call back function.
	 * @return EVAL_PAGE.
	 * @throws JspException Jsp Exception.
	 */
	public int doEndTag() throws JspException
	{
		//		try {
		//			JspWriter out = pageContext.getOut();
		//			out.print("</tr>     </table>");
		//		}catch(Exception io)
		//		{
		//			io.printStackTrace();
		//		}
		return EVAL_PAGE;
	}

	/**
	 * Sets disabled.
	 * @param value The disabled to set.
	 */
	public void setDisabled(boolean value)
	{
		this.disabled = value;
	}

	/**
	 * @param value The formLabelStyle to set.
	 */
	public void setFormLabelStyle(String value)
	{
		this.formLabelStyle = value;
	}

	/**
	 * @return Returns the tdStyleClassArray.
	 */
	public String[] getTdStyleClassArray()
	{
		List<String> tdStyleClassList = Arrays.asList(tdStyleClassArray);
		String[] tdStyleClass = new String[tdStyleClassList.size()];
		return tdStyleClassList.toArray(tdStyleClass);
	}

	/**
	 * @param tdStyleClassArray The tdStyleClassArray to set.
	 */
	public void setTdStyleClassArray(String[] tdStyleClassArray)
	{
		List<String> tdStyleClassList = Arrays.asList(tdStyleClassArray);
		String[] tdStyleClass = new String[tdStyleClassList.size()];
		this.tdStyleClassArray = tdStyleClassList.toArray(tdStyleClass);
	}
    /**
     * This method is used to set html representation of nlevelcombo tag.
     * Used in StorageContainerAjaxAction.java.
     * @param containerMap - container map
     * @param props - retrieve properties from Tag.properties
     * @throws IOException - IOException
     */
	public void setComboProperties(Map containerMap, Properties props) throws IOException
	{
		setDataMap(containerMap);
		numberOfCombosNeeded();
		getCombos(containerMap,props);
	}
}