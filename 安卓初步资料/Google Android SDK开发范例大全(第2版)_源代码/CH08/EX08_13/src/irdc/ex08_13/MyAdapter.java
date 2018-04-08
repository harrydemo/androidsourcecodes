package irdc.ex08_13;

/* import相关class */
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class MyAdapter extends BaseAdapter
{
  /* 变量声明  */
  private LayoutInflater mInflater;
  private List<News> items;

  /* MyAdapter的建构子，传入两个参数  */  
  public MyAdapter(Context context,List<News> it)
  {
    /* 参数初始化 */
    mInflater = LayoutInflater.from(context);
    items = it;
  }
  
  /* 因继承BaseAdapter，需重写以下method */
  @Override
  public int getCount()
  {
    return items.size();
  }

  @Override
  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  @Override
  public View getView(int position,View convertView,ViewGroup par)
  {
    ViewHolder holder;
    
    if(convertView == null)
    {
      /* 使用自定义的news_row作为Layout */
      convertView = mInflater.inflate(R.layout.news_row, null);
      /* 初始化holder的text与icon */
      holder = new ViewHolder();
      holder.text = (TextView) convertView.findViewById(R.id.text);      
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }
    News tmpN=(News)items.get(position);
    holder.text.setText(tmpN.getTitle());
    
    return convertView;
  }
  
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
  }
}

