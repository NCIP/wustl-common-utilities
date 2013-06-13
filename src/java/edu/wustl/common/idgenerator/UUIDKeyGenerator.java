/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.idgenerator;

import java.util.UUID;


public class UUIDKeyGenerator implements IUniqueKeyGenerator
{
	public String getKey() 
    {
        return UUID.randomUUID().toString();
    }
}
