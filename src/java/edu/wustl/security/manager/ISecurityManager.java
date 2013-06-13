/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.manager;

import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.security.exception.SMException;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * Interface for SecurityManager.
 * @author deepti_shelar
 */
public interface ISecurityManager
{

	// UserProvisioningManager getUserProvisioningManager() throws CSException;
	/**
	 * login.
	 * @param loginName name
	 * @param password pwd
	 * @throws SMException ex
	 * @return boolean flag
	 */
	boolean login(String loginName, String password) throws SMException;
	/**
	 * @param user user
	 * @throws SMException ex
	 */
	void createUser(User user) throws SMException;
	/**
	 * @param loginName name
	 * @return user user
	 * @throws SMException ex
	 */
	User getUser(String loginName) throws SMException;
	/**
	 * @param userId string id
	 * @throws SMException ex
	 */
	void removeUser(String userId) throws SMException;
	/**
	 * @return List roles
	 * @throws SMException ex
	 */
	List<Role> getRoles() throws SMException;
	/**
	 * @param userID user id
	 * @param roleID id of the role
	 * @throws SMException ex
	 */
	void assignRoleToUser(String userID, String roleID) throws SMException;
	/**
	 * @param roleID id
	 * @return String
	 * @throws SMException e
	 */
	String getGroupIdForRole(String roleID) throws SMException;
	/**
	 * @param userID id
	 * @return Role role
	 * @throws SMException ex
	 */
	Role getUserRole(long userID) throws SMException;
	/**
	 * @param userID id
	 * @return String name
	 * @throws SMException ex
	 */
	String getRoleName(long userID) throws SMException;
	/**
	 * @param user user
	 * @throws SMException ex
	 */
	void modifyUser(User user) throws SMException;
	/**
	 * @param userId id
	 * @return User user
	 * @throws SMException ex
	 */
	User getUserById(String userId) throws SMException;
	/**
	 * @return List of users
	 * @throws SMException ex
	 */
	List<User> getUsers() throws SMException;
	/**
	 * @param userGroupname name
	 * @param userId id
	 * @throws SMException ex
	 */
	void removeUserFromGroup(String userGroupname, String userId) throws SMException;
	/**
	 * @param userGroupname name
	 * @param userId id
	 * @throws SMException ex
	 */
	void assignUserToGroup(String userGroupname, String userId) throws SMException;
	/**
	 * @param userId id
	 * @param groupIds ids
	 * @throws SMException ex
	 */
	void assignAdditionalGroupsToUser(String userId, String[] groupIds) throws SMException;
	/**
	 * @param obj obj
	 * @return String array
	 * @throws SMException ex
	 */
	String[] getProtectionGroupByName(AbstractDomainObject obj) throws SMException;
	/**
	 * @param obj obj
	 * @param String className
	 * @return String array
	 * @throws SMException ex
	 */
	String getProtectionGroupByName(AbstractDomainObject obj,String className) throws SMException;
	/**
	 * @param userGroupname name
	 * @return Group grp
	 * @throws SMException e
	 */
	Group getUserGroup(String userGroupname) throws SMException;
}
