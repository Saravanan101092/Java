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

public class IntgSrvAppConstants {

	public IntgSrvAppConstants() {

		// TODO Auto-generated constructor stub
	}

	public static final String	SCHDULER_XML_DEFAULT											= "job-Scheduler.xml";
	public static final String	SCHDULER_SKUFLAGGING_DEFAULT									= "job-Scheduler-SKUFlagging.xml";
	public static final String	BATCH_JOB_XML_CREATE_UPDATE										= "job-ItemCreateUpdate.xml";
	public static final String	BATCH_JOB_XML_DEFAULT2											= "job-DBtoCSV.xml";
	public static final String	CONFIG_COMMON_PROPS_DEFAULT										= "config_common.properties";
	public static final String	CONFIG_ENV_PROPS_DEFAULT										= "config_env.properties";
	public static final String	SYSTEM_USER_DIRECTORY											= "user.dir";

	public static final String	JP_DATE															= "date";
	public static final String	JP_INPUT_FILE													= "inputFile";
	public static final String	INPUT_FILE														= "inputFile";
	public static final String	JP_OUTPUT_FILE_XSV												= "outputFile";
	public static final String	JP_OUTPUT_FILE_CSV												= "outputFile";
	public static final String	JP_OUTPUT_FILE_STEP_XML											= "outputFile";
	public static final String	JP_OUTPUT_FILE_STEP_XML_ADDUPDATE								= "outputFileAddUpdate";
	public static final String	JP_OUTPUT_FILE_STEP_XML_DELETE									= "outputFileDelete";
	public static final String	JP_OUTPUT_FILE_FIXLENGTH										= "outputFile2";
	public static final String	JP_OUTPUT_FILE_EXCEL											= "outputFile3";

	public static final String	JP_SETP_EXPORT_TIME												= "STEPExportTime";
	public static final String	JP_PUBLISH_ID													= "PublishId";
	public static final String	JP_TRANSACTION_TYPE_FILETOFILE									= "FileToFile";
	public static final String	JP_TRANSACTION_TYPE_FILETODB									= "FileToDB";
	public static final String	JP_TRANSACTION_TYPE_DBTOFILE									= "FileToDB";
	public static final String	JP_TRANSACTION_TYPE_DBTODB										= "DBToDB";

	public static final String	JP_LOCATION_LEVEL_QUERY_NAME_RETAIL								= "locationLevelRetail";
	public static final String	JP_LOCATION_LEVEL_QUERY_NAME_SCC								= "locationLevelSCC";

	public static final String	JP_EHF_TRACEID_JOBRUNNER										= "ehfTraceIdJobRunner";
	public static final String	JP_EHF_TRACEID_TRANSFORM										= "ehfTraceIdTransform";
	public static final String	JP_EHF_TRACEID_ITEM												= "ehfTraceIdItem";													// default

	// Application constants
	public static final String	STEP_PUBLISH_ID													= "STEE000";
	public static final String	SPRINGBATCH_PUBLISH_ID_XSV										= "STEE102";
	public static final String	SPRINGBATCH_PUBLISH_ID_FIXLENGTH								= "STEE103";
	public static final String	SPRINGBATCH_PUBLISH_ID_EXCEL									= "STEE0018,STCE103";

	public static final String	EHF_LOGGER														= "ehflogger";
	public static final String	EHF_LOGGER_PRODUCTITEMS_XSV										= "ehflogger.productitems.xsv";
	public static final String	EHF_LOGGER_PRODUCTITEMS_CSV										= "ehflogger.productitems.csv";
	public static final String	EHF_LOGGER_PRODUCTITEMS_FIXLENGTH								= "ehflogger.productitems.fixlength";
	public static final String	EHF_LOGGER_PRODUCTITEMS_EXCEL									= "ehflogger.productitems.excel";
	public static final String	EHF_LOGGER_APPENDER												= "EHFLogger";
	public static final String	EHF_LOGGER_APPENDER_PRODUCTITEMS_XSV							= "EHFLoggerProductItemsXSV";
	public static final String	EHF_LOGGER_APPENDER_PRODUCTITEMS_CSV							= "EHFLoggerProductItemsCSV";
	public static final String	EHF_LOGGER_APPENDER_PRODUCTITEMS_FIXLENGTH						= "EHFLoggerProductItemsFixLength";
	public static final String	EHF_LOGGER_APPENDER_PRODUCTITEMS_EXCEL							= "EHFLoggerProductItemsExcel";
	public static final String	FREEFORM_TRACE_LOGGER											= "tracelogger";
	public static final String	FREEFORM_TRACE_LOGGER_APPENDER									= "FreeformTraceLogger";

	public static final String	SIMPLE_DATE_FORMATTER											= "yyyy-MM-dd";

	public static final String	DELIMITER_TILDA													= "~";
	
	public static final String	ITEM_DESCRPTION_TILDE											= "__TILDE__";

	public static final String	DELIMITER_PIPE													= "|";

	public static final String	DELIMITER_TILDA_PIPE_TILDA										= "~|~";

