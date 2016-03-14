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
 * File name     :   ProductAttributesProces
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.bean.STEPProductInformation.Products.Product.Values.Value;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class ProductAttributesInProcess {
		 
	public  static HashMap<String, String> attrIDNameHashMap = null;
	public  static HashMap assetHashMap = null;
	public  static HashMap<String, HashMap> classificationHashMap = null;
	public static boolean bContainsSKU = false;
	
	public static StepTransmitterBean setClassificationsAndAssets(StepTransmitterBean transmitter, boolean reload) 
	{		// File fXmlFile,boolean reload, Map attrIDNameHashMap){
		 
	 File fXmlFile =  (File)transmitter.getItem(IntgSrvAppConstants.CURRENT_FILE_TO_HASHMAP);
	 bContainsSKU = false;
	 
	 attrIDNameHashMap = (HashMap<String, String>)transmitter.getItem(IntgSrvAppConstants.ATTR_ID_NAME_HASH_MAP_TO_HASHMAP);		 
	 assetHashMap = (HashMap)transmitter.getItem(IntgSrvAppConstants.ASSET_HASH_MAP_TO_HASHMAP);  
	 classificationHashMap = (HashMap<String, HashMap>)transmitter.getItem(IntgSrvAppConstants.CLASSIFICATION_HASH_MAP_TO_HASHMAP);   
	 
    try { 
    	 
    	HashMap<String, String> categoryHashMap = null;
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = dBuilder.parse(fXmlFile);
     
    	//optional, but recommended
    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    	doc.getDocumentElement().normalize();
     
    	IntgSrvUtils.printConsole("Root element :" + doc.getDocumentElement().getNodeName());
    	

		NodeList productsNode=doc.getElementsByTagName("Products");
		getVendorUPCProduct(productsNode);
     	getVendorUPCActiveFlag();
    	NodeList nList = doc.getElementsByTagName("Asset");
     
    	IntgSrvUtils.printConsole("--------------Asset--------------");
    
    	for (int temp = 0; temp < nList.getLength(); temp++) {
     
    		Node nNode = nList.item(temp);
     
    		IntgSrvUtils.printConsole("\nCurrent Element :" + nNode.getNodeName());
     
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
     
    			Element eElement = (Element) nNode;
    			String assetID = eElement.getAttribute("ID");
    			IntgSrvUtils.printConsole("assetID : " + assetID);
    			NodeList nList2 = eElement.getElementsByTagName("AssetPushLocation");
    			for (int i=0;i<nList2.getLength(); i++){
    				Node nNode2 = nList2.item(i);
    				Element eElement2 = (Element) nNode2;
    				String assetPushLocationValue = nNode2.getTextContent();
    				assetPushLocationValue = assetPushLocationValue.substring(assetPushLocationValue.lastIndexOf('/')+1, assetPushLocationValue.length());
    				IntgSrvUtils.printConsole("\nCurrent Element 2 :" + nNode2.getNodeName());
    				IntgSrvUtils.printConsole("assetPushLocationValue : " + assetPushLocationValue);
    				assetHashMap.put(assetID, assetPushLocationValue);    				
    			}
    		}
    	}
    	nList = doc.getElementsByTagName("Product");
    	IntgSrvUtils.printConsole("---------------Product-------------size="+nList.getLength());
    	//Below condition commented and hasSKU method is used to check whether XML has Item & SKU. 
    	//If Item & SKU is exist return true otherwise false
    	//if (nList.getLength() >1){// has both Item and SKU
    		bContainsSKU = hasSKU(nList);    		
//    	} 	
    	
    	nList = doc.getElementsByTagName("Classification");
    	
    	getDocTypeofInputXml(nList);
	     
    	IntgSrvUtils.printConsole("---------------Classification-------------");
    
    	for (int temp = 0; temp < nList.getLength(); temp++) {
    		//categoryHashMap = new HashMap();
    		Node nNode = nList.item(temp);
     
    		IntgSrvUtils.printConsole("\nCurrent Element :" + nNode.getNodeName());
     
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
     
    			Element eElement = (Element) nNode;
    			String iD = eElement.getAttribute("ID");
    			IntgSrvUtils.printConsole("iD : " + iD);
    			String userTypeID = eElement.getAttribute("UserTypeID");
    			IntgSrvUtils.printConsole("userTypeID : " + userTypeID);
    			String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
    			IntgSrvUtils.printConsole("name : " + name);
    			if (!userTypeID.equals("Classification 1 user-type root")){
    				continue;
    			}
    			// level 2
    			NodeList nList2 = eElement.getElementsByTagName("Classification");
    			if (nList2 != null && nList2.getLength() >0){
	    			Node nNode2 = nList2.item(0);
    				Element eElement2 = (Element) nNode2;
	    			String iD2 = eElement2.getAttribute("ID");
	    			IntgSrvUtils.printConsole("iD2 : " + iD2);
	    			String userTypeID2 = eElement2.getAttribute("UserTypeID");
	    			IntgSrvUtils.printConsole("userTypeID2 : " + userTypeID2);
	    			String name2 = eElement2.getElementsByTagName("Name").item(0).getTextContent();
	    			IntgSrvUtils.printConsole("name2 : " + name2);
	    			// level 3
	    			NodeList nList3 = eElement2.getElementsByTagName("Classification");
	    			if (nList2 != null && nList3.getLength() >0){
		    			Node nNode3 = nList3.item(0);
	    				Element eElement3 = (Element) nNode3;
		    			String iD3 = eElement3.getAttribute("ID");
		    			IntgSrvUtils.printConsole("iD3 : " + iD3);
		    			String userTypeID3 = eElement3.getAttribute("UserTypeID");
		    			IntgSrvUtils.printConsole("userTypeID3 : " + userTypeID3);
		    			String name3 = eElement3.getElementsByTagName("Name").item(0).getTextContent();
		    			IntgSrvUtils.printConsole("name3 : " + name3);
		    			// level 4
		    			NodeList nList4 = eElement3.getElementsByTagName("Classification");
		    			if (nList4 != null && nList4.getLength() >0){
			    			Node nNode4 = nList4.item(0);
		    				Element eElement4 = (Element) nNode4;
			    			String iD4 = eElement4.getAttribute("ID");
			    			IntgSrvUtils.printConsole("iD4 : " + iD4);
			    			String userTypeID4 = eElement4.getAttribute("UserTypeID");
			    			IntgSrvUtils.printConsole("userTypeID4 : " + userTypeID4);
			    			String name4 = eElement4.getElementsByTagName("Name").item(0).getTextContent();
			    			IntgSrvUtils.printConsole("name4 : " + name4);
			    			// level 5
			    			NodeList nList5 = eElement4.getElementsByTagName("Classification");
			    			if (nList5 != null && nList5.getLength() >0){
				    			Node nNode5 = nList5.item(0);
			    				Element eElement5 = (Element) nNode5;
				    			String iD5 = eElement5.getAttribute("ID");
				    			IntgSrvUtils.printConsole("iD5 : " + iD5);
				    			String userTypeID5 = eElement5.getAttribute("UserTypeID");
				    			IntgSrvUtils.printConsole("userTypeID5 : " + userTypeID5);
				    			String name5 = eElement5.getElementsByTagName("Name").item(0).getTextContent();
				    			IntgSrvUtils.printConsole("name5 : " + name5);
				    			categoryHashMap = new HashMap();
				    			categoryHashMap.put("SuperCategory", name5);
				    			// level 6
				    			NodeList nList6 = eElement5.getElementsByTagName("Classification");
				    			if (nList6 != null && nList6.getLength() >0){
					    			Node nNode6 = nList6.item(0);
				    				Element eElement6 = (Element) nNode6;
					    			String iD6 = eElement6.getAttribute("ID");
					    			IntgSrvUtils.printConsole("iD6 : " + iD6);
					    			String userTypeID6 = eElement6.getAttribute("UserTypeID");
					    			IntgSrvUtils.printConsole("userTypeID6 : " + userTypeID6);
					    			String name6 = eElement6.getElementsByTagName("Name").item(0).getTextContent();
					    			IntgSrvUtils.printConsole("name6 : " + name6);
					    			if (categoryHashMap == null)
					    			{
					    				categoryHashMap = new HashMap();
					    			}
					    			categoryHashMap.put("Category", name6);
					    			// level 7
					    			NodeList nList7 = eElement6.getElementsByTagName("Classification");
					    			if (nList7 != null && nList7.getLength() >0){
						    			Node nNode7 = nList7.item(0);
					    				Element eElement7 = (Element) nNode7;
						    			String iD7 = eElement7.getAttribute("ID");
						    			IntgSrvUtils.printConsole("iD7 : " + iD7);
						    			String userTypeID7 = eElement7.getAttribute("UserTypeID");
						    			IntgSrvUtils.printConsole("userTypeID7 : " + userTypeID7);
						    			String name7 = eElement7.getElementsByTagName("Name").item(0).getTextContent();
						    			IntgSrvUtils.printConsole("name7 : " + name7);
						    			if (categoryHashMap == null)
						    			{
						    				categoryHashMap = new HashMap();
						    			}
						    			categoryHashMap.put("Department", name7);
						    			// level 8
						    			NodeList nList8 = eElement7.getElementsByTagName("Classification");
						    			if (nList8 != null && nList8.getLength() >0){
							    			Node nNode8 = nList8.item(0);
						    				Element eElement8 = (Element) nNode8;
							    			String iD8 = eElement8.getAttribute("ID");
							    			IntgSrvUtils.printConsole("iD8 : " + iD8);
							    			String userTypeID8 = eElement8.getAttribute("UserTypeID");
							    			IntgSrvUtils.printConsole("userTypeID8 : " + userTypeID8);
							    			String name8 = eElement8.getElementsByTagName("Name").item(0).getTextContent();
							    			IntgSrvUtils.printConsole("name8 : " + name8);
							    			if (categoryHashMap == null)
							    			{
							    				categoryHashMap = new HashMap();
							    			}
							    			categoryHashMap.put("Class", name8);
							    			classificationHashMap.put(iD8, categoryHashMap);
							    			
							    			
						    			}
					    			}
				    			}
			    			}
			    			 
		    			}
	    			}
    			}
    		}
    	}
    	if (categoryHashMap != null)
    	{
    		transmitter.addItem(IntgSrvAppConstants.CATEGORY_MAP_TO_HASHMAP, categoryHashMap);
    	}
    	transmitter.addItem(IntgSrvAppConstants.CLASSIFICATION_MAP_TO_HASHMAP, classificationHashMap);
    	transmitter.addItem(IntgSrvAppConstants.ASSETS_MAP_TO_HASHMAP, assetHashMap);
    	
    	IntgSrvUtils.printConsole("Attribute reload="+reload);
    	if (reload || (attrIDNameHashMap != null && attrIDNameHashMap.size() < 1)){
	    	nList = doc.getElementsByTagName("Attribute");
	    	IntgSrvUtils.printConsole("---------------Attribute-------------"); 
	    
	    	for (int temp = 0; temp < nList.getLength(); temp++) {
	    		//categoryHashMap = new HashMap();
	    		Node nNode = nList.item(temp);
	     
	    		IntgSrvUtils.printConsole("\nCurrent Element :" + nNode.getNodeName());
	     
	    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	     
	    			Element eElement = (Element) nNode;
	    			String iD = eElement.getAttribute("ID");
	    			IntgSrvUtils.printConsole("iD : " + iD);
	    			//String userTypeID = eElement.getAttribute("UserTypeID");
	    			//IntgSrvUtils.printConsole("userTypeID : " + userTypeID);
	    			String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
	    			IntgSrvUtils.printConsole("name : " + name);
	    			// level 2
	    			NodeList nList2 = eElement.getElementsByTagName("AttributeGroupLink");
	    			if (nList2 != null && nList2.getLength() >0){
	    				for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
		    				Node nNode2 = nList2.item(temp2);
		    				Element eElement2 = (Element) nNode2;
			    			String iD2 = eElement2.getAttribute("AttributeGroupID");
			    			IntgSrvUtils.printConsole("iD2 : " + iD2);
			    			//if (iD.startsWith("CAT")){
			    			if (iD2.equalsIgnoreCase("Dotcom Spec Attributes")){
			    				attrIDNameHashMap.put(iD, name);
			    				IntgSrvUtils.printConsole("add to attrIDNameHashMap iD="+iD+"; name=" + name);
			    			}
	    				}
	    			}
	    		}
	    	}
	    	
    	}
    	    	 
    	transmitter.addItem(IntgSrvAppConstants.ATTR_ID_NAME_HASH_MAP_TO_HASHMAP, attrIDNameHashMap);		 
   	    transmitter.addItem(IntgSrvAppConstants.ASSET_HASH_MAP_TO_HASHMAP, assetHashMap);  
   	    transmitter.addItem(IntgSrvAppConstants.CLASSIFICATION_HASH_MAP_TO_HASHMAP, classificationHashMap);   
   	    transmitter.addItem(IntgSrvAppConstants.BOTH_ITEM_AND_SKU_TO_HASHMAP, bContainsSKU);
    	
    } catch (Exception e) {
    	e.printStackTrace();
    }
	return transmitter;   
}

