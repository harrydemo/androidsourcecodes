package com.parabola.main;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class WeatherInfoActivity extends TabActivity implements
		OnTabChangeListener {
	public static final String TAG = "WeatherInfoActivity";
	WebServiceHelper wsh;
	WeatherBean bean = new WeatherBean();
	TabHost mTabHost;
	String city;

	TextView txt1, txt2, txt3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.weatherinfo);

		Intent i = this.getIntent();
		if (i != null) {
			Bundle bd = i.getExtras();
			if (bd != null) {
				if (bd.containsKey("city")) {
					city = bd.getString("city");
				}
			}
		}

		mTabHost = getTabHost();
		// 设置容器的背景色
		mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));

		mTabHost.addTab(mTabHost
				.newTabSpec("one")
				.setIndicator("天气", getResources().getDrawable(R.drawable.icon))
				.setContent(R.id.lin1));
		mTabHost.addTab(mTabHost
				.newTabSpec("two")
				.setIndicator("天气情况",
						getResources().getDrawable(R.drawable.icon))
				.setContent(R.id.textview2));
		mTabHost.addTab(mTabHost
				.newTabSpec("three")
				.setIndicator("城市信息",
						getResources().getDrawable(R.drawable.icon))
				.setContent(R.id.textview3));

		mTabHost.setCurrentTab(0);

		mTabHost.setOnTabChangedListener(this);

		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						try {
							SoapObject msg = new SoapObject(
									"http://WebXml.com.cn/",
									"getWeatherbyCityName");
							msg.addProperty("theCityName", city);

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							envelope.bodyOut = msg;
							envelope.dotNet = true;

							AndroidHttpTransport sendRequest = new AndroidHttpTransport(
									"http://www.webxml.com.cn/webservices/weatherwebservice.asmx");
							envelope.setOutputSoapObject(msg);
							sendRequest
									.call("http://WebXml.com.cn/getWeatherbyCityName",
											envelope);

							SoapObject result = (SoapObject) envelope.bodyIn;
							SoapObject detail = (SoapObject) result
									.getProperty("getWeatherbyCityNameResult");
							System.out.println(detail + "");

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();

			}

		});
	}

	public void init() {
		txt1 = (TextView) this.findViewById(R.id.textview1);
		txt2 = (TextView) this.findViewById(R.id.textview2);
		txt3 = (TextView) this.findViewById(R.id.textview3);
	}

	@Override
	public void onTabChanged(String tabId) {
		WebServiceHelper wsh = new WebServiceHelper();

		// 如果是天气选项卡，就获取天气
		if (tabId.equals("one")) {

			// SimpleAdapter adapter = new SimpleAdapter(this, weather_list_map,
			// R.layout.listview_item,
			// new String[]{"weatherDay","icons"},
			// new int[]{R.id.info_weather,R.id.image_weather});
			// mListView_weather.setAdapter(adapter);
			
		}

		// 如果是天气情况选项卡，就获取天气情况
		if (tabId.equals("two")) {
			wsh.getWeatherByCityTest(city);

		}

		// 如果是城市信息选项卡，就获取城市信息
		if (tabId.equals("three")) {
			bean = wsh.getWeatherByCity(city);
			

			String liveWeather = bean.getLiveWeather(); // 天气
			String cityDes = bean.getCityDescription();
			String cityInfo = bean.getCityName(); // 城市信息

			Log.i(TAG, "liveWeather = " + liveWeather);
			Log.i(TAG, "cityDes = " + cityDes);
			Log.i(TAG, "cityInfo = " + cityInfo);
			
		}

	}

}
