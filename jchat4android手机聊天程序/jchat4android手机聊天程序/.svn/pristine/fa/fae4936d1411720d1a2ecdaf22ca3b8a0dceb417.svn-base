/*****************************************************************
 jChat is a  chat application for Android based on JADE
  Copyright (C) 2008 Telecomitalia S.p.A. 
 
 GNU Lesser General Public License

 This is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this software; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

package it.telecomitalia.jchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Activity that allows a user to write an SMS message text and to send it to a contact.
 * SMS functionalities are provided by SMSManager class, and a custom IntentReceiver is used to receive 
 * all messages.
 * <p>
 * Please note that the emulator for now always report success after sending an SMS  
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0
 */

public class SendSMSActivity extends Activity {

	/**
	 * Text body of SMS
	 */
	private EditText smsBody;
	/**
	 * Action that shall be used for SMS correctly sent (It shall be catched by {@link SMSIntentReceiver})
	 */
	public static final String SMS_SENT_ACTION="com.tilab.msn.SMS_SENT";
	/**
	 * Action that shall be used for error in sending SMS (It shall be catched by {@link SMSIntentReceiver})
	 */
	public static final String SMS_ERROR_ACTION="com.tilab.msn.SMS_ERROR";
	/**
	 * Name of SMS address parameter
	 */
	public static final String SMS_ADDRESS_PARAM="SMS_ADDRESS_PARAM";
	
	/**
	 * Name of SMS delivery message parameter
	 */
	public static final String SMS_DELIVERY_MSG_PARAM="SMS_DELIVERY_MSG_PARAM";
	/**
	 * Name of SMS address list
	 */
	public static final String SMS_ADDRESS_LIST="SMS_ADDRESS_LIST";
	/**
	 * List of SMS receivers' addresses 
	 */
	private List<String> addresses;

	/**
	 * Instance of progress dialog shown after sending SMS
	 */
	private ProgressDialog dlg;


	/**
	 * Handler used to post delayed Toast (for simulating SMS sending)
	 */
	private Handler handler;
	/**
	 * Customized intent receiver for receiving SMS
	 */
	private BroadcastReceiver smsReceiver;
	

	
	/**
	 * Overrides Activity.onCreate() for the current activity,
	 * It basically prepares the gui and registers the customized intent receiver providing 
	 * our own IntentFilter.
	 * 
	 */
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.sms_dialog);
		handler = new Handler();
		
		smsReceiver = new SMSIntentReceiver(handler);
		IntentFilter myFilter = new IntentFilter();
		myFilter.addAction(SMS_SENT_ACTION);
		myFilter.addAction(SMS_ERROR_ACTION);
		
		registerReceiver(smsReceiver,myFilter );
		
		Intent dataIntent = getIntent();
	    addresses= (List<String>) dataIntent.getSerializableExtra(SMS_ADDRESS_LIST);
	    
		EditText title = (EditText) super.findViewById(R.id.addressEdt);
		title.setText(formatAdresses(addresses));
	
		smsBody = (EditText) super.findViewById(R.id.smsbodyEdt);
		ImageButton sendBtn = (ImageButton) super.findViewById(R.id.sendsmsBtn);
		
		sendBtn.setOnClickListener( new View.OnClickListener(){

			/**
			 * Handles sending of SMS, dividing the message in sub messages, sending them and showing a progress dialog 
			 */
			public void onClick(View arg0) {
				SmsManager smsMgr = SmsManager.getDefault();
				String smsText = smsBody.getText().toString();
				
				if (smsText.length() > 0){
					
					ImageButton btn = (ImageButton)arg0;
					btn.setEnabled(false);
					ArrayList<String> messages = smsMgr.divideMessage(smsText);
					StringBuffer buffer = new StringBuffer();
					
					if (messages.size() > 1) {
						buffer.append("SMS is too long! A total of ");
						buffer.append(messages.size());
						buffer.append(" messages will be sent!");
					}
					
					
					if (buffer.length() > 0){
						Toast.makeText(SendSMSActivity.this, buffer.toString(), 1000).show();
					}
					

					
					dlg = new ProgressDialog(SendSMSActivity.this);
					dlg.setIndeterminate(true);
					dlg.setCancelable(false);
					dlg.setMessage("Sending  " + messages.size() + " messages to "  +  SendSMSActivity.this.addresses.size() + " contacts...");
					
					SendSMSActivity.this.handler.postDelayed(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							dlg.show();
						}
						
					}, 2000);
					
					
					
					for (String address : SendSMSActivity.this.addresses) {
					
						ArrayList<PendingIntent> listOfIntents = new ArrayList<PendingIntent>();
						
						for (int i=0; i < messages.size(); i++){
							Intent sentIntent = new Intent(SendSMSActivity.SMS_SENT_ACTION);
							sentIntent.putExtra(SMS_ADDRESS_PARAM, address);
							sentIntent.putExtra(SMS_DELIVERY_MSG_PARAM, (messages.size() > 1)? "Part " +  i + " of SMS " : "SMS ");
							PendingIntent pi = PendingIntent.getBroadcast(SendSMSActivity.this, 0, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
							listOfIntents.add(pi);
						}
						
						smsMgr.sendMultipartTextMessage(address, null, messages, listOfIntents, null);
					}


					
					
				} else {
					Toast.makeText(SendSMSActivity.this,SendSMSActivity.this.getText(R.string.error_sms_empty) , 3000).show();
				}
				
			}
			
		});
	}
	

	
	/**
	 * Unregisters the customized SMS Intent receiver
	 */
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}
	


	/**
	 * Prepares a string chaining together all the phone numbers (SMS addresses) using ";" as a separator
	 * @param addresses for all contacts
	 * @return the string containing all phone numbers chained together
	 */
	private String formatAdresses(List<String> addresses){
		StringBuffer buffer = new StringBuffer();
		
		int numOfAddr = addresses.size();
		
		for (int i=0; i < numOfAddr-1; i++){
			buffer.append(addresses.get(i));
			buffer.append("; ");
		}
		
		buffer.append(addresses.get(numOfAddr-1));
		
		return buffer.toString();
	}


	/**
	 * Returns the progress dialog instance
	 * @return the progress dialog
	 */
	public  ProgressDialog getProgressDialog(){
		return dlg;
	}


}
