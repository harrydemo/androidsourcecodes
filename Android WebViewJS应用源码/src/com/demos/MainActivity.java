package com.demos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
   
    private WebView mWebView;
    Location mLocation;
 
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        mWebView = (WebView) findViewById(R.id.webview);
        
        getTelePhonyInfo();
        openGPSSettings();
        initGPS();
    }
    
    @Override
	protected void onStart() {     	
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
     
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
        
        mWebView.loadUrl("file:///android_asset/demo.html");
        
        mWebView.setWebViewClient(new WebViewClient(){
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
		            view.loadUrl(url);
		            return true;
	            }
        	});
        
		super.onStart();
	}



	final class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {
        }
 
        public String getImei(String a){
        	Log.d("YangBo", "getImei(): " + a);
        	return getTelePhonyImei();
        }
        
        public String getImsi(){
        	return getTelePhonyImsi();
        }
        
        public String getNumber(){
        	return getTelePhonyNumber();
        }
        
        public String getSysVer(){
        	return getTelePhonySysVersion();
        }
        
        public String getCompany(){
        	return getTelePhonyCompany();
        }
        
        public String getLocation(){
        	final String location = getGPSInfo();
        	return location;
        }
    }

    public Handler mhandl = new Handler(){
    	@Override
        public void handleMessage(Message msg) {
    		switch(msg.what){
    			case 123 :
    			mWebView.loadUrl("file:///android_asset/Android&WebView.html");
    			break;   			
    		}
    	}
    }; 
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
        mWebView.goBack();
        return true;
        }
        return super.onKeyDown(keyCode, event);
	}
    
	private String mImei = null;
	private String mTpNumber = null;
	private String mImsi = null;
	private String mSysVersion = null;
	private String mCompany = null;
    
    /**
     * ��ȡ�ֻ������Ϣ
     * @return
     */
    private void getTelePhonyInfo(){
    	TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	//ȡ���ֻ�IMEI��
    	if(mImei == null){
    		mImei = telephonyManager.getDeviceId();
    		Log.d("YangBo", "getTelePhonyInfo TelephonyNumber: " + mTpNumber);
    	}   	
    	//ȡ���ֻ�����
    	if(mTpNumber == null){
    		mTpNumber = telephonyManager.getLine1Number();
    		Log.d("YangBo", "getTelePhonyInfo TelephonyNumber: " + mTpNumber);
    	}
    	//ȡ���ֻ� imsi
    	if(mImsi == null){
	    	mImsi = telephonyManager.getSubscriberId();
	    	Log.d("YangBo", "getTelePhonyInfo getSubscriberId: " + mImsi);
    	}
    	//ȡ��androidϵͳ�汾��
    	if(mSysVersion == null){
	    	mSysVersion = android.os.Build.VERSION.RELEASE; 
	    	Log.d("YangBo", "getTelePhonyInfo systemVersion: " + mSysVersion);
    	}
    	//ȡ���ֻ�������
    	if(mCompany == null){
	    	mCompany = android.os.Build.BRAND; 
	    	Log.d("YangBo", "company: " + mCompany);
    	}
    }
    
    /**
     * ��ȡ�ֻ�IMEI
     * @return
     */
    private String getTelePhonyImei(){
    	return (mImei == null ? "IMEI��ȡʧ��" : mImei) ;
    }
    
    /**
     * ��ȡ�ֻ���
     * @return
     */
    private String getTelePhonyNumber(){
    	return (mTpNumber == null ? "�ֻ��Ż�ȡʧ��" : mTpNumber) ;
    }
    
    /**
     * ��ȡ�ֻ� imsi
     * @return
     */
    private String getTelePhonyImsi(){
    	return (mImsi == null ? "IMSI��ȡʧ��" : mImsi) ;
    }
    
    /**
     * ��ȡ�ֻ�������
     * @return
     */
    private String getTelePhonyCompany(){
    	return (mCompany == null ? "�ֻ������̻�ȡʧ��" : mCompany) ;
    }
    
    /**
     * ��ȡ�ֻ� ϵͳ�汾��
     * @return
     */
    private String getTelePhonySysVersion(){
    	return (mSysVersion == null ? "ϵͳ�汾�Ż�ȡʧ��" : mSysVersion) ;
    }
    
    /**
     * ��ȡGPS�����Ϣ
     * @return
     */
    private String getGPSInfo(){   	
    	final String location = "γ�ȣ� " + mLocation.getLatitude() + "\n" + "  ���ȣ� " + mLocation.getLongitude();
    	return location;
    }
    
    /**
     * �ж�GPSģ���Ƿ���ڻ����ǿ���
     */
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return;
        }
        
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����
    }
    
    /**
     * ���е�GPS����
     */
    private void initGPS(){
    	//ͨ��ϵͳ����ȡ��LocationManager���� 
    	LocationManager loctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
//    	//ͨ��GPSλ���ṩ�����λ��(ָ�������λ���ṩ��)
//    	mLocation = loctionManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
       
    	//����������Ҫ��λ���ṩ�����ṩλ��
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
    	criteria.setAltitudeRequired(false);//��Ҫ�󺣰�
    	criteria.setBearingRequired(false);//��Ҫ��λ
    	criteria.setCostAllowed(true);//�����л���
    	criteria.setPowerRequirement(Criteria.POWER_LOW);//�͹��� 
    	
        //�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
        String provider = loctionManager.getBestProvider(criteria, true);
       
        //������һ�α仯��λ��
        mLocation = loctionManager.getLastKnownLocation(provider); 
        Log.d("YangBo", "initGPS() location: " + mLocation);   
        //��Ӽ���GPS
        loctionManager.requestLocationUpdates(provider, 100 * 1000, 500, locationListener); 
    }
    
    /**
     * ����GPS�����Ϣ
     * @return
     */
    private final LocationListener locationListener = new LocationListener() {
    	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}
		
		//��λ�ñ仯ʱ����
		@Override
		public void onLocationChanged(Location location) {
			mLocation = location;
			Log.d("YangBo", "onLocationChanged() location: " + location);
		}
	}; 
}