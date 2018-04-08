package com.song.util;

public class StringUtils {
	
	public static String getBucketPath(String fullPath,String fileName){	
		
		return fullPath.substring(0, fullPath.lastIndexOf(fileName));
		
	}
	
	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
