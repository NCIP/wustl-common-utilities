
package edu.wustl.security.locator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.global.CSMGroup;

/**
 * It return class PG Name,PI GroupName or Coordinator GroupName.
 * @author ravi_kumar
 *
 */
public final class CSMGroupLocator
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(CSMGroupLocator.class);

	/**
	 * File name for CSM group configuration.
	 */ 
	private static final String CSM_GROUP_CONF_FILE = "CSMGroup.xml";

	/**
	 * Specifies parse Exception Message.
	 */
	private static String parseExcepMessage;
	/**
	 * This indicates whether instants has been created successfully or not.
	 */
	private static boolean success = true;
	/**
	 * object of CSMGroupLocator.
	 */
	private static CSMGroupLocator csmGroupLocatr = new CSMGroupLocator();
	/**
	 * Map of bit-number and Privilege Object.
	 */
	private Map<Class, CSMGroup> classVsCsmGroupMap;

	/**
	 * Element name for 'csm-group'.
	 */
	private static final String ELE_CSM_GROUP = "csm-group";

	/**
	 * Element name for 'pg-name'.
	 */
	private static final String ELE_PG_NAME = "pg-name";

	/**
	 * Element name for 'pi-group-name'.
	 */
	private static final String ELE_PI_GROUP_NAME = "pi-group-name";

	/**
	 * Element name for 'coordinator-group-name'.
	 */
	private static final String ELE_COORDINATOR_GROUP_NAME = "coordinator-group-name";

	/**
	 * Attribute name for 'class-name'.
	 */
	private static final String ATTR_CLASS_NAME = "class-name";

	/**
	 * No argument constructor.
	 */
	private CSMGroupLocator()
	{
		try
		{
			init();
		}
		catch (Exception exception)
		{
			success = false;
			parseExcepMessage = exception.getMessage();
			logger.error("can not load configuration file " + exception.getMessage(), exception);
		}
	}

	/**
	 *
	 * @return object of CSMGroupLocator.
	 * @throws ApplicationException if not able to parse the file.
	 */
	public static CSMGroupLocator getInstance() throws ApplicationException
	{
		if (!success)
		{
			logger.error("Can not create instance, Please see the log file for details.");
			throw new ApplicationException(ErrorKey.getErrorKey("csm.getinstance.error"), null,
					parseExcepMessage);
		}
		return csmGroupLocatr;
	}

	/**
	 * @param identifier identifier
	 * @param className Class Name
	 * @return PG name
	 */
	public String getPGName(Long identifier, Class className)
	{
		return classVsCsmGroupMap.get(className).getPgName() + identifier;
	}

	/**
	 * @param identifier identifier
	 * @param className Class Name
	 * @return PI group Name
	 */
	public String getPIGroupName(Long identifier, Class className)
	{
		return classVsCsmGroupMap.get(className).getPiGroupName() + identifier;
	}

	/**
	 * @param identifier identifier
	 * @param className Class Name
	 * @return Coordinator Group Name
	 */
	public String getCoordinatorGroupName(Long identifier, Class className)
	{
		return classVsCsmGroupMap.get(className).getCoGroupName() + identifier;
	}

	/**
	 * This method load the Privileges into map.
	 * @throws ApplicationException throws this exception if
	 * specified xml file not found or not able to parse the file.
	 */
	public void init() throws ApplicationException
	{
		try
		{
			InputStream inputStream = CSMGroupLocator.class.getClassLoader()
			.getResourceAsStream(CSM_GROUP_CONF_FILE);
			Document doc = XMLParserUtility.getDocument(inputStream);
			NodeList csmGrNodeLst = doc.getElementsByTagName(ELE_CSM_GROUP);
			populateMaps(csmGrNodeLst);
		}
		catch (Exception exception)
		{
			logger.error(exception.getMessage(), exception);
			throw new ApplicationException(ErrorKey.getErrorKey("csm.getinstance.error"), exception,
					parseExcepMessage);
		}
	}

	/**
	 * @param csmGrNodeLst this method populate xml data to maps.
	 * @throws ClassNotFoundException If Class not found
	 */
	private void populateMaps(NodeList csmGrNodeLst) throws ClassNotFoundException
	{
		Node csmGrNode;
		classVsCsmGroupMap = new HashMap<Class, CSMGroup>();
		for (int s = 0; s < csmGrNodeLst.getLength(); s++)
		{
			csmGrNode = csmGrNodeLst.item(s);
			if (csmGrNode.getNodeType() == Node.ELEMENT_NODE)
			{
				addNewCsmGroupToMap(csmGrNode);
			}
		}
	}

	/**
	 * @param csmGrNode Node- xml privilege node
	 * @throws ClassNotFoundException If Class not found
	 */
	private void addNewCsmGroupToMap(Node csmGrNode) throws ClassNotFoundException
	{
		String csmGrClassName;
		String pgName;
		String piGroupName;
		String coGroupName;
		CSMGroup csmGroup;
		Element csmGrElmnt = (Element) csmGrNode;
		csmGrClassName = csmGrElmnt.getAttribute(ATTR_CLASS_NAME);
		Class clazz = Class.forName(csmGrClassName);
		pgName = XMLParserUtility.getElementValue(csmGrElmnt, ELE_PG_NAME);
		piGroupName = XMLParserUtility.getElementValue(csmGrElmnt, ELE_PI_GROUP_NAME);
		coGroupName = XMLParserUtility.getElementValue(csmGrElmnt, ELE_COORDINATOR_GROUP_NAME);
		csmGroup = new CSMGroup(pgName, piGroupName, coGroupName);
		classVsCsmGroupMap.put(clazz, csmGroup);
	}
}
