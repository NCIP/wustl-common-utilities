
package edu.wustl.common.labelSQLApp.domain;

public class LabelSQLAssociation implements java.io.Serializable
{

	private long id;
	private Long labelSQLCollectionProtocol;
	private LabelSQL labelSQL;
	private String userDefinedLabel;
	private Integer seqOrder;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public LabelSQL getLabelSQL()
	{
		return labelSQL;
	}

	public void setLabelSQL(LabelSQL labelsql)
	{
		this.labelSQL = labelsql;
	}

	public Long getLabelSQLCollectionProtocol()
	{
		return labelSQLCollectionProtocol;
	}

	public void setLabelSQLCollectionProtocol(Long labelSQLCollectionProtocol)
	{
		this.labelSQLCollectionProtocol = labelSQLCollectionProtocol;
	}

	public String getUserDefinedLabel()
	{
		return userDefinedLabel;
	}

	public void setUserDefinedLabel(String userDefinedLabel)
	{
		this.userDefinedLabel = userDefinedLabel;
	}

	public Integer getSeqOrder()
	{
		return seqOrder;
	}

	public void setSeqOrder(Integer seqOrder)
	{
		this.seqOrder = seqOrder;
	}

}
