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

import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Provides conversation sessions management functionalities.
 * In particular, it allows the creation of a new conversation session and the removal of an existing
 * one, retrieving existing sessions, adding messages to a session and so on.
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0
 */
public class MsnSessionManager {
	
	/** 
	 * The instance of the session manager 
	 */
	private static MsnSessionManager instance = new MsnSessionManager(); 
	
	/** 
	 * The session map. Each session is stored using its ID as a map key. 
	 */
	private Map<String,MsnSession> sessionMap; 
	
	
	
	/** 
	 * Max number of session 
	 */
	public static final int MAX_MSN_SESSION_NUMBER =10; 
	

	private MsnSessionManager(){
		sessionMap = new HashMap<String, MsnSession>(MAX_MSN_SESSION_NUMBER);
	}
	
	/**
	 * Gets the instance of MsnSessionManager.
	 * 
	 * @return the instance of MsnSessionManager
	 */
	public static MsnSessionManager getInstance(){
		return instance;
	}
	
	
	
	
	/**
	 * Called when the user initiates a session with other contacts, creates a new session generating the session id 
	 * from the participants id list.
	 * The session is then added to the session map and its session Id is returned. 
	 * 
	 * @param participantIds the list of participants ids (phone numbers)
	 * @return String that represents the session Id for the newly created session
	 */
	public String startMsnSession(List<String> participantIds ){
		String sessionIdAsString = getSessionIdFromParticipants(participantIds);
		addMsnSession(sessionIdAsString, participantIds);
		return sessionIdAsString;
	}
	
	
	/**
	 * Gets the session id from participants id list.
	 * Session id is computed by hashing each participant phone number and then by exoring them all together.
	 * This should create a fairly unique values that depends only from the participants ids and not by the order
	 * in which they appear.  
	 * 
	 * @param participantIds the list of participant phone numbers	 
	 * 
	 * @return the session id as a string
	 */
	public String getSessionIdFromParticipants(List<String> participantIds){		
		String myAgentId =	ContactManager.getInstance().getMyContact().getPhoneNumber();
		//The session id is computed by hashing agentNames
		int sessionIdAsInt= myAgentId.hashCode();
		
		for (String participantId : participantIds) {
			int tmp = participantId.hashCode();
			sessionIdAsInt ^= tmp;
		}
		
		String sessionIdAsStr = String.valueOf(sessionIdAsInt);
		return sessionIdAsStr;
	}
	

	/**
	 * Adds a new msn session initiated by another contact (the other contact start the communication)
	 * 
	 * 
	 * @param sessionId the session id
	 * @param participantIds the list of all participants'ids
	 */
	public synchronized void addMsnSession(String sessionId, List<String> participantIds){
		if (!sessionMap.containsKey(sessionId)){
			int sessionCounter= sessionMap.size()+1;
			MsnSession session = new MsnSession(sessionId, participantIds,sessionCounter); 
			//register it
			sessionMap.put(sessionId, session);	
		}
	}
	
	
	/**
	 * Adds a new conversation session initiated by another contact (the other contact start the communication)
	 * 
	 * @param sessionId the session id
	 * @param recvIt the iterator over the receiver list
	 * @param senderPhone the phone number of the contact that initiates the session 
	 */
	public synchronized void addMsnSession(String sessionId, Iterator recvIt, String senderPhone){
		if (!sessionMap.containsKey(sessionId)){
			int sessionCounter= sessionMap.size()+1;
			MsnSession session = new MsnSession(sessionId, recvIt, senderPhone, sessionCounter); 
			//register it
			sessionMap.put(sessionId, session);	
		}
	}
	
	
	/**
	 * Removes an active session given its ID.
	 * 
	 * @param msnSession the session id
	 */
	public synchronized void removeMsnSession(String msnSession){		
			sessionMap.remove(msnSession);		
	}
	
	

	/**
	 * Retrieve a copy of a session, given its id.
	 * 
	 * @param sessionId id of the session we would like to retrieve
	 * 
	 * @return the copy of the desired session
	 */
	public MsnSession retrieveSession(String sessionId){		
	
		MsnSession session=null;
		MsnSession copyOfSession = null;
		
		synchronized (this) {
			session = sessionMap.get(sessionId);
			if (session != null)
				copyOfSession = new MsnSession(session);
		}
		
		return copyOfSession;
			
	}
	
	/**
	 * Adds the message to a given session in a thread safe way.
	 * 
	 * @param sessionId the session id of the session
	 * @param msg the message that should be added
	 */
	public synchronized void addMessageToSession(String sessionId, MsnSessionMessage msg){
		MsnSession session = sessionMap.get(sessionId);
		
		if (session != null){
			session.addMessage(msg);
		}
	}
	
	

	/**
	 * Performs some cleanup
	 */
	public synchronized void shutdown(){
		sessionMap.clear();
	}

	
	/**
	 * Retrieves the list of participants to all active session in a given moment ( useful for changing map icon to contacts 
	 * that are in an active session)
	 * 
	 * @return the list of ids of all contacts participating to a session.
	 */
	public synchronized Set<String> getAllParticipantIds() {
		//define a set to avoid duplicates
		Set<String> idSet = new HashSet<String>();
		ArrayList<MsnSession> c = null;
		
		synchronized(this){
			c= new ArrayList<MsnSession>( sessionMap.values()); 
		}
		
		for (MsnSession session : c) {
			List<String> partIds = session.getAllParticipantIds();
			idSet.addAll(partIds);
		}
		
		return idSet;
	}
	
	
}
   
