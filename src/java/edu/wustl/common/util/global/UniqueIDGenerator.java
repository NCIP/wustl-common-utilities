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
