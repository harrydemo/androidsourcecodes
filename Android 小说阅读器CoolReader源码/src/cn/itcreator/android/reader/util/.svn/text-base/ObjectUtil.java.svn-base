/**
 * <object operation util.>
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
package cn.itcreator.android.reader.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * object operation util
 * 
 * @author Wang XinFeng
 * @version 1.0
 */
public class ObjectUtil {
	
	/**object output stream*/
	private ObjectOutputStream mObjectOutputStream = null;
	
	/**object input stream*/
	private ObjectInputStream mObjectInputStream = null;
	
	/**the file path*/
	private String filePath;

	/**
	 * 
	 * @param filePath
	 *            file path  when read a object or save object file
	 */
	public ObjectUtil(String filePath) {
		this.filePath = filePath;

	}

	/**
	 * save object to file
	 * 
	 * @param o
	 *            a object u wanna save
	 * @return if save successful ,return true ,otherwise false
	 */
	public boolean saveToFile(Object o) {

		boolean flag = true;
		try {
			mObjectOutputStream = new ObjectOutputStream(new FileOutputStream(
					filePath));
			mObjectOutputStream.writeObject(o);
			mObjectOutputStream.flush();
			mObjectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * read a file to java object
	 * @return the java object
	 */
	public Object fileToObject() {
		Object ob = null;
		try {
			mObjectInputStream = new ObjectInputStream(new FileInputStream(
					filePath));
			ob = mObjectInputStream.readObject();
			mObjectInputStream.close();
		} catch (IOException e) {

			ob = null;
		} catch (ClassNotFoundException e) {
			ob = null;
		}

		return ob;
	}

	
}
