/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import android.util.Log;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SHA1Utils {
	
	final static String CLASS_NAME = "SHA1Util";
	
	// Genera SHA-1 de un char[]
	public static byte[] generateSHA1 (char chars[]) {
		return generateSHA1(new String(chars));
	}

	// Genera SHA-1 de un String
	public static byte[] generateSHA1 (String str) {
		return generateSHA1(str.getBytes());
	}

	// Genera SHA-1 de un InputStream
	public static byte[] generateSHA1 (InputStream is) {
		try {
			return generateSHA1(InputStreamUtils.InputStreamTOByte(is));
		} catch (Exception e) {
			return null;
		}
	}	
	
	/**
	 * This function converts a string without conding into a String encoded
	 * into a SHA1
	 * 
	 * @param str
	 * @return
	 */
	public static String generateSHA1toString (String str) {
		try
		{
			byte[] datos = generateSHA1(str.getBytes());
			return byteArrayToHexString(datos);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, 
					ExceptionUtils.exceptionTraceToString(
							e.toString(), 
							e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * This function converts an InputStream into a SHA1 String
	 * 
	 * @param is
	 * @return
	 */
	public static String generateSHA1toString (InputStream is) {
		try {
			return InputStreamUtils.byteToString(generateSHA1(InputStreamUtils.InputStreamTOByte(is)));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This function generates a SHA1 byte[] from a file
	 * @param file
	 * @return
	 */
	public static byte[] generateSHA1 (File file) {
		try {
			return generateSHA1(new FileInputStream (file));
		} catch (Exception e) {
			return null;
		}
	}	

	/**
	 * This function generates a SHA1 byte[] from another byte[].
	 * @param bytes
	 * @return
	 */
	public static byte[] generateSHA1 (byte[] bytes) {
		byte[] encryted = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();			
			digest.update(bytes);
			encryted = digest.digest();
			
		} catch (Exception e) { 
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
		return encryted;
	}
	
	

	/**
	 * This function encodes byte[] into a hex
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArrayToHexString(byte[] b){
		if (b==null) return null;
		
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++){
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * This function encodes a Hex String into a byte[]
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		if (s==null) return null;
		
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++){
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte)v;
		}
		return b;
	}
	
	/**
	 * This function compares two bytes[]
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean compareByteArrays (byte[] b1, byte[] b2) {
		return b1!=null && b2!=null && Arrays.equals(b1, b2) ;
	}

	/**
	 * This function compares two Strings.
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean compareHexString (String s1, String s2) {
		return s1!=null && s2!=null && s1.equalsIgnoreCase(s2);
	}

	
}