package com.ccn.cn;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;

public class main extends MapActivity {
             MapView myMapView;
	         MapController myMapController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myMapView = (MapView) findViewById(R.id.myMapView);
		myMapView.setBuiltInZoomControls(true);
		myMapController = myMapView.getController();
		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());// Intrinsic����
		myMapView.getOverlays().add(new MyItemizedOverlay(marker, this));

	}

	// ͬһ���͸�����Ļ���
	class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		// ����
		private Drawable marker;
		private Context mContext;
		private List<OverlayItem> geoList = new ArrayList<OverlayItem>();

		// ��γ�ȵ�����
		private double mLat1 = 39.9022;
		private double mLon1 = 116.3922;

		private double mLat2 = 39.607723;
		private double mLon2 = 116.397741;

		private double mLat3 = 39.917723;
		private double mLon3 = 116.6552;

		// ���췽��
		public MyItemizedOverlay(Drawable marker, Context context) {
			super(boundCenterBottom(marker));

			this.marker = marker;
			this.mContext = context;

			// �����������
			GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
			GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
			GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

			geoList.add(new OverlayItem(p1, "P1", "point1"));
			geoList.add(new OverlayItem(p2, "P2", "point2"));
			geoList.add(new OverlayItem(p3, "P3", "point3"));

			populate();// ִ����䷽��

		}

		// ���Ʒ���
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// ͶӰ��������Ļ���ص�����ϵͳ�����γ�ȵ�����ϵͳ��ת��
			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) {
				OverlayItem overlayItem = this.getItem(index);
				String title = overlayItem.getTitle();
				Point point = projection.toPixels(overlayItem.getPoint(), null);

				Paint painttext = new Paint();
				painttext.setColor(Color.BLACK);
				painttext.setTextSize(15);
				canvas.drawText(title, point.x - 30, point.y - 25, painttext);

			}

			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);

		}

		// ��ӳ�Ա����
		@Override
		protected OverlayItem createItem(int i) {

			return geoList.get(i);
		}

		@Override
		public int size() {

			return geoList.size();
		}

		//��ӵ���¼�
		public boolean onTap(int i){
			setFocus(geoList.get(i));
			Toast.makeText(this.mContext, geoList.get(i).getSnippet(), Toast.LENGTH_LONG).show();//snippetƬ��
			return true;
		}
		public boolean onTap(GeoPoint point,MapView mapView){
			return super.onTap(point,mapView);
		}
		
		
		
	}

}