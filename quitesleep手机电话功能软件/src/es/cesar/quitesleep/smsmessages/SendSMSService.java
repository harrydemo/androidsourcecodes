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
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
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
 * Class that implements Android Service for send sms to the incoming caller
 *
 */
public class SendSMSService extends Service {
	
	private final String CLASS_NAME = getClass().getName();
	
	private String smsText;
	private String incomingCallNumber;		
	
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
	//------------------------------------------------------------------------//
		
	//------------		Inherited method re-implement	----------------------//
	
	@Override
	public void onCreate () {
				
	}
	
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		
		String incomingCallNumber = 
			intent.getExtras().getString(ConfigAppValues.INCOMING_CALL_NUMBER);
		SendSMSService sendSMS = new SendSMSService(incomingCallNumber);
		sendSMS.sendSms();
		
		return Service.START_STICKY;
	}
	//------------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters
	 */
	public SendSMSService () {
		
	}
	
	/**
	 * Constructor with the phonenumber of the receiver sms
	 */
	public SendSMSService (String incomingCallNumber) {
		init(incomingCallNumber);
	}
	

	/**
	 * Function that is called for onStartCommand Service method start
	 * @param incomingCallNumber
	 */
	public void init (String incomingCallNumber) {
		
		this.incomingCallNumber = incomingCallNumber;	
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
		
	
	/**
	 * Send one SMS message to the receiver
	 * 	 
	 */
	private void sendSms () {
		
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
				int smsSize = multipartSmsText.size();
				
				//Create the arraylist PendingIntents for use it.
				ArrayList<PendingIntent> sentPiList = 
					new ArrayList<PendingIntent>(smsSize);
				ArrayList<PendingIntent> deliverPiList = 
					new ArrayList<PendingIntent>(smsSize);
				
				for (int i=0; i<smsSize; i++) {
					sentPiList.add(sentPI);
					deliverPiList.add(deliverPI);
				}
				
				//Try to send the sms message
				smsManager.sendMultipartTextMessage(
						incomingCallNumber, 
						null, 
						multipartSmsText, 
						sentPiList, 
						deliverPiList);								
			}			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	
	
	
	

}
