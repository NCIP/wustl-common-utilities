/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.dao.orm.translator.gridCQL;

import gov.nih.nci.system.util.ClassCache;

import java.util.List;

public class DomainTypesInformationUtil extends gov.nih.nci.cagrid.sdkquery4.processor.DomainTypesInformationUtil
{

	private ClassCache cache;
	
	public DomainTypesInformationUtil(ClassCache cache)
	{
		super(null);
		this.cache = cache;
	}

	@Override
	public String getAttributeJavaType(String classname, String classCache)
	{
		try
		{
			return cache.getReturnType(classname, classCache);
		} 
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public List<String> getSubclasses(String klassName)
	{
		return cache.getSubClassNames(klassName);
	}
}