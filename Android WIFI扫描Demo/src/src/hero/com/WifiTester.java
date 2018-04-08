package src.hero.com;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/*
 *信号强度是负数 wifiinfo.getRssi()这个方法决定的
 *得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，
 *-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线。 
 */
public class WifiTester extends Activity
{
	private Button btn_scan;

	/** 定义WifiManager对象 */
	private WifiManager mainWifi;

	/** 扫描完毕接收器 */
	private WifiReceiver receiverWifi;

	/** 扫描出的网络连接列表 */
	private List<ScanResult> wifiList;

	private ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		((WifiTesterApp) getApplication()).setWifiTester(WifiTester.this);// 设定实例，供其他类调用

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		receiverWifi = new WifiReceiver();

		btn_scan = (Button) this.findViewById(R.id.button_scran);
		btn_scan.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				scanWifi();
			}
		});
	}

	/**
	 * 扫描WIFI 加载进度条
	 */
	void scanWifi()
	{
		OpenWifi();
		mainWifi.startScan();
		dialog = ProgressDialog.show(WifiTester.this, "", "正在扫描WIFI热点,请稍候");
	}

	/**
	 * 打开WIFI
	 */
	public void OpenWifi()
	{

		if (!mainWifi.isWifiEnabled())
		{
			mainWifi.setWifiEnabled(true);
		}

	}

	/**
	 * 关闭WIFI
	 */
	public void CloseWifi()
	{

		if (mainWifi.isWifiEnabled())
		{
			mainWifi.setWifiEnabled(false);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 0, 0, "重新扫描");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		scanWifi();
		return super.onMenuItemSelected(featureId, item);
	}

	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(receiverWifi);// 注销广播
	}

	protected void onResume()
	{
		super.onResume();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));// 注册广播
	}

	class WifiReceiver extends BroadcastReceiver
	{
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
			{
				wifiList = mainWifi.getScanResults();
				dialog.dismiss();
				Toast.makeText(context, "扫描完毕", Toast.LENGTH_LONG).show();
				Intent in = new Intent();
				in.setClass(WifiTester.this, ListOk.class);
				WifiTester.this.startActivity(in);
			}

		}
	}

	public List<ScanResult> getWifiList()
	{
		return wifiList;
	}

	public void setWifiList(List<ScanResult> wifiList)
	{
		this.wifiList = wifiList;
	}

	public WifiManager getMainWifi()
	{
		return mainWifi;
	}

	public void setMainWifi(WifiManager mainWifi)
	{
		this.mainWifi = mainWifi;
	}

}