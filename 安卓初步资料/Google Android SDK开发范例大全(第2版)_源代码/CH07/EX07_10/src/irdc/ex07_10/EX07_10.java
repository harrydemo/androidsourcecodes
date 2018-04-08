package irdc.ex07_10;

import java.io.IOException; 
import android.app.Activity; 
import android.media.MediaPlayer; 
import android.media.MediaPlayer.OnCompletionListener; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView; 

public class EX07_10 extends Activity 
{ 
  private ImageButton mPause, mNext, mBefore, mStart, mStop;
  private TextView mTextView1; 
  private ImageView mImageView1; 
  /*设定bIsReleased一开始为false */
  private boolean bIsReleased = false;
  /*设定bIsPaused一开始为false */
  private boolean bIsPaused = false; 
  public MediaPlayer myPlayer1 = new MediaPlayer();
    
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    mStart = (ImageButton) findViewById(R.id.myImageButton1); 
    mStop = (ImageButton) findViewById(R.id.myImageButton2);
    mPause = (ImageButton) findViewById(R.id.pause); 
    mNext = (ImageButton) findViewById(R.id.next);
    mBefore = (ImageButton) findViewById(R.id.before); 
    mImageView1 = (ImageView) findViewById(R.id.myImageView1);
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
 
    /*开始按钮 */
    mStart.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        mStart.setImageResource(R.drawable.stars);
        mImageView1.setImageResource(R.drawable.dance);
        mPause.setImageResource(R.drawable.pause);
        
        try 
        { 
          if(myPlayer1.isPlaying()==true) 
          {
            /*把 MediaPlayer重设*/
            myPlayer1.reset();            
          }
          /*设定 MediaPlayer读取SDcard的档案*/
          myPlayer1.setDataSource( "/sdcard/nana.mp3" );
          myPlayer1.prepare();
          /*把 MediaPlayer开始播放*/
          myPlayer1.start(); 
          mTextView1.setText(R.string.str_start); 
        } 
        catch (IllegalStateException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
         
        /* 当音乐播完会执行的Listener */  
        myPlayer1.setOnCompletionListener(new OnCompletionListener() 
        { 
          // @Override 
          public void onCompletion(MediaPlayer arg0) 
          {  
            mTextView1.setText(R.string.str_finished);
            mStart.setImageResource(R.drawable.star);
          } 
        });
      } 
    });     
     
    /*暂停按钮 */ 
    mPause.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      public void onClick(View view) 
      { 
        if (myPlayer1 != null) 
        { 
          if(bIsReleased == false) 
          { 
            if(bIsPaused==false) 
            { 
              /*设定 MediaPlayer暂停播放*/
              myPlayer1.pause(); 
              bIsPaused = true; 
              mTextView1.setText(R.string.str_pause); 
              mStart.setImageResource(R.drawable.star);
              mPause.setImageResource(R.drawable.pause_2);
            } 
            else if(bIsPaused==true) 
            { 
              /*设定 MediaPlayer播放*/
              myPlayer1.start(); 
              bIsPaused = false; 
              mTextView1.setText(R.string.str_start);
              mStart.setImageResource(R.drawable.stars);
              mPause.setImageResource(R.drawable.pause);
            } 
          } 
        } 
      } 
    }); 
    
    /*往下一首歌的按钮 */
    mNext.setOnClickListener(new ImageButton.OnClickListener() 
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub       
        mStart.setImageResource(R.drawable.stars);
        mImageView1.setImageResource(R.drawable.dance);
        
        try 
        { 
          if(myPlayer1.isPlaying()==true) 
          { 
            /*把 MediaPlayer重设*/ 
            myPlayer1.reset();            
          } 
          /*设定 MediaPlayer读取SDcard的文件*/
          myPlayer1.setDataSource( "/sdcard/bb.mp3" );
          myPlayer1.prepare();
          /*启动MediaPlayer*/
          myPlayer1.start(); 
          mTextView1.setText(R.string.str_start); 
        } 
        catch (IllegalStateException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      }      
    });
    
    /*往上一首歌按钮 */
    mBefore.setOnClickListener(new ImageButton.OnClickListener() 
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        mStart.setImageResource(R.drawable.stars);
        mImageView1.setImageResource(R.drawable.dance);
        
        try 
        { 
          if(myPlayer1.isPlaying()==true) 
          { 
            /*将 MediaPlayer重设*/ 
            myPlayer1.reset();            
          }
          /*设定 MediaPlayer读取SDcard的文件*/
          myPlayer1.setDataSource( "/sdcard/nana.mp3" );
          myPlayer1.prepare();
          /*把 MediaPlayer启动*/
          myPlayer1.start(); 
          mTextView1.setText(R.string.str_start); 
        } 
        catch (IllegalStateException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        { 
          // TODO Auto-generated catch block 
          mTextView1.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      }      
    });
    
    /*停止按钮 */
    mStop.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        
        if(myPlayer1.isPlaying()==true) 
        { 
          /*将 MediaPlayer重设*/
          myPlayer1.reset(); 
          mTextView1.setText(R.string.str_stopped);
          mStart.setImageResource(R.drawable.star);
          mPause.setImageResource(R.drawable.pause);
          mImageView1.setImageResource(R.drawable.black);
        } 
      } 
    }); 
  } 
}

