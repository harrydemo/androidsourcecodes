package irdc.ex08_13;

/* import���class */
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* �Զ����Adapter���̳�android.widget.BaseAdapter */
public class MyAdapter extends BaseAdapter
{
  /* ��������  */
  private LayoutInflater mInflater;
  private List<News> items;

  /* MyAdapter�Ľ����ӣ�������������  */  
  public MyAdapter(Context context,List<News> it)
  {
    /* ������ʼ�� */
    mInflater = LayoutInflater.from(context);
    items = it;
  }
  
  /* ��̳�BaseAdapter������д����method */
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
      /* ʹ���Զ����news_row��ΪLayout */
      convertView = mInflater.inflate(R.layout.news_row, null);
      /* ��ʼ��holder��text��icon */
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

