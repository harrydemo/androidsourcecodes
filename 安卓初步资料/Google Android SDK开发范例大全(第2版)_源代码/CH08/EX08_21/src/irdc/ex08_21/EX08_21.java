package irdc.ex08_21;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EX08_21 extends Activity
{
  private EditText EditText01;
  private Button Button01;
  private WebView WebView01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    EditText01.setText("http://www.google.com");

    WebView01 = (WebView) this.findViewById(R.id.WebView01);

    /* WebView取得WebSettings */
    WebSettings webSettings = WebView01.getSettings();
    /* 设置能运行JavaScript */
    webSettings.setJavaScriptEnabled(true);
    /* WebView设置WebViewClient */
    WebView01.setWebViewClient(new WebViewClient()
    {
      @Override
      public void onPageFinished(WebView view, String url)
      {
        // TODO Auto-generated method stub
        /* 撷取画面 */
        Picture picture = view.capturePicture();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /* 取得宽和高 */
        int width = picture.getWidth();
        int height = picture.getHeight();

        if (width > 0 && height > 0)
        {

          Bitmap bitmap = Bitmap.createBitmap(width, height,
              Bitmap.Config.ARGB_8888);

          Canvas canvas = new Canvas(bitmap);

          picture.draw(canvas);

          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

          FileOutputStream fos = null;
          try
          {
            fos = new FileOutputStream("/sdcard/EX08_21_"
                + System.currentTimeMillis() + ".jpg");
            if (fos != null)
            {
              bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
              fos.close();
            }

          } catch (Exception e)
          {
            e.printStackTrace();
          }

          Toast.makeText(view.getContext(), "Snapshot OK", Toast.LENGTH_SHORT)
              .show();

        }
        super.onPageFinished(view, url);
      }
    });

    Button01 = (Button) this.findViewById(R.id.Button01);
    Button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        /* 设定网份 */
        WebView01.loadUrl(EditText01.getText().toString());
      }
    });
  }
}

