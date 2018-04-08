package irdc.ex10_10;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EX10_10_1 extends Activity
{
  private ListView ListView01;
  private ArrayList<String> arylistTask;
  private ArrayList<String> arylistTaskPackageName;
  private ArrayAdapter<String> aryAdapter1;
  private ActivityManager mActivityManager;
  int click_id;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_1);

    /* 取得EX10_10所放的ArrayList */
    Bundle bunde = this.getIntent().getExtras();
    arylistTask = bunde.getStringArrayList("arylistTask");
    arylistTaskPackageName = bunde.getStringArrayList("arylistTaskPackageName");

    ListView01 = (ListView) findViewById(R.id.ListView01);
    /* 将ArrayList放到Adapter */
    aryAdapter1 = new ArrayAdapter<String>(EX10_10_1.this,
        R.layout.simple_list_item_1, arylistTask);
    ListView01.setAdapter(aryAdapter1);

    ListView01.setOnItemClickListener(new ListView.OnItemClickListener()
    {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3)
      {
        // TODO Auto-generated method stub
        /* 取得ActivityManager */
        mActivityManager = (ActivityManager) EX10_10_1.this
            .getSystemService(ACTIVITY_SERVICE);
        /* 所点选的index */
        click_id = id;

        AlertDialog.Builder builder = new AlertDialog.Builder(EX10_10_1.this);
        builder.setCancelable(false);
        builder.setTitle("Message");
        builder.setMessage("确定要删除吗??");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            // TODO Auto-generated method stub
            /* 停止所点击的应用程序 */
            mActivityManager.restartPackage(arylistTaskPackageName
                .get(click_id).toString());
            /* 移除ArrayList里的值并更新ListView */
            arylistTask.remove(click_id);
            ListView01.invalidateViews();

          }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {

          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            // TODO Auto-generated method stub

          }
        });
        builder.show();

      }
    });

  }
}

