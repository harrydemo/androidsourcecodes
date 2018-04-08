package com.search.telephone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.search.R;
import com.search.RequestListener;
import com.search.common.ActivityUtils;

/**
 * ����http�����ȡ�����гɹ������쳣ʱ�����Ķ���
 * @author Administrator
 *
 */
public class TelephoneListener implements RequestListener {
	
	private TelephoneSearch context;
	
	private ProgressDialog progress;
	
	private Resources res;
	
	public TelephoneListener(TelephoneSearch context){
		this.context = context;
		res = context.getResources();
		progress = ProgressDialog.show(context, res.getString(R.string.tel_searching), res.getString(R.string.getting));
		progress.show();
	}

	@Override
	public void onComplete(final String result) {
		this.context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("telephone", parseJSON(result)); //���ﴫ�ݵ��Ƕ���
				intent.putExtras(bundle);
				intent.setClass(context, SearchResult.class);
				progress.dismiss();
				context.startActivity(intent);
				
			}
		});


	}

	@Override
	public void onException(Exception e) {
		this.context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				if(progress != null){
					progress.dismiss();
					
				}				
				ActivityUtils.showDialog(context, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.get_nothing));
				
			}
		});
	}
	
	//��Json����ת����Telephone
	public Telephone parseJSON(String jsonStr){
		Telephone tel = null;
		//���ص�����ǰ���querycallback();���,������ȥ��
		jsonStr = jsonStr.substring(14, jsonStr.length()-2);
		try {
			tel = new Telephone();
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			tel.setMobile(jsonObj.getString("Mobile"));
			tel.setQueryResult(jsonObj.getString("QueryResult"));
			tel.setProvince(jsonObj.getString("Province"));
			tel.setCity(jsonObj.getString("City"));
			tel.setAreaCode(jsonObj.getString("AreaCode"));
			tel.setPostCode(jsonObj.getString("PostCode"));
			tel.setCorp(jsonObj.getString("Corp"));
			tel.setCard(jsonObj.getString("Card"));
			
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.getMessage());
			//e.printStackTrace();
		}
		
		return tel;
	}
	
	private static final String LOG_TAG = "TelephoneListener";

}
