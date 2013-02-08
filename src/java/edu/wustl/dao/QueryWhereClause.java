/**
 * <p>Title: QueryWhereClause Class>
 * <p>Description:	QueryWhereClause used to create where clause of Query.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana_thakur
 * @version 1.00
 */package edu.wustl.dao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.condition.Condition;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;
import edu.wustl.dao.util.DAOUtility;

/**
 * @author kalpana_thakur
 *
 */

public class QueryWhereClause
{
	/**
	 * Class Logger.
	 */
	private static Logger logger = Logger.getCommonLogger(AbstractJDBCDAOImpl.class);
	/**
	 * This will hold the complete where clause of query.
	 */
	private final StringBuffer whereClauseBuff;

	/**
	 * Class name or table name.
	 */
	private String sourceObjectName = "";

	/**
	 * The public constructor to restrict creating object without
	 * initializing mandatory members.
	 * It will instantiate the whereClause buff.
	 * @param sourceObjectName : name of the class or table.
	 */
	public QueryWhereClause(String sourceObjectName)
	{
		whereClauseBuff = new StringBuffer();
		whereClauseBuff.append(DAOConstants.TRAILING_SPACES)
		.append("WHERE").append(DAOConstants.TRAILING_SPACES);
		this.sourceObjectName = sourceObjectName;
	}

	/**
	 * It will instantiate the whereClause buff.
	 */
	/*public QueryWhereClause()
	{
		whereClauseBuff = new StringBuffer();
		whereClauseBuff.append(DAOConstants.TAILING_SPACES)
		.append("WHERE").append(DAOConstants.TAILING_SPACES);
	}*/

	/**
	 * @return :
	 */
	public String toWhereClause()
	{
		return whereClauseBuff.toString();
	}


	/**
	 * @return :And join condition.
	 */
	public QueryWhereClause andOpr()
	{
		whereClauseBuff.append(DAOConstants.AND_JOIN_CONDITION).toString();
		return this;
	}

	/**
	 * @return :Or join condition.
	 */
	public QueryWhereClause orOpr()
	{
		whereClauseBuff.append(DAOConstants.OR_JOIN_CONDITION).toString();
		return this;
	}



	/**
	 * This will be called to generate condition clause sql.
	 * It will also set the table or class name if required.
	 * @param condition :
	 * @return the QueryWhereClause object.
	 * @throws DAOException database exception.
	 */
	public QueryWhereClause addCondition(Condition condition)throws DAOException
	{
		try
		{
			if(condition.getSourceObjectName() == null)
			{
				condition.setSourceObjectName(sourceObjectName);
			}
			whereClauseBuff.append(condition.buildSql());
		}

		catch(DAOException exp)
		{
			logger.info(exp.getMessage(),exp);
			throw DAOUtility.getInstance().getDAOException(exp,
					"db.query.condition.gen.error","QueryWhereClause.java");
		}
		return this;
	}

	/**
	 * This method is catissuecore application specific,it is used to generate complete where clause
	 * of the query.It reads the QueryConditionMap<condition,class name/> having key as condition and
	 * value as class specific to condition.
	 * @param whereColumnName : It holds the column names
	 * @param whereColumnCondition : It holds the conditions like "in","=" etc
	 * @param whereColumnValue : It holds the column values
	 * @param joinCondition : It holds the join condition.
	 * @return : the complete where clause of the query.
	 * @throws DAOException : Database Exception.
	 * It iterates the whereCondition clause reads the class name from the map as per the key ,
	 * instantiate the object and generates the condition clause.
	 * @deprecated : Create where clause using Condition interface.
	 */
	public QueryWhereClause getWhereCondition(String[] whereColumnName, String[]
	       whereColumnCondition, Object[] whereColumnValue,	String joinCondition) throws DAOException
	{	Map<String,String> queryConMap = getWhereClauseCondMap();
		int counter = 0;
		try
		{	for(int index=0 ;index<whereColumnCondition.length;index++)
			{
				Condition condition = null;
				Class conditionClass = Class.forName(queryConMap.get(whereColumnCondition[index])
						.toString());
				if(whereColumnCondition[index].contains(DAOConstants.IN_CONDITION) ||
				   whereColumnCondition[index].contains(DAOConstants.NOT_IN_CONDITION))
				{
					Constructor constructor =	conditionClass.getConstructor(new Class[]
					                         	       {String.class  ,Object[].class} );
					condition = (Condition)constructor.newInstance(new Object[] {
							whereColumnName[index],whereColumnValue[index] } );
				}
				else if(whereColumnCondition[index].contains(DAOConstants.EQUAL) ||
					whereColumnCondition[index].contains(DAOConstants.NOT_EQUAL))
				{
					Constructor constructor = conditionClass.getConstructor(new Class[]
					                                  {String.class  ,Object.class } );
					condition = (Condition)constructor.newInstance(new Object[] {
							whereColumnName[index],whereColumnValue[index]  } );
				}
				else
				{
					Constructor constructor = conditionClass.getConstructor(new Class[]
					                               {String.class } );
					condition = (Condition)constructor.newInstance(new Object[] {
								whereColumnName[index]} );
				}
				if(condition.getSourceObjectName() == null)
				{
					condition.setSourceObjectName(sourceObjectName);
				}
				whereClauseBuff.append(condition.buildSql());
				if(counter < whereColumnCondition.length-1)
				{
					whereClauseBuff.append(joinCondition);
				}
				counter++;
			}
		}
		catch(Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			ErrorKey errorKey = ErrorKey.getErrorKey("db.operation.error");
			throw new DAOException(errorKey,exp,"QueryWhereClause.java :");
		}
		return this;
	}

	/**
	 * This is specific to catissuecore application.
	 * @return QueryConditionMap<condition,class name/> having key as condition and
	 * value as class specific to condition.
	 */
	private Map<String, String> getWhereClauseCondMap()
	{
		Map<String, String> queryCondMap = new HashMap<String, String>();
		queryCondMap.put(DAOConstants.IN_CONDITION, "edu.wustl.dao.condition.INClause");
		queryCondMap.put(DAOConstants.NOT_IN_CONDITION, "edu.wustl.dao.condition.NotINClause");
		queryCondMap.put(DAOConstants.EQUAL, "edu.wustl.dao.condition.EqualClause");
		queryCondMap.put(DAOConstants.NOT_EQUAL, "edu.wustl.dao.condition.NotEqualClause");
		queryCondMap.put(DAOConstants.NOT_NULL_CONDITION, "edu.wustl.dao.condition.NotNullClause");
		queryCondMap.put(DAOConstants.NULL_CONDITION, "edu.wustl.dao.condition.NullClause");

		return queryCondMap;

	}
}
