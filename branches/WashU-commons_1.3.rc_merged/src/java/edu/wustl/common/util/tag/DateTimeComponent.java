/*
 * Created on Aug 9, 2006
 *
 * This class is the tag handler class for the Date Time component.
 */

package edu.wustl.common.util.tag;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * This class is the tag handler class for the Date Time component.
 */
public class DateTimeComponent extends TagSupport
{

	/**
	* LOGGER Logger - Generic LOGGER.
	*/
	private static final Logger LOGGER = Logger.getCommonLogger(DateTimeComponent.class);
	/**
	 * specify serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constant for Size.
	 */
	private static final int SIZE = 10;
	/**
	 * Constant for Size.
	 */
	private static final int START_YEAR = Integer.parseInt(ApplicationProperties.getValue("min.year"));
	/**
	 * Constant for Size.
	 */
	private static final int END_YEAR = Integer.parseInt(ApplicationProperties.getValue("max.year"));;
	/**
	 * Name of the text field for the date component.
	 */
	private transient String name;

	/**
	 * Value of the text field for the date component.
	 */
	private transient String value;

	/**
	 * Id of the text field for the date component.
	 */
	private transient String identifier;

	/**
	 * Style class for the text field for the date component.
	 */
	private transient String styleClass = "";

	/**
	 * Size of the text field for the date component.
	 */
	private transient Integer size = Integer.valueOf(SIZE);

	/**
	 * disabled property for the text field of the date component.
	 */
	private transient Boolean disabled = Boolean.FALSE;;

	/**
	 * Month of year of which the calendar is to be displayed.
	 */
	private transient Integer month;

	/**
	 * Day of month to be selected in the calendar.
	 */
	private transient Integer day;

	/**
	 * Year of date of which the calendar is to be displayed.
	 */
	private transient Integer year;

	/**
	 * Date pattern to be used in displaying date.
	 */
	private transient String pattern = "MM-dd-yyyy";;

	/**
	 * Name of the html form which will contain the date component.
	 */
	private transient String formName;

	/**
	 * Start year for the year drop down combo box.
	 */
	private transient Integer startYear = Integer.valueOf(START_YEAR);;

	/**
	 * End year for the year drop down combo box.
	 */
	private transient Integer endYear = Integer.valueOf(END_YEAR);;

	/**
	 * Tooltip to be displayed on the calendar icon.
	 */
	private transient String iconComment = "This is a Calendar";;

	/**
	 * Boolean to decide whether to display time controls.
	 */
	private transient Boolean displayTime = Boolean.FALSE;;

