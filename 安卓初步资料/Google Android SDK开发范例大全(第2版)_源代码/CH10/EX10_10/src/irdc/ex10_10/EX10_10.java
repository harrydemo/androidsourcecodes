package irdc.ex10_10;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX10_10 extends Activity
{
  private Button Button01;
  private Button Button02;
  private ActivityManager mActivityManager;
  private ArrayList<String> arylistTask;
  private ArrayList<String> arylistTaskPackageName;
  private ArrayList<String> arylistService;
  private ArrayList<String> arylistServicePackageName;
  private int intGetTaskCounter = 30;
  public List<ActivityManager.RunningTaskInfo> mRunningTasks;
  public List<RunningServiceInfo> mRunningTaskServices;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button01 = (Button) findViewById(R.id.Button01);
    Button02 = (Button) findViewById(R.id.Button02);
    /* 取得ActivityManager */
    mActivityManager = (ActivityManager) EX10_10.this
        .getSystemService(ACTIVITY_SERVICE);

    Button01.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        arylistTask = new ArrayList<String>();
        arylistTaskPackageName = new ArrayList<String>();
        /* 用ActivityManager取得正在运行的硬用程序 */
        mRunningTasks = mActivityManager.getRunningTasks(intGetTaskCounter);

        int i = 1;
        /* 以循环及baseActivity方式取得名称与ID并放入ArrayList */
        for (ActivityManager.RunningTaskInfo amTask : mRunningTasks)
        {
          arylistTask.add("" + (i++) + ": "
              + amTask.baseActivity.getClassName() + "(ID=" + amTask.id + ")");
          arylistTaskPackageName.add(amTask.baseActivity.getPackageName());
        }
        /* 将ArrayList放到Bundle并将Activity开启 */
        Intent intent = new Intent();
        intent.setClass(EX10_10.this, EX10_10_1.class);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("arylistTask", arylistTask);
        bundle.putStringArrayList("arylistTaskPackageName",
            arylistTaskPackageName);

        intent.putExtras(bundle);
        startActivity(intent);
      }
    });
    Button02.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        arylistService = new ArrayList<String>();
        arylistServicePackageName = new ArrayList<String>();
        mRunningTaskServices = mActivityManager
            .getRunningServices(intGetTaskCounter);

        int i = 1;
        /* 北循环及RunningServiceInfo对象取得服务名称与ID并放入ArrayList */
        for (RunningServiceInfo amTask : mRunningTaskServices)
        {
          arylistService.add("" + (i++) + ": " + amTask.process + "(ID="
              + amTask.pid + ")");
          arylistServicePackageName.add(amTask.service.getPackageName());
        }
        /* 将ArrayList放到Bundle并将Activity开启 */
        Intent intent = new Intent();
        intent.setClass(EX10_10.this, EX10_10_2.class);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("arylistService", arylistService);
        bundle.putStringArrayList("arylistServicePackageName",
            arylistServicePackageName);

        intent.putExtras(bundle);
        startActivity(intent);
      }
    });
  }
}

