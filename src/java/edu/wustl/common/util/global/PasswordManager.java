/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/wustl-common-utilities/LICENSE.txt for details.
 */

package edu.wustl.common.util.global;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;
import gov.nih.nci.security.util.StringEncrypter;
import gov.nih.nci.security.util.StringEncrypter.EncryptionException;

/**
 * Removed all logger statement in this file because
 * its main can be called from outside.
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public final class PasswordManager
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getCommonLogger(PasswordManager.class);
	/**
	 * private constructor.
	 */
	private PasswordManager()
	{
	}
	/**
	 * Constant for TEN.
	 */
	private static final int TEN = 10;
	/**
	 * Constant for TWO.
	 */
	private static final int TWO = 2;
	/**
	 * Constant for FOUR.
	 */
	private static final int FOUR = 4;
	/**
	 * Key used for password encryption.
	 */
	private static final String KEY = "AWelcomeTocaTISSUECOREOfThisIsThe" +
			"FirstReleaseOfcaTISSUECOREDevelopedByWashUAtPersistentSystemsPrivateLimited";
	/**
	 * array used for password encryption.
	 */
	private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f'};
	/**
	 *  constant for MINIMUM_PASSWORD_LENGTH.
	 */
	public static final String MINIMUM_PASSWORD_LENGTH = "minimumPasswordLength";
	public static final String PASSWORD_NOT_SAMEAS_LAST_N="password.not_same_as_last_n";
	/**
	 *  constant for USER_CLASS_NAME.
	 */
	public static final String USER_CLASS_NAME = "edu.wustl.common.domain.User";
	/**
	 * Code for no error.
	 */
	public static final int NOT_FAILED = -1;
	/**
	 * specify Success.
	 */
	public static final int SUCCESS = 0;
	/**
	 * specify fail password length.
	 */
	public static final int FAIL_LENGTH = 1;
	/**
	 * specify fail password same as old.
	 */
	public static final int FAIL_SAME_AS_OLD = 2;
	/**
	 * specify fail password same as user-name.
	 */
	public static final int FAIL_SAME_AS_USERNAME = 3;
	/**
	 * specify fail password in pattern.
	 */
	public static final int FAIL_IN_PATTERN = 4;
	/**
	 * specify fail same session.
	 */
	public static final int FAIL_SAME_SESSION = 5;
	/**
	 * specify fail wrong old password.
	 */
	public static final int FAIL_WRONG_OLD_PASSWORD = 6;
	/**
	 *  specify fail invalid session.
	 */
	public static final int FAIL_INVALID_SESSION = 7;
	public static final int FAIL_IN_UCASE=8;
	public static final int FAIL_IN_LCASE=9;
	public static final int FAIL_IN_NUMBER=10;
	public static final int FAIL_IN_SPACE=11;
	/**
	 *  15 in hexa-decimal.
	 */
	public static final int HEX_15 = 0x0f;
	/**
	 *  240 in hexa-decimal.
	 */
	public static final int HEX_240 = 0xf0;
	/**
	 * This map contains error message for different error code.
	 */
	private static Map<Integer, String> errorMess;

	/**
	 * Generate random alpha numeric password.
	 * @return Returns the generated password.
	 */
	public static String generatePassword()
	{
		//Define a Constants alpha-numeric String
		final String upperCharString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String lowerCharString = "abcdefghijklmnopqrstuvwxyz";
		final String digitString = "0123456789";

		Random random = new Random();
		StringBuffer passwordBuff = new StringBuffer();
		//This password must satisfy the following criteria:New Password must include at least one
		//Upper Case, Lower Case letter and a Number. It must not include Space.
		for (int i = 0; i < TWO; i++)
		{
			//Generate a random number from 0(inclusive) to lenght of CHAR_STRING(exclusive).
			//Get the character corrosponding to random number and append it to password buffer.
			int randomVal = random.nextInt(upperCharString.length());
			passwordBuff.append(upperCharString.charAt(randomVal));
			randomVal = random.nextInt(lowerCharString.length());
			passwordBuff.append(lowerCharString.charAt(randomVal));
			randomVal = random.nextInt(digitString.length());
			passwordBuff.append(digitString.charAt(randomVal));
		}
		return passwordBuff.toString();
	}
	/**
	 * specify stringEncrypter.
	 */
	private static StringEncrypter stringEncrypter = null;
	/**
	 * TO get the instance of StringEncrypter class.
	 * @return The Object reference of StringEncrypter class.
	 * @throws EncryptionException generic EncryptionException
	 */
	private static StringEncrypter getEncrypter() throws EncryptionException
	{
		if (stringEncrypter == null)
		{
			stringEncrypter = new StringEncrypter();
		}
		return stringEncrypter;
	}
	/**
	 * TO get Encrypted password for the given password.
	 * @param password The password to be encrypted.
	 * @return The Encrypted password for the given password.
	 * @throws PasswordEncryptionException generic PasswordEncryptionException
	 */
	public static String encrypt(String password) throws PasswordEncryptionException
	{
		try
		{
			return getEncrypter().encrypt(password);
		}
		catch (EncryptionException e)
		{
			throw new PasswordEncryptionException(e.getMessage(), e);
		}
	}
	/**
	 * TO get Decrypted password for the given password.
	 * @param password The password to be Decrypted.
	 * @return The Decrypted password for the given password.
	 * @throws PasswordEncryptionException generic PasswordEncryptionException
	 */
	public static String decrypt(String password) throws PasswordEncryptionException
	{
		try
		{
			return getEncrypter().decrypt(password);
		}
		catch (EncryptionException e)
		{
			throw new PasswordEncryptionException(e.getMessage(), e);
		}
	}
	/**
	 * This method encode input string.
	 * @param input string
	 * @return string
	 */
	@Deprecated
	public static String encode(String input)
	{
		StringBuffer inString = getInString(input);
		StringBuffer stringBuffer = getEncodedString(inString);
		return stringBuffer.toString();

	}
	/**
	 * @param inString a string for encryption.
	 * @return Encoded string.
	 */
	private static StringBuffer getEncodedString(StringBuffer inString)
	{
		byte[] bytes = inString.toString().getBytes();
		StringBuffer stringBuffer = new StringBuffer(bytes.length * TWO);

		for (int i = 0; i < bytes.length; i++)
		{
			byte singleByte = bytes[i];
			stringBuffer.append(DIGITS[(singleByte & HEX_240) >> FOUR]);
			stringBuffer.append(DIGITS[singleByte & HEX_15]);
		}
		return stringBuffer;
	}
	/**
	 * Create a string for encryption.
	 * @param input input-string.
	 * @return a string for encryption.
	 */
	private static StringBuffer getInString(String input)
	{
		StringBuffer inString = new StringBuffer();
		for (int i = 0; i < input.length(); i++)
		{
			inString.append(input.charAt(i));
			inString.append(KEY.charAt(i));
		}
		return inString;
	}
	/**
	 * This method decode string.
	 * @param decodeString string to decode
	 * @return  string
	 */
	@Deprecated
	public static String decode(String decodeString)
	{
		byte[] bytes = getStringAsBytes(decodeString);
		String sin = new String(bytes);
		StringBuffer sout = new StringBuffer();
		for (int i = 0; i < sin.length(); i += TWO)
		{
			sout.append(sin.substring(i, i + 1));
		}
		return sout.toString();
	}
