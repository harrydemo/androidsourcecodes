package irdc.ex08_15;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/* Activity实现SurfaceHolder.Callback */
public class EX08_15 extends Activity implements SurfaceHolder.Callback
{
  private TextView mTextView01; 
  private EditText mEditText01;
  /* 建立MediaPlayer对象 */ 
  private MediaPlayer mMediaPlayer01;
  /* 用以配置MediaPlayer的SurfaceView */
  private SurfaceView mSurfaceView01;
  /* SurfaceHolder对象 */
  private SurfaceHolder mSurfaceHolder01;
  private ImageButton mPlay, mReset, mPause, mStop;
  
  /* 识别MediaPlayer是否已被释放 */
  private boolean bIsReleased = false;
  
  /* 识别MediaPlayer是否正处于暂停 */
  private boolean bIsPaused = false;
  
  /* LogCat输出TAG filter */
  private static final String TAG = "HippoMediaPlayer";
  private String currentFilePath = "";
  private String currentTempFilePath = "";
  private String strVideoURL = "";
  private List<String> aryFileDownloaded;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* 将.3gp影像文件存放URL网址 */
    strVideoURL = "http://www.dubblogs.cc:8751/Android/Test/Media/3gp/test.3gp";
    //http://www.dubblogs.cc:8751/Android/Test/Media/3gp/test2.3gp
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    mEditText01.setText(strVideoURL);
    
    /* 初始化临时盘路径数组 */
    aryFileDownloaded = new ArrayList<String>(); 
    
    /* 绑定Layout上的SurfaceView */
    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
    
    /* 设定PixnelFormat */
    getWindow().setFormat(PixelFormat.TRANSPARENT);
    
    /* 设定SurfaceHolder为Layout SurfaceView */
    mSurfaceHolder01 = mSurfaceView01.getHolder();
    mSurfaceHolder01.addCallback(this);
    
    /* 由于原有的影片Size较小，故指定其为固定比例 */
    mSurfaceHolder01.setFixedSize(160, 128);
    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    
    mPlay = (ImageButton) findViewById(R.id.play);
    mReset = (ImageButton) findViewById(R.id.reset);
    mPause = (ImageButton) findViewById(R.id.pause);
    mStop = (ImageButton) findViewById(R.id.stop);
    
    /* 播放按钮 */
    mPlay.setOnClickListener(new ImageButton.OnClickListener()
    {
      public void onClick(View view)
      {
        if(checkSDCard())
        {
          strVideoURL = mEditText01.getText().toString();
          playVideo(strVideoURL);
          mTextView01.setText(R.string.str_play);
        }
        else
        {
          mTextView01.setText(R.string.str_err_nosd);
        }
      }
    });
    
    /* 重新播放按钮 */
    mReset.setOnClickListener(new ImageButton.OnClickListener()
    {
      public void onClick(View view)
      {
        if(checkSDCard())
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
        else
        {
          mTextView01.setText(R.string.str_err_nosd);
        }
      }
    });
    
    /* 暂停按钮 */
    mPause.setOnClickListener(new ImageButton.OnClickListener()
    {
      public void onClick(View view)
      {
        if(checkSDCard())
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
        else
        {
          mTextView01.setText(R.string.str_err_nosd);
        }
      }
    }); 
    
    /* 终止按钮 */
    mStop.setOnClickListener(new ImageButton.OnClickListener()
    {
      public void onClick(View view)
      {
        if(checkSDCard())
        {
          try
          {
            if (mMediaPlayer01 != null)
            {
              if(bIsReleased==false)
              {
                mMediaPlayer01.seekTo(0);
                mMediaPlayer01.pause();
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
        else
        {
          mTextView01.setText(R.string.str_err_nosd);
        }
      }
    });
  }
  
  /* 自定义下载URL影片并播放 */ 
  private void playVideo(final String strPath)
  {
    try
    {
      /* 若传入的strPath为现有播放的链接，则直接播放 */
      if (strPath.equals(currentFilePath) && mMediaPlayer01 != null)
      {
        mMediaPlayer01.start();
        return;
      }
      else if(mMediaPlayer01 != null)
      {
        mMediaPlayer01.stop();
      }
      
      currentFilePath = strPath;
      
      /* 重新建构MediaPlayer对象 */
      mMediaPlayer01 = new MediaPlayer();
      /* 设定播放音量 */
      mMediaPlayer01.setAudioStreamType(2);
      
      /* 设定显示于SurfaceHolder */
      mMediaPlayer01.setDisplay(mSurfaceHolder01);
      
      mMediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener()
      {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra)
        {
          // TODO Auto-generated method stub
          Log.i(TAG, "Error on Listener, what: " + what + "extra: " + extra);
          return false;
        }
      });
      
      mMediaPlayer01.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
      {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent)
        {
          // TODO Auto-generated method stub
          Log.i(TAG, "Update buffer: " + Integer.toString(percent) + "%");
        }
      });
      
      mMediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
      {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
          // TODO Auto-generated method stub
          Log.i(TAG,"mMediaPlayer01 Listener Completed");
          mTextView01.setText(R.string.str_done);
        }
      });
      
      mMediaPlayer01.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
      {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
          // TODO Auto-generated method stub
          Log.i(TAG,"Prepared Listener");
        }
      });
      
      Runnable r = new Runnable()
      {
        public void run()
        {
          try
          {
            /* 在线程运行中，调用自定义函数抓下文件 */
            setDataSource(strPath);
            /* 下载完后才会调用prepare */
            mMediaPlayer01.prepare();
            Log.i(TAG, "Duration: " + mMediaPlayer01.getDuration());
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
        mMediaPlayer01.stop();
        mMediaPlayer01.release();
      }
    }
  }
  
  /* 自定义setDataSource，由线程启动 */
  private void setDataSource(String strPath) throws Exception
  {
    if (!URLUtil.isNetworkUrl(strPath))
    {
      mMediaPlayer01.setDataSource(strPath);
    }
    else
    {
      if(bIsReleased == false)
      {
        URL myURL = new URL(strPath);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        if (is == null)
        {
          throw new RuntimeException("stream is null");
        }
        File myFileTemp = File.createTempFile("hippoplayertmp", "."+getFileExtension(strPath));
        currentTempFilePath = myFileTemp.getAbsolutePath();
        
        /*currentTempFilePath = /sdcard/mediaplayertmp39327.dat */
        
        
        if(currentTempFilePath!="")
        {
          Log.i(TAG, currentTempFilePath);
          aryFileDownloaded.add(currentTempFilePath);
        }
        
        
        FileOutputStream fos = new FileOutputStream(myFileTemp);
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
  
  private String getFileExtension(String strFileName)
  {
    File myFile = new File(strFileName);
    String strFileExtension=myFile.getName();
    strFileExtension=(strFileExtension.substring(strFileExtension.lastIndexOf(".")+1)).toLowerCase();
    if(strFileExtension=="")
    {
      /* 若无法顺利取得扩展名，预设为.dat */
      strFileExtension = "dat";
    }
    return strFileExtension;
  }
  
  private void delFile(String strFileName)
  {
    File myFile = new File(strFileName);
    if(myFile.exists())
    {
      myFile.delete();
    }
  }
  
  private boolean checkSDCard()
  {
    /* 判断记忆卡是否存在 */
    if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  @Override 
  public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed");
  }
  
  @Override
  public void surfaceCreated(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed");
  }
  
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed");
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    /* 删除所有下载的临时文件 */
    for(int i=0;i<aryFileDownloaded.size();i++)
    {
      delFile(aryFileDownloaded.get(i).toString());
    }
    super.onPause();
  }
}

