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

import java.util.ArrayList;
import java.util.List;

import com.xmobileapp.rockplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class PlaylistArrayAdapter extends ArrayAdapter{

	Context context;
	ArrayList<Playlist> playlistArray;
	
	public PlaylistArrayAdapter(Context context, 
								int layoutResourceId,
								List playlists) {
		super(context, layoutResourceId, playlists);
		this.context = context;
		this.playlistArray = (ArrayList<Playlist>) playlists;
	}

    /* (non-Javadoc)
     * This is where you actually create the item view of the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
		View view;
    	if(convertView != null)
    		view = convertView;
    	else
    		view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
    									.inflate(R.layout.playlist_item, parent, false);
    	
    	((TextView) view.findViewById(R.id.playlist_name)).setText(playlistArray.get(position).name);
    	
    	return view;
    }
}