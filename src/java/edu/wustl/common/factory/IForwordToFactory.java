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

import edu.wustl.common.util.AbstractForwardToProcessor;


/**
 * @author prashant_bandal
 *
 */
public interface IForwordToFactory
{

	/***
	 * get Forward To Processor.
	 * @return ForwardToProcessor
	 */
	AbstractForwardToProcessor getForwardToProcessor();

	 /**
	  * get Forward To Print Processor.
	  * @return ForwardToPrintProcessor
	  */
	AbstractForwardToProcessor getForwardToPrintProcessor();

}
