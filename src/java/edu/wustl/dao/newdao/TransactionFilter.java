/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao.newdao;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.DAOUtility;

public class TransactionFilter implements Filter
{

	private static final Logger logger = Logger.getCommonLogger(TransactionFilter.class);
	//private String userTransactionJndiName;
	
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		UserTransaction transaction = null;
		DAOUtility daoUtil = DAOUtility.getInstance();

		try
		{
			daoUtil.beginTransaction();
			chain.doFilter(request, response);
			if(isSuccessFulAction(request))
			{
				logger.info("Commiting Transaction");
				daoUtil.commitTransaction();
			}
			else
			{
				logger.error("Transaction is getting rollback due to no actionstatus found.");
				daoUtil.rollbackTransaction();
			}
		}
		catch (final Throwable errorInServlet)
		{
			try
			{
				if(transaction!=null)
					transaction.rollback();
			}
			catch (final Exception rollbackFailed)
			{
				logger.error("Transaction failed !", rollbackFailed);
			}
			throw new ServletException(errorInServlet);
		}
	}

	private boolean isSuccessFulAction(ServletRequest request)
	{

		boolean isSuccessFulAction = false;
		ActionStatus actionStatus = (ActionStatus)request.getAttribute(ActionStatus.ACTIONSTAUS);
		if(actionStatus!=null&&actionStatus.isSuccessAction())
		{
			isSuccessFulAction = true;
		}
		else if (request instanceof HttpServletRequest) 
		{
			 String url = ((HttpServletRequest)request).getRequestURL().toString();
			 if(url.contains("SimpleSearch.do")||url.contains("SearchCategory.do") || url.contains("UploadFile"))
			 {
				 isSuccessFulAction = true;
			 }
		}

			
		return isSuccessFulAction;
	}
	/*public void init(FilterConfig filterConfig) throws ServletException
	{
		userTransactionJndiName = filterConfig.getInitParameter("userTransactionJndiName");

	}*/

	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub
		
	}

}
