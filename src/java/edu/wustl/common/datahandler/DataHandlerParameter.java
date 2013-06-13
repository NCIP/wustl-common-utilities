/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.datahandler;

import java.util.HashMap;


/**
 *
 * @author ravindra_jain
 * @version 1.0
 * @since April 22, 20009
 *
 */
public class DataHandlerParameter
{
	/**
	 * HashMap object.
	 */
	private HashMap<ParametersEnum, Object> parametersMap = new HashMap<ParametersEnum, Object>();
	/**
	 *  CONSTRUCTOR.
	 */
	public DataHandlerParameter()
	{
		parametersMap = new HashMap<ParametersEnum, Object>();
	}
	/**
	 *
	 * @param key ParametersEnum
	 * @param value Object
	 */
	public void setProperty(ParametersEnum key, Object value)
	{
		parametersMap.put(key, value);
	}
	/**
	 *
	 * @return HashMap object.
	 */
	public HashMap<ParametersEnum, Object> getParametersMap()
	{
		return parametersMap;
	}
}
