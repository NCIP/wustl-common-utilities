/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.queryFolder.action;

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

public class ObjDeleteAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		TagBizLogic tagBizLogic = new TagBizLogic();
		String assignIds = (String) request.getParameter(Constants.ASSIGNID_STRING);
		String tagIds = (String) request.getParameter(Constants.TAGID_STRING);
		long assignId = Long.parseLong(assignIds);
		long tagId = Long.parseLong(tagIds);
		AssignTag assignTag = new AssignTag();
		assignTag.setId(assignId);
		Tag tag = new Tag();
		tag.setTagId(tagId);
		assignTag.setTag(tag);
		tagBizLogic.deleteAssignObject(assignTag);
		return null;
	}
}
