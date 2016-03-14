
package com.staples.pim.delegate.wholesalers.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.delegate.wholesalers.model.WholesalerDotcomFeedBean;

public class WholesalerDotcomFeedFieldSetMapper implements FieldSetMapper<WholesalerDotcomFeedBean> {

	@Override
	public WholesalerDotcomFeedBean mapFieldSet(FieldSet fieldSet) throws BindException {

		WholesalerDotcomFeedBean bean = new WholesalerDotcomFeedBean();

		bean.setActionType(StringUtils.trimToEmpty(fieldSet.readString("actionType")));
		bean.setWholesalerID(StringUtils.trimToEmpty(fieldSet.readString("wholesalerID")));
		bean.setWholesalerItemNo(StringUtils.trimToEmpty(fieldSet.readString("wholesalerItemNo")));
		bean.setDescription(replaceActualDelata(StringUtils.trimToEmpty(fieldSet.readString("description"))));
		bean.setStaplesSKUNo(StringUtils.trimToEmpty(fieldSet.readString("staplesSKUNo")));
		bean.setGenericSKUNo(StringUtils.trimToEmpty(fieldSet.readString("genericSKUNo")));
		bean.setUpcCode(StringUtils.trimToEmpty(fieldSet.readString("upcCode")));
		bean.setVendorName(StringUtils.trimToEmpty(fieldSet.readString("vendorName")));
		bean.setMfgPartNo(StringUtils.trimToEmpty(fieldSet.readString("mfgPartNo")));
		bean.setProductClassCode(StringUtils.trimToEmpty(fieldSet.readString("productClassCode")));
		bean.setProductNumber(StringUtils.trimToEmpty(fieldSet.readString("productNumber")));
		bean.setCategoryNumbers(StringUtils.trimToEmpty(fieldSet.readString("categoryNumbers")));
		bean.setInventoryUnit(StringUtils.trimToEmpty(fieldSet.readString("inventoryUnit")));
		bean.setBoxPackQty(StringUtils.trimToEmpty(fieldSet.readString("boxPackQty")));
		bean.setBoxPackUnit(StringUtils.trimToEmpty(fieldSet.readString("boxPackUnit")));
		bean.setCartonPackQty(StringUtils.trimToEmpty(fieldSet.readString("cartonPackQty")));
		bean.setCartonPackUnit(StringUtils.trimToEmpty(fieldSet.readString("cartonPackUnit")));
		bean.setCartonWeight(StringUtils.trimToEmpty(fieldSet.readString("cartonWeight")));
		bean.setCartonLength(StringUtils.trimToEmpty(fieldSet.readString("cartonLength")));
		bean.setCartonWidth(StringUtils.trimToEmpty(fieldSet.readString("cartonWidth")));
		bean.setCartonHeight(StringUtils.trimToEmpty(fieldSet.readString("cartonHeight")));
		bean.setListQty(StringUtils.trimToEmpty(fieldSet.readString("listQty")));
		bean.setListUnit(StringUtils.trimToEmpty(fieldSet.readString("listUnit")));
		bean.setListPrice(StringUtils.trimToEmpty(fieldSet.readString("listPrice")));
		bean.setListPriceEffDate(StringUtils.trimToEmpty(fieldSet.readString("listPriceEffDate")));
		bean.setUnitCost(StringUtils.trimToEmpty(fieldSet.readString("unitCost")));
		bean.setCostEffDate(StringUtils.trimToEmpty(fieldSet.readString("costEffDate")));
		bean.setCommissioningClass(StringUtils.trimToEmpty(fieldSet.readString("commissioningClass")));
		bean.setCatalogPage(StringUtils.trimToEmpty(fieldSet.readString("catalogPage")));
		bean.setUpsableFlag(StringUtils.trimToEmpty(fieldSet.readString("upsableFlag")));
		bean.setAssemblyRequired(StringUtils.trimToEmpty(fieldSet.readString("assemblyRequired")));
		bean.setFurnitureServicesAllowed(StringUtils.trimToEmpty(fieldSet.readString("furnitureServicesAllowed")));
		bean.setLongDescription(replaceActualDelata(StringUtils.trimToEmpty(fieldSet.readString("longDescription"))));
		bean.setDotcomNetPrice(StringUtils.trimToEmpty(fieldSet.readString("dotcomNetPrice")));
		bean.setNetPrice(StringUtils.trimToEmpty(fieldSet.readString("netPrice")));
		bean.setItemRefCode(StringUtils.trimToEmpty(fieldSet.readString("itemRefCode")));
		bean.setReferenceItem(StringUtils.trimToEmpty(fieldSet.readString("referenceItem")));
		bean.setRawWholesalerNo(StringUtils.trimToEmpty(fieldSet.readString("rawWholesalerNo")));
		bean.setSkuPartType(StringUtils.trimToEmpty(fieldSet.readString("skuPartType")));
		bean.setManFurnFlag2(StringUtils.trimToEmpty(fieldSet.readString("manFurnFlag2")));
		bean.setInvSearchClass(StringUtils.trimToEmpty(fieldSet.readString("invSearchClass")));
		bean.setAssemblyFee(StringUtils.trimToEmpty(fieldSet.readString("assemblyFee")));
		bean.setUncrateReqFlag(StringUtils.trimToEmpty(fieldSet.readString("uncrateReqFlag")));
		bean.setUncratingFee(StringUtils.trimToEmpty(fieldSet.readString("uncratingFee")));
		bean.setQtlyUpdateFlag(StringUtils.trimToEmpty(fieldSet.readString("qtlyUpdateFlag")));
		return bean;
	}
	private String replaceActualDelata(String filed){

		if(filed.contains(IntgSrvAppConstants.ITEM_DESCRPTION_TILDE))
		{
			String filedValue=filed.replace(IntgSrvAppConstants.ITEM_DESCRPTION_TILDE,IntgSrvAppConstants.DELIMITER_TILDA);
			return filedValue;
		}
		return filed;
	}
}