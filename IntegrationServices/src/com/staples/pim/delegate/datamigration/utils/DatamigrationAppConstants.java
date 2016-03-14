
package com.staples.pim.delegate.datamigration.utils;

public class DatamigrationAppConstants {

	// LOGGER
	public static final String		EHF_LOGGER											= "ehflogger";

	public static final String		EHF_LOGGER_CNP										= "ehflogger.cnp";

	public static final String		EHF_LOGGER_WAYFAIR									= "ehflogger.wayfair";	

	public static final String		EHF_MSGTYPE_INFO_NONSLA								= "";

	public static final String		EHF_SPRINGBATCH_ITEMUTILITY_USER					= "SpringBatchUser";

	public static final String		EHF_SPRINGBATCH_PUBLISH_ID_PROD						= "PIME101";

	public static final String		EHF_SPRINGBATCH_PUBLISH_ID_MERCH					= "GALE102";

	public static final String		EHF_SPRINGBATCH_PUBLISH_ID_RESP						= "GALE103";

	public static final String		EHF_SPRINGBATCH_PUBLISH_ID_VENDOR_UPC				= "GALE104";

	public static final String		EHF_PUBLISH_ID_WAYFAIRPRICE							= "TLDE003";

	public static final String		EHF_PUBLISH_ID_WAYFAIRIMAGE							= "TLDE004";

	public static final String		EHF_PUBLISH_ID_WAYFAIR_PRODUCT						= "TLDE003";

	public static final String		EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED				= "TLDE004";

	public static final String		EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_METADATA			= "TLDE003";

	public static final String		EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY				= "TLDE004";

	public static final String		EHF_PUBLISH_ID_WAYFAIR_ACTIVE_SKU					= "TLDE004";	

	public static final String		FREEFORM_TRACE_LOGGER								= "tracelogger";

	public static final String		FREEFORM_TRACE_LOGGER_PRODUCT						= "tracelogger.product";

	public static final String		FREEFORM_TRACE_LOGGER_MERCH							= "tracelogger.merch";

	public static final String		FREEFORM_TRACE_LOGGER_RESP							= "tracelogger.resp";

	public static final String		FREEFORM_TRACE_LOGGER_VENDORUPC						= "tracelogger.vendorupc";

	public static final String		FREEFORM_TRACE_LOGGER_CNP							= "tracelogger.cnp";

	public static final String		FREEFORM_TRACE_LOGGER_ITEMUTLITY					= "tracelogger.itemutility";
	
	public static final String		FREEFORM_TRACE_LOGGER_WAYFAIRPRICE					= "tracelogger.wayfairprice";

	public static final String		FREEFORM_TRACE_LOGGER_WAYFAIRIMAGE					= "tracelogger.wayfairimage";	
	
	// DataMigration common

	public static final String		ACTION_CODE_DELETE									= "D";

	public static final String		A0484_STR											= "A0484";

	public static final String		A0523_STR											= "A0523";

	public static final String		A0522_STR											= "A0522";

	public static final String		A0524_STR											= "A0524";

	public static final String		A0521_STR											= "A0521";

	public static final String		A0024_STR											= "A0024";

	public static final String		A0025_STR											= "A0025";

	public static final String		A0026_STR											= "A0026";

	public static final String		A0532_STR											= "A0532";

	public static final String		A0530_STR											= "A0530";

	// MerchandisingHierarchy DataMigration

	public static final String		MERCHANDISING_OUTPUT_FOLDER							= "MERCHANDISING_OUTPUT_FOLDER";

	public static final String		MERCHANDISING_OUTPUT_FOLDER_DELETE					= "MERCHANDISING_OUTPUT_FOLDER_DELETE";

	public static final String		MERCHANDISING_FILEDONE_FOLDER						= "/opt/stibo/integration/hotfolder/MerchIncoming/File_Done/";

	// WayfairImage

	public static final String		WAYFAIRIMAGE_OUTPUT_FOLDER							= "WAYFAIRIMAGE_OUTPUT_FOLDER";

	public static final String		WAYFAIRIMAGE_INPUT_FOLDER							= "WAYFAIRIMAGE_INPUT_FOLDER";

	public static final String		WAYFAIRIMAGE_BAD_FOLDER								= "WAYFAIRIMAGE_BAD_FOLDER";

	public static final String		WAYFAIRIMAGE_ITEMNO									= "WAYFAIRIMAGE_ITEMNO";

