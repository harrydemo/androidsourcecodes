package irdc.EX10_07;

import java.util.Date; 
import java.util.List;
import java.util.Random;
import android.app.Activity; 
import android.content.Context; 
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener; 
import android.hardware.SensorManager; 
import android.media.MediaPlayer; 
import android.os.Bundle; 
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView; 

public class EX10_07 extends Activity 
{ 
  private TextView mTextView01; 
  private ImageView mImageView01;
  private SensorManager mSensorManager01;  
  private float velocityFinal = 0; 
  private MediaPlayer mMediaPlayer01; 
  private MediaPlayer mMediaPlayer02;
  private MediaPlayer mMediaPlayer03;
  private Date lastUpdateTime; 
  private boolean bIfMakeSound = true; 
  private int godanswer = 0;
  private final int saint=1;
  private final int laugh=2;
  private final int bad=3;
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    /*取得目前系统时间*/ 
    lastUpdateTime = new Date(System.currentTimeMillis()); 
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mImageView01 = (ImageView)findViewById(R.id.myImageView1);

    mSensorManager01 =  
    (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
    /*透过MediaPlayer.create()来取得raw底下音乐文件*/
    mMediaPlayer01 = new MediaPlayer(); 
    mMediaPlayer01 = MediaPlayer.create(EX10_07.this, R.raw.saint); 
    mMediaPlayer02 = new MediaPlayer(); 
    mMediaPlayer02 = MediaPlayer.create(EX10_07.this, R.raw.laugh); 
    mMediaPlayer03 = new MediaPlayer(); 
    mMediaPlayer03 = MediaPlayer.create(EX10_07.this, R.raw.bad); 
  }
  
  @Override 
  protected void onResume() 
  { 
    // TODO Auto-generated method stub 
    /*注册SensorEvenListener与设定参数*/
    
    List<Sensor> sensors = 
    mSensorManager01.getSensorList(SensorManager.SENSOR_ACCELEROMETER);
    mSensorManager01.registerListener
    (
      mSensorLisener,
      sensors.get(0),
      SensorManager.SENSOR_DELAY_UI
    );
    super.onResume(); 
  } 
  
  @Override 
  protected void onPause() 
  { 
    /*解除SensorEvenListener的设定*/
    mSensorManager01.unregisterListener(mSensorLisener); 
    super.onPause(); 
  }   
  
  /*建立SensorEventListener对象*/
  private final SensorEventListener mSensorLisener = 
  new SensorEventListener() 
  { 
    /*声明变量*/
    private float previousAcceleration; 
     /*重写onSensorChanged事件来监测手机投掷事件*/
    @Override 
    public void onSensorChanged(SensorEvent event) 
    { 
      // TODO Auto-generated method stub 
      /*加速度是否存在*/
      if(event.sensor.getType()==SensorManager.SENSOR_ACCELEROMETER)
      { 
        float x = event.values[SensorManager.DATA_X]; 
        float y = event.values[SensorManager.DATA_Y]; 
        float z = event.values[SensorManager.DATA_Z]; 
         /*手机是否落下的速度标准*/
        double forceThreshHold = 1.5f;        
        double appliedAcceleration = 0.0f; 
         
        /* SensorManager.GRAVITY_EARTH = 9.8m/s2 */ 
        appliedAcceleration +=  
        Math.pow(x/SensorManager.GRAVITY_EARTH, 2.0);  
         
        appliedAcceleration +=  
        Math.pow(y/SensorManager.GRAVITY_EARTH, 2.0);  
         
        appliedAcceleration +=  
        Math.pow(z/SensorManager.GRAVITY_EARTH, 2.0);  
         
        appliedAcceleration = Math.sqrt(appliedAcceleration);  
          
        /*判断手机是否落下的判断式
         * 落地当时速度需小于1.5f, 落地前速度必须大于1.5f*/
        if((appliedAcceleration < forceThreshHold) &&  
           (previousAcceleration > forceThreshHold)) 
        { 
          /* Shake手机事件触发 */ 
          Date timeNow = new Date(System.currentTimeMillis()); 
           
          /* 从初速度到末速度所经过的时间 */ 
          long timePeriod =  
          (long)timeNow.getTime() - (long)lastUpdateTime.getTime();
           
          /* (v) = a*t */ 
          velocityFinal =  
          (float) (appliedAcceleration * ((float)timePeriod/1000));
          Log.i("V=",Float.toString(velocityFinal));

          if(bIfMakeSound==true) 
          { 
            /*Random Number产生器*/
            Random generator=new Random();
            /*掷筊*/
            godanswer = generator.nextInt(3) + 1;
            
            switch (godanswer)
            {
             /*圣筊*/
            case saint:
              mTextView01.setText 
              ( 
                getResources().getText(R.string.str_saint).toString() 
              ); 
              mImageView01.setImageDrawable
              (getResources().getDrawable(R.drawable.saint));
              if (mMediaPlayer01 != null) 
              { 
                try 
                { 
                  mMediaPlayer01.seekTo(0); 
                  mMediaPlayer01.stop(); 
                  mMediaPlayer01.prepare(); 
                  mMediaPlayer01.setVolume(10, 10);
                  mMediaPlayer01.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              } 
              else 
              { 
                try 
                { 
                  mMediaPlayer01.prepare(); 
                  mMediaPlayer01.setVolume(10, 10);
                  mMediaPlayer01.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              }
              break;
            /*笑筊*/
            case laugh:
              mTextView01.setText 
              ( 
                getResources().getText(R.string.str_laugh).toString() 
              ); 
              mImageView01.setImageDrawable
              (getResources().getDrawable(R.drawable.laugh));
              if (mMediaPlayer02 != null) 
              { 
                try 
                { 
                  mMediaPlayer02.seekTo(0); 
                  mMediaPlayer02.stop(); 
                  mMediaPlayer02.prepare(); 
                  mMediaPlayer02.setVolume(10, 10);
                  mMediaPlayer02.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              } 
              else 
              { 
                try 
                { 
                  mMediaPlayer02.prepare(); 
                  mMediaPlayer02.setVolume(10, 10);
                  mMediaPlayer02.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              }
              break;
              /*阴筊*/
            case bad:
              mTextView01.setText 
              ( 
                getResources().getText(R.string.str_bad).toString() 
              ); 
              mImageView01.setImageDrawable
              (getResources().getDrawable(R.drawable.bad));
              if (mMediaPlayer03 != null) 
              { 
                try 
                { 
                  mMediaPlayer03.seekTo(0); 
                  mMediaPlayer03.stop(); 
                  mMediaPlayer03.prepare(); 
                  mMediaPlayer03.setVolume(10, 10);
                  mMediaPlayer03.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              } 
              else 
              { 
                try 
                { 
                  mMediaPlayer03.prepare(); 
                  mMediaPlayer03.setVolume(10, 10);
                  mMediaPlayer03.start(); 
                } 
                catch(Exception e) 
                { 
                  mTextView01.setText(e.toString()); 
                  e.printStackTrace(); 
                } 
              }
              break;
            }
          }
        }
        else 
        { 
          /* 末速度=0 */ 
          /* 更新lastUpdateTime为现在 */ 
          Date timeNow = new Date(System.currentTimeMillis()); 
          lastUpdateTime.setTime(timeNow.getTime()); 
        } 
        previousAcceleration = (float) appliedAcceleration; 
      } 
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
      // TODO Auto-generated method stub
    }
  }; 
} 

