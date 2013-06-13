/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

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
