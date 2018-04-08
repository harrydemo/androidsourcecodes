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

import com.xmobileapp.rockplayer.R;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.drawable.TransitionDrawable;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;


public class MusicChangedIntentReceiver	extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("MUSICINTENT", "musicChanged");
		RockPlayer filex =((RockPlayer) context);
		
		
		try {
			/*
			 * Destroy song Progress Dialog if on screen
			 */
			try{
				if(filex.songProgressAlertDialog != null){
					/*
					 * dismiss (And remove) the song progress dialog
					 */
					filex.dismissDialog(R.layout.song_progress_dialog);
					filex.removeDialog(R.layout.song_progress_dialog);
					filex.songProgressAlertDialog = null;
					filex.songProgressView = null;
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			
			/*
			 * UpdateAlbumCursor
			 */
			filex.albumCursor.moveToPosition(
					filex.playerServiceIface.getAlbumCursorPosition());
			
			/*
			 * Update Song Cursor
			 */
			filex.songCursor = filex.initializeSongCursor(
					filex.albumCursor.getString(
							filex.albumCursor.getColumnIndexOrThrow(
									MediaStore.Audio.Albums.ALBUM)));
			filex.songCursor.moveToPosition(
					filex.playerServiceIface.getSongCursorPosition());
			
			/*
			 * Update Song UI
			 */
			filex.songProgressBar.setProgress(0);
			filex.songProgressBar.setMax((int) filex.songCursor.getDouble(
												filex.songCursor.getColumnIndex(
														MediaStore.Audio.Media.DURATION)));
			filex.updateSongTextUI();
			filex.triggerSongProgress();
			
			/*
			 * playPauseButton
			 */
//			TransitionDrawable playPauseTDrawable = (TransitionDrawable) filex.playPauseImage.getDrawable();
//			playPauseTDrawable.setCrossFadeEnabled(true);
//			playPauseTDrawable.startTransition(1);
//			playPauseTDrawable.invalidateSelf();
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CursorIndexOutOfBoundsException e){
			e.printStackTrace();
			
			// TODO: Maybe resync service and frontend cursors....
			
			try{
				filex.initializeAlbumCursor();
				filex.albumCursor.moveToNext();
				filex.initializeSongCursor(
						filex.albumCursor.getString(
								filex.albumCursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ALBUM)));
			}catch(Exception ee){
				ee.printStackTrace();
			}
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
	
}