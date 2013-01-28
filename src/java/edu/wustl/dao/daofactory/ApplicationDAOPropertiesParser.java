/**
 * This class is used to parse the ApplicationDAOProperties.xml
 * It populates Map daoFactoryMap.
 */

package edu.wustl.dao.daofactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DatabaseProperties;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author prashant_bandal
 *
 */
public class ApplicationDAOPropertiesParser
{


	/**
	 * Specifies application variables.
	 */
	private String defaultConnManager,jdbcConnManager,applicationName, daoFactoryName,
	defaultDaoName,isDefaultDAOFactory,	configFile, jdbcDAOName;

	/**
	 * Database specific properties.
	 */
	private String databaseType,dataSource,defaultBatchSize,datePattern,timePattern,
	dateFormatFunction,timeFormatFunction,dateTostrFunction,strTodateFunction,
	queryGeneratorName;

	private String daoConfigurationFileName;
	/**
	 *Class Logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(ApplicationDAOPropertiesParser.class);
	
	public ApplicationDAOPropertiesParser()
	{
	
	}
	public ApplicationDAOPropertiesParser(String daoConfigurationFileName)
	{
		this.daoConfigurationFileName = daoConfigurationFileName;
	}
	
	/**
	 * This method gets DAO Factory Map.
	 * @return DAO Factory Map.
	 * @throws DAOException database exception
	 */
	public Map<String, IDAOFactory> getDaoFactoryMap()throws DAOException
	{
		Map<String, IDAOFactory> daoFactoryMap = new HashMap<String, IDAOFactory>();
		try
		{
			String defaultDaoConfigFile = "ApplicationDAOProperties.xml";
			if(daoConfigurationFileName!=null)
			{
				defaultDaoConfigFile = daoConfigurationFileName; 
			}
			Document doc = readFile(defaultDaoConfigFile);
			parseDocument(daoFactoryMap,doc);
		}
		catch (Exception exception)
		{
			logger.error(exception.getMessage(), exception);
			throw DAOUtility.getInstance().getDAOException(exception,
					"db.app.prop.parsing.exp", "ApplicationDAOPropertiesParser.java ");
		}
		return daoFactoryMap;
	}

	/**
	 * This method parse XML File.
	 * @return document object.
	 * @param fileName : filename
	 * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private Document readFile(String fileName) throws ParserConfigurationException,
	SAXException, IOException
	{
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//Using factory get an instance of document builder
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
		InputStream inputStream = ApplicationDAOPropertiesParser.class.getClassLoader()
		.getResourceAsStream(fileName);
		return documentBuilder.parse(inputStream);

	}

	/**
	 * This method parse the document.
	 * @param daoFactoryMap this will hold the factory object as per the application.
	 * @param doc document instance.
	 * @throws InstantiationException Instantiation Exception
	 * @throws IllegalAccessException Illegal Access Exception
	 * @throws ClassNotFoundException Class Not Found Exception
	 * @throws DAOException :generic DAOException.
	 * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private void parseDocument(Map<String, IDAOFactory> daoFactoryMap,Document doc)
	throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DAOException,
			ParserConfigurationException, SAXException, IOException
	{
		//get the root element
		Element root = doc.getDocumentElement();
		NodeList rootChildren = root.getElementsByTagName("Application");
		for (int i = 0; i < rootChildren.getLength(); i++)
		{
			Node applicationChild = rootChildren.item(i);

			if (applicationChild.getNodeName().equals("Application"))
			{
				setApplicationProperties(applicationChild);
			}

			IDAOFactory daoFactory = (IDAOFactory) Class.forName(daoFactoryName).newInstance();
			DatabaseProperties databaseProperties = new DatabaseProperties();

			daoFactory.setApplicationName(applicationName);
			daoFactory.setIsDefaultDAOFactory(Boolean.parseBoolean(isDefaultDAOFactory));

			daoFactory.setDefaultDAOClassName(defaultDaoName);
			daoFactory.setConfigurationFile(configFile);
			daoFactory.setDefaultConnMangrName(defaultConnManager);

			daoFactory.setJdbcDAOClassName(jdbcDAOName);
			daoFactory.setJdbcConnMangrName(jdbcConnManager);

			daoFactory.buildSessionFactory();

			setDatabaseProperties(databaseProperties);
			daoFactory.setDatabaseProperties(databaseProperties);

			daoFactoryMap.put(daoFactory.getApplicationName(), daoFactory);
			resetApplicationProperties();
			databaseProperties = null;

		}

	}


	/**
	 * This method will be called to set all database specific properties like database name
	 * database source.
	 * @param databaseProperties : database properties
	 */
	private void setDatabaseProperties(DatabaseProperties databaseProperties)
	{
		databaseProperties.setDataBaseType(databaseType);
		databaseProperties.setDataSource(dataSource);
		databaseProperties.setDateFormatFunction(dateFormatFunction);
		databaseProperties.setDatePattern(datePattern);
		databaseProperties.setDateTostrFunction(dateTostrFunction);
		databaseProperties.setStrTodateFunction(strTodateFunction);
		databaseProperties.setTimeFormatFunction(timeFormatFunction);
		databaseProperties.setTimePattern(timePattern);
		databaseProperties.setQueryGeneratorName(queryGeneratorName);
		databaseProperties.setDefaultBatchSize(Integer.valueOf(defaultBatchSize));
	}

