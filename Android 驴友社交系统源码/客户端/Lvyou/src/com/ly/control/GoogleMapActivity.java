package com.ly.control;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.ly.control.R;

import android.content.Intent;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class GoogleMapActivity extends MapActivity {
	private Bitmap bit;
	private Button bt;
	private MapView mv;
	private MapController mc;
	private LocationManager lm;
	public static double jingdu=126.38* 1E6;
	public static double weidu=45.45* 1E6;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);
        bit=BitmapFactory.decodeResource(getResources(), R.drawable.ballon);
        bt=(Button)findViewById(R.id.Button01);
        bt.setOnClickListener(l);
        mv=(MapView)findViewById(R.id.myMap);
        mc=mv.getController();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, listener);
        updateMap(39.9 * 1E6, 116.07 * 1E6);
    }
    
    public void showLocation(Location l){
    	if(l!=null){
//    		jingdu=l.getLatitude();
//    		weidu=l.getLongitude();
    		jingdu=126.38* 1E6;
    		weidu=45.45* 1E6;
    		
    	}
    	else{
    		Toast.makeText(GoogleMapActivity.this, "获取不到", Toast.LENGTH_LONG).show();
    	}
    }
    
    private LocationListener listener = new LocationListener() {
    	
    
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		// TODO Auto-generated method stub
    		Location l = lm.getLastKnownLocation(provider);
    		showLocation(l);
    	}
  
    	public void onProviderEnabled(String provider) {
    		// TODO Auto-generated method stub
    		Location l = lm.getLastKnownLocation(provider);
    		showLocation(l);
    	}
   
    	public void onProviderDisabled(String provider) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    
    	public void onLocationChanged(Location location) {
    		// TODO Auto-generated method stub
    		showLocation(location);
    	}
    };
    
    private OnClickListener l=new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v==bt){
				Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				showLocation(l);
				Intent i=new Intent(GoogleMapActivity.this,LocationActivity.class);
				
			//	i.p(jingdu, l.getLatitude());
				startActivity(i);
			}			
		}
     };
    
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

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}