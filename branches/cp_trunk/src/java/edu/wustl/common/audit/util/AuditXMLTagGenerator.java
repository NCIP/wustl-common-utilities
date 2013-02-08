
package edu.wustl.common.audit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates the tag used metadata audit xml
 * @author kunal_kamble
 *
 */
public class AuditXMLTagGenerator
{
	/**
	 * @param className
	 * @return
	 */
	public String getAuditableMetatdataXMLString(String className)
	{
		StringBuffer auditableMetatdataXML = new StringBuffer();
		auditableMetatdataXML.append(getAuditableClassTag(className));
		auditableMetatdataXML.append('\n');
		try
		{
			Class newObjectClass = Class.forName(className);
			Map<String, Field> fieldList = new HashMap<String, Field>();
			populateAllFields(newObjectClass, fieldList);
			for (Field field : fieldList.values())
			{
				if (!"serialVersionUID".equalsIgnoreCase(field.getName())
						&& validateAttribute(field, newObjectClass))
				{
					auditableMetatdataXML.append('\t');
					updateAttributeTag(auditableMetatdataXML, field);
				}

			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			auditableMetatdataXML.append("</AuditableClass>");
		}
		return auditableMetatdataXML.toString();
	}

	/**
	 * check whether getter present for the given attribute in the class or not
	 * @param field
	 * @param newObjectClass
	 * @return
	 */
	protected boolean validateAttribute(Field field, Class newObjectClass)
	{
		boolean validAttribute = false;
		String fieldName = field.getName();
		List<Method> methlist = new ArrayList<Method>();
		populateAllMethods(newObjectClass, methlist);
		for (Method method : methlist)
		{
			if (method.getName().equalsIgnoreCase("get" + fieldName))
			{
				validAttribute = true;
			}
		}
		return validAttribute;
	}

	/**
	 * @param klass
	 * @param fieldList field list declared in the class and in its parent classes
	 */
	private void populateAllFields(Class klass, Map<String, Field> fieldList)
	{
		if (klass.getSuperclass() != null)
		{
			populateAllFields(klass.getSuperclass(), fieldList);
		}
		klass.getDeclaredFields();
		for (Field field : klass.getDeclaredFields())
		{
			fieldList.put(field.getName(), field);
		}
	}

	/**
	 * @param klass
	 * @param fieldList field list declared in the class and in its parent classes
	 */
	private void populateAllMethods(Class klass, List<Method> fieldList)
	{

		if (klass.getSuperclass() != null)
		{
			populateAllMethods(klass.getSuperclass(), fieldList);
		}
		klass.getDeclaredFields();
		for (Method method : klass.getDeclaredMethods())
		{
			fieldList.add(method);
		}
	}

	/**
	 * @param className
	 * @return Auditable class tag
	 */
	private String getAuditableClassTag(String className)
	{
		String relationshipType = "main";
		String roleName = "";
		String auditableClass = AuditXMLConstants.AUDIT_CLASS_TAG;
		auditableClass = auditableClass.replace(AuditXMLConstants.CLASS_NAME_TOKEN, className);
		auditableClass = auditableClass.replace(AuditXMLConstants.ROLE_NAME_TOKEN, roleName);
		auditableClass = auditableClass.replace(AuditXMLConstants.RELATIONSHIP_TYPE_TOKEN,
				relationshipType);

		return auditableClass;

	}

	/**
	 * @param field
	 * @return attribute tag
	 */
	protected void updateAttributeTag(StringBuffer auditableMetatdataXML, Field field)
	{
		if (!AuditXMLGenerator.excludeAssociation
				&& field.getType().getName().startsWith("java.util.")
				&& !field.getType().getName().startsWith("java.util.Date"))
		{
			generateAssociationTag(auditableMetatdataXML, field);
		}
		else if (!field.getType().getName().startsWith("java.util.Collection"))
		{
			generateAttributeTag(auditableMetatdataXML, field);
		}
	}

	/**
	 * @param auditableMetatdataXML
	 * @param field
	 */
	protected void generateAssociationTag(StringBuffer auditableMetatdataXML, Field field)
	{
		auditableMetatdataXML.append(this.getContainmentAssociationCollection(field));
		auditableMetatdataXML.append('\n');
	}

	/**
	 * @param auditableMetatdataXML
	 * @param field
	 */
	protected void generateAttributeTag(StringBuffer auditableMetatdataXML, Field field)
	{
		String fieldName = field.getName();
		String fieldType = field.getType().getName();
		String attributeTag = AuditXMLConstants.ATTRIBUTE_TAG;
		attributeTag = attributeTag.replace(AuditXMLConstants.NAME_TOKEN, fieldName);
		attributeTag = attributeTag.replace(AuditXMLConstants.DATA_TYPE_TOKEN, fieldType);
		auditableMetatdataXML.append(attributeTag);
		auditableMetatdataXML.append('\n');
	}

	/**
	 * @param field
	 * @return Containment association collection tag
	 */
	protected String getContainmentAssociationCollection(Field field)
	{
		String containmentAssociationTag = AuditXMLConstants.CONTAINMENT_ASSOCIATION_COLLECTION_TAG;
		String relationshipType = "containment";
		String roleName = field.getName();
		String className = field.getType().getName();

		containmentAssociationTag = containmentAssociationTag.replace(
				AuditXMLConstants.CLASS_NAME_TOKEN, className);
		containmentAssociationTag = containmentAssociationTag.replace(
				AuditXMLConstants.RELATIONSHIP_TYPE_TOKEN, relationshipType);
		containmentAssociationTag = containmentAssociationTag.replace(
				AuditXMLConstants.ROLE_NAME_TOKEN, roleName);
		return containmentAssociationTag;
	}
}
