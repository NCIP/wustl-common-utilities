/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.xml2excel;
import java.io.IOException;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CSVxml {
	private Document dom;
	private String profile;
	private NodeList header;
	private NodeList rows;
	private NodeList footer;
	private int headerPos;
	private int footerPos;
	private int rowsPos;

	private String[] text = null;
	private String[] label = null;

	Element pageData;
	private void parseXmlString(String xml_string){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			StringReader reader = new StringReader(xml_string);
			InputSource inputSource = new InputSource(reader);
			inputSource.setEncoding("UTF-8");
			dom = db.parse(inputSource);
			reader.close();
			Element root = ((Element)dom.getElementsByTagName("rows").item(0));
			//root.getElementsByTagName(arg0)
			//profile = dom.getDocumentElement().getAttribute("profile");
			profile = root.getAttribute("profile");
			if (profile == null) profile = "color";
			
			header = root.getElementsByTagName("head");
			if (header.getLength()>0){
				header = header.item(0).getChildNodes();
				header = removeSettings(header);
			}
			headerPos = 0;
			
			footer = root.getElementsByTagName("foot");
			if (footer.getLength()>0){
				footer = footer.item(0).getChildNodes();
				footer = removeSettings(footer);
			}
			footerPos = 0;
			
			rows = root.getElementsByTagName("row");
			rowsPos = 0;
			
			if(dom.getElementsByTagName("outerdata").getLength()>0 ){
				pageData = (Element)dom.getElementsByTagName("outerdata").item(0);
				extractOuterData();
			}
			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public CSVxml(String xml) {
		parseXmlString(xml);
	}
	
	private String[] getDataArray(Node node){
		NodeList columns = node.getChildNodes();
		String[] data = new String[columns.getLength()];
		for	(int i=columns.getLength()-1; i>=0; i--)
			data[i] = columns.item(i).getTextContent();
		
		return data;
	}
	public String[] getHeader(){
		if (header==null || header.getLength() <= headerPos) return null;
		Node node = header.item(headerPos);
		headerPos += 1;
		
		return getDataArray(node);
	}
	public String[] getFooter(){
		if (footer == null || footer.getLength() <= footerPos) return null;
		Node node = footer.item(footerPos);
		footerPos += 1;
		
		return getDataArray(node);
	}
	public String[] getRow(){
		if (rows == null || rows.getLength() <= rowsPos) return null;
		Node node = rows.item(rowsPos);
		rowsPos += 1;
		
		return getDataArray(node);
	}
	
	private NodeList removeSettings(NodeList header) {
		// remove settings sections
		for (int i = 0; i < header.getLength(); i++) {
			NodeList childs = header.item(i).getChildNodes();
			for (int j = 0; j < childs.getLength(); j++) {
				if (childs.item(j).getNodeType() == 3 || childs.item(j).getNodeName().equals("settings"))
					header.item(i).removeChild(childs.item(j));
			}
		}
		return header;
	}

	public String getProfile() {
		return profile;
	}
	
	public String[] getText(){
		return text;
	}
	
	public String[] getLabel(){
		return label;
	}
	
	private void extractOuterData(){
		NodeList nodeList = pageData.getChildNodes();
		text = new String[nodeList.getLength()];
		label = new String[nodeList.getLength()];
		for(int i = 0 ;i < nodeList.getLength(); i++){
			Element element = (Element) nodeList.item(i);
			text[i] = element.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
			label[i] = element.getElementsByTagName("label").item(0).getFirstChild().getNodeValue();
		}

	}
}
