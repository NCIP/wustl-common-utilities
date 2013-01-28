/**
 *
 */

package edu.wustl.security.global;

/**
 * Interface for storing the roles of users.
 * @author Srinivasarao_Vassadi
 *
 */
public interface Roles
{
	/**
	 * ADMINISTRATOR String role of administrator.
	 */
	String ADMINISTRATOR = "ADMINISTRATOR";
	/**
	 * SUPERVISOR String role of supervisor.
	 */
	String SUPERVISOR = "SUPERVISOR";
	/**
	 * TECHNICIAN String role of technician.
	 */
	String TECHNICIAN = "TECHNICIAN";
	/**
	 * SCIENTIST String   scientist.
	 */
	String SCIENTIST = "SCIENTIST";
	/**
	 * PARTICIPANT String   participant.
	 */
	String PARTICIPANT = "PARTICIPANT";
	/**
	 * READ_ONLY String read only.
	 */
	String READ_ONLY = "READ_ONLY";
	/**
	 * USE_ONLY String use only.
	 */
	String USE_ONLY = "USE_ONLY";
	/**
	 * UPDATE_ONLY String update only.
	 */
	String UPDATE_ONLY = "UPDATE_ONLY";
}
