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

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * Class for show or not Toast notificactions
 * Two levels:
 * - Debug mode (for develop process)
 * - Release mode (for release app)
 *
 */
public class QSToast {

	private final static String CLASS_NAME = "es.cesar.quitelseep.utils.QSToast";
	
	public static final boolean DEBUG = false;
	public static final boolean RELEASE = true;
	
	
	/**
	 * Toast for debug (d) mode
	 * 
	 * @param context
	 * @param message
	 * @param length
	 */
	public static void d(Context context, String message, int length) {
		
		Toast.makeText(
			context,
			message,
			length).show();
	}
	
	/**
	 * Toast for release (r) mode
	 * 
	 * @param context
	 * @param message
	 * @param length
	 */
	public static void r(Context context, String message, int length) {
		
		Toast.makeText(
			context,
			message,
			length).show();
	}
	
}
