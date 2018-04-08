package irdc.ex08_06;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EX08_06 extends Activity
{
  private Button mButton1;
  private TextView mTextView1;
  private ImageView mImageView1;
  String uriPic = "http://lh3.ggpht.com/_s354WAuIc9E/"
      + "R_DpW4Rzj-I/AAAAAAAAAsc/Ox73tdxGLSw/logo.jpg";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mButton1 = (Button) findViewById(R.id.myButton1);
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mImageView1 = (ImageView) findViewById(R.id.myImageView1);

    mButton1.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        /* �趨Bitmap��ImageView�� */
        mImageView1.setImageBitmap(getURLBitmap());
        mTextView1.setText("");

      }
    });
  }

  public Bitmap getURLBitmap()
  {
    URL imageUrl = null;
    Bitmap bitmap = null;
    try
    {
      /* new URL������ַ���� */
      imageUrl = new URL(uriPic);
    } catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    try
    {
      /* ȡ������ */
      HttpURLConnection conn = (HttpURLConnection) imageUrl
          .openConnection();
      conn.connect();
      /* ȡ�ûش���InputStream */
      InputStream is = conn.getInputStream();
      /* ��InputStream���Bitmap */
      bitmap = BitmapFactory.decodeStream(is);
      /* �ر�InputStream */
      is.close();
      
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return bitmap;
  }

}