public static String processProductAttr(String arrIDs[],String arrNames[],String line)  {
		 
	//Map assetHashMap = null; 
	//Map classificationHashMap = null;
	
	String strProductFileContent = "";
	String sDelemiter = "~|~";
	int iAttrStart = 0;
	//String line = null;
	String lineProduct = null;
	String lineAttribute = null;
	String [] arrValues = null;
	String [] arrProductValues = null;
	String [] arrAttrValues = null;
	
	arrValues = line.split("~\\|~###",100);
	IntgSrvUtils.printConsole("arrValues size="+arrValues.length);
	lineProduct = arrValues[0];
	lineAttribute = arrValues[1];
	arrProductValues = lineProduct.split("~\\|~",100);
	
	String strSKU = "";
	String strAttribute_Group = "";
	String strAttribute_Name = "";
	String sID = "";
	String strValue = "";
	String strClass = "";
	String strActionCode = "";
	IntgSrvUtils.printConsole("arrProductValues size="+arrProductValues.length);
	for (int i=0;i<arrProductValues.length;i++){
		strValue = arrProductValues[i];
		sID = arrIDs[i];
		//IntgSrvUtils.printConsole("processProductAttr::sID="+sID);
		if (sID.equalsIgnoreCase("A0012")){
			strSKU = arrProductValues[i];
		}
		else if (sID.equalsIgnoreCase("Class")){
			strClass = arrProductValues[i];
		}
		else if (sID.equalsIgnoreCase("Action Code")){
			strActionCode = arrProductValues[i];
		}
		
		if (sID.equals("SuperCategory") && !strValue.equals("")){
			//IntgSrvUtils.printConsole("processProductAttr(): SuperCategory="+classificationHashMap.get(strValue).toString());
			if ((HashMap)classificationHashMap.get(strValue) != null){
				strValue = (String)((HashMap)classificationHashMap.get(strValue)).get("SuperCategory");
			}
		}
		else if (sID.equals("Category") && !strValue.equals("")){
			if ((HashMap)classificationHashMap.get(strValue) != null){
				strValue = (String)((HashMap)classificationHashMap.get(strValue)).get("Category");
			}
		}
		else if (sID.equals("Department") && !strValue.equals("")){
			if ((HashMap)classificationHashMap.get(strValue) != null){
				strValue = (String)((HashMap)classificationHashMap.get(strValue)).get("Department");
			}
		}
		else if (sID.equals("Class") && !strValue.equals("")){
			if ((HashMap)classificationHashMap.get(strValue) != null){
				strValue = (String)((HashMap)classificationHashMap.get(strValue)).get("Class");
			}
		}
		else if (sID.equals("Image1")
				|| sID.equals("Image2")
				|| sID.equals("Image3")
				|| sID.equals("Image4")
				|| sID.equals("Image5")
				|| sID.equals("Image6")
				|| sID.equals("Image7")
				|| sID.equals("Image8")
				|| sID.equals("Image9")
		){
			if (assetHashMap.get(strValue) != null){
				strValue = (String)assetHashMap.get(strValue);
			}
		}
		strProductFileContent = strProductFileContent + strValue +sDelemiter;
	}
	strProductFileContent = strProductFileContent.substring(0, strProductFileContent.length() - sDelemiter.length());
	IntgSrvUtils.printConsole("processCategoryAttr(): strAttrFileContent="+strProductFileContent);
	
	return strProductFileContent;

}

