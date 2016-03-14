
package com.staples.pim.delegate.wholesalers.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.staples.pim.delegate.datamigration.utils.AttributesConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.wholesalers.common.Util;

public class WholesalerDotcomFeedBean implements AttributesConstants {

	private String	actionType;
	private String	wholesalerID;
	private String	wholesalerItemNo;
	private String	description;
	private String	staplesSKUNo;
	private String	genericSKUNo;
	private String	upcCode;
	private String	vendorName;
	private String	mfgPartNo;
	private String	productClassCode;
	private String	productNumber;
	private String	categoryNumbers;
	private String	inventoryUnit;
	private String	boxPackQty;
	private String	boxPackUnit;
	private String	cartonPackQty;
	private String	cartonPackUnit;
	private String	cartonWeight;
	private String	cartonLength;
	private String	cartonWidth;
	private String	cartonHeight;
	private String	listQty;
	private String	listUnit;
	private String	listPrice;
	private String	listPriceEffDate;
	private String	unitCost;
	private String	costEffDate;
	private String	commissioningClass;
	private String	catalogPage;
	private String	upsableFlag;
	private String	assemblyRequired;
	private String	furnitureServicesAllowed;
	private String	longDescription;
	private String	dotcomNetPrice;
	private String	netPrice;
	private String	itemRefCode;
	private String	referenceItem;
	private String	rawWholesalerNo;
	private String	skuPartType;
	private String	manFurnFlag2;
	private String	invSearchClass;
	private String	assemblyFee;
	private String	uncrateReqFlag;
	private String	uncratingFee;
	private String	qtlyUpdateFlag;

	public String getActionType() {

		return actionType;
	}

	public void setActionType(String actionType) {

		this.actionType = actionType;
	}

	public String getWholesalerID() {

		return wholesalerID;
	}

	public void setWholesalerID(String wholesalerID) {

		this.wholesalerID = wholesalerID;
	}

	public String getWholesalerItemNo() {

		return wholesalerItemNo;
	}