	public static final String		IMG_NO												= "IMG_NO";

	public static final String		IMG_SEQNO											= "IMG_SEQNO";

	public static final String		IMAGE_NAME											= "IMAGE_NAME";

	// WayFairPrice

	public static final String		WAYFAIRPRODUCT_OUTPUT_FOLDER						= "WAYFAIRPRODUCT_OUTPUT_FOLDER";

	public static final String		WAYFAIRPRICE_INPUT_FOLDER							= "WAYFAIRPRICE_INPUT_FOLDER";

	public static final String		WAYFAIRPRICE_BAD_FOLDER								= "WAYFAIRPRICE_BAD_FOLDER";

	public static final String		A0547												= "A0547";

	public static final String		PRC_COSTPRICE										= "PRC_COSTPRICE";

	public static final String		PRC_SHIPCOST										= "PRC_SHIPCOST";

	public static final String		PRC_POCOST											= "PRC_POCOST";

	public static final String		PRC_SHIPCOSTTYPE									= "PRC_SHIPCOSTTYPE";

	public static final String		A0197												= "A0197";

	public static final String		PRC_LISTPRICE										= "PRC_LISTPRICE";

	// WayfairProduct

	public static final String		WAYFAIRPRODUCT_INPUT_FOLDER							= "WAYFAIRPRODUCT_INPUT_FOLDER";
	public static final String		WAYFAIRPRICE_OUTPUT_FOLDER							= "WAYFAIRPRICE_OUTPUT_FOLDER";
	public static final String		WAYFAIR_PRICE_HEADER								= "WAYFAIR_PRICE_HEADER";

	// WayfairTaxonomy
	public static final String		WAYFAITAXONOMY_INPUT_FOLDER							= "WAYFAITAXONOMY_INPUT_FOLDER";
	public static final String		WAYFAITAXONOMY_OUTPUT_FOLDER						= "WAYFAITAXONOMY_OUTPUT_FOLDER";

	/*
	 * public static final String
	 * MERCHANDISING_DELIMITER="xsvfile.delimiter.merch";
	 */

	public static final String		MERCHANDISING_OUTPUT_CHANNEL_DELETE					= "merchDeleteOutputChannel";

	public static final String		MERCHANDISING_INPUT_FOLDER							= "MERCHANDISING_INPUT_FOLDER";
	
	// Responsibility Matrix

	public static final String		RESPMATRIX_FILEDONE_FOLDER							= "/opt/stibo/integration/hotfolder/RespMatrixIncoming/File_Done/";

	public static final String		RESPONSIBILITY_MATRIX_OUTPUT_FOLDER					= "RESPONSIBILITY_MATRIX_OUTPUT_FOLDER";

	public static final String		RESPONSIBILITY_MATRIX_INPUT_FOLDER					= "RESPONSIBILITY_MATRIX_INPUT_FOLDER";

	// Product

	public static final String		PRODUCT_FILEDONE_FOLDER								= "/opt/stibo/integration/hotfolder/ProductIncoming/File_Done/";

	public static final String		PRODUCT_OUTPUT_FOLDER								= "PRODUCT_OUTPUT_FOLDER";

	public static final String		PRODUCT_OUTPUT_FOLDER_CNP							= "PRODUCT_OUTPUT_FOLDER_CNP";

	// public static final String FREEFORM_TRACE_LOGGER =
	// "tracelogger.datamigration.product";

	public static final String		PRODUCT_INPUT_FOLDER								= "PRODUCT_INPUT_FOLDER";

	public static final String		PRODUCT_BAD_FOLDER									= "PRODUCT_BAD_FOLDER";

	public static final String		ERROR_FREEFORM_TRACE_LOGGER							= "errortracelogger.datamigration.product";

	public static final String		FILE_UNPROCESSED									= "file_unprocessed";

	public static final String		FILE_BAD											= "file_bad";

	// public static final String
	// DATAMIGRATION_PRODUCT_DELIMITER="datamigration.product.delimiter";

	public static final String		PRODUCT_MERCHID_MAPPING_TRANSFORMATION_ENABLE		= "PRODUCT_MERCHID_MAPPING_TRANSFORMATION_ENABLE";

	public static final String		PRODUCT_VALIDATIONS_ENABLE							= "PRODUCT_VALIDATIONS_ENABLE";

	public static final String		PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE				= "PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE";

