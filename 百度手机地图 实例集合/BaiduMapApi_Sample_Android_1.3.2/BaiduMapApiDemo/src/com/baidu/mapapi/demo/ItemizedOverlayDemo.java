package com.baidu.mapapi.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baidu.mapapi.*;


public class ItemizedOverlayDemo extends MapActivity {
	
	static View mPopView = null;	// 点击mark时弹出的气泡View
	static MapView mMapView = null;
	int iZoom = 0;
	OverItemT overitem = null;
	protected void onCreate(Bundle savedInstanceState) {
		
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
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //设置在缩放动画过程中也显示overlay,默认为不绘制
        mMapView.setDrawOverlayWhenZooming(true);
        GeoPoint point =new GeoPoint((int)(39.90923*1e6), (int)(116.397428*1e6));
        mMapView.getController().setCenter(point);
  
        // 添加ItemizedOverlay
		Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  //得到需要标在地图上的资源
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());   //为maker定义位置和边界
		
		overitem = new OverItemT(marker, this, 3);
		mMapView.getOverlays().add(overitem); //添加ItemizedOverlay实例到mMapView
		
		// 创建点击mark时的弹出泡泡
		mPopView=super.getLayoutInflater().inflate(R.layout.popview, null);
		mMapView.addView( mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		null, MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
		iZoom = mMapView.getZoomLevel();
//		mMapView.regMapViewListener(app.mBMapMan, new MKMapViewListener(){
//			public void onMapMoveFinish() {
//				if(iZoom != mMapView.getZoomLevel())
//				{// 比例尺不一样
//					iZoom = mMapView.getZoomLevel();
//					if(overitem.mGeoList.size() > 0)
//						overitem.mGeoList.remove(0);
//					//overitem.updateOverlay();
////					Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  //得到需要标在地图上的资源
////					marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
////							.getIntrinsicHeight());   //为maker定义位置和边界
////					mMapView.getOverlays().add(new OverItemT(marker, ItemizedOverlayDemo.this, 3));
//				}
//			}
//		});
	}

	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if(app.mBMapMan != null)
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
}
class OverItemT extends ItemizedOverlay<OverlayItem> {

	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;

	private double mLat1 = 39.90923; // point1纬度
	private double mLon1 = 116.357428; // point1经度

	private double mLat2 = 39.90923;
	private double mLon2 = 116.397428;

	private double mLat3 = 39.90923;
	private double mLon3 = 116.437428;

	public OverItemT(Drawable marker, Context context, int count) {
		super(boundCenterBottom(marker));

		this.marker = marker;
		this.mContext = context;

		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		
		// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
		mGeoList.add(new OverlayItem(p1, "P1", "point1"));
		mGeoList.add(new OverlayItem(p2, "P2", "point2"));
		if(count == 3)
		{
			GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
			mGeoList.add(new OverlayItem(p3, "P3", "point3"));
		}
		populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	public void updateOverlay()
	{
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		// Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item

			String title = overLayItem.getTitle();
			// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null); 

			// 可在此处添加您的绘制代码
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y, paintText); // 绘制文本
		}

		super.draw(canvas, mapView, shadow);
		//调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mGeoList.size();
	}
	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {
		setFocus(mGeoList.get(i));
		// 更新气泡位置,并使之显示
		GeoPoint pt = mGeoList.get(i).getPoint();
		ItemizedOverlayDemo.mMapView.updateViewLayout( ItemizedOverlayDemo.mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		pt, MapView.LayoutParams.BOTTOM_CENTER));
		ItemizedOverlayDemo.mPopView.setVisibility(View.VISIBLE);
		Toast.makeText(this.mContext, mGeoList.get(i).getSnippet(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		// 消去弹出的气泡
		ItemizedOverlayDemo.mPopView.setVisibility(View.GONE);
		return super.onTap(arg0, arg1);
	}
}

