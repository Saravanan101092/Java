
package com.staples.pim.delegate.datamigration.utils;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.COMMA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EMPTY_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FLOAT_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.HYPHEN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INTEGER_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_ATTRIBUTE_NAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_ATTRIBUTE_POSITION;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LIST_OF_VALUES_PROPERTIES;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STRING_TYPE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.spi.FileSystemProvider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;

import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.itemutility.beans.PropertiesVO;
import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;

public class DatamigrationCommonUtil {

	private static final String	TRUE_STR			= "true";
	public static IntgSrvLogger	logger				= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static Marshaller	jaxbMarshaller		= null;

	// variable used for multiple endpoint transmission
	public static boolean		isMultipleEndpoints	= false;
	public static String[]		multipleEndpointPaths;
	public static int			noOfEndpoints;
	public static int			counter				= -1;

	public static File[] sortFilesBasedOnFIFO(File inputFolder, final String PUBLISH_ID_OR_USECASE) {

		File[] files = null;
		try {
			logger.info("sortFilesBasedOnFIFO : getAbsolutePath  : " + inputFolder.getAbsolutePath());
			DatamigrationCommonUtil.printConsole("sortFilesBasedOnFIFO : getAbsolutePath  : " + inputFolder.getAbsolutePath());
			files = inputFolder.listFiles();
//			if (files != null && files.length > 1) {
//				Arrays.sort(files, new Comparator<File>() {
//
//					public int compare(File file1, File file2) {
//
//						String[] file1Name = file1.getName().split("_", 2);
//						String[] file2Name = file2.getName().split("_", 2);
//						int result = file1Name[1].compareTo(file2Name[1]);
//						return result;
//					}
//				});
//			}
		} catch (Exception e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
		}
		return files;
	}

	public static void sendFile(File outputFile, File fileName, String filedone, String applicationId, boolean isFileMoveActivated,
			String publishID) {

		String hostName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_HOST_STEP);
		if (hostName != null && !"null".equalsIgnoreCase(hostName)) {

			FTPConnectionBean ftpConnectionBean = getSFTPConnectConfigBean(applicationId, outputFile.getPath());
			if (ftpConnectionBean.getDestinationUrl().contains("~")) {
				isMultipleEndpoints = true;
				multipleEndpointPaths = ftpConnectionBean.getDestinationUrl().split("~");
				noOfEndpoints = multipleEndpointPaths.length;
				if (counter == -1 || counter >= noOfEndpoints) {
					counter = 0;
				}
				ftpConnectionBean.setDestinationUrl(multipleEndpointPaths[counter]);
				counter++;
			}
			// PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL
			SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
			sftpManager.setPublishId(publishID);

			boolean isSentSuccessfully = false;
			while (!isSentSuccessfully) {
				isSentSuccessfully = sftpManager.uploadFile(outputFile.getPath());
				if (!isSentSuccessfully) {
					long thread_sleep_interval;
					String threadsleepintervalString = IntgSrvPropertiesReader
							.getProperty(DatamigrationAppConstants.PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL);
					if (threadsleepintervalString != null) {
						thread_sleep_interval = Long.parseLong(threadsleepintervalString);
					} else {
						thread_sleep_interval = 60000;
					}
					try {
						Thread.sleep(thread_sleep_interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
						IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), publishID);
					}
				}
			}

			DatamigrationCommonUtil.printConsole("SFTP transfer success:" + isSentSuccessfully);

