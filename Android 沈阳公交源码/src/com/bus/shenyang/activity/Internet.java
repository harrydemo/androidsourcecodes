package com.bus.shenyang.activity;
import com.bus.shenyang.R;
import com.bus.shenyang.net.BusNet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Internet extends Activity {
	TextView editSt, editEn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internet);
		editSt = (TextView) findViewById(R.id.editText1);
		editEn = (TextView) findViewById(R.id.editText2);
		Button search = (Button) findViewById(R.id.button1);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!NetWorkAvailable(Internet.this)) {
					HttpTest(Internet.this);
				} else {
					if ((editSt.getText().toString()).equals("")
							&& (editEn.getText().toString()).equals("")) {
						new AlertDialog.Builder(Internet.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("提示")
						.setMessage("你输入正确的站点")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										
									}

								}).show();
					} else {
						Intent i = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("ststop", editSt.getText().toString());
						bundle.putString("enstop", editEn.getText().toString());
						i.setClass(Internet.this, BusNet.class);
						i.putExtras(bundle);
						System.out.println("11111SSSSSSSSSSSSSSSSS11111111");
						startActivity(i);
					}
				}

			}

			private void HttpTest(final Activity mActivity) {
				if (!NetWorkAvailable(mActivity)) {

					AlertDialog.Builder builders = new AlertDialog.Builder(
							mActivity);
					builders.setTitle("抱歉,网络无效，无法处理您的请求");
					builders.setPositiveButton("设置网络",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									mActivity.finish();
									startActivity(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS));

								}
							});
					builders.setNegativeButton("取消操作", null);
					builders.show();

				}

			}

			private boolean NetWorkAvailable(Activity mActivity) {
				Context context = mActivity.getApplicationContext();
				ConnectivityManager connectivity = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);

				if (connectivity == null) {
					return false;
				} else {
					NetworkInfo[] info = connectivity.getAllNetworkInfo();

					if (info != null) {

						for (int i = 0; i < info.length; i++) {

							if (info[i].getState() == NetworkInfo.State.CONNECTED) {
								return true;
							}
						}
					}
				}
				return false;

			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(Internet.this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("沈阳离线公交")
					.setMessage("你确定退出了哦?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}

							}).setNegativeButton("取消", null).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
