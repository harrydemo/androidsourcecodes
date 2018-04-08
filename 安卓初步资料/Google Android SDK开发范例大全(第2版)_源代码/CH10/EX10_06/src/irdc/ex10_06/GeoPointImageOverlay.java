package irdc.ex10_06;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class GeoPointImageOverlay extends Overlay
{
  private Location mLocation;
  private int drawableID;
  private double geoLatitude,geoLongitude;
  
  /* ��������һ������Location���� */
  public GeoPointImageOverlay(Location location, int drawableID)
  {
    this.mLocation = location;
    this.drawableID = drawableID;
    /* ȡ��Location�ľ�γ�� */
    this.geoLatitude = this.mLocation.getLatitude()*1E6;
    this.geoLongitude = this.mLocation.getLongitude()*1E6;
  }
  
  public GeoPointImageOverlay(Double geoLatitude, Double geoLongitude, int drawableID)
  {
    this.geoLatitude = geoLatitude*1E6;
    this.geoLongitude = geoLongitude*1E6;
    this.drawableID = drawableID;
  }
  
  public GeoPointImageOverlay(GeoPoint gp, int drawableID)
  {
    this.geoLatitude = gp.getLatitudeE6();
    this.geoLongitude = gp.getLongitudeE6();
    this.drawableID = drawableID;
  }
  
  @Override
  public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
  {
    Projection projection = mapView.getProjection();
    if (shadow == false)
    {
      GeoPoint gp = new GeoPoint((int)this.geoLatitude,(int)this.geoLongitude);
      /* ��Locationת��Point���� */
      Point point = new Point();
      projection.toPixels(gp, point);
      
      /* �趨��ˢ */
      Paint paint = new Paint();
      paint.setAntiAlias(true);
            
      /* ����Overlay */
      //canvas.drawOval(oval, paint);
      Bitmap bm = BitmapFactory.decodeResource(mapView.getResources(), this.drawableID); 
      canvas.drawBitmap(bm, point.x, point.y, paint);
    }
    return super.draw(canvas, mapView, shadow, when);
  }
  
  public void updateLocation(Location location)
  {
    this.mLocation = location;
  }
  
  public Location getLocation()
  {
    return mLocation;
  }
}

