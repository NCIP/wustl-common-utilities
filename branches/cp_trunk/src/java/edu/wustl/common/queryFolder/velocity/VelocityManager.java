
package edu.wustl.common.queryFolder.velocity;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author amol
 * 
 */
public class VelocityManager
{

	private static VelocityManager velocityManager;
	private static VelocityEngine velocityEngine;
	private static final String GRID_POPULATE_OBJECT_LIST = "gridPopulateObjectList";
	private static final String VELOCITY_CLASSPATH_RESOURCE_LOADER = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
	private static final String CLASS_RESOURCE_LOADER_CLASS = "class.resource.loader.class";
	private static final String RESOURCE_LOADER = "resource.loader";
	private static final String CLASS = "class";

	/**
	 * Singleton class
	 * 
	 * @throws Exception
	 */
	private VelocityManager() throws Exception
	{
		initializeVelocityEngine();
	}

	/**
	 * replaces Object value in the template and gives the resultant o/p
	 * 
	 * @param gridPopulateObjectList
	 * @param templateFileName
	 * @return
	 * @throws Exception
	 */
	public String evaluate(List<?> gridPopulateObjectList, String templateFileName)
			throws Exception
	{
		Template template = velocityEngine.getTemplate(templateFileName);
		VelocityContext context = new VelocityContext();
		context.put(GRID_POPULATE_OBJECT_LIST, gridPopulateObjectList);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	public static VelocityManager getInstance() throws Exception
	{
		if (velocityManager == null)
		{
			velocityManager = new VelocityManager();
		}
		return velocityManager;
	}

	private void initializeVelocityEngine() throws Exception
	{
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RESOURCE_LOADER, CLASS);
		velocityEngine.setProperty(CLASS_RESOURCE_LOADER_CLASS, VELOCITY_CLASSPATH_RESOURCE_LOADER);
		velocityEngine.init();
	}
}
