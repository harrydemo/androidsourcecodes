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

package es.cesar.quitesleep.ddbb;

import java.util.List;

import android.util.Log;

import com.db4o.ObjectContainer;
import com.db4o.ext.StoredClass;
import com.db4o.query.Query;

import es.cesar.quitesleep.interfaces.IDDBB;
import es.cesar.quitesleep.staticValues.DDBBValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Selects implements IDDBB {
	
	private final String CLASS_NAME = getClass().getName();		
	
		
	private ObjectContainer db;
	
	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public Selects (ObjectContainer db) {
		this.db = db;
	}
	
	/**
	 * This function gets the number of the contacts objects in the ddbb.
	 * 
	 * @return
	 */
	public int getNumberOfContacts () {
		
		try {			
			synchronized (SEMAPHORE) {
				
				StoredClass sc = db.ext().storedClass(Contact.class);
				
				if (sc != null)
					return sc.getIDs().length;
				else
					return 0;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * This function gets the number of the MuteOrHangUp objects in the ddbb.
	 * At most, should only be one object
	 * 
	 * @return
	 */
	public int getNumberOfMuteOrHangup () {
		
		try {			
			synchronized (SEMAPHORE) {
				
				StoredClass sc = db.ext().storedClass(MuteOrHangUp.class);
				
				if (sc != null)
					return sc.getIDs().length;
				else
					return 0;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * This function gets the number of the BlockCallsConf objects in the ddbb.
	 * At most, should only be one object
	 * 
	 * @return
	 */
	public int getNumberOfBlockCallsConf () {
		
		try {			
			synchronized (SEMAPHORE) {
				
				StoredClass sc = db.ext().storedClass(BlockCallsConf.class);
				
				if (sc != null)
					return sc.getIDs().length;
				else
					return 0;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * Select all Contact objects from the DDBB and return it.
	 * 
	 * @return		All Contact objects from the DDBB
	 * @see			List<Contact>
	 */
	public List<Contact> selectAllContacts () {
		
		try {
			
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				
				query.constrain(Contact.class);
				
				//Ordered by name (first A....last Z)
				query.descend(DDBBValues.CONTACT_NAME).orderAscending();
				
				List<Contact> contactList = query.execute();
				
				return contactList;								
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Select one Contact that is the same with the name passed
	 * 
	 * @param 		contactName
	 * @return		The contact match with the passed name
	 * @see			Contact 
	 */
	public Contact selectContactForName (String contactName) {
		
		try {
			
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Contact.class);
				query.descend(DDBBValues.CONTACT_NAME).constrain(contactName);
				
				List<Contact> contactList = query.execute();
				if (contactList != null && contactList.size() == 1)
					return contactList.get(0);
				else
					return null;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, e.toString());
			return null;
		}
	}
	
	
	/**
	 * Select Contact object match with the passed phoneNumber, search
	 * along all contact's phone numbers.
	 * 
	 * @param 			phoneContact
	 * @return			Contact that match with the passed phone number
	 * 					any contact's phone number
	 * @see				Contact
	 */
	public Contact selectContactForPhoneNumber (String phoneContact) {
		
		try {
			
			synchronized (SEMAPHORE) { 
			
				Query query = db.query();
				query.constrain(Phone.class);
				query.descend(DDBBValues.CONTACT_PHONE).constrain(phoneContact);
					
				List<Phone> contactList = query.execute();
				if (contactList != null && contactList.size() == 1) {
					Phone phone = contactList.get(0);
					return phone.getContact();
				}					
				else
					return null;
				
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, e.toString());
			return null;
		}
	}
	
	/**
	 * Select Contact object match with the passed phoneNumber, search
	 * along all contact's phone numbers.
	 * 
	 * @param 			phoneNumber
	 * @return			Contact that match with the passed phone number
	 * 					any contact's phone number
	 * @see				Contact
	 */
	public Contact selectContactForMail (String mailContact) {
		
		try {
			
			synchronized (SEMAPHORE) { 
			
				Query query = db.query();
				query.constrain(Mail.class);
				query.descend(DDBBValues.CONTACT_MAIL).constrain(mailContact);
					
				List<Mail> contactList = query.execute();
				if (contactList != null && contactList.size() == 1) {
					Mail mail = contactList.get(0);
					return mail.getContact();
				}					
				else
					return null;
				
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, e.toString());
			return null;
		}
	}
	
	
	/**
	 * Get all phones of the selected contact
	 * @param 			contactName
	 * @return			All phones about the selected contact
	 * @see				List<Phone>			
	 */
	public List<Phone> selectAllContactPhonesForName (String contactName) {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Phone.class);
				
				query.descend(DDBBValues.CONTACT).descend(
						DDBBValues.CONTACT_NAME).constrain(contactName);
				
				List<Phone> phonesList = query.execute();
				
				return phonesList;
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Get all mails from the selected contact
	 * @param 			contactName
	 * @return			List with all contact's mails
	 * @see				List<Mail>
	 */
	public List<Mail> selectAllContactMailsForName (String contactName) {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Mail.class);
				
				query.descend(DDBBValues.CONTACT).descend(
						DDBBValues.CONTACT_NAME).constrain(contactName);
				
				List<Mail> mailsList = query.execute();
				
				return mailsList;
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * 
	 * @param contact
	 * @return
	 */
	public List<Mail> selectAllContactMailForContact (Contact contact) {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Mail.class);
				query.descend(DDBBValues.CONTACT).constrain(contact);
				List<Mail> mailList = query.execute();
				
				return mailList;
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Get all banned contacts from the database
	 * 
	 * @return		List with all contacts or null if there any exception
	 * @see			List<Banned>
	 */
	public List<Banned> selectAllBannedContacts () {
		
		try {
			
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				
				query.constrain(Banned.class);
				
				//Ordered by name (first A....last Z)
				query.descend(DDBBValues.CONTACT).
					descend(DDBBValues.CONTACT_NAME).orderAscending();
				
				List<Banned> bannedList = query.execute();
				
				return bannedList;								
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	

	/**
	 * Get all not banned contacts from the database
	 * 
	 * @return
	 */
	public List<Contact> selectAllNotBannedContacts () {
		
		try {			
			synchronized (SEMAPHORE) {
				Query query = db.query();
				query.constrain(Contact.class);				
				query.descend(DDBBValues.BANNED).constrain(false);
				query.descend(DDBBValues.CONTACT_NAME).orderAscending();												
				
				List<Contact> contactList = query.execute();
				
				return contactList;				
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Get the banned contact associate to the phone number passed to the 
	 * function. Check if the contact name associate to the phone is in the
	 * ddbb and if the contact has the flag banned to true.
	 * 
	 * @param 				phoneNumber
	 * @return 				contact if exists one contact banned who has
	 * 						the incoming number passed
	 * @see					Contact
	 */
	public Contact selectBannedContactForPhoneNumber (String phoneNumber) {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Phone.class);
				
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "PhoneNumber: " + phoneNumber);
				query.descend(DDBBValues.CONTACT_PHONE).constrain(phoneNumber).and(
						query.descend(DDBBValues.CONTACT).descend(DDBBValues.BANNED).constrain(true));
				
				List<Phone> phoneBannedList = query.execute();
				if (phoneBannedList != null && phoneBannedList.size() == 1) {
				
					Phone phone = phoneBannedList.get(0);
					if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Phone: " + phone);
					return phone.getContact();
				}
				else
					return null;
					
			}									
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
		
	}
	
	/**
	 * Get the banned object that match with the contactName passed.
	 * 
	 * @param 			contactName
	 * @return			The banned object matches with the contactName passed.
	 * @see				Banned
	 */
	public Banned selectBannedContactForName (String contactName) {
		
		try {
			synchronized(SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Banned.class);
				query.descend(DDBBValues.CONTACT).descend(
						DDBBValues.CONTACT_NAME).constrain(contactName);
				
				List<Banned> bannedList = query.execute();
				if (bannedList != null && bannedList.size() == 1) 
					return bannedList.get(0);
				else 
					return null;			
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	
	
	
	/**
	 * Get the (for the moment) the one Schedule object in the database.
	 * For now (04-17-2010) in this application version of quitesleep, must be
	 * only one Schedule object in the database, so all banned objects have to
	 * use the same Schedule object.
	 * 
	 * @return			The (for the moment) only Schedule object in the database
	 * @see				Schedule
	 */
	public Schedule selectSchedule () {
		
		try {			
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Schedule.class);
				
				List<Schedule> scheduleList = query.execute();
				if (scheduleList != null && scheduleList.size() == 1)
					return scheduleList.get(0);
				else
					return null;
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Return the settings object only if is the one in the ddbb, else
	 * return null because have to be an error, only must be one Settings object
	 * in the ddbb.
	 * 
	 * @return		The settings object
	 * @see			Settings
	 */
	public Settings selectSettings () {
		
		try {
			synchronized(SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Settings.class);
				
				List<Settings> settingsList = query.execute();				
				if (settingsList != null && settingsList.size()==1)
					return settingsList.get(0);
				else
					return null;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Select the Phone object that contains the phoneNumber passed throw the param
	 * value. Returns null if exists more than one object, or there isn't nothing 
	 * in the ddbb.
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public Phone selectPhoneForPhoneNumber (String phoneNumber) {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Phone.class);
				query.descend(DDBBValues.CONTACT_PHONE).constrain(phoneNumber);
				List<Phone> phoneList = query.execute();
				if (phoneList != null)
					return phoneList.get(0);
				else
					return null;								
			}			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}		
		
	/**
	 * Select all CallLog objects from the ddbb
	 * 
	 * @return
	 */
	public List<CallLog> selectAllCallLog () {
		
		try {
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(CallLog.class);
				
				//Order by the most new to the most older
				query.descend(DDBBValues.NUM_ORDER).orderDescending();
				List<CallLog> callLogList = query.execute();
				
				return callLogList;
					
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}

	/**
	 * Return an integer counts the number of CallLog objects in the ddbb.
	 * 
	 * @return int
	 */
	public int countCallLog () {
		
		try {
			synchronized (SEMAPHORE) {
				
				StoredClass storedClass  = db.ext().storedClass(CallLog.class);
				return storedClass.getIDs().length;
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * Select the BlockCallsConfig object in the ddbb, only must return one object.
	 * If there are more than one, then returns null.
	 * 
	 * @return {@link BlockCallsConf}
	 */
	public BlockCallsConf selectBlockCallConf () {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(BlockCallsConf.class);
				
				List<BlockCallsConf> listBlockCallsConf = query.execute();
				if (listBlockCallsConf != null && listBlockCallsConf.size() == 1)
					return listBlockCallsConf.get(0);
				else
					return null;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	
	/**
	 * This function gets a MuteOrHangUp object from the ddbb.
	 * @return
	 */
	public MuteOrHangUp selectMuteOrHangUp () {
		
		try {			
			synchronized (SEMAPHORE) {
				Query query = db.query();
				query.constrain(MuteOrHangUp.class);
				
				List<MuteOrHangUp> listMuteOrHangup = query.execute();
				if (listMuteOrHangup != null && listMuteOrHangup.size() == 1)
					return listMuteOrHangup.get(0);
				else
					return null;
				
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
}
