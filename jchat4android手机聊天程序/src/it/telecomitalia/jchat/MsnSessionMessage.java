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



import jade.lang.acl.ACLMessage;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class contains data related to a single IM message
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0
 */

public class MsnSessionMessage {
	
	/** 
	 * The time this message was received in milliseconds
	 */
	private long time;	
	
	/** 
	 * The message content. 
	 */
	private String messageContent;
	
	/** 
	 * The message sender name. 
	 */
	private String messageSenderName;
	
	/** 
	 * The message sender phone number. 
	 */
	private String senderPhoneNum;
	
	/**
	 * Instantiates a new  session message.
	 * 
	 * @param message the message content as a string
	 * @param senderName the sender name
	 * @param senderTel the sender telephone number
	 */
	public MsnSessionMessage(String message, String senderName, String senderTel){
		this(message,senderName,senderTel, System.currentTimeMillis());
	}
	
	/**
	 * Instantiates a new  session message.
	 * 
	 * @param msg the message as an ACLMessage
	 */
	public MsnSessionMessage(ACLMessage msg){
		this.senderPhoneNum = msg.getSender().getLocalName();
		this.messageSenderName = ContactManager.getInstance().getContact(senderPhoneNum).getName();
		this.messageContent = msg.getContent();
		this.time = System.currentTimeMillis();
	}
	
	/**
	 * Copies a  session message.
	 * 
	 * @param message the message to be copied
	 */
	public MsnSessionMessage(MsnSessionMessage message){
		this.senderPhoneNum = new String(message.senderPhoneNum);
		this.messageSenderName = new String(message.messageSenderName);
		this.messageContent = new String(message.messageContent);
		this.time = message.time;
	}
	
	//
	/**
	 * Instantiates a new  session message.
	 * 
	 * @param message the message content as a string
	 * @param senderName the message sender name
	 * @param senderTel the message sender telephone
	 * @param timestamp the time in milliseconds the message arrives at
	 */
	public MsnSessionMessage(String message, String senderName, String senderTel, long timestamp){
		time = timestamp;
		messageContent = message;
		messageSenderName = senderName;
		senderPhoneNum = senderTel;
	}

	/**
	 * Gets the sender phone number.
	 * 
	 * @return the sender phone number
	 */
	public String getSenderNumTel(){
		return senderPhoneNum;
	}
	
	/**
	 * Gets the time this message arrives at
	 * 
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the message content.
	 * 
	 * @return the message content
	 */
	public String getMessageContent() {
		return messageContent;
	}
	
	/**
	 * Gets the sender name.
	 * 
	 * @return the sender name
	 */
	public String getSenderName(){
		return messageSenderName;
	}

	/**
	 * Overrides Object.equals(). Two messages are considered equals if they have same senderName and content
	 * 
	 * @param o object to be compared
	 * @return true if equals, false otherwise
	 */

	public boolean equals(Object o) {
		if ( !(o instanceof MsnSessionMessage) ) {
			return false;
		}
		MsnSessionMessage msg = (MsnSessionMessage) o;
		return (msg.messageContent.equals(messageContent) && msg.messageSenderName.equals(messageSenderName));
	} 
	
	/**
	 * Gets the time this message was received as a string.
	 * 
	 * @return the time received as string
	 */
	public String getTimeReceivedAsString(){
		Calendar calend = new GregorianCalendar();
		calend.setTimeInMillis(time);
		int hour = calend.get(Calendar.HOUR_OF_DAY);
		int minutes = calend.get(Calendar.MINUTE);
		return pad(hour) + ":" + pad(minutes);
		
	}

	/**
	 * Formats date and time correctly
	 * 
	 * @return the time received as string
	 */
	private String pad(int c) {
		return (c<10)?"0"+String.valueOf(c):String.valueOf(c);
	}
	
	/**
	 * Overrides Object.toString(). Provides a string representation for a message
	 * 
	 * @return String representation of the message
	 */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("At ");
		buffer.append(getTimeReceivedAsString());
		buffer.append(" ");
		buffer.append(messageSenderName);
		buffer.append(" says: \n");
		buffer.append(messageContent);
		buffer.append("\n\n");
		return buffer.toString();
	}
}
