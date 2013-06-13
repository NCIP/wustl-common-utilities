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
 * Created on Nov 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryResultObjectData
{

	/**
	 * Specify aliasName.
	 */
	private String aliasName;
	/**
	 * Specify identifierColumnId.
	 */
	private int identifierColumnId;
	/**
	 * Specify dependentColumnIds.
	 */
	private List dependentColumnIds;
	/**
	 * Specify dependentObjectAliases.
	 */
	private List dependentObjectAliases;
	/**
	 * Specify relatedQueryResultObjects.
	 */
	private List relatedQueryResultObjects;
	/**
	 * Specify IdentifiedDataColumnIds.
	 */
	private List<Integer> identifiedDataColumnIds;

	/**
	 * Constructor.
	 */
	public QueryResultObjectData()
	{
		super();
	}

	/**
	 * Constructor.
	 * @param aliasName alias Name.
	 */
	public QueryResultObjectData(String aliasName)
	{
		this.aliasName = aliasName;
	}

	/**
	 * @return Returns the aliasName.
	 */
	public String getAliasName()
	{
		return aliasName;
	}

	/**
	 * @param aliasName The aliasName to set.
	 */
	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}

	/**
	 * @return Returns the dependentObjectAliases.
	 */
	public List getDependentObjectAliases()
	{
		return dependentObjectAliases;
	}

	/**
	 * @param dependentObjectAliases The dependentObjectAliases to set.
	 */
	public void setDependentObjectAliases(List dependentObjectAliases)
	{
		this.dependentObjectAliases = dependentObjectAliases;
	}

	/**
	 * @return Returns the identifierColumnId.
	 */
	public int getIdentifierColumnId()
	{
		return identifierColumnId;
	}

	/**
	 * @param identifierColumnId The identifierColumnId to set.
	 */
	public void setIdentifierColumnId(int identifierColumnId)
	{
		this.identifierColumnId = identifierColumnId;
	}

	/**
	 * @return Returns the relatedColumnIds.
	 */
	public List getDependentColumnIds()
	{
		return dependentColumnIds;
	}

	/**
	 * @param dependentColumnIds The dependentColumnIds to set.
	 */
	public void setDependentColumnIds(List dependentColumnIds)
	{
		this.dependentColumnIds = dependentColumnIds;
	}

	/**
	 * This method add Related Query Result Object.
	 * @param queryResultObjectData query Result Object Data.
	 */
	public void addRelatedQueryResultObject(QueryResultObjectData queryResultObjectData)
	{
		this.relatedQueryResultObjects.add(queryResultObjectData);
	}

	/**
	 * This method gets Number Of Independent Objects.
	 * @return Number Of Independent Objects.
	 */
	public int getNumberOfIndependentObjects()
	{
		return relatedQueryResultObjects.size() + 1;
	}

	/**
	 * get Independent Object Aliases.
	 * @return independent Object Aliases.
	 */
	public List getIndependentObjectAliases()
	{
		List<String> independentObjectAliases = new ArrayList<String>();
		independentObjectAliases.add(this.aliasName);
		for (int i = 0; i < relatedQueryResultObjects.size(); i++)
		{
			independentObjectAliases.add(((QueryResultObjectData) relatedQueryResultObjects.get(i))
					.getAliasName());
		}
		return independentObjectAliases;
	}

	/**
	 * This method gets Independent Query Objects.
	 * @return independent Query Objects.
	 */
	public List getIndependentQueryObjects()
	{
		List independentQueryObjects = new ArrayList();
		independentQueryObjects.add(this);
		independentQueryObjects.addAll(this.relatedQueryResultObjects);
		return independentQueryObjects;
	}

	/**
	 * @return Returns the relatedQueryResultObjects.
	 */
	public List getRelatedQueryResultObjects()
	{
		return relatedQueryResultObjects;
	}

	/**
	 * @param relatedQueryResultObjects The relatedQueryResultObjects to set.
	 */
	public void setRelatedQueryResultObjects(List relatedQueryResultObjects)
	{
		this.relatedQueryResultObjects = relatedQueryResultObjects;
	}

	/**
	 * @return Returns the identifiedDataColumnIds.
	 */
	public List getIdentifiedDataColumnIds()
	{
		return this.identifiedDataColumnIds;
	}

	/**
	 * @param identifiedDataColumnIds The identifiedDataColumnIds to set.
	 */
	public void setIdentifiedDataColumnIds(List identifiedDataColumnIds)
	{
		this.identifiedDataColumnIds = identifiedDataColumnIds;
	}

	/**
	 * This method add Identified Data Column Id.
	 * @param columnId column Id to add.
	 */
	public void addIdentifiedDataColumnId(Integer columnId)
	{
		this.identifiedDataColumnIds.add(columnId);
	}
}
