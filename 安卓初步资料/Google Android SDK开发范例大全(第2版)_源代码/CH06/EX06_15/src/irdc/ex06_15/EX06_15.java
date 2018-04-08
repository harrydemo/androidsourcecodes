package irdc.ex06_15;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EX06_15 extends Activity
{
  /* 建立自定义mServiceReceiver对象 */
  private mServiceReceiver mReceiver01;
  private TextView mTextView01;
  private Button mButton01, mButton02;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    
    /* 启动系统服务（Service） */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 开始执行后台服务(Service) */
        Intent i = new Intent( EX06_15.this, mService1.class ); 
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
        startService(i);
        mMakeTextToast
        (
          getResources().getText(R.string.str_service_online).toString(),
          false
        );
      }
    });
    
    /* 终止系统服务（Service） */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 指定终止执行后台服务(mService1) */
        Intent i = new Intent( EX06_15.this, mService1.class );
        
        /* 顺?关闭系统服务 */
        if(stopService(i)==true)
        {
          mTextView01.setText(R.string.str_button2);
          mMakeTextToast
          (
            getResources().getText(R.string.str_service_offline).toString(),
            false
          );
        }
        else
        {
          /* 显示关闭服务失败 */
          mTextView01.setText(R.string.str_err_stopservicefailed);
        }
      }
    });
  }
  
  /* 建立继承告BroadcastReceiver的 mServiceReceiver类接受广播讯息 */
  public class mServiceReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      // TODO Auto-generated method stub
      
      try
      {
        /* 取并来自后台服务所Broadcast的参数 */
        Bundle bunde = intent.getExtras();
        String strParam1 = bunde.getString("STR_PARAM1");
        
        /* 将从Service里传来的广播讯息显示于TextView */
        mTextView01.setText(strParam1);
        Intent i = new Intent( EX06_15.this, mService1.class );
        if(stopService(i)==true)
        {
          mMakeTextToast
          (
            getResources().getText(R.string.str_service_offline).toString(),
            true
          );
        }
      }
      catch(Exception e)
      {
        mTextView01.setText(e.toString());
        e.getStackTrace();
      }
    }
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX06_15.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX06_15.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    try
    {
      /* 前景程序生命周期开始 */
      IntentFilter mFilter01;
      
      /* 自定义要过滤的广播讯息(DavidLanz) */
      mFilter01 = new IntentFilter(mService1.HIPPO_SERVICE_IDENTIFIER);
      mReceiver01 = new mServiceReceiver();
      registerReceiver(mReceiver01, mFilter01);
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.getStackTrace();
    }
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    /* 前景程序生命周期结束，解除刚守系统注册的Receiver */
    unregisterReceiver(mReceiver01);
    super.onPause();
  }
}

