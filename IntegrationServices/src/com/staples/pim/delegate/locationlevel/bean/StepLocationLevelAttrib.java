/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     	:   StepLocationLevelAttrib
 * Creation Date 	:   StepLocationLevelBean.java
 * @author  	 	:	Sima Zaslavsky
 * @version 1.0
 */ 

package com.staples.pim.delegate.locationlevel.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepLocationLevelAttrib {

	
	private Map<Object, Object> funcSpecVars = null;	
	
	private int skuNumber = 0;//A0012
	private String channel = null;//derived from Attribute ID
	private String usecase = "NOT_APPLICABLE";// default "NOT_APPLICABLE"; maybe "REACTIVATION","SKU_CONVERSION","ADD_SKU_TO_RETAIL"
	private String vendorModelNumber = "";//A0013
	private String dsdFlag = "";//A0023
	private String retailIA_Rebuyer = "";//A0028
	private String retailProductManager = "";//A0029
	private String InventoryGroup = "";//A0031
	private String palletTi = "";//A0045
	private String palletHi = "";//A0046
	private String whsp = "";//A0065
	private String sucp = "";//A0067
	private String masterCasePackLength = ""; //A0068
	private String masterCasePackWidth = "";//A0069
	private String masterCasePackHeight = "";//A0070
	private String masterCasePackWeight = "";//A0071
	private String merchStatus = "";//A0072
	private String src = "";//A0073
	private String dcPurchaseFlag = "";
	private String vendorNumber = "";
	private String listPrice = "";
	private String sellUnitLength = "";
	private String sellUnitWidth = "";
	private String sellUnitHeight = "";
	private String sellUnitWeight = "";
	private String purchaseCaseWeight = "";
	private String dcHoldFlag = "";
	private String dcFlow = "";
	private String storeSourceWarehouse = "";
	private String storeLocationsActivate = "";
	private String storeLocationsInactivate = "";
	private String addRemoveSWO = "";
	private String purchaseCaseCube = "";
	private String discontinuedDbmDbs = "";
	private String specificStore = "";
	private String dropShipFC = "";
	private String fcPurchaseFlag  = ""; 
	private String upcNumber = "";
	private String upcFlag = "";
	private String targetMax = "";//"N"
	private String stateCode = "";
	private String storeOnly = "N";
	private String warehouseOnly = "N";
	private String addOnAllowed = "";
	private String distributionPack = "";
	private String distributionPolicyCode = "";
	private String velocityCode = "";
	private String seasonalCode = "";
	private String targetDays = "";
	private String dCMinBalance = "";
	private String orderPOCode = "";
	private String orderBaseQty = "";
	private String orderIncrQty = "";
	private String casesPerPallet = "";
	private String pOCost = "";
	private String foreignCostPerUnitFreight = "";
	private String dutyPercent = "";
	private String otherCost = "";
	private String otherPercent = "";
	private String storeTmax = "";
	private String sellUoM = "";

	private String store = "";
	private String warehouse = "";
	private String zone = "";

	private boolean isSKULevelUpdate = false;
	private List<String> listOfStores = null;
	private List<String> listOfFCs = null;
	private List<String> listOfDCs = null;
	private List<String> listOfAllLocation = null;
/*
A0007	Distribution Center aka List of Warehouses
A0013	Vendor Model Number
A0023	Direct Store Delivery (DSD Flag)
A0028	Retail IA (Rebuyer) Number
A0029	Retail Product Manager Number
A0031	Inventory Group
A0045	Pallet Ti
A0046   Pallet Hi
A0065	WHSP
A0066	Store Tmax (Min Pres)
A0067	Selling Units per Master Case Pack (SUCP)
A0068	Master Case Pack Length
A0069	Master Case Pack Width
A0070	Master Case Pack Height
A0071	Master Case Pack Weight
A0072	STA aka Merchandising Status @ Location Level
A0073	SRC
A0074	DC Purchase Flag
A0075	Vendor Number
A0077	List Price
A0237	Sell Unit Length
A0238	Sell Unit Width
A0239	Sell Unit Height
A0240	Sell Unit Weight
A0241	Purchase Case Weight
A0372	DC Hold Flag
A0373	DC Flow
A0374	Store - Source Warehouse (DC)
A0375	Store Locations Activate
A0376	Store Locations Inactivate
A0389	Add/Remove SWO SKU
A0390	Purchase Case Cube
A0391	Discontinued by Manufacturer (DBM) / Staples (DBS)
A0421	Specific Store
A0432	Drop Ship @ FC Level
A0433	FC Purchase Flag
A0526	Store Code (List)
A0527	Fulfillment  Center Code (List)
A0528	Distribution Center Code (List)
A0497   End Cost
A0498   Vendor Load
//A0054  Add on allowed
//A0055  Distribution Pack
//A0056  Distribution Policy Code
//A0044  Velocity Code
//A0057  Seasonal Code
//A0058  Target  days
//A0059  DC Min balance
//A0060  Order PO Code
//A0061  Order Base Qty
//A0062  Order Incr Qty
//A0063  Cases/Pallet
//A0078  PO Cost
//A0047  Foreign Cost/Unit Freight
//A0048  Duty %
//A0064  Other Cost
//A0049  Other %
//A0066  Store Tmax
//A0043  Sell UoM
*/
	
	public StepLocationLevelAttrib()
	{
		funcSpecVars = new HashMap<Object, Object>();
		listOfStores = new ArrayList<String>();
		listOfFCs = new ArrayList<String>(); 
		listOfDCs = new ArrayList<String>(); 
		 
	}
	
	
	public String getUpcNumber() {
		return upcNumber;
	}


	public void setUpcNumber(String upcNumber) {
		this.upcNumber = upcNumber;
	}


	public String getUpcFlag() {
		return upcFlag;
	}


	public void setUpcFlag(String upcFlag) {
		this.upcFlag = upcFlag;
	}
  
	
	public Map<Object, Object> getFuncSpecVars() {
		return funcSpecVars;
	}


	public void setFuncSpecVars(Map<Object, Object> funcSpecVars) {
		this.funcSpecVars = funcSpecVars;
	}


	public String getVendorModelNumber() {
		return vendorModelNumber;
	}


	public void setVendorModelNumber(String vendorModelNumber) {
		this.vendorModelNumber = vendorModelNumber;
	}


	public int getSkuNumber() {
		return skuNumber;
	}


	public void setSkuNumber(int skuNumber) {
		this.skuNumber = skuNumber;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	public void setUsecase(String usecase) {
		this.usecase = usecase;
	}


	public String getUsecase() {
		return usecase;
	}


	public String getDsdFlag() {
		return dsdFlag;
	}


	public void setDsdFlag(String dsdFlag) {
		this.dsdFlag = dsdFlag;
	}


	public String getRetailIA_Rebuyer() {
		return retailIA_Rebuyer;
	}


	public void setRetailIA_Rebuyer(String retailIA_Rebuyer) {
		this.retailIA_Rebuyer = retailIA_Rebuyer;
	}


	public String getRetailProductManager() {
		return retailProductManager;
	}


	public void setRetailProductManager(String retailProductManager) {
		this.retailProductManager = retailProductManager;
	}


	public String getInventoryGroup() {
		return InventoryGroup;
	}


	public void setInventoryGroup(String inventoryGroup) {
		InventoryGroup = inventoryGroup;
	}


	public String getPalletTi() {
		return palletTi;
	}


	public void setPalletTi(String palletTi) {
		this.palletTi = palletTi;
	}


	public String getPalletHi() {
		return palletHi;
	}


	public void setPalletHi(String palletHi) {
		this.palletHi = palletHi;
	}


	public String getWhsp() {
		return whsp;
	}


	public void setWhsp(String whsp) {
		this.whsp = whsp;
	}


	public String getSucp() {
		return sucp;
	}


	public void setSucp(String sucp) {
		this.sucp = sucp;
	}


	public String getMasterCasePackLength() {
		return masterCasePackLength;
	}


	public void setMasterCasePackLength(String masterCasePackLength) {
		this.masterCasePackLength = masterCasePackLength;
	}


	public String getMasterCasePackWidth() {
		return masterCasePackWidth;
	}


	public void setMasterCasePackWidth(String masterCasePackWidth) {
		this.masterCasePackWidth = masterCasePackWidth;
	}


	public String getMasterCasePackHeight() {
		return masterCasePackHeight;
	}


	public void setMasterCasePackHeight(String masterCasePackHeight) {
		this.masterCasePackHeight = masterCasePackHeight;
	}


	public String getMasterCasePackWeight() {
		return masterCasePackWeight;
	}


	public void setMasterCasePackWeight(String masterCasePackWeight) {
		this.masterCasePackWeight = masterCasePackWeight;
	}


	public String getMerchStatus() {
		return merchStatus;
	}


	public void setMerchStatus(String merchStatus) {
		this.merchStatus = merchStatus;
	}


	public String getSrc() {
		return src;
	}


	public void setSrc(String src) {
		this.src = src;
	}


	public String getDcPurchaseFlag() {
		return dcPurchaseFlag;
	}


	public void setDcPurchaseFlag(String dcPurchaseFlag) {
		this.dcPurchaseFlag = dcPurchaseFlag;
	}


	public String getVendorNumber() {
		return vendorNumber;
	}


	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}


	public String getListPrice() {
		return listPrice;
	}


	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}


	public String getSellUnitLength() {
		return sellUnitLength;
	}


	public void setSellUnitLength(String sellUnitLength) {
		this.sellUnitLength = sellUnitLength;
	}


	public String getSellUnitWidth() {
		return sellUnitWidth;
	}


	public void setSellUnitWidth(String sellUnitWidth) {
		this.sellUnitWidth = sellUnitWidth;
	}


	public String getSellUnitHeight() {
		return sellUnitHeight;
	}


	public void setSellUnitHeight(String sellUnitHeight) {
		this.sellUnitHeight = sellUnitHeight;
	}


	public String getSellUnitWeight() {
		return sellUnitWeight;
	}


	public void setSellUnitWeight(String sellUnitWeight) {
		this.sellUnitWeight = sellUnitWeight;
	}


	public String getPurchaseCaseWeight() {
		return purchaseCaseWeight;
	}


	public void setPurchaseCaseWeight(String purchaseCaseWeight) {
		this.purchaseCaseWeight = purchaseCaseWeight;
	}


	public String getDcHoldFlag() {
		return dcHoldFlag;
	}


	public void setDcHoldFlag(String dcHoldFlag) {
		this.dcHoldFlag = dcHoldFlag;
	}


	public String getDcFlow() {
		return dcFlow;
	}


	public void setDcFlow(String dcFlow) {
		this.dcFlow = dcFlow;
	}


	public String getStoreSourceWarehouse() {
		return storeSourceWarehouse;
	}


	public void setStoreSourceWarehouse(String storeSourceWarehouse) {
		this.storeSourceWarehouse = storeSourceWarehouse;
	}


	public String getStoreLocationsActivate() {
		return storeLocationsActivate;
	}


	public void setStoreLocationsActivate(String storeLocationsActivate) {
		this.storeLocationsActivate = storeLocationsActivate;
	}


	public String getStoreLocationsInactivate() {
		return storeLocationsInactivate;
	}


	public void setStoreLocationsInactivate(String storeLocationsInactivate) {
		this.storeLocationsInactivate = storeLocationsInactivate;
	}


	public String getAddRemoveSWO() {
		return addRemoveSWO;
	}


	public void setAddRemoveSWO(String addRemoveSWO) {
		this.addRemoveSWO = addRemoveSWO;
	}


	public String getPurchaseCaseCube() {
		return purchaseCaseCube;
	}


	public void setPurchaseCaseCube(String purchaseCaseCube) {
		this.purchaseCaseCube = purchaseCaseCube;
	}


	public String getDiscontinuedDbmDbs() {
		return discontinuedDbmDbs;
	}


	public void setDiscontinuedDbmDbs(String discontinuedDbmDbs) {
		this.discontinuedDbmDbs = discontinuedDbmDbs;
	}


	public String getSpecificStore() {
		return specificStore;
	}


	public void setSpecificStore(String specificStore) {
		this.specificStore = specificStore;
	}


	public String getDropShipFC() {
		return dropShipFC;
	}


	public void setDropShipFC(String dropShipFC) {
		this.dropShipFC = dropShipFC;
	}
 

	public String getFcPurchaseFlag() {
		return fcPurchaseFlag;
	}


	public void setFcPurchaseFlag(String fcPurchaseFlag) {
		this.fcPurchaseFlag = fcPurchaseFlag;
	}


	public String getStore() {
		return store;
	}


	public void setStore(String store) {
		this.store = store;
	}


	public String getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getAddOnAllowed() {
		return addOnAllowed;
	}


	public void setAddOnAllowed(String addOnAllowed) {
		this.addOnAllowed = addOnAllowed;
	}


	public String getDistributionPack() {
		return distributionPack;
	}


	public void setDistributionPack(String distributionPack) {
		this.distributionPack = distributionPack;
	}


	public String getDistributionPolicyCode() {
		return distributionPolicyCode;
	}


	public void setDistributionPolicyCode(String distributionPolicyCode) {
		this.distributionPolicyCode = distributionPolicyCode;
	}


	public String getVelocityCode() {
		return velocityCode;
	}


	public void setVelocityCode(String velocityCode) {
		this.velocityCode = velocityCode;
	}


	public String getSeasonalCode() {
		return seasonalCode;
	}


	public void setSeasonalCode(String seasonalCode) {
		this.seasonalCode = seasonalCode;
	}


	public String getTargetDays() {
		return targetDays;
	}


	public void setTargetDays(String targetDays) {
		this.targetDays = targetDays;
	}


	public String getdCMinBalance() {
		return dCMinBalance;
	}


	public void setdCMinBalance(String dCMinBalance) {
		this.dCMinBalance = dCMinBalance;
	}


	public String getOrderPOCode() {
		return orderPOCode;
	}


	public void setOrderPOCode(String orderPOCode) {
		this.orderPOCode = orderPOCode;
	}


	public String getOrderBaseQty() {
		return orderBaseQty;
	}


	public void setOrderBaseQty(String orderBaseQty) {
		this.orderBaseQty = orderBaseQty;
	}


	public String getOrderIncrQty() {
		return orderIncrQty;
	}


	public void setOrderIncrQty(String orderIncrQty) {
		this.orderIncrQty = orderIncrQty;
	}


	public String getCasesPerPallet() {
		return casesPerPallet;
	}


	public void setCasesPerPallet(String casesPerPallet) {
		this.casesPerPallet = casesPerPallet;
	}


	public String getpOCost() {
		return pOCost;
	}


	public void setpOCost(String pOCost) {
		this.pOCost = pOCost;
	}


	public String getForeignCostPerUnitFreight() {
		return foreignCostPerUnitFreight;
	}


	public void setForeignCostPerUnitFreight(String foreignCostPerUnitFreight) {
		this.foreignCostPerUnitFreight = foreignCostPerUnitFreight;
	}


	public String getDutyPercent() {
		return dutyPercent;
	}


	public void setDutyPercent(String dutyPercent) {
		this.dutyPercent = dutyPercent;
	}


	public String getOtherCost() {
		return otherCost;
	}


	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}


	public String getOtherPercent() {
		return otherPercent;
	}


	public void setOtherPercent(String otherPercent) {
		this.otherPercent = otherPercent;
	}


	public String getStoreTmax() {
		return storeTmax;
	}


	public void setStoreTmax(String storeTmax) {
		this.storeTmax = storeTmax;
	}


	public String getSellUoM() {
		return sellUoM;
	}


	public void setSellUoM(String sellUoM) {
		this.sellUoM = sellUoM;
	}


	public String getZone() {
		return zone;
	}


	public void setZone(String zone) {
		this.zone = zone;
	}

	public boolean isSKULevelUpdate() {
		return isSKULevelUpdate;
	}


	public void setSKULevelUpdate(boolean isSKULevelUpdate) {
		this.isSKULevelUpdate = isSKULevelUpdate;
	}


	@SuppressWarnings("unchecked")
	public List<String> getListOfStores() {
		return listOfStores;
	}


	public void setListOfStores(List<String> listOfStores) {
		this.listOfStores = listOfStores;
	}


	@SuppressWarnings("unchecked")
	public List<String> getListOfFCs() {
		return listOfFCs;
	}


	public void setListOfFCs(List<String> listOfFCs) {
		this.listOfFCs = listOfFCs;
	}


	@SuppressWarnings("unchecked")
	public List<String> getListOfDCs() {
		return listOfDCs;
	}


	public void setListOfAllLocation(List<String> listOfAllLocation) {
		this.listOfAllLocation = listOfAllLocation;
	}

	@SuppressWarnings("unchecked")
	public List<String> getListOfAllLocation() {
		return listOfAllLocation;
	}


	public void setListOfDCs(List<String> listOfDCs) {
		this.listOfDCs = listOfDCs;
	}


	public String getTargetMax() {
		return targetMax;
	}


	public void setTargetMax(String targetMax) {
		this.targetMax = targetMax;
	}


	public String getStateCode() {
		return stateCode;
	}


	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}


	public String getStoreOnly() {
		return storeOnly;
	}


	public void setStoreOnly(String storeOnly) {
		this.storeOnly = storeOnly;
	}


	public String getWarehouseOnly() {
		return warehouseOnly;
	}


	public void setWarehouseOnly(String warehouseOnly) {
		this.warehouseOnly = warehouseOnly;
	}  
	
}
