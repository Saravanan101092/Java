package com.staples.pcm.springbatch.common.beans;


/**
 * @author 522462
 *
 */
public class TemplatesVO {
	
	private String templateId;
	private String templateSku;
	private String itemType;
	private String templateName;
	private String templateDesc;
	private String styleId;
	private String templateType;
	private String sellUom;
	private String sellUomQty;
	private String baseUom;
	private String orderQtyList;
	private String minOrdrQty;
	private String maxOrdrQty;
	private String paymentReqd;
	private String artWorkProvision;
	private String turnTimeRange;
	private String quickDelvryInd;
	private String productionNotes;
	private String activeInd;
	//Phase 2
	private String pricingStategy;
	private String isSaleableInd;
	private String exceptionPagesAllowedInd;
	
	
	public TemplatesVO(){
		
	}
	/**
	 * @param templateId
	 * @param templateSku
	 * @param itemType
	 * @param templateName
	 * @param templateDesc
	 * @param styleId
	 * @param templateType
	 * @param sellUom
	 * @param sellUomQty
	 * @param baseUom
	 * @param orderQtyList
	 * @param minOrdrQty
	 * @param maxOrdrQty
	 * @param paymentReqd
	 * @param artWorkProvision
	 * @param turnTimeRange
	 * @param quickDelvryInd
	 * @param productionNotes
	 * @param activeInd
	 */
	public TemplatesVO(String templateId, String templateSku, String itemType,
			String templateName, String templateDesc, String styleId,
			String templateType, String sellUom, String sellUomQty,
			String baseUom, String orderQtyList, String minOrdrQty,
			String maxOrdrQty, String paymentReqd, String artWorkProvision,
			String turnTimeRange, String quickDelvryInd,
			String productionNotes, String activeInd) {
		super();
		this.templateId = templateId;
		this.templateSku = templateSku;
		this.itemType = itemType;
		this.templateName = templateName;
		this.templateDesc = templateDesc;
		this.styleId = styleId;
		this.templateType = templateType;
		this.sellUom = sellUom;
		this.sellUomQty = sellUomQty;
		this.baseUom = baseUom;
		this.orderQtyList = orderQtyList;
		this.minOrdrQty = minOrdrQty;
		this.maxOrdrQty = maxOrdrQty;
		this.paymentReqd = paymentReqd;
		this.artWorkProvision = artWorkProvision;
		this.turnTimeRange = turnTimeRange;
		this.quickDelvryInd = quickDelvryInd;
		this.productionNotes = productionNotes;
		this.activeInd = activeInd;
	}
	
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
	 * @return the templateSku
	 */
	public String getTemplateSku() {
		return templateSku;
	}
	/**
	 * @param templateSku the templateSku to set
	 */
	public void setTemplateSku(String templateSku) {
		this.templateSku = templateSku;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	/**
	 * @return the templateDesc
	 */
	public String getTemplateDesc() {
		return templateDesc;
	}
	/**
	 * @param templateDesc the templateDesc to set
	 */
	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}
	/**
	 * @return the styleId
	 */
	public String getStyleId() {
		return styleId;
	}
	/**
	 * @param styleId the styleId to set
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}
	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	/**
	 * @return the sellUom
	 */
	public String getSellUom() {
		return sellUom;
	}
	/**
	 * @param sellUom the sellUom to set
	 */
	public void setSellUom(String sellUom) {
		this.sellUom = sellUom;
	}
	/**
	 * @return the sellUomQty
	 */
	public String getSellUomQty() {
		return sellUomQty;
	}
	/**
	 * @param sellUomQty the sellUomQty to set
	 */
	public void setSellUomQty(String sellUomQty) {
		this.sellUomQty = sellUomQty;
	}
	/**
	 * @return the baseUom
	 */
	public String getBaseUom() {
		return baseUom;
	}
	/**
	 * @param baseUom the baseUom to set
	 */
	public void setBaseUom(String baseUom) {
		this.baseUom = baseUom;
	}
	/**
	 * @return the orderQtyList
	 */
	public String getOrderQtyList() {
		return orderQtyList;
	}
	/**
	 * @param orderQtyList the orderQtyList to set
	 */
	public void setOrderQtyList(String orderQtyList) {
		this.orderQtyList = orderQtyList;
	}
	/**
	 * @return the minOrdrQty
	 */
	public String getMinOrdrQty() {
		return minOrdrQty;
	}
	/**
	 * @param minOrdrQty the minOrdrQty to set
	 */
	public void setMinOrdrQty(String minOrdrQty) {
		this.minOrdrQty = minOrdrQty;
	}
	/**
	 * @return the maxOrdrQty
	 */
	public String getMaxOrdrQty() {
		return maxOrdrQty;
	}
	/**
	 * @param maxOrdrQty the maxOrdrQty to set
	 */
	public void setMaxOrdrQty(String maxOrdrQty) {
		this.maxOrdrQty = maxOrdrQty;
	}
	/**
	 * @return the paymentReqd
	 */
	public String getPaymentReqd() {
		return paymentReqd;
	}
	/**
	 * @param paymentReqd the paymentReqd to set
	 */
	public void setPaymentReqd(String paymentReqd) {
		this.paymentReqd = paymentReqd;
	}
	/**
	 * @return the artWorkProvision
	 */
	public String getArtWorkProvision() {
		return artWorkProvision;
	}
	/**
	 * @param artWorkProvision the artWorkProvision to set
	 */
	public void setArtWorkProvision(String artWorkProvision) {
		this.artWorkProvision = artWorkProvision;
	}
	/**
	 * @return the turnTimeRange
	 */
	public String getTurnTimeRange() {
		return turnTimeRange;
	}
	/**
	 * @param turnTimeRange the turnTimeRange to set
	 */
	public void setTurnTimeRange(String turnTimeRange) {
		this.turnTimeRange = turnTimeRange;
	}
	/**
	 * @return the quickDelvryInd
	 */
	public String getQuickDelvryInd() {
		return quickDelvryInd;
	}
	/**
	 * @param quickDelvryInd the quickDelvryInd to set
	 */
	public void setQuickDelvryInd(String quickDelvryInd) {
		this.quickDelvryInd = quickDelvryInd;
	}
	/**
	 * @return the productionNotes
	 */
	public String getProductionNotes() {
		return productionNotes;
	}
	/**
	 * @param productionNotes the productionNotes to set
	 */
	public void setProductionNotes(String productionNotes) {
		this.productionNotes = productionNotes;
	}
	/**
	 * @return the activeInd
	 */
	public String getActiveInd() {
		
		return activeInd;
	}
	/**
	 * @param activeInd the activeInd to set
	 */
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}
	
	//Phase 2
	/**
	 * @return the pricingStategy
	 */
	public String getPricingStategy() {
		return pricingStategy;
	}
	/**
	 * @param pricingStategy the pricingStategy to set
	 */
	public void setPricingStategy(String pricingStategy) {
		this.pricingStategy = pricingStategy;
	}
	/**
	 * @return the isSaleableInd
	 */
	public String getIsSaleableInd() {
		
		return isSaleableInd;
	}
	/**
	 * @param isSaleableInd the isSaleableInd to set
	 */
	public void setIsSaleableInd(String isSaleableInd) {
		this.isSaleableInd = isSaleableInd;
	}
	
	/**
	 * @return the exceptionPagesAllowedInd
	 */
	public String getExceptionPagesAllowedInd() {
		
		return exceptionPagesAllowedInd;
	}
	/**
	 * @param exceptionPagesAllowedInd the exceptionPagesAllowedInd to set
	 */
	public void setExceptionPagesAllowedInd(String exceptionPagesAllowedInd) {
		this.exceptionPagesAllowedInd = exceptionPagesAllowedInd;
	}
	
}
