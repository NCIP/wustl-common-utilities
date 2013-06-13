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

package edu.wustl.common.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author prashant_bandal
 *
 */
public final class AbstractFactoryConfig
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(AbstractFactoryConfig.class);

	/**
	 * File name for Factory configuration.
	 */
	private static final String FACTORY_CONF_FILE = "Factory.xml";
	/**
	 * Specifies success.
	 */
	private static boolean success = true;

	/**
	 * Specifies ControlConfigurationsFactory instance.
	 */
	private static AbstractFactoryConfig configFactory = new AbstractFactoryConfig();

	/**
	 * Specifies dataType Configuration Map.
	 */
	private final Map<String, FactoryConfigObject> factoryConfigurationMap;

	/**
	 * Specifies Document object.
	 */
	private Document dom;

	/**
	 * Specifies parse Exception Message.
	 */
	private static String parseExcepMessage;

	/**
	 * ControlConfigurationsFactory constructor.
	 */
	private AbstractFactoryConfig()
	{
		factoryConfigurationMap = new HashMap<String, FactoryConfigObject>();

		try
		{
			parseXML(FACTORY_CONF_FILE);
		}
		catch (ParseException exception)
		{
			success = false;
			parseExcepMessage = exception.getMessage();
			LOGGER.error(parseExcepMessage, exception);
		}

	}

	/**
	 * This method gets ControlConfigurationsFactory Instance.
	 * @return ControlConfigurationsFactory instance.
	 * @throws BizLogicException BizLogic Exception.
	 */
	public static AbstractFactoryConfig getInstance() throws BizLogicException
	{
		if (success)
		{
			return configFactory;
		}
		else
		{
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), null,
					parseExcepMessage);
		}
	}

	/**
	 * This method parse xml File.
	 * @param xmlFile xml File
	 * @throws ParseException Parse Exception.
	 */
	private void parseXML(String xmlFile) throws ParseException
	{

		InputStream inputStream = Utility.getCurrClassLoader().getResourceAsStream(xmlFile);
		try
		{
			dom = XMLParserUtility.getDocument(inputStream);
			parseDocument();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new ParseException(exception);
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (IOException exception)
			{
				LOGGER.error("Not able to close input stream", exception);
			}
		}
	}

	/**
	 * This method parse document.
	 */
	private void parseDocument()
	{
		//get the root elememt
		Element root = dom.getDocumentElement();

		NodeList factoryList = root.getChildNodes();
		//int noOfDataTypes = validationDataTypeList.getLength();
		Node factoryNode = null;

		//String dataTypeName = null;
		for (int i = 0; i < factoryList.getLength(); i++)
		{
			factoryNode = factoryList.item(i);
			if (factoryNode != null)
			{
				insertInToMap(factoryNode);
			}
		}
	}

	/**
	This method inserts dataType Name and DataTypeConfigObject into Map.
	 * @param validationDataTypeNode validation Data Type Node.
	 * @throws DOMException
	 */
	private void insertInToMap(Node validationDataTypeNode)
	{
		Node factoryNameNode;
		Node classNode;
		NamedNodeMap dataTypeAttributes;
		String factoryName = null;
		FactoryConfigObject confObject = new FactoryConfigObject();
		dataTypeAttributes = validationDataTypeNode.getAttributes();
		if (dataTypeAttributes != null)
		{
			factoryNameNode = dataTypeAttributes.getNamedItem("name");
			classNode = dataTypeAttributes.getNamedItem("className");
			if (factoryNameNode != null && classNode != null)
			{
				factoryName = factoryNameNode.getNodeValue();
				confObject.setFactoryName(factoryName);
				confObject.setFactoryClassName(classNode.getNodeValue());
			}
		}
		if (factoryName != null)
		{
			factoryConfigurationMap.put(factoryName, confObject);
		}
	}

	/**
	 * This method gets BizLogic Factory Instance.
	 * @return dataTypeInterface
	 * @throws BizLogicException BizLogic Exception.
	 */
	public IFactory getBizLogicFactory() throws BizLogicException
	{
		try
		{
			String className = getFactClassName("bizLogicFactory");
			Class<IFactory> factoryClass = (Class<IFactory>) Class.forName(className);
			return factoryClass.newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), exception,
					"");
		}
	}

	/**
	 * This method gets factory class name.
	 * @param factoryName factory Name.
	 * @return class Name.
	 */
	private String getFactClassName(String factoryName)
	{
		FactoryConfigObject config = factoryConfigurationMap.get(factoryName);
		return config.getFactoryClassName();
	}

	/**
	 * get Forward To Factory.
	 * @return IForwordToFactory
	 * @throws BizLogicException BizLogicException.
	 */
	public IForwordToFactory getForwToFactory() throws BizLogicException
	{
		try
		{
			String className = getFactClassName("forwardToFactory");
			Class<IForwordToFactory> factoryClass = (Class<IForwordToFactory>) Class
					.forName(className);
			return factoryClass.newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), exception,
					"");
		}

	}

	/**
	 * get Domain Object Factory.
	 * @return IDomainObjectFactory
	 * @throws BizLogicException BizLogicException.
	 */
	public IDomainObjectFactory getDomainObjectFactory() throws BizLogicException
	{
		try
		{
			String className = getFactClassName("domainObjectFactory");
			Class<IDomainObjectFactory> factoryClass = (Class<IDomainObjectFactory>) Class
					.forName(className);
			return factoryClass.newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), exception,
					"");
		}

	}
	
	/**
	 * get Transform Object Factory.
	 * @return ITransformerFactory
	 * @throws BizLogicException BizLogicException.
	 */
	public ITransformerFactory getTransformFactory() throws BizLogicException
	{
		try
		{
			String className = getFactClassName("transformerFactory");
			Class<ITransformerFactory> factoryClass = (Class<ITransformerFactory>) Class
					.forName(className);
			return factoryClass.newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), exception,
					"");
		}

	}
	
	/**
	 * get Transform Object Factory.
	 * @return ITransformerFactory
	 * @throws BizLogicException BizLogicException.
	 */
	public IDataValidatorFactory getDataValidatorFactory() throws BizLogicException
	{
		try
		{
			String className = getFactClassName("dataValidatorFactory");
			Class<IDataValidatorFactory> factoryClass = (Class<IDataValidatorFactory>) Class
					.forName(className);
			return factoryClass.newInstance();
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			throw new BizLogicException(ErrorKey.getErrorKey("biz.getinstance.error"), exception,
					"");
		}

	}
}
