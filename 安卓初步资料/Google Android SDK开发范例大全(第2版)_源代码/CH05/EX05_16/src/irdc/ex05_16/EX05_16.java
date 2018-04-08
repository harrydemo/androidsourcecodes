package irdc.ex05_16;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX05_16 extends Activity
{
  private Button myButton1;
  private Button myButton2;
  private File cacheDir;
  private File fileDir;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myButton1 = (Button) findViewById(R.id.myButton1);
    myButton2 = (Button) findViewById(R.id.myButton2);

    /* 取得目前Cache目录 */
    cacheDir = this.getCacheDir();
    /* 取得目前File目录 */
    fileDir = this.getFilesDir();

    myButton1.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {

        String path = fileDir.getParent() + java.io.File.separator
            + fileDir.getName();

        showListActivity(path);

      }
    });

    myButton2.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        String path = cacheDir.getParent() + java.io.File.separator
            + cacheDir.getName();

        showListActivity(path);
      }
    });

  }

  /* 调用EX05_16_1并将路径传入 */
  private void showListActivity(String path)
  {
    Intent intent = new Intent();
    intent.setClass(EX05_16.this, EX05_16_1.class);

    Bundle bundle = new Bundle();
    bundle.putString("path", path);
    intent.putExtras(bundle);

    startActivity(intent);
  }

}

