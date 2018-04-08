package com.updateapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UpdateAppActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "Update";
	private Button btnUpdateApp;
	private ProgressDialog pBar;
	private String downPath = "http://10.0.2.2:8080/";
	private String appName = "NewAppSample.apk";
	private String appVersion = "version.json";
	private int newVerCode = 0;
	private String newVerName = "";
	private Handler handler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
        	if(isNetworkAvailable(this) == false){
        		return;
        	}else{
        		checkToUpdate();
        	}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        btnUpdateApp = (Button)findViewById(R.id.btnUpdateApp);
        btnUpdateApp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
        });
    }
    //check the Network is available
    private static boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
    	try{
    	
    		ConnectivityManager cm = (ConnectivityManager)context
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());//妫�祴缃戠粶鏄惁鍙敤
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	//check new version and update
	private void checkToUpdate() throws NameNotFoundException {
		// TODO Auto-generated method stub
		if(getServerVersion()){
			int currentCode = CurrentVersion.getVerCode(this);
			if(newVerCode > currentCode)
			{//Current Version is old
				//寮瑰嚭鏇存柊鎻愮ず瀵硅瘽妗�				showUpdateDialog();
			}
		}
	}
	//show Update Dialog
	private void showUpdateDialog() throws NameNotFoundException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append(CurrentVersion.getVerName(this));
		sb.append("VerCode:");
		sb.append(CurrentVersion.getVerCode(this));
		sb.append("\n");
		sb.append("鍙戠幇鏂扮増鏈細");
		sb.append(newVerName);
		sb.append("NewVerCode:");
		sb.append(newVerCode);
		sb.append("\n");
		sb.append("");
		Dialog dialog = new AlertDialog.Builder(UpdateAppActivity.this)
		.setTitle("杞欢鏇存柊")
		.setMessage(sb.toString())
		.setPositiveButton("鏇存柊", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				showProgressBar();//鏇存柊褰撳墠鐗堟湰
			}
		})
		.setNegativeButton("鏆備笉鏇存柊", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).create();
		dialog.show();
	}
	protected void showProgressBar() {
		// TODO Auto-generated method stub
		pBar = new ProgressDialog(UpdateAppActivity.this);
		pBar.setTitle("姝ｅ湪涓嬭浇");
		pBar.setMessage("璇风◢鍚�..");
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		downAppFile(downPath + appName);
	}
	//Get ServerVersion from GetUpdateInfo.getUpdateVerJSON
	private boolean getServerVersion() {
		// TODO Auto-generated method stub
		try{
			String newVerJSON = GetUpdateInfo.getUpdataVerJSON(downPath + appVersion);
			JSONArray jsonArray = new JSONArray(newVerJSON);
			if(jsonArray.length() > 0){
				JSONObject obj = jsonArray.getJSONObject(0);
				try{
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				}catch(Exception e){
					Log.e(TAG, e.getMessage());
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	protected void downAppFile(final String url) {
		pBar.show();
		new Thread(){
			public void run(){
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					Log.isLoggable("DownTag", (int) length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if(is == null){
						throw new RuntimeException("isStream is null");
					}
					File file = new File(Environment.getExternalStorageDirectory(),appName);
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					do{
						ch = is.read(buf);
						if(ch <= 0)break;
						fileOutputStream.write(buf, 0, ch);
					}while(true);
					is.close();
					fileOutputStream.close();
					haveDownLoad();
					}catch(ClientProtocolException e){
						e.printStackTrace();
						}catch(IOException e){
						e.printStackTrace();
						}
				}
		}.start();
	}
	//cancel progressBar and start new App
	protected void haveDownLoad() {
		// TODO Auto-generated method stub
		handler.post(new Runnable(){
			public void run(){
				pBar.cancel();
				//寮瑰嚭璀﹀憡妗�鎻愮ず鏄惁瀹夎鏂扮殑鐗堟湰
				Dialog installDialog = new AlertDialog.Builder(UpdateAppActivity.this)
				.setTitle("涓嬭浇瀹屾垚")
				.setMessage("鏄惁瀹夎鏂扮殑搴旂敤")
				.setPositiveButton("纭畾", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						installNewApk();
						finish();
						}
					})
					.setNegativeButton("鍙栨秷", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
							}
						})
						.create();
				installDialog.show();
				}
			});
		}
	//瀹夎鏂扮殑搴旂敤
	protected void installNewApk() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(
				new File(Environment.getExternalStorageDirectory(),appName)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
}