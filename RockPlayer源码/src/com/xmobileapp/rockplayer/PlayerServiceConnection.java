/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmobileapp.rockplayer;

import com.xmobileapp.rockplayer.PlayerServiceInterface;

import com.xmobileapp.rockplayer.utils.Constants;
import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PlayerServiceConnection implements ServiceConnection{

	RockPlayer filex;
	PlayerServiceConnection(RockPlayer filex){
		this.filex = filex;
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i("SCONN", "Service connected");
		
		if(filex == null || service == null){
			Log.i("SCONN", "But failed to get service interface for some reason");
			return;
		}
		
		filex.playerServiceIface = PlayerServiceInterface.
										Stub.
											asInterface(service);
		/*
		 * Set some preferences
		 */
		RockOnPreferenceManager prefs = (new RockOnPreferenceManager(filex.FILEX_PREFERENCES_PATH));
		try{
			filex.playerServiceIface.setRecentPeriod(
					prefs
						.getInt(filex.constants.PREF_KEY_RECENT_PERIOD, filex.constants.RECENT_PERIOD_DEFAULT_IN_DAYS));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * Set Shuffle
		 */
		try{
			filex.playerServiceIface.setShuffle(filex.SHUFFLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * Set Scrobbling
		 */
		try{
//			Log.i("SCRBLREADING", ""+filex.getSharedPreferences(Filex.PREFS_NAME, 0)
			Log.i("SCRBLREADING", ""+prefs
									.getBoolean(RockPlayer.PREFS_SCROBBLE_DROID, false));
			filex.playerServiceIface.setScrobbleDroid((boolean)
//					filex.getSharedPreferences(Filex.PREFS_NAME, 0)
					prefs
					.getBoolean(RockPlayer.PREFS_SCROBBLE_DROID, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * Set the playlist of the service
		 */
		try {
			filex.playerServiceIface.setPlaylist(filex.playlist);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		/*
		 * Reset albumCursor of the Service
		 */
		try {
			filex.playerServiceIface.resetAlbumCursor();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		/*
		 * Get whatever the service is playing
		 */
		filex.getCurrentPlaying();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		
	}
	
}