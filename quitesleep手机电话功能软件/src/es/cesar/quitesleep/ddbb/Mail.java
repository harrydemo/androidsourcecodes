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
public class Mail extends Id {
	
	private Contact contact;
	private String contactMail;
	private boolean usedToSend;
	
	
	//------	Getters & Setters	---------------//
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getContactMail() {
		return contactMail;
	}
	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}
	public boolean isUsedToSend() {
		return usedToSend;
	}
	public void setUsedToSend(boolean usedToSend) {
		this.usedToSend = usedToSend;
	}
	//------------------------------------------------//
	
	/**
	 * Constructor without parameters
	 */
	public Mail () {		
		super();
	}
	
	/**
	 * Constructor with basic parameters
	 * @param contact
	 * @param contactMail
	 */
	public Mail (Contact contact, String contactMail) {
		
		super();
		this.contact = contact;
		this.contactMail = contactMail;		
	}
	
	/**
	 * Constructor with all parameters
	 * @param contact
	 * @param contactMail
	 * @param usedToSend
	 */
	public Mail (Contact contact, String contactMail, boolean usedToSend) {
		
		super();
		this.contact = contact;
		this.contactMail = contactMail;
		this.usedToSend = usedToSend;		
	}

}
