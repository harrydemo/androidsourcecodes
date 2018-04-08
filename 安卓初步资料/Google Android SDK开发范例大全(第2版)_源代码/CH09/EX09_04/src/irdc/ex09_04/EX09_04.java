package irdc.ex09_04; 

/* import相关class */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import com.google.android.maps.GeoPoint; 
import com.google.android.maps.MapActivity; 
import com.google.android.maps.MapController; 
import com.google.android.maps.MapView; 

public class EX09_04 extends MapActivity 
{
  private MapController mMapController01; 
  private MapView mMapView01;
  private Button mButton01,mButton02,mButton03; 
  private EditText mEditText01;
  private EditText mEditText02;
  private int intZoomLevel=0;
  /* Map启动时的预设坐标： */
  private double dLat=25.0402555;
  private double dLng=121.512377;

  @Override 
  protected void onCreate(Bundle icicle) 
  { 
    super.onCreate(icicle); 
    setContentView(R.layout.main); 

    /* 建立MapView对象 */ 
    mMapView01 = (MapView)findViewById(R.id.myMapView1); 
    mMapController01 = mMapView01.getController(); 
    /* 设定MapView的显示选项（卫星、街道） */
    mMapView01.setSatellite(false); 
    mMapView01.setStreetView(true);
    /* 预设放己的层级 */
    intZoomLevel = 17; 
    mMapController01.setZoom(intZoomLevel); 
    /* 设定Map的中点为预设经纬度 */ 
    refreshMapView();
    
    mEditText01 = (EditText)findViewById(R.id.myEdit1); 
    mEditText02 = (EditText)findViewById(R.id.myEdit2);
    
    /* 送出查询的Button */ 
    mButton01 = (Button)findViewById(R.id.myButton1); 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* 经纬度空白检查 */
        if(mEditText01.getText().toString().equals("")||
           mEditText02.getText().toString().equals(""))
        {
          showDialog("经度或纬度填写不正确!");
        }
        else
        {
          /* 取得输入的经纬度 */
          dLng=Double.parseDouble(mEditText01.getText().toString());
          dLat=Double.parseDouble(mEditText02.getText().toString());
          /* 依输入的经纬度重整Map */
          refreshMapView(); 
        }
      } 
    }); 
     
    /* 放大Map的Button */ 
    mButton02 = (Button)findViewById(R.id.myButton2); 
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        intZoomLevel++; 
        if(intZoomLevel>mMapView01.getMaxZoomLevel()) 
        { 
          intZoomLevel = mMapView01.getMaxZoomLevel(); 
        } 
        mMapController01.setZoom(intZoomLevel); 
      } 
    }); 
     
    /* 缩小Map的Button */
    mButton03 = (Button)findViewById(R.id.myButton3); 
    mButton03.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        intZoomLevel--; 
        if(intZoomLevel<1) 
        { 
          intZoomLevel = 1; 
        } 
        mMapController01.setZoom(intZoomLevel); 
      } 
    });
  } 
  
  /* 重整Map的method */
  public void refreshMapView() 
  { 
    GeoPoint p = new GeoPoint((int)(dLat* 1E6), (int)(dLng* 1E6)); 
    mMapView01.displayZoomControls(true);
    /* 将Map的中点移吹GeoPoint */
    mMapController01.animateTo(p); 
    mMapController01.setZoom(intZoomLevel); 
  } 
   
  @Override 
  protected boolean isRouteDisplayed() 
  { 
    return false; 
  }
  
  /* 显示Dialog的method */
  private void showDialog(String mess){
    new AlertDialog.Builder(EX09_04.this).setTitle("Message")
    .setMessage(mess)
    .setNegativeButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
      }
    })
    .show();
  }
} 

