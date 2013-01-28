
package edu.wustl.common.util.tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Divyabhanu Singh
 *@version 1.0
 */

public class PagenationTag extends TagSupport
{

	/**
	 * specify serial Version Unique ID.
	 */
	private static final long serialVersionUID = 3660111496312380494L;

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(PagenationTag.class);

	/**
	 * Constant for TOTAL_RESULT.
	 */
	private static final int TOTAL_RESULT = 1000;
	/**
	 * Constant for TEN.
	 */
	private static final int TEN = 10;
	/**
	 * Constant for RESULTS_PER_PAGE.
	 */
	private static final int RESULTS_PER_PAGE = 15;

	/**
	 * specify name.
	 */
	protected String name = "Bhanu";

	/**
	 * specify pageNum.
	 */
	protected int pageNum = 1;

	/**
	 * specify previous Page.
	 */
	protected String prevPage = null;

	/**
	 * specify total Results.
	 */
	protected int totalResults = TOTAL_RESULT;

	/**
	 * specify number of Results Per Page.
	 */
	protected int numResultsPerPage = RESULTS_PER_PAGE;

	/**
	 * specify page Link Start.
	 */
	protected int mpageLinkStart = 1;

	/**
	 * specify page Link End.
	 */
	protected int mpageLinkEnd = TEN;

	/**
	 * specify show Next.
	 */
	protected boolean mshowNext;

	/**
	 * specify search term.
	 */
	protected String searchTerm = null;

	/**
	 * specify search Term Values.
	 */
	protected String searchTermValues = null;

	/**
	 * specify selectedOrgs.
	 */
	protected String[] selectedOrgs = null;

	/**
	 * specify numLinks.
	 */
	private int numLinks = TEN;

	/**
	 * specify resultLowRange.
	 */
	private int resultLowRange = 1;

	/**
	 * specify resultHighRange.
	 */
	private int resultHighRange = 1;

	/**
	 * specify pageName.
	 */
	private String pageName = null;

	/**
	 * Showing combo for Records/page values.
	 */
	protected boolean showPageSizeCombo = false;

	/**
	 * specify recordPerPageList.
	 */
	protected int[] recordPerPageList = Constants.RESULT_PERPAGE_OPTIONS;

	/**
	 * doStartTag.
	 * @return SKIP_BODY
	 */
	public int doStartTag()
	{
		try
		{
			mshowNext = true;
			JspWriter out = pageContext.getOut();
			//pageName = SpreadsheetView for ViewResults page (SimpleSearchDataView.jsp)
			setTableTag(out);

			setMPageLinkStart();

			// If user has opted to view all Records on this page.
			setRecordsToShow();

			//pageName = SpreadsheetView for ViewResults page (SimpleSearchDataView.jsp)
			if (!getPageName().equals("SpreadsheetView.do"))
			{
				out.println("<tr> <td class = \"formtextbg\" align=\"CENTER\">" + name + "</td>");
			}

			showCombo(out);
			setTotalResultsToTag(out);

			setPageLink(out);

			setPageNum(out);

		}
		catch (IOException ioe)
		{
			LOGGER.debug("Error generating prime: " + ioe, ioe);
		}
		catch (Exception e)
		{
			LOGGER.debug(e.getMessage(), e);
		}
		return SKIP_BODY;
	}

	/**
	 * @param out JspWriter
	 * @throws IOException IO Exception
	 */
	private void setPageNum(JspWriter out) throws IOException
	{
		int count = mpageLinkStart;
		for (count = mpageLinkStart; count <= mpageLinkEnd; count++)
		{
			if (count == pageNum)
			{
				out.print("<td align=\"CENTER\">" + count + " </td>");
			}
			else
			{
				out.print("<td align=\"CENTER\"> <a href=\"javascript:send(" + count + ","
						+ numResultsPerPage + ",'" + prevPage + "','"
						+ pageName + "')" + "\">" + count + " </a></td>");
			}
		}
		if (mshowNext)
		{
			out.print("<td align=\"CENTER\"><a href=\"javascript:send(" + count + ","
					+ numResultsPerPage + ",'" + prevPage + "','" + pageName + "')"
					+ "\"> >>  </a> </td>");
		}
		else
		{
			out.print("<td align=\"CENTER\">&nbsp;</td>");
		}
		out.print("</tr></table>");
	}

	/**
	 * @param out JspWriter
	 * @throws IOException IO Exception
	 */
	private void setPageLink(JspWriter out) throws IOException
	{
		if ((mpageLinkEnd) > numLinks)
		{
			out.print("<td align=\"CENTER\"><a href=\"javascript:send(" + (mpageLinkStart - 1)
					+ "," + numResultsPerPage + ",'" + prevPage + "','"
					+ pageName + "')" + "\"> &lt;&lt;  </a></td>");
		}
		else
		{

			out.print("<td align=\"CENTER\">&nbsp;</td>");
		}
	}

