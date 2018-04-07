package com.baidu.mapapi.demo;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocation extends Activity {
	
	LocationListener mLocationListener = null;//create时注册此listener，Destroy时需要Remove
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
		

        // 注册定位事件
        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("您当前的位置:\r\n" +
							"纬度:%f\r\n" +
							"经度:%f",
							location.getLongitude(), location.getLatitude());

					TextView mainText = (TextView)findViewById(R.id.textview);
			        mainText.setText(strLog);
				}
			}
        };
	}

	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// 移除listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// 注册Listener
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}
}
