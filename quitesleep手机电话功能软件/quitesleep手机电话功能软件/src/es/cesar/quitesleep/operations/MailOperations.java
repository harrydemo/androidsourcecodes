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
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class MailOperations {
	
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.operations.MailOperations";
	
	
	/**
	 * Function that save the mail settings into Settings object from the ddbb,
	 * if the Settings object doesn't exists we create it, and if it exists,
	 * update one.
	 * 
	 * @param 		user
	 * @param 		passwd
	 * @param 		subject
	 * @param 		body
	 * 
	 * @return		true or false if the operation was sucessfully or not
	 * @see			boolean
	 */
	public static boolean saveMailSettings (
			String user, 
			String passwd, 
			String subject, 
			String body) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			
			/* -------	Save the mail settings, both if Settings object not
			 * exists or if exists		------------------------------------- */
			
			Settings settings = clientDDBB.getSelects().selectSettings();

			if (settings != null) {
				settings.setUser(user);
				settings.setPasswd(passwd);
				settings.setSubject(subject);
				settings.setBody(body);
				clientDDBB.getUpdates().insertSettings(settings);								
			}			
			/* Never must be used this else because in onCreate, if the Settings
			 * object isn't created previously, then there we create it.
			 * But we i leave post because previously i already coded it.
			 */
			else {
				settings = new Settings(false);
				settings.setUser(user);
				settings.setPasswd(passwd);
				settings.setSubject(subject);
				settings.setBody(body);
				clientDDBB.getInserts().insertSettings(settings);				
			}
			
			
			//------------		Commit & close		-------------------------//
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
	
	
	/**
	 * Function that save the mailService sending state, in both, the Settings
	 * object in the ddbb and the static variable for more fast access later.
	 * 
	 * @param 			mailServiceState
	 * 
	 * @return			true or false depends of sucessfully operation
	 * @see				boolean
	 */
	public static boolean saveMailServiceState (boolean mailServiceState) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			Settings settings = clientDDBB.getSelects().selectSettings();
			if (settings != null) {
				settings.setMailService(mailServiceState);
				clientDDBB.getUpdates().insertSettings(settings);
			}
			/* Never must be used this else because in onCreate, if the Settings
			 * object isn't created previously, then there we create it.
			 * But we i leave post because previously i already coded it.
			 */
			else {
				settings = new Settings(false);
				settings.setMailService(mailServiceState);
				clientDDBB.getUpdates().insertSettings(settings);
			}
			
			//Update the global static variable for fast access later
			ConfigAppValues.setMailServiceState(mailServiceState);
			
			//Comit & close
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
