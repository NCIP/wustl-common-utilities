
package edu.wustl.common.labelSQLApp.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.labelSQLApp.domain.LabelSQL;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class LabelSQLBizlogic extends CommonBizlogic
{

	/**
	 * Gives the LabelSQL from the LabelSQL id
	 * @param labelSQLId
	 * @return
	 * @throws Exception
	 */
	public LabelSQL getLabelSQLById(Long labelSQLId) throws Exception
	{
		LabelSQL labelSQL = null;

		System.out.println("create session getLabelSQLById...");
	//	Session session = HibernateUtil.newSession();
		System.out.println("create session getLabelSQLById done...");
		try
		{
			Long id = labelSQLId;
//			HibernateDatabaseOperations<LabelSQL> dbHandler = new HibernateDatabaseOperations<LabelSQL>(
//					session);
			System.out.println("retrieveById getLabelSQLById...");
			labelSQL =(LabelSQL) retrieve(LabelSQL.class.getName(), id);
			//labelSQL = dbHandler.retrieveById(LabelSQL.class.getName(), id);
			System.out.println("retrieveById getLabelSQLById done...");
		}
		finally
		{
			System.out.println("closing session getLabelSQLById...");
		//	session.close();
			System.out.println("closing session getLabelSQLById done...");
		}

		return labelSQL;
	}

	/**
	 * Retrieves the LabelSQL on the basis of label
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public List<LabelSQL> getLabelSQLByLabel(String label) throws Exception
	{
		List labelSQLList = null;
		System.out.println("create session getLabelSQLByLabel...");
	//	Session session = HibernateUtil.newSession();
		System.out.println("create session getLabelSQLByLabel done...");
		try
		{
//			HibernateDatabaseOperations<LabelSQL> dbHandler = new HibernateDatabaseOperations<LabelSQL>(
//					session);
			System.out.println("retrieve getLabelSQLByLabel...");
			 QueryWhereClause queryWhereClause = new QueryWhereClause(LabelSQL.class.getName());
	            queryWhereClause.addCondition(new EqualClause("label", '?'));
	            List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
	    			columnValueBeans.add(new ColumnValueBean(label));
			labelSQLList = retrieve(LabelSQL.class.getName(), null, queryWhereClause, columnValueBeans);
		//	(LabelSQL.class.getName(), "label", label);
			System.out.println("retrieve getLabelSQLByLabel done...");
		}
		finally
		{
			System.out.println("closing session getLabelSQLByLabel...");
//			session.close();
			System.out.println("closing session getLabelSQLByLabel done...");
		}

		return (List<LabelSQL>)labelSQLList;
	}

	/**
	 * Insert LabelSQL
	 * @param label
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public long insertLabelSQL(String label, String sql) throws Exception
	{
		LabelSQL labelSQL = new LabelSQL();
		labelSQL.setLabel(label);
		labelSQL.setQuery(sql);

		System.out.println("create session insertLabelSQL...");
//		Session session = HibernateUtil.newSession();
		System.out.println("create session insertLabelSQL done...");
		try
		{
//			HibernateDatabaseOperations<LabelSQL> dbHandler = new HibernateDatabaseOperations<LabelSQL>(
//					session);
			System.out.println("insert insertLabelSQL...");
		//	dbHandler.insert(labelSQL);
			insert(labelSQL);
			System.out.println("insert insertLabelSQL done...");

			return labelSQL.getId();
		}
		finally
		{
			System.out.println("closing session insertLabelSQL...");
	//		session.close();
			System.out.println("closing session insertLabelSQL done...");
		}

	}

	/**
	 * Gives all LabelSQL
	 * @param labelSQLId
	 * @return
	 * @throws Exception
	 */
	public List<LabelSQL> getAllLabelSQL() throws Exception
	{
		List<LabelSQL> labelSQLs = null;

		System.out.println("create session getAllLabelSQL...");
	//	Session session = HibernateUtil.newSession();
		System.out.println("create session getAllLabelSQL done...");
		try
		{
//
//			HibernateDatabaseOperations<LabelSQL> dbHandler = new HibernateDatabaseOperations<LabelSQL>(
//					session);
			System.out.println("retrieve getAllLabelSQL...");
//			labelSQLs = dbHandler.retrieve(LabelSQL.class.getName());
			labelSQLs = retrieve(LabelSQL.class.getName());
			System.out.println("retrieve getAllLabelSQL done...");
		}
		finally
		{
			System.out.println("closing session getAllLabelSQL...");
	//		session.close();
			System.out.println("closing session getAllLabelSQL done...");
		}

		return labelSQLs;
	}

	/**
	 * Returns the LabelSQL based on label or display name.
	 * @param labelSQLId
	 * @return
	 * @throws Exception
	 */
	public Long getLabelSQLIdByLabelOrDisplayName(Long CPId, String label) throws Exception
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		String hql="getSysLabelSQLIdByLabelOrDisplayName";
		if(CPId!=null)
		{
			substParams.put("0", new NamedQueryParam(DBTypes.LONG, CPId));
			substParams.put("1", new NamedQueryParam(DBTypes.STRING, label));
			substParams.put("2", new NamedQueryParam(DBTypes.STRING, label));
			hql="getLabelSQLIdByLabelOrDisplayName";
		}
		else
		{
			substParams.put("1", new NamedQueryParam(DBTypes.STRING, label));
			substParams.put("0", new NamedQueryParam(DBTypes.STRING, label));
		}
		List<?> result = executeHQL(hql, substParams);

		if (result.size() != 0)
		{
			return Long.parseLong(result.get(0).toString());
		}
		else
		{
			return null;
		}

	}

}
