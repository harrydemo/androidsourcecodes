package com.pixtas.smsfilter.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.pixtas.smsfilter.Config;




public class RestAPI {
	private static String TAG = "RestAPI";
	
	public static boolean checkApkUpgrade(){
		boolean arg = true;
		HttpPost post = new HttpPost(Config.check_version_url);
		List <NameValuePair> vars = new ArrayList <NameValuePair>();
		vars.add(new BasicNameValuePair("app_name", Config.appName));
		vars.add(new BasicNameValuePair("version", Config.version));
		 try {
				post.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));
				JSONObject jo = execute(post);
				arg = jo.getJSONObject("ret").getBoolean("can_run");
				if(Config.debug){
					Log.v(TAG,jo.toString());
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
}
