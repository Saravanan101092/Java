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
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */ 

package com.staples.pim.delegate.createupdateitem.domain;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.domain.ProductAttributesInProcess;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;

public class ProductFilterStepXML implements ItemProcessor<STEPProductInformation.Products.Product,STEPProductInformation.Products.Product> {
	@Override
	public STEPProductInformation.Products.Product process(STEPProductInformation.Products.Product item) throws Exception {
 
		//filter object which UserTypeID is "Item"
		if(item.getUserTypeID().equalsIgnoreCase("ITEM")){
			if (RunSchedulerItemCreateUpdate.bContainsSKU){
				return null; // null = ignore this object
			}
			else {

				item.values.value.addAll(ProductAttributesInProcess.responsibilityAttributeList);
				item.values.value.addAll(ProductAttributesInProcess.vendorData);
				return item;
			}
		}
		//filter the object when the usertypeid is "ItemToVendor"
		if(item.getUserTypeID().equalsIgnoreCase("ItemToVendor")){
			return null;
		}
		return item;
	}
}
 
