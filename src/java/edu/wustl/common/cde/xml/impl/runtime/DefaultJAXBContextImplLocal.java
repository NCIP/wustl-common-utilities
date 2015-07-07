/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.cde.xml.impl.runtime;


import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import com.sun.tools.xjc.runtime.DefaultJAXBContextImpl;
import com.sun.tools.xjc.runtime.GrammarInfo;
import com.sun.tools.xjc.runtime.MarshallerImpl;
import com.sun.tools.xjc.runtime.UnmarshallerImpl;
import com.sun.tools.xjc.runtime.ValidatorImpl;
import com.sun.xml.bind.Messages;


/**
 * @author shrishail_kalshetty
 *
 */
public class DefaultJAXBContextImplLocal extends DefaultJAXBContextImpl
{
	 /**
     * This is the constructor used by javax.xml.bind.FactoryFinder which
     * bootstraps the RI.  It causes the construction of a JAXBContext that
     * contains a GrammarInfoFacade which is the union of all the generated
     * JAXBContextImpl objects on the contextPath.
     * @param contextPath String variable
     * @param classLoader ClassLoader object
     * @throws JAXBException throw JAXBException
     */
    public  DefaultJAXBContextImplLocal( String contextPath, ClassLoader classLoader )
    throws JAXBException
    {
    	super(contextPath,classLoader) ;
		// TODO Auto-generated constructor stub
    }
    /**
     * Create an instance of the specified Java content interface.
     *
     * @param javaContentInterface the Class object
     * @return an instance of the Java content interface
     * @exception JAXBException throw JAXBException
     */
    public Object newInstance( Class javaContentInterface )
        throws JAXBException
        {

        if( javaContentInterface == null )
        {
            throw new JAXBException( Messages.format( Messages.CI_NOT_NULL ) );
        }

        try
        {
            Class c = super.getGrammarInfo().getDefaultImplementation( javaContentInterface );
            if(c==null)
            {
                throw new JAXBException(
                    Messages.format( Messages.MISSING_INTERFACE, javaContentInterface ));
            }
            return c.newInstance();
        }
        catch( Exception e )
        {
            throw new JAXBException( e );
        }
    }
    /**
     * This constructor is used by the default no-argument constructor in the
     * generated JAXBContextImpl objects.  It is also used by the
     * bootstrapping constructor in this class.
     * @param grammarInfo GrammarInfo object
     */
    public DefaultJAXBContextImplLocal( GrammarInfo grammarInfo )
    {
    	super(grammarInfo);
		// TODO Auto-generated constructor stub
	}
    /**
     * Create a <CODE>Marshaller</CODE> object that can be used to convert a
     * java content-tree into XML data.
     *
     * @return a <CODE>Marshaller</CODE> object
     * @throws JAXBException if an error was encountered while creating the
     *                      <code>Marshaller</code> object
     */
    public Marshaller createMarshaller() throws JAXBException
    {
    	return new MarshallerImpl( this );
    }
    /**
     * Create an <CODE>Unmarshaller</CODE> object that can be used to convert XML
     * data into a java content-tree.
     *
     * @return an <CODE>Unmarshaller</CODE> object
     * @throws JAXBException if an error was encountered while creating the
     *                      <code>Unmarshaller</code> object
     */
    public Unmarshaller createUnmarshaller() throws JAXBException
    {
    	return new UnmarshallerImpl( this, getGrammarInfo() );
    }
    /**
     * Create a <CODE>Validator</CODE> object that can be used to validate a
     * java content-tree.
     *
     * @return an <CODE>Unmarshaller</CODE> object
     * @throws JAXBException if an error was encountered while creating the
     *                      <code>Validator</code> object
     */
    public Validator createValidator() throws JAXBException
    {
    	return new ValidatorImpl( this );
    }
}
