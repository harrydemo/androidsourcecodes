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
public class CallLog extends Id {
	
	private int numOrder;
	private Contact contact;
	private String phoneNumber;
	private boolean sendSms = false;
	private int numSendMail;	
	private String timeCall;	
		
	
	//------------------------------------------------------------------------//
	public void setNumOrder(int numOrder) {
		this.numOrder = numOrder;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public Contact getContact() {
		return contact;
	}
	public int getNumOrder() {
		return numOrder;
	}	

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	public boolean isSendSms() {
		return sendSms;
	}
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}
	
	
	public int getNumSendMail() {
		return numSendMail;
	}
	public void setNumSendMail(int numSendMail) {
		this.numSendMail = numSendMail;
	}

	public String getTimeCall() {
		return timeCall;
	}
	public void setTimeCall(String timeCall) {
		this.timeCall = timeCall;
	}
	
	//------------------------------------------------------------------------//



	/**
	 * Constructor without parameters
	 */
	public CallLog () {
		super();
	}
	
	
	/**
	 * Constructor with the first paramter to get usually
	 * 
	 * @param phone
	 */
	public CallLog (String phone) {
		super();
		this.phoneNumber = phone;
	}
	
	
	/**
	 * Constructor with all parameters
	 * 
	 * @param contact
	 * @param phone
	 * @param numSendSms
	 * @param sendMail
	 * @param timeCall
	 * @param timeCallLong
	 */
	public CallLog (
			Contact contact, 
			String phone, 
			boolean sendSms, 
			int numSendMail, 
			String timeCall) {
		
		super();
		
		this.contact = contact;
		this.phoneNumber = phone;
		this.sendSms = sendSms;
		this.numSendMail = numSendMail;
		this.timeCall = timeCall;			
	}
	
	@Override	
	public String toString () {
		
		if (contact != null)
			return contact.getContactName() + "\t(" + phoneNumber + ")" + "\n" + 
				timeCall + "\t\tSms: " + (sendSms ? 1:0) + "\tEmail: " + numSendMail;
		else
			return "Unknown" + "\t(" + phoneNumber + ")" + "\n" + 
			timeCall + "\t\tSms: " + (sendSms ? 1:0) + "\tEmail: " + numSendMail;
	}
	
	
		

}
