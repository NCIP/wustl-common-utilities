/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.xml2pdf;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PDFRow {

	private PDFCell[] cells;
	
	public void parse(Node parent, HttpServletResponse resp) throws IOException {
		NodeList nodes = ((Element) parent).getElementsByTagName("cell");
		Node text_node;
		if ((nodes != null)&&(nodes.getLength() > 0)) {
			cells = new PDFCell[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++) {
				PDFCell cell = new PDFCell();
				text_node = nodes.item(i);
				if (text_node != null)
					cell.parse(text_node, resp);
				cells[i] = cell;
			}
		}
	}
	
	public PDFCell[] getCells() {
		return cells;
	}
}
