/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.impexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

/**
 * This class contains some common functionality for import/export process.
 * @author ravi_kumar
 *
 */
public abstract class AbstractAutomateImpExp implements IAutomateImpExp
{

	/**
	 * Index for file which contains all table names.
	 */
	private static final int  INDX_FOR_TABLE_NAMES_FILE=8;

	/**
	 * Index for csv file name.
	 */
	private static final int  INDX_FOR_CSV_FILE_NAME=9;

	/**
	 * dbUtil object is used to store the argument values and getting database connection.
	 */
	private DatabaseUtility dbUtil;
	/**
	 * Number of tables.
	 */
	private int size;

	/**
	 * File path.
	 */
	private String filePath;

	/**
	 * list of table name.
	 */
	private List<String> tableNamesList;

	/**
	 * @param dbUtil :object of DatabaseUtility
	 * @throws ApplicationException Application Exception
	 */
	public void init(DatabaseUtility dbUtil) throws ApplicationException
	{
		this.dbUtil=dbUtil;
	}

	/**
	 * This method do some process before import/export operation.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	protected void preImpExp(String[] args)  throws ApplicationException
	{
		tableNamesList=getTableNamesList(args[INDX_FOR_TABLE_NAMES_FILE]);
		size = tableNamesList.size();
		filePath = args[INDX_FOR_CSV_FILE_NAME].replaceAll("\\\\", "//");
	}
	/**
	 * Common.
	 * This method will read the table list file.
	 * @param fileName file name
	 * @return table list
	 * @throws ApplicationException Generic IO exception
	 */
	private List<String> getTableNamesList(String fileName) throws ApplicationException
	{
		ArrayList<String> tableNamesList = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String linereader = reader.readLine();
			while (linereader != null)
			{
				tableNamesList.add(linereader);
				linereader = reader.readLine();
			}
			reader.close();
		}
		catch(IOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.tablenamelist.error");
			throw new ApplicationException(errorKey,exception,"AbstractAutomateImpExp");
		}
		return tableNamesList;
	}

	/**
	 * This method closes database connection.
	 * @param conn Connection object.
	 * @throws ApplicationException Throws this exception if not able to close database connection.
	 */
	protected void closeConnection(Connection conn) throws ApplicationException
	{
		try
		{
			conn.close();
		}
		catch (SQLException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.dbclose.error");
			throw new ApplicationException(errorKey,exception,"AbstractAutomateImpExp");
		}
	}

	/**
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}


	/**
	 * @param size the size to set
	 */
	public void setSize(int size)
	{
		this.size = size;
	}


	/**
	 * @return the filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}


	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}


	/**
	 * @return the tableNamesList
	 */
	public List<String> getTableNamesList()
	{
		return tableNamesList;
	}


	/**
	 * @param tableNamesList the tableNamesList to set
	 */
	public void setTableNamesList(List<String> tableNamesList)
	{
		this.tableNamesList = tableNamesList;
	}

	/**
	 * @return the dbUtility
	 */
	public DatabaseUtility getDbUtility()
	{
		return dbUtil;
	}

	/**
	 * @param dbUtil the dbUtility to set
	 */
	public void setDbUtility(DatabaseUtility dbUtil)
	{
		this.dbUtil = dbUtil;
	}

}
