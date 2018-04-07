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

package es.cesar.quitesleep.staticValues;

import java.util.concurrent.Semaphore;

import es.cesar.quitesleep.ddbb.BlockCallsConf;

import android.content.Context;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ConfigAppValues {
	
	//-------------		FINAL VALUES (Constants)	--------------------------//
	
	public static final String CONTACT_NAME = "CONTACT_NAME";
	
	//---------------	req Codes for return activities		------------------//
	public static final int REQCODE_ADD_BANNED			=	1;
	public static final int REQCODE_DELETE_BANNED		=	2;
	public static final int REQCODE_SMS_SETTINGS 		=	3;
	public static final int REQCODE_MAIL_SETTINGS 		= 	4;
	public static final int REQCODE_SYNC_CONTACTS		=	5;
	public static final int REQCODE_CONTACT_DETAILS 	=	6;
	public static final int REQCODE_EDIT_CONTACT 		=	7;
	public static final int REQCODE_BLOCK_OTHER_CALLS 	=	8;
	public static final int reqTest						=	100;
	//------------------------------------------------------------------------//
	
	//------------	Codes for dialog actions	------------------------------//
	public static final int WARNING_SYNC_CONTACTS 			=		1;
	public static final int WARNING_ADD_ALL_CONTACTS		=		2;
	public static final int WARNING_REMOVE_ALL_CONTACTS		=		3;
	public static final int WARNING_SMS_ACTION				=		4;
	public static final int WARNING_MAIL_ACTION				=		5;
	public static final int WARNING_REMOVE_ALL_CALL_LOGS 	=		6;
	public static final int WARNING_REFRESH_CALL_LOG		=		7;
	public static final int WARNING_FIRST_TIME				=		8;
	//------------------------------------------------------------------------//
	
	//----------		Codes for informaton through intents		----------//
	public static final String NUM_REMOVE_CONTACTS = "NUM_REMOVE_CONTACTS";
	public static final String NUM_REMOVE_CALL_LOGS = "NUM_REMOVE_CALL_LOGS";
	public static final String REFRESH_CALL_LOG	= "REFRESH_CALL_LOG";
		
	
	//Min Api level used in this app
	private static int minApiLevel = 1;
	
	//General context used in this app
	private static Context context = null;
	
	/* Used for check without to need to use the ddbb if the service 
	 * QuiteSleep must be running or not.
	 */	
	private static Boolean quiteSleepServiceState 		= null;
	private static Boolean mailServiceState 			= null;
	private static Boolean smsServiceState 				= null;
	private static BlockCallsConf blockCallsConf 		= null;		
	private static Boolean muteOrHangup 				= null;	//mute = false, hangup = true	
																	
	
	//Constant string for get the incomingCallNumber
	public static String INCOMING_CALL_NUMBER = "INCOMING_CALL_NUMBER";
	
			
	//For use as semaphores in a producer-consumer way
	public static boolean processRingCall = false;
	public static boolean processIdleCall = false;
	
	//Variables used for the system's cache
	
	
	//----------	Getters & Setters	-----------------------------------//
	public static int getMinApiLevel() {
		return minApiLevel;
	}
	public static void setMinApiLevel(int minApiLevel) {
		ConfigAppValues.minApiLevel = minApiLevel;
	}
	
	public static Context getContext () {
		return context;
	}	
	public static void setContext (Context context) {
		ConfigAppValues.context = context;
	}

	public static Boolean getQuiteSleepServiceState() {
		return quiteSleepServiceState;
	}
	public static void setQuiteSleepServiceState(Boolean quiteSleepServiceState) {
		ConfigAppValues.quiteSleepServiceState = quiteSleepServiceState;
	}
	
	public static Boolean getMailServiceState() {
		return mailServiceState;
	}
	public static void setMailServiceState(Boolean mailServiceState) {
		ConfigAppValues.mailServiceState = mailServiceState;
	}
	
	public static Boolean getSmsServiceState() {
		return smsServiceState;
	}
	public static void setSmsServiceState(Boolean smsServiceState) {
		ConfigAppValues.smsServiceState = smsServiceState;
	}					
	
	public static BlockCallsConf getBlockCallsConf () {
		return blockCallsConf;
	}
	
	public static void setBlockCallsConf (BlockCallsConf blockCallsConf) {
		ConfigAppValues.blockCallsConf = blockCallsConf;
	}
	
	public static Boolean getMuteOrHangup () {
		return muteOrHangup;
	}
	
	public static void setMuteOrHangup (boolean muteOrHangup) {
		ConfigAppValues.muteOrHangup = muteOrHangup;
	}
	//------------------------------------------------------------------------//
	
	
	

}
