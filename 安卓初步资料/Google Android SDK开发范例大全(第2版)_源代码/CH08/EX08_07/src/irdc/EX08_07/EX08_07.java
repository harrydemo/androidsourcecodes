package irdc.EX08_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EX08_07 extends Activity
{
  private TextView mTextView1;
  private ImageView mImageView1;
  private Button mButton1;
  /* n�����һ�� */
  final int IntervalSec = 30;
  java.text.SimpleDateFormat sdf;
  public boolean run = true;;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    mButton1 = (Button) findViewById(R.id.myButton1);
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mImageView1 = (ImageView) findViewById(R.id.myImageView1);

    mButton1.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        getYamWeatherPic();
      }

    });
    /* ����Thread */
    new Thread(mTasks).start();
  }

  @Override
  protected void onDestroy()
  {
    run = false;
    super.onDestroy();
  }

  private void getYamWeatherPic()
  {
    try
    {
      String uriAPI ="http://www.dubblogs.cc:8751/DubBlogs"
         +"/API/YamWeather/";
      
      //String uriAPI = "http://shop.teac.idv.tw/Android"
      //+ "/API/YamWeather/";
     
      URL objURL = new URL(uriAPI);
      /* ȡ������ */
      URLConnection conn = objURL.openConnection();
      conn.connect();
      /* ��InputStreamת��Reader */
      BufferedReader in = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      String inputLine;
      /* ͼ�ļ�·�� */
      String uriPic = "";
      /* һ��һ�ж�ȡ */
      while ((inputLine = in.readLine()) != null)
      {
        uriPic += inputLine;
      }

      objURL = new URL(uriPic);
      /* ȡ������ */
      HttpURLConnection conn2 = (HttpURLConnection) objURL
          .openConnection();
      conn2.connect();
      /* ȡ�ûش���InputStream */
      InputStream is = conn2.getInputStream();
      /* ��InputStream���Bitmap */
      Bitmap bm = BitmapFactory.decodeStream(is);
      /* �ر�InputStream */
      is.close();

      mImageView1.setImageBitmap(bm);

      mTextView1.setText(sdf.format(new java.util.Date()));

    } catch (MalformedURLException e)
    {
      // TODO Auto-generated catch block
      mTextView1.setText("MalformedURLException:" + e.toString());
      e.printStackTrace();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      mTextView1.setText("IOException:" + e.toString());
      e.printStackTrace();
    }
  }

  private Runnable mTasks = new Runnable()
  {
    public void run()
    {
      while (run)
      {
        try
        {
          Thread.sleep(IntervalSec * 1000);
          /* ����Message��Handler */
          mHandler.sendMessage(mHandler.obtainMessage());

        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  };
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message msg)
    {
      super.handleMessage(msg);
      getYamWeatherPic();
    }
  };

}

