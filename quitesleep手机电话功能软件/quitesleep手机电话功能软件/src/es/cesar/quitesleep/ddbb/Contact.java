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


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Contact extends Id {
	
	private String contactId;	
	private String contactName;	
	
	private boolean banned = false;
	
	
	
	//-------------		Getters & Setters		------------------------------//	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	//------------------------------------------------------------------------//
	
	
	
	/**
	 * Constructor without parameters
	 */
	public Contact () {		
		super();
	}
	

	/**
	 * Complete constructor with two essential properties, contact id and contact name
	 * 
	 * @param contactId
	 * @param contactName
	 */
	public Contact (String contactId, String contactName) {
		
		super();
		this.contactId = contactId;
		this.contactName = contactName;
	}
	
	/**
	 * Function return toString
	 */
	public String toString () {
		
		return "ContactId: " + contactId + "\tContactName: " + contactName + 
			"\tBanned: " + banned; 
	}
	
	

}
