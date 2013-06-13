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

/**
 * This file is to store all Text constants.
 * @author ravi_kumar
 *
 */
public final class TextConstants
{
	/**
	 * private constructor.
	 */
	private TextConstants()
	{

	}
	/**
	 * constants for line seperator.
	 */
	public static final String LINE_SEPARATOR = System.getProperty(("line.separator"));
	/**
	 * constants for two consecutive new line character.
	 */
	public static final String TWO_LINE_BREAK = "\n\n";
	/**
	 * Constant for new line character.
	 */
	public static final String LINE_BREAK = "\n";
	/**
	 * Constant for tab character.
	 */
	public static final String TAB = "\t";
	/**
	 * Constant for Empty String.
	 */
	public static final String EMPTY_STRING = "";
	/**
	 * Constant for zero.
	 */
	public static final String STR_ZERO = "0";
	/**
	 * XML file name to initialize title table mappting.
	 */
	public static final String TITLI_TABLE_MAPPING_FILE="titli-table-mapping.xml";
	/**
	 * XML file name to initialize Privileges Map.
	 */
	public static final String PERMSN_MAP_DET_FILE="PermissionMapDetails.xml";

	/**
	 * ERROR KEY FOR TABLE.
	 */
	public static final String ERROR_KEY_FOR_TABLE = "simpleQuery.object.required";

	/**
	 * ERROR KEY FOR FIELD.
	 */
	public static final String ERROR_KEY_FOR_FIELD = "simpleQuery.attribute.required";

	/**
	 * ERROR_MESSAGE.
	 */
	public static final String ERROR_MESSAGE = "Please contact the caTissue" +
			" Core support at catissue_support@mga.wustl.edu";
}