	public static final String		EMPTY_STR											= "";
	public static final String		NULL_STR											= "NULL";
	public static final String		NA_STR												= "N/A";

	public static final String		RULE_ID_STR											= " RuleID# ";

	// upc

	public static final String		UPC_OUTPUT_CHANNEL									= "";

	public static final String		VENDOR_UPC_OUTPUT_FOLDER							= "VENDOR_UPC_OUTPUT_FOLDER";

	public static final String		VENDOR_UPC_INPUT_FOLDER								= "VENDOR_UPC_INPUT_FOLDER";

	// public static final String UPC_DELIMITER="delimiter.upc";

	public static final String		UPC_FILEDONE_FOLDER									= "/opt/stibo/integration/hotfolder/UPCIncoming/File_Done/";

	public static final String		CONTEXT_ID_VALUE									= "EnglishUS";

	public static final String		EXPORT_CONTEXT_VALUE								= "EnglishUS";

	public static final boolean		USE_CONTEXT_LOCALE_VALUE							= false;

	public static final String		WORKSPACE_ID_VALUE									= "Main";

	public static final String		ITEM_STR											= "Item";

	public static final String		A0019_STR											= "A0019";

	public static final String		A0012_STR											= "A0012";

	public static final String		A1363_STR											= "A1363";

	public static final String		LIST_OF_VALUES_PROPERTIES							= "/list-of-values.properties";

	public static final String		ISO_DATE_TIME_FORMAT								= "yyyy-MM-dd HH:mm:ss";

	public static final String		ACTION_CODE_A										= "A";

	public static final String		ACTION_CODE_U										= "U";

	public static final String		ACTION_CODE_D										= "D";

	public static final String		ACTION_CODE_M										= "M";

	public static final String		ACTION_CODE_N										= "N";

	public static final String		SKU_STR												= "SKU";

	public static final String		SYSTEM_OUT_CONSOLE_ENABLE							= "SYSTEM_OUT_CONSOLE_ENABLE";

	public static final String		ERROR_REPORT_FILE_PREFIX							= "Error_Report_";

	public static final String		ISO_DATE_FORMAT										= "yyyy-MM-dd";

	public static final String		DDMMMYYYY_DATE_FORMAT								= "dd-MMM-yyyy";

	public static final String		PCF_DATE_FORMAT										= "EEE MMM dd HH:mm:ss Z yyyy";
	// YYYY-MM-DD HH24:MI:SS

	public static final String		PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL				= "PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL";

	// CnP
	public static final String		INBOUND_MAPPING_FILE								= "/inbound_mapping.xml";

	public static final String		DEFAULT_PIMCORE_XML_TEST							= "c:/temp/inbound/pimcore.xml";

	public static final String		DEFAULT_STEP_XML_TEST								= "c:/temp/inbound/Step.xml";

	public static final String		CONFIG_DIR_DEFAULT									= "/opt/stibo/SpringBatchCopynPrint/configurations";

	public static final String		SPRING_BATCH_CANDP_CONFIG_DIR						= "SPRING_BATCH_CANDP_CONFIG_DIR";

	public static final String		COMMA												= ",";

	public static final String		HYPHEN												= "-";

	public static final String		STEP_DATE_FORMAT									= "yyyy-MM-dd hh:mm:ss";

	public static final String		VICTOR_OUTFILE_DATE_FORMAT							= "yyyyMMddHHmmss";

	public static final String		PATH_TYPE											= "file:";

	public static final String		CONTEXT_FILE										= "/context.xml";

	public static final String		SFTPCONTEXT_FILE									= "/SFTPContext.xml";

	public static final String		PROPERTIES_FILE_DEFAULT								= "config_common.properties";

	public static final String		Y_STR												= "Y";

	public static final String		N_STR												= "N";

	public static final String		YES_STR												= "YES";

	public static final String		NO_STR												= "NO";

	public static final String		STRING_TYPE											= "STRING";

	public static final String		FLOAT_TYPE											= "FLOAT";

	public static final String		INTEGER_TYPE										= "INTEGER";

	public static final String		INBOUND_DEBUG_PATH_SB_IN							= "/debug/inbound/springbatch-in/";

	public static final String		INBOUND_DEBUG_PATH_SB_OUT							= "/debug/inbound/springbatch-out/";

	public static final String		OUTBOUND_DEBUG_PATH_SB_OUT							= "/debug/outbound/springbatch-out/";

	public static final String		TRUE_STR											= "true";