	public static final String	DELIMITER_CUSTOM_NEW_LINE										= "~|NewLine|~";

	public static final String	DASH															= "_";

	public static final String	COMMA															= ",";

	public static final String	BRACKET_LEFT													= "{";

	public static final String	BRACKET_WRITE													= "}";

	public static final String	SLASH															= "/";

	public static final String	END_OF_THE_LINE													= "\r\n";

	public static final String	STIBO															= "STIBO_";

	public static final String	STIBO100														= "STIBO100";

	public static final String	STIBO103														= "STIBO103";

	public static final String	XML_FILE_EXTENSION												= ".xml";

	public static final String	XSV_FILE_EXTENSION												= ".xsv";

	public static final String	XSV_FILE_EXTENSION_TEMP											= "_Temp.xsv";

	public static final String	HEADER_LINE_TO_MAP												= "headerLineToMap";

	public static final String	SPRING_BATCH_CONFIG_DIR											= "SPRING_BATCH_CONFIG_DIR";

	public static final String	PROPERTY_FILE													= "propertyFile";

	public static final String	STIBO_MQ_RECIEVED_FILE_NAME										= "STIBO_MQ_RECIEVED_FILE_NAME";

	public static final String	XML_INPUT_ARCHIVE_DIRECTORY										= "XML_INPUT_ARCHIVE_DIRECTORY";

	public static final String	XML_INPUT_ARCHIVE_FAILED_DIRECTORY								= "XML_INPUT_ARCHIVE_FAILED_DIRECTORY";

	public static final String	FILE_EXTENSION_XML												= "FILE_EXTENSION_XML";

	public static final String	FIX_OUTPUT_DIRECTORY											= "FIX_OUTPUT_DIRECTORY";

	public static final String	XSV_OUTPUT_DIRECTORY											= "XSV_OUTPUT_DIRECTORY";

	public static final String	EXCEL_OUTPUT_DIRECTORY											= "EXCEL_OUTPUT_DIRECTORY";

	public static final String	FILE_EXTENSION_TEXT												= "FILE_EXTENSION_TEXT";

	public static final String	FIX_HEADER_LINE_ATTRIBUTE_IDS									= "FIX_HEADER_LINE_ATTRIBUTE_IDS";

	public static final String	FIX_ATTRIBUTE_LENGTHS											= "FIX_ATTRIBUTE_LENGTHS";

	public static final String	FIX_ATTRIBUTE_TYPES												= "FIX_ATTRIBUTE_TYPES";

	public static final String	FIX_ATTRIBUTE_PRECISION											= "FIX_ATTRIBUTE_PRECISION";

	public static final String	XML_INPUT_DIRECTORY												= "XML_INPUT_DIRECTORY";

	public static final String	XSV_PRODUCT_FILE_NAME_OUTPUT									= "XSV_PRODUCT_FILE_NAME_OUTPUT";

	public static final String	XSV_ATTRIBUTE_FILE_NAME_OUTPUT									= "XSV_ATTRIBUTE_FILE_NAME_OUTPUT";

	// Added for CnP & Item utility mig to intg services
	public static final String	CNP_OUTBOUND_XML_INPUT_DIRECTORY								= "CNP_OUTBOUND_XML_INPUT_DIRECTORY";

	public static final String	CNP_OUTBOUND_EXCEL_OUTPUT_DIRECTORY								= "CNP_OUTBOUND_EXCEL_OUTPUT_DIRECTORY";

	public static final String	CNP_INBOUND_XML_OUTPUT_DIRECTORY								= "CNP_INBOUND_XML_OUTPUT_DIRECTORY";

	public static final String	CNP_INBOUND_XML_INPUT_DIRECTORY									= "CNP_INBOUND_XML_INPUT_DIRECTORY";

	public static final String	ITEMUTILITY_INBOUND_FL_INPUT_DIRECTORY							= "ITEMUTILITY_INBOUND_FL_INPUT_DIRECTORY";

	public static final String	ITEMUTILITY_INBOUND_XML_OUTPUT_DIRECTORY						= "ITEMUTILITY_INBOUND_XML_OUTPUT_DIRECTORY";

	public static final String	LOG_FILE_BASE_URL												= "LOG_FILE_BASE_URL";

	public static final String	HEADER															= "HEADER";

