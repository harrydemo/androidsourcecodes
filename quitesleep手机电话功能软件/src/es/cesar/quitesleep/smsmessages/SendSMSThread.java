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

package es.cesar.quitesleep.smsmessages;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import es.cesar.quitesleep.ddbb.CallLog;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Phone;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author 	Cesar Valiente Gordo
 * @mail 	cesar.valiente@gmail.com
 * 
 * Class that implements Java Thread for send sms to the incoming caller
 *
 */
public class SendSMSThread extends Thread {
	
	private final String CLASS_NAME = getClass().getName();
	
	private String smsText;
	private String incomingCallNumber;		
	private CallLog callLog;
	
	//--------------		Getters & Setters		--------------------------//
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	
	public String getIncomingCallNumber() {
		return incomingCallNumber;
	}
	public void setIncomingCallNumber(String incomingCallNumber) {
		this.incomingCallNumber = incomingCallNumber;
	}
	
	public CallLog getCallLog() {
		return callLog;
	}
	public void setCallLog(CallLog callLog) {
		this.callLog = callLog;
	}
	//------------------------------------------------------------------------//
	
	
	
	/**
	 * Constructor with the phonenumber of the receiver sms
	 */
	public SendSMSThread (String incomingCallNumber, CallLog callLog) {
		init(incomingCallNumber, callLog);
	}
	

	/**
	 * Function that is called for the constructor
	 * @param incomingCallNumber
	 */
	public void init (String incomingCallNumber, CallLog callLog) {
		
		this.incomingCallNumber = incomingCallNumber;
		this.callLog = callLog;
		getAllData();
	}
			
	
	/**
	 * Get all data of SMS settings
	 */
	private void getAllData () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null)  				
				smsText = settings.getSmsText();
										
			clientDDBB.close();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Check if the receiver phone number have permission to use for send SMS
	 * message
	 * 
	 * @return		true or false if the phone number (receiver) can or not receive
	 * messages
	 */
	private  boolean checkSendPhoneNumber () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Phone phone = clientDDBB.getSelects().selectPhoneForPhoneNumber(incomingCallNumber);
			clientDDBB.close();
			
			if (phone != null) 			
				return phone.isUsedToSend();
			else
				return false;										
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
		
	@Override
	public void run () {
		sendSms();
	}
	
	
	/**
	 * Send one SMS message to the receiver
	 * 
	 */
	public void sendSms () {
		
		try {			
			if (checkSendPhoneNumber()) {
				
				
				final String SENT_SMS_ACTION 			= 	"SENT_SMS_ACTION";
				final String DELIVERED_SMS_ACTION 		= 	"DELIVERED_SMS_ACTION";
				
				//Create the setIntent parameter
				Intent sentIntent = new Intent(SENT_SMS_ACTION);
				PendingIntent sentPI = PendingIntent.getBroadcast(
						ConfigAppValues.getContext(),
						0,
						sentIntent,
						0);
				
				//Create the deliveryIntetn parameter
				Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
				PendingIntent deliverPI = PendingIntent.getBroadcast(
						ConfigAppValues.getContext(),
						0,
						deliveryIntent,
						0);							
				
				SmsManager smsManager = SmsManager.getDefault();																									
								
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "SmsText: " + smsText);
								
				/* In Nexus One there is a bug (how in htc tatoo) that sent sms
				 * using sendTextMessage not found, so i try to send sms by 
				 * cut into parts (if its necessary) and send using sendMultipartMessage
				 */
				ArrayList<String> multipartSmsText = smsManager.divideMessage(smsText);
				int multipartSize = multipartSmsText.size();
				
				//Create the arraylist PendingIntents for use it.
				ArrayList<PendingIntent> sentPiList = 
					new ArrayList<PendingIntent>(multipartSize);
				ArrayList<PendingIntent> deliverPiList = 
					new ArrayList<PendingIntent>(multipartSize);
				
				for (int i=0; i<multipartSize; i++) {
					sentPiList.add(sentPI);
					deliverPiList.add(deliverPI);
				}
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "MultipartSize: " + multipartSize);
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "MultpartMessage: " + multipartSmsText);
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Send sms to: " + incomingCallNumber);								
				
				String operator = getOperator();
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Operator: " + operator);
				
				smsManager.sendMultipartTextMessage(
						incomingCallNumber, 
						operator, 
						multipartSmsText, 
						sentPiList, 
						deliverPiList);		
				
				/* In the smssettings.xml layout i specified only 160 character,
				 * one message, so if the send have been done, setSendSms is true				 
				 */
				if (multipartSize > 0) {
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "MEssage send!!!");
					callLog.setSendSms(true);
				}
					
			}			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	private String getOperator () {
		
		try {
			final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
			final String REPLY_PATH_PRESENT = "reply_path_present";
			final String SERVICE_CENTER = "service_center";
			final int COLUMN_REPLY_PATH_PRESENT = 0;
			final int COLUMN_SERVICE_CENTER = 1;
			
			final String[] SERVICE_CENTER_PROJECTION =
			    new String[] {REPLY_PATH_PRESENT, SERVICE_CENTER,};
			
			Cursor cursor =
		        ConfigAppValues.getContext().getContentResolver().query(SMS_CONTENT_URI,
		            SERVICE_CENTER_PROJECTION, "thread_id = " + 0, null, "date DESC");

		      // cursor = SqliteWrapper.query(mContext, mContext.getContentResolver(),
		      // Sms.CONTENT_URI, SERVICE_CENTER_PROJECTION,
		      // "thread_id = " + threadId, null, "date DESC");

		      if ((cursor == null) || !cursor.moveToFirst()) {
		        return null;
		      }

		      boolean replyPathPresent = (1 == cursor.getInt(COLUMN_REPLY_PATH_PRESENT));
		      
		      if (cursor != null) 
			        cursor.close();		      		      
		      return replyPathPresent ? cursor.getString(COLUMN_SERVICE_CENTER) : null;
		      		     		    		      		
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	
	
	

}
