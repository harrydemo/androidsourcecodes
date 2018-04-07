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
public class StartStopServicesOperations {
	
	private static final String CLASS_NAME = "es.cesar.quitesleep.operations.StartStopService";
	
	
	/**
	 * Start or Stop the QuiteSleep incoming call service. If the service is already started
	 * nothing to do and return true, if the service isn't active, return true 
	 * saving the state in the Settings object in the ddbb.
	 * 
	 * @param 		stateQuiteSleepService
	 * @return		True if the operation was sucessfully. False if an exception occurred.
	 * @see			boolean
	 */
	public static boolean startStopQuiteSleepService (
			boolean stateQuiteSleepService) {
		
		try {								
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();			
			if (settings != null) { 										
				settings.setQuiteSleepServiceState(stateQuiteSleepService);				
						
			}else {
				settings = new Settings();
				settings.setQuiteSleepServiceState(stateQuiteSleepService);								
			}			
			clientDDBB.getUpdates().insertSettings(settings);
			
			clientDDBB.commit();			
			clientDDBB.close();
			
			//Put true or false if the start service was ok
			ConfigAppValues.setQuiteSleepServiceState(stateQuiteSleepService);
			
			//IncomingCallOperations.vibrateOff();
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;			
		}
	}
	
	
	
	/**
	 * Function that update the ddbb with the state of mailService
	 * 
	 * @param	mailServiceState
	 * @return	true if the operation was sucessfully or false if some
	 * 			exception occurred
	 * @see		boolean
	 */
	public static boolean startStopMailService (boolean mailServiceState) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null) 
				settings.setMailService(mailServiceState);
			
			else {
				settings = new Settings(false);
				settings.setMailService(mailServiceState);
			}			
			clientDDBB.getUpdates().insertSettings(settings);
			
			clientDDBB.commit();
			clientDDBB.close();
			
			//Put the mail state service in the static attribute
			ConfigAppValues.setMailServiceState(mailServiceState);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Function that update the ddbb with the state of smsService
	 * 
	 * @param 		smsServiceState
	 * @return		true if the operation was sucessfully or false if not
	 * @see			boolean
	 */
	public static boolean startStopSmsService (boolean smsServiceState) {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null)
				settings.setSmsService(smsServiceState);
			else {
				settings = new Settings(false);
				settings.setSmsService(smsServiceState);
			}
			
			clientDDBB.getInserts().insertSettings(settings);
			
			clientDDBB.commit();
			clientDDBB.close();
			
			//Put the sms state service in the static attribute
			ConfigAppValues.setSmsServiceState(smsServiceState);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	

}
