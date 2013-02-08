package edu.wustl.dao.query.generator;


/**
 * @author kalpana_thakur
 * This class creates the bean having column name and column value.
 */
public class ColumnValueBean implements Comparable
{
	/**
	 * Name of the Column.
	 */
	private String columnName;

	/**
	 * Value of the Column.
	 */
	private Object columnValue;
	/**
	 * Type of column.
	 */
	private int columnType;

	/**
	 * @param columnName : column name
	 * @param columnValue : column value
	 * @param columnType : column type
	 */
	public ColumnValueBean(String columnName,
			Object columnValue,int columnType)
	{
		this.columnName = columnName;
		this.columnValue = columnValue;
		this.columnType = columnType;

	}

	/**
	 * @param columnName : column name
	 * @param columnValue : column value
	 */
	public ColumnValueBean(String columnName,
			Object columnValue)
	{
		this.columnName = columnName;
		this.columnValue = columnValue;
	}


	/**
	 * @param columnValue : column value
	 * @param columnType : column type
	 */
	public ColumnValueBean(Object columnValue,int columnType)
	{
		this.columnValue = columnValue;
		this.columnType = columnType;

	}

	/**
	 * @param columnValue : column value
	 */
	public ColumnValueBean(Object columnValue)
	{
		this.columnValue = columnValue;
	}


	/**
	 * @return : the column name
	 */
	public String getColumnName()
	{
		return columnName;
	}
	/**
	 * @return : the column value.
	 */
	public Object getColumnValue()
	{
		return columnValue;
	}

	/**
	 * @return : the column type.
	 */
	public int getColumnType()
	{
		return columnType;
	}
	/**
	 * @param columnName : set the column name.
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
	/**
	 * @param columnValue : set the column value.
	 */
	public void setColumnValue(Object columnValue)
	{
		this.columnValue = columnValue;
	}
	/**
	 * @param columnType : set the column type.
	 */
	public void setColumnType(int columnType)
	{
		this.columnType = columnType;
	}

	/**
	 * @param arg0 ColumnValueBean first.
	 * @param arg1 ColumnValueBean second.
	 * @return integer value.
	 */
	public int compare(Object arg0, Object arg1)
	{

		int returnVal = 0;
		ColumnValueBean columnValueBeanFrst = (ColumnValueBean)arg0;
		ColumnValueBean columnValueBeanSec = (ColumnValueBean)arg1;
		if(columnValueBeanFrst instanceof ColumnValueBean &&
				columnValueBeanSec instanceof ColumnValueBean)
		{
			if(columnValueBeanFrst !=null && columnValueBeanSec != null	)
			{
				String columnFrst = columnValueBeanFrst.getColumnName();
				String columnSec = columnValueBeanSec.getColumnName();
				if(columnFrst != null && columnSec!= null)
				{
					returnVal = columnFrst.compareTo(columnSec);
				}
				else if(columnFrst ==null && columnSec == null)
				{
					returnVal = 0;
				}
				else if(columnFrst ==null)
				{
					returnVal = 1;
				}
				else if(columnSec == null)
				{
					returnVal = -1;
				}

			}
			else if(columnValueBeanFrst ==null && columnValueBeanSec == null)
			{
				returnVal = 0;
			}
			else if(columnValueBeanFrst ==null)
			{
				returnVal = 1;
			}
			else if(columnValueBeanSec == null)
			{
				returnVal = -1;
			}
		}
		return returnVal;
	}

	/**
	 * Test method.
	 * @param args args
	 */
	public static void main(String[] args) 
	{/*


		List<ColumnValueBean> list = new ArrayList<ColumnValueBean>();

		ColumnValueBean colValueBean = new ColumnValueBean("identifier",
				1,DBTypes.INTEGER);
		list.add(colValueBean);

		ColumnValueBean first_name = new ColumnValueBean("first_name",
				"",DBTypes.STRING);
		list.add(first_name);

		ColumnValueBean second_name = new ColumnValueBean("second_name",
				"",DBTypes.STRING);
		list.add(second_name);

		ColumnValueBean age = new ColumnValueBean("1age",
				1,DBTypes.INTEGER);
		list.add(age);

		ColumnValueBean address_id = new ColumnValueBean("uaddress_id",
				1,DBTypes.INTEGER);
		list.add(address_id);

		ColumnValueBean account_id = new ColumnValueBean("account_id",
				1,DBTypes.INTEGER);
		list.add(account_id);

		ColumnValueBean birth_date = new ColumnValueBean("birth_date",
				null,DBTypes.DATE);
		list.add(birth_date);

		ColumnValueBean isAvailable = new ColumnValueBean("isAvailable",
				true,DBTypes.BOOLEAN);
		list.add(isAvailable);

		Collections.sort(list);

		Iterator itr = list.iterator();

		while(itr.hasNext())
		{
			System.out.println(" ::"+((ColumnValueBean)itr.next()).getColumnName());
		}


	*/}

	/**
	 * @param arg1 ColumnValueBean second.
	 * @return integer value.
	 */
	public int compareTo(Object arg1)
	{

		int returnVal = 0;
		ColumnValueBean columnValueBeanSec = (ColumnValueBean)arg1;
		if(columnValueBeanSec instanceof ColumnValueBean)
		{
			if(columnValueBeanSec != null	)
			{
				String columnFrst = this.getColumnName();
				String columnSec = columnValueBeanSec.getColumnName();
				if(columnFrst != null && columnSec!= null)
				{
					returnVal = columnFrst.compareTo(columnSec);
				}
				else if(columnFrst ==null && columnSec == null)
				{
					returnVal = 0;
				}
				else if(columnFrst ==null)
				{
					returnVal = 1;
				}
				else if(columnSec == null)
				{
					returnVal = -1;
				}

			}
			else if(columnValueBeanSec == null)
			{
				returnVal = -1;
			}
		}
		return returnVal;
	}

}
