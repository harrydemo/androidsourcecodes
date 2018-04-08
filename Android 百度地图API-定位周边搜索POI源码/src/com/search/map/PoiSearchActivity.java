package com.search.map;

import android.location.Location;
import android.os.Bundle;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.PoiOverlay;

public class PoiSearchActivity extends MapActivity {
	// �����ͼ���������
	private BMapManager mapManager;// ��������������
	private MKSearch mMKSearch;
	private MapView mapView;
	private MapController mapController;

	LocationListener mLocationListener = null;// onResumeʱע���listener��onPauseʱ��ҪRemove
	MyLocationOverlay mLocationOverlay = null; // ��λͼ��

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// ��ʼ��MapActivity
		mapManager = new BMapManager(getApplication());

		// init�����ĵ�һ�����������������APIKey
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);
		mapView = (MapView) findViewById(R.id.map_View);
		// ���õ�ͼģʽΪ��ͨ��ͼ
		mapView.setTraffic(true);
		// �����������õ����ſؼ�
		mapView.setBuiltInZoomControls(true);
		// ���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
		mapView.setDrawOverlayWhenZooming(true);

		// ��Ӷ�λͼ��
		mLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(mLocationOverlay);

		// ע�ᶨλ�¼�
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mapView.getController().animateTo(geoPoint);
					mapController = mapView.getController();
					// ���õ�ͼ������
					mapController.setCenter(geoPoint);
					// ���õ�ͼĬ�ϵ����ż���
					mapController.setZoom(16);
					// ��ʼ��
					MKSearch mMKSearch = new MKSearch();
					mMKSearch.init(mapManager, new MySearchListener());
					// �������ݴ�ѧУ�ſڸ���500�׷�Χ���Զ�ȡ���
					mMKSearch.poiSearchNearBy("ATM", geoPoint, 500);
				}
			}
		};

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			// �����˳�ǰ����ô˷���
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			// ��ֹ�ٶȵ�ͼAPI
			mapManager.getLocationManager().removeUpdates(mLocationListener);
			mLocationOverlay.disableMyLocation();
	        mLocationOverlay.disableCompass(); // �ر�ָ����
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			// �����ٶȵ�ͼAPI
			// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
			mapManager.getLocationManager().requestLocationUpdates(mLocationListener);
			mLocationOverlay.enableMyLocation();
			mLocationOverlay.enableCompass(); // ��ָ����
			mapManager.start();
		}
		super.onResume();
	}

	/**
	 * * ʵ��MKSearchListener�ӿ�,����ʵ���첽�������� * @author liufeng
	 */
	public class MySearchListener implements MKSearchListener {
		/** * ���ݾ�γ��������ַ��Ϣ��� * * @param result ������� * @param iError ����� ��0��ʾ��ȷ���أ� */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
		}

		/** * �ݳ�·��������� * * @param result ������� * @param iError ����� */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
		}

		/**
		 * * POI�����������Χ����������POI�������ܱ߼����� * * @param result ������� * @param type
		 * ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б� * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			if (result == null) {
				return;
			}
			// PoiOverlay��baidu map api�ṩ��������ʾPOI��Overlay
			PoiOverlay poioverlay = new PoiOverlay(PoiSearchActivity.this, mapView);
			// ������������POI����
			poioverlay.setData(result.getAllPoi());
			// �ڵ�ͼ����ʾPoiOverlay��������������Ȥ���ע�ڵ�ͼ�ϣ�
			mapView.getOverlays().add(poioverlay);
		}

		/** * ��������·��������� * * @param result ������� * @param iError ����ţ�0��ʾ��ȷ���أ� */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
		}

		/** * ����·��������� * * @param result ������� * @param iError ����ţ�0��ʾ��ȷ���أ� */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
		}
	}
}