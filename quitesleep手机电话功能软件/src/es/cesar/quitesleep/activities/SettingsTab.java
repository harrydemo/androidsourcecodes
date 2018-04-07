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

package es.cesar.quitesleep.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.MuteOrHangUp;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.interfaces.IDialogs;
import es.cesar.quitesleep.notifications.QuiteSleepNotification;
import es.cesar.quitesleep.operations.StartStopServicesOperations;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.subactivities.BlockCallsConfScreen;
import es.cesar.quitesleep.subactivities.MailSettings;
import es.cesar.quitesleep.subactivities.SmsSettings;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@mgail.com
 *
 */
public class SettingsTab extends Activity implements OnClickListener, IDialogs {
	
	private final String CLASS_NAME = getClass().getName();
	
	//IDs for dialogs
	final int ABOUT_DIALOG		= 1;
	final int HELP_DIALOG		= 2;
	
	//Ids for the button widgets	
	final int mailButtonId = R.id.settings_button_mail;
	final int smsButtonId = R.id.settings_button_sms;
	final int blockOtherCallsId = R.id.settings_button_blockCallsConfiguration;
	final int muteRButtonId = R.id.settings_radiobutton_mute;
	final int hangUpRButtonId = R.id.settings_radiobutton_hangup;
	final int serviceToggleButtonId = R.id.settings_togglebutton_service;	
	
	//Ids for option menu
	final int aboutMenuId = R.id.menu_information_about;
	final int helpMenuId = R.id.menu_information_help;
	
	//Activity buttons
	private Button mailButton;
	private Button smsButton; 
	private Button blockOtherCalls;
	private RadioButton muteRButton;
	private RadioButton hangUpRButton;
	private ToggleButton serviceToggleButton;
	
