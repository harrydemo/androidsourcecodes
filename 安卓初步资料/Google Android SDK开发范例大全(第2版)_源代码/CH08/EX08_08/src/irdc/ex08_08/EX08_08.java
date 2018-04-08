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
  
  /* 记录目前正在播放中的URL地址 */
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
     
    /* mp3文件会下载至local端 */
    //strVideoURL = "http://www.dubblogs.cc:8751/Android/Test/Media/mp3/test.mp3";
    strVideoURL = "http://www.dubblogs.cc:8751/Android/Test/Media/mp3/test.mp3";
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    /* 设定透明度 */
    getWindow().setFormat(PixelFormat.TRANSPARENT);
    
    mPlay = (ImageButton)findViewById(R.id.play);
    mReset = (ImageButton)findViewById(R.id.reset);
    mPause = (ImageButton)findViewById(R.id.pause);
    mStop = (ImageButton)findViewById(R.id.stop);
     
    /* 播放按钮 */
    mPlay.setOnClickListener(new ImageButton.OnClickListener()
    { 
      public void onClick(View view)
      {
        /* 调用播放影片Function */
        playVideo(strVideoURL);
        mTextView01.setText
        (
          getResources().getText(R.string.str_play).toString()+
          "\n"+ strVideoURL
        );
      }
    });
    
    /* 重新播放按钮 */
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
    
    /* 暂停按钮 */
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
    
    /* 终止按钮 */
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
      
      /* 捕捉当MediaPlayer出错事件 */
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
      
      /* 捕捉若使用MediaPlayer缓冲区事件 */
      mMediaPlayer01.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
      {
        @Override 
        public void onBufferingUpdate(MediaPlayer mp, int percent)
        {
          //TODO Auto-generated method stub 
          Log.i(TAG, "Update buffer: " + Integer.toString(percent)+ "%");
        }
      });
      
      /* 当mp3音乐已经播放完毕所触发的事件 */
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
      
      /* 当Prepare阶段的Listener */
      mMediaPlayer01.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
      {
        @Override 
        public void onPrepared(MediaPlayer mp)
        {
          //TODO Auto-generated method stub 
          Log.i(TAG,"Prepared Listener"); 
        }
      });
      
      /* 起一个Runnable来确保档案在储存完之后才开始start() */
      Runnable r = new Runnable()
      {  
        public void run()
        {  
          try 
          {
            /* setDataSource会将档案存至SD卡 */
            setDataSource(strPath);
            /* 
             * 由于执行绪是循序执行
             * 故会在setDataSource之后执行prepare() 
             * */
            mMediaPlayer01.prepare();
            Log.i(TAG, "Duration: " + mMediaPlayer01.getDuration());
            
            /* 开始播放mp3 */
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
        /* 当线程异常发生，终止播放并 */
        mMediaPlayer01.stop();
        mMediaPlayer01.release();
      }
      e.printStackTrace();
    }
  }
  
  /* 自定义函数保存URL的mp3文件到SD存储卡 */
  private void setDataSource(String strPath) throws Exception 
  {
    /* 判断传入的地址是否为URL */
    if (!URLUtil.isNetworkUrl(strPath))
    {
      mMediaPlayer01.setDataSource(strPath);
    }
    else
    {
      if(bIsReleased == false)
      {
        /* 建立URL对象 */
        URL myURL = new URL(strPath);  
        URLConnection conn = myURL.openConnection();  
        conn.connect();
        
        /* 取得URLConnection的InputStream */
        InputStream is = conn.getInputStream();  
        if (is == null)
        {
          throw new RuntimeException("stream is null");
        }
        
        /* 建立新的临时文件 */
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
        
        /* 直到fos存储完毕，调用MediaPlayer.setDataSource */
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
  
  /* 取得音乐文件的扩展名自定义函数 */
  private String getFileExtension(String strFileName)
  {
    File myFile = new File(strFileName);
    String strFileExtension=myFile.getName();
    strFileExtension=(strFileExtension.substring(strFileExtension.lastIndexOf(".")+1)).toLowerCase();
    if(strFileExtension=="")
    {
      /* 若无法顺利取得扩展名，默认为.dat */
      strFileExtension = "dat";
    }
    return strFileExtension;
  }
  
  /* 离开程序时需调用自定义函数删除临时音乐文件 */
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
    
    /* 删除临时文件 */
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

