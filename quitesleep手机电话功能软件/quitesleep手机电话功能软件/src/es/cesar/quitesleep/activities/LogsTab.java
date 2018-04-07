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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.CallLog;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.dialogs.WarningDialog;
import es.cesar.quitesleep.interfaces.IDialogs;
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
public class LogsTab extends ListActivity implements IDialogs {

	//Constants
	final private String CLASS_NAME = this.getClass().getName();
	final private int WARNING_REMOVE_DIALOG 	= 	0;
	final private int WARNING_REFRESH_DIALOG 	= 	1;
	final private int HELP_DIALOG 		= 2;

	//Widgets Ids
	private final int removeCallLogMenuId = R.id.menu_calllog_remove;
	private final int refreshCallLogMenuId = R.id.menu_calllog_refresh;
	private final int helpCallLogMenuId = R.id.menu_information_help;
	
	//Widgets
	private WarningDialog warningRemoveDialog;
	private WarningDialog warningRefreshDialog;
	private ArrayAdapter<String> arrayAdapter;	
	
	//this activity
	private Activity thisActivity = this;
	
	@Override	
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		warningRemoveDialog = new WarningDialog(
				this, 
				ConfigAppValues.WARNING_REMOVE_ALL_CALL_LOGS);
		
		warningRefreshDialog = new WarningDialog(
				this, 
				ConfigAppValues.WARNING_REFRESH_CALL_LOG);
		
		getAllCallLogList();
		
	}
	
	/**
	 * Get all CallLog list from de ddbb
	 */
	private void getAllCallLogList () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			List<CallLog> callLogList = clientDDBB.getSelects().selectAllCallLog();
			List<String> callLogListString = convertCallLogList(callLogList);
			
			if (callLogListString != null) {
				arrayAdapter = new ArrayAdapter<String>(
					this, 
					R.layout.logstab,
					R.id.logstab_textview_contact,
					callLogListString);
			
				setListAdapter(arrayAdapter);														
			}
			
			clientDDBB.close();
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
							e.toString(), 
							e.getStackTrace()));
		}		
	}
	
	
	/**
	 * 
	 * @param 		contactList
	 * @return 		The contactList but only the list with the name contacts
	 * @see			List<String>
	 */
	private List<String> convertCallLogList (List<CallLog> callLogList) throws Exception {
		
		try {
			
			if (callLogList != null && callLogList.size()>0) {
				
				List<String> callLogListString = new ArrayList<String>();
				
				for (int i=0; i<callLogList.size(); i++) {
					String callLogString = callLogList.get(i).toString();
					if (callLogString != null)						
						callLogListString.add(callLogString);					
				}
				return callLogListString;
			}
			return null;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
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
			case WARNING_REMOVE_DIALOG:				
				dialog = warningRemoveDialog.getAlertDialog();				
				break;
			case WARNING_REFRESH_DIALOG:				
				dialog = warningRefreshDialog.getAlertDialog();				
				break;
			case HELP_DIALOG:
				dialog = showWebviewDialog(IDialogs.HELP_LOGS_URI);
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
	 * This function prepare the dalogs every time to call for some of this
	 * 
	 *  @param int
	 *  @param dialog
	 */
	@Override
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
		
		try {
			
			switch (idDialog) {			
				case WARNING_REMOVE_DIALOG:
					warningRemoveDialog.setContext(this);
					warningRemoveDialog.setArrayAdapter(arrayAdapter);
					warningRemoveDialog.setHandler(handlerRemove);										
					break;
				case WARNING_REFRESH_DIALOG:
					warningRefreshDialog.setContext(this);
					warningRefreshDialog.setArrayAdapter(arrayAdapter);
					warningRefreshDialog.setHandler(handlerRefresh);										
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
	
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		try {							
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.calllogmenu, menu);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
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
			
				case removeCallLogMenuId:					
					showDialog(WARNING_REMOVE_DIALOG);
					break;					
				case refreshCallLogMenuId:
					showDialog(WARNING_REFRESH_DIALOG);
					break;
				case helpCallLogMenuId:
					showDialog(HELP_DIALOG);
					break;
				default:
					break;
			}
			return false;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;			
		}
	}
	
	
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRemove = new Handler() {
		public void handleMessage(Message message) {						
			
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numRemoveCallLogs = message.getData().getInt(
						ConfigAppValues.NUM_REMOVE_CALL_LOGS);
				
				//clear the arrayAdapter
				arrayAdapter.clear();
				
				//Show the toast message
				Toast.makeText(
                		ConfigAppValues.getContext(),
                		numRemoveCallLogs + " " + ConfigAppValues.getContext().getString(
                				R.string.menu_calllog_remove_toast),
                		Toast.LENGTH_SHORT).show();		
			}
		}
	};
	
	
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRefresh = new Handler() {
		public void handleMessage(Message message) {						
			
			ArrayList<String> callLogListString = null;
			
			if (arrayAdapter != null) {																
				callLogListString = message.getData().getStringArrayList(
						ConfigAppValues.REFRESH_CALL_LOG);
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "callLoglistString: " + callLogListString);
				
				//set the array adapter
				if (callLogListString != null) {
					
					//first delete the previous content list
					arrayAdapter.clear();
					
					//Second, add all call logs to the list
					for (int i=0; i<callLogListString.size(); i++) {
						String callLog = callLogListString.get(i);
						Log.d(CLASS_NAME, "callLog:" ) ;
						arrayAdapter.add(callLog);
					}
				}									
				//Show the toast message
				if (QSToast.RELEASE) QSToast.r(
                		ConfigAppValues.getContext(),
                		ConfigAppValues.getContext().getString(
                				R.string.menu_calllog_refresh_toast),
                		Toast.LENGTH_SHORT);			
			}
			/* If the arrayAdapter previously doesn't have any call log, this
			 * not been initialized, so initialize now.
			 */
			else {				
				callLogListString = message.getData().getStringArrayList(
						ConfigAppValues.REFRESH_CALL_LOG);
				Log.d(CLASS_NAME, "inicializando arrayAdapter. " +
						"CallLogListString: " + callLogListString);
				arrayAdapter = new ArrayAdapter<String>(
						thisActivity, 
						R.layout.logstab,
						R.id.logstab_textview_contact,
						callLogListString);
				setListAdapter(arrayAdapter);		
			}
		}
	};
	
	
}
