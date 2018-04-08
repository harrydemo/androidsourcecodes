package irdc.ex06_09;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class EX06_09 extends Activity
{
  private Button myButton1;
  private Button myButton2;
  private File fileDir;
  private File sdcardDir;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myButton1 = (Button) findViewById(R.id.myButton1);
    myButton2 = (Button) findViewById(R.id.myButton2);

    /* ȡ��ĿǰFileĿ¼ */
    fileDir = this.getFilesDir();

    /* ȡ��SD CardĿ¼ */
    sdcardDir = Environment.getExternalStorageDirectory();

    /* ��SD Card�޲���ʱ��myButton2��ɲ��ܰ� */
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))
    {
      myButton2.setEnabled(false);
    }

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
        String path = sdcardDir.getParent() + sdcardDir.getName();
        showListActivity(path);
      }
    });

  }

  private void showListActivity(String path)
  {
    Intent intent = new Intent();
    intent.setClass(EX06_09.this, EX06_09_1.class);
    Bundle bundle = new Bundle();
    /* ��·������EX06_09_1 */
    bundle.putString("path", path);
    intent.putExtras(bundle);
    startActivity(intent);
  }
}

