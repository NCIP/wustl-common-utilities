/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao;

/**
 * @author kalpana_thakur
 */
public class DatabaseProperties
{

	/**
	 * Database Name.
	 */
	private String dataBaseType;

	/**
	 * Database specific date pattern.
	 */
	private String datePattern;

	/**
	 * Database specific time pattern.
	 */
	private String timePattern;
	/**
	 * Database specific date format function.
	 */
	private String dateFormatFunction;
	/**
	 * Database specific time format function.
	 */
	private String timeFormatFunction;
	/**
	 * Database specific date to string function.
	 */
	private String dateTostrFunction;
	/**
	 * Database specific string to date function.
	 */
	private String strTodateFunction;
	/**
	 * Database data source name.
	 */
	private String dataSource;
	/**
	 * Database default batch size.
	 */
	private int defaultBatchSize;

    /**
     * Query executor name.
     */
	private String queryGeneratorName;

	/**
	 * This method will be called to get the database name.
	 * @return database name.
	 */
	public String getDataBaseType()
	{
		return dataBaseType;
	}

	/**
	 * This method will be called to set the database name.
	 * @param dataBaseType : database Type.
	 */
	public void setDataBaseType(String dataBaseType)
	{
		this.dataBaseType = dataBaseType;
	}


	/**
	 * This method will be called to get the query executor name.
	 * @return query executor name.
	 */
	public String getQueryGeneratorName()
	{
		return queryGeneratorName;
	}


	/**
	 * This method will be called to set the query executor name.
	 * @param queryGeneratorName query generator name.
	 */
	public void setQueryGeneratorName(String queryGeneratorName)
	{
		this.queryGeneratorName = queryGeneratorName;
	}

	/**
	 * @return :This will return the Date Pattern.
	 */
	public String getDatePattern()
	{
		return datePattern;
	}

	/**
	 * @return :This will return the Time Pattern.
	 */
	public String getTimePattern()
	{
		return timePattern;
	}
	/**
	 * @return :This will return the Date Format Function.
	 */
	public String getDateFormatFunction()
	{
		return dateFormatFunction;
	}
	/**
	 * @return :This will return the Time Format Function.
	 */
	public String getTimeFormatFunction()
	{
		return timeFormatFunction;
	}

	/**
	 * @return :This will return the Date to string function
	 */
	public String getDateTostrFunction()
	{
		return dateTostrFunction;
	}
	/**
	 * @return :This will return the string to Date function
	 */
	public String getStrTodateFunction()
	{

		return strTodateFunction;
	}

	/**
	 * This method will set the date pattern.
	 * @param datePattern  date pattern.
	 */
	public void setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
	}

	/**
	 * This method will set the time pattern.
	 * @param timePattern time pattern.
	 */
	public void setTimePattern(String timePattern)
	{
		this.timePattern = timePattern;
	}

	/**
	 * This method will set the date format function.
	 * @param dateFormatFunction date format function.
	 */
	public void setDateFormatFunction(String dateFormatFunction)
	{
		this.dateFormatFunction = dateFormatFunction;
	}

	/**
	 * This method will set the time format function.
	 * @param timeFormatFunction time format function
	 */
	public void setTimeFormatFunction(String timeFormatFunction)
	{
		this.timeFormatFunction = timeFormatFunction;
	}

	/**
	 * This method will set the date to string function.
	 * @param dateTostrFunction date to string function
	 */
	public void setDateTostrFunction(String dateTostrFunction)
	{
		this.dateTostrFunction = dateTostrFunction;
	}

	/**
	 * This method will set the string to date function.
	 * @param strTodateFunction string to date function.
	 */
	public void setStrTodateFunction(String strTodateFunction)
	{
		this.strTodateFunction = strTodateFunction;
	}

	 /**
	 * This method will be called to get the data source.
	 * @return dataSource
	 */
	 public String getDataSource()
	 {
		 return dataSource;
	 }

	 /**
	 * This method will be called to set the data source.
	 * @param dataSource : JDBC connection name.
	 */
	 public void setDataSource(String dataSource)
	 {
		 this.dataSource = dataSource;
	 }
	 /**
	 * This method will be called to set the default batch size.
	 * @param batchSize : batch size
	 */
	public void setDefaultBatchSize(int batchSize)
	{
		this.defaultBatchSize = batchSize;
	}

	/**
	 * This will be called to get the default batch size.
	 * @return batch size
	 */
	public int getDefaultBatchSize()
	{
		return defaultBatchSize;
	}
}
