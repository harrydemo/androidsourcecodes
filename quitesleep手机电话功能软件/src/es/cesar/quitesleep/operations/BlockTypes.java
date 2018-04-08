/* 
 	Copyright 2011 Cesar Valiente Gordo
 
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

import java.lang.reflect.Method;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import es.cesar.quitesleep.beans.BCBean;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.MuteOrHangUp;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * This class contains the methods to block or not the incoming call regarding
 * the incoming number and block configuration.
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * @version 1 02-16-2011
 *
 */
public class BlockTypes {
	
	
	public static final String CLASS_NAME = "es.cesar.quitesleep.operations.BlockTypes";
	
	private static ITelephony telephonyService = null;
	
	static {	
		telephonyService = createITelephonyImp();		
	}
	
	/**
	 * This function checks is must be re-initialize the telephnyService
	 */
	private static void checkTelephonyService () {
		if (telephonyService == null)
			telephonyService = createITelephonyImp();
	}
	
	/**
	 * This function performs the block action that proceed, muting or hang up.
	 */
	private static void muteOrHangupAction () {
		
		try {
			
			if (ConfigAppValues.getMuteOrHangup() == null) {
				ClientDDBB clientDDBB = new ClientDDBB();
				MuteOrHangUp muteOrHangup = clientDDBB.getSelects().selectMuteOrHangUp();
				if (muteOrHangup != null) {
					if (muteOrHangup.isHangUp())
						ConfigAppValues.setMuteOrHangup(true);
					else
						ConfigAppValues.setMuteOrHangup(false);
				}
				/* the mute operation is the one by default, and if we reach at
				 * this point, then we insert the new object in the ddbb as the
				 * first and unique MuteOrHangUp object.
				 * This section not should reach never, but I put this to cure myself.
				 */
				else {
					ConfigAppValues.setMuteOrHangup(false);
					muteOrHangup = new MuteOrHangUp(true, false);					
					clientDDBB.getInserts().insertMuteOrHangUp(muteOrHangup);
					clientDDBB.commit();
				}
				
				clientDDBB.close();
			}
			
			//Block the incoming call by hanging up or silent it.
			if (ConfigAppValues.getMuteOrHangup())
				telephonyService.endCall();
			else
				putRingerModeSilent();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));					
		}							
	}
	
	/**
	 * This function performs the complete block to all incoming calls.
	 * 
	 * @param clientDDBB	
	 * @param incomingNumber
	 * @return
	 */
	public static BCBean blockAll (
			ClientDDBB clientDDBB, 
			String incomingNumber) {
		
		try {						
				
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Option 1 - Blocking all incoming calls");
			
			Contact usedContact = clientDDBB.getSelects().
				selectContactForPhoneNumber(incomingNumber);
			
			checkTelephonyService();
			
			muteOrHangupAction();
			
			return new BCBean(true, usedContact);
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));		
			return null;
		}				
	}
	
	/**
	 * This function performs block actions regarding to block only to specified
	 * blocked contacts.
	 * 
	 * @param clientDDBB
	 * @param incomingNumber
	 * @return
	 */
	public static BCBean blockBloquedContacts (
			ClientDDBB clientDDBB,			
			String incomingNumber) {
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Option 2 - Blocking only blocked contacts");
			
			Contact usedContact = clientDDBB.getSelects().
				selectBannedContactForPhoneNumber(incomingNumber);
			
			//If the contact is in the banned list
			if (usedContact != null) {								
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Contact: " + 
						usedContact.getContactName() + 
						"\t isBanned: " + usedContact.isBanned());	
						
				checkTelephonyService();											
				muteOrHangupAction();
					
				return new BCBean(true, usedContact);
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
	 * This function performs block action over unknown incoming numbers.
	 * 
	 * @param clientDDBB
	 * @param incomingNumber
	 * @return
	 */
	public static BCBean blockUnknown (
			ClientDDBB clientDDBB, 
			String incomingNumber) {
		
		try {
		
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Option 3 - Blocking unknwon contacts");
			
			Contact usedContact = clientDDBB.getSelects().
				selectContactForPhoneNumber(incomingNumber);
			
			if (usedContact == null) {
				
				checkTelephonyService();
				muteOrHangupAction();
				
				return new BCBean(true, usedContact);
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
	 * This function performs block action over unkwnown and predefined blocked contacts.
	 * @param clientDDBB 
	 * @param incomingNumber
	 * @return
	 */
	public static BCBean blockUnknownAndBlockedContacts (
			ClientDDBB clientDDBB,			
			String incomingNumber) { 
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Option 4 - Blocking unknwown and blocked contacts");
			
			Contact usedContact = clientDDBB.getSelects().
				selectContactForPhoneNumber(incomingNumber);
			
			if (usedContact == null || usedContact.isBanned()) {
				
				checkTelephonyService();
				muteOrHangupAction();
				return new BCBean(true, usedContact);
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
	 * This function does use of reflection process to get a ITelephony object
	 * that is implemented automatically by de compiler using the 
	 * com.android.internal.telephony.ITelephony.aidl interface.
	 * This aidl is the interface of the com.android.telephony.ITelephony
	 * whic is located by internal way inside the Android OS and can't use throw
	 * the standard and public SDK.
	 * 
	 *  This code is based in the post written by Prasanta Paul on his blog
	 *  and post: @link http://prasanta-paul.blogspot.com/2010/09/call-control-in-android.html
	 */
	private static ITelephony createITelephonyImp () {
		
		try {					
			
			TelephonyManager telephonyManager = (TelephonyManager)ConfigAppValues.getContext().
				getSystemService(Context.TELEPHONY_SERVICE);
			
			//Java reflection to gain access to TelephonyManager			
			QSLog.d(CLASS_NAME, "In startITelephony");
			
			Class c = Class.forName(telephonyManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			
			com.android.internal.telephony.ITelephony telephonyService = 
				(ITelephony)m.invoke(telephonyManager);
			
			return telephonyService;
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	
	/**
	 * Put the mobile in silence mode (audio and vibrate)
	 * 
	 */
	private static void putRingerModeSilent () {
		
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Poniendo el movil en modo silencio");
													
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}		
	}	
	
	/**
	 * Put the system into normal ring mode.
	 */
	private static void putRingerModeNormal () {
		
		try {
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Poniendo el movil en modo normal");
				
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
							
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);											
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}		
	}
	
	/**
	 * Put the vibrator in mode off
	 */
	private void vibrateOff () {
		
		try {
			
			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator)ConfigAppValues.getContext().
				getSystemService(vibratorService);
						
			vibrator.cancel();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}

}
