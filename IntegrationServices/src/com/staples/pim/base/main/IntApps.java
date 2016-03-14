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
 * File name     :  Apps.java
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.base.main;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.ContextLoaderFactory;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.ZipManager;

public class IntApps {

	private static IntgSrvLogger	logger						= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
/*
	private static final short		ITEMCREATEUPDATE			= 1;
	private static final short		FUTUREDATEDPO				= 2;
	private static final short		ITEMUTILITY					= 3;
	private static final short		COPYANDPRINT				= 4;
	private static final short		LOCATION_FEED				= 5;
	private static final short		MERCHUPDATE					= 6;
	private static final short		RESPONSIBILITYMATRIXUPDATE	= 7;
	private static final short		PRODUCTUPDATE				= 8;
	private static final short		VENDORUPCACTIVEUPDATE		= 9;
	private static final short		SUPPLIER_SETUP				= 10;
	private static final short 		WAYFAIR						= 11;
	private static final short		WHOLESALERS					= 12;
	private static final short 		ASSET_IMPORT				= 13;
*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int i = 0;
		//int applicationID = 1;

		//if (args.length > 0)
		//	applicationID = Integer.parseInt(args[0]);
		String appID = "ItemCreateUpdateReclass";
		if (args.length > 0) appID = args[0];
		for (String arg : args) {
			logger.info("com.staples.pcm.springbatch.xmltoxsv.App", "main", "number of args = " + i++ + "/" + args.length + ", arg = "
					+ arg);
		}

		try {
			/*
			switch (applicationID) {
				case ITEMCREATEUPDATE:

					// This job used to compress the files based on
					// configuration values
					String isZipEnable = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ARCHIVES_PROCESS_ENABLE);
					if ("True".equalsIgnoreCase(isZipEnable)) {
						ZipManager.loadContext();
					}

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ITEM_CREATE_UPDATE_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case FUTUREDATEDPO:
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.APPLICATION_CONTEXT_FILE);
					logger.debug("Application Context loaded successfully.");
					System.out.println("Application Context loaded successfully.");
					break;

				case ITEMUTILITY:
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ITEMUTILITY_CONTEXT_FILE);
					logger.info("Application Context loaded successfully.");
					logger.info("Galaxy Queue listener started.");
					break;

				case COPYANDPRINT:
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.COPYANDPRINT_CONTEXT_FILE);
					logger.info("Application Context loaded successfully.");
					logger.info("CnP queue listener started.");
					// Bill Cox stuff would be here context-cnp.xml
					break;

				case LOCATION_FEED:
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.LOCATION_FEED_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case MERCHUPDATE:

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.MERCH_HIERARCHY_UPDATE_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case RESPONSIBILITYMATRIXUPDATE:

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.RESPONSIBILITY_MATRIX_UPDATE_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case PRODUCTUPDATE:

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.PRODUCT_CREATE_UPDATE_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case VENDORUPCACTIVEUPDATE:

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.VENDOR_UPC_UPDATE_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;

				case SUPPLIER_SETUP:

					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.SUPPLIERSETUP_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;
					
				case WAYFAIR:
					
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.WAYFAIR_CONTEXT_FILE);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;
					
				case WHOLESALERS:
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.WHOLESALERS_FEED_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;
					

				case ASSET_IMPORT:
								
					ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ASSET_IMPORT_SCHEDULER);
					logger.debug("File System XML Application Context loaded successfully.");
					System.out.println("File System XML Application Context loaded successfully.");
					break;
			}
			*/
	        if (appID.equalsIgnoreCase("ItemCreateUpdateReclass")){
				// This job used to compress the files based on
				// configuration values
				String isZipEnable = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ARCHIVES_PROCESS_ENABLE);
				if ("True".equalsIgnoreCase(isZipEnable)) {
					ZipManager.loadContext();
				}

				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ITEM_CREATE_UPDATE_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("FutureDatedPO")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.APPLICATION_CONTEXT_FILE);
				logger.debug("Application Context loaded successfully.");
				System.out.println("Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("ItemUtility")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ITEMUTILITY_CONTEXT_FILE);
				logger.info("Application Context loaded successfully.");
				logger.info("Galaxy Queue listener started.");
	        }
	        else if (appID.equalsIgnoreCase("CopyandPrint")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.COPYANDPRINT_CONTEXT_FILE);
				logger.info("Application Context loaded successfully.");
				logger.info("CnP queue listener started.");
	        }
	        else if (appID.equalsIgnoreCase("LocationFeed")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.LOCATION_FEED_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("MerchUpdate")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.MERCH_HIERARCHY_UPDATE_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("ResponsibilityMatrixUpdate")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.RESPONSIBILITY_MATRIX_UPDATE_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("PCFProducts")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.PRODUCT_CREATE_UPDATE_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("VendorUPCActiveUpdate")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.VENDOR_UPC_UPDATE_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("SupplierSetup")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.SUPPLIERSETUP_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("Wayfair")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.WAYFAIR_CONTEXT_FILE);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("WholeSalers")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.WHOLESALERS_FEED_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("OFSupplierHierarchy")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ORACLE_FINANCIAL);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("CopyAttributes")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.COPY_ATTRIBUTES);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("AssetImport")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ASSET_IMPORT_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }	        
	        else if (appID.equalsIgnoreCase("PyramidIOBTemplate")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.PYRAMID_IOB_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("PyramidNotifyMerchants")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.PYRAMID_NOTIFY_MERCHANTS_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
	        else if (appID.equalsIgnoreCase("ProhibitItemSaleTrigger")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.PROHIBITITEMSALETRIGGER_SCHEDULER);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
	        }
			else if (appID.equalsIgnoreCase("Wercs")){
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.WERCS_SCHEDULER_CONTEXT_FILE);
				logger.debug("File System XML Application Context loaded successfully.");
				System.out.println("File System XML Application Context loaded successfully.");
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			System.out.println("Application Context is not loaded.");
		}
	}
}