	public static final String		DEBUG_XML_GENERATE_FLAG								= "debug.xml.generate.flag";

	public static final String		THREAD_SLEEP_INTERVAL_MS							= "thread.sleep.interval.ms";

	public static final String		MQ_MAX_RETRY_COUNT									= "mq.max.retry.count";

	public static final String		PARENT_ID_ITEM_FOLDER_NAME							= "item.folder.name";

	public static final String		DEFAULT_PARENT_ID_ITEM_FOLDER_NAME					= "Unassigned";
	// For out bound Spread sheet names

	// public static final String[] WORK_SHEET_NAMES =
	// {"Web Hierarchy","Config Hierarchy","Templates","Configurations","Images"};

	public static final String[]	WORK_SHEET_NAMES_WEB								= { "Web Hierarchy", "Image References" };

	public static final String[]	WORK_SHEET_NAMES_PROD								= { "Templates", "Template Configurations",
			"Template Components", "Invalid Associations", "Kit Contents", "Image References", "Style References" };

	public static final String[]	WORK_SHEET_NAMES_CONFIG								= { "Config Hierarchy", "Component Properties",
			"Messaging Type", "Image References"										};

	public static final String		WEB_HIERARCHY										= "Web Hierarchy";

	public static final String		CONFIG_HIERARCHY									= "Config Hierarchy";

	public static final String		TEMPLATES											= "Templates";

	public static final String		CONFIGURATIONS										= "Template Configurations";

	public static final String		IMAGES												= "Image References";

	// For out bound spread sheet's Headers

	public static final String[]	WEB_HIERARCHY_HEADERS								= { "Hierarchy Detail ID", "Hierarchy Detail Name",
		"Hierarchy Detail Short Desc", "Hierarchy Detail Long Desc", "Display Sequence", "Hierarchy Detail Active Ind",
		"Hierarchy Level ID", "Hierarchy Level Name", "Hierarchy Detail Parent Id", "Hierarchy ID" };

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	public static final String[]	CONFIG_HIERARCHY_HEADERS							= { "Config Hierarchy Detail ID",
			"Config Hierarchy Detail Name", "Config Hierarchy Detail Short Desc", "Display Sequence", "Active Ind", "Display Ind",
			"Drives Cost Ind", "Custom Input Indicator", "Custom Input Format", "Custom Input Prompt", "DCS Only Ind",
			"Third Party Only Ind", "Base UOM", "Measurement Scope", "Quantity Multipler", "Quantity Divider", "Config Hierarchy Level ID",
			"Config Hierarchy Level Name", "Config Hierarchy Detail Parent ID"			};


	public static final String[]	CONFIGURATIONS_HEADERS								= { "Template ID", "Template Configuration ID",
			"Configuration Attribute ID", "Configuration Attribute Value ID", "Default Attr Value Ind", "Active Ind" };

	public static final String[]	IMAGES_HEADERS										= { "Entity ID", "Entity Type", "Image ID",
			"Image Type", "Default Ind"												};

	// Hierarchy Level

	public static final String		CATEGORY											= "Category";

	public static final String		PRODUCTTYPE											= "ProductType";

	public static final String		STYLE												= "Style";

	public static final String		ITEMCREATE											= "ItemCreate";

	public static final String		ITEMUPDATE											= "ItemUpdate";

	// For EHF logger

	public static final String		SPRINGBATCH_ECO_SYSTEM								= "PCM - Product Content Management";

	public static final String		SPRINGBATCH_COMPONENT_ID							= "Spring Batch Copy and Print";

	public static final String		SPRINGBATCH_ICD_CLASSNAME							= "com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD";

	public static final String		PUBLISH_ID_NEW										= "PIME004";
	// Std or Non Std or Service - new item

	public static final String		PUBLISH_ID_UPDATE									= "PIME005";
	// Std or Non Std or Service - update item

	// public static final String EHF_SPRINGBATCH_ITEMUTILITY_USER =
	// "SpringBatchCopynPrint";

	public static final String		EHF_ATTR_TRANSACTION_TYPE_QUEUETOSPRING				= "Queue-to-Spring";

	public static final String		EHF_ATTR_TRANSACTION_TYPE_QUEUETOQUEUE				= "Queue-to-Queue";

	// public static final String[] PRODUCT_TEMPLATE_FANIDS = {"A1021", "A0012",
	// "A1024", "A1025", "A1022", "A1027", "A0212", "A0211", "A1345", "A1059",
	// "A1233", "A1061", "A1234", "A1235", "A1236", "A1062", "A1357", "A1021",
	// "A1352", "A1358"};

