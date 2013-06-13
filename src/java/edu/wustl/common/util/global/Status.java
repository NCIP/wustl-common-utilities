/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.common.util.global;

/**
 * @author prashant_bandal
 *
 */
public final class Status
{

	/**
	 * Specify status Name.
	 */
	private String statusName;
	/**
	 * Specify status No.
	 */
	private int statusNo;

	/**
	 * private constructor.
	 * @param statusName status Name.
	 * @param statusNo status Number.
	 */
	private Status(String statusName, int statusNo)
	{
		this.statusName = statusName;
		this.statusNo = statusNo;
	}

	/**
	 * Specify ACTIVITY_STATUS_ACTIVE.
	 */
	public static final Status ACTIVITY_STATUS_ACTIVE = new Status("Active", 1);
	/**
	 * Specify ACTIVITY_STATUS_DISABLED.
	 */
	public static final Status ACTIVITY_STATUS_DISABLED = new Status("Disabled", 2);
	/**
	 * Specify ACTIVITY_STATUS_CLOSED.
	 */
	public static final Status ACTIVITY_STATUS_CLOSED = new Status("Closed", 3);
	/**
	 * Specify ACTIVITY_STATUS.
	 */
	public static final Status ACTIVITY_STATUS = new Status("activityStatus", 4);
	/**
	 * Specify ACTIVITY_STATUS_APPROVE.
	 */
	public static final Status ACTIVITY_STATUS_APPROVE = new Status("Approve", 5);
	/**
	 * Specify ACTIVITY_STATUS_REJECT.
	 */
	public static final Status ACTIVITY_STATUS_REJECT = new Status("Reject", 6);
	/**
	 * Specify ACTIVITY_STATUS_NEW.
	 */
	public static final Status ACTIVITY_STATUS_NEW = new Status("New", 7);
	/**
	 * Specify ACTIVITY_STATUS_PENDING.
	 */
	public static final Status ACTIVITY_STATUS_PENDING = new Status("Pending", 8);

	//Approve User status values.
	/**
	 * Specify APPROVE_USER_APPROVE_STATUS.
	 */
	public static final Status APPROVE_USER_APPROVE_STATUS = new Status("Approve", 9);
	/**
	 * Specify APPROVE_USER_REJECT_STATUS.
	 */
	public static final Status APPROVE_USER_REJECT_STATUS = new Status("Reject", 10);
	/**
	 * Specify APPROVE_USER_PENDING_STATUS.
	 */
	public static final Status APPROVE_USER_PENDING_STATUS = new Status("Pending", 11);

	/**
	 * This method compare status String.
	 * @param statusString object.
	 * @return true if object is equal.
	 */
	@Override
	public boolean equals(Object statusString)
	{
		boolean status = false;
		if (this.statusName.equals(statusString))
		{
			status = true;
		}
		return status;
	}

	/**
	 * This method returns hash Code.
	 * @return int.
	 */
	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		if (this.statusName != null)
		{
			hashCode = this.statusName.hashCode();
		}
		return hashCode;
	}

	/**
	 * This method converts object to string.
	 * @return string object.
	 */
	public String toString()
	{
		return this.statusName;
	}
	/**
	 * Get status.
	 * @return statusName.
	 */
	public String getStatus()
	{
		return this.statusName;
	}

	/**
	 * @return the statusNo
	 */
	public int getStatusNo()
	{
		return statusNo;
	}
}
