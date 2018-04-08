package com.uvchip.files;

import java.io.File;
import java.util.Comparator;

public class FileComparatorBySize implements Comparator<File>{
	
	@Override
	public int compare(File file1, File file2) {
		if(file1.isDirectory() && !file2.isDirectory()){
			return -1;
		}else if(file1.isDirectory() && file2.isDirectory()){
			return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
		}else if(!file1.isDirectory() && file2.isDirectory()){
			return 1;
		}else{
			long result = (file1.length()-file2.length());
			if(result>=0){
				return 1;
			}else{
				return -1;
			}
		}
	}
}
