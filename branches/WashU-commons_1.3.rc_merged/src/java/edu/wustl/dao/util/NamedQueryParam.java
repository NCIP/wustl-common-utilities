package edu.wustl.dao.util;

/**
 * @author kalpana_thakur
 *
 */
public class NamedQueryParam
{
	/**
	 * Parameter type.
	 */
	int type;
	/**
	 * Parameter value.
	 */
	Object value;

	/**
	 * @param type Parameter type.
	 * @param value Parameter value.
	 */
	public NamedQueryParam(int type,Object value)
	{
		this.type = type;
		this.value = value;
	}

	/**
	 * @return Parameter type.
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * Set parameter type.
	 * @param type :parameter type.
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	/**
	 * @return value.
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * @param value the value
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

}
