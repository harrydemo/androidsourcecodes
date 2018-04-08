package irdc.ex07_11;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EX07_11 extends Activity
{
  private ImageButton myButton1;
  private ImageButton myButton2;
  private ImageButton myButton3;
  private ImageButton myButton4;
  private ListView myListView1;
  private String strTempFile = "ex07_11_";
  private File myRecAudioFile;
  private File myRecAudioDir;
  private File myPlayFile;
  private MediaRecorder mMediaRecorder01;

  private ArrayList<String> recordFiles;
  private ArrayAdapter<String> adapter;
  private TextView myTextView1;
  private boolean sdCardExit;
  private boolean isStopRecord;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myButton1 = (ImageButton) findViewById(R.id.ImageButton01);
    myButton2 = (ImageButton) findViewById(R.id.ImageButton02);
    myButton3 = (ImageButton) findViewById(R.id.ImageButton03);
    myButton4 = (ImageButton) findViewById(R.id.ImageButton04);
    myListView1 = (ListView) findViewById(R.id.ListView01);
    myTextView1 = (TextView) findViewById(R.id.TextView01);
    myButton2.setEnabled(false);
    myButton3.setEnabled(false);
    myButton4.setEnabled(false);

    /* 判断SD Card是否插入 */
    sdCardExit = Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED);
    /* 取得SD Card路径做为录音的文件位置 */
    if (sdCardExit)
      myRecAudioDir = Environment.getExternalStorageDirectory();

    /* 取得SD Card目录里的所有.amr文件 */
    getRecordFiles();

    adapter = new ArrayAdapter<String>(this,
        R.layout.my_simple_list_item, recordFiles);
    /* 将ArrayAdapter存入ListView对象中 */
    myListView1.setAdapter(adapter);

    /* 录音 */
    myButton1.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        try
        {
          if (!sdCardExit)
          {
            Toast.makeText(EX07_11.this, "请插入SD Card",
                Toast.LENGTH_LONG).show();
            return;
          }

          /* 建立录音档 */
          myRecAudioFile = File.createTempFile(strTempFile, ".amr",
              myRecAudioDir);

          mMediaRecorder01 = new MediaRecorder();
          /* 设定录音来源为麦克风 */
          mMediaRecorder01
              .setAudioSource(MediaRecorder.AudioSource.MIC);
          mMediaRecorder01
              .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
          mMediaRecorder01
              .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

          mMediaRecorder01.setOutputFile(myRecAudioFile
              .getAbsolutePath());

          mMediaRecorder01.prepare();

          mMediaRecorder01.start();

          myTextView1.setText("录音中");

          myButton2.setEnabled(true);
          myButton3.setEnabled(false);
          myButton4.setEnabled(false);

          isStopRecord = false;

        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }
    });
    /* 停止 */
    myButton2.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        if (myRecAudioFile != null)
        {
          /* 停止录音 */
          mMediaRecorder01.stop();
          /* 将录音文件名给Adapter */
          adapter.add(myRecAudioFile.getName());
          mMediaRecorder01.release();
          mMediaRecorder01 = null;
          myTextView1.setText("停止：" + myRecAudioFile.getName());

          myButton2.setEnabled(false);

          isStopRecord = true;
        }
      }
    });
    /* 播放 */
    myButton3.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        if (myPlayFile != null && myPlayFile.exists())
        {
          /* 开启播放的程序 */
          openFile(myPlayFile);
        }

      }
    });
    /* ?除 */
    myButton4.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        if (myPlayFile != null)
        {
          /* 因将Adapter移除文件名 */
          adapter.remove(myPlayFile.getName());
          /* 删除文件 */
          if (myPlayFile.exists())
            myPlayFile.delete();
          myTextView1.setText("完成删除");
        }

      }
    });

    myListView1
        .setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1,
              int arg2, long arg3)
          {
            /* 当有点选文件名时将删除及播放按钮Enable */
            myButton3.setEnabled(true);
            myButton4.setEnabled(true);

            myPlayFile = new File(myRecAudioDir.getAbsolutePath()
                + File.separator
                + ((CheckedTextView) arg1).getText());
            myTextView1.setText("你选的是："
                + ((CheckedTextView) arg1).getText());
          }
        });

  }

  @Override
  protected void onStop()
  {
    if (mMediaRecorder01 != null && !isStopRecord)
    {
      /* 停止录音 */
      mMediaRecorder01.stop();
      mMediaRecorder01.release();
      mMediaRecorder01 = null;
    }
    super.onStop();
  }

  private void getRecordFiles()
  {
    recordFiles = new ArrayList<String>();
    if (sdCardExit)
    {
      File files[] = myRecAudioDir.listFiles();
      if (files != null)
      {

        for (int i = 0; i < files.length; i++)
        {
          if (files[i].getName().indexOf(".") >= 0)
          {
            /* 读取.amr文件 */
            String fileS = files[i].getName().substring(
                files[i].getName().indexOf("."));
            if (fileS.toLowerCase().equals(".amr"))
              recordFiles.add(files[i].getName());

          }
        }
      }
    }
  }

  /* 开启播放录音文件的程序 */
  private void openFile(File f)
  {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(android.content.Intent.ACTION_VIEW);

    String type = getMIMEType(f);
    intent.setDataAndType(Uri.fromFile(f), type);
    startActivity(intent);
  }

  private String getMIMEType(File f)
  {
    String end = f.getName().substring(
        f.getName().lastIndexOf(".") + 1, f.getName().length())
        .toLowerCase();
    String type = "";
    if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
        || end.equals("amr") || end.equals("mpeg")
        || end.equals("mp4"))
    {
      type = "audio";
    } else if (end.equals("jpg") || end.equals("gif")
        || end.equals("png") || end.equals("jpeg"))
    {
      type = "image";
    } else
    {
      type = "*";
    }
    type += "/*";
    return type;
  }
}

