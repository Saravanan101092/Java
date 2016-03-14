package com.tool.view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JCheckBox;

import com.tool.logic.XSVProcessor;

import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.Scrollbar;

import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;


public class MainWindowFrame {

	public static List<File> validFiles;
	public static JFrame frmXsvpro;
	public JTextField textField;
	public JComboBox comboBox;
	public static JTextArea textArea;
	public static JTextField noOfLinesText;
	public static JCheckBox chckbxSplitFiles;
	public JLabel lblEnterTheNo;
	public XSVProcessor xsvProcessor;
	public static File outputFolder;
	public static JTextArea ConsoleTextArea;
	public static JTextArea txtAreaMessageForUser;
	public static JCheckBox chckbxFindTextIn;
	public static JLabel lblKeyword;
	public static JCheckBox chckbxMatchCase;
	public static JTextField textField_1;
	public static JTextField textField_3;
	public static JLabel lblNoOfOutput;
	public static JLabel lblTotalNoOf;
	public static JCheckBox delimiterCheckBox;
	public JButton btnSelectDelimiter;
	public JButton btnNewButton;
	public static JComboBox comboBox_1;
	private JRadioButton rdbtnEnterLineNo;
	private JRadioButton rdbtnSelectFromFirst;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public JTextPane headerTextPane;
	private JScrollPane scrollPane_2;
	public static String[] headers;
	public static String delimiter;
	private JLabel lblLookForValue;
	private JLabel lblSelectColumn;
	public static JLabel lblKeywordsCount;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {
					MainWindowFrame window = new MainWindowFrame();
					window.frmXsvpro.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindowFrame() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmXsvpro = new JFrame();
		frmXsvpro.getContentPane().setBackground(new Color(204, 255, 204));
		
		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Load Input Files : ");
		lblNewLabel.setBounds(10, 5, 129, 20);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.setToolTipText("You can select a folder containing all the input files or you can select a single file. If a folder is selected, all the files in the folder will be processed.");
		comboBox.setBounds(149, 7, 87, 20);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Folder", "Select File"}));
		comboBox.setSelectedIndex(1);
		panel.add(comboBox);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(246, 6, 87, 23);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseButtonActionPerformed(e);
			}
		});
		panel.add(btnBrowse);
		
		textField = new JTextField();
		textField.setBounds(332, 7, 427, 20);
		textField.setColumns(10);
		panel.add(textField);
		
		frmXsvpro.getContentPane().add(panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_1.setBounds(332, 38, 427, 82);
		panel.add(scrollPane_1);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Verdana", Font.BOLD, 14));
		textArea.setBackground(Color.PINK);
		scrollPane_1.setViewportView(textArea);
		
		chckbxSplitFiles = new JCheckBox("Split File(s)");
		chckbxSplitFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splitFilesCheckBoxActionListener(e);
			}
		});
		chckbxSplitFiles.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxSplitFiles.setToolTipText("Check this checkbox to split the large files into smaller files. Files will be split based on the number of lines needed in the smaller files. Checking this will enable the other controls under this section.");
		chckbxSplitFiles.setBounds(10, 144, 101, 23);
		panel.add(chckbxSplitFiles);
		
		lblEnterTheNo = new JLabel("No of lines per output file:");
		lblEnterTheNo.setEnabled(false);
		lblEnterTheNo.setToolTipText("Enter the no of lines per output file. for eg if '10' is entered, the output files will contain 10 lines each.");
		lblEnterTheNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterTheNo.setBounds(117, 148, 163, 14);
		panel.add(lblEnterTheNo);
		
		noOfLinesText = new JTextField();
		noOfLinesText.setEnabled(false);
		noOfLinesText.setToolTipText("Enter the no of lines per output file. for eg if '10' is entered, the output files will contain 10 lines each.");
		noOfLinesText.setBounds(290, 147, 86, 20);
		panel.add(noOfLinesText);
		noOfLinesText.setColumns(10);
		
		JButton btnProcess = new JButton("Process");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processButtonActionPerformed(e);
			}
		});
		btnProcess.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnProcess.setBackground(new Color(51, 204, 102));
		btnProcess.setBounds(290, 578, 154, 23);
		panel.add(btnProcess);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 445, 749, 122);
		panel.add(scrollPane);
		
		ConsoleTextArea = new JTextArea();
		ConsoleTextArea.setEditable(false);
		scrollPane.setViewportView(ConsoleTextArea);
		
		txtAreaMessageForUser = new JTextArea();
		txtAreaMessageForUser.setEditable(false);
		txtAreaMessageForUser.setWrapStyleWord(true);
		txtAreaMessageForUser.setLineWrap(true);
		txtAreaMessageForUser.setForeground(new Color(0, 102, 153));
		txtAreaMessageForUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		txtAreaMessageForUser.setRows(4);
		txtAreaMessageForUser.setToolTipText("Messages For User from XSVPro appears here.\r\n");
		txtAreaMessageForUser.setBounds(10, 41, 312, 79);
		panel.add(txtAreaMessageForUser);
		
		chckbxFindTextIn = new JCheckBox("Retrieve lines containing string");
		chckbxFindTextIn.setToolTipText("Retrieves all the lines that contain the entered keyword from all input files.");
		chckbxFindTextIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxFindTextInActionPerformed(e);
			}
		});
		chckbxFindTextIn.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxFindTextIn.setBounds(10, 170, 237, 23);
		panel.add(chckbxFindTextIn);
		
		lblKeyword = new JLabel("");
		lblKeyword.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblKeyword.setForeground(new Color(0, 51, 102));
		lblKeyword.setBackground(new Color(255, 255, 204));
		lblKeyword.setToolTipText("Retrieves all the lines that contain the mentioned keyword from the input files.");
		lblKeyword.setEnabled(false);
		lblKeyword.setBounds(256, 173, 206, 20);
		panel.add(lblKeyword);
		
		chckbxMatchCase = new JCheckBox("match case");
		chckbxMatchCase.setEnabled(false);
		chckbxMatchCase.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxMatchCase.setBounds(483, 172, 97, 23);
		panel.add(chckbxMatchCase);
		
		delimiterCheckBox = new JCheckBox("Process File(s) based on Delimiter");
		delimiterCheckBox.setToolTipText("Select this check box to process file(s) based on delimiter. ");
		delimiterCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delimiterChckboxActionPerformer(e);
			}
		});
		delimiterCheckBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		delimiterCheckBox.setBounds(10, 196, 255, 23);
		panel.add(delimiterCheckBox);
		
		btnSelectDelimiter = new JButton("Select Delimiter*");
		btnSelectDelimiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delimiterButtonActionPerformer(e);
			}
		});
		btnSelectDelimiter.setToolTipText("Select the delimiter of the input file(s) if any. Clicking on this will open a dialog to enter delimiter.");
		btnSelectDelimiter.setEnabled(false);
		btnSelectDelimiter.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectDelimiter.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSelectDelimiter.setBounds(10, 226, 141, 23);
		panel.add(btnSelectDelimiter);
		
		textField_1 = new JTextField();
		textField_1.setEnabled(false);
		textField_1.setBounds(161, 226, 133, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		btnNewButton = new JButton("Select Headers");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectHeaderButtonclicked(e);
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(10, 249, 141, 23);
		panel.add(btnNewButton);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setEnabled(false);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"All"}));
		comboBox_1.setBounds(138, 324, 133, 20);
		panel.add(comboBox_1);
		
		textField_3 = new JTextField();
		textField_3.setEnabled(false);
		textField_3.setBounds(138, 348, 133, 20);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		lblTotalNoOf = new JLabel("Total no of rows processed : 0");
		lblTotalNoOf.setBounds(10, 420, 237, 14);
		panel.add(lblTotalNoOf);
		
		lblNoOfOutput = new JLabel("No of output files created : 0");
		lblNoOfOutput.setBounds(263, 420, 181, 14);
		panel.add(lblNoOfOutput);
		
		rdbtnEnterLineNo = new JRadioButton("Enter Line no");
		rdbtnEnterLineNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headerLineSelect(e);
			}
		});
		rdbtnEnterLineNo.setEnabled(false);
		buttonGroup.add(rdbtnEnterLineNo);
		rdbtnEnterLineNo.setBounds(156, 250, 109, 23);
		panel.add(rdbtnEnterLineNo);
		
		rdbtnSelectFromFirst = new JRadioButton("Select from first 10 lines");
		rdbtnSelectFromFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				headerSelectfromLines(e);
			}
		});
		rdbtnSelectFromFirst.setEnabled(false);
		buttonGroup.add(rdbtnSelectFromFirst);
		rdbtnSelectFromFirst.setBounds(263, 250, 199, 23);
		panel.add(rdbtnSelectFromFirst);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setBounds(10, 274, 749, 39);
		panel.add(scrollPane_2);
		
		headerTextPane = new JTextPane();
		scrollPane_2.setViewportView(headerTextPane);
		headerTextPane.setEnabled(false);
		headerTextPane.setEditable(false);
		
		lblSelectColumn = new JLabel("Select Column");
		lblSelectColumn.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSelectColumn.setBounds(10, 328, 118, 14);
		panel.add(lblSelectColumn);
		
		lblLookForValue = new JLabel("Look For Value");
		lblLookForValue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLookForValue.setBounds(10, 349, 118, 14);
		panel.add(lblLookForValue);
		
		JButton btnChooseFileContaining = new JButton("Choose file containing");
		btnChooseFileContaining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFileContainingValues(e);
			}
		});
		btnChooseFileContaining.setBounds(287, 347, 141, 23);
		panel.add(btnChooseFileContaining);
		
		lblKeywordsCount = new JLabel("Keywords count : 0");
		lblKeywordsCount.setBounds(511, 420, 147, 14);
		panel.add(lblKeywordsCount);
		
		frmXsvpro.setBackground(Color.GRAY);
		frmXsvpro.setTitle("XSVPro");
		frmXsvpro.setFont(new Font("Tahoma", Font.BOLD, 13));
		frmXsvpro.setBounds(100, 100, 785, 650);
		frmXsvpro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void chckbxFindTextInActionPerformed(ActionEvent e){
		
		if(chckbxFindTextIn.isSelected()){
			lblKeyword.setEnabled(true);
			String Keyword = (String)JOptionPane.showInputDialog(
                    this.frmXsvpro,
                    "Enter the keyword to be searched:",
                    "Keyword entry dialog",
                    JOptionPane.PLAIN_MESSAGE);
			lblKeyword.setText(Keyword);
			chckbxMatchCase.setEnabled(true);
		}else{
			lblKeyword.setText("");
			lblKeyword.setEnabled(false);
			chckbxMatchCase.setEnabled(false);
		}
	}
	
	private void delimiterChckboxActionPerformer(ActionEvent e){
		
		if(delimiterCheckBox.isSelected()){
			btnSelectDelimiter.setEnabled(true);
			btnNewButton.setEnabled(true);
			
			//disable keyword search controls
			chckbxFindTextIn.setSelected(false);
			chckbxFindTextIn.setEnabled(false);
			lblKeyword.setText("");
			lblKeyword.setEnabled(false);
			chckbxMatchCase.setEnabled(false);
		}else{
			btnSelectDelimiter.setEnabled(false);
			textField_1.setText("");
			textField_1.setEnabled(false);
			btnNewButton.setEnabled(false);
			headerTextPane.setText("");
			headerTextPane.setEnabled(false);
			lblLookForValue.setEnabled(false);
			lblSelectColumn.setEnabled(false);
			comboBox_1.setEnabled(false);
			textField_3.setText("");
			textField_3.setEnabled(false);
			chckbxFindTextIn.setEnabled(true);
			rdbtnEnterLineNo.setEnabled(false);
			rdbtnSelectFromFirst.setEnabled(false);
		}
	}
	
	private void splitFilesCheckBoxActionListener(ActionEvent e){
		
		if(chckbxSplitFiles.isSelected()){
			lblEnterTheNo.setEnabled(true);
			noOfLinesText.setEnabled(true);
			noOfLinesText.setEditable(true);
		}else{
			lblEnterTheNo.setEnabled(false);
			noOfLinesText.setText("");
			noOfLinesText.setEnabled(false);
			noOfLinesText.setEditable(false);
		}
	}
	
	private void processButtonActionPerformed(ActionEvent e) {
		
		if(validFiles != null && validFiles.size()>0){
			JFileChooser filechooser = new JFileChooser();
			File[] filesInFolder = null;
			filechooser.setDialogTitle("Choose folder to write the output files:");
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = filechooser.showOpenDialog(this.frmXsvpro);
			if (result == JFileChooser.APPROVE_OPTION) {
				outputFolder = filechooser.getSelectedFile();
				if(delimiterCheckBox.isSelected()){
					if(delimiter.equalsIgnoreCase("")){
						if(textField_1.getText().equalsIgnoreCase("")){
							JOptionPane.showMessageDialog(this.frmXsvpro,"Select a delimiter.");
						}else{
							delimiter=textField_1.getText();
							if(textField_3.getText().equalsIgnoreCase("")){
								JOptionPane.showMessageDialog(this.frmXsvpro,"Please enter a value for look for in the delimiterized file.");
							}else{
							xsvProcessor = new XSVProcessor();
							xsvProcessor.execute();
							}
						}
					}else{
						
						if(textField_3.getText().equalsIgnoreCase("")){
							JOptionPane.showMessageDialog(this.frmXsvpro,"Please enter a value for look for in the delimiterized file.");
						}else{
						xsvProcessor = new XSVProcessor();
						xsvProcessor.execute();
						}
					}
					
				}else{
					xsvProcessor = new XSVProcessor();
					xsvProcessor.execute();
				}
			}else{
				JOptionPane.showMessageDialog(this.frmXsvpro,"I mean.. Seriously?? How hard is it to choose a output folder?");
			}
		}else{
			JOptionPane.showMessageDialog(this.frmXsvpro,"Stop wasting my time. There is no input file to process!");
		}
	}
	
	private void browseButtonActionPerformed(ActionEvent e) {

		validFiles = new ArrayList<File>();
		int selection = comboBox.getSelectedIndex();
		JFileChooser filechooser = new JFileChooser();
		File[] filesInFolder = null;
		filechooser.setDialogTitle("Choose folder containing files");
		
		if(selection==1){
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}else{
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		
		int result = filechooser.showOpenDialog(this.frmXsvpro);
		if (result == JFileChooser.APPROVE_OPTION) {

			String fileNames = "";
			File folder = filechooser.getSelectedFile();
			if(folder.isDirectory()){
				filesInFolder = folder.listFiles();
			}else{
				if(folder.getName().endsWith(".xsv") || folder.getName().endsWith(".csv") || folder.getName().endsWith(".txt")) {
					fileNames +=folder.getName()+"\n";
					validFiles.add(folder);
				}else{
					JOptionPane.showMessageDialog(this.frmXsvpro,"Cant process this type of extension. Sorry");
				}
			}
			if(filesInFolder != null){
				for (int fileIterator = 0; fileIterator < filesInFolder.length; fileIterator++) {
					if(filesInFolder[fileIterator].getName().endsWith(".xsv") || filesInFolder[fileIterator].getName().endsWith(".csv") || filesInFolder[fileIterator].getName().endsWith(".txt")){
						fileNames += filesInFolder[fileIterator].getName() + "\n";
						validFiles.add(filesInFolder[fileIterator]);
					}
				
				}
			}
			textField.setText(folder.getPath());
			textArea.setText(fileNames);
		}
	}
	
	private void selectHeaderButtonclicked(ActionEvent e){
		if(btnNewButton.isEnabled()){
			if(rdbtnEnterLineNo.isEnabled() && rdbtnSelectFromFirst.isEnabled()){
				rdbtnEnterLineNo.setEnabled(false);
				rdbtnSelectFromFirst.setEnabled(false);
			}else{
				rdbtnEnterLineNo.setEnabled(true);
				rdbtnSelectFromFirst.setEnabled(true);
			}
		}
	}
	
	private void delimiterButtonActionPerformer(ActionEvent e){

		
		String linenoStr = JOptionPane.showInputDialog(this.frmXsvpro, "Enter the lineno of the input file that will contain a valid row(NOT headers)");
		int lineno = 0;
		if(!linenoStr.equalsIgnoreCase("")){
			try{
				lineno=Integer.parseInt(linenoStr);
			}catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(this.frmXsvpro, "Please enter a valid line no.");
			}
			if(validFiles.size()>0){
				String firstRow = XSVProcessor.getOneRowFromIpFiles(validFiles,lineno);
				if(!firstRow.equalsIgnoreCase("")){
					DelimiterSelectionDialog delimiterDialog = new DelimiterSelectionDialog(this.frmXsvpro,firstRow,textField_1);
					delimiterDialog.setVisible(true);
				}else{

				}
			}else{
				JOptionPane.showMessageDialog(this.frmXsvpro, "Please load input files first.");
			}
		}else{
			JOptionPane.showMessageDialog(this.frmXsvpro, "Enter a valid line no.");
		}
	}
	
	private void headerLineSelect(ActionEvent e){

		String linenoStr = JOptionPane.showInputDialog(this.frmXsvpro, "Enter the lineno of the input file that will contain the Header");
		int lineno = 0;
		if(!linenoStr.equalsIgnoreCase("")){
			try{
				lineno=Integer.parseInt(linenoStr);
			}catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(this.frmXsvpro, "Please enter a valid line no.");
			}
			if(validFiles.size()>0){
				String firstRow = XSVProcessor.getOneRowFromIpFiles(validFiles,lineno);
				if(!firstRow.equalsIgnoreCase("")){
					headerTextPane.setEnabled(true);
					headerTextPane.setEditable(false);
					headerTextPane.setText(firstRow);
					headers = firstRow.split(delimiter, -1);
					comboBox_1.setModel(new DefaultComboBoxModel(XSVProcessor.getComboBoxArrayStr(headers)));
					comboBox_1.setEnabled(true);
					lblLookForValue.setEnabled(true);
					lblSelectColumn.setEnabled(true);
					textField_3.setEditable(true);
					textField_3.setEnabled(true);
				}else{
				}
			}else{
				JOptionPane.showMessageDialog(this.frmXsvpro, "Please load input files first.");
			}
		}else{
			JOptionPane.showMessageDialog(this.frmXsvpro, "Enter a valid line no.");
		}
	}
	
	private void headerSelectfromLines(ActionEvent e){
		if(rdbtnSelectFromFirst.isSelected()){
			if(validFiles.size()>0){
				List<String> sampleRows = XSVProcessor.getFirstTenRows(validFiles);
				Map<String,String> trimmedValues = XSVProcessor.trimheaders(sampleRows);
				String[] headers = trimmedValues.keySet().toArray(new String[0]);
//				String[] headers = sampleRows.toArray(new String[0]);
				String headerstr = (String) JOptionPane.showInputDialog(this.frmXsvpro, 
				        "Select Header from the following lines",
				        "Header selection",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        headers, 
				        headers[0]);
				headerTextPane.setEnabled(true);
				headerTextPane.setEditable(false);
				headerTextPane.setText(trimmedValues.get(headerstr));
				headers = trimmedValues.get(headerstr).split(delimiter, -1);
				comboBox_1.setModel(new DefaultComboBoxModel(XSVProcessor.getComboBoxArrayStr(headers)));
				comboBox_1.setEnabled(true);
				lblLookForValue.setEnabled(true);
				lblSelectColumn.setEnabled(true);
				textField_3.setEditable(true);
				textField_3.setEnabled(true);
				
			}else{
				JOptionPane.showMessageDialog(this.frmXsvpro, "Please load input files first.");
			}
		}
	}
	
	private void chooseFileContainingValues(ActionEvent e){
		
	}
}
