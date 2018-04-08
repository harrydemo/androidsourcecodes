package irdc.ex05_08;

/* import相关class */
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EX05_08 extends Activity
{
  /*声明对象变量*/
  private NotificationManager myNotiManager;
  private Spinner mySpinner;
  private ArrayAdapter<String> myAdapter;
  private static final String[] status =
  { "在线","离开","忙碌中","马上回来","脱机" };
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 载入main.xml Layout */
    setContentView(R.layout.main);
    
    /* 初始化对象 */
    myNotiManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    mySpinner=(Spinner)findViewById(R.id.mySpinner);
    myAdapter=new ArrayAdapter<String>(this,
                      android.R.layout.simple_spinner_item,status);
    /* 套用myspinner_dropdown自定义下拉菜单样式 */
    myAdapter.setDropDownViewResource(R.layout.myspinner_dropdown);
    /* 将ArrayAdapter加入Spinner对象中 */
    mySpinner.setAdapter(myAdapter);

    /* 将mySpinner加入OnItemSelectedListener */
    mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> arg0,View arg1,
                                 int arg2,long arg3)
      {
        /* 依照选择的item来判断要发哪一个notification */
        if(status[arg2].equals("在线"))
        {
          setNotiType(R.drawable.msn,"在线");
        }
        else if(status[arg2].equals("离开"))
        {
          setNotiType(R.drawable.away,"离开");
        }
        else if(status[arg2].equals("忙碌中"))
        {
          setNotiType(R.drawable.busy,"忙碌中");
        }
        else if(status[arg2].equals("马上回来"))
        {
          setNotiType(R.drawable.min,"马上回来");
        }
        else
        {
          setNotiType(R.drawable.offine,"脱机");
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
      }
    });
  }
  
  /* 发出Notification的method */
  private void setNotiType(int iconId, String text)
  {
    /* 建立新的Intent，作为点选Notification留言条时，
     * 会执行的Activity */ 
    Intent notifyIntent=new Intent(this,EX05_08_1.class);  
    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
    /* 建立PendingIntent作为设定递延执行的Activity */ 
    PendingIntent appIntent=PendingIntent.getActivity(EX05_08.this,0,
                                                      notifyIntent,0);
       
    /* 建立Notication，并设定相关参数 */ 
    Notification myNoti=new Notification();
    /* 设定statusbar显示的icon */
    myNoti.icon=iconId;
    /* 设定statusbar显示的文字讯息 */
    myNoti.tickerText=text;
    /* 设定notification发生时同时发出预设声音 */
    myNoti.defaults=Notification.DEFAULT_SOUND;
    /* 设定Notification留言条的参数 */
    myNoti.setLatestEventInfo(EX05_08.this,"MSN登入状态",text,appIntent);
    /* 送出Notification */
    myNotiManager.notify(0,myNoti);
  } 
}

