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
 * File name     :   ConverterToFixLength
 * Creation Date :   
 * @author  	 :   
 * @version 1.0
 */ 


package com.staples.pim.delegate.commonusecases.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.createupdateitem.processor.TransformationRulesForItemUpdateCreate;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.futuredatedpo.processor.TransformationRulesForFDPO;
import com.staples.pim.delegate.reclass.processor.TransformationRulesForReclass;


public class FormaterToFixLength implements IFormatterToFixLength {
	
	 
	private static IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private static String clazzroot = "STEPProductInformation";
	private static IntgSrvLogger LOGGER = IntgSrvLogger.getInstance("ImportXMLSetMapper", FormaterToFixLength.class.getName());
	private static IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	
	private ErrorHandlingFrameworkICD ehfICD;
    private ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();
   
    private static IntgSrvLogger ehfItemLoggerFixLength = 
		IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_FIXLENGTH); 
	
	private static final short UPDATEITEM =  1;
	private static final short FUTUREDATEDPO =  2;
	private static final short GETNEWINSTANCEFORFUTUREDATEDPO =  22;
	private static final short RECLASS =  3;
	private static final short POCOSTCODEBOTH =  4;
	private static final short NOSKULIFECYCLE =  5;
	
	String msgDesc;
	String infoLogString;
	int transaction_Type = 0;
	int poCostCodeRet = 0; 
	private String traceId; 
	boolean doWeHaveSKUlifecycle = false;
	 
	
	  private void init() throws ErrorHandlingFrameworkException {
			  ehfICD = 
				 ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
						 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
						 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
						 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	    }
	
	
	public FormaterToFixLength() { 
		 
	}
	
	public StepTransmitterBean buildFixLengthString(STEPProductInformation.Products.Product.ProductCrossReference productCrossReference,
									List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef,
									List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueProdCrossRefAttribList,
									List<STEPProductInformation.Products.Product.Values.Value> valueList,
									List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueAttribList,
									String productID) 
	 
	{
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName();
		StepTransmitterBean serviceBean = new StepTransmitterBean(); 
		
    	traceLogger.debug(clazzroot, "2Product.buildFixLengthString", "ENTER (Mapped): fragmentroot at Product");   		 
		
    	ProcessAttributesValueForFixLength attribValuesPocess = new ProcessAttributesValueForFixLength();
    	HashMap<String, String> allValuesHashMap = attribValuesPocess.getAttrHashMap();    	
    	 
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
    	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");    	
    	
    	List<String> listOfMessagesToPublish = null;
    	List<List> listOfMessagesInTheListToPublish = new ArrayList<List>();
    	
    	try {
    		init();
    		traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
            String publishId = IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH; 
            
            //initialize transform header metadata 
            allValuesHashMap.put("traceid", traceId);
            //allValuesHashMap.put("A0000", requestId);
            allValuesHashMap.put(IntgSrvAppConstants.A0001, 
            					IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH); 
             
    		ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH);
            ehfICD.setAttributeItemType(null); //initially unknown
            ehfICD.setAttributeRequestType(null); //initially unknown
    		ehfICD.setAttributeTransactionType(
    						IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);
    		ehfICD.setAttributeFreeForm(
    						IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY, 
    						IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_ITEM);
    		
   			int numXRefAttrs = 0; 
   			int numProductAttrs = 0; 
   			 
    		if (productCrossReference != null){
    			IntgSrvUtils.printConsole("ProductCrossReference is not null");
    			allValuesHashMap = this.convertProductCrossReference(valueListProductCrossRef, 
    																multiValueProdCrossRefAttribList,
    																allValuesHashMap); 
    		}
    		
    		allValuesHashMap = this.convertProductValue(valueList, multiValueAttribList, allValuesHashMap);    		 
    		
    		allValuesHashMap.put(IntgSrvAppConstants.A0405, productID);
    		
    		allValuesHashMap = TransformationRulesForItemUpdateCreate.overrideValuesForItemUpdateCreate(allValuesHashMap);		
    		
    		if (transaction_Type == FUTUREDATEDPO)
			{
				allValuesHashMap = TransformationRulesForFDPO.overrideValuesForFutureDatedPO(allValuesHashMap);
				if (IntgSrvAppConstants.BOTH.equalsIgnoreCase(((String)allValuesHashMap.get(IntgSrvAppConstants.A0009))))
				{
					poCostCodeRet = POCOSTCODEBOTH;
				}
				else
				{
					poCostCodeRet = 0;
				} 
			}
			 
			if (transaction_Type == RECLASS)
			{
				allValuesHashMap = TransformationRulesForReclass.overrideValuesForReclass(allValuesHashMap);
			}
		 
		for (int counter = 0; counter < 2; counter++)
		{    		
			if (transaction_Type == FUTUREDATEDPO && poCostCodeRet == POCOSTCODEBOTH  && counter > 0)
			{
				if (allValuesHashMap.get(IntgSrvAppConstants.A0204)!= null  			&& 
						!allValuesHashMap.get(IntgSrvAppConstants.A0204).equals("") 	&& 
						(((String)allValuesHashMap.get(IntgSrvAppConstants.A0009)).equalsIgnoreCase(IntgSrvAppConstants.BOTH)  ||
	    						((String)allValuesHashMap.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.SCC)))  
								
				{
					allValuesHashMap.put(IntgSrvAppConstants.A0202, IntgSrvAppConstants.LI);   
					allValuesHashMap.put(IntgSrvAppConstants.A0200, (String)allValuesHashMap.get(IntgSrvAppConstants.A0204));  
					allValuesHashMap.put(IntgSrvAppConstants.A0078,(String)allValuesHashMap.get(IntgSrvAppConstants.A0200));
					//allValuesHashMap.put(IntgSrvAppConstants.A0078_RET,(String)allValuesHashMap.get(IntgSrvAppConstants.A0200));
					allValuesHashMap.put(IntgSrvAppConstants.A0078_NAD,(String)allValuesHashMap.get(IntgSrvAppConstants.A0200));
					allValuesHashMap.put(IntgSrvAppConstants.A0077,(String)allValuesHashMap.get(IntgSrvAppConstants.A0204));
					//allValuesHashMap.put(IntgSrvAppConstants.A0077_RET,(String)allValuesHashMap.get(IntgSrvAppConstants.A0204));
					allValuesHashMap.put(IntgSrvAppConstants.A0077_NAD,(String)allValuesHashMap.get(IntgSrvAppConstants.A0204));
					String requestId = IntgSrvUtils.getUniqueID();  
		            allValuesHashMap.put(IntgSrvAppConstants.A0000, requestId);
				} 
			}
			
			if (doWeHaveSKUlifecycle)
			{
				listOfMessagesToPublish = this.buildMSGString(allValuesHashMap, numXRefAttrs, numProductAttrs, usecase, codeModule);  
				listOfMessagesInTheListToPublish.add(listOfMessagesToPublish);
			}
				
			/* add SKULifecycle value to the next element of array for future use  */  
			serviceBean.addItem(IntgSrvAppConstants.LIST_OF_MESSAGES_TO_HASHMAP, listOfMessagesInTheListToPublish); 
	 		serviceBean.addItem(IntgSrvAppConstants.SKU_LIFE_CYCLE_TO_HASHMAP, 
								allValuesHashMap.get(IntgSrvAppConstants.SKULIFECYCLE));
			serviceBean.addItem(IntgSrvAppConstants.PRODUCT_ID_TO_HASHMAP, productID); 
			
				/* MOVE TO THE pUBLISHER MQ CLASS start */  
			
				/* MOVE TO THE pUBLISHER MQ CLASS end */
			if (transaction_Type != FUTUREDATEDPO || poCostCodeRet != POCOSTCODEBOTH)
			{ 
				break;
			}
    		
		} // end for loop
		
    	} catch (Throwable e) {
	    	String errorLogString = ehfHandler.getErrorLog(
	    					new Date(), traceId, 
	    					IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, 	e, 
	    					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
	    					usecase, codeModule, ehfICD);
	 		ehfLogger.error(errorLogString);
	 		ehfItemLoggerFixLength.error(errorLogString); 
	 		traceLogger.error(clazzroot, "2Product.buildFixLengthString", ehfICD.toStringEHFExceptionStackTrace(e));
    		e.printStackTrace();
    	}
    	
    	traceLogger.debug(clazzroot, "2product.getfixlengthofvalues", "EXIT");
    	serviceBean.setTransactionType(transaction_Type);
    	return  serviceBean; 
		 
	} 
	
	@SuppressWarnings("unused")
	public HashMap<String, String> convertProductCrossReference(List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef,
										List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueProdCrossRefAttribList,
										HashMap<String, String> allValuesHashMap) 
	{  
		HashMap<String, String> allValuesHashMapLoc = allValuesHashMap;
		
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName();  
		
		int numXRefAttrs = valueListProductCrossRef.size();
		
		String msgDesc = "IntegrationServices getProductCrossReference incoming number of attributes: " + numXRefAttrs;
		String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, 
				IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, 
				msgDesc, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
				usecase, codeModule, ehfICD);
		ehfItemLoggerFixLength.debug("Incoming Number of Attributes - ProductCrossReferece: " + numXRefAttrs);
		traceLogger.debug(clazzroot, "2Product.getFixLengthofValues", msgDesc);
		
		if (null != valueListProductCrossRef)
		{
		Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueListProductCrossRef.iterator();
		int j = 0;
		while(it.hasNext())
		{
			STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
			 
			String iD = valueAttr.getAttributeID(); 
			 
			if (iD.equals(IntgSrvAppConstants.A0000)) ehfICD.setAttributeRequestId(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0001)) ehfICD.setAttributePublishId(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0002)) ehfICD.setAttributeItemType(valueAttr.getValue());
			if (iD.equals(IntgSrvAppConstants.A0003)) ehfICD.setAttributeRequestType(valueAttr.getValue());
			traceLogger.debug(clazzroot, "2Product.getFixLengthofValues", "getProductCrossReference() incoming attributes: " + j++ + " - [ " + iD + ", " + valueAttr.getValue() + " ]");
			ehfItemLoggerFixLength.debug(clazzroot, "2Product.getFixLengthofValues", "getProductCrossReference() incoming attributes: " + j++ + " - [ " + iD + ", " + valueAttr.getValue() + " ]");
			
			if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
				allValuesHashMapLoc.put(iD, "");
				 
			}
			else {
				allValuesHashMapLoc.put(iD, valueAttr.getValue()); 
				 
				if (iD.equalsIgnoreCase(IntgSrvAppConstants.SKULIFECYCLE))
				{
					allValuesHashMapLoc = this.setHashMapForSKULifeCycle(valueAttr, allValuesHashMapLoc);  						
				}     
			}
		} 
		}
	 
		if ( null != multiValueProdCrossRefAttribList)
		{
			allValuesHashMapLoc =  convertMultiValues(multiValueProdCrossRefAttribList, allValuesHashMapLoc);  
		}
		
		 return allValuesHashMapLoc;
	} 
	 
	
	public HashMap<String, String> convertMultiValues(List<STEPProductInformation.Products.Product.Values.MultiValue> attribList, 
										HashMap<String, String> allValuesHashMap) 
	{   
		HashMap<String, String> allValuesHashMapLoc = allValuesHashMap;
		
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName();
	 
		int numXRefAttrsMulti = attribList.size();
	
		msgDesc = "SpringBatch getProductCrossReference incoming number of attributes: " + numXRefAttrsMulti;
		infoLogString = ehfHandler.getInfoLog(new Date(), traceId, 
			IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, 
			ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc, 
			IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
			usecase, codeModule, ehfICD);
		ehfItemLoggerFixLength.debug("Incoming Number of Attributes - ProductCrossReferece: " + numXRefAttrsMulti);
		traceLogger.debug(clazzroot, "2Product.getFixLengthofValues", msgDesc);
	
		Iterator<STEPProductInformation.Products.Product.Values.MultiValue> itMultList  = attribList.iterator();
		// Below statement is commented and move to inside while because all multivalue are appended in
		// single variable, it leads to send incorrect values to downstream
		//String stringDelim = null;
		while(itMultList.hasNext())
		{
			String stringDelim = null;
			STEPProductInformation.Products.Product.Values.MultiValue values  = (STEPProductInformation.Products.Product.Values.MultiValue)itMultList.next();
		 
			String iD = values.getAttributeID(); 
		 
			if (values.getAttributeID() != null || ! values.getAttributeID().equalsIgnoreCase(""))
			{
			    
			   Iterator<STEPProductInformation.Products.Product.Values.MultiValue.Value> itMult  = values.getValue().iterator(); 
   				while(itMult.hasNext())
   				{
   					STEPProductInformation.Products.Product.Values.MultiValue.Value value   = (STEPProductInformation.Products.Product.Values.MultiValue.Value)(itMult.next());
   				  
   					if (stringDelim == null)
   					{  
   						stringDelim = StringUtils.split(value.getValue(),":")[0]; 
   					}
   					else
   					{
   						stringDelim = stringDelim +  "," + StringUtils.split(value.getValue(),":")[0]; 
   					}
   				} 
			
			allValuesHashMapLoc.put(iD, stringDelim); 
		}
	}
	
		return allValuesHashMapLoc;
	}
	
	
	public HashMap<String, String> convertProductValue (List<STEPProductInformation.Products.Product.Values.Value> valueList, 
															List<STEPProductInformation.Products.Product.Values.MultiValue> attribList,			
															HashMap<String, String> allValuesHashMap)	 
	{
		HashMap<String, String> allValuesHashMapLoc = allValuesHashMap;
		
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName(); 
		
		int numProductAttrs = valueList.size();
		msgDesc = "SpringBatch Product incoming number of attributes: " + numProductAttrs;
		infoLogString = ehfHandler.getInfoLog(
				new Date(), traceId, 
				IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, 
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc, 
				IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
				usecase, codeModule, ehfICD);
		ehfItemLoggerFixLength.debug("Incoming Number of Attributes - Product: " + numProductAttrs);
		traceLogger.debug(clazzroot, "2Product.getFixLengthofValues", msgDesc);
			
		if (null != valueList)
		{
		Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
		
		int i = 0;		
		while(it.hasNext())
		{
			STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
			 
			String iD = valueAttr.getAttributeID();
			
				traceLogger.debug(clazzroot, "2Product.getFixLengthofValues", "getValues() incoming attribute: " + i++ + " - [ " + iD + ", " + valueAttr.getValue() + " ]");
				ehfItemLoggerFixLength.debug(clazzroot, "2Product.getFixLengthofValues", "getValues() incoming attribute: " + i++ + " - [ " + iD + ", " + valueAttr.getValue() + " ]");
				
       	 	if (iD.equals(IntgSrvAppConstants.A0000)) ehfICD.setAttributeRequestId(valueAttr.getValue());
       	 	if (iD.equals(IntgSrvAppConstants.A0001)) ehfICD.setAttributePublishId(valueAttr.getValue());
       	 	if (iD.equals(IntgSrvAppConstants.A0002)) ehfICD.setAttributeItemType(valueAttr.getValue());
       	 	if (iD.equals(IntgSrvAppConstants.A0003)) ehfICD.setAttributeRequestType(valueAttr.getValue());
       	 	
			if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
				allValuesHashMapLoc.put(iD, ""); 
			}
			else {
				allValuesHashMapLoc.put(iD, valueAttr.getValue()); 
				 
				if (iD.equals(IntgSrvAppConstants.A0012)){
					allValuesHashMapLoc.put(IntgSrvAppConstants.A0000, 
									IntgSrvUtils.getPCMUniqueID(valueAttr.getValue()));
				}
				 
				if (iD.equals(IntgSrvAppConstants.SKULIFECYCLE))
				{
					allValuesHashMapLoc = this.setHashMapForSKULifeCycle(valueAttr, allValuesHashMapLoc);	
					doWeHaveSKUlifecycle = true;
				} 
				if(iD.equals(IntgSrvAppConstants.PUBLISHTOGALAXY))
				{
					String publishToGalaxy=valueAttr.getValue();
					if(!IntgSrvUtils.isNullOrEmpty(publishToGalaxy)){
						doWeHaveSKUlifecycle = true;
					}
				}
			}
		} 
	}
		
		if (null != attribList)
		{
			allValuesHashMapLoc = this.convertMultiValues(attribList,  allValuesHashMapLoc);  
		}
		
		return allValuesHashMapLoc;	 
	}
	
	public HashMap<String, String>  setHashMapForSKULifeCycle (STEPProductInformation.Products.Product.Values.Value valueAttr, HashMap<String, String> allValuesHashMap) 
	{
		HashMap<String, String> allValuesHashMapLoc =   allValuesHashMap;
		
		if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.MAINTENANCE))
		{
			allValuesHashMapLoc.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.UPDATE_ITEM);  
			transaction_Type = UPDATEITEM;
		}
		if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.RECLASS))
		{
			allValuesHashMapLoc.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.RECLASS);  
			allValuesHashMapLoc.put(IntgSrvAppConstants.A0001, IntgSrvAppConstants.STEE110);  
			transaction_Type = RECLASS;
		} 
		if (valueAttr.getValue().equalsIgnoreCase("Future Dated PO"))
		{
			allValuesHashMapLoc.put("A0003", "Future Dated PO");  
			allValuesHashMapLoc.put("A0001", "STEE109");  
			transaction_Type = FUTUREDATEDPO;
		}   
		
		return allValuesHashMapLoc;
	}	
	   	   
	 private List<String> buildMSGString(HashMap<String, String> allValuesHashMap,int numXRefAttrs, int numProductAttrs, String usecase, String codeModule)
	 {
		int idx = 0;
	   	int length = 20;
		int precision = 0;
		int numLength = 0;
		String type = "text";
		String [] lengths = ProcessAttributesValueForFixLength.lengths;
		String [] types = ProcessAttributesValueForFixLength.types;
		String [] precisions = ProcessAttributesValueForFixLength.precisions;
		String strDelimiter = "|";
		String strResult = null;
		String strResult2 = null;
		List <String> listOfResults = new ArrayList<String>();
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
	    HashMap<String, String> hmConvertRemoveFANs = ProcessAttributesValueForFixLength.getConvertRemoveAttr();
	    
		int numAllAttrs = allValuesHashMap.size();
		String msgDesc = "SpringBatch FlatFileWriterFixLength processed item: number of attrs {xref, prd, all, defined} = {"+ numXRefAttrs + ", " + numProductAttrs + ", " + numAllAttrs + ", " + lengths.length +"}";
		String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, 
						IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, 
						msgDesc, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
						usecase, codeModule, ehfICD);
				ehfLogger.info(infoLogString);
				ehfItemLoggerFixLength.info(infoLogString);
	   			traceLogger.info(clazzroot, "2Product.getFixLengthofValues", msgDesc);
	    	   
	   	
	    for (String iD: ProcessAttributesValueForFixLength.iDs)
	    {
	 	   length =Integer.parseInt(lengths[idx]);
	 	   type = types[idx]; 
	 	   
	/*  // need to tranform, e.g. "1 : Furniture" to "1"
	 	   A0011
	 	   A0024
	 	   A0025
	 	   A0026
	 	   A0027
	 	   A0029
	 	   A0043
	 	   A0085
	 	   A0086
	 	   A0087
	 	   A0088
	 	   A0089
	 	   A0097
	 	   A0114
	 	   A0144
	 	   A0146
	 	   A0169
	 	   A0172
	 	   A0181
	 	   A0182
	 	   A0185
	 	   A0186
	 	   A0189
	 	   A0190
	 	   A0191
	 	   A0197
	 	   A0212
	 	   A0213
	 	   A0214
	 	   A0230
	 	   A0243
	 	   A0244
	*/
	 	     
	 	 String temp = (String)allValuesHashMap.get(iD);
	 	 LOGGER.debug(idx+"; iD="+iD+"; length="+length+";type="+type+"; XML_Value="+temp+ "###");
	 	 if (null != temp && temp.length() > length){ 
	 		 if (temp != null && !temp.equals("") && temp.contains(":")){
	 			   temp = temp.split(":")[0].trim();
	 			   //PCMP-1855, 1856 
	 			   if("A0210".equalsIgnoreCase(iD) || "A0212".equalsIgnoreCase(iD)){
	 				  temp = DatamigrationCommonUtil.addLeadingCharacter(temp, 2, ' ');
	 			   }
	 			   //End PCMP-1855, 1856 	 			  
	 			   if (temp.length() > length){
	 				   temp = temp.substring(0, length);
	 			   }
	 			   else {
	 				   temp = String.format("%-"+length+ "s", temp);
	 			   }
	 		   }
	 		   else {
	 			   temp = temp.substring(0, length);
	 		   }
	 	   }
	 	   else {
	 		   temp = String.format("%-"+length+ "s", allValuesHashMap.get(iD));
	 	   }
	 	   if (null != allValuesHashMap.get(iD) && allValuesHashMap.get(iD).equals("Yes")){
	 		   temp = String.format("%-"+length+ "s", "Y");
	 	   }
	 	   if (null != allValuesHashMap.get(iD) && allValuesHashMap.get(iD).equals("No")){
	 		   temp = String.format("%-"+length+ "s", "N");
	 	   }
	 	   
	 	   
	 	   if (type.equals("ZONED DECIMAL") || type.equals("PACKED DECIMAL")){
	 		   temp = (String)allValuesHashMap.get(iD);
	     	   if (iD.equals("A0150") || iD.equals("A0151")){
	     		   String percentage = (String)allValuesHashMap.get(iD);
	     		   if (percentage.contains("%")){
	     			   temp = String.format("%"+length+ "s", percentage.split("%")[0]).replace(' ', '0');
	     		   }
	 			   else {
	 				   temp = String.format("%"+length+ "s", percentage).replace(' ', '0');
	 			   }
	     	   }
	     	   else if (null != temp && temp.length() > length){ 
	     		   if (temp != null && !temp.equals("") && temp.contains(":")){
	     			   temp = temp.split(":")[0].trim();
	     			   if (temp.length() > length){
	     				   temp = temp.substring(0, length);
	     			   }
	     			   else {
	     				   temp = String.format("%"+length+ "s", temp).replace(' ', '0');
	     			   }
	     		   }
	     		   else {
	     			   temp = temp.substring(0, length);
	     			// start merge with sprint_A3   
	         		   if (temp != null && !temp.equals("") && temp.contains(".")){
	         			   precision = 0;
	         			   if (precisions[idx] != null && !precisions[idx].equals("N/A") ){
	         				   String sPRECISION = precisions[idx].split("\\|")[1];
	         				   precision = Integer.parseInt(sPRECISION);
	         			   }
	         			   numLength = length - precision;
	         			   String [] arrTokens = temp.split("\\.");
	         			   String num = ((String)arrTokens[0]).trim();
	         			   String precisionValue = ((String)arrTokens[1]).trim();
	         			  
	         			   if (precisionValue.length() < precision){
	         				   precisionValue = String.format("%-"+precision+ "s", precisionValue).replace(' ', '0');
	         			   }
	         			   else {
	         				   precisionValue = precisionValue.substring(0, precision);
	         			   }
	         			   if (num.length() < numLength){
	         				   num = String.format("%"+numLength+ "s", num).replace(' ', '0');
	         			   }
	         			   else {
	         				   num = num.substring(num.length() - numLength, num.length());
	         			   }
	         			   temp = num + precisionValue;
	         			 
	         		   }
	         		   else {
	         			   if (temp != null && !temp.equals("")){
	             			   precision = 0;
	             			   if (precisions[idx] != null && !precisions[idx].equals("N/A") ){
	             				   String sPRECISION = precisions[idx].split("\\|")[1];
	             				   precision = Integer.parseInt(sPRECISION);
	             			   }
	             			   numLength = length - precision;
	         				   String num = temp;
	         				   String precisionValue = "";
	             			   
	             			   if (precisionValue.length() < precision){
	             				   precisionValue = String.format("%-"+precision+ "s", precisionValue).replace(' ', '0');
	             			   }
	             			    
	             			   if (num.length() < numLength){
	             				   num = String.format("%"+numLength+ "s", num).replace(' ', '0');
	             			   }
	             			   else {
	             				   num = num.substring(num.length() - numLength, num.length());
	             			   }
	             			 
	             			   temp = num + precisionValue;
	         			   }
	         			   else {
	         				   temp = String.format("%"+length+ "s", "").replace(' ', '0');
	         			   }
	         		   }
						// end merge with sprint_A3
	     		   }
	     	   }
	     	   else { 
	     		   if (temp != null && !temp.equals("") && temp.contains(".")){
	     			   precision = 0;
	     			   if (precisions[idx] != null && !precisions[idx].equals("N/A") ){
	     				   String sPRECISION = precisions[idx].split("\\|")[1];
	     				   precision = Integer.parseInt(sPRECISION);
	     			   }
	     			   numLength = length - precision;
	     			   String [] arrTokens = temp.split("\\.");
	     			   String num = ((String)arrTokens[0]).trim();
	     			   String precisionValue = ((String)arrTokens[1]).trim();
	     			   //LOGGER.debug(idx+"; iD="+iD+"; precision="+precision+"; numLength="+numLength+"; num="+num+";precisionValue="+precisionValue);
	     			   if (precisionValue.length() < precision){
	     				   precisionValue = String.format("%-"+precision+ "s", precisionValue).replace(' ', '0');
	     			   }
	     			   else {
	     				   precisionValue = precisionValue.substring(0, precision);
	     			   }
	     			   if (num.length() < numLength){
	     				   num = String.format("%"+numLength+ "s", num).replace(' ', '0');
	     			   }
	     			   else {
	     				   num = num.substring(num.length() - numLength, num.length());
	     			   }
	     			   temp = num + precisionValue;
	     			   //LOGGER.debug(idx+"; iD="+iD+"; precision="+precision+"; numLength="+numLength+"; num="+num+"; precisionValue="+precisionValue+"; temp="+temp+ "###");
	     			   
	     		   }
	     		   else {
	     			   if (temp != null && !temp.equals("")){
	         			   precision = 0;
	         			   if (precisions[idx] != null && !precisions[idx].equals("N/A") ){
	         				   String sPRECISION = precisions[idx].split("\\|")[1];
	         				   precision = Integer.parseInt(sPRECISION);
	         			   }
	         			   numLength = length - precision;
	     				   String num = temp;
	     				   String precisionValue = "";
	         			   //LOGGER.debug(idx+"; iD="+iD+"; precision="+precision+"; numLength="+numLength+"; num="+num+";precisionValue="+precisionValue);
	         			   if (precisionValue.length() < precision){
	         				   precisionValue = String.format("%-"+precision+ "s", precisionValue).replace(' ', '0');
	         			   }
	         			   //else {
	         				//   precisionValue = precisionValue.substring(0, precision);
	         			   //}
	         			   if (num.length() < numLength){
	         				   num = String.format("%"+numLength+ "s", num).replace(' ', '0');
	         			   }
	         			   else {
	         				   num = num.substring(num.length() - numLength, num.length());
	         			   }
	         			   //LOGGER.debug(idx+"; iD="+iD+"; precision="+precision+"; numLength="+numLength+"; num="+num+";precisionValue="+precisionValue);
	         			   temp = num + precisionValue;
	     			   }
	     			   else {
	     				   temp = String.format("%"+length+ "s", "").replace(' ', '0');
	     			   }
	     		   }
	     	   }
	 	   }
	 	   else if (type.equals("ISO DATE")){
	 		    try {
	 			   if (allValuesHashMap.get(iD)!= null && !allValuesHashMap.get(iD).equals("") && !allValuesHashMap.get(iD).equals("0001-01-01")){
	 				   Date tempDate = formatter.parse((String)allValuesHashMap.get(iD));
	 				   temp = String.format("%"+length+ "s", formatter.format(tempDate));
	 			   }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						LOGGER.info("exception="+e + "; value="+allValuesHashMap.get(iD)+"##");
						//IntgSrvUtils.printConsole("date parsing exception="+e);
						//temp = String.format("%"+length+ "s", "");
					}
	 	   }
	 	   else if (type.equals("ISO TIMESTAMP")){
	 		    try {
	 			   if (iD.equals("A0004")){
	 				   Date tempDate = formatter2.parse((String)allValuesHashMap.get(iD));	 				   
	 			   }
					} catch (ParseException e) {						 
						e.printStackTrace();
						LOGGER.info("exception="+e + "; value="+allValuesHashMap.get(iD)+"##");
						 
					}
	 	   }
	 	   
	 	   if (hmConvertRemoveFANs.containsKey(iD)){
	 		   traceLogger.debug("iD="+iD+"; check R; temp="+temp);
	 		   if (temp != null && !temp.equals("") && (temp.equals("R") || temp.startsWith("R "))){
	 			   temp = String.format("%-"+length+ "s", " ");
	 		   }
	 	   }
	 	  
	 	   
	 	   
	 	   if (temp != null && temp.length() != length){
	 		   traceLogger.debug("temp.length() != length; temp="+temp+ "###");
	 	   }
	 	   if (temp.trim().equalsIgnoreCase("null"))
	 	   {
	 		   temp = "";
	 	   }
	 	   if (strResult2 == null)
	 	   { 
	 		   strResult2 = temp + strDelimiter;
	 		   strResult = temp;
	 	   }
	 	   else
	 	   { 
	 		   strResult2 = strResult2 + temp + strDelimiter;
	 		   strResult = strResult + temp;
	 	   }
	 	   traceLogger.debug(idx+"; iD="+iD+"; length="+length+";type="+type+"; temp="+temp+ "###");
	 	   idx++;
	 	   
	     }
	        listOfResults.add(0, strResult);
	        listOfResults.add(1, strResult2);
	        return listOfResults;
	   }  
}


