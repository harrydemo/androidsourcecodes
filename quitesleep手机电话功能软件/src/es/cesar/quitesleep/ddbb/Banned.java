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
public class Banned extends Id {
	
	private Contact contact;
	private Schedule schedule;
	
	//--------	Getters & Setters 	---------------//
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	//----------------------------------------------//
	
	/**
	 * Constructor without parameters
	 */
	public Banned () {
		super();	
	}
	
	/**
	 * Contacts with all parameters
	 * 
	 * @param contact
	 * @param schedule
	 */
	public Banned (Contact contact, Schedule schedule) {
		
		super();
		this.contact = contact;
		this.schedule = schedule;
	}
	
	@Override
	public String toString () {
		
		return "Contact: " + contact + "\nSchedule: " + schedule;
	}
	

}