	public static final String	A0000															= "A0000";
	public static final String	A0001															= "A0001";
	public static final String	A0002															= "A0002";
	public static final String	A0003															= "A0003";
	public static final String	A0005															= "A0005";
	public static final String	A0008															= "A0008";
	public static final String	A0009															= "A0009";
	public static final String	A0012															= "A0012";
	public static final String	A0013															= "A0013";
	public static final String	A0013_RET														= "A0013_RET";
	public static final String	A0013_NAD														= "A0013_NAD";
	public static final String	A0015															= "A0015";
	public static final String	A0016															= "A0016";
	public static final String	A0018															= "A0018";
	public static final String	A0018_RET														= "A0018_RET";
	public static final String	A0018_NAD														= "A0018_NAD";
	public static final String	A0019															= "A0019";
	public static final String	A0020															= "A0020";
	public static final String	A0021															= "A0021";
	public static final String	A0022															= "A0022";
	public static final String	A0023															= "A0023";
	public static final String	A0031															= "A0031";
	public static final String	A0033															= "A0033";
	public static final String	A0038															= "A0038";
	public static final String	A0041															= "A0041";
	public static final String	A0072															= "A0072";
	public static final String	A0073															= "A0073";
	public static final String	A0074															= "A0074";
	public static final String	A0077															= "A0077";
	public static final String	A0077_RET														= "A0077_RET";
	public static final String	A0077_NAD														= "A0077_NAD";
	public static final String	A0078															= "A0078";
	public static final String	A0078_RET														= "A0078_RET";
	public static final String	A0078_NAD														= "A0078_NAD";
	public static final String	A0081															= "A0081";
	public static final String	A0090															= "A0090";
	public static final String	A0096															= "A0096";
	public static final String	A0097															= "A0097";
	public static final String	A0098															= "A0098";
	public static final String	A0099															= "A0099";
	public static final String	A0100															= "A0100";
	public static final String	A0124															= "A0124";
	public static final String	A0180															= "A0180";
	public static final String	A0184															= "A0184";
	public static final String	A0187															= "A0187";
	public static final String	A0200															= "A0200";
	public static final String	A0202															= "A0202";
	public static final String	A0203															= "A0203";
	public static final String	A0203_DC														= "A0203_DC";
	public static final String	A0203_FC														= "A0203_FC";
	public static final String	A0204															= "A0204";
	public static final String	A0205															= "A0205";
	public static final String	A0206															= "A0206";
	public static final String	A0210															= "A0210";
	public static final String	A0211															= "A0211";
	public static final String	A0229															= "A0229";
	public static final String	A0231															= "A0231";
	public static final String	A0234															= "A0234";
	public static final String	A0240															= "A0240";
	public static final String	A0404															= "A0404";
	public static final String	A0405															= "A0405";
	public static final String	A0410															= "A0410";
	public static final String	A0430															= "A0430";
	public static final String	A0430_SUPC														= "A0430_SUPC";
	public static final String	A0430_CUPC														= "A0430_CUPC";
	public static final String	A0430_IUPC														= "A0430_IUPC";
	public static final String	A0430_PUPC														= "A0430_PUPC";
	public static final String	A0431															= "A0431";
	public static final String	A0497															= "A0497";
	public static final String	A0500															= "A0500";
	public static final String	A0501															= "A0501";
	public static final String	A0502															= "A0502";

	public static final String	A0106															= "A0106";
	public static final String	A0108															= "A0108";
	public static final String	A0128															= "A0128";

	public static final String	ITMDECCNT														= "ITMDECCNT";
	public static final String	ITMSKUCNT														= "ITMSKUCNT";
	public static final String	ITMWSKUCNT														= "ITMWSKUCNT";
	public static final String	ITMVNDCNT														= "ITMVNDCNT";
	public static final String	ITMSUPCCNT														= "ITMSUPCCNT";
	public static final String	ITMCUPCCNT														= "ITMCUPCCNT";
	public static final String	ITMIUPCCNT														= "ITMIUPCCNT";
	public static final String	ITMPUPCCNT														= "ITMPUPCCNT";
	public static final String	ITMHAZCNT														= "ITMHAZCNT";
	public static final String	ITMENVCNT														= "ITMENVCNT";
	public static final String	ITMOTHCNT														= "ITMOTHCNT";
	public static final String	ITMPVTCNT														= "ITMPVTCNT";

	public static final String	STEE103															= "STEE103";
	public static final String	STEE109															= "STEE109";
	public static final String	STEE110															= "STEE110";

	public static final String	SUPER_CATEGORY													= "SuperCategory";
	public static final String	CATEGORY														= "Category";
	public static final String	DEPARTMENT														= "Department";
	public static final String	CLASS															= "Class";
	public static final String	WEB_SUPER_CATEGORY												= "Web_Super_Category";
	public static final String	WEB_CATEGORY													= "Web_Category";
	public static final String	WEB_DEPARTMENT													= "Web_Department";
	public static final String	WEB_CLASS														= "Web_Class";
	public static final String	WEB_CLASS_ID													= "Web_Class_ID";
	public static final String	STAPLES_DOT_COM_CLASS											= "StaplesDotComClass-";

	public static final String	NON_STOCK_ITEM													= "Non Stock Item";

	public static final String	CREATE_NEW_ITEM													= "Create New Item";

	public static final String	PRODUCT_SYNCHRONIZATION											= "Product Synchronization";

	public static final String	ATTRIBUTE_SYNCHRONIZATION										= "Attribute Synchronization";

	public static final String	NEW																= "NEW";

	public static final String	NO																= "N";

	public static final String	YES																= "Y";

	public static final String	NI																= "NI";

	public static final String	OH																= "OH";

	public static final String	CO																= "CO";

	public static final String	DEFAULT_1														= "1";

