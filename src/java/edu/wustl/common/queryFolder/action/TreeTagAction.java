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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.queryFolder.beans.Tag;
import edu.wustl.common.queryFolder.bizlogic.TagBizLogic;
import edu.wustl.common.queryFolder.velocity.VelocityManager;
import edu.wustl.common.util.global.Constants;

public class TreeTagAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		TagBizLogic tagBizLogic = new TagBizLogic();
		List<Tag> tagList = tagBizLogic.listTag();
		request.setAttribute(Constants.TAGLIST_STRING, tagList);
		String responseString = VelocityManager.getInstance().evaluate(tagList,
				"privilegeTreeTemplate.vm");
		response.getWriter().write(responseString);
		response.setContentType("text/xml");
		return null;
	}

}