	/**
	 * This method resets Application Properties.
	 */
	private void resetApplicationProperties()
	{
		daoFactoryName = "";
		applicationName = "";
		isDefaultDAOFactory = "";

		defaultDaoName = "";
		configFile = "";
		defaultConnManager = "";

		jdbcDAOName = "";
		dataSource = "";
		jdbcConnManager = "";
		databaseType ="";
		datePattern = "";
		timePattern= "";
		dateFormatFunction= "";
		timeFormatFunction= "";
		dateTostrFunction= "";
		strTodateFunction= "";
		queryGeneratorName="";
		defaultBatchSize="";
	}

	/**
	 * This method sets Application Properties.
	 * @param applicationChild application Children.
	 * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private void setApplicationProperties(Node applicationChild) throws
	ParserConfigurationException, SAXException, IOException
	{
		NamedNodeMap attributeMap = applicationChild.getAttributes();
		applicationName = ((Node) attributeMap.item(0)).getNodeValue();

		NodeList applicationChildList = applicationChild.getChildNodes();

		for (int k = 0; k < applicationChildList.getLength(); k++)
		{
			Node applicationChildNode = applicationChildList.item(k);

			if (applicationChildNode.getNodeName().equals("DAOFactory"))
			{
				setDAOFactoryProperties(applicationChildNode);
			}
		}
	}

	/**
	 * This method sets DAOFactory Properties.
	 * @param childNode DAOFactory child Node.
	  * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private void setDAOFactoryProperties(Node childNode) throws
	ParserConfigurationException, SAXException, IOException
	{
		setAttributesOfDAOFactory(childNode);
		NodeList childlist = childNode.getChildNodes();
		for (int m = 0; m < childlist.getLength(); m++)
		{
			Node childrenDAOFactory = childlist.item(m);
			if (childrenDAOFactory.getNodeName().equals("DefaultDAO"))
			{
				setDefaultDAOProperties(childrenDAOFactory);
			}
			if (childrenDAOFactory.getNodeName().equals("JDBCDAO"))
			{
				setJDBCDAOProperties(childrenDAOFactory);
			}
		}

	}

	/**
	 * This method sets the DAO factor Attributes as name and
	 * default settings.
	 * @param childNode :
	 */
	private void setAttributesOfDAOFactory(Node childNode)
	{
		NamedNodeMap attrMap = childNode.getAttributes();
		for(int i =0 ; i < attrMap.getLength();i++)
		{
			Node daoFactoryPropertyNode = attrMap.item(i);
			if(daoFactoryPropertyNode.getNodeName().equals("default"))
			{
				isDefaultDAOFactory = daoFactoryPropertyNode.getNodeValue();
			}
			if(daoFactoryPropertyNode.getNodeName().equals("name"))
			{
				daoFactoryName = daoFactoryPropertyNode.getNodeValue();
			}
		}
	}