	public static final String	DEFAULT_2														= "2";

	public static final String	DEFAULT_0														= "0";

	public static final String	DEFAULT_A														= "A";

	public static final String	DEFAULT_0001_01_01												= "0001-01-01";

	public static final String	DEFAULT_NAD														= "NAD";

	public static final String	SKULIFECYCLE													= "SKULifecycle";

	// variable to publish datamigration feed to galaxy which would not have
	// skulifecycle attribute.
	public static final String	PUBLISHTOGALAXY													= "PublishtoGalaxy";

	public static final String	MAINTENANCE														= "MAINTENANCE";

	public static final String	UPDATE_ITEM														= "Update Item";

	public static final String	RECLASS															= "Reclass";

	public static final String	FUTURE_DATED_PO													= "Future Dated PO";

	public static final String	BOTH															= "Both";

	public static final String	COST															= "Cost";

	public static final String	CORPORATE														= "COR";

	public static final String	RETAIL															= "RET";

	public static final String	SCC																= "SCC";

	public static final String	LI																= "LI";

	public static final String	LIST															= "List";

	public static final String	ACTIVATED														= "Activated";

	public static final String	ACTION_CODE														= "Action Code";

	public static final String	REQUEST_TYPE													= "Request_Type";

	public static final String	SKU_LEVEL														= "SKU Level";

	public static final String	SPECIFIC_DC														= "Specific DC";

	public static final String	SPECIFIC_FC														= "Specific FC";

	public static final String	MQ_HOSTNAME														= "mq.hostname";

	public static final String	MQ_PORT															= "mq.port";

	public static final String	MQ_QUEUE_MANAGER												= "mq.queuemanager";

	public static final String	MQ_CHANNEL														= "mq.channel";

	public static final String	MQ_QUEUE_NAME_FDPO												= "mq.queuename_futurePODate";

	public static final String	MQ_QUEUE_NAME													= "mq.queuename";

	public static final String	MQ_TIMEOUT														= "mq.timeout";

	public static final String	LIST_OF_MESSAGES_TO_HASHMAP										= "ListOfMessagesToHashMap";

	public static final String	SKU_LIFE_CYCLE_TO_HASHMAP										= "SkuLifeCycleToHashMap";

	public static final String	PRODUCT_ID_TO_HASHMAP											= "ProductIdToHashMap";

	public static final String	PUBLISH_CONFIRMATION_MSG_TO_MQ									= "sell side items sent to MQ: iD =";

	public static final String	FTP_HOST														= "ftp.Host";

	public static final String	FTP_USER														= "ftp.User";

	public static final String	FTP_PASSWORD													= "ftp.Password";

	public static final String	FTP_DESTINATION_DIRECTORY										= "ftp.DestinationDirectory";

	public static final String	FTP_ORIGINATED_DIRECTORY										= "ftp.OriginatedDirectory";

	public static final String	XSV_HEADER_LINE_ATTRIBUTE_IDS									= "XSV_HEADER_LINE_ATTRIBUTE_IDS";

	public static final String	EXCEL_HEADER_LINE_ATTRIBUTE_IDS									= "EXCEL_HEADER_LINE_ATTRIBUTE_IDS";

	public static final String	XSV_HEADER_LINE_ATTRIBUTE_NAMES									= "XSV_HEADER_LINE_ATTRIBUTE_NAMES";

	public static final String	FTP_HOST_WORDFILTER												= "ftp.Host.WordFilter";
	public static final String	FTP_USER_WORDFILTER												= "ftp.User.WordFilter";
	public static final String	FTP_PASSWORD_WORDFILTER											= "ftp.Password.WordFilter";
	public static final String	FTP_TARGETDIRECTORY_WORDFILTER									= "ftp.TargetDirectory.WordFilter";
	public static final String	FTP_LOCALDIRECTORY_WORDFILTER									= "ftp.localDirectory.WordFilter";
	public static final String	FTP_TARGETDIRECTORY_LOCATIONLEVELPUSHDOWNRESULT					= "ftp.TargetDirectory.LocationLevelPushdownResult";
	public static final String	FTP_LOCALDIRECTORY_LOCATIONLEVELPUSHDOWNRESULT					= "ftp.localDirectory.LocationLevelPushdownResult";

	public static final String	TRACE_ID														= "traceid";

	public static final String	CURRENT_FILE_TO_HASHMAP											= "currentFileToHashMap";

	public static final String	TIME_STAMP_STEP_EXPORT_TO_HASHMAP								= "timeStampSTEPExportToHashMap";

	public static final String	TRACE_ID_JOB_RUNNER_TO_HASHMAP									= "traceIdJobRunnerToHashMap";

	public static final String	TRACE_ID_TRANSFORM_TO_HASHMAP									= "traceIdTransformToHashMap";

	public static final String	TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP							= "targetProductFileUrlToHashMap";

	public static final String	TARGET_ATTRIBUTE_FILE_XSV_URL_TO_HASHMAP						= "targetAttributeFileUrlToHashMap";

