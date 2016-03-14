/*
 * Created by JFormDesigner on Thu Mar 19 16:34:58 IST 2015
 */

package com.message.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.*;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.message.receiver.QueueManager;

/**
 * @author Saravanan Sampath
 */
public class QueueDetails extends JDialog {
	private String hostname=null;
	private String port=null;
	private String channel=null;
	private String queuename=null;
	private String timeout=null;
	private String queuemanager=null;
	private MQQueue mq=null;
	public QueueDetails(Frame owner,boolean mode) throws MQException {
		super(owner);
		initComponents();
		getQueueDetailsFromConfigurations(mode);
		setvalues();
	}

	public QueueDetails(MQWindow owner,boolean mode) throws MQException {
		super(owner);
		initComponents();
		getQueueDetailsFromConfigurations(mode);
		setvalues();
	}
	public void getQueueDetailsFromConfigurations(boolean mode){
		String filename="";
		if(mode==true){
			filename="/config-env-sender.properties";
		}else{
			filename="/config-env-receiver.properties";
		}
		Properties prop=new Properties();
		File jarPath=new File(MessagePublisher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path=jarPath.getParent()+filename;
		File file=new File(path);
		try {
			prop.load(new FileReader(file));
			hostname=prop.getProperty("mq.hostname.step");
			port=prop.getProperty("mq.port.step");
			queuemanager=prop.getProperty("mq.queuemanager.step");
			channel=prop.getProperty("mq.channel.step");
			queuename=prop.getProperty("mq.queuename.step");
			timeout=prop.getProperty("mq.timeout.step");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getMQQueue() throws MQException{
		try {
			QueueManager queuemngr=new QueueManager(hostname, Integer.parseInt(port), channel, queuemanager);
			mq=queuemngr.getMQ(queuename);
			
		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}
	public void setvalues() throws MQException{
		getMQQueue();
		QnameLabel.setText(queuename);
		try {
			currentdepthLabel.setText(Integer.toString(mq.getCurrentDepth()));
			OpenInputLabel.setText(Integer.toString(mq.getOpenInputCount()));
			OpenOutputLabel.setText(Integer.toString(mq.getOpenOutputCount()));
			String text=mq.getDescription()+" \nMaximumDepth -"+mq.getMaximumDepth()+" \nMaximum Message Length -"+mq.getMaximumMessageLength()+" \nQueueType "+mq.getQueueType()+" \nSharability "+mq.getShareability()+" \nCreation time "+new SimpleDateFormat("YYYY-MM-dd-hh-mm-ss").format(mq.getCreationDateTime().getTimeInMillis());
			qTextArea.setText(text);
			mq.close();
		} catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void button1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		this.setVisible(false);
	}
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Saravanan Sampath
		label1 = new JLabel();
		QnameLabel = new JLabel();
		currentdepthLabel = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		OpenInputLabel = new JLabel();
		label7 = new JLabel();
		OpenOutputLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		qTextArea = new JTextArea();
		label9 = new JLabel();
		button1 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- label1 ----
		label1.setText("Queue Name             ");
		label1.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(label1);
		label1.setBounds(15, 50, 185, 25);

		//---- QnameLabel ----
		QnameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(QnameLabel);
		QnameLabel.setBounds(215, 50, 380, 25);

		//---- currentdepthLabel ----
		currentdepthLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(currentdepthLabel);
		currentdepthLabel.setBounds(215, 80, 310, 25);

		//---- label4 ----
		label4.setText("Current Depth          ");
		label4.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(label4);
		label4.setBounds(15, 80, 190, 25);

		//---- label5 ----
		label5.setText("Open Input Count ");
		label5.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(label5);
		label5.setBounds(15, 110, 190, 25);

		//---- OpenInputLabel ----
		OpenInputLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(OpenInputLabel);
		OpenInputLabel.setBounds(215, 110, 310, 25);

		//---- label7 ----
		label7.setText("Open Output Count ");
		label7.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(label7);
		label7.setBounds(15, 140, 190, 25);

		//---- OpenOutputLabel ----
		OpenOutputLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		contentPane.add(OpenOutputLabel);
		OpenOutputLabel.setBounds(215, 140, 310, 25);

		//======== scrollPane1 ========
		{

			//---- qTextArea ----
			qTextArea.setLineWrap(true);
			scrollPane1.setViewportView(qTextArea);
		}
		contentPane.add(scrollPane1);
		scrollPane1.setBounds(20, 185, 530, 100);

		//---- label9 ----
		label9.setText("Queue Details");
		label9.setFont(new Font("Verdana", Font.BOLD, 18));
		label9.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label9);
		label9.setBounds(85, 10, 445, 25);

		//---- button1 ----
		button1.setText("OK");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		contentPane.add(button1);
		button1.setBounds(205, 300, 125, button1.getPreferredSize().height);

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
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Saravanan Sampath
	private JLabel label1;
	private JLabel QnameLabel;
	private JLabel currentdepthLabel;
	private JLabel label4;
	private JLabel label5;
	private JLabel OpenInputLabel;
	private JLabel label7;
	private JLabel OpenOutputLabel;
	private JScrollPane scrollPane1;
	private JTextArea qTextArea;
	private JLabel label9;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
