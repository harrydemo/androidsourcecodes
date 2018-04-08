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
 *�ź�ǿ���Ǹ��� wifiinfo.getRssi()�������������
 *�õ���ֵ��һ��0��-100������ֵ����һ��int�����ݣ�����0��-50��ʾ�ź���ã�
 *-50��-70��ʾ�ź�ƫ�С��-70��ʾ���п������Ӳ��ϻ��ߵ��ߡ� 
 */
public class WifiTester extends Activity
{
	private Button btn_scan;

	/** ����WifiManager���� */
	private WifiManager mainWifi;

	/** ɨ����Ͻ����� */
	private WifiReceiver receiverWifi;

	/** ɨ��������������б� */
	private List<ScanResult> wifiList;

	private ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		((WifiTesterApp) getApplication()).setWifiTester(WifiTester.this);// �趨ʵ���������������

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
	 * ɨ��WIFI ���ؽ�����
	 */
	void scanWifi()
	{
		OpenWifi();
		mainWifi.startScan();
		dialog = ProgressDialog.show(WifiTester.this, "", "����ɨ��WIFI�ȵ�,���Ժ�");
	}

	/**
	 * ��WIFI
	 */
	public void OpenWifi()
	{

		if (!mainWifi.isWifiEnabled())
		{
			mainWifi.setWifiEnabled(true);
		}

	}

	/**
	 * �ر�WIFI
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
		menu.add(0, 0, 0, "����ɨ��");
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
		unregisterReceiver(receiverWifi);// ע���㲥
	}

	protected void onResume()
	{
		super.onResume();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));// ע��㲥
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
				Toast.makeText(context, "ɨ�����", Toast.LENGTH_LONG).show();
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