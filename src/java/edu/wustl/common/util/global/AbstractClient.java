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
 * Created on Dec 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.util.global;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ajay_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AbstractClient
{

	/**
	 * Map that stores the relation condition between two objects.
	 */
	public static Map relationConditionsForRelatedTables = new HashMap();

	/**
	 * This stores the relation between objects where key is source object of the relation
	 * and value is the target object of the relation.
	 * For example if Participant is the source and Accession is the target of a relation
	 * then their is a value "Accession" associated with a key "Participant" in the map.
	 */
	public static Map relations = new HashMap();

	/**
	 * This maps the objects with actual table names.
	 */
	public static Map objectTableNames = new HashMap();

	/**
	 * This maps the table alias with the type of privilege on that table.
	 */
	public static Map privilegeTypeMap = new HashMap();

	/**
	 * This maps the table alias with the vector of Identified data fields it has.
	 */
	public static Map identifiedDataMap = new HashMap();
}
