
package com.staples.pim.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class ZipManager {

	public String	archivesPath	= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("ARCHIVES_ROOT_FOLDER"));
	double				archFileAftNoofDays		= IntgSrvUtils.toDouble(IntgSrvPropertiesReader.getProperty("ARCHIVE_FILES_AFTER_NO_OF_DAYS"));

	public void doArchive() {

		String folderPaths = IntgSrvPropertiesReader.getProperty("ZIP_FOLDER");
		String[] foldersNames = null;
		if (folderPaths != null) {
			foldersNames = folderPaths.split("~\\|~");
		}
		for (int foldercount = 0; foldercount < foldersNames.length; foldercount++) {
			// IntgSrvUtils.printConsole("Current folder that is being archived is "
			// + foldersNames[foldercount]);

			File folder = new File(IntgSrvUtils.reformatFilePath(foldersNames[foldercount]));
			if (folder.exists()) {

				File[] listOfFiles = folder.listFiles();
				if (listOfFiles.length > 0) {
					try {
						// create byte buffer
						byte[] buffer = new byte[1024];
						String dateString = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());

						String outpath = IntgSrvUtils.reformatFilePath(archivesPath + foldersNames[foldercount] + "/archive_" + dateString
								+ ".zip");
						File outFile = new File(outpath);
						if (!outFile.getParentFile().exists()) {
							outFile.getParentFile().mkdirs();
						}
						FileOutputStream fos = null;
						ZipOutputStream zos = null;

						for (int i = 0; i < listOfFiles.length; i++) {

							/*
							 * long diff = new Date().getTime() -
							 * listOfFiles[i].lastModified();
							 */
							BasicFileAttributes attrs = Files.readAttributes(listOfFiles[i].toPath(), BasicFileAttributes.class);
							long diff = new Date().getTime() - attrs.lastAccessTime().toMillis();

							if (diff > archFileAftNoofDays * 24 * 60 * 60 * 1000) {

								if (fos == null) {
									fos = new FileOutputStream(outpath);
									zos = new ZipOutputStream(fos);
								}

								FileInputStream fis = new FileInputStream(listOfFiles[i]);
								// begin writing a new ZIP entry, positions the
								// stream
								// to the start of the entry data
								zos.putNextEntry(new ZipEntry(listOfFiles[i].getName()));
								int length;
								while ((length = fis.read(buffer)) > 0) {
									zos.write(buffer, 0, length);
								}
								zos.closeEntry();
								// close the InputStream
								fis.close();
								listOfFiles[i].delete();
							}
							// close the ZipOutputStream
						}
						zos.close();
					} catch (IOException ioe) {
						IntgSrvUtils.printConsole("Error creating zip file: " + ioe);
						ioe.printStackTrace();
					}

				} else {
					IntgSrvUtils.printConsole("no file to archive in the folder: " + foldersNames[foldercount]);
				}
			}
		}

	}

	public void doDeleteOlderArchives(File folder) {

		File[] innerFoldersAndFiles = folder.listFiles();
		for (int i = 0; i < innerFoldersAndFiles.length; i++) {
			if (innerFoldersAndFiles[i].isDirectory()) {
				doDeleteOlderArchives(innerFoldersAndFiles[i]);
			} else if (innerFoldersAndFiles[i].isFile()) {
				if (innerFoldersAndFiles[i].getName().endsWith(".zip")) {

					/*
					 * long diff = new Date().getTime() -
					 * innerFoldersAndFiles[i].lastModified();
					 */

					BasicFileAttributes attrs;
					try {
						attrs = Files.readAttributes(innerFoldersAndFiles[i].toPath(), BasicFileAttributes.class);

						long diff = new Date().getTime() - attrs.lastAccessTime().toMillis();

						int noofdays = Integer.parseInt(IntgSrvPropertiesReader.getProperty("ARCHIVES_VALID_PERIOD"));
						if (diff > noofdays * 24 * 60 * 60 * 1000) {
							IntgSrvUtils.printConsole("File deleted " + innerFoldersAndFiles[i].getName());
							innerFoldersAndFiles[i].delete();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void initZip() {

		IntgSrvUtils.printConsole("File zipper triggered");
		doArchive();
		doDeleteOlderArchives(new File(archivesPath));
	}

	public static void loadContext() {

		IntgSrvUtils.printConsole(IntgSrvPropertiesReader.getProperty("ARCHIVES_ROOT_FOLDER"));
		String proValue = IntgSrvPropertiesReader.getProperty("ARCHIVES_ROOT_FOLDER");
		if (proValue != null && !"NULL".equalsIgnoreCase(proValue)) {
			String path = IntgSrvUtils.getConfigDir();
			GenericXmlApplicationContext zipContext = new GenericXmlApplicationContext();
			zipContext.load("file:" + path + IntgSrvUtilConstants.FILE_ZIPMANAGER_SCHEDULER);
			zipContext.refresh();
			DatamigrationCommonUtil.printConsole("Zipmanager Context loaded..");
		}
	}

	public static void main(String args[]) {

		ZipManager zipper = new ZipManager();
		IntgSrvUtils.printConsole("File zipper triggered");
		zipper.doDeleteOlderArchives(new File(zipper.archivesPath));
	}
}
