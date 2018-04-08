package irdc.EX07_09;

import android.app.Activity; 
import android.media.MediaPlayer; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.ImageButton;
import android.widget.TextView; 

public class EX07_09 extends Activity 
{ 
  /*声明一个 ImageButton,TextView,MediaPlayer变量*/
  private ImageButton mButton01, mButton02, mButton03;
  private TextView mTextView01; 
  private MediaPlayer mMediaPlayer01; 
  /*声明一个Flag做为确认音乐是否暂停的变量并预设为false*/
  private boolean bIsPaused = false; 

   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /*透过findViewById建构巳建立TextView与ImageView对象*/ 
    mButton01 =(ImageButton) findViewById(R.id.myButton1); 
    mButton02 =(ImageButton) findViewById(R.id.myButton2); 
    mButton03 =(ImageButton) findViewById(R.id.myButton3);
    mTextView01 = (TextView) findViewById(R.id.myTextView1); 
     
    /* onCreate时建立MediaPlayer对象 */ 
    mMediaPlayer01 = new MediaPlayer();  
    /* 将音乐以Import的方式保存?res/raw/always.mp3 */ 
    mMediaPlayer01 = MediaPlayer.create(EX07_09.this, R.raw.big); 

    /* 执行播放音乐的按钮 */ 
    mButton01.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      /*重写OnClick事件*/
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub         
        try 
        { 
          
          if (mMediaPlayer01 != null) 
          { 
            mMediaPlayer01.stop(); 
          } 
          /*?MediaPlayer取得播放资源与stop()之后
           * 要准备Playback的状态前几定要使用MediaPlayer.prepare()*/
          mMediaPlayer01.prepare(); 
          /*开始或反复播放*/
          mMediaPlayer01.start(); 
          /*改变TextView为开始播放状态*/
          mTextView01.setText(R.string.str_start);  
        } 
        catch (Exception e) 
        { 
          // TODO Auto-generated catch block 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      } 
    }); 
     
    /* 停止播放 */ 
    mButton02.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        // TODO Auto-generated method stub 
        try 
        { 
          if (mMediaPlayer01 != null) 
          { 
            /*停止播放*/
            mMediaPlayer01.stop(); 
            /*改变TextView为停止播放状态*/
            mTextView01.setText(R.string.str_close);
          } 
            
        } 
        catch (Exception e) 
        { 
          // TODO Auto-generated catch block 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      } 
    }); 
    
    /* 暂停播放 */ 
    mButton03.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        // TODO Auto-generated method stub 
        try 
        { 
          if (mMediaPlayer01 != null) 
          { 
            /*是?为暂停状态=?*/
            if(bIsPaused==false) 
            { 
              /*暂停播放*/
              mMediaPlayer01.pause(); 
              /*设定Flag为treu表示 Player状态为暂停*/
              bIsPaused = true; 
              /*改变TextView为暂停播放*/
              mTextView01.setText(R.string.str_pause); 
            } 
            /*是?为暂停状态=是*/
            else if(bIsPaused==true) 
            { 
              /*并复播叨状态*/
              mMediaPlayer01.start(); 
              /*设定Flag为false表示 Player状态为非暂停状态*/
              bIsPaused = false; 
              /*改变TextView为开始播放*/
              mTextView01.setText(R.string.str_start); 
            } 
          }          
        } 
        catch (Exception e) 
        { 
          // TODO Auto-generated catch block 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      } 
    }); 
     
    /* 当MediaPlayer.OnCompletionLister会执行的Listener */  
    mMediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
    { 
      // @Override 
      /*重写文件播放完毕事件*/
      public void onCompletion(MediaPlayer arg0) 
      { 
        try 
        { 
          /*解除资源与MediaPlayer的指派关系
           * 资源*/
          mMediaPlayer01.release(); 
          /*改变TextView为播放结束*/
          mTextView01.setText(R.string.str_OnCompletionListener); 
        } 
        catch (Exception e) 
        { 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      } 
    }); 
     
    /* 当MediaPlayer.OnErrorListener会执行的Listener */ 
    mMediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() 
    { 
      @Override 
      /*重写错误处理事件*/
      public boolean onError(MediaPlayer arg0, int arg1, int arg2) 
      { 
        // TODO Auto-generated method stub 
        try 
        { 
          /*发生错误时夕解除资源与MediaPlayer的指派*/
          mMediaPlayer01.release(); 
          mTextView01.setText(R.string.str_OnErrorListener); 
        } 
        catch (Exception e) 
        { 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
        return false; 
      } 
    }); 
  } 

  @Override 
  /*重写加程序暂停状态事件*/
  protected void onPause() 
  { 
    // TODO Auto-generated method stub 
    try 
    { 
      /*圳加程序暂停时解除资源与MediaPlayer的指派关系*/
      mMediaPlayer01.release(); 
    } 
    catch (Exception e) 
    { 
      mTextView01.setText(e.toString()); 
      e.printStackTrace(); 
    } 
    super.onPause(); 
  } 
} 

