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
