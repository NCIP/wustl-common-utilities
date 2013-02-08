package edu.wustl.dao.query.generator;

/**
 * @author kalpana_thakur
 *
 */
public class MySQLQueryGenerator extends AbstractQueryGenerator
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
			case DBTypes.BIGINT :
			case DBTypes.BIT :
			case DBTypes.FLOAT :
			case DBTypes.TINYINT :
				value = colValBean.getColumnValue();
			break;

			default :
				value= "'"+colValBean.getColumnValue()+"'";
			break;

		}
		return value;

	}

}
