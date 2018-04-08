/**
 * <This class for process byte[].>
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

import java.math.BigInteger;

/**
 * @author mingkg21
 * Date: 2009-3-27
 */
public class IntergerUtil {
	
	public static final int getInt(byte[] bytes){
		return (new BigInteger(getBytes(bytes))).intValue();
	}
	
	public static final short getShort(byte[] bytes){
		return (new BigInteger(getBytes(bytes))).shortValue();
	}
	
	public static final byte[] getBytes(byte[] bytes){
		int length = bytes.length;
		byte[] temp = new byte[length];
		for(int i = 0; i < length; ++i){
			temp[length - i - 1] = bytes[i];
		}
		return temp;
	}
	
	public static final void reverseBytes(byte[] bytes){
		int length = bytes.length;
		for(int i = 0; i < length; i += 2){
			byte highBit = bytes[i];
			byte lowBit = bytes[i+1];
			bytes[i] = lowBit;
			bytes[i+1] = highBit;
		}
	}
	
	public static final byte[] getReverseBytes(byte[] bytes){
		int length = bytes.length;
		for(int i = 0; i < length; i += 2){
			byte highBit = bytes[i];
			byte lowBit = bytes[i+1];
			bytes[i] = lowBit;
			bytes[i+1] = highBit;
		}
		return bytes;
	}
	
	public static final long int2long(int value){
		long temp = (long) value;
		if(value < 0){
			temp = temp << 32;
			temp = temp >>> 32;
		}
		return temp;
	}

}
