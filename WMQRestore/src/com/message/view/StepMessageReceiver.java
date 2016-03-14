package com.message.view;

import javax.swing.JOptionPane;

import org.springframework.context.support.GenericXmlApplicationContext;



/**
 * This class contains methods to fetch the connection and bind the message
 * sender class
 * 
 * @author 522462
 * 
 */
public class StepMessageReceiver {

	/**
	 * This method used to call the MessageSender’s method using context bean
	 * 
	 * @param msg
	 *            Step XML
	 */
	public static void messageReceiver() throws Exception{
		
		GenericXmlApplicationContext ctx = ContextLoader
				.getInstanceLoadContext(2).loadContext(null);
		
	}

	/*private static void printConsole(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}*/

}
