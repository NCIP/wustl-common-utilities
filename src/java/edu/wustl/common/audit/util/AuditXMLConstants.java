package edu.wustl.common.audit.util;

/**
 * @author kunal_kamble.
 *
 */
public class AuditXMLConstants
{

	public static final String CLASS_NAME_TOKEN = "@@className@@";
	public static final String ROLE_NAME_TOKEN = "@@roleName@@";
	public static final String RELATIONSHIP_TYPE_TOKEN = "@@relationShipType@@";
	public static final String AUDIT_CLASS_TAG = "<AuditableClass className=\"@@className@@\" "
			+ "relationShipType=\"@@relationShipType@@\" roleName=\"@@roleName@@\">";

	public static final String NAME_TOKEN = "@@name@@";
	public static final String DATA_TYPE_TOKEN = "@@dataType@@";
	public static final String ATTRIBUTE_TAG = "<attribute name=\"@@name@@\" dataType=\"@@dataType@@\" />";

	public static final String CONTAINMENT_ASSOCIATION_COLLECTION_TAG = "<containmentAssociationCollection "
			+ "className=\"@@className@@\" relationShipType=\"@@relationShipType@@\" "
			+ "roleName=\"@@roleName@@\"/>";

	public static final String REFERENCE_ASSOCIATION_COLLECTION_TAG = "<referenceAssociationCollection "
			+ "className=\"@@className@@\" relationShipType=\"@relationShipType@\" "
			+ "roleName=\"@@roleName@@\"/>";

}