	//Notification
	//private NotificationManager notificationManager;
	//final private int notificationId = R.layout.quitesleep_notification;
	
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingstab);

		//All Activity buttons
		mailButton = (Button)findViewById(mailButtonId);
		smsButton = (Button)findViewById(smsButtonId);
		blockOtherCalls = (Button)findViewById(blockOtherCallsId);
		muteRButton = (RadioButton)findViewById(muteRButtonId);
		hangUpRButton = (RadioButton)findViewById(hangUpRButtonId);
		serviceToggleButton = (ToggleButton)findViewById(serviceToggleButtonId);
		
			
		//Define all button listeners
		mailButton.setOnClickListener(this);
		smsButton.setOnClickListener(this);
		blockOtherCalls.setOnClickListener(this);
		muteRButton.setOnClickListener(this);
		hangUpRButton.setOnClickListener(this);
		serviceToggleButton.setOnClickListener(this);
		
		//Get the notification manager service
		//notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		/* Set the previous saved state in the ddbb in the widgets that need
		 * the saved state, such as toggle button.
		 */
		initActivity();
	}
	
	@Override
	public void onResume () {
		
		try {
			
			QSLog.d(CLASS_NAME, "En onResume");
			super.onResume();
			initActivity();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	/**
	 * Listener for all buttons in this Activity
	 */
	public void onClick (View view) {
		
		int viewId = view.getId();
				
		switch (viewId) {
		
			case mailButtonId:
				Intent intentMailSettings = new Intent(this, MailSettings.class);
				startActivityForResult(intentMailSettings, ConfigAppValues.REQCODE_MAIL_SETTINGS);
				break;
				
			case smsButtonId:
				Intent intentSmsSettings = new Intent(this, SmsSettings.class);
				startActivityForResult(intentSmsSettings, ConfigAppValues.REQCODE_SMS_SETTINGS);
				break;
				
			case muteRButtonId:
				/* false (although the mute option is check to true) 
				 * indicates that the user has selected mute option
				 */
				saveMuteOrHangUpOption(false);
				break;
				
			case hangUpRButtonId:
				// true indicates that the user has selected hang up option						
				saveMuteOrHangUpOption(true);
				break;
								
			case blockOtherCallsId:
				Intent intentBlockOtherCalls = new Intent(this, BlockCallsConfScreen.class);
				startActivityForResult(intentBlockOtherCalls, ConfigAppValues.REQCODE_BLOCK_OTHER_CALLS);
				break;
				
			case serviceToggleButtonId:
				startStopServiceProcess();					
				QuiteSleepNotification.showNotification(
						this, 
						serviceToggleButton.isChecked());			
				break;
				
			default:
				break;
		}						
	}
	
	/**
	 * This function save the user option regarding both mute or hang up in the ddbb.
	 * 
	 * @param optionValue
	 */
	private void saveMuteOrHangUpOption (boolean optionValue) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			MuteOrHangUp muteOrHangup = clientDDBB.getSelects().selectMuteOrHangUp();
			if (muteOrHangup != null) {
				
				//If is true, then the hangup option is established
				if (optionValue) {
					muteOrHangup.setHangUp(true);
					ConfigAppValues.setMuteOrHangup(true);
				}
				//If is false, then the mute mode is set to true
				else {
					muteOrHangup.setMute(true);
					ConfigAppValues.setMuteOrHangup(false);
				}
									
				clientDDBB.getUpdates().insertMuteOrHangUp(muteOrHangup);
				clientDDBB.commit();	
			} 
			
			//If MuteOrHangUp object is not created.
			else {
				muteOrHangup = new MuteOrHangUp();
				clientDDBB.getInserts().insertMuteOrHangUp(muteOrHangup);
				clientDDBB.commit();
			}						
			clientDDBB.close();
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));			
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		try {							
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.informationmenu, menu);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}		
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		try {
			
			switch (item.getItemId()) {
				case aboutMenuId:
					showDialog(ABOUT_DIALOG);
					break;
				case helpMenuId:
					showDialog(HELP_DIALOG);
					break;
				default:
					break;
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
	 * Create the activity dialogs used for it
	 * 
	 * @param id
	 * @return the dialog for the option specified
	 * @see Dialog
	 */
	@Override
	protected Dialog onCreateDialog (int id) {
		
		Dialog dialog;
		
		switch (id) {			
			case ABOUT_DIALOG:
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Create about dialog for 1st time");
				dialog = showWebviewDialog(IDialogs.ABOUT_URI);
				break;	
			case HELP_DIALOG:
				dialog = showWebviewDialog(IDialogs.HELP_SETTINGS_URI);
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	
	/**
	 * Create the webview dialog using the file (uri) specified to show the information.
	 * 
	 * @return
	 */
	public Dialog showWebviewDialog(String uri) {
		
		try {
			  View contentView = getLayoutInflater().inflate(R.layout.webview_dialog, null, false);
              WebView webView = (WebView) contentView.findViewById(R.id.webview_content);
              webView.getSettings().setJavaScriptEnabled(true);              
              
              webView.loadUrl(uri);

              return new AlertDialog.Builder(this)
                  .setCustomTitle(null)
                  .setPositiveButton(android.R.string.ok, null)
                  .setView(contentView)
                  .create();
              
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Put the ddbb saved data in the activity widgets 
	 */
	private void initActivity () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			
			//Togglebutton (QuuiteSleep service) check
			Settings settings = clientDDBB.getSelects().selectSettings();
			if (settings != null) {
				serviceToggleButton.setChecked(settings.isQuiteSleepServiceState());
			}else
				serviceToggleButton.setChecked(false);
						
			//Mute or hangup radio buttons check
			if (ConfigAppValues.getMuteOrHangup() == null) {
				MuteOrHangUp muteOrHangup = clientDDBB.getSelects().selectMuteOrHangUp();
				if (muteOrHangup != null) {
					if (muteOrHangup.isMute())
						muteRButton.setChecked(true);
					else if (muteOrHangup.isHangUp())
						hangUpRButton.setChecked(true);										
				}
				//If MuteOrHangUp object is not created. Should not occur
				else {
					muteOrHangup = new MuteOrHangUp();
					clientDDBB.getInserts().insertMuteOrHangUp(muteOrHangup);
					clientDDBB.commit();
				}			
				
			}else {
				if (ConfigAppValues.getMuteOrHangup())
					hangUpRButton.setChecked(true);
				else
					muteRButton.setChecked(true);
			}
			clientDDBB.close();						
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
				
	
	
	/**
	 * Function that depends the serviceToggleButton state try to start the
	 * QuiteSleep incoming call service or stop it.
	 */
	private void startStopServiceProcess () {
		
		try {
			
			boolean result = StartStopServicesOperations.startStopQuiteSleepService(
					serviceToggleButton.isChecked());
			
			if (serviceToggleButton.isChecked()) {			
				
				/* Deactivate the notification toast because now use the
				 * status bar notification 
				 */
				/*
				if (result)
				
					//All right, start the service was ok!
					Toast.makeText(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_start_service),
	                		Toast.LENGTH_SHORT).show();
	                
				else
					//An error has ocurred!!
					Toast.makeText(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_fail_service),
	                		Toast.LENGTH_SHORT).show();			
	           */								
			}else {																												
				if (result)
					//All right, stop the service was ok!
					if (QSToast.RELEASE) QSToast.r(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_stop_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					if (QSToast.RELEASE) QSToast.r(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}							
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	
	/*
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);				
	}
	*/
	
	

}

