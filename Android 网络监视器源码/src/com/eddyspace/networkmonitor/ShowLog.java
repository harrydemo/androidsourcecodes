/*
 * This file is part of NetworkMonitor copyright Dave Edwards (eddyspace.com)
 * 
 * Firstly thanks to all and sundry for the tutorials and forum threads I've used
 * in putting this together.
 *
 * Where I've copied whole chunks of stuff I've tried to attribute it properly.
 * Otherwise, I've at least typed it in and munged it to suit which makes it 
 * mine I guess.
 *
 * As far as it goes: Network Monitor is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 * 
 * Network Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * Please see  <a href='http://www.gnu.org/licenses/'>www.gnu.org/licenses</a> 
 * for a copy of the GNU General Public License.
 */

package com.eddyspace.networkmonitor;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Class to show lines from the log file
 * 
 * @author davo
 *
 */
public class ShowLog extends ListActivity {
	static final String LOG_TAG = "NetworkMonitor.ShowLog";
	private final static boolean localLOGV = true;
	private NetInfoAdapter ni = null;
	
	private static String[] DATA;
	private static int dataPointer;
	
	/**
	 * Taken from an android sample application
	 * 
	 * @author davo
	 *
	 */
    private static class EfficientAdapter extends BaseAdapter {
    	private LayoutInflater mInflater;
        
        public EfficientAdapter(Context context) {
        	// Cache the LayoutInflate to avoid asking for a new one each time.
        	mInflater = LayoutInflater.from(context);
        }

        /**
         * The number of items in the list is determined by the number of lines
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return DATA.length;
        }

        /**
         * Since the data comes from an array, just returning the index is
         * Sufficient to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.show_log_list, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);              

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            holder.text.setText(DATA[position]);            

            return convertView;
        }

        static class ViewHolder {
            TextView text;
        }
    }

    /**
     * On Create routine
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	LogAdapter la = new LogAdapter(getBaseContext());
    	
    	//  Get the data from the log adapter
    	List<String> list = la.getAllLines();
    	
    	// Populate the global DATA array
    	DATA = (String[]) list.toArray(new String[0]);
    	
    	// Generate the view
        setListAdapter(new EfficientAdapter(this));
        
        // Get an InfoAdaptor instance
        ni = new NetInfoAdapter(this);
    }
    
    /**
     * Handle clicks on list items
     */
    protected void onListItemClick(ListView lv, View v, int position, long id) {
    	if(localLOGV) Log.v(LOG_TAG, "onListItemClick got position: " + position );
    	ShowDetail(position);
    }
    
    /**
     * Show the detail from the log line
     * 
     * @param line	CSV string from the log file
     */
    void ShowDetail (int position) {    	
    	dataPointer = position;
    	
    	if( ni.isConnected() ) {        	
        	if(localLOGV) Log.v(LOG_TAG, "Seems we're connected..");
        } else {
        	if(localLOGV) Log.v(LOG_TAG, "Seems we're not connected.."); 
        }        
        
	  	// setContentView(R.layout.netmon_info);
	    final Dialog detail = new Dialog(this);
	    detail.setTitle(R.string.show_log_detail_name);
	  	detail.setContentView(R.layout.show_log_detail);
	  	
	  	Button prev = ((Button) detail.findViewById(R.id.button_previous));
	  	Button next = ((Button) detail.findViewById(R.id.button_next));
	  	ShowLine(detail);
	  	prev.setOnClickListener(new OnClickListener() {
	  		public void onClick(View view) {
	  			dataPointer = dataPointer > 1? dataPointer -= 1: dataPointer;
	  			ShowLine(detail);
	  		}
	  	});
	  	next.setOnClickListener(new OnClickListener() {
	  		public void onClick(View view) {
	  			dataPointer = dataPointer < DATA.length - 1? dataPointer += 1: dataPointer;
	  			ShowLine(detail);
	  		}
	  	});
	  	detail.show();
	}
    
    void ShowLine (Dialog detail) {
    	String line = DATA[dataPointer];        
    	
    	Log.v(LOG_TAG, "Data pointer is: " + Integer.toString(dataPointer));
    	String[] list = line.split(",", -1);
    	if(localLOGV) Log.v(LOG_TAG, "Found " + list.length + " items in list");
    	
    	int i = 0;
        TextView tv = (TextView) detail.findViewById(R.id.show_log_date_value);
        
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_time_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_state_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_type_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_netid_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_speed_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_roaming_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_bgdata_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_interface_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_ip_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_gateway_value);
        tv.setText(list[i++]);
        tv = (TextView) detail.findViewById(R.id.show_log_dns_value);
        tv.setText(list[i++]);                	
	}
}
