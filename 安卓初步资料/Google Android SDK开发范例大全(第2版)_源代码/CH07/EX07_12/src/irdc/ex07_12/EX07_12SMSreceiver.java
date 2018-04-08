package irdc.ex07_12;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class EX07_12SMSreceiver extends BroadcastReceiver
{

  private static final String mACTION 
  = "android.provider.Telephony.SMS_RECEIVED";
  private String strTempFile = "ex07_12_";
  private File myRecAudioFile;
  private File myRecAudioDir;
  private MediaRecorder mMediaRecorder01;
  private int SleepSec = 30;
  private boolean isStartRec;
  private long startRecTime;
  private Context myContext;

  @Override
  public void onReceive(Context context, Intent intent)
  {
    myContext = context;
    if (intent.getAction().equals(mACTION))
    {
      Intent i = new Intent(myContext, EX07_12.class);
      Bundle mbundle = new Bundle();
      mbundle.putString("TextView_Text", "�յ�����");
      i.putExtras(mbundle);
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      myContext.startActivity(i);
      /* ��ʼ¼�� */
      startRec();
      new Thread(mTasks).start();

    }

  }

  private Runnable mTasks = new Runnable()
  {

    @Override
    public void run()
    {
      /* ��30�����ִ��ֹͣ¼�� */
      while (System.currentTimeMillis() <= startRecTime + SleepSec
          * 1000)
      {
        try
        {
          Thread.sleep(1000);
        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      /* ֹͣ¼�� */
      stopRec();
    }
  };

  private void startRec()
  {
    try
    {
      if (Environment.getExternalStorageState().equals(
          android.os.Environment.MEDIA_MOUNTED))
      {
        /* ȡ�ÿ�ʼִ�е�ʱ�� */
        startRecTime = System.currentTimeMillis();
        /* ȡ��SD Card·����Ϊ¼�����ļ�λ�� */
        myRecAudioDir = Environment.getExternalStorageDirectory();
        /* ����¼���� */
        myRecAudioFile = File.createTempFile(strTempFile, ".amr",
            myRecAudioDir);

        mMediaRecorder01 = new MediaRecorder();
        /* �趨¼����ԴΪ��˷� */
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
        isStartRec = true;
        Log.i("EX07_12SMSreceiver", "startRec");
      } else
      {
        SleepSec = 1;
      }

    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void stopRec()
  {
    Log.i("EX07_12SMSreceiver", "stopRec");
    if (isStartRec)
    {
      /* ֹͣ¼�� */
      mMediaRecorder01.stop();
      mMediaRecorder01.release();
      mMediaRecorder01 = null;
    }
  }

}

