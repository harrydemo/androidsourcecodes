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

package es.cesar.quitesleep.callServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import es.cesar.quitesleep.operations.IncomingCallOperations;
import es.cesar.quitesleep.staticValues.ConfigAppValues;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SilentModeCallService extends Service {
	
	private final String CLASS_NAME = getClass().getName();
	
	
	//------------		Inherited method re-implement	----------------------//	
	@Override
	public void onCreate () {
				
	}
	
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		
		/*
		if ((flags & START_FLAG_RETRY) == 0) {
			//TODO If it's a restart, do something
		}else {
			//TODO Alternative background process
		}
		*/
		
		String incomingCallNumber = intent.getExtras().getString(ConfigAppValues.INCOMING_CALL_NUMBER);
		IncomingCallOperations incomingCallOperations = 
			new IncomingCallOperations(incomingCallNumber);
		
		//If i use threads
		incomingCallOperations.start();
		
		//If i don't use threads
		//incomingCallOperations.silentIncomingCall();
		
		//Stop the service by self way		
		stopSelf();
		
		//before i used start_sticky
		return Service.START_NOT_STICKY;
	}
	//------------------------------------------------------------------------//
	

}
