/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.scheduler.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class SchedulerConstants
{

	public static final String DAILY_INTERVAL = "daily";
	public static final String WEEKLY_INTERVAL = "weekly";
	public static final String MONTHLY_INTERVAL = "monthly";
	public static final String QUARTERLY_INTERVAL = "quarterly";
	public static final String YEARLY_INTERVAL = "yearly";
	public static final String THREAD_POOL_SIZE = "scheduler.threadPool.size";
	public static final String PROPERTIES_FILE_NAME = "Scheduler.properties";
	public static final String TAG_FILE_NAME = "schedulerTag.txt";
	public static final String SCHEDULE_TYPE_TOKEN = "$scheduleTypeToken$";
	public static final String CAPTION_TOKEN = "$captionToken$";
	public static final String DROPDOWN_CAPTION_TOKEN = "$dropDownCaptionToken$";
	public static final String DROPDOWN_TOKEN = "$dropDownToken$";
	public static final String USER_DROPDOWN_TOKEN = "$userDropDownToken$";
	public static final String OWNER_ID_TOKEN = "$ownerIdToken$";
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String SCHEDULE_ID = "Id";
	public static final String SCHEDULE_NAME = "Name";
	public static final String INTERVAL = "Interval";
	public static final String OWNER_ID = "OwnerId";
	public static final String RECIPIENT_LIST = "RecepientUserIdCollection";
	public static final String START_DATE = "StartDate";
	public static final String END_DATE = "EndDate";
	public static final String DROPDOWN = "dropDown";
	public static final String DROPDOWN_TYPE = "dropDownType";
	public static final String COMMA_SEPARATOR = ",";
	public static final String HOST_NAME = "host.appName";
	public static final String HOST_USER_CLASS_NAME = "host.user.type";
	public static final String HOST_USER_EMAIL_ATTRIBUTE_NAME = "host.user.type.emailAttribute.name";
	public static final String HOST_USER_ID_NAME = "host.user.type.id";
	public static final String HOST_USER_FIRST_NAME = "host.user.type.firstNameAttribute.name";
	public static final String HOST_USER_LAST_NAME = "host.user.type.lastNameAttribute.name";
	public static final String FORM_JSON = "formJson";
	public static final String SCHEDULE_TYPE = "scheduleType";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String QUERY_SCHEDULE = "edu.wustl.common.scheduler.domain.QuerySchedule";
	public static final String REPORT_SCHEDULE = "edu.wustl.common.scheduler.domain.ReportSchedule";
	public static final String TYPE = "type";
	public static final String MESSAGE = "message";
	public static final String DATA_SAVED = " saved successfully.";
	public static final String SESSION_DATA = "sessionData";
	public static final String DATA_NOT_SAVED = " not saved successfully.";
	public static final String COMMENTS = "Comments";
	public static final String ID = "id";
	public static final String ROW = "row";
	public static final String ROWS = "rows";
	public static final String DATA = "data";
	public static final String FIELD = "field";
	public static final String TOTAL_COUNT = "totalCount";
	public static final String LIMIT = "limit";
	public static final String QUERY = "query";
	public static final String START = "start";
	public static final String SET = "set";
	public static final String QUERY_ID = "QueryId";
	public static final String QUERY_SCHEDULE_DISPLAY_NAME = "QuerySchedule";
	public static final String INCLUDE_ME = "meTo";
	public static final String FILE = "file";
	public static final String PAGE_OF_DOWNLOAD = "pageOfDownload";
	public static final String REPORTS_DIR_NAME = "/ScheduledReportsData";
	public static final String QUERIES_DIR_NAME = "/ScheduledQueriesData";
	public static final String SCHEDULE_REPORT = "Report";
	public static final String SCHEDULE_QUERY = "Query";
	public static final String CLEANUP_THRESHOLD = "scheduler.cleanUp.timeInterval.days";
	public static final String SCHEDULE_TYPES = "schedule.types";
	public static final String EXECUTION_TIME_HRS = "scheduler.execution.time.hrs";
	public static final String EXECUTION_TIME_MIN = "scheduler.execution.time.min";
	public static final String ZIP_EXTENSION = ".zip";
	public static final String HOST_USER_RETRIEVER = "host.user.retrieval.implName";
	public static final ArrayList<String> SCHEDULER_PROPERTIES_LIST = new ArrayList<String>(
			Arrays.asList("scheduler.threadPool.size", "scheduler.execution.time.hrs",
					"scheduler.execution.time.min", "schedule.types", "host.app.url",
					"host.user.retrieval.implName", "host.mail.alias",
					"scheduler.cleanUp.timeInterval.days", "scheduler.mail.subject",
					"scheduler.mail.header", "scheduler.mail.end"));
	public static final ArrayList<String> DB_DETAILS_LIST = new ArrayList<String>(Arrays.asList(
			"database.type", "database.host", "database.port", "database.name",
			"database.userName", "database.password"));

}
