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
 * Created on Nov 30, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package edu.wustl.common.util.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.beans.NameValueBean;

/**
 * @author ajay_sharma
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * This class is specific to common files. It is used by common package.
 * */

public final class Variables
{

	/**
	 * private constructor.
	 */
	private Variables()
	{

	}
	/**
	 *  Ravi : for Multi Site Rep. changes.
	 *  specify privilege Details Map.
	 */
	public static final Map<String, String> privilegeDetailsMap = new HashMap<String, String>();

	/**
	 * specify privilegeGroupingMap.
	 */
	public static final Map<String, List<NameValueBean>> privilegeGroupingMap
	= new HashMap<String, List<NameValueBean>>();
}