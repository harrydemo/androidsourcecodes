package irdc.EX07_09;

import android.app.Activity; 
import android.media.MediaPlayer; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.ImageButton;
import android.widget.TextView; 

public class EX07_09 extends Activity 
{ 
  /*����һ�� ImageButton,TextView,MediaPlayer����*/
  private ImageButton mButton01, mButton02, mButton03;
  private TextView mTextView01; 
  private MediaPlayer mMediaPlayer01; 
  /*����һ��Flag��Ϊȷ�������Ƿ���ͣ�ı�����Ԥ��Ϊfalse*/
  private boolean bIsPaused = false; 

   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /*͸��findViewById�����Ƚ���TextView��ImageView����*/ 
    mButton01 =(ImageButton) findViewById(R.id.myButton1); 
    mButton02 =(ImageButton) findViewById(R.id.myButton2); 
    mButton03 =(ImageButton) findViewById(R.id.myButton3);
    mTextView01 = (TextView) findViewById(R.id.myTextView1); 
     
    /* onCreateʱ����MediaPlayer���� */ 
    mMediaPlayer01 = new MediaPlayer();  
    /* ��������Import�ķ�ʽ����?res/raw/always.mp3 */ 
    mMediaPlayer01 = MediaPlayer.create(EX07_09.this, R.raw.big); 

    /* ִ�в������ֵİ�ť */ 
    mButton01.setOnClickListener(new ImageButton.OnClickListener() 
    { 
      @Override 
      /*��дOnClick�¼�*/
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub         
        try 
        { 
          
          if (mMediaPlayer01 != null) 
          { 
            mMediaPlayer01.stop(); 
          } 
          /*?MediaPlayerȡ�ò�����Դ��stop()֮��
           * Ҫ׼��Playback��״̬ǰ����Ҫʹ��MediaPlayer.prepare()*/
          mMediaPlayer01.prepare(); 
          /*��ʼ�򷴸�����*/
          mMediaPlayer01.start(); 
          /*�ı�TextViewΪ��ʼ����״̬*/
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
     
    /* ֹͣ���� */ 
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
            /*ֹͣ����*/
            mMediaPlayer01.stop(); 
            /*�ı�TextViewΪֹͣ����״̬*/
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
    
    /* ��ͣ���� */ 
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
            /*��?Ϊ��ͣ״̬=?*/
            if(bIsPaused==false) 
            { 
              /*��ͣ����*/
              mMediaPlayer01.pause(); 
              /*�趨FlagΪtreu��ʾ Player״̬Ϊ��ͣ*/
              bIsPaused = true; 
              /*�ı�TextViewΪ��ͣ����*/
              mTextView01.setText(R.string.str_pause); 
            } 
            /*��?Ϊ��ͣ״̬=��*/
            else if(bIsPaused==true) 
            { 
              /*������߶״̬*/
              mMediaPlayer01.start(); 
              /*�趨FlagΪfalse��ʾ Player״̬Ϊ����ͣ״̬*/
              bIsPaused = false; 
              /*�ı�TextViewΪ��ʼ����*/
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
     
    /* ��MediaPlayer.OnCompletionLister��ִ�е�Listener */  
    mMediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
    { 
      // @Override 
      /*��д�ļ���������¼�*/
      public void onCompletion(MediaPlayer arg0) 
      { 
        try 
        { 
          /*�����Դ��MediaPlayer��ָ�ɹ�ϵ
           * ��Դ*/
          mMediaPlayer01.release(); 
          /*�ı�TextViewΪ���Ž���*/
          mTextView01.setText(R.string.str_OnCompletionListener); 
        } 
        catch (Exception e) 
        { 
          mTextView01.setText(e.toString()); 
          e.printStackTrace(); 
        } 
      } 
    }); 
     
    /* ��MediaPlayer.OnErrorListener��ִ�е�Listener */ 
    mMediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() 
    { 
      @Override 
      /*��д�������¼�*/
      public boolean onError(MediaPlayer arg0, int arg1, int arg2) 
      { 
        // TODO Auto-generated method stub 
        try 
        { 
          /*��������ʱϦ�����Դ��MediaPlayer��ָ��*/
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
  /*��д�ӳ�����ͣ״̬�¼�*/
  protected void onPause() 
  { 
    // TODO Auto-generated method stub 
    try 
    { 
      /*�ڼӳ�����ͣʱ�����Դ��MediaPlayer��ָ�ɹ�ϵ*/
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

