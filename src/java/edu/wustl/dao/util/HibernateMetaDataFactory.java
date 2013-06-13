/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.dao.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.Configuration;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.exception.DAOException;

/**
 * This class keeps a map of all the HibernateMetaData instances for all the
 * applications.
 * @author Shrishail_kalshetty
 */
public final class HibernateMetaDataFactory
{
	/**
	 * Map to cache all the instances of HibernateMetaData.
	 */
	public static final Map<String, HibernateMetaData> metaDataCache=
		new HashMap<String, HibernateMetaData>();
	/**
	 * This method adds the Configuration to the map for the given application.
	 * @param appName appName
	 * @param cfg cfg
	 */
	public static void setHibernateMetaData(String appName, Configuration cfg)
	{
		HibernateMetaData hibernateMetaData=metaDataCache.get(appName);
		if(hibernateMetaData==null)
		{
			hibernateMetaData=new HibernateMetaData(cfg);
			metaDataCache.put(appName, hibernateMetaData);
		}
	}
	/**
	 * private instance.
	 */
	private HibernateMetaDataFactory()
	{

	}

	/**
	 * This method returns the Configuration given the application name.
	 * @param appName appName
	 * @return HibernateMetaData HibernateMetaData
	 * @throws DAOException database Exception
	 */
	public static HibernateMetaData getHibernateMetaData(String appName) throws DAOException
	{
		HibernateMetaData metadata = null;
		if(appName!=null)
		{
			metadata=metaDataCache.get(appName);
		}
		if(appName == null || metadata == null)
		{
			throw new DAOException(ErrorKey.getErrorKey
					("problem.in.fetching.hibernate.metadata"),null, "");
		}
		return metadata;
	}
}