	public static final String	TARGET_ATTRIBUTE_FILE_XSV_TEMP_URL_TO_HASHMAP					= "targetAttributeFileUrlTempToHashMap";

	public static final String	TARGET_FILE_FIX_LENGTH_URL_TO_HASHMAP							= "targetFileFixLengthUrlToHashMap";

	public static final String	TARGET_FILE_EXCEL_URL_TO_HASHMAP								= "targetFileExcelhUrlToHashMap";

	public static final String	BOTH_ITEM_AND_SKU_TO_HASHMAP									= "bothItemAndSKU";

	public static final String	JOB_DATE_yyyyMMddTO_HASHMAP										= "jobDateToHashMap";

	public static final String	DATE_MMdd_TO_HASHMAP											= "dateToHashMap";

	public static final String	ASSETS_MAP_TO_HASHMAP											= "assetsMapToHashMap";

	public static final String	CLASSIFICATION_MAP_TO_HASHMAP									= "classificationMapToHashMap";

	public static final String	CATEGORY_MAP_TO_HASHMAP											= "categoryMapToHashMap";

	public static final String	ATTR_ID_NAME_HASH_MAP_TO_HASHMAP								= "attrIDNameHashMapToHashMap";

	public static final String	ASSET_HASH_MAP_TO_HASHMAP										= "assetHashMapToHashMap";

	public static final String	CLASSIFICATION_HASH_MAP_TO_HASHMAP								= "classificationHashMapToHashMap";

	public static final String	JOB_LAUNCHER													= "jobLauncher";

	public static final String	ITEM_CREATE_UPDATE_JOB											= "itemCreateUpdateJob";

	public static final String	FILE_UNPROCESSED												= "File_Unprocessed";

	public static final String	FILE_DONE														= "File_Done";

	public static final String	FILE_BAD														= "File_Bad";

	public static final String	SMTP_HOST_NAME													= "SMTP_HOST_NAME";

	public static final String	SMTP_AUTH_USER													= "SMTP_AUTH_USER";

	public static final String	SMTP_AUTH_PWD													= "SMTP_AUTH_PWD";

	public static final String	FROM_ADDRESS													= "FROM_ADDRESS";

	public static final String	TO_ADDRESS														= "TO_ADDRESS";

	public static final String	STEP_USER_INT													= "stepint";

	public static final String	STEP_USER_PCM													= "VCXPCM";

	public static final String	PROTOCOL														= "sftp://";

	public static final String	COLON															= ":";

	public static final String	AT_SIGN															= "@";

	public static final String	R																= "R";

	// Data Migration Constant - added
	public static final String	MERCH_INPUTFILE_DELIMITER										= "MERCH_INPUTFILE_DELIMITER";

	public static final String	PRODUCT_INPUTFILE_DELIMITER										= "PRODUCT_INPUTFILE_DELIMITER";

	public static final String	RESPONSIBILITY_MATRIX_INPUTFILE_DELIMITER						= "RESPONSIBILITY_MATRIX_INPUTFILE_DELIMITER";

	public static final String	VENDOR_UPC_INPUTFILE_DELIMITER									= "VENDOR_UPC_INPUTFILE_DELIMITER";

	// SFTP configuration details

	public static final String	SFTP_HOST_STEP													= "sftp.hostname.step";

	public static final String	SFTP_USERNAME_STEP												= "sftp.username.step";

	public static final String	SFTP_PASSWORD_STEP												= "sftp.password.step";

	public static final String	SFTP_MERCH_UPDATE_TARGETDIR_STEP								= "sftp.merch.update.targetdirectory.step";

	public static final String	SFTP_MERCH_DELETE_TARGETDIR_STEP								= "sftp.merch.delete.targetdirectory.step";

	public static final String	SFTP_RM_UPDATE_TARGETDIR_STEP									= "sftp.rm.update.targetdirectory.step";

	public static final String	SFTP_CNP_INBOUND_CREATE_TARGETDIR_STEP							= "sftp.cnp.inbound.create.targetdirectory.step";

	public static final String	SFTP_CNP_INBOUND_UPDATE_TARGETDIR_STEP							= "sftp.cnp.inbound.update.targetdirectory.step";

	public static final String	SFTP_PRODUCT_UPDATE_TARGETDIR_STEP								= "sftp.product.update.targetdirectory.step";

	public static final String	SFTP_VENDORUPC_UPDATE_TARGETDIR_STEP							= "sftp.vendorupc.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_PRODUCT_TARGETDIR_STEP								= "sftp.wayfair.product.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_PRICING_UPDATE_TARGETDIR_STEP						= "sftp.wayfair.pricing.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_IMAGES_UPDATE_TARGETDIR_STEP						= "sftp.wayfair.images.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_ATTRIBUTEFEED_UPDATE_TARGETDIR_STEP				= "sftp.wayfair.attributefeed.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_TAXONOMY_UPDATE_TARGETDIR_STEP						= "sftp.wayfair.taxonomy.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_ATTRIBUTEMETADATA_UPDATE_TARGETDIR_STEP			= "sftp.wayfair.attributemetadata.update.targetdirectory.step";