	public void setWholesalerItemNo(String wholesalerItemNo) {

		this.wholesalerItemNo = wholesalerItemNo;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getStaplesSKUNo() {

		return ignoreZero(staplesSKUNo);
	}

	public void setStaplesSKUNo(String staplesSKUNo) {

		this.staplesSKUNo = staplesSKUNo;
	}

	public String getGenericSKUNo() {

		return genericSKUNo;
	}

	public void setGenericSKUNo(String genericSKUNo) {

		this.genericSKUNo = genericSKUNo;
	}

	public String getUpcCode() {

		return upcCode;
	}

	public void setUpcCode(String upcCode) {

		this.upcCode = upcCode;
	}

	public String getVendorName() {

		return vendorName;
	}

	public void setVendorName(String vendorName) {

		this.vendorName = vendorName;
	}

	public String getMfgPartNo() {

		return mfgPartNo;
	}

	public void setMfgPartNo(String mfgPartNo) {

		this.mfgPartNo = mfgPartNo;
	}

	public String getProductClassCode() {

		return productClassCode;
	}

	public void setProductClassCode(String productClassCode) {

		this.productClassCode = productClassCode;
	}

	public String getProductNumber() {

		return productNumber;
	}

	public void setProductNumber(String productNumber) {

		this.productNumber = productNumber;
	}

	public String getCategoryNumbers() {

		return categoryNumbers;
	}

	public void setCategoryNumbers(String categoryNumbers) {

		this.categoryNumbers = categoryNumbers;
	}

	public String getInventoryUnit() {

		return Util.getInstance().getWholesalerSupplierDetails(A0212, inventoryUnit);
	}

	public void setInventoryUnit(String inventoryUnit) {

		this.inventoryUnit = inventoryUnit;
	}

	public String getBoxPackQty() {

		return boxPackQty;
	}

	public void setBoxPackQty(String boxPackQty) {

		this.boxPackQty = boxPackQty;
	}

	public String getBoxPackUnit() {

		return Util.getInstance().getWholesalerSupplierDetails(A0586, boxPackUnit);
	}

	public void setBoxPackUnit(String boxPackUnit) {

		this.boxPackUnit = boxPackUnit;
	}

	public String getCartonPackQty() {

		return cartonPackQty;
	}

	public void setCartonPackQty(String cartonPackQty) {

		this.cartonPackQty = cartonPackQty;
	}

	public String getCartonPackUnit() {

		return Util.getInstance().getWholesalerSupplierDetails(A0588, cartonPackUnit);
	}

	public void setCartonPackUnit(String cartonPackUnit) {

		this.cartonPackUnit = cartonPackUnit;
	}

	public String getCartonWeight() {

		return cartonWeight;
	}

	public void setCartonWeight(String cartonWeight) {

		this.cartonWeight = cartonWeight;
	}

	public String getCartonLength() {

		return cartonLength;
	}

	public void setCartonLength(String cartonLength) {

		this.cartonLength = cartonLength;
	}

	public String getCartonWidth() {

		return cartonWidth;
	}

	public void setCartonWidth(String cartonWidth) {

		this.cartonWidth = cartonWidth;
	}

	public String getCartonHeight() {

		return cartonHeight;
	}

	public void setCartonHeight(String cartonHeight) {

		this.cartonHeight = cartonHeight;
	}

	public String getListQty() {

		return listQty;
	}

	public void setListQty(String listQty) {

		this.listQty = listQty;
	}

	public String getListUnit() {

		return Util.getInstance().getWholesalerSupplierDetails(A0210, listUnit);
	}

	public void setListUnit(String listUnit) {

		this.listUnit = listUnit;
	}

	public String getListPrice() {

		return listPrice;
	}

	public void setListPrice(String listPrice) {

		this.listPrice = listPrice;
	}

	public String getListPriceEffDate() {

		return getFormattedDate(ignoreZero(listPriceEffDate));
	}

	public void setListPriceEffDate(String listPriceEffDate) {

		this.listPriceEffDate = listPriceEffDate;
	}

	public String getUnitCost() {

		return unitCost;
	}

	public void setUnitCost(String unitCost) {

		this.unitCost = unitCost;
	}

	public String getCostEffDate() {

		return getFormattedDate(ignoreZero(costEffDate));
	}

	public void setCostEffDate(String costEffDate) {

		this.costEffDate = costEffDate;
	}

	public String getCommissioningClass() {

		return commissioningClass;
	}

	public void setCommissioningClass(String commissioningClass) {

		this.commissioningClass = commissioningClass;
	}

	public String getCatalogPage() {

		return catalogPage;
	}

	public void setCatalogPage(String catalogPage) {

		this.catalogPage = catalogPage;
	}

	public String getUpsableFlag() {

		String transUpsableFlag = StringUtils.EMPTY;
		if ("G".equalsIgnoreCase(upsableFlag) || "X".equalsIgnoreCase(upsableFlag) || "Y".equalsIgnoreCase(upsableFlag)) {
			transUpsableFlag = "Y";
		} else if ("N".equalsIgnoreCase(upsableFlag)) {
			transUpsableFlag = "N";
		}
		return transUpsableFlag;
	}

	public void setUpsableFlag(String upsableFlag) {

		this.upsableFlag = upsableFlag;
	}

	public String getAssemblyRequired() {

		String transAssemblyRequired = StringUtils.EMPTY;
		if ("G".equalsIgnoreCase(assemblyRequired) || "X".equalsIgnoreCase(assemblyRequired) || "Y".equalsIgnoreCase(assemblyRequired)) {
			transAssemblyRequired = "Y";
		} else if ("N".equalsIgnoreCase(assemblyRequired)) {
			transAssemblyRequired = "N";
		}
		return transAssemblyRequired;
	}

	public void setAssemblyRequired(String assemblyRequired) {

		this.assemblyRequired = assemblyRequired;
	}

	public String getFurnitureServicesAllowed() {

		String transFurnitureServicesAllowed = StringUtils.EMPTY;
		if ("X".equalsIgnoreCase(furnitureServicesAllowed) || "Y".equalsIgnoreCase(furnitureServicesAllowed)) {
			transFurnitureServicesAllowed = "Y";
		} else if ("N".equalsIgnoreCase(furnitureServicesAllowed)) {
			transFurnitureServicesAllowed = "N";
		}
		return transFurnitureServicesAllowed;
	}

	public void setFurnitureServicesAllowed(String furnitureServicesAllowed) {

		this.furnitureServicesAllowed = furnitureServicesAllowed;
	}

	public String getLongDescription() {

		return longDescription;
	}

	public void setLongDescription(String longDescription) {

		this.longDescription = longDescription;
	}

	public String getDotcomNetPrice() {

		return dotcomNetPrice;
	}

	public void setDotcomNetPrice(String dotcomNetPrice) {

		this.dotcomNetPrice = dotcomNetPrice;
	}

	public String getNetPrice() {

		return netPrice;
	}

	public void setNetPrice(String netPrice) {

		this.netPrice = netPrice;
	}

	public String getItemRefCode() {

		return itemRefCode;
	}

	public void setItemRefCode(String itemRefCode) {

		this.itemRefCode = itemRefCode;
	}

	public String getReferenceItem() {

		return referenceItem;
	}

	public void setReferenceItem(String referenceItem) {

		this.referenceItem = referenceItem;
	}

	public String getRawWholesalerNo() {

		return rawWholesalerNo;
	}

	public void setRawWholesalerNo(String rawWholesalerNo) {

		this.rawWholesalerNo = rawWholesalerNo;
	}

	public String getSkuPartType() {

		return skuPartType;
	}

	public void setSkuPartType(String skuPartType) {

		this.skuPartType = skuPartType;
	}

	public String getManFurnFlag2() {

		return manFurnFlag2;
	}

	public void setManFurnFlag2(String manFurnFlag2) {

		this.manFurnFlag2 = manFurnFlag2;
	}

	public String getInvSearchClass() {

		return invSearchClass;
	}

	public void setInvSearchClass(String invSearchClass) {

		this.invSearchClass = invSearchClass;
	}

	public String getAssemblyFee() {

		return assemblyFee;
	}

	public void setAssemblyFee(String assemblyFee) {

		this.assemblyFee = assemblyFee;
	}

	public String getUncrateReqFlag() {

		return uncrateReqFlag;
	}

	public void setUncrateReqFlag(String uncrateReqFlag) {

		this.uncrateReqFlag = uncrateReqFlag;
	}

	public String getUncratingFee() {

		return uncratingFee;
	}

	public void setUncratingFee(String uncratingFee) {

		this.uncratingFee = uncratingFee;
	}

	public String getQtlyUpdateFlag() {

		return qtlyUpdateFlag;
	}

	public void setQtlyUpdateFlag(String qtlyUpdateFlag) {

		this.qtlyUpdateFlag = qtlyUpdateFlag;
	}

	public Map<String, String> getItemAttributeValues() {

		Map<String, String> map = new HashMap<String, String>();

		map.put(A0579, wholesalerID);
		map.put(A0580, wholesalerItemNo);
		map.put(A0076_Dotcom, description);
		map.put(A0581, genericSKUNo);
		map.put(A0123, mfgPartNo);
		map.put(A0582, productClassCode);
		map.put(A0583, productNumber);
		map.put(A0584, categoryNumbers);
		map.put(A0212, getInventoryUnit());
		map.put(A0585, boxPackQty);
		map.put(A0586, getBoxPackUnit());
		map.put(A0587, cartonPackQty);
		map.put(A0588, getCartonPackUnit());
		map.put(A0589, cartonWeight);
		map.put(A0590, cartonLength);
		map.put(A0591, cartonWidth);
		map.put(A0592, cartonHeight);
		map.put(A0211, listQty);
		map.put(A0210, getListUnit());
		map.put(A0077_DotCom, listPrice);
		map.put(A0594, getListPriceEffDate());
		map.put(A0078_DotCom, unitCost);
		map.put(A0596, getCostEffDate());
		map.put(A0597, commissioningClass);
		map.put(A0598, catalogPage);
		map.put(A0490, getUpsableFlag());
		map.put(A0337, getAssemblyRequired());
		map.put(A0477, getFurnitureServicesAllowed());
		map.put(A0620_Dotcom, longDescription);
		map.put(A0601, dotcomNetPrice);
		map.put(A0602, netPrice);
		map.put(A0603, itemRefCode);
		map.put(A0604, referenceItem);
		map.put(A0605, rawWholesalerNo);
		map.put(A0606, skuPartType);
		map.put(A0478, manFurnFlag2);
		map.put(A0607, invSearchClass);
		map.put(A0480, assemblyFee);
		map.put(A0481, uncrateReqFlag);
		map.put(A0482, uncratingFee);
		map.put(A0608, qtlyUpdateFlag);
		map.put(A0615, "EA:Each");
		map.put(A0616, "EA:Each");
		map.put(A0002, "Standard");
		map.put(A0537, "Wholesaler");
		map.put(A0122, vendorName);
		map.put(A0501, isActiveDotCom());

		map.put(A0018_NAD, "REFER VENDOR DESCRIPTION-A0076");
		map.put(A0018_RET, "REFER VENDOR DESCRIPTION-A0076");

		return map;
	}

	public Map<String, String> getSKUAttributeValues() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(A0012, ignoreZero(staplesSKUNo));
		return map;
	}

