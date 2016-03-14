/*
 * Created by JFormDesigner on Fri Mar 13 12:33:27 IST 2015
 */

package com.message.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;

/**
 * @author Saravanan Sampath
 */
public class PublishMessageIntimator extends JDialog {
	public PublishMessageIntimator(Frame owner) {
		super(owner);
		initComponents();
	}

	public void setPropertyValues(){
		Properties prop=new Properties();
		File jarPath=new File(MessagePublisher.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		File file=new File(jarPath.getParent()+"/config-env-sender.properties");
		try {
			prop.load(new FileReader(file));
			
			hostnameLabel.setText(prop.getProperty("mq.hostname.step"));
			portLabel.setText(prop.getProperty("mq.port.step"));
			queuemanagerlabel.setText(prop.getProperty("mq.queuemanager.step"));
			channelLabel.setText(prop.getProperty("mq.channel.step"));
			queuenameLabel.setText(prop.getProperty("mq.queuename.step"));
			timeoutLabel.setText(prop.getProperty("mq.timeout.step"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PublishMessageIntimator(Dialog owner) {
		super(owner);
		initComponents();
		
	}

	private void button1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		this.setVisible(false);
		
	}

	private void initComponents() {
		
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Saravanan Sampath
		label1 = new JLabel();
		label2 = new JLabel();
		panel1 = new JPanel();
		label4 = new JLabel();
		label5 = new JLabel();
		label8 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		hostnameLabel = new JLabel();
		portLabel = new JLabel();
		queuemanagerlabel = new JLabel();
		channelLabel = new JLabel();
		queuenameLabel = new JLabel();
		timeoutLabel = new JLabel();
		button1 = new JButton();
		setPropertyValues();
		//======== this ========
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- label1 ----
		label1.setText("MESSAGE BEING PUBLISHED TO THE QUEUE");
		label1.setFont(new Font("Verdana", Font.BOLD, 16));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label1);
		label1.setBounds(10, 5, 425, 30);

		//---- label2 ----
		label2.setText("HOSTNAME :");
		label2.setFont(new Font("Verdana", Font.BOLD, 16));
		label2.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label2);
		label2.setBounds(5, 60, 155, 25);

		//======== panel1 ========
		{

			// JFormDesigner evaluation mark
			panel1.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
					java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

			panel1.setLayout(null);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < panel1.getComponentCount(); i++) {
					Rectangle bounds = panel1.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = panel1.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				panel1.setMinimumSize(preferredSize);
				panel1.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(panel1);
		panel1.setBounds(new Rectangle(new Point(20, 175), panel1.getPreferredSize()));

		//---- label4 ----
		label4.setText("PORT :");
		label4.setFont(new Font("Verdana", Font.BOLD, 16));
		label4.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label4);
		label4.setBounds(5, 85, 155, 25);

		//---- label5 ----
		label5.setText("QUEUE MANAGER :");
		label5.setFont(new Font("Verdana", Font.BOLD, 16));
		label5.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label5);
		label5.setBounds(5, 110, 175, 25);

		//---- label8 ----
		label8.setText("CHANNEL :");
		label8.setFont(new Font("Verdana", Font.BOLD, 16));
		label8.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label8);
		label8.setBounds(5, 135, 155, 25);

		//---- label6 ----
		label6.setText("QUEUE NAME");
		label6.setFont(new Font("Verdana", Font.BOLD, 16));
		label6.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label6);
		label6.setBounds(5, 160, 155, 25);

		//---- label7 ----
		label7.setText("TIMEOUT");
		label7.setFont(new Font("Verdana", Font.BOLD, 16));
		label7.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(label7);
		label7.setBounds(5, 185, 155, 25);

		//---- hostnameLabel ----
		hostnameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		hostnameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(hostnameLabel);
		hostnameLabel.setBounds(175, 60, 290, 25);

		//---- portLabel ----
		portLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		portLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(portLabel);
		portLabel.setBounds(175, 85, 295, 25);

		//---- queuemanagerlabel ----
		queuemanagerlabel.setFont(new Font("Verdana", Font.BOLD, 14));
		queuemanagerlabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(queuemanagerlabel);
		queuemanagerlabel.setBounds(175, 110, 275, 25);

		//---- channelLabel ----
		channelLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		channelLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(channelLabel);
		channelLabel.setBounds(175, 135, 295, 25);

		//---- queuenameLabel ----
		queuenameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		queuenameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(queuenameLabel);
		queuenameLabel.setBounds(175, 160, 340, 25);

		//---- timeoutLabel ----
		timeoutLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		timeoutLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(timeoutLabel);
		timeoutLabel.setBounds(175, 185, 295, 25);

		//---- button1 ----
		button1.setText("CONTINUE");
		button1.setFont(new Font("Verdana", Font.BOLD, 14));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		contentPane.add(button1);
		button1.setBounds(160, 225, 135, 40);

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
	private JLabel label2;
	private JPanel panel1;
	private JLabel label4;
	private JLabel label5;
	private JLabel label8;
	private JLabel label6;
	private JLabel label7;
	private JLabel hostnameLabel;
	private JLabel portLabel;
	private JLabel queuemanagerlabel;
	private JLabel channelLabel;
	private JLabel queuenameLabel;
	private JLabel timeoutLabel;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
