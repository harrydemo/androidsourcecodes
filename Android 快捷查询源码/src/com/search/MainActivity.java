package com.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.search.common.ImageAdapter;
import com.search.ems.EmsSearch;
import com.search.ip.IpSearch;
import com.search.telephone.TelephoneSearch;
import com.search.train.TrainSearch;
import com.search.weather.WeatherSearch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class MainActivity extends ListActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.main);
		
		List<Map<String, Object>> datas = this.getDatas();
		
		ImageAdapter adapter = new ImageAdapter(this, datas);
		
		this.getListView().setAdapter(adapter);
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){
		Intent intent = new Intent();
		switch(position){
		case 0:
			//号码归属地查询
			intent.setClass(this, TelephoneSearch.class);
			startActivity(intent);
			break;
		case 1:
			//天气查询
			intent.setClass(this, WeatherSearch.class);
			startActivity(intent);
			break;
		case 2:
			//快递查询
			intent.setClass(this, EmsSearch.class);
			startActivity(intent);
			break;
		case 3:
			//火车查询
			intent.setClass(this, TrainSearch.class);
			startActivity(intent);
			break;
		case 4:
			//ip查询
			intent.setClass(this, IpSearch.class);
			startActivity(intent);
			break;
		}
	}
	
	public List<Map<String, Object>> getDatas(){
		
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		String[] items = this.getResources().getStringArray(R.array.menu);
		for(int i=0; i<items.length; i++){
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("left", R.drawable.item_left);
			item.put("text",items[i]);
			item.put("right", R.drawable.item_right);
			
			results.add(item);
		}
		
		return results;
		
	}

}
