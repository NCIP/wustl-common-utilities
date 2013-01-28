package edu.wustl.security.locator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.security.privilege.Privilege;

/**
 * This class loada all privileges from xml file.
 * @author ravi_kumar
 */
public final class PrivilegeLocator
{
	/**
	 * locator obj.
	 */
	private static PrivilegeLocator locator = new PrivilegeLocator();
	/**
	 * private constructor.
	 */
	private PrivilegeLocator()
	{
		init();
	}
	/**
	 * returns the single obj.
	 * @return {@link PrivilegeLocator} obj
	 */
	public static PrivilegeLocator getInstance()
	{
		return locator;
	}

	/**
	 * File name for privilege configuration.
	 */
	private static final String PRIV_CONF_FILE = "PrivilegeConf.xml";

	/**
	 * Element name for 'privilege'.
	 */
	private static final String ELE_PRIVILEGE = "privilege";

	/**
	 * Element name for 'bit-number'.
	 */
	private static final String ELE_BIT_NUM = "bit-number";

	/**
	 * Attribute name for 'name'.
	 */
	private static final String ATTR_NAME = "name";

	/**
	 * Map of privilege Name and Privilege Object.
	 */
	private static Map<String, Privilege> namePrivMap;

	/**
	 * Map of bit-number and Privilege Object.
	 */
	private static Map<Integer, Privilege> bitPrivMap;

	/**
	 * This method returns Privilege object by privilege name.
	 * @param name Privilege Name
	 * @return Privilege object.
	 */
	public Privilege getPrivilegeByName(final String name)
	{
		return namePrivMap.get(name);
	}

	/**
	 * This method returns Privilege object by bit number.
	 * @param bit bit number associated with privilege.
	 * @return Privilege object.
	 */
	public Privilege getPrivilegeByBit(final Integer bit)
	{
		return bitPrivMap.get(bit);
	}

	/**
	 * This method load the Privileges into map.
	 */
	public void init()
	{
		try {
		InputStream inputStream = PrivilegeLocator.class.getClassLoader()
		.getResourceAsStream(PRIV_CONF_FILE);
		Document doc;
	
			doc = XMLParserUtility.getDocument(inputStream);
		
		NodeList privNodeLst = doc.getElementsByTagName(ELE_PRIVILEGE);
		populateMaps(privNodeLst);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param privNodeLst this method populate xml data to maps.
	 */
	private void populateMaps(final NodeList privNodeLst)
	{
		Node privNode;
		namePrivMap = new HashMap<String, Privilege>();
		bitPrivMap = new HashMap<Integer, Privilege>();
		for (int s = 0; s < privNodeLst.getLength(); s++)
		{
			privNode = privNodeLst.item(s);
			if (privNode.getNodeType() == Node.ELEMENT_NODE)
			{
				addNewPrivilegeToMap(privNode);
			}
		}
	}

	/**
	 * @param privNode Node- xml privilege node
	 */
	private void addNewPrivilegeToMap(final Node privNode)
	{
		String privName;
		int bitNumber;
		Privilege privilege;
		Element privElmnt = (Element) privNode;
		privName = privElmnt.getAttribute(ATTR_NAME);
		bitNumber = Integer.parseInt(XMLParserUtility.getElementValue(privElmnt, ELE_BIT_NUM));
		privilege = new Privilege(privName, bitNumber);
		namePrivMap.put(privName, privilege);
		bitPrivMap.put(bitNumber, privilege);
	}
}
