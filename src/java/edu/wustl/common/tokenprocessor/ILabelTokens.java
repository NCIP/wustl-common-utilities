
package edu.wustl.common.tokenprocessor;

import edu.wustl.common.exception.ApplicationException;


// TODO: Auto-generated Javadoc
/**
 * The Interface LabelTokens.
 */
/**
 * @author nitesh_marwaha
 *
 */
public interface ILabelTokens
{

	/**
	 * Gets the token value.
	 *
	 * @param object the object
	 * @param token the token
	 *
	 * @return the token value
	 */
	String getTokenValue(Object object)throws ApplicationException;
}
