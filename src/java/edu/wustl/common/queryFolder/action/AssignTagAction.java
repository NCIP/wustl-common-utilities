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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.queryFolder.beans.Tag;
import edu.wustl.common.queryFolder.bizlogic.TagBizLogic;
import edu.wustl.common.util.global.Constants;
import edu.wustl.dao.exception.DAOException;

public class AssignTagAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String queryChecks = request.getParameter(Constants.QUERYCHECKBOX_STRING);
		List<String> queryCheckList = getListFromString(queryChecks);
		addTag(request, queryCheckList);
		assignTag(request, queryCheckList);
		return null;
	}

	private void assignTag(HttpServletRequest request, List<String> queryCheckList)
			throws BizLogicException
	{
		TagBizLogic tagBizLogic = new TagBizLogic();
		String tagChecks = request.getParameter(Constants.TAGCHECKBOX_STRING);
		List<String> tagCheckList = getListFromString(tagChecks);
		Iterator<String> tagIterate = tagCheckList.iterator();
		while (tagIterate.hasNext())
		{
			String tagString = (String) tagIterate.next();
			int tagId = Integer.parseInt(tagString);
			assignTag(queryCheckList, tagBizLogic, tagId);
		}
	}

	private void addTag(HttpServletRequest request, List<String> queryCheckList)
			throws BizLogicException, DAOException
	{
		String newTagName = request.getParameter(Constants.NEWTAGNAME_STRING);
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				edu.wustl.common.util.global.Constants.SESSION_DATA);
		TagBizLogic tagBizLogic = new TagBizLogic();
		if (newTagName != null && !newTagName.isEmpty())
		{
			Tag tag = new Tag();
			tag.setTagLabel(newTagName);
			tag.setUserId(sessionDataBean.getUserId());
			long tagId = tagBizLogic.insertTag(tag);
			assignTag(queryCheckList, tagBizLogic, tagId);
		}
	}

	private void assignTag(List<String> queryCheckList, TagBizLogic tagBizLogic, long tagId)
			throws BizLogicException
	{
		Iterator<String> queryIterate = queryCheckList.iterator();
		while (queryIterate.hasNext())
		{
			String queryString = (String) queryIterate.next();
			int objId = Integer.parseInt(queryString);

			tagBizLogic.assignTag(tagId, objId, Constants.OBJ_TYPE_QUERY);
		}
	}

	private List<String> getListFromString(String idStr)
	{
		List<String> queryCheckList = new ArrayList<String>();
		StringTokenizer querytokens = new StringTokenizer(idStr, ",");
		while (querytokens.hasMoreTokens())
		{
			queryCheckList.add((String) querytokens.nextToken());
		}
		return queryCheckList;
	}

}