	// FAN ID STR

	// public static final String A0019_STR = "A0019";
	//
	// public static final String A0012_STR = "A0012";
	//
	// public static final String A1363_STR = "A1363";

	public static final String		VALUE_TYPE											= "ValueType";

	public static final String		VALUES_TYPE											= "ValuesType";

	public static final String		ASSET_CROSS_REFERENCE_TYPE							= "AssetCrossReferenceType";

	public static final String		PRODUCT_TYPE										= "ProductType";

	public static final String		MATERIAL_CATEGORY									= "MaterialCategory";

	public static final String		PRIMARY_IMAGE										= "PrimaryImage";

	public static final String		PRODUCT_CROSS_REFERENCE_TYPE						= "ProductCrossReferenceType";

	public static final String		MULTIVALUE_TYPE										= "MultiValueType";

	public static final String		METADATA_TYPE										= "MetaDataType";

	public static final String		PRIMARY_MATERIAL_HIERARCHY							= "PrimaryMaterialHierarchy";

	public static final String		PRIMARY_PRODUCTS									= "Primary Products";

	public static final String		ITEM												= "Item";

	public static final String		SKUFOLDER											= "SKUFolder";

	public static final String		PRIMARY_ITEM										= "PrimaryItem";

	public static final String		CHILD_MATERIAL										= "ChildMaterial";

	public static final String		NAME_TYPE											= "NameType";

	public static final String		CLASSIFICATION_TYPE									= "ClassificationType";

	public static final int			LEVEL_ONE											= 1;

	public static final int			LEVEL_TWO											= 2;

	public static final int			LEVEL_THREE											= 3;

	public static final String		VICTOR_OUTPUT_CHANNEL								= "vectorOutputChannel";

	public static final String		SFTP_HOSTNAME_VICTOR								= "cnp.sftp.hostname.victor";

	public static final String		IMAGE_TYPE											= "A1383";

	// Phase 2
	public static final String		SBCATEGORY											= "SBCategory";

	public static final String		SBPRODUCT											= "SBProduct";

	public static final String		SBSTYLE												= "SBStyle";

	public static final String		EPRICEBOOKHIERARCHY									= "ePriceBookHierarchy";

	public static final String		SOLUTIONBUILDERHIERARCHY							= "SolutionBuilderHierarchy";

	public static final String		EPRICEBOOK											= "ePriceBook";

	public static final String		SOLUTIONBUILDER										= "SolutionBuilder";

	// Phase 2
	public static final String		TEMPLATE_COMPONENTS									= "Template Components";

	public static final String		INVALID_ASSOCIATIONS								= "Invalid Associations";

	public static final String		STYLE_REFERENCES									= "Style References";

	public static final String		KIT_CONTENTS										= "Kit Contents";

	public static final String		KIT_TYPE											= "Kit Template";

	public static final String		COMPONENT_PROPERTIES								= "Component Properties";

	public static final String		MESSAGING_TYPE										= "Messaging Type";

	public static final String[]	TEMPLATES_HEADERS									= { "Template ID", "Template SKU", "Item Type",
		"Template Name", "Template Short Description", "Template Long Description", "Template Type", "Pricing Strategy",
		"IsSaleableInd", "Sell UOM", "Sell UOM Qty", "Base UOM", "Order Qty List", "Min Order Qty", "Max Order Qty",
		"Prepayment Required", "Artwork Provision Method", "Turn Time Range", "Available For Quick Delivery Indicator",
		"Production Notes", "Active Ind", "Exception Pages Allowed Ind"			};

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	public static final String[]	TEMPLATE_COMPONENTS_HEADERS							= { "Template ID",
			"Configuration Attribute Name ID", "Configuration Attribute Value ID", "Default Value Ind", "Suppress Msg Ind", "Active Ind",
			"Custom Qty Min Value", "Custom Qty Max Value", "Applicable Pages", "Exception Page Config Attribute Ind" };

	public static final String[]	INVALID_ASOCIATIONS_HEADERS							= { "Template ID",
			"Configuration Attribute Value ID", "Invalid Configuration Attribute Value ID", "Applicable for Exception Pages" };

	public static final String[]	STYLE_REFERENCES_HEADERS							= { "Template ID", "Style ID", "Hierarchy ID" };

