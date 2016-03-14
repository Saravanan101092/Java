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

import java.util.HashMap;
import java.util.Map;

public class FTPConnectionBean { 
	
	private Map<Object, Object> funcSpecVars = null;
	private String hostName = null;
	private String userId = null;
	private String password = null;
	private String destinationUrl = null;
	private String originatedURL = null;
	
	
	public FTPConnectionBean()
	{
		setFuncSpecVars(new HashMap<Object, Object>());
	}

	public void setFuncSpecVars(Map<Object, Object> funcSpecVars) {
		this.funcSpecVars = funcSpecVars;
	}

	public Map<Object, Object> getFuncSpecVars() {
		return funcSpecVars;
	}
	
	public void addItem (Object obj, String key){
		
		funcSpecVars.put(obj, key);		
	}
	
	public void getItem (String key){
		
		funcSpecVars.get(key);	
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

	public String getOriginatedURL() {
		return originatedURL;
	}

	public void setOriginatedURL(String originatedURL) {
		this.originatedURL = originatedURL;
	} 
}
