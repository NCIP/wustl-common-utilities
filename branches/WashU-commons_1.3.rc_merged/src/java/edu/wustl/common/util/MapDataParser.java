
package edu.wustl.common.util;


import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import edu.wustl.common.datatypes.DataTypeConfigFactory;
import edu.wustl.common.datatypes.IDBDataType;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author Kapil Kaveeshwar
 *
 */
public class MapDataParser // NOPMD
{
	/**
	 * LOGGER Logger - generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(MapDataParser.class);
	/**
	 * packageName String name of package.
	 */
	private String packageName;
	/**
	 * bigMap Map hash map to hold object.
	 */
	private Map bigMap;
	/**
	 * dataList Collection list of data.
	 */
	private Collection dataList;
	/**
	 * Create a map and linked hash set.
	 * @param packageName String name of package.
	 */
	public MapDataParser(String packageName)
	{
		this.packageName = packageName;
		bigMap = new HashMap();
		dataList = new LinkedHashSet();
	}
	/**
	 * Create map of specimen data.
	 * @return map containing specimen data.
	 */
	private Map<String, String> createMap()
	{
		Map<String, String> map = new TreeMap<String, String>();
		map.put("DistributedItem:1_Specimen_id", "1");
		map.put("DistributedItem:1_quantity", "100");
		map.put("DistributedItem:1_Specimen_className", "Tissue");
		map.put("DistributedItem:2_Specimen_id", "2");
		map.put("DistributedItem:2_quantity", "200");
		map.put("DistributedItem:2_Specimen_className", "Molecular");
		return map;
	}
	/**
	 * Determine the type of object from the class name and type passed as parameters.
	 * @param str String specify object.
	 * @param type Class type of class.
	 * @return object that refers to the value passed as parameters.
	 * @throws ParseException parsing exception.
	 * @throws ApplicationException ApplicationException
	 * @throws IOException I/O exception.
	 */
	private Object toObject(String str, Class type) throws ApplicationException, ParseException,
			IOException
	{
		Object object;
		if(TextConstants.EMPTY_STRING.equals(str) && !type.equals(String.class))
		{
			object=null;
		}
		else
		{
			String dataType = type.getSimpleName().toLowerCase();
			IDBDataType dbDataType = DataTypeConfigFactory.getInstance().getDataType(dataType);
			object=dbDataType.getObjectValue(str);
		}
		return object;
	}
	/**
	 * Create array containing methods details of the class passed as parameters.
	 * @param objClass Class class for object.
	 * @param methodName String name of method.
	 * @return array containing method details.
	 * @throws Exception generic exception.
	 */
	private Method findMethod(Class objClass, String methodName) throws Exception
	{
		Method methdName = null;
		Method[] method = objClass.getMethods();
		for (int i = 0; i < method.length; i++)
		{
			if (method[i].getName().equals(methodName))
			{
				methdName = method[i];
				break;
			}
		}
		return methdName;
	}
	/**
	 * Get the methods of the class by parsing the string.
	 * @param parentObj Object parent object.
	 * @param str String string to be parsed.
	 * @param value String value of the element.
	 * @param parentKey String key of parent object.
	 * @throws Exception generic exception.
	 */
	private void parstData(Object parentObj, String str, String value, String parentKey)
			throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(str, "_");

