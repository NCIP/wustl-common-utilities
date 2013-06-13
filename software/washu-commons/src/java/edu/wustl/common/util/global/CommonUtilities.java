/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.global;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

public class CommonUtilities
{


	/**
	 * LOGGER -Generic Logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CommonUtilities.class);

	/**
	 * Parses the string format of date in the given format and returns the Data object.
	 * @param date the string containing date.
	 * @param pattern the pattern in which the date is present.
	 * @return the string format of date in the given format and returns the Data object.
	 * @throws ParseException throws ParseException if date is not in pattern specified.
	 */
	public static Date parseDate(String date, String pattern) throws ParseException
	{
		Date dateObj = null;
		if (date != null && !CommonConstants.EMPTY_STRING.equals(date.trim()))
		{
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CommonServiceLocator
						.getInstance().getDefaultLocale());
				dateObj = dateFormat.parse(date);
			}
			catch (ParseException exception)
			{
				String message = new StringBuffer("Date '").append(date).append(
						"' is not in format : ").append(pattern).toString();
				LOGGER.debug(message, exception);
				throw new ParseException(message, exception.getErrorOffset());
			}
		}
		return dateObj;
	}

	/**
	 * Parses the fully qualified class name and returns only the class name.
	 * @param qualifiedName The fully qualified class name.
	 * @return The class name.
	 */
	public static String parseClassName(String qualifiedName)
	{
		final int index = qualifiedName.lastIndexOf('.');
		final String className = qualifiedName.substring(index+1);
		return className;
	}


	/**
	 * Parses the string format of date in the given format and returns the Data object.
	 * @param date the string containing date.
	 * @return the string format of date in the given format and returns the Data object.
	 * @throws ParseException throws ParseException if date is not valid.
	 */
	public static Date parseDate(String date) throws ParseException
	{
		String pattern = datePattern(date);

		return parseDate(date, pattern);
	}

	/**
	 * Returns matching date pattern.
	 * @param strDate Date as string
	 * @return matched date pattern.
	 */
	public static String datePattern(String strDate)
	{
		String datePattern = null;
		if(strDate != null)
		{
			datePattern = CommonServiceLocator.getInstance().getDatePattern();
			/*List<SimpleDateFormat> datePatternList = new ArrayList<SimpleDateFormat>();
			datePatternList.add(new SimpleDateFormat("MM-dd-yyyy", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			datePatternList.add(new SimpleDateFormat("MM/dd/yyyy", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			datePatternList.add(new SimpleDateFormat("yyyy-MM-dd", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			datePatternList.add(new SimpleDateFormat("yyyy/MM/dd", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			datePatternList.add(new SimpleDateFormat("dd-MM-yyyy", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			datePatternList.add(new SimpleDateFormat("dd/MM/yyyy", CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			Date date = null;
			String matchingPattern = null;
			for (SimpleDateFormat dtPattern : datePatternList)
			{
				try
				{
					date = dtPattern.parse(strDate);
					if (date != null)
					{
						matchingPattern = dtPattern.toPattern();
						if (strDate.equals(dtPattern.format(date)))
						{
							datePattern = matchingPattern;
							break;
						}
					}
				}
				catch (ParseException exception)
				{
					LOGGER.info(strDate+" date not in formate:" + dtPattern.toPattern());
				}
			}*/
		}
		return datePattern;
	}

	/**
	 * This method creates Accessor Method Name.
	 * @param attr attribute
	 * @param isSetter specifies is Setter.
	 * @return Method Name.
	 */
	public static String createAccessorMethodName(String attr, boolean isSetter)
	{
		String str = "get";
		if (isSetter)
		{
			str = "set";
		}
		StringBuffer mathodname = new StringBuffer(attr);
		mathodname.setCharAt(0, Character.toUpperCase(attr.charAt(0)));
		mathodname.insert(0, str);
		return mathodname.toString();
	}

	/**
	 * Create the getter method of attribute.
	 * @param obj Object
	 * @param attrName attribute Name.
	 * @return Object.
	 * @throws InvocationTargetException InvocationTargetException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws NoSuchMethodException NoSuchMethodException
	 */
	public static Object getValueFor(Object obj, String attrName) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		//Create the getter method of attribute
		String methodName = createAccessorMethodName(attrName, false);
		Class objClass = obj.getClass();
		Method method = objClass.getMethod(methodName, new Class[0]);

		return method.invoke(obj, new Object[0]);
	}

	/**
	 * Start: Change for API Search   --- Jitendra 06/10/2006
	 * In Case of Api Search, previoulsy it was failing since there was default
	 * class level initialization
	 * on domain object. For example in ParticipantMedicalIdentifier object, it was initialized as
	 * protected Site site= new Site(); So we removed default class level initialization on domain object.
	 * Hence getValueFor() method was returning null. So write new method SetValueFor() which will
	 * instantiate new Object and set it in parent object.
	 * @param obj Object
	 * @param attrName attribute Name.
	 * @param attrValue attribute Value.
	 * @return Object.
	 * @throws IllegalAccessException Illegal Access Exception
	 * @throws InstantiationException Instantiation Exception
	 * @throws InvocationTargetException Invocation Target Exception
	 */
	public static Object setValueFor(Object obj, String attrName, Object attrValue)
			throws InstantiationException, IllegalAccessException, InvocationTargetException

	{

		//create the setter method for the attribute.
		String methodName = createAccessorMethodName(attrName, true);
		Method method = findMethod(obj.getClass(), methodName);
		Object object = attrValue;
		if (object == null)
		{
			object = method.getParameterTypes()[0].newInstance();
		}
		Object[] objArr = {object};
		//set the newInstance to the setter nethod of parent obj
		method.invoke(obj, objArr);
		return object;

	}

	/**
	 * Finds method of given method Name.
	 * @param objClass Class.
	 * @param methodName the method Name
	 * @return Method
	 */
	private static Method findMethod(Class objClass, String methodName)
	{
		Method[] methods = objClass.getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++)
		{
			if(!methods[i].getName().equals(methodName)
					||
					methods[i].getParameterTypes()[0].isInterface())
            {
                continue;
            }
            method = methods[i];
            if(!"java.lang.Object".equals(method.getParameterTypes()[0].getName()))
            {
                break;
            }
		}
		return method;
	}



	/**
	 * This method gets Array String of Object Array.
	 * @param objectIds Array of object Ids.
	 * @return Array String.
	 */
	public static String getArrayString(Object[] objectIds)
	{
		StringBuffer arrayStr = new StringBuffer();
		for (int i = 0; i < objectIds.length; i++)
		{
			arrayStr.append(objectIds[i].toString()).append(',');
		}
		return arrayStr.toString();
	}

	/**
	 * This method gets Class Object.
	 * @param fullClassName Full qualified name
	 * @return Class.
	 */
	public static Class getClassObject(String fullClassName)
	{
		Class className = null;
		try
		{
			className = Class.forName(fullClassName);
		}
		catch (ClassNotFoundException classNotExcp)
		{
			LOGGER.warn("Didn't find any class as " + fullClassName, classNotExcp);
		}

		return className;
	}

	/**
	 * This method checks for null value.
	 * @param obj Object to be check.
	 * @return true if null else false.
	 */
	public static boolean isNull(Object obj)
	{
		boolean isNull = false;
		if (obj == null)
		{
			isNull = true;
		}
		return isNull;
	}

	/**
	 * Instantiates and returns the object of the class name passed.
	 * @param className The class name whose object is to be instantiated.
	 * @return the object of the class name passed.
	 */
	public static Object getObject(String className)
	{
		Object object = null;

		try
		{
			Class classObject = getClassObject(className);
			if (classObject != null)
			{
				object = classObject.newInstance();
			}
		}
		catch (InstantiationException instExp)
		{
			LOGGER.debug("Can not create instance of class:" + className, instExp);
		}
		catch (IllegalAccessException illAccExp)
		{
			LOGGER.debug("Can not create instance of class:" + className, illAccExp);
		}

		return object;
	}

	/**
	 * This method add Element in object array.
	 * @param array object array.
	 * @param obj Object to be add.
	 * @return object array.
	 */
	public static Object[] addElement(Object[] array, Object obj)
	{
		int arraySize = array.length + 1;
		Object[] newObjectArr = new Object[arraySize];

		if (array instanceof String[])
		{
			newObjectArr = new String[arraySize];
		}

		System.arraycopy(array, 0, newObjectArr, 0, array.length);
		newObjectArr[array.length] = obj;
		return newObjectArr;
	}



	/**
	 * This method removes null values from given list.
	 * @param list list.
	 * @return List without null values.
	 */
	public static List removeNull(List list)
	{
		List nullFreeList = new ArrayList();
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) != null)
			{
				nullFreeList.add(list.get(i));
			}
		}
		return nullFreeList;
	}



	/**
	 * Returns name of FormBean specified in struts-config.xml for passed Object of FormBean.
	 * @param obj - FormBean object
	 * @return String - name of FormBean object
	 */
	public static String getFormBeanName(Object obj)
	{
		String className=obj.getClass().getSimpleName();
		 String classNameFirstCharacter=className.substring(0,1);

		className=classNameFirstCharacter.toLowerCase(CommonServiceLocator.getInstance().getDefaultLocale())
		+className.substring(1,
				(className.length()));
		return className;
	}

	/**
	 * Parses the Date in given format and returns the string representation.
	 * @param date the Date to be parsed.
	 * @param pattern the pattern of the date.
	 * @return returns the string representation of Date.
	 */
	public static String parseDateToString(Date date, String pattern)
	{
		String dateStr = CommonConstants.EMPTY_STRING;
		if (date != null)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, CommonServiceLocator
					.getInstance().getDefaultLocale());
			dateStr = dateFormat.format(date);
		}
		return dateStr;
	}

	/**
	 * This method converts object to string.
	 * @param obj object to be convert.
	 * @return string object.
	 */
	public static String toString(Object obj)
	{
		String retValue;
		if (obj == null)
		{
			retValue = CommonConstants.EMPTY_STRING;
		}
		else
		{
			retValue = obj.toString();
		}

		return retValue;
	}



	/**
	 * This method converts collection to Long Array.
	 * @param collection Collection to be convert.
	 * @return long array.
	 */
	public static Long[] toLongArray(Collection<Long> collection)
	{

		Long[] obj = new Long[collection.size()];

		int index = 0;
		Iterator<Long> iterator = collection.iterator();
		while (iterator.hasNext())
		{
			obj[index] = iterator.next();
			index++;
		}
		return obj;
	}

	/**
	 * This method convert object to int.
	 * @param obj Object to be convert.
	 * @return int value.
	 */
	public static int toInt(Object obj)
	{
		int value = 0;
		if (obj != null)
		{
			String objVal = String.valueOf(obj);
			if (objVal.length() > 0)
			{
				Integer intObj = Integer.parseInt(objVal);
				value = intObj.intValue();
			}
		}
		return value;
	}

	/**
	 * This method convert object to Long.
	 * @param obj Object to be convert.
	 * @return long value.
	 */
	public static long toLong(Object obj)
	{
		long value = 0;
		if (obj != null)
		{
			String objVal = String.valueOf(obj);
			if (objVal.length() > 0)
			{
				Long intObj = Long.parseLong(objVal);
				value = intObj.longValue();
			}
		}
		return value;

	}

	/**
	 *  This method convert object to Double.
	 * @param obj Object to be convert.
	 * @return Double value.
	 */
	public static double toDouble(Object obj)
	{
		double value = 0;
		if (obj != null)
		{
			value = ((Double) obj).doubleValue();
		}
		return value;
	}


	/**
	 * @param str String to be converted to Proper case.
	 * @return The String in Proper case.
	 */
	public static String initCap(String str)
	{
		StringBuffer retStr;
		if (Validator.isEmpty(str))
		{
			retStr = new StringBuffer();
			LOGGER.debug("Utility.initCap : - String provided is either empty or null" + str);
		}
		else
		{
			retStr = new StringBuffer(str.toLowerCase(CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			retStr.setCharAt(0, Character.toUpperCase(str.charAt(0)));
		}
		return retStr.toString();
	}

	/**
	 * Specifies date pattern.
	 */
	private static String pattern = CommonServiceLocator.getInstance().getDatePattern();//"MM-dd-yyyy";

	/**
	 * This method gets month from given date.
	 * @param date date
	 * @return month.
	 */
	public static int getMonth(String date)
	{
		return getCalendar(date, pattern).get(Calendar.MONTH) + 1;
	}

	/**
	 * This method gets day from given date.
	 * @param date date
	 * @return day.
	 */
	public static int getDay(String date)
	{
		return getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * This method gets Year from given date.
	 * @param date date
	 * @return Year.
	 */
	public static int getYear(String date)
	{
		return getCalendar(date, pattern).get(Calendar.YEAR);
	}

	/**
	 * This method gets Calendar.
	 * @param date date
	 * @param pattern pattern.
	 * @return Calendar
	 */
	private static Calendar getCalendar(String date, String pattern)
	{
		Calendar calendar = Calendar.getInstance();
		try
		{

			if(date != null && !date.trim().equals(""))
			{
				SimpleDateFormat dateformat = new SimpleDateFormat(pattern, CommonServiceLocator
						.getInstance().getDefaultLocale());
				Date givenDate = dateformat.parse(date);
				calendar = Calendar.getInstance();
				calendar.setTime(givenDate);
			}
		}
		catch (ParseException exception)
		{
			LOGGER.error("exception in getCalendar: date=" + date, exception);
			//calendar = Calendar.getInstance();
		}
		return calendar;
	}

	/**
	 * This method replace string.
	 * This method is used in AutoCompleteTag.java and SimpleQueryInterfaceForm.java(SimpleQuery)
	 * @param source source String
	 * @param toReplace toReplace String
	 * @param replacement replacement String
	 * @return String.
	 */
	public static String replaceAll(String source, String toReplace, String replacement)
	{
		String sourceString = source;
		if (sourceString.contains(toReplace))
		{
			sourceString = sourceString.replace(toReplace, replacement);
		}
		return sourceString;
	}

	/**
	 * Returns current thread's class loader.
	 * @return current thread's class loader.
	 */
	public static ClassLoader getCurrClassLoader()
	{
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * This method parse Attribute Name.
	 * @param methodName method Name to be parse.
	 * @return attribute Name.
	 */
	public static String parseAttributeName(String methodName)
	{
		StringBuffer attributeName = new StringBuffer();
		int index = methodName.indexOf("get");
		if (index != -1)
		{
			attributeName.append(methodName.substring(index + "get".length()));
		}
		attributeName.setCharAt(0, Character.toLowerCase(attributeName.charAt(0)));
		return attributeName.toString();
	}

	/**
	 * For MSR changes.
	 * @return All Privileges.
	 */
	public static List getAllPrivileges()
	{
		List<NameValueBean> allPrivileges = new ArrayList<NameValueBean>();

		List<NameValueBean> list1 = Variables.privilegeGroupingMap.get("SITE");
		List<NameValueBean> list2 = Variables.privilegeGroupingMap.get("CP");
		List<NameValueBean> list3 = Variables.privilegeGroupingMap.get("SCIENTIST");
		List<NameValueBean> list4 = Variables.privilegeGroupingMap.get("GLOBAL");

		allPrivileges.addAll(list1);
		allPrivileges.addAll(list2);
		allPrivileges.addAll(list3);
		allPrivileges.addAll(list4);
		return allPrivileges;
	}
	/**
	 * gets unique key for name,barcode etc...
	 * @return string
	 */
	public static String getUniqueKey()
	{
		Date date = new Date();
		String uniqueKey = String.valueOf(date.getTime());
		return uniqueKey;
	}
}
