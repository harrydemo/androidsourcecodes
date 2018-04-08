package irdc.ex06_19; 

import android.app.Service; 
import android.appwidget.AppWidgetManager; 
import android.appwidget.AppWidgetProvider; 
import android.content.BroadcastReceiver; 
import android.content.ComponentName; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.IBinder; 
import android.widget.RemoteViews; 

/* �̳�AppWidgetProvider */
public class EX06_19 extends AppWidgetProvider 
{ 
  /* SharedPreferences Tag */
  public static final String MY_PREFS="mSharedPreferences01"; 

  @Override 
  public void onUpdate 
  (Context context,
   AppWidgetManager appWidgetManager,int[] appWidgetIds)
  {
    super.onUpdate(context, appWidgetManager, appWidgetIds); 
    context.startService(new Intent(context, UpdateService.class));
  }
   
  /* UpdateServiceϵͳ�����̨���� */ 
  public static class UpdateService extends Service 
  { 
    private mBroadcastReceiver mReceiver01; 
     
    @Override 
    public void onStart(Intent intent, int startId) 
    {  
      super.onStart(intent, startId);
      /* ע��Receiver */
      IntentFilter mFilter01; 
      mFilter01 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
      mReceiver01 = new mBroadcastReceiver(); 
      registerReceiver(mReceiver01, mFilter01);
      
      /* ����RemoteViews������ΪWidget��View */ 
      RemoteViews updateViews = keepUpdate(this); 
      
      /* ����ComponentName������AppWidgetManager���� */ 
      ComponentName thisWidget = 
      new ComponentName(this, EX06_19.class);
      
      AppWidgetManager manager = 
      AppWidgetManager.getInstance(this);
      
      /* ����AppWidgetManager.updateAppWidget������
         ��RemoteViews����(Widget)��ʾ������ */
      manager.updateAppWidget(thisWidget, updateViews); 
    }
    
    public RemoteViews keepUpdate(Context context) 
    { 
      RemoteViews retRemoteViews = null; 
      retRemoteViews = new RemoteViews(context.getPackageName(),
                                       R.layout.update_widget); 
      /* ȡ�ñ�����SharedPreferences�ĵ���ֵ */
      int power=0;
      SharedPreferences pres = 
      context.getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
      if(pres !=null)
      {
        power = pres.getInt("power", 0);
      }      
      /* ���µ���������ʾ */
      retRemoteViews.setTextViewText(R.id.myTextView1,power+"%");
      /* ���µ���ͼ����ʾ */
      if(power!=0)
      {
        Bitmap bmp=BitmapFactory.decodeResource
        (getResources(),R.drawable.power);
        
        float x=57.0f/100*power;
        Matrix matrix = new Matrix();
        matrix.postScale(x,1.0f); 
        Bitmap resizeBmp = Bitmap.createBitmap
        (bmp,0,0,1,39,matrix,true);
        
        retRemoteViews.setBitmap
        (
          R.id.myImageView1,"setImageBitmap",resizeBmp
        );
      }
      return retRemoteViews; 
    } 
     
    @Override 
    public IBinder onBind(Intent intent) 
    { 
      return null; 
    } 
     
    @Override 
    public void onDestroy() 
    {
      /* ע��Receiver */
      unregisterReceiver(mReceiver01); 
      super.onDestroy(); 
    } 
    
    /* mBroadcastReceiver�̳�BroadcastReceiver */
    public class mBroadcastReceiver extends BroadcastReceiver 
    { 
      @Override 
      public void onReceive(Context context, Intent intent) 
      { 
        /* ����ص����ı�ʱ��ȡ��Ŀǰ���� */
        if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) 
        { 
          
          int intLevel = intent.getIntExtra("level", 0);  
          int intScale = intent.getIntExtra("scale", 100); 
          /* ������������SharedPreferences�� */
          SharedPreferences pres = 
          context.getSharedPreferences
          (
            MY_PREFS,Context.MODE_PRIVATE
          );
          if(pres!=null)
          {
            SharedPreferences.Editor ed = pres.edit(); 
            ed.putInt("power",(intLevel*100/intScale)); 
            ed.commit();
          }
        }
      } 
    }
  }
} 