public static String processCategoryAttr(String arrIDs[],String arrNames[],String line) {
		// String arrIDs[],String arrNames[],String line, Map classificationHashMap, Map attrIDNameHashMap){
		// all parms would be replaced by transmitter bean
	//int iAttrRecordCount = 0;
	String strAttrFileContent = "";
	String sDelemiter = "~|~";
	int iAttrStart = 0;  
	String lineProduct = null;
	String lineAttribute = null;
	String [] arrValues = null;
	String [] arrProductValues = null;
	String [] arrAttrValues = null; 
	//Map classificationHashMap = null;
	//Map attrIDNameHashMap = null;
	
	arrValues = line.split("~\\|~###",100);
	IntgSrvUtils.printConsole("arrValues size="+arrValues.length);
	lineProduct = arrValues[0];
	lineAttribute = arrValues[1];
	arrProductValues = lineProduct.split("~\\|~",100);
	
	String strSKU = "";
	String strAttribute_Group = "";
	String strAttribute_Name = "";
	String strValue = "";
	String strClass = "";
	String strActionCode = "";
	IntgSrvUtils.printConsole("arrProductValues size="+arrProductValues.length);
	for (int i=0;i<arrProductValues.length;i++){
		if (arrIDs[i].equalsIgnoreCase("A0012")){
			strSKU = arrProductValues[i];
		}
		else if (arrIDs[i].equalsIgnoreCase("Class")){
			if (classificationHashMap.get(arrProductValues[i]) != null){
				strClass = (String)((HashMap)classificationHashMap.get(arrProductValues[i])).get("Class");
			}
		}
		else if (arrIDs[i].equalsIgnoreCase("Action Code")){
			strActionCode = arrProductValues[i];
		}
	}
	iAttrStart = arrProductValues.length;
	IntgSrvUtils.printConsole("processCategoryAttr(): iAttrStart="+iAttrStart);
	arrAttrValues = lineAttribute.split("~\\|~",500);
	IntgSrvUtils.printConsole("arrAttrValues size="+arrAttrValues.length);
	String strAttrLine = "";
	String [] arrValues2 = null;
    String sAttributeID = "";
    String sAttrValue = "";
	for (int i=0;i<arrAttrValues.length;i++){
		if (arrAttrValues[i] != null && !arrAttrValues[i].equalsIgnoreCase("")){
			arrValues2 = arrAttrValues[i].split("###",5);
			//strAttribute_Name = arrNames[iAttrStart + i];
			strAttribute_Name = arrValues2[0];
			IntgSrvUtils.printConsole("processCategoryAttr(): FAN_ID="+strAttribute_Name);
			strAttribute_Name = (String)attrIDNameHashMap.get(strAttribute_Name);
			IntgSrvUtils.printConsole("processCategoryAttr(): strAttribute_Name="+strAttribute_Name);
			IntgSrvUtils.printConsole("processCategoryAttr(): FAN="+arrValues2[1]);
			strValue = arrValues2[1];
			String temp = strValue;
		   if (temp != null && !temp.equals("") && temp.contains(":")){
			   temp = temp.split(":")[0].trim();
		   }
		   

			strAttrLine = strSKU + sDelemiter + 
						strAttribute_Group + sDelemiter + 
						strAttribute_Name + sDelemiter + 
						temp + sDelemiter + 
						strClass + sDelemiter + 
						strActionCode;
			IntgSrvUtils.printConsole("processCategoryAttr(): strAttrLine="+strAttrLine);
			strAttrFileContent = strAttrFileContent + strAttrLine + "\r\n";
			//iAttrRecordCount ++;
		}
	}
	IntgSrvUtils.printConsole("processCategoryAttr(): strAttrFileContent="+strAttrFileContent);
	
	return strAttrFileContent;

}
	
