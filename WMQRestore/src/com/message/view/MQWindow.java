/*
 * Created by JFormDesigner on Thu Mar 12 17:19:42 IST 2015
 */

package com.message.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.springframework.jms.listener.SessionAwareMessageListener;

import com.ibm.mq.MQException;
import com.message.publisher.StepMessageSender;

/**
 * @author Saravanan Sampath
 */
public class MQWindow extends JFrame{
	private static java.util.Map<Integer,String> messageList;
	private static int count=0;
	public MQWindow(){
		
	}
	
	/*public void onMessage(Message arg0, Session arg1) throws JMSException {
		// TODO Auto-generated method stub
		System.out.println("Message received!");

		JOptionPane.showMessageDialog(null,"New Message received!! \nLength : "+arg0.toString().length());
		TextMessage message = (TextMessage) arg0;
		String messagetext=message.getText();
		textArea1.setText(messagetext);
		count++;
		messageList.put(count,messagetext);
		UserMessage.setForeground(Color.blue);
		UserMessage.setText("Message Entry :"+count);
		System.out.println("message is "+message.getText());
		
	}*/
	public static void showErrorMessage(String message,Color color){
		UserMessage.setForeground(color);
		UserMessage.setText(message);
	}
	
	public static void sendMessage(String message){
		JOptionPane.showMessageDialog(null, "New message received!");
		int thisentry=messageList.size()+1;
		messageList.put(thisentry, message);
		resetButton.setEnabled(true);
		resetButton2.setEnabled(true);
		
		label1.setText("Total Number of messages:"+messageList.size());
		if(thisentry==1){
		textArea1.setText(messageList.get(thisentry));
		count=1;
		}
		System.out.println(messageList.get(thisentry));
		
	}
	
	private void menuItem2ActionPerformed(ActionEvent e) {
		// TODO add your code here
		System.exit(0);
	}