/**
	 * @param decodeString String to be decoded.
	 * @return string as byte array.
	 */
	private static byte[] getStringAsBytes(String decodeString)
	{
		int len = decodeString.length();
		byte[] bytes = new byte[len / TWO];
		for (int i = 0; i < bytes.length; i++)
		{
			int digit1 = decodeString.charAt(i * TWO);
			int digit2 = decodeString.charAt(i * TWO + 1);
			digit1 = getEncodedDigit(digit1);
			digit2 = getEncodedDigit(digit2);
			bytes[i] = (byte) ((digit1 << FOUR) + digit2);
		}
		return bytes;
	}
	/**
	 * @param digit digit to be encoded.
	 * @return encoded digit.
	 */
	private static int getEncodedDigit(int digit)
	{
		int encodedDigit = digit;
		if ((encodedDigit >= '0') && (encodedDigit <= '9'))
		{
			encodedDigit -= '0';
		}
		else if ((encodedDigit >= 'a') && (encodedDigit <= 'f'))
		{
			encodedDigit -= 'a' - TEN;
		}
		return encodedDigit;
	}
	/**
	 * This method returns the validation results on Form Bean. This method should be called
	 * from validate method of form bean.
	 * @param newPassword New Password value
	 * @param oldPassword Old Password value
	 * @param pwdChangedInsameSession : its value will come from session and passed to this method like,
	 * (Boolean) httpSession .getAttribute(Constants.PASSWORD_CHANGE_IN_SESSION);
	 * @return SUCCESS if all condition passed
	 *   else return respective error code (constant int) value
	 */
	public static int validatePasswordOnFormBean(String newPassword, String oldPassword)
	{
		int errorNo = NOT_FAILED;
		errorNo = chkPassForMinLength(newPassword, errorNo);
		errorNo = getErrorNumber(newPassword, errorNo);
		return errorNo;
	}
	
	/**
	 * @param newPassword New Password.
	 * @param erNo error number-method body executes only when there is no error i.e. -1
	 * @return int error number or -1
	 */
	private static int chkPassForMinLength(String newPassword, int erNo)
	{
		int erroNo = erNo;
		if (NOT_FAILED == erNo)
		{
			int minimumPasswordLength = Integer.parseInt(XMLPropertyHandler
					.getValue(MINIMUM_PASSWORD_LENGTH));
			// to Check length of password,if not valid return FAIL_LENGTH
			if (newPassword.length() < minimumPasswordLength)
			{
				erroNo = FAIL_LENGTH;
			}
		}
		return erroNo;
	}
	
	/**
	 * following code checks pattern i.e password must include atleast one UCase,LCASE and Number
	 * and must not contain space character.
	 * @param newPassword New password
	 * @return int error number or -1
	 */
	private static int validateUpLowerNumSpaceCharInPass(String newPassword)
	{
		int erroNo = NOT_FAILED;
		final String regExpUpperCase = "^.*\\p{Upper}.*$";
		final String regExpLowerCase = "^.*\\p{Lower}.*$";
		final String regExpDigit = "^.*\\p{Digit}.*$";
		final String regExpSpace = "^.*\\p{Space}.*$";

		//boolean to check UCase character found in string
		boolean foundUCase = Pattern.matches(regExpUpperCase, newPassword);
		//boolean to check LCase character found in string
		boolean foundLCase = Pattern.matches(regExpLowerCase, newPassword);
		//boolean to check Digit/Number character found in string
		boolean foundNumber = Pattern.matches(regExpDigit, newPassword);
		//boolean to check space in String
		boolean foundSpace = Pattern.matches(regExpSpace, newPassword);

		// condition to check whether all above condotion is satisfied
		if(!foundSpace)
		{
		
			if(!foundUCase){
				return erroNo = FAIL_IN_UCASE;
			}
			if(!foundLCase){
				return erroNo = FAIL_IN_LCASE;
			}
			if(!foundNumber){
				return erroNo = FAIL_IN_NUMBER;
			}
		}
		else
		{
			erroNo = FAIL_IN_SPACE;
		}
		return erroNo;
	}
	/**
	 * This method combines UI validation and business rules validation which is incorrect.
	 * Call validatePasswordOnFormBean for Form bean validations.
	 * Write your own methods for business validations.
	 * @param newPassword New Password value
	 * @param oldPassword Old Password value
	 * @param pwdChangedInsameSession : this will come from session like
	 * (Boolean) httpSession .getAttribute(Constants.PASSWORD_CHANGE_IN_SESSION);
	 * @param sessionData : SessionDataBean this will come from session.
	 * httpSession.getAttribute(Constants.SESSION_DATA)
	 * @return SUCCESS (constant int 0) if all condition passed
	 *   else return respective error code (constant int) value
	 */
	public static int validate(String newPassword, String oldPassword,SessionDataBean sessionData)
	{
		int errorNo = NOT_FAILED;
		String userName = "";
		if (sessionData == null)
		{
			errorNo = FAIL_INVALID_SESSION;
		}
		else
		{
			userName = sessionData.getUserName();
		}
		errorNo = validateOldPassword(oldPassword, errorNo, userName);
		errorNo = chkPassForMinLength(newPassword, errorNo);
		errorNo = getErrorNumber(newPassword, errorNo);
		return errorNo;
	}
	/**
	 * @param newPassword new Password
	 * @param errorNo error No.
	 * @return error Number
	 */
	private static int getErrorNumber(String newPassword, int errorNo)
	{
		int errNumber = errorNo;
		if (NOT_FAILED == errNumber)
		{
			errNumber = validateUpLowerNumSpaceCharInPass(newPassword);
		}
		if (NOT_FAILED == errNumber)
		{
			errNumber = SUCCESS;
		}
		return errNumber;
	}
	
	
	/**
	 * @param oldPassword Old Password.
	 * @param erNo error number-method body executes only when there is no error i.e. -1
	 * @param userName User Name
	 * @return int: error number or -1
	 */
	private static int validateOldPassword(String oldPassword, int erNo, String userName)
	{
		int errorNo = erNo;
		if (NOT_FAILED == erNo)
		{
			// to check whether user entered correct old password
			try
			{
				String password = getOldPassword(userName);
				//compare password stored in database with value of old password
				//currently entered by user for Change Password operation
				if (!oldPassword.equals(PasswordManager.decode(password)))
				{
					errorNo = FAIL_WRONG_OLD_PASSWORD; //retun value is int 6
				}
			}
			catch (Exception e)
			{
				// if error occured during password comparision
				errorNo = FAIL_WRONG_OLD_PASSWORD;
			}
		}
		return errorNo;
	}
	/**
	 * @param userName user Name.
	 * @return old password of a user.
	 * @throws DAOException database exception.
	 * @throws BizLogicException BizLogicException
	 */
	private static String getOldPassword(String userName) throws DAOException, BizLogicException
	{
		// retrieve User DomainObject by user name
		IBizLogic bizLogic = new DefaultBizLogic();
		String[] selectColumnNames = {"password"};
		String[] whereColumnNames = {"loginName"};
		String[] whereColumnCondition = {"="};
		String[] whereColumnValues = {userName};

		//Gautam_COMMON_TEMP_FIX USER_CLASS_NAME
		List userList = bizLogic.retrieve(USER_CLASS_NAME, selectColumnNames, whereColumnNames,
				whereColumnCondition, whereColumnValues, null);
		String password = null;
		if (userList != null && !userList.isEmpty())
		{
			password = (String) userList.get(0);
		}
		return password;
	}
	/**
	 * Error message.
	 * @param errorCode int value return by validate() method
	 * @return String error message with respect to error code
	 */
	public static String getErrorMessage(int errorCode)
	{
		String errMsg;
		if(getErrorMessMap()==null)
		{
			errMsg = ApplicationProperties.getValue("errors.newPassword.genericmessage");
		}
		else
		{
			errMsg = errorMess.get(errorCode);
		}
		return errMsg;
	}
	/**
	 * This methods take a decision whether map can be initialized or not and accordingly
	 * call a method initErrorMessMap.
	 * @return Map Error message map.
	 */
	private static Map<Integer, String> getErrorMessMap()
	{
		Map<Integer, String> map=errorMess;
		if(errorMess==null && !XMLPropertyHandler.isDocumentNull())
		{
			initErrorMessMap();
		}
		return map;
	}
	/**
	 * This method initialize the error message map.
	 */
	private static void initErrorMessMap()
	{
		errorMess = new HashMap<Integer, String>();
		int minimumPasswordLength = Integer.parseInt(XMLPropertyHandler.getValue(MINIMUM_PASSWORD_LENGTH));
			List<String> placeHolders = new ArrayList<String>();
		placeHolders.add(Integer.valueOf(minimumPasswordLength).toString());
		
		
		String errorMsg = ApplicationProperties.getValue("errors.newPassword.length", placeHolders);
			errorMess.put(FAIL_LENGTH, errorMsg);
		errorMess.put(FAIL_SAME_AS_OLD, ApplicationProperties.getValue("errors.newPassword.sameAsOld"));
		errorMess.put(FAIL_SAME_AS_USERNAME, ApplicationProperties.getValue("errors.newPassword.sameAsUserName"));
		
		errorMess.put(FAIL_SAME_SESSION, ApplicationProperties.getValue("errors.newPassword.sameSession"));
		errorMess.put(FAIL_WRONG_OLD_PASSWORD, ApplicationProperties.getValue("errors.oldPassword.wrong"));
		errorMess.put(FAIL_INVALID_SESSION, ApplicationProperties.getValue("errors.newPassword.genericmessage"));
		
	
		errorMess.put(FAIL_IN_UCASE,ApplicationProperties.getValue("errors.newPassword.ucase"));
		errorMess.put(FAIL_IN_LCASE,ApplicationProperties.getValue("errors.newPassword.lcase"));
		errorMess.put(FAIL_IN_NUMBER,ApplicationProperties.getValue("errors.newPassword.number"));
		errorMess.put(FAIL_IN_SPACE, ApplicationProperties.getValue("errors.newPassword.space"));
	}
	
	/**
	 *
	 * @param args filename,password.
	 * @throws PasswordEncryptionException generic PasswordEncryptionException
	 * @throws IOException Generic IO Exception.
	 */
	public static void main(String[] args) throws PasswordEncryptionException, IOException
	{
		if (args.length > 1)
		{
			String filename = args[0];
			String password = args[1];
			String encodedPWD = encrypt(password);
			writeToFile(filename, encodedPWD);
		}
	}
	/**
	 * This method writes the encoded password to the file.
	 * @param filename File to be written.
	 * @param encodedPassword Encoded password.
	 * @throws IOException Generic IO Exception.
	 */
	private static void writeToFile(String filename, String encodedPassword) throws IOException
	{
			File fileObject = new File(filename);
			FileWriter writeObject = new FileWriter(fileObject);
			writeObject.write("first.admin.encodedPassword=" + encodedPassword + "\n");
			writeObject.close();
	}
}
