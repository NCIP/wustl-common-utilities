package edu.wustl.dao.query.generator;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.dao.QueryWhereClause;


/**
 * @author kalpana_thakur
 * This class stores all query related data.
 */
public class QueryData
{
	/**
	 *
	 */
   private Collection<ColumnValueBean> colValBeans
	= new HashSet<ColumnValueBean>();

  /**
   * Name of the table.
   */
  private String tableName;

  /**
   * Where condition clause.
   */
  private QueryWhereClause queryWhereClause;


  /**
   * @return the table name.
   */
  public String getTableName()
  {
		return tableName;
   }
  /**
   * @return query where clause.
   */
   public QueryWhereClause getQueryWhereClause()
   {
		return queryWhereClause;
   }

   /**
    *@param tableName name of table.
    */
   public void setTableName(String tableName)
   {
		this.tableName = tableName;
   }
   /**
    *@param queryWhereClause where conditional clause of query.
    */
   public void setQueryWhereClause(QueryWhereClause queryWhereClause)
   {
		this.queryWhereClause = queryWhereClause;
   }

   /**
	 *@param columnValueBean : Bean having column name and column value.
	 *@return QueryData It returns the current instance of QueryData.
	 */
   public QueryData addColValBean(ColumnValueBean columnValueBean)
   {
	   colValBeans.add(columnValueBean);
		return this;
   }

   /**
    * @return column value beans.
    */
   public Collection<ColumnValueBean> getColumnValueBeans()
   {
	   return colValBeans;
   }

}