	public static final String	SFTP_WAYFAIR_ACTIVESKU_UPDATE_TARGETDIR_STEP					= "sftp.wayfair.activesku.update.targetdirectory.step";

	public static final String	SFTP_ORACLE_FINANCIAL_TARGETDIR_STEP							= "sftp.oracle.financial.targetdirectory.step";

	public static final String	LOCATION_FEED_SFTP_HOST_STEP									= "LOCATION_FEED_SFTP_HOST_STEP";
	public static final String	LOCATION_FEED_SFTP_USERNAME_STEP								= "LOCATION_FEED_SFTP_USERNAME_STEP";
	public static final String	LOCATION_FEED_SFTP_PASSWORD_STEP								= "LOCATION_FEED_SFTP_PASSWORD_STEP";
	public static final String	LOCATION_FEED_SFTP_TARGET_DIR_STEP								= "LOCATION_FEED_SFTP_TARGET_DIR_STEP";
	public static final String	A0497_RET														= "A0497_RET";
	public static final String	A0497_NAD														= "A0497_NAD";

	public static final String	PCF_ITEM_PUBLISH_ENABLE											= "PCF_ITEM_PUBLISH_ENABLE";

	public static final String	UNABLE_EXTRACT_OBJECT_FROM_CALLABLE_STATEMENT_ERROR_MSG_TEXT	= "mapResults :: Unable to extract object from the CallableStatement~";

	public static final String	TILDA_DELIM														= "~";
	public static final String	DELIM_LESS_LESS													= "<<";
	public static final String	DELIM_GREATER_GREATER											= ">>";
	public static final String	DELIM_GREATER													= ">";

	public static final int		LOCATION_LEVEL_STRING_LENGHT									= 36000;

	public static final int		LOCATION_LEVEL_THRESHOLD										= 10;

	public static final String	LOCATION_LEVEL_MX_CODE_0										= "0";
	public static final String	LOCATION_LEVEL_MX_CODE_6										= "6";
	public static final String	LOCATION_LEVEL_MX_CODE_7										= "7";
	public static final String	LOCATION_LEVEL_MX_CODE_11										= "11";
	public static final String	LOCATION_LEVEL_MX_CODE_12										= "12";
	public static final String	LOCATION_LEVEL_MX_CODE_13										= "13";
	public static final String	LOCATION_LEVEL_MX_CODE_14										= "14";
	public static final String	LOCATION_LEVEL_MX_CODE_15										= "15";
	public static final String	LOCATION_LEVEL_MX_CODE_16										= "16";
	public static final String	LOCATION_LEVEL_MX_CODE_17										= "17";
	public static final String	LOCATION_LEVEL_MX_CODE_18										= "18";
	public static final String	LOCATION_LEVEL_MX_CODE_19										= "19";
	public static final String	LOCATION_LEVEL_MX_CODE_23										= "23";
	public static final String	LOCATION_LEVEL_MX_CODE_31										= "31";
	public static final String	LOCATION_LEVEL_MX_CODE_37										= "37";
	public static final String	LOCATION_LEVEL_MX_CODE_45										= "45";
	public static final String	LOCATION_LEVEL_MX_CODE_46										= "46";
	public static final String	LOCATION_LEVEL_MX_CODE_47										= "47";
	public static final String	LOCATION_LEVEL_MX_CODE_48										= "48";
	public static final String	LOCATION_LEVEL_MX_CODE_49										= "49";
	public static final String	LOCATION_LEVEL_MX_CODE_50										= "50";
	public static final String	LOCATION_LEVEL_MX_CODE_51										= "51";
	public static final String	LOCATION_LEVEL_MX_CODE_52										= "52";
	public static final String	LOCATION_LEVEL_MX_CODE_53										= "53";
	public static final String	LOCATION_LEVEL_MX_CODE_54										= "54";
	public static final String	LOCATION_LEVEL_MX_CODE_55										= "55";
	public static final String	LOCATION_LEVEL_MX_CODE_56										= "56";
	public static final String	LOCATION_LEVEL_MX_CODE_62										= "62";
	public static final String	LOCATION_LEVEL_MX_CODE_64										= "64";
	public static final String	LOCATION_LEVEL_MX_CODE_65										= "65";
	public static final String	LOCATION_LEVEL_MX_CODE_97										= "97";

