/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import org.aopalliance.intercept.MethodInvocation;

public interface ProxyHelper {
	public Object convertToProxy(ApplicationService as, Object obj);

	public Object convertToObject(Object proxyObject) throws Throwable;

	public boolean isInitialized(MethodInvocation invocation) throws Throwable;

	public Object lazyLoad(ApplicationService as, MethodInvocation invocation)throws Throwable;

}