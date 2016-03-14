package com.tool.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.tool.logic.XSVProcessor;

public class HomePage extends JFrame {

	public void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Saravanan Sampath
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem2 = new JMenuItem();
		delimiterCheckbox = new JCheckBox();
		label1 = new JLabel();
		folderPathText = new JTextField();
		browseButton = new JButton();
		chooseFileContainingKeywordsButton = new JButton("Choose");
		UserMessage = new JLabel();
		keywordLabel = new JLabel("Enter the Keyword:");
		label2 = new JLabel();
		textArea = new JTextArea();
		scrollPane1 = new JScrollPane();
		delimiterCheckbox = new JCheckBox("Delimiter");
		keyCheckbox = new JCheckBox("Choose records containing keyword");
		countCheckbox = new JCheckBox("No of records per output file ");
		exactCheckbox = new JCheckBox("Keyword as a Value");
		chooseFileContainingKeywordsCheckbox = new JCheckBox(
				"Choose file containing Keywords to be checked");
		processButton = new JButton("Process Files");
		delimiterText = new JTextField();
		keyWordText = new JTextField();
		countText = new JTextField();
		// ======== this ========
		setBackground(new Color(204, 204, 255));
		setFont(new Font("Verdana", Font.PLAIN, 16));
		setResizable(false);
		setTitle("XSVPro");
		setIconImage(new ImageIcon(getClass().getResource("/staples.png"))
				.getImage());
		setVisible(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		// ======== menuBar1 ========
		{

			// ======== menu1 ========
			{
				menu1.setText("File");
				menu1.setFont(new Font("Times New Roman", Font.PLAIN, 14));

				// ---- menuItem2 ----
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

		}
		setJMenuBar(menuBar1);

		// ---- checkBox1 ----
		// checkBox1.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// //checkBox1ActionPerformed(e);
		// }
		// });
		// contentPane.add(checkBox1);
		// checkBox1.setBounds(new Rectangle(new Point(35, 30),
		// checkBox1.getPreferredSize()));

		delimiterCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delimiterCheckboxActionPerformed(e);
			}
		});
		contentPane.add(delimiterCheckbox);
		delimiterCheckbox.setBounds(new Rectangle(new Point(20, 80),
				delimiterCheckbox.getPreferredSize()));

		keyCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyCheckboxActionPerformed(e);
			}
		});
		contentPane.add(keyCheckbox);
		keyCheckbox.setBounds(new Rectangle(new Point(20, 120), keyCheckbox
				.getPreferredSize()));

		countCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				countCheckboxActionPerformed(e);
			}
		});
		contentPane.add(countCheckbox);
		countCheckbox.setBounds(new Rectangle(new Point(20, 480), countCheckbox
				.getPreferredSize()));

		exactCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exactCheckboxActionPerformed(e);
			}
		});
		contentPane.add(exactCheckbox);
		exactCheckbox.setEnabled(false);
		exactCheckbox.setBounds(new Rectangle(new Point(20, 190), exactCheckbox
				.getPreferredSize()));
		chooseFileContainingKeywordsCheckbox
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						chooseFileContainingKeywordsCheckboxActionPerformed(e);
					}
				});
		contentPane.add(chooseFileContainingKeywordsCheckbox);
		chooseFileContainingKeywordsCheckbox.setEnabled(true);
		chooseFileContainingKeywordsCheckbox.setBounds(new Rectangle(new Point(
				20, 230), chooseFileContainingKeywordsCheckbox
				.getPreferredSize()));

		// ---- label1 ----
		label1.setText("Choose the folder containing the input files ");
		label1.setFont(new Font("Verdana", Font.BOLD, 12));
		contentPane.add(label1);
		label1.setBounds(65, 25, 315, 35);

		// ---- textField1 ----
		folderPathText.setEditable(false);
		contentPane.add(folderPathText);
		folderPathText.setBounds(380, 25, 380, 30);

		delimiterText.setEditable(false);
		contentPane.add(delimiterText);
		delimiterText.setBounds(100, 80, 100, 30);

		keyWordText.setEditable(false);
		contentPane.add(keyWordText);
		keyWordText.setBounds(210, 150, 120, 30);

		countText.setEditable(false);
		countText.setText("50000");
		contentPane.add(countText);
		countText.setBounds(230, 480, 50, 30);

		// ---- browseButton ----
		browseButton.setText("Browse");
		browseButton.setEnabled(true);
		browseButton.setFont(new Font("Verdana", Font.BOLD, 14));
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browseButtonActionPerformed(e);
			}
		});
		contentPane.add(browseButton);
		browseButton.setBounds(760, 25, 100, 30);

		chooseFileContainingKeywordsButton.setText("Choose");
		chooseFileContainingKeywordsButton.setEnabled(false);
		chooseFileContainingKeywordsButton.setFont(new Font("Verdana",
				Font.BOLD, 14));
		chooseFileContainingKeywordsButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						chooseFileContainingKeywordsButtonActionPerformed(e);
					}
				});
		contentPane.add(chooseFileContainingKeywordsButton);
		chooseFileContainingKeywordsButton.setBounds(50, 260, 100, 30);
		// ---- UserMessage ----
		UserMessage.setBackground(new Color(249, 230, 246));
		UserMessage.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(UserMessage);
		UserMessage.setBounds(150, 510, 750, 35);

		contentPane.add(keywordLabel);
		keywordLabel.setBounds(50, 150, 150, 30);
		// ---- label2 ----
		label2.setText("Copyrights@TCS                                                             DeveloperID : s.saravanan14@tcs.com");
		label2.setForeground(Color.GRAY);
		label2.setAlignmentX(CENTER_ALIGNMENT);
		label2.setAlignmentY(CENTER_ALIGNMENT);
		contentPane.add(label2);
		label2.setBounds(20, 605, 800, label2.getPreferredSize().height);

		// ---- text area ----
		textArea.setVisible(true);
		textArea.setFont(new Font("Verdana", Font.PLAIN, 13));
		scrollPane1.setViewportView(textArea);
		// textArea.setBackground(Color.gray);
		textArea.setEditable(false);
		textArea.setForeground(Color.BLACK);
		contentPane.add(textArea);

		{
			// ---- scrollpane ----
			textArea.setFont(new Font("Verdana", Font.PLAIN, 13));
			scrollPane1.setViewportView(textArea);
		}
		contentPane.add(scrollPane1);
		scrollPane1.setBounds(400, 105, 470, 400);

		// ---- processButton ----
		processButton.setVisible(true);
		processButton.setEnabled(true);

		processButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processButtonActionPerformed(e);
			}
		});
		contentPane.add(processButton);
		processButton.setBounds(20, 510, 120, 35);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for (int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width,
						preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height,
						preferredSize.height);
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

	// processButton functionality
	public void processButtonActionPerformed(ActionEvent e) {
		// callmethodhere
		if (countText.getText().equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(this,
					"Output File will contain 50000 records per file each!");
		}
		if(delimiterCheckbox.isSelected() && delimiterText.getText().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(this,"Delimiter can't be empty!");
		}else{
		if ((filesInFolder != null) && (filesInFolder.length > 0)) {
			XSVProcessor xsvtaskBG = new XSVProcessor();
			xsvtaskBG.execute();

		} else {
			JOptionPane.showMessageDialog(this, "No files in the folder");
		}
		}
	}

	// delimitercheckbox functionality
	public void delimiterCheckboxActionPerformed(ActionEvent e) {
		if (delimiterCheckbox.isSelected()) {
			delimiterText.setEditable(true);
		} else {
			delimiterText.setEditable(false);
		}
	}

	// keycheckbox functionality
	public void keyCheckboxActionPerformed(ActionEvent e) {
		if (keyCheckbox.isSelected()) {
			keyWordText.setEditable(true);
			exactCheckbox.setEnabled(true);
			chooseFileContainingKeywordsCheckbox.setEnabled(false);
			chooseFileContainingKeywordsButton.setEnabled(false);
		} else {
			keyWordText.setEditable(false);
			exactCheckbox.setEnabled(false);
			chooseFileContainingKeywordsCheckbox.setEnabled(true);
			chooseFileContainingKeywordsButton.setEnabled(true);
		}
	}

	// countcheckbox functionality
	public void countCheckboxActionPerformed(ActionEvent e) {
		if (countCheckbox.isSelected()) {
			countText.setEditable(true);
		} else {
			countText.setEditable(false);
		}
	}

	// exactcheckbox functionality
	public void exactCheckboxActionPerformed(ActionEvent e) {

	}

	// choose file containing keywords checkboxs
	public void chooseFileContainingKeywordsCheckboxActionPerformed(
			ActionEvent e) {

		if (chooseFileContainingKeywordsCheckbox.isSelected()) {
			exactCheckbox.setEnabled(true);
			chooseFileContainingKeywordsButton.setEnabled(true);
			keyCheckbox.setEnabled(false);
			keyWordText.setEditable(false);
		} else {
			exactCheckbox.setEnabled(false);
			chooseFileContainingKeywordsButton.setEnabled(false);
			keyCheckbox.setEnabled(true);
			keyWordText.setEditable(true);keyWordText.setText("");
		}
	}

	// Browse button functionality
	private void browseButtonActionPerformed(ActionEvent e) {

		JFileChooser filechooser = new JFileChooser();
		filechooser.setDialogTitle("Choose folder containing files");
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = filechooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {

			File folder = filechooser.getSelectedFile();
			filesInFolder = folder.listFiles();
			String fileNames = "";
			for (int fileIterator = 0; fileIterator < filesInFolder.length; fileIterator++) {
				fileNames += filesInFolder[fileIterator].getName() + "\n";
			}
			folderPathText.setText(folder.getPath());
			textArea.setText(fileNames);
			// TODO Auto-generated catch block
		}
	}

	public void menuItem2ActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	public void chooseFileContainingKeywordsButtonActionPerformed(ActionEvent e) {

		JFileChooser filechooser = new JFileChooser();
		filechooser
				.setDialogTitle("Choose file containing keywords(separated by ,)");
		int result = filechooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {

			fileContainingKeywords = filechooser.getSelectedFile();
		}
	}

	// variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem2;
	private JCheckBox delimiterCheckbox;
	private JCheckBox keyCheckbox;
	private JCheckBox countCheckbox;
	private JCheckBox chooseFileContainingKeywordsCheckbox;
	public static JCheckBox exactCheckbox;
	private static JLabel label1;
	private JTextField folderPathText;
	public static JTextField delimiterText;
	public static JTextField keyWordText;
	public static JTextField countText;
	private JButton browseButton;
	private JButton chooseFileContainingKeywordsButton;
	private JButton processButton;
	public static JLabel UserMessage;
	private JLabel label2;
	private JLabel keywordLabel;
	public static JTextArea textArea;
	private JScrollPane scrollPane1;
	public static File[] filesInFolder;
	public static File fileContainingKeywords;
}
