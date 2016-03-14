
package com.staples.pim.delegate.copyandprint.beans;

/**
 * @author 522462
 * 
 */
public class ConfigHierarchyVO {

	private String	detailId;
	private String	detailName;
	private String	detailDesc;
	private String	dispSeq;
	private String	activeInd;
	private String	dispInd;
	private String	hasCostInd;
	private String	customQtyInd;
	private Integer	levelId;
	private String	levelName;
	private String	detailParentId;
	private String	cnpLifeCycle;
	private String	productTagId;
	// phase 2
	private String	isDCS;
	// Phase 2
	// private String weight;
	// private String brightness;
	// private String mediaColor;
	// private String targetedAttribute;
	// private String upsellTitle;
	// private String upsellDescription;

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	private String	customInputFormat;
	private String	measurementScope;
	private String	quantityMultiplier;
	private String	quantityDivider;
	private String	baseUOM;
	private String	customInputPrompt;
	private String	dcsOnlyIndicator;
	private String	thirdPartyOnlyIndicator;

	public ConfigHierarchyVO() {

	}

	/**
	 * @param detailId
	 * @param detailName
	 * @param detailDesc
	 * @param dispSeq
	 * @param activeInd
	 * @param dispInd
	 * @param hasCostInd
	 * @param customQtyInd
	 * @param levelId
	 * @param levelName
	 * @param detailParentId
	 */
	public ConfigHierarchyVO(String detailId, String detailName, String detailDesc, String dispSeq, String activeInd, String dispInd,
			String hasCostInd, String customQtyInd, Integer levelId, String levelName, String detailParentId) {

		super();
		this.detailId = detailId;
		this.detailName = detailName;
		this.detailDesc = detailDesc;
		this.dispSeq = dispSeq;
		this.activeInd = activeInd;
		this.dispInd = dispInd;
		this.hasCostInd = hasCostInd;
		this.customQtyInd = customQtyInd;
		this.levelId = levelId;
		this.levelName = levelName;
		this.detailParentId = detailParentId;
	}

	/**
	 * @return the detailId
	 */
	public String getDetailId() {

		return detailId;
	}

	/**
	 * @param detailId
	 *            the detailId to set
	 */
	public void setDetailId(String detailId) {

		this.detailId = detailId;
	}

	/**
	 * @return the detailName
	 */
	public String getDetailName() {

		return detailName;
	}

	/**
	 * @param detailName
	 *            the detailName to set
	 */
	public void setDetailName(String detailName) {

		this.detailName = detailName;
	}

	/**
	 * @return the detailDesc
	 */
	public String getDetailDesc() {

		return detailDesc;
	}

	/**
	 * @param detailDesc
	 *            the detailDesc to set
	 */
	public void setDetailDesc(String detailDesc) {

		this.detailDesc = detailDesc;
	}

	/**
	 * @return the dispSeq
	 */
	public String getDispSeq() {

		return dispSeq;
	}

