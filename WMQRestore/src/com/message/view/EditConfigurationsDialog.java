

package com.message.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.*;

/**
 * @author Saravanan Sampath
 */
public class EditConfigurationsDialog extends JDialog {
	private int mode=0;
	public EditConfigurationsDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public EditConfigurationsDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	public void setPropertyValues(int opmode){
		Properties prop=new Properties();
		this.mode=opmode;
		String fileString="";
		if(mode==1){
			fileString="/config-env-sender.properties";
		}else{
			fileString="/config-env-receiver.properties";
		}
		File jarPath=new File(MessagePublisher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path=jarPath.getParent()+fileString;
		File file=new File(path);
		try {
			prop.load(new FileReader(file));
			hostnameText.setText(prop.getProperty("mq.hostname.step"));
			portText.setText(prop.getProperty("mq.port.step"));
			queuemanagerText.setText(prop.getProperty("mq.queuemanager.step"));
			channelText.setText(prop.getProperty("mq.channel.step"));
			queuenameText.setText(prop.getProperty("mq.queuename.step"));
			timeoutText.setText(prop.getProperty("mq.timeout.step"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savePropertyValues(int mode){
		Properties prop=new Properties();
		OutputStream out=null;
		String fileString="";
		if(mode==1){
			fileString="/config-env-sender.properties";
		}else{
			fileString="/config-env-receiver.properties";
		}
		File jarPath=new File(MessagePublisher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path=jarPath.getParent()+fileString;
		File file=new File(path);
		try {

			prop.load(new FileReader(file));
			prop.setProperty("mq.hostname.step",hostnameText.getText());
			prop.setProperty("mq.port.step",portText.getText());
			prop.setProperty("mq.queuemanager.step",queuemanagerText.getText());
			prop.setProperty("mq.channel.step",channelText.getText());
			prop.setProperty("mq.queuename.step",queuenameText.getText());
			prop.setProperty("mq.timeout.step",timeoutText.getText());
			file.createNewFile();
			out = new FileOutputStream(file);
			prop.store(out, "modified by mq publisher");
			out.close();
			if(mode!=1){
			StepMessageReceiver.messageReceiver();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MQWindow.showErrorMessage("Could not listen to the queue! Check network connection!",Color.red);
			e.printStackTrace();
		}
	}
	
	private void button1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		JButton button=(JButton)e.getSource();
		if(button.getText().equals("EDIT")){
			hostnameText.setEditable(true);
			portText.setEditable(true);
			queuemanagerText.setEditable(true);
			channelText.setEditable(true);
			queuenameText.setEditable(true);
			timeoutText.setEditable(true);
			button.setText("SAVE");
			MQWindow.showErrorMessage("",Color.black);
		}else if(button.getText().equals("SAVE")){
			savePropertyValues(mode);
			button.setText("EDIT");
			MQWindow.showErrorMessage("Configuration properties saved!!",Color.blue);
			this.setVisible(false);
		}
	}

	private void button2ActionPerformed(ActionEvent e) {
		// TODO add your code here
		if(mode!=1){
		try {
			StepMessageReceiver.messageReceiver();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		this.setVisible(false);
		
	}

	private void initComponents() {
	
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		hostnameText = new JTextField();
		portText = new JTextField();
		queuemanagerText = new JTextField();
		channelText = new JTextField();
		queuenameText = new JTextField();
		timeoutText = new JTextField();
		button1 = new JButton();
		button2 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- label1 ----
		label1.setText("EDIT QUEUE CONFIGURATIONS");
		label1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(label1);
		label1.setBounds(70, 10, 320, 25);

		//---- label2 ----
		label2.setText("HOSTNAME");
		label2.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label2);
		label2.setBounds(15, 60, 205, 25);

		//---- label3 ----
		label3.setText("PORT");
		label3.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label3);
		label3.setBounds(15, 90, 205, 25);

		//---- label4 ----
		label4.setText("QUEUE MANAGER");
		label4.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label4);
		label4.setBounds(15, 120, 205, 25);

		//---- label5 ----
		label5.setText("CHANNEL");
		label5.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label5);
		label5.setBounds(15, 150, 205, 25);

		//---- label6 ----
		label6.setText("QUEUE NAME");
		label6.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label6);
		label6.setBounds(15, 180, 205, 25);

		//---- label7 ----
		label7.setText("TIMEOUT");
		label7.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(label7);
		label7.setBounds(15, 210, 205, 25);

		//---- hostnameText ----
		hostnameText.setEditable(false);
		contentPane.add(hostnameText);
		hostnameText.setBounds(220, 60, 325, 25);

		//---- portText ----
		portText.setEditable(false);
		contentPane.add(portText);
		portText.setBounds(220, 90, 325, 25);

		//---- queuemanagerText ----
		queuemanagerText.setEditable(false);
		contentPane.add(queuemanagerText);
		queuemanagerText.setBounds(220, 120, 325, 25);

		//---- channelText ----
		channelText.setEditable(false);
		contentPane.add(channelText);
		channelText.setBounds(220, 150, 325, 25);

		//---- queuenameText ----
		queuenameText.setEditable(false);
		contentPane.add(queuenameText);
		queuenameText.setBounds(220, 180, 325, 25);

		//---- timeoutText ----
		timeoutText.setEditable(false);
		contentPane.add(timeoutText);
		timeoutText.setBounds(220, 210, 325, 25);

		//---- button1 ----
		button1.setText("EDIT");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		contentPane.add(button1);
		button1.setBounds(20, 265, 145, 30);

		//---- button2 ----
		button2.setText("CANCEL");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});
		contentPane.add(button2);
		button2.setBounds(300, 265, 135, 28);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		pack();
		setLocationRelativeTo(getOwner());
	
	}

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JTextField hostnameText;
	private JTextField portText;
	private JTextField queuemanagerText;
	private JTextField channelText;
	private JTextField queuenameText;
	private JTextField timeoutText;
	private JButton button1;
	private JButton button2;
	
}
