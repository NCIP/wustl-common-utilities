/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.labelSQLApp.bizlogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.labelSQLApp.domain.LabelSQLAssociation;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class CommonBizlogic extends DefaultBizLogic
{

	/**
	 * This method executes the HQL in LabelSQL_HQL.hbm.xml
	 * @param queryName
	 * @param values
	 * @return
	 * @throws HibernateException
	 */
	public List<?> executeHQL(String queryName, Map<String, NamedQueryParam> substParams)
			throws DAOException
	{
		List objects = null;
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO) getHibernateDao(getAppName(), null);
			objects = hibernateDao.executeNamedQuery(queryName, substParams);
		
		}
		finally
		{
			hibernateDao.closeSession();
		}

		return objects;

	}

	/**
	 * Retrieves the label for dashboard item can be predefined label or display name
	 * @param labelSQLAssocId
	 * @return
	 * @throws Exception
	 */
	public String getLabelByLabelSQLAssocId(Long labelSQLAssocId) throws Exception
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, labelSQLAssocId));
		substParams.put("1", new NamedQueryParam(DBTypes.LONG, labelSQLAssocId));

		List<?> result = executeHQL("getLabelByLabelSQLAssocId", substParams);

		if (result.size() != 0)
		{
			return (String) result.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the query associated with label id
	 * @param labelSQLId
	 * @return
	 * @throws Exception
	 */
	public List<Clob> getQueryByLabelSQLAssocId(Long labelSQLId) throws Exception
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, labelSQLId));

		List<?> result = executeHQL("getQueryByLabelSQLAssocId", substParams);

		return (List<Clob>) result;
	}

	/**
	 * Returns map with the label/display name and query count
	 * @param CPId
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Integer> getLabelQueryResultMapByCPId(Long CPId) throws Exception
	{
		LinkedHashMap<String, Integer> labelQueryResultMap = new LinkedHashMap<String, Integer>();
		LabelSQLAssociationBizlogic labelSQLAssociationBizlogic = new LabelSQLAssociationBizlogic();

		/*Retrieve all the LabelSQL association associated with the CP/CS*/
		List<LabelSQLAssociation> labelSQLAssociations = labelSQLAssociationBizlogic
				.getLabelSQLAssocCollection(CPId);

		for (LabelSQLAssociation labelSQLAssociation : labelSQLAssociations)
		{

			Long labelAssocId = labelSQLAssociation.getId();

			/*insert label and query count in map*/
			String label = getLabelByLabelSQLAssocId(labelAssocId);
			int result = executeQuery(
					clobToStringConversion((getQueryByLabelSQLAssocId(labelAssocId)).get(0)), CPId);

			if (!"".equals(label) && label != null)
			{
				labelQueryResultMap.put(label, result);
			}

		}

		return labelQueryResultMap;
	}

	/**
	 * Returns String with labelSQLAsociation and query result
	 * @param labelSQLAssocID
	 * @return
	 * @throws Exception
	 */
	public String getQueryResultByLabelSQLAssocId(Long labelSQLAssocID) throws Exception
	{
		/*Retrieve all the LabelSQL association associated with the CP/CS*/
		LabelSQLAssociation labelSQLAssociation = new LabelSQLAssociationBizlogic()
				.getLabelSQLAssocById(labelSQLAssocID);

		int result = executeQuery(
				clobToStringConversion((getQueryByLabelSQLAssocId(labelSQLAssociation.getId()))
						.get(0)), labelSQLAssociation.getLabelSQLCollectionProtocol());//retrieve the query from labelSQLAssocID and execute it
		String resultString = labelSQLAssocID.toString() + "," + result;
		return resultString;
	}

	public JDBCDAO getJDBCDAO(SessionDataBean sessionDataBean) throws DAOException
	{
		JDBCDAO jdbcDao = DAOConfigFactory.getInstance().getDAOFactory(getAppName()).getJDBCDAO();
		jdbcDao.openSession(sessionDataBean);
		return jdbcDao;
	}

	/**
	 *
	 * @param jdbcDao
	 * @throws DAOException
	 */
	public static void closeJDBCDAO(JDBCDAO jdbcDao) throws DAOException
	{
		if (jdbcDao != null)
		{
			jdbcDao.closeSession();
		}
	}

	/**
	 *
	 * @param hibernateDao
	 * @throws DAOException
	 */
	public static void closeHibernateDAO(HibernateDAO hibernateDao) throws DAOException
	{
		if (hibernateDao != null)
		{
			hibernateDao.closeSession();
		}
	}
	
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		return true;
	}

	/**
	* Executes the stored SQL to give the count
	* @param sql
	* @param CPId
	* @return
	* @throws Exception
	*/
	private int executeQuery(String sql, Long CPId) throws Exception
	{
		int count = 0;
		//		Session session = HibernateUtil.newSession();
		sql = sql.trim();
		if (sql.endsWith(";"))
		{
			sql = sql.substring(0, sql.length() - 1);
		}

		JDBCDAO jdbcDao = getJDBCDAO(null);
		try
		{
			LinkedList<ColumnValueBean> columnValueBeanList = null;
			if (CPId != null)
			{
				columnValueBeanList = new LinkedList<ColumnValueBean>();
				columnValueBeanList.add(new ColumnValueBean(CPId, DBTypes.LONG));
			}
			List resultList = jdbcDao.executeQuery(sql, columnValueBeanList);
			count = Integer.valueOf(((List) resultList.get(0)).get(0).toString());
		}
		catch (Exception e)
		{
			Logger.out.error("Error executing query -> " + sql);
			e.printStackTrace();
		}
		finally
		{
			jdbcDao.closeSession();
		}
		return count;
	}

	private static String clobToStringConversion(Clob clb) throws IOException, SQLException
	{
		if (clb == null)
			return "";

		StringBuffer str = new StringBuffer();
		String strng;

		BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());

		while ((strng = bufferRead.readLine()) != null)
			str.append(strng);

		return str.toString();
	}

}
