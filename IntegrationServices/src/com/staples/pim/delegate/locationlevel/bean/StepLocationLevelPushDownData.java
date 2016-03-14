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
 * File name     	:   StepLocationLevelPushDownData
 * Creation Date 	:    
 * @author  	 	:	Sima Zaslavsky
 * @version 1.0
 */ 

package com.staples.pim.delegate.locationlevel.bean;

import java.util.HashMap;
import java.util.Map;

public class StepLocationLevelPushDownData {
	
	private Map<Object, Object> funcSpecVars = null;
	private String channel = null;
	private String procNameRet = null;
	//private String procNameScc = null;
	private String procNameNad = null; 
	private String keyLine = null;
	private String dataline1 = "";
	private String dataline2 = "";
	private String dataline3 = "";
	private String dataline4 = "";
	private String dataline5 = "";
	private String dataline6 = "";
	private String dataline7 = "";
	private String dataline8 = "";
	private String dataline9 = "";
	private String dataline10 = "";
	private String dataline11 = "";
	private String dataline12 = "";
	
	
	public StepLocationLevelPushDownData()
	{
		setFuncSpecVars(new HashMap<Object, Object>());
	}


	public Map<Object, Object> getFuncSpecVars() {
		return funcSpecVars;
	}


	public void setFuncSpecVars(Map<Object, Object> funcSpecVars) {
		this.funcSpecVars = funcSpecVars;
	}
 

	public String getKeyLine() {
		return keyLine;
	}


	public void setKeyLine(String keyLine) {
		this.keyLine = keyLine;
	}


	public String getDataline1() {
		return dataline1;
	}


	public void setDataline1(String dataline1) {
		this.dataline1 = dataline1;
	}


	public String getDataline2() {
		return dataline2;
	}


	public void setDataline2(String dataline2) {
		this.dataline2 = dataline2;
	}


	public String getDataline3() {
		return dataline3;
	}


	public void setDataline3(String dataline3) {
		this.dataline3 = dataline3;
	}


	public String getDataline4() {
		return dataline4;
	}


	public void setDataline4(String dataline4) {
		this.dataline4 = dataline4;
	}


	public String getDataline5() {
		return dataline5;
	}


	public void setDataline5(String dataline5) {
		this.dataline5 = dataline5;
	}


	public String getDataline6() {
		return dataline6;
	}


	public void setDataline6(String dataline6) {
		this.dataline6 = dataline6;
	}


	public String getDataline7() {
		return dataline7;
	}


	public void setDataline7(String dataline7) {
		this.dataline7 = dataline7;
	}


	public String getDataline8() {
		return dataline8;
	}


	public void setDataline8(String dataline8) {
		this.dataline8 = dataline8;
	}


	public String getDataline9() {
		return dataline9;
	}


	public void setDataline9(String dataline9) {
		this.dataline9 = dataline9;
	}


	public String getDataline10() {
		return dataline10;
	}


	public void setDataline10(String dataline10) {
		this.dataline10 = dataline10;
	}


	public String getDataline11() {
		return dataline11;
	}


	public void setDataline11(String dataline11) {
		this.dataline11 = dataline11;
	}


	public String getDataline12() {
		return dataline12;
	}


	public void setDataline12(String dataline12) {
		this.dataline12 = dataline12;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getProcNameRet() {
		return procNameRet;
	}


	public void setProcNameRet(String procNameRet) {
		this.procNameRet = procNameRet;
	}

/*
	public String getProcNameScc() {
		return procNameScc;
	}


	public void setProcNameScc(String procNameScc) {
		this.procNameScc = procNameScc;
	}
*/ 
	public String getProcNameNad() {
		return procNameNad;
	}


	public void setProcNameNad(String procNameNad) {
		this.procNameNad = procNameNad;
	}
	
}


