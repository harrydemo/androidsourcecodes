package xiaosi.baiduMap;

import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;


public class BaiduMapActivity extends MapActivity{
    /** Called when the activity is first created. */
	private  BMapManager mapManager = null;
	private String key = "1B79478DA01F7800AEA8602517A6D89B38151105";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapManager = new BMapManager(getApplication());
        mapManager.init(key, null);
        super.initMapActivity(mapManager);
         
        MapView mapView = (MapView) findViewById(R.id.mapsView);
        mapView.setBuiltInZoomControls(true);  //�����������õ����ſؼ�
         
        MapController mapController = mapView.getController();  // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
        GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
                (int) (116.404 * 1E6));  //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
        mapController.setCenter(point);  //���õ�ͼ���ĵ�
        mapController.setZoom(12);    //���õ�ͼzoom����
    }
    @Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
    
    @Override
    protected void onDestroy() {
        if (mapManager != null) {
            mapManager.destroy();
            mapManager = null;
        }
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        if (mapManager != null) {
            mapManager.stop();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        if (mapManager != null) {
            mapManager.start();
        }
        super.onResume();
    }
    
	
}