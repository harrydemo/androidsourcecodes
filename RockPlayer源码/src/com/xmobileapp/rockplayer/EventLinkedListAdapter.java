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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.xmobileapp.rockplayer.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.GradientDrawable;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;



public class EventLinkedListAdapter extends ArrayAdapter{
	private LinkedList<ArtistEvent>	artistEventList;
    private Context 				context;
    private int						layoutId;
    private LayoutInflater			inflater;
    private Date					date;
    private DateFormat				dayFormat = new SimpleDateFormat("dd");
    private DateFormat				yearFormat = new SimpleDateFormat("MMM");
    ArtistEvent 					artistEvent;
    
    private TextView				dayText = null;
    private TextView				yearText = null;
    private TextView				artistText = null;
    private TextView				cityText = null;
    private TextView				countryText = null;
    
    public EventLinkedListAdapter(Context context, 
    							int layout, 
    							List<ArtistEvent> artistEventList) 
    {
    	//init(context, layout, 0, artistEventList);
    	super(context, layout, artistEventList);
    	this.context = context;
    	this.artistEventList = (LinkedList<ArtistEvent>) artistEventList;
    	this.layoutId = layout;
    	this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	//super(context, layout, artistEventList);
        //this.artistEventList = (LinkedList<ArtistEvent>) artistEventList;
        //this.context = context;
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
    		view = inflater.inflate(R.layout.eventlist_item, parent, false);
    	
    	dayText = (TextView) view.findViewById(R.id.eventlist_day);
    	yearText = (TextView) view.findViewById(R.id.eventlist_year);
    	artistText = (TextView) view.findViewById(R.id.eventlist_artist);
    	cityText = (TextView) view.findViewById(R.id.eventlist_city);
    	countryText = (TextView) view.findViewById(R.id.eventlist_country);
 
    	artistEvent = this.artistEventList.get(position);

    	date = new Date((long) artistEvent.dateInMillis);
    	yearText.setText(yearFormat.format(date));
    	dayText.setText(dayFormat.format(date));
    	artistText.setText(artistEvent.artist);
    	cityText.setText(artistEvent.city);
    	countryText.setText(artistEvent.country);
    	
//    	LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
//    											LayoutParams.FILL_PARENT);
//    	TextView field= new TextView(context);
//    	field.setLayoutParams(params);
//    	field.setText("Comer castanhas!");
//    	parent.addView(field);
    	
    	return view;
    }
    
    
}