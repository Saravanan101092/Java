package com.tool.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import java.awt.GridLayout;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeywordFileChooser extends JDialog {

	private final JPanel	contentPanel	= new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextArea textArea;
	private JTextArea textArea_1;
	public static File keywordFile;
	public static String[] keywords;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			KeywordFileChooser dialog = new KeywordFileChooser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public KeywordFileChooser() {

		setBounds(100, 100, 665, 506);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JButton btnNewButton = new JButton("Choose File");
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					filechooserbuttonaction(e);
				}
			});
			btnNewButton.setBounds(29, 11, 142, 23);
			contentPanel.add(btnNewButton);
		}
		
		textField = new JTextField();
		textField.setBounds(181, 12, 452, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 55, 604, 81);
		contentPanel.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel lblDelimiter = new JLabel("Delimiter");
		lblDelimiter.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDelimiter.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelimiter.setBounds(35, 172, 71, 14);
		contentPanel.add(lblDelimiter);
		
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				delimiterKeyPressed(e);
			}
		});
		textField_1.setBounds(29, 197, 86, 20);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblKeyword = new JLabel("Keywords");
		lblKeyword.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblKeyword.setBounds(306, 146, 71, 14);
		contentPanel.add(lblKeyword);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(235, 172, 398, 208);
		contentPanel.add(scrollPane_1);
		
		textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			GridBagLayout gbl_buttonPane = new GridBagLayout();
			gbl_buttonPane.columnWidths = new int[]{670, 0};
			gbl_buttonPane.rowHeights = new int[]{23, 23, 0};
			gbl_buttonPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_buttonPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			buttonPane.setLayout(gbl_buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okbuttonclicked(e);
					}
				});
				okButton.setActionCommand("OK");
				GridBagConstraints gbc_okButton = new GridBagConstraints();
				gbc_okButton.fill = GridBagConstraints.VERTICAL;
				gbc_okButton.insets = new Insets(0, 0, 5, 0);
				gbc_okButton.gridx = 0;
				gbc_okButton.gridy = 0;
				buttonPane.add(okButton, gbc_okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				GridBagConstraints gbc_cancelButton = new GridBagConstraints();
				gbc_cancelButton.gridx = 0;
				gbc_cancelButton.gridy = 1;
				buttonPane.add(cancelButton, gbc_cancelButton);
			}
		}
	}
	
	private void filechooserbuttonaction(ActionEvent e) {
		
		JFileChooser filechooser = new JFileChooser();
		File[] filesInFolder = null;
		filechooser.setDialogTitle("Choose File containing Keywords");
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = filechooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			keywordFile = filechooser.getSelectedFile();
			textField.setText(keywordFile.getName());
			textArea.setText(getContent(keywordFile));
		}
	}
		
	private void delimiterKeyPressed(KeyEvent e){
		
		String delimiter = textField_1.getText();
		String keywordString = textArea.getText();
		keywords = keywordString.split(delimiter,-1);
		textArea_1.setText("");
		for(int i=0;i<keywords.length;i++){
			textArea_1.setText(textArea_1.getText().concat("\n"+keywords[i]));
		}
		
	}
	
	private void okbuttonclicked(ActionEvent e){
		
		MainWindowFrame.keywords = this.keywords;
		MainWindowFrame.textField_2.setText(keywordFile.getName());
		MainWindowFrame.keywordFile = keywordFile;
		this.dispose();
	}
	
	public String getContent(File file){
		
		String keywordsString="";
		
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				keywordsString=keywordsString.concat(tempString);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return keywordsString;
	}
}
