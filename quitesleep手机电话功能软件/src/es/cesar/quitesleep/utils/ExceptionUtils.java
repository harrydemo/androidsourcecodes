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


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ExceptionUtils {
	
	public static final String CLASS_NAME = "es.cesar.quitesleep.utils.ExceptionUtils";
			
	
	/**
	 * 
	 * @param stackTrace
	 * @return
	 */
	public static String exceptionTraceToString (
			String exception, 
			StackTraceElement[] stackTrace) {
		
		try {
			
			String completeTrace = "";
			for (int i=0; i<stackTrace.length; i++) {
				if (completeTrace.equals(""))
					completeTrace = exception + "\n\t" + stackTrace[i].toString() + "\n";
				else
					completeTrace = completeTrace + "\t" + stackTrace[i] + "\n";
			}
			
			return completeTrace;
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, e.toString());
			return "";
		}
	}

}
