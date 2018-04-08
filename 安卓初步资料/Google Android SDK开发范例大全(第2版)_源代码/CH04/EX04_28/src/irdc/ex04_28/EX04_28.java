package irdc.ex04_28;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class EX04_28 extends AppWidgetProvider
{

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds)
  {
    // TODO Auto-generated method stub

    /* 新起UpdateService的Intent */
    Intent intent = new Intent(context, UpdateService.class);
    context.startService(intent);

    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

  public static class UpdateService extends Service
  {

    @Override
    public IBinder onBind(Intent arg0)
    {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
      super.onStart(intent, startId);
      /* 取得Widget的View */
      RemoteViews updateViews = new RemoteViews(this.getPackageName(),
          R.layout.main);
      /* 格式化时间hh:mm占表时跟分 */
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
      /* 将时间放置TextView */
      updateViews.setTextViewText(R.id.TextView01, "" + sdf.format(new Date()));

      /* 更新widget */
      ComponentName thisWidget = new ComponentName(this, EX04_28.class);
      AppWidgetManager manager = AppWidgetManager.getInstance(this);
      manager.updateAppWidget(thisWidget, updateViews);

    }
  }

}

