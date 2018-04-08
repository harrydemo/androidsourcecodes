package src.hero.com;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListOk extends ListActivity implements OnItemClickListener
{

	private ListView lv;

	private WifiTester wifiTester;

	// -----------------------连接WIFI
	private ScanResult scanRet;

	private WifiConfiguration wc;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setTitle("当前可用的WIFI列表");

		WifiTesterApp _TestActivityApp = (WifiTesterApp) this.getApplication();
		wifiTester = (_TestActivityApp).getWifiTester();

		wc = new WifiConfiguration();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				getString(wifiTester.getWifiList()));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	public String[] getString(List<ScanResult> wifiList)
	{
		ArrayList<String> listStr = new ArrayList<String>();

		for (int i = 0; i < wifiList.size(); i++)
		{
			listStr.add(wifiList.get(i).toString());
		}
		return listStr.toArray(new String[0]);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{

		scanRet = wifiTester.getWifiList().get(position);
		wc.SSID = "\"" + scanRet.SSID + "\""; // 配置wifi的SSID，即该热点的名称，如：TP-link_xxx
		wc.preSharedKey = "\"7675781777\""; // 该热点的密码
		wc.hiddenSSID = true;
		wc.status = WifiConfiguration.Status.ENABLED;
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		// int res = wifiTester.getMainWifi().addNetwork(wc);
		// Log.d("Wif iPreference", "1111111add Network returned " + res);
		// boolean b = wifiTester.getMainWifi().enableNetwork(res, true);
		// Log.d("WifiPreference", "2222222222enableNetwork returned " + b);

	}
}
