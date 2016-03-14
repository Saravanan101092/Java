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

package com.staples.pim.delegate.reclass.processor;

import java.util.HashMap;

import com.staples.pim.base.util.IntgSrvAppConstants;

public class TransformationRulesForReclass {
	
	@SuppressWarnings("unused")
	public static HashMap<String, String> overrideValuesForReclass(HashMap<String, String> allValuesHashMap)
	   {
		   HashMap<String, String> allValues = allValuesHashMap;
		   
		   if (allValues.get(IntgSrvAppConstants.ITMDECCNT) != null)
		   {
				allValues.put(IntgSrvAppConstants.ITMDECCNT, "1");
		   }
			if (allValues.get(IntgSrvAppConstants.ITMSKUCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMSKUCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMWSKUCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMWSKUCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMVNDCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMVNDCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMSUPCCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMSUPCCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMCUPCCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMCUPCCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMIUPCCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMIUPCCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMPUPCCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMPUPCCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMHAZCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMHAZCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMENVCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMENVCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMOTHCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMOTHCNT, "1");
			}
			if (allValues.get(IntgSrvAppConstants.ITMPVTCNT) != null)
			{
				allValues.put(IntgSrvAppConstants.ITMPVTCNT, "1");  
			}
		   
		   return allValues;
	   }

}
