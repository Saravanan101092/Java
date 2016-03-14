package com.message.publisher;

import javax.swing.JOptionPane;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.message.view.ContextLoader;


/**
 * This class contains methods to fetch the connection and bind the message
 * sender class
 * 
 * @author 522462
 * 
 */
public class StepMessageSender {

	/**
	 * This method used to call the MessageSender’s method using context bean
	 * 
	 * @param msg
	 *            Step XML
	 */
	public static void messageSender(String msg) throws Exception{
		
		GenericXmlApplicationContext ctx = ContextLoader
				.getInstanceLoadContext(1).loadContext(null);
		System.out.println("Message ----------->"+msg);
		MessageSender sender = ctx
				.getBean("messageSender", MessageSender.class);
		printConsole("Before send..");
		sender.sendMessage(msg);
		printConsole("After send..");
	}
	
	private static void printConsole(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}

}