		int tokenCount = tokenizer.countTokens();
		if (tokenCount > 1)
		{
			String className = tokenizer.nextToken();
			String mapKey = new StringBuffer(parentKey).append('-').append(
					str.substring(0, str.indexOf('_'))).toString();
			Object obj = parseClassAndGetInstance(parentObj, className, mapKey);

			if (tokenCount == 2)
			{
				String attrName = tokenizer.nextToken();
				String methodName = Utility.createAccessorMethodName(attrName, true);
				Class objClass = obj.getClass();
				Method method = findMethod(objClass, methodName);
				Object[] objArr = {toObject(value, method.getParameterTypes()[0])};
				method.invoke(obj, objArr);
			}
			else
			{
				int firstIndex = str.indexOf('_');
				className = str.substring(firstIndex + 1);
				parstData(obj, className, value, mapKey);
			}
		}
	}
	/**
	 * This method gets the instance of the class.
	 * @param parentObj Object parent object.
	 * @param str String string to be parsed.
	 * @param mapKey String key for the map.
	 * @return Object object of class.
	 * @throws Exception generic exception.
	 */
	private Object parseClassAndGetInstance(Object parentObj, String str, String mapKey) // NOPMD
			throws Exception
	{
		StringTokenizer innerST = new StringTokenizer(str, ":");
		String className = "";
		Object obj;
		int count = innerST.countTokens();
		if (count == 2) //Case obj is a collection
		{
			className = innerST.nextToken();
			String index = innerST.nextToken();
			Collection collection = null;
			if (parentObj == null)
			{
				collection = dataList;
				StringTokenizer tokenizer = new StringTokenizer(className, "#");
				if (tokenizer.countTokens() > 1)
				{
					tokenizer.nextToken();
					className = tokenizer.nextToken();
				}
			}
			else
			{
				String collectionName = className;
				StringTokenizer tokenizer = new StringTokenizer(className, "#");
				if (tokenizer.countTokens() > 1)
				{
					collectionName = tokenizer.nextToken();
					className = tokenizer.nextToken();
				}
				collection = getCollectionObj(parentObj, collectionName);
			}

			obj = getObjFromList(collection, index, className, mapKey);
		}
		else
		{
			className = str;
			StringTokenizer tokenizer = new StringTokenizer(className, "#");
			if (tokenizer.countTokens() > 1)
			{
				className = tokenizer.nextToken();
			}
			Object retObj = Utility.getValueFor(parentObj, className);
			if (retObj == null)
			{
				retObj = Utility.setValueFor(parentObj, className, null);
			}
			obj = retObj;
		}
		return obj;
	}
	/**
	 * Get the qualified class name and create instance of it.
	 * @param coll Collection collection of objects.
	 * @param index String index value.
	 * @param className String class name.
	 * @param mapKey String map key.
	 * @return Object the object.
	 * @throws Exception generic exception.
	 */
	private Object getObjFromList(Collection coll, String index, String className, String mapKey)
			throws Exception
	{
		Object obj = bigMap.get(mapKey);
		if (obj == null)
		{
			String qualifiedClassName = packageName + "." + className;
			Class aClass = Class.forName(qualifiedClassName);
			obj = aClass.newInstance();
			coll.add(obj);
			bigMap.put(mapKey, obj);
		}
		return obj;
	}
	/**
	 * @param parentObj Object parent object.
	 * @param str String collection string.
	 * @return the collection of attributes.
	 * @throws Exception generic exception.
	 */
	private Collection getCollectionObj(Object parentObj, String str) throws Exception
	{
		String attrName = str + "Collection";
		return (Collection) Utility.getValueFor(parentObj, attrName);
	}
	/**
	 * @param map Map map of generated data.
	 * @param isOrdered boolean check for ordered.
	 * @return collection containing data.
	 * @throws Exception generic exception.
	 */
	public Collection generateData(Map map, boolean isOrdered) throws Exception
	{
		if (isOrdered)
		{
			dataList = new ArrayList();
		}
		return generateData(map);
	}
	/**
	 * @param map Map generated map.
	 * @return collection containing data.
	 * @throws Exception generic exception.
	 */
	public Collection generateData(Map map) throws Exception
	{
		Set entries = map.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry)it.next();
	         String key = (String)entry.getKey();
	         String value=null;
		        if(entry.getValue() instanceof String[])
		        {
		          value = ((String[])entry.getValue())[0];
		        }
		        else
		        {
		        	value = (String )entry.getValue();
		        }
			parstData(null, key, value, "KEY");
		}
		return dataList;
	}
	/**
	 * This method get the row no.
	 * @param key String key to get row no.
	 * @return row no.
	 */
	public int parseKeyAndGetRowNo(String key)
	{
		int start = key.indexOf(':');
		int end = key.indexOf('_');
		return Integer.parseInt(key.substring(start + 1, end));
	}
	/**
	 * display information about the collection of data.
	 * @param args String[] arguments.
	 * @throws Exception generic exception.
	 */
	public static void main(String[] args) throws Exception
	{
		MapDataParser aMapDataParser = new MapDataParser("edu.wustl.catissuecore.domain");
		Map map = aMapDataParser.createMap();
		//map = aMapDataParser.fixMap(map);
		Collection dataCollection = aMapDataParser.generateData(map);
		LOGGER.info(dataCollection);
	}
	/**
	 * @param list List list of row
	 * @param map Map mapped the form details.
	 * @param status String status of the execution of statement.
	 */
	public static void deleteRow(List list, Map map, String status)
	{
		deleteRow(list, map, status, null);
	}
	/**
	 * This method delete the data on clicking of delete button.
	 * @param list List list of row.
	 * @param map Map mapped form details.
	 * @param status String status of execution.
	 * @param outer String
	 */
	public static void deleteRow(List<String> list, Map map, String status, String outer) // NOPMD
	{

		//whether delete button is clicked or not
		boolean isDeleteClicked = true;
		if (status == null)
		{
			isDeleteClicked = Boolean.FALSE.booleanValue();
		}
		else
		{
			isDeleteClicked = Boolean.getBoolean(status);
		}

		String text;
		for (int k = 0; k < list.size(); k++)
		{
			text = (String) list.get(k);
			String first = text.substring(0, text.indexOf(':'));
			String second = text.substring(text.indexOf('_'));

			//condition for creating ids for innerTable
			boolean condition = false;
			String third = "", fourth = "";

			//checking whether key is innerTable'key or not
			if (second.indexOf(':') != -1)
			{
				condition = true;
				third = second.substring(0, second.indexOf(':'));
				fourth = second.substring(second.lastIndexOf('_'));
			}

			if (isDeleteClicked)
			{
				Map values = map;

				//for outerTable
				int outerCount = 1;

				//for innerTable
				int innerCount = 1;
				for (int i = 1; i <= values.size(); i++)
				{
					String id = "";
					String mapId = "";
					//for innerTable key's rearrangement
					if (condition)
					{
						if (outer == null)
						{
							//for outer key's rearrangement
							for (int j = 1; j <= values.size(); j++)
							{
								id = first + ":" + i + third + ":" + j + fourth;
								mapId = first + ":" + outerCount + third + ":"
								+ j + fourth;
								//checking whether map from
								//form contains keys or not
								if (values.containsKey(id))
								{
									values.put(mapId, map.get(id));
									outerCount++;
								}
							}
						}
						else
						{
							id = first + ":" + outer + third + ":" + i + fourth;
							mapId = first + ":" + outer + third
							+ ":" + innerCount + fourth;
						}
					}
					else
					{
						id = first + ":" + i + second;
						mapId = first + ":" + innerCount + second;
					}

					//rearranging key's
					if (values.containsKey(id))
					{
						values.put(mapId, map.get(id));
						innerCount++;
					}
				}
			}
		}
	}
}
