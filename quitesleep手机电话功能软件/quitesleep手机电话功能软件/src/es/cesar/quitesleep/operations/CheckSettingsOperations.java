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

import java.util.Calendar;

import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Schedule;
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
public class CheckSettingsOperations {
	
	private static final String CLASS_NAME = "es.cesar.quitesleep.operations.CheckServicesOperations";
	
	/**
	 * Check if the Settings object is created in the ddbb. If it's created
	 * check this attribute serviceState for check if the service is up
	 * or is down.
	 * 
	 * @return		true if the service is running or false if not.
	 */
	public static boolean checkQuiteSleepServiceState () {
		
		try {
						 			
			boolean serviceState = false;
			
			if (ConfigAppValues.getQuiteSleepServiceState() != null) 				
				serviceState = ConfigAppValues.getQuiteSleepServiceState();
			else {
				ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				clientDDBB.close();
				
				if (settings != null)
					serviceState = settings.isQuiteSleepServiceState();
			}
			
			return serviceState;						
												
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Check if today is a selected day for use the banned contacts list and schedule.
	 * 
	 * @param 			schedule
	 * @return			true if today is one "banned day" or false if isn't
	 * @see				boolean
	 */
	public static boolean checkDayWeek (Schedule schedule) {
		
		try {
							
			Calendar dateAndTime = Calendar.getInstance();
			
			int dayWeek = dateAndTime.get(Calendar.DAY_OF_WEEK);
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Day Week: " + dayWeek);
		
			switch (dayWeek) {
				case Calendar.SUNDAY:
					return schedule.isSunday();					
				case Calendar.MONDAY:
					return schedule.isMonday();					
				case Calendar.TUESDAY:
					return schedule.isTuesday();					
				case Calendar.WEDNESDAY:
					return schedule.isWednesday();
				case Calendar.THURSDAY:
					return schedule.isThursday();
				case Calendar.FRIDAY:
					return schedule.isFriday();
				case Calendar.SATURDAY:
					return schedule.isSaturday();				
			}
			
			return false;
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Check if the mail service for send email is activated or not
	 * 
	 * @param 		clientDDBB
	 * @return		true or false if it is activated or not
	 */
	public static boolean checkMailService (ClientDDBB clientDDBB) {
		
		try {
			
			if (ConfigAppValues.getMailServiceState() != null)
				return ConfigAppValues.getMailServiceState();
			else {
				//ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				if (settings != null) {
					ConfigAppValues.setMailServiceState(settings.isMailService());
					//clientDDBB.close();
					return ConfigAppValues.getMailServiceState();									
				}
				/* Mustn't be never this case because previously the settings object
				 * must be created
				 */				
				else {
					settings = new Settings(false);
					clientDDBB.getInserts().insertSettings(settings);
					clientDDBB.commit();
					//clientDDBB.close();
					return false;
				}
					
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(
					CLASS_NAME, 
					ExceptionUtils.exceptionTraceToString(e.toString(), e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Check if the sms service for send sms is activated or not
	 * 
	 * @param 		clientDDBB
	 * @return		true or false depends of the state
	 * @see			boolean
	 */
	public static boolean checkSmsService (ClientDDBB clientDDBB) {
		
		try {
			if (ConfigAppValues.getSmsServiceState() != null)
				return ConfigAppValues.getSmsServiceState();
			else {
				//ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				if (settings != null) {
					ConfigAppValues.setSmsServiceState(settings.isSmsService());
					//clientDDBB.close();
					return ConfigAppValues.getSmsServiceState();
				}else {
					settings = new Settings(false);
					clientDDBB.getInserts().insertSettings(settings);
					clientDDBB.commit();
					//clientDDBB.close();
					return false;
				}				
			}			
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}

}
