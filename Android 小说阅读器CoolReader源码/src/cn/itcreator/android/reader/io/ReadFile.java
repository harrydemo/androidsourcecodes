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

/**
 * This class for read a file
 * 
 * @author SinFrancis
 * @version 1.0
 * 
 */
public class ReadFile {
	/**data input stream*/
	private DataInputStream mDataInputStream;
	/**file path*/
	private String mFilePath;
	/**bytes u wanna skip*/
	final static int mSkipBuffer = 16384;
	/**the buffer size*/
	final static int mBUFFER = 16384;
	/**file*/
	private File mFile;
	/**offset*/
	private int mOffset = 0;
	/**
	 * constructor a new read file instance
	 * @param filePath file path
	 */
	public ReadFile(String filePath)throws FileNotFoundException {
		mFilePath = filePath;
		mOffset = 0;
		mFile = new File(mFilePath);
		open();
	}

	/**
	 * open a input stream
	 */
	public void open()throws FileNotFoundException {
			if (!isDirectory()) {
				mDataInputStream = new DataInputStream(new FileInputStream(
						mFilePath));
			}
		
	}

	/**
	 * read data
	 * 
	 * @param len
	 *            u wanna read data size
	 * @return
	 */
	public byte[] read(int len) {
		
		if (isDirectory()) {//is a directory?
			return null;
		}

		byte abyte0[] = null;

		long l = 0L;
		l = mFile.length();//get file length

		if (mOffset + len > (int) l) {
			len = (int) (l - mOffset);
		}
		abyte0 = new byte[len];// new a byte array
		if (mBUFFER > len) { // read all data
			try {
				mDataInputStream.read(abyte0);
				mOffset += len;
			} catch (IOException ex1) {
				return null;
			}
		} else { // read the data by paragraph
			int datapos = 0;
			int times = len / mBUFFER;// calculate the buffer count
			for (int i = 0; i < times; i++) {
				byte[] buffer = new byte[mBUFFER];
				try {
					mDataInputStream.read(buffer);
					
					//copy the data to a byte array
					System.arraycopy(buffer, 0, abyte0, datapos, mBUFFER);
					
					datapos += mBUFFER;
					mOffset += mBUFFER;
				} catch (IOException ex2) {
					return null;
				}
			}
			int rest = len - datapos;
			//If you read the provisions of the length of the actual reading is greater than the length of the
			//go on read data
			byte[] buffer = new byte[rest];
			try {
				mDataInputStream.read(buffer);
				System.arraycopy(buffer, 0, abyte0, datapos, rest);
				mOffset += rest;
			} catch (IOException ex3) {
			}
			buffer = null;
		}
		System.gc();
		return abyte0;
	}

	
	/**
	 * Analysis of bytes of data to see if there are half a Chinese phenomenon, if there is a time in the future to continue bytes
	 * @param b To analyze the data
	 * @return Completion of the analysis of the data
	 */
	public byte[] analyzeByte(byte[] b){
		int ii = 0;
		byte[] temp;
    	for (int i=0;i<b.length ;i++) {
    		byte x = b[i];
			if(x<0){
				ii++;//The number of bytes of the characters have been
			}
		}
    	
    	if(ii %2 !=0){//If the Chinese characters for the odd number of bytes, that there are half Chinese
    		temp =new byte[b.length+1];//Generation than in the past few head of a new array
    		System.arraycopy(b, 0, temp, 0, b.length);//The original copy data to the inside
    		try {
				temp[b.length]=mDataInputStream.readByte();//In the ensuing read a byte
			} catch (IOException e) {
				e.printStackTrace();
			}
    		mOffset++;//A shift in the future will be offset bytes
    		return temp;
    	}else{
    		
    		return b;
    	}
	}
	
	/**
	 * Skip a number of
	 * 
	 * @param len
	 *            int The figures need to skip
	 */
	public void skip(int len) throws FileNotFoundException{
		if (isDirectory()) {
			return;
		}

		mOffset += len;
		if (len < 0) {
			close();
			open();
			fastSkip((int) mOffset);
			return;
		}
		fastSkip(len);
		return;
	}

	/**
	 *  documents to the position shift
	 * 
	 * @param i
	 *            int Shift the location of
	 */
	public void locate(int i) throws FileNotFoundException{
		if (i < 0 || i > fileSize() - 1) {
			return;
		}
		int len = i - mOffset;
		skip(len);
	}

	/**
	 * use read instead of read,fast skip
	 * @param len
	 *            int the length skiped
	 */
	private void fastSkip(int len) {
		if (len <= 0) {
			return;
		}

		byte[] b;
		if (mSkipBuffer > len) { // Reading a one-time income
			b = new byte[len];
			try {
				mDataInputStream.read(b);
			} catch (IOException ex) {
			}
		} else { //read the data by paragraph
			int times = len / mSkipBuffer;
			for (int i = 0; i < times; i++) {
				b = new byte[mSkipBuffer];
				try {
					mDataInputStream.read(b);
				} catch (IOException ex1) {
				}
			}
			int rest = len - mSkipBuffer * times;
			b = new byte[rest];
			try {
				mDataInputStream.read(b);
			} catch (IOException ex2) {
			}
		}
		b = null;
		System.gc();
	}

	public long fileSize() {
		long l = -1L;

		l = mFile.length();
		return l;
	}
	
	
	/**
	 * the current offset
	 * @return int 
	 */
	public int getMOffset(){
		return mOffset ;
	}

	/**
	 * the file is exist?
	 * @return exist return true,otherwise false
	 */
	public boolean exists() {
		return mFile.exists();
	}

	/**
	 * is a directory ?
	 * @return is a diredctory return true,otherwise false
	 */
	public boolean isDirectory() {
		return mFile.isDirectory();
	}

	/**
	 *close file stream
	 */
	public void close() {
		try {
			if (!isDirectory()) {
				mDataInputStream.close();
			}
			mDataInputStream = null;
		} catch (Exception exception) {
		}
	}
	
}
