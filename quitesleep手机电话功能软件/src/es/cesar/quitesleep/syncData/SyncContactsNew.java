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

package es.cesar.quitesleep.syncData;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import es.cesar.quitesleep.ddbb.BlockCallsConf;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Mail;
import es.cesar.quitesleep.ddbb.MuteOrHangUp;
import es.cesar.quitesleep.ddbb.Phone;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.dialogs.SyncContactsDialog;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.TokenizerUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SyncContactsNew extends Thread {
	
	final private String CLASS_NAME = getClass().getName(); 
	
	final private String NUM_CONTACTS = "NUM_CONTACTS";
	
	private final Context context;
	private int insertContact = 0;
	private SyncContactsDialog syncDialog;	
	private Handler handler;
		
	
	//--------------		Getters & Setters		-------------------------//
	public int getInsertContact() {
		return insertContact;
	}
	public void setInsertContact(int insertContact) {
		this.insertContact = insertContact;
	}

	public SyncContactsDialog getSyncDialog() {
		return syncDialog;
	}
	public void setSyncDialog(SyncContactsDialog syncDialog) {
		this.syncDialog = syncDialog;
	}		
	
	public Handler getHandler () {
		return handler;
	}	
	public void setHandler (Handler handler) {
		this.handler = handler;
	}
	//----------------------------------------------------------------------//

	/**
	 * Constructor with all the three obligatory params for run the synchronization
	 * use for the first time running the application (empty db4o database 
	 * contacts data) or for any other synchronization.
	 * 
	 * @param context
	 * @param syncDialog
	 * @param handler
	 */
	public SyncContactsNew (
			Context context, 
			SyncContactsDialog syncDialog,
			Handler handler) {
		
		this.context = context;
		this.syncDialog = syncDialog;
		this.handler = handler;
	}	
	
	/**
	 * Start the thread with the specified operations
	 */
	public void run () {
		
		createEssentialObjects();
		contactsSync();
	}
	
	
	/**
	 * This function creates the essential objects in the ddbb before to begin
	 * with the contacts synchronization.
	 */
	private void createEssentialObjects () {
		
		try {
	
			ClientDDBB clientDDBB = new ClientDDBB();
			
			//MuteOrHangup object initialization
			if (clientDDBB.getSelects().getNumberOfMuteOrHangup() == 0) {
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Inicializando mute or hangup");
				MuteOrHangUp muteOrHangUp = new MuteOrHangUp();
				clientDDBB.getInserts().insertMuteOrHangUp(muteOrHangUp);
				ConfigAppValues.setMuteOrHangup(false);
			}
			
			//BlockCallsConf object initialization
			if (clientDDBB.getSelects().getNumberOfBlockCallsConf() == 0) {
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Inicializando blockcallsconf");
				BlockCallsConf blockCallsConf = new BlockCallsConf();
				clientDDBB.getInserts().insertBlockCallsConf(blockCallsConf);
				ConfigAppValues.setBlockCallsConf(blockCallsConf);				
			}
			
			clientDDBB.commit();			
			clientDDBB.close();						
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
	
	/**
	 * Synchronize the SQLite data contacts, with at least one phone number,
	 * with the DB4O database.
	 *  
	 * @see				insertsContacts after run and finish this function for
	 * 					check how many contacts are synchronized.
	 */
	private void contactsSync () {
		
		try {
			
			if (context != null) {
				
				//int insertContact = 0;
				ClientDDBB clientDDBB = new ClientDDBB();
				
				//get all contacts from de ddbb
				Cursor contactCursor = context.getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI, 
						null,		// Which columns to return.
						null,		// WHERE clause.
						null,		// WHERE clause value substitution
						null);		// Sort order.
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Num contacts: " + contactCursor.getCount()); 
				
				//While startCursor has content
				while (contactCursor.moveToNext()) {
					
					String contactName = contactCursor.getString(contactCursor.getColumnIndex(
							ContactsContract.Data.DISPLAY_NAME));
					
				   String contactId = contactCursor.getString(contactCursor.getColumnIndex(
						   ContactsContract.Contacts._ID));
				   
				   String hasPhone = contactCursor.getString(contactCursor.getColumnIndex(
						   ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
				   
				   //-------	for debug only		-------------------//
				   if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Name: " + contactName + "\tId: " + 
						   contactId + "\thasPhone: " + hasPhone);
				   
				   boolean res = Boolean.parseBoolean(hasPhone);
				   if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "boolean: " + res);
				   //---------------------------------------------------//
				   
				   //If the used contact has at least one phone number, we
				   //insert it in the db4o ddbb, else not insert.
				   if (hasPhone.equals("1")) { 
					   
					   //Create the db4o contact object.
					   Contact contact = new Contact(contactId, contactName);		
					   
					   //insert the contact object
					   clientDDBB.getInserts().insertContact(contact);
					   
					   insertContact ++;
					   					   				   					 
					   //where clausule
					   String where = ContactsContract.Data.CONTACT_ID + " = " + 
					   		contactId + " AND " + ContactsContract.Data.MIMETYPE + 
					   		" = '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
					   		"'";
					   
					   Cursor phonesCursor = context.getContentResolver().query(
								ContactsContract.Data.CONTENT_URI,
								null,
								where,
								null,
								null);
					   
					   if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "count: " + phonesCursor.getCount());
					   
					   List<String> phonesList = new ArrayList<String>(phonesCursor.getCount());
					   
					   //While the phonesCursor has content
					   while (phonesCursor.moveToNext()) {
						   
						   String phoneNumber = phonesCursor.getString(
								   phonesCursor.getColumnIndex(
										   ContactsContract.CommonDataKinds.Phone.NUMBER));
						   
						   phonesList.add(phoneNumber);
						   
						   if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "phone: " + phoneNumber);
					   } 
					   //close the phones cursor
					   phonesCursor.close();
					   
					   //create the phones object asociated to contacts
					   createPhoneObjects(contact, phonesList, clientDDBB);			
				   
					   //Get all mail from the contact
					   Cursor mailCursor = context.getContentResolver().query(
							   ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							   null, 
							   ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, 
							   null, 
							   null);
					   
					   if (mailCursor.getCount() > 0) {
						   
						   List<String> mailsList = new ArrayList<String>(mailCursor.getColumnCount());
						   //While the emailsCursor has content
						   while (mailCursor.moveToNext()) { 
							   
							   //This would allow you get several email addresses 
							   String emailAddress = mailCursor.getString( 
									   mailCursor.getColumnIndex( 
											   ContactsContract.CommonDataKinds.Email.DATA));
							   						   
							   if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "email: " + emailAddress);
							   mailsList.add(emailAddress);						   
						   }
						   						   
						   //Create the mail objects asociated to every contact
						   createMailObjects(contact, mailsList, clientDDBB);
					   } 				   										  
					   //close the email cursor
					  mailCursor.close();
					 
				   }//end hasPhone	
				 
				   //create the Schedule object if not have been created previously
				   createSchedule(clientDDBB);
				   
				   //Commit the transaction
				   clientDDBB.commit();
				   
				}//end contact cursor
								
				//return insertContact;
				
			}//end context!=null
			else			
				//return -1;
				insertContact = -1;
			
			//this.sleep(5000);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			//return -1;
			insertContact = -1;
		}finally {
			//Hide and dismiss de synchronization dialog
			syncDialog.stopDialog(context);			
			
			//Create and send the numBanned message to the handler in gui main thread
			Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(NUM_CONTACTS, insertContact);
            message.setData(bundle);
            handler.sendMessage(message);
		}
	}
	
	/**
	 * 
	 * @param 		contact
	 * @param 		phonesList
	 * @param 		clientDDBB
	 * @return		the number of phones created
	 * @see			int
	 */
	private int createPhoneObjects (
			Contact contact, 
			List<String> phonesList, 
			ClientDDBB clientDDBB) {
		
		try {
			
			int numPhones = 0;
			
			for (int i=0; i<phonesList.size(); i++) {
								
				/* Here parse the original phone number, wich, by default
				 * contains x dash by other wich contains nothing delim.
				 */
				String newPhone = TokenizerUtils.tokenizerPhoneNumber(phonesList.get(i), null);
				
				Phone phone = new Phone(contact, newPhone);
				numPhones ++;			
				clientDDBB.getInserts().insertPhone(phone);
			}
			
			return numPhones;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * 
	 * @param 		contact
	 * @param 		mailsList
	 * @param 		clientDDBB
	 * @return		the number of mails created (inserted)
	 * @see 		int
	 */
	private  int createMailObjects (
			Contact contact, 
			List<String> mailsList,
			ClientDDBB clientDDBB) {
		
		try {						
			int numMails = 0;
			
			for (int i=0; i<mailsList.size(); i++) {											
				Mail mail = new Mail(contact, mailsList.get(i));
				numMails ++;										
				clientDDBB.getInserts().insertMail(mail);
			}
			
			return numMails;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * Create the Schedule object if not was created previously. This object
	 * is an empty Schedule.
	 * 
	 * @param 			clientDDBB
	 * @return			true if Schedule object is created or false is not
	 * @see				boolean
	 */
	private boolean createSchedule (ClientDDBB clientDDBB) {
		
		try {			
			
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			if (schedule == null) {
				schedule = new Schedule();
				clientDDBB.getInserts().insertSchedule(schedule);
				return true;
			}
			
			return false;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}

	
	

}
