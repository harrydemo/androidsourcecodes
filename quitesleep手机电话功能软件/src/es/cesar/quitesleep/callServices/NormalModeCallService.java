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
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Vibrator;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class NormalModeCallService extends Service {

	
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
				
		putRingerModeNormal();		
		
		stopSelf();
		
		return Service.START_NOT_STICKY;
	}
	//------------------------------------------------------------------------//
	
	

	/**
	 * Put the mobile in normal mode (audio and vibrate), important: normal mode
	 * such the user defined previously  (before to put the mobile in silence)	 
	 * 
	 */
	private void putRingerModeNormal () {
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Poniendo el movil en modo normal");
				
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			
			ConfigAppValues.processIdleCall = false;						
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Put the vibrator in mode On
	 */
	private void vibrateOn () {
		
		try {
			
			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator)ConfigAppValues.getContext().
				getSystemService(vibratorService);
			
			vibrator.vibrate(1000);
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
}
