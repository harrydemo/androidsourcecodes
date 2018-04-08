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
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;



public class SongCursorAdapter extends SimpleCursorAdapter{
    public int					viewWidth;
    public int					songInitialPosition;
    
    public SongCursorAdapter(Context context, 
    							int layout, 
    							Cursor c,
    							String[] from,
    							int[] to) 
    {
        super(context, layout, c, from, to);
        this.songInitialPosition = c.getPosition();
    }
        
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    	/*
    	 * Get the item list text components and fill them
    	 */  	
    	TextView songName = 		(TextView)
			view.findViewById(R.id.songlist_item_song_name);
    	TextView songDuration = 	(TextView)
			view.findViewById(R.id.songlist_item_song_duration);
    	
    	songName.setText(cursor.getString(
				cursor.getColumnIndex(
						MediaStore.Audio.Media.TITLE)));
    	try{
    		double duration = new Double (cursor.getString(
    									cursor.getColumnIndex(
												MediaStore.Audio.Media.DURATION)));
	    	double minutes = Math.floor(duration / 1000 / 60);
	    	double seconds = duration / 1000 % 60;
	    	if(seconds > 10)
	    		songDuration.setText(String.valueOf((int)minutes)+":"+String.valueOf((int)seconds));
	    	else
	    		songDuration.setText(String.valueOf((int)minutes)+":0"+String.valueOf((int)seconds));
    	} catch (Exception e){
    		e.printStackTrace();
    		songDuration.setText("e:-e");
    	}
    }
}