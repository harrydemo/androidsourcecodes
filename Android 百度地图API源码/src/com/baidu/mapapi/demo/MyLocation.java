package com.baidu.mapapi.demo;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocation extends Activity {
	
	LocationListener mLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
		

        // ע�ᶨλ�¼�
        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("����ǰ��λ��:\r\n" +
							"γ��:%f\r\n" +
							"����:%f",
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
		// �Ƴ�listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// ע��Listener
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}
}
