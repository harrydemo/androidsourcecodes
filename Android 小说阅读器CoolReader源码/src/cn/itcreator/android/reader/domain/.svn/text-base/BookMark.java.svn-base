/**
 * <This class for book mark .>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
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
package cn.itcreator.android.reader.domain;

import java.io.Serializable;


/**
 * This class for book mark 
 * @author Wang XinFeng
 * @version 1.0
 *
 */
public class BookMark implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7887363738929016732L;
	/**the offset in file*/
	private int currentOffset=0;
	/**book mark name*/
	private String markName="";
	/**Êé¼®µÄID*/
	private int bookId = 0;
	
	private int bookMarkId = 0;
	
	private String saveTime = "0000-00-00 00:00:00";
	
	public BookMark() {
	}
	public BookMark(int offset,String markName,int bookId){
		this.currentOffset=offset;
		this.markName = markName;
		this.bookId = bookId;
	}
	
	public int getCurrentOffset() {
		return currentOffset;
	}
	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}
	public String getMarkName() {
		return markName;
	}
	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getBookMarkId() {
		return bookMarkId;
	}
	public void setBookMarkId(int bookMarkId) {
		this.bookMarkId = bookMarkId;
	}
	
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	@Override
	public String toString() {
		return markName + "   "+saveTime;
	}
	
}
