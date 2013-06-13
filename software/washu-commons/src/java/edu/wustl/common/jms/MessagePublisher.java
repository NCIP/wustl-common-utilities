/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.jms;

/**
 *
 * @author kalpana_thakur
 * TODO This interface is used to initiate the connection and to
 * publish the message or data
 */
public interface MessagePublisher
{

	/**
	 * This method is used to initialize the connection.
	 * */
	void initialize();

	/**
	 * @param obj
	 * This method is used to publish message or data
	 */
	void update(Object obj);
}
