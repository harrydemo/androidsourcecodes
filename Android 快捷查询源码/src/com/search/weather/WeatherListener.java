package com.search.weather;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;


import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;

public class WeatherListener implements RequestListener {
	
	private WeatherSearch context;
	
	private ProgressDialog progress;
	
	private Resources res;
	
	public WeatherListener(WeatherSearch context){
		this.context = context;
		res = this.context.getResources();
		this.progress = ProgressDialog.show(context, res.getString(R.string.wea_searching), res.getString(R.string.getting));
		this.progress.show();
	}

	@Override
	public void onComplete(final String result) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream is = null;
					is = new ByteArrayInputStream(result.getBytes("UTF-8"));
					
					List<Weather> results = WeatherPullParser.getData(is);
					if(results.size()>=2){
						Weather today = results.get(0);
						Weather tomorrow = results.get(1);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("today", today);
						bundle.putSerializable("tomorrow", tomorrow);
						
						intent.putExtras(bundle);
						intent.setClass(context, SearchResult.class);
						//results中是今天和明天的天气情况
						//Toast.makeText(context, results.get(0).toString(), Toast.LENGTH_LONG).show();
						if(progress != null){
							progress.dismiss();
						}
						context.startActivity(intent);						
					}else{
						if(progress != null){
							progress.dismiss();
						}
						ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.xml_error));
					}

					
				} catch (UnsupportedEncodingException e) {
					Log.e(LOG_TAG, e.getMessage());
					ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.xml_error));
					//e.printStackTrace();
				}
				
				//Toast.makeText(context, results.size()+":"+results.toString(), Toast.LENGTH_LONG).show();
			}
		});

		

	}

	@Override
	public void onException(final Exception e) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				if(progress != null){
					progress.dismiss();
				}
				
				//Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
				ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.get_nothing));
			}
		});

	}
	
	private static final String LOG_TAG = "WeatherListener";

}
