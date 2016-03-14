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

package com.staples.pim.base.common.bean;

import java.util.HashMap;
import java.util.Map;

public class StepTransmitterBean 
{
	private Map<Object, Object> funcSpecVars = null;
	long threadId = 0;
	private String fileName = null; 
	private String fileNameForProduct = null;
	private String fileNameForAttribute = null;
	private String fileNameForCSVFlgSku = null;
	private String fileNameForCSVBoomerang = null;
	private String baseURL = null;
	private String message = null; 
	
	private String inputDirectory = null;
	private String targetDirectoryXSV = null;
	private String targetDirectoryFixLength = null;
	private String targetDirectoryExcel = null;
	private String targetDirectoryCSVFlgSku = null;
	private String targetDirectoryCSVBoomerang = null;
	private String targetDirectoryExcelBoomerang = null;
	private String dateScheduleMMdd = null; 
	
	private String mqHostName = null;
	private int mqPport = 0;
	private String mqQueueManager = null;
	private String mqChannel = null;
	private String mqQueueName = null;
	private String mqName = null;
	private int mqTimeout = 0;  
	private int transactionType = 0;
	private int status = 0;
	
	private String headerLineAttribIdXSV   = null; 
	
	private String publishId = null;
	
	
	 
	/**
	 * public constructor
	 *
	 */  
	public StepTransmitterBean()
	{
		funcSpecVars = new HashMap<Object, Object>();
	}

	public Map<Object, Object> getFuncSpecVars() {
		return funcSpecVars;
	}

	public void setFuncSpecVars(Map<Object, Object> funcSpecVars) {
		this.funcSpecVars = funcSpecVars;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 
	
	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}  
	
	public String getMqHostName() {
		return mqHostName;
	}

	public void setMqHostName(String mqHostName) {
		this.mqHostName = mqHostName;
	}

	public int getMqPport() {
		return mqPport;
	}

	public void setMqPport(int mqPport) {
		this.mqPport = mqPport;
	} 
	
	public String getMqChannel() {
		return mqChannel;
	}

	public void setMqChannel(String mqChannel) {
		this.mqChannel = mqChannel;
	}

	public String getMqQueueName() {
		return mqQueueName;
	}

	public void setMqQueueName(String mqQueueName) {
		this.mqQueueName = mqQueueName;
	}

	public int getMqTimeout() {
		return mqTimeout;
	}

	public void setMqTimeout(int mqTimeout) {
		this.mqTimeout = mqTimeout;
	}

	public String getMqQueueManager() {
		return mqQueueManager;
	}

	public void setMqQueueManager(String mqQueueManager) {
		this.mqQueueManager = mqQueueManager;
	}

	public String getMqName() {
		return mqName;
	}

	public void setMqName(String mqName) {
		this.mqName = mqName;
	}

	/**
	* @param key - The key against which data is stored in the map.
	* @param item - The object to be stored in the map.
	*/
	public void addItem(String key, Object item)
	{
		funcSpecVars.put(key, item);
	}
	/**
	 * @param key - The key against which data is retrieved from the map.
	 * @return - The object stored against the key.
	 */
	public Object getItem(String key)
	{
		return funcSpecVars.get(key);
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getInputDirectory() {
		return inputDirectory;
	}

	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public String getTargetDirectoryXSV() {
		return targetDirectoryXSV;
	}

	public void setTargetDirectoryXSV(String targetDirectoryXSV) {
		this.targetDirectoryXSV = targetDirectoryXSV;
	} 

	public String getTargetDirectoryFixLength() {
		return targetDirectoryFixLength;
	}

	public void setTargetDirectoryFixLength(String targetDirectoryFixLength) {
		this.targetDirectoryFixLength = targetDirectoryFixLength;
	}

	public String getTargetDirectoryExcel() {
		return targetDirectoryExcel;
	}

	public void setTargetDirectoryExcel(String targetDirectoryExcel) {
		this.targetDirectoryExcel = targetDirectoryExcel;
	}

	public String getFileNameForProduct() {
		return fileNameForProduct;
	}

	public void setFileNameForProduct(String fileNameForProduct) {
		this.fileNameForProduct = fileNameForProduct;
	}

	public String getFileNameForAttribute() {
		return fileNameForAttribute;
	}

	public void setFileNameForAttribute(String fileNameForAttribute) {
		this.fileNameForAttribute = fileNameForAttribute;
	} 

	public String getTargetDirectoryCSVFlgSku() {
		return targetDirectoryCSVFlgSku;
	}

	public void setTargetDirectoryCSVFlgSku(String targetDirectoryCSVFlgSku) {
		this.targetDirectoryCSVFlgSku = targetDirectoryCSVFlgSku;
	}

	public String getTargetDirectoryCSVBoomerang() {
		return targetDirectoryCSVBoomerang;
	}

	public void setTargetDirectoryCSVBoomerang(String targetDirectoryCSVBoomerang) {
		this.targetDirectoryCSVBoomerang = targetDirectoryCSVBoomerang;
	}

	public String getTargetDirectoryExcelBoomerang() {
		return targetDirectoryExcelBoomerang;
	}

	public void setTargetDirectoryExcelBoomerang(
			String targetDirectoryExcelBoomerang) {
		this.targetDirectoryExcelBoomerang = targetDirectoryExcelBoomerang;
	}

	public String getFileNameForCSVFlgSku() {
		return fileNameForCSVFlgSku;
	}

	public void setFileNameForCSVFlgSku(String fileNameForCSVFlgSku) {
		this.fileNameForCSVFlgSku = fileNameForCSVFlgSku;
	}

	public String getFileNameForCSVBoomerang() {
		return fileNameForCSVBoomerang;
	}

	public void setFileNameForCSVBoomerang(String fileNameForCSVBoomerang) {
		this.fileNameForCSVBoomerang = fileNameForCSVBoomerang;
	}

	public String getDateScheduleMMdd() {
		return dateScheduleMMdd;
	}

	public void setDateScheduleMMdd(String dateScheduleMMdd) {
		this.dateScheduleMMdd = dateScheduleMMdd;
	}

	public String getHeaderLineAttribIdXSV() {
		return headerLineAttribIdXSV;
	}

	public void setHeaderLineAttribIdXSV(String headerLineAttribIdXSV) {
		this.headerLineAttribIdXSV = headerLineAttribIdXSV;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setPublishId(String pubId){
		this.publishId = pubId;
	}
	
	public String getPublishId(){
		return this.publishId;
	}	
}
