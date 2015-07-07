/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.labelSQLApp.domain;


public class LabelSQL implements java.io.Serializable
{
	private long id;
	private String label;
	private String query;
	
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}

	
	public String getQuery()
	{
		return query;
	}

	
	public void setQuery(String query)
	{
		this.query = query;
	}

	
		
	
	
}