package irdc.ex08_02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EX08_02 extends Activity 
{
  private String TAG = "HIPPO_DEBUG";
  private ImageButton mImageButton1;
  private EditText mEditText1;
  private WebView mWebView1;  
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
   
    mImageButton1 = (ImageButton)findViewById(R.id.myImageButton1);
    mEditText1 = (EditText)findViewById(R.id.myEditText1);
    mEditText1.setText("http://www.dubblogs.cc/");
    mWebView1 = (WebView) findViewById(R.id.myWebView1);
    
    mWebView1.setWebViewClient(new WebViewClient() 
    {
      /*延含学习
      @Override
      public void onPageFinished(WebView view, String url)
      {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
      }
      */     
    });
    
    /*当按下箭头时的事件*/
    mImageButton1.setOnClickListener(new ImageButton.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        {                    
          mImageButton1.setImageResource(R.drawable.go_2);
          /*设定抓取EditText里面的内容*/
          String strURI = (mEditText1.getText().toString()); 
          /*?WebView里面显示网页数据*/
          mWebView1.loadUrl(strURI);
          Log.i(TAG, "loadUrl");
          Toast.makeText
          (
            EX08_02.this,
            getString(R.string.load)+strURI,
            Toast.LENGTH_LONG).show();          
        }
      }      
    });
  }
}

