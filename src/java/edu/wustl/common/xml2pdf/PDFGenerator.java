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
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@SuppressWarnings("serial")
public class PDFGenerator extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		String xml;
		xml = req.getParameter("grid_xml");
		xml = URLDecoder.decode(xml, "UTF-8");
		(new PDFWriter()).generate(xml, resp);
	}
}
