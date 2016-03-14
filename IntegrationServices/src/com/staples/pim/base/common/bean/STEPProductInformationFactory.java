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

package com.staples.pim.base.common.bean;

public class STEPProductInformationFactory {

	//private PCMLogger traceLogger = PCMLogger.getInstance(FREEFORM_TRACE_LOGGER);
	//private String clazzname = this.getClass().getName();
    /**
     * This is an abstract factory class used for retrieving instance of the STEPProductInformation inner classes.
     *
     */
    public STEPProductInformationFactory() {
    	 
    }

    /**
     * Create an instance of {@link STEPProductInformation }
     *
     */
    public STEPProductInformation getInstanceOfSTEPProductInformation() {
    	 
        return new STEPProductInformation();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products }
     *
     */
    public STEPProductInformation.Products getInstanceOfSTEPProductInformationProducts() {
    	 
        return new STEPProductInformation.Products();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products.Product }
     *
     */
    public STEPProductInformation.Products.Product getInstanceOfSTEPProductInformationProductsProduct() {
    	 
        return new STEPProductInformation.Products.Product();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products.Product.Values }
     *
     */
    public STEPProductInformation.Products.Product.Values 
    						getInstanceOfSTEPProductInformationProductsProductValues() { 
        return new STEPProductInformation.Products.Product.Values();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products.Product.ClassificationReference }
     *
     */
    public STEPProductInformation.Products.Product.ClassificationReference 	
    						getInstanceOfSTEPProductInformationProductsProductClassificationReference() { 
        return new STEPProductInformation.Products.Product.ClassificationReference();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products.Product.AssetCrossReference }
     *
     */
    public STEPProductInformation.Products.Product.AssetCrossReference 
    						getInstanceOfSTEPProductInformationProductsProductAssetCrossReference() { 
        return new STEPProductInformation.Products.Product.AssetCrossReference();
    }

    /**
     * Create an instance of {@link STEPProductInformation.Products.Product.AssetCrossReference }
     *
     */
    public STEPProductInformation.Products.Product.ProductCrossReference 
    						getInstanceOfSTEPProductInformationProductsProductProductCrossReference() { 
        return new STEPProductInformation.Products.Product.ProductCrossReference();
    }
    
    /**
     * Create an instance of {@link STEPProductInformation.Products.Product.Values.Value }
     *
     */
    public STEPProductInformation.Products.Product.Values.Value 	
    						getInstanceOfSTEPProductInformationProductsProductValuesValue() { 
        return new STEPProductInformation.Products.Product.Values.Value();
    } 
}
