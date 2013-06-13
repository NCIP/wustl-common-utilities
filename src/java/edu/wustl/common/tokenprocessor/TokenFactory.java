/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.tokenprocessor;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Token objects.
 */
/**
 * @author nitesh_marwaha
 *
 */
public final class TokenFactory
{

	/** Logger object. */
	private static final Logger LOGGER = Logger.getCommonLogger(TokenFactory.class);

	/**
	 * Private constructor.
	 */
	private TokenFactory()
	{

	}

	/** Singleton instance of SpecimenLabelGenerator. */
	private static Map<String, Object> tokenMap = new HashMap<String, Object>();

	/**
	 * Get singleton instance of SpecimenLabelGenerator.
	 * The class name of an instance is picked up from properties file
	 *
	 * @param tokenKey Property key name for specific Object's
	 * Label generator class (eg.specimenLabelGeneratorClass)
	 *
	 * @return LabelGenerator
	 *
	 * @throws NameGeneratorException NameGeneratorException
	 */
	public static synchronized ILabelTokens getInstance(String tokenKey)
			throws ApplicationException
	{
		try
		{
			ILabelTokens labelToken;
			if (tokenMap.get(tokenKey) == null)
			{
				String className = PropertyHandler.getValue(tokenKey);
				if(Validator.isEmpty(className))
				{
					throw new ApplicationException(null, null, "Invalid Token", "Invalid TokenKey : "+tokenKey);
				}
				tokenMap.put(tokenKey, Class.forName(className).newInstance());
				labelToken = (ILabelTokens) tokenMap.get(tokenKey);

			}
			else
			{
				labelToken = (ILabelTokens) tokenMap.get(tokenKey);
			}
			return labelToken;
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			throw new ApplicationException(null, e, "Could not create LabelGenerator instance: "
					+ e.getMessage());
		}

	}
}