	public Map<String, String> getVendorAttributeValues() {

		Map<String, String> map = new HashMap<String, String>();
		map.put(A0080, getNAString(ignoreZero(upcCode)));
		map.put(A0082, getNAString(DatamigrationAppConstants.EMPTY_STR));
		map.put(A0083, getNAString(DatamigrationAppConstants.EMPTY_STR));
		map.put(A0084, getNAString(DatamigrationAppConstants.EMPTY_STR));
		map.put(A0302, getNAString(getWholesalerSupplierDetails().getSupplierName()));
		map.put(A0075_NAD, getNAString(getWholesalerSupplierDetails().getSupplierId()));
		map.put(A0075_RET, getNAString(getWholesalerSupplierDetails().getSupplierId()));

		return map;
	}

	private String getNAString(String str) {

		return (str != null && !DatamigrationAppConstants.EMPTY_STR.equals(str)) ? str : DatamigrationAppConstants.NA_STR;
	}

	private String ignoreZero(String str) {

		return (str != null && !"0".equalsIgnoreCase(str)) ? str : DatamigrationAppConstants.EMPTY_STR;
	}

	/**
	 * 
	 * @param prefix
	 * @return
	 */
	public String getVendorNameForSTEPxml() {

		return getWholesalerSupplierDetails().getSupplierId() + "-" + this.getWholesalerID() + "-" + this.getWholesalerItemNo();
	}

