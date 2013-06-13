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

import java.io.IOException;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

import edu.wustl.common.util.logger.LoggerConfig;


/**
 * This class is for import/export data to database.
 * @author abhishek_mehta
 *
 */
public final class AutomateImport
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	//private static final Logger logger = Logger.getCommonLogger(AutomateImport.class);
	/**
	 * Private constructor for utility class.
	 */
	private AutomateImport()
	{

	}
	/**
	 * Minimum number of arguments required.
	 */
	private static final int MIN_NO_OF_ARGS=10;
	/**
	 * Index for CA model operation like import or export.
	 */
	private static final int  INDX_FOR_OPERATION=7;
	/**
	 *
	 * @param args the arguments to be passed are:
	 * 		 	- database.host
	 * 			- database.port
	 * 			- database.type
	 * 			- database.name
	 * 			- database.username
	 * 			- database.password
	 * 			- database driver
	 * 			- import/export
	 * 			- path for dumpFileColumnInfo.txt which contains
	 * 				the table name list to be imported/exported.
	 * 			- folder path for CAModelCSVs files
	 * 			- folder path for CAModelCTLs files required in case of oracle
	 * 			- oracle.tns.name required in case of oracle
	 * @throws ApplicationException -Generic Application exception.
	 */
	public static void main(String[] args) throws ApplicationException
	{
		AutomateImport automateImport = new AutomateImport();
		DatabaseUtility dbUtility= new DatabaseUtility();
		automateImport.configureDBConnection(args,dbUtility);
		IAutomateImpExp impExpObj=dbUtility.getAutomatImpExpObj();
		impExpObj.init(dbUtility);
		if(args[INDX_FOR_OPERATION].equalsIgnoreCase("import"))
		{
			impExpObj.executeImport(args);
		}
		else
		{
			impExpObj.executeExport(args);
		}
	}
	/**
	 * assigning database parameters.
	 * @param args String[] of configuration info
	 * @param dbUtility DatabaseUtility
	 * @throws ApplicationException  Generic Application Exception
	 */
	private void configureDBConnection(String[] args,DatabaseUtility dbUtility) throws ApplicationException
	{
		if (args.length < MIN_NO_OF_ARGS)
		{
			try
			{
				ErrorKey.init("-");
			}
			catch (IOException exception)
			{
				ErrorKey errorKey=ErrorKey.getErrorKey("");
				throw new ApplicationException(errorKey,exception,"Properties file not found.");
			}
			ErrorKey errorKey=ErrorKey.getErrorKey("");
			throw new ApplicationException(errorKey,null,"Insufficient number of arguments");
		}
		dbUtility.setDbParams(args);
	}
}
