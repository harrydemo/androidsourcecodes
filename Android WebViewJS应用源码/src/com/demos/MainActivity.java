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
     * 获取手机相关信息
     * @return
     */
    private void getTelePhonyInfo(){
    	TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	//取得手机IMEI号
    	if(mImei == null){
    		mImei = telephonyManager.getDeviceId();
    		Log.d("YangBo", "getTelePhonyInfo TelephonyNumber: " + mTpNumber);
    	}   	
    	//取得手机号码
    	if(mTpNumber == null){
    		mTpNumber = telephonyManager.getLine1Number();
    		Log.d("YangBo", "getTelePhonyInfo TelephonyNumber: " + mTpNumber);
    	}
    	//取得手机 imsi
    	if(mImsi == null){
	    	mImsi = telephonyManager.getSubscriberId();
	    	Log.d("YangBo", "getTelePhonyInfo getSubscriberId: " + mImsi);
    	}
    	//取得android系统版本号
    	if(mSysVersion == null){
	    	mSysVersion = android.os.Build.VERSION.RELEASE; 
	    	Log.d("YangBo", "getTelePhonyInfo systemVersion: " + mSysVersion);
    	}
    	//取得手机制造商
    	if(mCompany == null){
	    	mCompany = android.os.Build.BRAND; 
	    	Log.d("YangBo", "company: " + mCompany);
    	}
    }
    
    /**
     * 获取手机IMEI
     * @return
     */
    private String getTelePhonyImei(){
    	return (mImei == null ? "IMEI获取失败" : mImei) ;
    }
    
    /**
     * 获取手机号
     * @return
     */
    private String getTelePhonyNumber(){
    	return (mTpNumber == null ? "手机号获取失败" : mTpNumber) ;
    }
    
    /**
     * 获取手机 imsi
     * @return
     */
    private String getTelePhonyImsi(){
    	return (mImsi == null ? "IMSI获取失败" : mImsi) ;
    }
    
    /**
     * 获取手机制造商
     * @return
     */
    private String getTelePhonyCompany(){
    	return (mCompany == null ? "手机制造商获取失败" : mCompany) ;
    }
    
    /**
     * 获取手机 系统版本号
     * @return
     */
    private String getTelePhonySysVersion(){
    	return (mSysVersion == null ? "系统版本号获取失败" : mSysVersion) ;
    }
    
    /**
     * 获取GPS相关信息
     * @return
     */
    private String getGPSInfo(){   	
    	final String location = "纬度： " + mLocation.getLatitude() + "\n" + "  经度： " + mLocation.getLongitude();
    	return location;
    }
    
    /**
     * 判断GPS模块是否存在或者是开启
     */
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return;
        }
        
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); //此为设置完成后返回到获取界面
    }
    
    /**
     * 进行到GPS设置
     */
    private void initGPS(){
    	//通过系统服务，取得LocationManager对象 
    	LocationManager loctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
//    	//通过GPS位置提供器获得位置(指定具体的位置提供器)
//    	mLocation = loctionManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
       
    	//配置你所需要的位置提供器，提供位置
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
    	criteria.setAltitudeRequired(false);//不要求海拔
    	criteria.setBearingRequired(false);//不要求方位
    	criteria.setCostAllowed(true);//允许有花费
    	criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗 
    	
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String provider = loctionManager.getBestProvider(criteria, true);
       
        //获得最后一次变化的位置
        mLocation = loctionManager.getLastKnownLocation(provider); 
        Log.d("YangBo", "initGPS() location: " + mLocation);   
        //添加监听GPS
        loctionManager.requestLocationUpdates(provider, 100 * 1000, 500, locationListener); 
    }
    
    /**
     * 监听GPS相关信息
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
		
		//当位置变化时触发
		@Override
		public void onLocationChanged(Location location) {
			mLocation = location;
			Log.d("YangBo", "onLocationChanged() location: " + location);
		}
	}; 
}