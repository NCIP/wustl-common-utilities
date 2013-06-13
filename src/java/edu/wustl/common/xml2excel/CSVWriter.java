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
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class CSVWriter extends BaseWriter {
	int rows = 0;
	int cols = 0;
	String watermark = null;
	
	public void generate(String xml,String fileName, HttpServletResponse resp) throws IOException {
		CSVxml data = new CSVxml(xml);
		
		resp.setContentType("application/download");
//		resp.setCharacterEncoding("UTF-8");
		if(fileName==null || fileName.trim().equals("")){
			fileName = "fileName";
		}
		resp.setHeader("Content-Disposition", "attachment;filename="+fileName);
		resp.setHeader("Cache-Control", "max-age=0");
	
		String[] csv;
		PrintWriter writer = resp.getWriter();
		
		writeOuterData(writer,data.getLabel(),data.getText());
		
		
		csv = data.getHeader();
		
		while(csv != null){			
			writer.append(dataAsString(csv));
			csv = data.getHeader();
		}
		
		csv = data.getRow();
		if (csv !=null)
			cols = csv.length;
		while(csv != null){
			writer.append(dataAsString(csv));
			csv = data.getRow();
			rows += 1;
		}
		
		csv = data.getFooter();
		while(csv != null){			
			writer.append(dataAsString(csv));
			writer.flush();
			csv = data.getFooter();
		}
		
		drawWatermark(writer);
		
		writer.flush();
		writer.close();
	}
	
	public void generate(String xml, HttpServletResponse resp) throws IOException {
		CSVxml data = new CSVxml(xml);
		
//		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/vnd.ms-excel");
//		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-Disposition", "attachment;filename=grid.csv");
		resp.setHeader("Cache-Control", "max-age=0");
	
		String[] csv;
		PrintWriter writer = resp.getWriter();
		
		
		csv = data.getHeader();
		
		while(csv != null){			
			writer.append(dataAsString(csv));
			csv = data.getHeader();
		}
		
		csv = data.getRow();
		if (csv !=null)
			cols = csv.length;
		while(csv != null){
			writer.append(dataAsString(csv));
			csv = data.getRow();
			rows += 1;
		}
		
		csv = data.getFooter();
		while(csv != null){			
			writer.append(dataAsString(csv));
			writer.flush();
			csv = data.getFooter();
		}
		
		drawWatermark(writer);
		
		writer.flush();
		writer.close();
	}

	private void writeOuterData(PrintWriter writer,String[] label,String[] text){
		if(label != null && text != null)
		for(int i = 0;i < label.length; i++){
			if(!"".equals(label[i].trim()) && !"".equals(text[i].trim())){
				java.util.List<String> arr = new java.util.ArrayList<String>();
				arr.add(label[i]);
				String[] textArr = text[i].split(",");
				for(int count = 0; count< textArr.length; count++){
					arr.add(textArr[count]);
				}
				writer.append(dataAsString(arr.toArray(new String[arr.size()])));
			}else if("".equals(label[i].trim()) && !"".equals(text[i].trim())){
				String[] arr = text[i].split(",");//new String[1];
				
				//arr[0] = text[i];
				writer.append(dataAsString(arr));
			}else if("".equals(text[i].trim()) && !"".equals(label[i].trim())){
				String[] arr = new String[1];
				arr[0] = label[i];
				writer.append(dataAsString(arr));
			}
			
			
		}
		
	}

	private String dataAsString(String[] csv) {
		if (csv.length == 0) return "";
		
		StringBuffer buff = new StringBuffer();
		for ( int i=0; i<csv.length; i++){
			if (i>0)
				buff.append(",");
			if (!csv[i].equals("")){
				buff.append("\"");
				buff.append(csv[i].trim().replace("\"", "\"\""));
				buff.append("\"");				
			}
		}	
		buff.append("\n");
		return buff.toString();
	}

	private void drawWatermark(PrintWriter writer) {
		if (watermark != null)
			writer.append(watermark);
	}
	
	public int getColsStat() {
		return cols;
	}

	public int getRowsStat() {
		return rows;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}

	@Override
	public void setFontSize(int fontsize) {
		// do nothing
	}
}
