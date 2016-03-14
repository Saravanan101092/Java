package com.message.view;


import java.awt.Color;
import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;


public class CustomException implements ExceptionListener {
	

	
	public static int retryCount = 0;
	
	
	
	public void onException(JMSException arg0) {
		
		System.err.println("Exception occurred "+ arg0);
		System.out.println("Listener Exception!");
		arg0.printStackTrace();
		MQWindow.showErrorMessage("Could not connect to the Queue. Check network connectivity!",Color.red);
		
		
		}
 

}
