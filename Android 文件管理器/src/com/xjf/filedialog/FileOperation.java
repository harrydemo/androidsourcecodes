package com.xjf.filedialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileOperation {
	public static void copyFile(String oldPath, String newPath)
			throws IOException {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) {
			InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[4096];
			//int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // �ֽ��� �ļ���С
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}
	}
	 
	
	/**
	 * �ݹ��ȡ�ļ����������ļ����ܴ�С
	 * BUG: ���ֱܷ������ļ�
	 * */
	public static long getDirectorySize(File f) throws IOException{
		long size = 0;
		File flist[] = f.listFiles();
		if (flist == null)
			return f.length();
		int length = flist.length;
		for (int i = 0; i < length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getDirectorySize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
	/**
	 * BUG: ���ֱܷ������ļ�
	 * */
	public static long getDirectorySize(String fp) throws IOException{
		long size = 0;
		File f = new File(fp);
		File flist[] = f.listFiles();
		if (flist == null)
			return f.length();
		int length = flist.length;
		for (int i = 0; i < length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getDirectorySize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
}
