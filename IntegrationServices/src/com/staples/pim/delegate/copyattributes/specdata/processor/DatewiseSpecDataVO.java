
package com.staples.pim.delegate.copyattributes.specdata.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatewiseSpecDataVO {

	public DatewiseSpecDataVO(){
		
		sequenceNo_AttributeValueMap = new HashMap<String, String>();
	}
	
	public Date getLastModified() {

		return lastModified;
	}

	public void setLastModified(Date lastModified) {

		this.lastModified = lastModified;
	}

	public Map<String, String> getSequenceNo_AttributeValueMap() {

		return sequenceNo_AttributeValueMap;
	}

	public void setSequenceNo_AttributeValueMap(Map<String, String> sequenceNo_AttributeValueMap) {

		this.sequenceNo_AttributeValueMap = sequenceNo_AttributeValueMap;
	}

	Map<String, String>	sequenceNo_AttributeValueMap;
	Date				lastModified;
}
