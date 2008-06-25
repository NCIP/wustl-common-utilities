package edu.wustl.common.util;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an enumeration formed from primitive <tt>enum</tt>s. In the
 * following description, the word <tt>enum</tt> refers to java's <b><tt>enum</tt></b>
 * type. <br>
 * A compound enum (<tt>compoundEnum</tt>)logically contains a list of
 * <tt>enum</tt> values that may belong to different <tt>enum</tt>s, but
 * are, in some context, meaningful when grouped together. For example, there
 * can exist two <tt>enum</tt>s called <tt>NumericTypes</tt> and
 * <tt>DateTypes</tt>; but we may need to group together these enumerated
 * values under a <tt>compoundEnum</tt> called <tt>AllDataTypes</tt>. Each
 * instance (value) of a <tt>compoundEnum</tt> has a corresponding
 * <tt>enum</tt> value and an ordinal in the context of this
 * <tt>compoundEnum</tt>.
 * <p>
 * It is recommended that the <tt>enum</tt>s that have some logical relation
 * share a (marker) interface. For example, we could have an interface
 * <tt>IDataType</tt> that is implemented by <tt>NumericTypes</tt> and
 * <tt>DateTypes</tt>. We can the declare the
 * <tt>compoundEnum AllDataTypes</tt> as
 * 
 * <pre>
 * public class AllDataTypes&lt;T extends Enum&lt;?&gt; &amp; IDataType&gt;
 *   extends CompoundEnum&lt;AllDataTypes&lt;T&gt;, T&gt;
 * </pre>
 * 
 * <br>
 * It is mandatory that a concrete <tt>compoundEnum</tt> implements static
 * methods with the following signature:
 * 
 * <pre>
 * public static &lt;T extends Enum&lt;?&gt;&gt; ConcreteCompoundEnum&lt;T&gt; compoundEnum(T primitiveEnum);
 * 
 * public static IDataType[] values();
 * </pre>
 * 
 * In the above example, since the data types share an interface, we can instead
 * define the following methods in <tt>AllDataTypes</tt>
 * 
 * <pre>
 * public static &lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; AllDataTypes&lt;T&gt; compoundEnum(T primitiveEnum);
 * 
 * public static IDataType[] values();
 * </pre>
 * 
 * <p>
 * <b>Compound enum implementation:</b><br>
 * Following is an example implementation of <tt>AllDataTypes</tt>:<br>
 * 
 * <pre>
 * // type params specify that the compound enum is of type AllDataTypes and that
 * // the primitive enum must be of type IDataType
 * 
 * public class AllDataTypes&lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; extends CompoundEnum&lt;AllDataTypes&lt;T&gt;, T&gt; {
 *  private static final long serialVersionUID = 7794336702657368626L;
 * 
 *  // list of the primitive enums.
 *  private static final List&lt;IDataType&gt; values = new ArrayList&lt;IDataType&gt;();
 * 
 *  // list of compound enums; ordering is same as the &quot;values&quot; list
 *  private static final List&lt;AllDataTypes&lt;?&gt;&gt; compoundValues = new ArrayList&lt;AllDataTypes&lt;?&gt;&gt;();
 * 
 *  // ordinal tracker
 *  private static int nextOrdinal = 1;
 * 
 *  // STATIC INIT
 *  static {
 *  // add all values of the following enums in this compound enum
 *      addEnums(NumericTypes.class);
 *      addEnums(DateTypes.class);
 *  }
 * 
 *  // add all enum values of the specified enum to the global lists
 *  private static &lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; void addEnums(Class&lt;T&gt; klass) {
 *      for (T e : klass.getEnumConstants()) {
 *          values.add(e);
 *          compoundValues.add(newCompoundEnum(e));
 *      }
 *  }
 * 
 *  // creates a compound enum corresponding the specified primitive enum
 *  private static &lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; AllDataTypes&lt;T&gt; newCompoundEnum(T e) {
 *      AllDataTypes&lt;T&gt; compoundEnum = new AllDataTypes&lt;T&gt;(e, nextOrdinal());
 *      return compoundEnum;
 *  }
 * 
 *  // creates a unique ordinal for the compound enum value
 *  private static int nextOrdinal() {
 *      return nextOrdinal++;
 *  }
 *  // END STATIC INIT
 * 
 *  // returns the compound enum corresponding to the specified primitiveEnum
 *  public static &lt;T extends Enum&lt;?&gt; &amp; IDataType&gt; AllDataTypes&lt;T&gt; compoundEnum(T primitiveEnum) {
 *      if (primitiveEnum == null) {
 *        return null;
 *      }
 *      int index = values.indexOf(primitiveEnum);
 *      return (AllDataTypes&lt;T&gt;) compoundValues.get(index);
 *  }
 * 
 *  // all the primitive enum values in this compound enum
 *  public static IDataType[] values() {
 *      return values.toArray(new IDataType[0]);
 *  }
 * 
 *  // private constructor because this is an enum
 *  private AllDataTypes(T primitiveEnum, int ordinal) {
 *      super(primitiveEnum, ordinal);
 *  }
 * 
 *  // serialization; ensure unique instance
 *  private Object readResolve() throws ObjectStreamException {
 *      return compoundValues.get(ordinal());
 *  }
 * </pre>
 * 
 * @author srinath_k
 * 
 * @param <E> the specific type of the compound enum; should be same as that of
 *            the compound enum.
 * @param <T> the type of primitive enum that the compound enum corresponds to.
 */
public abstract class CompoundEnum<E extends CompoundEnum<E, T>, T extends Enum<?>>
        implements
            Comparable<E>,
            Serializable {
    private T primitiveEnum;

    private int ordinal;

    protected CompoundEnum(T primitiveEnum, int ordinal) {
        this.primitiveEnum = primitiveEnum;
        this.ordinal = ordinal;
    }

    public T primitiveEnum() {
        return primitiveEnum;
    }

    public int ordinal() {
        return ordinal;
    }

    public final int compareTo(E o) {
        return ordinal - o.ordinal;
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return primitiveEnum.toString();
    }
}
