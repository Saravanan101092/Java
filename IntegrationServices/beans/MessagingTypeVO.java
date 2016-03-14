package com.staples.pcm.springbatch.common.beans;

/**
 * @author 843881
 * This bean class is used to set values for "MessagingTypr sheet" in Config Hierarchy template
 */


public class MessagingTypeVO {
	
	private String configAttrValId;
	private String configAttrValName;
	private String messageType;
	private String targetedValId;
	private String targetedValName;
	private String msgTitle;
	private String msgDescription;
	
	
	/**
	 * @return configAttrValId
	 */
	public String getConfigAttrValId(){
		return configAttrValId;
	}
	/**
	 * @param configAttrValId
	 */
	public void setConfigAttrValId(String configAttrValId){
		this.configAttrValId=configAttrValId;
	}
	
	/**
	 * @return configAttrValName
	 */
	public String getConfigAttrValName(){
		return configAttrValName;
	}
	/**
	 * @param configAttrValName
	 */
	public void setConfigAttrValName(String configAttrValName){
		this.configAttrValName=configAttrValName;
	}
	/**
	 * @return messageType
	 */
	public String getMessageType(){
		return messageType;
	}
	/**
	 * @param messageType
	 */
	public void setMessageType(String messageType){
		this.messageType=messageType;
	}
	/**
	 * @return targetedValId
	 */
	public String getTargetedValId(){
		return targetedValId;
	}
	/**
	 * @param targetedValId
	 */
	public void setTargetedValId(String targetedValId){
		this.targetedValId=targetedValId;
	}
	
	/**
	 * @return targetedValName
	 */
	public String getTargetedValName(){
		return targetedValName;
	}
	/**
	 * @param targetedValName
	 */
	public void setTargetedValName(String targetedValName){
		this.targetedValName=targetedValName;
	}
	/**
	 * @return msgTitle
	 */
	public String getMsgTitle(){
		return msgTitle;
	}
	/**
	 * @param msgTitle
	 */
	public void setmsgTitle(String msgTitle){
		this.msgTitle=msgTitle;
	}
	
	/**
	 * @return msgDescription
	 */
	public String getMsgDescription(){
		return msgDescription;
	}
	/**
	 * @param msgDescription
	 */
	public void setMsgDescription(String msgDescription){
		this.msgDescription=msgDescription;
	}


}
