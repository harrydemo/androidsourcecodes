package com.np.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.np.data.DBAdapter;
import com.np.other.Alert;
import com.np.other.ToastSeal;
import com.np.other.Alert.AlertListener;
import com.wb.np.R;

public class Main extends Activity {
	private Main th;
	private LinearLayout layoutText;
	private EditText edit;
	private Button bt;
//	private ImageView img360;
	private Handler myHandler;
	private DBAdapter db;
	private Cursor cursor;

	private String strArray[];
	private String phone;
	private String strUrl;
	private String phoneInfo[];

	private final int start = 0x01;
	private final int GUANYU = Menu.NONE;
	private final int HELP = Menu.FIRST;
	private final int EXIT = Menu.FIRST + 1;

	private final int LOOK = Menu.FIRST + 2;
	private final int SAVING = Menu.FIRST + 3;

	private final int backgroundColor1 = 0xFF000000;
	private final int backgroundColor2 = 0xFF313031;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		th = this;
		setContentView(R.layout.main);
		init();

		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myHandler.sendEmptyMessage(start);
			}
		});

		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case start:
					getPhoneInfo();
					break;
				}
				super.handleMessage(msg);
			}
		};

//		img360 = (ImageView) this.findViewById(R.id.layoutAdView);
//		img360.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// Intent intent = new
//				// Intent(Intent.ACTION_VIEW,Uri.parse("http://m.360.cn/partner/xxs.wml"));
//				Intent intent = new Intent(
//						Intent.ACTION_VIEW,
//						Uri.parse("http://shouji.360.cn/partner/html/101089.web.html"));
//				th.startActivity(intent);
//			}
//		});
	}

	private void init() {
		edit = (EditText) this.findViewById(R.id.edit);
		bt = (Button) this.findViewById(R.id.bt);
		layoutText = (LinearLayout) this.findViewById(R.id.layoutText);
		strArray = th.getResources().getStringArray(R.array.code);
		// strUrl = "http://api.showji.com/locating/?m=";
		strUrl = "http://api.liqwei.com/location/?mobile=";
		db = new DBAdapter(th).open();
	}

	private void getPhoneInfo(){
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		HttpURLConnection conn = null;
		phone = edit.getText().toString();
		if (phone.length() < 3) {
			Toast.makeText(th, R.string.editError, 1).show();
			return;
		}
		layoutText.removeAllViews();
		for(int i=0;i<4;i++){
			try {
				if (i % 2 == 0){
					URL url = new URL(strUrl + phone);
					conn = (HttpURLConnection) url.openConnection();
				} else {
					InetSocketAddress address = new InetSocketAddress("10.0.0.172", 80);
					Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
					proxyAuthenticator();
					conn = (HttpURLConnection) new URL(strUrl + phone).openConnection(proxy);
				}
				conn.setDoInput(true);
				conn.setConnectTimeout(2000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("accept","*/*");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.connect();
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = conn.getInputStream();
					bos = new ByteArrayOutputStream();
					byte b[] = new byte[255];
					int c = -1;
					while ((c = is.read(b)) != -1) {
						bos.write(b, 0, c);
					}
					byte buf[] = bos.toByteArray();
					phoneInfo = new String(buf,"GBK").split(",");
					if(phoneInfo == null){
						new Alert(th).setBuilder(0, null, "联网失败，请稍后再试！", null, null);
						return;
					}else if(phoneInfo.length < 7){
						String s[] = phoneInfo;
						phoneInfo = new String[7];
						for(int k=0;k<phoneInfo.length;k++){
							if(k<s.length)
								phoneInfo[k] = s[k];
							else
								phoneInfo[k] = "";
						}
					}
					for (int j = 0; j < phoneInfo.length; j++){
						View view = View.inflate(th, R.layout.text, null);
						LinearLayout linear = (LinearLayout) view.findViewById(R.id.layoutInfo);
						TextView text1 = (TextView) view.findViewById(R.id.text1);
						TextView text2 = (TextView) view.findViewById(R.id.text2);
						if (j % 2 == 0)
							linear.setBackgroundColor(backgroundColor1);
						else {
							linear.setBackgroundColor(backgroundColor2);
						}
						if (j < strArray.length)
							text1.setText(strArray[j]);
						text2.setText(phoneInfo[j]);
						layoutText.addView(view);
					}
					return;
				}
			} catch (SocketTimeoutException t) {
			}catch (IOException e) {
			} finally {
				try {
					if (is != null) {is.close();is = null;}
					if (bos != null) {bos.close();bos = null;}
				} catch (IOException e) {
				}
			}
			new Alert(th).setBuilder(0, null, "联网失败，请稍后再试！", null, null);
			return;
		}
	}
	
	private void proxyAuthenticator(){
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("username", "password".toCharArray());
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, GUANYU, 1, R.string.guanyu).setIcon(
				R.drawable.ic_menu_info_details);
		menu.add(1, HELP, 1, R.string.help).setIcon(R.drawable.ic_menu_help);
		menu.add(2, EXIT, 1, R.string.exit).setIcon(R.drawable.ic_menu_revert);

		menu.add(0, LOOK, 0, R.string.look).setIcon(R.drawable.ic_menu_archive);
		menu.add(1, SAVING, 0, R.string.saving).setIcon(
				R.drawable.ic_menu_slideshow);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case GUANYU:
			new Alert(th).setBuilder(0, item.getTitle().toString(), "软件版本："
					+ getAppVersionName(th) + "\n开发者：明" + "\nQQ：1469567153",
					null, null);
			break;
		case HELP:
			new Alert(th)
					.setBuilder(
							0,
							item.getTitle().toString(),
							"手机号码参数，可以识别大多数的手机号码格式，全角半角均可，例如：139、１３９１２３４、13912345678、0139-1234-5678 等。",
							null, null);
			break;
		case EXIT:
			exit(th);
			break;
		case LOOK:
			Intent intent = new Intent();
			intent.setClass(th, SavingList.class);
			startActivity(intent);
			break;
		case SAVING:
			saving();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void exit(final Context context) {
		Alert alert = new Alert(context);
		alert.setBuilder(0, null, "是否确定要退出?", "确定", "取消");
		alert.setAlertListenet(new AlertListener() {
			public void setOnListenetr(int button) {
				if (button == Alert.LEFT) {
					db.close();
					((Activity) context).finish();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit(th);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 保存 */
	private void saving() {
		if (phoneInfo != null) {
			cursor = db.getAllTitles();
			if (cursor.moveToFirst()) {
				do {
					if (phoneInfo[0].equals(cursor.getString(cursor
							.getColumnIndex(DBAdapter.KEY_MOBILE)))) {
						ToastSeal.toast(th, "信息已存在，不需要保存", 1);
						return;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.insertTitle(phoneInfo);
			ToastSeal.toast(th, "保存成功", 1);
		} else {
			ToastSeal.toast(th, "没有信息需要保存", 1);
		}
	}

	/**
	 * 检查程序版本信息
	 * 
	 * @param context
	 * @return 程序版本
	 */
	public String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "1.0";
			}
		} catch (Exception e) {
			Log.e("", "Exception", e);
		}
		return versionName;
	}
}