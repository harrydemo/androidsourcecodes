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
  /* �����ֵļ�� */
  private int intShiftPadding = 14;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* ����DisplayMetrics����ȡ����Ļ�ֱ��� */
    DisplayMetrics dm = new DisplayMetrics(); 
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* ������Label������Ļ���Ϸ� */
    mTextView01.setLayoutParams
    (
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView01.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView01.getText().toString().length()))-10,0)
    );
    
    /* ʹUser���TextView�ı����¼����� */
    mTextView01.setOnClickListener(new TextView.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* ��ʾ��¼�Ի��� */
        showLoginForm();
      }
    });
  }
  
  /* �Զ����¼�Ի����� */
  private void showLoginForm()
  {
    try
    {
      /* ��LayoutInflaterȡ�ü�Activity��context */
      mInflater01 = LayoutInflater.from(EX08_16.this);
      /* �趨������View��Ҫʹ�õ�Layout Resource */
      mView01 = mInflater01.inflate(R.layout.login, null);
      
      /* �˺�EditText */
      mEditText01 = (EditText) mView01.findViewById(R.id.myEditText1);
      
      /* ����EditText */
      mEditText02 = (EditText) mView01.findViewById(R.id.myEditText2);
      
      /* ��չѧϰ��ʹEditText���������� */
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
      
      /* ����Login����Ի��� */
      new AlertDialog.Builder(this)
      .setView(mView01)
      .setPositiveButton("OK",
      new DialogInterface.OnClickListener()
      {
        /* ��User����OKʱ���е�¼����ʵ�� */
        public void onClick(DialogInterface dialog, int whichButton)
        {
          /* �����Զ���processInternetLogin������¼ */
          if(processInternetLogin(mEditText01.getText().toString(), mEditText02.getText().toString()))
          {
            /* ����¼�ɹ����������Activity������¼��� */
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
  
  /* �Զ����¼��վURL Login��ʵ�� */
  private boolean processInternetLogin(String strUID, String strUPW)
  {
    /* Demo��¼�������Сд��ͬ */
    /* �˺ţ�david */
    /* ���룺1234 */
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

