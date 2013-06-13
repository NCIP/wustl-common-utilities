/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.security.privilege;

import edu.wustl.common.beans.SessionDataBean;
/**
 *
 */
public interface IValidator
{
	/**
	 *
	 * @param sessionDataBean sessionDataBean
	 * @param baseObjectId baseObjectId
	 * @param privilegeName privilegeName
	 * @return boolean flag
	 */
	boolean hasPrivilegeToView(SessionDataBean sessionDataBean, String baseObjectId,
			String privilegeName);
	/**
	 *
	 * @param sessionDataBean sessionDataBean
	 * @return boolean flag
	 */
	boolean hasPrivilegeToViewGlobalParticipant(SessionDataBean sessionDataBean);
	/**
	 *
	 * @param tqColumnMetadataList tq columns
	 * @param row row
	 * @param isAuthorizedUser boolean
	 * @return boolean
	 */
	/*boolean hasPrivilegeToViewTemporalColumn(List tqColumnMetadataList,List<String> row,
			boolean isAuthorizedUser);*/
}
