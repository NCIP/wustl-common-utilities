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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExcelCell {

	private String value = "";
	private String bgColor = "";
	private String textColor = "";
	private String bold = "";
	private String italic = "";
	private String align = "";

	public void parse(Node parent) {
		//(REV)we may have empty cell, which will return null for getFirstChild
		if(parent.getFirstChild()!=null){

		value = parent.getFirstChild().getNodeValue();
		Element el = (Element) parent;
		bgColor = (el.hasAttribute("bgColor")) ? el.getAttribute("bgColor") : "";
		textColor = (el.hasAttribute("textColor")) ? el.getAttribute("textColor") : "";
		bold = (el.hasAttribute("bold")) ? el.getAttribute("bold") : "";
		italic = (el.hasAttribute("italic")) ? el.getAttribute("italic") : "";
		align = (el.hasAttribute("align")) ? el.getAttribute("align") : "";
		}
	}

	public String getValue() {
		return value;
	}

	public String getBgColor() {
		return bgColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public Boolean getBold() { //(REV)why we store string , and not boolean?
		if (bold.equals("bold"))
			return true;
		else
			return false;
	}

	public Boolean getItalic() { //(REV)why we store string , and not boolean?
		if (italic.equals("italic"))
			return true;
		else
			return false;
	}

	public String getAlign() {
		return align;
	}

}