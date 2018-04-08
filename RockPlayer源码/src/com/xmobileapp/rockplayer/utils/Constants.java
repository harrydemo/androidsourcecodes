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

package com.xmobileapp.rockplayer.utils;

public class Constants{
	/*
	 * Preferences
	 */
	public final String PREFS_DIR = "/sdcard/RockOn/preferences/";
	public final String PREFS_NAME = "RockOnPreferences";
	 
    /*
     * Last song keys
     */
    public final String KEY_PREFERENCE_LAST_ALBUM = "last_album";
    public final String KEY_PREFERENCE_LAST_SONG = "last_song";
    public final String KEY_PREFERENCE_LAST_SONG_POSITION = "last_song_position";
	
	/*
	 * Playlist Stuff
	 */
	public final String PREF_KEY_PLAYLIST = "Playlist";

	public final long PLAYLIST_ALL = -1;	
	public final long PLAYLIST_RECENT = -2;	
	public final long PLAYLIST_NONE = -500;	

	/*
	 * Intents
	 */
	public final String		MUSIC_CHANGED = "org.abrantes.filex.intent.action.MUSIC_CHANGED";  
    public final String		ALBUM_CHANGED = "org.abrantes.filex.intent.action.ALBUM_CHANGED";
    public final String		MEDIA_BUTTON_PAUSE = "org.abrantes.filex.intent.action.MEDIA_BUTTON_PAUSE";  
    public final String		MEDIA_BUTTON_PLAY = "org.abrantes.filex.intent.action.MEDIA_BUTTON_PLAY";
    
	
	/*
	 * Recent Songs Stuff
	 */
	public final String PREF_KEY_RECENT_PERIOD = "recent_period";
	
	public final int RECENT_PERIOD_DEFAULT_IN_DAYS = 15;
	
	/*
	 * Activity Codes
	 */
	public final int PREFERENCES_REQUEST = 1;
	
	/*
	 * Interaction Intervals
	 */
	public final double DOUBLE_CLICK_INTERVAL = 700;

}
