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

    /* ����UpdateService��Intent */
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
      /* ȡ��Widget��View */
      RemoteViews updateViews = new RemoteViews(this.getPackageName(),
          R.layout.main);
      /* ��ʽ��ʱ��hh:mmռ��ʱ���� */
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
      /* ��ʱ�����TextView */
      updateViews.setTextViewText(R.id.TextView01, "" + sdf.format(new Date()));

      /* ����widget */
      ComponentName thisWidget = new ComponentName(this, EX04_28.class);
      AppWidgetManager manager = AppWidgetManager.getInstance(this);
      manager.updateAppWidget(thisWidget, updateViews);

    }
  }

}