	/**
	 * @param dispSeq
	 *            the dispSeq to set
	 */
	public void setDispSeq(String dispSeq) {

		this.dispSeq = dispSeq;
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
	 * @return the deleteInd
	 */
	public String getDispInd() {

		return dispInd;
	}

	/**
	 * @param dispInd
	 *            the dispInd to set
	 */
	public void setDispInd(String dispInd) {

		this.dispInd = dispInd;
	}

	/**
	 * @return the hasCostInd
	 */
	public String getHasCostInd() {

		return hasCostInd;
	}

	/**
	 * @param hasCostInd
	 *            the hasCostInd to set
	 */
	public void setHasCostInd(String hasCostInd) {

		this.hasCostInd = hasCostInd;
	}

	/**
	 * @return the customQtyInd
	 */
	public String getCustomQtyInd() {

		return customQtyInd;
	}

	/**
	 * @param customQtyInd
	 *            the customQtyInd to set
	 */
	public void setCustomQtyInd(String customQtyInd) {

		this.customQtyInd = customQtyInd;
	}

	/**
	 * @return the levelId
	 */
	public Integer getLevelId() {

		return levelId;
	}

	/**
	 * @param levelId
	 *            the levelId to set
	 */
	public void setLevelId(Integer levelId) {

		this.levelId = levelId;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {

		return levelName;
	}

	/**
	 * @param levelName
	 *            the levelName to set
	 */
	public void setLevelName(String levelName) {

		this.levelName = levelName;
	}

	/**
	 * @return the detailParentId
	 */
	public String getDetailParentId() {

		return detailParentId;
	}

	/**
	 * @param detailParentId
	 *            the detailParentId to set
	 */
	public void setDetailParentId(String detailParentId) {

		this.detailParentId = detailParentId;
	}

	/**
	 * @return the cnpLifeCycle
	 */
	public String getCnpLifeCycle() {

		return cnpLifeCycle;
	}

	/**
	 * @param cnpLifeCycle
	 *            the cnpLifeCycle to set
	 */
	public void setCnpLifeCycle(String cnpLifeCycle) {

		this.cnpLifeCycle = cnpLifeCycle;
	}

	/**
	 * @return the productTagId
	 */
	public String getProductTagId() {

		return productTagId;
	}

	/**
	 * @param productTagId
	 *            the productTagId to set
	 */
	public void setProductTagId(String productTagId) {

		this.productTagId = productTagId;
	}

	// Phase 2
	/**
	 * @return the isDCS
	 */
	public String getIsDCS() {

		return isDCS;
	}

	/**
	 * @param isDCS
	 *            the isDCS to set
	 */
	public void setIsDCS(String isDCS) {

		this.isDCS = isDCS;
	}
	// Phase 2
	// /**
	// * @return weight
	// */
	// public String getWeight(){
	// return weight;
	// }
	// /**
	// * @param weight
	// */
	// public void setWeight(String weight){
	// this.weight=weight;
	// }
	//	
	// /**
	// * @return brightness
	// */
	// public String getBrightness(){
	// return brightness;
	// }
	// /**
	// * @param brightness
	// */
	// public void setBrightness(String brightness){
	// this.brightness=brightness;
	// }
	//
	// /**
	// * @return mediaColor
	// */
	// public String getMediaColor(){
	// return mediaColor;
	// }
	// /**
	// * @param mediaColor
	// */
	// public void setMediaColor(String mediaColor){
	// this.mediaColor=mediaColor;
	// }

	// /**
	// * @return targetedAttribute
	// */
	// public String getTargetedAttribute(){
	// return targetedAttribute;
	// }
	// /**
	// * @param targetedAttribute
	// */
	// public void setTargetedAttribute(String targetedAttribute){
	// this.targetedAttribute=targetedAttribute;
	// }
	//
	// /**
	// * @return upsellTitle
	// */
	// public String getUpsellTitle(){
	// return upsellTitle;
	// }
	// /**
	// * @param upsellTitle
	// */
	// public void setUpsellTitle(String upsellTitle){
	// this.upsellTitle=upsellTitle;
	// }
	//
	// /**
	// * @return upsellDescription
	// */
	// public String getUpsellDescription(){
	// return upsellDescription;
	// }
	// /**
	// * @param upsellDescription
	// */
	// public void setUpsellDescription(String upsellDescription){
	// this.upsellDescription=upsellDescription;
	// }

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	/**
	 * @return the customInputFormat
	 */
	public String getCustomInputFormat() {

		return customInputFormat;
	}

	/**
	 * @param customInputFormat
	 *            the customInputFormat to set
	 */
	public void setCustomInputFormat(String customInputFormat) {

		this.customInputFormat = customInputFormat;
	}

	/**
	 * @return the measurementScope
	 */
	public String getMeasurementScope() {

		return measurementScope;
	}

	/**
	 * @param measurementScope
	 *            the measurementScope to set
	 */
	public void setMeasurementScope(String measurementScope) {

		this.measurementScope = measurementScope;
	}

	/**
	 * @return the quantityMultiplier
	 */
	public String getQuantityMultiplier() {

		return quantityMultiplier;
	}

	/**
	 * @param quantityMultiplier
	 *            the quantityMultiplier to set
	 */
	public void setQuantityMultiplier(String quantityMultiplier) {

		this.quantityMultiplier = quantityMultiplier;
	}

	/**
	 * @return the quantityDivider
	 */
	public String getQuantityDivider() {

		return quantityDivider;
	}

	/**
	 * @param quantityDivider
	 *            the quantityDivider to set
	 */
	public void setQuantityDivider(String quantityDivider) {

		this.quantityDivider = quantityDivider;
	}

	/**
	 * @return the baseUOM
	 */
	public String getBaseUOM() {

		return baseUOM;
	}

	/**
	 * @param baseUOM
	 *            the baseUOM to set
	 */
	public void setBaseUOM(String baseUOM) {

		this.baseUOM = baseUOM;
	}

	/**
	 * @return the customInputPrompt
	 */
	public String getCustomInputPrompt() {

		return customInputPrompt;
	}

	/**
	 * @param customInputPrompt
	 *            the customInputPrompt to set
	 */
	public void setCustomInputPrompt(String customInputPrompt) {

		this.customInputPrompt = customInputPrompt;
	}

	/**
	 * @return the dcsOnlyIndicator
	 */
	public String getDcsOnlyIndicator() {

		return dcsOnlyIndicator;
	}

	/**
	 * @param dcsOnlyIndicator
	 *            the dcsOnlyIndicator to set
	 */
	public void setDcsOnlyIndicator(String dcsOnlyIndicator) {

		this.dcsOnlyIndicator = dcsOnlyIndicator;
	}

	/**
	 * @return the thirdPartyOnlyIndicator
	 */
	public String getThirdPartyOnlyIndicator() {

		return thirdPartyOnlyIndicator;
	}

	/**
	 * @param thirdPartyOnlyIndicator
	 *            the thirdPartyOnlyIndicator to set
	 */
	public void setThirdPartyOnlyIndicator(String thirdPartyOnlyIndicator) {

		this.thirdPartyOnlyIndicator = thirdPartyOnlyIndicator;
	}

}
