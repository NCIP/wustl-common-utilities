/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.queryFolder.beans;

import java.io.Serializable;

public class AssignTag implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long objId;
	private String objType;
	private Tag tag;

	public Tag getTag()
	{
		return tag;
	}

	public void setTag(Tag tag)
	{
		this.tag = tag;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getObjId()
	{
		return objId;
	}

	public void setObjId(long objId)
	{
		this.objId = objId;
	}

	public String getObjType()
	{
		return objType;
	}

	public void setObjType(String objType)
	{
		this.objType = objType;
	}

}
