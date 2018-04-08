package irdc.ex04_27_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* 告定义Adapter，继承BaseAdapter */
public class MyGridViewAdapter extends BaseAdapter 
{ 
  private Context _con;
  private String[] _items;
  private int[] _icons;
  /* 构造器 */
  public MyGridViewAdapter(Context con,String[] items,int[] icons) 
  {
    _con=con;
    _items=items;
    _icons=icons;
  }

  @Override
  public int getCount()
  {
    return _items.length;
  }

  @Override
  public Object getItem(int arg0)
  {
    return _items[arg0];
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LayoutInflater factory = LayoutInflater.from(_con);
    /* 使用grid.xml为每几个item的Layout */
    View v = (View) factory.inflate(R.layout.grid, null);
    /* 取得View */
    ImageView iv = (ImageView) v.findViewById(R.id.icon);
    TextView tv = (TextView) v.findViewById(R.id.text);
    /* 设置显示的Image与文? */
    iv.setImageResource(_icons[position]);
    tv.setText(_items[position]);
    return v;
  } 
} 

