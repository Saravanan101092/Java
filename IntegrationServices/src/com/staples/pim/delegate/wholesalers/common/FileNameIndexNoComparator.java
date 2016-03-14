
package com.staples.pim.delegate.wholesalers.common;

import java.io.File;
import java.util.Comparator;

/*
 * Used to sort the files based on File Name Index No
 */

public class FileNameIndexNoComparator implements Comparator<File> {

	@Override
	public int compare(File file1, File file2) {

		int file1Index = 0;
		int file2Index = 0;

		file1Index = Integer.parseInt(file1.getName().split("\\.")[1]);
		file2Index = Integer.parseInt(file2.getName().split("\\.")[1]);

		if (file1Index > file2Index) {
			return 1;
		} else if (file1Index < file2Index) {
			return -1;
		} else {
			return -1;
		}
	}
}