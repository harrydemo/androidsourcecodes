package irdc.ex10_08;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EX10_08 extends Activity
{
  public static final String MY_PREFS = "MosPre";
  private ImageButton button01;
  private ImageView image01;
  private int mosStatus;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    
    /* 取得保存在SharedPreferences的防蚊状态 */
    SharedPreferences pres = 
      getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
    if(pres !=null)
    {
      mosStatus = pres.getInt("status", 0);
    }
    
    image01 = (ImageView)findViewById(R.id.image01);
    button01 = (ImageButton)findViewById(R.id.button01);
    
    /*检查mosStatus是否启动状态(1) */
    if (mosStatus==1)
    {
      /* 设置启动图案 */
      image01.setImageResource(R.drawable.mos_open);
      button01.setBackgroundResource(R.drawable.power_on);
    }
    else
    {
      /* 设置关闭图案 */
      image01.setImageResource(R.drawable.mos_close);
      button01.setBackgroundResource(R.drawable.power_off);
    }
    
    button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (mosStatus==1)
        {
          SharedPreferences pres = 
            getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
          if(pres!=null)
          {
            /* 设定状态为关闭(0) */
            mosStatus=0;
            SharedPreferences.Editor ed = pres.edit(); 
            ed.putInt("status",mosStatus); 
            ed.commit();
          }
          /* 设定关闭图案 */
          image01.setImageResource(R.drawable.mos_close);
          button01.setBackgroundResource(R.drawable.power_off);
          /* 终止service */
          stopMyService(1);
        }
        else if(mosStatus==0)
        {
          SharedPreferences pres = 
            getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
          if(pres!=null)
          {
            /* 设定状态为启动(1) */
            mosStatus=1;
            SharedPreferences.Editor ed = pres.edit(); 
            ed.putInt("status",mosStatus); 
            ed.commit();
          }
          /*设定启动图案*/
          image01.setImageResource(R.drawable.mos_open);
          button01.setBackgroundResource(R.drawable.power_on);
          /* 启动service */
          startMyService();
        }
        else
        {
          Toast.makeText(EX10_08.this,"系统错误",Toast.LENGTH_LONG)
            .show();
        }  
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {    
    /* 叵丈离开的menu */
    menu.add(0,1,1,"").setIcon(R.drawable.menu_exit); 
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch(item.getItemId())
    {
      case (1):
        /* 离开前ALERT提醒 */
        new AlertDialog.Builder(EX10_08.this)
        .setTitle("Message")
        .setMessage("确定要离开吗？")
        .setPositiveButton("确定",
          new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialoginterface,int i)
            {           
              finish();
            }
          }
        ).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialoginterface, int i)   
          {
          }
        }).show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
  
  public void startMyService()
  {
    try
    {
      /* 先终止之前可能还在运行的service */
      stopMyService(0);
      /* 启动MyService */
      Intent intent = new Intent( EX10_08.this, MyService.class); 
      intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
      startService(intent);
      Toast.makeText(EX10_08.this,getResources().getString(R.string.start),
                     Toast.LENGTH_LONG).show();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void stopMyService(int flag)
  {
    try
    {
      /* 停止MyService */
      Intent intent = new Intent( EX10_08.this, MyService.class );
      stopService(intent);
      if(flag==1)
      {
        Toast.makeText(EX10_08.this,getResources().getString(R.string.stop),
                       Toast.LENGTH_LONG).show();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}

