package irdc.ex06_10;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.util.Log; //调试包
import android.view.Gravity;

public class EX06_10 extends Activity
{
  

  Button mButton1; //timeset buttom
  Button mButton2; //enable buttom
  int state = 0; // 0:disable  1:enable
  Calendar c=Calendar.getInstance();
  
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    
    /* 引用timeset.xml为Layout */
    LayoutInflater factory = LayoutInflater.from(this);
    final View setView = factory.inflate(R.layout.timeset,null);
    final TimePicker tPicker=(TimePicker)setView
                               .findViewById(R.id.tPicker);
    tPicker.setIs24HourView(true);
    
    
    
    /* 设置Dialog */   
    final AlertDialog di=new AlertDialog.Builder(EX06_10.this)
          .setIcon(R.drawable.clock)
          .setTitle("设置")
          .setView(setView)
          .setPositiveButton("确定",
            new DialogInterface.OnClickListener()
           {
             public void onClick(DialogInterface dialog, int which)
             {
               /* 取得设置的开始时间，秒及毫秒设为0 */
               c.setTimeInMillis(System.currentTimeMillis());
               c.set(Calendar.HOUR_OF_DAY,tPicker.getCurrentHour());
               c.set(Calendar.MINUTE,tPicker.getCurrentMinute());
               c.set(Calendar.SECOND,0);
               c.set(Calendar.MILLISECOND,0);

               
               /* 更新显示的设置闹钟时间 */    
               String tmpS=format(tPicker.getCurrentHour())+"："+
                           format(tPicker.getCurrentMinute());
               mButton1.setText(""+tmpS);
               /* 以Toast提示设置已完成 */
               Toast toast = Toast.makeText(EX06_10.this,"设置闹钟时间为"+tmpS,
                                Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.TOP, 0, 40);
               toast.show();
               Log.e("canlendar", c.toString());//log
               if (state == 1){
                   /* 指定闹钟设置时间到时要运行CallAlarm.class */
                    Intent intent = new Intent(EX06_10.this, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(
                                        EX06_10.this,1, intent, 0);
                    /* set 闹钟 */
                    AlarmManager am;
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            c.getTimeInMillis(),
                            sender
                          );
               }
             }
           })
          .setNegativeButton("取消",
            new DialogInterface.OnClickListener()
           {
             public void onClick(DialogInterface dialog, int which)
             {
             }
           }).create();

    /* timeset Button */
    mButton1=(Button) findViewById(R.id.mButton1);
    mButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        /* 取得点击按钮时的时间作为tPicker的默认值 */
        c.setTimeInMillis(System.currentTimeMillis());
        tPicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        tPicker.setCurrentMinute(c.get(Calendar.MINUTE));
        
        di.show();
      }
    });

    /* enable Button */
    mButton2=(Button) findViewById(R.id.mButton2);
    
    mButton2.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
        
            if (state == 0){
                if (!"Set Time".equals(mButton1.getText().toString())){
                    /* 指定闹钟设置时间到时要运行CallAlarm.class */
                    Intent intent = new Intent(EX06_10.this, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(
                                             EX06_10.this,1, intent, 0);
                    /* set 闹钟 */
                    AlarmManager am;
                    am = (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,
                            c.getTimeInMillis(),
                            sender
                          );
                }    
                state = 1;
                /* 以Toast提示设置已完成 */
                Toast toast = Toast.makeText(EX06_10.this,"闹钟启用",
                                Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();
                /* 设置图标 */
                mButton2.setBackgroundResource(R.drawable.enable);
                Log.e("canlendar", c.toString());//log
            }
            else if (state == 1){
                Intent intent = new Intent(EX06_10.this, CallAlarm.class);
                PendingIntent sender = PendingIntent.getBroadcast(
                                     EX06_10.this,1, intent, 0);
                /* 由AlarmManager中删除 */
                AlarmManager am;
                am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.cancel(sender);
                
                Toast toast = Toast.makeText(EX06_10.this,"闹钟禁用",
                                Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 40);
                toast.show();
                state = 0;
                /* 设置图标 */
                mButton2.setBackgroundResource(R.drawable.disable);
                Log.e("canlendar", c.toString());//log
            }
      }
    });
  }

  /* 日期时间显示两位数的方法 */
  private String format(int x)
  {
    String s=""+x;
    if(s.length()==1) s="0"+s;
    return s;
  }
}
 
