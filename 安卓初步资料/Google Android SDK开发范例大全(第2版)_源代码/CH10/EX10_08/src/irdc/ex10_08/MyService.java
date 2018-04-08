package irdc.ex10_08;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

/* 自定义MyService继承Service */
public class MyService extends Service
{
  private String MY_PREFS = "MosPre";
  private NotificationManager notiManager;
  private int mosStatus;
  private int notiId=99;
  private MediaPlayer myPlayer;
    
  @Override
  public void onCreate()
  {
    try
    {
      /* 取得NotificationManager */
      notiManager=
        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      /* Create MediaPlayer */
      myPlayer=new MediaPlayer();
      myPlayer = MediaPlayer.create(MyService.this, R.raw.killmosall);
      
      /* 读取防蚊服务状态(1:启动，0:关闭) */
      SharedPreferences pres = 
        getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
      if(pres !=null)
      {
        mosStatus = pres.getInt("status", 0);
      }  
      
      if(mosStatus==1)
      {
        /* 加一个Notification */
        setNoti(R.drawable.antimos,notiId,"防蚊服务启动");     
        /* 播放防蚊铃声 */
        if(!myPlayer.isPlaying())
        {
          myPlayer.seekTo(0);
          myPlayer.setLooping(true);
          myPlayer.start();
        }
      }
      else if(mosStatus==0)
      {
        /* 删除Notification */
        deleteNoti(notiId);
        /* 关闭防蚊铃声 */
        if(myPlayer.isPlaying())
        {
          myPlayer.setLooping(false);
          myPlayer.pause();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onCreate();
  }
  
  @Override
  public void onDestroy()
  {
    try
    {
      /* Service关闭时释放MediaPlayer，
       * 并删除Notification */
      myPlayer.release();
      deleteNoti(notiId);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onDestroy();
  }

  
  /* 新增Notification的method */
  public void setNoti(int iconImg,int iconId,String icontext)
  {
    /* 建立点选Notification留言条时，会执行的Activity */
    Intent notifyIntent=new Intent(this,EX10_08.class);  
    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
    /* 建立PendingIntent当为设定递延执行的Activity */ 
    PendingIntent appIntent=PendingIntent.getActivity(this,0,notifyIntent,0); 
    /* 建立Notification，并设定相关参数 */ 
    Notification myNoti=new Notification();
    /* 设定status bar显示的icon */
    myNoti.icon=iconImg;
    /* 设定notification发生时她时发叨预设声音 */
    myNoti.defaults=Notification.DEFAULT_SOUND;
    myNoti.setLatestEventInfo(this,"防蚊服务启动",icontext,appIntent);
    /* 送出Notification */
    notiManager.notify(iconId,myNoti);
  } 
  
  /* 删除Notification的method */
  public void deleteNoti(int iconId)
  {
    notiManager.cancel(iconId);
  }
  
  @Override
  public IBinder onBind(Intent arg0)
  {
    return null;
  }
}

