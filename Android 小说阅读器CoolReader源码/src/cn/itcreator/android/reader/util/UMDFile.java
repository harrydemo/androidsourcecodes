/**
 * <This class for resolve UMD file.>
 *  Copyright (C) <2009>  <mingkg21,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package cn.itcreator.android.reader.util;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cn.itcreator.android.reader.domain.BookInfo;
import cn.itcreator.android.reader.domain.Content;

/**
 * @author mingkg21
 * Date: 2009-4-8
 */

public class UMDFile {
	
	/** UMD file format */
	public static final long UMD_FORMAT = 0xde9a9b89L;
	/** UMD file type: text and picture */
	public static final byte UMD_BOOK_TYPE_TEXT = 1;
	public static final byte UMD_BOOK_TYPE_PICTURE = 2;
	
	public int contentLength;
	private long additionalCheck;
	
	/** UMD file info, eg: book title, author, publisher */
	public BookInfo bookInfo;
	
	public int[] chapOff;
	private ArrayList<String> chapters;
	
	private ArrayList<Content> contentArr;
	
	private long currentPoint;
	
//	private InputStream is;
	private String bookPath;
	
	public UMDFile() {
		bookInfo = new BookInfo();
		this.chapters = new ArrayList<String>();
		this.contentArr = new ArrayList<Content>();
	}

	private long readUInt32(DataInputStream dis) throws Exception{
		return IntergerUtil.int2long(IntergerUtil.getInt(readBytes(dis, 4)));
	}
	
	private int readInt32(DataInputStream dis) throws Exception{
		return IntergerUtil.getInt(readBytes(dis, 4));
	}
	
	private short readInt16(DataInputStream dis) throws Exception{
		return IntergerUtil.getShort(readBytes(dis, 2));
	}
	
	private byte[] readBytes(DataInputStream dis, int num) throws Exception{
		if(num <= 0 || null == dis){
			return null;
		}
		byte[] value = new byte[num];
		long temp = dis.read(value);
		currentPoint += temp;
		return value;
	}
	
	private int readBytes(DataInputStream dis, byte[] bytes) throws Exception{
		currentPoint += bytes.length;
		return dis.read(bytes);
	}
	
	private byte readByte(DataInputStream dis) throws Exception{
		currentPoint += 1;
		return dis.readByte();
	}
	
	private String readString(DataInputStream dis, byte length) throws Exception{
		return new String(IntergerUtil.getReverseBytes(readBytes(dis, length)), "UNICODE");
	}
	
