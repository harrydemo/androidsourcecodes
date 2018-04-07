package com.feicong.qqmsglook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DBFiles
{
	private static String dbPath = "/data/data/com.tencent.mobileqq/databases/";
	private static String msglookPath = "/data/data/com.feicong.qqmsglook/databases/";
	
	public static void main(String args[]){	
		File fileSrc = null;
		File fileDst = null;
		try{
			fileSrc = new File(dbPath);
			fileDst = new File(msglookPath);
			if(!fileDst.exists()) fileDst.mkdir();
			File[] fileList = fileSrc.listFiles();	
			for(File f : fileList){
				if(f.isFile()){
					if("qcenter.db".equalsIgnoreCase(f.getName()) ||
						"webview.db".equalsIgnoreCase(f.getName()) ||
						"webviewCache.db".equalsIgnoreCase(f.getName())){
						continue;
					}
					if(".db".equalsIgnoreCase(getFileExt(f)) && (f.getName().length() != 0))
						copyDBFile(dbPath + f.getName(), msglookPath + f.getName());			
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 拷贝文件
	 * @param fileSrc
	 * @param fileDst
	 */
	public static Boolean copyDBFile(String fileSrc, String fileDst){
		try{
			File fSrc = new File(fileSrc);
			if (!fSrc.canRead()) return false;
			File fDst = new File(fileDst);
			if (fDst.exists()) fDst.delete();
			FileInputStream fin = new FileInputStream(fSrc);
			FileOutputStream fout = new FileOutputStream(fDst);
			int ch;
			byte[] b= new byte[1024*4];
			try{
				while((ch = fin.read(b)) != -1){
					fout.write(b, 0, ch);
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				try
				{
					fin.close();
					fout.flush();
					fout.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}				
			}
			return true;
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 获取文件扩展名
	 * @param file
	 * @return
	 */
	public static String getFileExt(File file){
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		String str = fileName.substring(index, fileName.length());
		return str;
	}
	/**
	 * 获取文件名但不包含扩展名
	 * @param file
	 * @return
	 */
	public static String getFileNameNoExt(File file){
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		String str = fileName.substring(0, index);
		return str;
	}
	public static List<String> getDBNames(){
		List<String> list = new ArrayList<String>();
		try
		{
			File file = new File(msglookPath);
			if (!file.canRead())
				return null;
			File[] fList = file.listFiles();
			for(File f : fList){
				if(f.isFile()){
					if(".db".equalsIgnoreCase(getFileExt(f))){
						String str = getFileNameNoExt(f);
						list.add(str);
					}
				}
			}			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> getDBPaths(){
		List<String> list = new ArrayList<String>();
		try
		{
			File file = new File(msglookPath);
			if (!file.canRead())
				return null;
			File[] fList = file.listFiles();
			for(File f : fList){
				if(f.isFile()){
					if(".db".equalsIgnoreCase(getFileExt(f))){
						String str = f.getAbsolutePath();
						list.add(str);
					}
				}
			}			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 删除目录下所有DB文件
	 * @return
	 */
	public static List<String> cleanDBFiles(){
		List<String> list = new ArrayList<String>();
		try
		{
			File file = new File(msglookPath);
			if (!file.canRead())
				return null;
			File[] fList = file.listFiles();
			System.out.println(String.valueOf(fList.length));
			for(File f : fList){
				if(f.isFile()){
					if(".db".equalsIgnoreCase(getFileExt(f))){
						f.delete();
					}
				}
			}			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
