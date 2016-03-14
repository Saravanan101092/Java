package com.message.view;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.springframework.jms.listener.SessionAwareMessageListener;


public class WMQueueMessageConsumer implements SessionAwareMessageListener{
	
	public WMQueueMessageConsumer(){
	
	}
	
	@Override
	public void onMessage(Message arg0, Session arg1) throws JMSException {
		// TODO Auto-generated method stub
		System.out.println("Message received");
		TextMessage mess=(TextMessage)arg0;
		System.out.println(voToString(arg0)); 
		MQWindow.sendMessage(mess.getText());
	}

	
	public static String voToString(Object obj) {

		StringBuffer buf = new StringBuffer();
		if (obj != null) {
			buf.append("<Object::" + obj.getClass().getName() + "> ");
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Object parameters[] = {};
				if ((methods[i].getName().indexOf("get") == 0 || methods[i]
						.getName().indexOf("is") == 0)
						&& !(methods[i].getName().equalsIgnoreCase("getClass"))) {
					if (methods[i].getGenericParameterTypes().length == 0) {
						try {
							buf.append("<" + methods[i].getName().substring(3)
									+ "::" + methods[i].invoke(obj, parameters)
									+ ">");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			buf.append("Given Object Is Null");
		}
		return buf.toString();
	}

}
