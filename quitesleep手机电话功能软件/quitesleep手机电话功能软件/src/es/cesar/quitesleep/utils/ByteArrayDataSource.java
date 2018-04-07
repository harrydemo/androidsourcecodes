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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;


/**
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * @codefrom http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-android-ap
 *
 */
public class ByteArrayDataSource implements DataSource {
	
	
    private byte[] data;   
    private String type;   


    public ByteArrayDataSource(byte[] data, String type) {   
        super();   
        this.data = data;   
        this.type = type;   
    }   


    public ByteArrayDataSource(byte[] data) {   
        super();   
        this.data = data;   
    }   


    public void setType(String type) {   
        this.type = type;   
    }   


    public String getContentType() {   
        if (type == null)   
            return "application/octet-stream";   
        else  
            return type;   
    }   


    public InputStream getInputStream() throws IOException {   
        return new ByteArrayInputStream(data);   
    }   


    public String getName() {   
        return "ByteArrayDataSource";   
    }   


    public OutputStream getOutputStream() throws IOException {   
        throw new IOException("Not Supported");   
    }   



}   