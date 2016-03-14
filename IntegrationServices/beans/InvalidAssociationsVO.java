package com.staples.pcm.springbatch.common.beans;

public class InvalidAssociationsVO {
	
	private String templateId;
	private String configAttrValId;
	private String invalidConfigAttrValId;
	private String exceptionPagesApplicable;
	
	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	/**
	 * @return the configAttrValId
	 */
	public String getConfigAttrValId() {
		return configAttrValId;
	}
	/**
	 * @param configAttrValId the configAttrValId to set
	 */
	public void setConfigAttrValId(String configAttrValId) {
		this.configAttrValId = configAttrValId;
	}
	
	/**
	 * @return the invalidConfigAttrValId
	 */
	public String getInvalidConfigAttrValId() {
		return invalidConfigAttrValId;
	}
	/**
	 * @param configAttrValId the configAttrValId to set
	 */
	public void setInvalidConfigAttrValId(String invalidConfigAttrValId) {
		this.invalidConfigAttrValId = invalidConfigAttrValId;
	}
	
	/**
	 * @return the exceptionPagesApplicable
	 */
	public String getExceptionPagesApplicable() {
		return exceptionPagesApplicable;
	}
	/**
	 * @param exceptionPagesApplicable the exceptionPagesApplicable to set
	 */
	public void setExceptionPagesApplicable(String exceptionPagesApplicable) {
		this.exceptionPagesApplicable = exceptionPagesApplicable;
	}

}
