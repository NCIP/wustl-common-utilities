/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.locator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * Locator for common interfaces.
 * @author deepti_shelar
 *
 */
public final class InterfaceLocator
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(InterfaceLocator.class);
	/**
	 * File name for interface configuration.
	 */
	private static final String INTERFACE_CONF_FILE="CommonInterfaces.xml";

	/**
	 * Element name for 'interface'.
	 */
	private static final String ELE_INTERFACE="interface";

	/**
	 * Attribute name for interface name.
	 */
	private static final String ATTR_NAME="name";

	/**
	 * Attribute name for className.
	 */
	private static final String ATTR_CLASS_NAME="className";

	/**
	 * Specifies interface Map.
	 */
	private Map<String,String> interfaceMap ;

	/**
	 * Specifies parse Exception Message.
	 */
	private static String parseExcepMessage;

	/**
	 * Specifies success.
	 */
	private static boolean success = false;
	/**
	 * private instance will be created when the class is loaded.
	 */
	private static InterfaceLocator locator = new InterfaceLocator();

	/**
	 * Making this class singleton.
	 */
	private InterfaceLocator()
	{
		try
		{
			init();
			success = true;
		}
		catch (ParseException exception)
		{
			parseExcepMessage = exception.getMessage();
			LOGGER.error(parseExcepMessage, exception);
		}
	}
	/**
	 * returning the same instance every time.
	 * @return InterfaceLocator
	 * @throws ApplicationException Application Exception.
	 */
	public static InterfaceLocator getInstance() throws ApplicationException
	{
		if (success)
		{
			return locator;
		}
		else
		{
			throw new ApplicationException(ErrorKey.getErrorKey("csm.getinstance.error"), null,
					parseExcepMessage);
		}
	}
	/**
	 * get Class Name For Interface.
	 * @param interfaceName interface Name.
	 * @return ClassNameForInterface
	 */
	public String getClassNameForInterface(String interfaceName)
	{
		return interfaceMap.get(interfaceName);
	}
	/**
	 * This method load the interfaces into map.
	 * @throws ParseException Parse Exception.
	 */
	private void init() throws ParseException
	{
		Document doc;
		try
		{
			doc = XMLParserUtility.getDocument(INTERFACE_CONF_FILE);
		}
		catch (ParserConfigurationException exception)
		{
			LOGGER.debug(exception.getMessage(), exception);
			throw new ParseException(exception);
		}
		catch (SAXException sException)
		{
			LOGGER.debug(sException.getMessage(), sException);
			throw new ParseException(sException);
		}
		catch (IOException iOException)
		{
			LOGGER.debug(iOException.getMessage(), iOException);
			throw new ParseException(iOException);
		}
		NodeList interfacesList = doc.getElementsByTagName(ELE_INTERFACE);
		populateMaps(interfacesList);
	}
	/**
	 * @param interfacesList this method populate xml data to maps.
	 */
	private void populateMaps(NodeList interfacesList)
	{
		Node interfaceNode;
		interfaceMap= new HashMap<String, String>();
		for (int s = 0; s < interfacesList.getLength(); s++)
		{
			interfaceNode = interfacesList.item(s);
			if (interfaceNode.getNodeType() == Node.ELEMENT_NODE)
			{
				addNewInterfaceToMap(interfaceNode);
			}
		}
	}
	/**
	 * @param interfaceNode Node- xml interface node
	 */
	private void addNewInterfaceToMap(Node interfaceNode)
	{
		String name;
		String className;
		Element interfaceElem = (Element) interfaceNode;
		name = interfaceElem.getAttribute(ATTR_NAME);
		className = interfaceElem.getAttribute(ATTR_CLASS_NAME);
		interfaceMap.put(name,className);
	}
}