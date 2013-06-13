/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.cde.xml;

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * $Id: Main.java,v 1.1.18.4 2008/11/25 13:19:30 ravi_kumar Exp $
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 *
 */
public class Main
{
    /**
     * This sample application demonstrates how to unmarshal an instance
     * document into a Java content tree and access data contained within it.
     * @param args String parameter
     */
    public static void main( String[] args )
	{
       try
		{
            // create a JAXBContext capable of handling classes generated into
            // the pspl.cde package
            JAXBContext jaxbContext = JAXBContext.newInstance( "edu.wustl.common.cde.xml" );
            // create an Unmarshaller
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            // unmarshal a root instance document into a tree of Java content
            // objects composed of classes from the pspl.cde package.
            XMLCDECACHE root =
                (XMLCDECACHE)unMarshaller.unmarshal( new FileInputStream( "CDEConfig.xml" ) );
            // display the cde details
			java.util.List list1 = root.getXMLCDE();
			if (!list1.isEmpty())
			{
				for (int z =0; z<list1.size(); z++ )
				{
					XMLCDE cdeobj = (XMLCDE)list1.get(z);
					displayCde( cdeobj );

				}
			}

        }
		catch( JAXBException je )
		{
            je.printStackTrace();
        }
		catch( IOException ioe )
		{
            ioe.printStackTrace();
        }
  } // main
    /**
     *
     * @param cdeojb XMLCDE object
     */
	public static void displayCde( XMLCDE cdeojb )
	{
        // display the cdeojb

        System.out.println( "NAME : \t" + cdeojb.getName() ); // NOPMD
        System.out.println( "PUBLICID : \t" + cdeojb.getPublicId() ); // NOPMD
        // NOPMD
        System.out.println( "CACHE : \t" + cdeojb.isCache() ); // NOPMD
	    // NOPMD
		System.out.println( "LAZYLOADING : \t" + cdeojb.isLazyLoading() ); // NOPMD
		java.util.List list2 = cdeojb.getXMLPermissibleValues();
			if (!list2.isEmpty())
			{
	            XMLPermissibleValueType pvt = (XMLPermissibleValueType)list2.get(0);
		        displayPV(pvt);
			}
		//System.out.println( "\n\t----------------------------------------");
    } //displayCde
	/**
	 *
	 * @param pvt XMLPermissibleValueType object.
	 */
	public static void displayPV(XMLPermissibleValueType pvt)
	{
		System.out.println( "\tEVS Term: " + pvt.getEvsTerminology()); // NOPMD
		System.out.println( "\tConcept Code: " + pvt.getConceptCode()); // NOPMD
		System.out.println( "\tParentConcept Code: " + pvt.getParentConceptCode()); // NOPMD
		System.out.println( "\tTree Depth: " + pvt.getDepthOfHierarchyTree()); // NOPMD

	} // displayPV
} // class

// --------------------------------------------------------
/*
    public static void displayAddress( CDE cdeojb ) {
        // display the cdeojb
        System.out.println( "\t" + cdeojb.getName() );
        System.out.println( "\t" + cdeojb.getPublicId() );
    }
    public static void displayItems( PermissibleValueType pvt ) {
        // the items object contains a List of pspl.cde.ItemType objects
        List itemTypeList = items.getItem();
        // iterate over List
        for( Iterator iter = itemTypeList.iterator(); iter.hasNext(); ) {
            Items.ItemType item = (Items.ItemType)iter.next();
            System.out.println( "\t" + item.getQuantity() +
                                " copies of \"" + item.getProductName() +
                                "\"" );
        }
    }
*/
