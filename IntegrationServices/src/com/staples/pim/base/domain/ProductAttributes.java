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

package com.staples.pim.base.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
 
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class ProductAttributes {
	  
	private IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String clazzname = this.getClass().getName();
	
	private static HashMap<String, String> attribIDsHashMapXSV = new HashMap<String, String>();   
	private static HashMap<String, String> attribIDsHashMapEXCEL = new HashMap<String, String>();  
	public static String[] iDsXSV = null;
	public static String[] iDsEXCEL = null;
	
	
	public ProductAttributes() {
		// original ProductExcelAttributes  ProductFixLengthAttr
		init();
	}
	
	
	private   void init() {
		
		if (attribIDsHashMapXSV.size() == 0){
			//HashMap<String, String> attribIDsHashMapXSV = new HashMap<String, String>(); 
			String attribIDs =  IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.XSV_HEADER_LINE_ATTRIBUTE_IDS); 
			iDsXSV = attribIDs.split("~\\|~");
			traceLogger.info(clazzname, "init", "Number of Attributes Defined and Initialized for XSV doc = " + iDsXSV.length);
			IntgSrvUtils.printConsole("ProductAttributes init()");
			for (String iD: iDsXSV){
    		 
				attribIDsHashMapXSV.put(iD, "");
			}
		}
		
		if (attribIDsHashMapEXCEL.size() == 0){
			//HashMap<String, String> attribIDsHashMapEXCEL = new HashMap<String, String>(); 
			String attribIDs =  IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.EXCEL_HEADER_LINE_ATTRIBUTE_IDS); 
			iDsEXCEL = attribIDs.split("~\\|~");
			traceLogger.info(clazzname, "init", "Number of Attributes Defined and Initialized for Excel doc= " + iDsEXCEL.length);
			IntgSrvUtils.printConsole("ProductAttributes init()");
			for (String iD: iDsEXCEL){
    		 
				attribIDsHashMapEXCEL.put(iD, "[NotExist]");
			}
		}
	}
	
	public HashMap<String, String> getAttrHashMapForXSV() {
		return attribIDsHashMapXSV;
	}
	
	public HashMap<String, String> getAttrHashMapForEXCEL() {
		return attribIDsHashMapEXCEL;
	}
	
	public String[] getIDsXSV() {
		init();
		return iDsXSV;
	}
	
	public String[] getIDsEXCEL () {
		init();
		return iDsEXCEL;
	}
	
	}
