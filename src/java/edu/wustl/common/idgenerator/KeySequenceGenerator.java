
package edu.wustl.common.idgenerator;

/**
 * @author suhas_khot
 *
 */
public class KeySequenceGenerator
{

	/**
	 * Identifier
	 */
	protected Long id;

	/**
	 * Key for which unique identifier is to be generated.
	 */
	protected String keyValue;

	/**
	 * Unique identifier to be generated for this key type. e.g. CS, CP, etc
	 */
	protected String keyType;

	/**
	 * Sequence identifier to be generated on basis of Key.
	 */
	protected String keySequenceId;

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 *
	 * @return the keyValue
	 */
	public String getKeyValue()
	{
		return keyValue;
	}

	/**
	 *
	 * @return the keyType
	 */
	public String getKeyType()
	{
		return keyType;
	}

	/**
	 * @return the keySequenceId
	 */
	public String getKeySequenceId()
	{
		return keySequenceId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 *
	 * @param keyValue the keyValue to set
	 */
	public void setKeyValue(String keyValue)
	{
		this.keyValue = keyValue;
	}

	/**
	 *
	 * @param keyType the keyType to set
	 */
	public void setKeyType(String keyType)
	{
		this.keyType = keyType;
	}

	/**
	 * @param keySequenceId the keySequenceId to set
	 */
	public void setKeySequenceId(String keySequenceId)
	{
		this.keySequenceId = keySequenceId;
	}

}
