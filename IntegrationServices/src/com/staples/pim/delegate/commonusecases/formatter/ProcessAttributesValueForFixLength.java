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
 * File name     :   ProcessAttributesValueForFixLength
 * Creation Date :   
 * @author  	 :   
 * @version 1.0
 */ 

package com.staples.pim.delegate.commonusecases.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class ProcessAttributesValueForFixLength { 
	
	private IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String clazzname = this.getClass().getName();
	
    private HashMap<String, String> attrHashMap = null;
	 
	private static String attrIDs =  null;
	private static String attrLengths = null;
	private static String attrTypes =  null; 
	private static String attrPRECISION =  null;
		
	private static SimpleDateFormat formatter = null;
	private static SimpleDateFormat formatter2 = null;
	
	public static String[] iDs = null;
	public static String[] lengths = null;
	public static String[] types = null;
	public static String[] precisions = null;

	public ProcessAttributesValueForFixLength() {   
		 
		attrIDs = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.FIX_HEADER_LINE_ATTRIBUTE_IDS);
		attrLengths = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.FIX_ATTRIBUTE_LENGTHS);
		attrTypes = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.FIX_ATTRIBUTE_TYPES);   
		attrPRECISION = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.FIX_ATTRIBUTE_PRECISION); 
			
		formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
		formatter2 = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		
		iDs = attrIDs.split("~\\|~");
		lengths = attrLengths.split("~\\|~");
		types = attrTypes.split("~\\|~");
		precisions = attrPRECISION.split("~\\|~");
    } 
	
    private void init(){ 
    	 
   		traceLogger.info(clazzname, "init", "Number of Attributes Defined: " + iDs.length);
   		attrHashMap = new HashMap<String, String>();
   		
    	for (String iD: iDs){
    		if (iD.equals(IntgSrvAppConstants.HEADER)){
    			attrHashMap.put(iD, IntgSrvAppConstants.HEADER);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0000)){
    			/* PCM followed by 27 digit random number; eg: PCM123456789012345678901234567  */
    			String pcmNumber = 
    						String.format("%27s", formatter2.format(new Date())).replace(' ', '0');    			 
    			String ReqestID = "PCM" + pcmNumber; 
    			
    			attrHashMap.put(iD, ReqestID);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0001)){
    			/*  "Publish ID" set to default to "STEE103"  */
    			attrHashMap.put(iD, IntgSrvAppConstants.STEE103);
    		}
    		//For PCMP-1434 
    		/*else if (iD.equals(IntgSrvAppConstants.A0002)){
    			 "Item Type" default to "Non Stock Item"  
    			attrHashMap.put(iD, IntgSrvAppConstants.NON_STOCK_ITEM);
    		}*/
    		else if (iD.equals("A0003")){
    			//"Request Type" default to "Create New Item"
    			attrHashMap.put(iD, IntgSrvAppConstants.CREATE_NEW_ITEM);
    		}
    		else if (iD.equals("A0004")){
    			/* "Request Timestamp" date format as "YYYY-MM-DD-HH.MM.SS.NNNNNN"  */
    			attrHashMap.put(iD, formatter.format(new Date()));
    		}
    		else if (	   iD.equals(IntgSrvAppConstants.A0008)
    					|| iD.equals(IntgSrvAppConstants.A0016)
    					|| iD.equals(IntgSrvAppConstants.A0022)
    					|| iD.equals(IntgSrvAppConstants.A0023)
    					|| iD.equals(IntgSrvAppConstants.A0033)
    					|| iD.equals(IntgSrvAppConstants.A0038)
    					|| iD.equals(IntgSrvAppConstants.A0090)
    					|| iD.equals(IntgSrvAppConstants.A0124)
    					|| iD.equals(IntgSrvAppConstants.A0229)
    					|| iD.equals(IntgSrvAppConstants.A0234)
    					|| iD.equals(IntgSrvAppConstants.A0404)
    					|| iD.equals(IntgSrvAppConstants.A0430)
    					|| iD.equals(IntgSrvAppConstants.A0431)
    					){
    			//default to "N"
    			attrHashMap.put(iD, IntgSrvAppConstants.NO);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0031)
    				){
    			/* if "Inventory Group" set default to "2"  */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_2);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0041)
    				|| iD.equals(IntgSrvAppConstants.A0231)
    				){
    			/* if "Set Code" or "Contract X-Item %" set default to "0"   */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_0);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0081)
    				){
    			/* if "UPC Type" set to default to "A" */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_A);
    		}
    		else if (iD.equals(IntgSrvAppConstants.A0187)
    				 || iD.equals(IntgSrvAppConstants.A0180)
    				 || iD.equals(IntgSrvAppConstants.A0184)
    				){
    			/* Change Effective Date (Private label field) - Retail,
    			 * Change Effective Date (Private label field) - NAD, and 
    			 * Received Date set to default value as "0001-01-01"  
    			 * */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_0001_01_01);
    		}
    		else if (iD.equals("A0410")
    				){
    			/* if Channel set to default to "NAD" */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_NAD);
    		}
    		else if (iD.equals(IntgSrvAppConstants.ITMDECCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMSKUCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMWSKUCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMVNDCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMSUPCCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMCUPCCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMIUPCCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMPUPCCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMHAZCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMENVCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMOTHCNT)
    				|| iD.equals(IntgSrvAppConstants.ITMPVTCNT)
    				|| iD.equals(IntgSrvAppConstants.A0020)
    				|| iD.equals(IntgSrvAppConstants.A0072)
    				|| iD.equals(IntgSrvAppConstants.A0073)
    				|| iD.equals(IntgSrvAppConstants.A0074)
    			){
    			/* if iSeries DB attrib set default to "1"  */
    			attrHashMap.put(iD, IntgSrvAppConstants.DEFAULT_1);
    		}
    		else {
    			attrHashMap.put(iD, "");
    		}
    		 
    	} //for iD
    	traceLogger.info(clazzname, "init", "Number of Attributes Initalized: attrHashMap.size() = " + attrHashMap.size());
    	 
    }
    
	public HashMap<String, String> getAttrHashMap() {
		if (attrHashMap == null)
		{
			init();
		}
		return attrHashMap;
	}

	public static HashMap<String, String> getConvertRemoveAttr(){
		 HashMap<String, String> hmFAN = new HashMap<String, String>();
		 hmFAN.put("A0011", "A0011");
		 hmFAN.put("A0130", "A0130");
		 hmFAN.put("A0095", "A0095");
		 hmFAN.put("A0097", "A0097");
		 hmFAN.put("A0098", "A0098");
		 hmFAN.put("A0099", "A0099");
		 hmFAN.put("A0100", "A0100");
		 hmFAN.put("A0109", "A0109");
		 hmFAN.put("A0110", "A0110");
		 hmFAN.put("A0120", "A0120");
		 hmFAN.put("A0134", "A0134");
		 hmFAN.put("A0135", "A0135");
		 hmFAN.put("A0136", "A0136");
		 hmFAN.put("A0144", "A0144");
		 hmFAN.put("A0145", "A0145");
		 hmFAN.put("A0146", "A0146");
		 hmFAN.put("A0147", "A0147");
		 hmFAN.put("A0090", "A0090");
		 hmFAN.put("A0091", "A0091");
		 hmFAN.put("A0092", "A0092");
		 hmFAN.put("A0093", "A0093");
		 hmFAN.put("A0094", "A0094");
		 hmFAN.put("A0101", "A0101");
		 hmFAN.put("A0104", "A0104");
		 hmFAN.put("A0103", "A0103");
		 hmFAN.put("A0102", "A0102");
		 hmFAN.put("A0105", "A0105");
		 hmFAN.put("A0107", "A0107");
		 hmFAN.put("A0108", "A0108");
		 hmFAN.put("A0113", "A0113");
		 hmFAN.put("A0115", "A0115");
		 hmFAN.put("A0116", "A0116");
		 hmFAN.put("A0117", "A0117");
		 hmFAN.put("A0111", "A0111");
		 hmFAN.put("A0114", "A0114");
		 hmFAN.put("A0106", "A0106");
		 hmFAN.put("A0112", "A0112");
		 hmFAN.put("A0118", "A0118");
		 hmFAN.put("A0119", "A0119");
		 hmFAN.put("A0096", "A0096");
		 hmFAN.put("A0121", "A0121");
		 hmFAN.put("A0129", "A0129");
		 hmFAN.put("A0128", "A0128");
		 hmFAN.put("A0130", "A0130");
		 hmFAN.put("A0137", "A0137");
		 hmFAN.put("A0138", "A0138");
		 hmFAN.put("A0139", "A0139");
		 hmFAN.put("A0140", "A0140");
		 hmFAN.put("A0141", "A0141");
		 hmFAN.put("A0142", "A0142");
		 hmFAN.put("A0143", "A0143");
		 hmFAN.put("A0127", "A0127");
		 hmFAN.put("A0124", "A0124");
		 hmFAN.put("A0125", "A0125");
		 hmFAN.put("A0090", "A0090");
		 hmFAN.put("A0091", "A0091");
		 hmFAN.put("A0092", "A0092");
		 hmFAN.put("A0093", "A0093");
		 hmFAN.put("A0094", "A0094");
		 hmFAN.put("A0101", "A0101");
		 hmFAN.put("A0104", "A0104");
		 hmFAN.put("A0103", "A0103");
		 hmFAN.put("A0102", "A0102");
		 hmFAN.put("A0105", "A0105");
		 hmFAN.put("A0107", "A0107");
		 hmFAN.put("A0108", "A0108");
		 hmFAN.put("A0113", "A0113");
		 hmFAN.put("A0115", "A0115");
		 hmFAN.put("A0116", "A0116");
		 hmFAN.put("A0117", "A0117");
		 hmFAN.put("A0111", "A0111");
		 hmFAN.put("A0114", "A0114");
		 hmFAN.put("A0106", "A0106");
		 hmFAN.put("A0112", "A0112");
		 hmFAN.put("A0118", "A0118");
		 hmFAN.put("A0119", "A0119");
		 hmFAN.put("A0096", "A0096");
		 hmFAN.put("A0121", "A0121");
		 hmFAN.put("A0129", "A0129");
		 hmFAN.put("A0128", "A0128");
		 hmFAN.put("A0130", "A0130");
		 hmFAN.put("A0137", "A0137");
		 hmFAN.put("A0138", "A0138");
		 hmFAN.put("A0139", "A0139");
		 hmFAN.put("A0140", "A0140");
		 hmFAN.put("A0141", "A0141");
		 hmFAN.put("A0142", "A0142");
		 hmFAN.put("A0143", "A0143");
		 hmFAN.put("A0127", "A0127");
		 hmFAN.put("A0124", "A0124");
		 hmFAN.put("A0125", "A0125");
		 hmFAN.put("A0130", "A0130");
		 hmFAN.put("A0095", "A0095");
		 hmFAN.put("A0097", "A0097");
		 hmFAN.put("A0098", "A0098");
		 hmFAN.put("A0099", "A0099");
		 hmFAN.put("A0100", "A0100");
		 hmFAN.put("A0109", "A0109");
		 hmFAN.put("A0110", "A0110");
		 hmFAN.put("A0120", "A0120");
		 hmFAN.put("A0134", "A0134");
		 hmFAN.put("A0135", "A0135");
		 hmFAN.put("A0136", "A0136");
		 hmFAN.put("A0144", "A0144");
		 hmFAN.put("A0145", "A0145");
		 hmFAN.put("A0146", "A0146");
		 hmFAN.put("A0147", "A0147");
		// start merged with sprint_A3
		 //pcm-891 add 4 attributes
		 hmFAN.put("A0165", "A0165");
		 hmFAN.put("A0230", "A0230");
		 hmFAN.put("A0243", "A0243");
		 hmFAN.put("A0244", "A0244");
		 hmFAN.put("A0224", "A0224");
		 // end merged with sprint_A3
		 return hmFAN;
	 }


}
