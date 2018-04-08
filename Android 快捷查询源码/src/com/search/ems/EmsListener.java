package com.search.ems;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;

public class EmsListener implements RequestListener {
	
	private EmsSearch context;
	
	private ProgressDialog progress;
	
	private Resources res;
	
	public EmsListener(EmsSearch context){
		this.context = context;
		res = context.getResources();
		progress =  ProgressDialog.show(context, res.getString(R.string.ems_searching), res.getString(R.string.getting));
		progress.show();
	}
	

	@Override
	public void onComplete(final String result) {
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("ems", parseJSON(result));
				intent.putExtras(bundle);
				intent.setClass(context, SearchResult.class);
				if(progress != null){
					progress.dismiss();
					
				}
				context.startActivity(intent);
				
			}
		});
		
	}

	
	protected Ems parseJSON(String result) {
		Ems ems = null;
		
		try {
			//Log.v("EMS_JSON", result);
			Bundle b = context.getIntent().getExtras();
			JSONObject json = new JSONObject(result);
			ems = new Ems();
			ems.setCompany(b.getString("company"));
			ems.setOrder(b.getString("order"));
			ems.setMessage(json.getString("message"));
			ems.setStatus(json.getString("status"));
			
			if(ems.getStatus().equals("1") && json.has("data")){
				JSONArray array = json.getJSONArray("data"); //这里封装了data，但data可能不存在
				JSONObject data = array.getJSONObject(0);
				ems.setTime(data.getString("time"));
				ems.setContext(data.getString("context"));				
			}
			//Log.v("EMS_JSON", ems.toString());
			return ems;
			
		} catch (Exception e) {
			ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), "json解析错误:"+e.getMessage());
			return null;
		}
		
	}


	@Override
	public void onException(Exception e) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(progress != null){
					progress.dismiss();
					
				}
				
				ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.get_nothing));
			}
		});

	}

}