	/**
	 * 
	 * @param prefix
	 * @return
	 */
	public String getItemNameForSTEPxml() {

		return this.getWholesalerID() + "-" + this.getWholesalerItemNo();

	}

	/**
	 * Read from configuration file and return WholesalerSupplierBean
	 * 
	 * @return
	 */
	public WholesalerSupplierBean getWholesalerSupplierDetails() {

		WholesalerSupplierBean bean = new WholesalerSupplierBean();

		String supplierString = new String(" : : : ");
		String tempSupplierString = Util.getInstance().getWholesalerSupplierDetails("WholesalerSupplierDetails", this.getWholesalerID());

		supplierString = (tempSupplierString != null && tempSupplierString.length() > 0) ? tempSupplierString : supplierString;

		bean.setVendorName(this.getWholesalerID());
		bean.setSupplierId(getNAString(supplierString.split(":")[1].trim()));
		bean.setSupplierName(getNAString(supplierString.split(":")[2].trim()));
		bean.setSupplierMail(getNAString(supplierString.split(":")[3].trim()));

		return bean;

	}

	private String getFormattedDate(String inputString) {

		String outputDateString = StringUtils.EMPTY;
		if (inputString != null && inputString.length() > 0) {
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = inputFormat.parse(inputString);
				outputDateString = outputFormat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return outputDateString;
	}

	private String isActiveDotCom() {

		String isActiveDotCom = StringUtils.EMPTY;

		if (actionType != null && ("A".equalsIgnoreCase(actionType) || "U".equalsIgnoreCase(actionType))) {
			isActiveDotCom = "Y";
		} else if (actionType != null && "D".equalsIgnoreCase(actionType)) {
			isActiveDotCom = "N";
		}
		return isActiveDotCom;
	}
}