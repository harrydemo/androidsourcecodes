package com.never.map;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoutePlan;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;

public class BuslineSearch extends MapActivity {
	Button mBtnSearch = null; // 搜索按钮

	MapView mMapView = null; // 地图View
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	String mCityName = null;
	LocationListener loc_listener;
	App app = null;
	static boolean flag = false;
	static Thread thread;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buslinesearch);

		app = (App) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new App.MyGeneralListener());
		}
		app.mBMapMan.start();
		// 如果使用地图SDK，请初始化地图Activity
		super.initMapActivity(app.mBMapMan);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mMapView.setDrawOverlayWhenZooming(true);
		mMapView.setBuiltInZoomControls(true);
		// 初始化搜索模块，注册事件监听
		MapController mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point); // 设置地图中心点
		mMapController.setZoom(15); // 设置地图zoom级别
		mSearch = new MKSearch();
		mSearch.init(app.mBMapMan, new MKSearchListener() {
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(BuslineSearch.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				// System.out.println(res.toString());
				// 找到公交路线poi node
				MKPoiInfo curPoi = null;
				int totalPoiNum = res.getNumPois();
				for (int idx = 0; idx < totalPoiNum; idx++) {
					Log.d("busline", "the busline is " + idx);
					curPoi = res.getPoi(idx);
					if (2 == curPoi.ePoiType) {
						break;
					}
				}
				mSearch.busLineSearch(mCityName, curPoi.uid);
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				res.getPlan(0).getDistance();
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				if (iError != 0 || result == null) {
					Toast.makeText(BuslineSearch.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				// result.getBusRoute().get
				// result.getBusRoute().getStart().toString();
				CustomRouteOverLay routeOverlay = new CustomRouteOverLay(
						BuslineSearch.this, mMapView);
				// 此处仅展示一个方案作为示例
				System.out.println(routeOverlay.size());
				routeOverlay.setData(result.getBusRoute());
				mMapView.getOverlays().clear();
				System.out.println(mMapView.getOverlays().size());
				mMapView.getOverlays().add(routeOverlay);
				mMapView.invalidate();
				mMapView.getController().animateTo(
						result.getBusRoute().getStart());
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				// TODO Auto-generated method stub

			}
		});

		// mLocationManager.requestLocationUpdates(listener);
		// 注册定位事件
		loc_listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					String strLog = String.format("您当前的位置:\r\n" + "纬度:%f\r\n"
							+ "经度:%f", location.getLongitude(),
							location.getLatitude());
					flag = true;
					Drawable marker = getResources()
							.getDrawable(R.drawable.ic_launcher);
					final GeoPoint p = new GeoPoint(
							(int) (location.getLatitude() * 1E6),
							(int) (location.getLongitude() * 1E6));
					CustomOverlayItem item = new CustomOverlayItem(marker,
							BuslineSearch.this, p, "我的位置", "", false);
					mMapView.getOverlays().add(item);
					mMapView.getController().animateTo(p);
				}
			}
		};
		// 设定搜索按钮的响应
		mBtnSearch = (Button) findViewById(R.id.search);

		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				SearchButtonProcess(v);
			}
		};

		mBtnSearch.setOnClickListener(clickListener);
	}

	void SearchButtonProcess(View v) {
		if (mBtnSearch.equals(v)) {
			mMapView.getOverlays().clear();
			mMapView.getOverlays().removeAll(mMapView.getOverlays());
			mMapView.invalidate();
			EditText editCity = (EditText) findViewById(R.id.city);
			EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
			mCityName = editCity.getText().toString();
			mSearch.poiSearchInCity(mCityName, editSearchKey.getText()
					.toString());
		}
	}

	@Override
	protected void onPause() {
		if (null == app)
			app = (App) this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(loc_listener);
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (null == app)
			app = (App) this.getApplication();
		app.mBMapMan.start();
		super.onResume();
		app.mBMapMan.getLocationManager().requestLocationUpdates(loc_listener);// 定位
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
