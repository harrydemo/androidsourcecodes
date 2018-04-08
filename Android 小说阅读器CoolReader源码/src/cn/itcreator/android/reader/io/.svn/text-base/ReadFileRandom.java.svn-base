/**
 * Copyright (C) 2009 Android OS Community Inc (http://androidos.cc/bbs).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itcreator.android.reader.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

/**
 * This class for read a file
 * 
 * @author SinFrancis
 * @version 1.0
 * 
 */
public class ReadFileRandom {
	//private RandomAccessFile randomAccessFile = null;
	private InputStream dataInputStream = null;
	private String filePath = null;
	public ReadFileRandom(String path) {
		String tag = "ReadFileRandom";
		this.filePath = path;
		try {
		//	randomAccessFile = new RandomAccessFile(filePath,"rw");
			dataInputStream = new DataInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			Log.d(tag, "Exception :"+e.getMessage());
		}
	}
	
	/**
	 * 打开新的随机流
	 */
	public void openNewStream(){
		close();
		try {
			//randomAccessFile = new RandomAccessFile(filePath,"rw");
			dataInputStream = new DataInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
		}
	}
	/**
	 * 读取数据
	 * @param length 读取多长
	 * @return
	 */
	public byte[] readBytes(int length){
		byte[] b = new byte[length];
		try {
			//randomAccessFile.read(b);
			if(dataInputStream == null){
				dataInputStream = new DataInputStream(new FileInputStream(filePath));
			}
			dataInputStream.read(b);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return b;
	}
	
	
	/**
	 * 读取数据，并将数据加载到字节数组中
	 * @param buffer
	 * @return 返回实际读取的字节数
	 */
	public int readBytes(byte[] buffer){
		int i = 0;
		try {
			i= dataInputStream.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}	
		return i;
	}
	
	
	/**
	 * 跳过字节
	 * @param length 跳过多少
	 */
	public void skip(int length){
		try {
			//dataInputStream.skipBytes(length);
			dataInputStream.skip(length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 快速跳过
	 * @param length 跳过多少
	 */
	public void fastSkip(int length ){
		readBytes(length);
	}
	
	
	
	/**
	 * 快速定为的文件中的位置
	 * @param location 定为的地点
	 */
	public void locate(int location){
		readBytes(location);
	}

	/**
	 * 得到文件的当前位置
	 * @return
	 */
	/*public long getCurrentLocation(){
		long i = 0;
		 try {
			i = randomAccessFile.getFilePointer();
		} catch (IOException e) {
			 return i;
		}
		 return i;
	}*/
	/**
	 * 取得文件的长度
	 * @return
	 */
	public long getFileLength(){
		long i =0;
		try {
			i= new File(filePath).length();
		} catch (Exception e) {
		}
		return i;
	}
	
	
	/**
	 * 关闭流
	 */
	public void close(){
		if(null!=dataInputStream)
			try {
				dataInputStream.close();
			} catch (IOException e) {
			}	
	}

	
	public static void main(String[] args) {
		
		ReadFileRandom r = new ReadFileRandom("src/mayun.txt");
		byte[] b = new byte[10]; 
		r.readBytes(b);
		System.err.println(new String(b));
		r.skip(10);
		b = r.readBytes(21);
		System.out.println("=================");
		System.err.println(new String(b));
		r.close();
	}
	
}
