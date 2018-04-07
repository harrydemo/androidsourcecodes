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

package es.cesar.quitesleep.operations;

import android.util.Log;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author  Cesar Valiente Gordo
 * @mail	cesar.valiente@gmail.com
 *
 */
public class SmsOperations {

	private static final String CLASS_NAME = "es.cesar.quitesleep.operations.SmsOperations";
	

	/**
	 * Save the smsText body in the ddbb.
	 * @param 		smsText
	 * @return		true or false if depends of the successfully operation
	 */
	public static boolean saveSmsSettings (String smsText) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null) 
				settings.setSmsText(smsText);
			else {
				settings = new Settings(false);
				settings.setSmsText(smsText);
			}
			
			clientDDBB.getInserts().insertSettings(settings);
			
			clientDDBB.commit();
			clientDDBB.close();
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
}
