package irdc.ex04_27_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* �涨��Adapter���̳�BaseAdapter */
public class MyGridViewAdapter extends BaseAdapter 
{ 
  private Context _con;
  private String[] _items;
  private int[] _icons;
  /* ������ */
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
    /* ʹ��grid.xmlΪÿ����item��Layout */
    View v = (View) factory.inflate(R.layout.grid, null);
    /* ȡ��View */
    ImageView iv = (ImageView) v.findViewById(R.id.icon);
    TextView tv = (TextView) v.findViewById(R.id.text);
    /* ������ʾ��Image����? */
    iv.setImageResource(_icons[position]);
    tv.setText(_items[position]);
    return v;
  } 
} 

