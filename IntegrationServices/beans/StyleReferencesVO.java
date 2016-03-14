package com.staples.pcm.springbatch.common.beans;

/**
 * @author 843881
 *
 */
public class StyleReferencesVO {
	
	private String templateID;
	private String styleID;
	private String hierarchyID;
	
	public StyleReferencesVO() {
		
	}
	
	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateID;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateID = templateId;
	}
	
	/**
	 * @return the styleId
	 */
	public String getstyleID() {
		return styleID;
	}
	/**
	 * @param styleID the styleID to set
	 */
	public void setstyleID(String styleID) {
		this.styleID = styleID;
	}
	
	/**
	 * @return the hierarchyID
	 */
	public String gethierarchyID() {
		return hierarchyID;
	}
	/**
	 * @param hierarchyID the hierarchyID to set
	 */
	public void sethierarchyID(String hierarchyID) {
		this.hierarchyID = hierarchyID;
	}
	
}
