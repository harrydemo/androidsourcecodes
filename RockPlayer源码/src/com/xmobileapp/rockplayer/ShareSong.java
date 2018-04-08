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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

/********************************
 * 
 * Definitions
 * @author fabrantes
 *
 ********************************/

/********************************
 * 
 * ShareSong class
 * 
 ********************************/
public class ShareSong{
	String songName = null;
	String albumName = null;
	String artistName = null;
	String songFilePath = null;
	RockPlayer filex = null;
	
	public ShareSong(Context context, Cursor songCursor){
			filex = (RockPlayer)context;
	
			try{
				songName = songCursor.getString(
							songCursor.getColumnIndex(
								MediaStore.Audio.Media.TITLE));
				albumName = songCursor.getString(
								songCursor.getColumnIndex(
									MediaStore.Audio.Media.ALBUM));
				artistName = songCursor.getString(
								songCursor.getColumnIndex(
									MediaStore.Audio.Media.ARTIST));
				songFilePath = songCursor.getString(
									songCursor.getColumnIndex(
											MediaStore.Audio.Media.DATA));
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public void shareByMail(){
		String emailText = "Hey! Check these guys out... they are awesome!\n"+
							"\n"+
							"Recommendation:\n"+
							songName+" from "+artistName;
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, emailText);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Music recommendation");
		//sendIntent.putExtra(Intent.EXTRA_STREAM, InputStream or URI);
		sendIntent.setType("message/rfc822");
		filex.startActivity(Intent.createChooser(sendIntent, "Choose Email Client")); 
	}
	
}