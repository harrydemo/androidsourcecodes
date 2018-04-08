package irdc.ex08_22;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class EX08_22 extends Activity
{
  public static String TAG = "HIPPO_DEBUG";
  private EditText mEditText01;
  private Button mButton01,mButton02,mButton03,mButton04;
  private WebView mWebView01;
  private WebSettings mWebSettings01;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mEditText01 = (EditText)this.findViewById(R.id.myEditText1);
    mEditText01.setText("http://shop.teac.idv.tw/Android/");
    mButton01 = (Button)this.findViewById(R.id.myButton1);
    mButton02 = (Button)this.findViewById(R.id.myButton2);
    mButton03 = (Button)this.findViewById(R.id.myButton3);
    mButton04 = (Button)this.findViewById(R.id.myButton4);
    
    mWebView01 = (WebView)this.findViewById(R.id.myWebView1);
    mWebSettings01 = mWebView01.getSettings();
    mWebSettings01.setJavaScriptEnabled(true);
    
    // AppCacheMaxSize in 512 KB = 524288 bytes
    mWebSettings01.setAppCacheMaxSize(524288);
    // Tell the WebView to enable Application Caches
    mWebSettings01.setAppCacheEnabled(true);
    // 延含学习setAppCachePath()指定缓存路径
    //mWebSettings01.setDatabaseEnabled(true); 
    //mWebSettings01.setDatabasePath("/data/data/com.package.name/databases");
    
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        mWebView01.loadUrl(mEditText01.getText().toString());
      }
    });
    
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        mWebSettings01.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
      }
    });
    
    mButton03.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        mWebSettings01.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
      }
    });
    
    mButton04.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        mWebSettings01.setDefaultZoom(WebSettings.ZoomDensity.FAR);
      }
    });
    
    mWebView01.setWebViewClient(new WebViewClient()
    {
      
    });
  }
}

