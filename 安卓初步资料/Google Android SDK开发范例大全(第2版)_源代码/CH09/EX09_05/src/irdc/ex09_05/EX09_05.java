package irdc.ex09_05;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class EX09_05 extends MapActivity
{
  private LocationManager mLocationManager01;
  private String strLocationPrivider = "";
  private Location mLocation01=null;
  private TextView mTextView01;
  private MapView mMapView01;
  private GeoPoint currentGeoPoint;
  private int intZoomLevel = 20;
  @Override
  protected void onCreate(Bundle icicle)
  {
    // TODO Auto-generated method stub
    super.onCreate(icicle);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    /* ����MapView���� */
    mMapView01 = (MapView)findViewById(R.id.myMapView1);
    
    /* ����LocationManager����ȡ��ϵͳLOCATION���� */
    mLocationManager01 = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    
    /* ��һ��ִ����Location Providerȡ��Location */
    mLocation01 = getLocationPrivider(mLocationManager01);
    
    if(mLocation01!=null)
    {
      processLocationUpdated(mLocation01);
    }
    else
    {
      mTextView01.setText
      (
        getResources().getText(R.string.str_err_location).toString()
      );
    }
    /* ����LocationManager���󣬼���Location���ʱ�¼�������MapView */
    mLocationManager01.requestLocationUpdates(strLocationPrivider, 2000, 10, mLocationListener01);
  }
  
  public final LocationListener mLocationListener01 = new LocationListener()
  {
    @Override
    public void onLocationChanged(Location location)
    {
      // TODO Auto-generated method stub
      
      /* ���ֻ��յ�λ�ñ��ʱ����location����ȡ�õ������� */
      processLocationUpdated(location);
    }
    
    @Override
    public void onProviderDisabled(String provider)
    {
      // TODO Auto-generated method stub
      /* ��Provider���뿪����Χʱ */
    }
    
    @Override
    public void onProviderEnabled(String provider)
    {
      // TODO Auto-generated method stub
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
      // TODO Auto-generated method stub
      
    }
  };
  
  public String getAddressbyGeoPoint(GeoPoint gp)
  {
    String strReturn = "";
    try
    {
      /* ��GeoPoint������null */
      if (gp != null)
      {
        /* ����Geocoder���� */
        Geocoder gc = new Geocoder(EX09_05.this, Locale.getDefault());
        
        /* ȡ���������꾭γ�� */
        double geoLatitude = (int)gp.getLatitudeE6()/1E6;
        double geoLongitude = (int)gp.getLongitudeE6()/1E6;
        
        /* �Ծ�γ��ȡ�õ�ַ�������ж��е�ַ�� */
        List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);
        StringBuilder sb = new StringBuilder();
        
        /* �жϵ�ַ�Ƿ�Ϊ���� */
        if (lstAddress.size() > 0)
        {
          Address adsLocation = lstAddress.get(0);

          for (int i = 0; i < adsLocation.getMaxAddressLineIndex(); i++)
          {
            sb.append(adsLocation.getAddressLine(i)).append("\n");
          }
          sb.append(adsLocation.getLocality()).append("\n");
          sb.append(adsLocation.getPostalCode()).append("\n");
          sb.append(adsLocation.getCountryName());
        }
        
        /* ��ߢȡ���ĵ�ַ����Ϻ����StringBuilder����������� */
        strReturn = sb.toString();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return strReturn;
  }
  
  public Location getLocationPrivider(LocationManager lm)
  {
    Location retLocation = null;
    try
    {
      Criteria mCriteria01 = new Criteria();
      mCriteria01.setAccuracy(Criteria.ACCURACY_FINE);
      mCriteria01.setAltitudeRequired(false);
      mCriteria01.setBearingRequired(false);
      mCriteria01.setCostAllowed(true);
      mCriteria01.setPowerRequirement(Criteria.POWER_LOW);
      strLocationPrivider = lm.getBestProvider(mCriteria01, true);
      retLocation = lm.getLastKnownLocation(strLocationPrivider);
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.printStackTrace();
    }
    return retLocation;
  }
  
  private GeoPoint getGeoByLocation(Location location)
  {
    GeoPoint gp = null;
    try
    {
      /* ��Location���� */
      if (location != null)
      {
        double geoLatitude = location.getLatitude()*1E6;
        double geoLongitude = location.getLongitude()*1E6;
        gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return gp;
  }
  
  public static void refreshMapViewByGeoPoint(GeoPoint gp, MapView mv, int zoomLevel, boolean bIfSatellite)
  {
    try
    {
      mv.displayZoomControls(true);
      /* ȡ��MapView��MapController */
      MapController mc = mv.getController();
      /* �����õ��������ַ */
      mc.animateTo(gp);
      
      /* �Ŵ��ͼ�㼶 */
      mc.setZoom(zoomLevel);
      
      /* ����ѧϰ��ȡ��MapView�����Ŵ�㼶 */
      //mv.getMaxZoomLevel()
      
      /* �趨MapView����ʾѡ����ǡ��ֵ���*/
      if(bIfSatellite)
      {
        mv.setSatellite(true);
        mv.setStreetView(true);
      }
      else
      {
        mv.setSatellite(false);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /* ���ֻ��յ�λ�ñ��ʱ����location������µ���GeoPoint��MapView */
  private void processLocationUpdated(Location location)
  {
    /* ����Location����ȡ��GeoPoint�������� */
    currentGeoPoint = getGeoByLocation(location);
    
    /* ����MapView��ʾGoogle Map */
    refreshMapViewByGeoPoint(currentGeoPoint, mMapView01, intZoomLevel, true);
    
    mTextView01.setText
    (
      getResources().getText(R.string.str_my_location).toString()+"\n"+
      /* ��չѧϰ��ȡ��GPS�������꣺ */
      
      getResources().getText(R.string.str_longitude).toString()+
      String.valueOf((int)currentGeoPoint.getLongitudeE6()/1E6)+"\n"+
      getResources().getText(R.string.str_latitude).toString()+
      String.valueOf((int)currentGeoPoint.getLatitudeE6()/1E6)+"\n"+
      
      getAddressbyGeoPoint(currentGeoPoint)
    );
  }
  
  @Override
  protected boolean isRouteDisplayed()
  {
    // TODO Auto-generated method stub
    return false;
  }
}

