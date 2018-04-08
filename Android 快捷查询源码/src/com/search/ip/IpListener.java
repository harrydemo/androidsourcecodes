package com.search.ip;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;
import com.search.ip.SearchResult;

public class IpListener implements RequestListener {
	
	private IpSearch context;
	
	private ProgressDialog progress;
	
	private Resources res;
	
	public IpListener(IpSearch context){
		this.context = context;
		res = context.getResources();
		progress = ProgressDialog.show(context, res.getString(R.string.ip_searching), res.getString(R.string.getting));
		progress.show();
	}

	@Override
	public void onComplete(final String result) {
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
			
				Log.v("result", result);
				try {
					InputStream is;
					is = new ByteArrayInputStream(result.getBytes("UTF-8"));
					Ip ip = IpPullParser.getData(is);
					
					if(ip != null){
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("ip", ip);
						intent.putExtras(bundle);
						intent.setClass(context, SearchResult.class);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});

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
