/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.queryFolder.bizlogic;

import java.util.List;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.queryFolder.beans.AssignTag;
import edu.wustl.common.queryFolder.beans.Tag;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

//import edu.wustl.common.util.querysuite.DAOUtil;

public class TagBizLogic extends DefaultBizLogic
{

	HibernateDAO hibernateDao = null;

	/**
	 * @param
	 * 
	 * 
	 */
	public void assignTag(long tagId, long objId, String objType) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = new SessionDataBean();
			dao = getHibernateDao(getAppName(), sessionDataBean);

			Tag tag = new Tag();
			tag.setTagId(tagId);
			AssignTag assignTag = new AssignTag();
			assignTag.setObjId(objId);
			assignTag.setObjType(objType);
			assignTag.setTag(tag);
			dao.insert(assignTag);
			dao.commit();

		}
		catch (Exception e)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("db.operation.error");
			throw new BizLogicException(errorKey, e, e.getMessage());
		}
		finally
		{
			closeSession(dao);
		}
	}

	/**
	 * @param
	 * 
	 * 
	 */

	public List<Tag> listTag() throws BizLogicException
	{
		List<Tag> tagList = null;
		DAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = new SessionDataBean();
			dao = getHibernateDao(getAppName(), sessionDataBean);

			tagList = dao.executeQuery("from " + Tag.class.getName());
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			closeSession(dao);
		}
		return tagList;
	}

	/**
	 * @param
	 * 
	 * 
	 */
	public void deleteTag(Tag tag) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = new SessionDataBean();
			dao = getHibernateDao(getAppName(), sessionDataBean);
			dao.delete(tag);
			dao.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{

			closeSession(dao);
		}

	}

	/**
	 * @param
	 * 
	 * 
	 */
	public void deleteAssignObject(AssignTag assignTag) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = new SessionDataBean();
			dao = getHibernateDao(getAppName(), sessionDataBean);
			dao.delete(assignTag);
			dao.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			closeSession(dao);
		}

	}

	/**
	 * @param
	 * 
	 * 
	 */
	public long insertTag(Tag tag) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			SessionDataBean sessionDataBean = new SessionDataBean();
			dao = getHibernateDao(getAppName(), sessionDataBean);
			insert(tag, dao);
			dao.commit();
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		finally
		{
			closeSession(dao);
		}
		return tag.getTagId();
	}

	/**
	 * @param
	 * 
	 * 
	 */

	public Tag getTagById(Long tagId) throws DAOException
	{
		DAO dao = null;
		SessionDataBean sessionDataBean = new SessionDataBean();
		dao = getHibernateDao(getAppName(), sessionDataBean);
		Tag tag = (Tag) dao.retrieveById(Tag.class.getName(), tagId);
		return tag;
	}
}