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


import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.util.leap.Properties;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;



/**
 * Represents the activity that allows sending and receiving messages to other contacts.
 * It is launched when a user clicks a notification on the status bar (for reading a message
 * sent by a contact) or when starts a conversation himself. 
 * <p>
 * Please note that only a single activity is used also for managing multiple conversation at a time,
 * that is the user always sees a single activity also when he switches from one to another: activity is
 * simply redrawn.
 * <p>
 * Implements the ConnectionListener interface to be able to connect to the MicroRuntime service for communication 
 * with agent.  
 *  
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0    
 */
public class ChatActivity extends Activity implements ConnectionListener{

	/** Instance of Jade Logger, for debugging purpose. */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());
	
	/** ListView showing participants to this chat session. */
	private ListView partsList;
	
	/** Button for sending data. */
	private ImageButton sendButton;	
	
	/** Button for closing this activity and session. */
	private ImageButton closeButton;
	
	/** List of already sent messages. */
	private ListView messagesSentList;
	
	/** Edit text for editing the message that must be sent. */
	private EditText messageToBeSent;
	
	/** Instance of jade gateway necessary to work with Jade add-on. */
	private JadeGateway gateway;
	
	/** Id of the session this activity is related to*/
	private String sessionId;
	
	/** Adapter used to fill up the message list */
	private MsnSessionAdapter sessionAdapter;
	
	private ChatActivityHandler activityHandler;
	
	
	/**
	 * Retrieves the id of the chat session this activity refers to.
	 * 
	 * @return Id of the session
	 */
	public String getMsnSession(){
		return sessionId;
	}
	
	/**
	 * Initializes basic GUI components and listeners. Also performs connection to add-on's Jade Gateway.
	 *  
	 * @param icicle Bundle of data if we are resuming a frozen state (not used)
	 */
	protected void onCreate(Bundle icicle) {
		Thread.currentThread().getId();
        myLogger.log(Logger.FINE, "onReceiveIntent called: My currentThread has this ID: " + Thread.currentThread().getId());
		super.onCreate(icicle);
	    requestWindowFeature(Window.FEATURE_LEFT_ICON); 
	    setContentView(R.layout.chat);
	    setFeatureDrawable(Window.FEATURE_LEFT_ICON, getResources().getDrawable(R.drawable.chat));		
		myLogger.log(Logger.FINE, "onCreate called ...");
		sessionAdapter = new MsnSessionAdapter(getWindow().getLayoutInflater(), getResources());	
		sendButton = (ImageButton) findViewById(R.id.sendBtn);		
		sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	String msgContent = messageToBeSent.getText().toString().trim();
            	myLogger.log(Logger.FINE,"onClick(): send message:" + msgContent);
            	if(msgContent.length()>0){
            		sendMessageToParticipants(msgContent);
            		}
            	messageToBeSent.setText("");
            }
        });		
		//retrieve the list
		partsList = (ListView) findViewById(R.id.partsList);		
		messageToBeSent = (EditText)findViewById(R.id.edit);
		messagesSentList = (ListView) findViewById(R.id.messagesListView);
		
		closeButton = (ImageButton) findViewById(R.id.closeBtn);
		closeButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				ChatSessionNotificationManager.getInstance().removeSessionNotification(sessionId);
				MsnSessionManager.getInstance().removeMsnSession(sessionId);
				finish();
			}
		});
		
		activityHandler = new ChatActivityHandler();
		
		//fill Jade connection properties
        Properties jadeProperties = getJadeProperties();
        
        //try to get a JadeGateway
        try {
			JadeGateway.connect(MsnAgent.class.getName(), jadeProperties, this, this);
		} catch (Exception e) {
			//troubles during connection
			Toast.makeText(this, 
						   getString(R.string.error_msg_jadegw_connection), 
						   Integer.parseInt(getString(R.string.toast_duration))
						   ).show();
			myLogger.log(Logger.SEVERE, "Error in chatActivity", e);
			e.printStackTrace();
		}
	}
	
	private Properties getJadeProperties() {
		//fill Jade connection properties
		Properties jadeProperties = new Properties();
		JChatApplication app = (JChatApplication)getApplication();
		jadeProperties.setProperty(Profile.MAIN_HOST, app.getProperty(JChatApplication.JADE_DEFAULT_HOST));
		jadeProperties.setProperty(Profile.MAIN_PORT, app.getProperty(JChatApplication.JADE_DEFAULT_PORT));
		jadeProperties.setProperty(JICPProtocol.MSISDN_KEY, app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));
		return jadeProperties;
	}
	
	/**
	 * Populates the GUI retrieving the sessionId from the intent that initiates the activity itself.
	 * The session Id is saved in the intent as an URI, whose fragment is the part we are interested in.
	 * <p>
	 * Please note that this method shall be called both when the activity is created for the first time and 
	 * when it is resumed from the background (that is, when it is in the foreground and the user switches to a new session
	 * by clicking the status bar notifications)
	 */
	@Override
	protected void onResume() {
		myLogger.log(Logger.FINE, "onResume() was called!" );
		Intent i = getIntent();
		Uri sessionIdUri = i.getData();
		sessionId = sessionIdUri.getFragment();
		
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(sessionId);
		setTitle(session.toString());
		List<String> participants = session.getAllParticipantNames();
		ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this,R.layout.participant_layout, R.id.participantName, participants);
		partsList.setAdapter(aAdapter);
		ChatSessionNotificationManager.getInstance().addNewSessionNotification(sessionId);
		messageToBeSent.setText("");
		
		//Retrieve messages if the session already contains data
		sessionAdapter.setNewSession(session);
		messagesSentList.setAdapter(sessionAdapter);
		MsnEventMgr.getInstance().registerEvent(MsnEvent.INCOMING_MESSAGE_EVENT, activityHandler);
		
		super.onResume();
	}
	
	/**
	 * Called only when resuming  an activity by clicking the status bar, just before <code> onResume() </code>
	 * <p>
	 * Sets the retrieved intent (containing info about the new session selected by the user) as the current one, to make 
	 * <code> onResume() </code> able to populate the GUI with the new data.
	 * 
	 * @param intent the intent launched when clicking on status bar notification (no new activity is created but the new intent is passed anyway)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		myLogger.log(Logger.FINE, "onNewIntent was called!! \n Intent received was: " + intent.toString());
		setIntent(intent);
		super.onNewIntent(intent);
	}

	

	/**
	 * Called only when destroying  the chat activity when closing the chat window (both when clicking the close button or when going back 
	 * in activity stack with the back arrow).
	 * <p>
	 * It basically performs a disconnection from the service, sends the closing message to the main activity and resets the ChatActivityUpdater
	 * to null (so the agent is aware that the chat activity is not visible). 
	 * 
	 * @param intent the intent launched when clicking on status bar notification (no new activity is created but the new intent is passed anyway)
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();		
				
		if (gateway != null){
			gateway.disconnect(this);
			myLogger.log(Logger.INFO, "ChatActivity.onDestroy() : disconnected from MicroRuntimeService");
		}		
		
	}
	
	/**
	 * Gets the instance to the add-on's JadeGateway to be able to send messages to be sent to the 
	 * Jade agent. It's a callback, called after the connection to add-on's <code>MicroRuntimeService</code>
	 * 
	 * @param gw Instance of the JadeGateway retrieved after the connection
	 * @see ConnectionListener
	 */
	public void onConnected(JadeGateway gw) {
		this.gateway = gw;
		myLogger.log(Logger.INFO, "onConnected(): SUCCESS!");
	}
	
	/**
	 * Dummy implementation for the ConnectionListener's onDisconnected
	 * 
	 * @see ConnectionListener
	 */
	public void onDisconnected() {
		}
	
	/**
	 * Sends a message to all participants of this session.
	 * <p>
	 * Instantiates a new SenderBehaviour object and sends it to the agent, together with message contents and receiver list, 
	 * then updates the message ListView.  
	 * 
	 * @param msgContent content of the message to be sent 
	 */
	private void sendMessageToParticipants(String msgContent){
		//set all participants as receivers
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(sessionId);
		List<String> receivers = session.getAllParticipantIds();
		
		try{
			gateway.execute(new SenderBehaviour(session.getSessionId(), msgContent, receivers));
			Contact myContact = ContactManager.getInstance().getMyContact();
    		MsnSessionMessage message = new MsnSessionMessage(msgContent,myContact.getName(),myContact.getPhoneNumber());
    		MsnSessionManager.getInstance().addMessageToSession(session.getSessionId(), message);
    		//Add a new view to the adapter
    		sessionAdapter.addMessageView(message);
    		//refresh the list
    		messagesSentList.setAdapter(sessionAdapter);
		}catch(Exception e){
			myLogger.log(Logger.WARNING, e.getMessage());
		}
	}
	
	/**
	 * Contains the actual code executed by the agent to send the message.
	 */
	private class SenderBehaviour extends OneShotBehaviour {

		/** ACLMessage to be sent */
		private ACLMessage theMsg;
		
		/**
		 * Instantiates a new sender behaviour. Fills up the ACLMessage with data provided.
		 * 
		 * @param convId the conv id
		 * @param content the content
		 * @param receivers the receivers
		 */
		public SenderBehaviour(String convId, String content, List<String> receivers) {
			theMsg = new ACLMessage(ACLMessage.INFORM);
			theMsg.setContent(content);
			theMsg.setOntology(MsnAgent.CHAT_ONTOLOGY);
			theMsg.setConversationId(convId);
			
			for(int i=0; i<receivers.size(); i++){
				String cId = receivers.get(i);
				theMsg.addReceiver(new AID(cId, AID.ISLOCALNAME));
			}
			
		}
		
		/**
		 * Sends the message. Executed by JADE agent.
		 */
		public void action() {
			myLogger.log(Logger.FINE, "Sending msg " +  theMsg.toString());
			myAgent.send(theMsg);
		}
	}
	
	
	/**
	 * Defines an handler for UI events.
	 * 
	 */
	private class ChatActivityHandler extends GuiEventHandler {
		
		/**
		 * Performs the update of the GUI. 
		 * It handles the arrival of a new message.
		 * <p>
		 * Two cases are possible:
		 * <ol>
		 * 	<li> incoming message is related to the current session and should be added to message list
		 * 	<li> incoming message is related to another session and a notification is to be shown
		 * </ol>
		 *  
		 * @param event the event that shall be notified to this listener to be handled 		 
		 */
		protected void processEvent(MsnEvent event) {
			
			String eventName = event.getName();
			
			//Handle case of new message
			if (eventName.equals(MsnEvent.INCOMING_MESSAGE_EVENT)){
				MsnSessionMessage msnMsg = (MsnSessionMessage) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_MSG);
				String sessionId = (String) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_SESSIONID);
				
				//check if the message is related to the same session we are currently in.
				//If so, add a new message to session udapter and update it
				if (sessionId.equals(ChatActivity.this.sessionId)){
					sessionAdapter.addMessageView(msnMsg);
					messagesSentList.setAdapter(sessionAdapter);
				} else {
					//if the incoming msg is not for our session, post a notification
					ChatSessionNotificationManager.getInstance().addNewMsgNotification(sessionId, msnMsg);
					Toast.makeText(ChatActivity.this, msnMsg.getSenderName() + " says: " + msnMsg.getMessageContent(), 3000).show();
				}
			} 
		}
		
	}
}
