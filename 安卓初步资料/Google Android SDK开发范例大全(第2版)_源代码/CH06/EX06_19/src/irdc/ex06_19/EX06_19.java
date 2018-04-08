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

/* 继承AppWidgetProvider */
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
   
  /* UpdateService系统服务后台运行 */ 
  public static class UpdateService extends Service 
  { 
    private mBroadcastReceiver mReceiver01; 
     
    @Override 
    public void onStart(Intent intent, int startId) 
    {  
      super.onStart(intent, startId);
      /* 注册Receiver */
      IntentFilter mFilter01; 
      mFilter01 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
      mReceiver01 = new mBroadcastReceiver(); 
      registerReceiver(mReceiver01, mFilter01);
      
      /* 建立RemoteViews对象，作为Widget的View */ 
      RemoteViews updateViews = keepUpdate(this); 
      
      /* 建立ComponentName对象与AppWidgetManager对象 */ 
      ComponentName thisWidget = 
      new ComponentName(this, EX06_19.class);
      
      AppWidgetManager manager = 
      AppWidgetManager.getInstance(this);
      
      /* 调用AppWidgetManager.updateAppWidget方法，
         将RemoteViews对象(Widget)显示于桌面 */
      manager.updateAppWidget(thisWidget, updateViews); 
    }
    
    public RemoteViews keepUpdate(Context context) 
    { 
      RemoteViews retRemoteViews = null; 
      retRemoteViews = new RemoteViews(context.getPackageName(),
                                       R.layout.update_widget); 
      /* 取得保存在SharedPreferences的电量值 */
      int power=0;
      SharedPreferences pres = 
      context.getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
      if(pres !=null)
      {
        power = pres.getInt("power", 0);
      }      
      /* 更新电量文字显示 */
      retRemoteViews.setTextViewText(R.id.myTextView1,power+"%");
      /* 更新电量图形显示 */
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
      /* 注销Receiver */
      unregisterReceiver(mReceiver01); 
      super.onDestroy(); 
    } 
    
    /* mBroadcastReceiver继承BroadcastReceiver */
    public class mBroadcastReceiver extends BroadcastReceiver 
    { 
      @Override 
      public void onReceive(Context context, Intent intent) 
      { 
        /* 当电池电量改变时，取得目前电量 */
        if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) 
        { 
          
          int intLevel = intent.getIntExtra("level", 0);  
          int intScale = intent.getIntExtra("scale", 100); 
          /* 将电量保存在SharedPreferences中 */
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

