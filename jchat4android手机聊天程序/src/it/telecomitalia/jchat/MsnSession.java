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

import jade.core.AID;
import jade.util.Logger;
import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;


/**
 * Represents a IM conversation between a set of participants.
 * It keeps an unique ID, the list of the participants and the list of 
 * already sent messages.
 * Provides functionalities to add messages to the session and to retrieve the session data and to retrieve 
 * a representation of this session's ID as an URI, to be set as Intent data.
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */
public class MsnSession {

	/** 
	 * Instance of the JADE logger for debugging 
	 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());
	
	/** 
	 * List of ids (phone numbers) of all conversation participants  
	 */
	private List<String> participantIdList;
	
	/** 
	 * The message list. 
	 */
	private ArrayList<MsnSessionMessage> messageList;
	
	
	/** 
	 *	Scheme that shall be used when representing session id as an URI  
	 */
	private static final String SESSION_ID_URI_SCHEME="content";
	
	/** 
	 * SSP that shall be used when representing session id as an URI 
	 */
	private static final String SESSION_ID_URI_SSP="sessionId";
	
	/** 
	 * Prefix used for building notification title 
	 */
	private static final String NOTIFICATION_TITLE ="Conversation ";
	
	
	/** 
	 * Title that will be shown in chat activity when showing this session's contents 
	 */
	private String sessionTitle;
	
	
	/** 
	 * The session id.  
	 */
	private String sessionId;
	
	
	/**
	 * Instantiates a new conversation session.
	 * 
	 * @param sessionId the session id
	 * @param recvIt iterator on the participant list
	 * @param senderPhone the sender phone
	 * @param sessionCounter unique integer assigned to this session 
	 */
	MsnSession(String sessionId, Iterator recvIt, String senderPhone, int sessionCounter){
		this.sessionId = sessionId;
		this.participantIdList = new ArrayList<String>();
		this.messageList = new ArrayList<MsnSessionMessage>();
		fillParticipantList(recvIt, senderPhone);
		//prepare the session title
		StringBuffer buffer= new StringBuffer(NOTIFICATION_TITLE);
		buffer.append(sessionCounter);
		this.sessionTitle= buffer.toString();
	}
	
	/**
	 * Instantiates a new conversation session.
	 * 
	 * @param sessionId the session id
	 * @param participantsIds the list of participants' ids
	 * @param sessionCounter unique integer assigned to this session
	 */
	MsnSession(String sessionId, List<String> participantsIds, int sessionCounter) {
		this.sessionId = sessionId;
		this.participantIdList = participantsIds;
		this.messageList = new ArrayList<MsnSessionMessage>();
		//prepare the session title
		StringBuffer buffer= new StringBuffer(NOTIFICATION_TITLE);
		buffer.append(sessionCounter);
		this.sessionTitle= buffer.toString();
	}
	
	/**
	 * Performs a copy  of a conversation session.
	 * 
	 * @param session the session to copy
	 */
	MsnSession(MsnSession session) {
		this.sessionTitle = session.sessionTitle;
		this.sessionId = session.sessionId;
		this.messageList = new ArrayList<MsnSessionMessage>();
		
		for (MsnSessionMessage msg : session.messageList) {
			this.messageList.add( new MsnSessionMessage(msg));
		}
		
		this.participantIdList = new ArrayList<String>(session.participantIdList);
	}
	 
	
	/**
	 * Fill the list of participants to this conversation (my contact is not included) 
	 * 
	 * @param receiversIt iterator over the list of receivers
	 * @param senderPhoneNum the phone number of the contact that initiates the session
	 */
	private void fillParticipantList(Iterator receiversIt, String senderPhoneNum){
		while( receiversIt.hasNext() ) {
			AID contactAID = (AID) receiversIt.next();
			//In this application the agent local name is the contact phone number
			String contactPhoneNum = contactAID.getLocalName();
			String myPhoneNum = ContactManager.getInstance().getMyContact().getPhoneNumber();
			
			//Check that this is not me
			if (!myPhoneNum.equals(contactPhoneNum)){
				//add as a new participant
				participantIdList.add(contactPhoneNum);
			}
		}
		
		participantIdList.add(senderPhoneNum);
	}
	
	/**
	 * Gets the session id as an URI in the form of "SESSION_ID_URI_SCHEME://SESSION_ID_URI_SSP#sessionId"
	 * Used for putting the sessionId inside Intent as data.
	 * 
	 * @return the session id as an URI
	 */
	public Uri getSessionIdAsUri(){
		return Uri.fromParts(SESSION_ID_URI_SCHEME, SESSION_ID_URI_SSP, sessionId);
	}
	
	
	/**
	 * Overrides Object.equals(). Two sessions are considered equals if and only if 
	 * their session ids are equal
	 * 
	 * @param o object to compare to
	 * @return true if the sessions have same id, false otherwise
	 */
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		
		boolean retval=false;
		
		if (o instanceof MsnSession){
			MsnSession otherSession = (MsnSession) o;
			retval = this.sessionId.equals(otherSession.sessionId);
		}
		
		return retval;
	}

	/**
	 * Adds the message to this session's message list
	 * 
	 * @param msg message to be added
	 */
	public void addMessage(MsnSessionMessage msg){		
			messageList.add(msg);		
	}
	
	/**
	 * Gets the message list.
	 * 
	 * @return the message list
	 */
	public ArrayList<MsnSessionMessage> getMessageList(){
		ArrayList<MsnSessionMessage> list = null;		 
		list = new ArrayList<MsnSessionMessage>(messageList);
		return list;
	}
	
	
	/**
	 * Gets the the list of participants'ids
	 * 
	 * @return the participant ids'list
	 */
	public List<String> getAllParticipantIds(){	
		return participantIdList;
	}
	
	/**
	 * Gets all participant names.
	 * 
	 * @return the list of all participants' names
	 */
	public List<String> getAllParticipantNames(){	
		ArrayList<String> participantNameList = new ArrayList<String>();
		for(String id : participantIdList){
			participantNameList.add(ContactManager.getInstance().getContact(id).getName());
		}
		return participantNameList;
	}
	
	/**
	 * Gets the participant count.
	 * 
	 * @return the participant count
	 */
	public int getParticipantCount(){
		return participantIdList.size()+1;
	}

	/**
	 * Gets the session id.
	 * 
	 * @return the session id
	 */
	public String getSessionId() {			
		return sessionId;
	}

	/**
	 * String representation (the session title) 
	 * 
	 * @return the session title
	 */
	public String toString(){		
		return sessionTitle;	
	}
}