	public static final String[]	KIT_CONTENTS_HEADERS								= { "Template ID", "Kit Contained SKU",
			"Active Ind", "Kit Contained SKU Qty"										};

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	public static final String[]	COMPONENT_PROPERTIES_HEADERS						= { "Config Attr Value ID",
			"Config Attr Value Name", "Property Fan ID", "Property Name", "Property Value" };

	public static final String[]	MESSAGING_TYPE_HEADERS								= { "Config Attr Value ID",
			"Config Attr Value Name", "Message Type", "Targeted Value ID", "Targeted Value Name", "Message Title", "Message Description" };

	public static final String		CNP_MQ_CHANNEL_STEP_IN								= "cnp.mq.channel.step.in";

	public static final String		CNP_MQ_HOSTNAME_STEP_IN								= "cnp.mq.hostname.step.in";

	public static final String		CNP_MQ_PORT_STEP_IN									= "cnp.mq.port.step.in";

	public static final String		CNP_MQ_QUEUEMANAGER_STEP_IN							= "cnp.mq.queuemanager.step.in";

	public static final String		CNP_MQ_QUEUENAME_STEP_IN							= "cnp.mq.queuename.step.in.itemcreate";

	public static final String		CNP_SFTP_TARGETDIRECTORY_VICTOR						= "cnp.sftp.targetdirectory.victor";

	public static final String		CNP_SFTP_PASSWORD_VICTOR							= "cnp.sftp.Password.victor";

	public static final String		CNP_SFTP_USERNAME_VICTOR							= "cnp.sftp.username.victor";

	public static final String		CNP_SFTP_PRIVATEKEY_VICTOR							= "cnp.sftp.privatekey.victor";

	// Item utility
	public static final String		SPRING_BATCH_CONFIG_DIR								= "SPRING_BATCH_CONFIG_DIR";

	public static final String		HEADER_SPLITER										= "~|~";

	public static final String		VALUE_SPLITER										= "|";

	// PCMP-766 - Modified for Channel specific attribute
	public static final String		A0077_ID											= "A0077_RET";
	// PCMP-766 - Modified for Channel specific attribute
	public static final String		A0078_ID											= "A0078_RET";

	// PCMP-766 channel specific implementation
	public static final String		A0077_RET_ID										= "A0077_RET";

	// PCMP-766 channel specific implementation
	public static final String		A0078_RET_ID										= "A0078_RET";

	// PCMP-766 channel specific implementation
	public static final String		A0077_NAD_ID										= "A0077_NAD";

	// PCMP-766 channel specific implementation
	public static final String		A0078_NAD_ID										= "A0078_NAD";

	// PCMP-766 channel specific implementation
	public static final String		A0497_RET_ID										= "A0497_RET";

	// PCMP-766 channel specific implementation
	public static final String		A0497_NAD_ID										= "A0497_NAD";

	// PCMP-766 channel specific implementation
	public static final String		RET_STR												= "RET";

	// PCMP-766 channel specific implementation
	public static final String		SCC_STR												= "SCC";

	// public static final String COR_STR="COR";

	public static final String		A0410_ID											= "A0410";

	public static final String		A0012_ID											= "A0012";
	public static final String		A0012_SUNBEAM										= "A0012_SUNBEAM";	

	public static final String		A0003_ID											= "A0003";

	public static final String		A0013_ID											= "A0013";

	public static final String		A0075_ID											= "A0075";

	public static final String		A0497_ID											= "A0497";
	
	public static final String		A0017_ID											= "A0017";

	public static final String		A0051_ID											= "A0051";

	public static final String		A0020_ID											= "A0020";

	public static final String		A0015_ID											= "A0015";

	public static final String		A0030_ID											= "A0030";

	public static final String		A0052_ID											= "A0052";

	public static final String		A0385_ID											= "A0385";	

	public static final String		KEY_VALUE_KEY_ID									= "Galaxy SKU Number";

	public static final String		LIST_PRICE_UPDATE									= "List Price Update";

	public static final String		PO_COST_UPDATE										= "PO Cost Update";

	public static final String		CONSIGNMENT_CODE_UPDATE								= "Consignment Code Update";

	public static final String		CONSIGNMENT_COST_UPDATE								= "Consignment Cost Update";

	public static final String		MERCH_STATUS_UPDATE									= "Merch Status Update";

	public static final String		PRICE_UPDATE										= "Price Update";

	public static final String		POG_ID_UPDATE										= "POG ID";

