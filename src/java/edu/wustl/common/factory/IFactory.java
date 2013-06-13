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
 *
 */
package edu.wustl.common.factory;

import edu.wustl.common.bizlogic.IBizLogic;


/**
 * @author prashant_bandal
 *
 */
public interface IFactory
{
	/**
	 * get the BizLogic.
	 * @param formId form Id
	 * @return IBizLogic.
	 */
	IBizLogic getBizLogic(int formId);

	/**
     * Returns DAO instance according to the fully qualified class name.
     * @param className The name of the class.
     * @return An DAO object.
     */
	IBizLogic getBizLogic(String className);
}
