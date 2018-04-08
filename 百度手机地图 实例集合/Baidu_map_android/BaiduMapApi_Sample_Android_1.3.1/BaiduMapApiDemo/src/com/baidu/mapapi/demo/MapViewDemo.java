package com.baidu.mapapi.demo;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;

import android.os.Bundle;
import android.util.Log;

public class MapViewDemo extends MapActivity {

	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MapViewDemo", "onCreate");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewdemo);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        
        MapView mapView = (MapView)findViewById(R.id.bmapView);
        mapView.setBuiltInZoomControls(true);
        //mapView.setTraffic(true);
	}

	@Override
	protected void onPause() {
		Log.d("MapViewDemo", "onPause");
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		Log.d("MapViewDemo", "onResume");
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
