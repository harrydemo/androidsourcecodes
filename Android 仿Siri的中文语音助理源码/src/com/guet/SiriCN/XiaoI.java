/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class XiaoI {

	private String Webbot_Path = "http://webbot.xiaoi.com/engine/widget1007/webbot.js?encoding=utf-8";
	private String Send_Path = "http://122.226.240.164/engine/widget1007/send.js?encoding=utf-8&";
	private String Recv_Path = "http://122.226.240.164/engine/widget1007/recv.js?encoding=utf-8&";

	private String mSessionId = null;
	private HttpClient httpClient = null;

	public boolean initialize() {
		boolean success=false;	
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		httpClient = new DefaultHttpClient(httpParams);
		try {
			String strGetId = Webbot_Path;
			HttpGet httpRequest = new HttpGet(strGetId);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				Pattern p = Pattern.compile("sessionId = .(\\d+)"); //get sessionId
				Matcher m = p.matcher(strResult);
				if (m.find()) {
					mSessionId = m.group(1);
					String strSendJoin = Send_Path + "SID=" + mSessionId
							+ "&USR=" + mSessionId + "&CMD=JOIN&r=";
					HttpGet httpRequest1 = new HttpGet(strSendJoin);
					httpResponse = httpClient.execute(httpRequest1);

					String strRevAsk = Recv_Path + "SID=" + mSessionId
							+ "&USR=" + mSessionId + "&r=";
					HttpGet httpRequest2 = new HttpGet(strRevAsk);
					httpResponse = httpClient.execute(httpRequest2);
					success=true;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return success;
		}
	}

	public void sendMsg(String msg) {
		String strTalksend = Send_Path + "SID=" + mSessionId + "&USR="
				+ mSessionId + "&CMD=CHAT&SIG=You&MSG=" + msg
				+ "&FTN=&FTS=&FTC=&r=";
		HttpGet httpRequest = new HttpGet(strTalksend);
		try {
			httpClient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String revMsg() {
		String strTalkRec = Recv_Path + "SID=" + mSessionId + "&USR="
				+ mSessionId + "&r=";
		HttpGet httpRequest = new HttpGet(strTalkRec);
		String msg = null;
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String msgTmp = EntityUtils.toString(httpResponse.getEntity());
				Pattern p = Pattern.compile("\"MSG\":\"(.*?)\"");
				Matcher m = p.matcher(msgTmp);
				if (m.find()) {
					msg = m.group(1);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;

	}
}