/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.labelSQLApp.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import edu.wustl.common.util.logger.Logger;
import au.com.bytecode.opencsv.CSVReader;
import edu.wustl.common.labelSQLApp.bizlogic.LabelSQLAssociationBizlogic;
import edu.wustl.common.labelSQLApp.bizlogic.LabelSQLBizlogic;
import edu.wustl.common.labelSQLApp.domain.LabelSQL;
import edu.wustl.common.labelSQLApp.exception.LabelSQLAppException;

public class InsertCSDashboardItem
{

	public static void main(String args[]) throws IOException
	{
		System.out.println("Entered in main...");
		CSVReader reader = new CSVReader(new FileReader(args[0]));
		String[] nextLine;
		System.out.println("Reading from CSV...");
		reader.readNext();
		int cnt = 0;

		while ((nextLine = reader.readNext()) != null)
		{
			System.out.println("Iterating CSV row...");
			try
			{
				cnt++;
				Long cpId = null;
				Integer order = new Integer(0);
				if (!nextLine[0].equals(""))
				{
					cpId = Long.valueOf(nextLine[0]);
				}
				if (!nextLine[4].equals(""))
				{
					order = Integer.parseInt(nextLine[4]);
				}
				System.out.println("Associating CSV entry...");
				associateCSWithDashboardItem(cpId, nextLine[1], nextLine[2], nextLine[3], order);
				System.out.println("Associating CSV entry done...");

			}
			catch (LabelSQLAppException e)
			{
				Logger.out.error("Error inserting record " + cnt);
				e.printStackTrace();
			}
		}
		System.out.println("Exiting main...");
	}

	private static void associateCSWithDashboardItem(Long CSId, String label, String sql,
			String displayName, int order) throws LabelSQLAppException
	{
		Long labelSQLId = null;

		if ((label == null || "".equals(label)) && !(sql == null || "".equals(sql)) && (order >= 0))
		{
			//this is the case to use a SQL directly, 
			//add the SQL with NULL label and associate with CS
			System.out.println("case to use a SQL directly...");
			if (displayName == null || "".equals(displayName))
			{
				throw new LabelSQLAppException("Display name is mandatory");
			}
			try
			{
				System.out.println("Insert sql...");
				labelSQLId = new LabelSQLBizlogic().insertLabelSQL(null, sql);
				System.out.println("Insert sql done...");
				System.out.println("Associating sql...");
				new LabelSQLAssociationBizlogic().insertLabelSQLAssociation(CSId, labelSQLId,
						displayName, order);
				System.out.println("Associating sql done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error while inserting SQL -> " + sql);
				e.printStackTrace();
			}
		}
		else if ((sql == null || "".equals(sql)) && !(label == null || "".equals(label))
				&& !(displayName == null || "".equals(displayName)) && (order >= 0))
		{
			//this is the case to use an existing label, 
			//check if there exists a label with the same name, if not throw error if yes use it
			System.out.println("case to use an existing label...");
			List<LabelSQL> result = null;
			try
			{
				System.out.println("getLabelSQLByLabel...");
				result = new LabelSQLBizlogic().getLabelSQLByLabel(label);
				System.out.println("getLabelSQLByLabel done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error retrieving LabelSQL from label -> " + label);
				e.printStackTrace();
			}
			if (result.size() == 0)
			{
				throw new LabelSQLAppException("There is no existing Label: " + label);
			}
			labelSQLId = result.get(0).getId();

			try
			{
				System.out.println("Associating label...");
				new LabelSQLAssociationBizlogic().insertLabelSQLAssociation(CSId, labelSQLId,
						displayName, order);
				System.out.println("Associating label done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error associating label -> " + label);
				e.printStackTrace();
			}

		}
		else if (!(sql == null || "".equals(sql)) && !(label == null || "".equals(label))
				&& (order >= 0))
		{
			//this is the case to add a new label, 
			//check if there exists a label with the same name, if yes throw error
			System.out.println("case to add a new label...");
			if (displayName == null || "".equals(displayName))
			{
				throw new LabelSQLAppException("Display name is mandatory");
			}

			List<LabelSQL> result = null;
			try
			{
				System.out.println("getLabelSQLByLabel...");
				result = new LabelSQLBizlogic().getLabelSQLByLabel(label);
				System.out.println("getLabelSQLByLabel done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error retrieving LabelSQL from label -> " + label);
				e.printStackTrace();
			}
			if (result.size() != 0)
			{
				throw new LabelSQLAppException("Label already exists : " + label);
			}
			try
			{
				System.out.println("insert labelsql...");
				labelSQLId = new LabelSQLBizlogic().insertLabelSQL(label, sql);
				System.out.println("insert labelsql done...");

				System.out.println("associating labelsql...");
				new LabelSQLAssociationBizlogic().insertLabelSQLAssociation(CSId, labelSQLId,
						displayName, order);
				System.out.println("associating labelsql done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error associating label -> " + label);
				e.printStackTrace();
			}
		}
		else if ((sql == null || "".equals(sql))
				&& !(displayName == null || "".equals(displayName)) && (order == -1))
		{
			//this is the case to delete the association of labelSQL and CS

			System.out.println("case to delete the association of labelSQL and CS...");
			try
			{
				System.out.println("getLabelSQLIdByLabelOrDisplayName...");
				labelSQLId = new LabelSQLBizlogic().getLabelSQLIdByLabelOrDisplayName(CSId,
						displayName);
				System.out.println("getLabelSQLIdByLabelOrDisplayName done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error retrieving association for CS: " + CSId
						+ " with display name -> " + displayName);
				e.printStackTrace();
			}

			if (labelSQLId == null)
			{
				throw new LabelSQLAppException("Display name: " + displayName
						+ " is not associated with the CS: " + CSId);
			}
			else
			{

				try
				{
					System.out.println("delete association...");
					new LabelSQLAssociationBizlogic().deleteLabelSQLAssociation(CSId, labelSQLId);
					System.out.println("delete association done...");
				}
				catch (Exception e)
				{
					Logger.out.error("Error deleting association " + CSId + " --> " + displayName);
					e.printStackTrace();
				}
			}
		}
		else if ((sql == null || "".equals(sql)) && !(label == null || "".equals(label))
				&& (displayName == null || "".equals(displayName)) && (order >= 0))
		{
			//this is the case to add a new label heading, 
			//check if there exists a label heading with the same name, if yes throw error
			System.out.println("case to add a new label heading...");
			List<LabelSQL> result = null;
			try
			{
				System.out.println("getLabelSQLByLabel...");
				result = new LabelSQLBizlogic().getLabelSQLByLabel(label);
				System.out.println("getLabelSQLByLabel done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error retrieving LabelSQL from label -> " + label);
				e.printStackTrace();
			}
			if (result.size() == 0)
			{
				try
				{
					System.out.println("insert heading...");
					labelSQLId = new LabelSQLBizlogic().insertLabelSQL(label, null);
					System.out.println("insert heading done...");
				}
				catch (Exception e)
				{
					Logger.out.error("Error inserting label: " + label);
					e.printStackTrace();
				}
			}
			else
			{
				labelSQLId = result.get(0).getId();
			}
			try
			{
				System.out.println("associate heading...");
				new LabelSQLAssociationBizlogic().insertLabelSQLAssociation(CSId, labelSQLId, null,
						order);
				System.out.println("associate heading done...");
			}
			catch (Exception e)
			{
				Logger.out.error("Error associating Label Heading " + label);
				e.printStackTrace();
			}

		}
		else
		{
			throw new LabelSQLAppException("Invalid CSV entries");
		}

	}

}
