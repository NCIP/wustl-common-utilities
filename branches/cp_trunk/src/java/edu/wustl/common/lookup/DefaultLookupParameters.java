/**
 * <p>Title: DefaultLookupParameters Class>
 * <p>Description:	This is the implementation class of LookupParameters
 *  which stores the object which is to be matched and the cutoff value </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author santosh_chandak
 */

package edu.wustl.common.lookup;

import java.util.Map;

/**
 * DefaultLookupParameters Class.
 * @author santosh_chandak
 *
 */
public class DefaultLookupParameters implements LookupParameters
{

	private Integer threshold;
	/**
	 * Object.
	 */
	private Object object;
	/**
	 * List of participants.
	 */
	private Map listOfParticipants;

	/**
	 * @return Returns the listOfParticipants.
	 */
	public Map getListOfParticipants()
	{
		return listOfParticipants;
	}

	/**
	 * @param listOfParticipants The listOfParticipants to set.
	 */
	public void setListOfParticipants(Map listOfParticipants)
	{
		this.listOfParticipants = listOfParticipants;
	}

	/**
	 * @return Returns the object.
	 */
	public Object getObject()
	{
		return object;
	}

	/**
	 * @param object The object to set.
	 */
	public void setObject(Object object)
	{
		this.object = object;
	}


	public Integer getThreshold()
	{
		return threshold;
	}


	public void setThreshold(Integer threshold)
	{
		this.threshold = threshold;
	}
}
