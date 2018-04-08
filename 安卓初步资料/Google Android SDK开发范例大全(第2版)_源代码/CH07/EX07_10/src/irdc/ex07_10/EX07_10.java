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
  /*�趨bIsReleasedһ��ʼΪfalse */
  private boolean bIsReleased = false;
  /*�趨bIsPausedһ��ʼΪfalse */
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
 
    /*��ʼ��ť */
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
            /*�� MediaPlayer����*/
            myPlayer1.reset();            
          }
          /*�趨 MediaPlayer��ȡSDcard�ĵ���*/
          myPlayer1.setDataSource( "/sdcard/nana.mp3" );
          myPlayer1.prepare();
          /*�� MediaPlayer��ʼ����*/
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
         
        /* �����ֲ����ִ�е�Listener */  
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
     
    /*��ͣ��ť */ 
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
              /*�趨 MediaPlayer��ͣ����*/
              myPlayer1.pause(); 
              bIsPaused = true; 
              mTextView1.setText(R.string.str_pause); 
              mStart.setImageResource(R.drawable.star);
              mPause.setImageResource(R.drawable.pause_2);
            } 
            else if(bIsPaused==true) 
            { 
              /*�趨 MediaPlayer����*/
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
    
    /*����һ�׸�İ�ť */
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
            /*�� MediaPlayer����*/ 
            myPlayer1.reset();            
          } 
          /*�趨 MediaPlayer��ȡSDcard���ļ�*/
          myPlayer1.setDataSource( "/sdcard/bb.mp3" );
          myPlayer1.prepare();
          /*����MediaPlayer*/
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
    
    /*����һ�׸谴ť */
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
            /*�� MediaPlayer����*/ 
            myPlayer1.reset();            
          }
          /*�趨 MediaPlayer��ȡSDcard���ļ�*/
          myPlayer1.setDataSource( "/sdcard/nana.mp3" );
          myPlayer1.prepare();
          /*�� MediaPlayer����*/
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
    
    /*ֹͣ��ť */
    mStop.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        
        if(myPlayer1.isPlaying()==true) 
        { 
          /*�� MediaPlayer����*/
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

