/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDEConConfig
{
	/**
	 * 	used for connection.
	 */
    public static String proxyhostip;
    /**
     * Proxy Port.
     */
    public static String proxyport;
    /**
     * User name.
     */
    public static String username;
    /**
     * Password.
     */
    public static String password;

	/**
	 * 	the dbserver to connect.
	 */
    public static String dbserver;
    /**
     * @return Returns the dbserver.
     */
    public String getDbserver()
    {
        return dbserver;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * @return Returns the proxyhostip.
     */
    public String getProxyhostip()
    {
        return proxyhostip;
    }
    /**
     * @return Returns the proxyport.
     */
    public String getProxyport()
    {
        return proxyport;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }
//	public CDEConConfig(String proxyhostip, String proxyport,
    //String username, String password, String dbserver)
//	{
//		this.proxyhostip = proxyhostip;
//		this.proxyport = proxyport;
//		this.username = username;
//		this.password = password;
//		this.dbserver = dbserver;
//	} // CDEConConfig constructor

}
