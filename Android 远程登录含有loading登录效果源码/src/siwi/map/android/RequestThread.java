//package siwi.map.android;
//
//import java.io.DataInputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.concurrent.Callable;
//
//import android.app.Activity;
//import android.util.Log;
//
///**用于在后台请求服务器是否登录成功*/
//public class RequestThread  extends Activity  implements Callable<Boolean[]> {
//	private String userName;
//	private String password;
//	private boolean loginState;
//	private boolean isNetError;
//
//	public RequestThread(String userName,String password) {
//		this.userName=userName;
//		this.password=password;
//	}
//	
//	@Override
//	/**返回[0]登录状态,[1]是否网络错误*/
//	public Boolean[] call() throws Exception {
//		// 用于标记登陆状态
//		boolean[] result=new boolean[2]; 
//		boolean loginState = false;
//		HttpURLConnection conn = null;
//		DataInputStream dis = null;
//		String validateUrl="http://192.168.1.229:8080/android_server/LoginValidate?userName="
//			+ userName + "&password=" + password;
//		try {
//			URL url = new URL(validateUrl);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(3000);
//			conn.setRequestMethod("GET");
//			conn.connect();
//			dis = new DataInputStream(conn.getInputStream());
//			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//				Log.d(this.toString(),
//						"getResponseCode() not HttpURLConnection.HTTP_OK");
//				isNetError = true;
//				return false;
//			}
//			// 读取服务器的登录状态码
//			int loginStateInt = dis.readInt();
//			if (loginStateInt > 0) {
//				loginState = true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			isNetError = true;
//			Log.d(this.toString(), e.getMessage() + "  127 line");
//		} finally {
//			if (conn != null) {
//				conn.disconnect();
//			}
//		}
//		// 登陆成功
//		if (loginState) {
//			if (isRememberMe()) {
//				saveSharePreferences(true, true);
//			} else {
//				saveSharePreferences(true, false);
//			}
//		} else {
//			// 如果不是网络错误
//			if (!isNetError) {
//				clearSharePassword();
//			}
//		}
//		if (!view_rememberMe.isChecked()) {
//			clearSharePassword();
//		}
//		return loginState;
//	}
//
//}
