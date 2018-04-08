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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;


public class AlbumChangedIntentReceiver	extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("ALBUMINTENT", "albumChanged");
		RockPlayer filex =((RockPlayer) context);
		try {
			/*
			 * Destroy song Progress Dialog if on screen
			 */
			try{
				if(filex.songProgressAlertDialog != null){
					filex.dismissDialog(R.layout.song_progress_dialog);
					filex.removeDialog(R.layout.song_progress_dialog);
					filex.songProgressAlertDialog = null;
					filex.songProgressView = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			filex.albumCursor.moveToPosition(
					filex.playerServiceIface.getAlbumCursorPosition());
    		filex.albumCursorPositionPlaying = filex.playerServiceIface.getAlbumCursorPosition();

			filex.songCursor = filex.initializeSongCursor(filex.albumCursor.getString(
												filex.albumCursor.getColumnIndexOrThrow(
														MediaStore.Audio.Albums.ALBUM)));
			filex.songCursor.moveToPosition(
					filex.playerServiceIface.getSongCursorPosition());

			filex.songProgressBar.setProgress(0);
			filex.songProgressBar.setMax((int) filex.songCursor.getDouble(
												filex.songCursor.getColumnIndexOrThrow(
														MediaStore.Audio.Media.DURATION)));
			
			filex.calledByService = true;
			
			Message msg = new Message();
			msg.obj = filex;
//			animationStarterHandler.sendMessageDelayed(msg, 100);
    		filex.currentAlbumPlayingLayoutOuter.startAnimation(filex.fadeAlbumOut);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 50);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 100);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 150);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 200);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 250);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 300);
    		filex.invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 350);
			filex.triggerSongProgress();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Handler animationStarterHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			RockPlayer filex = (RockPlayer) msg.obj;
    		filex.currentAlbumPlayingLayoutOuter.startAnimation(filex.fadeAlbumOut);
    		//msg.recycle();
		}
	};
	
}