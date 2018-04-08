package com.jameschen.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MovieFileSearch {
		
	/*得到文件夹中所有文件名的文件*/
	public List<MyFile> getFileName(String strPath){
		List<MyFile> nameList = new ArrayList<MyFile>();
		File bn = new File(strPath);
		if(bn.exists()){
			File [] files = bn.listFiles();
			if( files != null && files.length > 0){
				for(int i = 0;i < files.length;i++){
					if(files[i].isFile()){
						if(files[i].getName().endsWith("mp4")||files[i].getName().endsWith("3gp")){
							String fileName = files[i].getName();
							MyFile  myFile = new MyFile(files[i].getAbsolutePath(), fileName);
							nameList.add(myFile);
						}
					} else if(files[i].isDirectory()){
						strPath =files[i].getAbsolutePath();
						List<MyFile> nameListSub = new ArrayList<MyFile>();
						nameListSub = getFileName(strPath);
						if(nameListSub != null){
							nameList.addAll(nameListSub);
						}
					}
				}
			}
			return nameList;
		}else{
		}
		return null;
	}
	public class MyFile {
		String path;
		String fileName;
		
		public MyFile(String path,String fileName) {
			this.path = path;
			this.fileName = fileName;
		}
	}
}
