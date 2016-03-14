package com.tool.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Window.Type;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalityType;


public class DelimiterSelectionDialog extends JDialog {

	/**
	 * 
	 */
	private JFrame frame;
	private static final long	serialVersionUID	= 1L;
	private final JPanel	contentPanel	= new JPanel();
	private JTextField textField;
	private JTable table;
	public String firstRow;
	public JButton okButton;
	public JTextField masterTextField;
	public String[] columns;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			DelimiterSelectionDialog dialog = new DelimiterSelectionDialog(null,"A~|~1723162~|~200260278~|~~|~Y~|~SCC~|~~|~677~|~677~|~1.0~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~N~|~~|~N~|~N~|~~|~~|~~|~I~|~I~|~I~|~I~|~N/A~|~N/A~|~N/A~|~N/A~|~A~|~~|~N~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~1.0~|~1.0~|~1.0~|~1.0~|~~|~~|~~|~~|~~|~5.0~|~5.0~|~5.0~|~5.0~|~~|~~|~~|~~|~~|~~|~W02~|~N02~|~S01~|~US~|~~|~~|~~|~~|~~|~~|~N~|~N~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~N~|~~|~0~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~N~|~0.0~|~N~|~~|~2 RESTRICTED~|~~|~~|~~|~~|~~|~RAIL PRODUCTS ARP INS JOINT~|~RAILPROARPIJ25~|~N~|~~|~N~|~~|~~|~~|~65.7675~|~PK~|~72361~|~~|~19.75~|~65.7675~|~70~|~~|~~|~~|~~|~~|~1~|~~|~~|~1~|~~|~N~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~~|~PK~|~25~|~PK~|~~|~2 RESTRICTED~|~~|~~|~~|~~|~1~|~~|~~|~~|~~|~~|~1~|~~|~~|~~|~~|~0.0~|~0.0~|~",new JTextField());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DelimiterSelectionDialog() {
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);

		setType(Type.UTILITY);
		setTitle("Delimiter selection\r\n");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 751, 289);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblNewLabel = new JLabel("Enter Delimiter :\r\n");
				lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
				lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
				panel.add(lblNewLabel);
			}
			{
				textField = new JTextField();
				textField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delimiterKeyTypedAction(e);
					}
				});
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						delimiterKeyTypedAction(e);
					}
				});
				panel.add(textField);
				textField.setColumns(25);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setFillsViewportHeight(true);
				//				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				//				table.setPreferredScrollableViewportSize(new Dimension(200,200));
				//				table.setPreferredSize(new Dimension(200,200));
				scrollPane.setViewportView(table);

				Object[][] rowData ={{firstRow}}; 
				Object[] columnNames = {"Column1"};
				table.setModel(new DefaultTableModel(rowData,columnNames));
			}
		}


		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonPress(e);
					}
				});
				okButton.setForeground(new Color(0, 153, 102));
				okButton.setEnabled(false);
				okButton.setBackground(UIManager.getColor("Button.background"));
				okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public DelimiterSelectionDialog(JFrame frame,String firstRow2, JTextField textField_1) {
		
		super(frame,"Delimiter selection");
		this.frame = frame;
		frame.setEnabled(false);
		masterTextField = textField_1;
		firstRow = firstRow2;
		
		setType(Type.UTILITY);
		setTitle("Delimiter selection\r\n");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 751, 289);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblNewLabel = new JLabel("Enter Delimiter :\r\n");
				lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
				lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
				panel.add(lblNewLabel);
			}
			{
				textField = new JTextField();
				textField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delimiterKeyTypedAction(e);
					}
				});
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						delimiterKeyTypedAction(e);
					}
				});
				panel.add(textField);
				textField.setColumns(25);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setFillsViewportHeight(true);
				//				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				//				table.setPreferredScrollableViewportSize(new Dimension(200,200));
				//				table.setPreferredSize(new Dimension(200,200));
				scrollPane.setViewportView(table);

				Object[][] rowData ={{firstRow}}; 
				Object[] columnNames = {"Column1"};
				table.setModel(new DefaultTableModel(rowData,columnNames));
			}
		}


		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonPress(e);
					}
				});
				okButton.setForeground(new Color(0, 153, 102));
				okButton.setEnabled(false);
				okButton.setBackground(UIManager.getColor("Button.background"));
				okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	
	}

	public String[] getColumnNames(int n){
		String[] columns = new String[n];

		for(int i=0;i<n;i++){
			int colInd = i+1;
			columns[i]="Column"+colInd;
		}
		this.columns = columns;
		return columns;
	}

	private void delimiterKeyTypedAction(ActionEvent e){
		String delimiter = textField.getText();
		if(!delimiter.isEmpty() && !delimiter.equals("")){
			String[] separatedStr =firstRow.split(delimiter, -1);
			Object[][] rowData ={separatedStr}; 
			Object[] columnNames = getColumnNames(separatedStr.length);
			table.setModel(new DefaultTableModel(rowData, columnNames));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			okButton.setEnabled(true);
		}else{
			Object[][] rowData ={{firstRow}}; 
			Object[] columnNames = {"Column1"};
			table.setModel(new DefaultTableModel(rowData, columnNames));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			okButton.setEnabled(false);
		}
	}
	private void delimiterKeyTypedAction(KeyEvent e){
		String delimiter = textField.getText();
		if(!delimiter.isEmpty() && !delimiter.equals("")){
			String[] separatedStr =firstRow.split(delimiter, -1);
			Object[][] rowData ={separatedStr}; 
			Object[] columnNames = getColumnNames(separatedStr.length);
			table.setModel(new DefaultTableModel(rowData, columnNames));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			okButton.setEnabled(true);
		}else{
			Object[][] rowData ={{firstRow}}; 
			Object[] columnNames = {"Column1"};
			table.setModel(new DefaultTableModel(rowData, columnNames));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			okButton.setEnabled(false);
		}
	}
	
	private void okButtonPress(ActionEvent e){
		masterTextField.setText(textField.getText());
		masterTextField.setEnabled(true);
		MainWindowFrame.delimiter = textField.getText();
		frame.setEnabled(true);
		MainWindowFrame.headers = columns;
		MainWindowFrame.comboBox_1.setModel(new DefaultComboBoxModel(columns));
		this.dispose();
	}
}
