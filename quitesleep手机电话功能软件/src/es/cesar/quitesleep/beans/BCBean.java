/* 
 	Copyright 2011 Cesar Valiente Gordo
 
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


package es.cesar.quitesleep.beans;

import es.cesar.quitesleep.ddbb.Contact;

/**
 * Bean for include data regarding contact used and if is blocked or not.
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class BCBean {

	
	private boolean isBlocked = false;
	private Contact usedContact = null;
	
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public Contact getUsedContact() {
		return usedContact;
	}
	public void setUsedContact(Contact usedContact) {
		this.usedContact = usedContact;
	}
	
	/**
	 * Empty constructor
	 */
	public BCBean () {
		
	}
	
	/**
	 * Constructor with all parameters.
	 * 
	 * @param isBlocked
	 * @param usedContact
	 */
	public BCBean (boolean isBlocked, Contact usedContact) {		
		this.isBlocked = isBlocked;
		this.usedContact = usedContact;
	}
	
	@Override
	public String toString () {
		
		return "UsedContact: " + usedContact + "\tisBlocked: " + isBlocked;
	}
	
}
