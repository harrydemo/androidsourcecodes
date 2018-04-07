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


import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.BlockCallsConf;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.MuteOrHangUp;
import es.cesar.quitesleep.dialogs.SyncContactsDialog;
import es.cesar.quitesleep.dialogs.WarningDialog;
import es.cesar.quitesleep.interfaces.IDialogs;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.syncData.SyncContactsNew;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * @author		Cesar Valiente Gordo
 * @mail		cesar.valiente@gmail.com	
 * 
 * @version 1.0, 02-21-2010
 * 
 * Main class for start QuiteSleep App, this class implement the tabs wigets
 * to show them.
 * 
 */
public class Main extends TabActivity {
	
	final String CLASS_NAME = getClass().getName();
	
	final private int FIRST_TIME_DIALOG 	=	1;
	
	
	private WarningDialog firstTimeDialog;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		try {						
			
			//Set the min api level for the application, used for choice differents
			//api methods for differents api levels.
			ConfigAppValues.setMinApiLevel(getApplicationInfo().targetSdkVersion);
			
			//Set the app context for get the ddbb file and other file access
			if (ConfigAppValues.getContext() == null)
				ConfigAppValues.setContext(this);
						
			if (QSLog.DEBUG_I)QSLog.i(CLASS_NAME, "SDK Version: " + 
					String.valueOf(ConfigAppValues.getMinApiLevel()));					
			
			super.onCreate(savedInstanceState);
			
			//Create the app top bar
			createAppBar();
			
			//If is the first time QuiteSleep is running, then performs sync operations.
			if (isTheFirstTime()) {
				//We instantiate firstTimeDialog 
				firstTimeDialog = new WarningDialog(this, ConfigAppValues.WARNING_FIRST_TIME);
						
				showDialog(FIRST_TIME_DIALOG);
			}							
			
			//Resource object to get Drawables
			Resources resources = getResources();
			//The activity tabHost
			final TabHost tabHost = getTabHost();
			
			//Reusable TabSpec for each tab
			final TabHost.TabSpec tabSpec = null;
			
			//Reusable intent for each tab
			final Intent intent = null;				
			
		
			//------------		Contacts Tab 	------------------------------//
			createContactsTab(intent, tabSpec, tabHost, resources);
			
			//------------		Schedule Tab	--------------------------//
			createScheduleTab(intent, tabSpec, tabHost, resources);						
			
			//------------		Settings Tab	--------------------------//
			createSettingsTab(intent, tabSpec, tabHost, resources);
			
			//-------------		Logs Tab		-----------------------------//
			createLogsTab(intent, tabSpec, tabHost, resources);
			
			//Set the init (main) tab
			tabHost.setCurrentTab(0);		
			
		}catch (Exception e) {			
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
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
			setContentView(R.layout.group_tabs);
			window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.quitesleep);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
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
			case FIRST_TIME_DIALOG:
				dialog = firstTimeDialog.getAlertDialog();
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	/**
	 * This function prepares the dialog with the passed parameters.
	 */
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
		
		try {		
			switch (idDialog) {
				case FIRST_TIME_DIALOG:
					firstTimeDialog.setContext(this);					
					firstTimeDialog.setHandler(handler);										
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
	 * This funcion check if the db4o database is full contacts empty, so indicate
	 * that is the first time too run the application.
	 * 
	 * @return				True or false if the db4o is contact empty or not
	 * @see					boolean
	 * @throws 				Exception
	 */
	private boolean isTheFirstTime () throws Exception{
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
		
			int numContacts = clientDDBB.getSelects().getNumberOfContacts();
			clientDDBB.close();
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "NumContacts: " + numContacts);
			
			if (numContacts == 0)
				return true;
			else 
				return false;
								
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
		}
	}	
	
	/**
	 * Create the Contacts Tab
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createContactsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {

		try {
		
			//---------------		Contacts Tab		----------------------//
			
			//Create an intent to launch an Activiy for the tab (to be reused)
			intent = new Intent().setClass(this, ContactsTab.class);
			
			//Get the string resource from the string xml set constants
			CharSequence contactsLabel = getString(R.string.contacts_tab_label);
			
			//Initialize a TabSpec for each tab and add it to the TabHost. Also
			//we add the intent to the tab for when it push, use the intent added
			//(go to the clock tab)
			tabSpec = tabHost.newTabSpec(contactsLabel.toString()).setIndicator(
					contactsLabel,
					resources.getDrawable(R.drawable.contactstab)).setContent(intent);			
					
			//Add the TabSpec to the TabHost
			tabHost.addTab(tabSpec);

			
		}catch (Exception e) {
			e.printStackTrace();
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception(e.toString());
		}
					
	}
	
	/**
	 * Create the Schedule Tab
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createScheduleTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Schedule Tab	--------------------------//
			
			intent = new Intent().setClass(this, ScheduleTab.class);
			
			CharSequence scheduleLabel = getString(R.string.schedule_tab_label);

			tabSpec = tabHost.newTabSpec(scheduleLabel.toString()).setIndicator(
					scheduleLabel,
					resources.getDrawable(R.drawable.scheduletab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception(e.toString());
		}
	}	
	
	
	/**
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createSettingsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Settings Tab	--------------------------//
			
			intent = new Intent().setClass(this, SettingsTab.class);
			
			CharSequence messageLabel = getString(R.string.settings_tab_label);

			tabSpec = tabHost.newTabSpec(messageLabel.toString()).setIndicator(
					messageLabel,
					resources.getDrawable(R.drawable.settingstab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			throw new Exception(e.toString());
		}
	}	
	
	/**
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createLogsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Logs Tab	--------------------------//
			
			intent = new Intent().setClass(this, LogsTab.class);
			
			CharSequence messageLabel = getString(R.string.logs_tab_label);

			tabSpec = tabHost.newTabSpec(messageLabel.toString()).setIndicator(
					messageLabel,
					resources.getDrawable(R.drawable.logstab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			throw new Exception(e.toString());
		}
	}	
	

	/**
	 * This handler manages the action regarding to check if the user click yes 
	 * over the confirm action that realize the first database synchronization
	 * or not.
	 */
	public final Handler handler = new Handler() {
		public void handleMessage(Message message) {
											
			final String NUM_CONTACTS = "NUM_CONTACTS";
			
			int numContacts = message.getData().getInt(NUM_CONTACTS);
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Num contacts sync 1st time: " + numContacts);								
		}
	};

}
