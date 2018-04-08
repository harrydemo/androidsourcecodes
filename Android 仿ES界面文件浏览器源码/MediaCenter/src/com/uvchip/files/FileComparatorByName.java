package com.uvchip.files;

import java.io.File;
import java.util.Comparator;


public class FileComparatorByName implements Comparator<File>{

	@Override
	public int compare(File file1, File file2) {
		if(file1.isDirectory() && !file2.isDirectory()){
			return -1;
		}else if(file1.isDirectory() && file2.isDirectory()){
			return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
		}else if(!file1.isDirectory() && file2.isDirectory()){
			return 1;
		}else{
			return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
		}
	}
}
