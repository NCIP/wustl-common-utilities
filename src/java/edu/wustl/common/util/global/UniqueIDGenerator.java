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

import java.util.UUID;
/**
 * This class will generate the Unique Identification String.
 * 
 * @author Atul
 *
 */
public class UniqueIDGenerator
{
	/**
	 * This method returns the uniquely generated string value.
	 * @return String
	 */
	public static String getUniqueID()
	{
		return UUID.randomUUID().toString();
	}
}
