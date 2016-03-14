package com.staples.pcm.springbatch.common.beans;


/**
 * @author 843881
 * This bean class is used to set values for "Component Properties sheet" in Config Hierarchy template
 *
 */

public class ComponentPropertiesVO {
	
	private String configAttrValId;
	private String configAttrValName;
	private String propertyName;
	private String propertyValue;
	
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
	 * @return propertyName
	 */
	public String getPropertyName(){
		return propertyName;
	}
	/**
	 * @param propertyName
	 */
	public void setPropertyName(String propertyName){
		this.propertyName=propertyName;
	}
	
	/**
	 * @return propertyValue
	 */
	public String getPropertyValue(){
		return propertyValue;
	}
	/**
	 * @param propertyValue
	 */
	public void setPropertyValue(String propertyValue){
		this.propertyValue=propertyValue;
	}

}
