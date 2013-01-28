/**
 * 
 */
package edu.wustl.common.cde.xml.impl.runtime;

import javax.xml.bind.JAXBException;

import com.sun.tools.xjc.runtime.GrammarInfo;
import com.sun.tools.xjc.runtime.UnmarshallerImpl;

/**
 * @author shrishail_kalshetty
 *
 */
public class UnmarshallerImplLocal extends UnmarshallerImpl
{
	/**
	 * DefaultJAXBContextImplLocal object.
	 */
	private DefaultJAXBContextImplLocal jaxbContext = null ;
	/**
	 *
	 * @param jaxbContext Local DefaultJAXBContextImplLocal object
	 * @param grammarInfo GrammarInfo object
	 */
	public UnmarshallerImplLocal(DefaultJAXBContextImplLocal jaxbContext, GrammarInfo grammarInfo )
	{
		super(jaxbContext,grammarInfo) ;
		this.jaxbContext = jaxbContext ;
	}
	/**
     * @param validating boolean variable
     * @throws JAXBException throw JAXBException
     */
    public void setValidating(boolean validating) throws JAXBException
    {
    	super.setValidating(validating);
    	if(validating)
    	{
    		// make sure that we can actually load the grammar.
    		// this could be a lengthy operation if your schema is big.
    		jaxbContext.getGrammar();
    	}
    }
}
