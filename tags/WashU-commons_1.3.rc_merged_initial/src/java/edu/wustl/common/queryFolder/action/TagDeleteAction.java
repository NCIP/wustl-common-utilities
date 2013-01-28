
package edu.wustl.common.queryFolder.action;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.queryFolder.beans.AssignTag;
import edu.wustl.common.queryFolder.beans.Tag;
import edu.wustl.common.queryFolder.bizlogic.TagBizLogic;
import edu.wustl.common.util.global.Constants;

public class TagDeleteAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		TagBizLogic tagBizLogic = new TagBizLogic();
		String tagIds = (String) request.getParameter(Constants.TAGID_STRING);
		long tagId = Long.parseLong(tagIds);

		Tag tagById = tagBizLogic.getTagById(tagId);
		Set<AssignTag> assignTagList = tagById.getAssignTag();

		for (AssignTag assignTag : assignTagList)
		{
			tagBizLogic.deleteAssignObject(assignTag);
		}

		Tag tag = new Tag();
		tag.setTagId(tagId);
		tagBizLogic.deleteTag(tag);
		return null;
	}

}