	public static final String		SKU_TYPE_UPDATE										= "SKU Type Update";

	public static final String		STOCK_CODE_UPDATE									= "Stock Code Update";

	public static final String		OH													= "OH";

	public static final String		NI													= "NI";

	public static final String		HEADER												= "VALUES";

	public static final String		MODEL_NUMBER_VALIDATION								= "[A-Z0-9\\s\\\\\\^\\$\\|\\?\\*\\+\\(\\)\\{\\}\\[\\]\\/=~:;<lt/><gt/>_!@`#%&-]*{1,50}";

	public static final String		CHANNEL_VALIDATION[]								= { "NAD", "COR", "SCC", "RET" };

	public static final String		ATTRIBUTE_PROPERTIES								= "/config_common.properties";

	public static final String		CONTEXT_FILE_ITEMUTILITY							= "/context-itemutility.xml";

	public static final String		PRODUCT_ITEM_HEADER_NAME							= "product.item.header.name";

	public static final String		PRODUCT_ITEM_HEADER_POSITION						= "product.item.header.position";

	public static final String		ITEM_ATTRIBUTE_NAME									= "item.attribute.name";

	public static final String		ITEMUTILITY_GALAXYOUT_MINLENGTH						= "itemutility.galaxyout.minlength";

	public static final String		ITEM_ATTRIBUTE_POSITION								= "item.attribute.position";

	public static final double		LIST_PO_MIN											= 0.01;

	public static final double		LIST_PO_MAX											= 99999.99;

	public static final int			VENDOR_NO_MIN										= 1;

	public static final int			VENDOR_NO_MAX										= 999999;

	public static final int			MODEL_NO_LENGTH										= 50;

	public static final String		COR_STR												= "COR";

	public static final String		XML_STR												= "/outputs/xml/";

	// For EHF logger

	public static final String		SPRINGBATCH_COMPONENT_ID_ITEMUTILITY				= "Spring Batch ItemUtility";

	public static final String		PUBLISH_ID											= "ITEMUTILITY";

	public static final String		EHF_SPRINGBATCH_ITEMUTILITY_USER_ITEMUTILITY		= "SpringBatchItemutility";

	public static final String		EHF_ATTR_TRANSACTION_TYPE_QUEUETODB					= "Queue-to-DB";

	public static final String		ITEMUTILITY_MQ_CHANNEL_STEP							= "itemutility.mq.channel.step";

	public static final String		ITEMUTILITY_MQ_HOSTNAME_STEP						= "itemutility.mq.hostname.step";

	public static final String		ITEMUTILITY_MQ_PORT_STEP							= "itemutility.mq.port.step";

	public static final String		ITEMUTILITY_MQ_QUEUEMANAGER_STEP					= "itemutility.mq.queuemanager.step";

	public static final String		ITEMUTILITY_MQ_QUEUENAME_STEP						= "itemutility.mq.queuename.step";
	
	public static final String		WAYFAIR_TAXONOMY_OUTPUT_FOLDER						= "wayfair.taxonomy.output.folder";

	public static final String		WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER						= "wayfair.attribute.output.folder";

	public static final String		WAYFAIR_ACTIVESKU_OUTPUT_FOLDER						= "wayfair.activesku.output.folder";

	public static final String		WAYFAIR_ATTRIBUTE_FEED_OUTPUT_FOLDER				= "wayfair.attributefeed.output.folder";

	// WAYFAIR

	public static final String		WAYFAIR_DOTCOM_HIERARCHYCODE						= "1";

	public static final String		WAYFAIR_QUILL_HIERARCHYCODE							= "2";

	public static final String		WAYFAIR_REFERENCE_CURRENT							= "current";

	public static final String		WAYFAIR_REFERENCE_OLD								= "old";

	public static final String		WAYFAIR_ATTRIBUTE_LOOKUP_ATTRIBUTEMETADATA_FILENAME	= "/AttributeMetadatalookupfileforAttributeFeed.xsv";

	public static final String		WAYFAIR_TAXONOMY_LOOKUP_PRODUCT_FILENAME			= "/Taxonomylookupfileforproduct.xsv";

	public static final String		REFERENCE_FILE_DELIMITER							= "~";

	public static final String		OT_BULLET_INPUT_FOLDER								= "OT_BULLET_INPUT_FOLDER";

	public static final String		OT_BULLET_BAD_FOLDER								= "OT_BULLET_BAD_FOLDER";

