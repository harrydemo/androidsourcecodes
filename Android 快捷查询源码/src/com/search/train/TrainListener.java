package com.search.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;


public class TrainListener implements RequestListener {
	
	private TrainSearch context;
	
	private ProgressDialog progress;
	
	private Resources res;
	
	public TrainListener(TrainSearch context){
		this.context = context;
		res = context.getResources();
		progress = ProgressDialog.show(context, res.getString(R.string.train_searching), res.getString(R.string.getting));
		progress.show();
	}

	@Override
	public void onComplete(final String result) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				List<Train> trains = TrainListener.this.parseJSON(result);
				//List<Train> trains = TrainListener.this.getDatas();
			
				Bundle bundle = new Bundle();
				
				if(trains.size() > 0){
					Train t = trains.get(0);
					bundle.putString("from", t.getFromStation());
					bundle.putString("to", t.getToStation());
				}
				for(int i=0; i<trains.size(); i++){
					//Log.v(t.getCheciType()+":", t.toString());
					Train train = trains.get(i);
					bundle.putSerializable("train"+i, train);
					
				}
				
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(context, TrainList.class);
				if(progress != null){
					progress.dismiss();
				}
				context.startActivity(intent);
//				if(progress != null){
//					progress.dismiss();
//				}
//				String[] from = new String[]{
//						"item_image",
//						"item_text1",
//						"item_text2"
//				};
//				int[] to = new int[]{
//						R.id.item_train_image,
//						R.id.item_train_text1,
//						R.id.item_train_text2
//				};
//				
//				SimpleAdapter adapter = new SimpleAdapter(context, trains, R.layout.list_item_train, from, to);
//				ListView lv = new ListView(context);
//				lv.setAdapter(adapter);
//				lv.setBackgroundColor(R.color.transparency);
//				new AlertDialog.Builder(context)
//					.setView(lv)
//					.create()
//					.show();

				
			}
		});
		
	}
	
//	protected List<Map<String, Object>> getDatas(){
//		
//		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
//		
//		for(int i=0; i<10; i++){
//			Map<String, Object> item = new HashMap<String, Object>();
//			item.put("item_image", R.drawable.icon);
//			StringBuilder sb = new StringBuilder();
//			sb.append("车次：").append("K256/K238").append("  ")
//			  .append("车型：").append("空调快速");
//			  
//			item.put("item_text1", sb.toString());
//			
//			StringBuilder sb2 = new StringBuilder();
//			sb2.append("出发时间：").append("3:40").append("  ")
//			  .append("到达时间：").append("6:30");
//			
//			item.put("item_text2", sb2.toString());
//			datas.add(item);
//		}
//		
//		return datas;
//	}

	protected List<Train> getDatas() {
		List<Train> trains = new ArrayList<Train>();
		
		for(int i=0; i<10; i++){
			Train train = new Train();
			train.setCheciType(i+"");
			train.setCls("k23"+i);
			train.setType("空调快速");
			train.setStartStation("芜湖");
			train.setEndStation("蚌埠");
			train.setStartTime("2:30");
			train.setEndTime("4:30");
			train.setDistant("400");
			train.setDuration("2个小时");
			train.setFromStation("芜湖");
			train.setToStation("蚌埠");
			train.setReachStation("芜湖");
			train.setReachTime("2:30");
			train.setLeaveTime("2:40");
			train.setPassDuration("2个小时");
			train.setPassDistant("");
			train.setPriceYingZuo("40");
			train.setPriceYingWo("40");
			train.setWaitInterval("10");	
			
			trains.add(train);
		}
		return trains;
	}

	protected List<Train> parseJSON(String jsonStr) {
		List<Train> result = new ArrayList<Train>();
		
		jsonStr = jsonStr.substring(20, jsonStr.length()-2);
		
		try {
			JSONArray array = new JSONArray(jsonStr);
			for(int i=0; i<array.length(); i++){
				JSONObject obj = array.getJSONObject(i);
				Train train = new Train();
				train.setCheciType(obj.getString("checiType"));
				train.setCls(obj.getString("cls"));
				train.setType(obj.getString("type"));
				train.setStartStation(obj.getString("startstation"));
				train.setEndStation(obj.getString("endstation"));
				train.setStartTime(obj.getString("starttime"));
				train.setEndTime(obj.getString("endtime"));
				train.setDistant(obj.getString("distant"));
				train.setDuration(obj.getString("duration"));
				train.setFromStation(obj.getString("fromstation"));
				train.setToStation(obj.getString("tostation"));
				train.setReachStation(obj.getString("reachstation"));
				train.setReachTime(obj.getString("reachtime"));
				train.setLeaveTime(obj.getString("leavetime"));
				train.setPassDuration(obj.getString("passduration"));
				train.setPassDistant(obj.getString("passdistant"));
				train.setPriceYingZuo(obj.getString("priceyingzuo"));
				train.setPriceYingWo(obj.getString("priceyingwodown"));
				train.setWaitInterval(obj.getString("waitinterval"));
				
				result.add(train);
			}
		} catch (Exception e) {
			Log.e("TrainListener", e.getMessage());
		}
		
		return result;
	}

	@Override
	public void onException(Exception e) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				if(progress != null){
					progress.dismiss();
				}
				ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.get_nothing));
			}
		});

	}

}
