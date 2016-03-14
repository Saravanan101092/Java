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
 * File name     :   HeaderWriterForXSV
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.delegate.commonusecases.writer;

import java.io.IOException;
import java.io.Writer;
 
import com.staples.pim.base.common.writer.HeaderWriter;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;

public class HeaderWriterForXSV extends HeaderWriter {
	
	public void writeHeader(Writer writer) throws IOException { 
		traceLogger.info(clazzname, "writeHeaderForXSV", "ENTER/.../EXIT: FlatFileHeaderCallback");	
		String attribIDs =  IntgSrvPropertiesReader.getProperty(
										IntgSrvAppConstants.XSV_HEADER_LINE_ATTRIBUTE_IDS); 
		String attribNames =  IntgSrvPropertiesReader.getProperty(
										IntgSrvAppConstants.XSV_HEADER_LINE_ATTRIBUTE_NAMES); 
		writer.write(attribIDs +"\r\n");
		writer.write(attribNames);
		traceLogger.info(clazzname, "writeHeaderForXSV", "EXIT");
	}
}
