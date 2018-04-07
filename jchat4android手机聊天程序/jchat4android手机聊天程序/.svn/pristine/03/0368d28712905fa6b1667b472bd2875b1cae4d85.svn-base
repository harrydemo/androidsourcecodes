/*****************************************************************
 jChat is a  chat application for Android based on JADE
  Copyright (C) 2008 Telecomitalia S.p.A. 
 
 GNU Lesser General Public License

 This is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this software; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

package it.telecomitalia.jchat;


/**
 * This class represents a generic contact that can be online or offline.
 * Each contact has a name and a mandatory phone number. Please note that the Agent shall be identified with the phone number.
 * It seems that the emulator does not support a way to customize the phone number. So for now the application reads the number
 * from a property file.
 * Each contact has non null location:  online contacts have a location that is updated
 * while offline contacts have a fixed location (null location)(they should not be drawn).
 * <p>
 * This class is almost immutable. The <code>isOnline</code> flag is the only mutable field
 *  
 * @author Cristina Cuccè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */

public class Contact  {

	/** 
	 * The contact phone number. 
	 */
	private final String phoneNumber;	
	
	/** 
	 * Status of the contact. 
	 * This flag is mutable 
	 */
	private volatile boolean isOnline;
	
	/** 
	 * The contact name. It is the one on the local phone contacts if 
	 * the contact is known, the phone number otherwise. 
	 */
	private final String name; 
	
	/** 
	 * True if the contact is present in local phone contacts, false otherwise. 
	 */
	private final boolean storedOnPhone;
	
		
	/**
	 * Instantiates a new contact.
	 * 
	 * @param name Contact name
	 * @param phoneNumber contact phone number
	 * @param stored true if contact is stored on phone, false otherwise
	 */
	public Contact(String name, String phoneNumber, boolean stored){
		this.name = name;
		this.phoneNumber= phoneNumber;
		isOnline = false;	
		storedOnPhone = stored;
	}
	
	/**
	 * Performs a deep copy of a contact.
	 * 
	 * @param c the contact to be copied
	 */
	public Contact(Contact c){
		this.name = c.name;
		this.phoneNumber = c.phoneNumber;
		this.isOnline = c.isOnline;
		this.storedOnPhone = c.storedOnPhone;
	}
	
	/**
	 * Gets the contact phone number.
	 * 
	 * @return the phone number
	 */
	public  String getPhoneNumber(){
		return phoneNumber;
	}
	

	/**
	 * Gets the contact name.
	 * 
	 * @return the contact name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Checks if a contact is stored on phone.
	 * 
	 * @return true if it is stored on phone, false otherwise
	 */
	public boolean isStoredOnPhone(){
		return storedOnPhone;
	}

	/**
	 * Prints out the contact name
	 * 
	 *  @return the contact name
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Change contact status to online.
	 */
	public void setOnline(){
		isOnline = true;
	}
	
	/**
	 * Change contact status to offline.
	 */
	public void setOffline(){
		isOnline = false;;
	}
	
	/**
	 * Checks if contact is online.
	 * 
	 * @return true if online, false otherwise
	 */
	public boolean isOnline(){
		return isOnline;
	}

	
	
	/**
	 * Checks for contacts equality
	 * 
	 *  @param o the object to be checked
	 *  @return true if contacts have the same phone number, false otherwise
	 */
	public boolean equals(Object o) {
		
		boolean res= false;
		
		if (o instanceof Contact) {
			Contact other = (Contact) o;					
			res= phoneNumber.equals(other.phoneNumber);	
		}
		
		return res;
	}	
}
