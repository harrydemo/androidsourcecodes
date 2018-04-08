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

package es.cesar.quitesleep.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import es.cesar.quitesleep.R;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SyncContactsDialog {
	
	private String CLASS_NAME = getClass().getName();	

	final int FIRST_TIME_SYNC = R.string.synccontactsdialog_message_firsttime_label;
	final int ANY_TIME_SYNC = R.string.synccontactsdialog_message_anytime_label;
	final int REFRESH_LIST = R.string.synccontactsdialog_message_refresh_label;
	
	private ProgressDialog progressDialog;
	private String messageDialog = "";	
	
	
	//--------	Getters & Setters	--------------------------------------//
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	public String getMessageDialog () {
		return messageDialog;
	}
	public void setMessageDialog (String messageDialog) {
		this.messageDialog = messageDialog;
	}
	
	//--------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters. 
	 */
	public SyncContactsDialog () {
					
	}
	
	
	/**
	 * Show message regarding first synchronization		
	 * @param context
	 */
	public void showDialogFirstTime (Context context) {
	
		messageDialog = context.getString(FIRST_TIME_SYNC);		
		showDialog(context);
	}
		
	/**
	 * Show message regarding any synchronization.
	 * At the moment (24/02/2010) is not used.
	 * @param context
	 */
	public void showDialogAnyTime (Context context) {
		
		messageDialog = context.getString(ANY_TIME_SYNC);
		showDialog(context);
	}
		
	/**
	 * Show message regarding refreshing synchronization.
	 * 
	 * @param context
	 */
	public void showDialogRefreshList (Context context) {
		messageDialog = context.getString(REFRESH_LIST);
		showDialog(context);
	}
	
	
	/**
	 * Show the dialog with other synchronization message that we like
	 * @param context
	 * @param messageDialg
	 */
	public void showDialogOtherMessage (Context context, String messageDialg) {
		this.messageDialog = messageDialg;
		showDialog(context);
	}
	

	/**
	 * Show the synchronization message
	 * @param context
	 */
	private void showDialog (Context context) {		
		
		progressDialog = ProgressDialog.show(context, "", messageDialog, true);
		
	}
	
	/**
	 * Hide and dismiss the synchronization message
	 * @param context
	 */
	public void stopDialog (Context context) {
		//progressDialog.cancel();
		progressDialog.dismiss();
	}

}