	public static final String	LOCATION_LEVEL_RET_CHANGE_WHERE									= "RET";
	public static final String	LOCATION_LEVEL_SCC_CHANGE_WHERE									= "SCC";
	public static final String	LOCATION_LEVEL_COR_CHANGE_WHERE									= "COR";
	public static final String	LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE							= "NOT_APPLICABLE";
	public static final String	LOCATION_LEVEL_REACTIVATION_USE_CASE							= "REACTIVATION";
	public static final String	LOCATION_LEVEL_SKU_CONVERSION_USE_CASE							= "SKU_CONVERSION";
	public static final String	LOCATION_LEVEL_ADD_SKU_TO_RETAIL_USE_CASE						= "ADD_SKU_TO_RETAIL";
	public static final String	LOCATION_LEVEL_SKULEVEL_CHANGE_FOR								= "SKU_LEVEL";
	public static final String	LOCATION_LEVEL_SPECIFIC_FC_CHANGE_FOR						    = "SPECIFIC_FC";
	public static final String	LOCATION_LEVEL_SPECIFIC_DC_CHANGE_FOR						    = "SPECIFIC_DC";
	public static final String	LOCATION_LEVEL_SPECIFIC_STORE_CHANGE_FOR					    = "SPECIFIC_STORE";
	public static final String	LOCATION_LEVEL_NOT_DEFINED_CHANGE_FOR					        = "NOT_DEFINED";

	public static final String LOCATION_LEVEL_DC_ITEM_LINK                                      = "DCItemLink";
	public static final String LOCATION_LEVEL_FC_ITEM_LINK                                      = "FCItemLink";
	public static final String LOCATION_LEVEL_STORE_ITEM_LINK                                   = "StoreItemLink";
	public static final String LOCATION_LEVEL_ALL_LINK                                          = "All Location Link";
	public static final String LOCATION_LEVEL_ALL_LOCATION_ID                                   = "All Location";

	/*
	 * Supplier SETUP
	 */
	public static final String	DELETEUSER														= "DELETEUSER";
	public static final String	ADDTEMPFOLDERGROUP												= "ADDTEMPFOLDERGROUP";
	public static final String	ADDTEMPUSER														= "ADDTEMPUSER";
	public static final String	DELETETEMPFOLDERGROUP											= "DELETETEMPFOLDERGROUP";
	public static final String	ADDPERMFOLDERGROUP												= "ADDPERMFOLDERGROUP";
	public static final String	ADDPERMUSER														= "ADDPERMUSER";

	public static final String	METAFILE_ADDTEMP_FORMAT											= "METAFILE_ADDTEMP_FORMAT";
	public static final String	METAFILE_DELTEMP_FORMAT											= "METAFILE_DELTEMP_FORMAT";
	public static final String	METAFILE_ADDPERM_FORMAT											= "METAFILE_ADDPERM_FORMAT";

	public static final String	METAFILE_DELTEMP_DELAY											= "METAFILE_DELTEMP_DELAY";

	public static final String	SUPPLIER_SETUP_INBOUND											= "SUPPLIER_SETUP_INBOUND";
	public static final String	TMP_META_FOLDER													= "TMP_META_FOLDER";
	public static final String	SSO_SFTP_USER_NAME												= "SSO_SFTP_USER_NAME";
	public static final String	SSO_SFTP_PASSWORD												= "SSO_SFTP_PASSWORD";
	public static final String	SSO_SFTP_HOST_NAME												= "SSO_SFTP_HOST_NAME";
	public static final String	SSO_SFTP_PORT													= "SSO_SFTP_PORT";

	/*
	 * Prohibit Item Sale Trigger
	 */
	public static final String	PROHIBIT_ITEM_SALE_TRIGGER_INBOUND								= "PROHIBIT_ITEM_SALE_TRIGGER_INBOUND";
	public static final String	PROHIBIT_ITEM_SALE_TRIGGER_TEMP									= "PROHIBIT_ITEM_SALE_TRIGGER_TEMP";

	/*
	 * Env
	 */
	public static final String	SPRINGBATCH_ENV													= "SpringBatchEnv";

	public static final String	ARCHIVES_PROCESS_ENABLE											= "ARCHIVES_PROCESS_ENABLE";

	// WAYFAIR
	public static final String	WAYFAIRPRICE_INPUTFILE_DELIMITER								= "WAYFAIRPRICE_INPUTFILE_DELIMITER";

	public static final String	WAYFAIRIMAGE_INPUTFILE_DELIMITER								= "WAYFAIRIMAGE_INPUTFILE_DELIMITER";

	public static final String	WHOLESALER_FEED_SFTP_HOST_STEP									= "WHOLESALER_FEED_SFTP_HOST_STEP";
	public static final String	WHOLESALER_FEED_SFTP_USERNAME_STEP								= "WHOLESALER_FEED_SFTP_USERNAME_STEP";
	public static final String	WHOLESALER_FEED_SFTP_PASSWORD_STEP								= "WHOLESALER_FEED_SFTP_PASSWORD_STEP";
	public static final String	WHOLESALER_DOTCOM_FEED_SFTP_TARGET_DIR_STEP						= "WHOLESALER_DOTCOM_FEED_SFTP_TARGET_DIR_STEP";
	public static final String	WHOLESALER_CONTRACT_FEED_SFTP_TARGET_DIR_STEP					= "WHOLESALER_CONTRACT_FEED_SFTP_TARGET_DIR_STEP";

