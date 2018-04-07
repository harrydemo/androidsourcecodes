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

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import es.cesar.quitesleep.beans.BCBean;
import es.cesar.quitesleep.ddbb.BlockCallsConf;
import es.cesar.quitesleep.ddbb.CallLog;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.mailmessages.SendMail;
import es.cesar.quitesleep.smsmessages.SendSMSThread;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.TokenizerUtils;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class IncomingCallOperations extends Thread {
	
	private static final String CLASS_NAME = "es.cesar.quitesleep.utils.operations.IncomingCallOperations"; 	
	
	private String incomingCallNumber;
	
	//----------------	Getters & Setters	----------------------------------//
	public String getIncomingCallNumber() {
		return incomingCallNumber;
	}

	public void setIncomingCallNumber(String incomingCallNumber) {
		this.incomingCallNumber = incomingCallNumber;
	}
	//------------------------------------------------------------------------//		
	
	/**
	 * Constructor
	 * @param incomingCallNumber
	 */
	public IncomingCallOperations(String incomingCallNumber) {
		
		this.incomingCallNumber = incomingCallNumber;		
	}
		

	@Override
	public void run () {		
		silentIncomingCall();		
	}
	
	/**
	 * This function is encharged to check if the incoming call is between interval
	 * and if it, then call to subfuctions to performs the block action and send
	 * sms and/or mail if is appropriate.
	 * 
	 */
	public synchronized void silentIncomingCall () {
		
		try {								
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			QSLog.d(CLASS_NAME, "IncomingNuber: " + incomingCallNumber);			
						
			String phoneNumberWhithoutDashes = 
				TokenizerUtils.tokenizerPhoneNumber(incomingCallNumber, null);
			
			QSLog.d(CLASS_NAME, "IncomingNumberFormated: " + phoneNumberWhithoutDashes);
				
			//create the CallLog object for log calls.
			CallLog callLog = new CallLog();
			
			//check if the call is in the interval time
			boolean isInInterval = checkSchedule(callLog, clientDDBB);
			
			if (isInInterval) {
		
				//Put the mobile phone in silent mode (sound+vibration)
				//putRingerModeSilent();
				
				//End call using the ITelephony implementation					
				//telephonyService.endCall();
				
				BCBean bcBean = processBlockedActionType(
						clientDDBB, 					
						phoneNumberWhithoutDashes);
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "BloquedContactBean: " + bcBean);
				
				
				/* Check if the mail service is running, if it is true
				 * create a SendMail object for try to send one or more 
				 * email to the contact with the incoming number
				 */
				if (bcBean != null)
					sendMail(incomingCallNumber, callLog, clientDDBB);
				
				/* Check if the sms service is running, if it is true
				 * create a SendSMS object for try to send a SMS to 
				 * the contact with the incoming number
				 */
				if (bcBean != null)
					sendSMS(incomingCallNumber, callLog, clientDDBB);	
				
				if (bcBean != null)
					//Save the callLog object in the ddbb if is appropriate.
					saveCallLog(
						clientDDBB, 
						callLog, 
						incomingCallNumber, 
						bcBean.getUsedContact());
			}
			//If the call isn't in the interval range
			else 
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "No está en el intervalo");
			
			//close the ddbb.
			clientDDBB.close();
																											
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));				
		}
	}
	
	
	/**
	 * This function call to subfuctions that performs different block actions over
	 * incoming calls.
	 * 
	 * @param clientDDBB
	 * @param incomingNumber
	 * @return
	 */
	private BCBean processBlockedActionType (
			ClientDDBB clientDDBB, 			
			String incomingNumber) {
		
		try {						
			
			BCBean bcBean = null;			
			
			if (ConfigAppValues.getBlockCallsConf() == null) {	
			
				BlockCallsConf blockCallsConf = clientDDBB.getSelects().selectBlockCallConf();
				if (blockCallsConf != null) 
					ConfigAppValues.setBlockCallsConf(blockCallsConf);									
			}							
			
			 switch (ConfigAppValues.getBlockCallsConf().whatIsInUse()) {
			 	case 0:
			 		//null
			 		break;
			 	case 1:
			 		//BLOCK_ALL
			 		bcBean = BlockTypes.blockAll(
			 				clientDDBB, 
			 				incomingNumber);
			 		break;
			 	case 2:
			 		//BLOCK_BLOCKED_CONTACTS
			 		bcBean = BlockTypes.blockBloquedContacts(
			 				clientDDBB,  
			 				incomingNumber);				 		
			 		break;				 	
			 	case 3:
			 		//BLOCK_UNKNOWN				 		
			 		bcBean = BlockTypes.blockUnknown(
			 				clientDDBB, 
			 				incomingNumber);				 		
			 		break;
			 	case 4:
			 		//BLOCK_UNKNOWN_AND_BLOCKED_CONTACTS				 		
			 		bcBean = BlockTypes.blockUnknownAndBlockedContacts(
			 				clientDDBB, 				 				
			 				incomingNumber);				 		
			 		break;				 					
			 }				 				 			
			return bcBean;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));	
			return null;
		}		
	}	
	
	
	/**
	 * Compare the present time with the schedule start and end times (interval)
	 * 
	 * @return			true if the present time is in the interval or false
	 * 					if isn't
	 * @see				boolean
	 */
	private boolean checkSchedule (CallLog callLog, ClientDDBB clientDDBB) {
		
		try {						
			
			//ClientDDBB clientDDBB = new ClientDDBB();
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			//clientDDBB.close();
			
			if (schedule != null && CheckSettingsOperations.checkDayWeek(schedule)) {
				
				return isInInterval(callLog, schedule);								
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
	 * Function that check if the call incoming with the now time, is between the 
	 * twho hours specified as start and end of the interval specified by the user.
	 * 
	 * @param 			callLog
	 * @param 			schedule
	 * @return			true or false if the incoming call with the actual hour
	 * 					is in an interval delimit by the start and end hours
	 * @see				boolean
	 */
	private boolean isInInterval (CallLog callLog, Schedule schedule) {
		
		try {
			DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);					
			Calendar dateAndTime = Calendar.getInstance();
			
			String timeNow = timeFormat.format(dateAndTime.getTime());
			String timeStart = schedule.getStartFormatTime();
			String timeEnd = schedule.getEndFormatTime();
													
			String timeNowComplete = getCompleteDate(dateAndTime);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "time now: " + timeNowComplete);
			
			callLog.setTimeCall(timeNowComplete);
			
			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
			Date start = parser.parse(timeStart);
			Date end = parser.parse(timeEnd);
			Date now = parser.parse(timeNow);
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "start: " + start + "\ttimeStart: " + timeStart);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "end: " + end + "\ttimeEnd: " + timeEnd);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "now: " + now + "\ttimeNow: " + timeNow);
			
			
			String dayCompleteString = "24:00";
			Date  dayComplete = parser.parse(dayCompleteString);
			String dayInitString = "00:00";
			Date  dayInit = parser.parse(dayInitString);
			
			
			final int INCREASE = 24;
			boolean isInInterval;
			
			//If both times are equals (24h)		(si 8:00 = 8:00)
			if (start.compareTo(end) == 0)
				isInInterval = true;					
			
			//(Si start=10:00 end=21:00))
			//If end time is after than start time (ie: start=10:00 end=21:00)
			else if (end.after(start) && (now.after(start) && now.before(end)))
				isInInterval = true;
			
			/* If end time is before than start time (ie: start=22:00 end=3:00)
			 * then, we must be add 24 to the end time. (so ie: start=22:00 end:27:00)
			 */					
			
			//(Si start=22:00 end=3:00 ==> newEnd=3:00+24=27:00)
			else if (end.before(start)) {
				String newEndTimeString = TokenizerUtils.addIncreaseDate(
						timeEnd, 
						INCREASE, 
						null);						
				Date newEndTime = parser.parse(newEndTimeString);											
				
				/* (Si start=22:00 now=23:00 dayComplete=24:00, antes 
				 * hemos comprobado que end<start==> end: 3:00)
				 */
				if (now.after(start) && now.before(dayComplete))
					isInInterval = true;
				
				/* (Si now>00:00==> now=2:00 now<newEnd(27:00) ==> now=2:00+24=26:00)
				 * así que ahora queda start=23:00 end=27:00 y now=26:00 
				 */
				else if (now.after(dayInit) && now.before(newEndTime)) {
					String newNowTimeString = TokenizerUtils.addIncreaseDate(
							timeNow,
							INCREASE,
							null);
					Date newNowTime = parser.parse(newNowTimeString);
					
					if (newNowTime.after(start) && newNowTime.before(newEndTime))
						isInInterval = true;
					else
						isInInterval = false;
				}else
					isInInterval = false;						
			}else
				isInInterval = false;
																		
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Incoming call in the interval?: " + isInInterval);
			
			return isInInterval;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Get the compelte format date in English mode
	 * 
	 * @param 			now
	 * @return			the complete format date
	 * @see				String
	 */
	private String getCompleteDate (Calendar now) {
		
		try {
			
			return (now.get(Calendar.MONTH) + 1) + "-"
			        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR) + " "
			        + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":"
			        + now.get(Calendar.SECOND);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
				
	
	
	/**
	 * Send mail to the contact caller
	 * @param incomingNumber
	 * @param callLog
	 * @param clientDDBB
	 */
	private void sendMail (String incomingNumber, CallLog callLog, ClientDDBB clientDDBB) {
		
		try {			
			if (CheckSettingsOperations.checkMailService(clientDDBB)) {
							
				//------------------------------------------------------------//
				
				//Send mail using Threads
				SendMail sendMail = new SendMail(incomingNumber, callLog);				
				Thread threadMail = new Thread(sendMail);
				threadMail.start();
				threadMail.join();
								
				//------------------------------------------------------------//
				
				//------------------------------------------------------------//
				//Send mail without use Threads
				/*
				SendMail sendMail = new SendMail(incomingNumber, callLog);
				sendMail.sendMail();
				*/				
			}	
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Send SMS message to the contact caller
	 * 
	 * @param incomingNumber
	 * @param callLog
	 * @param clientDDBB
	 */
	private void sendSMS (String incomingNumber, CallLog callLog, ClientDDBB clientDDBB) {
		
		try {
			
			if (CheckSettingsOperations.checkSmsService(clientDDBB)) {
					
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "antes de enviar el sms");				
				
				//--  If u choose use Android Service for send SMS use this --//
				/*
				ConfigAppValues.getContext().startService(
						new Intent(
								ConfigAppValues.getContext(),
								SendSMSService.class).putExtra(
									ConfigAppValues.RECEIVER, 
									incomingNumber));
				*/
				//------------------------------------------------------------//
				
				//-----	  If u choose Java Thread for send SMS use this  -----//				
				SendSMSThread sendSMS = new SendSMSThread(incomingNumber, callLog);
				sendSMS.start();
				sendSMS.join();
				//------------------------------------------------------------//
				
				//------------------------------------------------------------//
				//Use without using threads
				/*
				SendSMSThread sendSMS = new SendSMSThread(incomingNumber);
				sendSMS.sendSms();
				*/
				//------------------------------------------------------------//
				
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	

	private void saveCallLog (
			ClientDDBB clientDDBB, 
			CallLog callLog, 
			String incomingNumber,
			Contact contactBanned) {
		
		try {
			
			//Set the parameters and save it
			callLog.setPhoneNumber(incomingNumber);
			callLog.setContact(contactBanned);
			
			//get the nomOrder for the new CallLog
			int numOrder = clientDDBB.getSelects().countCallLog();				
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "CallLog numOrder: " + numOrder);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "usedContact: " + contactBanned);
			
			callLog.setNumOrder(numOrder+1);
			
			clientDDBB.getInserts().insertCallLog(callLog);
			clientDDBB.commit();
			
			//we close clientDDBB in the caller function (top of the class)
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	
}
