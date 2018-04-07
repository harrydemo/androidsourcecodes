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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class InputStreamUtils {
	
	
	final static int BUFFER_SIZE = 4096;		// Tamaño de los bloques a leer/escribir al comprimir en ZIP
	
	/**************************************************************************
	 * 
	 * @param in InputStream
	 * @return String
	 * @throws Exception
	 * 
	 * This function converts an InputStream to a String
	 **************************************************************************/
	
	public static String InputStreamTOString (InputStream in) throws Exception {
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	   	byte[] data = new byte[BUFFER_SIZE];
	   	int count = -1;	// Bytes leidos por bloque
	   	while ( (count = in.read(data,0,BUFFER_SIZE)) != -1 )
	   		outStream.write(data,0,count);
	   	
	   	data=null;	   	
	   	
	   	return new String (outStream.toByteArray(),"ISO-8859-1");
		
	}
	
	/**
	 * This function converts an InputStream in a predefined encoding to a String.
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamTOString (InputStream in, String encoding) throws Exception {
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	   	byte[] data = new byte[BUFFER_SIZE];
	   	int count = -1;	// Bytes leidos por bloque
	   	while ( (count = in.read(data,0,BUFFER_SIZE)) != -1 )
	   		outStream.write(data,0,count);
	   	
	   	data=null;	   	
	   	
	   	return new String (outStream.toByteArray(),encoding);
		
	}
	
	/**************************************************************************
	 * 
	 * @param in String
	 * @return InputStream
	 * @throws Exception
	 * 
	 * This function converts a String to a InputStream
	 **************************************************************************/
	
	public static InputStream StringTOInputStream (String in) throws Exception {
		
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
		
		return is;
	}
	
	
	/**************************************************************************
	 * 
	 * @param in InputStream
	 * @return byte[]
	 * @throws IOException
	 * 
	 * This function converts an InputStream to a byte[]	 
	 **************************************************************************/
	
	public static byte[] InputStreamTOByte (InputStream in) throws IOException {
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	   	byte[] data = new byte[BUFFER_SIZE];
	   	int count = -1;	// Bytes leidos por bloque
	   	while ( (count = in.read(data,0,BUFFER_SIZE)) != -1 )
	   		outStream.write(data,0,count);
	   	
	   	data=null;	   	
	   	
	   	return outStream.toByteArray();
		
	}
		
	/**
	 * This function converts a byte[] into a InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream (byte[] in) throws Exception {
		
		ByteArrayInputStream resultado = new ByteArrayInputStream(in);
		
		return resultado;		
	}
	
	/**
	 * This function converts byte[] into a String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String byteToString (byte[] in) throws Exception {
		
		InputStream ins = byteTOInputStream(in);
		return InputStreamTOString(ins);
	}
	
	
	
}