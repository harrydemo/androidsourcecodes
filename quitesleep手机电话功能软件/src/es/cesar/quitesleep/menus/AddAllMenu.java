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

package es.cesar.quitesleep.menus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import es.cesar.quitesleep.ddbb.Banned;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.dialogs.AddAllDialog;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author 	Cesar Valiente Gordo
 * @mail	cesar.valiente@gmail.com
 *
 */
public class AddAllMenu extends Thread {

	
	private final String CLASS_NAME = getClass().getName();
	
	private ArrayAdapter<String> arrayAdapter = null;
	private AddAllDialog addAllDialog;	
	private Handler handler;

	//-------------		Getters & Setters		------------------------------//
	public ArrayAdapter<String> getArrayAdapter() {
		return arrayAdapter;
	}
	public void setArrayAdapter(ArrayAdapter<String> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}	
	//------------------------------------------------------------------------//
	
	
	
	/**
	 * Constructor with the basic parameter
	 * 
	 * @param		arrayAdapter
	 * @param 		addAllDialog
	 * @param 		handler
	 */
	public AddAllMenu (
			ArrayAdapter<String> arrayAdapter, 
			AddAllDialog addAllDialog, 
			Handler handler) {
		
		this.arrayAdapter = arrayAdapter;
		this.addAllDialog = addAllDialog;
		this.handler = handler;		
	}
	
	
	public void run () {
		
		addAll();
	}
	
	/**
	 * Add to the banned list all contacts of the listview (arrayadapter)
	 * 
	 */
	private void addAll () {
		
		final String NUM_BANNED = "NUM_BANNED";
		int numBanend = 0;
		
		try {													
			ClientDDBB clientDDBB = new ClientDDBB();
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			
			if (schedule != null) { 								
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "size list: " + arrayAdapter.getCount());
								
				//while (arrayAdapter.getCount()>0) {
				for (int i=0; i<arrayAdapter.getCount(); i++) {
					
					/* all time we get the first element (0) of the list
					 * because when we remove the actual element at the final code
					 * the next element put to the first position, and so with all.
					 */
 					String contactName = arrayAdapter.getItem(i);
					
 					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "añadiendo: " + contactName);
					
					if (contactName != null && !contactName.equals("")) {					
						Contact contact = 
							clientDDBB.getSelects().selectContactForName(contactName);
						
						if (contact != null) {
							
							Banned banned = new Banned(contact, schedule);
							contact.setBanned(true);
							clientDDBB.getUpdates().insertContact(contact);
							clientDDBB.getInserts().insertBanned(banned);
							
							numBanend ++;							
						}	
					}
				}
				clientDDBB.commit();														
			}			
			clientDDBB.close();			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}finally {
			//Hide and dismiss de synchronization dialog
			addAllDialog.stopDialog(ConfigAppValues.getContext());
			
			//Create and send the numBanned message to the handler in gui main thread
			Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(NUM_BANNED, numBanend);
            message.setData(bundle);
            handler.sendMessage(message);
		}
	}
}
