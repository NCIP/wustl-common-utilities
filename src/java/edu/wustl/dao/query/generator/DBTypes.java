/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao.query.generator;

/**
 * <P>The class that defines the constants that are used to identify generic
 * SQL types, called JDBC types.
 * The actual type constant values are equivalent to those in XOPEN.
 * <p>
 * This class is never instantiated.
 */
 public final class DBTypes
 {

	 /**
	 * Private constructor.
	 */
	private DBTypes()
	{

	}
/**
  * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
 * <code>BIT</code>.
 */
 public static final  int BIT = 0;

 /**
 *	<P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
 * <code>TINYINT</code>.
 */
    public  static final  int TINYINT = 1;

 /**
  * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
  * <code>SMALLINT</code>.
  */
   public static final  int SMALLINT = 2;

 /**
 * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>INTEGER</code>.
  */
    public static final  int INTEGER = 3;

 /**
 * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>BIGINT</code>.
 */
    public static final  int BIGINT = 4;

 /**
 * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>FLOAT</code>.
 */
   public static final  int FLOAT = 5;

/**
 * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>REAL</code>.
*/
   public static final  int REAL = 6;

   /**
 * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
  * <code>DOUBLE</code>.
  */
    public static final  int DOUBLE = 7;

    /**
* <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>NUMERIC</code>.
 */
    public static final  int NUMBER = 8;

/**
* <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>DECIMAL</code>.
  */
    public static final  int DECIMAL = 9;

 /**
* <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
  * <code>CHAR</code>.
  */
    public static final  int CHAR = 10;

 /**
 * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
 * <code>VARCHAR</code>.
  */
     public static final  int VARCHAR = 11;

 /**
  * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
  * <code>LONGVARCHAR</code>.
  */
     public static final  int LONGVARCHAR = 12;

     /**
  * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
  * <code>DATE</code>.
  */
    public static final  int DATE = 13;

 /**
 * <P>The constant in the Java programming language, sometimes referred
  * to as a type code, that identifies the generic SQL type
 * <code>TIME</code>.
  */
    public static final  int TIME = 14;

 /**
 * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
  * <code>TIMESTAMP</code>.
  */
     public static final  int TIMESTAMP = 15;


/**
  * <P>The constant in the Java programming language, sometimes referred
 * to as a type code, that identifies the generic SQL type
 * <code>BINARY</code>.
  */
    public static final  int BINARY = 16;


    /**
      * The constant in the Java programming language, sometimes referred to
    * as a type code, that identifies the generic SQL type
     * <code>DISTINCT</code>.
       */
         public static final  int DISTINCT = 17;

   /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>BLOB</code>.
     */
        public static final  int BLOB = 18;

    /**
      * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
    * <code>CLOB</code>.
     */
         public static final  int CLOB = 19;


    /**
      * The constant in the Java programming language, somtimes referred to
      * as a type code, that identifies the generic SQL type <code>BOOLEAN</code>.
      */
    public static final  int BOOLEAN = 20;

    /**
     * The constant in the Java programming language, somtimes referred to
     * as a type code, that identifies the generic SQL type <code>STRING</code>.
     */
    public static final  int STRING = 21;

    /**
     * The constant in the Java programming language.
     * LONG
     */
    public static final int LONG = 22;

}