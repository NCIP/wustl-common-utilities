package edu.wustl.common.audit.util;

import java.util.Collection;
import java.util.Date;


/**
 *
 * @author.
 *
 */
public final class AuditUtil
{
	/**
	 * Constructor.
	 */
	private AuditUtil()
	{
		//Empty constructor.
	}
	/**
	 * This function returns the function name of passed argument.
	 * @param name name of the attribute whose function will returned.
	 * @return function name of the attribute.
	 */
	public static String getGetterFunctionName(String name)
	{
		String functionName = null;
		if (name != null && name.length() > 0)
		{
			String firstAlphabet = name.substring(0, 1);
			String upperCaseFirstAlphabet = firstAlphabet.toUpperCase();
			String remainingString = name.substring(1);
			functionName = "get" + upperCaseFirstAlphabet + remainingString;
		}
		return functionName;
	}

	/**
	 * This method is called to obtain name of the object within the collection.
	 * @param currentObjectColl currentObjectColl
	 * @param prevObjectColl prevObjectColl
	 * @return Object Name.
	 */
	public static String getAssociationCollectionObjectName(Collection
			currentObjectColl,Collection prevObjectColl)
	{
		String objectName = "";
		if(currentObjectColl != null && !((Collection)currentObjectColl).isEmpty())
		{
			objectName = (((Collection)currentObjectColl).iterator().next())
			.getClass().getName();
		}
		else if(prevObjectColl != null && !((Collection)prevObjectColl).isEmpty())
		{
			objectName = (((Collection)prevObjectColl).iterator().next())
			.getClass().getName();
		}
		return objectName;
	}

	/**
	 * Check whether the object type is a primitive data type or a user defined dataType.
	 * @param obj object.
	 * @return return true if object type is a primitive data type else false.
	 */
	public static boolean isVariable(Object obj)
	{
		boolean objectType = obj instanceof Number || obj instanceof String
				|| obj instanceof Boolean || obj instanceof Character || obj instanceof Date;
		return objectType;
	}

}
