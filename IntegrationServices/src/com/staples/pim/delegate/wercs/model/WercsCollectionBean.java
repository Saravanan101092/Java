package com.staples.pim.delegate.wercs.model;

import java.util.Map;


public class WercsCollectionBean {

	Map<String,String> attributeValueMap;
	
	public Map<String,String> getAttributeValueMap(){
		return attributeValueMap;
	}
	
	public void setAttributeValueMap(Map<String,String> map){
		attributeValueMap = map;
	}

	String KEYID=null;
	
	public String getKEYID() {
		
		return KEYID;
		
	}

	public void setKEYID(String KeyList) {
	
		KEYID = KeyList;
	}
}
