/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Jun 7, 2006
 */

package edu.wustl.common.util.tag;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.TextConstants;

/**
 * @author chetan_bh
 *
 * A Utility class to generate java-script code for constructing
 * java-script hash-table data structure from Java Map data-structures.
 *
 * Assumes that the input is a Map and all keys for outer and inner
 * Maps are strings, and values are either Map or List.
 * Elements of all inner Lists are assumed to be string again.
 */
public final class ScriptGenerator
{

	/**
	 * Constructor.
	 */
	private ScriptGenerator()
	{

	}

	/**
	 * This method gets JS For Outermost Data Table.
	 * @return returner
	 */
	public static String getJSForOutermostDataTable()
	{
		StringBuffer returner = new StringBuffer(
				"<script language=\"JavaScript\" type=\"text/javascript\">"+
				" \n var outerMostDataTable = new Hashtable(); \n </script>\n");
		return returner.toString();
	}

	/**
	 * This method gets JS Equivalent For.
	 * @param dataMap dataMap Java Map datastructure.
	 * @param rowNum rowNum
	 * @return returner
	 */
	public static String getJSEquivalentFor(Map dataMap, String rowNum)
	{
		StringBuffer returner = new StringBuffer(
				"<script language=\"JavaScript\" type=\"text/javascript\"> \n");
		if (dataMap == null || dataMap.isEmpty())
		{
			returner.append("</script>\n");
		}
		else
		{
			int mapDepth = depthOfMap(dataMap);
			for (int i = 0; i <= mapDepth; i++)
			{
				returner.append("var dataTable_").append(rowNum).append('_').append((i + 1))
						.append("; \n");
			}
			returner.append((String) getVarInStringForm(dataMap, 0, "", rowNum));
			returner.append("\n outerMostDataTable.put(\"").append(rowNum).append("\",dataTable_")
					.append(rowNum).append("_1); \n</script>\n");
		}
		return returner.toString();
	}

	/**
	 * A utility function to find the depth of Map.
	 * Uses iteration to find the depth.
	 *
	 * depth of the map means the level of recursion used like
	 * Map of Map of Map of Map of Map of List
	 * here the depth of the outer map is 5.
	 *
	 * @param mapval Java Map datastructure.
	 * @return depth of the map.
	 */
	public static int depthOfMap(Map mapval)
	{
		Map map = mapval;
		int depth = 0;
		while (map instanceof Map)
		{
			Set kSet = map.keySet();
			Object[] kSetArray = kSet.toArray();
			Object firstVal = map.get(kSetArray[0]);
			if (firstVal instanceof Map)
			{
				map = (Map) firstVal;
			}
			else
			{
				break;
			}
			depth++;
		}
		depth++;
		return depth;
	}

	/**
	 * A recursive function to construct a string equivalent of the Javascript code
	 * for constructing a Javascript hastable.
	 * @param obj an Object, it can be either Map or List, if List the call will terminate.
	 * @param depthval the depth of the curent obj in the actual Map.
	 * @param keyValue key from which we got this obj value, for outer map it is empty.
	 * @param rowNumber row Number
	 * @return String equivalent of javascript code.
	 */
	public static String getVarInStringForm(Object obj, int depthval, String keyValue,
			String rowNumber)
	{
		int depth = depthval;
		depth++;
		StringBuffer returner = new StringBuffer();
		if (obj instanceof Map)
		{
			returner = getMapReturner(obj, keyValue, rowNumber, depth);
		}
		else if (obj instanceof List)
		{
			returner = getListReturner(obj, keyValue, rowNumber, depth);
		}

		return returner.toString();
	}

	/**
	 * @param obj Object
	 * @param keyValue key Value.
	 * @param rowNumber row Number.
	 * @param depth depth.
	 * @return returner.
	 */
	private static StringBuffer getListReturner(Object obj, String keyValue, String rowNumber,
			int depth)
	{
		StringBuffer returner = new StringBuffer();
		String varName = "dataTable_" + rowNumber + "_";
		List dList = (List) obj;

		returner.append(varName).append(depth).append(" = new Array(); \n");
		for (int i = 0; i < dList.size(); i++)
		{
			NameValueBean nvb = (NameValueBean) dList.get(i);
			returner.append(varName).append(depth).append('[').append(i).append("] = ").append("\"").append(
					nvb.getValue()).append("\"").append("; \n");
		}
		returner.append(varName).append((depth - 1)).append(".put(\"").append(keyValue).append(
				"\",").append(varName).append(depth).append("); \n");
		return returner;
	}

	/**
	 * @param obj Object
	 * @param keyValue key Value
	 * @param rowNumber row Number
	 * @param depth depth
	 * @return returner.
	 */
	private static StringBuffer getMapReturner(Object obj, String keyValue, String rowNumber,
			int depth)
	{
		StringBuffer returner = new StringBuffer();
		String varName = "dataTable_" + rowNumber + "_";
		Map dMap = (Map) obj;
		returner.append(varName).append(depth).append(" = new Hashtable(); \n");

		Set keySet = dMap.keySet();
		Object[] keySetObj = keySet.toArray();
		for (int i = 0; i < keySetObj.length; i++)
		{
			Object valObj = dMap.get(keySetObj[i]);
			//String kValForNext = keySetObj[i].toString();
			NameValueBean nvb = (NameValueBean) keySetObj[i];
			String kValForNext = nvb.getValue();
			returner.append(getVarInStringForm(valObj, depth, kValForNext, rowNumber));

		}
		if (!TextConstants.EMPTY_STRING.equals(keyValue))
		{
			returner.append(varName).append((depth - 1)).append(".put(\"").append(keyValue).append(
					"\",").append(varName).append(depth).append("); \n");
		}
		return returner;
	}

	/**
	 *
	 * @param args args
	 */
	public static void main(String[] args)
	{
		Map containerMap = new Hashtable();

		Map xMap1 = new Hashtable();
		Map xMap2 = new Hashtable();

		List list1 = new ArrayList();
		list1.add("A");
		List list2 = new ArrayList();
		list2.add("B");
		List list3 = new ArrayList();
		list3.add("C");
		list3.add("D");

		xMap1.put("1", list1);
		xMap1.put("2", list2);

		xMap2.put("22", list3);

		containerMap.put("111", xMap1);
		containerMap.put("222", xMap2);

		Map fourthMap = new Hashtable();
		fourthMap.put("@", containerMap);

		//String result = getJSEquivalentFor(fourthMap, "1");

	}

	/**
	 *
	 * @param args args
	 */
	public static void main11(String[] args)
	{
		Map containerMap = new Hashtable();

		Map xMap1 = new Hashtable();
		Map xMap2 = new Hashtable();

		List list1 = new ArrayList();
		list1.add(new NameValueBean("A", "A"));
		List list2 = new ArrayList();
		list2.add(new NameValueBean("B", "B"));
		List list3 = new ArrayList();
		list3.add(new NameValueBean("C", "C"));
		list3.add(new NameValueBean("D", "D"));

		xMap1.put(new NameValueBean("1", "1"), list1);
		xMap1.put(new NameValueBean("2", "2"), list2);

		xMap2.put(new NameValueBean("22", "22"), list3);

		containerMap.put(new NameValueBean("111", "111"), xMap1);
		containerMap.put(new NameValueBean("222", "222"), xMap2);

		Map fourthMap = new Hashtable();
		fourthMap.put(new NameValueBean("@", "@"), containerMap);

		//String result = getJSEquivalentFor(fourthMap, "1");

	}
}
