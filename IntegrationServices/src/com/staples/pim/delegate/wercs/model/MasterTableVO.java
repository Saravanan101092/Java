package com.staples.pim.delegate.wercs.model;

import java.sql.Date;


public class MasterTableVO {

	public String UPCNo;
	
	public MasterTableVO(){
		//needed or else the variable is initialized to 0.
		registrationStatus=-1;
	}
	public String getUPCNo() {
	
		return UPCNo;
	}
	
	public void setUPCNo(String uPCNo) {
	
		UPCNo = uPCNo;
	}
	
	public String getWercsID() {
	
		return wercsID;
	}
	
	public void setWercsID(String wercsID) {
	
		this.wercsID = wercsID;
	}
	
	public String getPipid() {
	
		return pipid;
	}
	
	public void setPipid(String pipid) {
	
		this.pipid = pipid;
	}
	
	public String getSkuno() {
	
		return skuno;
	}
	
	public void setSkuno(String skuno) {
	
		this.skuno = skuno;
	}
	
	public String getStepid() {
	
		return stepid;
	}
	
	public void setStepid(String stepid) {
	
		this.stepid = stepid;
	}
	
	public String getModelno() {
	
		return modelno;
	}
	
	public void setModelno(String modelno) {
	
		this.modelno = modelno;
	}
	
	public String getItemdesc() {
	
		return itemdesc;
	}
	
	public void setItemdesc(String itemdesc) {
	
		this.itemdesc = itemdesc;
	}
	
	public String getSupplierName() {
	
		return supplierName;
	}
	
	public void setSupplierName(String supplierName) {
	
		this.supplierName = supplierName;
	}
	
	public String getRequestorName() {
	
		return requestorName;
	}
	
	public void setRequestorName(String requestorName) {
	
		this.requestorName = requestorName;
	}
	
	public String getSupplierId() {
	
		return supplierId;
	}
	
	public void setSupplierId(String supplierMail) {
	
		this.supplierId = supplierMail;
	}
	
	public String getPsId() {
	
		return psId;
	}
	
	public void setPsId(String psmail) {
	
		this.psId = psmail;
	}
	
	public String getMerchantId() {
	
		return merchantId;
	}
	
	public void setMerchantId(String merchantMail) {
	
		this.merchantId = merchantMail;
	}
	
	public String getTranstype() {
	
		return transtype;
	}
	
	public void setTranstype(String transtype) {
	
		this.transtype = transtype;
	}
	
	public java.sql.Timestamp getCreatedDate() {
	
		return createdDate;
	}
	
	public void setCreatedDate(java.sql.Timestamp createdDate) {
	
		this.createdDate = createdDate;
	}
	
	public int getRegistrationStatus() {
	
		return registrationStatus;
	}
	
	public void setRegistrationStatus(int registrationStatus) {
	
		this.registrationStatus = registrationStatus;
	}
	
	public String getWercsTrigger() {
	
		return wercsTrigger;
	}
	
	public void setWercsTrigger(String wercsTrigger) {
	
		this.wercsTrigger = wercsTrigger;
	}
	
	public String getRegulatoryStatus() {
	
		return regulatoryStatus;
	}
	
	public void setRegulatoryStatus(String regulatoryStatus) {
	
		this.regulatoryStatus = regulatoryStatus;
	}
	public String wercsID;
	public String pipid;
	public String skuno;
	public String stepid;
	public String modelno;
	public String itemdesc;
	public String supplierName;
	public String requestorName;
	public String requestorID;
	public String supplierId;
	public String psId;
	public String merchantId;
	public String transtype;
	public java.sql.Timestamp createdDate;
	public int registrationStatus;
	public String wercsTrigger;
	public String regulatoryStatus;
	public String toner_Wholesaler;
	public int previousRegistrationStatus;
	public int eventID;
	public String stepExemptFlag;

	
	
	public String getStepExemptFlag() {
	
		return stepExemptFlag;
	}
	
	public void setStepExemptFlag(String stepExemptFlag) {
	
		this.stepExemptFlag = stepExemptFlag;
	}
	
	public int getEventID() {
	
		return eventID;
	}
	
	public void setEventID(int eventID) {
	
		this.eventID = eventID;
	}
	
	public int getPreviousRegistrationStatus() {
	
		return previousRegistrationStatus;
	}
	
	public void setPreviousRegistrationStatus(int previousRegistrationStatus) {
	
		this.previousRegistrationStatus = previousRegistrationStatus;
	}
	
	public String getToner_Wholesaler() {
	
		return toner_Wholesaler;
	}

	
	public void setToner_Wholesaler(String toner_Wholesaler) {
	
		this.toner_Wholesaler = toner_Wholesaler;
	}
	

	public String getRequestorID() {
	
		return requestorID;
	}
	
	public void setRequestorID(String requestorID) {
	
		this.requestorID = requestorID;
	}
}