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

package es.cesar.quitesleep.listeners;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import es.cesar.quitesleep.callServices.NormalModeCallService;
import es.cesar.quitesleep.callServices.SilentModeCallService;
import es.cesar.quitesleep.operations.CheckSettingsOperations;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class MyPhoneStateListener extends PhoneStateListener {
	
	private final String CLASS_NAME = getClass().getName();
			
	/**
	 * Constructor with the context parameter for use if the application
	 * not run and is the BOOT_COMPLETED BroadcastReceiver that is launch
	 * this.
	 * @param context
	 * @param ITelephony
	 */
	public MyPhoneStateListener (Context context) {
		
		super();
		if (ConfigAppValues.getContext() == null)
			ConfigAppValues.setContext(context);		
	}
	
	
	/**
	 * Function that receive the state id for the phone state type 
	 * (idle, offhook and ringing), and the incoming number that is doing
	 * the call.
	 */
	public void onCallStateChanged (int state, String incomingNumber) {
		
		try {										
			
			switch (state) {					
			
				//-------------		CALL_STATE_IDLE		----------------------//
				//Device call state: No activity. 
				case TelephonyManager.CALL_STATE_IDLE:																				
					processCallStateIdle();												
					break;
				
				//-----------------		CALL_STATE_OFFHOOK		--------------//
				//Device call state: Off-hook. At least one call exists that is 
				//dialing, active, or on hold, and no calls are ringing or waiting. 
				case TelephonyManager.CALL_STATE_OFFHOOK:					
					processCallStateOffhook();
					break;
				
				//-----------------		CALL_STATE_RINGING		--------------//
				//Device call state: Ringing. A new call arrived and is ringing 
				//or waiting. In the latter case, another call is already active. 
				case TelephonyManager.CALL_STATE_RINGING:							
					processCallStateRinging(incomingNumber);
					break;
					
				default:
					break;
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}		
	
	
	/**
	 * Proces the CALL_STATE_IDLE signal from PhoneReceiver
	 */
	private void processCallStateIdle () {
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "IDLE");
			
			ConfigAppValues.processRingCall = false;
			
			if (ringerMode() == AudioManager.RINGER_MODE_SILENT && 
					CheckSettingsOperations.checkQuiteSleepServiceState() && 
					!ConfigAppValues.processIdleCall) {																						
				
				/* Put one pause of 1 second for wait before put the
				 * ringer mode  to normal again
				 */						
				//Thread.sleep(1000);						
				ConfigAppValues.processIdleCall = true;

				//--  If you choose use Android Service for process incoming call use this --//				
				ConfigAppValues.getContext().startService(
						new Intent(
								ConfigAppValues.getContext(),
								NormalModeCallService.class)); 				
				//------------------------------------------------------------//							

				
				if (QSToast.DEBUG) QSToast.d(
						ConfigAppValues.getContext().getApplicationContext(),
						"IDLE!!!!!!!!",
						Toast.LENGTH_SHORT);
			}					
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(
					CLASS_NAME, 
					ExceptionUtils.exceptionTraceToString(e.toString(), e.getStackTrace()));
			
		}
	}
	
	/**
	 * Proces the CALL_STATE_OFFHOOK signal from PhoneReceiver
	 */
	private void processCallStateOffhook () {
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "OFFHOOK");	
			
			ConfigAppValues.processRingCall = false;
			
			if (QSToast.DEBUG) QSToast.d(
            		ConfigAppValues.getContext().getApplicationContext(),
            		"OFFHOOK!!!!!!!!",
            		Toast.LENGTH_SHORT);	
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	/**
	 *	Proces the CALL_STATE_RINGING signal from PhoneReceiver 
	 */
	private void processCallStateRinging (String incomingNumber) {
		
		try {
						
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "RINGING");					
			
			/* Put the device in silent mode if the incoming number is
			 * from contact banned and in schedule interval 
			 */					
			if (ringerMode() != AudioManager.RINGER_MODE_SILENT && 
					CheckSettingsOperations.checkQuiteSleepServiceState() &&
					!ConfigAppValues.processRingCall)  {				
					
				ConfigAppValues.processRingCall = true;
				
				
				// Process to control the incoming call and kill it if proceed.				
				ConfigAppValues.getContext().startService(
						new Intent(
								ConfigAppValues.getContext(),
								SilentModeCallService.class).putExtra(
									ConfigAppValues.INCOMING_CALL_NUMBER, 
									incomingNumber));											
				
				//------------------------------------------------------------//
										
				if (QSToast.DEBUG) QSToast.d(
						ConfigAppValues.getContext().getApplicationContext(),
						"RINGING!!!!!!!!",
						Toast.LENGTH_SHORT);															
			}														
	
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Get what ringer mode is at the moment. 
	 * 
	 * @return			return RINGER_MODE_SILENT(0), RINGER_MODE_NORMAL(2), RINGER_MODE_VIBRATE(1)	  								
	 * @see 			int	
	 * @throws 			Exception
	 */
	private int ringerMode () throws Exception {
		
		try {
			
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
			
			
			//Only for traces
			switch (audioManager.getRingerMode()) {
			
				case AudioManager.RINGER_MODE_SILENT:
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Ringer_Mode_Silent");					
					break;
				
				case AudioManager.RINGER_MODE_NORMAL:
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Ringer_Mode_Normal");					
					break;
				
				case AudioManager.RINGER_MODE_VIBRATE:
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Ringer_Mode_Vibrate");
					break;
					
				default:
					break;
			}
			
			return audioManager.getRingerMode();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
		}
		
	}

}
