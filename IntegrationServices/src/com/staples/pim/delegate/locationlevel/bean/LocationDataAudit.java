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
 * File name     	:   LocationDataAudit.java
 * Creation Date 	:   
 * @author  	 	:	Junfu WU
 */ 

package com.staples.pim.delegate.locationlevel.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationDataAudit {
	private String correlation_ID = "";
	private int SKU_ID = 0;//A0012
	private String STEP_Item_ID = "";
	private String channel = "";//derived from Attribute ID
	private String location_Attribute_FAN = "";
	private String attribute_Name = "";
	private String attribute_Value = "";
	private String location_Type = "";
	private String location_ID = "";

	
	public String getCorrelation_ID() {
	
		return correlation_ID;
	}
	
	public void setCorrelation_ID(String correlation_ID) {
	
		this.correlation_ID = correlation_ID;
	}
	
	public int getSKU_ID() {
	
		return SKU_ID;
	}
	
	public void setSKU_ID(int SKU_ID) {
	
		this.SKU_ID = SKU_ID;
	}
	
	public String getSTEP_Item_ID() {
	
		return STEP_Item_ID;
	}
	
	public void setSTEP_Item_ID(String STEP_Item_ID) {
	
		this.STEP_Item_ID = STEP_Item_ID;
	}
	
	public String getChannel() {
	
		return channel;
	}
	
	public void setChannel(String channel) {
	
		this.channel = channel;
	}
	
	public String getLocation_Attribute_FAN() {
	
		return location_Attribute_FAN;
	}
	
	public void setLocation_Attribute_FAN(String location_Attribute_FAN) {
	
		this.location_Attribute_FAN = location_Attribute_FAN;
	}
	
	public String getAttribute_Name() {
	
		return attribute_Name;
	}
	
	public void setAttribute_Name(String attribute_Name) {
	
		this.attribute_Name = attribute_Name;
	}
	
	public String getAttribute_Value() {
	
		return attribute_Value;
	}
	
	public void setAttribute_Value(String attribute_Value) {
	
		this.attribute_Value = attribute_Value;
	}
	
	public String getLocation_Type() {
	
		return location_Type;
	}
	
	public void setLocation_Type(String location_Type) {
	
		this.location_Type = location_Type;
	}
	
	public String getLocation_ID() {
	
		return location_ID;
	}
	
	public void setLocation_ID(String location_ID) {
	
		this.location_ID = location_ID;
	}
}
