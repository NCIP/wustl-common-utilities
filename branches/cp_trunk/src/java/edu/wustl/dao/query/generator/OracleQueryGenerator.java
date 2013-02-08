package edu.wustl.dao.query.generator;

/**
 * @author kalpana_thakur
 *
 */
public class OracleQueryGenerator extends AbstractQueryGenerator
{

	/**
	 * @param colValBean : bean having column name value and type.
	 * @return object : value of the column as per the type
	 */

	protected Object fetchColumnValue(ColumnValueBean colValBean)
	{
		Object value;
		switch(colValBean.getColumnType())
		{
			case DBTypes.DATE :
			    value = "to_date ( '"+colValBean.getColumnValue()+"','yyyy-mm-dd')";
				break;

			case DBTypes.NUMBER :
			case DBTypes.INTEGER :
				value = colValBean.getColumnValue();
				break ;

			default :
				value = "'"+colValBean.getColumnValue()+"'";
				break;

		}
		return value;
	}

}
