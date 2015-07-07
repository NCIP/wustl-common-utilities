/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.report.reportBizLogic.ReportBizLogic;
import edu.wustl.common.scheduler.bizLogic.ReportAuditDataBizLogic;
import edu.wustl.common.scheduler.constants.SchedulerConstants;
import edu.wustl.common.scheduler.domain.BaseSchedule;
import edu.wustl.common.scheduler.domain.ReportAuditData;
import edu.wustl.common.scheduler.domain.ReportSchedule;
import edu.wustl.common.scheduler.domain.Schedule;
import edu.wustl.common.scheduler.propertiesHandler.SchedulerConfigurationPropertiesHandler;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.EmailDetails;
import edu.wustl.common.util.global.SendEmail;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

public class SchedulerDataUtility
{

	/**
	 * @param jsonDataMap
	 * @param userId
	 * @return
	 * @throws JSONException
	 * @throws ParseException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws BizLogicException 
	 */
	public static Schedule getScheduleFromJsonMap(JSONObject jsonDataMap, Long userId)
			throws JSONException, ParseException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, SecurityException, NoSuchFieldException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, BizLogicException
	{
		BaseSchedule base = (BaseSchedule) Class.forName(
				jsonDataMap.getString(SchedulerConstants.SCHEDULE_TYPE)).newInstance();
		if (jsonDataMap.has(SchedulerConstants.SCHEDULE_ID))
		{
			base.setId(jsonDataMap.getLong(SchedulerConstants.SCHEDULE_ID));
		}
		base.setName(jsonDataMap.getString(SchedulerConstants.SCHEDULE_NAME));
		base.setInterval(jsonDataMap.getString(SchedulerConstants.INTERVAL));
		base.setComments(jsonDataMap.getString(SchedulerConstants.COMMENTS));
		if (jsonDataMap.has(SchedulerConstants.OWNER_ID))
		{
			base.setOwnerId(jsonDataMap.getLong(SchedulerConstants.OWNER_ID));
		}
		else
		{
			base.setOwnerId(userId);
		}
		if (jsonDataMap.has(SchedulerConstants.RECIPIENT_LIST))
		{
			String[] recipientUsersIds = jsonDataMap.getString(SchedulerConstants.RECIPIENT_LIST)
					.split(SchedulerConstants.COMMA_SEPARATOR);
			Collection<Long> recipientUserIdsCollection = new LinkedHashSet<Long>();
			for (int i = 1; i < recipientUsersIds.length; i++)
			{
				if (!recipientUsersIds[i].equals(""))
					recipientUserIdsCollection.add(Long.valueOf(recipientUsersIds[i]));
			}
			/*if(recipientUserIdsCollection.isEmpty())
			{
				recipientUserIdsCollection = null;
			}*/
			base.setRecepientUserIdCollection(recipientUserIdsCollection);
		}
		SimpleDateFormat formatter = new SimpleDateFormat(SchedulerConstants.DATE_FORMAT);
		if (jsonDataMap.has(SchedulerConstants.START_DATE)
				&& !jsonDataMap.getString(SchedulerConstants.START_DATE).equalsIgnoreCase(
						"undefined"))
		{
			base.setStartDate(formatter.parse(jsonDataMap.getString(SchedulerConstants.START_DATE)));
		}
		if (jsonDataMap.has(SchedulerConstants.END_DATE)
				&& !jsonDataMap.getString(SchedulerConstants.END_DATE)
						.equalsIgnoreCase("undefined"))
		{
			base.setEndDate(formatter.parse(jsonDataMap.getString(SchedulerConstants.END_DATE)));
		}
		if (jsonDataMap.has(SchedulerConstants.INCLUDE_ME))
		{
			base.setIncludeMe(jsonDataMap.getBoolean(SchedulerConstants.INCLUDE_ME));
		}
		else
		{
			base.setIncludeMe(false);
		}

		if (jsonDataMap.has("Duration"))
		{
			((ReportSchedule) base).setDuration(jsonDataMap.getString("Duration"));
		}
		else
		{
			((ReportSchedule) base).setDuration(jsonDataMap.getString(SchedulerConstants.INTERVAL));
		}

		base.setActivityStatus("Active");
		String tempItemList[] = jsonDataMap.getString("itemIdCollection").split(";");
		Collection<Long> idCollection = new HashSet<Long>();
		for (String idString : tempItemList)
		{
			if (!idString.equals(""))
			{
				idCollection.add(Long.valueOf(idString));
			}
		}

		base.setScheduleItemIdCollection(idCollection);

		return base;
	}

