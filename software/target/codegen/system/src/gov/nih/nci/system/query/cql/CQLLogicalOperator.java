/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package gov.nih.nci.system.query.cql;

/**
 * @author Satish Patel
 *
 */
public class CQLLogicalOperator implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    private String value;

    // Constructor
    private CQLLogicalOperator(String value) {
        this.value = value;
    }

    public static final CQLLogicalOperator AND = new CQLLogicalOperator("AND");
    public static final CQLLogicalOperator OR = new CQLLogicalOperator("OR");
    
    public String getValue() { return value;}

    public boolean equals(Object obj) {
    	if(obj!=null && obj instanceof CQLLogicalOperator)
    		return ((CQLLogicalOperator)obj).getValue().equals(value);
    	else
    		return false;
    }
    public int hashCode() { return toString().hashCode();}
    public String toString() { return value;}

}
