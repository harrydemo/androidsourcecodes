package irdc.ex04_30;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class EX04_30 extends AppWidgetProvider
{
  final String addButton_actionName = "button01";
  final String removeButton_actionName = "button02";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds)
  {
    // TODO Auto-generated method stub
    Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

    // 主要的RemoteViews
    RemoteViews mainViews = new RemoteViews(context.getPackageName(),
        R.layout.main);

    // 新增按鈕的Intent
    Intent addIntent = new Intent(context, EX04_30.class);
    addIntent.setAction(addButton_actionName);
    PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0,
        addIntent, 0);
    mainViews.setOnClickPendingIntent(R.id.Button01, addPendingIntent);

    // 移除按鈕的Intent
    Intent removeIntent = new Intent(context, EX04_30.class);
    removeIntent.setAction(removeButton_actionName);
    PendingIntent removePendingIntent = PendingIntent.getBroadcast(context, 0,
        removeIntent, 0);
    mainViews.setOnClickPendingIntent(R.id.Button02, removePendingIntent);

    /* 更新widget */
    appWidgetManager.updateAppWidget(appWidgetIds, mainViews);
  }

  @Override
  public void onReceive(Context context, Intent intent)
  {
    // TODO Auto-generated method stub
    Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

    if (intent.getAction().equals(addButton_actionName))
    {
      // 主要的RemoteViews
      RemoteViews mainViews = new RemoteViews(context.getPackageName(),
          R.layout.main);
      // 清空LinearLayout02裡所有的View
      mainViews.removeAllViews(R.id.LinearLayout02);

      RemoteViews subViews = new RemoteViews(context.getPackageName(),
          R.layout.sub);
      // 將subViews加到主要的RemoteViews
      mainViews.addView(R.id.LinearLayout02, subViews);

      /* 更新widget */
      ComponentName thisWidget = new ComponentName(context, EX04_30.class);
      AppWidgetManager manager = AppWidgetManager.getInstance(context);
      manager.updateAppWidget(thisWidget, mainViews);
    } else if (intent.getAction().equals(removeButton_actionName))
    {
      // 主要的RemoteViews
      RemoteViews mainViews = new RemoteViews(context.getPackageName(),
          R.layout.main);
      // 清空LinearLayout02裡所有的View
      mainViews.removeAllViews(R.id.LinearLayout02);

      /* 更新widget */
      ComponentName thisWidget = new ComponentName(context, EX04_30.class);
      AppWidgetManager manager = AppWidgetManager.getInstance(context);
      manager.updateAppWidget(thisWidget, mainViews);
    }

    super.onReceive(context, intent);
  }

}
