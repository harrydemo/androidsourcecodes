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
	// 定义地图引擎管理类
	private BMapManager mapManager;// 定义搜索服务类
	private MKSearch mMKSearch;
	private MapView mapView;
	private MapController mapController;

	LocationListener mLocationListener = null;// onResume时注册此listener，onPause时需要Remove
	MyLocationOverlay mLocationOverlay = null; // 定位图层

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 初始化MapActivity
		mapManager = new BMapManager(getApplication());

		// init方法的第一个参数需填入申请的APIKey
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);
		mapView = (MapView) findViewById(R.id.map_View);
		// 设置地图模式为交通地图
		mapView.setTraffic(true);
		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mapView.setDrawOverlayWhenZooming(true);

		// 添加定位图层
		mLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(mLocationOverlay);

		// 注册定位事件
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					mapView.getController().animateTo(geoPoint);
					mapController = mapView.getController();
					// 设置地图的中心
					mapController.setCenter(geoPoint);
					// 设置地图默认的缩放级别
					mapController.setZoom(16);
					// 初始化
					MKSearch mMKSearch = new MKSearch();
					mMKSearch.init(mapManager, new MySearchListener());
					// 搜索贵州大学校门口附近500米范围的自动取款机
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
			// 程序退出前需调用此方法
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			// 终止百度地图API
			mapManager.getLocationManager().removeUpdates(mLocationListener);
			mLocationOverlay.disableMyLocation();
	        mLocationOverlay.disableCompass(); // 关闭指南针
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			// 开启百度地图API
			// 注册定位事件，定位后将地图移动到定位点
			mapManager.getLocationManager().requestLocationUpdates(mLocationListener);
			mLocationOverlay.enableMyLocation();
			mLocationOverlay.enableCompass(); // 打开指南针
			mapManager.start();
		}
		super.onResume();
	}

	/**
	 * * 实现MKSearchListener接口,用于实现异步搜索服务 * @author liufeng
	 */
	public class MySearchListener implements MKSearchListener {
		/** * 根据经纬度搜索地址信息结果 * * @param result 搜索结果 * @param iError 错误号 （0表示正确返回） */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
		}

		/** * 驾车路线搜索结果 * * @param result 搜索结果 * @param iError 错误号 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
		}

		/**
		 * * POI搜索结果（范围检索、城市POI检索、周边检索） * * @param result 搜索结果 * @param type
		 * 返回结果类型（11,12,21:poi列表 7:城市列表） * @param iError 错误号（0表示正确返回）
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			if (result == null) {
				return;
			}
			// PoiOverlay是baidu map api提供的用于显示POI的Overlay
			PoiOverlay poioverlay = new PoiOverlay(PoiSearchActivity.this, mapView);
			// 设置搜索到的POI数据
			poioverlay.setData(result.getAllPoi());
			// 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）
			mapView.getOverlays().add(poioverlay);
		}

		/** * 公交换乘路线搜索结果 * * @param result 搜索结果 * @param iError 错误号（0表示正确返回） */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
		}

		/** * 步行路线搜索结果 * * @param result 搜索结果 * @param iError 错误号（0表示正确返回） */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
		}
	}
}