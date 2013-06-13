/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.datahandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ravindra_jain
 * @version 1.0
 * @created 21-Apr-2009 6:57:51 PM
 */
public class TextDataHandler extends AbstractDataHandler
{
	/**
	 *
	 */
	private BufferedWriter bufferedWriter;
	/**
	 *
	 */
	private int bufferSize;

	/**
	 *
	 * @param fileName Filename.
	 */
	TextDataHandler(String fileName)
	{

	}
	/**
	 *
	 * @param fileName FileName.
	 * @param bufferSize Buffer Size.
	 */
	TextDataHandler(String fileName, int bufferSize)
	{
		//Empty Constructor.
	}
	/**
	 * @throws IOException throw IOException
	 */
	@Override
	public void openFile() throws IOException
	{
		// TODO Auto-generated method stub

	}
	/**
	 *
	 * @param values List object.
	 */
	public void appendData(List<Object> values)
	{
		//empty appendData method.
	}
	/**
	 *
	 * @param values String object
	 */
	public void appendData(String values)
	{
		//empty appendData method.
	}
	/**
	 *
	 */
	public void closeFile()
	{
		//empty closeFile method.
	}
	/**
	 * @throws IOException throw IOException.
	 */
	@Override
	public void flush() throws IOException
	{
		// TODO Auto-generated method stub
	}
}