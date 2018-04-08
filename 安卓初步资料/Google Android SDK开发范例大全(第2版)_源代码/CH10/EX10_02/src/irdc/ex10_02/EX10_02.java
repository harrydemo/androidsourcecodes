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
    
    /* ����MapView���� */ 
    mMapView = (MapView)findViewById(R.id.myMapView1); 
    mMapController = mMapView.getController();
    /* �����ʼ�� */
    mTextView = (TextView)findViewById(R.id.myText1);
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    mButton04 = (Button)findViewById(R.id.myButton4);
    /* �趨Ԥ��ķŴ�㼶 */
    zoomLevel = 17; 
    mMapController.setZoom(zoomLevel); 

    /* Provider��ʼ�� */
    mLocationManager = (LocationManager)
                       getSystemService(Context.LOCATION_SERVICE); 
    /* ȡ��Provider��Location */
    getLocationPrivider();
    if(mLocation!=null)
    {
      /* ȡ��Ŀǰ�ľ�γ�� */
      gp1=getGeoByLocation(mLocation); 
      gp2=gp1;
      /* ��MapView���е�����Ŀǰλ�� */
      refreshMapView();
      /* �趨�¼���Listener */
      mLocationManager.requestLocationUpdates(mLocationPrivider,
          2000, 10, mLocationListener);   
    }
    else
    {
      new AlertDialog.Builder(EX10_02.this).setTitle("ϵͳѶϢ")
      .setMessage(getResources().getString(R.string.str_message))
      .setNegativeButton("ȷ��",new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialog, int which)
         {
           EX10_02.this.finish();
         }
       })
       .show();
    }
    
    /* ��ʼ��¼��Button */
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        gp1=gp2;
        /* ���Overlay */
        resetOverlay();
        /* ����� */
        setStartPoint();
        /* ����MapView */
        refreshMapView();
        /* �����ƶ�����Ϊ0��������TextView */
        distance=0;
        mTextView.setText("�ƶ����룺0M");
        /* ������·�ߵĻ��� */
        _run=true;
      } 
    });
    
    /* ������¼��Button */
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* ���յ� */
        setEndPoint();
        /* ����MapView */
        refreshMapView();
        /* ��ֹ��·�ߵĻ��� */
        _run=false;
      } 
    }); 
    
    /* ��С��ͼ��Button */
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
    
    /* �Ŵ��ͼ��Button */
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
  
  /* MapView��Listener */
  public final LocationListener mLocationListener = 
    new LocationListener() 
  { 
    @Override 
    public void onLocationChanged(Location location) 
    { 
      /* �����¼�����У��ͻ�·�߲������ƶ����� */
      if(_run)
      {
        /* �����ƶ����λ�� */
        gp2=getGeoByLocation(location);
        /* ��·�� */
        setRoute();
        /* ����MapView */
        refreshMapView();
        /* ȡ���ƶ����� */
        distance+=GetDistance(gp1,gp2);
        mTextView.setText("�ƶ����룺"+format(distance)+"M"); 

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

  /* ȡ��GeoPoint��method */ 
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
  
  /* ȡ��LocationProvider */
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
  
  /* �趨����method */
  private void setStartPoint() 
  {  
    int mode=1;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* �趨·�ߵ�method */  
  private void setRoute() 
  {  
    int mode=2;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* �趨�յ��method */
  private void setEndPoint() 
  {  
    int mode=3;
    MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.add(mOverlay);
  }
  /* ����Overlay��method */
  private void resetOverlay() 
  {
    List<Overlay> overlays = mMapView.getOverlays(); 
    overlays.clear();
  } 
  /* ����MapView��method */
  public void refreshMapView() 
  { 
    mMapView.displayZoomControls(true); 
    MapController myMC = mMapView.getController(); 
    myMC.animateTo(gp2); 
    myMC.setZoom(zoomLevel); 
    mMapView.setSatellite(false); 
  } 
  
  /* ȡ�������ľ����method */
  public double GetDistance(GeoPoint gp1,GeoPoint gp2)
  {
    double Lat1r = ConvertDegreeToRadians(gp1.getLatitudeE6()/1E6);
    double Lat2r = ConvertDegreeToRadians(gp2.getLatitudeE6()/1E6);
    double Long1r= ConvertDegreeToRadians(gp1.getLongitudeE6()/1E6);
    double Long2r= ConvertDegreeToRadians(gp2.getLongitudeE6()/1E6);
    /* ����뾶(KM) */
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
  
  /* format�ƶ������method */
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

