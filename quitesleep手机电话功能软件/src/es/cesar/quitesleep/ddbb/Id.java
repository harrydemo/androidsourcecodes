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

package es.cesar.quitesleep.ddbb;

import java.io.Serializable;

import es.cesar.quitesleep.utils.SHA1Utils;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String id = "";
	
	public String getId () {
		return id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	/**
	 * Constructor that create an unique Identificator Id for any object
	 * in the DDBB.
	 */
	public Id () {								
		
		//get the actual system time in nanoseconds
		String time = String.valueOf(System.nanoTime());
		
		//Create a sha1 string for the time previously got
		this.id = SHA1Utils.generateSHA1toString(time);															
		
	}
	
}