	/**
	 * This method sets JDBCDAO Properties.
	 * @param childrenDAOFactory children DAOFactory.
	 * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private void setJDBCDAOProperties(Node childrenDAOFactory) throws
	ParserConfigurationException, SAXException, IOException
	{
		NodeList childJDBCDAO = childrenDAOFactory.getChildNodes();
		for (int l = 0; l < childJDBCDAO.getLength(); l++)
		{
			Node childnode = childJDBCDAO.item(l);

			if (childnode.getNodeName().equals("DBPropertyFile"))
			{
				Node attNode = getNextnode(childnode);
				String dbPropertyFile = attNode.getNodeValue();
				parseDBPropFile(dbPropertyFile);

			}
		}

	}

	/**
	 * @param dbPropFile database property file.
	 * @throws ParserConfigurationException : parse exception
	 * @throws IOException IO exception
	 * @throws SAXException parser exception
	 */
	private  void parseDBPropFile(String dbPropFile) throws
	ParserConfigurationException, SAXException, IOException
	{

		Document doc = readFile(dbPropFile);
		Element root = doc.getDocumentElement();
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
		{
			Node childnode = list.item(i);
			if (childnode.getNodeName().equals("Class-name"))
			{
				Node attNode = getNextnode(childnode);
				jdbcDAOName = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DataSource"))
			{
				Node attNode = getNextnode(childnode);
				dataSource = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("JDBCConnectionManager"))
			{
				Node attNode = getNextnode(childnode);
				jdbcConnManager = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DatabaseType"))
			{
				Node attNode = getNextnode(childnode);
				databaseType = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DatePattern"))
			{
				Node attNode = getNextnode(childnode);
				datePattern = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("TimePattern"))
			{
				Node attNode = getNextnode(childnode);
				timePattern = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DateFormatFunction"))
			{
				Node attNode = getNextnode(childnode);
				dateFormatFunction = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("TimeFormatFunction"))
			{
				Node attNode = getNextnode(childnode);
				timeFormatFunction = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DateToStrFunction"))
			{
				Node attNode = getNextnode(childnode);
				dateTostrFunction = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("StrTodateFunction"))
			{
				Node attNode = getNextnode(childnode);
				strTodateFunction = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("QueryGenerator"))
			{
				Node attNode = getNextnode(childnode);
				queryGeneratorName = attNode.getNodeValue();
			}
			if(childnode.getNodeName().equals("DefaultBatchSize"))
			{
				Node attNode = getNextnode(childnode);
				defaultBatchSize = attNode.getNodeValue();
			}
		}


	}

	/**
	 * This will return the next node details.
	 * @param childnode :next node
	 * @return : next node
	 */
	private Node getNextnode(Node childnode)
	{
		NamedNodeMap defaultConnMangrMap = childnode.getAttributes();
		return defaultConnMangrMap.item(0);
	}

	/**
	 * This method sets Default DAO Properties.
	 * @param childrenDAOFactory children DAOFactory
	 * @throws DOMException
	 */
	private void setDefaultDAOProperties(Node childrenDAOFactory)
	{
		NodeList childDefaultDAO = childrenDAOFactory.getChildNodes();
		for (int l = 0; l < childDefaultDAO.getLength(); l++)
		{
			Node childnode = childDefaultDAO.item(l);

			if (childnode.getNodeName().equals("Class-name"))
			{
				Node attNode = getNextnode(childnode);
				defaultDaoName = attNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("Config-file"))
			{
				Node configFileMapNode = getNextnode(childnode);
				configFile = configFileMapNode.getNodeValue();
			}
			if (childnode.getNodeName().equals("DefaultConnectionManager"))
			{
				Node defaultConnMangrNode = getNextnode(childnode);
				defaultConnManager = defaultConnMangrNode.getNodeValue();
			}
		}
	}

	/*public static void main(String[] args)
	{
		Map<String, IDAOFactory> daoFactoryMap = new HashMap<String, IDAOFactory>();
		ApplicationDAOPropertiesParser parser = new ApplicationDAOPropertiesParser();
		daoFactoryMap = parser.getDaoFactoryMap();
		System.out.println(daoFactoryMap.size());

	}*/

}
