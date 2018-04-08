package org.mingjiang.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trainApp.common.HttpHelper;
import trainApp.common.TrainInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RemainTicketsList extends Activity {

	int mLastPosition = -1;
	View mLastView;
	
	ListView list;
	ArrayList<HashMap<String, Object>> listItem;
	HashMap<String, Object> map;
	SimpleAdapter listItemAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.remainticketslist);
		// 绑定Layout里面的ListView
		list = (ListView) findViewById(R.id.remainTicketsList);
		TextView refreshTime_Label = (TextView) this.findViewById(R.id.refreshTime_Label);
		
		
		Bundle mBundle = this.getIntent().getExtras();
		String data = mBundle.getString("data");
		String title = mBundle.getString("start_arriveStation");
		String refreshTime = mBundle.getString("refreshTime");
		
		this.setTitle( title + "的查询结果");
		refreshTime_Label.setText("蓝虫提醒您：每小时更新一次，此次更新时间:" + refreshTime);

		List<TrainInfo> trainArray = HttpHelper.getTrainInfoList(data);

		listItem = new ArrayList<HashMap<String, Object>>();

		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.remainticketslistitem, 
					new String[] { "ItemTitle", "Start_ArriveStation", "ItemText1", "ItemText2", "ItemText3" }, 
					new int[] { R.id.ItemTitle,R.id.start_arriveStation, R.id.ItemText1, R.id.ItemText2, R.id.ItemText3 });
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
			map.put("ItemText1", itemText1);
			
			String itemText2 = "软座:" + trainArray.get(i).getSoftSeatCount()
					+ "    硬座:" + trainArray.get(i).getHardSeatCount()
					+ "    一等座:" + trainArray.get(i).getFirstClassSeatCount()
					+ "    二等座:" + trainArray.get(i).getSecondClassSeatCount();
			map.put("ItemText2", itemText2);
			
			String itemText3 = "站票:" + (trainArray.get(i).isHaveSeat() == true ? "有" : "无")
					+ "    软卧:" + trainArray.get(i).getSoftCouchetteCount() 
					+ "    硬卧:" + trainArray.get(i).getHartCouchetteCount()
					+ "    高级软卧:" + trainArray.get(i).getPremiumCouchetteCount();
			map.put("ItemText3", itemText3);
			listItem.add(map);
			listItemAdapter.notifyDataSetChanged();
		}

		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				if(position != mLastPosition) {  
		             //如果点击的条目和上一次的不同，那么就展开本条目，关闭上次展开的条目  
		            setVisible(view);  
		            setGone(mLastView);  
		            mLastPosition = position;  
		            mLastView = view;  
		        } else {  
		              //如果点击的条目和上一次的相同，那么就弹出对话框，提供更多功能选项  
		        }  
			}
		});

	}

	private void setVisible(View view) {
		if (view == null)
			return;
		view.findViewById(R.id.ItemText2).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ItemText3).setVisibility(View.VISIBLE);
	}
	
	 private void setGone(View view) {  
	       if(view == null)return;  
	       view.findViewById(R.id.ItemText2).setVisibility(View.GONE);  
	       view.findViewById(R.id.ItemText3).setVisibility(View.GONE);  
	    } 

	protected void displayText(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
}
