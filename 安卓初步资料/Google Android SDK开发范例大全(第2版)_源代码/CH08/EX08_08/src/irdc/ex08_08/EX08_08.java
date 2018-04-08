package irdc.ex08_08;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;

public class EX08_08 extends Activity
{
  private TextView mTextView01;
  private MediaPlayer mMediaPlayer01;
  private ImageButton mPlay, mReset, mPause, mStop;
  private boolean bIsReleased = false;
  private boolean bIsPaused = false;
  private static final String TAG = "Hippo_URL_MP3_Player";
  //private SurfaceView mSurfaceView01; 
  //private SurfaceHolder mSurfaceHolder01; 
  
  /* ��¼Ŀǰ���ڲ����е�URL��ַ */
  private String currentFilePath = "";
  
  /*  */
  private String currentTempFilePath = "";
  private String strVideoURL = "";
  
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
     
    /* mp3�ļ���������local�� */
    //strVideoURL = "http://www.dubblogs.cc:8751/Android/Test/Media/mp3/test.mp3";
    strVideoURL = "http://www.dubblogs.cc:8751/Android/Test/Media/mp3/test.mp3";
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* �趨͸���� */
    getWindow().setFormat(PixelFormat.TRANSPARENT);
    
    mPlay = (ImageButton)findViewById(R.id.play);
    mReset = (ImageButton)findViewById(R.id.reset);
    mPause = (ImageButton)findViewById(R.id.pause);
    mStop = (ImageButton)findViewById(R.id.stop);
     
    /* ���Ű�ť */
    mPlay.setOnClickListener(new ImageButton.OnClickListener()
    { 
      public void onClick(View view)
      {
        /* ���ò���ӰƬFunction */
        playVideo(strVideoURL);
        mTextView01.setText
        (
          getResources().getText(R.string.str_play).toString()+
          "\n"+ strVideoURL
        );
      }
    });
    
    /* ���²��Ű�ť */
    mReset.setOnClickListener(new ImageButton.OnClickListener()
    { 
      public void onClick(View view)
      {
        if(bIsReleased == false)
        {
          if (mMediaPlayer01 != null)
          {
            mMediaPlayer01.seekTo(0);
            mTextView01.setText(R.string.str_play);
          }
        }
      } 
    });
    
    /* ��ͣ��ť */
    mPause.setOnClickListener(new ImageButton.OnClickListener()
    {
      public void onClick(View view)
      {
        if (mMediaPlayer01 != null)
        {
          if(bIsReleased == false)
          {
            if(bIsPaused==false)
            {
              mMediaPlayer01.pause();
              bIsPaused = true;
              mTextView01.setText(R.string.str_pause);
            }
            else if(bIsPaused==true)
            {
              mMediaPlayer01.start();
              bIsPaused = false;
              mTextView01.setText(R.string.str_play);
            }
          }
        }
      }
    });
    
