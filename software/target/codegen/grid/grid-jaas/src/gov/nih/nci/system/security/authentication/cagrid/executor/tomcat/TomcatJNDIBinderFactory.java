/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.security.authentication.cagrid.executor.tomcat;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

public class TomcatJNDIBinderFactory implements ObjectFactory 
{
	private static Map map = new Hashtable();
	
 
	public TomcatJNDIBinderFactory()
	{
		
	}
	
	public Object getObjectInstance(Object obj,
      Name name, Context nameCtx, Hashtable environment)
      throws NamingException {

      return map;

  }

}
