package irdc.ex10_09;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class EX10_09 extends Activity
{

  private ListView ListView01;
  private TextView TextView01;
  String[] s1 =
  { "", "", "办公室", "厕所", "客厅", "厨房", "学校", "家里", "车上", "房间", "", "" };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ListView01 = (ListView) findViewById(R.id.ListView01);
    TextView01 = (TextView) findViewById(R.id.TextView01);

    /* 将字符串数据放至ArrayAdapter */
    ArrayAdapter<String> list1 = new ArrayAdapter<String>(this,
        R.layout.file_row, s1);
    /* 设定ListView的Adapter */
    ListView01.setAdapter(list1);
    /* 卷动时透明化 */
    ListView01.setCacheColorHint(00000000);
    ListView01.setFastScrollEnabled(true);
    /* 雾化边缘 */
    ListView01.setFadingEdgeLength(100);

    ListView01.setOnScrollListener(new ListView.OnScrollListener()
    {
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem,
          int visibleItemCount, int totalItemCount)
      {
        // TODO Auto-generated method stub
        /* 取得第几个显示的下两格的值 */
        TextView01.setText(s1[firstVisibleItem + 2]);
      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
        // TODO Auto-generated method stub
      }
    });

    ListView01.setOnItemClickListener(new ListView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View v, int id, long arg3)
      {
        // TODO Auto-generated method stub
        /* 设定点选的下两格为开始 */
        ListView01.setSelectionFromTop(id - 2, 0);
        TextView01.setText(s1[id]);
      }
    });

    ListView01.setOnItemSelectedListener(new ListView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> parent, View v, int id,
          long arg3)
      {
        // TODO Auto-generated method stub
        TextView01.setText(s1[id]);
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
        // TODO Auto-generated method stub
      }
    });

  }
}

