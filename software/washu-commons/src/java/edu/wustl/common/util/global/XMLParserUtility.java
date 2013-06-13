/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.global;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class has utility methods to parse xml file.
 * @author ravi_kumar
 *
 */
public final class XMLParserUtility
{

	/**
	 * private constructor.
	 */
	private XMLParserUtility()
	{

	}

	/**
	 * This method return role name from xml file.
	 * @param element String- privilege Element
	 * @param elementName -Element name for which value has to be return
	 * @return String Role name
	 */
	public static String getElementValue(Element element, String elementName)
	{
		String roleName="";
		NodeList elementList = element.getElementsByTagName(elementName);
		Element ele = (Element) elementList.item(0);
		NodeList valueNodeList = ele.getChildNodes();
		Node node=((Node) valueNodeList.item(0));
		if(node!=null)
		{
			roleName = node.getNodeValue();
		}
		return roleName;
	}

	/**
	 * This method returns the Document object for xml file.
	 * @param fileName File name.
	 * @return Document xml document.
	 * @throws ParserConfigurationException throws this exception if DocumentBuilderFactory not created.
	 * @throws IOException throws this exception if file not found.
	 * @throws SAXException throws this exception if not able to parse xml file.
	 */
	public static Document getDocument(String fileName) throws ParserConfigurationException,
			SAXException, IOException
	{
		File file = new File(fileName);
		DocumentBuilder documentBuilder = getDocumentBuilder();
		return documentBuilder.parse(file);
	}

	/**
	 * This method returns the Document object for input stream.
	 * @param inputStream InputStream of xml file .
	 * @return Document object for input stream.
	 * @throws ParserConfigurationException throws this exception if DocumentBuilderFactory not created.
	 * @throws IOException throws this exception if file not found.
	 * @throws SAXException throws this exception if not able to parse xml file.
	 */
	public static Document getDocument(InputStream inputStream)
			throws ParserConfigurationException, SAXException, IOException
	{

		DocumentBuilder documentBuilder = getDocumentBuilder();
		return documentBuilder.parse(inputStream);
	}

	/**
	 * @return DocumentBuilder object
	 * @throws ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
	 */
	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		return dbf.newDocumentBuilder();
	}
}
