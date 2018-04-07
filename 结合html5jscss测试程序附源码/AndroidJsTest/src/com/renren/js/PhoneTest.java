package com.renren.js;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PhoneTest extends Activity {      
    
    private static final String LOG_TAG = "WebViewDemo";      
    private WebView mWebView;      
    private TextView mReusultText ;      
    private Handler mHandler = new Handler();      
     
    @Override     
    public void onCreate(Bundle icicle) {      
     
        super.onCreate(icicle);      
        
        setContentView(R.layout.main); 
        
        //获得浏览器组件      
        //WebView就是一个简单的浏览器      
        //android浏览器源码存在于LINUX/android/package/apps/Browser中      
        //里面的所有操作都是围绕WebView来展开的      
        mWebView = (WebView) findViewById(R.id.webview);      
        mReusultText = (TextView) findViewById(R.id.resultText);     
        
        //WebSettings 几乎浏览器的所有设置都在该类中进行      
        WebSettings webSettings = mWebView.getSettings();      
        webSettings.setSavePassword(false);      
        webSettings.setSaveFormData(false);      
        webSettings.setJavaScriptEnabled(true);  
        webSettings.setAllowFileAccess(true); //设置允许访问文件数据 
        webSettings.setSupportZoom(false);      
  
        mWebView.setWebViewClient(new DemoWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());  
        
        /*    
         * DemoJavaScriptInterface类为js调用android服务器端提供接口    
         * android 作为DemoJavaScriptInterface类的客户端接口被js调用    
         * 调用的具体方法在DemoJavaScriptInterface中定义：    
         * 例如该实例中的clickOnAndroid    
         */     
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(),"demo");      
        mWebView.loadUrl("file:///android_asset/index.html");    //对应当前project的asserts目录 
                                                                 //也可以写file:///sdcard/index.html
        
        // mWebView.loadUrl("file:///android_asset/index2.html"); 
         
    }      
    
    class DemoWebViewClient extends WebViewClient { 
    	@Override 
    	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	    	view.loadUrl(url); 
	    	return true; 
    	} 
    } 
 
    final class DemoJavaScriptInterface {      
        DemoJavaScriptInterface() {
        	
        }      
     
        /**    
         * 该方法被浏览器端调用    
         */     
        public void clickOnAndroid() {      
            mHandler.post(new Runnable() {      
                public void run() {      
                    //调用js中的wave()方法      
                    mWebView.loadUrl("javascript:wave()");      
                }      
            });      
        }      
    }      
     
    /**    
     * 继承WebChromeClient类    
     * 在这个类的3个方法中，分别使用Android的内置控件重写了Js中对应的对话框，就是说对js中的对话框做处理了，就是重写了。
     *     
     */     
    final class MyWebChromeClient extends WebChromeClient {      
     
        /**    
         * 处理alert弹出框    
         */     
        @Override     
        public boolean onJsAlert(WebView view,String url,      
                                 String message,final JsResult result) {      
            Log.d(LOG_TAG,"onJsAlert:"+message);      
          //  mReusultText.setText("Alert:"+message);      
            //对alert的简单封装      
//            new AlertDialog.Builder(PhoneTest.this)
//                .setTitle("Alert")
//                .setMessage(message)
//                .setPositiveButton("OK",      
//                new DialogInterface.OnClickListener() {      
//                    @Override     
//                    public void onClick(DialogInterface arg0, int arg1) {      
//                    	 // result.confirm();   
//                         // MyWebView.this.finish();   
//
//                   }      
//            })
//            .setCancelable(true)
//            .create()
//            .show();      
            
            result.confirm();      //不加上面的代码也能出框，为嘛呢？
            return true;
//            return super.onJsConfirm(view,url,message, result);      
        }      
     
        /**    
         * 处理confirm弹出框    
         */     
        @Override     
        public boolean onJsConfirm(WebView view, String url, String message,      
                JsResult result) {      
            Log.d(LOG_TAG, "onJsConfirm:"+message);      
            mReusultText.setText("Confirm:"+message);
            //对confirm的简单封装      
            new AlertDialog.Builder(PhoneTest.this).      
                setTitle("Confirm")
                .setMessage(message)
                .setPositiveButton("OK",      
                new DialogInterface.OnClickListener() {      
                    @Override     
                    public void onClick(DialogInterface arg0, int arg1) {      
                      
                   }      
	            })
	            .create()
	            .show();  
            
            result.confirm();      
            return true;
            //如果采用下面的代码会另外再弹出个消息框，目前不知道原理
            // 因为调用了父类的构造函数了。。。
            //return super.onJsConfirm(view, url, message, result);      
        }      
     
        /**    
         * 处理prompt弹出框    
         */     
        @Override     
        public boolean onJsPrompt(WebView view, String url, String message,      
                String defaultValue, JsPromptResult result) {      
            Log.d(LOG_TAG,"onJsPrompt:"+message);      
            mReusultText.setText("Prompt input is :"+message);      
            result.confirm();      
            return super.onJsPrompt(view, url, message, message, result);      
        }      
    }      
}  


       