	/** get the cover image of the UMD file
	 * @return
	 */
	public Bitmap getCoverImage(){
		if(bookInfo == null){
			return null;
		}
		if(bookInfo.cover == null){
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(bookInfo.cover, 0, bookInfo.cover.length);
		
		return bitmap;
	}
	
//	public Drawable getDrawable(int index){
//		byte[] bytes = getContentBytes(index);
//		if(bytes == null){
//			return null;
//		}
//		Drawable drawable = Drawable
//	}
	
	/** if the type of the UMD file is picture, then content is picture. get the picture from content
	 * @param index
	 * @return
	 */
	public Bitmap getBitmap(int index){
		byte[] bytes = getContentBytes(index);
		if(bytes == null){
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public int getContentSize(){
		if(contentArr == null){
			return 0;
		}
		return contentArr.size();
	}
	
	public int getChapterSize(){
		if(chapters == null){
			return 0;
		}
		return chapters.size();
	}
	
	public ArrayList<String> getChapters(){
		return chapters;
	}
	
	public String getContentText(int index){
		String tempStr = null;
		try {
			byte[] newBytes = new byte[0x8000];
			Inflater inflater1 = new Inflater();
			inflater1.setInput(getContentBytes(index));
			inflater1.inflate(newBytes);
			byte[] strBytes = new byte[inflater1.getTotalOut()];
			System.arraycopy(newBytes, 0, strBytes, 0, Math.min(strBytes.length, inflater1.getTotalOut()));
			tempStr = new String(IntergerUtil.getReverseBytes(strBytes), "UNICODE").replace("\u2029", "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return tempStr;
	}
	
	/** if the type of the UMD file is text, 
	 * @param context
	 */
	public void writeFile(Context context){
		try {
			InputStream is = new DataInputStream(new FileInputStream(bookPath));
			long skipNum = 0;
			long nn = 0;
			int tempI = 0;
			byte[] copyByte = null;
			int copyLength = 0;
			for(int j = 0; j < this.chapOff.length; ++j){
				int start = this.chapOff[j];
				int strByteL = 0;
				if(j < this.chapOff.length - 1){
					strByteL = this.chapOff[j+1] - start;
				}else{
					strByteL = this.contentLength - start;
				}
				int tempL = 0;
				byte[] strBytes = new byte[strByteL];
				if(copyByte != null){
					tempL = copyByte.length - copyLength;
					System.arraycopy(copyByte, copyLength, strBytes, 0, tempL);
				}
				for(int i = tempI; i < contentArr.size(); ++i){
					long index = contentArr.get(i).getIndex();
					skipNum = index - nn;
					is.skip(skipNum);
					int length = (int) contentArr.get(i).getLength();
					nn = index + length;
					byte[] bytes = new byte[length];
					is.read(bytes);
					byte[] newBytes = new byte[0x8000];
					Inflater inflater1 = new Inflater();
					inflater1.setInput(bytes);
					inflater1.inflate(newBytes);
					if(tempL < strBytes.length){
						copyLength = Math.min(strBytes.length - tempL, inflater1.getTotalOut());
						System.arraycopy(newBytes, 0, strBytes, tempL, copyLength);
						tempL += inflater1.getTotalOut();
						if(tempL >= strBytes.length){
							copyByte = newBytes;
							tempI = i + 1;
							break;
						}
					}
				}
				String content = new String(IntergerUtil.getReverseBytes(strBytes), "UNICODE").replace("\u2029", "\n");
				
				// file path: /sdcard/cr_temp/crbookXX.txt
				String folderPath = "/sdcard/cr_temp";
				File cr_temp = new File(folderPath);
				if(!cr_temp.exists()){
					cr_temp.mkdir();
				}
				StringBuffer sb = new StringBuffer();
				sb.append(folderPath);
				sb.append("/crbook");
				sb.append(j);
				sb.append(".txt");
//				String fileName = "/sdcard/tempfile" + j + ".txt";
				FileOutputStream fos = new FileOutputStream(sb.toString());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.write(content);
				bw.flush();
				bw.close();
				fos.close();
			}
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getContentBytes(int index){
		byte[] bytes = null;
		InputStream is = null;
		try{
			is = new DataInputStream(new FileInputStream(bookPath));
			long skipLength= contentArr.get(index).getIndex();
			is.skip(skipLength);
			int length = (int) contentArr.get(index).getLength();
			bytes = new byte[length];
			is.read(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}
	
	public void read(String umdFile){
		bookPath = umdFile;
		InputStream is = null;
		try {
			is = new DataInputStream(new FileInputStream(bookPath));
			read(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/** parse the UMD file and read the content of UMD file
	 * @param is
	 */
	public void read(InputStream is){
		try {
			DataInputStream dis = new DataInputStream(is);
			long header;
			//check UMD format
			header = readUInt32(dis);
			if(UMD_FORMAT != header){
				throw new Exception("invalid file format\n");
			}
			byte[] symbol = new byte[1];
//			int eof = dis.read(symbol);
			int eof = readBytes(dis, symbol);
			while(eof != -1 && 0x23 == symbol[0]){//0x23 '#'
				short id = readInt16(dis);
//				byte num3 = dis.readByte();
//				byte length = (byte) (dis.readByte() - 5);
				byte num3 = readByte(dis);
				byte length = (byte) (readByte(dis) - 5);
				readSection(id, num3, length, dis);
//				eof = dis.read(symbol);
				eof = readBytes(dis, symbol);
				if((0xf1 == id) || (10 == id)){
					id = 0x84;
				}
				while(eof != -1 && '$' == symbol[0]){
					long num5 = readUInt32(dis);
					long num6 = readUInt32(dis) - 9;
					readAdditional(id, num5, num6, dis);
//					eof = dis.read(symbol);
					eof = readBytes(dis, symbol);
				}
			}
			dis.close();
//			is.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("read errs " + e);
			e.printStackTrace();
		}
	}
	
	/** read the content
	 * @param id
	 * @param check
	 * @param length
	 * @param dis
	 * @throws Exception
	 */
	private void readAdditional(short id, long check, long length, DataInputStream dis) throws Exception{
		int num1;
		int num2;
		byte[] buffer1;
		switch(id){
		case 0x0E://only picture
			if(UMD_BOOK_TYPE_PICTURE == bookInfo.type){
				this.contentArr.add(new Content(currentPoint, length));
			}
			readBytes(dis, (int) length);
			return;
		case 0x0F://only text
			return;
		case 0x81:
			readBytes(dis, (int) length);
			return;
		case 130://cover image
			this.bookInfo.cover = readBytes(dis, (int) length);
			return;
		case 0x83://each chapter length
			int chapOffLen = (int) (length / 4);
			this.chapOff = new int[chapOffLen];
			num1 = 0;
			while(num1 < chapOffLen){
				this.chapOff[num1] = readInt32(dis);
				num1++;
			}
			return;
		case 0x84:
			//read the content
			if(this.additionalCheck != check){
				this.contentArr.add(new Content(currentPoint, length));
				readBytes(dis, (int) length);
				return;
			}
			num2 = 0;
			buffer1 = readBytes(dis, (int) length);
			if(null == buffer1){
				return;
			}
			//read the chapter's name
			while(num2 < buffer1.length){
				byte num3 = buffer1[num2];
				byte[] temp = new byte[num3];
				++num2;
				for(int i = 0; i < num3; ++i){
					temp[i] = buffer1[i+num2];
				}
				this.chapters.add(new String(IntergerUtil.getReverseBytes(temp), "UNICODE"));
				num2 += num3;
			}
			return;
		default:
			readBytes(dis, (int) length);
			return;
		}
	}
	
	private void readSection(short id, byte b, byte length, DataInputStream dis) throws Exception{
		switch(id){
		case 1://type
			this.bookInfo.type = readByte(dis);
			this.bookInfo.pgkSeed = readInt16(dis);
			return;
		case 2://title
			this.bookInfo.title = readString(dis, length);
			return;
		case 3://author
			this.bookInfo.author = readString(dis, length);
			return;	
		case 4://year
			this.bookInfo.year = readString(dis, length);
			return;
		case 5://month
			this.bookInfo.month = readString(dis, length);
			return;
		case 6://day
			this.bookInfo.day = readString(dis, length);
			return;
		case 7://gender
			this.bookInfo.gender = readString(dis, length);
			return;
		case 8://publisher
			this.bookInfo.publisher = readString(dis, length);
			return;
		case 9://vendor
			this.bookInfo.vendor = readString(dis, length);
			return;
		case 10:
			this.bookInfo.cid = readInt32(dis);
			return;
		case 11://
			this.contentLength = readInt32(dis);
			return;
		case 12:
			readUInt32(dis);
			return;
		case 0x81:
		case 0x83:
		case 0x84:
			this.additionalCheck = readUInt32(dis);
			return;
		case 0x0E:
			readByte(dis);
			return;
		case 0x0F:
			readByte(dis);
			return;
		case 130:
			readByte(dis);
			this.additionalCheck = readUInt32(dis);
			return;
		}
		readBytes(dis, length);
	}

}
