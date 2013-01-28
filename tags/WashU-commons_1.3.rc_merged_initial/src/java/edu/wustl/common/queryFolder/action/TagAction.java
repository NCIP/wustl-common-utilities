
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

public class TagAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		TagBizLogic tBizLogic = new TagBizLogic();
		List<Tag> tagList = tBizLogic.listTag();
		String responseString = VelocityManager.getInstance().evaluate(tagList,
				"privilegeGridTemplate.vm");
		response.getWriter().write(responseString);
		response.setContentType("text/xml");

		return null;
	}

}