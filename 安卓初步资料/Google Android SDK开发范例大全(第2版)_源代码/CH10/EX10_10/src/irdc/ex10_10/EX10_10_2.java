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

public class EX10_10_2 extends Activity
{
  private ListView ListView02;
  private ArrayList<String> arylistService;
  private ArrayList<String> arylistServicePackageName;
  private ArrayAdapter<String> aryAdapter1;
  private ActivityManager mActivityManager;
  int click_id;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_2);

    /* ȡ��EX10_10���ŵ�ArrayList */
    Bundle bunde = this.getIntent().getExtras();
    arylistService = bunde.getStringArrayList("arylistService");
    arylistServicePackageName = bunde
        .getStringArrayList("arylistServicePackageName");

    ListView02 = (ListView) findViewById(R.id.ListView02);
    /* ��ArrayList�ŵ�Adapter */
    aryAdapter1 = new ArrayAdapter<String>(EX10_10_2.this,
        R.layout.simple_list_item_1, arylistService);
    ListView02.setAdapter(aryAdapter1);

    ListView02.setOnItemClickListener(new ListView.OnItemClickListener()
    {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3)
      {
        // TODO Auto-generated method stub
        /* ȡ��ActivityManager */
        mActivityManager = (ActivityManager) EX10_10_2.this
            .getSystemService(ACTIVITY_SERVICE);
        /* �������index */
        click_id = id;

        AlertDialog.Builder builder = new AlertDialog.Builder(EX10_10_2.this);
        builder.setCancelable(false);
        builder.setTitle("Message");
        builder.setMessage("ȷ��Ҫɾ����??");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            // TODO Auto-generated method stub
            /* ֹͣ������ķ��� */
            mActivityManager.restartPackage(arylistServicePackageName.get(
                click_id).toString());
            /* �Ƴ�ArrayList���ֵ������ListView */
            arylistService.remove(click_id);
            ListView02.invalidateViews();

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

