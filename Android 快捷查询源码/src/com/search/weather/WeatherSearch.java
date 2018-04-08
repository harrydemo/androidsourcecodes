package com.search.weather;

import com.search.R;
import com.search.common.ActivityUtils;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class WeatherSearch extends Activity {
  
	private EditText city;
	
	private Search search;
	
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather);
        city = (EditText)this.findViewById(R.id.weather_city);
        search = new Search();
        
        res = this.getResources();
        
    }
    
    public void onClick(View v){
    	
    	if(v.getId() == R.id.weather_search){
    		String cityName = city.getText().toString().trim();
    		if(!cityName.equals("")){
    			
    			String woeid = validate(cityName);
    			
    			if(woeid != null){
            		WeatherListener listener = new WeatherListener(this);
            		search.asyncRequest(woeid, listener);
    			}else{
    				ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.no_city));
    			}

    		}else{
    			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.wea_hint));
    		}

    	}
    }

    //判断用户输入的城市存不存在,存在返回这个城市对应的woeid,否则返回null
	private String validate(String cityName) {
		String[] woeids = res.getStringArray(R.array.woeid);
		String[] cityNames = res.getStringArray(R.array.city_names);
		
		String woeid = null;
		
		for(int i=0; i<cityNames.length; i++){
			
			if(cityName.equals(cityNames[i])){
				woeid = woeids[i];
				break;
			}
		}
		
		return woeid;
	}
    
}