	/**
	 * Hours to be displayed as selected in the drop down combo for hours.
	 */
	private transient Integer hour = Integer.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));;

	/**
	 * Minutes to be displayed as selected in the drop down combo for minutes.
	 */
	private transient Integer minutes = Integer.valueOf(Calendar.getInstance().get(Calendar.MINUTE));;

	/**
	 * Javascript function to be called on click of calendar image.
	 */
	private transient String onClickImage;

	// ------------ SETTER Methods ----------------------------------

	/**
	 * @param datePattern String - The pattern to set.
	 */
	public void setPattern(String datePattern)
	{
		this.pattern = datePattern;
	}

	/**
	 * @param dayOfMonth Integer - The day to set.
	 */
	public void setDay(Integer dayOfMonth)
	{
		this.day = dayOfMonth;
	}

	/**
	 * @param displayTime The displayTime to set.
	 */
	public void setDisplayTime(Boolean displayTime)
	{
		this.displayTime = displayTime;
	}

	/**
	 * @param endYear The endYear to set.
	 */
	public void setEndYear(Integer endYear)
	{
		this.endYear = endYear;
	}

	/**
	 * @param formName The formName to set.
	 */
	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	/**
	 * @param iconComment The iconComment to set.
	 */
	public void setIconComment(String iconComment)
	{
		this.iconComment = iconComment;
	}

	/**
	 * @param monthOfYear Integer - The month to set.
	 */
	public void setMonth(Integer monthOfYear)
	{
		this.month = monthOfYear;
	}

	/**
	 * @param startYear The startYear to set.
	 */
	public void setStartYear(Integer startYear)
	{
		this.startYear = startYear;
	}

	/**
	 * @param timeInHours Integer - The hour to set.
	 */
	public void setHour(Integer timeInHours)
	{
		this.hour = timeInHours;
	}

	/**
	 * @param timeInMinutes Integer - The minutes to set.
	 */
	public void setMinutes(Integer timeInMinutes)
	{
		this.minutes = timeInMinutes;
	}

	/**
	 * @param txtdate String - The name to set.
	 */
	public void setName(String txtdate)
	{
		this.name = txtdate;
	}

	/**
	 * @param txtdateid String - The identifier to set.
	 */
	public void setId(String txtdateid)
	{
		this.identifier = txtdateid;
	}

	/**
	 * @param yearOfDate Integer - The year to set.
	 */
	public void setYear(Integer yearOfDate)
	{
		this.year = yearOfDate;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * @param styleClass The styleClass to set.
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}

	/**
	 * @param size The size to set.
	 */
	public void setSize(Integer size)
	{
		this.size = size;
	}

	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(Boolean disabled)
	{
		this.disabled = disabled;
	}

	/**
	 * @param onClickImage The onClickImage to set.
	 */
	public void setOnClickImage(String onClickImage)
	{
		this.onClickImage = onClickImage;
	}

	// ------------SETTER Methods end ----------------------------------

	/**
	 * A call back function, which gets executed by JSP runtime when opening tag for this
	 * custom tag is encountered.
	 * @exception JspException jsp exception.
	 * @return integer value as per validation.
	 */
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();

			out.print("");
			if (validate())
			{
				initializeParameters();
				out.print(generateOutput());
			}

		}
		catch (IOException ioe)
		{
			LOGGER.debug(ioe.getMessage(), ioe);
			throw new JspTagException("Error:IOException while writing to the user");
		}

		return SKIP_BODY;
	}

	/**
	 * A call back function DateTimeComponent class.
	 * @exception JspException jsp exception.
	 * @return integer value for evaluated page.
	 */
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
		// validations for name
		result = validateName(result, errors);
		//validations for identifier
		result = validateId(result, errors);

		//validations for formName
		result = validateFormName(result, errors);

		//setting errors in request
		request.setAttribute(Globals.ERROR_KEY, errors);

		return result;
	}

	/**
	 * @param result result
	 * @param errors ActionErrors
	 * @return result
	 */
	private boolean validateFormName(boolean result, ActionErrors errors)
	{
		boolean reslt = result;
		if (Utility.isNull(formName))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("FormName attribute is null"));
			reslt = false;
		}
		else if ("".equals(formName.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("FormName attribute is empty"));
			reslt = false;
		}
		return reslt;
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
		else if ("".equals(identifier.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ID attribute is empty"));
			reslt = false;
		}
		return reslt;
	}

	/**
	 * @param result result
	 * @param errors ActionErrors
	 * @return result
	 */
	private boolean validateName(boolean result, ActionErrors errors)
	{
		boolean reslt = result;
		if (Utility.isNull(name))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Name attribute is null"));
			reslt = false;
		}
		else if ("".equals(name.trim()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Name attribute is empty"));
			reslt = false;
		}
		return reslt;
	}

	//method to generate the required output.
	/**
	 * @return String generated output.
	 * @throws IOException I/O exception.
	 */
	private String generateOutput() throws IOException
	{

		InputStream stream = Utility.getCurrClassLoader().getResourceAsStream("Tag.properties");
		Properties props = new Properties();
		props.load(stream);
		String isDisabled = ">";
		if (disabled.booleanValue())
		{
			isDisabled = " disabled=\"disabled\">";
		}
		Object[] inputTagArgs = {name, identifier, value, styleClass, size, isDisabled};
		StringBuffer output = new StringBuffer(MessageFormat.format(props
				.getProperty("DTCinputTag"), inputTagArgs));
		String onClickFunction = getOnClickFunction();

		Object[] anchorTagArgs = {onClickFunction, iconComment};
		output.append(MessageFormat.format(props.getProperty("DTCanchorTag"), anchorTagArgs));

		Object[] divTagArgs = {identifier};
		output.append(MessageFormat.format(props.getProperty("DTCdivTag"), divTagArgs));
		output.append("<SCRIPT>");
		if (displayTime.booleanValue())
		{
			Object[] timeCalenderArgs = {identifier, day, month, year, hour, minutes};
			output.append(MessageFormat.format(props.getProperty("DTCtimeCalender"),
					timeCalenderArgs));
		}
		else
		{
			Object[] calenderArgs = {identifier, day, month, year};
			output.append(MessageFormat.format(props.getProperty("DTCcalender"), calenderArgs));
		}
		output.append("</SCRIPT></DIV>");
		return output.toString();
	}

	/**
	 * @return onClickFunction.
	 */
	private String getOnClickFunction()
	{
		String onClickFunction = "";
		/**
		 * Changes done by Jitendra to fix the bug if two DateTimeComponent included in the same jsp.
		 * So to fix this bug, now we are passing identifier attribute to showCalendar, printCalendar and
		 * printTimeCalendar js function.
		 */
		if (onClickImage == null)
		{
			onClickFunction = "showCalendar('" + identifier + "'," + year + "," + month + "," + day
					+ ",'" + pattern + "','" + formName + "','" + name + "',event," + startYear
					+ "," + endYear + ");";
		}
		else
		{
			onClickFunction = onClickImage;
		}
		return onClickFunction;
	}

	/**
	 * Method to initialize the optional parameters.
	 */
	private void initializeParameters()
	{
		if (value == null)
		{
			value = "";
		}
		Integer specimenYear = 0;
		Integer specimenMonth = 0;
		Integer specimenDay = 0;
		if (!"".equals(value.trim()))
		{
			specimenYear = Integer.valueOf(Utility.getYear(value));
			specimenMonth = Integer.valueOf(Utility.getMonth(value));
			specimenDay = Integer.valueOf(Utility.getDay(value));
		}
		initiializeMonthDayYear(specimenYear, specimenMonth, specimenDay);
	}

	/**
	 * This method initiialize Month Day Year.
	 * @param specimenYear specimen Year
	 * @param specimenMonth specimen Month
	 * @param specimenDay specimen Day.
	 */
	private void initiializeMonthDayYear(Integer specimenYear, Integer specimenMonth,
			Integer specimenDay)
	{
		day = specimenDay.intValue();
		month = specimenMonth.intValue();
		year = specimenYear.intValue();

		if (month == 0)
		{
			month = Integer.valueOf((Calendar.getInstance().get(Calendar.MONTH)) + 1);
		}
		if (day == 0)
		{
			day = Integer.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		}
		if (year == 0)
		{
			year = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
	}

	/**
	 * @param args arguments.
	 * @throws IOException Generic IO exception
	 */
	public static void main(String[] args) throws IOException
	{
		DateTimeComponent obj = new DateTimeComponent();
		obj.name = "mddate";
		obj.identifier = "mddate";
		obj.formName = "newsForm";
		//	obj.onClickImage ="f1()";
		obj.initializeParameters();
		obj.generateOutput();
	}
}
