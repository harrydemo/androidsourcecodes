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

package es.cesar.quitesleep.operations;

import java.util.List;

import android.util.Log;
import android.widget.CheckBox;
import es.cesar.quitesleep.ddbb.Banned;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Mail;
import es.cesar.quitesleep.ddbb.Phone;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author 	Cesar Valiente Gordo
 * @mail	cesar.valiente@gmail.com
 *
 */
public class ContactOperations {
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.operations.AddContact";
	
	
	/**
	 * Update the contact object, create the associated Banned object and
	 * updates the phones and mail objects putting the flag useToSend to true
	 * or false for send messages when incoming calls that not respond.
	 * 
	 * @param 		contactName
	 * @param 		phoneCheckboxList
	 * @param 		mailCheckboxList
	 * @return 		true or false depends for the operation successfully
	 * @see			boolean
	 */
	public static boolean addContact (
			String contactName,
			List<CheckBox> phoneCheckboxList, 
			List<CheckBox> mailCheckboxList) {
				
		try {
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "EN addContact");
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			//--------		Update Contact			--------------------------//
			Contact contact = clientDDBB.getSelects().selectContactForName(contactName);
			contact.setBanned(true);
			clientDDBB.getUpdates().insertContact(contact);
			
			
			//----------	Create and insert Banned		------------------//
			Schedule schedule = clientDDBB.getSelects().selectSchedule();			
			Banned banned = new Banned(contact, schedule);
			clientDDBB.getInserts().insertBanned(banned);
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Updated Contact and Banned");
			
			//---------------		Update Phone	--------------------------//
			List<Phone> phoneList = 
				clientDDBB.getSelects().selectAllContactPhonesForName(contactName);
						
			for (int i=0; i<phoneList.size(); i++) {
				
				Phone phone = phoneList.get(i);
				int j=0;
				boolean find = false;
				while (j<phoneCheckboxList.size() && !find) {
					CheckBox checkbox = phoneCheckboxList.get(j);
					if (checkbox.getText().equals(phone.getContactPhone())) {
						phone.setUsedToSend(checkbox.isChecked());
						clientDDBB.getUpdates().insertPhone(phone);
						find = true;						
					}
					j++;
				}
			}
							
			//-----------------		Update Mail		--------------------------//
			List<Mail> mailList = 
				clientDDBB.getSelects().selectAllContactMailsForName(contactName);
			
			for (int i=0; i<mailList.size(); i++) {
				Mail mail = mailList.get(i);
				int j=0;
				boolean find = false;
				while (j<mailCheckboxList.size() && !find) {
					CheckBox checkbox = mailCheckboxList.get(j);
					if (checkbox.getText().equals(mail.getContactMail())) {
						mail.setUsedToSend(checkbox.isChecked());
						clientDDBB.getUpdates().insertMail(mail);
						find = true;
					}
					j++;
				}
			}
			
			//----------	Commit and close		--------------------------//
			clientDDBB.commit();
			clientDDBB.close();
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}		
	}
	
	
	/**
	 * Remove contact from the banned list, updating the contact object and
	 * remove the banned object.
	 * 
	 * @param 			contactName
	 * @return			true or false depends for the operation successfully
	 * @see				boolean
	 */
	public static boolean removeContact (String contactName) {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			//---------		Update Contact		------------------------------//
			Contact contact = clientDDBB.getSelects().selectContactForName(contactName);
			contact.setBanned(false);
			clientDDBB.getUpdates().insertContact(contact);
			
			//---------		Delete Banned			--------------------------//
			Banned banned = clientDDBB.getSelects().selectBannedContactForName(contactName);
			clientDDBB.getDeletes().deleteBanned(banned);
			
			//----------		Commit and close		----------------------//
			clientDDBB.commit();
			clientDDBB.close();
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	
	/**
	 * Update all edit attributes from the contact passed as its phone numbers
	 * and mail addresses used for send messages when incomig call is here
	 * and the contact is banned.
	 * 
	 * @param contactName
	 * @param phoneCheckboxList
	 * @param mailCheckboxList
	 * 
	 * @return			true or false depends for the operation successfully
	 * @see				boolean
	 */
	public static boolean editContact (
			String contactName, 
			List<CheckBox> phoneCheckboxList,
			List<CheckBox> mailCheckboxList) {
		
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			
			//-----------------		Update all edit phones	------------------//
			List<Phone> phoneList = 
				clientDDBB.getSelects().selectAllContactPhonesForName(contactName);
			
			for (int i=0; i<phoneList.size(); i++) {
				Phone phone = phoneList.get(i);
				int j=0;
				boolean find = false;
				while (j<phoneCheckboxList.size() && !find) {
					CheckBox checkbox = phoneCheckboxList.get(j);
					if (checkbox.getText().equals(phone.getContactPhone())) {
						phone.setUsedToSend(checkbox.isChecked());
						clientDDBB.getUpdates().insertPhone(phone);
						find = true;
					}
					j++;
				}
			}
			
			//-------------		Update all edit mails 		------------------//
			List<Mail> mailList = 
				clientDDBB.getSelects().selectAllContactMailsForName(contactName);
			
			for (int i=0; i<mailList.size(); i++) {
				Mail mail = mailList.get(i);
				int j=0;
				boolean find = false;
				while (j<mailCheckboxList.size() && !find) {
					CheckBox checkbox = mailCheckboxList.get(j);
					if (checkbox.getText().equals(mail.getContactMail())) {
						mail.setUsedToSend(checkbox.isChecked());
						clientDDBB.getUpdates().insertMail(mail);
						find = true;
					}
					j++;
				}				
			}
			
			//----------------		Commit and close		------------------//
			clientDDBB.commit();
			clientDDBB.close();
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
}
