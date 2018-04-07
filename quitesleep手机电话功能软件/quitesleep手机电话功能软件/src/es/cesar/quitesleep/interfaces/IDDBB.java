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

package es.cesar.quitesleep.interfaces;

import android.os.Environment;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public interface IDDBB {
		
	//For the sdcard I/O
	public final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final String QUITESLEEP_PATH = "quitesleep";
	
	//For the sdcard & internal data app
	public final String DDBB_DIR = "ddbb"; 
	public final String DDBB_FILE = "quitesleep.db";
			
	
	//Specify the deep down in the objects search on db4o
	public final int DEEP = 3;
	
	//Global semaphore for all ddbb functions
	public static final String SEMAPHORE = "SEMAPHORE";		
	
}