	/**
	 * @param idList
	 * @return
	 * @throws Exception 
	 */
	public static List<Object[]> getUsersIdAndEmailAddressList(Collection<Long> idList)
			throws Exception
	{
		IHostAppUserDataRetriever iHostUserRet = (IHostAppUserDataRetriever) Class.forName(
				(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
						"host.user.retrieval.implName")).newInstance();
		List<Object[]> userIdAndEmailList = iHostUserRet.getUserIdAndMailAddressList(idList);
		return userIdAndEmailList;
	}

	/**
	 * @param idList
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getUserNamesList(Collection<Long> idList) throws Exception
	{
		IHostAppUserDataRetriever iHostUserRet = (IHostAppUserDataRetriever) Class.forName(
				(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
						"host.user.retrieval.implName")).newInstance();
		List<String> list = iHostUserRet.getUserNamesList(idList);
		return list;
	}

	/**
	 * @param userIdList
	 * @return
	 */
	public static String getQueryInClauseStringFromIdList(Collection<Long> userIdCollection)
	{
		StringBuilder userList = new StringBuilder("(");
		for (Long id : userIdCollection)
		{
			userList.append("," + id);
		}
		String userListStr = userList.toString();
		userListStr = userListStr.replaceFirst(",", "");
		userListStr += ")";
		return userListStr;
	}

	/**
	 * @param baseScheduleList
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getJSONFromScheduleList(List<Schedule> baseScheduleList)
			throws Exception
	{
		JSONArray jsonDataRow = new JSONArray();
		JSONObject mainJsonRow = new JSONObject();
		SimpleDateFormat formatter = new SimpleDateFormat(SchedulerConstants.DATE_FORMAT);
		int i = 0;
		if (baseScheduleList != null && !baseScheduleList.isEmpty())
		{
			for (Schedule schedule : baseScheduleList)
			{
				i++;
				JSONObject jsonObject = new JSONObject();
				JSONArray dataArray = new JSONArray();
				dataArray.put(i);
				dataArray.put(schedule.getName());

				if (schedule instanceof ReportSchedule)
				{

					String items = "";
					ReportBizLogic repoBiz = new ReportBizLogic();
					for (Long itemId : ((ReportSchedule) schedule).getScheduleItemIdCollection())
					{
						items += "," + repoBiz.getReportNameById(itemId);
					}
					items = items.replaceFirst(",", "");
					dataArray.put(items);

				}

				if (schedule.getStartDate() == null || schedule.getEndDate() == null)
				{
					dataArray.put("");
				}
				else
				{
					dataArray.put(formatter.format(schedule.getStartDate()) + " : "
							+ formatter.format(schedule.getEndDate()));
				}

				dataArray.put(schedule.getInterval());
				dataArray.put(getUserNamesList(
						Arrays.asList(((BaseSchedule) schedule).getOwnerId())).get(0));

				if (!((BaseSchedule) schedule).getRecepientUserIdCollection().isEmpty())
				{
					dataArray.put(getRecipientNamesStringFromIds(((BaseSchedule) schedule)
							.getRecepientUserIdCollection()));
				}
				else
				{
					dataArray.put("");
				}

				dataArray.put("<a href='javascript:deleteSchedule(" + schedule.getId()
						+ ")'><img src='images/advQuery/cancel.png'alt='delete'></a>");

				String itemIds = "";
				for (Long itemId : ((BaseSchedule) schedule).getScheduleItemIdCollection())
				{
					itemIds += ";" + itemId;
				}
				itemIds = itemIds.replaceFirst(";", "");
				dataArray.put(itemIds);

				dataArray.put(((BaseSchedule) schedule).getOwnerId());
				dataArray.put(schedule.getId());
				String idList = getQueryInClauseStringFromIdList(((BaseSchedule) schedule)
						.getRecepientUserIdCollection());
				idList = idList.replace("(", "");
				idList = idList.replace(")", "");
				dataArray.put(idList);
				dataArray.put(((BaseSchedule) schedule).getComments());
				dataArray.put(((BaseSchedule) schedule).getIncludeMe());
				if (schedule instanceof ReportSchedule)
				{
					if (((ReportSchedule) schedule).getDuration() != null)
						dataArray.put(ReportSchedulerUtil.durationLiteralMap
								.get(((ReportSchedule) schedule).getDuration()));
				}
				jsonObject.put(SchedulerConstants.DATA, dataArray);
				jsonObject.put(SchedulerConstants.ID, schedule.getId());
				jsonDataRow.put(jsonObject);
			}
		}
		mainJsonRow.put(SchedulerConstants.ROWS, jsonDataRow);
		return mainJsonRow;
	}

	/**
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	public static String getRecipientNamesStringFromIds(Collection<Long> ids) throws Exception
	{
		StringBuilder userNamesList = new StringBuilder("");
		IHostAppUserDataRetriever iHostUserRet = (IHostAppUserDataRetriever) Class.forName(
				(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
						"host.user.retrieval.implName")).newInstance();
		List<String> list = iHostUserRet.getUserNamesList(ids);
		for (String name : list)
		{
			userNamesList.append(";" + name);
		}
		String nameList = userNamesList.toString();
		nameList = nameList.replaceFirst(";", "");
		return nameList;
	}

	/**
	 * @param request
	 * @param csId
	 * @return
	 * @throws JSONException
	 * @throws DAOException
	 * @throws IOException
	 * @throws BizLogicException
	 */
	public static JSONObject getReportNameListJSON(HttpServletRequest request, Long csId)
			throws JSONException, DAOException, IOException, BizLogicException
	{
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				SchedulerConstants.SESSION_DATA);