	/**
	 * @param out Jsp Writer
	 * @throws IOException IO Exception.
	 */
	private void setTotalResultsToTag(JspWriter out) throws IOException
	{
		//Mandar 19-Apr-06 : 1697 :- Summary shows wrong data. Checking for zero records.
		if (totalResults > 0)
		{
			out.println("<td  align = \"CENTER\" class = \"formtextbg\">" + resultLowRange
					+ " - " + resultHighRange + " of " + totalResults + "</td>");
		}
		else
		{
			out.println("<td  align = \"CENTER\" class = \"formtextbg\">Showing Results " + "0"
					+ " - " + "0" + " of " + "0" + "</td>");
		}
		//Mandar 19-Apr-06 : 1697 :- Summary shows wrong data. end
	}

	/**
	 * @param out JspWriter
	 * @throws IOException IO Exception
	 */
	private void showCombo(JspWriter out) throws IOException
	{
		if (showPageSizeCombo)
		{
			// Showing combo for Records/page values.

			String options = getOptions();

			out
					.println("<td>Records Per Page <select name=\"recordPerPage\" size=\"1\"" +
							" onChange=\"javascript:changeRecordPerPage("
							+ (mpageLinkStart)
							+ ",this,'"
							+ pageName
							+ "')\""
							+ " >"
							+ options
							+ "</select> <td>");

		}
	}

	/**
	 * @return options.
	 */
	private String getOptions()
	{
		StringBuffer options = new StringBuffer();

		int[] possibleResultPerPageValues = putValueIfNotPresent(recordPerPageList,
				numResultsPerPage);

		for (int i = 0; i < possibleResultPerPageValues.length; i++)
		{
			int value = possibleResultPerPageValues[i];
			String name = Integer.toString(possibleResultPerPageValues[i]);

			if (value == Integer.MAX_VALUE)
			{
				name = "All";
			}

			if (possibleResultPerPageValues[i] == numResultsPerPage)
			{
				options.append("<option value=\"").append(value)
				.append("\" selected=\"selected\" >").append(name).append("</option>");
			}
			else
			{
				options.append("<option value=\"").append(value)
				.append("\">").append(name).append("</option>");
			}
		}
		return options.toString();
	}

	/**
	 * @param out JspWriter
	 * @throws IOException IO Exception.
	 */
	private void setTableTag(JspWriter out) throws IOException
	{
		if (getPageName().equals("SpreadsheetView.do"))
		{
			out.println("<table class=\"black_ar\" border=0 bordercolor=#FFFFFF width=98% >");
		}
		else
		{
			out.println("<table class=\"black_ar\" border=0 bordercolor=#FFFFFF width=38%>");
		}
	}

	/**
	 * set Records To Show.
	 */
	private void setRecordsToShow()
	{
		if (numResultsPerPage == Integer.MAX_VALUE)
		{
			mpageLinkStart = 1;
			mpageLinkEnd = 1;
			mshowNext = false;
			resultLowRange = 1;
			resultHighRange = totalResults;
		}
		else
		{
			setMPageLinkEnd();

			if ((mpageLinkEnd * numResultsPerPage >= totalResults))
			{
				mshowNext = false;
			}
			setResultRange();
		}
	}

	/**
	 * set Result Range.
	 */
	private void setResultRange()
	{
		resultLowRange = (pageNum - 1) * numResultsPerPage + 1;
		if (totalResults - ((pageNum - 1) * numResultsPerPage) < numResultsPerPage)
		{
			resultHighRange = resultLowRange + totalResults
					- ((pageNum - 1) * numResultsPerPage) - 1;
		}
		else
		{
			resultHighRange = resultLowRange + numResultsPerPage - 1;
		}
	}

	/**
	 * set MPage Link End.
	 */
	private void setMPageLinkEnd()
	{
		if ((totalResults - ((mpageLinkStart - 1) * numResultsPerPage)) >= numResultsPerPage
				* numLinks)
		{
			mpageLinkEnd = mpageLinkStart + (numLinks - 1);

		}
		else
		{
			if ((totalResults - (mpageLinkStart * numResultsPerPage)) > 0)
			{
				if (totalResults % numResultsPerPage == 0)
				{
					mpageLinkEnd = (mpageLinkStart
							+ (totalResults - (mpageLinkStart * numResultsPerPage))
							/ numResultsPerPage);
				}
				else
				{
					mpageLinkEnd = (mpageLinkStart
							+ (totalResults - (mpageLinkStart * numResultsPerPage))
							/ numResultsPerPage) + 1;
				}
			}
			else
			{
				mpageLinkEnd = (mpageLinkStart
						+ (totalResults - (mpageLinkStart * numResultsPerPage))
						/ numResultsPerPage);

			}

		}
	}

