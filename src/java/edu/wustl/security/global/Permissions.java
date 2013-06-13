/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.global;

/**
 * Interface for storing the names of Permissions that are defined
 * in the authorization database.
 *
 *
 * @author Brian Husted
 */
public interface Permissions
{
	/**
	 * READ String read permission.
	 */
	String READ = "READ";
	/**
	 * READ_DENIED String read denied permission.
	 */
	String READ_DENIED = "READ_DENIED";
	/**
	 * UPDATE String update permission.
	 */
	String UPDATE = "UPDATE";
	/**
	 * DELETE String delete permission.
	 */
	String DELETE = "DELETE";
	/**
	 * CREATE String create permission.
	 */
	String CREATE = "CREATE";
	/**
	 * EXECUTE String execute permission.
	 */
	String EXECUTE = "EXECUTE";
	/**
	 * USE String use permission.
	 */
	String USE = "USE";
	/**
	 * ASSIGN_READ String assign read permission.
	 */
	String ASSIGN_READ = "ASSIGN_READ";
	/**
	 * ASSIGN_USE String assign use permission.
	 */
	String ASSIGN_USE = "ASSIGN_USE";
	/**
	 * IDENTIFIED_DATA_ACCESS String identifying data access permission.
	 */
	String IDENTIFIED_DATA_ACCESS = "IDENTIFIED_DATA_ACCESS";
	/**
	 * USER_PROVISIONING String provisioning user permission.
	 */
	String USER_PROVISIONING = "USER_PROVISIONING";
	/**
	 * REPOSITORY_ADMINISTRATION String repository administration permission.
	 */
	String REPOSITORY_ADMINISTRATION = "REPOSITORY_ADMINISTRATION";
	/**
	 * STORAGE_ADMINISTRATION String storage administration permission.
	 */
	String STORAGE_ADMINISTRATION = "STORAGE_ADMINISTRATION";
	/**
	 * PROTOCOL_ADMINISTRATION String protocol administration permission.
	 */
	String PROTOCOL_ADMINISTRATION = "PROTOCOL_ADMINISTRATION";
	/**
	 * DEFINE_ANNOTATION String defining annotation permission.
	 */
	String DEFINE_ANNOTATION = "DEFINE_ANNOTATION";
	/**
	 * REGISTRATION String registration permission.
	 */
	String REGISTRATION = "REGISTRATION";
	/**
	 * SPECIMEN_ACCESSION String specimen accession permission.
	 */
	String SPECIMEN_ACCESSION = "SPECIMEN_ACCESSION";
	/**
	 * DISTRIBUTION String distribution permission.
	 */
	String DISTRIBUTION = "DISTRIBUTION";
	/**
	 * QUERY String permission for query.
	 */
	String QUERY = "QUERY";
	//    String PHI = "PHI";
	/**
	 * PHI String phi access permission.
	 */
	String PHI = "PHI_ACCESS";
	/**
	 * PARTICIPANT_SCG_ANNOTATION String annotation for scg participant permission.
	 */
	String PARTICIPANT_SCG_ANNOTATION = "PARTICIPANT_SCG_ANNOTATION";
	/**
	 * SPECIMEN_ANNOTATION String annotation for specimen permission.
	 */
	String SPECIMEN_ANNOTATION = "SPECIMEN_ANNOTATION";
	/**
	 * SPECIMEN_PROCESSING String specimen processing permission.
	 */
	String SPECIMEN_PROCESSING = "SPECIMEN_PROCESSING";
	/**
	 * SPECIMEN_STORAGE String specimen storage permission.
	 */
	String SPECIMEN_STORAGE = "SPECIMEN_STORAGE";
	/**
	 * GENERAL_SITE_ADMINISTRATION String general site administration permission.
	 */
	String GENERAL_SITE_ADMINISTRATION = "GENERAL_SITE_ADMINISTRATION";
	/**
	 */
	String GENERAL_ADMINISTRATION = "GENERAL_ADMINISTRATION";
	/**
	 * SHIPMENT_PROCESSING String shipment processing permission.
	 */
	String SHIPMENT_PROCESSING = "SHIPMENT_PROCESSING";
	/**
	 * EXECUTE_QUERY.
	 */
	String EXECUTE_QUERY = "EXECUTE_QUERY";
}
