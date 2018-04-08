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
import com.db4o.query.Query;

import es.cesar.quitesleep.interfaces.IDDBB;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Deletes implements IDDBB {
	
	final String CLASS_NAME = getClass().getName();
	
	private ObjectContainer db;
	
	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public Deletes (ObjectContainer db) {
		this.db = db;
	}
	
	
	/**
	 * Delete one Contact object in the DDBB that matches with the passed
	 * 
	 * @param 			contact
	 * @return			true or false with the operation result
	 * @see				boolean
	 */
	public boolean deleteContact (Contact contact) {
		
		try {
			synchronized (SEMAPHORE) {
				db.delete(contact);
				return true;				
			}						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Delete the banned object passed
	 * 
	 * @param 			banned
	 * @return			true or false of the banned object delete sucessfully
	 * @see				boolean
	 */
	public boolean deleteBanned (Banned banned) {
		
		try {
			synchronized (SEMAPHORE) { 
				
				db.delete(banned);
				return true;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * 
	 * @param settings
	 * @return
	 */
	public boolean deleteSettings (Settings settings) {
		
		try {
			synchronized(SEMAPHORE) {
				
				db.delete(settings);
				return true;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int deleteAllContacts () {
		
		try {
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Contact.class);
				List<Contact> contactList = query.execute();
				int deletes = 0;
				
				if (contactList != null)
					for (int i=0; i<contactList.size(); i++){
						Contact contact = contactList.get(i);
						db.delete(contact);
						deletes ++;
					}				
				return deletes;				
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int deleteAllPhones () {
		
		try {
			synchronized(SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(Phone.class);
				List<Phone> phoneList = query.execute();
				int deletes = 0;
				
				if (phoneList != null)
					for (int i=0; i<phoneList.size(); i++) {
						Phone phone = phoneList.get(i);
						db.delete(phone);
						deletes ++;
					}
				
				return deletes;
			}
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int deleteAllMails () {
		
		try {
			synchronized (SEMAPHORE) {
			
				Query query = db.query();
				query.constrain(Mail.class);
				List<Mail> mailList = query.execute();				
				int deletes = 0;
				
				if (mailList != null)
					for (int i=0; i<mailList.size(); i++) {
						Mail mail = mailList.get(i);
						db.delete(mail);
						deletes++;
					}
				return deletes;
			}						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int deleteAllBanned () {
		
		try {
			
			Query query = db.query();
			query.constrain(Banned.class);
			List<Banned> bannedList = query.execute();
			int deletes = 0;
			
			if (bannedList != null)
				for (int i=0; i<bannedList.size(); i++) {
					Banned banned = bannedList.get(i);
					db.delete(banned);
					deletes++;
				}
			
			return deletes;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int deleteAllCallLog () {
		
		try {
			synchronized (SEMAPHORE) {
				
				Query query = db.query();
				query.constrain(CallLog.class);
				List<CallLog> callLogList = query.execute();
				int deletes = 0;
				
				if (callLogList != null)
					for (int i=0; i<callLogList.size(); i++) {
						CallLog callLog = callLogList.get(i);
						db.delete(callLog);
						deletes ++;
					}
				return deletes;
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return -1;
		}
	}

}