	/**
	 * set MPage Link Start.
	 */
	private void setMPageLinkStart()
	{
		if (pageNum > numLinks)
		{
			if (pageNum % numLinks == 0)
			{
				mpageLinkStart = (pageNum - numLinks) + 1;
			}
			else
			{
				mpageLinkStart = ((pageNum / numLinks) * numLinks + 1);
			}
		}
		else
		{
			//For first time or for PageNum < 10.
			mpageLinkStart = 1;
		}
	}

	/**
	 * This method put Value If Not Present.
	 * @param originalArray originalArray
	 * @param value value
	 * @return newArray
	 */
	private int[] putValueIfNotPresent(int[] originalArray, int value)
	{
		int flag = 0;
		int[] newArray = new int[originalArray.length + 1];
		flag = isValuePresent(originalArray, value);
		if (flag == 0)
		{
			// array doesn't containe value, hence define new array.
			int counter = 0;
			for (; counter < originalArray.length && value > originalArray[counter]; counter++)
			{
				// copying all elements less than value.
				newArray[counter] = originalArray[counter];
			}

			newArray[counter++] = value;
			for (; counter < newArray.length; counter++) // moving array elements 1 position next.
			{
				newArray[counter] = originalArray[counter - 1];
			}
		}
		else
		{
			newArray = originalArray;
		}
		return newArray;

	}

	/**
	 * Check is Value Present.
	 * @param originalArray original Array.
	 * @param value value
	 * @return flag 1 if present else 0.
	 */
	private int isValuePresent(int[] originalArray, int value)
	{
		int flag = 0;
		for (int i = 0; i < originalArray.length; i++)
		{
			//if array contains the value, then return same array.
			if (value == originalArray[i])
			{
				//return originalArray;
				flag = 1;
			}
		}
		return flag;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param pageNum The pageNum to set.
	 */
	public void setPageNum(int pageNum)
	{
		try
		{
			this.pageNum = pageNum;
		}
		catch (NumberFormatException nfe)
		{
			this.pageNum = 1;
			LOGGER.error(nfe.getMessage(), nfe);
		}

	}

	/**
	 * @param totalResults The totalResults to set.
	 */
	public void setTotalResults(int totalResults)
	{
		try
		{
			this.totalResults = totalResults;
		}
		catch (NumberFormatException nfe)
		{
			LOGGER.debug(nfe.getMessage(), nfe);
			this.totalResults = TOTAL_RESULT;
		}
	}

	/**
	 * @param numResultsPerPage The numResultsPerPage to set.
	 */
	public void setNumResultsPerPage(int numResultsPerPage)
	{
		try
		{
			this.numResultsPerPage = numResultsPerPage;
		}
		catch (NumberFormatException nfe)
		{
			LOGGER.debug(nfe.getMessage(), nfe);
			this.numResultsPerPage = TEN;
		}
	}

	/**
	 * @param searchTerm The searchTerm to set.
	 */
	public void setSearchTerm(String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	/**
	 * @param searchTermvalues The searchTermvalues to set.
	 */
	public void setSearchTermValues(String searchTermvalues)
	{
		this.searchTermValues = searchTermvalues;
	}

	/**
	 * @param selectedOrgs The selectedOrgs to set.
	 */
	public void setSelectedOrgs(String[] selectedOrgs)
	{
		List<String> selectedOrgsList = Arrays.asList(selectedOrgs);
		String[] selectedOrgArray = new String[selectedOrgsList.size()];
		this.selectedOrgs = selectedOrgsList.toArray(selectedOrgArray);
	}

	/**
	 * @return Returns the prevPage.
	 */
	public String getPrevPage()
	{
		return prevPage;
	}

	/**
	 * @param prevPage The prevPage to set.
	 */
	public void setPrevPage(String prevPage)
	{
		this.prevPage = prevPage;
	}

	/**
	 * @return Returns the pageName.
	 */
	public String getPageName()
	{
		return pageName;
	}

	/**
	 * @param pageName The pageName to set.
	 */
	public void setPageName(String pageName)
	{
		this.pageName = pageName;
	}

	/**
	 * @param showPageSizeCombo The showPageSizeCombo to set.
	 */
	public void setShowPageSizeCombo(boolean showPageSizeCombo)
	{
		this.showPageSizeCombo = showPageSizeCombo;
	}

	/**
	 * @param recordPerPageList The recordPerPageList to set.
	 */
	public void setRecordPerPageList(int[] recordPerPageList)
	{
		this.recordPerPageList = recordPerPageList.clone();
	}
}