/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/**
 * <p>Title: DatabaseConnectionParams Class>
 * <p>Description:	DatabaseConnectionParams handles opening closing ,initialization of all database specific
 * parameters  .</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.dao.util;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author kalpana_thakur
 * This class is specific to hibernate.
 */

public final class HibernateMetaData
{

	/**
	 * creates a single object.
	 */
	private static HibernateMetaData hibernateMData ; //= new HibernateMetaData();;
	/**
	 * Private constructor.
	 */
	protected HibernateMetaData(Configuration cfg)
	{
		this.cfg = cfg ;
	}
	/**
	 * returns the single object.
	 * @return Utility object
	 */
	public static HibernateMetaData getInstance()
	{
		return hibernateMData;
	}

	/**
	 * Configuration file .
	 */
	private Configuration cfg;


	/**
	 * This method will return domain object from proxy Object.
	 * @param domainObject :
	 * @return domain Object :
	 */
	public static Object getProxyObjectImpl(Object domainObject)
	{
		Object object = domainObject;
		if (domainObject instanceof HibernateProxy)
		{
			HibernateProxy hiberProxy  = (HibernateProxy)domainObject;
			object = hiberProxy.getHibernateLazyInitializer().getImplementation();
		}
        return object;
	}

	/**
	 * This method will be called to return table name for the given class.
	 * @param classObj : Class of the object
	 * @return tableName : It returns the table name associated to the class.
	 */
	public String getTableName(Class classObj)
	{

		String tableName = "";
		Table tbl = this.cfg.getClassMapping(classObj.getName()).getTable();
		if(tbl!=null)
		{
			tableName = tbl.getName();
		}
		return tableName;

	}
	/**
	 * This method will be called to return root table name for the given class.
	 * @param classObj :Class of the object
	 * @return :It returns the root table name associated to the class.
	 */
	public String getRootTableName(Class classObj)
	{
		String rootTableName = "";
		Table tbl = this.cfg.getClassMapping(classObj.getName()).getRootTable();
		if(tbl!=null)
		{
			rootTableName = tbl.getName();
		}
		return rootTableName;

	}

	/**
	 * This will return the column name mapped to given attribute of the given class.
	 * @param classObj : Class of the object
	 * @param attributeName :attribute of the given class
	 * @return returns the Column Name mapped to the attribute.
	 */
	public String getColumnName(Class classObj, String attributeName)
	{
		String columnName = DAOConstants.TRAILING_SPACES;
		boolean foundColName = false;
		Iterator<Object> iter = this.cfg.getClassMapping(classObj.getName()).getPropertyClosureIterator();
		while(iter.hasNext())
		{
			columnName = getColumnName(attributeName,iter);
			if(!DAOConstants.TRAILING_SPACES.equals(columnName))
			{
				foundColName = true;
				break;
			}
		}

		if(!foundColName)
		{
			columnName = getColumName(classObj, attributeName);
		}
		return columnName;
	}
	/**
	 * This method will returns the column name associate to given property.
	 * @param iter : holds the property object.
	 * @param attributeName :attribute of the given class
	 * @return returns the Column Name mapped to the attribute.
	 */
	private static String getColumnName(String attributeName,
			Iterator<Object> iter)
	{
		String columnName = DAOConstants.TRAILING_SPACES;
		Property property = (Property)iter.next();
		if(property!=null && property.getName().equals(attributeName))
		{
			Iterator<Object> colIt = property.getColumnIterator();
			if(colIt.hasNext())
			{
				Column col = (Column)colIt.next();
				columnName = col.getName();
			}
		}
		return columnName;
	}
	/**
	 *This method will iterate the Identified property file and returns the column name.
	 * @param classObj : Class of the object
	 * @param attributeName :attribute of the given class
	 * @return returns the Column Name mapped to the attribute.
	 */
	private String getColumName(Class classObj, String attributeName)
	{
		String columnName = DAOConstants.TRAILING_SPACES;
		Property property = this.cfg.getClassMapping(classObj.getName()).getIdentifierProperty();
		if(property.getName().equals(attributeName))
		{
			Iterator<Object> colIt = property.getColumnIterator();//y("id").getColumnIterator();
			if(colIt.hasNext())
			{
				Column col = (Column)colIt.next();
				columnName = col.getName();
			}
		}
		return columnName;
	}

	/**
	 * This method will be called to obtained column width of attribute field of given class.
	 * @param classObj Name of the class.
	 * @param attributeName Name of the attribute.
	 * @return The width of the column. Returns width of the column or zero.
	 */
	public int getColumnWidth(Class classObj, String attributeName)
	{
		Iterator iterator = this.cfg.getClassMapping(classObj.getName()).getPropertyClosureIterator();
		int colLength = 50;
		while(iterator.hasNext())
		{
			Property property = (Property)iterator.next();

			if(property!=null && property.getName().equals(attributeName))
			{
				Iterator colIt = property.getColumnIterator();
				while(colIt.hasNext())
				{
					Column col = (Column)colIt.next();
					colLength = col.getLength();
				}
			}
		}
		// if attribute is not found than the default width will be 50.
		return colLength;
	} // getColumnWidth


	/**
	 * This method will be called to obtained the
	 * domain class name mapped to given table.
	 * @param tableName :Name of table.
	 * @return the class name
	 */
	public String getClassName(String tableName)
	{
		Iterator iter = this.cfg.getClassMappings();
		PersistentClass persistentClass;
		String className = DAOConstants.TRAILING_SPACES;
		while(iter.hasNext())
		{
			persistentClass = (PersistentClass) iter.next();
			if(tableName.equalsIgnoreCase(persistentClass.getTable().getName()))
			{
				className = persistentClass.getClassName();
			}
		}

		return className;
	}

	/**
	 * This method will be called to obtained the
	 * persistent class mapped to given class.
	 * @param className : Class name.
	 * @return the class name
	 */
	public PersistentClass getPersistentClass(String className)
	{
		PersistentClass persistentClass = this.cfg.getClassMapping(className);
		return persistentClass;
	}



}