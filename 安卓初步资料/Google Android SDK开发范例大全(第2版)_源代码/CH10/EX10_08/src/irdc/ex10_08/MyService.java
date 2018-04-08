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

/* �Զ���MyService�̳�Service */
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
      /* ȡ��NotificationManager */
      notiManager=
        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      /* Create MediaPlayer */
      myPlayer=new MediaPlayer();
      myPlayer = MediaPlayer.create(MyService.this, R.raw.killmosall);
      
      /* ��ȡ���÷���״̬(1:������0:�ر�) */
      SharedPreferences pres = 
        getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
      if(pres !=null)
      {
        mosStatus = pres.getInt("status", 0);
      }  
      
      if(mosStatus==1)
      {
        /* ��һ��Notification */
        setNoti(R.drawable.antimos,notiId,"���÷�������");     
        /* ���ŷ������� */
        if(!myPlayer.isPlaying())
        {
          myPlayer.seekTo(0);
          myPlayer.setLooping(true);
          myPlayer.start();
        }
      }
      else if(mosStatus==0)
      {
        /* ɾ��Notification */
        deleteNoti(notiId);
        /* �رշ������� */
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
      /* Service�ر�ʱ�ͷ�MediaPlayer��
       * ��ɾ��Notification */
      myPlayer.release();
      deleteNoti(notiId);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onDestroy();
  }

  
  /* ����Notification��method */
  public void setNoti(int iconImg,int iconId,String icontext)
  {
    /* ������ѡNotification������ʱ����ִ�е�Activity */
    Intent notifyIntent=new Intent(this,EX10_08.class);  
    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
    /* ����PendingIntent��Ϊ�趨����ִ�е�Activity */ 
    PendingIntent appIntent=PendingIntent.getActivity(this,0,notifyIntent,0); 
    /* ����Notification�����趨��ز��� */ 
    Notification myNoti=new Notification();
    /* �趨status bar��ʾ��icon */
    myNoti.icon=iconImg;
    /* �趨notification����ʱ��ʱ��߶Ԥ������ */
    myNoti.defaults=Notification.DEFAULT_SOUND;
    myNoti.setLatestEventInfo(this,"���÷�������",icontext,appIntent);
    /* �ͳ�Notification */
    notiManager.notify(iconId,myNoti);
  } 
  
  /* ɾ��Notification��method */
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

