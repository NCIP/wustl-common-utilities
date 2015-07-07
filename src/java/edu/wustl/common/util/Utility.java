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
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exceptionformatter.ExceptionFormatterFactory;
import edu.wustl.common.report.bean.FileDetails;
import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;

/**
 * @author kapil_kaveeshwar
 *
 */
public final class Utility extends CommonUtilities
{
	/**
	 * private constructor.
	 */
	private Utility()
	{
		super() ;
	}
	/**
	 * Constant for TWO.
	 */
	private static final int TWO = 2;
	/**
	 * LOGGER -Generic Logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(Utility.class);
	/**
	 * Changes the format of the string compatible to Grid Format,
	 * removing escape characters and special characters from the string.
	 * @param obj - Unformatted obj to be printed in Grid Format
	 * @return obj - Foratted obj to print in Grid Format
	 */
	public static Object toGridFormat(Object obj)
	{
		Object retObj = obj;
		if (obj instanceof String)
		{
			StringBuffer tokenedString = new StringBuffer();
			StringTokenizer tokenString = new StringTokenizer((String) obj, "\n\r\f");
			while (tokenString.hasMoreTokens())
			{
				tokenedString.append(tokenString.nextToken()).append(' ');
			}
			String gridFormattedStr = new String(tokenedString);
			retObj = gridFormattedStr.replaceAll("\"", "\\\\\"");
		}

		return retObj;
	}
	/**
	 * checking whether key's value is persisted or not.
	 * @param map map.
	 * @param key key.
	 * @return Return true if Persisted Value in map else false.
	 */
	public static boolean isPersistedValue(Map map, String key)
	{
		Object obj = map.get(key);
		String val = null;
		boolean isPersistedValue = false;
		if (obj != null)
		{
			val = obj.toString();
		}
		if ((val != null && !(TextConstants.STR_ZERO.equals(val)))
				&& !(TextConstants.EMPTY_STRING.equals(val)))
		{
			isPersistedValue = true;
		}
		return isPersistedValue;
	}
	/**
	 * This method is used in JSP pages to get the width of columns for the html fields.
	 * It acts as a wrapper for the HibernateMetaData getColumnWidth() method.
	 * @param className Class name of the field
	 * @param attributeName Attribute name of the field.
	 * @return Length of the column.
	 * @throws DAOException database exception.
	 * @see HibernateMetaData.getColumnWidth()
	 */
	public static String getColumnWidth(Class className, String attributeName) throws DAOException
	{
		String applicationName = CommonServiceLocator.getInstance().getAppName() ;
		HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
					.getHibernateMetaData(applicationName) ;
		return Integer.toString((hibernateMetaData.getColumnWidth(className, attributeName)));
	}
	/**
	 * To sort the Tree nodes based on the comparators overidden by the TreeNodeImpl object.
	 * @param nodes reference to the Vector containing object of class implementing TreeNodeImpl class.
	 */
	public static void sortTreeVector(List nodes)
	{
		Collections.sort(nodes);
		for (int i = 0; i < nodes.size(); i++)
		{
			TreeNodeImpl child = (TreeNodeImpl) nodes.get(i);
			sortTreeVector(child.getChildNodes());
		}
	}
	/**
	 * Remove special characters and white space from a string.
	 * @param str string.
	 * @return String after removing special characters.
	 */
	public static String removeSpecialCharactersFromString(String str)
	{
		String regexExpression = "[\\p{Punct}\\s]";
		return str.replaceAll(regexExpression, "");
	}
	/**
	 * Returns the label for objects name. It compares ascii value of each char for lower or upper case and
	 * then forms a capitalized label.
	 * e.g. firstName is converted to First Name
	 * @param objectName name of the attribute
	 * @return capitalized label
	 */
	public static String getDisplayLabel(String objectName)
	{
		StringBuffer formatedStr = new StringBuffer();
		int prevIndex = 0;
		String tempStr;
		for (int i = 0; i < objectName.length(); i++)
		{
			if (Character.isUpperCase(objectName.charAt(i)))
			{
				tempStr = objectName.substring(prevIndex, i);
				if (!TextConstants.EMPTY_STRING.equals(tempStr))
				{
					formatedStr.append(initCap(tempStr));
					formatedStr.append(Constants.CONST_SPACE_CAHR);
				}
				prevIndex = i;
			}
		}
		tempStr = objectName.substring(prevIndex, objectName.length());
		formatedStr.append(initCap(tempStr));
		return formatedStr.toString();
	}
	/**
	 * This method gets time.
	 * @param date Date.
	 * @return time.
	 */
	public static String[] getTime(Date date)
	{
		String[] time = new String[TWO];
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		time[0] = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		time[1] = Integer.toString(cal.get(Calendar.MINUTE));
		return time;
	}
	/**
	 * For MSR changes.
	 * @throws edu.wustl.common.exception.ParseException throws this exception if
	 * specified xml file not found or not able to parse the file.
	 */
	public static void initializePrivilegesMap() throws edu.wustl.common.exception.ParseException
	{
		Map<String, String> privDetMap = Variables.privilegeDetailsMap;
		Map<String, List<NameValueBean>> privGroupMap = Variables.privilegeGroupingMap;
		InputStream inputXmlFile = getCurrClassLoader().getResourceAsStream(
				TextConstants.PERMSN_MAP_DET_FILE);
		if (inputXmlFile != null)
		{
			Document doc;
			try
			{
				doc = XMLParserUtility.getDocument(inputXmlFile);
			}
			catch (Exception ioe)
			{
				LOGGER.error(ioe.getMessage(), ioe);
				throw new edu.wustl.common.exception.ParseException(ioe);
			}
			finally
			{
				try
				{
					inputXmlFile.close();
				}
				catch (IOException exception)
				{
					LOGGER.error("Not able to close input stream", exception);
				}
			}
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("PrivilegeMapping");
			int length = nodeList.getLength();
			for (int counter = 0; counter < length; counter++)
			{
				Element element = (Element) (nodeList.item(counter));
				String key = element.getAttribute("key");
				String value = element.getAttribute("value");

				privDetMap.put(key, value);
			}
			privGroupMap.put("SITE", getPriviligesList(root, "siteMapping"));
			privGroupMap.put("CP", getPriviligesList(root, "collectionProtocolMapping"));
			privGroupMap.put("SCIENTIST", getPriviligesList(root, "scientistMapping"));
			privGroupMap.put("GLOBAL", getPriviligesList(root, "globalMapping"));
		}
	}
	/**
	 * returns Privilege List.
	 * @param root root Element.
	 * @param tagName tag Name.
	 * @return Priviliges List.
	 */
	private static List<NameValueBean> getPriviligesList(Element root, String tagName)
	{
		NodeList nodeList1 = root.getElementsByTagName(tagName);
		int length1 = nodeList1.getLength();
		List<NameValueBean> sitePrivList = new ArrayList<NameValueBean>();
		NameValueBean nmv;
		for (int counter = 0; counter < length1; counter++)
		{
			Element element = (Element) (nodeList1.item(counter));
			nmv = new NameValueBean(element.getAttribute("name"), element.getAttribute("id"));
			sitePrivList.add(nmv);
		}
		return sitePrivList;
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
	 * This method returns records per page from session.
	 * @param session HttpSession
	 * @return no of records per page has been extracted.
	 */
	public static int getRecordsPerPage(HttpSession session)
	{
		int recordsPerPage;
		String recPerPageSessVal = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		if (recPerPageSessVal == null)
		{
			recordsPerPage = Integer.parseInt(XMLPropertyHandler
					.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
			session.setAttribute(Constants.RESULTS_PER_PAGE, String.valueOf(recordsPerPage));
		}
		else
		{
			recordsPerPage = Integer.parseInt(recPerPageSessVal);
		}
		return recordsPerPage;
	}
	/**
	 * return the  actual class name.
	 * @param name String
	 * @return String
	 */
	public static String getActualClassName(String name)
	{
		String className = name;
		if (className != null && !className.trim().equals(TextConstants.EMPTY_STRING))
		{
			String splitter = "\\.";
			String[] arr = className.split(splitter);
			if (arr != null && arr.length != 0)
			{
				className = arr[arr.length - Constants.ONE];
			}
		}
		return className;
	}
	/**
	 * @param tableName : name of the table.
	 * @param jdbcDAO : JDBCDAO object.
	 * @return : String value
	 * @throws DAOException : throw DAOException
	 */
	public static String getDisplayName(String tableName,JDBCDAO jdbcDAO) throws DAOException
	{
		String displayName="";
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"+tableName+"'";
		try
		{
			ResultSet resultSet = jdbcDAO.getQueryResultSet(sql);
			while(resultSet.next())
			{
				displayName=resultSet.getString("DISPLAY_NAME");
				break;
			}
			resultSet.close();
		}
		catch(Exception ex)
		{
			LOGGER.error(ex.getMessage(),ex);
		}
		return displayName;
	}
	/**
	 * This method returns method object.
	 * @param obj Object
	 * @param method method
	 * @return the method object.
	 * @throws IllegalAccessException Illegal Access Exception
	 * @throws InvocationTargetException Invocation Target Exception.
	 */
	public static Object getValueFor(Object obj, Method method) throws IllegalAccessException,
			InvocationTargetException
	{
		return method.invoke(obj, new Object[0]);

	}
	/**
	 * Generates error messages.
	 * @param exep :
	 * @return error message.
	 */
	public static String generateErrorMessage(Exception exep)
	{
		String messageToReturn = "";
		if (exep instanceof HibernateException)
        {
            HibernateException hibernateException = (HibernateException) exep;
            StringBuffer message = new StringBuffer(messageToReturn);
            String[] str = hibernateException.getMessages();
            if (str == null)
            {
            	messageToReturn = "Unknown Error";
            }
            else
            {
            	  for (int i = 0; i < str.length; i++)
                  {
                  	message.append(str[i]).append(' ');
                  }
                  messageToReturn =  message.toString();
            }

        }
        else
        {
        	messageToReturn = exep.getMessage();
        }
		  return messageToReturn;
	}
	 /**
     * Constants that will appear in HQL for retreiving Attributes of the Collection data type.
     */
    private static final String ELEMENTS = "elements";
	/**
	 * Check whether the select Column start with "elements" & ends with ")" or not.
	 * @param columnName The columnName
	 * @return true if the select Column start with "elements" & ends with ")" or not
	 */
	public static boolean isColumnNameContainsElements(String columnName) // NOPMD
	{
		columnName = columnName.toLowerCase().trim();
		return columnName.startsWith(ELEMENTS) && columnName.endsWith(")");
	}
	/**
	 * Parse the exception object and find DB table name.
	 * @param objExcp exception object.
	 * @throws Exception Exception
	 * @return table Name.
	 */
	public static String parseException(Exception objExcp) throws Exception // NOPMD
	{
		LOGGER.debug(objExcp.getClass().getName());
		String tableName = "";
		if (objExcp instanceof gov.nih.nci.security.exceptions.CSTransactionException)
		{
			objExcp = (Exception) objExcp.getCause();
			LOGGER.debug(objExcp);
		}
		/*	if(args[0]!=null) {	tableName = (String)args[0]; }
		 *  else {logger.debug("Table Name not specified"); tableName=new String("Unknown Table"); }
			logger.debug("Table Name:" + tableName);*/
		//get Class name from message "could not insert [classname]"
		ConstraintViolationException cEX = (ConstraintViolationException) objExcp;
		String message = cEX.getMessage();
		LOGGER.debug("message :" + message);
		int startIndex = message.indexOf("[");
		/**
		 * Bug ID: 4926
		 * Description:In case of Edit, get Class name from message "could not insert [classname #id]"
		*/
		int endIndex = message.indexOf("#");
		if (endIndex == -1)
		{
			endIndex = message.indexOf("]");
		}
		String className = message.substring((startIndex + 1), endIndex);
		LOGGER.debug("ClassName: " + className);
		Class classObj = Class.forName(className);


		HibernateMetaData hibernateMetaData = HibernateMetaDataFactory
								.getHibernateMetaData(CommonServiceLocator.getInstance().getAppName()) ;
		tableName = hibernateMetaData.getRootTableName(classObj); // get table name from class
		/**
		 * Bug ID: 6034
		 * Description:To retrive the appropriate tablename checking the SQL"
		*/
		if (!(cEX.getSQL().contains(tableName)))
		{
			tableName = hibernateMetaData.getTableName(classObj);
			Properties prop = new Properties();
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(
					"tablemapping.properties"));
			if (prop.getProperty(tableName) != null)
			{
				tableName = prop.getProperty(tableName); // NOPMD
			}
		}
		return tableName;
	}
	/**
	 * Format and return message to display.
	 * @param columnNames column Names
	 * @param tableName table Name
	 * @param jdbcdao jdbc dao
	 * @return error message to display.
	 * @throws Exception
	 */
	public static String prepareMessage(StringBuffer columnNames, String tableName, JDBCDAO jdbcdao)
	{
		String formattedErrMsg = "";
		String columnName = ""; //stores Column_Name of table

		// Create arrays of object containing data to insert in CONSTRAINT_VOILATION_ERROR
		Object[] arguments = new Object[2];
		String dispTableName = ExceptionFormatterFactory.getDisplayName(tableName, jdbcdao);
		arguments[0] = dispTableName;
		columnName = columnNames.toString();
		columnName = columnName.substring(0, columnName.length());
		arguments[1] = columnName;
		LOGGER.debug("Column Name: " + columnNames.toString());

		// Insert Table_Name and Column_Name in CONSTRAINT_VOILATION_ERROR message
		formattedErrMsg = MessageFormat.format(Constants.CONSTRAINT_VOILATION_ERROR, arguments);

		return formattedErrMsg;
	}

	public static FileDetails generateFilePath(String reportFileName) throws IOException
	{
		StringBuffer dirName = new StringBuffer(System.getProperty("java.io.tmpdir"));
		/**
		 * This check is added for Linux and Mac OS
		 * System.getProperty(java.io.tmpdir) returns different values for
		 * different OS.
		 * On Windows: C:\DOCUME~1\<user>\LOCALS~1\Temp\
		 * On Solaris: /var/tmp/
		 * On Linux: /tmp
		 * On Mac OS X: /tmp
		 */
		if (!dirName.toString().endsWith(File.separator))
		{
			dirName.append(File.separator);
		}

		StringBuffer fileName = new StringBuffer(reportFileName);
		fileName = new StringBuffer(fileName.toString().replaceAll(" ", "_").replaceAll("\\.", "_")
				.replaceAll("-", "_"));

		/**
		 * Makes a directory, including any necessary but nonexistent parent directories.
		 * If there already exists a file with specified name or the
		 * directory cannot be created then an exception is thrown
		 */

		fileName.append(File.separator);
		fileName.append("_");
		fileName.append(new java.sql.Timestamp(new Date().getTime()));
		fileName = new StringBuffer(fileName.toString().replaceAll(" ", "_").replaceAll("\\.", "_")
				.replaceAll("-", "_").replaceAll(":", "_"));
		fileName = new StringBuffer(Utility.replaceSpCharForFile(fileName.toString(), "_"));
		dirName.append(fileName.toString());
		File file = new File(dirName.toString());
		FileUtils.forceMkdir(file);
		/**
		 * Schedules a file to be deleted when JVM exits.
		 * If file is directory delete it and all sub-directories.
		 */
		//FileUtils.forceDeleteOnExit(file);
		FileDetails fileDetails = new FileDetails(fileName.toString()+ Constants.CSV_FILE_EXTENTION, dirName.toString() + Constants.CSV_FILE_EXTENTION);
		return fileDetails;
	}

	public static String replaceSpCharForFile(String input, String replacement)
	{
		return input.replaceAll("[?:^/ \\ * < > | \"]", replacement);
	}

	/**
	 * @param response
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public static void sendFile(HttpServletResponse response, String fileName)
			throws FileNotFoundException, IOException, ApplicationException
	{
		File file = new File(fileName);
		FileInputStream inputStream;
		inputStream = new FileInputStream(file);

		byte[] fileContent = new byte[(int) file.length()];
		inputStream.read(fileContent);

		Blob reportContent = Hibernate.createBlob(fileContent);
		String[] tmpFile = fileName.split("/");
		sendFileToClient(reportContent, tmpFile[tmpFile.length - 1], response);
	}

	/**
	 * This method will send the file with the given filename in the response.
	 * 
	 * @param fileContent file to send
	 * @param fileName the file name
	 * @param response response in which to send the file.
	 * 
	 * @throws ApplicationException the application exception
	 */
	public static void sendFileToClient(final Blob fileContent, String fileName,
			final HttpServletResponse response) throws ApplicationException
	{
		try
		{
			response.setContentType("application/download");
			String csvFile = fileName.substring(fileName.lastIndexOf(System
					.getProperty("file.separator")) + 1);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + csvFile + "\";");
			response.setContentLength((int) fileContent.length());
			final OutputStream outputStream = response.getOutputStream();
			final BufferedInputStream bis = new BufferedInputStream(fileContent.getBinaryStream());
			final byte buf[] = new byte[4096];
			outputStream.flush();
			int count;

			while ((count = bis.read(buf)) > -1)
			{
				outputStream.write(buf, 0, count);
			}
			outputStream.flush();
			bis.close();
			response.flushBuffer();
		}
		catch (IOException ioException)
		{
			throw new ApplicationException(null, ioException, ioException.getMessage());
		}
		catch (SQLException sqlException)
		{
			throw new ApplicationException(null, sqlException, sqlException.getMessage());
		}

	}

}
