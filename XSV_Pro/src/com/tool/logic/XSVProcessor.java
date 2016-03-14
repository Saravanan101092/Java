package com.tool.logic;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.sun.xml.internal.ws.util.StringUtils;
import com.tool.view.HomePage;
import com.tool.view.MainWindowFrame;

public class XSVProcessor extends SwingWorker<String, String> {

	public static final String ROWCOUNTSTR = "ROWCOUNT::";
	public static final String CONSOLESTR = "CONSOLE::";
	public static final String FILECOUNTSTR = "FILECOUNT::";
	public static final String USRMSGSTR = "USRMSG::";
	public static final String KEYWORDSTR = "KEYWORD::";
	public List<String> keywordsList;
	FileReader fileReader;
	BufferedReader reader;
	String tempString;

	long timestamp;
	FileWriter writer;
	String outputPath;

	public static String getOneRowFromIpFiles(List<File> validFiles,int lineno){
		String rowStr="";
		int currentLine=1;
			FileReader fileReader;
			try {
				for(File file : validFiles){
					fileReader = new FileReader(file);
					BufferedReader reader = new BufferedReader(fileReader);

					while ((rowStr = reader.readLine()) != null) {
						
						if(currentLine == lineno){
							if(!rowStr.equalsIgnoreCase("")){
								fileReader.close();
								reader.close();
								return rowStr;
							}else{
								JOptionPane.showMessageDialog(null, "Specified line contains empty string! Select different line.");
							}
						}
						currentLine++;
					}
					reader.close();fileReader.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return rowStr;
	}
	
	public static String[] getComboBoxArrayStr(String[] array){
		String[] arrayWithAll = new String[array.length+1];
		arrayWithAll[0]="All";
		for(int i=0;i<array.length;i++){
			arrayWithAll[i+1]=array[i];
		}
		return arrayWithAll;
	}
	
	public static Map<String,String> trimheaders(List<String> headerStrings){
		Map<String,String> trimmedToFullHeaders = new HashMap<String,String>();
		for(String str : headerStrings){
			if(str.length()>200){
				String temp = str.substring(0, 150)+"..";
				trimmedToFullHeaders.put(temp, str);
			}else{
				trimmedToFullHeaders.put(str, str);
			}
		}
		return trimmedToFullHeaders;
	}
	
	public static List<String> getFirstTenRows(List<File> validFiles){
		List<String> firstTenRows = new ArrayList<String>();

		String rowStr="";
		FileReader fileReader;
		try {
			for(File file : validFiles){
				fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fileReader);

				while ((rowStr = reader.readLine()) != null) {

					if(!rowStr.equalsIgnoreCase("")){
						firstTenRows.add(rowStr);
						if(firstTenRows.size()>9){
							reader.close();fileReader.close();
							return firstTenRows;
						}
					}
				}
				reader.close();fileReader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return firstTenRows;
	}
	
	public String doInBackground() throws Exception {
		publish(CONSOLESTR+"Loaded "+MainWindowFrame.validFiles.size()+" input files.");
		if(MainWindowFrame.chckbxSplitFiles.isSelected() && !MainWindowFrame.chckbxFindTextIn.isSelected() && !MainWindowFrame.delimiterCheckBox.isSelected()){
			publish(USRMSGSTR+"Simple File Splitting. Output Files will have "+MainWindowFrame.noOfLinesText.getText()+" lines each.");
			simpleSplitFiles(MainWindowFrame.validFiles);
		}else if(!MainWindowFrame.chckbxSplitFiles.isSelected() && MainWindowFrame.chckbxFindTextIn.isSelected() && !MainWindowFrame.delimiterCheckBox.isSelected()){
			publish(USRMSGSTR+"Retrieve lines that contain the keyword :'"+MainWindowFrame.lblKeyword.getText()+"' from all the input files. Output will be in a single file.");
			simpleFindKeyword(MainWindowFrame.validFiles);
		}else if(MainWindowFrame.chckbxSplitFiles.isSelected() && MainWindowFrame.chckbxFindTextIn.isSelected()  && !MainWindowFrame.delimiterCheckBox.isSelected()){
			publish(USRMSGSTR+"Retrieve lines that contain the keyword :'"+MainWindowFrame.lblKeyword.getText()+"' from all the input files. Output files contain"+MainWindowFrame.noOfLinesText.getText()+" lines each.");
			findKeywordAndSplit(MainWindowFrame.validFiles);
		}else if(MainWindowFrame.delimiterCheckBox.isSelected() && !MainWindowFrame.textField_3.getText().equalsIgnoreCase("")){
			publish(USRMSGSTR+"Processing the file(s) based on delimiter, searching the entered keyword.");
			processFilesBasedonDelimiter(MainWindowFrame.validFiles);
		}else if(MainWindowFrame.delimiterCheckBox.isSelected() && MainWindowFrame.keywords!=null && MainWindowFrame.keywords.length>0){
			publish(USRMSGSTR+"Processing the file(s) based on delimiter, Searching for the keywords in the file.");
			processFilesBasedonDelimiterKeywords(MainWindowFrame.validFiles,MainWindowFrame.keywords);
		}

		return "";
	}

	private void processFilesBasedonDelimiterKeywords(List<File> validFiles, String[] keywords) {

		
		boolean splitFiles = MainWindowFrame.chckbxSplitFiles.isSelected();
		String delimiter = MainWindowFrame.delimiter;
		String[] headers = MainWindowFrame.headers;
		
		//get value from file()
		boolean searchAllColumns = false;
		if(MainWindowFrame.comboBox_1.getSelectedIndex()==0)
		{
			searchAllColumns=true;
		}	
		
		if(searchAllColumns){
			processDelimiterFilesAllColumnsKeywords(validFiles, delimiter, splitFiles,keywords);
		}else{
			int column = MainWindowFrame.comboBox_1.getSelectedIndex();
			processDelimiterFilesAtColumnKeywords(column-1, validFiles, delimiter, splitFiles, keywords);
		}
		
	}


	private void processDelimiterFilesAtColumnKeywords(int i, List<File> validFiles, String delimiter, boolean splitFiles,String[] keywords) {

		List<String> keywordslist = convertArrayToList(keywords);
		long totalNoOfLines = 0;
		int fileCount=1;
		int MaxLinesPerOutputFile=0;
		if(splitFiles){
			MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		}
		int recordCountInCurrentFile =0;
		long keywordFoundCount=0;
		
		File firstFile = validFiles.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : validFiles){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					if ((splitFiles) && (recordCountInCurrentFile == MaxLinesPerOutputFile)) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish(CONSOLESTR+"Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}
					
					String[] splittedValues = tempString.split(delimiter, -1);
					if(keywordslist.contains(splittedValues[i])){
						writer.write(tempString + "\n");
						keywordFoundCount++;
						publish(KEYWORDSTR+keywordFoundCount);
					}
					recordCountInCurrentFile++;
				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+fileCount);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		
	}

	public List<String> convertArrayToList(String[] arr){
		
		keywordsList=new ArrayList<String>();
		for(int i=0;i<arr.length;i++){
			keywordsList.add(arr[i]);
		}
		return keywordsList;
	}
	private void processDelimiterFilesAllColumnsKeywords(List<File> validFiles, String delimiter, boolean splitFiles,String[] keywords) {
		List<String> keywordslist = convertArrayToList(keywords);
		long totalNoOfLines = 0;
		int fileCount=1;
		int MaxLinesPerOutputFile=0;
		if(splitFiles){
			MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		}
		int recordCountInCurrentFile =0;
		long keywordFoundCount=0;
		
		File firstFile = validFiles.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : validFiles){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					if ((splitFiles) && (recordCountInCurrentFile == MaxLinesPerOutputFile)) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish(CONSOLESTR+"Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}
					
					String[] splittedValues = tempString.split(delimiter, -1);
					for(String str : splittedValues){
						if(keywordslist.contains(str)){
							writer.write(tempString + "\n");
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
							break;
						}
					}
					recordCountInCurrentFile++;
				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+fileCount);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	
		
	}

	public void processFilesBasedonDelimiter(List<File> inputFiles){
		
		boolean splitFiles = MainWindowFrame.chckbxSplitFiles.isSelected();
		String delimiter = MainWindowFrame.delimiter;
		String[] headers = MainWindowFrame.headers;
		String Value = MainWindowFrame.textField_3.getText();
		
		//get value from file()
		boolean searchAllColumns = false;
		if(MainWindowFrame.comboBox_1.getSelectedIndex()==0)
		{
			searchAllColumns=true;
		}	
		
		if(searchAllColumns){
			processDelimiterFilesAllColumns(inputFiles, delimiter, splitFiles, Value);
		}else{
			int column = MainWindowFrame.comboBox_1.getSelectedIndex();
			processDelimiterFilesAtColumn(column-1, inputFiles, delimiter, splitFiles, Value);
		}
		
		
	}
	
	public void simpleFindKeyword(List<File> inputFiles){

		long totalNoOfLines = 0;
		long keywordFoundCount=0;
		FileWriter writer;
		File firstFile = inputFiles.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		String keyword = MainWindowFrame.lblKeyword.getText();
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+extension);
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : inputFiles){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);
				
				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					
					if(MainWindowFrame.chckbxMatchCase.isSelected()){
						if(tempString.contains(keyword)){
							writer.write(tempString + "\n");
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
						}
					}else{
						if(tempString.toLowerCase().contains(keyword.toLowerCase())){
							writer.write(tempString + "\n");
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
						}	
					}

				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+1);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void findKeywordAndSplit(List<File> inputFiles){
		long totalNoOfLines = 0;
		int fileCount=1;
		long keywordFoundCount=0;
		int MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		int recordCountInCurrentFile =0;
		File firstFile = inputFiles.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		String keyword = MainWindowFrame.lblKeyword.getText();
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : inputFiles){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					if (recordCountInCurrentFile == MaxLinesPerOutputFile) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish("Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}

					if(MainWindowFrame.chckbxMatchCase.isSelected()){
						if(tempString.contains(keyword)){
							writer.write(tempString + "\n");
							recordCountInCurrentFile++;
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
						}
					}else{
						if(tempString.toLowerCase().contains(keyword.toLowerCase())){
							writer.write(tempString + "\n");
							recordCountInCurrentFile++;
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
						}	
					}

				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			
			publish(CONSOLESTR+"PROCESS COMPLETED.");
			
			if(outputFile.length()>0){
				writer.close();
				publish(FILECOUNTSTR+fileCount);
			}else{
				writer.close();
				outputFile.delete();
				if(fileCount==1){
					JOptionPane.showMessageDialog(MainWindowFrame.frmXsvpro, "No lines with the mentioned Keyword found!");
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(MainWindowFrame.frmXsvpro,
					"Exception! try again!");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MainWindowFrame.frmXsvpro,
					"Exception! try again!");
			e.printStackTrace();
		}
	}

	public void simpleSplitFiles(List<File> inputFiles) {
		
		long totalNoOfLines = 0;
		int fileCount=1;
		int MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		int recordCountInCurrentFile =0;
		File firstFile = inputFiles.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : inputFiles){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					if (recordCountInCurrentFile == MaxLinesPerOutputFile) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish(CONSOLESTR+"Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}
					writer.write(tempString + "\n");
					System.out.println(tempString);writer.write("sdsdsds\n");
					recordCountInCurrentFile++;
				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+fileCount);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processDelimiterFilesAllColumns(List<File> files, String delimiter,boolean issplit, String value){

		
		long totalNoOfLines = 0;
		int fileCount=1;
		int MaxLinesPerOutputFile=0;
		if(issplit){
			MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		}
		int recordCountInCurrentFile =0;
		long keywordFoundCount=0;
		
		File firstFile = files.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : files){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					if ((issplit) && (recordCountInCurrentFile == MaxLinesPerOutputFile)) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish(CONSOLESTR+"Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}
					
					String[] splittedValues = tempString.split(delimiter, -1);
					for(String str : splittedValues){
						if(str.equalsIgnoreCase(value)){
							writer.write(tempString + "\n");
							keywordFoundCount++;
							publish(KEYWORDSTR+keywordFoundCount);
							break;
						}
					}
					recordCountInCurrentFile++;
				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+fileCount);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	public void processDelimiterFilesAtColumn(int column,List<File> files, String delimiter,boolean issplit, String value){
		
		long totalNoOfLines = 0;
		int fileCount=1;
		int MaxLinesPerOutputFile=0;
		if(issplit){
			MaxLinesPerOutputFile = Integer.parseInt(MainWindowFrame.noOfLinesText.getText());
		}
		int recordCountInCurrentFile =0;
		long keywordFoundCount=0;
		
		File firstFile = files.get(0);
		String extension = firstFile.getName().substring(firstFile.getName().lastIndexOf("."));
		String filenameWOExtension = firstFile.getName().substring(0,firstFile.getName().lastIndexOf("."));
		String dateStr = Long.toString(new Date().getTime());
		File outputFile = new File(MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension);
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile,true);
			for(File file : files){
				publish(CONSOLESTR+"Processing Input File :"+file.getName());
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					totalNoOfLines++;
					if ((issplit) && (recordCountInCurrentFile == MaxLinesPerOutputFile)) {
						publish(FILECOUNTSTR+fileCount);
						fileCount++;
						String outputFileStr=MainWindowFrame.outputFolder+File.separator+filenameWOExtension+dateStr+"_"+fileCount+extension;
						publish(CONSOLESTR+"Writing output file : "+filenameWOExtension+dateStr+"_"+fileCount+"."+extension);
						outputFile = new File(outputFileStr);
						recordCountInCurrentFile = 0;
						writer.close();
						writer = new FileWriter(outputFile, true);
						System.out.println("writing file no:"+fileCount);
					}
					
					String[] splittedValues = tempString.split(delimiter, -1);
					if(splittedValues[column].equalsIgnoreCase(value)){
						writer.write(tempString + "\n");
						keywordFoundCount++;
						publish(KEYWORDSTR+keywordFoundCount);
					}
					recordCountInCurrentFile++;
				}
				publish(ROWCOUNTSTR+totalNoOfLines);
				reader.close();
			}
			writer.close();
			publish(FILECOUNTSTR+fileCount);
			publish(CONSOLESTR+"PROCESS COMPLETED.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Exception!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void process(java.util.List<String> messages) {
		for (String message : messages) {
			if(message.startsWith(CONSOLESTR)){
				MainWindowFrame.ConsoleTextArea.setForeground(Color.blue);
				String text = MainWindowFrame.ConsoleTextArea.getText();
				String[] arr=message.split(CONSOLESTR);
				MainWindowFrame.ConsoleTextArea.setText(text +arr[1] +"\n");
			}else if(message.startsWith(ROWCOUNTSTR)){
				String[] arr=message.split(ROWCOUNTSTR);
				MainWindowFrame.lblTotalNoOf.setText("Total no of rows processed : "+arr[1]);
			}else if(message.startsWith(FILECOUNTSTR)){
				String[] arr=message.split(FILECOUNTSTR);
				MainWindowFrame.lblNoOfOutput.setText("No of output files created : "+arr[1]);
			}else if(message.startsWith(USRMSGSTR)){
				String[] arr=message.split(USRMSGSTR);
				MainWindowFrame.txtAreaMessageForUser.setText(arr[1]);
			}else if(message.startsWith(KEYWORDSTR)){
				String[] arr=message.split(KEYWORDSTR);
				MainWindowFrame.lblKeywordsCount.setText("Keywords count : "+arr[1]);
			}
		}
	}

	@Override
	protected void done() {
		JOptionPane.showMessageDialog(null,
				"Operation completed! ouputfiles generated!");
		MainWindowFrame.txtAreaMessageForUser.setText("ouputfiles Generated @" + MainWindowFrame.outputFolder.getAbsolutePath());
	}


	////////////////////////////////////////////////////

	public void writeToFile(List<String> records, int fileCount, String outputPath, String filename, String extension)
			throws IOException {
		File file = new File(outputPath+File.separator+filename+"_"+ fileCount + extension);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		outputPath = file.getParentFile().getPath();
		writer = new FileWriter(file, true);
		for (String record : records) {
			writer.write(record + "\n");
		}
		writer.close();
	}

	public void splitPriyanka(File[] filesInFolder){
		int recordCount = 1;
		List<String> outputRecords = new ArrayList<String>();
		timestamp = new Date().getTime();
		int filecount = 1;
		for (int filesIterator = 0; filesIterator < filesInFolder.length; filesIterator++) {
			System.out.println("Processing file..."
					+ filesInFolder[filesIterator].getName());
			try {
				fileReader = new FileReader(filesInFolder[filesIterator]);
				reader = new BufferedReader(fileReader);
				File file = new File("D:/saravanan/XSVProFiles/OuputFile_" + timestamp
						+ "_" + filecount + ".xsv");
				writer = new FileWriter(file, true);

				while ((tempString = reader.readLine()) != null) {
					if (recordCount > 50000) {
						recordCount = 0;
						writer.close();
						filecount++;
						file = new File("D:/saravanan/XSVProFiles/OuputFile_" + timestamp
								+ "_" + filecount + ".xsv");
						writer = new FileWriter(file, true);
						System.out.println("writing file no:"+filecount);
					}
					writer.write(tempString + "\n");
					recordCount++;
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		//	if (recordCount > 0) {
		//		try {
		//			writeToFile(outputRecords, filecount);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//	}
	}
	public boolean doesRecordContain(String[] valuesInRecord, String keyword,
			boolean isExact, String[] manyKeywords, boolean isKeywordFile) {

		Date starttime = new Date();
		if (!isKeywordFile) {
			for (int i = 0; i < valuesInRecord.length; i++) {
				if (isExact) {
					if (valuesInRecord[i].equals(keyword)) {
						return true;
					}
				} else {
					if (valuesInRecord[i].contains(keyword)) {
						return true;
					}
				}
			}
			return false;
		} else {

			if (isExact) {
				for (int keyworditerator = 0; keyworditerator < manyKeywords.length; keyworditerator++) {
					for(int valuesIterator=0;valuesIterator<valuesInRecord.length;valuesIterator++){
						if (valuesInRecord[valuesIterator]
								.equals(manyKeywords[keyworditerator])) {
							return true;
						}
					}
				}
			} else {
				for (int keyworditerator = 0; keyworditerator < manyKeywords.length; keyworditerator++) {
					if(valuesInRecord.length>2){
						try{
							if (valuesInRecord[2]
									.contains(manyKeywords[keyworditerator])) {

								System.out.println("valuesinRecord="+valuesInRecord[2]+" keyword="+manyKeywords[keyworditerator]);
								return true;
							}
						}catch(Exception e){
							return false;
						}
					}
				}
			}
		}
		Date endtime = new Date();
		return false;
	}


	public static void main(String[] args){
		String keyword = "CONSOLE::SARU";
		String[] arr = keyword.split(CONSOLESTR);
		System.out.println(arr[0]+"\n"+arr[1]);
		
	}
}
