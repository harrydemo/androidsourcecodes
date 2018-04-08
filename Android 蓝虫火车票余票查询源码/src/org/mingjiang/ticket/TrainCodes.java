package org.mingjiang.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trainApp.common.HttpHelper;
import trainApp.common.TrainInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TrainCodes extends Activity {

	ListView list;
	ArrayList<HashMap<String, Object>> listItem;
	HashMap<String, Object> map;
	SimpleAdapter listItemAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.traincodeslist);
		// 绑定Layout里面的ListView
		list = (ListView) findViewById(R.id.trainCodesList);
		TextView refreshTime_Label = (TextView) this.findViewById(R.id.refreshTime_Label1);
		
		Bundle mBundle = this.getIntent().getExtras();
		String data = mBundle.getString("data");
		String title = mBundle.getString("start_arriveStation");
		String refreshTime = mBundle.getString("refreshTime");
		
		this.setTitle( title + "的车次");
		refreshTime_Label.setText("蓝虫提醒您：每小时更新一次，此次更新时间:" + refreshTime);
		
		List<TrainInfo> trainArray = HttpHelper.getTrainInfoList(data);

		listItem = new ArrayList<HashMap<String, Object>>();

		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.traincodeslistitem, 
					new String[] { "ItemTitle", "Start_ArriveStation", "Start_ArriveTime" }, 
					new int[] { R.id.trainCodeItemTitle,R.id.start_arriveStation1, R.id.start_arrive_time1});
		// 添加并且显示
		list.setAdapter(listItemAdapter);
		
		
		for (int i = 0; i < trainArray.size(); i++) {
			map = new HashMap<String, Object>();
			
			map.put("ItemTitle", trainArray.get(i).getTrainCode().replace(">", ""));
			
			String Start_ArriveStation =" " + trainArray.get(i).getStartStation() + "-" + trainArray.get(i).getArriveStation();
			map.put("Start_ArriveStation", Start_ArriveStation);
			
			String itemText1 = " 发时:" + trainArray.get(i).getStartTime()
					+ "  到时:" + trainArray.get(i).getArrtiveTime() + "  历时:"
					+ trainArray.get(i).getUsedTime();
			map.put("Start_ArriveTime", itemText1);


			listItem.add(map);
			listItemAdapter.notifyDataSetChanged();
		}
		
		
		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				String trainCode = ((TextView)view.findViewById(R.id.trainCodeItemTitle)).getText().toString().trim();
				Intent intent = new Intent();
		        Bundle bundle = new Bundle();
		        bundle.putString("trainCode", trainCode);
		        intent.putExtras(bundle);
		        setResult(1111111, intent);
				finish();  
			}
		});
		
	}
}
