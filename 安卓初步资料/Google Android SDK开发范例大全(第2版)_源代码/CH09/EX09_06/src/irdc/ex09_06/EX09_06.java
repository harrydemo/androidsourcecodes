package irdc.ex09_06;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX09_06 extends MapActivity
{
  private MapController mMapController01;
  private MapView mMapView01;
  private Button mButton01,mButton02,mButton03;
  private EditText mEditText01;
  private int intZoomLevel=15;
  private String TAG = "HIPPO_GEO_DEBUG";
  
  @Override
  protected void onCreate(Bundle icicle)
  {
    // TODO Auto-generated method stub
    super.onCreate(icicle);
    setContentView(R.layout.main);
    
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    mEditText01.setText
    (
      getResources().getText(R.string.str_default_address).toString()
    );
    
    /* 建立MapView对象 */
    mMapView01 = (MapView)findViewById(R.id.myMapView1);
    mMapController01 = mMapView01.getController();
    
    // 设定MapView的显示选项（卫星、街道）
    mMapView01.setSatellite(true);
    mMapView01.setStreetView(true);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        if(mEditText01.getText().toString()!="")
        {
          refreshMapViewByGeoPoint
          (
            getGeoByAddress
            (
              mEditText01.getText().toString()
            ),mMapView01,intZoomLevel,true
          );
        }
      }
    });
    
    /* 放大 */
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        intZoomLevel++;
        if(intZoomLevel>mMapView01.getMaxZoomLevel())
        {
          intZoomLevel = mMapView01.getMaxZoomLevel();
        }
        mMapController01.setZoom(intZoomLevel);
      }
    });
    
    /* 缩小 */
    mButton03 = (Button)findViewById(R.id.myButton3);
    mButton03.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        intZoomLevel--;
        if(intZoomLevel<1)
        {
          intZoomLevel = 1;
        }
        mMapController01.setZoom(intZoomLevel);
      }
    });
    
    /* 初次查询地点 */
    refreshMapViewByGeoPoint
    (
      getGeoByAddress
      (
          getResources().getText(R.string.str_default_address).toString()
      ),mMapView01,intZoomLevel,true
    );
  }
  
  private GeoPoint getGeoByAddress(String strSearchAddress)
  {
    GeoPoint gp = null;
    try
    {
      if(strSearchAddress!="")
      {
        Geocoder mGeocoder01 = new Geocoder(EX09_06.this, Locale.getDefault());
        List<Address> lstAddress = mGeocoder01.getFromLocationName(strSearchAddress, 1);
        if (!lstAddress.isEmpty())
        {
          /* 延伸学习： pizza usa*/
          // Address[addressLines=[0:"U.S PIZZA",1:"15th Main Rd, Phase II, J P Nagar",2:"Bengaluru, Karnataka",3:"India"],feature=U.S PIZZA,admin=Karnataka,sub-admin=Bengaluru,locality=Bengaluru,thoroughfare=15th Main Rd,postalCode=null,countryCode=IN,countryName=India,hasLatitude=true,latitude=18.508933,hasLongitude=true,longitude=73.8042,phone=null,url=null,extras=null]
          /*
          for (int i = 0; i < lstAddress.size(); ++i)
          {
            Address adsLocation = lstAddress.get(i);
            Log.i(TAG, "Address found = " + adsLocation.toString()); 
            //lat = adsLocation.getLatitude();
            //lon = adsLocation.getLongitude();
          }
          */
          Address adsLocation = lstAddress.get(0);
          double geoLatitude = adsLocation.getLatitude()*1E6;
          double geoLongitude = adsLocation.getLongitude()*1E6;
          gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
        }
        else
        {
          Log.i(TAG, "Address GeoPoint NOT Found.");
        }
      }
    }
    catch (Exception e)
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
      /* 取得MapView的MapController */
      MapController mc = mv.getController();
      /* 移至该地理坐标地址 */
      mc.animateTo(gp);
      
      /* 放大地图层级 */
      mc.setZoom(zoomLevel);
      
      /* 设定MapView的显示选项（卫星、街道）*/
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
  
  @Override
  protected boolean isRouteDisplayed()
  {
    // TODO Auto-generated method stub
    return false;
  }
}