	private void checkBox1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		JCheckBox checkbox=(JCheckBox)e.getSource();
		if(checkbox.isSelected()){
			textField1.setEditable(true);
			browseButton.setEnabled(true);
		}else{
			textField1.setEditable(false);
			textField1.setText("");
			browseButton.setEnabled(false);
		}

	}

	private void button3ActionPerformed(ActionEvent e) {
		// TODO add your code here
		if(!resetButton.getText().equals("Previous")){
		
		textArea1.setEditable(true);
		textArea1.setForeground(Color.BLACK);
		textArea1.setText("");
		UserMessage.setForeground(Color.black);
		UserMessage.setText("");
		textField1.setText("");
		}else{
			if((messageList.get(count-1)!=null)&&(messageList.get(count-1)!="")){
			count--;
			resetButton2.setEnabled(true);
			UserMessage.setForeground(Color.blue);
			UserMessage.setText("Message Entry : "+count);
			textArea1.setText(messageList.get(count));
			}else{
				resetButton.setEnabled(false);resetButton2.setEnabled(true);
			}
		}
	}

	private void menuItem1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		EditConfigurationsDialog editconfigurationsdialog=new EditConfigurationsDialog(this);
		if(radioButtonMenuItem1.isSelected()){
		editconfigurationsdialog.setPropertyValues(2);
		}else{
			editconfigurationsdialog.setPropertyValues(1);
		}
		editconfigurationsdialog.setVisible(true);
	}



	private void browseButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
		JFileChooser filechooser=new JFileChooser();
		int result=filechooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){

			File file=filechooser.getSelectedFile();
			textField1.setText(file.getPath());
			FileReader reader=null;
			textArea1.setEditable(true);
			textArea1.setForeground(Color.BLACK);
			try {
				reader = new FileReader(file);
				//BufferedReader buf=new BufferedReader(reader);
				textArea1.setForeground(Color.BLACK);
				textArea1.read(reader, null);
				UserMessage.setForeground(Color.GREEN);
				UserMessage.setText("File loaded successfully!!");
			} catch (FileNotFoundException ex) {
				// TODO Auto-generated catch block
				UserMessage.setForeground(Color.RED);
				UserMessage.setText("File not found!! Please enter the correct path");
				ex.printStackTrace();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				UserMessage.setForeground(Color.RED);
				UserMessage.setText("Error while reading from file!!");
				ex.printStackTrace();
			}
		}
	}


	private void publishButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
		
		if(publishButton.getText().equals("Publish Message")){
		if(!textArea1.getText().equals("")){
			PublishMessageIntimator messageintimator=new PublishMessageIntimator(this);
			String message=textArea1.getText();

			UserMessage.setForeground(Color.blue);
			UserMessage.setText("Publishing message to the queue..");	
			//	File file = new File("C:/mqconfig/configurations/message.txt");
			try {
				//		FileWriter writer=new FileWriter(file);
				//		textArea1.write(writer);
				//		writer.close();


				//executing jar file
				/*Process cmd;
				Runtime re = Runtime.getRuntime();
				textArea1.setEditable(false);
				BufferedReader output=null;  messageintimator.setVisible(true);		
				cmd = re.exec("java -jar C:/saravanan/message_publisher/SpringMQPublisher.jar");

				messageintimator.setVisible(false);
				output =  new BufferedReader(new InputStreamReader(cmd.getInputStream()));
				textArea1.read(output, null);*/
				messageintimator.setVisible(true);

				StepMessageSender.messageSender(message);
				JOptionPane.showMessageDialog(null, "Message published to the queue successfully..");
				textArea1.setText("");
				UserMessage.setForeground(Color.green);
				UserMessage.setText("Message published to the queue successfully..");	
				
				textField1.setText("");/*if(textArea1.getText().endsWith(" Before send..\n")){
					UserMessage.setForeground(Color.red);
					UserMessage.setText("MESSAGE SENDING FAILED.. CHECK CONNECTION STATUS.");
				}else{
					UserMessage.setForeground(Color.green);
					UserMessage.setText("MESSAGE PUBLISHED TO THE QUEUE SUCCESSFULLY!!");	
				}*/

			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				UserMessage.setForeground(Color.RED);
				UserMessage.setText("Error in MQ connectivity. Please check network connection!");
				JOptionPane.showMessageDialog(null, "Error in MQ connectivity!! Please check network connection.");
				ex.printStackTrace();
			}

		}else{
			UserMessage.setForeground(Color.RED);
			UserMessage.setText("Error!! load a message to publish first.");
		}
		}else if(publishButton.getText().equals("GetMessages")){
			resetButton.setEnabled(true);
			resetButton2.setEnabled(true);
			
			UserMessage.setForeground(Color.blue);
			UserMessage.setText(messageList.size()+ " Messages received! Message Entry : 1");
			count=1;
			System.out.println(messageList.get(1));
			textArea1.setText(messageList.get(1));
		}
	}

	

	private void radioButtonMenuItem2ActionPerformed(ActionEvent e) {
		// TODO add your code here
		ContextLoader.getContextloader().getCtx().close();
		modeLabel.setText("SENDER MODE");
		label1.setText("Enter the path of the Message file to be loaded");
		checkBox1.setVisible(true);
		label1.setVisible(true);
		textField1.setVisible(true);
		resetButton2.setVisible(false);
		textArea1.setEditable(true);
		browseButton.setVisible(true);
		resetButton.setEnabled(true);
		resetButton2.setEnabled(true);
		saveButton.setVisible(false);
		saveAllButton.setVisible(false);
		textArea1.setForeground(Color.BLACK);
		textArea1.setText("");
		UserMessage.setForeground(Color.black);
		UserMessage.setText("");
		publishButton.setText("Publish Message");
		resetButton.setText("Reset");
		JOptionPane.showMessageDialog(null, "WMQ is set to the Sender mode!");
		
	}

	private void radioButtonMenuItem1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		
		resetButton2.setVisible(true);
		textArea1.setEditable(true);
		textArea1.setForeground(Color.BLACK);
		textArea1.setText("");
		UserMessage.setForeground(Color.blue);
		UserMessage.setText("Listening to the queue..");
		checkBox1.setVisible(false);
		label1.setText("Total Number of messages:"+messageList.size());
		textField1.setVisible(false);
		resetButton.setEnabled(true);
		resetButton2.setEnabled(true);
		saveButton.setVisible(true);
		saveAllButton.setVisible(true);
		browseButton.setVisible(false);
		modeLabel.setText("LISTENER MODE");
		publishButton.setText("GetMessages");
		resetButton.setText("Previous");
		try {
			JOptionPane.showMessageDialog(null, "WMQ is set to the listener mode!");
			EditConfigurationsDialog edit=new EditConfigurationsDialog(this);
			edit.setVisible(true);
			edit.setPropertyValues(2);
			//JOptionPane.showMessageDialog(null, "hjinhksdfjn!");
			//while(edit.isVisible()){}
		
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UserMessage.setForeground(Color.red);
			UserMessage.setText("Error while listening to the queue! Check network connection");
		}

	}
	public void initialize(){
		initComponents();
		messageList=new HashMap<Integer,String>();
	}

	private void resetButton2ActionPerformed(ActionEvent e) {
		// TODO add your code here
		if((messageList.get(count+1)!=null)&&(messageList.get(count+1)!="")){
		count++;
		
		resetButton.setEnabled(true);
		UserMessage.setForeground(Color.blue);
		UserMessage.setText("Message Entry : "+count);
		textArea1.setText(messageList.get(count));
		}else{
			resetButton2.setEnabled(false);resetButton.setEnabled(true);
		}
	}

	private void button1ActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void menuItem3ActionPerformed(ActionEvent e) {
		// TODO add your code here
		QueueDetails queuedetailswindow;
		try {
			queuedetailswindow = new QueueDetails(this, radioButtonMenuItem2.isSelected());
			queuedetailswindow.setVisible(true);
		} catch (MQException e1) {
			// TODO Auto-generated catch block
			UserMessage.setForeground(Color.red);
			UserMessage.setText("Could not connect to the Queue. Check network connections!");
			e1.printStackTrace();
		}
		
	}
	private void initComponents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Saravanan Sampath
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		menuItem3 = new JMenuItem();
		menuItem2 = new JMenuItem();
		menu2 = new JMenu();
		radioButtonMenuItem1 = new JRadioButtonMenuItem();
		radioButtonMenuItem2 = new JRadioButtonMenuItem();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		checkBox1 = new JCheckBox();
		label1 = new JLabel();
		textField1 = new JTextField();
		browseButton = new JButton();
		UserMessage = new JLabel();
		publishButton = new JButton();
		resetButton = new JButton();
		saveButton=new JButton();
		saveAllButton=new JButton();
		label2 = new JLabel();
		modeLabel = new JLabel();
		resetButton2 = new JButton();

		//======== this ========
		setBackground(new Color(204, 204, 255));
		setFont(new Font("Verdana", Font.PLAIN, 16));
		setResizable(false);
		setTitle("WMQ MESSAGE PUBLISHER");
		setIconImage(new ImageIcon(getClass().getResource("/staples.png")).getImage());
		setVisible(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//======== menuBar1 ========
		{

			//======== menu1 ========
			{
				menu1.setText("File");
				menu1.setFont(new Font("Times New Roman", Font.PLAIN, 14));

				//---- menuItem1 ----
				menuItem1.setText("Change Configurations");
				menuItem1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem1ActionPerformed(e);
					}
				});
				menu1.add(menuItem1);

				//---- menuItem3 ----
				menuItem3.setText("Queue Details");
				menuItem3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem3ActionPerformed(e);
					}
				});
				menu1.add(menuItem3);

				//---- menuItem2 ----
				menuItem2.setText("Exit");
				menuItem2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem2ActionPerformed(e);
					}
				});
				menu1.add(menuItem2);
			}
			menuBar1.add(menu1);

			//======== menu2 ========
			{
				menu2.setText("Receiver/Sender");

				//---- radioButtonMenuItem1 ----
				radioButtonMenuItem1.setText("Receiver");
				radioButtonMenuItem1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						radioButtonMenuItem1ActionPerformed(e);
					}
				});
				menu2.add(radioButtonMenuItem1);

				//---- radioButtonMenuItem2 ----
				radioButtonMenuItem2.setText("Sender");
				radioButtonMenuItem2.setSelected(true);
				radioButtonMenuItem2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						radioButtonMenuItem2ActionPerformed(e);
					}
				});
				menu2.add(radioButtonMenuItem2);
			}
			menuBar1.add(menu2);
		}
		setJMenuBar(menuBar1);

		//======== scrollPane1 ========
		{

			//---- textArea1 ----
			textArea1.setFont(new Font("Verdana", Font.PLAIN, 13));
			scrollPane1.setViewportView(textArea1);
		}
		contentPane.add(scrollPane1);
		scrollPane1.setBounds(20, 105, 870, 440);

		//---- checkBox1 ----
		checkBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkBox1ActionPerformed(e);
			}
		});
		contentPane.add(checkBox1);
		checkBox1.setBounds(new Rectangle(new Point(35, 30), checkBox1.getPreferredSize()));

		//---- label1 ----
		label1.setText("Enter the path of the Message file to be loaded");
		label1.setFont(new Font("Verdana", Font.BOLD, 12));
		contentPane.add(label1);
		label1.setBounds(65, 25, 315, 35);

		//---- textField1 ----
		textField1.setEditable(false);
		contentPane.add(textField1);
		textField1.setBounds(380, 25, 380, 30);

		//---- browseButton ----
		browseButton.setText("Browse");
		browseButton.setEnabled(false);
		browseButton.setFont(new Font("Verdana", Font.BOLD, 14));
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
				browseButtonActionPerformed(e);
			}
		});
		contentPane.add(browseButton);
		browseButton.setBounds(760, 25, 100, 30);

		//---- UserMessage ----
		UserMessage.setBackground(new Color(249, 230, 246));
		UserMessage.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(UserMessage);
		UserMessage.setBounds(20, 65, 895, 35);

		//---- publishButton ----
		publishButton.setText("Publish Message");
		publishButton.setFont(new Font("Verdana", Font.BOLD, 14));
		publishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				publishButtonActionPerformed(e);
			}
		});
		contentPane.add(publishButton);
		publishButton.setBounds(20, 560, 175, 30);

		//---- resetButton ----
		resetButton.setText("Reset");
		resetButton.setFont(new Font("Verdana", Font.BOLD, 14));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button3ActionPerformed(e);
			}
		});
		contentPane.add(resetButton);
		resetButton.setBounds(205, 560, 175, 30);

		//--- saveButton and saveAllButton
		saveButton.setText("Save");
		saveButton.setFont(new Font("Verdana", Font.BOLD, 14));
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				savebuttonActionPerformed(arg0);
			}
			
		});
		contentPane.add(saveButton);
		saveButton.setBounds(575, 560, 175, 30);
		saveButton.setVisible(false);
		
		saveAllButton.setText("Save All");
		saveAllButton.setFont(new Font("Verdana", Font.BOLD, 14));
		saveAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveAllbuttonActionPerformed(arg0);
			}
			
		});
		contentPane.add(saveAllButton);
		saveAllButton.setBounds(760, 560, 175, 30);
		saveAllButton.setVisible(false);
		
		
		//---- label2 ----
		label2.setText("Copyrights@STIBO springbatch");
		contentPane.add(label2);
		label2.setBounds(380, 605, 180, label2.getPreferredSize().height);

		//---- modeLabel ----
		modeLabel.setText("SENDER MODE");
		modeLabel.setFont(new Font("Verdana", Font.BOLD, 18));
		contentPane.add(modeLabel);
		modeLabel.setBounds(0, 0, 200, 20);

		//---- resetButton2 ----
		resetButton2.setText("Next");
		resetButton2.setFont(new Font("Verdana", Font.BOLD, 14));
		resetButton2.setVisible(false);
		resetButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetButton2ActionPerformed(e);
			}
		});
		contentPane.add(resetButton2);
		resetButton2.setBounds(390, 560, 175, 30);

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

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButtonMenuItem1);
		buttonGroup1.add(radioButtonMenuItem2);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	protected void saveAllbuttonActionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File folder=new File("savedfiles");
		if(!folder.exists()){
			folder.mkdir();
		}
		File file; FileOutputStream fileWriter;
		try {
			for(int i=0;i<messageList.size();i++){
				file=new File(new Date().getTime()+"_Message_"+i);
				fileWriter=new FileOutputStream(file);
				fileWriter.write(messageList.get(i).getBytes());
				fileWriter.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	protected void savebuttonActionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		File folder=new File("D://saravanan/WMQ/savedfiles");
		File file=new File("D://saravanan/WMQ/savedfiles/"+new Date().getTime()+"_Message_"+count+".txt");
		if(!folder.exists()){
			folder.mkdirs();
		}
		try {
			if(messageList.size()==0){
				JOptionPane.showMessageDialog(this, "No File to save!");
			}else{
			FileOutputStream fileWriter=new FileOutputStream(file);
			fileWriter.write(messageList.get(count).getBytes());
			fileWriter.close();
			JOptionPane.showMessageDialog(this, "file saved at :"+file.getPath()+"! Hence will be deleted from WMQ explorer!");
			messageList.remove(count);
			System.out.println("file saved at :"+file.getPath());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error!");
		} catch(Exception e){
			JOptionPane.showMessageDialog(this, "No File to save!");
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Saravanan Sampath
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JMenuItem menuItem3;
	private JMenuItem menuItem2;
	private JMenu menu2;
	private JRadioButtonMenuItem radioButtonMenuItem1;
	private JRadioButtonMenuItem radioButtonMenuItem2;
	private JScrollPane scrollPane1;
	private static JTextArea textArea1;
	private JCheckBox checkBox1;
	private static JLabel label1;
	private JTextField textField1;
	private JButton browseButton;
	private static JLabel UserMessage;
	private JButton publishButton;
	private static JButton resetButton;
	private JButton saveButton;
	private JButton saveAllButton;
	private JLabel label2;
	private JLabel modeLabel;
	private static JButton resetButton2;
	//private static List<String> savedFiles; aborted
	
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
