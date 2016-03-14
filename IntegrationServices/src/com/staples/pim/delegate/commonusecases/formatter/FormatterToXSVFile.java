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

package com.staples.pim.delegate.commonusecases.formatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.domain.ProductAttributes;
import com.staples.pim.base.domain.ProductAttributesInProcess;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;



public class FormatterToXSVFile implements IFormatterToXSVFormat { 
	
	private static IntgSrvLogger ehfItemLoggerXSV = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_XSV);
	private static String clazzroot = "FormatterToXSVFile";
	
	private ErrorHandlingFrameworkICD ehfICD;
	private ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();
	private static IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);     
    private static IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	
	
	private String traceId;
	
	private void init() throws ErrorHandlingFrameworkException {
	   	 ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
	   			 					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
	   			 					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
	   			 					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	    }
	 
	public String buildXSVString(List<STEPProductInformation.Products.Product.AssetCrossReference> assetCrossReferenceList,
								List<STEPProductInformation.Products.Product.ClassificationReference> classificationReferenceList,
								STEPProductInformation.Products.Product.ProductCrossReference productCrossReference,   
								List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef,
								List<STEPProductInformation.Products.Product.Values.Value> valueList,
								String strDelimiter) 
			 	
	{ 
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName();
		String msgDesc;
		String infoLogString;
		 
		
		StepTransmitterBean transmitter = new StepTransmitterBean();
		transmitter.setHeaderLineAttribIdXSV(IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.XSV_HEADER_LINE_ATTRIBUTE_IDS));
		
			
	HashMap<String, String> allValuesHashMap = new ProductAttributes().getAttrHashMapForXSV();
	 
	String strResult = "";//getID() + delimiter + getName() + delimiter;
	
	int numProductAttrs = 0;
	int i = 0;
	
	try {
		init();
		traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
		 
        String requestId = IntgSrvUtils.getUniqueID();
        String publishId = IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV; 
       
        allValuesHashMap.put(IntgSrvAppConstants.TRACE_ID, traceId);
        allValuesHashMap.put(IntgSrvAppConstants.A0000, requestId);
        allValuesHashMap.put(IntgSrvAppConstants.A0001, IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV);
        ehfICD.setAttributeRequestId(requestId);
        ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV);
        ehfICD.setAttributeItemType(null); //initially unknow
        ehfICD.setAttributeRequestType(null); //initially unknow
	
		ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);
		ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY, 
										IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_ITEM);
		/* process Asset  */
		if (productCrossReference != null && (assetCrossReferenceList != null)  && assetCrossReferenceList.size() > 0){
			
			allValuesHashMap = this.formatAssetValue(assetCrossReferenceList,   
														allValuesHashMap);  	
		}
		/* process ClassificationReference  */
		if (classificationReferenceList != null  && classificationReferenceList.size() > 0) {  
			allValuesHashMap = this.formatClassificationReference(classificationReferenceList, allValuesHashMap);   
		}
		/* process ProductCrossReference  */
		if (productCrossReference != null  && valueListProductCrossRef != null && valueListProductCrossRef.size() > 0)
		{
			IntgSrvUtils.printConsole("ProductCrossReference is not null"); 
			allValuesHashMap = this.formatProductCrossReference(valueListProductCrossRef, allValuesHashMap);    
		}
		/* process Product Value  */
		if (valueList != null && valueList.size() > 0)
		{
			allValuesHashMap = this.formatProductValue(valueList, allValuesHashMap);    
		}
		
		msgDesc = "Integration services FlatFileWriterXSV processed item: number of attrs {incoming, processed, defined} = { " + 
		numProductAttrs + ", " + allValuesHashMap.size() + ", " + allValuesHashMap.size() + "}";
		infoLogString = ehfHandler.getInfoLog(new Date(), traceId, 
				IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, 
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc, 
				IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
				usecase, codeModule, ehfICD);
		ehfLogger.info(infoLogString);
		ehfItemLoggerXSV.info(infoLogString);
		traceLogger.info(clazzroot, "2Product.getXSVofValues", msgDesc);
		
		/* format the String for the target delivery  */
		strResult = this.formatResultString(allValuesHashMap, strDelimiter, transmitter);		
		
	} catch (Throwable e) {
    	String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, 
    					IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, e, 
    					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
    					usecase, codeModule, ehfICD);
 		ehfLogger.error(errorLogString);
 		ehfItemLoggerXSV.error(errorLogString); 
 		traceLogger.error(clazzroot, "2Product.getXSVofValues", ehfICD.toStringEHFExceptionStackTrace(e));
		e.printStackTrace();
	}
	traceLogger.debug(clazzroot, "2Product.getXSVofValue", "EXIT");
	
	return strResult;

	}
	 
	
	public HashMap <String, String> formatAssetValue(List<STEPProductInformation.Products.Product.AssetCrossReference> assetList, HashMap<String, String> allValuesHashMap){
		
		HashMap<String, String> allValuesHashMapLocal = allValuesHashMap;		 
		Iterator<STEPProductInformation.Products.Product.AssetCrossReference> it = assetList.iterator();
		
		while(it.hasNext()){
			STEPProductInformation.Products.Product.AssetCrossReference assetCrossRef = (STEPProductInformation.Products.Product.AssetCrossReference)it.next();
			String assetID = assetCrossRef.getAssetID();
			IntgSrvUtils.printConsole("assetID="+assetID);
			String assetType = assetCrossRef.getType();
			
			IntgSrvUtils.printConsole("assetType="+assetType); 
			IntgSrvUtils.printConsole("assetValue="+assetID);  
			
			if (assetType.equals("PrimaryImage")){
				allValuesHashMapLocal.put("Image1", assetID);
				IntgSrvUtils.printConsole("set Image1="+assetID);
			}
			else if (assetType.equals("Secondary Image 1")){
				allValuesHashMapLocal.put("Image2", assetID);
				IntgSrvUtils.printConsole("set Image2="+assetID);
			}
			else if (assetType.equals("Secondary Image 2")){
				allValuesHashMapLocal.put("Image3", assetID);
			}
			else if (assetType.equals("Secondary Image 3")){
				allValuesHashMapLocal.put("Image4", assetID);
			}
			else if (assetType.equals("Secondary Image 4")){
				allValuesHashMapLocal.put("Image5", assetID);
			}
			else if (assetType.equals("Secondary Image 5")){
				allValuesHashMapLocal.put("Image6", assetID);
			}
			else if (assetType.equals("Secondary Image 6")){
				allValuesHashMapLocal.put("Image7", assetID);
			}
			else if (assetType.equals("Secondary Image 7")){
				allValuesHashMapLocal.put("Image8", assetID);
			}
			else if (assetType.equals("Secondary Image 8")){
				allValuesHashMapLocal.put("Image9", assetID);
			}
		}
		return allValuesHashMapLocal;
	}
	
	public HashMap <String, String> formatClassificationReference(List<STEPProductInformation.Products.Product.ClassificationReference> classificationList, 
																	HashMap<String, String> allValuesHashMap){
		
		HashMap<String, String> allValuesHashMapLocal = allValuesHashMap; 
		String classificationID = null;
		 
		Iterator<STEPProductInformation.Products.Product.ClassificationReference> it = classificationList.iterator();
		while(it.hasNext()){
			STEPProductInformation.Products.Product.ClassificationReference classificationCrossRef = (STEPProductInformation.Products.Product.ClassificationReference)it.next();
			classificationID = classificationCrossRef.getClassificationID();
			IntgSrvUtils.printConsole("classificationID="+classificationID);

			if (classificationID !=null && !classificationID.equals("")){
				allValuesHashMapLocal.put(IntgSrvAppConstants.SUPER_CATEGORY, classificationID);
				allValuesHashMapLocal.put(IntgSrvAppConstants.CATEGORY, classificationID);
				allValuesHashMapLocal.put(IntgSrvAppConstants.DEPARTMENT, classificationID);
				allValuesHashMapLocal.put(IntgSrvAppConstants.CLASS, classificationID);
			}
		}
		
		return allValuesHashMapLocal;
		
	}
	
	public HashMap <String, String>  formatProductCrossReference(List<STEPProductInformation.Products.Product.Values.Value> valueList, 
																HashMap<String, String> allValuesHashMap){
		
		IntgSrvUtils.printConsole("ProductCrossReference is not null");
		int numProductAttrs = 0;
		int i = 0;
		
		HashMap<String, String> allValuesHashMapLocal = allValuesHashMap;  
		 
		Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
    	
		numProductAttrs = numProductAttrs + valueList.size();
	
		while(it.hasNext())
		{
			i++;
			STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
			 
			String iD = valueAttr.getAttributeID();
			
			if (iD.equals(IntgSrvAppConstants.A0000)) ehfICD.setAttributeRequestId(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0001)) ehfICD.setAttributePublishId(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0002)) ehfICD.setAttributeItemType(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0003)) ehfICD.setAttributeRequestType(valueAttr.getValue());
			 
			ehfItemLoggerXSV.debug(clazzroot, "2Product.getXSVofValues", "process incoming attribute: " + (i++) + " - [id=" + iD + ", " + valueAttr.getValue() + "]");
			
			if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
				allValuesHashMapLocal.put(iD, "");
			}
			else { 
				String temp = valueAttr.getValue();
				if (temp != null && !temp.equals("") && temp.contains(" : ")){
				   temp = temp.split(" : ")[1].trim();
				}
				allValuesHashMapLocal.put(iD, temp);
			}
		} 
		
		return allValuesHashMapLocal;
	}
	
	public HashMap <String, String>  formatProductValue(List<STEPProductInformation.Products.Product.Values.Value> valueList, HashMap<String, String> allValuesHashMap){
		
		HashMap<String, String> allValuesHashMapLocal = allValuesHashMap; 
		
		int numProductAttrs = 0;
    	int i = 0; 
    	
    	Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
		numProductAttrs = numProductAttrs + valueList.size();
		while(it.hasNext())
		{
			i++;
			STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
			 
			String iD = valueAttr.getAttributeID();
			
			if (iD.equals("A0000")) ehfICD.setAttributeRequestId(valueAttr.getValue());
			if (iD.equals("A0001")) ehfICD.setAttributePublishId(valueAttr.getValue());
			if (iD.equals("A0002")) ehfICD.setAttributeItemType(valueAttr.getValue());
			if (iD.equals("A0003")) ehfICD.setAttributeRequestType(valueAttr.getValue());
			 
			ehfItemLoggerXSV.debug(clazzroot, "2Product.getXSVofValues", "process incoming attribute: " + (i++) + " - [id=" + iD + ", " + valueAttr.getValue() + "]");
			
			if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
				allValuesHashMapLocal.put(iD, "");
			}
			else {				 
				String temp = valueAttr.getValue();
				if (temp != null && !temp.equals("") && temp.contains(" : ")){
				   temp = temp.split(" : ")[1].trim();
				}
				allValuesHashMapLocal.put(iD, temp);
			}
		}
		
		return allValuesHashMapLocal;
	}
	
	public String formatResultString(HashMap<String, String> allValuesHashMap, String strDelimiter, StepTransmitterBean transmitter)
	{
		String strResult = "";
		String actionCode = "";
		String activeStaples = (String)allValuesHashMap.get(IntgSrvAppConstants.A0501);
		String sKULifecycle = (String)allValuesHashMap.get(IntgSrvAppConstants.SKULIFECYCLE);
		if (sKULifecycle == null){
			sKULifecycle = "";
		}
		if (activeStaples != null && activeStaples.equalsIgnoreCase("Y")){
			if (sKULifecycle.equalsIgnoreCase(IntgSrvAppConstants.MAINTENANCE)){
				actionCode = "U";
			}
			else if (sKULifecycle.equalsIgnoreCase(IntgSrvAppConstants.ACTIVATED)){
				actionCode = "I";
			}
		}
		else if (activeStaples != null && activeStaples.equalsIgnoreCase("N")){
			// merge with sprint_A3 
			if (sKULifecycle.equalsIgnoreCase(IntgSrvAppConstants.MAINTENANCE) || 
					sKULifecycle.equalsIgnoreCase(IntgSrvAppConstants.ACTIVATED)){
						actionCode = "D";
			}
		}
		if (!actionCode.equals("")){
    		// cancat all attribute value 
			String attribIDs = transmitter.getHeaderLineAttribIdXSV();
			String[] iDs = attribIDs.split("~\\|~");
			
    		for (String iD: iDs){ 
    			
           	 	if (iD.equals(IntgSrvAppConstants.A0000)) ehfICD.setAttributeRequestId((String)allValuesHashMap.get(iD));
           	 	if (iD.equals(IntgSrvAppConstants.A0001)) ehfICD.setAttributePublishId((String)allValuesHashMap.get(iD));
           	 	if (iD.equals(IntgSrvAppConstants.A0002)) ehfICD.setAttributeItemType((String)allValuesHashMap.get(iD));
           	 	if (iD.equals(IntgSrvAppConstants.A0003)) ehfICD.setAttributeRequestType((String)allValuesHashMap.get(iD));
           	 	if (iD.equals(IntgSrvAppConstants.A0502)){// source
           	 		strResult = strResult + "STEP" + strDelimiter;
           	 	}
           	 	else if (iD.equals(IntgSrvAppConstants.ACTION_CODE)){
           	 		strResult = strResult + actionCode + strDelimiter;
           	 	}
           	 	// start merged with sprint_A3
           	 	else if (iD.equals(IntgSrvAppConstants.A0210)){ // new requirement, need to send as "3/Box"
        	 		if (!(allValuesHashMap.get(IntgSrvAppConstants.A0211).equals(""))){//quantity is not empty, then concat
        	 			strResult = strResult + allValuesHashMap.get(IntgSrvAppConstants.A0211) +
        	 			IntgSrvAppConstants.SLASH + allValuesHashMap.get(iD) + strDelimiter;
        	 		}
        	 		else {
        	 			strResult = strResult + allValuesHashMap.get(iD) + strDelimiter;
        	 		}
        	 	}
           	 	
           	 	// end merged with sprint_A3
           	 	else {
           	 		strResult = strResult + allValuesHashMap.get(iD) + strDelimiter;
           	 	}
    		}
    		strResult = strResult + "###";// start category attributes
            Iterator<Map.Entry<String, String>> iterator = allValuesHashMap.entrySet().iterator() ;
            String sAttributeID = "";
            String sAttrValue = "";
            while(iterator.hasNext()){
            	Map.Entry<String, String> valueEntry = iterator.next();
            	sAttributeID = valueEntry.getKey();
            	sAttrValue = valueEntry.getValue();
            	IntgSrvUtils.printConsole(sAttributeID);
             
            	if (ProductAttributesInProcess.attrIDNameHashMap.containsKey(sAttributeID)){
            		 
            		if (sAttrValue != null && !sAttrValue.equals("")){
            			IntgSrvUtils.printConsole(sAttributeID +" :: "+ sAttrValue);
            			strResult = strResult + sAttributeID + "###" + sAttrValue + strDelimiter;
            		}
            	  }
            }
            if (strResult.endsWith(strDelimiter)){
            	strResult = strResult.substring(0, strResult.length() - strDelimiter.length());
            }
            
            strResult = strResult.replace("\\n", IntgSrvAppConstants.DELIMITER_CUSTOM_NEW_LINE);   // merge with sprint_A3
		}
		
		IntgSrvUtils.printConsole("FormaterToXSVFile final strResult="+strResult);
		traceLogger.debug(clazzroot, "2Product.getXSVofValue", strResult); 
		 
    	//strResult = strResult.replace("\\n", IntgSrvAppConstants.DELIMITER_CUSTOM_NEW_LINE);   // merge with sprint_A3
		
		return strResult;		
	}

}
