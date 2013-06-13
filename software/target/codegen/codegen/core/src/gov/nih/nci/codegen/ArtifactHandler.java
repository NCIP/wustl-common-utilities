/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.codegen;


/**
 * Endpoint for the artifact is handled by the ArtifactHandler. Implementation of this interface will be responsible for
 * managing the artifact (e.g. persisting on the file system)
 *  
 * @author Satish Patel
 *
 */
public interface ArtifactHandler{

	public void handleArtifact(Artifact artifact) throws GenerationException;
	
}