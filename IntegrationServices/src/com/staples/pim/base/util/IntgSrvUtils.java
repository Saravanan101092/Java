/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.staples.pim.base.common.exception.IntgSrvResourceConfigException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;

public class IntgSrvUtils {

	private static IntgSrvLogger	LOGGER				= IntgSrvLogger.getInstance("FreeformTraceLogger"); // simachange
	private static String			configDirLocation	= null;

	public IntgSrvUtils() {

		// simachange all hardcode
	}

	public static URL locate(String configFileName) throws IntgSrvResourceConfigException {

		return locate(null, configFileName);
	}

	public static URL locate(String basePath, String fileName) throws IntgSrvResourceConfigException {

		// returns null if fileName is undefined
		if (fileName == null) {
			return null;
		}

		URL url = null;
		// attempt to create an URL directly, on exception reset to null and try
		// other methods
		try {
			if (basePath == null) {
				url = new URL(fileName);
			} else {
				URL baseURL = new URL(basePath);
				url = new URL(baseURL, fileName);
				// check if the file exists
				InputStream in = null;
				try {
					in = url.openStream();
				} finally {
					if (in != null) {
						in.close();
					}
				}
			}
		} catch (IOException e) {
			url = null;
			// start merged for sprint_A3
			// LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils",
			// "locate", "set url=null;" + toStringExceptionStackTrace(e));
			// end merged for sprint_A3
		}
		// attempt to load from an absolute path
		if (url == null) {
			File tempFile = new File(fileName);
			if (tempFile.isAbsolute() && tempFile.exists()) {
				try {
					url = tempFile.toURI().toURL();
				} catch (MalformedURLException e) {
					// simachange
					LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "locate", toStringExceptionStackTrace(e));
				}
			}
		}
		// attempt to load from the base directory
		if (url == null) {
			try {
				File tfile = getFile(basePath, fileName);
				if (tfile != null && tfile.exists()) {
					url = tfile.toURI().toURL();
				}
				if (url != null) {
				}
			} catch (IOException e) {
				// simachange
				LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "locate", toStringExceptionStackTrace(e));
			}
		}
		// attempt to load from the context classpath
		if (url == null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			url = loader.getResource(fileName);
			if (url != null) {
			}
		}
		// attempt to load from the system classpath
		if (url == null) {
			url = ClassLoader.getSystemResource(fileName);
			if (url != null) {
			}
		}
		// if still null, throw config exception as resource cannot be located
		if (url == null) {
			throw new IntgSrvResourceConfigException("Cannot locate file :" + fileName);
		}
		return url;
	}

	public static File getFile(String basePath, String fileName) {

		File file = null;
		if (basePath == null) {
			file = new File(fileName);
		} else {
			StringBuffer fName = new StringBuffer();
			fName.append(basePath);
			if (!basePath.endsWith(File.separator)) {
				fName.append(File.separator);
			}
			if (fileName.startsWith("." + File.separator)) {
				fName.append(fileName.substring(2));
			} else {
				fName.append(fileName);
			}
			file = new File(fName.toString());
		}
		return file;
	}

	public static boolean toBoolean(String value) {

		// For empty input, false is returned as default
		if (isEmptyString(value)) {
			return false;
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("enabled")
				|| value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("on")) {
			return true;
		}
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("disabled")
				|| value.equalsIgnoreCase("N") || value.equalsIgnoreCase("off")) {
			return false;
		}
		// Any integer greater than 0 will be considered as true
		try {
			int val = Integer.parseInt(value);
			return val > 0;
		} catch (NumberFormatException ex) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toBoolean", toStringExceptionStackTrace(ex));
			ex.printStackTrace();
		}
		// return false if it doesn't satisify above conditions or if a
		// NumberFormatException occurs
		return false;
	}

	public static boolean isEmptyString(String value) {

		return value == null || value.trim().length() == 0 || value.equalsIgnoreCase("null");
	}

	public static double toDouble(String string) {

		if (isEmptyString(string)) {
			return 0;
		}
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toDouble", toStringExceptionStackTrace(nfe));
			nfe.printStackTrace();
		}
		return 0;
	}

	public static float toFloat(String string) {

		if (isEmptyString(string)) {
			return 0;
		}
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException nfe) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toFloat", toStringExceptionStackTrace(nfe));
			nfe.printStackTrace();
		}
		return 0;
	}

	public static int toInt(String string) {

		if (isEmptyString(string)) {
			return 0;
		}
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException nfe) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toInt", toStringExceptionStackTrace(nfe));
			nfe.printStackTrace();
		}
		return 0;
	}

	public static long toLong(String string) {

		if (isEmptyString(string)) {
			return 0;
		}
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException nfe) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toLong", toStringExceptionStackTrace(nfe));
			nfe.printStackTrace();
		}
		return 0;
	}

	public static String trim(String property) {

		if (property == null) {
			return null;
		}
		String ret = property.trim();
		if (ret.length() == 0) {
			return null;
		}
		return ret;
	}

	/**
	 * public static void copyBean(Object source, Object destination) { try {
	 * destination = BeanUtils.cloneBean(source); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (InvocationTargetException e) { e.printStackTrace(); } catch
	 * (InstantiationException e) { e.printStackTrace(); } catch
	 * (NoSuchMethodException e) { e.printStackTrace(); } }
	 */
	public static <T> boolean isNullOrEmpty(Collection<T> list) {

		return list == null || list.isEmpty();
	}

	public static <T> boolean isNullOrEmpty(Map<?, ?> map) {

		return map == null || map.isEmpty();
	}

	public static <T> boolean isNullOrEmpty(String str) {

		return str == null || str.isEmpty();
	}

	public static <T> boolean isNullOrEmpty(Integer inte) {

		return inte == null || inte.intValue() == 0;
	}

	public static <T> boolean isNullOrEmpty(BigDecimal inte) {

		return inte == null || inte.compareTo(BigDecimal.ZERO) == 0;
	}

	public static Float formatFloat(Float value) {

		BigDecimal returnValue = new BigDecimal(value);
		return returnValue.setScale(2, RoundingMode.HALF_EVEN).floatValue();
	}

	public static String toCamelCase(String value) {

		StringBuffer returnValue = new StringBuffer();
		if (value != null && value != "") {
			value = value.trim();
			String[] valueList = value.split("\\s+");
			for (String splitValue : valueList) {
				returnValue = returnValue.append(splitValue.substring(0, 1).toUpperCase())
						.append(splitValue.substring(1, splitValue.length()).toLowerCase()).append(" ");
			}
			return returnValue.toString().trim();
		}
		return value;
	}

	public static boolean isNumeric(String stringNmb) {

		try {
			Integer.parseInt(stringNmb);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String value) {

		try {
			Double.parseDouble(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isLong(String value) {

		try {
			Long.parseLong(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * This method milliseconds into X Minutes Y Seconds Z Milliseconds format.
	 * 
	 * 
	 * @param milliseconds
	 *            MilliSeconds
	 * @return String formatted String.
	 */
	public static String readableTimeStamp(long milliseconds) {

		long millisecondsTEMP = milliseconds;
		if (millisecondsTEMP < 0) {
			LOGGER.error("Received negative value in readableTimeStamp(...), continuing with its positive value");
			millisecondsTEMP = -millisecondsTEMP;
		}
		if (millisecondsTEMP == 0) {
			return "No Time";
		}
		// Extract time components...
		long exHours = millisecondsTEMP / (1000 * 60 * 60);
		long exMinsTemp = millisecondsTEMP % (1000 * 60 * 60);
		long exMins = exMinsTemp / (1000 * 60);
		long exSecsTemp = exMinsTemp % (1000 * 60);
		long exSecs = exSecsTemp / (1000);
		long exMilliSecs = exSecsTemp % (1000);
		// Format return String...
		String retStr = "";
		if (exHours == 1) {
			retStr = retStr + exHours + " Hour ";
		} else if (exHours > 1) {
			retStr = retStr + exHours + " Hours ";
		}
		if (exMins == 1) {
			retStr = retStr + exMins + " Minute ";
		} else if (exMins > 1) {
			retStr = retStr + exMins + " Minutes ";
		}
		if (exSecs == 1) {
			retStr = retStr + exSecs + " Second ";
		} else if (exSecs > 1) {
			retStr = retStr + exSecs + " Seconds ";
		}
		if (exMilliSecs == 1) {
			retStr = retStr + exMilliSecs + " Millisecond";
		} else if (exMilliSecs > 1) {
			retStr = retStr + exMilliSecs + " Milliseconds";
		}
		return retStr.trim();
	}

	/**
	 * 
	 * @param map
	 * @return
	 */

	/**
	 * public static HttpHeaders createHeaders(Map<String, String> map) {
	 * HttpHeaders headers = new HttpHeaders(); if (null != map) {
	 * Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
	 * while (entries.hasNext()) { Map.Entry<String, String> entry =
	 * entries.next(); headers.set(entry.getKey(), entry.getValue()); } } return
	 * headers; }
	 */
	public static Date converStringToDate(String format, String dateInput) throws ParseException {

		SimpleDateFormat sdFormat = new SimpleDateFormat(format, Locale.getDefault());
		return sdFormat.parse(dateInput);
	}

	/**
	 * Format the give date based on the required format
	 * 
	 * @param date
	 * @param format
	 * @return String
	 * @since
	 */
	public static String getStringFromDate(Date date, String format) {

		return new SimpleDateFormat(format, Locale.getDefault()).format(date);
	}

	public static boolean isDate(String value) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		try {
			Date date = dateFormat.parse(value);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static Date toDate(String value) {

		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		try {
			date = dateFormat.parse(value);
		} catch (ParseException e) {
			LOGGER.error("com.staples.pcm.springbatch.common.utility.CommonUtils", "toDate", toStringExceptionStackTrace(e));
			e.printStackTrace();
		}
		return date;
	}

	public static Long generateUniqueRandomNumber(Long offerId) {

		long value = (new Double(Math.random() * 1000000000)).longValue() * 10 + offerId;
		return value;
	}

	public static String toStringExceptionStackTrace(Throwable te) {

		return toStringExceptionStackTrace(te, System.getProperty("line.separator"));
	}

	public static String toStringExceptionStackTrace(Throwable te, String delimiter) {

		StringBuffer stebf = new StringBuffer();
		stebf.append("Exception: " + te.toString() + delimiter);
		stebf.append("Stack Trace: " + delimiter);
		for (StackTraceElement ste : te.getStackTrace()) {
			stebf.append(ste.toString());
			stebf.append(delimiter);
		}
		return stebf.toString();
	}

	public static String reformatFilePath(String filename) {

		String filenameNew = filename.replace("\\", "/");
		filenameNew = filenameNew.replaceAll("/{2,}", "/");

		String osName = System.getProperty("os.name");
		if (osName.toUpperCase().contains("WINDOWS")) {
			if (!filenameNew.toUpperCase().startsWith("C:")) {
				filenameNew = "C:" + filenameNew;
			}
			filenameNew = filenameNew.replace("/", "\\");
		} else {
			if (filenameNew.toUpperCase().startsWith("C:")) {
				filenameNew = filenameNew.substring(2, filenameNew.length());
			}
		}
		// System.out.println("LW-DEBUG: CommonUtils.reformatFilePath | filenameNew = "
		// + filenameNew);
		return filenameNew;
	}

	public static void copyFileUsingFileStreams(File source, File dest)

	throws IOException {

		InputStream input = null;

		OutputStream output = null;

		try {

			input = new FileInputStream(source);

			output = new FileOutputStream(dest);

			byte[] buf = new byte[1024];

			int bytesRead;

			while ((bytesRead = input.read(buf)) > 0) {

				output.write(buf, 0, bytesRead);

			}
			IntgSrvUtils.printConsole("File was successfully archived.\n");

		} finally {

			input.close();

			output.close();

		}

	}

	/**
	 * This function will generate 32 unique IDs per millisecond. It also uses
	 * the host IP so that the IDs are unique across multiple hosts.
	 * 
	 * These Unique IDs are of 64-bit. It needs about 16 characters to represent
	 * in a hexa-decimal format.
	 * 
	 * @return String Unique id in
	 */
	public static String getUniqueID() {

		// Get the current time
		long currentTimeMillis = System.currentTimeMillis();
		/*
		 * try { // Get the IP Address InetAddress addr =
		 * InetAddress.getLocalHost();
		 * 
		 * // Add the hash code to current time currentTimeMillis +=
		 * addr.hashCode(); } catch (UnknownHostException e) { ; }
		 */

		// Convert to hex
		/*
		 * return StringUtils.leftPad( Long.toHexString((currentTimeMillis << 5)
		 * | (staticCounter++ & 31)), 16, 'f');
		 */
		String uniqueId = null;

		// String tempstr = Long.toHexString((currentTimeMillis +
		// (long)(Math.random()))).toString();
		// return StringUtils.leftPad(Long.toHexString((currentTimeMillis +
		// (long)(Math.random()))), 16, 'f');

		uniqueId = StringUtils.leftPad(
				(Long.toHexString(UUID.randomUUID().hashCode()) + (long) Math.random() + UUID.randomUUID().toString().substring(0, 5)), 16,
				'f').substring(0, 16);

		return uniqueId;
	}

	/**
	 * This function will generate an unique id of the following format.
	 * <PIM><11 digit primary key><16 character unique id>
	 * 
	 * @param pimid
	 *            PIM primary key
	 * @return String Unique id of 30 characters including primary key
	 */
	public static String getPIMUniqueID(int pimid) {

		return "PIM" + StringUtils.leftPad(String.valueOf(pimid), 11, '0') + getUniqueID();
	}

	public static String getPCMUniqueID(String SKU) {

		return "PCM" + StringUtils.leftPad(String.valueOf(SKU), 11, '0') + getUniqueID();
	}

	/**
	 * This function will get the PIM primary key from the unique id and returns
	 * as a String.
	 * 
	 * @param pimUniqueID
	 *            The 30 character PIM unique ID
	 * @return String The 11-digit PIM ID String
	 */
	public static String getIDFromUniqueIDString(String pimUniqueID) {

		return pimUniqueID.substring(3, 14);
	}

	/**
	 * This function will get the PIM primary key from the unique id and returns
	 * as an integer.
	 * 
	 * @param pimUniqueID
	 *            The 30 character PIM unique ID
	 * @return String The 11-digit PIM ID converted to an integer
	 */
	public static int getIDFromUniqueID(String pimUniqueID) {

		return Integer.parseInt(pimUniqueID.substring(3, 14));
	}

	public static String getConfigDir() {

		if (configDirLocation == null) {
			String sysEnvConfigDirLocation = System.getenv(IntgSrvAppConstants.SPRING_BATCH_CONFIG_DIR);
			if (sysEnvConfigDirLocation != null) {
				LOGGER.info("Config directory is set to the environment variable");
				configDirLocation = sysEnvConfigDirLocation;
			} else {
				LOGGER.info("Config directory is set to the default value");
				configDirLocation = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT);
			}
			configDirLocation = IntgSrvUtils.reformatFilePath(configDirLocation);
			IntgSrvUtils.printConsole("CONFIG_DIR: " + configDirLocation);
			LOGGER.info("CONFIG_DIR : " + configDirLocation);
		}
		return configDirLocation;
	}

	public String getConfigDir(String argStr) {

		if (configDirLocation == null) {
			String sysEnvConfigDirLocation = System.getenv(IntgSrvAppConstants.SPRING_BATCH_CONFIG_DIR);
			if (argStr != null && !argStr.isEmpty()) {
				LOGGER.info("Config directory is set to the command line argument");
				configDirLocation = argStr;
			} else if (sysEnvConfigDirLocation != null) {
				LOGGER.info("Config directory is set to the environment variable");
				configDirLocation = sysEnvConfigDirLocation;
			} else {
				LOGGER.info("Config directory is set to the default value");
				// configDirLocation =
				// IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT;
				configDirLocation = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT);
			}
			configDirLocation = IntgSrvUtils.reformatFilePath(configDirLocation);
			// IntgSrvUtils.printConsole("CONFIG_DIR: " + configDirLocation);
			LOGGER.info("CONFIG_DIR : " + configDirLocation);

		}
		return configDirLocation;
	}

	@SuppressWarnings("unused")
	public static void alertByEmail(String alert, String emailMessage) {

		EmailUtil emailUtil = new EmailUtil();
		emailUtil.sendEmail(alert, emailMessage);
	}

	/**
	 * Constructor used to create this object. Responsible for setting this
	 * object's creation date, as well as incrementing the number instances of
	 * this object.
	 * 
	 * @param query
	 *            String containing the query
	 * @return returns a String object that contains the query to be used
	 * @query String Used to pass the Stored procedure name and signature
	 */
	public static String prepareQuery(String query) {

		//  String genericQuery = "{ CALL " + query + " }";
		String genericQuery = "CALL " + query;
		return genericQuery;
	}

	/**
	 * Used to sent Handled Exception email alert
	 * 
	 * @param throwable
	 * @param instance
	 * @param publishId
	 */
	public static void alertByEmail(Throwable throwable, String instance, String publishId) {

		String alertMsg = getMailMessage(throwable.getMessage(), instance, publishId);
		String springBatchEnv = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV);
		String mailSub = "Spring Batch Exception Email Alert - " + springBatchEnv;
		EmailUtil emailUtil = new EmailUtil();
		emailUtil.sendEmail(mailSub, alertMsg);
	}

	/**
	 * Used to prepare handled exception email alert message
	 * 
	 * @param stackTrace
	 * @param instance
	 * @param publishId
	 * @return
	 */
	private static String getMailMessage(String stackTrace, String instance, String publishId) {

		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("Hi All, \n\n");
			buffer.append("Exception occurred on Spring Batch Instance - " + instance + ", Please find below details: \n\n");

			buffer.append("Source System: " + InetAddress.getLocalHost().getHostName() + "\n");

			if (publishId != null) {
				buffer.append("Publish Id: " + publishId + "\n");
			}
			buffer.append("Exception StackTrace: " + stackTrace);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	/**
	 * @param str
	 */
	public static void printConsole(String str) {

		if ("True".equalsIgnoreCase(IntgSrvPropertiesReader.getProperty("SYSTEM_OUT_CONSOLE_ENABLE"))) {
			System.out.println(new Date() + " : " + str);
		}
	}
}
