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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.Banned;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.dialogs.WarningDialog;
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
public class DeleteBanned extends ListActivity {
	
	//Constants
	final private String CLASS_NAME = this.getClass().getName();
	final private int WARNING_DIALOG = 0;
	
	//Widgets Ids
	private final int removeAllMenuId = R.id.menu_removeall;
	
	//Widgets
	private WarningDialog warningDialog;		
	private ArrayAdapter<String> arrayAdapter;	
	
	//Attributes
	private String selectContactName;		

	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		try {
			super.onCreate(savedInstanceState);						
			
			warningDialog = new WarningDialog(
					this, 
					ConfigAppValues.WARNING_REMOVE_ALL_CONTACTS);
		
			getAllContactList();
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	/**
	 * Get all banned contact list from the database
	 */
	private void getAllContactList () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			List<Banned> contactList = clientDDBB.getSelects().selectAllBannedContacts();
			List<String> contactListString = convertContactList(contactList);
			
			if (contactListString != null) {
				arrayAdapter = new ArrayAdapter<String>(
					this, 
					R.layout.deletebanned,
					R.id.deleteBanned_textview_name,
					contactListString);
			
				setListAdapter(arrayAdapter);																							
			}
			
			clientDDBB.close();
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
							e.toString(), 
							e.getStackTrace()));
		}
		
	}
	
	/**
	 * Function that convert all banned contact list in Contact objects to
	 * one List<String> with only the contact name attribute.
	 * 
	 * @param 		contactList
	 * @return 		The contactList but only the list with the name contacts
	 * @see			List<String>
	 */
	private List<String> convertContactList (List<Banned> bannedList) throws Exception {
		
		try {
			
			if (bannedList != null && bannedList.size()>0) {
				
				List<String> bannedListString = new ArrayList<String>(bannedList.size());
				
				for (int i=0; i<bannedList.size(); i++) {
					Banned banned = bannedList.get(i);
					String contactName = banned.getContact().getContactName();
					if (contactName == null)
						contactName = "";
					bannedListString.add(contactName);
					
				}
				return bannedListString;
			}
			return null;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
		}
	}
	
	@Override
	protected void onListItemClick (
			ListView listView,
    		View view,
    		int position,
    		long id){
		
		try {
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "OnListItemClick");
			
			super.onListItemClick(listView, view, position, id);
						
			selectContactName = (String) this.getListAdapter().getItem(position);	
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Name: " + selectContactName);						
			
			/* If we like to use one subactivity for show better contact details
			 * and edit what phone number and/or mail addresses are used for 
			 * send busy response, and remove contact from banned list.
			 */			
			Intent intentEditContact = new Intent(this, EditContact.class);
			intentEditContact.putExtra(ConfigAppValues.CONTACT_NAME, selectContactName);
			startActivityForResult(intentEditContact, ConfigAppValues.REQCODE_EDIT_CONTACT);
									
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));			
		}		
	}
	
	@Override	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
			case ConfigAppValues.REQCODE_EDIT_CONTACT:
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Valor retornado: " + resultCode);
				if (resultCode == Activity.RESULT_OK)
					arrayAdapter.remove(selectContactName);
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
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Create the AlertDialog for 1st time");
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
					warningDialog.setArrayAdapter(arrayAdapter);
					warningDialog.setHandler(handler);										
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
			menuInflater.inflate(R.menu.removeallmenu, menu);
			
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
			
				case removeAllMenuId:					
					showDialog(WARNING_DIALOG);
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
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handler = new Handler() {
		public void handleMessage(Message message) {						
			
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numRemoveContacts = message.getData().getInt(
						ConfigAppValues.NUM_REMOVE_CONTACTS);
				
				//clear the arrayAdapter
				arrayAdapter.clear();
				
				//Show the toast message
				if (QSToast.RELEASE) QSToast.r(
                		ConfigAppValues.getContext(),
                		numRemoveContacts + " " + ConfigAppValues.getContext().getString(
                				R.string.menu_removeall_toast_removecount),
                		Toast.LENGTH_SHORT);		
			}
		}
	};

}
