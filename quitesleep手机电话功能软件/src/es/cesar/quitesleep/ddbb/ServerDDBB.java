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


import android.util.Log;

import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;

import es.cesar.quitesleep.interfaces.IDDBB;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ServerDDBB implements IDDBB {
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.ddbb.ServerDDBB";			
	
	/**
	 * This object is for get only one Singleton object and create ONLY inside
	 * of this class mode.
	 */
	private static ServerDDBB SINGLETON = null;	
	
	/**
	 * This object is the server DDBB file properly said
	 */
	private static ObjectServer server = null;
	
	
	/**
	 * Constructor for the ServerDDBB		
	 */
	private ServerDDBB () {
		
		try
		{														
			if (QSLog.DEBUG_I)QSLog.i(CLASS_NAME, "Before to open the DDBB");
			
			
			ServerConfiguration configuration = 
				Db4oClientServer.newServerConfiguration();						
			
			configuration.common().allowVersionUpdates(true);
			configuration.common().activationDepth(DEEP);
																											
			server = Db4oClientServer.openServer(
					configuration, getDDBBFile(DDBB_FILE), 0);												
			
			if (server == null)
				if (QSLog.DEBUG_W)QSLog.w(CLASS_NAME, "Server Null!!!!");
						
																							
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
		
	
	/**
	 * Create the server instance if the server doesn't create before
	 */
	private synchronized static void createInstance () {
				
		if (server == null) 
			SINGLETON = new ServerDDBB();				
	}
	
	/**
	 * Get the server DDBB instance, if the singleton object doesn't create
	 * before, we create for use it.
	 * 
	 * @return		The server object
	 * @see			ObjectsServer
	 */
	public static ObjectServer getServer() {			
	
		if (SINGLETON == null)
			createInstance();
		return server;								
	}
	
	/**
	 * Function for logs only
	 */
	public static void checkServer () {
	
		if (SINGLETON == null)
			if (QSLog.DEBUG_W)QSLog.w(CLASS_NAME, "Singleton Nul!!!");
		else
			if (QSLog.DEBUG_W)QSLog.w(CLASS_NAME, "Singleton Not Null!!!");
		
		if (server == null) 
			if (QSLog.DEBUG_W)QSLog.w(CLASS_NAME, "Server Null!!!");
		else
			if (QSLog.DEBUG_W)QSLog.w(CLASS_NAME, "Server Not Null!!!");			
	}
	
	
	/**
	 * Destructor class function
	 */
	protected void finalize () {
		if (server != null) {
			server.close();
			server = null;
		}
	}
	
	
	/**
	 * Function that defrag de Server DDBB File for get free space
	 */
	public static void defrag () {
		
		try
		{
			if (server != null)
				com.db4o.defragment.Defragment.defrag(
						getDDBBFile(DDBB_FILE), 
						getDDBBFile(DDBB_FILE + ".old")); 
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
	
	
	
	/**
	 * Function that returns the ddbb file string from the app
	 * 
	 * @param 		ddbb_file
	 * @return		The ddbb filename
	 * @see			String
	 */
	private static String getDDBBFile(String ddbb_file) {
				
		//ConfigAppValues.getContext();
		if (ConfigAppValues.getContext() != null)
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "getContext != null");
		else
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "getContext == null");
		
		return ConfigAppValues.getContext().getDir(
				DDBB_DIR, 
				ConfigAppValues.getContext().MODE_PRIVATE) 
				+ "/" + ddbb_file;							
		
		//NOTA: Para escribir en la sdcard, no se utiliza getContext().getDir()
		//Se utiliza FileOutputStream y las funciones normales de java.io
	}
} 