    /* ��ֹ��ť */
    mStop.setOnClickListener(new ImageButton.OnClickListener()
    { 
      public void onClick(View view)
      { 
        try 
        {
          if (mMediaPlayer01 != null)
          {
            if(bIsReleased==false)
            {
              mMediaPlayer01.seekTo(0);
              mMediaPlayer01.pause();
              //mMediaPlayer01.stop();
              //mMediaPlayer01.release();
              //bIsReleased = true;
              mTextView01.setText(R.string.str_stop);
            }
          }
        }
        catch(Exception e)
        {
          mTextView01.setText(e.toString());
          Log.e(TAG, e.toString());
          e.printStackTrace();
        }
      }
    });
  }
  
  private void playVideo(final String strPath)
  {
    try 
    {
      if (strPath.equals(currentFilePath)&& mMediaPlayer01 != null)
      {
        mMediaPlayer01.start(); 
        return; 
      }
      
      currentFilePath = strPath;
      
      mMediaPlayer01 = new MediaPlayer();
      mMediaPlayer01.setAudioStreamType(2);
      
      /* ��׽��MediaPlayer�����¼� */
      mMediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener()
      {
        @Override 
        public boolean onError(MediaPlayer mp, int what, int extra)
        {
          //TODO Auto-generated method stub 
          Log.i(TAG, "Error on Listener, what: " + what + "extra: " + extra); 
          return false;
        }
      });
      
      /* ��׽��ʹ��MediaPlayer�������¼� */
      mMediaPlayer01.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
      {
        @Override 
        public void onBufferingUpdate(MediaPlayer mp, int percent)
        {
          //TODO Auto-generated method stub 
          Log.i(TAG, "Update buffer: " + Integer.toString(percent)+ "%");
        }
      });
      
      /* ��mp3�����Ѿ�����������������¼� */
      mMediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
      {
        @Override 
        public void onCompletion(MediaPlayer mp)
        {
          //TODO Auto-generated method stub 
          //delFile(currentTempFilePath);
          Log.i(TAG,"mMediaPlayer01 Listener Completed");
        }
      });
      
      /* ��Prepare�׶ε�Listener */
      mMediaPlayer01.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
      {
        @Override 
        public void onPrepared(MediaPlayer mp)
        {
          //TODO Auto-generated method stub 
          Log.i(TAG,"Prepared Listener"); 
        }
      });
      
      /* ��һ��Runnable��ȷ�������ڴ�����֮��ſ�ʼstart() */
      Runnable r = new Runnable()
      {  
        public void run()
        {  
          try 
          {
            /* setDataSource�Ὣ��������SD�� */
            setDataSource(strPath);
            /* 
             * ����ִ������ѭ��ִ��
             * �ʻ���setDataSource֮��ִ��prepare() 
             * */
            mMediaPlayer01.prepare();
            Log.i(TAG, "Duration: " + mMediaPlayer01.getDuration());
            
            /* ��ʼ����mp3 */
            mMediaPlayer01.start();
            bIsReleased = false;
          }
          catch (Exception e)
          {
            Log.e(TAG, e.getMessage(), e);
          }
        }
      };  
      new Thread(r).start();
    }
    catch(Exception e)
    {
      if (mMediaPlayer01 != null)
      {
        /* ���߳��쳣��������ֹ���Ų� */
        mMediaPlayer01.stop();
        mMediaPlayer01.release();
      }
      e.printStackTrace();
    }
  }
  
  /* �Զ��庯������URL��mp3�ļ���SD�洢�� */
  private void setDataSource(String strPath) throws Exception 
  {
    /* �жϴ���ĵ�ַ�Ƿ�ΪURL */
    if (!URLUtil.isNetworkUrl(strPath))
    {
      mMediaPlayer01.setDataSource(strPath);
    }
    else
    {
      if(bIsReleased == false)
      {
        /* ����URL���� */
        URL myURL = new URL(strPath);  
        URLConnection conn = myURL.openConnection();  
        conn.connect();
        
        /* ȡ��URLConnection��InputStream */
        InputStream is = conn.getInputStream();  
        if (is == null)
        {
          throw new RuntimeException("stream is null");
        }
        
        /* �����µ���ʱ�ļ� */
        File myTempFile = File.createTempFile("hippoplayertmp", "."+getFileExtension(strPath));
        currentTempFilePath = myTempFile.getAbsolutePath();
        
        /* currentTempFilePath = /sdcard/hippoplayertmp39327.mp3 */
        
        /* 
        if(currentTempFilePath!="")
        {
          Log.i(TAG, currentTempFilePath);
        }
        */
        
        FileOutputStream fos = new FileOutputStream(myTempFile);
        byte buf[] = new byte[128];  
        do
        { 
          int numread = is.read(buf);
          if (numread <= 0)
          {
            break;
          }
          fos.write(buf, 0, numread);
        }while (true);
        
        /* ֱ��fos�洢��ϣ�����MediaPlayer.setDataSource */
        mMediaPlayer01.setDataSource(currentTempFilePath);
        try
        {
          is.close();
        }
        catch (Exception ex)
        {
          Log.e(TAG, "error: " + ex.getMessage(), ex);
        }
      }
    }
  }
  
  /* ȡ�������ļ�����չ���Զ��庯�� */
  private String getFileExtension(String strFileName)
  {
    File myFile = new File(strFileName);
    String strFileExtension=myFile.getName();
    strFileExtension=(strFileExtension.substring(strFileExtension.lastIndexOf(".")+1)).toLowerCase();
    if(strFileExtension=="")
    {
      /* ���޷�˳��ȡ����չ����Ĭ��Ϊ.dat */
      strFileExtension = "dat";
    }
    return strFileExtension;
  }
  
  /* �뿪����ʱ������Զ��庯��ɾ����ʱ�����ļ� */
  private void delFile(String strFileName)
  {
    File myFile = new File(strFileName);
    if(myFile.exists())
    {
      myFile.delete();
    }
  }
  
  @Override 
  protected void onPause()
  {
    //TODO Auto-generated method stub 
    
    /* ɾ����ʱ�ļ� */
    try
    {
      delFile(currentTempFilePath);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onPause();
  }
}

