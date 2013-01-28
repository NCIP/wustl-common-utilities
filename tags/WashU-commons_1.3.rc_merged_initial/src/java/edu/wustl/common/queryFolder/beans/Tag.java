
package edu.wustl.common.queryFolder.beans;

import java.io.Serializable;
import java.util.Set;

public class Tag implements Serializable
{

	private String tagLabel;
	private long tagId;
	private long userId;
	private Set<AssignTag> assignTag;

	public Set<AssignTag> getAssignTag()
	{
		return assignTag;
	}

	public void setAssignTag(Set<AssignTag> assignTag)
	{

		this.assignTag = assignTag;
	}

	public String getTagLabel()
	{
		return tagLabel;
	}

	public void setTagLabel(String tagLabel)
	{
		this.tagLabel = tagLabel;
	}

	public long getTagId()
	{
		return tagId;
	}

	public void setTagId(long tagId)
	{
		this.tagId = tagId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

}
