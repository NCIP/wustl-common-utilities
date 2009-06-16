/**
 * <p>Title: Validator Class>
 * <p>Description:  This Class contains the methods used for validation of the fields in the userform.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 1, 2005
 */

package edu.wustl.common.util.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 *  This Class contains the methods used for validation of the fields in the userform.
 *  @author gautam_shetty
 */

public class Validator
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(Validator.class);

	/**
	 * This is regular expression to check for XXS vulnerable characters e.g <, >, (, ) etc.
	 * */
	private static final String REGEX_XSS_VULNERABLE = "[()<>]";
	/**
	 * This is regular expression to validate email id.
	 */
	private static final String REGEX_VALID_EMAIL
	= "^\\w(\\.?[\\w-])*@\\w(\\.?[-\\w])*\\.([a-z]{3,}(\\.[a-z]{2,})?|[a-z]{2,}(\\.[a-z]{2,})?)$";
	/**
	 * This is regular expression to validate phone number.
	 */
	private static final String REGEX_VALID_PHONE = "\\(\\d{3}\\)\\d{3}-\\d{4}|\\d{3}\\-\\d{3}-\\d{4}";

	/**
	 * Checks that the input String is a valid email address.
	 * @param aEmailAddress String containing the email address to be checked.
	 * @return Returns true if its a valid email address, else returns false.
	 * */
	public boolean isValidEmailAddress(String aEmailAddress)
	{
		boolean result = true;
		try
		{
			if (isEmpty(aEmailAddress))
			{
				result = false;
			}
			else
			{
				result = isValidEmailId(aEmailAddress);
			}
		}
		catch (Exception ex)
		{
			LOGGER.debug(ex.getMessage(), ex);
			result = false;
		}
		return result;
	}

	/**
	 *
	 * @param ssn Social Security Number to check
	 * @return boolean depending on the value of ssn.
	 */
	public boolean isValidSSN(String ssn)
	{
		boolean result = true;
		try
		{
			Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}",
					Pattern.CASE_INSENSITIVE);
			Matcher mat = pattern.matcher(ssn);
			result = mat.matches();
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			result = false;
		}
		return result;
	}

	/**
	 * Checks whether a string is empty and adds an ActionError object in the ActionErrors object.
	 * @param str string to be checked.
	 * @return Returns true if the componentName is empty else returns false.
	 */
	public static boolean isEmpty(String str)
	{
		boolean isEmpty = false;
		if (str == null || CommonConstants.EMPTY_STRING.equals(str.trim()))
		{
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * Checks that the input String contains only alphabetic characters.
	 * @param alphaString The string whose characters are to be checked.
	 * @return false if the String contains any digit else returns true.
	 * */
	public boolean isAlpha(String alphaString)
	{
		boolean isAlpha = true;
		int index = 0;
		while (index < alphaString.length())
		{
			if (!Character.isLetter(alphaString.charAt(index)))
			{
				isAlpha = false;
				break;
			}
			index++;
		}
		return isAlpha;
	}

	/**
	 * Checks that the input String contains only numeric digits.
	 * @param numString The string whose characters are to be checked.
	 * @return false if the String contains any alphabet else returns true.
	 * */
	public boolean isNumeric(String numString)
	{
		boolean isNumeric = true;
		try
		{
			long longValue = Long.parseLong(numString);
			if (longValue <= 0)
			{
				isNumeric = false;
			}
		}
		catch (NumberFormatException exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			isNumeric = false;
		}
		return isNumeric;
	}

	/**
	 *
	 * Checks that the input String contains only numeric digits.
	 * @param numString The string whose characters are to be checked.
	 * @param positiveCheck Positive integer to check for positive number
	 * @return Returns false if the String contains any alphabet else returns true.
	 * Depending on the value of the positiveCheck will check for positive values
	 *
	 */
	public boolean isNumeric(String numString, int positiveCheck)
	{
		return isPositiveNumeric(numString, positiveCheck);
	}

	/**
	 *
	 * Checks that the input String contains only positive numeric digits.
	 * @param numString The string whose characters are to be checked.
	 * @param positiveCheck Positive integer to check for positive number
	 * @return Returns false if the String contains any alphabet or negative integer else returns true.
	 * @author aarti_sharma
	 * Depending on the value of the positiveCheck will check for positive values
	 *
	 */
	public boolean isPositiveNumeric(String numString, int positiveCheck)
	{
		boolean isPosNum = true;
		Double value = convertToNumber(numString);
		if (value == null)
		{
			isPosNum = false;
		}
		else
		{
			isPosNum = checkForPositive(positiveCheck,value);
		}
		return isPosNum;
	}

	/**
	 * @param positiveCheck positive Check
	 * @param value value
	 * @return isPosNum
	 */
	private boolean checkForPositive(int positiveCheck, Double value)
	{
		boolean isPosNum = true;
		if (positiveCheck > 0 && value.longValue() <= 0)
		{
			isPosNum = false;
		}
		else if (positiveCheck == 0 && value.longValue() < 0)
		{
			isPosNum = false;
		}
		return isPosNum;
	}

	/**
	 *
	 * Return Long representation of numString if its convertible else returns null.
	 * @param numString The string whose characters are to be checked.
	 * @author aarti_sharma
	 * @return Long representation of numString if its convertible else returns null
	 *
	 */
	public Long convertToLong(String numString)
	{
		Long longValue = null;
		try
		{
			longValue = Long.valueOf(numString);

		}
		catch (NumberFormatException exp)
		{
			LOGGER.debug("NumberFormatException:" + exp.getMessage(), exp);
		}
		return longValue;
	}

	/**
	 * This converts string into a number.
	 * @param numString Number as a string.
	 * @return Double number.
	 */
	public Double convertToNumber(String numString)
	{
		Double value = null;
		try
		{
			value = new Double(numString);
		}
		catch (NumberFormatException exp)
		{
			LOGGER.error("NumberFormatException:" + exp.getMessage(), exp);
		}
		return value;
	}

	/**
	 * Checks the given String for double value.
	 *
	 * @param dblString string
	 * @return boolean True if the string contains double number or
	 *  false if any non numeric character is present.
	 */
	public boolean isDouble(String dblString)
	{
		boolean isDouble = true;
		try
		{
			double dblValue = Double.parseDouble(dblString);
			if (dblValue < 0 || Double.isNaN(dblValue))
			{
				isDouble = false;
			}
		}
		catch (NumberFormatException exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			isDouble = false;
		}
		return isDouble;
	}

	/**
	 * @param dblString The String value to be verified for Double
	 * @param positiveCheck Boolean value indicating whether to check for positive values only or not.
	 * @return Boolean indicating the given string value is double or not.
	 */
	public boolean isDouble(String dblString, boolean positiveCheck)
	{
		boolean isDouble = true;
		try
		{
			double dblValue = Double.parseDouble(dblString);

			if (Double.isNaN(dblValue))
			{
				isDouble = false;
			}
			if (positiveCheck && dblValue < 0)
			{
				isDouble = false;
			}
		}
		catch (NumberFormatException exp)
		{
			LOGGER.debug("NumberFormatException:" + exp.getMessage(), exp);
			isDouble = false;
		}
		return isDouble;
	}

	/**
	 * Check option is valid or not.
	 * @param option option
	 * @return option valid or not
	 */
	public boolean isValidOption(String option)
	{
		boolean isValidOption = false;
		if (option != null
				&& !(option.trim().equals("") || option.trim().equals("-1") || option
						.equals(CommonConstants.SELECT_OPTION)))
		{
			isValidOption = true;
		}
		return isValidOption;
	}

	/**
	 * Check emailAddress is valid or not.
	 * @param emailAddress email Address
	 * @return valid or not
	 */
	private boolean isValidEmailId(String emailAddress)
	{
		boolean result = true;
		try
		{
			Pattern pattern = Pattern.compile(REGEX_VALID_EMAIL, Pattern.CASE_INSENSITIVE);
			Matcher mat = pattern.matcher(emailAddress);
			result = mat.matches();
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			result = false;
		}
		return result;
	}

	/**
	 * delimiter Excluding Given list.
	 * @param ignoreList ignore delimiter List
	 * @return delimiter string Excluding Given list
	 */
	public String delimiterExcludingGiven(String ignoreList)
	{
		String spChars = "!@#$%^&*()=+\\|{[ ]}\'\";:/?.>,<`~ -_";
		StringBuffer specialChars = new StringBuffer(spChars);
		StringBuffer retStr = new StringBuffer();

		try
		{
			char[] spIgnoreChars = ignoreList.toCharArray();
			for (int spCharCount = 0; spCharCount < spIgnoreChars.length; spCharCount++)
			{
				char chkChar = spIgnoreChars[spCharCount];
				int chInd = specialChars.indexOf(CommonConstants.EMPTY_STRING + chkChar);
				while (chInd != -1)
				{
					specialChars = specialChars.deleteCharAt(chInd);
					chInd = specialChars.indexOf(CommonConstants.EMPTY_STRING + chkChar);
				}
			}
			retStr = specialChars;
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
		}
		return retStr.toString();
	}

	// ----------------------------------- Date Validation --------------------
	/**
	 * check Valid Date Pattern.
	 * @param checkDate date value
	 * @return valid or not.
	 */
	private boolean isValidDatePattern(String checkDate)
	{
		boolean result = true;
		try
		{
			Pattern pattern = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}",
					Pattern.CASE_INSENSITIVE);
			Matcher mat = pattern.matcher(checkDate);
			result = mat.matches();
			// check for  / separator
			if (!result)
			{
				pattern = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}", Pattern.CASE_INSENSITIVE);
				mat = pattern.matcher(checkDate);
				result = mat.matches();
			}
		}
		catch (Exception exp)
		{
			LOGGER.error("IsValidDatePattern : exp : " + exp);
			result = false;
		}
		return result;
	}

	/**
	 * specify second in a min.
	 */
	private static final int SEC_IN_A_MIN = 60;
	/**
	 * specify min in a hour.
	 */
	private static final int MIN_IN_A_HR = 60;
	/**
	 * specify hours in a day.
	 */
	private static final int HRS_IN_A_DAY = 24;
	/**
	 * specify 1000 number.
	 */
	private static final int ONE_THOUSAND = 1000;

	/**
	 * To check date value.
	 * @param dtStr check date value
	 * @return isDate valid or not.
	 */
	private boolean isDate(String dtStr)
	{
		boolean isDate = true;
		int minYear = Integer.parseInt(CommonServiceLocator.getInstance().getMinYear());
		int maxYear = Integer.parseInt(CommonServiceLocator.getInstance().getMaxYear());
		String dateFormatStr=CommonServiceLocator.getInstance().getDatePattern();
		
		String[] dateFormats = dateFormatStr.split(",");
		for(String dtFormat: dateFormats)
		{
			SimpleDateFormat dateFormat= new SimpleDateFormat(dtFormat,CommonServiceLocator
					.getInstance().getDefaultLocale());
			try
			{
				Date date= dateFormat.parse(dtStr);
				isDate=dtStr.equals(dateFormat.format(date));
				if(isDate)
				{
					Calendar gcalendar = new GregorianCalendar();
			        gcalendar.setTime(date);
					int year=gcalendar.get(Calendar.YEAR);
					isDate^=(year < minYear || year > maxYear);
				}
			}
			catch (ParseException exp)
			{
				LOGGER.error("Date '" +dtStr+ "' is not valid for format:" + dtFormat);
				isDate = false;
			}
			if(isDate)
			{
				break;
			}
		}
		if (!isDate)
		{
			LOGGER.error("Date is not valid:" + dtStr);
		}
		return isDate;
	}

	/**
	 * To check date.
	 * @param checkDate date value
	 * @return result valid or not
	 */
	public boolean checkDate(String checkDate)
	{
		boolean result = true;
		try
		{
			if (isEmpty(checkDate))
			{
				result = false;
			}
			else
			{
				if (isValidDatePattern(checkDate))
				{
					result = isDate(checkDate);
				}
				else
				{
					result = false;
				}
			}
		}
		catch (Exception exp)
		{
			LOGGER.error("Check Date : exp : " + exp);
			result = false;
		}
		return result;
	}

	/**
	 * To compare date with current one.
	 * @param dateToCheck date value to check
	 * @return result true or false
	 */
	public boolean compareDateWithCurrent(String dateToCheck)
	{
		boolean result = true;
		Date currentDate = Calendar.getInstance().getTime();
		String pattern=CommonServiceLocator.getInstance().getDatePattern();
		
		String[] dateFormats = pattern.split(",");
		for(String dtFormat: dateFormats)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat,CommonServiceLocator
					.getInstance().getDefaultLocale());
			result = true;			
			try
			{
				Date toCheck = dateFormat.parse(dateToCheck);
				int dateCheckResult = currentDate.compareTo(toCheck);
				if (dateCheckResult < 0)
				{
					result = false;
				}
				if(result)
				{
					break;
				}
			}
			catch (Exception exp)
			{
				LOGGER.error("Date '" +dateToCheck+ "' is not valid for format:" + dtFormat);
				result = false;
			}
		}
		return result;
	}

	/**
	 * To compare Dates.
	 * @param startDate start date
	 * @param endDate end date
	 * @return result true or false
	 */
	public boolean compareDates(String startDate, String endDate)
	{
		boolean result = true;
		isValidDatePattern(startDate);
		String pattern=CommonServiceLocator.getInstance().getDatePattern();
		
		String[] dateFormats = pattern.split(",");
		for(String dtFormat: dateFormats)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat,CommonServiceLocator
					.getInstance().getDefaultLocale());
			result = true;
			try
			{
				Date toCheck = dateFormat.parse(startDate);
				isValidDatePattern(endDate);
				SimpleDateFormat dF1 = new SimpleDateFormat(dtFormat,CommonServiceLocator
						.getInstance().getDefaultLocale());
				Date maxDate = dF1.parse(endDate);
				int dateCheckResult = maxDate.compareTo(toCheck);
				if (dateCheckResult < 0)
				{
					result = false;
				}
				if(result)
				{
					break;
				}
			}
			catch (Exception exp)
			{
				LOGGER.error("Date '" +startDate+ "' is not valid for format:" + dtFormat);
				result = false;
			}
		}
		return result;
	}

	/**
	 * method to check date and format the errors accordingly.
	 * Error messages for empty date, Invalid date, and Future dates are formatted .
	 * @param strDate date
	 * @param checkFutureDate boolean value
	 * @return string error-key in property file
	 */
	public String validateDate(String strDate, boolean checkFutureDate)
	{
		String returnString = "";
		if (isEmpty(strDate))
		{
			returnString = "errors.item.required";
		}
		else
		{
			returnString = chkDateFormat(strDate);
			if ("".equals(returnString)
					&& checkFutureDate && !compareDateWithCurrent(strDate))
				{
					returnString = "errors.invalid.date";
				}
		}
		return returnString;
	}

	/**
	 * @param strDate Date as String.
	 * @return error key if any.
	 */
	private String chkDateFormat(String strDate)
	{
		String returnString ="";
		if (!isValidDatePattern(strDate) || !isDate(strDate))
		{
			returnString = "errors.date.format";
		}
		return returnString;
	}

	/**
	 * method to check operator is valid or not.
	 * @param value operator
	 * @return Returns TRUE if operator is valid else return FALSE
	 */
	public boolean isOperator(String value)
	{
		boolean isOperator = true;
		if (value == null || value.equals(CommonConstants.ANY) || value.equals("-1"))
		{
			isOperator = false;
		}
		return isOperator;
	}

	/**
	 * method to check value.
	 * @param value value
	 * @return Returns TRUE if value is valid else return FALSE
	 */
	public boolean isValue(String value)
	{
		boolean isValue = true;
		//Purposefully not checked for value==null.
		if (value != null
				&& ("".equals(value.trim()) || value.equals("-1") || value
						.equals(CommonConstants.SELECT_OPTION)))
		{
			isValue = false;
		}
		return isValue;
	}

	// -------------Date Validation ends---------

	/**
	 * metho to check zipcode.
	 * @param zipCode zipcode
	 * @return Returns true if zip code is valid else returns false.
	 */
	public boolean isValidZipCode(String zipCode)
	{
		boolean result = false;
		try
		{
			// valid format for zip code are '99999-9999' or '99999'
			Pattern pattern = Pattern.compile("\\d{5}\\-\\d{4}|\\d{5}");
			Matcher matcher = pattern.matcher(zipCode);
			result = matcher.matches();
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			//LOGGER.error("Check Zip Code : exp : "+ exp);
			result = false;
		}
		return result;
	}

	/**
	 * method to check phoneNumber.
	 * @param phoneNumber phoneNumber
	 * @return Returns true if Phone/Fax number is valid else returns false.
	 */
	public boolean isValidPhoneNumber(String phoneNumber)
	{
		boolean result = false;
		try
		{
			// valid format for phone/fax number ar '(999)999-9999' or '999-999-9999'
			Pattern pattern = Pattern.compile(REGEX_VALID_PHONE); // for fax and phone
			Matcher matcher = pattern.matcher(phoneNumber);
			result = matcher.matches();
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			//LOGGER.error("Check Phone/Fax Number : exp : "+ exp);
			result = false;
		}
		return result;
	}

	/**
	 * Returns true if the time is in invalid pattern else returns false.
	 * @param time the time to be validated.
	 * @param pattern the pattern in which it is to be validated.
	 * @return true if the time is in invalid pattern else returns false.
	 */
	public boolean isValidTime(String time, String pattern)
	{
		boolean isValid = true;

		try
		{
			CommonUtilities.parseDate(time, pattern);
		}
		catch (ParseException parseExp)
		{
			LOGGER.debug(parseExp.getMessage(), parseExp);
			isValid = false;
		}

		return isValid;
	}

	/**
	 * Returns true if enumerated value is valid else false.
	 * @param list List the list of enumerated values.
	 * @param value String the value that is to be checked.
	 * @return true if enumerated value is valid else false.
	 */
	public static boolean isEnumeratedValue(List<NameValueBean> list, String value)
	{
		boolean isValid = false;

		if (list != null && value != null)
		{
			isValid = isValueInList(list, value);
		}

		return isValid;
	}

	/**
	 * Returns true if enumerated value is valid else false.
	 * @param list List - list of object NameValueBean
	 * @param value String the value that is to be checked.
	 * @return true if enumerated value is valid else false.
	 */
	private static boolean isValueInList(List<NameValueBean> list, String value)
	{
		boolean isValid = false;
		for (int i = 1; i < list.size(); i++)
		{
			NameValueBean bean = list.get(i);

			if (value.equals(bean.getValue()))
			{
				isValid = true;
				break;
			}
		}
		return isValid;
	}

	/**
	 * Returns true if enumerated value is valid else false.
	 * @param list String[] the list of enumerated values.
	 * @param value String the value that is to be checked.
	 * @return true if enumerated value is valid else false.
	 */
	public static boolean isEnumeratedValue(String[] list, String value)
	{
		boolean isValid = false;

		if (list != null && value != null)
		{
			for (int i = 1; i < list.length; i++)
			{
				if (value.equals(list[i]))
				{
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}

	/**
	 * Returns true if enumerated value is valid or null else false.
	 * @param list List - the list of enumerated values.
	 * @param value String - the value that is to be checked.
	 * @return true if enumerated value is valid or null else false.
	 */
	public static boolean isEnumeratedOrNullValue(List<NameValueBean> list, String value)
	{
		boolean isValid = false;

		if (value == null)
		{
			isValid = true;
		}
		else if (list != null)
		{
			isValid = isValueInList(list, value);
		}
		return isValid;
	}

	/**
	 * Returns true if enumerated value is valid or null else false.
	 * @param list String[]- the list of enumerated values.
	 * @param value String -the value that is to be checked.
	 * @return true if enumerated value is valid or null else false.
	 */
	public static boolean isEnumeratedOrNullValue(String[] list, String value)
	{
		boolean isValid = false;
		if (value == null)
		{
			isValid = true;
		}
		else if (list != null)
		{
			isValid = isEnumeratedValue(list, value);
		}

		return isValid;
	}

	/**
	 * method to check SpecialCharacters.
	 * @param mainString mainString
	 * @param delimiter delimiter
	 * @return true if contains SpecialCharacters else false.
	 */
	public boolean containsSpecialCharacters(String mainString, String delimiter)
	{
		boolean hasSpChars = false;
		try
		{
			char[] specialChars = delimiter.toCharArray();
			for (int spCharCount = 0; spCharCount < specialChars.length; spCharCount++)
			{
				char searchChar = specialChars[spCharCount];
				int pos = mainString.indexOf(searchChar);
				if (pos != -1)
				{
					hasSpChars = true;
					break;
				}
			}
		}
		catch (Exception exp)
		{
			LOGGER.debug(exp.getMessage(), exp);
			hasSpChars = true;
		}

		return hasSpChars;
	}

	/**
	 * Method to return difference between to dates in days.
	 * @param startDate start Date.
	 * @param endDate end Date.
	 * @return return difference between to dates in days.
	 */
	public long getDateDiff(Date startDate, Date endDate)
	{
		long time1 = startDate.getTime();
		long time2 = endDate.getTime();
		long diff = time2 - time1;
		return diff / (ONE_THOUSAND * SEC_IN_A_MIN * MIN_IN_A_HR * HRS_IN_A_DAY);
	}

	/**
	 * This method check for xxs vulnerable characters like <, >, (, ) etc.
	 * @param value String for which xss vulnerable character [ (,),< or > ] to be checked.
	 * @return true if the string contains any xss vulnerable character else false.
	 */
	public static boolean isXssVulnerable(String value)
	{
		boolean isXssVulnerable = false;
		if (value != null)
		{
			Pattern pattern = Pattern.compile(REGEX_XSS_VULNERABLE);
			Matcher matcher = pattern.matcher(value);
			isXssVulnerable = matcher.find();
		}
		return isXssVulnerable;
	}
}