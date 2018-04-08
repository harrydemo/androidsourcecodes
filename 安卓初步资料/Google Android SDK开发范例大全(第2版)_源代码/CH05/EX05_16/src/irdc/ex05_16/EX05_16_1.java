package irdc.ex05_16;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EX05_16_1 extends ListActivity
{
  private List<String> items = null;
  private String path;

  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mylist);

    Bundle bunde = this.getIntent().getExtras();
    /* 取得EX05_16所传的路径 */
    path = bunde.getString("path");

    this.setTitle(path);

    java.io.File file = new java.io.File(path);
    /* 列出该路径下的所有文件 */
    fill(file.listFiles());
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id)
  {
    File file = new File(path + java.io.File.separator + items.get(position));
    if (file.isDirectory())
      fill(file.listFiles());

  }

  private void fill(File[] files)
  {
    items = new ArrayList<String>();
    if (files == null)
      return;

    /* 取得文件名放入ArrayList */
    for (File file : files)
      items.add(file.getName());
    /* 将ArrayList放入ArrayAdapter */
    ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, items);
    setListAdapter(fileList);
  }
}

