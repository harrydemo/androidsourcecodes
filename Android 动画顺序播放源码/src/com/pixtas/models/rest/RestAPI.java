package com.pixtas.models.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.util.Log;

import com.pixtas.models.option.DataBaseOption;
import com.pixtas.models.struts.FlashUrl;
import com.pixtas.yogapowervinyasa.Config;


public class RestAPI {
	private static String TAG = "RestAPI";
	
	public static boolean checkApkUpgrade(){
		boolean arg = true;
		HttpPost post = new HttpPost(Config.checkNewApkVersion_url);
		List <NameValuePair> vars = new ArrayList <NameValuePair>();
		vars.add(new BasicNameValuePair("app_name", Config.audioTitle));
		vars.add(new BasicNameValuePair("version", Config.version));
		 try {
				post.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));
				JSONObject jo = execute(post);
				arg = jo.getJSONObject("ret").getBoolean("can_run");
				if(Config.debug){
					Log.i(TAG,jo.toString());
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return arg;
	}
	
	private static JSONObject execute(HttpRequestBase method)throws IOException, JSONException{
		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpResponse response;

		response = client.execute(method);
		String ret = EntityUtils.toString(response.getEntity());

		return new JSONObject(ret);
	}
	
	private static JSONObject getFlashObject() throws JSONException, ClientProtocolException, IOException{
		HttpPost post = new HttpPost(Config.init_url);
		List <NameValuePair> vars = new ArrayList <NameValuePair>();
		vars.add(new BasicNameValuePair("audioTitle", Config.audioTitle));
		post.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));
		return execute(post);
	}
	
	public static boolean checkNewVersion(){
		boolean hasNewVersion = false;
		HttpPost post = new HttpPost(Config.checkNewVersion_url);
		int version = 1;
		if(DataBaseOption.installHasData()){
			Cursor c = DataBaseOption.fetchInstallData();
			if(c.moveToFirst()){
				version = c.getInt(c.getColumnIndex("version"));
			}
		}
		List <NameValuePair> vars = new ArrayList <NameValuePair>();
		vars.add(new BasicNameValuePair("audioTitle", Config.audioTitle));
        vars.add(new BasicNameValuePair("version", Integer.toString(version)));
        try {
			post.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));
			JSONObject jo = execute(post);
			hasNewVersion = jo.getJSONObject("ret").getBoolean("upload");
			if(hasNewVersion){
				Config.newVersion = Integer.parseInt(jo.getJSONObject("ret").getString("new_version"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasNewVersion;
	}
	
	public static void initFlashUrl(){
		try {
			JSONObject ret = getFlashObject();
			String audioTitle = ret.getJSONObject("ret").getString("audio_title");
			String audioUrl = ret.getJSONObject("ret").getString("audio_url");
			String version = ret.getJSONObject("ret").getString("version");
			Config.audioSize = ret.getJSONObject("ret").getInt("audio_size");
			Config.allImageSize = ret.getJSONObject("ret").getInt("photo_size");
			JSONArray photoUrls = ret.getJSONObject("ret").getJSONArray("photos");
			int len = photoUrls.length();
			if(DataBaseOption.installHasData()){
				DataBaseOption.deleteInstallData();
			}
			DataBaseOption.insertInstallData(audioTitle, audioUrl, len, 0,Integer.parseInt(version));
			if(DataBaseOption.flashHasData()){
				DataBaseOption.deleteFlashData();
			}
			for(int i = 0;i < photoUrls.length();i++){
				String title1 = photoUrls.getJSONObject(i).getString("photo_title1");
				String title2 = photoUrls.getJSONObject(i).getString("photo_title2");
				String title3 = photoUrls.getJSONObject(i).getString("photo_title3");
				String time = photoUrls.getJSONObject(i).getString("chapter_time");
				String url = photoUrls.getJSONObject(i).getString("photo_url");
				DataBaseOption.insertDataToFlashTB(title1, title2, title3, Integer.parseInt(time), url);
			}
			initFlash();//初始化flash参数
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*初始化flash参数*/
	public static void initFlash(){
		Cursor c = DataBaseOption.fechtchAllFlashData();
		Cursor c2 = DataBaseOption.fetchInstallData();
		if(c2.getCount() > 0){
//			long keyId = c2.getLong(c2.getColumnIndex("_id"));
			String audioTitle = c2.getString(c2.getColumnIndex("audioTitle"));
			String audioUrl = c2.getString(c2.getColumnIndex("audioUrl"));
			int version = c2.getInt(c2.getColumnIndex("version"));
			FlashUrl.audioTitle = audioTitle;
			FlashUrl.audioUrl = audioUrl;
			FlashUrl.version = version;
		}
		if(c.getCount() > 0){
			int len = c.getCount();
			FlashUrl.chaptersNum = len;
			FlashUrl.photoEnglishTitles = new String[len];
			FlashUrl.photoSanskritTitles = new String[len];
			FlashUrl.photoSpanishTitles = new String[len];
			FlashUrl.chapterTimes = new int[len];
			FlashUrl.photoUrls = new String[len];
			int i = 0;
			do{
				String t1,t2,t3,url;
				int time;
				t1 = c.getString(c.getColumnIndex("title1"));
				t2 = c.getString(c.getColumnIndex("title2"));
				t3 = c.getString(c.getColumnIndex("title3"));
				time = c.getInt(c.getColumnIndex("cTime"));
				url = c.getString(c.getColumnIndex("photoUrl"));
				
				FlashUrl.photoEnglishTitles[i] = t1;
				FlashUrl.photoSanskritTitles[i] = t2;
				FlashUrl.photoSpanishTitles[i] = t3;
				FlashUrl.chapterTimes[i] = time;
				FlashUrl.photoUrls[i] = url;
				i += 1;
				
			}while(c.moveToNext());
		}
		c.close();
		c2.close();
	}
}
