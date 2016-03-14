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

public interface IntgSrvUtilConstants {

	/**
	 * CONFIG_DIR_LOCATION_DEFAULT
	 */
	public static final String	CONFIG_DIR_LOCATION_DEFAULT				= "/opt/stibo/SpringBatch/configurations";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	APPLICATION_CONTEXT_FILE				= "/context_FDPO.xml";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	ITEM_CREATE_UPDATE_SCHEDULER			= "/job-Scheduler-ItemCreateUpdate.xml";

	/**
	 * CONFIG_COMMON_PROPERTIES_FILE
	 */
	public static final String	CONFIG_COMMON_PROPERTIES_FILE			= "/config_common.properties";

	/**
	 * CONFIG_ENV_PROPERTIES_FILE
	 */
	public static final String	CONFIG_ENV_PROPERTIES_FILE				= "/config_env.properties";

	/**
	 * QUEUE_DETAILS_PROPERTIES_FILE
	 */
	public static final String	QUEUE_DETAILS_PROPERTIES_FILE			= "/queue_details.properties";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	MERCH_HIERARCHY_UPDATE_SCHEDULER		= "/job-Scheduler-Merch.xml";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	RESPONSIBILITY_MATRIX_UPDATE_SCHEDULER	= "/job-Scheduler-RespMatrix.xml";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	PRODUCT_CREATE_UPDATE_SCHEDULER			= "/job-Scheduler-Product.xml";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	VENDOR_UPC_UPDATE_SCHEDULER				= "/job-Scheduler-VendorUpc.xml";

	/**
	 * LOCATION FEED SCHEDULER CONTEXT FILE
	 */
	public static final String	LOCATION_FEED_SCHEDULER					= "/job-Scheduler-LocationFeed.xml";
	public static final String	JOB_LOCATION_FEED_CONTEXT				= "/job-LocationFeed.xml";

	/**
	 * APPLICATION_CONTEXT_FILE
	 */
	public static final String	FILE_ZIPMANAGER_SCHEDULER				= "/job-Scheduler-ZipManager.xml";

	/**
	 * INTEGRATION_SERVICES_DAO_CLASS
	 */
	public static final String	INTEGRATION_SERVICES_DAO_CLASS			= "com.staples.pim.base.persistence.daofactory.LocationLevelPushDownDAOImpl";

	/**
	 * SUPPLIER SETUP SCHEDULER FILE
	 */
	public static final String	SUPPLIERSETUP_SCHEDULER					= "/job-Scheduler-SupplierSetup.xml";
	/**
	 * PROHIBIT ITEM SALE TRIGGER SCHEDULER FILE
	 */
	public static final String	PROHIBITITEMSALETRIGGER_SCHEDULER		= "/job-Scheduler-ProhibitItemSaleTrigger.xml";
	/**
	 * ITEM UTILITY CONTEXT FILE
	 */
	public static final String	ITEMUTILITY_CONTEXT_FILE				= "/context-itemutility.xml";
	/**
	 * COPY AND PRINT CONTEXT FILE
	 */
	public static final String	COPYANDPRINT_CONTEXT_FILE				= "/context-cnp.xml";

	/**
	 * WHOLE SALERS SCHEDULER and Context
	 */
	public static final String	WHOLESALERS_FEED_SCHEDULER				= "/job-Scheduler-Wholesalers.xml";
	public static final String	WHOLESALER_DOTCOM_CONTEXT_FILE			= "/context-WholesalerDotcom.xml";
	public static final String	WHOLESALER_CONTRACT_CONTEXT_FILE		= "/context-WholesalerContract.xml";

	/**
	 * WAYFAIR CONTEXT FILE
	 */
	public static final String	WAYFAIR_CONTEXT_FILE					= "/job-Scheduler-Wayfair.xml";

	/**
	 * ASSET_IMPORT SCHEDULER FILE
	 */
	public static final String	ASSET_IMPORT_SCHEDULER					= "/job-Scheduler-AssetImport.xml";

	public static final String	PYRAMID_IOB_SCHEDULER					= "/job-Scheduler-PYRItemOnbrdTmUpdate.xml";

	public static final String	PYRAMID_IOB_CONTEXT_FILE				= "/context-PYRItemOnbrdTmUpdate.xml";
	/**
	 * ORACLE FINANCIAL SCHEDULER FILE
	 */
	public static final String	ORACLE_FINANCIAL						= "/job-Scheduler-OFSupplierHierarchy.xml";

	/**
	 * COPY ATTRIBUTES SCHEDULER FILE
	 */
	public static final String	COPY_ATTRIBUTES							= "/job-Scheduler-CopyAttributes.xml";

	public static final String	PYRAMID_NOTIFY_MERCHANTS_SCHEDULER		= "/job-Scheduler-NotifyMerchants.xml";
	public static final String	PYRAMID_NOTIFY_MERCHANTS_JOB_XML		= "/job-NotifyMerchants.xml";
	
	/**
	 * WERCS SCHEDULER FILE
	 */
	public static final String	WERCS_SCHEDULER_CONTEXT_FILE			= "/job-Scheduler-Wercs.xml";
	public static final String	UPDATEWERCS_REGULATORYDATA				= "/spring-batch-context.xml";

	public static final String	WERCS_REGULATORYDATA_CONTEXT_FILE		= "/context-wercscorpdmztostep.xml";
	public static final String	WERCS_MSDSDOC_CONTEXT_FILE				= "/context-WERCSMSDSDoc.xml";

	public static final String	WERCS_CORPDMZTOSTEP						= "/job-Scheduler-wercscorpdmztostep.xml";
	public static final String	WERCS_PIPTOSTEP							= "/job-Scheduler-wercspiptostep.xml";
	public static final String	STEP_PIP_CONTEXT_FILE					= "/context-WERCS-StepPip.xml";

	public static final String	PIP_STEP_CONTEXT_FILE					= "/context-WERCS-PipStep.xml";

}