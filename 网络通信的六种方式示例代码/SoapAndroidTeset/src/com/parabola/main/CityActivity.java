package com.parabola.main;

import java.util.ArrayList;
import java.util.List;

import com.parabola.main.ProvinceActivity.ProvinceAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CityActivity extends Activity { 
	public static final String TAG = "CityActivity";
	String city = null;
	WebServiceHelper wsh = new WebServiceHelper();
	List<String> citys = new ArrayList<String>();
	CityAdapter cityAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);
        
        ListView cityList = (ListView) this.findViewById(R.id.cityList);
        
        Intent i = this.getIntent();
        if(i != null){
        	Bundle bd = i.getExtras();
        	if(bd != null){
        		if(bd.containsKey("province")){
        			city = bd.getString("province");
        		}
        	}
        }
        
        citys = wsh.getCitys(city);
        cityAdapter = new CityAdapter();
         
        cityList.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = view.getTag(); 
				if (obj != null) {
					String id1 = obj.toString();
					Log.i(TAG, ""+ obj.toString());
					Intent intent = new Intent(CityActivity.this, WeatherInfoActivity.class);
					Bundle b = new Bundle();
					b.putString("city", id1);
					intent.putExtras(b);
					startActivity(intent); 
				}
			}
		});
        
        cityList.setAdapter(cityAdapter);
         
    }
    
    class CityAdapter extends BaseAdapter {

		@Override
		public int getCount() { 
			return citys.size();
		}

		@Override
		public Object getItem(int position) { 
			return citys.get(position);
		}

		@Override
		public long getItemId(int position) { 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater mInflater = getLayoutInflater(); 
			convertView =  mInflater.inflate(R.layout.province_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.proviceText);
			tv.setText(citys.get(position));
			convertView.setTag(tv.getText());
			return convertView;
		}
    	
    }
}