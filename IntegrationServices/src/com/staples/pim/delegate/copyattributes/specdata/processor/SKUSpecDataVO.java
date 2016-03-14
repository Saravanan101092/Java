package com.staples.pim.delegate.copyattributes.specdata.processor;

import java.util.HashMap;
import java.util.Map;


public class SKUSpecDataVO {

	public SKUSpecDataVO(){
		
		attributes = new HashMap<String, AttributewiseSpecDataVO>();
	}
	
	public String getSKUid() {
	
		return SKUid;
	}
	
	public void setSKUid(String sKUid) {
	
		SKUid = sKUid;
	}
	
	public Map<String, AttributewiseSpecDataVO> getAttributes() {
	
		return attributes;
	}

	
	public void setAttributes(Map<String, AttributewiseSpecDataVO> attributes) {
	
		this.attributes = attributes;
	}

	String SKUid;
	Map<String , AttributewiseSpecDataVO> attributes;
}
