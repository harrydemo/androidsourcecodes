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

import jade.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an event. An event is basically a container object that carries
 * arbitrary data and has an unique name.
 * It is filled by the entity that fires the event.
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0
 *
 */
public class MsnEvent {
	
		private Logger myLogger = Logger.getMyLogger(MsnEvent.class.getName());
		
		/**
		 * Event that is fired each time an update should be performed
		 */
		public static  final String TICK_EVENT="TICK_EVENT";
		/**
		 * Event that is fired when a new message arrives
		 */
		public static  final String INCOMING_MESSAGE_EVENT="INCOMING_MESSAGE_EVENT";
		/**
		 * Event that is fired when a refresh of the view is needed ()
		 */
		public static final String VIEW_REFRESH_EVENT="VIEW_REFRESH_EVENT";
		/**
		 * Event that is fired when a contact disconnects
		 */
		public static final String CONTACT_DISCONNECT_EVENT="CONTACT_DISCONNECT_EVENT";
		
		
		//Parameters defined for INCOMING MSG EVENT
		/**
		 * Name for the incoming message parameter 
		 */
		public static final String INCOMING_MESSAGE_PARAM_MSG="INCOMING_MESSAGE_PARAM_MSG";
		/**
		 * Name for the session id parameter for an incoming message  
		 */
		public static final String INCOMING_MESSAGE_PARAM_SESSIONID="INCOMING_MESSAGE_PARAM_SESSIONID";
		
		//Parameters defined for VIEW REFRESH EVENT
		/**
		 * Name for the list of changes parameter at each refresh 
		 */
		public static final String VIEW_REFRESH_PARAM_LISTOFCHANGES="VIEW_REFRESH_PARAM_LISTOFCHANGES";
		/**
		 * Name of contacts map parameter at each refresh
		 */
		public static final String VIEW_REFRESH_CONTACTSMAP="VIEW_REFRESH_CONTACTSMAP";
		/**
		 * Name of location map parameter at each refresh
		 */
		public static final String VIEW_REFRESH_PARAM_LOCATIONMAP="VIEW_REFRESH_PARAM_LOCATIONMAP";
		
		//Parameters defined for CONTACT DISCONNECT EVENT
		/**
		 * Name of parameter 
		 */
		public static final String CONTACT_DISCONNECT_PARAM_CONTACTNAME="CONTACT_DISCONNECT_PARAM_CONTACTNAME";
		
		/**
		 * Name of the event
		 */
		private  String name;
		
		/**
		 * Maps that stores event parameters
		 */
		private Map<String, Object> paramsMap;
		
		/**
		 * Returns the name of the event
		 * @return event name
		 */
		public final String getName() {
			return name;
			
		}
		
		/**
		 * Builds a new event
		 * @param name name of the event
		 */
		public MsnEvent(String name){
			this.name = name;
		}
		
		
		/**
		 * Adds a parameter to the event using the given name
		 * 
		 * @param name name of the parameter
		 * @param value value to be added
		 */
		public void addParam(String name, Object value){
			if (paramsMap == null){
				paramsMap = new HashMap<String, Object>();
			}
			
			paramsMap.put(name, value);
			myLogger.log(Logger.FINE,"putting in event map parameter " + name +" having value "+ value.toString() );
		}
		
		/**
		 * Retrieves a parameter from an event
		 * 
		 * @param name of the parameter to retrieve
		 * @return value of the parameter
		 */
		public Object getParam(String name){
			return paramsMap.get(name);
		}
}


