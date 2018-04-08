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
  { "", "", "�칫��", "����", "����", "����", "ѧУ", "����", "����", "����", "", "" };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ListView01 = (ListView) findViewById(R.id.ListView01);
    TextView01 = (TextView) findViewById(R.id.TextView01);

    /* ���ַ������ݷ���ArrayAdapter */
    ArrayAdapter<String> list1 = new ArrayAdapter<String>(this,
        R.layout.file_row, s1);
    /* �趨ListView��Adapter */
    ListView01.setAdapter(list1);
    /* ��ʱ͸���� */
    ListView01.setCacheColorHint(00000000);
    ListView01.setFastScrollEnabled(true);
    /* ����Ե */
    ListView01.setFadingEdgeLength(100);

    ListView01.setOnScrollListener(new ListView.OnScrollListener()
    {
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem,
          int visibleItemCount, int totalItemCount)
      {
        // TODO Auto-generated method stub
        /* ȡ�õڼ�����ʾ���������ֵ */
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
        /* �趨��ѡ��������Ϊ��ʼ */
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

