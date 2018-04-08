package src.hero.com;

import android.app.Application;

public class WifiTesterApp extends Application
{

	private WifiTester wifiTester = null;

	public WifiTester getWifiTester()
	{
		return wifiTester;
	}

	public void setWifiTester(WifiTester wifiTester)
	{
		this.wifiTester = wifiTester;
	}
}
