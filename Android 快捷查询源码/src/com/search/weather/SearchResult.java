package com.search.weather;

import com.search.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SearchResult extends Activity {
	
	private TextView today,tomorrow,low1,low2,high1,high2;
	
	private Resources res;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_info);
        
        today = (TextView)this.findViewById(R.id.wea_today);
        tomorrow = (TextView)this.findViewById(R.id.wea_tomorrow);
        low1 = (TextView)this.findViewById(R.id.wea_low);
        low2 = (TextView)this.findViewById(R.id.wea_low2);
        high1 = (TextView)this.findViewById(R.id.wea_high);
        high2 = (TextView)this.findViewById(R.id.wea_high2);
        
        res = this.getResources();
        
        this.showResult();
    }

	private void showResult() {
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		
		Weather w_today = (Weather)bundle.getSerializable("today");
		Weather w_tomorrow = (Weather)bundle.getSerializable("tomorrow");
		
		String unit = res.getString(R.string.wea_unit);
		
		if(w_today != null){
			String today_codition = this.getCondition(w_today.getCode()); //获取code对应的天气
			today.setText(today_codition);
			low1.setText(w_today.getLow()+unit);
			high1.setText(w_today.getHigh()+unit);
		}
		
		if(w_tomorrow != null){
			
			String tommorrow_condition = this.getCondition(w_tomorrow.getCode()); 
			tomorrow.setText(tommorrow_condition);
			low2.setText(w_tomorrow.getLow()+unit);
			high2.setText(w_tomorrow.getHigh()+unit);
		}

		
	}

	//根据代码获取对应的天气信息。在weather的string-array中匹配
	private String getCondition(String code) {
		
		String[] weathers = res.getStringArray(R.array.weather);
		
		int temp = Integer.parseInt(code);
		if(temp < 48 && temp >= 0){
			return weathers[temp];
		}
		return res.getString(R.string.wea_undefine);
	}

}
