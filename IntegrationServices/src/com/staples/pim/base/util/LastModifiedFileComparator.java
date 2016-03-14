
package com.staples.pim.base.util;

import java.io.File;
import java.util.Comparator;

/*
 * Used to sort the files based on FILE LAST MODIFIED DATE TIME
 */
public class LastModifiedFileComparator implements Comparator<File> {

	@Override
	public int compare(File file1, File file2) {

		if (file1.lastModified() > file2.lastModified()) {
			return 1;
		} else if (file1.lastModified() < file2.lastModified()) {
			return -1;
		} else {
			return -1;
		}
	}
}