public static List<String>  addRecordCount (StepTransmitterBean transmitter, String fullPathToProductXsvFile)  throws IOException {   
	 
	String twoDelimeter = "###";
	String line = null;
	String lineProduct = null;
	String lineAttribute = null;
	String [] arrValues = null;
	String [] arrOfProductIDs = null;
	String [] arrNames = null;
	
	SimpleDateFormat dateformatMMdd = new SimpleDateFormat("MM_dd_HH_mm_ss_SSS");
	String dateScheduleMMdd = transmitter.getDateScheduleMMdd();  
	 
	String productXSVFileUrl = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForProduct() +
									dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION; 
	
	String attributeXSVFileUrlTemp = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForAttribute() +
		dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION_TEMP; 
		 
	String attributeXSVFileUrl = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForAttribute() +
						dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION;    
		 
	
	/*String productXSVFileUrl = (String)transmitter.getItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP);
	String attributeXSVFileUrlTemp = (String)transmitter.getItem(IntgSrvAppConstants.TARGET_ATTRIBUTE_FILE_XSV_URL_TO_HASHMAP);
	String attributeXSVFileUrl = (String)transmitter.getItem(IntgSrvAppConstants.TARGET_ATTRIBUTE_FILE_XSV_TEMP_URL_TO_HASHMAP);  */
	
	
	IntgSrvUtils.printConsole("addRecordCount(): productXSVFileUrl = "+fullPathToProductXsvFile);
	BufferedReader br = new BufferedReader(new FileReader(fullPathToProductXsvFile));
	int iRecordCount = 2;// set to 1, because file contains at least header line
	 
	while ((line = br.readLine()) != null) {
		if (line.contains(twoDelimeter)){//exclude header line and empty line
			iRecordCount++;
		}
	}
	
	br.close();
	
	
	String strAttrFileContent = "";
	List<String> listOfFilesURLForNextStep = null;
	
	if (iRecordCount > 2){// there are two header lines, so 2
		br = new BufferedReader(new FileReader(fullPathToProductXsvFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(productXSVFileUrl));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(attributeXSVFileUrlTemp));
		iRecordCount = iRecordCount - 2; // decrease 2 because it include header line count
		IntgSrvUtils.printConsole("addRecordCount(): iRecordCount="+iRecordCount);
		
		SimpleDateFormat dateformatyyyy_MM_dd  = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_sss");
		SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.sss");
		Date oDateNow = new Date();
		
		int iCount = 0;			
		String delimiterTildaPipeTilda = IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA;
		
		
		while ((line = br.readLine()) != null) {
			iCount++;
			IntgSrvUtils.printConsole("iCount="+iCount +"; line="+line);
			lineProduct = "";
			lineAttribute = "";
			if (iCount == 1){
				arrOfProductIDs = line.split("~\\|~",500);
				IntgSrvUtils.printConsole("iCount="+iCount +"; arrIDs size="+arrOfProductIDs.length);
			}
			else if (iCount == 2){
				arrNames = line.split("~\\|~",500);
				IntgSrvUtils.printConsole("iCount="+iCount +"; arrNames size="+arrNames.length);
				lineProduct = IntgSrvAppConstants.STIBO									+ 
							dateformatyyyy_MM_dd.format(oDateNow)						+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.STIBO100								+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.NON_STOCK_ITEM							+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.PRODUCT_SYNCHRONIZATION					+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							dateformatyyyyMMdd.format(oDateNow) 						+ 
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							iRecordCount;
				lineAttribute = 
							IntgSrvAppConstants.STIBO									+
							dateformatyyyy_MM_dd.format(oDateNow)						+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.STIBO103								+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.NON_STOCK_ITEM							+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.ATTRIBUTE_SYNCHRONIZATION 				+
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							dateformatyyyyMMdd.format(oDateNow) 						+ 
							IntgSrvAppConstants.DELIMITER_TILDA_PIPE_TILDA				+
							IntgSrvAppConstants.END_OF_THE_LINE;
				
				bw.write(lineProduct);
				bw.newLine();
				bw2.write(lineAttribute);
				 
			}
			else if (line.contains("###")){
				line = line.replace(IntgSrvAppConstants.DELIMITER_CUSTOM_NEW_LINE, "\n");  // merged with Sprint A3
				arrValues = line.split("~\\|~###");
				lineProduct = arrValues[0]; 
				//bw.write(ProductAttributes.processProductAttr(arrIDs,arrNames,line));
				bw.write(ProductAttributesInProcess.processProductAttr(arrOfProductIDs, arrNames, line)); 
				bw.newLine();
				// strAttrFileContent = strAttrFileContent + ProductAttributes.processCategoryAttr(arrIDs,arrNames,line);					
				strAttrFileContent = strAttrFileContent + ProductAttributesInProcess.processCategoryAttr(arrOfProductIDs, arrNames, line); 
				IntgSrvUtils.printConsole("iCount="+iCount +"; strAttrFileContent="+strAttrFileContent);
			}
		}
		bw2.write(strAttrFileContent);
		bw2.newLine();
		br.close();
		bw.close();
		bw2.close();
	 
		File oldXSV = new File(attributeXSVFileUrlTemp);
		if (oldXSV.exists()){				 
			attributeXSVFileUrl = addAttrRecordCount(transmitter, attributeXSVFileUrlTemp);
			oldXSV.delete(); 
		}		
		 listOfFilesURLForNextStep = new ArrayList();
		 listOfFilesURLForNextStep.add(0, productXSVFileUrl);
		 listOfFilesURLForNextStep.add(1, attributeXSVFileUrl); 
	}
	return listOfFilesURLForNextStep;
} 

