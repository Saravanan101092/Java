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
 * File name     :   HeaderWriterForFixLength
 * Creation Date :   
 * @author  
 * @version 1.0
 */


package com.staples.pim.delegate.commonusecases.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.staples.pim.base.common.writer.HeaderWriter;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;

public class HeaderWriterForFixLength extends HeaderWriter {
	
	public void writeHeader(Writer writer) throws IOException { 
		traceLogger.info(clazzname, "writeHeader", "ENTER/.../EXIT: FlatFileHeaderCallback");	
		String attribIDs =  IntgSrvPropertiesReader.getProperty(
								IntgSrvAppConstants.FIX_HEADER_LINE_ATTRIBUTE_IDS);  
		attribIDs=getChannelSpecificAttributes(attribIDs);
		//IF Condition is added to restrict the fix length file header creation only for SKUs.
		if(RunSchedulerItemCreateUpdate.bContainsSKU){
			writer.write(attribIDs); 
			traceLogger.info(clazzname, "writeHeader", "Header is blocked if SKU is unavailable");
		}
		traceLogger.info(clazzname, "writeHeader", "EXIT");
	}

	/**
	 * Headers are changed on the basis of downstream request
	 * @param headerString
	 * @return
	 */
	public String getChannelSpecificAttributes(String headerString){
		List<String> channelSpecificAttributes=new ArrayList<String>();
		channelSpecificAttributes.add("A0013");
		channelSpecificAttributes.add("A0018");
		channelSpecificAttributes.add("A0045");
		channelSpecificAttributes.add("A0046");
		channelSpecificAttributes.add("A0067");
		channelSpecificAttributes.add("A0075");
		channelSpecificAttributes.add("A0077");
		channelSpecificAttributes.add("A0078");
		channelSpecificAttributes.add("A0430");
		for(String channelSpAttribute:channelSpecificAttributes){
			headerString=headerString.replaceAll( channelSpAttribute+"_RET",channelSpAttribute);
			headerString=headerString.replaceAll( channelSpAttribute+"_NAD",channelSpAttribute);
			headerString=headerString.replaceAll( channelSpAttribute+"_SUPC",channelSpAttribute);
			headerString=headerString.replaceAll( channelSpAttribute+"_CUPC",channelSpAttribute);
			headerString=headerString.replaceAll( channelSpAttribute+"_IUPC",channelSpAttribute);
			headerString=headerString.replaceAll( channelSpAttribute+"_PUPC",channelSpAttribute);
		}
		return headerString;
	}
}
