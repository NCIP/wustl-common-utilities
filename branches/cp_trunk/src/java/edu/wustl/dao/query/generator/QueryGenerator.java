package edu.wustl.dao.query.generator;


/**
 * @author kalpana_thakur
 * It will be used to generate SQL queries like insert,update.
 * It accept the query data and parse the data to create insert, update query.
 */
public interface QueryGenerator
{

	/**
	 * Parse the QueryData and creates the insert query.
	 * @return : the insert query.
	 */
	String getInsertQuery();

	/**
	 * Parse the QueryData and creates the update query.
	 * @return :
	 */
	String getUpdateQuery();

	/**
	 * This method will be called to set the Query data.
	 * @param queryData queryData.
	 */
	void setQueryData(QueryData queryData);

}