public static String addAttrRecordCount(StepTransmitterBean transmitter, String fileToBeProcessedUrl) {
	 
	String line = null; 
	
	String atributeXSVFileUrlNew = 	transmitter.getTargetDirectoryXSV() 	+ 
									transmitter.getFileNameForAttribute()	+
									transmitter.getDateScheduleMMdd()		+
									IntgSrvAppConstants.XSV_FILE_EXTENSION; 
	
	 
	IntgSrvUtils.printConsole("addAttrRecordCount(): filePath=" + fileToBeProcessedUrl); 
	
	BufferedReader br = null;
	BufferedWriter bw = null;
	int iRecordCount = 0;
	
	try {
		br = new BufferedReader(new FileReader(fileToBeProcessedUrl));		 
	 
		while ((line = br.readLine()) != null) {
			iRecordCount++;
		}
	 
		br.close(); 
	 
		br = new BufferedReader(new FileReader(fileToBeProcessedUrl)); 
	 
		bw = new BufferedWriter(new FileWriter(atributeXSVFileUrlNew));
	 
		iRecordCount--; // decrease 1 because it include header line count
		iRecordCount--; // decrease 1 because it include empty line
		IntgSrvUtils.printConsole("addRecordCount(): iRecordCount="+iRecordCount);
		int iCount = 0;
	 
		while ((line = br.readLine()) != null) {
			iCount++;
			if (iCount == 1){
				line = line + iRecordCount;// add record count to the end of first line
			}
			bw.write(line);
			bw.newLine();
		}
	 
		br.close();		 
		bw.close();
		
	} catch (FileNotFoundException e) {
		if (null!= br){
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		if (null != bw){
			try {
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		e.printStackTrace();
	 
	} catch (IOException e) {
		if (null!= br){
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		if (null != bw){
			try {
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		e.printStackTrace();
	}
	 
	return atributeXSVFileUrlNew;
} 
public static List<STEPProductInformation.Products.Product.Values.Value> vendorData=null;
public static List<STEPProductInformation.Products.Product.Values.Value> responsibilityAttributeList=null;

public static void init(){
	responsibilityAttributeList=new ArrayList<STEPProductInformation.Products.Product.Values.Value>();
	vendorData=new ArrayList<STEPProductInformation.Products.Product.Values.Value>();
}
public static void getDocTypeofInputXml(NodeList nlistWithClassifications){

	responsibilityAttributeList=new ArrayList<STEPProductInformation.Products.Product.Values.Value>();
	STEPProductInformation.Products.Product.Values.Value value;
	IntgSrvUtils.printConsole("Inside Runscheduler function to build doc type"+nlistWithClassifications.getLength());
	for(int i=0;i<nlistWithClassifications.getLength();i++){
		Node thisNode=nlistWithClassifications.item(i);

		if (thisNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) thisNode;
			String iD = eElement.getAttribute("UserTypeID");
			if(("Class".equals(iD)) || ("Department".equals(iD)) || ("Division".equals(iD))){
				Node metadataNode = eElement.getElementsByTagName("MetaData").item(0);
				Element nElement=(Element)metadataNode;
				NodeList valueList=nElement.getElementsByTagName("Value");

				for(int valno=0;valno<valueList.getLength();valno++){
					Element valueelement=(Element)valueList.item(valno);
					value=new STEPProductInformation.Products.Product.Values.Value();
					value.setAttributeID(valueelement.getAttribute("AttributeID"));
					value.setValue(valueelement.getTextContent());
					responsibilityAttributeList.add(value);
				}
			}    			
		}
	}

}

public static void getVendorUPCDetails(Element eElement){
	STEPProductInformation.Products.Product.Values.Value value;
	NodeList valuesList=eElement.getElementsByTagName("Values");
	for(int i=0;i<valuesList.getLength();i++){
		Node valuesNode=valuesList.item(i);
		if (valuesNode.getNodeType() == Node.ELEMENT_NODE) {
			Element valuesElement=(Element)valuesNode;
			NodeList valueList=valuesElement.getElementsByTagName("Value");
			for(int j=0;j<valueList.getLength();j++){
				Node valueNode=valueList.item(j);
				if(valueNode.getNodeType() ==Node.ELEMENT_NODE){
					Element valueElement=(Element)valueNode;
					value=new STEPProductInformation.Products.Product.Values.Value();
					if(!("".equals(valueElement.getAttribute("AttributeID"))) && valueElement.getAttribute("AttributeID")!=null){
						value.setAttributeID(valueElement.getAttribute("AttributeID"));
						value.setValue(valueElement.getTextContent());
						vendorData.add(value);
					}
				}
			}


			NodeList multiValueList=valuesElement.getElementsByTagName("MultiValue");
			for(int j=0;j<multiValueList.getLength();j++){
				Node multiValueNode=multiValueList.item(j);
				if(multiValueNode.getNodeType() ==Node.ELEMENT_NODE){
					Element multiValueElement=(Element)multiValueNode;
					value=new STEPProductInformation.Products.Product.Values.Value();
					value.setAttributeID(multiValueElement.getAttribute("AttributeID"));
					NodeList valueMList=multiValueElement.getElementsByTagName("Value");
					String valueContent="";

					for(int k=0;k<valueMList.getLength();k++){
						Node valueMNode=valueMList.item(k);
						Element valueMElement=(Element)valueMNode;
						if(k==0){
							valueContent+=valueMElement.getTextContent();
						}else{
							valueContent+=","+valueMElement.getTextContent();
						}
					}
					value.setValue(valueContent);
					vendorData.add(value);
				}
			}
		}
	}
}

public static void getVendorUPCProduct(NodeList nodeContainingProducts){
	for(int j=0;j<nodeContainingProducts.getLength();j++){
		Node productsNode=nodeContainingProducts.item(j);
		if(productsNode.getNodeType() == Node.ELEMENT_NODE){
			Element productsElement=(Element)productsNode;
			NodeList productNode=productsElement.getElementsByTagName("Product");
			for(int i=0;i<productNode.getLength();i++){
				Node node=productNode.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					String usertypeID=eElement.getAttribute("UserTypeID");
					if("ItemToVendor".equalsIgnoreCase(usertypeID)){
						if(vendorData.isEmpty()){
						getVendorUPCDetails(eElement);
						}
					}

				}
			}
		}
	}
}
/**
	 * Vendor upc and active flag data set into respective attributes.
	 * 
	 */
	public static void getVendorUPCActiveFlag(){
		Value valuetype;
		
		for(int i=0;i<vendorData.size();i++){
			IntgSrvUtils.printConsole("Vendor data list size : "+vendorData.size());
			valuetype=vendorData.get(i);
			if(valuetype.getAttributeID().equalsIgnoreCase("A0080")){
				getActiveFlagValue(valuetype,i,IntgSrvAppConstants.A0430_SUPC);
				IntgSrvUtils.printConsole("Vendor data: "+valuetype.getAttributeID()+" /"+valuetype.getValue());
			}
			if(valuetype.getAttributeID().equalsIgnoreCase("A0082")){
				getActiveFlagValue(valuetype,i,IntgSrvAppConstants.A0430_CUPC);
				IntgSrvUtils.printConsole("Vendor data: "+valuetype.getAttributeID()+" /"+valuetype.getValue());
			}

			if(valuetype.getAttributeID().equalsIgnoreCase("A0083")){
				getActiveFlagValue(valuetype,i,IntgSrvAppConstants.A0430_IUPC);
				IntgSrvUtils.printConsole("Vendor data: "+valuetype.getAttributeID()+" /"+valuetype.getValue());
			}

			if(valuetype.getAttributeID().equalsIgnoreCase("A0084")){
				getActiveFlagValue(valuetype,i,IntgSrvAppConstants.A0430_PUPC);
				IntgSrvUtils.printConsole("Vendor data: "+valuetype.getAttributeID()+" /"+valuetype.getValue());
			}
		}
	}

	/**
	 * @param valuetype
	 * @param i
	 * @param attributeid
	 */
	public static void getActiveFlagValue(Value valuetype,int i,String attributeid){
		String value;
		Value newValue;
		String[] splitValuesColon=null;
		String[] splitValuesComma;
		value=valuetype.getValue();
		newValue=new Value();
		newValue.setAttributeID(attributeid);
		if(value.equalsIgnoreCase("N/A")){
			newValue.setValue("N");
			vendorData.add(newValue);
		}else if(value.contains(",")){
			IntgSrvUtils.printConsole("comma separated values.. "+value );
			splitValuesComma=value.split(",");
			boolean isSet=false;
			for(String str:splitValuesComma){
			
				splitValuesColon=str.split(":");
				if(splitValuesColon.length==2){
					if(splitValuesColon[1].length()==1 && splitValuesColon[1].equalsIgnoreCase("Y")){
						isSet=true;
						newValue.setValue(splitValuesColon[1]);
						vendorData.get(i).setValue(splitValuesColon[0]);
					}
				}
			}
			if(!isSet){
				vendorData.get(i).setValue(splitValuesColon[0]);
				newValue.setValue("N");
				vendorData.add(newValue);
			}else{
				vendorData.add(newValue);
			}
			
		}else if(value.contains(":")){
			 
			splitValuesColon=value.split(":");
			if(splitValuesColon.length==2){
				vendorData.get(i).setValue(splitValuesColon[0]);
				if(splitValuesColon[1].length()==1){
					newValue.setValue(splitValuesColon[1]);
					vendorData.add(newValue);
				}
			}
		} else if(IntgSrvAppConstants.A0430_SUPC.equalsIgnoreCase(attributeid)){
		 
			newValue.setValue("Y");
			vendorData.add(newValue);
		} else {
		 
			newValue.setValue("N");
			vendorData.add(newValue);
		}

	
	}

	/**
	 * To check whether XML has SKU. If SKU is exist return true otherwise false
	 * @param nlist
	 * @return
	 */
	public static boolean hasSKU(NodeList nlist){
		
		boolean isSku = false;
		boolean isItem = false;
		for(int i=0;i<nlist.getLength();i++){
			Node node=nlist.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
    			Element eElement = (Element) node;
    			String userTypeID=eElement.getAttribute("UserTypeID");
    			IntgSrvUtils.printConsole(userTypeID);
    			if("SKU".equalsIgnoreCase(userTypeID)){
    				isSku = true;
    			}
    			if("Item".equalsIgnoreCase(userTypeID)){
    				isItem = true;
    			}
    		}
		}
		if(isItem && isSku){
			return true;
		}
		IntgSrvUtils.printConsole("XML does not contains sku.");
		return false;
	}

}
