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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;


public class MediaButtonPauseIntentReceiver	extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("MEDIABUTTONPAUSE", "pausetriggered");
		RockPlayer filex =((RockPlayer) context);
		try {
			TransitionDrawable playPauseTDrawable = (TransitionDrawable) filex.playPauseImage.getDrawable();
			playPauseTDrawable.setCrossFadeEnabled(true);
			playPauseTDrawable.reverseTransition(500);
			playPauseTDrawable.invalidateSelf();
			
			filex.invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
			filex.invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 300);
			filex.invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 450);
			filex.invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 600);
			filex.invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 750);
			
			filex.stopSongProgress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}