	public static final String		OT_BULLET_DONE_FOLDER								= "OT_BULLET_DONE_FOLDER";

	public static final String		OT_BULLET_OUTPUT_FOLDER								= "OT_BULLET_OUTPUT_FOLDER";

	public static final String		OT_HIERARCHY_INPUT_FOLDER							= "OT_HIERARCHY_INPUT_FOLDER";

	public static final String		OT_HIERARCHY_BAD_FOLDER								= "OT_HIERARCHY_BAD_FOLDER";

	public static final String		OT_HIERARCHY_DONE_FOLDER							= "OT_HIERARCHY_DONE_FOLDER";

	public static final String		OT_HIERARCHY_OUTPUT_FOLDER							= "OT_HIERARCHY_OUTPUT_FOLDER";

	public static final String		CLASS_ID_REFERENCE_FILE_CURRENT_PATH				= "CLASS_ID_REFERENCE_FILE_CURRENT_PATH";

	public static final String		CLASS_ID_REFERENCE_FILE_OLD_PATH					= "CLASS_ID_REFERENCE_FILE_OLD_PATH";

	public static final String		WAYFAIR_PRODUCT_TARGET_SFTP_FOLDER					= "sftp.wayfair.product.update.targetdirectory.step";

	public static final String		WAYFAIR_TAXONOMY_TARGET_SFTP_FOLDER					= "sftp.wayfair.taxonomy.update.targetdirectory.step";

	public static final String		WAYFAIR_ATTRIBUTEMETADATA_TARGET_SFTP_FOLDER		= "sftp.wayfair.attributemetadata.update.targetdirectory.step";

	public static final String		WAYFAIR_ACTIVESKU_TARGET_SFTP_FOLDER				= "sftp.wayfair.activesku.update.targetdirectory.step";

	public static final String		WAYFAIR_CATEGORY_SPEC_ATTR_TARGET_SFTP_FOLDER		= "sftp.wayfair.attributefeed.update.targetdirectory.step";

	public static final String		WAYFAIR_IMAGES_TARGET_SFTP_FOLDER					= "sftp.wayfair.images.update.targetdirectory.step";

	public static final String		WAYFAIR_PRICING_TARGET_SFTP_FOLDER					= "sftp.wayfair.pricing.update.targetdirectory.step";

	public static final String		WAYFAIR_LOV_TARGET_SFTP_FOLDER						= "sftp.wayfair.lov.update.targetdirectory.step";

	public static final String		WAYFAIR_PRODUCT_INPUT_FOLDER						= "WAYFAIR_PRODUCT_INPUT_FOLDER";

	public static final String		WAYFAIR_PRODUCT_BAD_FOLDER							= "WAYFAIR_PRODUCT_BAD_FOLDER";

	public static final String		WAYFAIR_PRODUCT_DONE_FOLDER							= "WAYFAIR_PRODUCT_DONE_FOLDER";

	public static final String		WAYFAIR_PRODUCT_OUTPUT_FOLDER						= "WAYFAIR_PRODUCT_OUTPUT_FOLDER";

	public static final String		WAYFAIR_PRODUCT_MAX_RECORDS_PER_FILE				= "wayfair.product.maxrecordsper.file";

	public static final String		XSV_EXTENSION										= ".xsv";

	public static final String		DSV_EXTENSION										= ".dsv";

	public static final String		REPORTS_FOLDER_NAME									= "Report";

	public static final String		WAYFAIRATTRIBUTE_BAD_FOLDER							= "WAYFAIRATTRIBUTE_BAD_FOLDER";
	
	public static final String		FREEFORM_TRACE_LOGGER_WHOLESALER_DOTCOM				= "tracelogger.wholesalerdotcom";
	public static final String		FREEFORM_TRACE_LOGGER_WHOLESALER_CONTRACT			= "tracelogger.wholesalercontract";
	public static final String		FREEFORM_TRACE_LOGGER_PIP_TO_STEP					= "tracelogger.piptostep";
	public static final String		FREEFORM_TRACE_LOGGER_PYR_ITEMONBRDTM_UPDATE		= "tracelogger.pyrItemonbrdTmUpdate";
	public static final String		FREEFORM_TRACE_LOGGER_ASSET_IMPORT					= "tracelogger.assetImport";
	public static final String		FREEFORM_TRACE_LOGGER_STEP_TO_PIP					= "tracelogger.steptopip";
	
}