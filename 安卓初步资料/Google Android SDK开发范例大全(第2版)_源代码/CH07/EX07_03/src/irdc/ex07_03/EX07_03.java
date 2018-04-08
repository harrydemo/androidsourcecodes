package irdc.ex07_03;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EX07_03 extends Activity
{
  private TextView mTextView01;
  private ImageView mImageView01;
  
  /* LayoutInflater������Ϊ�½�AlertDialog֮�� */
  private LayoutInflater mInflater01;
  
  /* ���������View */
  private View mView01;
  private EditText mEditText01,mEditText02;
  
  /* menuѡ��identifier�����Ա�ʶ�¼� */
  static final private int MENU_ABOUT = Menu.FIRST;
  static final private int MENU_EXIT = Menu.FIRST+1;
  private Handler mHandler01 = new Handler();
  private Handler mHandler02 = new Handler();
  private Handler mHandler03 = new Handler();
  private Handler mHandler04 = new Handler();
  /* ����User��ֹ��?��Counter */
  private int intCounter1, intCounter2;
  /* ����FadeIn��Fade Out��Counter */
  private int intCounter3, intCounter4;
  /* ����ѭ���滻����ͼID��Counter  */
  private int intDrawable=0;
  /* ��һ��User�в�����Time Stamp */
  private Date lastUpdateTime;
  /* ����User������û�в��� */
  private long timePeriod;
  /* ��ֹ����n�뽫�Զ�������Ļ���� */
  private float fHoldStillSecond = (float) 5;
  private boolean bIfRunScreenSaver;
  private boolean bFadeFlagOut, bFadeFlagIn = false;
  private long intervalScreenSaver = 1000;
  private long intervalKeypadeSaver = 1000;
  private long intervalFade = 100;
  private int screenWidth, screenHeight;
  /* ÿn���û�ͼƬ */
  private int intSecondsToChange = 5;
  
  /* �趨Screen Saver��Ҫ�õ��ı���ͼ */
  private static int[] screenDrawable = new int[]
  {
    R.drawable.screen1,
    R.drawable.screen2,
    R.drawable.screen3,
    R.drawable.screen4,
    R.drawable.screen5
  };
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    /* ����?setContentView֮ǰ���û���Ļ��ʾ */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags
    (
      WindowManager.LayoutParams.FLAG_FULLSCREEN,
      WindowManager.LayoutParams.FLAG_FULLSCREEN
    );
    setContentView(R.layout.main);
    
    /* onCreate all Widget */
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mImageView01 = (ImageView)findViewById(R.id.myImageView1);
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    
    /* ��ʼȡ��User�����ֻ���ʱ�� */
    lastUpdateTime = new Date(System.currentTimeMillis());
    
    /* ��ʼ��Layout�ϵ�Widget?���� */
    recoverOriginalLayout();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // TODO Auto-generated method stub
    
    /* menuȺ��ID */
    int idGroup1 = 0;
    
    /* The order position of the item */
    int orderMenuItem1 = Menu.NONE;
    int orderMenuItem2 = Menu.NONE+1;
    
    /* ��������SubMenu��menu */
    menu.add(idGroup1, MENU_ABOUT, orderMenuItem1, R.string.app_about);
    /* �����뿪 */
    menu.add(idGroup1, MENU_EXIT, orderMenuItem2, R.string.str_exit);
    menu.setGroupCheckable(idGroup1, true, true);
    
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // TODO Auto-generated method stub
    switch(item.getItemId())
    {
      case (MENU_ABOUT):
        new AlertDialog.Builder
        (
          EX07_03.this
        ).setTitle(R.string.app_about).setIcon
        (
          R.drawable.hippo
        ).setMessage
        (
          R.string.app_about_msg
        ).setPositiveButton(R.string.str_ok,
        new DialogInterface.OnClickListener()
        {
          public void onClick
          (DialogInterface dialoginterface, int i)
          {
          }
        }).show();
        break;
      case (MENU_EXIT):
        /* �뿪���� */
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
  
  /* ���Userû�в������߳� */
  private Runnable mTasks01 = new Runnable() 
  {
    public void run() 
    {
      intCounter1++;
      Date timeNow = new Date(System.currentTimeMillis());
      
      /* ����User��ֹ������ʱ���� */
      timePeriod = (long)timeNow.getTime() - (long)lastUpdateTime.getTime();
      
      float timePeriodSecond = ((float)timePeriod/1000);
      
      /* �������ʱ�侲ֹ���� */
      if(timePeriodSecond>fHoldStillSecond)
      {
        /* ��ֹ����ʱ��ڼ��εı�� */
        if(bIfRunScreenSaver==false)
        {
          /* �����߳�2 */
          mHandler02.postDelayed(mTasks02, intervalScreenSaver);
          
          /* Fade Out*/
          if(intCounter1%(intSecondsToChange)==0)
          {
            bFadeFlagOut=true;
            mHandler03.postDelayed(mTasks03, intervalFade);
          }
          else
          {
            /* ��Fade Out������Fade In */
            if(bFadeFlagOut==true)
            {
              bFadeFlagIn=true;
              mHandler04.postDelayed(mTasks04, intervalFade);
            }
            else
            {
              bFadeFlagIn=false;
              intCounter4 = 0;
              mHandler04.removeCallbacks(mTasks04);
            }
            intCounter3 = 0;
            bFadeFlagOut = false;
          }
          bIfRunScreenSaver = true;
        }
        else
        {
          /* screen saver ���������� */
          
          /* Fade Out*/
          if(intCounter1%(intSecondsToChange)==0)
          {
            bFadeFlagOut=true;
            mHandler03.postDelayed(mTasks03, intervalFade);
          }
          else
          {
            /* ��Fade Out������Fade In */
            if(bFadeFlagOut==true)
            {
              bFadeFlagIn=true;
              mHandler04.postDelayed(mTasks04, intervalFade);
            }
            else
            {
              bFadeFlagIn=false;
              intCounter4 = 0;
              mHandler04.removeCallbacks(mTasks04);
            }
            intCounter3 = 0;
            bFadeFlagOut=false;
          }
        }
      }
      else
      {
        /* ��Userû�ж��ļ��δ����ʱ�� */
        bIfRunScreenSaver = false;
        /* �ָ�ԭ����Layout Visible*/
        recoverOriginalLayout();
      }
      
      /* ��LogCat�࿴User��ֹ������ʱ���� */
      Log.i("HIPPO", "Counter1:"+Integer.toString(intCounter1)+"/"+Float.toString(timePeriodSecond));
      
      /* ����ִ���߳�1 */
      mHandler01.postDelayed(mTasks01, intervalKeypadeSaver);
    } 
  };
  
  /* Screen Saver Runnable */
  private Runnable mTasks02 = new Runnable() 
  {
    public void run() 
    {
      if(bIfRunScreenSaver==true)
      {
        intCounter2++;
        
        hideOriginalLayout();
        showScreenSaver();
        
        //Log.i("HIPPO", "Counter2:"+Integer.toString(intCounter2));
        mHandler02.postDelayed(mTasks02, intervalScreenSaver);
      }
      else
      {
        mHandler02.removeCallbacks(mTasks02);
      }
    } 
  };
  
  /* Fade Out��ЧRunnable */
  private Runnable mTasks03 = new Runnable() 
  {
    public void run() 
    {
      if(bIfRunScreenSaver==true && bFadeFlagOut==true)
      {
        intCounter3++;
        
        /* �趨ImageView��͸���Ƚ�����ȥ */
        mImageView01.setAlpha(255-intCounter3*28);
        
        Log.i("HIPPO", "Fade out:"+Integer.toString(intCounter3));
        mHandler03.postDelayed(mTasks03, intervalFade);
      }
      else
      {
        mHandler03.removeCallbacks(mTasks03);
      }
    } 
  };
  
  /* Fade In��ЧRunnable */
  private Runnable mTasks04 = new Runnable() 
  {
    public void run() 
    {
      if(bIfRunScreenSaver==true && bFadeFlagIn==true)
      {
        intCounter4++;
        
        /* �趨ImageView��͸���Ƚ������� */
        mImageView01.setAlpha(intCounter4*28);
        
        mHandler04.postDelayed(mTasks04, intervalFade);
        Log.i("HIPPO", "Fade In:"+Integer.toString(intCounter4));
      }
      else
      {
        mHandler04.removeCallbacks(mTasks04);
      }
    } 
  };
  
  /* �ָ�ԭ�е�Layout������ */
  private void recoverOriginalLayout()
  {
    mTextView01.setVisibility(View.VISIBLE);
    mEditText01.setVisibility(View.VISIBLE);
    mImageView01.setVisibility(View.GONE);
  }
  
  /* ����ԭ��Ӧ�ó�����İ���������� */
  private void hideOriginalLayout()
  {
    /* �������ص�Widgetд�ڴ� */
    mTextView01.setVisibility(View.INVISIBLE);
    mEditText01.setVisibility(View.INVISIBLE);
  }
  
  /* ��ʼScreenSaver */
  private void showScreenSaver()
  {
    /* ��Ļ����֮��Ҫ�����¼�д�ڴ�*/
    
    if(intDrawable>4)
    {
      intDrawable = 0;
    }
    
    DisplayMetrics dm=new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    screenWidth = dm.widthPixels;
    screenHeight = dm.heightPixels;
    Bitmap bmp=BitmapFactory.decodeResource(getResources(), screenDrawable[intDrawable]);
    
    /* Matrix���� */ 
    float scaleWidth = ((float) screenWidth) / bmp.getWidth();
    float scaleHeight = ((float) screenHeight) / bmp.getHeight() ;
    
    Matrix matrix = new Matrix(); 
    /* ʹ��Matrix.postScale�趨ά��ReSize */ 
    matrix.postScale(scaleWidth, scaleHeight);
    
    /* ReSizeͼ�ļ�����Ļ�ֱ��� */
    Bitmap resizedBitmap = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
    BitmapDrawable myNewBitmapDrawable = new BitmapDrawable(resizedBitmap); 
    mImageView01.setImageDrawable(myNewBitmapDrawable);
    
    /* ʹImageView�ɼ� */
    mImageView01.setVisibility(View.VISIBLE);
    
    /* ÿ����趨�����û�ͼƬID������һ��runnable2�Ż���Ч */
    if(intCounter2%intSecondsToChange==0)
    {
      intDrawable++;
    }
  }
  
  public void onUserWakeUpEvent()
  {
    if(bIfRunScreenSaver==true)
    {
      try
      {
        /* LayoutInflater.fromȡ�ô�Activity��context */
        mInflater01 = LayoutInflater.from(EX07_03.this);
        
        /* ������������ʹ��View��Layout */
        mView01 = mInflater01.inflate(R.layout.securescreen, null);
        
        /* �ڶԻ�����Ψһ��EditText�ȴ������������ */
        mEditText02 = (EditText) mView01.findViewById(R.id.myEditText2);
        
        /* ����AlertDialog */
        new AlertDialog.Builder(this)
        .setView(mView01)
        .setPositiveButton("OK",
        new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            /* �ȶ������������ԭActivity����趨�Ƿ���� */
            if(mEditText01.getText().toString().equals(mEditText02.getText().toString()))
            {
              /* ��������ȷ?��Ľ�����Ļ����װ�� */
              resetScreenSaverListener();
            }
          }
        }).show();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void updateUserActionTime()
  {
    /* ȡ�ð�أ�����¼�ʱ��ϵͳTime Millis */
    Date timeNow = new Date(System.currentTimeMillis());
    /* ���¼��㰴�°��������ϼ��ξ�ֹ��ʱ���� */
    timePeriod = (long)timeNow.getTime() - (long)lastUpdateTime.getTime();
    lastUpdateTime.setTime(timeNow.getTime());
  }
  
  public void resetScreenSaverListener()
  {
    /* �Ƴ����е�Runnable */
    mHandler01.removeCallbacks(mTasks01);
    mHandler02.removeCallbacks(mTasks02);
    
    /* ȡ�ð��°����¼�ʱ��ϵͳTime Millis */
    Date timeNow = new Date(System.currentTimeMillis());
    /* ���¼��㰴�°���������һ�ξ�ֹ��ʱ���� */
    timePeriod = (long)timeNow.getTime() - (long)lastUpdateTime.getTime();
    lastUpdateTime.setTime(timeNow.getTime());
    
    /* for Runnable2��ȡ����Ļ���� */
    bIfRunScreenSaver = false;
    
    /* ����Runnable1��Runnable1��Counter */
    intCounter1 = 0;
    intCounter2 = 0;
    
    /* �ָ�ԭ����Layout Visible*/
    recoverOriginalLayout();
    
    /* ����postDelayed()�µ�Runnable */
    mHandler01.postDelayed(mTasks01, intervalKeypadeSaver);
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    // TODO Auto-generated method stub
    if(bIfRunScreenSaver==true && keyCode!=4)
    {
      /* ����Ļ������������ִ���У����������Ļ�������� */
      onUserWakeUpEvent();
    }
    else
    {
      /* ����Userδ�����ֻ���ʱ����� */
      updateUserActionTime();
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    // TODO Auto-generated method stub
    if(bIfRunScreenSaver==true)
    {
      /* ����Ļ����������?ִ���У����������Ļ�������� */
      onUserWakeUpEvent();
    }
    else
    {
      /* ����Userδ�����ֻ���ʱ����� */
      updateUserActionTime();
    }
    return super.onTouchEvent(event);
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    mHandler01.postDelayed(mTasks01, intervalKeypadeSaver);
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    try
    {
      /* �Ƴ�ִ���е��߳� */
      mHandler01.removeCallbacks(mTasks01);
      mHandler02.removeCallbacks(mTasks02);
      mHandler03.removeCallbacks(mTasks03);
      mHandler04.removeCallbacks(mTasks04);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onPause();
  }
}

