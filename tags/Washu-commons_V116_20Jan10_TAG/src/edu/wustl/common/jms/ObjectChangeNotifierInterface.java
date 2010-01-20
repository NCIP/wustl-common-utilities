
package edu.wustl.common.jms;

import java.io.Serializable;

/**
 *
 * @author kalpana_thakur
 * TODO This interface is used to read the Object
 *
 */
public interface ObjectChangeNotifierInterface extends Serializable
{

	/**
	 * This method is used to read the object.
	 *  @param obj Object to read.
	 * */
	void read(Object obj);
}
