package com.baidu.mapapi.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;

public class GeoCoder extends MapActivity {
	Button mBtnReverseGeoCode = null;	// 将坐标反编码为地址
	Button mBtnGeoCode = null;	// 将地址编码为坐标
	
	MapView mMapView = null;	// 地图View
	MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.geocoder);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        
        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapMan, new MKSearchListener(){
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					Toast.makeText(GeoCoder.this, str, Toast.LENGTH_LONG).show();
					return;
				}

				mMapView.getController().animateTo(res.geoPt);
					
				String strInfo = String.format("纬度：%f 经度：%f\r\n", res.geoPt.getLatitudeE6()/1e6, 
							res.geoPt.getLongitudeE6()/1e6);

				Toast.makeText(GeoCoder.this, strInfo, Toast.LENGTH_LONG).show();
				Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  //得到需要标在地图上的资源
				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
						.getIntrinsicHeight());   //为maker定义位置和边界
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(new OverItemT(marker, GeoCoder.this, res.geoPt, res.strAddr));
			}
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				if (error != 0 || res == null) {
					Toast.makeText(GeoCoder.this, "解析失败", Toast.LENGTH_LONG).show();
					return;
				}
				if (res != null && res.getCurrentNumPois() > 0) {
					GeoPoint ptGeo = res.getAllPoi().get(0).pt;
					// 移动地图到该点：
					mMapView.getController().animateTo(ptGeo);
					
					String strInfo = String.format("纬度：%f 经度：%f\r\n", ptGeo.getLatitudeE6()/1e6, 
							ptGeo.getLongitudeE6()/1e6);
					strInfo += "\r\n附近有：";
					for (int i = 0; i < res.getAllPoi().size(); i++) {
						strInfo += (res.getAllPoi().get(i).name + ";");
					}
					Toast.makeText(GeoCoder.this, strInfo, Toast.LENGTH_LONG).show();
				}
			}
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

        });
        
        // 设定地理编码及反地理编码按钮的响应
        mBtnReverseGeoCode = (Button)findViewById(R.id.reversegeocode);
        mBtnGeoCode = (Button)findViewById(R.id.geocode);
        
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
					SearchButtonProcess(v);
			}
        };
        
        mBtnReverseGeoCode.setOnClickListener(clickListener); 
        mBtnGeoCode.setOnClickListener(clickListener); 
	}
	void SearchButtonProcess(View v) {
		if (mBtnReverseGeoCode.equals(v)) {
			GeoPoint ptCenter = new GeoPoint(39904965, 116327764);//mMapView.getMapCenter();
			mSearch.reverseGeocode(ptCenter);
		} else if (mBtnGeoCode.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editGeoCodeKey = (EditText)findViewById(R.id.geocodekey);
			mSearch.geocode(editGeoCodeKey.getText().toString(), editCity.getText().toString());
		}
	}

	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	class OverItemT extends ItemizedOverlay<OverlayItem>{
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
			super(boundCenterBottom(marker));
			
			mGeoList.add(new OverlayItem(pt, title, null));

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		@Override
		public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
			Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
			return false;
		}
	}

}
