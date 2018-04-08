package irdc.ex08_16;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class EX08_16 extends Activity
{
  private TextView mTextView01;
  private LayoutInflater mInflater01;
  private View mView01;
  private EditText mEditText01,mEditText02;
  private String TAG = "HTTP_DEBUG";
  /* 中文字的间距 */
  private int intShiftPadding = 14;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* 建立DisplayMetrics对象，取得屏幕分辨率 */
    DisplayMetrics dm = new DisplayMetrics(); 
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* 将文字Label放在屏幕右上方 */
    mTextView01.setLayoutParams
    (
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView01.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView01.getText().toString().length()))-10,0)
    );
    
    /* 使User点击TextView文本的事件处理 */
    mTextView01.setOnClickListener(new TextView.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 显示登录对话框 */
        showLoginForm();
      }
    });
  }
  
  /* 自定义登录对话框函数 */
  private void showLoginForm()
  {
    try
    {
      /* 以LayoutInflater取得加Activity的context */
      mInflater01 = LayoutInflater.from(EX08_16.this);
      /* 设定建立的View所要使用的Layout Resource */
      mView01 = mInflater01.inflate(R.layout.login, null);
      
      /* 账号EditText */
      mEditText01 = (EditText) mView01.findViewById(R.id.myEditText1);
      
      /* 密码EditText */
      mEditText02 = (EditText) mView01.findViewById(R.id.myEditText2);
      
      /* 扩展学习，使EditText仅接受数字 */
      /* import android.text.method.NumberKeyListener; */
      mEditText02.setKeyListener(new NumberKeyListener()
      {
        @Override
        protected char[] getAcceptedChars()
        {
          char[] numberChars = {'1','2','3','4','5','6','7','8','9','0','.'};
          return numberChars;
        }

        @Override
        public int getInputType()
        {
          // TODO Auto-generated method stub
          return 0;
        }
      });
      
      /* 建立Login窗体对话框 */
      new AlertDialog.Builder(this)
      .setView(mView01)
      .setPositiveButton("OK",
      new DialogInterface.OnClickListener()
      {
        /* 当User按下OK时进行登录网络实现 */
        public void onClick(DialogInterface dialog, int whichButton)
        {
          /* 调用自定义processInternetLogin函数登录 */
          if(processInternetLogin(mEditText01.getText().toString(), mEditText02.getText().toString()))
          {
            /* 若登录成功，则结束否Activity调出登录完成 */
            Intent i = new Intent();
            i.setClass(EX08_16.this, EX08_16_02.class);
            startActivity(i);
            finish();
          }
        }
      }).show();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /* 自定义登录网站URL Login的实现 */
  private boolean processInternetLogin(String strUID, String strUPW)
  {
    /* Demo登录，留意大小写不同 */
    /* 账号：david */
    /* 密码：1234 */
    String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/TestLogin/index.php";
    String strRet = "";
    
    try
    {
      DefaultHttpClient httpclient = new DefaultHttpClient();
      HttpResponse response;
      HttpPost httpost = new HttpPost(uriAPI);
      List <NameValuePair> nvps = new ArrayList <NameValuePair>();
      nvps.add(new BasicNameValuePair("uid", strUID)); 
      nvps.add(new BasicNameValuePair("upw", strUPW)); 
      
      httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
      
      response = httpclient.execute(httpost);
      HttpEntity entity = response.getEntity();
      //entity = response.getEntity();
      
      Log.d(TAG, "HTTP POST getStatusLine: " + response.getStatusLine());
      
      /* HTML POST response BODY */
      strRet = EntityUtils.toString(entity);
      Log.i(TAG, strRet);
      strRet = strRet.trim().toLowerCase();
      
      List<Cookie> cookies = httpclient.getCookieStore().getCookies();
      if (entity != null)
      {
        entity.consumeContent();
      }
      
      Log.d(TAG, "HTTP POST Initialize of cookies."); 
      cookies = httpclient.getCookieStore().getCookies(); 
      if (cookies.isEmpty())
      {
        Log.d(TAG, "HTTP POST Cookie not found.");
        Log.i(TAG, entity.toString());
      }
      else
      {
        for (int i = 0; i < cookies.size(); i++)
        {
          Log.d(TAG, "HTTP POST Found Cookie: " + cookies.get(i).toString()); 
        } 
      }
      
      
      if(strRet.equals("y"))
      {
        Log.i("TEST", "YES");
        return true;
      }
      else
      {
        Log.i("TEST", "NO");
        return false;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }
}

