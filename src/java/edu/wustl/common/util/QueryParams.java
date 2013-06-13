/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util;

import java.sql.Connection;
import java.util.Map;

import edu.wustl.common.beans.SessionDataBean;
/**
 * Query Params.
 *
 */
public class QueryParams
{

	/**
	 * query.
	 */
	private String query;
	/**
	 * connection.
	 */
	private Connection connection;
	/**
	 * sessionDataBean.
	 */
	private	SessionDataBean sessionDataBean;
	/**
	 * secureToExecute.
	 */
	private boolean secureToExecute;
	/**
	 * hasConditionOnIdentifiedField.
	 */
	private boolean hasConditionOnIdentifiedField;
	/**
	 * queryResultObjectDataMap.
	 */
	private Map queryResultObjectDataMap;
	/**
	 * startIndex.
	 */
	private int startIndex;
	/**
	 * noOfRecords.
	 */
	private int noOfRecords;

	/**
	 * @param query :
	 */
	public void setQuery(String query)
	{
		this.query = query;
	}

	/**
	 * @return the query
	 */
	public String getQuery()
	{
		return query;
	}
	/**
	 * @return connection
	 */
	public Connection getConnection()
	{
		return connection;
	}
	/**
	 * @param connection :
	 */
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	/**
	 * @return sessionDataBean
	 */
	public SessionDataBean getSessionDataBean()
	{
		return sessionDataBean;
	}
	/**
	 * @param sessionDataBean :
	 */
	public void setSessionDataBean(SessionDataBean sessionDataBean)
	{
		this.sessionDataBean = sessionDataBean;
	}
	/**
	 * @return secureToExecute
	 */
	public boolean isSecureToExecute()
	{
		return secureToExecute;
	}
	/**
	 * @param isSecureExecute :
	 */
	public void setSecureToExecute(boolean isSecureExecute)
	{
		this.secureToExecute = isSecureExecute;
	}
	/**
	 * @return hasConditionOnIdentifiedField
	 */
	public boolean isHasConditionOnIdentifiedField()
	{
		return hasConditionOnIdentifiedField;
	}
	/**
	 * @param hasConditionOnIdentifiedField :
	 */
	public void setHasConditionOnIdentifiedField(
			boolean hasConditionOnIdentifiedField)
	{
		this.hasConditionOnIdentifiedField = hasConditionOnIdentifiedField;
 	}
	/**
	 * @return queryResultObjectDataMap
	 */
	public Map getQueryResultObjectDataMap()
	{
		return queryResultObjectDataMap;
	}
	/**
	 * @param queryResultObjectDataMap :
	 */
	public void setQueryResultObjectDataMap(Map queryResultObjectDataMap)
	{
		this.queryResultObjectDataMap = queryResultObjectDataMap;
	}
	/**
	 * @return startIndex
	 */
	public int getStartIndex()
	{
		return startIndex;
	}
	/**
	 * @param startIndex :
	 */
	public void setStartIndex(int startIndex)
	{
		this.startIndex = startIndex;
	}
	/**
	 * @return noOfRecords
	 */
	public int getNoOfRecords()
	{
		return noOfRecords;
	}
	/**
	 * @param noOfRecords :
	 */
	public void setNoOfRecords(int noOfRecords)
	{
		this.noOfRecords = noOfRecords;
	}


}
