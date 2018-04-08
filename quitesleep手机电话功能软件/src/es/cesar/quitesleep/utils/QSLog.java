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

import android.util.Log;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class QSLog {	

  public static final boolean DEBUG_V = false;
  public static final boolean DEBUG_D = false;
  public static final boolean DEBUG_I = false;
  public static final boolean DEBUG_W = false;
  public static final boolean DEBUG_E = true;
  
  
  public static void v(String className, String msg) {
    Log.v(className, msg);
  }
  
  public static void d(String className, String msg) {
	    Log.d(className, msg);
  }
  
  public static void i(String className, String msg) {
	    Log.i(className, msg);
  }
  
  public static void w(String className, String msg) {
	    Log.w(className, msg);
  }

  public static void e(String className, String msg) {
    Log.e(className, msg);
  }
  
  
}
