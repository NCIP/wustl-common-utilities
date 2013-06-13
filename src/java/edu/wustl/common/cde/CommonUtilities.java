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
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;

import java.util.StringTokenizer;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommonUtilities
{
    /**
     *
     * @param num The String value that is to be used as a number
     *  the number to be in range of min and max.
     *  Any other number will beconsidered as general number validation.
     * @param min Indicates the minimum value for the number.
     * @param max Indicates the maximum value for the number.
     * @return It returns a Boolean value depending on the value passed as a num parameter.
     */
    public static boolean checknum(String num, int min, int max)
    {
    	boolean flag = true ;
        try
        {
        	//Integer value = new Integer(num);
            Integer value = Integer.valueOf(num);
            if ((min < max))
            {
                int chkValue = value.intValue();
                flag = checkValue(chkValue,min,max) ;
            }
        } // try
        catch (NumberFormatException nfe)
        {
            //return false;
        	flag = false ;
        } // catch
        catch (Exception e1)
        {
            //return false;
        	flag = false ;
        } // catch
        //return true ;
        return flag;
    } // checknum
//------------------------------------------------
    /**
     *
     * @param ipadd  It contains the IPAddress in String format.This value will be verified for IPAddress.
     * IPAddress will be in the format: A.B.C.D where A,B,C,D all are integers in the range 0 to 255.
     * @return Returns boolean value depending on the ipaddress provided.
     */
    public static boolean isvalidIP(String ipadd)
    {
        StringTokenizer strToken = new StringTokenizer(ipadd, ".");
        int tokenCnt = strToken.countTokens();
        boolean flag = true ;
        if (tokenCnt == 4)
        {
            String ipPart[] = new String[4];
            int incr = 0;
            while (strToken.hasMoreTokens())
            {
                ipPart[incr] = strToken.nextToken();
                incr++;
            }
            for (int z = 0; z < ipPart.length; z++)
            {
                if (!(checknum(ipPart[z], 0, 255)))
                {
                	flag = false ;
                	break ;
                    //return false;
                }
            }
            //return true;
        } // if
        else
        {
        	flag = false ;
        }
        //return false;
        return flag ;
    } //isvalidip
    /**
     *
     * @param chkValue The value to check.
     * @param min Minimum value
     * @param max Maximum value
     * @return boolean result.
     */
    public static boolean checkValue(int chkValue,int min,int max)
    {
    	boolean flag = true ;
    	if (!(chkValue >= min) && !(chkValue <= max))
        {
            //return false;
        	flag = false ;
        }
    	return flag ;
    }
}//CommonUtilities
