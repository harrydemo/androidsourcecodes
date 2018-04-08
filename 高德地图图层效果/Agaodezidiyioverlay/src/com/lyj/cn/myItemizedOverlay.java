package com.lyj.cn;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;
   //自定义绘制点图层的类
public class myItemizedOverlay extends ItemizedOverlay<OverlayItem>{
    private List<OverlayItem> geoList=new ArrayList<OverlayItem>();
	
	private Drawable          markerDrawable;
	private Context           mContext;
	
	//经纬度的属性
	private double           mLat1=39.9022;
	private double           mLon1=116.3922;
	private double           mLat2=39.607723;
	private double           mLon2=116.397741;
	private double           mLat3=39.917723;
	private double           mLon3=116.6552;
	
	
	//构造方法
	public myItemizedOverlay(Drawable marker,Context context) {
		super(boundCenterBottom(marker));
		this.markerDrawable=marker;
		this.mContext=context;
		
		GeoPoint  g1=new GeoPoint((int)(mLat1*1E6),(int)(mLon1*1E6));
		GeoPoint  g2=new GeoPoint((int)(mLat2*1E6),(int)(mLon2*1E6));
		GeoPoint  g3=new GeoPoint((int)(mLat3*1E6),(int)(mLon3*1E6));
		
		geoList.add(new OverlayItem(g1, "P1", "point1"));
		geoList.add(new OverlayItem(g2, "P2", "point2"));
		geoList.add(new OverlayItem(g3, "P3", "point3"));
		
		populate();//填充
	}

	public void draw(Canvas canvas,MapView mapView,boolean shadow){
		Projection projection=mapView.getProjection();
		
		for(int index=size()-1;index>=0;index--){
		    OverlayItem overlayItem=this.getItem(index);
			String titleString=overlayItem.getTitle();
			Point point=projection.toPixels(overlayItem.getPoint(),null);
			
			Paint textPaint=new Paint();
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(15);
			canvas.drawText(titleString, point.x-30, point.y-25, textPaint);
			
		}
		
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(markerDrawable);
	}
	
	
	@Override
	protected OverlayItem createItem(int i) {
		
		return geoList.get(i);
	}

	@Override
	public int size() {
		
		return geoList.size();
	}

	public boolean onTap(int i){
		setFocus(geoList.get(i));
		Toast.makeText(this.mContext, geoList.get(i).getSnippet(), Toast.LENGTH_LONG).show();//snippet片段
		return true;
	}
	
	
	
}
