package irdc.ex05_21;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EX05_21 extends Activity
{
  private Button mButton01;
  private ListView mListView01;
  private ArrayAdapter<String> aryAdapter1;
  private ArrayList<String> arylistTask;
  
  /* 类成员设定取并最?几笔的Task数量 */
  private int intGetTastCounter=30;
  
  /* 类成员ActivityManager对象 */
  private ActivityManager mActivityManager;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mListView01 = (ListView)findViewById(R.id.myListView1);
    
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        try
        {
          /* ActivityManager对象守系统取得ACTIVITY_SERVICE */
          mActivityManager = (ActivityManager)EX05_21.this.getSystemService(ACTIVITY_SERVICE);
          
          arylistTask = new ArrayList<String>();
          
          /* 以getRunningTasks方法取并正在运行中的程序TaskInfo */
          List<ActivityManager.RunningTaskInfo> mRunningTasks = mActivityManager.getRunningTasks(intGetTastCounter);
          //List<ActivityManager.RunningServiceInfo> mRunningTasks = mActivityManager.getRunningServices(intGetTastCounter);
          
          int i = 1;
          /* 以循环及baseActivity方式取得名称与ID */ 
          for (ActivityManager.RunningTaskInfo amTask : mRunningTasks)
          //for (ActivityManager.RunningServiceInfo amTask : mRunningTasks)
          {
            /* baseActivity.getClassName */
            arylistTask.add("" + (i++) + ": "+ amTask.baseActivity.getClassName() + "(ID=" + amTask.id +")"); 
            //arylistTask.add("" + (i++) + ": "+ amTask.process + "(ID=" + amTask.pid +")");
          }
          aryAdapter1 = new ArrayAdapter<String>(EX05_21.this, R.layout.simple_list_item_1, arylistTask);
          if(aryAdapter1.getCount()==0)
          {
            /* 当没有任何运行的操作，则提示信息 */
            mMakeTextToast
            (
              getResources().getText(R.string.str_err_no_running_task).toString(),
              //getResources().getText(R.string.str_err_no_running_service).toString(),
              true
            );
          }
          else
          {
            /* 发现后台运行程序，以ListView Widget条在出现 */
            mListView01.setAdapter(aryAdapter1);
          }
        }
        catch(SecurityException e)
        {
          /* 当无GET_TASKS权限时(SecurityException例?)提示信息 */
          mMakeTextToast
          (
            getResources().getText(R.string.str_err_permission).toString(),
            true
          );
        }
      }
    });
    
    /* 当User运行选择时的事件处理 */
    mListView01.setOnItemSelectedListener(new ListView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View v, int id, long arg3)
      {
        // TODO Auto-generated method stub
        /* 由于将运行，故以id取出数组元素名称 */
        mMakeTextToast(arylistTask.get(id).toString(),false);
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
        // TODO Auto-generated method stub
        
      }
    });
    
    /* 当User运行点击时的事件处理 */
    mListView01.setOnItemClickListener(new ListView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View v, int id,  long arg3)
      {
        // TODO Auto-generated method stub
        /* 由于将运行?囫北阵在?放，故北id取叨阵在元素宅称 */
        mMakeTextToast(arylistTask.get(id).toString(), false);
      }
    });
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX05_21.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX05_21.this, str, Toast.LENGTH_SHORT).show();
    }
  }
}

