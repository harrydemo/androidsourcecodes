package irdc.ex04_29;

import java.io.InputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class EX04_29 extends Activity
{
  private static final int MENU_SEARCH = 1;
  private Gallery Gallery01;
  private TextView TextView01;
  private ArrayList<String> fileNames;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Gallery01 = (Gallery) findViewById(R.id.Gallery01);
    TextView01 = (TextView) findViewById(R.id.TextView01);

    /*在?manifest要定义SEARCH的intent-filter */
    Intent queryIntent = getIntent();
    final String queryAction = queryIntent.getAction();
    /* 取得当按下搜索时的Intent */
    if (Intent.ACTION_SEARCH.equals(queryAction))
    {
      /* 取得欲搜索的字符串 */
      String query = queryIntent.getStringExtra(SearchManager.QUERY);
      queryPicture(query);
    }

    /* 按下键盘即弹出搜索框 */
    setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // TODO Auto-generated method stub
    menu.add(0, MENU_SEARCH, 0, "Search").setIcon(
        android.R.drawable.ic_search_category_default).setAlphabeticShortcut(
        SearchManager.MENU_KEY);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // TODO Auto-generated method stub
    switch (item.getItemId())
    {
      case MENU_SEARCH:
        /* 调用搜索框 */
        onSearchRequested();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /* 输入日期到SD Card去寻找最后修改时间的图 */
  private void queryPicture(String strQuery)
  {
    fileNames = new ArrayList<String>();
    /* 目录文件命、最后修改时间、文件名称 */
    String[] projection =
    { MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.DISPLAY_NAME };
    /* 搜寻条件 */
    String selection = "(" + projection[1] + ">=? and " + projection[1]
        + "<=? )";
    /* 条件里的参数 */
    String selectionArgs[] = getStartEnd(strQuery);
    /* 用输丈的日期叵勺日期的开始时间跟结束时间查询图仞 */
    Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection, selection, selectionArgs, null);

    while (cursor.moveToNext())
    {
      String filename = cursor.getString(0);
      fileNames.add(filename);
    }
    TextView01.setText(strQuery + "囝有" + fileNames.size() + "张图");
    Gallery01.setAdapter(new MyAdapter(this));
  }

  /* 用日期取得该日期的开始时间跟结束时间 */
  private String[] getStartEnd(String strQueryDate)
  {

    String[] result =
    { "", "" };
    String startDateTime = strQueryDate + "000000000";
    String endDateTime = strQueryDate + "235959000";
    try
    {
      java.util.Calendar calendar = java.util.Calendar.getInstance();

      calendar.set(java.util.Calendar.YEAR, new Integer(startDateTime
          .substring(0, 4)).intValue());
      calendar.set(java.util.Calendar.MONTH, new Integer(startDateTime
          .substring(4, 6)).intValue() - 1);
      calendar.set(java.util.Calendar.DATE, new Integer(startDateTime
          .substring(6, 8)).intValue());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, new Integer(startDateTime
          .substring(8, 10)).intValue());
      calendar.set(java.util.Calendar.MINUTE, new Integer(startDateTime
          .substring(10, 12)).intValue());
      calendar.set(java.util.Calendar.SECOND, new Integer(startDateTime
          .substring(12, 14)).intValue());
      /* 将后面3位去掉 */
      startDateTime = String.valueOf(calendar.getTimeInMillis()).substring(0,
          10);

      calendar.set(java.util.Calendar.YEAR, new Integer(endDateTime.substring(
          0, 4)).intValue());
      calendar.set(java.util.Calendar.MONTH, new Integer(endDateTime.substring(
          4, 6)).intValue() - 1);
      calendar.set(java.util.Calendar.DATE, new Integer(endDateTime.substring(
          6, 8)).intValue());
      calendar.set(java.util.Calendar.HOUR_OF_DAY, new Integer(endDateTime
          .substring(8, 10)).intValue());
      calendar.set(java.util.Calendar.MINUTE, new Integer(endDateTime
          .substring(10, 12)).intValue());
      calendar.set(java.util.Calendar.SECOND, new Integer(endDateTime
          .substring(12, 14)).intValue());
      /* 将后面3位去掉 */
      endDateTime = String.valueOf(calendar.getTimeInMillis()).substring(0, 10);
    } catch (Exception e)
    {
      startDateTime = "";
      endDateTime = "";
    } finally
    {
      result[0] = startDateTime;
      result[1] = endDateTime;
    }
    return result;

  }

  private class MyAdapter extends BaseAdapter
  {
    Context myContext;

    public MyAdapter(Context context)
    {
      myContext = context;
    }

    @Override
    public int getCount()
    {
      // TODO Auto-generated method stub
      return fileNames.size();
    }

    @Override
    public Object getItem(int arg0)
    {
      // TODO Auto-generated method stub
      return arg0;
    }

    @Override
    public long getItemId(int position)
    {
      // TODO Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      // TODO Auto-generated method stub

      ImageView imageView = new ImageView(this.myContext);
      InputStream is = null;
      try
      {
        is = new java.io.FileInputStream(fileNames.get(position));
        /* 将InputStream变成Bitmap */
        Bitmap bm = BitmapFactory.decodeStream(is);
        /* 关闭InputStream */
        is.close();
        /* 设置Bitmap于ImageView中 */
        imageView.setImageBitmap(bm);
      } catch (Exception e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return imageView;
    }

  }
}

