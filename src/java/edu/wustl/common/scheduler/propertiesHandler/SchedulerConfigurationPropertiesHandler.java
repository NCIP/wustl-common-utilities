
package edu.wustl.common.scheduler.propertiesHandler;

import java.util.HashMap;
import java.util.Map;
import edu.wustl.common.scheduler.util.ISchedulerPropertiesFetcher;

public class SchedulerConfigurationPropertiesHandler
{

	private static SchedulerConfigurationPropertiesHandler schedulerProperties;
	private static Map<String, Object> globalProperties = new HashMap<String, Object>();

	/**
	 * @throws Exception
	 */
	private SchedulerConfigurationPropertiesHandler() throws Exception
	{
		/*InputStream inStream = SchedulerTag.class.getClassLoader().getResourceAsStream(
				SchedulerConstants.PROPERTIES_FILE_NAME);*/
		ISchedulerPropertiesFetcher propertiesFetcher = (ISchedulerPropertiesFetcher) Class
				.forName("edu.wustl.clinportal.scheduleImpls.SchedulerPropertiesFetcherImpl")
				.newInstance();
		globalProperties.putAll(propertiesFetcher.getSchedulerPropertiesMap());
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static synchronized SchedulerConfigurationPropertiesHandler getInstance()
			throws Exception
	{
		if (schedulerProperties == null)
		{
			schedulerProperties = new SchedulerConfigurationPropertiesHandler();
		}
		return schedulerProperties;
	}

	/**
	 * @param key
	 * @return
	 */
	public synchronized Object getProperty(String key)
	{
		Object property = null;
		if (globalProperties.containsKey(key))
		{
			property = globalProperties.get(key);
		}
		return property;
	}

}
