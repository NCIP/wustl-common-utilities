/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import oracle.sql.CLOB;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.SqlConstants;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * This class is for creating a file with a given list of data.
 * It creates the file according to delimeter specified.
 * For eg: if comma is the delimiter specified then a CSV file is created.
 * @author Poornima Govindrao
 * @author deepti_shelar
 * @author Supriya Dankh
 *
 */

public class ExportReport
{
	/**
	 * temp BufferedWriter temporary buffered writer.
	 */
	private transient BufferedWriter temp;
	/**
	 * zipFileName String zipped file name.
	 */
	private transient String zipFileName;
	/**
	 * path String path.
	 */
	private transient String path;
	/**
	 * cvsFileWriter BufferedWriter cvs file buffered writer.
	 */
	private transient BufferedWriter cvsFileWriter;
	/**
	 * fileName String file name.
	 */
	private transient String fileName;
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ExportReport.class);

	/**
	 * Constant for TWO.
	 */
	private static final int TWO = 2;
	/**
	 * @param fileName String file name.
	 * @throws IOException I/O exception.
	 */
	public ExportReport(String fileName) throws IOException
	{
		temp = new BufferedWriter(new FileWriter(fileName));
	}

	/**
	 * This method creates the file according to delimeter specified, without any indentation
	 * for the any row and no blank lines are inserted before start of values.
	 * @param values List
	 * @param delimiter delimiter
	 * @throws IOException I/O exception.
	 */
	public void writeData(List values, String delimiter) throws IOException
	{
		writeData(values, delimiter, 0, 0);
	}

	/**
	 * This constructor is called when the exported list contains data for 'file' attributes.
	 *
	 * @param path String specified path
	 * @param csvFileName String csv file name.
	 * @param zipFileName String zipped file name.
	 * @throws IOException I/O exception.
	 */
	public ExportReport(String path, String csvFileName, String zipFileName) throws IOException
	{
		this.path = path;
		this.zipFileName = zipFileName;
		this.fileName = csvFileName;
		this.cvsFileWriter = new BufferedWriter(new FileWriter(csvFileName));
	}

	/**
	 * This method creates a zip file which contains a csv file and other data files.
	 * The file is according to delimeter specified.
	 *
	 * @param values values list. It is List of List.
	 * @param delimiter delimiter used for separating individual fields.
	 * @param mainEntityIdsList list of main entity ids : required in case of file exports.
	 * @throws IOException I/O exception.
	 */
	public void writeDataToZip(List values, String delimiter,List mainEntityIdsList) throws IOException
	{
		List<String> files = new ArrayList<String>();
		Map<String, String> idFileNameMap = new HashMap<String, String>();
		if (mainEntityIdsList != null && !mainEntityIdsList.isEmpty())
		{
			StringBuffer sql=new StringBuffer(SqlConstants.SQL_REPORT_DATA);
			for (Iterator iterator = mainEntityIdsList.iterator(); iterator.hasNext();)
			{
				String mainEntityId = (String) iterator.next();
				sql.append(mainEntityId).append(',');
				String file = Constants.EXPORT_FILE_NAME_START + mainEntityId + ".txt";
				files.add(path + file);
				idFileNameMap.put(mainEntityId + ".txt", file);
			}
			sql = new StringBuffer(sql.substring(0, sql.lastIndexOf(",")));
			sql.append(')');
			createDataFiles(sql.toString());
		}
		createCSVFile(values, delimiter,idFileNameMap);
		closeFile();
		files.add(fileName);
		createZip(files);
	}

	/**
	 * Creates text files for reports.
	 * @param sql to get the reports
	 */
	private void createDataFiles(String sql)
	{
		try
		{
			List list = executeQuery(sql);
			if (!list.isEmpty())
			{
				Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					List columnList = (List) iterator.next();
					createFile(columnList);
				}
			}
		}
		catch (Exception excp)
		{
			LOGGER.error(excp.getMessage(), excp);
		}
	}

	/**
	 * write the column details in created file.
	 * @param columnList containing list of columns.
	 * @throws SQLException sql exception
	 * @throws IOException I/O exception.
	 */
	private void createFile(List columnList) throws SQLException, IOException
	{
		CLOB clob;
		BufferedReader bufferReader;
		if (!columnList.isEmpty())
		{
			if (columnList.get(1) instanceof CLOB)
			{
				clob = (CLOB) columnList.get(1);
				bufferReader = new BufferedReader(clob.getCharacterStream());
			}
			else
			{
				String data = (String) columnList.get(1);
				bufferReader = new BufferedReader(new StringReader(data));
			}
			String mainEntityId = (String) columnList.get(0);
			String dataFileName = path + Constants.EXPORT_FILE_NAME_START + mainEntityId + ".txt";
			File outFile = new File(dataFileName);
			FileWriter out = new FileWriter(outFile);
			StringBuffer strOut = new StringBuffer();
			String aux=bufferReader.readLine();
			while (aux != null)
			{
				strOut.append(aux);
				strOut.append(TextConstants.LINE_SEPARATOR);
				aux=bufferReader.readLine();
			}
			bufferReader.close();
			out.write(strOut.toString());
			out.close();
		}
	}

	/**
	 * Creates a csv file.
	 * @param values List data
	 * @param delimiter String comma
	 * @param idFileNameMap Map of type String map which stores id and file name
	 * @throws IOException I/O exception.
	 */
	private void createCSVFile(List values, String delimiter,Map<String, String> idFileNameMap)
		throws IOException
	{
		if (values != null)
		{
			Iterator itr = values.iterator();
			while (itr.hasNext())
			{
				List rowValues = (List) itr.next();
				Iterator rowItr = rowValues.iterator();
				while (rowItr.hasNext())
				{
					String tempStr = (String) rowItr.next();
					if (tempStr.indexOf(Constants.EXPORT_FILE_NAME_START) != -1)
					{
						String[] split = tempStr.split("_");
						String entityId = split[TWO];
						String fName = idFileNameMap.get(entityId);
						tempStr = fName;
					}
					cvsFileWriter.write(getStrWithoutDoubleQuotes(tempStr) + delimiter);
				}
				cvsFileWriter.write(TextConstants.LINE_SEPARATOR);
			}
		}
	}

	/**
	 * This method removes double quotes from string and adds at start and end.
	 * @param str String in which double quotes has to remove.
	 * @return  String without double quotes.
	 */
	private String getStrWithoutDoubleQuotes(String str)
	{
		String tempStr;
		if (str == null)
		{
			tempStr = TextConstants.EMPTY_STRING;
		}
		else
		{
			tempStr = str.replaceAll( "\"", "'");
		}

		return new StringBuffer().append('"').append(tempStr).append('"').toString();
	}

	/**
	 * Closes file stream.
	 * @throws IOException I/O exception.
	 */
	public void closeFile() throws IOException
	{
		if (temp != null)
		{
			temp.close();
		}
		if (cvsFileWriter != null)
		{
			cvsFileWriter.close();
		}
	}

	/**
	 * This method creates the file according to delimeter specified.
	 * @param values values list. It is List of List.
	 * @param delimiter delimiter used for separting individaul fields.
	 * @param noblankLines No of blank lines added before values
	 * @param columnIndent No columns that will be left blank for values
	 * @throws IOException I/O exception.
	 */
	public void writeData(List values, String delimiter, int noblankLines, int columnIndent)
			throws IOException
	{
		addLineSeparator(noblankLines);
		if (values != null)
		{
			Iterator itr = values.iterator();
			while (itr.hasNext())
			{
				List rowValues = (List) itr.next();
				Iterator rowItr = rowValues.iterator();

				for (int i = 0; i < columnIndent; i++)
				{
					temp.write(delimiter);
				}

				while (rowItr.hasNext())
				{
					String tempStr = (String) rowItr.next();
					temp.write(getStrWithoutDoubleQuotes(tempStr) + delimiter);
				}
				temp.write(TextConstants.LINE_SEPARATOR);
			}
		}
	}

	/**
	 * @param noblankLines Number of non blank lines.
	 * @throws IOException generic IO exception
	 */
	private void addLineSeparator(int noblankLines) throws IOException
	{
		//Writes the list of data into file
		for (int i = 0; i < noblankLines; i++)
		{
			temp.write(TextConstants.LINE_SEPARATOR);
		}
	}

	/**
	 * Creates a zip file , contains a csv and other text files.
	 * @param files files to be compressed.
	 */
	public void createZip(List<String> files)
	{
		try
		{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(this.zipFileName));
			File file;
			for (Iterator<String> iterator = files.iterator(); iterator.hasNext();)
			{
				String fileName = (String) iterator.next();
				file = new File(fileName);
				if (fileName.indexOf("csv") == -1)
				{
					putFileToZip(out, fileName);
				}
				else
				{
					putCSVFileToZip(out, fileName);
				}
				boolean isDeleted = file.delete();
				if(!isDeleted)
				{
					LOGGER.info("Not able to delete file "+fileName);
				}
			}
			out.close();
		}
		catch (IOException excp)
		{
			LOGGER.error(excp.getMessage(), excp);
		}
	}

	/**
	 * Zip the file passed as parameter.
	 * @param buf
	 * @param out ZipOutputStream zipped output data.
	 * @param fileName String file name.
	 * @throws FileNotFoundException
	 * @throws IOException I/O exception.
	 */
	private void putFileToZip(ZipOutputStream out, String fileName) throws IOException
	{
		byte[] buf = new byte[Constants.ONE_KILO_BYTES];
		FileInputStream inputReader = new FileInputStream(fileName);
		out.putNextEntry(new ZipEntry(fileName));
		int len=inputReader.read(buf);
		while (len  > 0)
		{
			out.write(buf, 0, len);
			len = inputReader.read(buf);
		}
		out.closeEntry();
		inputReader.close();
	}

	/**
	 * Zip the file passed as parameter.
	 * @param out ZipOutputStream zipped output data.
	 * @param fileName String file name.
	 * @throws FileNotFoundException
	 * @throws IOException I/O exception.
	 */
	private void putCSVFileToZip(ZipOutputStream out, String fileName) throws IOException
	{
		BufferedReader bufRdr = new BufferedReader(new FileReader(fileName));
		StringTokenizer tokenizer;
		out.putNextEntry(new ZipEntry(fileName));
		String line = bufRdr.readLine();
		while (line!= null)
		{
			tokenizer = new StringTokenizer(line, ",");
			while (tokenizer.hasMoreTokens())
			{
				String token = tokenizer.nextToken();
				out.write(token.getBytes(), 0, token.length());
				token = ",";
				out.write(token.getBytes(), 0, token.length());

			}
			out.write(TextConstants.LINE_SEPARATOR.getBytes(), 0,
					TextConstants.LINE_SEPARATOR.length());
			line = bufRdr.readLine();
		}
		out.closeEntry();
		bufRdr.close();
	}

	/**
	 * Executes sql and returns data list.
	 * @param sql String sql query.
	 * @return List containing data list as output.
	 * @throws DAOException
	 * @throws ClassNotFoundException exception for class not found.
	 * @throws DAOException DAO exception.
	 */
	private List<Object> executeQuery(String sql) throws DAOException, ClassNotFoundException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao = daofactory.getJDBCDAO();

		dao.openSession(null);
		return (List<Object>)dao.executeQuery(sql);
	}
}