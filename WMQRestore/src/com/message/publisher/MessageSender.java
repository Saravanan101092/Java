package com.message.publisher;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.swing.JOptionPane;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import com.ibm.mq.MQException;
import com.ibm.mq.jms.MQQueue;

/**
 * This class used to send the message to STEP queue
 * 
 * @author 522462
 */
public class MessageSender {

	/**
	 * JMSTemplate.
	 */
	private JmsTemplate jmsTemplateStep;
	

	/**
	 * JMSTemplate.
	 */
	private Destination jmsDestination;

	/**
	 * This method used to send a message to STEP queue.
	 */
	public void sendMessage(final String message) throws Exception {
		jmsTemplateStep.send(jmsDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage(message);
			}
		});
		System.out.println("Message Sent......");
		JOptionPane.showMessageDialog(null, "Message Sent......");
	}

	/**
	 * Sets jmsTemplate to the given value.
	 * 
	 * @param jmsTemplate
	 *            the jmsTemplate to set
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplateStep = jmsTemplate;
	}

	/**
	 * Sets jmsDestination to the given value.
	 * 
	 * @param jmsDestination
	 *            the jmsDestination to set
	 * @throws MQException 
	 */
	public void setJmsDestination(Destination jmsDestination) throws MQException {
		
		this.jmsDestination = jmsDestination;
	}
	
	/**
	 * @return
	 */
	public JmsTemplate getJmsTemplateStep() {
		return jmsTemplateStep;
	}

	/**
	 * @param jmsTemplateStep
	 */
	public void setJmsTemplateStep(JmsTemplate jmsTemplateStep) {
		this.jmsTemplateStep = jmsTemplateStep;
	}

	/**
	 * @return
	 */
	public Destination getJmsDestination() {
		return jmsDestination;
	}
}