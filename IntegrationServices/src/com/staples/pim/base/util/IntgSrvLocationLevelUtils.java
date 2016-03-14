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
 * File name     :  IntgSrvLocationLevelUtils.java
 * Creation Date :  7/13/2015 
 * @author       :  Sima Zaslavsky
 * @version 1.0
 */ 
package com.staples.pim.base.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.IIntgSrvConnConstants;
import com.staples.pim.base.domain.ProductAttributes;
import com.staples.pim.base.persistence.daofactory.IntgSrvDAOFactory;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelPushDownData; 

public class IntgSrvLocationLevelUtils {
	
	@SuppressWarnings("unused")
	private static IntgSrvLogger LOGGER = IntgSrvLogger.getInstance("FreeformTraceLogger");  
	
	
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, List<StepLocationLevelAttrib>> sortBulkUpload(StepLocationLevelAttrib  locationLevelAttribBean,
									HashMap<Integer, List<StepLocationLevelAttrib>> attributesCollectionSort) 
								 
	{ 
		
		List<StepLocationLevelAttrib>  listForUpload = null;
		LOGGER.debug("SKU ID="+locationLevelAttribBean.getSkuNumber());
		LOGGER.debug("channel="+locationLevelAttribBean.getChannel());
		LOGGER.debug("is SKU Level="+locationLevelAttribBean.isSKULevelUpdate());
		LOGGER.debug("usecase="+locationLevelAttribBean.getUsecase());
		if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)){
			 /* Use case REACTIVATION / Retail */
			 if (locationLevelAttribBean.getUsecase().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_REACTIVATION_USE_CASE)
					 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(128)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(128));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(128, listForUpload);		
				LOGGER.debug("process ID=128");
				
				return attributesCollectionSort;
				  
			 }
			 /* Use case SKU_CONVERSION / Retail */
			 if (locationLevelAttribBean.getUsecase().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SKU_CONVERSION_USE_CASE)
					 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(129)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(129));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(129, listForUpload);		
				LOGGER.debug("process ID=129");
				
				return attributesCollectionSort;
				  
			 }
			 /* Use case ADD_SKU_TO_RETAIL / Retail */
			 if (locationLevelAttribBean.getUsecase().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ADD_SKU_TO_RETAIL_USE_CASE)
					 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(130)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(130));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(130, listForUpload);		
				LOGGER.debug("process ID=130");
				
				return attributesCollectionSort;
				  
			 }

		}
		
		if (locationLevelAttribBean.isSKULevelUpdate()){			
			/* RET  channel for A0028  / SKU Level*/
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE) &&
					 !locationLevelAttribBean.getRetailIA_Rebuyer().equalsIgnoreCase("") &&  
					 locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackLength().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackWidth().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackHeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPalletTi().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPalletHi().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitLength().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitWidth().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitHeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPurchaseCaseWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPurchaseCaseCube().equalsIgnoreCase("")
					 
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(100)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(100));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(100), listForUpload);		
				LOGGER.debug("process ID=100");
				//return attributesCollectionSort;
				  
				
			 }
			 /* RET  channel for A0075  / SKU Level*/
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE) &&
					 !locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getRetailIA_Rebuyer().equalsIgnoreCase("") &&  
					 locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackLength().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackWidth().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackHeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getMasterCasePackWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPalletTi().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPalletHi().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitLength().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitWidth().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitHeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getSellUnitWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPurchaseCaseWeight().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getPurchaseCaseCube().equalsIgnoreCase("")
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(101)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(101));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(101), listForUpload);		
				LOGGER.debug("process ID=101");
				
				//return attributesCollectionSort;
				  
			 }
			 /* COR  channel for A0075 / SKU Level */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)   &&
					 ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("")
					 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(102)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(102));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(102, listForUpload);		
				LOGGER.debug("process ID=102");
				
				//return attributesCollectionSort;
				  
			 }
			 /* RET  channel for A0067 or A0066 / SKU_Level */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getWhsp().equalsIgnoreCase("")
					 	)    
			 )
			 {
				 LOGGER.debug("Sucp="+locationLevelAttribBean.getSucp());
				 LOGGER.debug("Whsp="+locationLevelAttribBean.getWhsp());
				 LOGGER.debug("TargetMax="+locationLevelAttribBean.getTargetMax());
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(111)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(111));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(111), listForUpload);		
				LOGGER.debug("process ID=111");
				
				//return attributesCollectionSort;
				  
				
			 }
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getWhsp().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPurchaseCaseWeight().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPurchaseCaseCube().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletTi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletHi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")
					 	)
					 && ! locationLevelAttribBean.getSellUnitLength().equalsIgnoreCase("")
					 && ! locationLevelAttribBean.getSellUnitWidth().equalsIgnoreCase("")
					 && ! locationLevelAttribBean.getSellUnitHeight().equalsIgnoreCase("")
					 && ! locationLevelAttribBean.getSellUnitWeight().equalsIgnoreCase("")			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(117)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(117));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(117), listForUpload);		
				LOGGER.debug("process ID=117");
				
				//return attributesCollectionSort;
				  
				
			 }
			 /* RET  channel for A0013 / All Locations    */
			 if (
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
				 && (	 ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getMasterCasePackLength().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getMasterCasePackWidth().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getMasterCasePackHeight().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getMasterCasePackWeight().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getPalletTi().equalsIgnoreCase("")   	|| 
						 ! locationLevelAttribBean.getPalletHi().equalsIgnoreCase("")   	 
				 	)
				)
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(118)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(118));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(118, listForUpload);		
				LOGGER.debug("process ID=118");
				
				//return attributesCollectionSort;
				  
			 }
			 if (
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE))
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(119)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(119));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(119, listForUpload);		
				LOGGER.debug("process ID=119");
				
				//return attributesCollectionSort;
				  
			 }
			 /* SCC  channel for A0433 / SKU Level*/
			 if (
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE) 
				 && (! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")
						 || ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("")
						 || ! locationLevelAttribBean.getDiscontinuedDbmDbs().equalsIgnoreCase("")
						 || ! locationLevelAttribBean.getFcPurchaseFlag().equalsIgnoreCase("")
				 	)
				 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(122)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(122));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(122, listForUpload);		
				LOGGER.debug("process ID=122");
				
				//return attributesCollectionSort;
				  
			 }
			 /* RET  channel for A0023 / SKU Level*/
			 if (! locationLevelAttribBean.getDsdFlag().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE))
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(120)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(120));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(120), listForUpload);		
				LOGGER.debug("process ID=120");
				
				//return attributesCollectionSort;
			 }
			 /* SCC  channel for A0075
				A0013
				A0237
				A0238
				A0239
				A0240
				A0241
				A0390
				A0045
				A0046
				A0067
			 / SKU Level*/
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSellUnitLength().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSellUnitWidth().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSellUnitHeight().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSellUnitWeight().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPurchaseCaseWeight().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPurchaseCaseCube().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletTi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletHi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")
					 	)    
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(124)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(124));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(124), listForUpload);		
				LOGGER.debug("process ID=124");
				
				//return attributesCollectionSort;
				  
				
			 }
			 /*
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletTi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletHi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")
					 	)    
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(125)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(125));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(125), listForUpload);		
				LOGGER.debug("process ID=125");
				
				//return attributesCollectionSort;
			 }
			 */
			 /*
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletTi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getPalletHi().equalsIgnoreCase("")   	|| 
							 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")
					 	)    
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(127)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(127));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(127), listForUpload);		
				LOGGER.debug("process ID=127");
				
				//return attributesCollectionSort;
				  
				
			 }
			 */
			 



			 
			 return attributesCollectionSort;
		}
		else if (locationLevelAttribBean.getListOfDCs().size() > 0){
			 /* RET  channel for A0075 or A0013 / SPECIFIC_DC */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)   &&
					 (! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("") || ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")) 
					 && !locationLevelAttribBean.getDcFlow().equalsIgnoreCase("X")
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(104)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(104));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(104, listForUpload);		
				LOGGER.debug("process ID=104");
				
				//return attributesCollectionSort;
				  
			 }
			 /* RET  channel for A0374 or A0074 / SPECIFIC_DC */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)  &&
					 locationLevelAttribBean.getVendorNumber().equalsIgnoreCase("") &&
					 locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("") &&
					 (!locationLevelAttribBean.getDcFlow().equalsIgnoreCase("") || !locationLevelAttribBean.getStoreSourceWarehouse().equalsIgnoreCase(""))
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(106)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(106));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(106), listForUpload);		
				LOGGER.debug("process ID=106");
				
				//return attributesCollectionSort;
				  
				
			 }
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
					 && (	 ! locationLevelAttribBean.getSucp().equalsIgnoreCase("")   	||
							 ! locationLevelAttribBean.getTargetMax().equalsIgnoreCase("")  ||
							 ! locationLevelAttribBean.getDcHoldFlag().equalsIgnoreCase("")
					 	)    
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(114)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(114));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(114), listForUpload);		
				LOGGER.debug("process ID=114");
				
				return attributesCollectionSort;
				  
				
			 }
			 /* RET  channel for A0373 / SPECIFIC_DC*/
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
					 && locationLevelAttribBean.getDcFlow().equalsIgnoreCase("X")   
					 && locationLevelAttribBean.getStoreSourceWarehouse().equalsIgnoreCase("")   
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(121)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(121));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(121), listForUpload);		
				LOGGER.debug("process ID=121");
				
				//return attributesCollectionSort;
				  
				
			 }

			 
			 return attributesCollectionSort;
		}
		else if (locationLevelAttribBean.getListOfFCs().size() > 0){
			 /* NAD  channel for A0013  / FC Level*/
			 /*
			 if ( !locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")  		&&
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE))
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(126)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(126));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(126, listForUpload);		
				LOGGER.debug("process ID=126");
				
				//return attributesCollectionSort;
				  
			 }
			 */
			 /* NAD  channel for A0433; A0432; A0013; A0075  / FC Level*/
			/*
			 if ((!locationLevelAttribBean.getFcPurchaseFlag().equalsIgnoreCase("")   			||
					 ! locationLevelAttribBean.getDropShipFC().equalsIgnoreCase("")   			|| 
					 ! locationLevelAttribBean.getVendorModelNumber().equalsIgnoreCase("")   	|| 
					 ! locationLevelAttribBean.getVendorNumber().equalsIgnoreCase(""))   		&&
					 locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE))
			 */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE))
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(123)))
				 {
					 listForUpload = (List)attributesCollectionSort.get(Integer.valueOf(123));
				 }
				 else
				 {
					 listForUpload = new ArrayList();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(123, listForUpload);		
				LOGGER.debug("process ID=123");
				
				//return attributesCollectionSort;
				  
			 }
			 
			 return attributesCollectionSort;
		}
		else if (locationLevelAttribBean.getListOfStores().size() > 0){
			 /* RET  channel for A0374 or A0074 / SPECIFIC_STORE */
			 if (locationLevelAttribBean.getChannel().equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
			 )
			 {
				 if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(108)))
				 {
					 listForUpload = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(108));
				 }
				 else
				 {
					 listForUpload = new ArrayList<StepLocationLevelAttrib>();
				 }
					 
				listForUpload.add(locationLevelAttribBean);
				attributesCollectionSort.put(Integer.valueOf(108), listForUpload);		
				LOGGER.debug("process ID=108");
				
				//return attributesCollectionSort;
				  
				
			 }
			 return attributesCollectionSort;
		}	
		 
		 LOGGER.debug("process ID=No process qualified!");
		 return attributesCollectionSort;
	}
	
	
	
	public static  StepLocationLevelPushDownData getDataForRebuyerUpload(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 100 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_11)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForRebuyerUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForRebuyerUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	public static  StringBuffer getDataSegmentForRebuyerUpload(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIM_GREATER);
		LOGGER.debug("getDataSegmentForVendorRetUpload::DataLine="+stringBuffer.toString());			
		return stringBuffer;
	}
	
	public static  StepLocationLevelPushDownData getDataForDSDUpload(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 120 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_31)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForDSDUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForDSDUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForDSDUpload(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDsdFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc104(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 104 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_DC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_7)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc104(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc104(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForProc104(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc105(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 105 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_18)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc105(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc105(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForProc105(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc106(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 106 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_DC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_13)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc106(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc106(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForProc106(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getMerchStatus())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSrc())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreSourceWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfStores()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("N")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc108(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 108 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_STORE_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_31)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc108(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc108(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForProc108(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 

		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getMerchStatus())
					.append("A")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getDcPurchaseFlag())
					.append("A")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getStoreSourceWarehouse())
					.append("I")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfStores()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("N")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc111(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 118 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_15)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc111(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc111(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc111(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcHoldFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getListOfDCs())
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc114(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 118 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_DC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_14)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc114(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc114(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc114(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcHoldFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc117(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 117 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_15)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc117(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc117(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc117(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcHoldFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getListOfDCs())
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					//.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc118(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 118 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_49)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc118(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc118(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc118(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailProductManager())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)					
					.append(inputTO.getMasterCasePackLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getMasterCasePackWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getMasterCasePackHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getMasterCasePackWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc119(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 117 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_7)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc119(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc119(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc119(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcHoldFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getListOfDCs())
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					//.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	
	public static  StepLocationLevelPushDownData getDataForProc121(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 121 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_DC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_16)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc121(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc121(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	public static  StringBuffer getDataSegmentForProc121(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
		 
		stringBuffer.append(inputTO.getStoreSourceWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcFlow())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc122(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 122 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_62)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc122(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc122(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc122(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDropShipFC())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getAddRemoveSWO())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDiscontinuedDbmDbs())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getFcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getTargetMax())
					.append("N")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getListOfDCs())//inputTO.getListOfFCs()
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}
	
	public static  StepLocationLevelPushDownData getDataForProc123(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 123 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_FC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_13)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc123(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc123(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc123(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDropShipFC())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getAddRemoveSWO())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDiscontinuedDbmDbs())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getFcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getTargetMax())
					.append("N")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfFCs()))//inputTO.getListOfFCs()
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))//inputTO.getListOfFCs()
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}
	

	public static  StepLocationLevelPushDownData getDataForProc126(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 122 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_FC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_7)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc126(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc126(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc126(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDropShipFC())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getAddRemoveSWO())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDiscontinuedDbmDbs())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getFcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getListOfDCs())//inputTO.getListOfFCs()
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}
	
	public static  StepLocationLevelPushDownData getDataForProc124(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 124 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_7)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc124(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc124(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc124(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())// A0304?
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc125(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 117 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_53)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc125(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc125(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc125(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcHoldFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getListOfDCs())
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					//.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}

	public static  StepLocationLevelPushDownData getDataForProc127(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String dsInfo = null;
		/*  build Key Like for contract ID # 102 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_6)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForVendorCorUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForVendorCorUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc127(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("") 
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp()) 
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
					
		return stringBuffer;
	}
	public static  StepLocationLevelPushDownData getDataForProc128(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String dsInfo = null;
		/*  build Key Like for contract ID # 128 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_REACTIVATION_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOT_DEFINED_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForProc128(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 		 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailProductManager())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0054  Add on allowed
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0055  Distribution Pack
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0056  Distribution Policy Code
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0044  Velocity Code
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0057  Seasonal Code
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0058  Target  days
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0059  DC Min balance
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0060  Order PO Code
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0061  Order Base Qty
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0062  Order Incr Qty
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0063  Cases/Pallet
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("10")          //A0078  PO Cost
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0047  Foreign Cost/Unit Freight
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0048  Duty %
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0064  Other Cost
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0049  Other %
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWhsp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0066  Store Tmax
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getSucp())
					.append("10")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getMasterCasePackLength())
					.append("3")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getMasterCasePackWidth())
					.append("4")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getMasterCasePackHeight())
					.append("5")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")          //A0043  Sell UoM
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getMasterCasePackWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getMerchStatus())
					.append("Y")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					//.append(inputTO.getDcPurchaseFlag())
					.append("Y")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(cancatStringListWithComma(inputTO.getListOfDCs()))
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
					
		return stringBuffer;
	}
	public static  StepLocationLevelPushDownData getDataForProc129(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String dsInfo = null;
		/*  build Key Like for contract ID # 128 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKU_CONVERSION_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOT_DEFINED_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}
	public static  StepLocationLevelPushDownData getDataForProc130(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String dsInfo = null;
		/*  build Key Like for contract ID # 128 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_ADD_SKU_TO_RETAIL_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOT_DEFINED_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_0)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForProc128(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
		return dataObject; 
	
	}

	public static  StepLocationLevelPushDownData getDataForVendorRetUpload(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		
		/*  build Key Like for contract ID # 101 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_6)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_RET_CHANGE_WHERE); 
		String dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForVendorRetUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForVendorRetUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		

		return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForVendorRetUpload(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) {
		 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("") 
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcFlag()) 
					.append(IntgSrvAppConstants.DELIM_GREATER);
		LOGGER.debug("getDataSegmentForVendorRetUpload::DataLine="+stringBuffer.toString());			
		return stringBuffer;
	}
	
public static  StepLocationLevelPushDownData getDataForVendorCorUpload(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String dsInfo = null;
		/*  build Key Like for contract ID # 102 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SKULEVEL_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_6)
						.append(IntgSrvAppConstants.DELIMITER_TILDA);
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_COR_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);

		String dsInfo2 = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM ; 
		String procNameNad = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo2).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameNad(procNameNad);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForVendorCorUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForVendorCorUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForVendorCorUpload(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) {
		 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("")
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append("") 
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getUpcFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getRetailIA_Rebuyer())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitLength())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWidth())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitHeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSellUnitWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseWeight())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPurchaseCaseCube())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletTi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getPalletHi())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getSucp()) 
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
					
		return stringBuffer;
	}
	
public static  StepLocationLevelPushDownData getDataForVendorModelDropShipPurchaseFlagFCUpload(List<StepLocationLevelAttrib> listOfData) { 
		
		StepLocationLevelPushDownData  dataObject = new StepLocationLevelPushDownData();	
		StepLocationLevelAttrib  attribObject = null;
		StringBuffer stringBuffer = null;
		StringBuffer stringBufferKey = new StringBuffer();
		String delim = IntgSrvAppConstants.DELIMITER_TILDA;
		String dsInfo = null;
		
		/*  build Key Like for contract ID # 100 */
		stringBufferKey.append(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_NOTAPPLICABLE_USE_CASE)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_SPECIFIC_FC_CHANGE_FOR)
						.append(IntgSrvAppConstants.DELIMITER_TILDA)
						.append(IntgSrvAppConstants.LOCATION_LEVEL_MX_CODE_7)
						.append(IntgSrvAppConstants.DELIMITER_TILDA); 
		dataObject.setKeyLine(stringBufferKey.toString());
		dataObject.setChannel(IntgSrvAppConstants.LOCATION_LEVEL_SCC_CHANGE_WHERE); 
		dsInfo = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM; 
		String procNameRet = IntgSrvPropertiesReader.getProperty(
				  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.PROC).toString());
		dataObject.setProcNameRet(procNameRet);
	
		/* stay in the loop until all objects processed from the collection */
		int lineNum = 0;
		
		for (int i = 0; i < listOfData.size(); i ++)
		{
		
			attribObject = listOfData.get(i);
			
			if (stringBuffer == null )
			{
				stringBuffer = new StringBuffer();
			} 
	  
			if (stringBuffer.length() <= IntgSrvAppConstants.LOCATION_LEVEL_STRING_LENGHT) 
			{  				 
				stringBuffer = 
				 		IntgSrvLocationLevelUtils.getDataSegmentForVendorModelDropShipPurchaseFlagFCUpload(attribObject, stringBuffer);			  
			}
			else
			{	 
				lineNum = lineNum + 1;
				populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
				stringBuffer = new StringBuffer();
				stringBuffer = 
			 		IntgSrvLocationLevelUtils.getDataSegmentForVendorModelDropShipPurchaseFlagFCUpload(attribObject, stringBuffer);	
			}
			
		}
		if (lineNum == 0)
			lineNum = lineNum + 1;
		
		populateLineNumberWithConstructedSegments(dataObject, stringBuffer, lineNum);
		
	return dataObject; 
	
	}
	
	
	
	public static  StringBuffer getDataSegmentForVendorModelDropShipPurchaseFlagFCUpload(StepLocationLevelAttrib inputTO, StringBuffer stringBuffer) { 
				 
		stringBuffer.append(inputTO.getSkuNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorModelNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getVendorNumber())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDropShipFC())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getAddRemoveSWO())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getDiscontinuedDbmDbs())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getFcPurchaseFlag())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStore())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouse())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getZone())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getTargetMax())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStateCode())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getStoreOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getWarehouseOnly())
					.append(IntgSrvAppConstants.DELIMITER_TILDA)
					.append(inputTO.getListOfFCs())
					.append(IntgSrvAppConstants.DELIM_GREATER);
					
		return stringBuffer;
	}
	
	
	private static void populateLineNumberWithConstructedSegments(StepLocationLevelPushDownData dataObject, StringBuffer stringBuffer, int lineNum)
	{		
		switch (lineNum)
		{
			case 1 :
				dataObject.setDataline1(stringBuffer.toString());
				break;
			case 2 :
				dataObject.setDataline2(stringBuffer.toString());
				break;
			case 3 :
				dataObject.setDataline3(stringBuffer.toString());
				break;
			case 4 :
				dataObject.setDataline4(stringBuffer.toString());
				break;
			case 5 :
				dataObject.setDataline5(stringBuffer.toString());
				break;
			case 6 :
				dataObject.setDataline6(stringBuffer.toString());
				break;
			case 7 :
				dataObject.setDataline7(stringBuffer.toString());
				break;
			case 8 :
				dataObject.setDataline8(stringBuffer.toString());
				break;
			case 9 :
				dataObject.setDataline9(stringBuffer.toString());
				break;
			case 10 :
				dataObject.setDataline10(stringBuffer.toString());
				break;
			case 11 :
				dataObject.setDataline11(stringBuffer.toString());
				break;
			case 12 :
				dataObject.setDataline12(stringBuffer.toString());
				break;		
		}
		
	}
	  public static void addLocationToHM(HashMap hmLocationLevel,String locationAttrID,String locationAttrValue,String classificationType,String classificationID){
		  //HashMap<String,HashMap> hmLocationLevel = new HashMap();
		  System.out.println("Enter addLocationToHM()");
		  System.out.println("locationAttrID="+locationAttrID);
		  System.out.println("locationAttrValue="+locationAttrValue);
		  System.out.println("classificationType="+classificationType);
		  System.out.println("classificationID="+classificationID);
		  HashMap<String,HashMap> hmAttrValue = (HashMap<String, HashMap>) hmLocationLevel.get(locationAttrID);
		  if (hmAttrValue == null){//new attribute
			  
			  System.out.println("hmAttrValue == null");
			  hmAttrValue = new HashMap();
			  HashMap<String,List<String>> hmValue = new HashMap();
			  List<String> listOfStores = new ArrayList<String>();
			  List<String> listOfFCs = new ArrayList<String>();
			  List<String> listOfDCs = new ArrayList<String>();
			  List<String> listOfAllLocation = new ArrayList<String>();
			  
			  if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK)){
				  listOfStores.add(classificationID);
			  }
			  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK)){
				  listOfFCs.add(classificationID);
			  }
			  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK)){
				  listOfDCs.add(classificationID);
			  }
			  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK)){
				  listOfAllLocation.add(classificationID);
			  }
			  
			  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK, listOfStores);
			  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK, listOfFCs);
			  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK, listOfDCs);
			  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK, listOfAllLocation);
			  hmAttrValue.put(locationAttrValue, hmValue);
			  System.out.println("hmAttrValue ="+hmAttrValue);
			  hmLocationLevel.put(locationAttrID,hmAttrValue);
		  }
		  else {
			  HashMap<String,List<String>> hmValue = (HashMap<String, List<String>>) hmAttrValue.get(locationAttrValue);
			  if (hmValue == null){//new value
				  hmValue = new HashMap();
				  List<String> listOfStores = new ArrayList<String>();
				  List<String> listOfFCs = new ArrayList<String>();
				  List<String> listOfDCs = new ArrayList<String>();
				  List<String> listOfAllLocation = new ArrayList<String>();
				  
				  if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK)){
					  listOfStores.add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK)){
					  listOfFCs.add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK)){
					  listOfDCs.add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK)){
					  listOfAllLocation.add(classificationID);
				  }
				  
				  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK, listOfStores);
				  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK, listOfFCs);
				  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK, listOfDCs);
				  hmValue.put(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK, listOfAllLocation);
				  hmAttrValue.put(locationAttrValue, hmValue);
				  System.out.println("hmAttrValue ="+hmAttrValue);
			  }
			  else {// same attribute and same value just add to location list
				  //List<String> listOfStores = hmValue.get("StoreItemLink");
				  //List<String> listOfFCs = hmValue.get("FCItemLink");
				  //List<String> listOfDCs = hmValue.get("DCItemLink");
				  
				  if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK)){
					  hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK).add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK)){
					  hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK).add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK)){
					  hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK).add(classificationID);
				  }
				  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK)){
					  hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK).add(classificationID);
				  }
				  System.out.println("add classificationID ="+classificationID);
			  }
			  
		  }
		  System.out.println("Exit addLocationToHM()");
	  }

	  public static void populateLocationLevelVO(StepLocationLevelAttrib stepLocationLevelAttrib,int SKUID,String locationAttrID,String locationAttrValue,String classificationType,String channel, List<String> listOfLocations){
		  //HashMap<String,HashMap> hmLocationLevel = new HashMap();
		  System.out.println("Enter populateLocationLevelVO()");
		  System.out.println("locationAttrID="+locationAttrID);
		  System.out.println("locationAttrValue="+locationAttrValue);
		  System.out.println("classificationType="+classificationType);
		  //System.out.println("classificationID="+classificationID);
		  stepLocationLevelAttrib.setSkuNumber(SKUID);
		  
		  // set channel
		  stepLocationLevelAttrib.setChannel(channel);
		  /*
		  if (locationAttrID.endsWith("_NAD")){
			  stepLocationLevelAttrib.setChannel("NAD");
		  }
		  else if (locationAttrID.endsWith("_RET")){
			  stepLocationLevelAttrib.setChannel("RET");
		  }
		  else if (locationAttrID.endsWith("_COR")){
			  stepLocationLevelAttrib.setChannel("COR");
		  }
		  else if (locationAttrID.endsWith("_SCC")){
			  stepLocationLevelAttrib.setChannel("SCC");
		  }
		  */
		  
		  
		  if (locationAttrID.startsWith("L_A0007")){
			  //stepLocationLevelAttrib.setChannel("NAD");
		  }
		  else if (locationAttrID.startsWith("L_A0013")){
			  stepLocationLevelAttrib.setVendorModelNumber(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0023")){
			  stepLocationLevelAttrib.setDsdFlag(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0028")){
			  stepLocationLevelAttrib.setRetailIA_Rebuyer(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0029")){
			  stepLocationLevelAttrib.setRetailProductManager(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0031")){
			  stepLocationLevelAttrib.setInventoryGroup(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0045")){
			  stepLocationLevelAttrib.setPalletTi(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0046")){
			  stepLocationLevelAttrib.setPalletHi(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0065")){
			  stepLocationLevelAttrib.setWhsp(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0066")){
			  stepLocationLevelAttrib.setTargetMax(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0067")){
			  stepLocationLevelAttrib.setSucp(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0068")){
			  stepLocationLevelAttrib.setMasterCasePackLength(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0069")){
			  stepLocationLevelAttrib.setMasterCasePackWidth(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0070")){
			  stepLocationLevelAttrib.setMasterCasePackHeight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0071")){
			  stepLocationLevelAttrib.setMasterCasePackWeight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0072")){
			  stepLocationLevelAttrib.setMerchStatus(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0073")){
			  stepLocationLevelAttrib.setSrc(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0074")){
			  stepLocationLevelAttrib.setDcPurchaseFlag(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0075")){
			  stepLocationLevelAttrib.setVendorNumber(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0077")){
			  stepLocationLevelAttrib.setListPrice(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0237")){
			  stepLocationLevelAttrib.setSellUnitLength(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0238")){
			  stepLocationLevelAttrib.setSellUnitWidth(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0239")){
			  stepLocationLevelAttrib.setSellUnitHeight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0240")){
			  stepLocationLevelAttrib.setSellUnitWeight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0241")){
			  stepLocationLevelAttrib.setPurchaseCaseWeight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0372")){
			  stepLocationLevelAttrib.setDcHoldFlag(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0373")){
			  stepLocationLevelAttrib.setDcFlow(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0374")){
			  stepLocationLevelAttrib.setStoreSourceWarehouse(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0375")){
			  stepLocationLevelAttrib.setStoreLocationsActivate(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0376")){
			  stepLocationLevelAttrib.setStoreLocationsInactivate(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0389")){
			  stepLocationLevelAttrib.setAddRemoveSWO(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0390")){
			  stepLocationLevelAttrib.setPurchaseCaseCube(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0391")){
			  stepLocationLevelAttrib.setDiscontinuedDbmDbs(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0421")){
			  stepLocationLevelAttrib.setSpecificStore(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0432")){
			  stepLocationLevelAttrib.setDropShipFC(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0433")){
			  stepLocationLevelAttrib.setFcPurchaseFlag(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0054")){
			  stepLocationLevelAttrib.setAddOnAllowed(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0055")){
			  stepLocationLevelAttrib.setDistributionPack(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0056")){
			  stepLocationLevelAttrib.setDistributionPolicyCode(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0044")){
			  stepLocationLevelAttrib.setVelocityCode(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0057")){
			  stepLocationLevelAttrib.setSeasonalCode(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0058")){
			  stepLocationLevelAttrib.setTargetDays(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0059")){
			  stepLocationLevelAttrib.setdCMinBalance(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0060")){
			  stepLocationLevelAttrib.setOrderPOCode(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0061")){
			  stepLocationLevelAttrib.setOrderBaseQty(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0062")){
			  stepLocationLevelAttrib.setOrderIncrQty(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0063")){
			  stepLocationLevelAttrib.setCasesPerPallet(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0078")){
			  stepLocationLevelAttrib.setpOCost(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0047")){
			  stepLocationLevelAttrib.setForeignCostPerUnitFreight(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0048")){
			  stepLocationLevelAttrib.setDutyPercent(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0064")){
			  stepLocationLevelAttrib.setOtherCost(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0049")){
			  stepLocationLevelAttrib.setOtherPercent(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0066")){
			  stepLocationLevelAttrib.setStoreTmax(locationAttrValue);
		  }
		  else if (locationAttrID.startsWith("L_A0043")){
			  stepLocationLevelAttrib.setSellUoM(locationAttrValue);
		  }
		//A0054  Add on allowed
		//A0055  Distribution Pack
		//A0056  Distribution Policy Code
		//A0044  Velocity Code
		//A0057  Seasonal Code
		//A0058  Target  days
		//A0059  DC Min balance
		//A0060  Order PO Code
		//A0061  Order Base Qty
		//A0062  Order Incr Qty
		//A0063  Cases/Pallet
		//A0078  PO Cost
		//A0047  Foreign Cost/Unit Freight
		//A0048  Duty %
		//A0064  Other Cost
		//A0049  Other %
		//A0066  Store Tmax
		//A0043  Sell UoM
		  
		  // set location list
		  if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK)
				  && listOfLocations.size() > 0){
			  stepLocationLevelAttrib.setListOfStores(listOfLocations);
		  }
		  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK)
				  && listOfLocations.size() > 0){
			  stepLocationLevelAttrib.setListOfFCs(listOfLocations);
		  }
		  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK)
				  && listOfLocations.size() > 0){
			  stepLocationLevelAttrib.setListOfDCs(listOfLocations);
		  }
		  else if (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK)){
			  stepLocationLevelAttrib.setSKULevelUpdate(true);
			  stepLocationLevelAttrib.setListOfAllLocation(listOfLocations);
		  }

		  //stepLocationLevelAttrib.setListOfStores(listOfLocations);
		  System.out.println("Exit populateLocationLevelVO()");
	  }

	  public static String cancatStringArray(String [] arr){
		  StringBuilder builder = new StringBuilder();
		  for(String s : arr) {
		      builder.append(s);
		  }
		  return builder.toString();
	  }
	  public static String cancatStringList(List<String> listOfIDs){
		  StringBuilder builder = new StringBuilder();
		  Iterator itKey = listOfIDs.iterator();
		  while (itKey.hasNext()) {
			  String sStoreID = (String) itKey.next();
			  builder.append(sStoreID);
		  }
		  return builder.toString();
	  }
	  public static String cancatStringListWithComma(List<String> listOfIDs){
		  StringBuilder builder = new StringBuilder();
		  Iterator itKey = listOfIDs.iterator();
		  while (itKey.hasNext()) {
			  String sStoreID = (String) itKey.next();
			  builder.append(sStoreID).append(IntgSrvAppConstants.COMMA);
		  }
		  String ret = builder.toString();
		  if (ret.length() > 1){
			  //ret = ret.substring(0, ret.length()-1);//remove last comma
		  }
		  else {
			  ret = "";
		  }
		  return ret;
	  }
	  public static HashMap<String,StepLocationLevelAttrib> createStepLocationLevelAttrib(int SKUID,String channel, HashMap<String,HashMap> hmLocationLevel){
		  System.out.println("Enter createStepLocationLevelAttrib():SKUID="+SKUID);
		  System.out.println("Enter createStepLocationLevelAttrib():hmLocationLevel="+hmLocationLevel);
		  HashMap<String,StepLocationLevelAttrib> hmVO = new HashMap();
		  Set<String> keySet1 = hmLocationLevel.keySet();
		  Iterator itKey1 = keySet1.iterator();  
		
		  while (itKey1.hasNext()) {  
			  String locationAttrID = (String) itKey1.next();
			  System.out.println("locationAttrID="+locationAttrID);
			  HashMap<String,HashMap> hmAttrValue = (HashMap<String, HashMap>) hmLocationLevel.get(locationAttrID);
			  if (hmAttrValue == null){
				  System.out.println("hmAttrValue is NULL with locationAttrID="+locationAttrID);			  }
			  else {
				  System.out.println("hmAttrValue is NOT NULL with locationAttrID="+locationAttrID);
				  Set<String> keySet2 = hmAttrValue.keySet();
				  Iterator itKey2 = keySet2.iterator();  
				
				  while (itKey2.hasNext()) {  
					  String locationAttrValue = (String) itKey2.next();
					  System.out.println("locationAttrValue="+locationAttrValue);
					  HashMap<String,List<String>> hmValue = (HashMap<String, List<String>>) hmAttrValue.get(locationAttrValue);
					  if (hmValue == null){
						  System.out.println("hmValue is NULL with locationAttrValue="+locationAttrValue);					  }
					  else {
						  System.out.println("hmValue is NOT NULL with locationAttrValue="+locationAttrValue);
						  System.out.println("hmValue is NOT NULL hmValue="+hmValue);
						  List<String> listOfStores = hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK);
						  List<String> listOfFCs = hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK);
						  List<String> listOfDCs = hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK);
						  List<String> listOfAllLocation = hmValue.get(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK);
						  
						  System.out.println("listOfStores="+listOfStores);
						  System.out.println("listOfFCs="+listOfFCs);
						  System.out.println("listOfDCs="+listOfDCs);
						  System.out.println("listOfAllLocation="+listOfAllLocation);
						  if (listOfStores.size() > 0){
							  //String [] arrOfStores = (String[]) listOfStores.toArray();
							  //Arrays.sort(arrOfStores);
							  String strStoreIDs = "ST_" + cancatStringList(listOfStores);
							  System.out.println("strStoreIDs="+strStoreIDs);
							  if (hmVO.get(strStoreIDs) == null){
								  StepLocationLevelAttrib stepLocationLevelAttrib = new StepLocationLevelAttrib();
								  populateLocationLevelVO(stepLocationLevelAttrib,SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK,channel,listOfStores);
								  hmVO.put(strStoreIDs, stepLocationLevelAttrib);
								  
							  }
							  else {
								  populateLocationLevelVO(hmVO.get(strStoreIDs),SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK,channel,listOfStores);

							  }
						  }
						  if (listOfFCs.size() > 0){
							  //String [] arrOfFCs = (String[]) listOfFCs.toArray();
							  //Arrays.sort(arrOfFCs);
							  String strFCIDs = "FC_" + cancatStringList(listOfFCs);
							  System.out.println("strFCIDs="+strFCIDs);
							  if (hmVO.get(strFCIDs) == null){
								  StepLocationLevelAttrib stepLocationLevelAttrib = new StepLocationLevelAttrib();
								  populateLocationLevelVO(stepLocationLevelAttrib,SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK,channel,listOfFCs);
								  hmVO.put(strFCIDs, stepLocationLevelAttrib);
								  
							  }
							  else {
								  populateLocationLevelVO(hmVO.get(strFCIDs),SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK,channel,listOfFCs);

							  }
						  }
						  if (listOfDCs.size() > 0){
							  //String [] arrOfDCs = (String[]) listOfDCs.toArray();
							  //Arrays.sort(arrOfDCs);
							  String strDCIDs = "DC_" + cancatStringList(listOfDCs);
							  System.out.println("strDCIDs="+strDCIDs);
							  if (hmVO.get(strDCIDs) == null){
								  StepLocationLevelAttrib stepLocationLevelAttrib = new StepLocationLevelAttrib();
								  populateLocationLevelVO(stepLocationLevelAttrib,SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK,channel,listOfDCs);
								  hmVO.put(strDCIDs, stepLocationLevelAttrib);
								  
							  }
							  else {
								  populateLocationLevelVO(hmVO.get(strDCIDs),SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK,channel,listOfDCs);

							  }
						  }
						  if (listOfAllLocation.size() > 0){
							  //String [] arrOfDCs = (String[]) listOfDCs.toArray();
							  //Arrays.sort(arrOfDCs);
							  String strAllLocationID = IntgSrvAppConstants.LOCATION_LEVEL_ALL_LOCATION_ID;
							  System.out.println("strAllLocationID="+strAllLocationID);
							  if (hmVO.get(strAllLocationID) == null){
								  StepLocationLevelAttrib stepLocationLevelAttrib = new StepLocationLevelAttrib();
								  populateLocationLevelVO(stepLocationLevelAttrib,SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK,channel,listOfAllLocation);
								  hmVO.put(strAllLocationID, stepLocationLevelAttrib);
								  
							  }
							  else {
								  populateLocationLevelVO(hmVO.get(strAllLocationID),SKUID,locationAttrID,locationAttrValue,IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK,channel,listOfAllLocation);

							  }
						  }
						  
					  }
				  }
			  }
		  }  


		  System.out.println("Exit createStepLocationLevelAttrib()");
		  return hmVO;
	  }
		@SuppressWarnings("rawtypes")		 
		public static HashMap<Integer, List<List>> buildLocationLevelDataToPushDown (List<StepLocationLevelAttrib> locationLevelAttrib) throws IntgSrvBatchException 
		{   
			HashMap<Integer, List<List>> locationLevelToPushDownCollection = 
										new HashMap<Integer, List<List>>();  
			List<List> locationLevelToPushDownCollectionList = new ArrayList<List>();
			HashMap<Integer, List<StepLocationLevelAttrib>> attributesCollectionSort = new HashMap<Integer, List<StepLocationLevelAttrib>>();
			List<StepLocationLevelAttrib> attributesCollectionSortSubSet = new ArrayList<StepLocationLevelAttrib>();
			
			 
			for (int q = 0; q < locationLevelAttrib.size()  ; q++)
			{			
				StepLocationLevelAttrib locationLevelAttribBean = (StepLocationLevelAttrib) locationLevelAttrib.get(q);

				attributesCollectionSort = (HashMap<Integer, List<StepLocationLevelAttrib>>)
									IntgSrvLocationLevelUtils.sortBulkUpload(locationLevelAttribBean, attributesCollectionSort); 
			
		    }			
			  
			for (int q = 0; q < attributesCollectionSort.size()  ; q++)
			{ 	
				
				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(100)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(100));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(100), locationLevelToPushDownCollectionList); 
				}	
				
				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(101)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(101));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(101), locationLevelToPushDownCollectionList); 
				}
				
				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(102)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(102));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(102), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(104)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(104));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(104), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(106)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(106));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(106), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(108)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(108));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(108), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(111)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(111));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(111), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(114)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(114));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(114), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(117)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(117));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(117), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(118)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(118));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(118), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(119)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(119));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(119), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(120)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(120));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(120), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(121)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(121));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(121), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(122)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(122));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(122), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(123)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(123));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(123), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(124)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(124));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(124), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(125)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(125));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(125), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(126)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(126));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(126), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(127)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(127));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(127), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(128)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(128));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(128), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(129)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(129));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(129), locationLevelToPushDownCollectionList); 
				}	 

				if (null != attributesCollectionSort && null != attributesCollectionSort.get(Integer.valueOf(130)))
				{
					attributesCollectionSortSubSet = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(Integer.valueOf(130));
				 
					locationLevelToPushDownCollectionList = getNextCollection(attributesCollectionSortSubSet);
					locationLevelToPushDownCollection.put(Integer.valueOf(130), locationLevelToPushDownCollectionList); 
				}	 

			} 	 
			
			return locationLevelToPushDownCollection;
	}
		
		
		@SuppressWarnings("rawtypes")
		public static List<List> getNextCollection(List<StepLocationLevelAttrib> attributesCollectionList)
		{ 
			 
			List<List> locationLevelToPushDownCollectionList = new ArrayList<List>();
			List<StepLocationLevelAttrib> locationLevelToPushDownCollectionSubSetList = new ArrayList<StepLocationLevelAttrib>();
			
			
		
			for (int z = 0; z < attributesCollectionList.size(); z++)
			{						 
				if (locationLevelToPushDownCollectionSubSetList.size()<=  IntgSrvAppConstants.LOCATION_LEVEL_THRESHOLD)
				{
					locationLevelToPushDownCollectionSubSetList.add((StepLocationLevelAttrib)attributesCollectionList.get(z));
				}
				else
				{	
					locationLevelToPushDownCollectionList.add(locationLevelToPushDownCollectionSubSetList);
					locationLevelToPushDownCollectionSubSetList = new ArrayList<StepLocationLevelAttrib>();
					locationLevelToPushDownCollectionSubSetList.
												add((StepLocationLevelAttrib)attributesCollectionList.get(z));
				
				}
			}
		 
		 
			locationLevelToPushDownCollectionList.add(locationLevelToPushDownCollectionSubSetList);	  
			
			return locationLevelToPushDownCollectionList;
	}

		public static HashMap<String, ArrayList<String>> combineLocationStatus(List listOfStrings){
			HashMap<String, ArrayList<String>> hmSKUID_CallResult = new HashMap();
			ArrayList<String>	listOfCallResult	= null;//new ArrayList<String>();
			String seperator = "###";
	        //start to process list of String
			Iterator itKey1 = listOfStrings.iterator();  
			
			//iterate through list
			while (itKey1.hasNext()) {  
				String result = (String) itKey1.next();
				LOGGER.debug("Location Level pushdown result="+result);
				if (result.startsWith("<<")){// remove first "<<"
					result = result.substring(2);
				}
				else if (result.contains("<<") && result.endsWith(">>")){
					result = result.substring(result.indexOf("<<")+2, result.length());
					System.out.println("Location Level pushdown has extra info="+result);
				}
				if (result.endsWith(">>")){// remove last ">>"
					result = result.substring(0, result.length()-2);
				}
				result = result.replace(">><<", "^");
			    StringTokenizer st = new StringTokenizer(result,"^");//>><<
				while (st.hasMoreElements()) {
					String SKUResult = (String) st.nextElement();
					SKUResult = SKUResult.trim();
					System.out.println("SKUResult="+SKUResult);
					int col = 0;
					String sSKUID = "";
					String sStatus = "";
					String sError_ID = "";
					String sError_Message = "";
					StringTokenizer st2 = new StringTokenizer(SKUResult,"!!");
					while (st2.hasMoreElements()) {
						String attributeResult = (String) st2.nextElement();
						System.out.println("attributeResult="+attributeResult);
						attributeResult = attributeResult.replace("~~", "~ ~");
						StringTokenizer st3 = new StringTokenizer(attributeResult,"~");
						sSKUID = "";
						sStatus = "";
						sError_ID = "";
						sError_Message = "";
						col = 0;
						while (st3.hasMoreElements()) {
							String colData = (String) st3.nextElement();
							if (colData.equals(" ")) colData = "";
							System.out.println("col="+col+"; colData="+colData);
							if (col ==0){
								sSKUID = colData;
							}
							else if (col ==1){
								sStatus = colData;
							}
							else if (col ==2){
								sError_ID = colData;
							}
							else if (col ==3){
								sError_Message = colData;
							}
							col++;
						}
						if (sStatus.equals("0")){
							sError_Message = "Passed";
						}

						listOfCallResult = hmSKUID_CallResult.get(sSKUID);
						if (listOfCallResult == null){
							listOfCallResult	= new ArrayList<String>();
							listOfCallResult.add(0, sStatus);
							listOfCallResult.add(1, sError_ID);
							listOfCallResult.add(2, sError_Message);
							hmSKUID_CallResult.put(sSKUID,listOfCallResult);
						}
						else {
							if (!sStatus.equals("0")){
								listOfCallResult.set(0, listOfCallResult.get(0) + seperator + sStatus);
								listOfCallResult.set(1, listOfCallResult.get(1) + seperator + sError_ID);
								listOfCallResult.set(2, listOfCallResult.get(2) + seperator + sError_Message);
							}
						}
					}
					
				}

			}
			return hmSKUID_CallResult;
		}

		@SuppressWarnings("rawtypes")

		public static void writeLocationLevelPushdownResultToExcel (List listOfStrings, HashMap<String, String> hmSKUID_ItemID, String outputFilename){
			HashMap<String, ArrayList<String>> hmSKUID_CallResult = combineLocationStatus(listOfStrings);
			
			Workbook workbook = null; 
			CellStyle dataCellStyle = null;
			int currRow = 0;
	        //workbook = new SXSSFWorkbook(100);
			workbook = new HSSFWorkbook();
	        Sheet sheet = workbook.createSheet("LocationLevel Pushdown Result"); 
	        //sheet.createFreezePane(0, 3, 0, 3); 
	        sheet.setDefaultColumnWidth(20); 
	  
	        //addTitleToSheet(sheet); 
	        //currRow++; 
	        //addHeaders(sheet);
	        Row row = sheet.createRow(currRow); 
	        int col = 0; 
	  
	        String[] excelHeaders=new String[5];
	        excelHeaders[0] = "SKU ID";
	        excelHeaders[1] = "Item ID";
	        excelHeaders[2] = "Status Code";
	        excelHeaders[3] = "Error ID";
	        excelHeaders[4] = "Message";
	        
	        CellStyle style = workbook.createCellStyle(); 
	        Font font = workbook.createFont(); 
	  
	        font.setFontHeightInPoints((short) 10); 
	        font.setFontName("Arial"); 
	        font.setBoldweight(Font.BOLDWEIGHT_BOLD); 
	        style.setAlignment(CellStyle.ALIGN_CENTER); 
	        style.setFont(font); 
	        for (String header : excelHeaders) { 
	            Cell cell = row.createCell(col); 
	            cell.setCellValue(header); 
	            cell.setCellStyle(style); 
	            col++; 
	        } 
	        currRow++; 
	        
	        // initial style
	        dataCellStyle = workbook.createCellStyle(); 
	        font = workbook.createFont(); 
	  
	        font.setFontHeightInPoints((short) 10); 
	        font.setFontName("Arial"); 
	        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT); 
	        dataCellStyle.setFont(font);  
 
	        
	        ArrayList<String>	listOfCallResult = null;
	        //start to process each item
			Set<String> setKey1 = hmSKUID_CallResult.keySet();  
			Iterator itKey1 = setKey1.iterator();
			//iterate through key set
			while (itKey1.hasNext()) {  
				String sSKUID = (String) itKey1.next();
				listOfCallResult = hmSKUID_CallResult.get(sSKUID);
				row = sheet.createRow(currRow);
				//col = 0;
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(sSKUID);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(hmSKUID_ItemID.get(sSKUID));

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(listOfCallResult.get(0));

				Cell cell3 = row.createCell(3);
				cell3.setCellValue(listOfCallResult.get(1));

				Cell cell4 = row.createCell(4);
				cell4.setCellValue(listOfCallResult.get(2));
				
				currRow++;
			}
			
			
			FileOutputStream fos;
			
			try {
				fos = new FileOutputStream(outputFilename);
		        workbook.write(fos); 
		        fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Exception e="+e);
			}
			
		}

		public static void writeLocationLevelPushdownResultToExcel2 (List listOfStrings, HashMap<String, String> hmSKUID_ItemID, String outputFilename){
			HashMap<String, HashMap> hmSKUID_CallResult = new HashMap();
			
			Workbook workbook = null; 
			CellStyle dataCellStyle = null;
			int currRow = 0;
	        //workbook = new SXSSFWorkbook(100);
			workbook = new HSSFWorkbook();
	        Sheet sheet = workbook.createSheet("LocationLevel Pushdown Result"); 
	        //sheet.createFreezePane(0, 3, 0, 3); 
	        sheet.setDefaultColumnWidth(20); 
	  
	        //addTitleToSheet(sheet); 
	        //currRow++; 
	        //addHeaders(sheet);
	        Row row = sheet.createRow(currRow); 
	        int col = 0; 
	  
	        String[] excelHeaders=new String[5];
	        excelHeaders[0] = "SKU ID";
	        excelHeaders[1] = "Item ID";
	        excelHeaders[2] = "Status Code";
	        excelHeaders[3] = "Error ID";
	        excelHeaders[4] = "Message";
	        
	        CellStyle style = workbook.createCellStyle(); 
	        Font font = workbook.createFont(); 
	  
	        font.setFontHeightInPoints((short) 10); 
	        font.setFontName("Arial"); 
	        font.setBoldweight(Font.BOLDWEIGHT_BOLD); 
	        style.setAlignment(CellStyle.ALIGN_CENTER); 
	        style.setFont(font); 
	        for (String header : excelHeaders) { 
	            Cell cell = row.createCell(col); 
	            cell.setCellValue(header); 
	            cell.setCellStyle(style); 
	            col++; 
	        } 
	        currRow++; 
	        
	        // initial style
	        dataCellStyle = workbook.createCellStyle(); 
	        font = workbook.createFont(); 
	  
	        font.setFontHeightInPoints((short) 10); 
	        font.setFontName("Arial"); 
	        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT); 
	        dataCellStyle.setFont(font);  
 
	        //start to process list of String
			Iterator itKey1 = listOfStrings.iterator();  
			
			//iterate through list
			while (itKey1.hasNext()) {  
				String result = (String) itKey1.next();
				System.out.println("Location Level pushdown result="+result);
				if (result.startsWith("<<")){// remove first "<<"
					result = result.substring(2);
				}
				if (result.endsWith(">>")){// remove last ">>"
					result = result.substring(0, result.length()-2);
				}
				StringTokenizer st = new StringTokenizer(result,">><<");
				while (st.hasMoreElements()) {
					String SKUResult = (String) st.nextElement();
					System.out.println(SKUResult);
					StringTokenizer st2 = new StringTokenizer(SKUResult,"!!");
					while (st2.hasMoreElements()) {
						String attributeResult = (String) st2.nextElement();
						System.out.println(attributeResult);
						row = sheet.createRow(currRow);
						col = 0;
						attributeResult = attributeResult.replace("~~", "~ ~");
						StringTokenizer st3 = new StringTokenizer(attributeResult,"~");
						while (st3.hasMoreElements()) {
							String colData = (String) st3.nextElement();
							if (colData.equals(" ")) colData = "";
							System.out.println("row:" + currRow + "; col:" + col + "; cellValue="+colData);
							Cell cell = row.createCell(col);
							cell.setCellValue(colData);
							if (col == 0){
								col++;
								Cell cellItemID = row.createCell(col);
								cellItemID.setCellValue(hmSKUID_ItemID.get(colData));
							}
							if (col == 2 && colData.equals("0")){
								col++;
								Cell cellErrorID = row.createCell(col);
								cellErrorID.setCellValue("");
								col++;
								Cell cellMessage = row.createCell(col);
								cellMessage.setCellValue("Passed");
								break;
							}
							col++;
						}
						currRow++;
					}
					
				}

			}
			
			
			FileOutputStream fos;
			
			try {
				fos = new FileOutputStream(outputFilename);
		        workbook.write(fos); 
		        fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Exception e="+e);
			}
			
		}
}
