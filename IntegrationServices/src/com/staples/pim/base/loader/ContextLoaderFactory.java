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
 * File name     :   ContextLoaderFactory
 * Creation Date :   
 * @author  
 * @version 1.0
 */ 

package com.staples.pim.base.loader;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext; 

import com.staples.pim.base.util.IntgSrvUtils;

public  class ContextLoaderFactory extends ContextLoaderAbstract {
	
	protected static ContextLoaderFactory contextLoader = null;
	private GenericXmlApplicationContext ctx = null;
	private FileSystemXmlApplicationContext ctxFileSys = null;
	
	public ContextLoaderFactory() {
		 
	}

	public static ContextLoaderFactory getInstanceLoadContext() {
		if (contextLoader == null) {
			contextLoader = new ContextLoaderFactory();
		}
		return contextLoader;
	}
	 

	public GenericXmlApplicationContext loadContext(String contextFileName) {
		 
		String path = IntgSrvUtils.getConfigDir();

		if (ctx == null) {
			ctx = new GenericXmlApplicationContext(); 
			ctx.load("file:" + path + contextFileName); 
			ctx.refresh();
		}
		return ctx;
	}   
	
	public FileSystemXmlApplicationContext loadContextFileSystem(String contextFileName) {
		 
		String path = IntgSrvUtils.getConfigDir();

		if (ctxFileSys == null) {
			// local testing ctxFileSys = new FileSystemXmlApplicationContext(path + contextFileName); 
			ctxFileSys = new FileSystemXmlApplicationContext("/configurations" + contextFileName); 	
		}
		return ctxFileSys;
	}

	   
}
