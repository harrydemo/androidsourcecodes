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

import java.util.StringTokenizer;

import android.util.Log;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class TokenizerUtils {
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.utils.TokenizerUtils";
	
	public static void main (String[] args) {
		
		
		final String PHONE = "6-76-10--4-15-9";
		String newPhone = tokenizerPhoneNumber(PHONE, null);
		
		//Log.e("tokens", "OldPhone: " + PHONE + "NewPhone: " + newPhone);
		System.out.println("OldPhone: " + PHONE + " NewPhone: " + newPhone);
			
	}
	

	/**
	 * Tokenize the passed phoneNumber erasing the string separator and return
	 * the phone number whitout these.
	 * 
	 * @param 			phoneNumber
	 * @param 			delim
	 * @return			the new String without the delim chars
	 * @see				String
	 */
	public static String tokenizerPhoneNumber (
			String phoneNumber, 
			String delim)  {
		
		final String DEFAULT_DELIM = "-";
		
		try {
			
			//If not delim has been specified, put the default delim
			if (delim == null)
				delim = DEFAULT_DELIM;
			
			//If phone number is different from null we parse it.
			if (phoneNumber != null) {
				
				/* Separate the phone number into tokens which the delim string 
				 * is the "separate word"
				 */
				StringTokenizer tokenizer = new StringTokenizer(phoneNumber, delim);
			
				String phoneNumberWithoutDelim = "";
				while (tokenizer.hasMoreTokens()) 
					phoneNumberWithoutDelim = phoneNumberWithoutDelim + tokenizer.nextToken();
							
				return phoneNumberWithoutDelim;
			}
			return null;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}				
	}		
	
	/**
	 * Add an increase of hours to the time passed, if the delim parameter is null
	 * we use the default parameter ":"
	 * 
	 * @param 		time
	 * @param 		increase
	 * @param 		delim
	 * @return		the new time with the increase done
	 * @see			String
	 */
	public static String addIncreaseDate (String time, int increase, String delim) {
		
		try {
			final String DEFAULT_DELIM = ":";
			
			if (delim == null)
				delim = DEFAULT_DELIM;
			
			if (time != null) {
				/* Separate the time into tokens which the delim string 
				 * is the "separate word"
				 */
				StringTokenizer tokenizer = new StringTokenizer(time, delim);
			
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "count tokens: " + tokenizer.countTokens());
				if (tokenizer.countTokens() == 2) {
				
					String hourString = tokenizer.nextToken();					
					String minStrig = tokenizer.nextToken();
					
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "hourString: " + hourString + "\tminString: " + minStrig);
					
					int hour = Integer.valueOf(hourString);
					String completeNewTime = String.valueOf(hour + increase) + ":" + minStrig;
							
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "new time: " + completeNewTime);
					return completeNewTime;
				}				
			} else {
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "time == null");
				return null;
			}
			return null;
									
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	
	
}