		List<NameValueBean> beans = getReportNameAndIdBeans(csId, sessionDataBean.getUserId(),
				sessionDataBean.isAdmin());
		return createExtJsJson(request, beans);
	}

	/**
	 * @param csId
	 * @param userId
	 * @param isAdmin
	 * @return
	 * @throws DAOException
	 * @throws IOException
	 * @throws BizLogicException
	 */
	public static List<NameValueBean> getReportNameAndIdBeans(Long csId, Long userId,
			Boolean isAdmin) throws DAOException, IOException, BizLogicException
	{
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		NameValueBean bean;
		ReportBizLogic repoBiz = new ReportBizLogic();
		List<Object> reportNameIdList = new ArrayList<Object>();

		/*if (isAdmin)
		{
			reportNameIdList = repoBiz.getReportNames(csId);
		}
		else
		{
			//reportNameIdList = repoBiz.getReportNamesByUserId(userId, csId);
			
		}*/
		reportNameIdList = repoBiz.getReportNames(csId);
		for (Object reportObj : reportNameIdList)
		{
			@SuppressWarnings("rawtypes")
			List list = (List) reportObj;
			bean = new NameValueBean();
			bean = new NameValueBean(list.get(0), list.get(1));
			beans.add(bean);
		}
		return beans;
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getUserNameListJSON(HttpServletRequest request) throws Exception
	{
		String scheduleType = request.getParameter(SchedulerConstants.SCHEDULE_TYPE);
		String idString = request.getParameter(SchedulerConstants.ID);
		Long id = 0l;

		if (idString != null && !idString.equalsIgnoreCase("undefined"))
		{
			id = Long.valueOf(request.getParameter(SchedulerConstants.ID));
		}
		List<NameValueBean> beans = new ArrayList<NameValueBean>();
		if (scheduleType.equals(SchedulerConstants.REPORT_SCHEDULE))
		{
			IHostAppUserDataRetriever iHostUserRet = (IHostAppUserDataRetriever) Class.forName(
					(String) SchedulerConfigurationPropertiesHandler.getInstance().getProperty(
							"host.user.retrieval.implName")).newInstance();
			beans = iHostUserRet.getUserIdNameListForReport(id);
		}
		return createExtJsJson(request, beans);
	}

	/**
	 * @param querySpecificNVBeans
	 * @param cplist
	 * @param query
	 */
	private static synchronized void populateQuerySpecificNameValueBeansList(
			List<NameValueBean> querySpecificNVBeans, List<NameValueBean> cplist, String query)
	{
		for (Object obj : cplist)
		{
			NameValueBean nvb = (NameValueBean) obj;

			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();
			if (nvb.getName().toLowerCase(locale).contains(query.toLowerCase(locale)))
			{
				querySpecificNVBeans.add(nvb);
			}
		}
	}

	/**
	 * @param request
	 * @param beans
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject createExtJsJson(HttpServletRequest request, List<NameValueBean> beans)
			throws JSONException
	{
		String limit = request.getParameter(SchedulerConstants.LIMIT);
		String query = request.getParameter(SchedulerConstants.QUERY) == null ? "" : request
				.getParameter(SchedulerConstants.QUERY);
		String start = request.getParameter(SchedulerConstants.START);
		Integer limitFetch = Integer.parseInt(limit);
		Integer startFetch = Integer.parseInt(start);
		JSONArray jsonArray = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		Integer total = limitFetch + startFetch;
		List<NameValueBean> querySpecificNVBeans = new ArrayList<NameValueBean>();
		populateQuerySpecificNameValueBeansList(querySpecificNVBeans, beans, query);
		mainJsonObject.put(SchedulerConstants.TOTAL_COUNT, querySpecificNVBeans.size());
		for (int i = startFetch; i < total && i < querySpecificNVBeans.size(); i++)
		{
			JSONObject jsonObject = new JSONObject();
			Locale locale = CommonServiceLocator.getInstance().getDefaultLocale();
			if (query == null
					|| querySpecificNVBeans.get(i).getName().toLowerCase(locale)
							.contains(query.toLowerCase(locale)) || query.length() == 0)
			{
				jsonObject.put(SchedulerConstants.ID, querySpecificNVBeans.get(i).getValue());
				jsonObject.put(SchedulerConstants.FIELD, querySpecificNVBeans.get(i).getName());
				jsonArray.put(jsonObject);
			}
		}
		mainJsonObject.put(SchedulerConstants.ROW, jsonArray);
		return mainJsonObject;
	}

	/**
	 * @param dirPath
	 * @param ageThreshold in Days
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws BizLogicException 
	 * @throws DAOException 
	 * 
	 */
	public static void cleanOldFilesFromDir(Long ageThreshold) throws IOException, ParseException,
			BizLogicException, DAOException
	{
		ReportAuditDataBizLogic fileBiz = new ReportAuditDataBizLogic();
		List<ReportAuditData> filesList = fileBiz.getFileDetailsList();
		for (ReportAuditData fileDetail : filesList)
		{
			if ((fileDetail.getExecuteionEnd() == null)
					|| (new Date().getTime() - fileDetail.getExecuteionEnd().getTime()) >= (ageThreshold * 86400000))
			{
				File file = new File(fileDetail.getFileName());
				if (file.exists())
				{
					FileUtils.forceDelete(file);
					String dir = file.getPath().replace("/" + file.getName(), "");
					FileUtils.forceDelete(new File(dir));
				}
				fileDetail.setJobStatus("Deleted");
				fileBiz.update(fileDetail);
			}
		}
	}

	/**
	 * @param emailAddress
	 * @param subject
	 * @param body
	 * @throws MessagingException
	 * @throws Exception 
	 */
	public static void sendScheduleMail(String emailAddress, String subject, String body)
			throws MessagingException, Exception
	{
		StringBuilder bodyValue = new StringBuilder();
		bodyValue.append(body);
		String sendFromEmailId = XMLPropertyHandler.getValue("email.sendEmailFrom.emailAddress");
		String mailServer = XMLPropertyHandler.getValue("email.mailServer");
		SendEmail email = new SendEmail(mailServer, sendFromEmailId);

		EmailDetails emailDetails = new EmailDetails();
		emailDetails.setCcAddress(new String[]{(String) SchedulerConfigurationPropertiesHandler
				.getInstance().getProperty("host.mail.alias")});
		emailDetails.addToAddress(emailAddress);
		emailDetails.setSubject(subject);
		emailDetails.setBody(bodyValue.toString());

		email.sendMail(emailDetails);
	}

	/**
	 * @return
	 * @throws DAOException
	 */
	public static JDBCDAO getJDBCDAO() throws DAOException
	{
		return DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getJDBCDAO();
	}

	public static List<String> getCustomReportParamList() throws Exception
	{
		List<String> dbDetailsList = new LinkedList<String>();

		SchedulerConfigurationPropertiesHandler configProp = SchedulerConfigurationPropertiesHandler
				.getInstance();

		for (String propName : SchedulerConstants.DB_DETAILS_LIST)
		{
			dbDetailsList.add((String) configProp.getProperty(propName));
		}

		return dbDetailsList;
	}

}
