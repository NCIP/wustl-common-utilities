/**
 * <p>Title: LookupResult Class>
 * <p>Description:	This contains the matching object with probablity match.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 */
package edu.wustl.common.lookup;

import java.io.Serializable;

/**
 * This contains the matching object with probablity match.
 * @author vaishali_khandelwal
 *
 */
public class DefaultLookupResult implements Serializable
{
	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -2925568915907972879L;

	/**
	 * object.
	 */
	private Object object;

	/**
	 * weight.
	 */
	private Double weight;

	/**
	 * isSSNPMI.
	 */
	private MatchingStatusForSSNPMI isSSNPMI;

	/**
	 * exactMatching.
	 */
	private boolean exactMatching;

	/**
	 * get IsSSNPMI.
	 * @return MatchingStatusForSSNPMI.
	 */
	public MatchingStatusForSSNPMI getIsSSNPMI()
	{
		return isSSNPMI;
	}

	/**
	 * set IsSSNPMI.
	 * @param isSSNPMI isSSNPMI.
	 */
	public void setIsSSNPMI(MatchingStatusForSSNPMI isSSNPMI)
	{
		this.isSSNPMI = isSSNPMI;
	}

	/**
	 * get Object.
	 * @return Object.
	 */
	public Object getObject()
	{
		return object;
	}

	/**
	 * set Object.
	 * @param object object.
	 */
	public void setObject(Object object)
	{
		this.object = object;
	}

	/**
	 * get Weight.
	 * @return weight.
	 */
	public Double getWeight()
	{
		return weight;
	}

	/**
	 * set Weight.
	 * @param weight weight
	 */
	public void setWeight(Double weight)
	{
		this.weight = weight;
	}


	/**
	 * @return the exactMatching
	 */
	public boolean isExactMatching()
	{
		return exactMatching;
	}

	/**
	 * @param exactMatching the exactMatching to set
	 */
	public void setExactMatching(boolean exactMatching)
	{
		this.exactMatching = exactMatching;
	}

}
