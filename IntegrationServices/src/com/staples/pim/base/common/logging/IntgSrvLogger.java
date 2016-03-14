package com.staples.pim.base.common.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class IntgSrvLogger {
	
	Logger logger = null;
	
	IntgSrvLogger() {
		super();
	}
	/**
	 * @param loggername
	 */
	IntgSrvLogger(String loggername) 
	{
		this.logger = Logger.getLogger(loggername);
	}
	
	public Logger getLogger() {return this.logger;}
	
	public String getLogFilename(String appendername) {
		String filename = null;
		Appender appender = logger.getAppender(appendername);
	    if (appender != null && appender instanceof org.apache.log4j.FileAppender) {
	    	FileAppender fappender = (FileAppender) appender;
	    	filename = fappender.getFile();
	    }
	    return filename;
	}
	
	public void setLogFilename(String appendername, String newfilename) {
		Appender appender = logger.getAppender(appendername);
	    if (appender != null && appender instanceof org.apache.log4j.FileAppender) {
	    	FileAppender fappender = (FileAppender) appender;
	    	fappender.setFile(newfilename);
	    	fappender.activateOptions();
	    }
	}
	
	public void setLogFilename(String appendername, String oldsubstr, String newsubstr ) {
		String oldfilename = getLogFilename(appendername);
		String newfilename = oldfilename.replace(oldsubstr, newsubstr);
		setLogFilename(appendername, newfilename);
	}
		
	public void close(String appendername) {
		Appender appender = logger.getAppender(appendername);
		appender.close();
	}
	
	/**
	 * @param loggerName
	 * @return
	 */
	public static IntgSrvLogger getInstance(String loggerName) {
		return IntgSrvLogManager.getLogger("" + loggerName);
	}
	/**
	 * @param modulePrefix
	 * @param loggerName
	 * @return
	 */
	public static IntgSrvLogger getInstance(String modulePrefix, String loggerName) {
		return IntgSrvLogManager.getLogger("|" + modulePrefix + "|" + loggerName);
	}
	/**
	 * @param clazz
	 * @return
	 */
	public static IntgSrvLogger getInstance(Class clazz) {
		return IntgSrvLogManager.getLogger("" + clazz.getName());
	}
	/**
	 * @param modulePrefix
	 * @param clazz
	 * @return
	 */
	public static IntgSrvLogger getInstance(String modulePrefix, Class clazz) {
		return IntgSrvLogManager.getLogger("|" + modulePrefix + "|"
				+ clazz.getName());
	}
	
	/* sima added logic to support shared resource with FDPO  start changes */ 
	/**
	 * @param loggerName
	 * @return
	 */
	public static IntgSrvLogger getInstanceFDPO(String loggerName) {
		return IntgSrvLogManager.getLogger("" + loggerName);
	}
	/**
	 * @param modulePrefix
	 * @param loggerName
	 * @return
	 */
	public static IntgSrvLogger getInstanceFDPO(String modulePrefix, String loggerName) {
		return IntgSrvLogManager.getLogger("|" + modulePrefix + "|" + loggerName);
	}
	/**
	 * @param clazz
	 * @return
	 */
	public static IntgSrvLogger getInstanceFDPO(Class clazz) {
		return IntgSrvLogManager.getLogger("" + clazz.getName());
	}
	 
	/**
	 * @param modulePrefix
	 * @param clazz
	 * @return
	 */
	public static IntgSrvLogger getInstanceFDPO(String modulePrefix, Class clazz) {
		return IntgSrvLogManager.getLogger("|" + modulePrefix + "|"
				+ clazz.getName());
	}
	
	/* sima added logic to support shared resource with FDPO  end changes */ 
	
	/**
	 * @param msg
	 */
	public void debug(Object msg) {
		if(isDebugEnabled()){
			logger.debug(msg);
		}
	}
	
	public void debug(String clazzname, String methodname, Object msg) {
		if(isDebugEnabled()){
			logger.debug(clazzname + "." + methodname + " | " + msg);
		}
	}
	/**
	 * @param msg
	 * @param t
	 */
	public void debug(Object msg, Throwable t) {
		if(isDebugEnabled()){
			logger.debug(msg, t);
		}
	}
	public void debug(String clazzname, String methodname, Object msg, Throwable t) {
		if(isDebugEnabled()){
			logger.debug(clazzname + "." + methodname + " | " + msg, t);
		}
	}
	/**
	 * @param msg
	 */
	public void info(Object msg) {
		logger.info(msg);
	}
	public void info(String clazzname, String methodname, Object msg) {
		logger.info(clazzname + "." + methodname + " | " + msg);
	}
	/**
	 * @param msg
	 * @param t
	 */
	public void info(Object msg, Throwable t) {
		logger.info(msg, t);
	}
	public void info(String clazzname, String methodname, Object msg, Throwable t) {
		logger.info(clazzname + "." + methodname + " | " + msg, t);
	}
	/**
	 * @param msg
	 */
	public void warn(Object msg) {
		logger.warn(msg);
	}
	public void warn(String clazzname, String methodname, Object msg) {
		logger.warn(clazzname + "." + methodname + " | " + msg);
	}
	/**
	 * @param msg
	 * @param t
	 */
	public void warn(Object msg, Throwable t) {
		logger.warn(msg, t);
	}
	public void warn(String clazzname, String methodname, Object msg, Throwable t) {
		logger.warn(clazzname + "." + methodname + " | " + msg, t);
	}
	/**
	 * @param msg
	 */
	public void error(Object msg) {
		logger.error(msg);
	}
	public void error(String clazzname, String methodname, Object msg) {
		logger.error(clazzname + "." + methodname + " | " + msg);
	}
	/**
	 * @param msg
	 * @param t
	 */
	public void error(Object msg, Throwable t) {
		logger.error(msg, t);
	}
	public void error(String clazzname, String methodname, Object msg, Throwable t) {
		logger.error(clazzname + "." + methodname + " | " + msg, t);
	}
	/**
	 * @param msg
	 */
	public void fatal(Object msg) {
		logger.fatal(msg);
	}
	public void fatal(String clazzname, String methodname, Object msg) {
		logger.fatal(clazzname + "." + methodname + " | " + msg);
	}
	/**
	 * @param msg
	 * @param t
	 */
	public void fatal(Object msg, Throwable t) {
		logger.fatal(msg, t);
	}
	public void fatal(String clazzname, String methodname, Object msg, Throwable t) {
		logger.fatal(clazzname + "." + methodname + " | " + msg, t);
	}
	/**
	 * @return
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	/**
	 * @return
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
}
