package irdc.ex05_24;

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
  /* �������� */
  private LayoutInflater mInflater;
  private List<String> items;
  private List<String> values;
  /* MyAdapter�Ľ����ӣ�������������  */  
  public MyAdapter(Context context,List<String> item,List<String> value)
  {
    /* ������ʼ�� */
    mInflater = LayoutInflater.from(context);
    items = item;
    values = value;
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
  public View getView(int position,View convertView,ViewGroup parent)
  {
    ViewHolder holder;
  
    if(convertView == null)
    {
      /* ʹ���Զ����file_row��ΪLayout */
      convertView = mInflater.inflate(R.layout.row_layout,null);
      /* ��ʼ��holder��text��icon */
      holder = new ViewHolder();
      holder.text1=(TextView)convertView.findViewById(R.id.myText1);
      holder.text2=(TextView)convertView.findViewById(R.id.myText2);
    
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }
    /* �趨Ҫ��ʾ����Ϣ */
    holder.text1.setText(items.get(position).toString());
    holder.text2.setText(values.get(position).toString());
  
    return convertView;
  }
  
  /* class ViewHolder */
  private class ViewHolder
  {
    /* text1����Ϣ����
     * text2����Ϣ���� */
    TextView text1;
    TextView text2;
  }
}

