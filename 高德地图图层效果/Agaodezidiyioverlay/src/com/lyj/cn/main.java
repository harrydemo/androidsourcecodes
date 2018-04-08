package com.lyj.cn;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;

public class main extends MapActivity {

	 MapView mMapView;
	 MapController controller;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    mMapView=(MapView)findViewById(R.id.myMapView);
    mMapView.setBuiltInZoomControls(true);
    controller=mMapView.getController();
    Drawable marker=getResources().getDrawable(R.drawable.da_marker_red);
    marker.setBounds(0,0,marker.getIntrinsicWidth(),marker.getIntrinsicHeight());
    mMapView.getOverlays().add(new myItemizedOverlay(marker, this));
    
    
    }
}