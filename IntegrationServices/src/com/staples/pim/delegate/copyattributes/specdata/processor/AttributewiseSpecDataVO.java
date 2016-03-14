
package com.staples.pim.delegate.copyattributes.specdata.processor;

import java.util.Map;

import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;

public class AttributewiseSpecDataVO {

	public String getAttributeID() {

		return attributeID;
	}

	public void setAttributeID(String attributeID) {

		this.attributeID = attributeID;
	}

	public DatewiseSpecDataVO getDatewiseSpecData() {
		
		return datewiseSpecData;
	}

	
	public void setDatewiseSpecData(DatewiseSpecDataVO datewiseSpecData) {
	
		this.datewiseSpecData = datewiseSpecData;
	}
	
	/*public String getAttributeDesc() {
	
		return attributeDesc;
	}

	public void setAttributeDesc(String attributeDesc) {
	
		this.attributeDesc = attributeDesc;
	}*/
	
	public String getAttributeValue(){
//		DatewiseSpecDataVO datewisedata = getLatestAttributeValue();
		String attributeValue="";
		
		Map<String,String> attributeValuesMap = datewiseSpecData.getSequenceNo_AttributeValueMap(); 
		for(String key : attributeValuesMap.keySet()){
			attributeValue+=attributeValuesMap.get(key)+DatamigrationAppConstants.COMMA;
		}
		
		attributeValue = attributeValue.substring(0, attributeValue.length()-1);
		return attributeValue;
	}
	
//	public Map<String, DatewiseSpecDataVO> getDatewiseSpecData() {
//		
//		return datewiseSpecData;
//	}
//
//	
//	public void setDatewiseSpecData(Map<String, DatewiseSpecDataVO> datewiseSpecData) {
//	
//		this.datewiseSpecData = datewiseSpecData;
//	}
	
//	public DatewiseSpecDataVO getLatestAttributeValue() {
//		DatewiseSpecDataVO latestData = null;
//		
//		for(String datewiseDataKey : datewiseSpecData.keySet()){
//			DatewiseSpecDataVO datewiseData = datewiseSpecData.get(datewiseDataKey);
//					
//			if(latestData == null){
//				latestData = datewiseData;
//			}else if(datewiseData.getLastModified().after(latestData.getLastModified())){
//				latestData = datewiseData;
//			}
//		}
//		
//		return latestData;
//	}

	DatewiseSpecDataVO	datewiseSpecData;
	
	String						attributeID;
//	String 						attributeDesc;
}