	// PYRAMID
	public static final String	PYR_IOB_TM_SFTP_HOST_STEP										= "PYR_IOB_TM_SFTP_HOST_STEP";
	public static final String	PYR_IOB_TM_SFTP_USERNAME_STEP									= "PYR_IOB_TM_SFTP_USERNAME_STEP";
	public static final String	PYR_IOB_TM_SFTP_PASSWORD_STEP									= "PYR_IOB_TM_SFTP_PASSWORD_STEP";
	public static final String	PYR_IOB_TM_SFTP_DOWNLOAD_TARGET_DIR_STEP						= "PYR_IOB_TM_SFTP_DOWNLOAD_TARGET_DIR_STEP";
	public static final String	PYR_IOB_TM_SFTP_UPLOAD_TARGET_DIR_STEP							= "PYR_IOB_TM_SFTP_UPLOAD_TARGET_DIR_STEP";

	// ASSET IMPORT
	public static final String	ASSET_IMPORT_INPUT_FOLDER										= "ASSET_IMPORT_INPUT_FOLDER";
	public static final String	ASSET_IMPORT_DONE_FOLDER										= "ASSET_IMPORT_DONE_FOLDER";
	public static final String	ASSET_IMPORT_OUTPUT_FOLDER										= "ASSET_IMPORT_OUTPUT_FOLDER";
	public static final String	ASSET_IMPORT_SFTP_TARGET_DIR_STEP								= "ASSET_IMPORT_SFTP_TARGET_DIR_STEP";
	public static final String	ASSET_IMPORT_SFTP_HOST_STEP										= "ASSET_IMPORT_SFTP_HOST_STEP";
	public static final String	ASSET_IMPORT_SFTP_USERNAME_STEP									= "ASSET_IMPORT_SFTP_USERNAME_STEP";
	public static final String	ASSET_IMPORT_SFTP_PASSWORD_STEP									= "ASSET_IMPORT_SFTP_PASSWORD_STEP";
	public static final String	ASSET_FILE_NAME													= "ASSET";
	public static final String	XLSX_EXTN														= ".xlsx";

	// COPY ATTRIBUTES
	public static final String	SFTP_COPY_ATTRIBUTES_STEP										= "sftp.copyattributes.attributes.targetdirectory.step";
	public static final String	SFTP_COPY_ATTRIBUTES_HEADER_STEP								= "sftp.copyattributes.attributeheaders.targetdirectory.step";
	public static final String	SFTP_COPY_ATTRIBUTES_LOV_STEP									= "sftp.copyattributes.attributeslov.targetdirectory.step";
	public static final String	SFTP_COPY_ATTRIBUTES_SPEC_STEP									= "sftp.copyattributes.specdata.targetdirectory.step";
	public static final String	SFTP_COPY_ATTRIBUTES_TAXONOMY_STEP								= "sftp.copyattributes.taxonomy.targetdirectory.step";
	public static final String	SFTP_COPY_ATTRIBUTES_ENTITY_STEP								= "sftp.copyattributes.entity.targetdirectory.step";

	// PYRAMID NOTIFY MERCHANTS
	public static final String	FREEFORM_TRACE_LOGGER_NOTIFY_MERCHANTS							= "tracelogger.notifyMerchants";

	public static final String	NOTIFY_MERCHANTS_UNPROCESSED_FOLDER								= "NOTIFY_MERCHANTS_UNPROCESSED_FOLDER";
	public static final String	NOTIFY_MERCHANTS_TEMP_FOLDER									= "NOTIFY_MERCHANTS_TEMP_FOLDER";
	public static final String	NOTIFY_MERCHANTS_DONE_FOLDER									= "NOTIFY_MERCHANTS_DONE_FOLDER";
	public static final String	NOTIFY_MERCHANTS_OUTPUT_FOLDER									= "NOTIFY_MERCHANTS_OUTPUT_FOLDER";
	public static final String	NOTIFY_MERCHANTS_EMAIL_SUBJECT									= "NOTIFY_MERCHANTS_EMAIL_SUBJECT";
	public static final String	NOTIFY_MERCHANTS_EMAIL_MESSAGE									= "NOTIFY_MERCHANTS_EMAIL_MESSAGE";
	public static final String	NOTIFY_MERCHANTS_TO_ADDRESS										= "NOTIFY_MERCHANTS_TO_ADDRESS";
	// WERCS
	public static final String SFTP_WERCS_WERCSRESPONSE_XML_STEP								= "sftp.wercs.wercsresponsexml.targetdirectory.step";

	public static final String WERCS_PIP_STEP_SFTP_TARGET_DIR_STEP								= "sftp.wercs.pip.to.step.targetdirectory.step";
	
	public static final String	WERCS_SMTP_HOST_NAME											= "WERCS_SMTP_HOST_NAME";
	public static final String	WERCS_SMTP_AUTH_USER											= "WERCS_SMTP_AUTH_USER";
	public static final String	WERCS_SMTP_AUTH_PWD												= "WERCS_SMTP_AUTH_PWD";
	public static final String	WERCS_FROM_ADDRESS												= "WERCS_FROM_ADDRESS";
	public static final String	WERCS_TO_ADDRESS												= "WERCS_TO_ADDRESS";

	
}