package irdc.ex10_02; 

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class EX10_02  extends MapActivity
{
  private TextView mTextView; 
  private Button mButton01;
  private Button mButton02;
  private Button mButton03;
  private Button mButton04;
  private MapView mMapView;
  private MapController mMapController; 
  private LocationManager mLocationManager;
  private Location mLocation;
  private String mLocationPrivider="";
  private int zoomLevel=0;
  private GeoPoint gp1;
  private GeoPoint gp2;
  private boolean _run=false;  
  private double distance=0;

  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /* 建立MapView对象 */ 
    mMapView = (MapView)findViewById(R.id.myMapView1); 
    mMapController = mMapView.getController();
    /* 物件初始化 */
    mTextView = (TextView)findViewById(R.id.myText1);
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    mButton04 = (Button)findViewById(R.id.myButton4);
    /* 设定预设的放大层级 */
    zoomLevel = 17; 
    mMapController.setZoom(zoomLevel); 

    /* Provider初始化 */
    mLocationManager = (LocationManager)
                       getSystemService(Context.LOCATION_SERVICE); 
    /* 取得Provider与Location */
    getLocationPrivider();
    if(mLocation!=null)
    {
      /* 取得目前的经纬度 */
      gp1=getGeoByLocation(mLocation); 
      gp2=gp1;
      /* 将MapView的中点移至目前位置 */
      refreshMapView();
      /* 设定事件的Listener */
      mLocationManager.requestLocationUpdates(mLocationPrivider,
          2000, 10, mLocationListener);   
    }
    else
    {
      new AlertDialog.Builder(EX10_02.this).setTitle("系统讯息")
      .setMessage(getResources().getString(R.string.str_message))
      .setNegativeButton("确定",new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialog, int which)
         {
           EX10_02.this.finish();
         }
       })
       .show();
    }
    
    /* 开始记录的Button */
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        gp1=gp2;
        /* 清除Overlay */
        resetOverlay();
        /* 画起点 */
        setStartPoint();
        /* 更新MapView */
        refreshMapView();
        /* 重设移动距离为0，并更新TextView */
        distance=0;
        mTextView.setText("移动距离：0M");
        /* 启动画路线的机制 */
        _run=true;
      } 
    });
    
    /* 结束记录的Button */
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* 画终点 */
        setEndPoint();
        /* 更新MapView */
        refreshMapView();
        /* 终止画路线的机制 */
        _run=false;
      } 
    }); 
    
    /* 缩小地图的Button */
    mButton03.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        zoomLevel--; 
        if(zoomLevel<1) 
        { 
          zoomLevel = 1; 
        } 
        mMapController.setZoom(zoomLevel); 
      } 
    }); 
    
    /* 放大地图的Button */
    mButton04.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        zoomLevel++; 
        if(zoomLevel>mMapView.getMaxZoomLevel()) 
        { 
          zoomLevel = mMapView.getMaxZoomLevel(); 
        } 
        mMapController.setZoom(zoomLevel); 
      }
    }); 
  }
  
  /* MapView的Listener */
  public final LocationListener mLocationListener = 
    new LocationListener() 
  { 
    @Override 
    public void onLocationChanged(Location location) 
    { 
      /* 如果记录进行中，就画路线并更新移动距离 */
      if(_run)
      {
        /* 记下移动后的位置 */
        gp2=getGeoByLocation(location);
        /* 画路线 */
        setRoute();
        /* 更新MapView */
        refreshMapView();
        /* 取得移动距离 */
        distance+=GetDistance(gp1,gp2);
        mTextView.setText("移动距离："+format(distance)+"M"); 

        gp1=gp2;
      }  
    } 
     
    @Override 
    public void onProviderDisabled(String provider) 
    { 
    } 
    @Override 
    public void onProviderEnabled(String provider) 
    { 
    } 
    @Override 
    public void onStatusChanged(String provider,int status,
                                Bundle extras) 
    { 
    } 
  }; 

  /* 取得GeoPoint的method */ 
  private GeoPoint getGeoByLocation(Location location) 
  { 
    GeoPoint gp = null; 
    try 
    { 
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
  
  /* 取得LocationProvider */
  public void getLocationPrivider() 
  { 
    Criteria mCriteria01 = new Criteria();
    mCriteria01.setAccuracy(Criteria.ACCURACY_FINE); 
    mCriteria01.setAltitudeRequired(false); 
    mCriteria01.setBearingRequired(false); 
    mCriteria01.setCostAllowed(true); 
    mCriteria01.setPowerRequirement(Criteria.POWER_LOW); 
    
    mLocationPrivider = mLocationManager
                        .getBestProvider(mCriteria01, true); 
    mLocation = mLocationManager
                .getLastKnownLocation(mLocationPrivider); 
  }
  
  /* 设定起点的method */
  private void setStartPoint() 
  {  
    int mode=1;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* 设定路线的method */  
  private void setRoute() 
  {  
    int mode=2;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* 设定终点的method */
  private void setEndPoint() 
  {  
    int mode=3;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* 重设Overlay的method */
  private void resetOverlay() 
  {
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.clear();
  } 
  /* 更新MapView的method */
  public void refreshMapView() 
  { 
    mMapView.displayZoomControls(true); 
    MapController myMC = mMapView.getController(); 
    myMC.animateTo(gp2); 
    myMC.setZoom(zoomLevel); 
    mMapView.setSatellite(false); 
  } 
  
  /* 取得两点间的距离的method */
  public double GetDistance(GeoPoint gp1,GeoPoint gp2)
  {
    double Lat1r = ConvertDegreeToRadians(gp1.getLatitudeE6()/1E6);
    double Lat2r = ConvertDegreeToRadians(gp2.getLatitudeE6()/1E6);
    double Long1r= ConvertDegreeToRadians(gp1.getLongitudeE6()/1E6);
    double Long2r= ConvertDegreeToRadians(gp2.getLongitudeE6()/1E6);
    /* 地球半径(KM) */
    double R = 6371;
    double d = Math.acos(Math.sin(Lat1r)*Math.sin(Lat2r)+
               Math.cos(Lat1r)*Math.cos(Lat2r)*
               Math.cos(Long2r-Long1r))*R;
    return d*1000;
  }

  private double ConvertDegreeToRadians(double degrees)
  {
    return (Math.PI/180)*degrees;
  }
  
  /* format移动距离的method */
  public String format(double num)
  {
    NumberFormat formatter = new DecimalFormat("###");
    String s=formatter.format(num);
    return s;
  }
  
  @Override
  protected boolean isRouteDisplayed()
  {
    return false;
  }
} 

