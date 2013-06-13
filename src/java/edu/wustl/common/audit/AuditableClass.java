/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.audit;

import java.util.ArrayList;
import java.util.Collection;

import edu.wustl.common.audit.util.AuditUtil;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.AuditException;

/**
 * This class is the POJO for the details of the objects read from the XML.
 * When auditing the domain object, an instance of this class will provide information
 * as to which attributes of the class would be audited and how will they be treated
 * (as references or containments).
 * @author niharika_sharma
 */
public class AuditableClass
{
	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(AuditManager.class);

	/**
	 * Class name of the domain object being referred to.
	 */
	private String className;
	/**
	 * Specifies is this the main class or a contained class.
	 */
	private String relationShipType;
	/**
	 * In case this class is a contained class, then this attribute refers to the
	 * property name of this class instance in the containing class.
	 */
	private String roleName;

	/**
	 * This will be set to false when user do not want the audit
	 * the objects of specific class, by default all the objects are audited.
	 */
	private boolean isAuditable = true;

	/**
	 * This will be set to true if objects of specific class needs to be audited.
	 * @return true if class will be audited else false.
	 */
	public boolean getIsAuditable()
	{
		return isAuditable;
	}
	/**
	 * This will be set to true if objects of specific class needs to be audited.
	 * @param isAuditable true if class will be audited else false.
	 */
	public void setIsAuditable(boolean isAuditable)
	{
		this.isAuditable = isAuditable;
	}

	/**
	 * Class object containing the instance of the domain object.
	 */
	private Class auditClass;
	/**
	 * Collection of attributes of the objects that are reference associations.
	 */
	private Collection<AuditableClass> referenceAssociationCollection = new ArrayList<AuditableClass>();
	/**
	 * Collection of attributes of the objects that are containment associations.
	 */
	private Collection<AuditableClass> containmentAssociationCollection = new ArrayList<AuditableClass>();
	/**
	 * Collection of simple attributes of the objects.
	 */
	private Collection<Attribute> attributeCollection = new ArrayList<Attribute>();
	/**
	 * Returns the collection of simple attributes of the object.
	 * @return collection of attribute.
	 */
	public Collection<Attribute> getAttributeCollection()
	{
		return attributeCollection;
	}
	/**
	 * Sets the collection of simple attributes of the object.
	 * @param attributeCollection collection of attribute.
	 */
	public void setAttributeCollection(Collection<Attribute> attributeCollection)
	{
		this.attributeCollection = attributeCollection;
	}
	/**
	 * Returns the collection of reference associations present in the object.
	 * @return Collection of AuditableClass
	 */
	public Collection<AuditableClass> getReferenceAssociationCollection()
	{
		return referenceAssociationCollection;
	}
	/**
	 * Sets the collection of reference associations present in the object.
	 * @param referenceAssociationCollection collection of Reference objects.
	 */
	public void setReferenceAssociationCollection(
			Collection<AuditableClass> referenceAssociationCollection)
	{
		this.referenceAssociationCollection = referenceAssociationCollection;
	}
	/**
	 * Returns the collection of containment associations present in the object.
	 * @return Collection of AuditableClass objects.
	 */
	public Collection<AuditableClass> getContainmentAssociationCollection()
	{
		return containmentAssociationCollection;
	}
	/**
	 * Sets the collection of containment associations present in the object.
	 * @param containmentAssociationCollection collection of containmentAssociation objects.
	 */
	public void setContainmentAssociationCollection(
			Collection<AuditableClass> containmentAssociationCollection)
	{
		this.containmentAssociationCollection = containmentAssociationCollection;
	}
	/**
	 * Returns the class name of the object instance.
	 * @return className
	 */
	public String getClassName()
	{
		return className;
	}
	/**
	 * Sets the class name of the object instance.
	 * @param className class Name of the AuditableObject.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}
	/**
	 * Returns the relation ShipType of the class.
	 * @return relationShipType
	 */
	public String getRelationShipType()
	{
		return relationShipType;
	}
	/**
	 * Sets the relation ShipType of the class.
	 * @param relationShipType RelationShip Type to set.
	 */
	public void setRelationShipType(String relationShipType)
	{
		this.relationShipType = relationShipType;
	}
	/**
	 * Returns the Role name.
	 * @return roleName.
	 */
	public String getRoleName()
	{
		return roleName;
	}
	/**
	 * Sets the Role Name.
	 * @param roleName roleName to set.
	 */
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	/**
	 * This method loads the class mentioned in the XML.
	 * @return Class.
	 * @throws AuditException throw AuditException
	 */
	public Class getClassObject() throws AuditException
	{
		try
		{
			if(auditClass==null)
			{
				auditClass = Class.forName(className);
			}
		}
		catch (ClassNotFoundException e)
		{
			LOGGER.error(e.getMessage(),e);
			throw new AuditException(ErrorKey.getErrorKey("class.not.found.exp"),e,className);
		}
		return  auditClass;
	}
	/**
	 * This method creates an instance of the class mentioned by className in the XML.
	 * @return Object.
	 * @throws AuditException throw AuditException.
	 */
	public Object getNewInstance() throws AuditException
	{
		Object returnObject = null;
		try
		{
			returnObject = Class.forName(className).newInstance();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage(),e);
			throw new AuditException(ErrorKey.getErrorKey("problem.getting.new.instance"),e,className);
		}
		return  returnObject;
	}
	/**
	 * Given the role name, this method invokes the getter method on the object being passed as the parameter.
	 * @param roleName Role name.
	 * @param objectOnWhichMethodToInvoke Object.
	 * @return Object value.
	 * @throws AuditException throw AuditException.
	 */
	public Object invokeGetterMethod(String roleName,Object objectOnWhichMethodToInvoke)
	throws AuditException
	{
		Object returnObject = null;
		try
		{
			if(objectOnWhichMethodToInvoke!=null)
			{
				String functionName = AuditUtil.getGetterFunctionName(roleName);
				returnObject = Class.forName(className).getMethod(functionName, null)
								.invoke(objectOnWhichMethodToInvoke, null);
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage(),e);
			throw new AuditException(ErrorKey.getErrorKey("problem.getting.methods"),e,className);
		}
		return returnObject;
	}

	/**
	 * Given the role name, this method invokes the getter method on the object being passed as the parameter.
	 * @param objectOnWhichMethodToInvoke Object.
	 * @return Object value.
	 * @throws AuditException throw AuditException.
	 */
	public Object invokeGetterForId(Object objectOnWhichMethodToInvoke)
	throws AuditException
	{
		Object returnObject = null;
		try
		{
			if(objectOnWhichMethodToInvoke!=null)
			{
				String functionName = AuditUtil.getGetterFunctionName("id");
				returnObject = Class.forName(className).getMethod(functionName, null)
								.invoke(objectOnWhichMethodToInvoke, null);
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage(),e);
			throw new AuditException(ErrorKey.getErrorKey("problem.getting.methods"),e,className);
		}
		return returnObject;
	}

}
