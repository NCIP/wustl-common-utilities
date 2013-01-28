
package edu.wustl.common.scheduler.util;

import java.util.Collection;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;

public interface IHostAppUserDataRetriever
{

	public List<NameValueBean> getUserIdNameListForReport(Long id) throws Exception;

	public List<Object[]> getUserIdAndMailAddressList(Collection<Long> userIdCollection)
			throws Exception;
	
	public List<String> getUserNamesList(Collection<Long> idList) throws Exception;
	
	public String getUserEmail(Long userId) throws Exception;

}