			if (isSentSuccessfully && isFileMoveActivated) {
				File file = new File(IntgSrvUtils.reformatFilePath(filedone) + fileName.getName());
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (fileName.exists()) {
					try {
						Files.copy(fileName.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
						System.gc();
						fileName.delete();
					} catch (IOException e) {
						e.printStackTrace();
						IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), publishID);
					}
				}
			}
		}
	}

	/**
	 * @param applicationId
	 * @param ftpSrc
	 * @return
	 */
	public static FTPConnectionBean getSFTPConnectConfigBean(String applicationId, String ftpSrc) {

		String ftpDest = null;

		if ("6_U".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_MERCH_UPDATE_TARGETDIR_STEP;
		} else if ("6_D".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_MERCH_DELETE_TARGETDIR_STEP;
		} else if ("7".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_RM_UPDATE_TARGETDIR_STEP;
		} else if ("8".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_PRODUCT_UPDATE_TARGETDIR_STEP;
		} else if ("9".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_VENDORUPC_UPDATE_TARGETDIR_STEP;
		} else if ("11".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_PRODUCT_TARGET_SFTP_FOLDER;
		} else if ("11_OT_BUL".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_PRODUCT_TARGET_SFTP_FOLDER;
		} else if ("11_OT_HIE".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_PRODUCT_TARGET_SFTP_FOLDER;
		} else if ("11_PRICING".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_PRICING_TARGET_SFTP_FOLDER;
		} else if ("11_IMAGES".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_IMAGES_TARGET_SFTP_FOLDER;
		} else if ("11_CATEGORYSPEC_ATTRIBUTES".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_CATEGORY_SPEC_ATTR_TARGET_SFTP_FOLDER;
		} else if ("11_TAXONOMY".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_TAXONOMY_TARGET_SFTP_FOLDER;
		} else if ("11_ATTRIBUTES".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_ATTRIBUTEMETADATA_TARGET_SFTP_FOLDER;
		} else if ("11_ACTIVESKU".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_ACTIVESKU_TARGET_SFTP_FOLDER;
		} else if ("11_LOV".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_LOV_TARGET_SFTP_FOLDER;
		} else if ("11_ENTITY".equalsIgnoreCase(applicationId)) {
			ftpDest = DatamigrationAppConstants.WAYFAIR_TAXONOMY_TARGET_SFTP_FOLDER;
		} else if ("4_I".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_CNP_INBOUND_CREATE_TARGETDIR_STEP;
		} else if ("4_U".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_CNP_INBOUND_UPDATE_TARGETDIR_STEP;
		} else if ("oraclefinancial".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_ORACLE_FINANCIAL_TARGETDIR_STEP;
		}  else if ("13_COPY_ATTRIBUTES".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_STEP;
		} else if ("13_COPY_ATTRIBUTES_HEADER".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_HEADER_STEP;
		} else if ("13_COPY_ATTRIBUTES_LOV".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_LOV_STEP;
		} else if ("13_COPY_ATTRIBUTES_SPEC".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_SPEC_STEP;
		} else if ("13_COPY_ATTRIBUTES_TAXONOMY".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_TAXONOMY_STEP;
		} else if ("13_COPY_ATTRIBUTES_ENTITY".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_COPY_ATTRIBUTES_ENTITY_STEP;
		} else if ("WercsResponseXML".equalsIgnoreCase(applicationId)) {
			ftpDest = IntgSrvAppConstants.SFTP_WERCS_WERCSRESPONSE_XML_STEP;
		} else if ("CorpDMZtoSTEP".equalsIgnoreCase(applicationId)) {
			ftpDest = WercsAppConstants.SFTP_WERCS_CORPDMZTOSTEP_STEP;
		}
		
		/* send Product and Attribute files to the SFTP server */
		FTPConnectionBean connectionsBean = new FTPConnectionBean();

		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(ftpDest));
		connectionsBean.setOriginatedURL(IntgSrvPropertiesReader.getProperty(ftpSrc));

		return connectionsBean;
	}

	public static File marshallObject(STEPProductInformation stepProductInformation, File fileName, String outputFolder,
			final String PUBLISH_ID_OR_USECASE) {

		File filetobewritten = null;

		try {

			Marshaller jaxbMarshaller = DatamigrationCommonUtil.getJaxbMarchallerObject(PUBLISH_ID_OR_USECASE);

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			String inputFileName = fileName.getName();
			inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
			DatamigrationCommonUtil.printConsole(IntgSrvPropertiesReader.getProperty(outputFolder));
			String outputFileName = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(outputFolder)) + inputFileName
					+ ".xml";

			logger.info("Writing the xml file : " + outputFileName);
			filetobewritten = new File(outputFileName);
			if (!filetobewritten.getParentFile().exists()) {
				filetobewritten.getParentFile().mkdirs();
			}
			jaxbMarshaller.marshal(stepProductInformation, filetobewritten);

		} catch (JAXBException e) {
			logger.error("Exception while writing the xml file :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
		}
		return filetobewritten;
	}

	private static Marshaller getJaxbMarchallerObject(final String PUBLISH_ID_OR_USECASE) {

		if (jaxbMarshaller == null) {

			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(STEPProductInformation.class);
				jaxbMarshaller = jaxbContext.createMarshaller();
			} catch (JAXBException e) {
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
			}
		}

		return jaxbMarshaller;
	}

	/**
	 * @param headers
	 * @param values
	 * @return
	 */
	public static Map<String, String> getValuesInMap(String[] headers, String[] values) {

		Map<String, String> map = new HashMap<String, String>();
		if (headers.length != values.length) {
			DatamigrationCommonUtil.printConsole("No of values in header and the number of values in this row do not match!");
			return map;
		}
		for (int i = 0; i < headers.length; i++) {
			map.put(headers[i], values[i]);
		}
		return map;
	}

	public static Map<String, String> getValuesWithDelimiterInMap(String[] headers, String[] values) {

		Map<String, String> map = new HashMap<String, String>();
		if (headers.length != values.length) {
			DatamigrationCommonUtil.printConsole("No of values in header and the number of values in this row do not match!");
			return map;
		}
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].contains(">") && (values[i].contains(">"))) {
				String[] splitHeaders = headers[i].split(">", -1);
				String[] splitValues = values[i].split(">", -1);
				if (splitHeaders.length == splitValues.length) {
					for (int j = 0; j < splitHeaders.length; j++) {
						map.put(splitHeaders[j], splitValues[j]);
					}
				}
			} else {
				map.put(headers[i], values[i]);
			}
		}
		return map;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String converYESNOIntoChar(String value) {

		if (value == null) {
			return "";
		} else if ("YES".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value) || "TRUE".equalsIgnoreCase(value)) {
			return "Y";
		} else if ("NO".equalsIgnoreCase(value) || "N".equalsIgnoreCase(value) || "FALSE".equalsIgnoreCase(value)) {
			return "N";
		}
		return value;
	}

	/**
	 * To get the Current date in STEP format
	 * 
	 * @return date string
	 */
	public static String getCurrentDateForSTEP() {

		return IntgSrvUtils.getStringFromDate(new Date(), DatamigrationAppConstants.ISO_DATE_TIME_FORMAT);
	}

	/**
	 * Gets the stack trace element.
	 * 
	 * @return the stack trace element
	 */
	private static StackTraceElement getStackTraceElement(int i) {

		Exception e = new Exception();
		StackTraceElement[] ste = e.getStackTrace();
		StackTraceElement cur = ste[i];
		return cur;
	}

	/**
	 * Gets the method name.
	 * 
	 * @return the method name
	 */
	public static String getMethodNameAndLineNumber() {

		StackTraceElement cur = getStackTraceElement(3);
		if (cur.getLineNumber() > 0) {
			return cur.getMethodName() + " [L]:" + cur.getLineNumber() + " ";
		}
		return cur.getMethodName() + "";
	}

	/**
	 * Gets the method name.
	 * 
	 * @return the method name
	 */
	public static String getMethodName() {

		StackTraceElement cur = getStackTraceElement(2);
		return cur.getMethodName() + "";
	}

	// public static String getM

	/**
	 * Gets the class name.
	 * 
	 * @param qualifiedName
	 *            the qualified name
	 * 
	 * @return the class name
	 */
	private static String getClassName(String qualifiedName) {

		String names[] = qualifiedName.split("\\.");
		if (names.length > 0) {
			String lastName = names[names.length - 1];
			if (lastName.indexOf("$") >= 0) {
				names = lastName.split("\\$");
				return names[names.length - 1];
			}
			return lastName;
		}
		return qualifiedName;
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 * 
	 */
	public static String getClassName() {

		StackTraceElement cur = getStackTraceElement(2);
		return getClassName(cur.getClassName());
	}

	/**
	 * Gets the class and method name.
	 * 
	 * @return the class and method name
	 */
	public static String getClassAndMethodName() {

		return " [C]:" + getClassName() + " [M]:" + getMethodNameAndLineNumber();
	}

	/**
	 * @param str
	 */
	public static void printConsole(Object str) {

		if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.SYSTEM_OUT_CONSOLE_ENABLE))) {
			System.out.println(str);
		}
	}

	public static String trimValues(String value) {

		if (value == null) {
			return null;
		}
		return value.trim();

	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isZeroValue(String value) {

		if (toDouble(value) == 0.0) {
			return true;
		}

		return false;
	}

	/**
	 * @param string
	 * @return
	 */
	public static double toDouble(String string) {

		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
		}
		return 0;
	}

	public static long getCurrentMilliSeconds() {

		return new Date().getTime();
	}

	/**
	 * This method is used to generate report file in case validations fail for
	 * any record.
	 * 
	 * @param fileName
	 *            : The error report file name
	 * @param header
	 *            : The first five lines (header) from input
	 * @param errorMsg
	 *            : The actual error message
	 * @param secondLine
	 *            : The record in which the validations failed
	 * @throws IOException
	 */
	public static void generateReportFile(String filebadDir, String fileName, String header, String errorMsg, String secondLine)
			throws IOException {

		DatamigrationCommonUtil.printConsole("errorMsg :::: " + errorMsg);
		filebadDir = IntgSrvUtils.reformatFilePath(filebadDir);
		DatamigrationCommonUtil.printConsole("errorMsg :::: " + filebadDir);
		File file = new File(filebadDir + DatamigrationAppConstants.ERROR_REPORT_FILE_PREFIX + fileName.split("\\.")[0] + ".xsv");

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		if (!file.exists()) {
			header = header + "\n";
		} else {
			header = "";
		}
		FileWriter errorWriter = new FileWriter(file, true);
		errorWriter.write(header);
		errorWriter.write(secondLine + "~|~" + errorMsg + "\n");
		errorWriter.close();
	}

	/**
	 * Replace dot value and trim the exponential Padding Zeros up to eight
	 * character
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceDotTrimExpon(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			value = value.replaceAll("\\.", "");
			value = value.toUpperCase();
			int index = value.indexOf("E");
			if (index > 0) {
				value = value.substring(0, index);
			}
			value = addTrailingCharacter(value, 8, '0');
			// String.format("%-8s", value).replace(' ', '0');
		}
		return value;
	}

	/**
	 * @param value
	 * @param noofChar
	 * @param replaceChar
	 * @return
	 */
	public static String addLeadingCharacter(String value, int noofChar, char replaceChar) {

		value = String.format("%" + noofChar + "s", value).replace(' ', replaceChar);
		return value;
	}

	/**
	 * @param value
	 * @param noofChar
	 * @param replaceChar
	 * @return
	 */
	public static String addTrailingCharacter(String value, int noofChar, char replaceChar) {

		value = String.format("%-" + noofChar + "s", value).replace(' ', replaceChar);
		return value;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String splitValidUPC(String value) {

		if (value != null && value.contains("~")) {
			return value.split("~", -1)[0];
		}
		return value;
	}

	/**
	 * @param fileName
	 */
	public static void moveFileToFileBad(File fileName, final String PUBLISH_ID_OR_USECASE) {

		String fileDonePath = fileName.getParentFile().getParentFile().getPath() + "/File_Bad/" + fileName.getName();
		File file = new File(fileDonePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (fileName.exists()) {
			try {
				Files.copy(fileName.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				fileName.delete();
			} catch (IOException e) {
				logger.error(e);
				logger.info(e.getMessage());
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
			}
		}
	}

	public static String voToString(Object obj, final String PUBLISH_ID_OR_USECASE) {

		StringBuffer buf = new StringBuffer();
		if (obj != null) {
			buf.append("<Object::" + obj.getClass().getName() + "> ");
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Object parameters[] = {};
				if ((methods[i].getName().indexOf("get") == 0 || methods[i].getName().indexOf("is") == 0)
						&& !(methods[i].getName().equalsIgnoreCase("getClass"))) {
					if (methods[i].getGenericParameterTypes().length == 0) {
						try {
							buf.append("<" + methods[i].getName().substring(3) + "::" + methods[i].invoke(obj, parameters) + ">");
						} catch (Exception e) {
							e.printStackTrace();
							IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
						}
					}
				}
			}
		} else {
			buf.append("Given Object Is Null");
		}
		return buf.toString();
	}

	/**
	 * @param unFormattedDate
	 * @return
	 * @throws ParseException
	 */
	public static String changeISODateTimeFormat(String unFormattedDate, final String PUBLISH_ID_OR_USECASE) throws ParseException {

		if (!IntgSrvUtils.isNullOrEmpty(unFormattedDate)) {

			return simpleDateFormatedValue(unFormattedDate, DatamigrationAppConstants.PCF_DATE_FORMAT,
					DatamigrationAppConstants.ISO_DATE_TIME_FORMAT, PUBLISH_ID_OR_USECASE);
		}
		return unFormattedDate;

	}

	public static String changeISODateFormat(String unFormattedDate, final String PUBLISH_ID_OR_USECASE) {

		if (!IntgSrvUtils.isNullOrEmpty(unFormattedDate)) {

			return simpleDateFormatedValue(unFormattedDate, DatamigrationAppConstants.PCF_DATE_FORMAT,
					DatamigrationAppConstants.ISO_DATE_FORMAT, PUBLISH_ID_OR_USECASE);

		}
		return unFormattedDate;

	}

	// For PCMP-1394
	public static String changeDDMONYYYYDateFormat(String unformattedDate, final String PUBLISH_ID_OR_USECASE) throws ParseException {

		if (!IntgSrvUtils.isNullOrEmpty(unformattedDate)) {
			return simpleDateFormatedValue(unformattedDate, null, DatamigrationAppConstants.DDMMMYYYY_DATE_FORMAT, PUBLISH_ID_OR_USECASE);
		}
		return unformattedDate;

	}

	public static String simpleDateFormatedValue(String unFormattedDate, String actualFormat, String expectedFormat,
			final String PUBLISH_ID_OR_USECASE) {

		Date date;
		String formattedDate = "";
		try {
			if (actualFormat != null) {
				SimpleDateFormat oldFformat = new SimpleDateFormat(actualFormat, Locale.US);
				date = oldFformat.parse(unFormattedDate);
			} else {
				date = new Date(unFormattedDate);
			}
			SimpleDateFormat newFormat = new SimpleDateFormat(expectedFormat);
			formattedDate = newFormat.format(date);
		} catch (ParseException e) {
			// e.printStackTrace();
			// IntgSrvUtils.alertByEmail(e,
			// DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
		} catch (Exception e) {
			// e.printStackTrace();
			// IntgSrvUtils.alertByEmail(e,
			// DatamigrationCommonUtil.getClassName(), PUBLISH_ID_OR_USECASE);
		}

		return formattedDate;
	}

	/**
	 * This method is used to set the minimum and maximum.
	 * 
	 * @param actualValue
	 *            Actual value
	 * @param minVal
	 *            Minimum value.
	 * @param maxValue
	 *            Maximum value
	 * @param isDefMinIfEmpty
	 *            boolean flag to set minvalue if the actual value is null or
	 *            empty.
	 * @return
	 */
	public static String setValueBasedOnMinMax(String actualValue, String minVal, String maxValue, boolean isDefMinIfEmpty) {

		if (isDefMinIfEmpty && IntgSrvUtils.isNullOrEmpty(actualValue) && !IntgSrvUtils.isNullOrEmpty(minVal)) {

			return minVal;

		} else if (!IntgSrvUtils.isNullOrEmpty(actualValue) && IntgSrvUtils.isDouble(actualValue)) {

			double actVal = IntgSrvUtils.toDouble(actualValue);

			if (!IntgSrvUtils.isNullOrEmpty(minVal) && IntgSrvUtils.isDouble(minVal) && actVal < IntgSrvUtils.toDouble(minVal)) {
				return minVal;
			}
			if (!IntgSrvUtils.isNullOrEmpty(maxValue) && IntgSrvUtils.isDouble(maxValue) && actVal > IntgSrvUtils.toDouble(maxValue)) {
				return maxValue;
			}
		}
		return actualValue;
	}

	// CnP CommonUtil methods

	private static Map<String, List<String>>	propMap	= new HashMap<String, List<String>>();

	private static String						configDir;

	private static JAXBContext					jaxbContext;

	/**
	 * To check the minimum and maximum value of given value
	 * 
	 * @param valueStr
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isMinMaxCheck(double value, double min, double max) {

		if (value < max && value > min) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * To check the minimum and maximum value of the Vendor
	 * 
	 * @param valueStr
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isIntMinMaxCheck(String valueStr, int min, int max) {

		Integer value = IntgSrvUtils.toInt(valueStr);
		if (value < max && value > min) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Returns the Properties object corresponding to the given property file
	 * location.
	 * 
	 * @param propertiesLocation
	 *            - location of the property file.
	 * @return returns the Properties corresponding the given property file.
	 * */
	/*
	 * public static Properties getProperties() {
	 * 
	 * InputStream input = null;
	 * 
	 * try { if (attributeProp.isEmpty()) { String path =
	 * IntgSrvUtils.getConfigDir(); input = new
	 * FileInputStream(path+ATTRIBUTE_PROPERTIES);
	 * 
	 * // load a properties file attributeProp.load(input); }
	 * 
	 * } catch (IOException ex) { logger.info(ex.getMessage());
	 * logger.error(ex); ex.printStackTrace();// FIXME } finally { if (input !=
	 * null) { try { input.close(); } catch (IOException e) {
	 * logger.info(e.getMessage()); logger.error(e); e.printStackTrace(); //
	 * FIXME } } } return attributeProp; }
	 */

	/*
	 * public static String getPropertiesValue(String key) {
	 * 
	 * Properties attributeProp = getProperties(); return
	 * attributeProp.getProperty(key); }
	 */

	/**
	 * Returns the Properties object corresponding to the given property file
	 * location.
	 * 
	 * @param propertiesLocation
	 *            - location of the property file.
	 * @return returns the Properties corresponding the given property file.
	 * */
	/*
	 * public static String getLOVProperties(String key) {
	 * 
	 * InputStream input = null;
	 * 
	 * try { if (lovProp.isEmpty()) { String path = getConfigDir(null); input =
	 * new FileInputStream(path+LIST_OF_VALUES_PROPERTIES);
	 * 
	 * // load a properties file lovProp.load(input); }
	 * 
	 * } catch (IOException ex) { logger.info(ex.getMessage());
	 * logger.error(ex); ex.printStackTrace();// FIXME } finally { if (input !=
	 * null) { try { input.close(); } catch (IOException e) {
	 * logger.info(e.getMessage()); logger.error(e); e.printStackTrace(); //
	 * FIXME } } } return lovProp.getProperty(key); }
	 */

	/**
	 * To get the Configuration file directory based on command line input,
	 * environment value and default
	 * 
	 * @param argStr
	 * @return
	 */
	public static String getConfigDir(String argStr) {

		if (configDir == null) {
			configDir = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT);
			DatamigrationCommonUtil.printConsole("CONFIG_DIR: " + configDir);
			logger.info("Inbound/Outbound integration spring batch CONFIG_DIR : " + configDir);

		}
		return configDir;

	}

	/**
	 * To remove the null values based on the type. eg. If null or non-numeric
	 * in integer then return 0
	 * 
	 * @param value
	 * @param formatStr
	 * @return
	 */
	public static Object getNotNullValue(String value, String formatStr) {

		DatamigrationCommonUtil.printConsole("value : " + value + ": Type:" + formatStr);
		if (formatStr.equals(STRING_TYPE)) {
			if (IntgSrvUtils.isEmptyString(value)) {
				return EMPTY_STR;
			}
		}
		if (formatStr.equals(INTEGER_TYPE)) {
			return IntgSrvUtils.toInt(value);
		}
		if (formatStr.equals(FLOAT_TYPE)) {
			return IntgSrvUtils.toDouble(value);
		}
		return value;
	}

	public static String voToString(Object obj) {

		StringBuffer buf = new StringBuffer();
		if (obj != null) {
			buf.append("<Object::" + obj.getClass().getName() + "> ");
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Object parameters[] = {};
				if ((methods[i].getName().indexOf("get") == 0 || methods[i].getName().indexOf("is") == 0)
						&& !(methods[i].getName().equalsIgnoreCase("getClass"))) {
					if (methods[i].getGenericParameterTypes().length == 0) {
						try {
							buf.append("<" + methods[i].getName().substring(3) + "::" + methods[i].invoke(obj, parameters) + ">");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			buf.append("Given Object Is Null");
		}
		return buf.toString();
	}

	/**
	 * @param list
	 * @return
	 */
	public static String getCommaSeparetedValues(List<String> list) {

		String commaVal = "";
		if (list != null) {
			for (String value : list) {
				commaVal += "," + value;
			}
			if (!commaVal.isEmpty()) {
				return commaVal.substring(1);
			}
		}
		return "";
	}

	public static void writerFile(String fileName, String Clog) {

		try {
			if (fileName != null) {
				fileName = IntgSrvUtils.reformatFilePath(fileName);
				createNewFile(fileName);

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));
				pw.write(Clog);
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void createNewFile(String fileName) {

		try {
			File file = new File(fileName);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception exp) {
			logger.error(exp);
		}
	}

	// ITEM UTILITY

	private static Map<String, PropertiesVO>	attributePropList	= new HashMap<String, PropertiesVO>();

	/**
	 * @return
	 */
	public static Map<String, PropertiesVO> getPropertiesVO() {

		if (attributePropList.isEmpty()) {

			// Attribute Name and position details to PropertiesVO
			String names = IntgSrvPropertiesReader.getProperty(ITEM_ATTRIBUTE_NAME);
			String positions = IntgSrvPropertiesReader.getProperty(ITEM_ATTRIBUTE_POSITION);
			updateAttributePropList(ITEM_ATTRIBUTE_NAME, names, positions);
		}
		return attributePropList;
	}

	/**
	 * @param key
	 * @param names
	 * @param positions
	 */
	private static void updateAttributePropList(String key, String names, String positions) {

		if (names != null && !names.isEmpty()) {
			String name[] = names.split(COMMA);
			String position[] = positions.split(COMMA);
			for (int i = 0; i < name.length; i++) {
				PropertiesVO propertiesVO = new PropertiesVO();
				propertiesVO.setAttributeName(name[i]);
				propertiesVO.setPropertyKey(key);
				propertiesVO.setStartPosition(Integer.parseInt(position[i].split(HYPHEN)[0]));
				propertiesVO.setEndPosition(Integer.parseInt(position[i].split(HYPHEN)[1]));
				attributePropList.put(name[i], propertiesVO);
			}
		}
	}

	public static void printConsole(String str) {

		if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.SYSTEM_OUT_CONSOLE_ENABLE))) {
			System.out.println(new Date() + " : " + str);
		}
	}

	/**
	 * @param fileName
	 *            This function creates a new thread and sends the file at the
	 *            given path to a FTP server.
	 */
	// public static void sendFileToSFTP(String fileName, String channelId) {
	//
	// File file=new File(fileName);
	// if(file!=null && file.exists()){
	// String sftpContextPath = IntgSrvUtils.getConfigDir();
	// sftpContextPath = PATH_TYPE + sftpContextPath + SFTPCONTEXT_FILE;
	// SFTPFileTransfer sftpthread=new
	// SFTPFileTransfer(file,sftpContextPath,channelId);
	// sftpthread.start();
	// logger.info("Thread started!");
	// }else{
	// logger.info("File is not exists - "+fileName);
	// }
	// }

	public static JAXBContext getStepContractJAXBCtxInstance() {

		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance("com.staples.pcm.stepcontract.beans");
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return jaxbContext;
	}

	/**
	 * @return
	 */
	public static Map<String, List<String>> getLOVProperties() {

		InputStream input = null;

		try {
			if (propMap.isEmpty()) {
				Properties lovProp = new Properties();
				// String path = CommonUtil.getConfigDir(null);
				input = new FileInputStream(IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT + "/"
						+ LIST_OF_VALUES_PROPERTIES));
				// load a properties file
				lovProp.load(input);
				for (Entry<Object, Object> entry : lovProp.entrySet()) {
					String lovValuesArr[] = entry.getValue().toString().split("~|~");
					List<String> lovValuesList = new ArrayList<String>();
					for (int j = 0; j < lovValuesArr.length; j++) {
						lovValuesList.add(lovValuesArr[j]);
					}
					propMap.put(entry.getKey().toString(), lovValuesList);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();// FIXME
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace(); // FIXME
				}
			}
		}
		return propMap;
	}

	/**
	 * This method is used to get the LOV from properties file
	 * 
	 * @param propertyKey
	 * @param attVal
	 * @return
	 */
	public static String getValuesFromLOV(String propertyKey, String attVal, boolean isSpace) {

		List<String> lovValuesList = new ArrayList<String>();
		// String lovValues = CommonUtil.getLOVProperties(propertyKey);
		// String logValuesArr[] = lovValues.split("~|~");
		// CommonUtil.printConsole("Key/FANID = "+propertyKey+" ::: Attribute Value = "+attVal
		// +" ::: LOV list= "+lovValues);
		// logger.info("Key/FANID = "+propertyKey+" ::: Attribute Value = "+attVal
		// +" ::: LOV list= "+lovValues);
		Map<String, List<String>> propMap = getLOVProperties();
		for (Entry<String, List<String>> entry : propMap.entrySet()) {
			if (propertyKey.equalsIgnoreCase(entry.getKey())) {
				lovValuesList = entry.getValue();
				for (String value : lovValuesList) {
					if (isSpace && value.startsWith(attVal + " :")) {
						return value;
					} else if (value.startsWith(attVal + ":")) {
						return value;
					}
				}
				break;
			} else {
				continue;
			}
		}
		return null;
	}

	public static String getValuesFromLOVForCaseMatch(String propertyKey, String attVal) {

		List<String> lovValuesList = new ArrayList<String>();
		Map<String, List<String>> propMap = getLOVProperties();
		for (Entry<String, List<String>> entry : propMap.entrySet()) {
			if (propertyKey.equalsIgnoreCase(entry.getKey())) {
				lovValuesList = entry.getValue();
				for (String value : lovValuesList) {
					if (value != null && value.equalsIgnoreCase(attVal)) {
						return value;
					}
				}
				break;
			} else {
				continue;
			}
		}
		return attVal;
	}

	/**
	 * @param attrValue1
	 * @param attrValue2
	 * @param isZeroLTrim
	 * @return
	 */
	public static String getConcatenateValue(String attrValue1, String attrValue2, boolean isZeroLTrim) {

		// if(attrValue1!=null && attrValue2!=null)
		if (!IntgSrvUtils.isNullOrEmpty(attrValue1) && !IntgSrvUtils.isNullOrEmpty(attrValue2)) {
			attrValue2 = attrValue2.replaceFirst(attrValue1, "").trim();
			if (isZeroLTrim && IntgSrvUtils.isNumeric(attrValue1)) {
				attrValue1 = "" + IntgSrvUtils.toInt(attrValue1);
			}
			attrValue1 = attrValue1 + " : " + attrValue2;
		}
		return attrValue1;
	}

	/**
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static String getClassificationIDValue(String value1, String value2) {

		List<String> lovValuesList = new ArrayList<String>();
		String mechId = null;
		Map<String, List<String>> propMap = getLOVProperties();
		for (Entry<String, List<String>> entry : propMap.entrySet()) {
			if (value2.equalsIgnoreCase(entry.getKey())) {
				lovValuesList = entry.getValue();
				for (String value : lovValuesList) {
					if (value.startsWith(value1 + ":") || value.startsWith(IntgSrvUtils.toInt(value1) + ":")) {
						mechId = value.split(":")[1];
					}
				}
			}
		}

		return mechId;
	}

	/**
	 * @param deletedFiles
	 * @param receipents
	 * @param subject
	 * 
	 *            Sends the info about deleted records to respective associates
	 */
	public static void sendEmailForDeletedRecords(Set<String> deletedFiles, String receipents, String subject) {

		EmailUtil emailUtil = new EmailUtil();
		String EmailMessage = "The Following records are deleted..\n";

		// Enter each deleted record to the Email message
		for (String deletedString : deletedFiles) {
			EmailMessage += deletedString + "\n";
		}

		// Get the receipent addresse from the properties file
		String toAddress = IntgSrvPropertiesReader.getProperty(receipents);

		// Send mail
		if(!IntgSrvUtils.isNullOrEmpty(toAddress) && !("NULL".equalsIgnoreCase(toAddress))){
			emailUtil.sendEmail(subject, EmailMessage, toAddress);
		}
	}

	/**
	 * @param newFile
	 * @param referencefolderCurrent
	 * 
	 *            Copies the input file to the reference folder for future
	 *            reference for the delta creation process
	 */
	public static void copyNewFileToReferenceFolder(File newFile, String referencefolderCurrent, IntgSrvLogger logger) {

		logger.info("copying input file to reference folder");

		// Get the current and old folders that contain the reference files
		File currentFolder = new File(IntgSrvUtils.reformatFilePath(referencefolderCurrent) + File.separator);
		File oldFolder = new File(currentFolder.getParentFile().getPath() + File.separator
				+ DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD + File.separator);

		try {

			// delete the files in reference Old folder
			File[] fileInOldFolder = oldFolder.listFiles();
			if(fileInOldFolder!=null){
				for (int i = 0; i < fileInOldFolder.length; i++) {
					fileInOldFolder[i].delete();
				}
			}
			
			// move the files from current to old
			File[] filesInCurrentFolder = currentFolder.listFiles();
			if(filesInCurrentFolder!=null){
				for (int i = 0; i < filesInCurrentFolder.length; i++) {
					Files.copy(filesInCurrentFolder[i].toPath(),
							new File(oldFolder.getPath() + File.separator + filesInCurrentFolder[i].getName()).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					filesInCurrentFolder[i].delete();
				}
			}

			// move the new file to current folder
			Files.copy(newFile.toPath(), new File(currentFolder.getPath() + File.separator + newFile.getName()).toPath(),
					StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			logger.error("Exception while copying file to reference folder :" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("copied input file successfully");
	}

	/**
	 * @param referenceFolderCurrentPath
	 * @param referenceFolderOldPath
	 * @return
	 * 
	 *         Searches for the reference file in the reference folder. If the
	 *         latest file is not present, It looks for the older file
	 */
	public static File getReferenceFile(String referenceFolderCurrentPath, String referenceFolderOldPath) {

		// Get the current and old folders that contain the reference files
		File referenceFolderCurrent = new File(IntgSrvUtils.reformatFilePath(referenceFolderCurrentPath));
		File referenceFolderOld = new File(IntgSrvUtils.reformatFilePath(referenceFolderOldPath));

		// create the folders if they arent created already
		if (!referenceFolderCurrent.exists()) {
			referenceFolderCurrent.mkdirs();
		}
		if (!referenceFolderOld.exists()) {
			referenceFolderOld.mkdirs();
		}

		// get the files in current folder
		File[] filesInCurrentFolder = referenceFolderCurrent.listFiles();

		if (filesInCurrentFolder.length == 1) {

			// return the only file that is present
			return filesInCurrentFolder[0];
		} else if (filesInCurrentFolder.length > 1) {

			DatamigrationCommonUtil.printConsole("More than one files in current folder");
			// return the first file
			return filesInCurrentFolder[0];
		} else if (filesInCurrentFolder.length == 0) {
			DatamigrationCommonUtil.printConsole("No files in the current folder");
			File[] filesInOldFolder = referenceFolderOld.listFiles();

			// look in the old folder for reference files
			if (filesInOldFolder.length == 1) {
				return filesInOldFolder[0];
			} else if (filesInOldFolder.length > 1) {
				return filesInOldFolder[0];
			} else if (filesInOldFolder.length == 0) {
				DatamigrationCommonUtil.printConsole("No files for reference!!");
				// No reference file available. Return null
				String emailMessage="Hi All,\n\n"+IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV)+" - No Reference File Found in\n "+referenceFolderCurrentPath;
				new EmailUtil().sendEmail("No Reference File Found-"+IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV), emailMessage, IntgSrvPropertiesReader.getProperty("TO_ADDRESS"));
				return null;
			}
		}

		return null;
	}

	/**
	 * @param deltaFeed
	 * @param delimiter
	 * @param headers
	 * @return
	 * 
	 *         get the delta records in Set<String> in a Map<String, String> for
	 *         processing
	 */
	public static Map<String, List<Map<String, String>>> getSetAsMaps(Set<String> deltaFeed, String delimiter, String[] headers,
			IntgSrvLogger logger, File file) {

		String[] deltaValues;
		Map<String, List<Map<String, String>>> quillAndDotComRecords = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> deltaRecordsDotCom = new ArrayList<Map<String, String>>();
		List<Map<String, String>> deltaRecordsQuill = new ArrayList<Map<String, String>>();
		Map<String, String> recordAsMap;

		// Parse through each delta record and process one by one
		for (String thisDeltaString : deltaFeed) {
			deltaValues = thisDeltaString.split(delimiter, -1);
			recordAsMap = getRecordAsMap(headers, deltaValues, logger);
			if (recordAsMap != null) {
				if (DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE.equalsIgnoreCase(recordAsMap
						.get(TaxonomyProcessor.HIERARCHY_CODE))) {
					deltaRecordsDotCom.add(recordAsMap);
				} else if (DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE.equalsIgnoreCase(recordAsMap
						.get(TaxonomyProcessor.HIERARCHY_CODE))) {
					deltaRecordsQuill.add(recordAsMap);
				}
			} else {
				logger.warn("Record omitted :" + thisDeltaString);
				DatamigrationCommonUtil.writerFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), thisDeltaString
						+ "~Has incorrect number of Values in record :"+deltaValues.length);
			}
		}

		// add quill records and dotcom records separately in a map
		/*if (deltaRecordsDotCom != null && !deltaRecordsDotCom.isEmpty()) {
			quillAndDotComRecords.put(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE, deltaRecordsDotCom);
		}
		if (deltaRecordsQuill != null && !deltaRecordsQuill.isEmpty()) {
			quillAndDotComRecords.put(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE, deltaRecordsQuill);
		}*/
		quillAndDotComRecords.put(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE, deltaRecordsDotCom);
		quillAndDotComRecords.put(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE, deltaRecordsQuill);
		return quillAndDotComRecords;
	}

	/**
	 * @param headers
	 * @param values
	 * @return
	 * 
	 *         get the values as a map containing header as key and content as
	 *         value
	 */
	public static Map<String, String> getRecordAsMap(String[] headers, String[] values, IntgSrvLogger logger) {

		// check if the record has exact no of values
		
		Map<String, String> mapContainingRecord = new HashMap<String, String>();
		if (headers.length == values.length) {
			for (int i = 0; i < headers.length; i++) {
				mapContainingRecord.put(headers[i], values[i]);
			}
		} else {
			logger.error("Record does not have the number of values expected. Total values in Record = " + values.length);
//			DatamigrationCommonUtil.printConsole("Record does not have the number of values expected. Total values in Record = "
//					+ values.length);
			return null;
		}

		return mapContainingRecord;
	}

	/**
	 * @param newFile
	 * @param oldFile
	 * @return
	 * 
	 *         Compare the new File with the Old file and generate the delta
	 *         feed from it
	 * @throws IOException
	 */
	public static Map<String, Set<String>> getDeltaFeedFromNewAndOldFiles(File newFile, File oldFile, IntgSrvLogger logger) {

		Map<String, Set<String>> deltaFeeds = new HashMap<String, Set<String>>();
		Set<String> oldFileRecords;
		try {
			oldFileRecords = new HashSet<String>(FileUtils.readLines(oldFile));
			Set<String> newFileRecords = new HashSet<String>(FileUtils.readLines(newFile));
			Set<String> similars = new HashSet<String>(oldFileRecords);
			similars.retainAll(newFileRecords);
			oldFileRecords.removeAll(similars); // now set1 contains distinct
			// lines in file1
			newFileRecords.removeAll(similars); // now set2 contains distinct
			// lines in file2
			deltaFeeds.put(DatamigrationAppConstants.WAYFAIR_REFERENCE_CURRENT, newFileRecords);
			deltaFeeds.put(DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD, oldFileRecords);

		} catch (IOException ioException) {
			logger.error("Exception while generating Delta feed. " + ioException.getMessage());
		}

		return deltaFeeds;
	}
	
	public static boolean isFolderContainsFile(File folder){
		
		if(folder!=null && folder.listFiles()!=null && folder.listFiles().length>0){
			return true;
		}
		return false;
	}

	/**
	 * @param fileObj
	 */
	public static void makeDirs(File fileObj, boolean isFile) {

		File folderObj = fileObj;
		if (folderObj != null) {
			if (isFile) {
				folderObj = folderObj.getParentFile();
			}
			if(!folderObj.exists()){
				folderObj.mkdirs();
			}
		
		}

	} 

	public static boolean isPropertyValueNULL(String value){
		if(value==null || value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase(DatamigrationAppConstants.EMPTY_STR)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void appendWriterFile(String fileName, String Clog) {

		try {
			if (fileName != null) {
				fileName = IntgSrvUtils.reformatFilePath(fileName);
				createNewFile(fileName);

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
				pw.write(Clog+"\n");
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	/**
	 * @param inputFeedType
	 * @param reportPath
	 * @param fileName
	 * @param successCount
	 * @param milliseconds
	 * @param failureCount
	 * @throws IOException
	 * @throws MessagingException
	 */
	public static void generateExecutionSummary(String inputFeedType, String reportPath, String fileName, int successCount,
			long milliseconds, int failureCount) throws IOException, MessagingException {

		File[] attachmentFiles = new File[2];
		String issueSummary = "";

		int totalErrorsCount = 0;
		File errorReportSummary = new File(reportPath + "Execution_Summary.txt");

		if (!errorReportSummary.getParentFile().exists()) {
			errorReportSummary.getParentFile().mkdirs();
		}

		FileWriter errorReportSummaryWriter = new FileWriter(errorReportSummary, true);
		errorReportSummaryWriter.write("Input file Name: " + fileName);
		errorReportSummaryWriter.write("\nExecution Date: " + DatamigrationCommonUtil.getCurrentDateForSTEP());
		errorReportSummaryWriter.write("\n");

		DatamigrationCommonUtil.printConsole("Success records count : " + successCount);
		DatamigrationCommonUtil.printConsole("Failure records count : " + failureCount);
		DatamigrationCommonUtil.printConsole("Turn around (in ms): " + milliseconds);

		errorReportSummaryWriter.write("Success records count : " + successCount + "\n");
		errorReportSummaryWriter.write("Failure records count : " + failureCount + "\n");

		errorReportSummaryWriter.write("Turn around time(in ms): " + milliseconds + "\n\n");
		errorReportSummaryWriter.close();

		String actualMsg = constructEmailBody(inputFeedType, fileName, successCount, totalErrorsCount, issueSummary, false, milliseconds);

		sendEmail(inputFeedType + "Migration Status", actualMsg, IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.TO_ADDRESS),
				attachmentFiles, false, true);

	}

	/**
	 * @param inputFeedType
	 * @param fileName
	 * @param successCount
	 * @param failureCount
	 * @param issueSummary
	 * @param isErrorPresent
	 * @param milliseconds
	 * @return
	 */
	public static String constructEmailBody(String inputFeedType, String fileName, int successCount, int failureCount, String issueSummary,
			boolean isErrorPresent, long milliseconds) {

		String header = "";
		if (isErrorPresent)
			header = "PFA\n\n";
		// String 
		// mailBody=header+"Product Feed Execution summary:\n\n"+"File name :"+fileName+"\n"+"Success records count : "+successCount+"\n"+"Failure records count : "+failureCount+"\n"+"Issue Summary"+issueSummary+"\n";
		String mailBody = inputFeedType + " EXECUTION SUMMARY \nFile name : " + fileName + "\nExecution time (in ms) : " + milliseconds
				+ "\nNo. of products success : " + successCount + "\nNo. of products failed : " + failureCount
				+ "\nError log : PFA \nIssue summary : \n" + issueSummary + "";
		// String mailBody="<h1>jigrji</h1>";
		return mailBody;
	}

	/**
	 * @param emailSubject
	 * @param emailMessage
	 * @param toAddress
	 * @param attachmentFiles
	 * @param isErrorPresent
	 * @param hasHTMLBody
	 * @throws MessagingException
	 */
	public static void sendEmail(String emailSubject, String emailMessage, String toAddress, File[] attachmentFiles,
			boolean isErrorPresent, boolean hasHTMLBody) throws MessagingException {

		EmailUtil emailUtil = new EmailUtil();
		if (isErrorPresent)
			emailUtil.sendEmail(emailSubject, emailMessage, toAddress, attachmentFiles);
//		else
//			emailUtil.sendEmail(emailSubject, emailMessage, toAddress);

	}

	public static String getReportFolderPath(File file){
		
		String reportFolderPath = file.getParentFile().getParentFile().getPath()+File.separator+DatamigrationAppConstants.REPORTS_FOLDER_NAME+File.separator;
		File reportfolder = new File(reportFolderPath);
		makeDirs(reportfolder,false);
		return reportFolderPath;
	}
	
	public static boolean doContainValidFile(File file,String[] headers,String delimiter,IntgSrvLogger logger){
		
		FileReader reader;
		try {
			reader = new FileReader(file);
			BufferedReader buffReader = new BufferedReader(reader);
			String tempString;
			
			while ((tempString = buffReader.readLine()) != null) {
				if (!tempString.equals("")) {
					String[] values = tempString.split(delimiter, -1);
					if(DatamigrationCommonUtil.getRecordAsMap(headers, values, logger)!=null){
						return true;
					}
				}
			}
			
			buffReader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public static void generateExecutionSummaryForWayFair(File file, boolean isOneTime, String delimiter, int noOfRecords, int succeededRecords,
			int rejectedRecords, int newRecords, int deletedRecords, Date starttime, Date endtime, String feedType) {

		// get the execution report folder through
		String reportsFolderName = file.getParentFile().getParentFile().getPath() + File.separator
				+ DatamigrationAppConstants.REPORTS_FOLDER_NAME;
		File ReportsFolder = new File(reportsFolderName);
		if (!ReportsFolder.exists()) {
			ReportsFolder.mkdirs();
		}
		File reportsFile = new File(ReportsFolder.getPath() + File.separator + feedType+"ExecutionReports.txt");
		try {
			FileWriter writer = new FileWriter(reportsFile, true);

			// Write file details
			writer.write("\nFile Name : " + file.getName() + "\n");
			if (isOneTime) {
				writer.write("ONETIME INTEGRATION FILE \t");
			} else {
				writer.write("CONTINUOUS INTEGRATION FILE \t");
			}
			writer.write("Delimiter :" + delimiter + "\n");

			// execution details
			writer.write("Execution started on : " + starttime.toString() + "\n");
			writer.write("Execution completed on : " + endtime.toString() + "\n");
			writer.write("Turn around time(in ms): " + (endtime.getTime() - starttime.getTime()) + "\n");

			if (isOneTime) {
				writer.write("Total no of records in input file: " + noOfRecords + "\n");
				writer.write("No of records successfully processed : " + succeededRecords + "\n");
				writer.write("No of records rejected : " + rejectedRecords + "\n");
			} else {
				writer.write("No of new records : " + newRecords);
				writer.write("No of deleted records : " + deletedRecords);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
