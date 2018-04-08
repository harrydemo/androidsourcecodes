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

package es.cesar.quitesleep.subactivities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.dialogs.WarningDialog;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.operations.MailOperations;
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
public class MailSettings extends Activity implements OnClickListener {
	
	private final String CLASS_NAME = getClass().getName();
	final private int WARNING_DIALOG = 0;
	
	//Widgets id's
	private final int userEditTextId = R.id.mailsettings_edittext_user;
	private final int passwdEditTextId = R.id.mailsettings_edittext_passwd;
	private final int subjectEditTextId = R.id.mailsettings_edittext_subject;
	private final int bodyEditTextId = R.id.mailsettings_edittext_body;
	private final int saveMailButtonId = R.id.mailsettings_button_savemail;	
	private final int mailServiceToggleButtonId = R.id.mailsettings_togglebutton_mailservice;	
	
	//Widgets
	private EditText userEditText;
	private EditText passwdEditText;
	private EditText subjectEditText;
	private EditText bodyEditText;
	private Button saveMailButton;
	private ToggleButton mailServiceToggleButton;	
	private WarningDialog warningDialog;
	
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		try {
					
			super.onCreate(savedInstanceState);
			
			createAppBar();
			
			setContentView(R.layout.mailsettings);
			
			//Set the widgets elements
			userEditText = (EditText)findViewById(userEditTextId);
			passwdEditText = (EditText)findViewById(passwdEditTextId);
			subjectEditText = (EditText)findViewById(subjectEditTextId);
			bodyEditText = (EditText)findViewById(bodyEditTextId);
			saveMailButton = (Button)findViewById(saveMailButtonId);
			mailServiceToggleButton = (ToggleButton)findViewById(mailServiceToggleButtonId);			
			
			//Set OnClickListener events
			saveMailButton.setOnClickListener(this);	
			mailServiceToggleButton.setOnClickListener(this);
		
			warningDialog = new WarningDialog(
					this, 
					ConfigAppValues.WARNING_MAIL_ACTION);
			
			//Put in the widgets the prevoious data saved into ddbb.
			getDefaultValues();
		
		}catch(Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Create the Application Title Bar in top of the activity
	 * 
	 * @throws Exception
	 */
	private void createAppBar () throws Exception {
		
		try {
			
			//Put an app icon to the upper left of the screen
			Window window = getWindow();
			window.requestFeature(Window.FEATURE_LEFT_ICON);
			setContentView(R.layout.mailsettings);
			window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.quitesleep);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
		}
	}
	
	
	@Override
	public void onClick (View view) {
		
		int viewId = view.getId();					
		
		switch (viewId) {
			case saveMailButtonId:				
				prepareSaveMailOperation();
				break;			
			
			case mailServiceToggleButtonId:
				if (mailServiceToggleButton.isChecked())
					showDialog(WARNING_DIALOG);
				else
					DialogOperations.checkMailService(
							this,
							mailServiceToggleButton.isChecked());
				break;						
				
			default:
				break;
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
			case WARNING_DIALOG:
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Create the WarningDialog for 1st time");
				dialog = warningDialog.getAlertDialog();				
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	/**
	 * This function prepare the dalogs every time to call for some of this
	 * 
	 *  @param int
	 *  @param dialog
	 */
	@Override
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
		
		try {
			
			switch (idDialog) {
				case WARNING_DIALOG:
					warningDialog.setContext(this);	
					warningDialog.setToggleButtonIsChecked(mailServiceToggleButton.isChecked());
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
	 * Function that put in the widgets the data saved into ddbb.
	 */
	private void getDefaultValues () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null) {
				
				if (settings.getUser() != null && !settings.getUser().equals(""))
					userEditText.setText(settings.getUser());
				if (settings.getPasswd() != null && !settings.getPasswd().equals(""))
					passwdEditText.setText(settings.getPasswd());
				if (settings.getSubject() != null && !settings.getSubject().equals(""))
					subjectEditText.setText(settings.getSubject());
				if (settings.getBody() != null && !settings.getBody().equals(""))
					bodyEditText.setText(settings.getBody());
				
				mailServiceToggleButton.setChecked(settings.isMailService());
			} 
			//If Settings object haven't been created previously, here we create.
			else {
				settings = new Settings(false);
				
				/* Save the mail settings, only the subject and the body if the
				 * Settings object haven't been created for have something for
				 * default attributes, user and passwd not because is important
				 * for the mail send, and the user soon or later will have set this.
				 */
				settings.setSubject(subjectEditText.getText().toString());
				settings.setBody(bodyEditText.getText().toString());
								
				clientDDBB.getInserts().insertSettings(settings);
				clientDDBB.commit();
			}
			
			clientDDBB.close();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Prepare all data from widgets for call to the function that save data
	 * into ddbb in Settings object
	 *
	 */
	private void prepareSaveMailOperation () {
		
		try {
			
			String user 		= userEditText.getText().toString();
			String passwd 		= passwdEditText.getText().toString();
			String subject 		= subjectEditText.getText().toString();
			String body 		= bodyEditText.getText().toString();
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "user: " + user);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "passwd: " + passwd);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "subject: " + subject);
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "body: " + body);
						
			if (MailOperations.saveMailSettings(user, passwd, subject, body))
				//All right, start the service was ok!
				if (QSToast.RELEASE) QSToast.r(
                		this,
                		this.getString(
                				R.string.mailsettings_toast_save),
                		Toast.LENGTH_SHORT);
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	

}
