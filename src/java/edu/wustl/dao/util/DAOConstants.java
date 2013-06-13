/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * <p>Title: DAOConstants Class>
 * <p>Description:	DAOConstants class holds the DAO specific constants .</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.util;

import edu.wustl.common.util.global.CommonConstants;

/**
 * @author kalpana_thakur
 * This class holds all DAO specific constants.
 */
public final class DAOConstants extends CommonConstants
{

	/**
	 * creates a single object.
	 */
	private static DAOConstants daoConstant = new DAOConstants();;
	/**
	 * Private constructor.
	 */
	private DAOConstants()
	{

	}
	/**
	 * returns the single object.
	 * @return Utility object
	 */
	public static DAOConstants getInstance()
	{
		return daoConstant;
	}

	/**
	 * Dot operators.
	 */
	public static final String DOT_OPERATOR = ".";

	/**
	 * Space .
	 */
	public static final String TRAILING_SPACES = " ";

	/**
	 * in operator.
	 */
	public static final String IN_CONDITION = "in";

	/**
	 * not in operator.
	 */
	public static final String NOT_IN_CONDITION = "not in";

	/**
	 * is not null operator.
	 */
	public static final String NOT_NULL_CONDITION = "is not null";


	/**
	 * is null operator.
	 */
	public static final String NULL_CONDITION = "is null";

	/**
	 * Equal(=) operator.
	 */
	public static final String EQUAL = "=";

	/**
	 * like operator.
	 */
	public static final String LIKE = "like";


	/**
	 * Greater (>) operator.
	 */
	public static final String GREATERTHEN = ">";

	/**
	 * Less then (<) operator.
	 */
	public static final String LESSTHEN = "<";


	/**
	 * Not Equal(!=) operator.
	 */
	public static final String NOT_EQUAL = "!=";

	/**
	 * index value operator"?".
	 */
	public static final String INDEX_VALUE_OPERATOR = "?";

	/**
	 * Security constant.
	 */
	public static final boolean SWITCH_SECURITY = true;

	/**
	 * System identifier.
	 */
	public static final String SYSTEM_IDENTIFIER = "id";

	/**
	 * Time pattern.
	 * TODO : need to confirm.
	 */
	public static final String TIME_PATTERN_HH_MM_SS = "HH:mm:ss";
	/**
	 * Date pattern.
	 */
	public static final String DATE_PATTERN_MM_DD_YYYY = "MM-dd-yyyy";

	/**
	 * Date pattern.
	 */
	public static final String TIMESTAMP_PATTERN = "MM-dd-yyyy HH:mm:ss";

	public static final String EMPTY_STRING = "";

	/**
	 * Opening bracket.
	 *//*
	public static final String OPENING_BRACKET_OPERATOR = "(";


	*//**
	 * Closing bracket.
	 *//*
	public static final String CLOSING_BRACKET_OPERATOR = ")";
*/

	  /**
	    * SQL injection : Reject input with following characters.
	    * EXECUTE, EXEC, or sp_executesql, DELETE, DROP
	    * ;  :Query delimiter.
	    * '  :Character data string delimiter.
		* -- : Comment delimiter.
		* xp_ : Used at the start of the name of catalog-extended stored procedures, such as xp_cmdshell.
	    * /*..*/ /*:Comment delimiters. Text between /* and *//*is not evaluated by the server.
	    */
	 public static final String[] INVALID_DATA = {"execute", "exec","sp_executesql",
		 "delete","drop",";","xp_","/*","*/"};

	 public static final String[] EXTRA_CHECK_DATA = {"execute", "exec","sp_executesql",
		 "delete","drop"};

}
