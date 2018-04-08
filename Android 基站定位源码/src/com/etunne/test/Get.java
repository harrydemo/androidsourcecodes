package com.etunne.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Get {

	public static class CellIDInfo {
		
		public int cellId;
		public String mobileCountryCode;
		public String mobileNetworkCode;
		public int locationAreaCode;
		public String radioType;
		public CellIDInfo(){};
	}
	
	
	
	 public Location callGear(ArrayList<CellIDInfo> cellID) {
//		 System.out.println(cellID.get(0));
	    	if (cellID.isEmpty()) {
	    		return null;
	    	}
	    	DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					"http://www.google.com/loc/json");
			JSONObject holder = new JSONObject();
			try {
				holder.put("version", "1.1.0");
				holder.put("host", "maps.google.com");
				holder.put("home_mobile_country_code", cellID.get(0).mobileCountryCode);
				holder.put("home_mobile_network_code", cellID.get(0).mobileNetworkCode);
				holder.put("radio_type", cellID.get(0).radioType);
				holder.put("request_address", true);
				if ("460".equals(cellID.get(0).mobileCountryCode)) 
					holder.put("address_language", "zh_CN");
				else
					holder.put("address_language", "en_US");
				JSONObject data,current_data;
				JSONArray array = new JSONArray();
				current_data = new JSONObject();
				current_data.put("cell_id", cellID.get(0).cellId);
				current_data.put("location_area_code", cellID.get(0).locationAreaCode);
				current_data.put("mobile_country_code", cellID.get(0).mobileCountryCode);
				current_data.put("mobile_network_code", cellID.get(0).mobileNetworkCode);
				current_data.put("age", 0);
				array.put(current_data);
				if (cellID.size() > 2) {
					for (int i = 1; i < cellID.size(); i++) {
						data = new JSONObject();
						data.put("cell_id", cellID.get(i).cellId);
						data.put("location_area_code", cellID.get(i).locationAreaCode);
						data.put("mobile_country_code", cellID.get(i).mobileCountryCode);
						data.put("mobile_network_code", cellID.get(i).mobileNetworkCode);
						data.put("age", 0);
						array.put(data);
					}
				}
				holder.put("cell_towers", array);
				StringEntity se = new StringEntity(holder.toString());
				Log.e("Location send", holder.toString());
				post.setEntity(se);
				HttpResponse resp = client.execute(post);
				HttpEntity entity = resp.getEntity();

				BufferedReader br = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				StringBuffer sb = new StringBuffer();
				String result = br.readLine();
				while (result != null) {
					Log.e("Locaiton receive", result);
					sb.append(result);
					result = br.readLine();
				}
				if(sb.length() <= 1)
					return null;
				data = new JSONObject(sb.toString());
				data = (JSONObject) data.get("location");

				Location loc = new Location(LocationManager.NETWORK_PROVIDER);
				loc.setLatitude((Double) data.get("latitude"));
				loc.setLongitude((Double) data.get("longitude"));
				loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
				loc.setTime(GetUTCTime());
				return loc;
			} catch (JSONException e) {
				return null;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	    
		/**
		 * 获取地理位置
		 * 
		 * @throws Exception
		 */
		public String getLocation(Location itude) throws Exception {
			String resultString = "";

			/** 这里采用get方法，直接将参数加到URL上 */
			String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.getLatitude(), itude.getLongitude());
			Log.i("URL", urlString);

			/** 新建HttpClient */
			HttpClient client = new DefaultHttpClient();
			/** 采用GET方法 */
			HttpGet get = new HttpGet(urlString);
			try {
				/** 发起GET请求并获得返回数据 */
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
				StringBuffer strBuff = new StringBuffer();
				String result = null;
				while ((result = buffReader.readLine()) != null) {
					strBuff.append(result);
				}
				resultString = strBuff.toString();

				/** 解析JSON数据，获得物理地址 */
				if (resultString != null && resultString.length() > 0) {
					JSONObject jsonobject = new JSONObject(resultString);
					JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
					resultString = "";
					for (int i = 0; i < jsonArray.length(); i++) {
						resultString = jsonArray.getJSONObject(i).getString("address");
					}
				}
			} catch (Exception e) {
				throw new Exception("获取物理位置出现错误:" + e.getMessage());
			} finally {
				get.abort();
				client = null;
			}

			return resultString;
		}
	    
	    public long GetUTCTime() { 
	        Calendar cal = Calendar.getInstance(Locale.CHINA); 
	        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET); 
	        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET); 
	        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset)); 
	        return cal.getTimeInMillis();
	    }
	
}
