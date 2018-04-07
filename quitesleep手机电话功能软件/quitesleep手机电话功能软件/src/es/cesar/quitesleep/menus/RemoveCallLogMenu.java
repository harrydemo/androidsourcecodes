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
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.dialogs.CallLogDialog;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class RemoveCallLogMenu extends Thread {
	
	private final String CLASS_NAME = getClass().getName();
	
	private ArrayAdapter<String> arrayAdapter = null;
	private CallLogDialog callLogDialog;
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
	 * @param 		callLogDialog
	 * @param 		handler
	 */
	public RemoveCallLogMenu (
			ArrayAdapter<String> arrayAdapter, 
			CallLogDialog callLogDialog, 
			Handler handler) {
		
		this.arrayAdapter = arrayAdapter;
		this.callLogDialog = callLogDialog;
		this.handler = handler;
	}
	
	
	public void run () {
		
		removeAll();
	}
	
	/**
	 * Remove all callLog objects from the ddbb and the list
	 */
	private void removeAll () {
				
		int numRemoveCallLogs = 0;
		
		try {		
			ClientDDBB clientDDBB = new ClientDDBB();
				
			numRemoveCallLogs = clientDDBB.getDeletes().deleteAllCallLog();				
				
			clientDDBB.commit();	
			clientDDBB.close();
																		
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));		
		}finally {
			//Hide and dismiss de synchronization dialog
			callLogDialog.stopDialog(ConfigAppValues.getContext());
			
			//Create and send the numBanned message to the handler in gui main thread
			Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(ConfigAppValues.NUM_REMOVE_CALL_LOGS, numRemoveCallLogs);
            message.setData(bundle);
            handler.sendMessage(message);

		}
		
	}

}
