package edu.wustl.dao.query.generator;

import java.util.Iterator;

import edu.wustl.dao.util.DAOConstants;

/**
 * @author kalpana_thakur
 * This class will be used to generate SQL queries like insert,update.
 * It accept the query data and parse the data to create insert update query.
 */
public abstract class AbstractQueryGenerator implements QueryGenerator
{


	/**
	 * It holds data to generate SQL.
	 * Data like table table columnValue bean having column name ,
	 * value assigned to the column and where condition clause.
	 * QueryData
	 */
	protected QueryData queryData;

	/**
	 * Parse the QueryData and create the insert query.
	 * @see edu.wustl.dao.query.generator.QueryGenerator#getInsertQuery(java.lang.String)
	 * @return : The insert query.
	 */
	public String getInsertQuery()
	{

		StringBuffer insertSql = new StringBuffer(DAOConstants.TRAILING_SPACES);
		StringBuffer valuePart = new StringBuffer(DAOConstants.TRAILING_SPACES);
		insertSql.append("insert into").append(DAOConstants.TRAILING_SPACES).
		append(queryData.getTableName()).append(" (");
		valuePart.append("values (");
		Iterator<ColumnValueBean> colValBeanItr = queryData.getColumnValueBeans().iterator();
		while(colValBeanItr.hasNext())
		{
			ColumnValueBean colValBean = colValBeanItr.next();
			insertSql.append(colValBean.getColumnName());
			valuePart.append(fetchColumnValue(colValBean));
			if(colValBeanItr.hasNext())
			{
				insertSql.append(DAOConstants.DELIMETER);
				valuePart.append(DAOConstants.DELIMETER);
			}

		}
		insertSql.append(" )");
		valuePart.append(" )");
		insertSql.append(valuePart.toString());

		return insertSql.toString();
	}

	/**
	 * @param colValBean : Bean holding column name and value.
	 * @return Object : Returns the column value.
	 */
	protected abstract Object fetchColumnValue(ColumnValueBean colValBean);

	/**
	 * Reads the queryData and generates the update query.
	 * @return update query.
	 */
	public String getUpdateQuery()
	{
		StringBuffer updateSql = new StringBuffer(DAOConstants.TRAILING_SPACES);
		updateSql.append("update").append(DAOConstants.TRAILING_SPACES).append(queryData.getTableName()).
		append(DAOConstants.TRAILING_SPACES).append("set").append(DAOConstants.TRAILING_SPACES);

		Iterator<ColumnValueBean> colValBeanItr = queryData.getColumnValueBeans().iterator();
		while(colValBeanItr.hasNext())
		{
			ColumnValueBean colValBean = colValBeanItr.next();
			updateSql.append(colValBean.getColumnName()).append(DAOConstants.EQUAL)
			.append(fetchColumnValue(colValBean));
			if(colValBeanItr.hasNext())
			{
				updateSql.append(DAOConstants.DELIMETER);
			}
		}
		updateSql.append(queryData.getQueryWhereClause().toWhereClause());
		return updateSql.toString();
	}


	/**
	 * This method will be called to set the Query data.
	 * @param queryData : queryData
	 */
	public void setQueryData(QueryData queryData)
	{
		this.queryData = queryData;
	}

	/*public static void main(String[] args)
	{
		QueryData queryData = new QueryData();
		queryData.setTableName("Temp");

		QueryWhereClause queryWhereClause = new QueryWhereClause("Temp");
		queryWhereClause.addCondition(new EqualClause("identifier",1));

		queryData.setQueryWhereClause(queryWhereClause);
		queryData.addColValBean(new ColumnValueBean("name","kt",DBTypes.STRING)).
		addColValBean(new ColumnValueBean("date","5feb09",DBTypes.DATE));
		AbstractQueryGenerator query = new OracleQueryGenerator();
		query.setQueryData(queryData);
		System.out.println("-----"+query.getUpdateQuery());
		System.out.println("-----"+query.getInsertQuery());
	}*/

}
