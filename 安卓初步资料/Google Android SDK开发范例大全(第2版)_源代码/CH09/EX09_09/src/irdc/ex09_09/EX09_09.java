package irdc.ex09_09;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

public class EX09_09 extends Activity
{
  private EditText myEditText1;
  private WebView myWebView1;
  private Handler mHandler01 = new Handler();
  private static final String LOG_TAG = "DEBUG";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myEditText1 = (EditText) findViewById(R.id.myEditText1);
    myWebView1 = (WebView) findViewById(R.id.myWebView1);

    /* ȡ��WebSettings */
    WebSettings webSettings = myWebView1.getSettings();
    /* �趨��ִ��JavaScript */
    webSettings.setJavaScriptEnabled(true);
    
    
    
    
    /* �趨��html���õĶ������� */
    myWebView1.addJavascriptInterface(new runJavaScript(), "irdc");
    /* ��assets/google_translate.html���� */
    String url = "file:///android_asset/google_translate.html";
    myWebView1.loadUrl(url);
  }

  final class runJavaScript
  {
    public void runOnAndroidJavaScript()
    {
      mHandler01.post(new Runnable()
      {
        public void run()
        {
          if (myEditText1.getText().toString() != "")
          {
            /* ����google_translate.html���javascript */
            myWebView1.loadUrl("javascript:translate('"
                + myEditText1.getText().toString() + "')");
          }
        }
      });
    }
  }

  /* ��׽��ҳ���alert javascript��Ϊ.js����֮�ã��������LogCat
   */
  final class MyWebChromeClient extends WebChromeClient
  {
    @Override
    public boolean onJsAlert(WebView view, String url,
        String message, JsResult result)
    {
      // TODO Auto-generated method stub
      Log.d(LOG_TAG, message);
      // result.confirm();
      return super.onJsAlert(view, url, message, result);
    }
  }
}

