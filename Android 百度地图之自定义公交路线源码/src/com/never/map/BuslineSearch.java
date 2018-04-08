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
	Button mBtnSearch = null; // ������ť

	MapView mMapView = null; // ��ͼView
	MKSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
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
		// ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
		super.initMapActivity(app.mBMapMan);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// ���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
		mMapView.setDrawOverlayWhenZooming(true);
		mMapView.setBuiltInZoomControls(true);
		// ��ʼ������ģ�飬ע���¼�����
		MapController mMapController = mMapView.getController(); // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6)); // �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(point); // ���õ�ͼ���ĵ�
		mMapController.setZoom(15); // ���õ�ͼzoom����
		mSearch = new MKSearch();
		mSearch.init(app.mBMapMan, new MKSearchListener() {
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(BuslineSearch.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_LONG).show();
					return;
				}
				// System.out.println(res.toString());
				// �ҵ�����·��poi node
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
					Toast.makeText(BuslineSearch.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_LONG).show();
					return;
				}
				// result.getBusRoute().get
				// result.getBusRoute().getStart().toString();
				CustomRouteOverLay routeOverlay = new CustomRouteOverLay(
						BuslineSearch.this, mMapView);
				// �˴���չʾһ��������Ϊʾ��
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
		// ע�ᶨλ�¼�
		loc_listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					String strLog = String.format("����ǰ��λ��:\r\n" + "γ��:%f\r\n"
							+ "����:%f", location.getLongitude(),
							location.getLatitude());
					flag = true;
					Drawable marker = getResources()
							.getDrawable(R.drawable.ic_launcher);
					final GeoPoint p = new GeoPoint(
							(int) (location.getLatitude() * 1E6),
							(int) (location.getLongitude() * 1E6));
					CustomOverlayItem item = new CustomOverlayItem(marker,
							BuslineSearch.this, p, "�ҵ�λ��", "", false);
					mMapView.getOverlays().add(item);
					mMapView.getController().animateTo(p);
				}
			}
		};
		// �趨������ť����Ӧ
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
		app.mBMapMan.getLocationManager().requestLocationUpdates(loc_listener);// ��λ
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
