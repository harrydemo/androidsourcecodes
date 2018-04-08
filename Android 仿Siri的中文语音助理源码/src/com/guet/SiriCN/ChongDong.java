/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;

public class ChongDong {
	private String WebAPI_Path = "http://wap.unidust.cn/api/searchout.do?type=wap&ch=1001&info=";
	private HttpClient httpClient = null;
	Handler mHandler;
	private ProgressDialog mProgressDialog;
	Context mContext;
	
	public ChongDong(){
		
	}
	public ChongDong(Context context,Handler handler){
		mContext=context;
		mHandler=handler;
	}
	
	public String getResult(String question) {
		String strResult = null;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		httpClient = new DefaultHttpClient(httpParams);
		try {
			String strQuestion = WebAPI_Path + question;
			HttpGet httpRequest = new HttpGet(strQuestion);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				String str = EntityUtils.toString(httpResponse.getEntity());
				strResult = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return strResult;
		}
	}
	
	public Spanned getAnswer(String question){
		String result=getResult(question);
		int firstIndex=result.indexOf("<br/>")+5;
	//	int secondIndex=result.indexOf("<br/>", firstIndex)+5;
		int lastIndex=result.lastIndexOf("<br/>");
		/*if(lastIndex>secondIndex)
		return Html.fromHtml(result.substring(secondIndex,lastIndex));
		else*/
		return Html.fromHtml(result.substring(firstIndex,lastIndex));
	}
	
	public void handleAnswer(String question) {
		mProgressDialog =new ProgressDialog(mContext);
		mProgressDialog.setMessage("这个问题有点难，让我好好想想");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();	
		new ThreadProcess(question).start();
	}
	
	class ThreadProcess extends Thread
	  {
		String question=null;
		public ThreadProcess(String quest){
			question=quest;
		}
		public void run() {	
			String result=getResult(question);
			Message message = new Message();
			message.what = 2013;
			message.obj =getAnswer(result);
			mHandler.sendMessage(message);
			mProgressDialog.dismiss();
		}  
	   }

}
