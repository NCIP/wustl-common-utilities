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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @author ravindra_jain
 * @version 1.0
 * @created 21-Apr-2009 6:57:50 PM
 */
public class DataHandlerFactory // NOPMD
{
	/*private HandlerTypeEnum fileType;
	private String fileName;
	private int bufferSize;*/
	/**
	 *
	 * @param handlerType HandlerTypeEnum object.
	 * @param parameters DataHandlerParameter object
	 * @param fileName Name of File.
	 * @return AbstractDataHandler object.
	 * @throws ClassNotFoundException throw ClassNotFoundException
	 * @throws InvocationTargetException throw InvocationTargetException
	 * @throws IllegalAccessException throw IllegalAccessException
	 * @throws InstantiationException throw InstantiationException
	 * @throws IllegalArgumentException throw IllegalArgumentException
	 * @throws NoSuchMethodException throw NoSuchMethodException
	 * @throws SecurityException throw SecurityException
	 */
	public static AbstractDataHandler getDataHandler(HandlerTypeEnum handlerType,
			DataHandlerParameter parameters, String fileName) throws ClassNotFoundException,
			IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException
	{
		AbstractDataHandler dataHandler = null;
		Integer rowCount = null;
		String delimiter = null;
		Map<ParametersEnum, Object> parametersMap = parameters.getParametersMap();

		if (parametersMap.get(ParametersEnum.BUFFERSIZE) != null)
		{
			rowCount = Integer.valueOf(parametersMap.get(ParametersEnum.BUFFERSIZE).toString());
		}
		if (parametersMap.get(ParametersEnum.DELIMITER) != null)
		{
			delimiter = parametersMap.get(ParametersEnum.DELIMITER).toString();
		}
		switch (handlerType)
		{
			case CSV :
				dataHandler = new CSVDataHandler(fileName, rowCount, delimiter);
				break;

			case TEXT :
				dataHandler = new TextDataHandler(fileName, rowCount);
				break;
			default :
		}
		return dataHandler;
	}
}
