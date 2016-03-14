
package com.staples.pim.delegate.copyandprint.beans;

public class KitContentsVO {

	private String	templateID;
	private String	kitContainedSku;
	private String	activeInd;
	private String	kitContainedSKUQty;

	public KitContentsVO() {

	}

	/**
	 * @return the templateId
	 */
	public String getTemplateId() {

		return templateID;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(String templateID) {

		this.templateID = templateID;
	}

	/**
	 * @return the kitContainedSku
	 */
	public String getKitContainedSku() {

		return kitContainedSku;
	}

	/**
	 * @param kitContainedSku
	 *            the kitContainedSku to set
	 */
	public void setKitContainedSku(String kitContainedSku) {

		this.kitContainedSku = kitContainedSku;
	}

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

	/**
	 * @return the kitContainedSKUQty
	 */
	public String getKitContainedSKUQty() {

		return kitContainedSKUQty;
	}

	/**
	 * @param kitContainedSKUQty
	 *            the kitContainedSKUQty to set
	 */
	public void setKitContainedSKUQty(String kitContainedSKUQty) {

		this.kitContainedSKUQty = kitContainedSKUQty;
	}
}
