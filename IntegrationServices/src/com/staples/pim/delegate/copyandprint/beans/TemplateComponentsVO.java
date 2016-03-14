
package com.staples.pim.delegate.copyandprint.beans;

public class TemplateComponentsVO {

	private String	templateId;
	private String	configAttrId;
	private String	configAttrValId;
	private String	defAttrValInd;
	private String	supressMsgInd;
	private String	customQtyPrompt;
	private String	customQtyMinValue;
	private String	customQtyMaxValue;
	private String	exceptionPageConfigAttrInd;
	private String	pageNumber;

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	private String	activeInd;

	/**
	 * @return the templateId
	 */
	public String getTemplateId() {

		return templateId;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(String templateId) {

		this.templateId = templateId;
	}

	/**
	 * @return the configAttrId
	 */
	public String getConfigAttrId() {

		return configAttrId;
	}

	/**
	 * @param configAttrId
	 *            the configAttrId to set
	 */
	public void setConfigAttrId(String configAttrId) {

		this.configAttrId = configAttrId;
	}

	/**
	 * @return the configAttrValId
	 */
	public String getConfigAttrValId() {

		return configAttrValId;
	}

	/**
	 * @param configAttrValId
	 *            the configAttrValId to set
	 */
	public void setConfigAttrValId(String configAttrValId) {

		this.configAttrValId = configAttrValId;
	}

	/**
	 * @return the defAttrValInd
	 */
	public String getDefAttrValInd() {

		return defAttrValInd;
	}

	/**
	 * @param defAttrValInd
	 *            the defAttrValInd to set
	 */
	public void setDefAttrValInd(String defAttrValInd) {

		this.defAttrValInd = defAttrValInd;
	}

	/**
	 * @return the supressMsgInd
	 */
	public String getSupressMsgInd() {

		return supressMsgInd;
	}

	/**
	 * @param supressMsgInd
	 *            the supressMsgInd to set
	 */
	public void setSupressMsgInd(String supressMsgInd) {

		this.supressMsgInd = supressMsgInd;
	}

	/**
	 * @return the customQtyPrompt
	 */
	public String getCustomQtyPrompt() {

		return customQtyPrompt;
	}

	/**
	 * @param customQtyPrompt
	 *            the customQtyPrompt to set
	 */
	public void setCustomQtyPrompt(String customQtyPrompt) {

		this.customQtyPrompt = customQtyPrompt;
	}

	/**
	 * @return the customQtyMinValue
	 */
	public String getCustomQtyMinValue() {

		return customQtyMinValue;
	}

	/**
	 * @param customQtyMinValue
	 *            the customQtyMinValue to set
	 */
	public void setCustomQtyMinValue(String customQtyMinValue) {

		this.customQtyMinValue = customQtyMinValue;
	}

	/**
	 * @return the customQtyMaxValue
	 */
	public String getCustomQtyMaxValue() {

		return customQtyMaxValue;
	}

	/**
	 * @param customQtyMaxValue
	 *            the customQtyMaxValue to set
	 */
	public void setCustomQtyMaxValue(String customQtyMaxValue) {

		this.customQtyMaxValue = customQtyMaxValue;
	}

	/**
	 * @return the exceptionPageConfigAttrInd
	 */
	public String getExceptionPageConfigAttrInd() {

		return exceptionPageConfigAttrInd;
	}

	/**
	 * @param exceptionPageConfigAttrInd
	 *            the exceptionPageConfigAttrInd to set
	 */
	public void setExceptionPageConfigAttrInd(String exceptionPageConfigAttrInd) {

		this.exceptionPageConfigAttrInd = exceptionPageConfigAttrInd;
	}

	/**
	 * @return the pageNumber
	 */
	public String getPageNumber() {

		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(String pageNumber) {

		this.pageNumber = pageNumber;
	}

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	/**
	 * @return the activeInd
	 */
	public String getActiveInd() {

		return activeInd;
	}

	/**
	 * @param activeInd
	 *            the activeInd to set
	 */
	public void setActiveInd(String activeInd) {

		this.activeInd = activeInd;
	}

}
