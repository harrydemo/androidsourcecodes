package com.catt.oss.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.catt.oss.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

public class MyService extends Activity {
	public static Activity activity;
	public Context con;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initContext(Context context) {
		con = context;
	}

	public static String post(Context context, String url,
			Map<String, Object> map) {
		StringBuffer gdata = new StringBuffer();
		BasicHttpParams basichttpParams = new BasicHttpParams();
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		Set<Map.Entry<String, Object>> entries = map.entrySet();
		for (Map.Entry<String, Object> entry : entries) {
			Object value = entry.getValue();
			String getValue = null;
			if (value instanceof Integer) {
				getValue = String.valueOf(value);
			} else {
				getValue = (String) value;
			}
			param.add(new BasicNameValuePair(entry.getKey(), getValue));
		}
		HttpPost httpPost = null;
		int res = 0;
		String informations = null;
		try {
			if (isHavingNet(context)) {
				// Set the default socket timeout (SO_TIMEOUT)
				HttpConnectionParams.setConnectionTimeout(basichttpParams,
						30000);
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(basichttpParams, 20000);
				DefaultHttpClient httpClient = new DefaultHttpClient(
						basichttpParams);
				if (url != null) {
					httpPost = new HttpPost(url);
					httpPost.setEntity(new UrlEncodedFormEntity(param,
							HTTP.UTF_8));
					res = httpClient.execute(httpPost).getStatusLine()
							.getStatusCode();
					if (res == 200) {
						/*
						 * 当返回码为200时，做处理 得到服务器端返回json数据，并做处理
						 */
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						StringBuilder builder = new StringBuilder();
						String str2 = null;
						str2 = EntityUtils.toString(httpResponse.getEntity());
						if (str2.trim().length() > 0) {
							builder.append(str2);
						}
						informations = builder.toString();
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			timeOutServer(context);
		}
		return informations;
	}

	// http://192.168.168.185:9996/oss/test.do?a=xxx&b=yyy
	// http://192.168.168.185:8080/oss/test.do;jsessionid=?a=xxx&b=yyy
	public static String get(Context context, String url) {
		BasicHttpParams basichttpParams = new BasicHttpParams();
		HttpGet httpGet = null;
		String informations = null;
		try {
			if (isHavingNet(context)) {
				// Set the default socket timeout (SO_TIMEOUT)
				HttpConnectionParams.setConnectionTimeout(basichttpParams,
						600000);
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(basichttpParams, 20000);
				DefaultHttpClient httpClient = new DefaultHttpClient(
						basichttpParams);
				if (url != null) {
					httpGet = new HttpGet(url);
					int res = 0;
					res = httpClient.execute(httpGet).getStatusLine()
							.getStatusCode();
					if (res == 200) {
						/*
						 * 当返回码为200时，做处理 得到服务器端返回json数据，并做处理
						 */
						HttpResponse httpResponse = httpClient.execute(httpGet);
						StringBuilder builder = new StringBuilder();
						String str2 = null;
						str2 = EntityUtils.toString(httpResponse.getEntity());
						if (str2.trim().length() > 0) {
							builder.append(str2);
						}
						informations = builder.toString();
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			timeOutServer(context);
		}
		return informations;
	}

	public static String getBasePath(String server_ip, String server_port) {
		String baseUrl = "http://";
		baseUrl = baseUrl + server_ip + ":" + server_port;
		return baseUrl;
	}

	public static String addSessionIdUrl(Context con, String url) {
		String addSessionIdurl = null;
		SharePrefer shareSession = new SharePrefer(con);
		String sessionId = shareSession.readSessionID();
		if (url.lastIndexOf("?") > 0) {
			int index = url.indexOf("?");
			String firstUrl = url.substring(0, index);
			String nexUrl = url.substring(index);
			// String res[]=url.split("/?");
			addSessionIdurl = firstUrl + ";" + "jsessionid=" + sessionId
					+ nexUrl;
		} else {
			addSessionIdurl = url + ";" + "jsessionid=" + sessionId;
		}
		return addSessionIdurl;
	}

	public static String checkLoginTel(Context context, String url,
			String action, String staffAccount) {
		StringBuffer gdata = new StringBuffer();
		BasicHttpParams basichttpParams = new BasicHttpParams();
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("action", action));
		param.add(new BasicNameValuePair("userName", staffAccount));
		HttpPost httpPost = null;
		int res = 0;
		String informations = null;
		try {
			if (isHavingNet(context)) {
				// Set the default socket timeout (SO_TIMEOUT)
				HttpConnectionParams.setConnectionTimeout(basichttpParams,
						20000);
				// in milliseconds which is the timeout for waiting for data.
				HttpConnectionParams.setSoTimeout(basichttpParams, 60000);
				DefaultHttpClient httpClient = new DefaultHttpClient(
						basichttpParams);
				if (url != null) {
					httpPost = new HttpPost(url);
					httpPost.setEntity(new UrlEncodedFormEntity(param,
							HTTP.UTF_8));
					res = httpClient.execute(httpPost).getStatusLine()
							.getStatusCode();
					if (res == 200) {
						/*
						 * 当返回码为200时，做处理 得到服务器端返回json数据，并做处理
						 */
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						StringBuilder builder = new StringBuilder();
						String str2 = null;
						str2 = EntityUtils.toString(httpResponse.getEntity());
						if (str2.trim().length() > 0) {
							builder.append(str2);
						}
						informations = builder.toString();
						JSONObject jsonObject = new JSONObject(informations
								.toString());
						String result;
						String data;
						result = jsonObject.getString("result");
						data = jsonObject.getString("data");
						gdata.append(result).append("/").append(data);
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			timeOutServer(context);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gdata.toString();

	}

	private static boolean isHavingNet(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取网络对象
		NetworkInfo netInfo = mgr.getActiveNetworkInfo();
		// 获取网络状态
		boolean netSataus = false;
		if (netInfo != null) {
			netSataus = netInfo.isAvailable();

		}
		if (!netSataus) {

			Builder b = new AlertDialog.Builder(context).setTitle("网络异常")

			.setMessage("手机无信号！");

			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			}).show();
			return false;

		} else {
			return true;
		}
	}

	public static boolean timeOutServer(Context context) {
		activity = (Activity) context;
		Builder b = new AlertDialog.Builder(context).setTitle("发生错误")
				.setMessage("请联系管理员！");
		b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				activity.finish();
			}
		}).show();
		return false;

	}

	public static boolean noMobileDataDisplay(Context context) {
		activity = (Activity) context;
		Builder b = new AlertDialog.Builder(context).setTitle("暂无数据")
				.setMessage("返回上一级！");
		b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				activity.finish();
			}
		}).show();
		return false;

	}

	public static boolean isNotNewVersion(Context context, String version) {
		boolean flag = false;
		activity = (Activity) context;
		String server_version = activity.getResources().getString(
				R.string.version);
		if (server_version.equals(version)) {
			flag = true;
		}
		return flag;
	}
	protected File downLoadFile(Context context, String httpUrl) {
		// TODO Auto-generated method stub
		final String fileName = "updata.apk";
		File tmpFile = new File("/sdcard/update");
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		final File file = new File("/sdcard/update/" + fileName);

		try {
			URL url = new URL(httpUrl);
			try {
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[256];
				conn.connect();
				double count = 0;
				if (conn.getResponseCode() >= 400) {
					Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
				} else {
					while (count <= 100) {
						if (is != null) {
							int numRead = is.read(buf);
							if (numRead <= 0) {
								break;
							} else {
								fos.write(buf, 0, numRead);
							}

						} else {
							break;
						}
					}
				}

				conn.disconnect();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return file;
	}

	// 打开APK程序代码

	private void openFile(File file) {
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public static String getUrl(Context context,String url) {
		String server_ip = context.getResources().getString(R.string.server_ip);
		String server_port = context.getResources().getString(R.string.server_port);
		String baseUrl = getBasePath(server_ip, server_port);
		url = baseUrl + url;
		return url;
	}
}
