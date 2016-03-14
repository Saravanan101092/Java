
package com.staples.pim.delegate.itemutility.beans;

import java.util.HashMap;
import java.util.Map;

import com.staples.pcm.stepcontract.beans.STEPProductInformation;

/**
 * Value object is used to hold the complete details about the service and
 * transaction.
 * 
 * @author :: 522462@tcs
 * @Date :: Nov 18, 2014
 * @FileName:: CommonVO.java
 * @Package :: com.staples.itemutility.beans
 */
public class CommonVO {

	/**
	 * To hold the all galaxy value based on common-config.xml
	 */
	private Map<String, String>		attributeMap	= new HashMap<String, String>();
	/**
	 * STEPProductInformation to generate the STEP XML
	 */
	private STEPProductInformation	stepProductInformation;

	/**
	 * ReportVO used to update DB
	 */
	private ReportVO				reportVO;

	/**
	 * TransactionType string is used for validation
	 */
	private String					transactionType;

	/**
	 * Vendor number for key value
	 */
	private String					channel;

	

	/**
	 * Model number for key value
	 */
	private String					modelNo;
	/**
	 * Galaxy SKU no for key value
	 */
	private String					skuNo;

	/**
	 * @param attributeMap
	 *            the attributeMap to set
	 */
	public void setAttributeMap(Map<String, String> attributeMap) {

		this.attributeMap = attributeMap;
	}

	/**
	 * @return the attributeMap
	 */
	public Map<String, String> getAttributeMap() {

		return attributeMap;
	}

	/**
	 * @return the stepProductInformation
	 */
	public STEPProductInformation getStepProductInformation() {

		return stepProductInformation;
	}

	/**
	 * @param stepProductInformation
	 *            the stepProductInformation to set
	 */
	public void setStepProductInformation(STEPProductInformation stepProductInformation) {

		this.stepProductInformation = stepProductInformation;
	}

	/**
	 * @return the reportVO
	 */
	public ReportVO getReportVO() {

		return reportVO;
	}

	/**
	 * @param reportVO
	 *            the reportVO to set
	 */
	public void setReportVO(ReportVO reportVO) {

		this.reportVO = reportVO;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {

		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(String transactionType) {

		this.transactionType = transactionType;
	}

	/**
	 * @param vendorNo
	 *            the vendorNo to set
	 */
	/*
	 * public void setVendorNo(String vendorNo) { this.vendorNo = vendorNo; }
	 *//**
	 * @return the vendorNo
	 */
	/*
	 * public String getVendorNo() { return vendorNo; }
	 *//**
	 * @param modelNo
	 *            the modelNo to set
	 */
	/*
	 * public void setModelNo(String modelNo) { this.modelNo = modelNo; }
	 *//**
	 * @return the modelNo
	 */
	/*
	 * public String getModelNo() { return modelNo; }
	 */
	

	public String getChannel() {
	
		return channel;
	}

	
	public void setChannel(String channel) {
	
		this.channel = channel;
	}
	
	/**
	 * @param skuNo
	 *            the skuNo to set
	 */
	public String getSkuNo() {

		return skuNo;
	}

	/**
	 * @return the skuNo
	 */
	public void setSkuNo(String skuNo) {

		this.skuNo = skuNo;
	}

}
