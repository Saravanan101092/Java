package com.message.view;



import org.springframework.context.ConfigurableApplicationContext;


import org.springframework.context.support.GenericXmlApplicationContext;


/**
 * 
 * This class used to load the Context.xml
 * 
 * @author 522462
 * 
 */
public class ContextLoader {
	

	private static ContextLoader contextLoader; 

	private GenericXmlApplicationContext ctx = null;
	private String PATH_TYPE="classpath:";
	private static String CONTEXT_FILE="context_publisher.xml";
	private ContextLoader() {

	}
	public GenericXmlApplicationContext getCtx(){
		return this.ctx;
	}
	public static ContextLoader getContextloader(){
		return contextLoader;
	}
	public static ContextLoader getInstanceLoadContext(int mode) {
		if (contextLoader == null) {
			contextLoader = new ContextLoader();
		}
		if(mode==1){
			CONTEXT_FILE="context_publisher.xml";
		}else if(mode==2){
			CONTEXT_FILE="context_receiver.xml";
		}
		return contextLoader;
	}
	public static void destroycontext(){
		if (contextLoader != null) {
			contextLoader = new ContextLoader();
			
		}
	}
	public GenericXmlApplicationContext loadContext(String argStr) {

	
		
		if (ctx == null) {
			ctx = new GenericXmlApplicationContext(); 
			ctx.load(PATH_TYPE+CONTEXT_FILE);
			
			System.out.println("Context file loaded : "+PATH_TYPE+CONTEXT_FILE);
			ctx.refresh();
		}else{
			ctx.destroy();
			ctx = new GenericXmlApplicationContext(); 
			ctx.load(PATH_TYPE+CONTEXT_FILE);
			
			System.out.println("Context file loaded : "+PATH_TYPE+CONTEXT_FILE);
			ctx.refresh();
		}
		return ctx;
	}
	
	

}
