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
 * File name     :   IConverterToFixLength
 * Creation Date :   
 * @author  	 :   
 * @version 1.0
 */ 


package com.staples.pim.delegate.commonusecases.formatter;

import java.util.HashMap;
import java.util.List;

import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.StepTransmitterBean;


public interface IFormatterToFixLength {

	public StepTransmitterBean buildFixLengthString(STEPProductInformation.Products.Product.ProductCrossReference productCrossReference,
													List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef,
													List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueProdCrossRefAttribList,
													List<STEPProductInformation.Products.Product.Values.Value> valueList,
													List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueAttribList,
													String productID); 
	 
	public HashMap<String, String> convertProductCrossReference(List<STEPProductInformation.Products.Product.Values.Value> valueList,
																List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueProdCrossRefAttribList,
																HashMap<String, String> allValuesHashMap);
	
	public HashMap<String, String> convertMultiValues(List<STEPProductInformation.Products.Product.Values.MultiValue> attribList, 
															HashMap<String, String> allValuesHashMap);
}
