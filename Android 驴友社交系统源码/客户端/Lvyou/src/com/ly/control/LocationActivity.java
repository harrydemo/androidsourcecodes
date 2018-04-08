package com.ly.control;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.ly.control.R;

public class LocationActivity extends MapActivity {
		private Bitmap bit;
		private LocationManager lm;
		private MapView mv;
		private MapController mc;
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.location);
	        
	        bit=BitmapFactory.decodeResource(getResources(), R.drawable.ballon);
	       
	        mv=(MapView)findViewById(R.id.myMap);
	        mc=mv.getController();
	        GoogleMapActivity gm=new GoogleMapActivity();
	        System.out.println(gm.jingdu+"???????????????????????????????????");
	   //     updateMap(39.9 * 1E6, 116.07 * 1E6);
	        updateMap(gm.weidu, gm.jingdu);
	  	}
	  public void updateMap(double lad,double lon){
	    	mv.setBuiltInZoomControls(true);
	    	mv.displayZoomControls(true);    	
	    	GeoPoint gp=new GeoPoint((int)lad, (int)lon);
	    	mc.animateTo(gp);
	    	mc.setZoom(8);//显示到城区（放大级别）
	    
	    	List<Overlay> lo=mv.getOverlays();
	    	lo.add(new MyOverlay(gp,bit));
	    }
	    private class MyOverlay extends Overlay{
	    	private GeoPoint gp;
	    	private Bitmap bit;
	    	
	    	public MyOverlay(GeoPoint gp,Bitmap bit){
	    		this.gp=gp;
	    		this.bit=bit;
	    	}


			public void draw(Canvas canvas, MapView mapView, boolean shadow) {
				// TODO Auto-generated method stub
				super.draw(canvas, mapView, shadow);
				if(!shadow){
					Projection pj=mv.getProjection();
					Point p=new Point();
					pj.toPixels(gp,p);
					canvas.drawBitmap(bit, p.x, p.y-bit.getHeight()/2,null);
					
				}
				
			}
	    	
	    }
	  
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}


